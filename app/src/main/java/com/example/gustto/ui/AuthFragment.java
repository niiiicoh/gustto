package com.example.gustto.ui;

import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.widget.TextView;
import com.example.gustto.R;
import com.example.gustto.repository.*;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AuthFragment extends FormFragment {
  private boolean register;
  private String username = "", email = "";

  @Override
  public void onCreate(Bundle b) {
    super.onCreate(b);
    if (b != null) {
      register = b.getBoolean("register");
      username = b.getString("username", "");
      email = b.getString("email", "");
    }
  }

  @Override
  public void onSaveInstanceState(Bundle b) {
    super.onSaveInstanceState(b);
    b.putBoolean("register", register);
    b.putString("username", username);
    b.putString("email", email);
  }

  protected void draw() {
    content.removeAllViews();
    TextView brand = title("Gustto");
    brand.setTextSize(40);
    brand.setTextColor(getResources().getColor(R.color.burgundy, null));
    text("Vuelve a tus favoritos\ny reseñas guardadas.");
    boolean complete = repo().auth.signedIn() && repo().profile == null;
    title(complete ? "Completa tu perfil" : register ? "Únete a Gustto" : "Inicia sesión");
    text(
        complete
            ? "Elige el nombre que aparecerá en tus reseñas."
            : register
                ? "Guarda favoritos y comparte tu experiencia."
                : "Usa tu correo y contraseña.");
    if (!repo().ready())
      notice("Firebase aún no está configurado. Puedes seguir explorando como invitado.");
    TextInputLayout name =
        (register || complete)
            ? field("Nombre de usuario", username, InputType.TYPE_CLASS_TEXT)
            : null;
    TextInputLayout mail =
        complete
            ? null
            : field(
                "Correo electrónico",
                email,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    TextInputLayout pass =
        complete
            ? null
            : field(
                "Contraseña",
                "",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    TextInputLayout confirmation =
        register && !complete
            ? field(
                "Confirmar contraseña",
                "",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
            : null;
    if (register || complete)
      notice("Tu nombre será visible en tus reseñas. La contraseña nunca se guarda en Gustto.");
    MaterialButton submit =
        button(
            complete ? "Guardar perfil" : register ? "Crear cuenta" : "Iniciar sesión", () -> {});
    submit.setOnClickListener(
        v -> {
          if (name != null) {
            username = value(name).trim();
            if (username.length() < 3 || username.length() > 40) {
              name.setError("Usa entre 3 y 40 caracteres.");
              name.requestFocus();
              return;
            }
            name.setError(null);
          }
          if (!complete) {
            email = value(mail).trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
              mail.setError("Escribe un correo válido.");
              mail.requestFocus();
              return;
            }
            mail.setError(null);
            if (value(pass).length() < 6) {
              pass.setError("Usa al menos 6 caracteres.");
              pass.requestFocus();
              return;
            }
            pass.setError(null);
            if (confirmation != null && !value(pass).equals(value(confirmation))) {
              confirmation.setError("Las contraseñas no coinciden.");
              return;
            }
          }
          Callback<Void> finish =
              done(
                  submit,
                  () -> {
                    host().updateChrome();
                    host().root(new ProfileFragment());
                  });
          Callback<Void> profileResult =
              new Callback<Void>() {
                public void success(Void n) {
                  finish.success(n);
                }

                public void failure(String m) {
                  finish.failure(m);
                  if (isAdded() && getView() != null) {
                    host().updateChrome();
                    draw();
                  }
                }
              };
          if (complete) {
            repo().createProfile(username, profileResult);
            return;
          }
          Callback<Void> signed =
              new Callback<Void>() {
                public void success(Void n) {
                  if (register) repo().createProfile(username, profileResult);
                  else repo().loadProfile(profileResult);
                }

                public void failure(String m) {
                  finish.failure(m);
                }
              };
          if (register) repo().auth.register(username, email, value(pass), signed);
          else repo().auth.login(email, value(pass), signed);
        });
    if (!register && !complete)
      button(
          "¿Olvidaste tu contraseña?",
          () -> {
            email = value(mail).trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
              mail.setError("Escribe tu correo para recuperar el acceso.");
              return;
            }
            repo()
                .auth
                .reset(
                    email,
                    new Callback<Void>() {
                      public void success(Void n) {
                        if (isAdded() && getView() != null)
                          host()
                              .message(
                                  "Si el correo tiene una cuenta, recibirás instrucciones para"
                                      + " recuperar el acceso.");
                      }

                      public void failure(String m) {
                        if (isAdded() && getView() != null) host().message(m);
                      }
                    });
          });
    if (!complete)
      button(
          register ? "Ya tengo cuenta · Iniciar sesión" : "Crear una cuenta",
          () -> {
            email = value(mail);
            register = !register;
            draw();
          });
    button(
        "Continuar como invitado",
        () -> {
          if (repo().auth.signedIn())
            repo()
                .logout(
                    new Callback<Void>() {
                      public void success(Void n) {
                        if (isAdded() && getView() != null)
                          host().root(ListFragment.create("home"));
                      }

                      public void failure(String m) {}
                    });
          else host().root(ListFragment.create("home"));
        });
  }
}
