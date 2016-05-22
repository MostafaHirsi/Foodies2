package com.abstractcoders.mostafa.foodies.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mostafa on 26/02/2016.
 */
public class SearchOptions implements Serializable{

    String[] dietary;
    String sortBy;
    int distance;
    boolean openNow, delivery;
    List<String> types;

    public SearchOptions(String[] dietary, String sortBy, int distance, boolean openNow, boolean delivery, List<String> types) {
        this.dietary = dietary;
        this.sortBy = sortBy;
        this.distance = distance;
        this.openNow = openNow;
        this.delivery = delivery;
        this.types = types;
    }

    public String[] getDietary() {
        return dietary;
    }

    public void setDietary(String[] dietary) {
        this.dietary = dietary;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
