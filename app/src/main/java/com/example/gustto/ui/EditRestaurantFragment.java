package com.example.gustto.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.*;
import android.widget.*;
import androidx.annotation.NonNull;
import com.example.gustto.R;
import com.example.gustto.model.*;
import com.example.gustto.util.*;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import java.util.*;
import java.util.function.Consumer;

public class EditRestaurantFragment extends FormFragment {
  private Restaurant draft;
  private int step = 1;

  public static EditRestaurantFragment create(Restaurant r) {
    EditRestaurantFragment f = new EditRestaurantFragment();
    Bundle b = new Bundle();
    if (r != null) b.putString("draft", new Gson().toJson(r));
    f.setArguments(b);
    return f;
  }

  @Override
  public void onCreate(Bundle b) {
    super.onCreate(b);
    String json =
        b != null
            ? b.getString("draft")
            : getArguments() == null ? null : getArguments().getString("draft");
    draft = json == null ? new Restaurant() : new Gson().fromJson(json, Restaurant.class);
    step = b == null ? 1 : b.getInt("step", 1);
    if (draft.schedules.isEmpty())
      for (int i = 1; i <= 7; i++) draft.schedules.add(new Schedule(i));
    getParentFragmentManager()
        .setFragmentResultListener(
            "location",
            this,
            (key, result) -> {
              draft.latitude = result.getDouble("lat");
              draft.longitude = result.getDouble("lng");
              draft.locationConfirmed = true;
              if (getView() != null) {
                content.removeAllViews();
                draw();
              }
            });
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle b) {
    super.onSaveInstanceState(b);
    b.putString("draft", new Gson().toJson(draft));
    b.putInt("step", step);
  }

  protected void draw() {
    content.removeAllViews();
    title(draft.name.isEmpty() ? "Nuevo establecimiento" : draft.name);
    text("Paso " + step + " de 2");
    if (!repo().admin()) {
      notice("Acceso restringido.");
      return;
    }
    if (step == 1) basic();
    else hours();
  }

  private void bind(TextInputLayout f, Consumer<String> set) {
    f.getEditText()
        .addTextChangedListener(
            new TextWatcher() {
              public void beforeTextChanged(CharSequence s, int st, int c, int a) {}

              public void onTextChanged(CharSequence s, int st, int b, int c) {
                set.accept(s.toString());
              }

              public void afterTextChanged(Editable e) {}
            });
  }

  private void basic() {
    text("Datos principales");
    bind(
        field(
            "Nombre del establecimiento *",
            draft.name,
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES),
        s -> draft.name = s);
    bind(
        field(
            "Dirección *",
            draft.address,
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES),
        s -> {
          if (!s.equals(draft.address)) draft.locationConfirmed = false;
          draft.address = s;
        });
    Spinner city =
        spinner(
            "Ciudad *",
            Catalogs.CITIES,
            Math.max(0, Arrays.asList(Catalogs.CITY_IDS).indexOf(draft.cityId)));
    city.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          public void onItemSelected(AdapterView<?> p, android.view.View v, int i, long id) {
            if (!draft.cityId.equals(Catalogs.CITY_IDS[i])) draft.locationConfirmed = false;
            draft.cityId = Catalogs.CITY_IDS[i];
            draft.cityName = Catalogs.CITIES[i];
          }

          public void onNothingSelected(AdapterView<?> p) {}
        });
    button(
        draft.locationConfirmed
            ? "Ubicación confirmada · Cambiar"
            : "Seleccionar ubicación en Google Maps",
        () -> host().open(MapFragment.pick(draft.address + ", " + draft.cityName)));
    ChipGroup types =
        chips("Tipos · selección múltiple *", Arrays.asList(Catalogs.TYPES), draft.types, false);
    types.setOnCheckedStateChangeListener((g, ids) -> draft.types = selected(g));
    ChipGroup cuisines =
        chips(
            "Gastronomías · selección múltiple *",
            Arrays.asList(Catalogs.CUISINES),
            draft.cuisines,
            false);
    cuisines.setOnCheckedStateChangeListener((g, ids) -> draft.cuisines = selected(g));
    bind(
        field(
            "URL de imagen HTTPS *",
            draft.imageUrl,
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI),
        s -> draft.imageUrl = s);
    bind(
        field(
            "Descripción",
            draft.description,
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE),
        s -> draft.description = s);
    bind(field("Teléfono", draft.phone, InputType.TYPE_CLASS_PHONE), s -> draft.phone = s);
    bind(
        field(
            "Sitio web HTTPS",
            draft.website,
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI),
        s -> draft.website = s);
    button(
        "Continuar a horarios",
        () -> {
          String error = ValidationUtils.restaurantError(draft);
          if (error != null) {
            host().message(error);
            return;
          }
          step = 2;
          draw();
        });
  }

  private void hours() {
    text("Horarios semanales");
    notice("Si el cierre es anterior a la apertura, se entiende que cierra al día siguiente.");
    for (Schedule s : draft.schedules) {
      MaterialSwitch closed = part(R.layout.part_switch);
      closed.setText(Schedule.DAYS[s.day - 1] + " · Cerrado");
      closed.setChecked(s.closed);
      MaterialButton opens = button("Apertura · " + s.opens, () -> {}),
          closes = button("Cierre · " + s.closes, () -> {});
      opens.setEnabled(!s.closed);
      closes.setEnabled(!s.closed);
      opens.setOnClickListener(
          v ->
              time(
                  s.opens,
                  t -> {
                    s.opens = t;
                    opens.setText("Apertura · " + t);
                  }));
      closes.setOnClickListener(
          v ->
              time(
                  s.closes,
                  t -> {
                    s.closes = t;
                    closes.setText("Cierre · " + t);
                  }));
      closed.setOnCheckedChangeListener(
          (b, value) -> {
            s.closed = value;
            opens.setEnabled(!value);
            closes.setEnabled(!value);
          });
    }
    String[] values = {"ACTIVO", "CERRADO_TEMPORALMENTE", "INACTIVO"};
    Spinner status =
        spinner(
            "Estado del establecimiento",
            new String[] {"Activo", "Cerrado temporalmente", "Inactivo"},
            Math.max(0, Arrays.asList(values).indexOf(draft.status)));
    status.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          public void onItemSelected(AdapterView<?> p, android.view.View v, int i, long id) {
            draft.status = values[i];
          }

          public void onNothingSelected(AdapterView<?> p) {}
        });
    MaterialButton save = button("Guardar cambios", () -> {});
    save.setOnClickListener(
        v ->
            repo()
                .saveRestaurant(
                    draft,
                    done(
                        save,
                        () -> {
                          host().message("Establecimiento guardado.");
                          host().getOnBackPressedDispatcher().onBackPressed();
                        })));
    button(
        "Volver a datos principales",
        () -> {
          step = 1;
          draw();
        });
  }

  private void time(String value, Consumer<String> cb) {
    String[] pieces = (Schedule.validTime(value) ? value : "09:00").split(":");
    new TimePickerDialog(
            requireContext(),
            (v, h, m) -> cb.accept(String.format(Locale.ROOT, "%02d:%02d", h, m)),
            Integer.parseInt(pieces[0]),
            Integer.parseInt(pieces[1]),
            true)
        .show();
  }
}
