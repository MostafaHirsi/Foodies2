package com.abstractcoders.mostafa.foodies.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by Mustafa on 08/04/2016.
 */
public class EateryRenderer extends DefaultClusterRenderer<Eatery> {
    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;
    private final ImageView mImageView;
    private final ClusterManager<Eatery> mClusterManager;
    private Context context;

    public EateryRenderer(Context context, final GoogleMap mMap, ClusterManager<Eatery> mClusterManager) {
        super(context, mMap, mClusterManager);
        mIconGenerator = new IconGenerator(context);
        mClusterIconGenerator = new IconGenerator(context);
        View multiProfile = ((Activity) context).getLayoutInflater().inflate(R.layout.multi_profile, null);
        mClusterIconGenerator.setContentView(multiProfile);
        multiProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        mImageView = new ImageView(context);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(150, 65));
        mIconGenerator.setContentView(mImageView);
        this.mClusterManager = mClusterManager;
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(Eatery eatery, MarkerOptions markerOptions) {
        mImageView.setImageResource(R.drawable.restaurant_icon);
        if(eatery.getPlace().getTypes().contains("bar")) {
            mImageView.setImageResource(R.drawable.bar);
        }else if(eatery.getPlace().getTypes().contains("cafe")){
            mImageView.setImageResource(R.drawable.cafe);
        }
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setPadding(3, 3, 3, 3);
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(eatery.name);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Eatery> cluster, MarkerOptions markerOptions) {
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }



    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}
