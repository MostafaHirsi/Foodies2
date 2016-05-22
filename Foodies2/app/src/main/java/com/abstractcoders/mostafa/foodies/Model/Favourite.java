package com.abstractcoders.mostafa.foodies.Model;

import java.io.Serializable;

/**
 * Created by hirsim2 on 18/03/16.
 */
public class Favourite implements Serializable{
    String favouriteID;
    String userID;
    String placeID;
    String placeName;
    String photoUrl;
    float rating;
    double latitude;
    double longitude;

    public Favourite(String favouriteID, String userID, String placeID, String placeName, String photoUrl, float rating, double latitude, double longitude) {
        this.favouriteID = favouriteID;
        this.userID = userID;
        this.placeID = placeID;
        this.placeName = placeName;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFavouriteID() {
        return favouriteID;
    }

    public void setFavouriteID(String favouriteID) {
        this.favouriteID = favouriteID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
