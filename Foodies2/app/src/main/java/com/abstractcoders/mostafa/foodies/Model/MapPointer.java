package com.abstractcoders.mostafa.foodies.Model;

import java.io.Serializable;

/**
 * Created by Mostafa on 08/03/2016.
 */
public class MapPointer implements Serializable {
    private GooglePlace place;

    public MapPointer(GooglePlace place) {
        this.place = place;
    }

    public GooglePlace getPlace() {
        return place;
    }

    public void setPlace(GooglePlace place) {
        this.place = place;
    }
}
