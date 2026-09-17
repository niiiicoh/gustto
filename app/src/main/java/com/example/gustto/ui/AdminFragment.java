package com.example.gustto.ui;

import com.example.gustto.model.Restaurant;
import com.example.gustto.repository.*;
import java.util.List;

public class AdminFragment extends FormFragment {
  protected void draw() {
    title("Panel administrador");
    text("Resumen de Gustto");
    if (!repo().admin()) {
      notice("Solo los administradores pueden acceder.");
      return;
    }
    android.widget.TextView summary = text("Cargando resumen…");
    repo()
        .adminRestaurants(
            new Callback<List<Restaurant>>() {
              public void success(List<Restaurant> list) {
                if (!isAdded() || getView() == null) return;
                int active = 0, reviews = 0;
                for (Restaurant r : list) {
                  if (r.status.equals("ACTIVO")) active++;
                  reviews += r.reviews.size();
                }
                summary.setText(active + " locales activos    ·    " + reviews + " reseñas");
              }

              public void failure(String m) {
                if (isAdded() && getView() != null) summary.setText(m);
              }
            });
    button("Nuevo establecimiento", () -> host().open(EditRestaurantFragment.create(null)));
    button("Gestionar restaurantes", () -> host().open(new AdminRestaurantsFragment()));
    button("Moderar reseñas", () -> host().open(new ReviewsFragment()));
    notice("Los cambios administrativos se validan también en el servidor.");
  }
}
