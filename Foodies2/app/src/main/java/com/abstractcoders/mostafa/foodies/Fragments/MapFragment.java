package com.abstractcoders.mostafa.foodies.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.EateryActivity;
import com.abstractcoders.mostafa.foodies.Handlers.OnTaskCompleted;
import com.abstractcoders.mostafa.foodies.Handlers.PlacesReadFeed;
import com.abstractcoders.mostafa.foodies.Handlers.LocationManager;
import com.abstractcoders.mostafa.foodies.Model.Eatery;
import com.abstractcoders.mostafa.foodies.Model.EateryRenderer;
import com.abstractcoders.mostafa.foodies.Model.GooglePlace;
import com.abstractcoders.mostafa.foodies.Model.GooglePlaceList;
import com.abstractcoders.mostafa.foodies.Model.MapPointer;
import com.abstractcoders.mostafa.foodies.NavigationDrawerActivity;
import com.abstractcoders.mostafa.foodies.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mostafa on 05/03/2016.
 */
public class MapFragment extends Fragment implements OnTaskCompleted {
    public Activity activity;
    public GoogleMap mMap;
    private FloatingActionButton mapFab;
    private MapPointer mapPointer;
    // Declare a variable for the cluster manager.
    public ClusterManager<Eatery> mClusterManager;
    private List<Marker> mapMarkers = new ArrayList<Marker>();

    public MapFragment(Activity activity, GoogleMap mMap) {
        this.activity = activity;
        this.mMap = mMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void initialiseClusterManager()
    {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Eatery>(getContext(), mMap);
        mClusterManager.setRenderer(new EateryRenderer(getContext(),mMap, mClusterManager));

        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<Eatery>() {
            @Override
            public void onClusterItemInfoWindowClick(Eatery eatery) {
                Intent switchToEatery = new Intent(getContext(), EateryActivity.class);
                switchToEatery.putExtra("SelectedEatery", eatery.getPlace());
                ((NavigationDrawerActivity) getContext()).startActivityForResult(switchToEatery, 1);
                getContext().startActivity(switchToEatery);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_layout, container, false);        // Gets the MapView from the XML layout and creates it
        MapView mapView = (MapView) rootView.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.onResume(); //without this, map showed but was empty

        // Gets to GoogleMap from the MapView and does initialization stuff
        mMap = mapView.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        mMap.setMyLocationEnabled(true);
        initialiseClusterManager();
        mapFab = (FloatingActionButton) rootView.findViewById(R.id.trackfab);
        mapFab.setBackgroundColor(Color.parseColor("#930702"));
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(getContext());
        if(LocationManager.getInstance(getContext()).canGetLocation()) {
            LatLng currentLocation = new LatLng(LocationManager.getInstance(getContext()).getLatitude(), LocationManager.getInstance(getContext()).getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 14);
            mMap.animateCamera(cameraUpdate);
        }

        mapFab = (FloatingActionButton)  rootView.findViewById(R.id.trackfab);
        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LocationManager.getInstance(getContext()).canGetLocation()) {
                    LatLng currentLocation = new LatLng(LocationManager.getInstance(getContext()).getLatitude(), LocationManager.getInstance(getContext()).getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 16);
                    mMap.animateCamera(cameraUpdate);
                }
                Toast.makeText(getContext(), "Locking onto location", Toast.LENGTH_SHORT).show();
            }
        });
        mapFab.setBackgroundColor(Color.parseColor("#FF930702"));
        beginSearch();
        if(mapPointer != null) {
            MapsInitializer.initialize(getContext());
            LatLng currentLocation = new LatLng(mapPointer.getPlace().getGeometry().getLocation().getLat(), mapPointer.getPlace().getGeometry().getLocation().getLng());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 18);
            this.mMap.animateCamera(cameraUpdate);
            mMap.addCircle(new CircleOptions()
                    .center(currentLocation)
                    .radius(1.0)
                    .strokeColor(Color.RED)
                    .strokeWidth(4));
            mClusterManager.addItem(new Eatery(mapPointer.getPlace()));
        }

        return rootView;
    }


    public void setMapPointer(MapPointer mapPointer) {
        this.mapPointer = mapPointer;
    }


    public void beginSearch() {
        String placesKey = activity.getResources().getString(R.string.places_key);
        double latitude = LocationManager.getInstance(activity).getLatitude();
        double longitude = LocationManager.getInstance(activity).getLongitude();
        String placesRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude +
                "&radius=500&type=bakery|cafe|food|meal_delivery|meal_takeaway|restaurant|bar&key=" + placesKey;
        PlacesReadFeed process = new PlacesReadFeed(this);
        process.execute(new String[]{placesRequest});
    }


    @Override
    public void onTaskCompleted(GooglePlaceList nearby) {
        final List<GooglePlace> nearbyPlaces = nearby.getResults();
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);

        for (int i = 0; i < nearbyPlaces.size(); i++) {
            final GooglePlace selectedPlace = nearbyPlaces.get(i);
            Eatery offsetItem = new Eatery(selectedPlace);
            mClusterManager.addItem(offsetItem);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (GooglePlace place : nearbyPlaces) {
                    if(marker.getPosition().latitude==place.getGeometry().getLocation().getLat()
                            &&marker.getPosition().longitude==place.getGeometry().getLocation().getLng())
                    {
                        Intent switchToEatery = new Intent(activity, EateryActivity.class);
                        switchToEatery.putExtra("SelectedEatery",place);
                        (activity).startActivityForResult(switchToEatery, 1);
                    }
                }
            }
        });
/*        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (GooglePlace place : nearbyPlaces) {
                    if(marker.getPosition().latitude==place.getGeometry().getLocation().getLat()
                            &&marker.getPosition().longitude==place.getGeometry().getLocation().getLng())
                    {
                        Intent switchToEatery = new Intent(activity, EateryActivity.class);
                        switchToEatery.putExtra("SelectedEatery",place);
                        (activity).startActivityForResult(switchToEatery, 1);
                    }
                }
                return false;
            }
        });*/
    }
}
