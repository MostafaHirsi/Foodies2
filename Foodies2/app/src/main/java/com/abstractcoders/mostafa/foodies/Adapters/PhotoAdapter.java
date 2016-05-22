package com.abstractcoders.mostafa.foodies.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import com.abstractcoders.mostafa.foodies.Model.GooglePlaceDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mustafa on 04/04/2016.
 */
public class PhotoAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private int itemBackground;
    private List<GooglePlaceDetails.Photo> photos;

    public PhotoAdapter(Context c, List<GooglePlaceDetails.Photo> photos)
    {
        context = c;
        this.photos = photos;
    }

    // returns the number of images
    public int getCount() {
        return photos.size();
    }
    // returns the ID of an item
    public Object getItem(int position) {
        return position;
    }
    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        final String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photos.get(position).getPhoto_reference() + "&key=AIzaSyDaQGuys_yJ1nIWrOZq-Rqq9shhyAF9678";

          Picasso.with(context).load(url).into(imageView);
        return imageView;
    }
}