package com.example.gustto.ui;

import com.example.gustto.util.*;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import java.util.*;

public class FiltersFragment extends FormFragment {
  protected void draw() {
    title("Filtros");
    text("Encuentra justo lo que se te antoja.");
    RestaurantFilter f = repo().filter;
    List<String> selectedCity = new ArrayList<>();
    for (int i = 0; i < Catalogs.CITY_IDS.length; i++)
      if (Catalogs.CITY_IDS[i].equals(f.city)) selectedCity.add(Catalogs.CITIES[i]);
    ChipGroup city = chips("Ciudad", Arrays.asList(Catalogs.CITIES), selectedCity, true);
    ChipGroup types =
        chips("Tipo de establecimiento", Arrays.asList(Catalogs.TYPES), f.types, false);
    ChipGroup cuisines = chips("Gastronomía", Arrays.asList(Catalogs.CUISINES), f.cuisines, false);
    MaterialSwitch near = part(com.example.gustto.R.layout.part_switch);
    near.setText("Más cercanos primero");
    near.setChecked(f.nearest);
    near.setEnabled(repo().latitude != null);
    if (repo().latitude == null)
      button("Activar mi ubicación", () -> host().open(MapFragment.browse()));
    notice("Puedes combinar varios tipos y gastronomías al mismo tiempo.");
    button(
        "Aplicar filtros",
        () -> {
          List<String> cities = selected(city);
          f.city =
              cities.isEmpty()
                  ? ""
                  : Catalogs.CITY_IDS[Arrays.asList(Catalogs.CITIES).indexOf(cities.get(0))];
          f.types = new HashSet<>(selected(types));
          f.cuisines = new HashSet<>(selected(cuisines));
          f.nearest = near.isChecked() && repo().latitude != null;
          host().getOnBackPressedDispatcher().onBackPressed();
        });
    button(
        "Limpiar filtros",
        () -> {
          f.clear();
          content.removeAllViews();
          draw();
        });
  }
}
