package com.abstractcoders.mostafa.foodies;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.Handlers.LocationManager;
import com.abstractcoders.mostafa.foodies.Handlers.NearbySearchHandler;
import com.abstractcoders.mostafa.foodies.Model.CurrentUserSingleton;
import com.abstractcoders.mostafa.foodies.Model.Eatery;
import com.abstractcoders.mostafa.foodies.Model.GooglePlace;
import com.abstractcoders.mostafa.foodies.Model.GooglePlaceList;
import com.abstractcoders.mostafa.foodies.Model.MapPointer;
import com.abstractcoders.mostafa.foodies.Model.Note;
import com.abstractcoders.mostafa.foodies.Fragments.DealsFragment;
import com.abstractcoders.mostafa.foodies.Fragments.FavouritesFragment;
import com.abstractcoders.mostafa.foodies.Fragments.MapFragment;
import com.abstractcoders.mostafa.foodies.Fragments.NearbyFragment;
import com.abstractcoders.mostafa.foodies.Fragments.NotesFragment;
import com.abstractcoders.mostafa.foodies.Adapters.SectionsPagerAdapter;
import com.abstractcoders.mostafa.foodies.Adapters.ViewPagerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NotesFragment.OnListFragmentInteractionListener {

    private CurrentUserSingleton cus;
    public boolean isFragmentsSetup = false;
    View logo;
    LinearLayout searchview;
    public GoogleMap mMap;
    private LocationManager tracker;
    boolean mapInitialised = false;

    FrameLayout mapViewLayout;
    LinearLayout eateryListLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private CameraPosition currentPosition;
    public NearbySearchHandler nsh;

    private FloatingActionButton mapFab;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private FavouritesFragment firstFragment;
    private NearbyFragment thirdFragment;
    private DealsFragment secondFragment;
    public MapFragment mapFragment;
    private Toolbar toolbar;
    private NotesFragment notesFragment
            ;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        getSupportActionBar().setElevation(0);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mapViewLayout = (FrameLayout) findViewById(R.id.mapLayout);
        eateryListLayout = (LinearLayout) findViewById(R.id.eateriesListLayout);
        searchview = (LinearLayout) findViewById(R.id.searchview);
        searchview.setVisibility(View.GONE);


        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        logo = inflator.inflate(R.layout.menulogo, null);
        logo.setLayoutParams(new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT));

        mapFragment = new MapFragment(this,this.mMap);
        getSupportActionBar().setTitle("");
        toolbar.addView(logo);

        notesFragment = new NotesFragment();



        tracker = new LocationManager(this);
        firstFragment = new FavouritesFragment("FAVOURITES", this, "");
        secondFragment = new DealsFragment("DEALS", this);
        thirdFragment = new NearbyFragment("NEARBY", this);
        initialiseTabs();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        nsh = new NearbySearchHandler(this, tracker, client, false,thirdFragment, mMap);
        CurrentUserSingleton csu = CurrentUserSingleton.getInstance();
        int i = csu.getUserName();
        Log.i("User ID", "User ID is " + i);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            this.getWindow().setStatusBarColor(Color.parseColor("#FF212121"));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initialiseTabs() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setBackgroundColor(Color.parseColor("#930702"));
        tabLayout.setElevation(2);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(firstFragment, "FAVOURITES");
        adapter.addFragment(secondFragment, "DEALS");
        adapter.addFragment(thirdFragment, "NEARBY");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (notesFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(notesFragment);
            ft.commit();
            return;
        }
        if (mapFragment.isVisible())
        {
            showHideMap();
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);

        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.searchbtbn) {
            Intent i = new Intent(NavigationDrawerActivity.this, SearchActivity.class);
            startActivity(i);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.mapmarkerbtn) {
            showHideMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MapFragment showHideMap()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(!mapInitialised) {
            searchview.setVisibility(View.GONE);
            eateryListLayout.setVisibility(View.GONE);
            mapFragment.setMapPointer(null);
            ft.add(R.id.contentview, mapFragment);
            ft.commit();
            ActionMenuItemView mapmarkerBtn = (ActionMenuItemView) findViewById(R.id.mapmarkerbtn);
            mapmarkerBtn.setIcon(getDrawable(R.drawable.listicon));
            mapInitialised = true;
        }else
        {
            eateryListLayout.setVisibility(View.VISIBLE);
            ActionMenuItemView mapmarkerBtn = (ActionMenuItemView) findViewById(R.id.mapmarkerbtn);
            mapmarkerBtn.setIcon(getDrawable(R.drawable.mapwhitelarge));
            ft.remove(mapFragment);
            ft.commit();
            mapInitialised = false;
        }
        return mapFragment;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if (id == R.id.nav_manage) {
            // Handle the camera action
        }else if(id == R.id.nav_eateries)
        {
            ft.show(firstFragment);
            ft.remove(notesFragment);
            ft.commit();
        }else if (id == R.id.nav_photos) {

        } else if (id == R.id.nav_notes) {
            if(!fragments.contains(notesFragment)) {
                ft.add(R.id.contentview, notesFragment);
                ft.commit();
                ft.show(notesFragment);
                searchview.setVisibility(View.GONE);
            }
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_meetups) {

        } else if (id == R.id.nav_app_settings) {
            Intent appSettingsActivity = new Intent(NavigationDrawerActivity.this, AppSettingsActivity.class);
            startActivity(appSettingsActivity);
        } else if (id == R.id.nav_account_settings) {
            Intent accountSettingsActivity = new Intent(NavigationDrawerActivity.this, AccountSettingsActivity.class);
            startActivity(accountSettingsActivity);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "NavigationDrawer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.abstractcoders.mostafa.foodies/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "NavigationDrawer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.abstractcoders.mostafa.foodies/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                MapPointer mp = (MapPointer) data.getSerializableExtra("SelectedPlaceMap");
                MapFragment mapFragment = showHideMap();
                mapFragment.setMapPointer(mp);
                Toast.makeText(NavigationDrawerActivity.this, "Focusing on " + mp.getPlace().getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(Note item) {
        Intent i = new Intent(this.getBaseContext(), NoteActivity.class);
        i.putExtra("SelectedNote",item);
        startActivity(i);
    }

}
