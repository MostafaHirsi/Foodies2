package com.abstractcoders.mostafa.foodies.Model;

import java.util.List;

/**
 * Created by Mostafa on 07/02/2016.
 */
public class GoogleDetailList {
    private List<GooglePlace> results;

    public List<GooglePlace> getResults() {
        return results;
    }

    public void setResults(List<GooglePlace> results) {
        this.results = results;
    }
}
