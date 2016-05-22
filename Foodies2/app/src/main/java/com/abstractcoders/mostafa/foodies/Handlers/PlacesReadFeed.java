package com.abstractcoders.mostafa.foodies.Handlers;

import android.os.AsyncTask;
import android.util.Log;

import com.abstractcoders.mostafa.foodies.Model.GooglePlaceList;
import com.google.gson.Gson;

/**
 * Created by Mustafa on 05/04/2016.
 */
public class PlacesReadFeed extends AsyncTask<String, Void, GooglePlaceList> {
    GooglePlaceList placeList = null;
    OnTaskCompleted listener;

    public PlacesReadFeed(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected GooglePlaceList doInBackground(String... urls) {
        try {
            String referer = null;
            //dialog.setMessage("Fetching Places Data");
            if (urls.length == 1) {
                referer = null;
            } else {
                referer = urls[1];
            }
            String input = GooglePlacesUtility.readGooglePlaces(urls[0], null);
            Gson gson = new Gson();
            GooglePlaceList places = gson.fromJson(input, GooglePlaceList.class);
            Log.i("PLACES_EXAMPLE", "Number of places found is " + places.getResults().size());
            return places;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("PLACES_EXAMPLE", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(GooglePlaceList places) {
        reportBack(places);
    }


    private void reportBack(GooglePlaceList nearby) {
        listener.onTaskCompleted(nearby);
    }
}