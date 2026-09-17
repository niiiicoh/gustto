package com.example.gustto.repository;

import android.content.Context;
import android.os.*;
import com.example.gustto.data.local.GusttoSQLiteHelper;
import com.example.gustto.data.remote.*;
import com.example.gustto.model.*;
import com.example.gustto.util.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GusttoRepository {
  public final AuthRepository auth = new AuthRepository();
  public final RestaurantFilter filter = new RestaurantFilter();
  public UserProfile profile;
  public Double latitude, longitude;
  public String notice = "";
  public final List<Restaurant> restaurants = new ArrayList<>();
  public final Set<String> favoriteIds = new HashSet<>();
  public final List<String> recentIds = new ArrayList<>(), searches = new ArrayList<>();
  public final ProfanityFilter profanity;
  private final GusttoSQLiteHelper db;
  private final ExecutorService disk = Executors.newSingleThreadExecutor();
  private final Handler main = new Handler(Looper.getMainLooper());
  private final SqlConnectBridge bridge = new SqlConnectBridge();
  private final Gson gson = new Gson();
  private int sessionVersion = 0;
  private final Set<String> pendingFavorites = new HashSet<>();

  public GusttoRepository(Context c) {
    db = new GusttoSQLiteHelper(c);
    List<String> words = new ArrayList<>();
    try (BufferedReader r =
        new BufferedReader(new InputStreamReader(c.getAssets().open("palabras_bloqueadas.txt")))) {
      String s;
      while ((s = r.readLine()) != null) words.add(s);
    } catch (IOException ignored) {
    }
    profanity = new ProfanityFilter(words);
  }

  public boolean admin() {
    return auth.signedIn() && profile != null && profile.isAdmin();
  }

  public boolean ready() {
    return auth.configured();
  }

  public void local(Callback<Void> cb) {
    disk.execute(
        () -> {
          List<Restaurant> cache = db.cached();
          Set<String> fav = db.favorites();
          List<String> ids = db.recentIds(), terms = db.searches();
          main.post(
              () -> {
                restaurants.clear();
                restaurants.addAll(cache);
                if (!auth.signedIn()) {
                  favoriteIds.clear();
                  favoriteIds.addAll(fav);
                }
                recentIds.clear();
                recentIds.addAll(ids);
                searches.clear();
                searches.addAll(terms);
                cb.success(null);
              });
        });
  }

  public void execute(String op, JsonObject vars, Callback<JsonObject> cb) {
    if (!ready()) {
      cb.failure("Conecta Firebase para cargar y sincronizar los establecimientos.");
      return;
    }
    bridge.execute(
        op,
        vars.toString(),
        new Callback<String>() {
          public void success(String value) {
            try {
              cb.success(JsonParser.parseString(value).getAsJsonObject());
            } catch (RuntimeException e) {
              cb.failure("La respuesta del servidor no se pudo leer.");
            }
          }

          public void failure(String m) {
            cb.failure(m);
          }
        });
  }

  public static JsonObject vars(String... pairs) {
    JsonObject o = new JsonObject();
    for (int i = 0; i < pairs.length; i += 2) o.addProperty(pairs[i], pairs[i + 1]);
    return o;
  }

  public void refresh(Callback<Void> cb) {
    execute(
        "ListRestaurants",
        new JsonObject(),
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            List<Restaurant> list = RemoteMapper.restaurants(o.getAsJsonArray("restaurants"));
            restaurants.clear();
            restaurants.addAll(list);
            notice = "";
            disk.execute(() -> db.cache(list));
            cb.success(null);
          }

          public void failure(String m) {
            notice =
                restaurants.isEmpty()
                    ? m
                    : "Sin conexiÃ³n Â· mostrando la Ãºltima informaciÃ³n guardada.";
            cb.failure(notice);
          }
        });
  }

  public Restaurant find(String id) {
    for (Restaurant r : restaurants) if (r.id.equals(id)) return r;
    return null;
  }

  public List<Restaurant> list(String mode) {
    List<Restaurant> result = new ArrayList<>();
    for (Restaurant r : restaurants) {
      if ("INACTIVO".equals(r.status)) continue;
      if (mode.equals("favorites") && !favoriteIds.contains(r.id)) continue;
      if (mode.equals("history") && !recentIds.contains(r.id)) continue;
      if ((mode.equals("home") || mode.equals("explore")) && !filter.matches(r)) continue;
      result.add(r);
    }
    if (mode.equals("history")) result.sort(Comparator.comparingInt(r -> recentIds.indexOf(r.id)));
    else if (filter.nearest && latitude != null)
      result.sort(Comparator.comparingDouble(this::distance));
    return result;
  }

  public double distance(Restaurant r) {
    return latitude == null
        ? Double.NaN
        : DistanceUtils.meters(latitude, longitude, r.latitude, r.longitude);
  }

  public void seen(String id) {
    recentIds.remove(id);
    recentIds.add(0, id);
    while (recentIds.size() > 20) recentIds.remove(20);
    disk.execute(() -> db.seen(id));
  }

  public void search(String term) {
    term = term.trim();
    if (term.isEmpty()) return;
    String value = term;
    searches.removeIf(s -> s.equalsIgnoreCase(value));
    searches.add(0, value);
    while (searches.size() > 10) searches.remove(10);
    disk.execute(() -> db.search(value));
  }

  public void clearHistory() {
    searches.clear();
    recentIds.clear();
    disk.execute(db::clearHistory);
  }

  public void toggleFavorite(String id, Callback<Void> cb) {
    if (pendingFavorites.contains(id)) {
      cb.failure("Espera a que termine el guardado.");
      return;
    }
    boolean add = !favoriteIds.contains(id);
    if (add) favoriteIds.add(id);
    else favoriteIds.remove(id);
    if (!auth.signedIn()) {
      disk.execute(() -> db.favorite(id, add));
      cb.success(null);
      return;
    }
    pendingFavorites.add(id);
    int version = sessionVersion;
    execute(
        add ? "AddFavorite" : "RemoveFavorite",
        vars("restaurantId", id),
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            if (version == sessionVersion) {
              pendingFavorites.remove(id);
              cb.success(null);
            }
          }

          public void failure(String m) {
            if (version != sessionVersion) return;
            pendingFavorites.remove(id);
            if (add) favoriteIds.remove(id);
            else favoriteIds.add(id);
            cb.failure(m);
          }
        });
  }

  public void loadProfile(Callback<Void> cb) {
    int version = sessionVersion;
    execute(
        "MyProfile",
        new JsonObject(),
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            if (version != sessionVersion) return;
            if (o.get("user").isJsonNull()) {
              profile = null;
              cb.failure(
                  "Tu cuenta existe, pero falta completar el perfil. Ingresa tu nombre de usuario"
                      + " para reintentar.");
              return;
            }
            profile = gson.fromJson(o.get("user"), UserProfile.class);
            syncFavorites(cb);
          }

          public void failure(String m) {
            if (version != sessionVersion) return;
            profile = null;
            cb.failure(m);
          }
        });
  }

  public void createProfile(String username, Callback<Void> cb) {
    execute(
        "CreateProfile",
        vars("username", username),
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            loadProfile(cb);
          }

          public void failure(String m) {
            cb.failure(
                "La cuenta estÃ¡ creada, pero el perfil no se guardÃ³. Reintenta con un nombre"
                    + " disponible y conexiÃ³n.");
          }
        });
  }

  private void syncFavorites(Callback<Void> cb) {
    int version = sessionVersion;
    execute(
        "MyFavorites",
        new JsonObject(),
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            if (version != sessionVersion) return;
            favoriteIds.clear();
            for (JsonElement e : o.getAsJsonArray("favorites"))
              favoriteIds.add(
                  RemoteMapper.text(e.getAsJsonObject().getAsJsonObject("restaurant"), "id"));
            disk.execute(
                () -> {
                  List<String> ids = new ArrayList<>(db.favorites());
                  main.post(() -> migrate(ids, 0, version, cb));
                });
          }

          public void failure(String m) {
            if (version == sessionVersion) cb.failure(m);
          }
        });
  }

  private void migrate(List<String> ids, int i, int version, Callback<Void> cb) {
    if (version != sessionVersion) return;
    if (i == ids.size()) {
      cb.success(null);
      return;
    }
    String id = ids.get(i);
    Callback<JsonObject> done =
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            if (version != sessionVersion) return;
            favoriteIds.add(id);
            disk.execute(
                () -> {
                  db.favorite(id, false);
                  main.post(() -> migrate(ids, i + 1, version, cb));
                });
          }

          public void failure(String m) {
            if (version == sessionVersion)
              cb.failure(
                  "SesiÃ³n iniciada. Tus favoritos pendientes siguen guardados en este dispositivo;"
                      + " reintenta la sincronizaciÃ³n.");
          }
        };
    if (favoriteIds.contains(id)) done.success(new JsonObject());
    else execute("AddFavorite", vars("restaurantId", id), done);
  }

  public void logout(Callback<Void> cb) {
    sessionVersion++;
    pendingFavorites.clear();
    auth.logout();
    profile = null;
    favoriteIds.clear();
    local(cb);
  }

  public Review ownReview(Restaurant r) {
    for (Review v : r.reviews) if (v.userId.equals(auth.uid())) return v;
    return null;
  }

  public void saveReview(Restaurant r, int rating, String comment, Callback<Void> cb) {
    if (!auth.signedIn() || profile == null) {
      cb.failure("Debes iniciar sesiÃ³n y completar tu perfil para escribir una reseÃ±a.");
      return;
    }
    String error = ValidationUtils.reviewError(rating, comment, profanity);
    if (error != null) {
      cb.failure(error);
      return;
    }
    JsonObject v = vars("restaurantId", r.id, "comment", comment.trim());
    v.addProperty("rating", rating);
    execute("SaveReview", v, refreshAfter(cb));
  }

  public void deleteReview(Review review, boolean moderation, Callback<Void> cb) {
    if (moderation && !admin()) {
      cb.failure("Acceso restringido.");
      return;
    }
    if (!moderation && !review.userId.equals(auth.uid())) {
      cb.failure("Solo puedes eliminar tu propia reseÃ±a.");
      return;
    }
    JsonObject v = vars("restaurantId", review.restaurantId);
    if (moderation) v.addProperty("userId", review.userId);
    execute(moderation ? "ModerateReview" : "DeleteMyReview", v, refreshAfter(cb));
  }

  private Callback<JsonObject> refreshAfter(Callback<Void> cb) {
    return new Callback<JsonObject>() {
      public void success(JsonObject o) {
        refresh(cb);
      }

      public void failure(String m) {
        cb.failure(m);
      }
    };
  }

  public void adminRestaurants(Callback<List<Restaurant>> cb) {
    if (!admin()) {
      cb.failure("Acceso restringido.");
      return;
    }
    execute(
        "AdminRestaurants",
        new JsonObject(),
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            cb.success(RemoteMapper.restaurants(o.getAsJsonArray("restaurants")));
          }

          public void failure(String m) {
            cb.failure(m);
          }
        });
  }

  public void adminReviews(Callback<List<Review>> cb) {
    if (!admin()) {
      cb.failure("Acceso restringido.");
      return;
    }
    execute(
        "AdminReviews",
        new JsonObject(),
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            List<Review> list = new ArrayList<>();
            for (JsonElement e : o.getAsJsonArray("reviews"))
              list.add(RemoteMapper.review(e.getAsJsonObject()));
            cb.success(list);
          }

          public void failure(String m) {
            cb.failure(m);
          }
        });
  }

  public void setStatus(Restaurant r, String status, Callback<Void> cb) {
    if (!admin()) {
      cb.failure("Acceso restringido.");
      return;
    }
    execute("SetRestaurantStatus", vars("id", r.id, "status", status), refreshAfter(cb));
  }

  public void saveRestaurant(Restaurant r, Callback<Void> cb) {
    if (!admin()) {
      cb.failure("Acceso restringido.");
      return;
    }
    String error = ValidationUtils.restaurantError(r);
    if (error != null) {
      cb.failure(error);
      return;
    }
    List<String> ops = new ArrayList<>();
    List<JsonObject> values = new ArrayList<>();
    JsonObject v =
        vars(
            "id",
            r.id,
            "name",
            r.name.trim(),
            "address",
            r.address.trim(),
            "cityId",
            r.cityId,
            "imageUrl",
            r.imageUrl.trim(),
            "description",
            r.description,
            "phone",
            r.phone,
            "website",
            r.website);
    v.addProperty("latitude", r.latitude);
    v.addProperty("longitude", r.longitude);
    ops.add("SaveRestaurant");
    values.add(v);
    for (String type : r.types) {
      ops.add("AssignType");
      values.add(vars("restaurantId", r.id, "typeId", type));
    }
    for (String c : r.cuisines) {
      ops.add("AssignCuisine");
      values.add(vars("restaurantId", r.id, "cuisineId", c));
    }
    for (Schedule s : r.schedules) {
      ops.add("SaveSchedule");
      JsonObject h = vars("restaurantId", r.id, "opens", s.opens, "closes", s.closes);
      h.addProperty("day", s.day);
      h.addProperty("closed", s.closed);
      values.add(h);
    }
    ops.add("SetRestaurantStatus");
    values.add(vars("id", r.id, "status", r.status));
    saveStep(ops, values, 0, cb);
  }

  private void saveStep(List<String> ops, List<JsonObject> values, int i, Callback<Void> cb) {
    if (i == ops.size()) {
      refresh(cb);
      return;
    }
    execute(
        ops.get(i),
        values.get(i),
        new Callback<JsonObject>() {
          public void success(JsonObject o) {
            saveStep(ops, values, i + 1, cb);
          }

          public void failure(String m) {
            cb.failure(
                "No se completÃ³ el guardado. Si se guardÃ³ el borrador, permanece inactivo."
                    + " Reintenta sin salir del formulario.");
          }
        });
  }
}
