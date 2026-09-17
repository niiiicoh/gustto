package com.example.gustto.data.remote;

import com.example.gustto.model.*;
import com.google.gson.*;
import java.util.*;

public final class RemoteMapper {
  public static String text(JsonObject o, String key) {
    return o.has(key) && !o.get(key).isJsonNull() ? o.get(key).getAsString() : "";
  }

  public static List<Restaurant> restaurants(JsonArray array) {
    List<Restaurant> result = new ArrayList<>();
    for (JsonElement e : array) result.add(restaurant(e.getAsJsonObject()));
    return result;
  }

  public static Restaurant restaurant(JsonObject o) {
    Restaurant r = new Restaurant();
    r.id = text(o, "id");
    r.name = text(o, "name");
    r.address = text(o, "address");
    r.imageUrl = text(o, "imageUrl");
    r.description = text(o, "description");
    r.phone = text(o, "phone");
    r.website = text(o, "website");
    r.status = text(o, "status");
    r.latitude = o.get("latitude").getAsDouble();
    r.longitude = o.get("longitude").getAsDouble();
    r.locationConfirmed = true;
    r.cityId = text(o.getAsJsonObject("city"), "id");
    r.cityName = text(o.getAsJsonObject("city"), "name");
    for (JsonElement e : o.getAsJsonArray("restaurantTypes_on_restaurant"))
      r.types.add(text(e.getAsJsonObject().getAsJsonObject("type"), "name"));
    for (JsonElement e : o.getAsJsonArray("restaurantCuisines_on_restaurant"))
      r.cuisines.add(text(e.getAsJsonObject().getAsJsonObject("cuisine"), "name"));
    for (JsonElement e : o.getAsJsonArray("schedules_on_restaurant"))
      r.schedules.add(new Gson().fromJson(e, Schedule.class));
    r.schedules.sort(Comparator.comparingInt(s -> s.day));
    for (JsonElement e : o.getAsJsonArray("reviews_on_restaurant")) {
      Review v = review(e.getAsJsonObject());
      v.restaurantId = r.id;
      v.restaurantName = r.name;
      r.reviews.add(v);
    }
    return r;
  }

  public static Review review(JsonObject o) {
    Review r = new Review();
    r.userId = text(o.getAsJsonObject("user"), "id");
    r.username = text(o.getAsJsonObject("user"), "username");
    r.rating = o.get("rating").getAsInt();
    r.comment = text(o, "comment");
    r.createdAt = text(o, "createdAt");
    r.updatedAt = text(o, "updatedAt");
    if (o.has("restaurant")) {
      r.restaurantId = text(o.getAsJsonObject("restaurant"), "id");
      r.restaurantName = text(o.getAsJsonObject("restaurant"), "name");
    }
    return r;
  }
}
