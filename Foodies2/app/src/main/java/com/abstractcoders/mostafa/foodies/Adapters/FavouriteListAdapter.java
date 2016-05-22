package com.abstractcoders.mostafa.foodies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.EateryActivity;
import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;
import com.abstractcoders.mostafa.foodies.Model.Favourite;
import com.abstractcoders.mostafa.foodies.NavigationDrawerActivity;
import com.abstractcoders.mostafa.foodies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mostafa on 04/03/2016.
 */
public class FavouriteListAdapter extends ArrayAdapter<Favourite> implements View.OnClickListener {

    ImageView eateryView;
    List<Favourite> favourites;
    View itemView;
    Favourite selectedFavourite;
    public FavouriteListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public FavouriteListAdapter(Context context, int resource, List<Favourite> items) {
        super(context, resource, items);
    }

    public FavouriteListAdapter(Context context, int resource, List<Favourite> items, Location location) {
        super(context, resource, items);
        this.favourites = favourites;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final Favourite p = getItem(position);
        selectedFavourite = p;
        final DatabaseHelper db = new DatabaseHelper(this.getContext());
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            itemView = vi.inflate(R.layout.favourite_eatery_item, null);
        }
        this.itemView = itemView;
        itemView.setClickable(true);
        eateryView = (ImageView) itemView.findViewById(R.id.eateryImage);
        ImageButton deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteBtn);
        
        eateryView.setImageResource(R.drawable.placeholder_banner);
        if(!p.getPhotoUrl().equals(""))
        {
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + p.getPhotoUrl() + "&key=AIzaSyDaQGuys_yJ1nIWrOZq-Rqq9shhyAF9678";

            Picasso.with(this.getContext()).load(url).into(eateryView);
        }else
        {
            eateryView.setImageResource(R.drawable.placeholder_banner);
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) eateryView.getLayoutParams();
        final float scale = eateryView.getContext().getResources().getDisplayMetrics().density;
        int hpixels = (int) (80 * scale + 0.5f);
        int wpixels = (int) (80 * scale + 0.5f);
        layoutParams.width = hpixels;
        layoutParams.height = wpixels;
        eateryView.setLayoutParams(layoutParams);
        eateryView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (p != null) {
            TextView eateryName = (TextView) itemView.findViewById(R.id.id);
            RatingBar ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            if (eateryName != null) {
                if(p.getPlaceName().length() < 15)
                {
                    eateryName.setText(p.getPlaceName());
                }
                else
                {
                    eateryName.setText(p.getPlaceName().substring(0,13) + "...");
                }
            }

            if(ratingBar != null)
            {
                ratingBar.setRating(p.getRating());
            }
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchToEatery = new Intent(getContext(), EateryActivity.class);
                switchToEatery.putExtra("SelectedFavourite", p);
                ((NavigationDrawerActivity)getContext()).startActivityForResult(switchToEatery, 1);
            }
        });

        deleteBtn.setOnClickListener(this);

        return itemView;
    }

    @Override
    public void onClick(View v) {
        final View item = (View) v.getParent();
        Animation anim = AnimationUtils.loadAnimation(
                v.getContext(), android.R.anim.slide_out_right
        );
        anim.setDuration(500);
        item.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.removeFavourite(selectedFavourite.getFavouriteID());
                Toast.makeText(getContext(), "Favourite place deleted", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                item.setVisibility(View.GONE);

            }
        }, anim.getDuration());
    }


}
