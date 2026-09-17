package com.example.gustto.adapter;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gustto.R;
import com.example.gustto.model.Review;
import java.util.*;
import java.util.function.Consumer;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Holder> {
  private final List<Review> items;
  private final String uid;
  private final boolean admin;
  private final Consumer<Review> delete;

  public ReviewAdapter(List<Review> i, String u, boolean a, Consumer<Review> d) {
    items = i;
    uid = u;
    admin = a;
    delete = d;
  }

  @NonNull
  public Holder onCreateViewHolder(@NonNull ViewGroup p, int t) {
    return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_review, p, false));
  }

  public void onBindViewHolder(@NonNull Holder h, int p) {
    Review r = items.get(p);
    ((TextView) h.itemView.findViewById(R.id.review_name))
        .setText(r.username + (admin ? " · " + r.restaurantName : ""));
    ((TextView) h.itemView.findViewById(R.id.review_rating))
        .setText(
            "★ "
                + r.rating
                + " / 5 · "
                + (r.updatedAt.length() >= 10 ? r.updatedAt.substring(0, 10) : ""));
    ((TextView) h.itemView.findViewById(R.id.review_body)).setText(r.comment);
    View b = h.itemView.findViewById(R.id.review_action);
    b.setVisibility(admin || r.userId.equals(uid) ? View.VISIBLE : View.GONE);
    b.setOnClickListener(v -> delete.accept(r));
  }

  public int getItemCount() {
    return items.size();
  }

  static class Holder extends RecyclerView.ViewHolder {
    Holder(View v) {
      super(v);
    }
  }
}
