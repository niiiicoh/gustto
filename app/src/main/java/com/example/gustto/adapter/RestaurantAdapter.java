package com.example.gustto.adapter;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gustto.R;
import com.example.gustto.model.Restaurant;
import com.example.gustto.repository.GusttoRepository;
import com.example.gustto.util.DistanceUtils;
import java.util.*;
import java.util.function.Consumer;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.Holder> {
  private final List<Restaurant> items = new ArrayList<>();
  private final GusttoRepository repo;
  private final Consumer<Restaurant> open, favorite;
  private final boolean admin;

  public RestaurantAdapter(
      GusttoRepository r, Consumer<Restaurant> o, Consumer<Restaurant> f, boolean a) {
    repo = r;
    open = o;
    favorite = f;
    admin = a;
    setHasStableIds(true);
  }

  public void submit(List<Restaurant> values) {
    items.clear();
    items.addAll(values);
    notifyDataSetChanged();
  }

  @Override
  public long getItemId(int p) {
    return items.get(p).id.hashCode();
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup p, int t) {
    return new Holder(
        LayoutInflater.from(p.getContext()).inflate(R.layout.item_restaurant, p, false));
  }

  @Override
  public void onBindViewHolder(@NonNull Holder h, int i) {
    Restaurant r = items.get(i);
    h.name.setText(r.name);
    h.category.setText(String.join(" · ", r.types));
    String d = repo.latitude == null ? "" : " · " + DistanceUtils.format(repo.distance(r));
    h.rating.setText(r.ratingLabel() + d);
    h.status.setText(
        admin
            ? r.cityName + " · " + r.statusLabel()
            : r.status.equals("CERRADO_TEMPORALMENTE") ? r.statusLabel() : r.cityName);
    Glide.with(h.image)
        .load(r.imageUrl)
        .centerCrop()
        .placeholder(R.drawable.placeholder_food)
        .error(R.drawable.placeholder_food)
        .into(h.image);
    h.itemView.setOnClickListener(v -> open.accept(r));
    h.itemView.findViewById(R.id.restaurant_row).setOnClickListener(v -> open.accept(r));
    h.favorite.setImageResource(admin ? R.drawable.ic_filter : R.drawable.ic_heart);
    h.favorite.setSelected(repo.favoriteIds.contains(r.id));
    h.favorite.setBackgroundResource(
        repo.favoriteIds.contains(r.id) && !admin
            ? R.drawable.bg_soft
            : android.R.color.transparent);
    h.favorite.setContentDescription(
        admin
            ? "Opciones de " + r.name
            : (repo.favoriteIds.contains(r.id) ? "Quitar de favoritos: " : "Guardar: ") + r.name);
    h.favorite.setOnClickListener(v -> favorite.accept(r));
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class Holder extends RecyclerView.ViewHolder {
    TextView name, category, rating, status;
    ImageView image;
    ImageButton favorite;

    Holder(View v) {
      super(v);
      name = v.findViewById(R.id.restaurant_name);
      category = v.findViewById(R.id.restaurant_category);
      rating = v.findViewById(R.id.restaurant_rating);
      status = v.findViewById(R.id.restaurant_status);
      image = v.findViewById(R.id.restaurant_image);
      favorite = v.findViewById(R.id.restaurant_favorite);
    }
  }
}
