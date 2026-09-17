package com.example.gustto.util;

import com.example.gustto.model.Restaurant;
import java.text.Normalizer;
import java.util.*;

public class RestaurantFilter {
  public String query = "", city = "";
  public Set<String> types = new HashSet<>(), cuisines = new HashSet<>();
  public boolean nearest;

  public boolean matches(Restaurant r) {
    return !"INACTIVO".equals(r.status)
        && normalize(r.name).contains(normalize(query))
        && (city.isEmpty() || city.equals(r.cityId))
        && (types.isEmpty() || !Collections.disjoint(types, r.types))
        && (cuisines.isEmpty() || !Collections.disjoint(cuisines, r.cuisines));
  }

  public static String normalize(String s) {
    return Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD)
        .replaceAll("\\p{M}", "")
        .toLowerCase(Locale.ROOT)
        .trim();
  }

  public void clear() {
    query = "";
    city = "";
    types.clear();
    cuisines.clear();
    nearest = false;
  }
}
