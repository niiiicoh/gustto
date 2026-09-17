package com.example.gustto.repository;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;

public class AuthRepository {
  public boolean configured() {
    return !FirebaseApp.getApps(com.example.gustto.GusttoApp.instance).isEmpty();
  }

  public FirebaseAuth auth() {
    return FirebaseAuth.getInstance();
  }

  public boolean signedIn() {
    return configured() && auth().getCurrentUser() != null;
  }

  public String uid() {
    return signedIn() ? auth().getCurrentUser().getUid() : "";
  }

  public void login(String email, String password, Callback<Void> cb) {
    if (!configured()) {
      cb.failure("Falta configurar Firebase en este proyecto.");
      return;
    }
    auth()
        .signInWithEmailAndPassword(email, password)
        .addOnSuccessListener(r -> cb.success(null))
        .addOnFailureListener(
            e ->
                cb.failure(
                    "No pudimos iniciar sesión. Revisa el correo, la contraseña y la conexión."));
  }

  public void register(String name, String email, String password, Callback<Void> cb) {
    if (!configured()) {
      cb.failure("Falta configurar Firebase en este proyecto.");
      return;
    }
    auth()
        .createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener(
            r -> {
              FirebaseUser u = r.getUser();
              if (u == null) {
                cb.failure("No se pudo recuperar la cuenta.");
                return;
              }
              u.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build())
                  .addOnCompleteListener(t -> cb.success(null));
            })
        .addOnFailureListener(
            e ->
                cb.failure(
                    "No se pudo crear la cuenta. Revisa el correo, la contraseña o si ya existe una"
                        + " cuenta."));
  }

  public void reset(String email, Callback<Void> cb) {
    if (!configured()) {
      cb.failure("Falta configurar Firebase.");
      return;
    }
    auth()
        .sendPasswordResetEmail(email)
        .addOnSuccessListener(v -> cb.success(null))
        .addOnFailureListener(e -> cb.failure("No se pudo enviar el correo de recuperación."));
  }

  public void logout() {
    if (configured()) auth().signOut();
  }
}
