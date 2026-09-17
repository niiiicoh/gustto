package com.example.gustto.ui;

import android.os.Bundle;
import androidx.recyclerview.widget.*;
import com.example.gustto.R;
import com.example.gustto.adapter.ReviewAdapter;
import com.example.gustto.model.*;
import com.example.gustto.repository.*;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.*;

public class ReviewsFragment extends FormFragment {
  public static ReviewsFragment mine() {
    ReviewsFragment f = new ReviewsFragment();
    Bundle b = new Bundle();
    b.putBoolean("mine", true);
    f.setArguments(b);
    return f;
  }

  protected void draw() {
    content.removeAllViews();
    boolean mine = getArguments() != null && getArguments().getBoolean("mine");
    title(mine ? "Mis reseñas" : "Moderación");
    if (!mine && !repo().admin()) {
      notice("Acceso restringido.");
      return;
    }
    notice(
        mine
            ? "Puedes editar tu opinión desde el detalle del lugar."
            : "Elimina solo contenido que incumpla las normas. Una mala calificación no es motivo"
                  + " de eliminación.");
    if (mine) {
      List<Review> own = new ArrayList<>();
      for (Restaurant r : repo().restaurants)
        for (Review review : r.reviews)
          if (review.userId.equals(repo().auth.uid())) own.add(review);
      show(own, false);
    } else {
      text("Cargando reseñas…");
      repo()
          .adminReviews(
              new Callback<List<Review>>() {
                public void success(List<Review> list) {
                  if (isAdded() && getView() != null) show(list, true);
                }

                public void failure(String m) {
                  if (isAdded() && getView() != null) {
                    notice(m);
                    button("Reintentar", () -> draw());
                  }
                }
              });
    }
  }

  private void show(List<Review> items, boolean admin) {
    if (items.isEmpty()) text("No hay reseñas para mostrar.");
    RecyclerView list = part(R.layout.part_list);
    list.setLayoutManager(new LinearLayoutManager(requireContext()));
    list.setAdapter(
        new ReviewAdapter(
            items,
            repo().auth.uid(),
            admin,
            r ->
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("¿Eliminar esta reseña?")
                    .setMessage(r.comment)
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton(
                        "Eliminar",
                        (d, n) ->
                            repo()
                                .deleteReview(
                                    r,
                                    admin,
                                    new Callback<Void>() {
                                      public void success(Void v) {
                                        if (isAdded() && getView() != null) draw();
                                      }

                                      public void failure(String m) {
                                        if (isAdded() && getView() != null) host().message(m);
                                      }
                                    }))
                    .show()));
    if (!admin)
      for (Review r : items)
        button(
            "Editar · " + r.restaurantName,
            () -> host().open(ReviewFragment.create(r.restaurantId)));
  }
}
