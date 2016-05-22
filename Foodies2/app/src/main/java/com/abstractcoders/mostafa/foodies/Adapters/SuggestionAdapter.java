package com.abstractcoders.mostafa.foodies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abstractcoders.mostafa.foodies.R;

import java.util.List;

/**
 * Created by Mustafa on 08/04/2016.
 */
public class SuggestionAdapter extends CursorAdapter {

    private List<String> items;

    private TextView text;

    public SuggestionAdapter(Context context, Cursor cursor, List<String> items) {

        super(context, cursor, false);

        this.items = items;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        text.setText(items.get(cursor.getPosition()));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.suggestion_item, parent, false);

        text = (TextView) view.findViewById(R.id.text);

        return view;

    }

}