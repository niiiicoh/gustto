package com.example.gustto.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.example.gustto.*;
import com.example.gustto.repository.*;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.*;
import com.google.android.material.textfield.*;
import java.util.*;

public abstract class FormFragment extends Fragment {
  protected LinearLayout content;

  protected MainActivity host() {
    return (MainActivity) requireActivity();
  }

  protected GusttoRepository repo() {
    return host().repo();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle s) {
    return i.inflate(R.layout.fragment_form, c, false);
  }

  @Override
  public void onViewCreated(@NonNull View v, Bundle s) {
    content = v.findViewById(R.id.form_content);
    draw();
  }

  protected abstract void draw();

  protected <T extends View> T part(int layout) {
    T v = (T) getLayoutInflater().inflate(layout, content, false);
    content.addView(v);
    return v;
  }

  protected TextView text(String value) {
    TextView t = part(R.layout.part_text);
    t.setText(value);
    return t;
  }

  protected TextView title(String value) {
    TextView t = text(value);
    t.setTextAppearance(R.style.Gustto_Title);
    ViewCompatHelper.heading(t);
    return t;
  }

  protected void notice(String value) {
    TextView t = text(value);
    t.setBackgroundResource(R.drawable.bg_soft);
    int d = (int) (16 * getResources().getDisplayMetrics().density);
    t.setPadding(d, d, d, d);
  }

  protected MaterialButton button(String label, Runnable action) {
    MaterialButton b = part(R.layout.part_button);
    b.setText(label);
    b.setOnClickListener(v -> action.run());
    return b;
  }

  protected TextInputLayout field(String label, String value, int type) {
    TextInputLayout box = part(R.layout.part_field);
    box.setHint(label);
    box.getEditText().setInputType(type);
    box.getEditText().setText(value);
    box.setId(View.generateViewId());
    box.getEditText().setId(View.generateViewId());
    if (type == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD))
      box.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
    return box;
  }

  protected String value(TextInputLayout f) {
    return f.getEditText().getText().toString();
  }

  protected Spinner spinner(String label, String[] values, int selected) {
    text(label);
    Spinner s = part(R.layout.part_spinner);
    s.setContentDescription(label);
    ArrayAdapter<String> a =
        new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, values);
    s.setAdapter(a);
    s.setSelection(selected);
    return s;
  }

  protected ChipGroup chips(
      String label, Collection<String> values, Collection<String> selected, boolean single) {
    text(label);
    ChipGroup g = part(R.layout.part_chip_group);
    g.setSingleSelection(single);
    for (String value : values) {
      Chip c = (Chip) getLayoutInflater().inflate(R.layout.part_chip, g, false);
      c.setId(View.generateViewId());
      c.setText(value);
      c.setCheckable(true);
      c.setChecked(selected.contains(value));
      g.addView(c);
    }
    return g;
  }

  protected List<String> selected(ChipGroup g) {
    List<String> out = new ArrayList<>();
    for (int i = 0; i < g.getChildCount(); i++) {
      Chip c = (Chip) g.getChildAt(i);
      if (c.isChecked()) out.add(c.getText().toString());
    }
    return out;
  }

  protected Callback<Void> done(MaterialButton b, Runnable success) {
    b.setEnabled(false);
    return new Callback<Void>() {
      public void success(Void v) {
        if (!isAdded() || getView() == null) return;
        b.setEnabled(true);
        success.run();
      }

      public void failure(String m) {
        if (!isAdded() || getView() == null) return;
        b.setEnabled(true);
        host().message(m);
      }
    };
  }

  private static final class ViewCompatHelper {
    static void heading(View v) {
      androidx.core.view.ViewCompat.setAccessibilityHeading(v, true);
    }
  }
}
