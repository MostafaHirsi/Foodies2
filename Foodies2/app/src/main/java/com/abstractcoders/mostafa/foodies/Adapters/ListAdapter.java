package com.abstractcoders.mostafa.foodies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abstractcoders.mostafa.foodies.EateryActivity;
import com.abstractcoders.mostafa.foodies.Model.GooglePlace;
import com.abstractcoders.mostafa.foodies.Model.GooglePlaceList;
import com.abstractcoders.mostafa.foodies.R;
import com.squareup.picasso.Picasso;

import java.util.List;
/**
 * Created by Mostafa on 29/01/2016.
 */
public class ListAdapter extends ArrayAdapter<GooglePlace> {
    Location currentLocation;
    public String photoUrl;
    ImageView eateryView;
    String viewID;

    public ListAdapter(Context context, int resource, List<GooglePlace> items) {
        super(context, resource, items);
    }

    public ListAdapter(Context context, int resource, List<GooglePlace> items, Location location) {
        super(context, resource, items);
        currentLocation = location;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.nearbyeateryitem, null);
        }


        eateryView = (ImageView) v.findViewById(R.id.eateryImage);

        final GooglePlace p = getItem(position);
        viewID = p.getPlace_id();
        if (p.getPhotos() != null && p.getPhotos().size() > 0) {
            if (p.getPhotos().size() > 0) {
                final String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + p.getPhotos().get(0).getPhoto_reference() + "&key=AIzaSyDaQGuys_yJ1nIWrOZq-Rqq9shhyAF9678";
/*                PlacesReadImage task = new PlacesReadImage();
                task.execute(new String[]{url});*/
                Picasso.with(getContext()).load(url).into(eateryView);
            }
        } else {
            eateryView.setImageResource(R.drawable.placeholder_restaurant);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) eateryView.getLayoutParams();
        final float scale = eateryView.getContext().getResources().getDisplayMetrics().density;
        int hpixels = (int) (80 * scale + 0.5f);
        int wpixels = (int) (80 * scale + 0.5f);
        layoutParams.width = hpixels;
        layoutParams.height = wpixels;
        eateryView.setLayoutParams(layoutParams);
        eateryView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        double latitude, longitude;
        latitude = p.getGeometry().getLocation().getLat();
        longitude = p.getGeometry().getLocation().getLng();
        if (latitude != 0 & longitude != 0) {
            if (p != null) {
                TextView eateryName = (TextView) v.findViewById(R.id.id);
                RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
                TextView distanceTxt = (TextView) v.findViewById(R.id.distanceTxt);

                if (eateryName != null) {
                    if(p.getName().length() <= 15)
                    {
                        eateryName.setText(p.getName());
                    }
                    else
                    {
                        eateryName.setText(p.getName().substring(0,14) + "...");
                    }
                }


                if(currentLocation != null) {
                    double distanceInKm = getDistanceFromLatLonInKm(latitude, longitude, currentLocation.getLatitude(), currentLocation.getLongitude());
                    if (distanceTxt != null) {
                        if (distanceInKm > 1) {
                            ((TextView) v.findViewById(R.id.distanceTxt)).setText(String.valueOf(distanceInKm).substring(0,4)+ "km");
                        } else {
                            distanceInKm = distanceInKm * 1000;
                            distanceTxt.setText((int) distanceInKm + "m");
                        }
                    }
                }

                if(ratingBar != null)
                {
                    ratingBar.setRating(p.getRating());
                }



            }
        }
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent switchToEatery = new Intent(getContext(), EateryActivity.class);
                    switchToEatery.putExtra("SelectedEatery", p);
                    ((Activity) getContext()).startActivityForResult(switchToEatery, 1);
                }
            });

        return v;
    }

    double getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
}