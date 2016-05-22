package com.abstractcoders.mostafa.foodies;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class IntroActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        this.overridePendingTransition(R.animator.slide_down_view,
                R.animator.slide_up_view);
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.titles);
        titleIndicator.setViewPager(mViewPager);


        BootstrapButton signInBtn, logInBtn;
        signInBtn = (BootstrapButton) findViewById(R.id.signUpBtn);
        logInBtn = (BootstrapButton) findViewById(R.id.loginBtn2);
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getBaseContext());
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroActivity.this, LoginPageActivity.class);
                startActivity(i);
            }
        });
        if(!databaseHelper.getUser("hirsim2","blabla").moveToFirst())
        {
            databaseHelper.insertExampleUser();
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            this.getWindow().setStatusBarColor(Color.parseColor("#FF212121"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        boolean isSetup = false;
        View v = null;
        private static final String ARG_SECTION_NUMBER = "section_number";
        int sectionNumber;
        public PlaceholderFragment(int sectionNumber) {
            this.sectionNumber = sectionNumber;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment(sectionNumber);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = new View(container.getContext());
            ImageView im;
            TextView fragmentText;
            if (!isSetup)
            {
                switch(sectionNumber)
                {
                    case 1:
                        rootView = inflater.inflate(R.layout.fragment_intro, container, false);
                        im = (ImageView) rootView.findViewById(R.id.imageView7);
                        im.setImageResource(R.drawable.mobile_banner);
                        fragmentText = (TextView) rootView.findViewById(R.id.fragmentTitle);
                        fragmentText.setText("Check out your favourite eateries");
                        break;
                    case 2:
                        rootView = inflater.inflate(R.layout.fragment_intro, container, false);
                        im = (ImageView) rootView.findViewById(R.id.imageView7);
                        im.setImageResource(R.drawable.mobile_banner_list);
                        fragmentText = (TextView) rootView.findViewById(R.id.fragmentTitle);
                        fragmentText.setText("See what's selling nearby");
                        break;
                    case 3:
                        rootView = inflater.inflate(R.layout.fragment_intro, container, false);
                        im = (ImageView) rootView.findViewById(R.id.imageView7);
                        im.setImageResource(R.drawable.mobile_banner_map);
                        fragmentText = (TextView) rootView.findViewById(R.id.fragmentTitle);
                        fragmentText.setText("Explore your surroundings");
                        break;
                }
                isSetup = true;
                v = rootView;
                return rootView;
            }else
            {
                return v;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        int resource;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
