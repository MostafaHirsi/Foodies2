package com.abstractcoders.mostafa.foodies;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.abstractcoders.mostafa.foodies.Adapters.ListAdapter;
import com.abstractcoders.mostafa.foodies.Handlers.LocationManager;
import com.abstractcoders.mostafa.foodies.Handlers.OnTaskCompleted;
import com.abstractcoders.mostafa.foodies.Handlers.PlacesReadFeed;
import com.abstractcoders.mostafa.foodies.Model.GooglePlace;
import com.abstractcoders.mostafa.foodies.Model.GooglePlaceList;
import com.abstractcoders.mostafa.foodies.Model.SearchOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnTaskCompleted {

    private LocationManager tracker;
    private SearchOptions searchOptions;
    private List<String> suggestions;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Toolbar toolbar;
    private Handler mHandler = new Handler();
    private SearchView se;
    private SpinKitView loadingImage;
    private TextView failedSearchText;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        suggestions = new ArrayList<>();
        tracker = new LocationManager(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout ll = (LinearLayout) toolbar.getChildAt(0);
        LinearLayout searchView = (LinearLayout) ll.getChildAt(0);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        loadingImage = (SpinKitView) findViewById(R.id.spin_kit);
        loadingImage.setVisibility(View.GONE);
        se = (SearchView) findViewById(R.id.search_query);
        se.setQueryHint("'Cafes in Birmingham'");
        se.requestFocus();
        se.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                suggestions.add(query);
                beginSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadHistory(newText);
                return false;
            }
        });
        failedSearchText = (TextView) findViewById(R.id.failedSearchText);
        failedSearchText.setVisibility(View.GONE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            this.getWindow().setStatusBarColor(Color.parseColor("#FF212121"));
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }


    // History
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadHistory(String query) {
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {*/


        final MatrixCursor cursor = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<suggestions.size(); i++) {
            if (suggestions.get(i).toLowerCase().startsWith(query.toLowerCase()))
                cursor.addRow(new Object[] {i, suggestions.get(i)});
        }
            final String[] from = new String[] {"cityName"};
            final int[] to = new int[] {android.R.id.text1};

        android.widget.SimpleCursorAdapter sca = new android.widget.SimpleCursorAdapter(getBaseContext(),
                    R.layout.suggestion_item,
                    null,
                    from,
                    to,
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            se.setSuggestionsAdapter(sca);
            sca.changeCursor(cursor);

    }

    private void beginSearch(String filter) {
        loadingImage.setVisibility(View.VISIBLE);
        String placesKey = this.getResources().getString(R.string.places_key);
        String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                tracker.getLatitude() + "," + tracker.getLongitude() + "&type=bakery|cafe|food|meal_delivery|meal_takeaway|restaurant|bar" + "&keyword=";

        filter = filter.replace(' ', '+');
        if (!filter.equals("") || !filter.isEmpty()) {
            placesRequest += filter;
        }
        if (searchOptions != null) {
            placesRequest += "&radius=" + (searchOptions.getDistance()*1000);
        } else {
            placesRequest += "&radius=10000";
        }
        placesRequest += "&key=" + placesKey;
        PlacesReadFeed process = new PlacesReadFeed(SearchActivity.this);
        process.execute(new String[]{placesRequest});

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void reportBack(GooglePlaceList nearby) {
        loadingImage.setVisibility(View.GONE);
        if (nearby.getResults() != null && nearby.getResults().size() != 0) {
            failedSearchText.setVisibility(View.GONE);
            ListView yourListView = (ListView) findViewById(R.id.itemListView);
            List<GooglePlace> nearbyPlaces = nearby.getResults();
            ListAdapter customAdapter = new ListAdapter(this, R.layout.itemlistrow, nearbyPlaces, LocationManager.getInstance(this).getLocation());
            yourListView.setAdapter(customAdapter);
        } else {
            failedSearchText.setVisibility(View.VISIBLE);
            ListView yourListView = (ListView) findViewById(R.id.itemListView);
            yourListView.setAdapter(null);
        }
    }

    @Override
    public void onTaskCompleted(GooglePlaceList list) {
        reportBack(list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            if (item.getTitle() != null && item.getTitle().equals("SEARCHOPTIONS")) {
                Intent i = new Intent(SearchActivity.this, SearchOptionsActivity.class);
                if (searchOptions != null) {
                    i.putExtra("SearchOptions", searchOptions);
                }
                startActivityForResult(i, 1);
            } else {
                finish();
            }
            return super.onOptionsItemSelected(item);
        } else {
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                searchOptions = (SearchOptions) data.getSerializableExtra("SearchOptions");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Search Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.abstractcoders.mostafa.foodies/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Search Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.abstractcoders.mostafa.foodies/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }
}
