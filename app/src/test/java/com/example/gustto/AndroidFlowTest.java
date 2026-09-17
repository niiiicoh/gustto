package com.example.gustto;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import com.example.gustto.data.local.GusttoSQLiteHelper;
import com.example.gustto.model.Restaurant;
import com.example.gustto.ui.*;
import java.util.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.robolectric.*;
import org.robolectric.annotation.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
@LooperMode(LooperMode.Mode.PAUSED)
public class AndroidFlowTest {
  @Test
  public void sqlitePersistsGuestFavoritesAndCacheAfterReopen() {
    Context c = RuntimeEnvironment.getApplication();
    c.deleteDatabase("gustto.db");
    GusttoSQLiteHelper db = new GusttoSQLiteHelper(c);
    Restaurant r = new Restaurant();
    r.id = "test-cafe";
    r.name = "Café Bruma";
    db.cache(Collections.singletonList(r));
    db.favorite(r.id, true);
    db.favorite(r.id, true);
    db.seen(r.id);
    db.close();
    db = new GusttoSQLiteHelper(c);
    assertEquals(1, db.favorites().size());
    assertTrue(db.favorites().contains(r.id));
    assertEquals("Café Bruma", db.cached().get(0).name);
    assertEquals(r.id, db.recentIds().get(0));
    db.close();
  }

  @Test
  public void sqliteBoundsHistoryAndDeduplicates() {
    Context c = RuntimeEnvironment.getApplication();
    c.deleteDatabase("gustto.db");
    GusttoSQLiteHelper db = new GusttoSQLiteHelper(c);
    for (int i = 0; i < 30; i++) {
      db.search("cafe " + i);
      db.seen("r" + i);
    }
    assertEquals(10, db.searches().size());
    assertEquals(20, db.recentIds().size());
    db.search("SUSHI");
    db.search("sushi");
    assertEquals(1, db.searches().stream().filter(s -> s.equalsIgnoreCase("sushi")).count());
    db.clearHistory();
    assertTrue(db.searches().isEmpty());
    assertTrue(db.recentIds().isEmpty());
    db.close();
  }

  @Test
  public void guestCanOpenPrimaryAndAuthScreensWithoutFirebase() throws Exception {
    try (org.robolectric.android.controller.ActivityController<MainActivity> ctl =
        Robolectric.buildActivity(MainActivity.class).setup()) {
      MainActivity a = ctl.get();
      for (int i = 0; i < 20; i++) {
        Thread.sleep(10);
        Shadows.shadowOf(Looper.getMainLooper()).idle();
      }
      assertNotNull(a.findViewById(R.id.bottom_nav));
      for (Fragment f :
          Arrays.asList(
              ListFragment.create("explore"),
              ListFragment.create("favorites"),
              ListFragment.create("history"),
              new ProfileFragment(),
              new AuthFragment(),
              new FiltersFragment())) {
        a.root(f);
        a.getSupportFragmentManager().executePendingTransactions();
        Shadows.shadowOf(Looper.getMainLooper()).idle();
        assertNotNull(f.getView());
      }
      assertFalse(a.repo().auth.signedIn());
    }
  }

  @Test
  public void detailsAndReviewGateInflateWithoutNetwork() throws Exception {
    try (org.robolectric.android.controller.ActivityController<MainActivity> ctl =
        Robolectric.buildActivity(MainActivity.class).setup()) {
      MainActivity a = ctl.get();
      for (int i = 0; i < 20; i++) {
        Thread.sleep(10);
        Shadows.shadowOf(Looper.getMainLooper()).idle();
      }
      Restaurant r = new Restaurant();
      r.name = "Prueba de UI";
      a.repo().restaurants.add(r);
      Fragment detail = DetailFragment.create(r.id);
      a.root(detail);
      a.getSupportFragmentManager().executePendingTransactions();
      assertNotNull(detail.getView());
      Fragment review = ReviewFragment.create(r.id);
      a.root(review);
      a.getSupportFragmentManager().executePendingTransactions();
      assertNotNull(review.getView());
    }
  }
}
