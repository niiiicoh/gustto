package com.example.gustto;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.*;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.example.gustto.repository.*;
import com.example.gustto.ui.*;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
  private DrawerLayout drawer;
  private NavigationView navigation;
  private BottomNavigationView bottom;

  public GusttoRepository repo() {
    return ((GusttoApp) getApplication()).repository;
  }

  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    drawer = findViewById(R.id.drawer);
    navigation = findViewById(R.id.navigation);
    bottom = findViewById(R.id.bottom_nav);
    ViewCompat.setOnApplyWindowInsetsListener(
        drawer,
        (v, insets) -> {
          Insets bars =
              insets.getInsets(
                  WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
          v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
          return WindowInsetsCompat.CONSUMED;
        });
    ((MaterialToolbar) findViewById(R.id.toolbar))
        .setNavigationOnClickListener(
            v -> {
              if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                getOnBackPressedDispatcher().onBackPressed();
              else drawer.openDrawer(GravityCompat.START);
            });
    bottom.setOnItemSelectedListener(
        item -> {
          route(item.getItemId());
          return true;
        });
    navigation.setNavigationItemSelectedListener(
        item -> {
          drawer.closeDrawers();
          route(item.getItemId());
          return true;
        });
    getSupportFragmentManager().addOnBackStackChangedListener(this::updateChrome);
    getOnBackPressedDispatcher()
        .addCallback(
            this,
            new OnBackPressedCallback(true) {
              public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawers();
                else if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                  getSupportFragmentManager().popBackStack();
                else if (!(getSupportFragmentManager().findFragmentById(R.id.content)
                        instanceof ListFragment)
                    || !"home"
                        .equals(
                            ((ListFragment)
                                    getSupportFragmentManager().findFragmentById(R.id.content))
                                .mode())) bottom.setSelectedItemId(R.id.nav_home);
                else {
                  setEnabled(false);
                  getOnBackPressedDispatcher().onBackPressed();
                }
              }
            });
    repo()
        .local(
            new Callback<Void>() {
              public void success(Void v) {
                if (state == null) root(ListFragment.create("home"));
                updateChrome();
                if (repo().auth.signedIn())
                  repo()
                      .loadProfile(
                          new Callback<Void>() {
                            public void success(Void v) {
                              updateChrome();
                            }

                            public void failure(String m) {
                              message(m);
                              updateChrome();
                            }
                          });
              }

              public void failure(String m) {
                message(m);
              }
            });
  }

  private void route(int id) {
    if (id == R.id.nav_auth) {
      if (repo().auth.signedIn())
        repo()
            .logout(
                new Callback<Void>() {
                  public void success(Void v) {
                    root(ListFragment.create("home"));
                    updateChrome();
                  }

                  public void failure(String m) {
                    message(m);
                  }
                });
      else open(new AuthFragment());
    } else if (id == R.id.nav_profile) root(new ProfileFragment());
    else if (id == R.id.nav_admin) {
      if (repo().admin()) root(new AdminFragment());
    } else
      root(
          ListFragment.create(
              id == R.id.nav_explore
                  ? "explore"
                  : id == R.id.nav_favorites
                      ? "favorites"
                      : id == R.id.nav_history ? "history" : "home"));
  }

  public void root(Fragment fragment) {
    getSupportFragmentManager()
        .popBackStackImmediate(
            null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    updateChrome();
  }

  public void open(Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content, fragment)
        .addToBackStack(null)
        .commit();
  }

  public void updateChrome() {
    boolean child = getSupportFragmentManager().getBackStackEntryCount() > 0;
    MaterialToolbar t = findViewById(R.id.toolbar);
    t.setNavigationIcon(child ? R.drawable.ic_back : R.drawable.ic_menu);
    t.setNavigationContentDescription(child ? "Volver" : "Abrir menú");
    bottom.setVisibility(child ? View.GONE : View.VISIBLE);
    navigation.getMenu().findItem(R.id.nav_admin).setVisible(repo().admin());
    navigation.getMenu().findItem(R.id.nav_profile).setVisible(repo().auth.signedIn());
    navigation
        .getMenu()
        .findItem(R.id.nav_auth)
        .setTitle(repo().auth.signedIn() ? "Cerrar sesión" : "Iniciar sesión");
    ((TextView) navigation.getHeaderView(0).findViewById(R.id.drawer_name))
        .setText(
            repo().profile == null
                ? "Descubre sabores cerca de ti"
                : repo().profile.username
                    + " · "
                    + (repo().admin() ? "Administrador" : "Tu cuenta"));
  }

  public void message(String text) {
    Snackbar.make(findViewById(R.id.main), text, Snackbar.LENGTH_LONG).show();
  }
}
