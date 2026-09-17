package com.example.gustto;

import static org.junit.Assert.*;

import com.example.gustto.model.*;
import com.example.gustto.util.*;
import java.util.*;
import org.junit.Test;

public class DomainTest {
  @Test
  public void accentInsensitiveSearch() {
    Restaurant r = new Restaurant();
    r.name = "Café Bruma";
    RestaurantFilter f = new RestaurantFilter();
    f.query = "CAFE";
    assertTrue(f.matches(r));
  }

  @Test
  public void filtersCombineWithOrWithinAndBetweenCategories() {
    Restaurant r = new Restaurant();
    r.name = "Bruma";
    r.types.add("Cafetería");
    r.cuisines.add("Francesa");
    RestaurantFilter f = new RestaurantFilter();
    f.types.addAll(Arrays.asList("Cafetería", "Pastelería"));
    f.cuisines.add("Francesa");
    assertTrue(f.matches(r));
    f.city = "COQUIMBO";
    assertFalse(f.matches(r));
  }

  @Test
  public void inactiveNeverMatches() {
    Restaurant r = new Restaurant();
    r.status = "INACTIVO";
    assertFalse(new RestaurantFilter().matches(r));
  }

  @Test
  public void overnightHoursAreValid() {
    Schedule s = new Schedule(6);
    s.opens = "12:00";
    s.closes = "00:30";
    assertTrue(s.valid());
    assertTrue(s.label().contains("día siguiente"));
  }

  @Test
  public void hoursRejectInvalidAndZeroDuration() {
    Schedule s = new Schedule(1);
    s.opens = "25:00";
    assertFalse(s.valid());
    s.opens = "18:00";
    s.closes = "18:00";
    assertFalse(s.valid());
    s.closed = true;
    assertTrue(s.valid());
  }

  @Test
  public void profanityNormalizesAndUsesWordBoundaries() {
    ProfanityFilter f = new ProfanityFilter(Arrays.asList("imbecil", "idiota"));
    assertTrue(f.blocked("Un IMBÉCIL"));
    assertTrue(f.blocked("idi0ta"));
    assertFalse(f.blocked("idiotamente"));
    assertFalse(f.blocked("No me gustó, muy mala experiencia"));
  }

  @Test
  public void ratingAndCommentBounds() {
    ProfanityFilter f = new ProfanityFilter(Collections.emptyList());
    assertNotNull(ValidationUtils.reviewError(0, "Bien", f));
    assertNotNull(ValidationUtils.reviewError(6, "Bien", f));
    assertNotNull(ValidationUtils.reviewError(2, "   ", f));
    assertNull(ValidationUtils.reviewError(1, "No volvería", f));
  }

  @Test
  public void distancesAndGeographicEdgeCases() {
    assertEquals(0, DistanceUtils.meters(-29, -71, -29, -71), 0.01);
    assertEquals(111195, DistanceUtils.meters(0, 0, 1, 0), 10);
    assertTrue(Double.isFinite(DistanceUtils.meters(0, 0, 0, 180)));
  }

  @Test
  public void urlsMustHaveSecureSchemeAndHost() {
    assertTrue(ValidationUtils.httpsUrl("https://example.com/a.jpg"));
    assertFalse(ValidationUtils.httpsUrl("javascript:alert(1)"));
    assertFalse(ValidationUtils.httpsUrl("https:/image.jpg"));
    assertFalse(ValidationUtils.httpsUrl("http://example.com"));
  }

  @Test
  public void restaurantRequiresUniqueSevenDaySchedule() {
    Restaurant r = new Restaurant();
    r.name = "Cafe";
    r.address = "Calle 1";
    r.locationConfirmed = true;
    r.latitude = -29.9;
    r.longitude = -71.2;
    r.types.add("Cafetería");
    r.cuisines.add("Chilena");
    r.imageUrl = "https://example.com/image.jpg";
    for (int i = 1; i <= 7; i++) r.schedules.add(new Schedule(i));
    assertNull(ValidationUtils.restaurantError(r));
    r.schedules.get(6).day = 1;
    assertNotNull(ValidationUtils.restaurantError(r));
  }

  @Test
  public void averageComesFromReviews() {
    Restaurant r = new Restaurant();
    assertEquals("Sin calificaciones", r.ratingLabel());
    Review a = new Review();
    a.rating = 5;
    Review b = new Review();
    b.rating = 1;
    r.reviews.add(a);
    r.reviews.add(b);
    assertEquals(3, r.average(), 0);
  }
}
