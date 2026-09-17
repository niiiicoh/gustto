package com.example.gustto.ui;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.recyclerview.widget.*;
import com.bumptech.glide.Glide;
import com.example.gustto.R;
import com.example.gustto.adapter.ReviewAdapter;
import com.example.gustto.model.*;
import com.example.gustto.repository.*;
import com.example.gustto.util.DistanceUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DetailFragment extends FormFragment {
  public static DetailFragment create(String id) {
    DetailFragment f = new DetailFragment();
    Bundle b = new Bundle();
    b.putString("id", id);
    f.setArguments(b);
    return f;
  }

  protected void draw() {
    content.removeAllViews();
    Restaurant r = repo().find(requireArguments().getString("id"));
    if (r == null) {
      title("Lugar no disponible");
      text("Vuelve a explorar y actualiza los establecimientos.");
      return;
    }
    ImageView photo = part(R.layout.part_image);
    photo.setContentDescription(r.name);
    Glide.with(this)
        .load(r.imageUrl)
        .placeholder(R.drawable.placeholder_food)
        .error(R.drawable.placeholder_food)
        .centerCrop()
        .into(photo);
    title(r.name);
    text(r.ratingLabel());
    text(String.join(" · ", r.types));
    text(String.join(" · ", r.cuisines));
    text(
        r.address
            + " · "
            + r.cityName
            + (repo().latitude != null
                ? "\nAproximadamente " + DistanceUtils.format(repo().distance(r))
                : ""));
    if (!r.status.equals("ACTIVO")) notice(r.statusLabel());
    button(
        repo().favoriteIds.contains(r.id) ? "Guardado · Quitar favorito" : "Guardar favorito",
        () -> {
          repo()
              .toggleFavorite(
                  r.id,
                  new Callback<Void>() {
                    public void success(Void n) {
                      if (isAdded() && getView() != null) draw();
                    }

                    public void failure(String m) {
                      if (isAdded() && getView() != null) {
                        host().message(m);
                        draw();
                      }
                    }
                  });
          draw();
        });
    button(
        "Cómo llegar",
        () ->
            openUrl(
                "https://www.google.com/maps/dir/?api=1&destination="
                    + r.latitude
                    + ","
                    + r.longitude));
    title("Acerca de");
    text(r.description.isEmpty() ? "Este lugar aún no tiene una descripción." : r.description);
    if (!r.phone.isEmpty())
      button(
          "Llamar · " + r.phone,
          () -> {
            try {
              startActivity(
                  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(r.phone))));
            } catch (ActivityNotFoundException e) {
              host().message("No hay una aplicación de teléfono disponible.");
            }
          });
    if (!r.website.isEmpty()) button("Visitar sitio web", () -> openUrl(r.website));
    title("Horarios");
    for (Schedule h : r.schedules) text(h.label());
    if (r.schedules.isEmpty()) text("Horarios no disponibles.");
    title("Reseñas");
    button(
        repo().ownReview(r) == null ? "Escribir una reseña" : "Editar mi reseña",
        () -> {
          if (repo().auth.signedIn() && repo().profile != null)
            host().open(ReviewFragment.create(r.id));
          else {
            host().message("Debes iniciar sesión para escribir una reseña.");
            host().open(new AuthFragment());
          }
        });
    if (r.reviews.isEmpty()) text("Sé la primera persona en compartir su experiencia.");
    RecyclerView list = part(R.layout.part_list);
    list.setLayoutManager(new LinearLayoutManager(requireContext()));
    list.setAdapter(
        new ReviewAdapter(
            r.reviews,
            repo().auth.uid(),
            false,
            review ->
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("¿Eliminar tu reseña?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton(
                        "Eliminar",
                        (d, n) ->
                            repo()
                                .deleteReview(
                                    review,
                                    false,
                                    new Callback<Void>() {
                                      public void success(Void x) {
                                        if (isAdded() && getView() != null) draw();
                                      }

                                      public void failure(String m) {
                                        if (isAdded() && getView() != null) host().message(m);
                                      }
                                    }))
                    .show()));
  }

  private void openUrl(String url) {
    try {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    } catch (ActivityNotFoundException e) {
      host().message("No hay una aplicación disponible para abrir el enlace.");
    }
  }
}
