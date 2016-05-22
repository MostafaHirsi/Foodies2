package com.abstractcoders.mostafa.foodies.Fragments;

/**
 * Created by Mostafa on 06/01/2016.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.abstractcoders.mostafa.foodies.Adapters.FavouriteListAdapter;
import com.abstractcoders.mostafa.foodies.Handlers.LocationManager;
import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;
import com.abstractcoders.mostafa.foodies.Model.CurrentUserSingleton;
import com.abstractcoders.mostafa.foodies.Model.Favourite;
import com.abstractcoders.mostafa.foodies.NavigationDrawerActivity;
import com.abstractcoders.mostafa.foodies.R;

import java.util.ArrayList;
import java.util.List;


public class FavouritesFragment extends Fragment{
    String tabName;
    ListView lv;
    SwipeRefreshLayout refreshLayout;
    View v;
    String selectedPlaceID;
    NavigationDrawerActivity activity;
    Favourite favourite;


    public FavouritesFragment(String tabName, NavigationDrawerActivity activity, String selectedPlaceID) {
        // Required empty public constructor
        this.tabName = tabName;
        this.activity = activity;
        this.selectedPlaceID = selectedPlaceID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if(!activity.isFragmentsSetup){
            View rootView = inflater.inflate(R.layout.list_fragment, container, false);
            refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
            lv = (ListView) rootView.findViewById(R.id.listView);
            v = rootView;
            updateFavourites();
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateFavourites();
                }
            });
            return rootView;
        }else
        {
            return v;
        }
    }

    public void updateFavourites() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.retrieveFavourites(CurrentUserSingleton.getInstance().getUserName());
        List<Favourite> favourites = new ArrayList<Favourite>();
        FavouriteListAdapter lap = new FavouriteListAdapter(getContext(), R.layout.favourite_eatery_item, favourites, LocationManager.getInstance(getContext()).getLocation());
        lv.setAdapter(lap);
        while (cursor.moveToNext())
        {
            Favourite favourite = new Favourite(cursor.getString(cursor.getColumnIndex("FavouriteID")),cursor.getString(cursor.getColumnIndex("UserID")),
                    cursor.getString(cursor.getColumnIndex("PlaceID")),cursor.getString(cursor.getColumnIndex("PlaceName")), cursor.getString(cursor.getColumnIndex("PhotoDrawable"))
                    , cursor.getFloat(cursor.getColumnIndex("Rating")), cursor.getDouble(cursor.getColumnIndex("Latitude")), cursor.getDouble(cursor.getColumnIndex("Longitude")));
            favourites.add(favourite);
        }
        refreshLayout.setRefreshing(false);
    }

}
