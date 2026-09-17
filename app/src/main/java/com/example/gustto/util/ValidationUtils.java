package com.example.gustto.util;

import com.example.gustto.model.*;
import java.net.URI;
import java.util.*;

public final class ValidationUtils {
  public static boolean httpsUrl(String s) {
    try {
      URI u = new URI(s);
      return "https".equals(u.getScheme()) && u.getHost() != null && !u.getHost().isEmpty();
    } catch (Exception e) {
      return false;
    }
  }

  public static String restaurantError(Restaurant r) {
    if (r.name.trim().isEmpty()) return "Escribe el nombre del establecimiento.";
    if (r.address.trim().isEmpty()) return "Escribe la dirección.";
    if (r.cityId.isEmpty()) return "Selecciona una ciudad.";
    if (!r.locationConfirmed
        || !Double.isFinite(r.latitude)
        || !Double.isFinite(r.longitude)
        || Math.abs(r.latitude) > 90
        || Math.abs(r.longitude) > 180) return "Confirma la ubicación en el mapa.";
    if (r.types.isEmpty() || r.cuisines.isEmpty())
      return "Selecciona al menos un tipo y una gastronomía.";
    if (!httpsUrl(r.imageUrl)) return "La imagen debe tener una URL HTTPS válida.";
    if (!r.website.trim().isEmpty() && !httpsUrl(r.website))
      return "El sitio web debe tener una URL HTTPS válida.";
    if (!Arrays.asList("ACTIVO", "CERRADO_TEMPORALMENTE", "INACTIVO").contains(r.status))
      return "Selecciona un estado válido.";
    if (r.schedules.size() != 7) return "Completa los siete días de la semana.";
    Set<Integer> days = new HashSet<>();
    for (Schedule s : r.schedules)
      if (!s.valid() || !days.add(s.day))
        return "Revisa los horarios: usa HH:mm y horarios distintos de apertura y cierre.";
    return null;
  }

  public static String reviewError(int rating, String comment, ProfanityFilter filter) {
    if (rating < 1 || rating > 5) return "Elige entre 1 y 5 estrellas.";
    if (comment.trim().isEmpty() || comment.length() > 500)
      return "Escribe una reseña de 1 a 500 caracteres.";
    if (filter.blocked(comment))
      return "Mantén una reseña respetuosa. Revisa las palabras utilizadas.";
    return null;
  }
}
