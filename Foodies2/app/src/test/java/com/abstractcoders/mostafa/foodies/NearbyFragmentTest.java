package com.abstractcoders.mostafa.foodies;

import android.widget.ListView;

import com.abstractcoders.mostafa.foodies.Fragments.NearbyFragment;
import com.abstractcoders.mostafa.foodies.Model.GooglePlace;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Mustafa
 */
public class NearbyFragmentTest extends TestCase {

    NavigationDrawerActivity navigationDrawerActivity;
    NearbyFragment nearbyFragment;
    String places_query = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
            "location=-33.8670522,151.1957362&radius=500&" +
            "type=restaurant&name=cruise&key=AIzaSyDaQGuys_yJ1nIWrOZq-Rqq9shhyAF9678";

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        navigationDrawerActivity = new NavigationDrawerActivity();
        nearbyFragment = new NearbyFragment("TestFragment",navigationDrawerActivity);
    }

    //asserts that activity and fragment exists
    public void testPreConditions()
    {
        assertNotNull(navigationDrawerActivity);
        assertNotNull(nearbyFragment);
    }

    //asserts that places are actually nearby
    public void testNearby()
    {
        ListView listView = nearbyFragment.getListView();
        Assert.assertTrue(listView.getAdapter().getCount() > 0);
    }

    // asserts that all nearby places are within 500 meters
    public void testPlacesAreNearby()
    {
        ListView listView = nearbyFragment.getListView();
        for(int i = 0 ; i < listView.getAdapter().getCount();i++)
        {
            GooglePlace currentPlace = (GooglePlace) listView.getAdapter().getItem(i);
            double latitude, longitude;
            latitude = currentPlace.getGeometry().getLocation().getLat();
            longitude = currentPlace.getGeometry().getLocation().getLng();
            double distanceInKm = getDistanceFromLatLonInKm(latitude, longitude, -33.8670522,151.1957362);
            if(distanceInKm > 500)
            {
                Assert.assertTrue(distanceInKm < 500);
                break;
            }
        }
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
