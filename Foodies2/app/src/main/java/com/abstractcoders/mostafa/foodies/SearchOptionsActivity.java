package com.abstractcoders.mostafa.foodies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.Model.SearchOptions;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.List;

public class SearchOptionsActivity extends AppCompatActivity {

    CheckBox openNow,deliver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Search Options");

        // Spinner Drop down elements
        final SearchOptions types = (SearchOptions) getIntent().getSerializableExtra("SearchOptions");
        LinearLayout typesList = (LinearLayout) findViewById(R.id.horizontal_types);
        final LinearLayout ll = (LinearLayout) typesList.getChildAt(0);
        final LinearLayout ll2 = (LinearLayout) typesList.getChildAt(1);
        for (int i = 0; i < ll.getChildCount(); i++) {
            CheckBox cbx =  (CheckBox) ll.getChildAt(i);
            String s = (String) cbx.getText();
            s = s.replace(" ", "_");
            s = s.toLowerCase();
            if(types != null && types.getTypes() != null && types.getTypes().contains(s))
            {
                cbx.setChecked(true);
            }
        }
        for (int i = 0; i < ll2.getChildCount(); i++) {
            CheckBox cbx =  (CheckBox) ll2.getChildAt(i);
            String s = (String) cbx.getText();
            s = s.replace(" ", "_");
            s = s.toLowerCase();
            if(types != null && types.getTypes() != null && types.getTypes().contains(s))
            {
                cbx.setChecked(true);
            }
        }
        final SeekBar distanceSeek = (SeekBar) findViewById(R.id.seekBar);
        final TextView distanceKM = (TextView) findViewById(R.id.distanceKM);
        distanceSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Toast.makeText(getBaseContext(),progress +"km",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distanceKM.setText(seekBar.getProgress() + "km");
            }
        });

        openNow = (CheckBox) findViewById(R.id.openCbx);
        deliver = (CheckBox) findViewById(R.id.dlvCbx);


        BootstrapButton saveOptionsBtn = (BootstrapButton) findViewById(R.id.save_options_btn);
        saveOptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedTypes= new ArrayList<String>();
                for(int i = 0; i < ll.getChildCount();i++)
                {
                    CheckBox cbx = (CheckBox) ll.getChildAt(i);
                    if(cbx.isChecked())
                    {
                        String ab = (String) cbx.getText();
                        ab = ab.replace(" ", "_");
                        ab = ab.toLowerCase();
                        selectedTypes.add(ab);
                    }
                }
                SearchOptions so = new SearchOptions(new String[3],"", distanceSeek.getProgress(), openNow.isChecked(), deliver.isChecked(), selectedTypes);
                Intent intent = new Intent();
                intent.putExtra("SearchOptions",so);
                setResult(RESULT_OK, intent);
                Toast.makeText(getBaseContext(),"Search settings saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
