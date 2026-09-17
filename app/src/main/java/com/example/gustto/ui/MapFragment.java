package com.example.gustto.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.gustto.*;
import com.example.gustto.R;
import com.example.gustto.model.Restaurant;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.CancellationTokenSource;
import java.util.*;
import java.util.concurrent.*;

public class MapFragment extends Fragment {
  private GoogleMap map;
  private LatLng selected;
  private Marker marker;
  private TextView description;
  private View root;
  private final ExecutorService geocoder = Executors.newSingleThreadExecutor();
  private CancellationTokenSource locationToken = new CancellationTokenSource();
  private final ActivityResultLauncher<String[]> permission =
      registerForActivityResult(
          new ActivityResultContracts.RequestMultiplePermissions(),
          r -> {
            if (Boolean.TRUE.equals(r.get(Manifest.permission.ACCESS_COARSE_LOCATION))
                || Boolean.TRUE.equals(r.get(Manifest.permission.ACCESS_FINE_LOCATION))) locate();
            else if (isAdded() && getView() != null)
              description.setText(
                  "Puedes explorar sin ubicaciÃ³n. ActÃ­vala cuando quieras ordenar por"
                      + " cercanÃ­a.");
          });

  public static MapFragment preview() {
    return make(false, true, "");
  }

  public static MapFragment browse() {
    return make(false, false, "");
  }

  public static MapFragment pick(String address) {
    return make(true, false, address);
  }

  private static MapFragment make(boolean pick, boolean preview, String address) {
    MapFragment f = new MapFragment();
    Bundle b = new Bundle();
    b.putBoolean("pick", pick);
    b.putBoolean("preview", preview);
    b.putString("address", address);
    f.setArguments(b);
    return f;
  }

  private boolean pick() {
    return requireArguments().getBoolean("pick");
  }

  private boolean previewMode() {
    return requireArguments().getBoolean("preview");
  }

  private MainActivity host() {
    return (MainActivity) requireActivity();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle s) {
    return i.inflate(R.layout.fragment_map, c, false);
  }

  @Override
  public void onViewCreated(@NonNull View v, Bundle state) {
    root = v;
    locationToken = new CancellationTokenSource();
    description = v.findViewById(R.id.map_description);
    TextView confirm = v.findViewById(R.id.map_confirm);
    confirm.setText(pick() ? "Usar esta ubicaciÃ³n" : "Usar mi ubicaciÃ³n");
    confirm.setVisibility(previewMode() ? View.GONE : View.VISIBLE);
    description.setText(
        pick()
            ? "Toca el mapa o arrastra el marcador para confirmar la ubicaciÃ³n."
            : "La Serena y Coquimbo Â· descubre sus sabores");
    if (pick() && !host().repo().admin()) {
      description.setText("Acceso restringido.");
      confirm.setEnabled(false);
      return;
    }
    if (BuildConfig.MAPS_API_KEY.trim().isEmpty()) {
      description.setText(
          "El mapa estarÃ¡ disponible al configurar la clave de Google Maps. Puedes seguir"
              + " explorando los lugares.");
      confirm.setEnabled(false);
      return;
    }
    if (state != null && state.containsKey("lat"))
      selected = new LatLng(state.getDouble("lat"), state.getDouble("lng"));
    SupportMapFragment f =
        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_host);
    if (f == null) {
      f = SupportMapFragment.newInstance();
      getChildFragmentManager().beginTransaction().replace(R.id.map_host, f).commitNow();
    }
    f.getMapAsync(
        g -> {
          if (!isAdded() || root == null) return;
          map = g;
          g.getUiSettings().setZoomControlsEnabled(!previewMode());
          g.getUiSettings().setMapToolbarEnabled(false);
          g.moveCamera(
              CameraUpdateFactory.newLatLngZoom(
                  selected == null ? new LatLng(-29.9045, -71.2489) : selected, 13));
          if (pick()) {
            if (selected != null) move(selected);
            else geocode();
            g.setOnMapClickListener(this::move);
            g.setOnMarkerDragListener(
                new GoogleMap.OnMarkerDragListener() {
                  public void onMarkerDragStart(Marker m) {}

                  public void onMarkerDrag(Marker m) {}

                  public void onMarkerDragEnd(Marker m) {
                    move(m.getPosition());
                  }
                });
          } else {
            refreshMarkers();
            g.setOnInfoWindowClickListener(
                m -> {
                  Object id = m.getTag();
                  if (id != null) {
                    host().repo().seen(id.toString());
                    host().open(DetailFragment.create(id.toString()));
                  }
                });
            if (!previewMode() && hasPermission()) locate();
          }
        });
    confirm.setOnClickListener(
        x -> {
          if (pick()) {
            if (selected == null) {
              description.setText("Primero toca un punto en el mapa.");
              return;
            }
            Bundle b = new Bundle();
            b.putDouble("lat", selected.latitude);
            b.putDouble("lng", selected.longitude);
            getParentFragmentManager().setFragmentResult("location", b);
            host().getOnBackPressedDispatcher().onBackPressed();
          } else if (hasPermission()) locate();
          else
            permission.launch(
                new String[] {
                  Manifest.permission.ACCESS_FINE_LOCATION,
                  Manifest.permission.ACCESS_COARSE_LOCATION
                });
        });
  }

  public void refreshMarkers() {
    if (map == null || pick() || !isAdded()) return;
    map.clear();
    for (Restaurant r : host().repo().list("home")) {
      Marker m =
          map.addMarker(
              new MarkerOptions()
                  .position(new LatLng(r.latitude, r.longitude))
                  .title(r.name)
                  .snippet("Toca para ver el detalle")
                  .icon(BitmapDescriptorFactory.defaultMarker(348)));
      if (m != null) m.setTag(r.id);
    }
  }

  private boolean hasPermission() {
    return ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED;
  }

  @SuppressWarnings("MissingPermission")
  private void locate() {
    if (!isAdded() || map == null || !hasPermission()) return;
    map.setMyLocationEnabled(true);
    description.setText("Buscando tu ubicaciÃ³nâ€¦");
    LocationServices.getFusedLocationProviderClient(requireActivity())
        .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, locationToken.getToken())
        .addOnSuccessListener(
            l -> {
              if (!isAdded() || root == null) return;
              if (l == null) {
                description.setText(
                    "No se pudo obtener la ubicaciÃ³n. Revisa que estÃ© activada y reintenta.");
                return;
              }
              host().repo().latitude = l.getLatitude();
              host().repo().longitude = l.getLongitude();
              host().repo().filter.nearest = true;
              map.animateCamera(
                  CameraUpdateFactory.newLatLngZoom(
                      new LatLng(l.getLatitude(), l.getLongitude()), 14));
              description.setText("UbicaciÃ³n lista Â· los lugares se ordenarÃ¡n por cercanÃ­a.");
            })
        .addOnFailureListener(
            e -> {
              if (isAdded() && root != null)
                description.setText("No pudimos obtener tu ubicaciÃ³n. Puedes seguir explorando.");
            });
  }

  private void move(LatLng p) {
    if (map == null) return;
    selected = p;
    if (marker == null)
      marker =
          map.addMarker(
              new MarkerOptions()
                  .position(p)
                  .draggable(true)
                  .icon(BitmapDescriptorFactory.defaultMarker(348)));
    else marker.setPosition(p);
    description.setText(
        String.format(
            Locale.getDefault(),
            "UbicaciÃ³n seleccionada: %.5f, %.5f. Confirma para guardar.",
            p.latitude,
            p.longitude));
  }

  @SuppressWarnings("deprecation")
  private void geocode() {
    String address = requireArguments().getString("address", "");
    android.content.Context context = requireContext().getApplicationContext();
    geocoder.execute(
        () -> {
          try {
            List<Address> found =
                new Geocoder(context, new Locale("es", "CL"))
                    .getFromLocationName(address + ", Chile", 1);
            if (found == null || found.isEmpty()) return;
            Address a = found.get(0);
            new Handler(Looper.getMainLooper())
                .post(
                    () -> {
                      if (isAdded() && root != null && selected == null) {
                        move(new LatLng(a.getLatitude(), a.getLongitude()));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(selected, 16));
                      }
                    });
          } catch (Exception ignored) {
          }
        });
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle b) {
    super.onSaveInstanceState(b);
    if (selected != null) {
      b.putDouble("lat", selected.latitude);
      b.putDouble("lng", selected.longitude);
    }
  }

  @Override
  public void onDestroyView() {
    locationToken.cancel();
    root = null;
    map = null;
    marker = null;
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    geocoder.shutdownNow();
    super.onDestroy();
  }
}
