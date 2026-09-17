package com.example.gustto.ui;

import com.example.gustto.repository.*;

public class ProfileFragment extends FormFragment {
  protected void draw() {
    content.removeAllViews();
    title("Perfil");
    if (!repo().auth.signedIn()) {
      notice("Modo invitado\nTus favoritos se guardan en este dispositivo.");
      button("Iniciar sesión", () -> host().open(new AuthFragment()));
    } else if (repo().profile == null) {
      notice("La sesión está iniciada. Falta cargar o completar tu perfil.");
      button(
          "Reintentar perfil",
          () ->
              repo()
                  .loadProfile(
                      new Callback<Void>() {
                        public void success(Void n) {
                          if (isAdded() && getView() != null) {
                            host().updateChrome();
                            draw();
                          }
                        }

                        public void failure(String m) {
                          if (isAdded() && getView() != null) {
                            host().message(m);
                            host().open(new AuthFragment());
                          }
                        }
                      }));
    } else {
      title(repo().profile.username);
      text(repo().profile.email);
      text(
          repo().favoriteIds.size()
              + " favoritos · "
              + repo().recentIds.size()
              + " visitas en este dispositivo");
      button("Mis reseñas", () -> host().open(ReviewsFragment.mine()));
      button(
          "Sincronizar favoritos",
          () ->
              repo()
                  .loadProfile(
                      new Callback<Void>() {
                        public void success(Void n) {
                          if (isAdded() && getView() != null) {
                            draw();
                            host().message("Favoritos sincronizados.");
                          }
                        }

                        public void failure(String m) {
                          if (isAdded() && getView() != null) host().message(m);
                        }
                      }));
      if (repo().admin()) button("Panel de administración", () -> host().open(new AdminFragment()));
    }
    button("Mis favoritos", () -> host().root(ListFragment.create("favorites")));
    button("Historial y recientes", () -> host().root(ListFragment.create("history")));
    button("Preferencias de ubicación", () -> host().open(MapFragment.browse()));
    if (repo().auth.signedIn())
      button(
          "Cerrar sesión",
          () ->
              repo()
                  .logout(
                      new Callback<Void>() {
                        public void success(Void n) {
                          if (isAdded() && getView() != null) {
                            host().updateChrome();
                            draw();
                          }
                        }

                        public void failure(String m) {
                          if (isAdded() && getView() != null) host().message(m);
                        }
                      }));
  }
}
