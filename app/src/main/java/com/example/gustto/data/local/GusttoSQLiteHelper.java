package com.example.gustto.data.local;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import com.example.gustto.model.Restaurant;
import com.google.gson.Gson;
import java.util.*;

/** Local guest state and a public restaurant cache. Never stores credentials or roles. */
public class GusttoSQLiteHelper extends SQLiteOpenHelper {
  private final Gson gson = new Gson();

  public GusttoSQLiteHelper(Context c) {
    super(c, "gustto.db", null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(
        "CREATE TABLE favoritos_locales(id_restaurante TEXT PRIMARY KEY,fecha_agregado INTEGER NOT"
            + " NULL)");
    db.execSQL(
        "CREATE TABLE busquedas_recientes(id INTEGER PRIMARY KEY AUTOINCREMENT,termino TEXT NOT"
            + " NULL COLLATE NOCASE UNIQUE,fecha_busqueda INTEGER NOT NULL)");
    db.execSQL(
        "CREATE TABLE restaurantes_vistos(id_restaurante TEXT PRIMARY"
            + " KEY,fecha_ultima_visualizacion INTEGER NOT NULL)");
    db.execSQL(
        "CREATE TABLE restaurante_cache(id_restaurante TEXT PRIMARY KEY,datos TEXT NOT"
            + " NULL,actualizado INTEGER NOT NULL)");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int old, int next) {
    throw new IllegalStateException("Falta una migraciÃ³n SQLite para la versiÃ³n " + next);
  }

  public Set<String> favorites() {
    Set<String> result = new LinkedHashSet<>();
    try (Cursor c =
        getReadableDatabase()
            .rawQuery(
                "SELECT id_restaurante FROM favoritos_locales ORDER BY fecha_agregado DESC",
                null)) {
      while (c.moveToNext()) result.add(c.getString(0));
    }
    return result;
  }

  public void favorite(String id, boolean value) {
    if (!value) {
      getWritableDatabase().delete("favoritos_locales", "id_restaurante=?", new String[] {id});
      return;
    }
    ContentValues v = new ContentValues();
    v.put("id_restaurante", id);
    v.put("fecha_agregado", System.currentTimeMillis());
    getWritableDatabase()
        .insertWithOnConflict("favoritos_locales", null, v, SQLiteDatabase.CONFLICT_IGNORE);
  }

  public void seen(String id) {
    ContentValues v = new ContentValues();
    v.put("id_restaurante", id);
    v.put("fecha_ultima_visualizacion", System.currentTimeMillis());
    SQLiteDatabase db = getWritableDatabase();
    db.insertWithOnConflict("restaurantes_vistos", null, v, SQLiteDatabase.CONFLICT_REPLACE);
    db.execSQL(
        "DELETE FROM restaurantes_vistos WHERE id_restaurante NOT IN (SELECT id_restaurante FROM"
            + " restaurantes_vistos ORDER BY fecha_ultima_visualizacion DESC,rowid DESC LIMIT 20)");
  }

  public List<String> recentIds() {
    List<String> result = new ArrayList<>();
    try (Cursor c =
        getReadableDatabase()
            .rawQuery(
                "SELECT id_restaurante FROM restaurantes_vistos ORDER BY fecha_ultima_visualizacion"
                    + " DESC,rowid DESC",
                null)) {
      while (c.moveToNext()) result.add(c.getString(0));
    }
    return result;
  }

  public void search(String term) {
    term = term.trim();
    if (term.isEmpty()) return;
    ContentValues v = new ContentValues();
    v.put("termino", term);
    v.put("fecha_busqueda", System.currentTimeMillis());
    SQLiteDatabase db = getWritableDatabase();
    db.insertWithOnConflict("busquedas_recientes", null, v, SQLiteDatabase.CONFLICT_REPLACE);
    db.execSQL(
        "DELETE FROM busquedas_recientes WHERE id NOT IN (SELECT id FROM busquedas_recientes ORDER"
            + " BY fecha_busqueda DESC,id DESC LIMIT 10)");
  }

  public List<String> searches() {
    List<String> result = new ArrayList<>();
    try (Cursor c =
        getReadableDatabase()
            .rawQuery(
                "SELECT termino FROM busquedas_recientes ORDER BY fecha_busqueda DESC,id DESC",
                null)) {
      while (c.moveToNext()) result.add(c.getString(0));
    }
    return result;
  }

  public void clearHistory() {
    getWritableDatabase().delete("busquedas_recientes", null, null);
    getWritableDatabase().delete("restaurantes_vistos", null, null);
  }

  public void cache(List<Restaurant> list) {
    SQLiteDatabase db = getWritableDatabase();
    db.beginTransaction();
    try {
      db.delete("restaurante_cache", null, null);
      for (Restaurant r : list) {
        if ("INACTIVO".equals(r.status)) continue;
        ContentValues v = new ContentValues();
        v.put("id_restaurante", r.id);
        v.put("datos", gson.toJson(r));
        v.put("actualizado", System.currentTimeMillis());
        db.insertWithOnConflict("restaurante_cache", null, v, SQLiteDatabase.CONFLICT_REPLACE);
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  public List<Restaurant> cached() {
    List<Restaurant> list = new ArrayList<>();
    try (Cursor c = getReadableDatabase().rawQuery("SELECT datos FROM restaurante_cache", null)) {
      while (c.moveToNext()) {
        try {
          list.add(gson.fromJson(c.getString(0), Restaurant.class));
        } catch (RuntimeException ignored) {
        }
      }
    }
    return list;
  }
}
