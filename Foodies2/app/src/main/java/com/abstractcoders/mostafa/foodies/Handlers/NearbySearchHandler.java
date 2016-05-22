package com.abstractcoders.mostafa.foodies.Handlers;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.abstractcoders.mostafa.foodies.Adapters.ListAdapter;
import com.abstractcoders.mostafa.foodies.EateryActivity;
import com.abstractcoders.mostafa.foodies.Model.GooglePlace;
import com.abstractcoders.mostafa.foodies.Model.GooglePlaceList;
import com.abstractcoders.mostafa.foodies.NavigationDrawerActivity;
import com.abstractcoders.mostafa.foodies.R;
import com.abstractcoders.mostafa.foodies.Fragments.NearbyFragment;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

/**
 * Created by Mostafa on 06/02/2016.
 */
public class NearbySearchHandler implements OnTaskCompleted{

    private NavigationDrawerActivity sourceActivity;
    private LocationManager tracker;
    private GoogleApiClient client;
    public boolean searched = false;
    private NearbyFragment nearbyFragment;
    public GoogleMap mMap;
    SwipeRefreshLayout refreshLayout;

    public NearbySearchHandler(NavigationDrawerActivity sourceActivity, LocationManager tracker, GoogleApiClient client, boolean searched, NearbyFragment nearbyFragment, GoogleMap map) {
        this.sourceActivity = sourceActivity;
        this.tracker = tracker;
        this.client = client;
        this.searched = searched;
        this.nearbyFragment = nearbyFragment;
        this.mMap = map;
    }

    public void beginSearch() {
        nearbyFragment.spinKitView.setVisibility(View.VISIBLE);
        String placesKey = sourceActivity.getResources().getString(R.string.places_key);
        double latitude = tracker.getLatitude();
        double longitude = tracker.getLongitude();
        String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                 "&radius=500&type=bakery|cafe|food|meal_delivery|meal_takeaway|restaurant|bar&key=" + placesKey;
        com.abstractcoders.mostafa.foodies.Handlers.PlacesReadFeed process = new com.abstractcoders.mostafa.foodies.Handlers.PlacesReadFeed(NearbySearchHandler.this);
        process.execute(new String[]{placesRequest});


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(sourceActivity).addApi(AppIndex.API).build();
    }

    @Override
    public void onTaskCompleted(GooglePlaceList list) {
        reportBack(list);
    }

    private void reportBack(GooglePlaceList nearby) {

        if(nearby != null) {
            final List<GooglePlace> nearbyPlaces = nearby.getResults();

            refreshLayout = (SwipeRefreshLayout) sourceActivity.findViewById(R.id.swipe_container);
            ListAdapter customAdapter = new ListAdapter(sourceActivity, R.layout.nearbyeateryitem, nearbyPlaces, tracker.getLocation());

            if (nearbyFragment.getListView() != null) {
                refreshLayout.setRefreshing(true);
                nearbyFragment.getListView().setAdapter(customAdapter);
                nearbyFragment.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("ItemClickListener", "Item clicked on");
                        GooglePlace selectedItem = (GooglePlace) parent.getAdapter().getItem(position);
                        Intent switchToEatery = new Intent(sourceActivity, EateryActivity.class);
                        switchToEatery.putExtra("SelectedEatery", selectedItem);
                        sourceActivity.startActivity(switchToEatery);
                    }
                });
            }
            searched = true;
            refreshLayout.setRefreshing(false);
            nearbyFragment.spinKitView.setVisibility(View.GONE);
        }
    }

}
