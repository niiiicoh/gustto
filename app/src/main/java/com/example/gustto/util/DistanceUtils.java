package com.example.gustto.util;

import java.util.Locale;

public final class DistanceUtils {
  private DistanceUtils() {}

  public static double meters(double a, double b, double c, double d) {
    double x = Math.toRadians(c - a), y = Math.toRadians(d - b);
    double h =
        Math.sin(x / 2) * Math.sin(x / 2)
            + Math.cos(Math.toRadians(a))
                * Math.cos(Math.toRadians(c))
                * Math.sin(y / 2)
                * Math.sin(y / 2);
    return 6371000 * 2 * Math.asin(Math.min(1, Math.sqrt(h)));
  }

  public static String format(double m) {
    return m < 1000
        ? Math.round(m) + " m"
        : String.format(new Locale("es", "CL"), "%.1f km", m / 1000);
  }
}
