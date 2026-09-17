package com.example.gustto.ui;

import android.os.Bundle;
import android.text.InputType;
import android.widget.RatingBar;
import com.example.gustto.R;
import com.example.gustto.model.*;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class ReviewFragment extends FormFragment {
  public static ReviewFragment create(String id) {
    ReviewFragment f = new ReviewFragment();
    Bundle b = new Bundle();
    b.putString("id", id);
    f.setArguments(b);
    return f;
  }

  protected void draw() {
    title("Tu reseña");
    Restaurant r = repo().find(requireArguments().getString("id"));
    if (r == null) {
      notice("Este lugar ya no está disponible.");
      return;
    }
    if (!repo().auth.signedIn()) {
      notice("Inicia sesión para escribir una reseña.");
      button("Iniciar sesión", () -> host().open(new AuthFragment()));
      return;
    }
    text(r.name + " · " + r.cityName);
    title("¿Cómo fue tu experiencia?");
    text("Tu opinión ayuda a descubrir nuevos sabores.");
    Review own = repo().ownReview(r);
    RatingBar rating = part(R.layout.part_rating);
    rating.setContentDescription("Calificación de 1 a 5 estrellas");
    rating.setRating(own == null ? 0 : own.rating);
    TextInputLayout comment =
        field(
            "Cuéntanos tu experiencia",
            own == null ? "" : own.comment,
            InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE
                | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    comment.getEditText().setMinLines(5);
    comment.setCounterEnabled(true);
    comment.setCounterMaxLength(500);
    comment
        .getEditText()
        .setFilters(
            new android.text.InputFilter[] {new android.text.InputFilter.LengthFilter(500)});
    notice("Mantén una reseña respetuosa. Una calificación baja también es una opinión válida.");
    MaterialButton b = button(own == null ? "Publicar reseña" : "Guardar cambios", () -> {});
    b.setOnClickListener(
        v ->
            repo()
                .saveReview(
                    r,
                    (int) rating.getRating(),
                    value(comment),
                    done(
                        b,
                        () -> {
                          host().message("Tu reseña se guardó.");
                          host().getOnBackPressedDispatcher().onBackPressed();
                        })));
    text("Máximo una reseña por establecimiento.");
  }
}
