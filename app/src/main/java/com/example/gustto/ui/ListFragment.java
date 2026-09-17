package com.example.gustto.ui;

import android.os.*;
import android.text.*;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.example.gustto.*;
import com.example.gustto.adapter.RestaurantAdapter;
import com.example.gustto.model.Restaurant;
import com.example.gustto.repository.*;
import com.google.android.material.chip.*;
import java.util.*;

public class ListFragment extends Fragment {
  private RestaurantAdapter adapter;
  private View root;
  private boolean changing;
  private final Handler handler = new Handler(Looper.getMainLooper());
  private Runnable searchTask;

  public static ListFragment create(String mode) {
    ListFragment f = new ListFragment();
    Bundle b = new Bundle();
    b.putString("mode", mode);
    f.setArguments(b);
    return f;
  }

  public String mode() {
    return getArguments() == null ? "home" : getArguments().getString("mode", "home");
  }

  private MainActivity host() {
    return (MainActivity) requireActivity();
  }

  private GusttoRepository repo() {
    return host().repo();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle s) {
    return i.inflate(R.layout.fragment_list, c, false);
  }

  @Override
  public void onViewCreated(@NonNull View v, Bundle s) {
    root = v;
    String mode = mode();
    boolean search = mode.equals("home") || mode.equals("explore");
    ((TextView) v.findViewById(R.id.page_title))
        .setText(
            mode.equals("home")
                ? "Gustto"
                : mode.equals("explore")
                    ? "Explorar"
                    : mode.equals("favorites") ? "Favoritos" : "Recientes");
    ((TextView) v.findViewById(R.id.page_subtitle))
        .setText(
            mode.equals("home")
                ? "Â¿QuÃ© te gustarÃ­a comer hoy?"
                : mode.equals("explore")
                    ? "Encuentra tu prÃ³ximo lugar favorito."
                    : mode.equals("favorites")
                        ? "Tus lugares guardados"
                        : "Sabores a los que quieres volver");
    if (mode.equals("home")) {
      v.findViewById(R.id.list_header)
          .setBackgroundColor(getResources().getColor(R.color.burgundy, null));
      ((TextView) v.findViewById(R.id.page_title))
          .setTextColor(getResources().getColor(R.color.paper, null));
      ((TextView) v.findViewById(R.id.page_subtitle))
          .setTextColor(getResources().getColor(R.color.paper, null));
      ((TextView) v.findViewById(R.id.list_count))
          .setTextColor(getResources().getColor(R.color.paper, null));
      ((TextView) v.findViewById(R.id.list_action))
          .setTextColor(getResources().getColor(R.color.paper, null));
    }
    v.findViewById(R.id.search_row).setVisibility(search ? View.VISIBLE : View.GONE);
    v.findViewById(R.id.chips_scroll)
        .setVisibility(search || mode.equals("history") ? View.VISIBLE : View.GONE);
    EditText input = v.findViewById(R.id.search);
    if (search) input.setText(repo().filter.query);
    input.addTextChangedListener(
        new TextWatcher() {
          public void beforeTextChanged(CharSequence s, int st, int c, int a) {}

          public void onTextChanged(CharSequence s, int st, int b, int c) {
            if (changing) return;
            repo().filter.query = s.toString();
            render();
            if (searchTask != null) handler.removeCallbacks(searchTask);
            String term = s.toString();
            searchTask =
                () -> {
                  if (isAdded() && getView() != null) repo().search(term);
                };
            handler.postDelayed(searchTask, 1000);
          }

          public void afterTextChanged(Editable e) {}
        });
    input.setOnEditorActionListener(
        (t, a, e) -> {
          if (a == EditorInfo.IME_ACTION_SEARCH) {
            repo().search(input.getText().toString());
            input.clearFocus();
            return true;
          }
          return false;
        });
    v.findViewById(R.id.filters).setOnClickListener(x -> host().open(new FiltersFragment()));
    ChipGroup chips = v.findViewById(R.id.quick_chips);
    List<String> labels =
        mode.equals("history")
            ? repo().searches
            : Arrays.asList("Todos", "CafeterÃ­a", "Japonesa", "PastelerÃ­a");
    for (String label : labels) {
      Chip chip = (Chip) getLayoutInflater().inflate(R.layout.part_chip, chips, false);
      chip.setText(label);
      chip.setCheckable(true);
      chips.addView(chip);
      chip.setOnClickListener(
          x -> {
            if (mode.equals("history")) {
              repo().filter.query = label;
              host().root(create("explore"));
              return;
            }
            repo().filter.types.clear();
            repo().filter.cuisines.clear();
            if (label.equals("Japonesa")) repo().filter.cuisines.add(label);
            else if (!label.equals("Todos")) repo().filter.types.add(label);
            render();
          });
    }
    RecyclerView list = v.findViewById(R.id.list);
    list.setLayoutManager(new LinearLayoutManager(requireContext()));
    adapter =
        new RestaurantAdapter(
            repo(),
            r -> {
              repo().seen(r.id);
              host().open(DetailFragment.create(r.id));
            },
            r -> {
              repo()
                  .toggleFavorite(
                      r.id,
                      new Callback<Void>() {
                        public void success(Void n) {
                          if (isAdded() && getView() != null) render();
                        }

                        public void failure(String m) {
                          if (isAdded() && getView() != null) {
                            host().message(m);
                            render();
                          }
                        }
                      });
              render();
            },
            false);
    list.setAdapter(adapter);
    TextView action = v.findViewById(R.id.list_action);
    action.setText(
        mode.equals("history")
            ? "Borrar historial"
            : mode.equals("home") ? "Mi ubicaciÃ³n" : "Actualizar");
    action.setOnClickListener(
        x -> {
          if (mode.equals("history")) {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Â¿Borrar el historial?")
                .setMessage("Se eliminarÃ¡n bÃºsquedas y visitas de este dispositivo.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton(
                    "Borrar",
                    (d, n) -> {
                      repo().clearHistory();
                      chips.removeAllViews();
                      render();
                    })
                .show();
          } else if (mode.equals("home")) host().open(MapFragment.browse());
          else refresh();
        });
    if (mode.equals("home")
        && getResources().getConfiguration().orientation
            != android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
      v.findViewById(R.id.map_container).setVisibility(View.VISIBLE);
      getChildFragmentManager()
          .beginTransaction()
          .replace(R.id.map_container, MapFragment.preview())
          .commit();
    }
    render();
    refresh();
  }

  private void refresh() {
    if (root == null) return;
    root.findViewById(R.id.progress).setVisibility(View.VISIBLE);
    repo()
        .refresh(
            new Callback<Void>() {
              public void success(Void v) {
                if (isAdded() && root != null) {
                  root.findViewById(R.id.progress).setVisibility(View.GONE);
                  render();
                }
              }

              public void failure(String m) {
                if (isAdded() && root != null) {
                  root.findViewById(R.id.progress).setVisibility(View.GONE);
                  render();
                }
              }
            });
  }

  private void render() {
    if (root == null || adapter == null) return;
    List<Restaurant> data = repo().list(mode());
    adapter.submit(data);
    Fragment preview = getChildFragmentManager().findFragmentById(R.id.map_container);
    if (preview instanceof MapFragment) ((MapFragment) preview).refreshMarkers();
    ((TextView) root.findViewById(R.id.list_count)).setText(data.size() + " lugares");
    TextView empty = root.findViewById(R.id.empty);
    empty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
    empty.setText(
        mode().equals("favorites")
            ? "TodavÃ­a no tienes favoritos. Guarda un lugar con el corazÃ³n para encontrarlo"
                  + " aquÃ­."
            : mode().equals("history")
                ? "Los lugares que visites aparecerÃ¡n aquÃ­."
                : "No hay lugares que coincidan. Prueba otra bÃºsqueda o limpia los filtros.");
    TextView notice = root.findViewById(R.id.list_notice);
    String msg = repo().notice;
    if (mode().equals("favorites") && !repo().auth.signedIn())
      msg =
          "Modo invitado Â· guardados en este dispositivo. Toca para iniciar sesiÃ³n y"
              + " sincronizar.";
    notice.setText(msg);
    notice.setVisibility(msg.isEmpty() ? View.GONE : View.VISIBLE);
    notice.setOnClickListener(
        v -> {
          if (mode().equals("favorites") && !repo().auth.signedIn())
            host().open(new AuthFragment());
          else refresh();
        });
  }

  @Override
  public void onDestroyView() {
    if (searchTask != null) handler.removeCallbacks(searchTask);
    root = null;
    adapter = null;
    super.onDestroyView();
  }
}
