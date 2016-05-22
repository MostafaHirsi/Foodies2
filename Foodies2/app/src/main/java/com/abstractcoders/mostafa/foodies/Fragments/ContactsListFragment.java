package com.abstractcoders.mostafa.foodies.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abstractcoders.mostafa.foodies.R;

/**
 * Created by hirsim2 on 20/03/16.
 */
public class ContactsListFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the list fragment layout
        View rootView = inflater.inflate(R.layout.contact_list_view, container, false);
        return rootView;
    }

}
