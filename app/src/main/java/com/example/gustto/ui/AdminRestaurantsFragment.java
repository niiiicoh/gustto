package com.example.gustto.ui;

import android.text.*;
import androidx.recyclerview.widget.*;
import com.example.gustto.R;
import com.example.gustto.adapter.RestaurantAdapter;
import com.example.gustto.model.Restaurant;
import com.example.gustto.repository.*;
import com.example.gustto.util.RestaurantFilter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import java.util.*;

public class AdminRestaurantsFragment extends FormFragment {
  private List<Restaurant> all = new ArrayList<>();

  protected void draw() {
    content.removeAllViews();
    title("Restaurantes");
    if (!repo().admin()) {
      notice("Acceso restringido.");
      return;
    }
    button("Nuevo establecimiento", () -> host().open(EditRestaurantFragment.create(null)));
    TextInputLayout search =
        field("Buscar establecimiento", "", android.text.InputType.TYPE_CLASS_TEXT);
    android.widget.Spinner status =
        spinner(
            "Estado", new String[] {"Todos", "Activos", "Cerrados temporalmente", "Inactivos"}, 0);
    RecyclerView list = part(R.layout.part_list);
    list.setLayoutManager(new LinearLayoutManager(requireContext()));
    RestaurantAdapter adapter =
        new RestaurantAdapter(
            repo(),
            r -> host().open(EditRestaurantFragment.create(r)),
            r ->
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(r.name)
                    .setItems(
                        new String[] {"Editar", "Activo", "Cerrado temporalmente", "Inactivo"},
                        (d, i) -> {
                          if (i == 0) host().open(EditRestaurantFragment.create(r));
                          else
                            new MaterialAlertDialogBuilder(requireContext())
                                .setTitle("¿Cambiar estado?")
                                .setMessage("Se actualizará la visibilidad de " + r.name + ".")
                                .setNegativeButton("Cancelar", null)
                                .setPositiveButton(
                                    "Guardar",
                                    (dialog, n) ->
                                        repo()
                                            .setStatus(
                                                r,
                                                new String[] {
                                                      "",
                                                      "ACTIVO",
                                                      "CERRADO_TEMPORALMENTE",
                                                      "INACTIVO"
                                                    }
                                                    [i],
                                                new Callback<Void>() {
                                                  public void success(Void v) {
                                                    if (isAdded() && getView() != null) draw();
                                                  }

                                                  public void failure(String m) {
                                                    if (isAdded() && getView() != null)
                                                      host().message(m);
                                                  }
                                                }))
                                .show();
                        })
                    .show(),
            true);
    list.setAdapter(adapter);
    Runnable filter =
        () -> {
          List<Restaurant> out = new ArrayList<>();
          String term = RestaurantFilter.normalize(value(search));
          String state =
              new String[] {"", "ACTIVO", "CERRADO_TEMPORALMENTE", "INACTIVO"}
                  [status.getSelectedItemPosition()];
          for (Restaurant r : all)
            if (RestaurantFilter.normalize(r.name).contains(term)
                && (state.isEmpty() || state.equals(r.status))) out.add(r);
          adapter.submit(out);
        };
    search
        .getEditText()
        .addTextChangedListener(
            new TextWatcher() {
              public void beforeTextChanged(CharSequence s, int st, int c, int a) {}

              public void onTextChanged(CharSequence s, int st, int b, int c) {
                filter.run();
              }

              public void afterTextChanged(Editable e) {}
            });
    status.setOnItemSelectedListener(
        new android.widget.AdapterView.OnItemSelectedListener() {
          public void onItemSelected(
              android.widget.AdapterView<?> p, android.view.View v, int i, long id) {
            filter.run();
          }

          public void onNothingSelected(android.widget.AdapterView<?> p) {}
        });
    text("Incluye los establecimientos inactivos y borradores.");
    repo()
        .adminRestaurants(
            new Callback<List<Restaurant>>() {
              public void success(List<Restaurant> data) {
                if (!isAdded() || getView() == null) return;
                all = data;
                filter.run();
                if (data.isEmpty()) text("Aún no hay establecimientos. Crea el primero.");
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
