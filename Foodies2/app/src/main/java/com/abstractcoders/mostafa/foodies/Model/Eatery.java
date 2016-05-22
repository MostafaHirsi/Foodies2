package com.abstractcoders.mostafa.foodies.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Mostafa on 05/03/2016.
 */
public class Eatery implements ClusterItem {
    public final String name;
    public final String icon;
    public final float rating;
    private final LatLng mPosition;
    private final GooglePlace place;



    public Eatery(GooglePlace place) {
        this.name = place.getName();
        icon = place.getUrl();
        mPosition = new LatLng(place.getGeometry().getLocation().getLat(),place.getGeometry().getLocation().getLng());
        this.rating = place.getRating();
        this.place = place;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public float getRating() {
        return rating;
    }

    public LatLng getmPosition() {
        return mPosition;
    }

    public GooglePlace getPlace() {
        return place;
    }
}
