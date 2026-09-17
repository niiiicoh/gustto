package com.example.gustto.model;

import java.util.*;

public class Restaurant {
  public String id = UUID.randomUUID().toString(),
      name = "",
      address = "",
      cityId = "LA_SERENA",
      cityName = "La Serena",
      description = "",
      phone = "",
      website = "",
      imageUrl = "",
      status = "ACTIVO";
  public double latitude, longitude;
  public boolean locationConfirmed;
  public List<String> types = new ArrayList<>(), cuisines = new ArrayList<>();
  public List<Schedule> schedules = new ArrayList<>();
  public List<Review> reviews = new ArrayList<>();

  public double average() {
    return reviews.stream().mapToInt(r -> r.rating).average().orElse(0);
  }

  public String ratingLabel() {
    return reviews.isEmpty()
        ? "Sin calificaciones"
        : String.format(new Locale("es", "CL"), "★ %.1f · %d reseñas", average(), reviews.size());
  }

  public String statusLabel() {
    return status.equals("INACTIVO")
        ? "Inactivo"
        : status.equals("CERRADO_TEMPORALMENTE") ? "Cerrado temporalmente" : "Activo";
  }
}
