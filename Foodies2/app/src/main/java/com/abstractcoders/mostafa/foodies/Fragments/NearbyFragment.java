package com.abstractcoders.mostafa.foodies.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.abstractcoders.mostafa.foodies.NavigationDrawerActivity;
import com.abstractcoders.mostafa.foodies.R;
import com.github.ybq.android.spinkit.SpinKitView;

/**
 * Created by Mostafa on 22/02/2016.
 */
public class NearbyFragment extends Fragment {
    String tabName;
    ListView lv;
    SwipeRefreshLayout refreshLayout;
    View v;
    NavigationDrawerActivity activity;
    public SpinKitView spinKitView;
    public NearbyFragment(String tabName, NavigationDrawerActivity activity) {
        // Required empty public constructor
        this.tabName = tabName;
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if(!activity.isFragmentsSetup){
            View rootView = inflater.inflate(R.layout.list_fragment, container, false);
            refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
            refreshLayout.setRefreshing(true);
            lv = (ListView) rootView.findViewById(R.id.listView);
            v = rootView;
            spinKitView = (SpinKitView) rootView.findViewById(R.id.spin_kit_list);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    activity.nsh.beginSearch();
                    refreshLayout.setRefreshing(false);
                }
            });
            if(!activity.nsh.searched)
            {
                activity.nsh.beginSearch();
            }
            activity.isFragmentsSetup = true;
            refreshLayout.setRefreshing(false);
            return rootView;
        }else
        {
            return v;
        }

    }




    public ListView getListView()
    {
        return lv;
    }
}
