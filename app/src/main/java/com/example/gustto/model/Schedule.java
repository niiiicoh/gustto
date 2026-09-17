package com.example.gustto.model;

public class Schedule {
  public static final String[] DAYS = {
    "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
  };
  public int day;
  public String opens = "09:00", closes = "18:00";
  public boolean closed;

  public Schedule() {}

  public Schedule(int day) {
    this.day = day;
  }

  public boolean valid() {
    return day >= 1
        && day <= 7
        && (closed || (validTime(opens) && validTime(closes) && !opens.equals(closes)));
  }

  public static boolean validTime(String s) {
    return s != null && s.matches("([01][0-9]|2[0-3]):[0-5][0-9]");
  }

  public String label() {
    return DAYS[day - 1]
        + " · "
        + (closed
            ? "Cerrado"
            : opens + " – " + closes + (closes.compareTo(opens) < 0 ? " (día siguiente)" : ""));
  }
}
