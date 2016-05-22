package com.abstractcoders.mostafa.foodies.Handlers;

import com.abstractcoders.mostafa.foodies.Model.GooglePlace;
import com.abstractcoders.mostafa.foodies.Model.GooglePlaceList;

/**
 * Created by Mustafa on 05/04/2016.
 */
public interface OnTaskCompleted {
    public void onTaskCompleted(GooglePlaceList list);
}