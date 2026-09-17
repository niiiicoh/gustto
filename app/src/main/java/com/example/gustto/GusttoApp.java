package com.example.gustto;

import android.app.Application;
import com.example.gustto.repository.GusttoRepository;
import com.google.firebase.FirebaseApp;

public class GusttoApp extends Application {
  public static GusttoApp instance;
  public GusttoRepository repository;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    FirebaseApp.initializeApp(this);
    repository = new GusttoRepository(this);
  }
}
