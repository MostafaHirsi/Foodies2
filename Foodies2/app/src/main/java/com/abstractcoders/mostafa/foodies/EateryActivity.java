package com.abstractcoders.mostafa.foodies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.Adapters.PhotoAdapter;
import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;
import com.abstractcoders.mostafa.foodies.Handlers.GooglePlacesUtility;
import com.abstractcoders.mostafa.foodies.Model.Contact;
import com.abstractcoders.mostafa.foodies.Model.CurrentUserSingleton;
import com.abstractcoders.mostafa.foodies.Model.Favourite;
import com.abstractcoders.mostafa.foodies.Model.GooglePlace;
import com.abstractcoders.mostafa.foodies.Model.GooglePlaceDetails;
import com.abstractcoders.mostafa.foodies.Model.MapPointer;
import com.abstractcoders.mostafa.foodies.Model.Note;
import com.abstractcoders.mostafa.foodies.Model.PlaceDetail;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

public class EateryActivity extends AppCompatActivity {

    Favourite selectedFavourite;
    GooglePlace selectedGooglePlace;
    private GoogleApiClient client;
    TextView description, eateryTitle, addressText, webAddressText;
    ImageView openTag;
    EditText dietary;
    public Date meetUp;
    ImageLoader imageLoader;
    public boolean placeDetailsFound = false;
    String placeId = "";
    String placeName = "";

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eatery);
        setTitle("Eatery");
        selectedFavourite = (Favourite) getIntent().getSerializableExtra("SelectedFavourite");
        selectedGooglePlace = (GooglePlace) getIntent().getSerializableExtra("SelectedEatery");
        eateryTitle = (TextView) findViewById(R.id.eateryTitle);
        addressText = (TextView) findViewById(R.id.addressText);
        webAddressText = (TextView) findViewById(R.id.webAddressText);
        openTag = (ImageView) findViewById(R.id.open_tag);
        dietary = (EditText) findViewById(R.id.dietaryTxt);
        if (selectedGooglePlace == null) {
            setTitle(selectedFavourite.getPlaceName());
            eateryTitle.setText(selectedFavourite.getPlaceName());
            placeName = selectedFavourite.getPlaceName();
            placeId = selectedFavourite.getPlaceID();
        } else {
            setTitle(selectedGooglePlace.getName());
            eateryTitle.setText(selectedGooglePlace.getName());
            placeId = selectedGooglePlace.getPlace_id();
            placeName = selectedGooglePlace.getName();
        }

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = 500;
        beginSearch();
        meetUp = new Date();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        Button shareBtn = (Button) findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestShare();
            }
        });

        Button meetupBtn = (Button) findViewById(R.id.meetup_btn);
        meetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, 2);*/
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Button noteBtn = (Button) findViewById(R.id.notes_btn);
        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),NoteActivity.class);
                CurrentUserSingleton csu = CurrentUserSingleton.getInstance();

                Note item = new Note(Integer.toString(csu.getUserName()),"","","",placeId,placeName);
                i.putExtra("SelectedNote",item);
                startActivity(i);
            }
        });
        imageLoader = ImageLoader.getInstance();
    }

    private void requestShare() {
        Intent i = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, 1);
    }

    private void requestMeet() {
        Intent i = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, 2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eaterymenu, menu);

        MenuItem favItem = menu.getItem(0);
        DatabaseHelper db = new DatabaseHelper(getBaseContext());
        int userID = CurrentUserSingleton.getInstance().getUserName();
        boolean favourited;
        if (selectedFavourite != null) {
            favourited = db.retrieveFavourite(userID, selectedFavourite.getPlaceID());
        } else {
            favourited = db.retrieveFavourite(userID, selectedGooglePlace.getPlace_id());
        }
        if (favourited) {
            favItem.setIcon(R.drawable.heart_white);
        } else {
            favItem.setIcon(R.drawable.heart_black);
        }
        return true;
    }


    public void beginSearch() {
        String placesKey = this.getResources().getString(R.string.places_key);
        if (placesKey.equals("PUT YOUR KEY HERE")) {
            Toast.makeText(this, "You haven't entered your Google Places Key into the strings file.  Dont forget to set a referer too.", Toast.LENGTH_LONG).show();
        } else {
            String detailsRequest = "";
            if (selectedFavourite != null) {
                detailsRequest = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + selectedFavourite.getPlaceID() + "&key=" + placesKey;
            } else {
                detailsRequest = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + selectedGooglePlace.getPlace_id() + "&key=" + placesKey;
            }
            PlacesDetailReadFeed process = new PlacesDetailReadFeed();
            process.execute(new String[]{detailsRequest});
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Eatery Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.abstractcoders.mostafa.foodies/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Eatery Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.abstractcoders.mostafa.foodies/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }

    private class PlacesDetailReadFeed extends AsyncTask<String, Void, PlaceDetail> {
        private final ProgressDialog dialog = new ProgressDialog(EateryActivity.this);
        String output;

        @Override
        protected PlaceDetail doInBackground(String... urls) {
            try {
                //dialog.setMessage("Fetching Places Data");
                String referer = null;
                //dialog.setMessage("Fetching Places Data");
                if (urls.length == 1) {
                    referer = null;
                } else {
                    referer = urls[1];
                }
                String input = GooglePlacesUtility.readGooglePlaces(urls[0], referer);
                Gson gson = new Gson();
                output = input;
                PlaceDetail place = gson.fromJson(input, PlaceDetail.class);
                Log.i("PLACES EXAMPLE", "Place found is " + place.toString());
                return place;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("PLACES EXAMPLE", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Getting place details...");
            this.dialog.show();

        }

        @Override
        protected void onPostExecute(PlaceDetail placeDetail) {
            try {
                reportBack(placeDetail.getResult(), output);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.dialog.dismiss();
        }
    }

    private void reportBack(GooglePlace place, String output) throws JSONException, InterruptedException {
        selectedGooglePlace = place;
        GooglePlaceDetails gpl = new GooglePlaceDetails(output, place);
        if(gpl.getPhotos() == null)
        {
            LinearLayout photosLayout = (LinearLayout) findViewById(R.id.photosLayout);
            photosLayout.setVisibility(View.GONE);
        }else
        {

            Gallery gallery = (Gallery) findViewById(R.id.gallery);
            gallery.setAdapter(new PhotoAdapter(this, gpl.getPhotos()));
            gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                               @Override
                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                   zoomImageFromThumb((ImageView)view,((ImageView) view).getDrawable());
                                               }
                                           });
            if(gpl.getPhotos() != null && gpl.getPhotos().size() > 0) {
                ImageView banner = (ImageView) findViewById(R.id.eateryBanner);
                String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + gpl.getPhotos().get(0).getPhoto_reference() + "&key=AIzaSyDaQGuys_yJ1nIWrOZq-Rqq9shhyAF9678";
                Picasso.with(this).load(url).into(banner);
            }
        }

        LinearLayout openingHours = (LinearLayout) findViewById(R.id.opening_hours_layout);
        for (String openingHour: gpl.getOpeningTimes()) {
            TextView openingHourTxt = new TextView(this);
            openingHourTxt.setText(openingHour);
            openingHourTxt.setTextColor(Color.BLACK);
            openingHours.addView(openingHourTxt);
        }
        addressText.setText(gpl.getFormatted_address());
        webAddressText.setText("Website: " + gpl.getWebsite());
        if (gpl.getIsOpen()) {
            openTag.setVisibility(View.VISIBLE);
        } else {
            openTag.setVisibility(View.VISIBLE);
            openTag.setImageResource(R.drawable.closed_sign);
        }
        if(place.getReviews() != null) {
            LinearLayout reviewLinearLayout = (LinearLayout) findViewById(R.id.reviewRelativeLayout);
            for (GooglePlace.Review review : place.getReviews()) {
                LinearLayout newLayout = new LinearLayout(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(5, 5, 5, 5);
                newLayout.setOrientation(LinearLayout.VERTICAL);
                TextView reviewerName = new TextView(this);
                reviewerName.setLayoutParams(lp);
                reviewerName.setText(review.getAuthor_name());
                reviewerName.setLayoutParams(lp);
                newLayout.addView(reviewerName);
                TextView reviewText = new TextView(this);
                reviewText.setLayoutParams(lp);
                reviewText.setText(review.getText());
                newLayout.addView(reviewText);
                RatingBar ratingBar = new RatingBar(this);
                ratingBar.setLayoutParams(lp);
                ratingBar.setRating(review.getRating());
                newLayout.addView(ratingBar);
                ratingBar.setIsIndicator(true);
                reviewLinearLayout.addView(newLayout);
            }
        }
    }


    private void zoomImageFromThumb(final View thumbView, Drawable imageResId) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        expandedImageView.setImageDrawable(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        ((Gallery) thumbView.getParent()).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel, back to their
                // original values.
                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void addToFavourites(MenuItem item) {
        DatabaseHelper db = new DatabaseHelper(getBaseContext());
        boolean favourited = false;
        if (selectedFavourite != null) {
            favourited = db.retrieveFavourite(CurrentUserSingleton.getInstance().getUserName(), selectedFavourite.getPlaceID());
        } else {
            favourited = db.retrieveFavourite(CurrentUserSingleton.getInstance().getUserName(), selectedGooglePlace.getPlace_id());
        }
        if (!favourited) {
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 150, 100, 150};
            v.vibrate(pattern, -1);
            item.setIcon(R.drawable.heart_white);
            int userID = CurrentUserSingleton.getInstance().getUserName();
            if (selectedFavourite != null) {
                db.insertFavourite(selectedFavourite.getPlaceID(), userID, selectedFavourite.getPlaceName(),
                        selectedFavourite.getPhotoUrl(), selectedFavourite.getRating(),
                        selectedFavourite.getLatitude(), selectedFavourite.getLongitude());
                Toast.makeText(EateryActivity.this, selectedFavourite.getPlaceName() + " added to favourites!", Toast.LENGTH_SHORT).show();
            } else {
                String photoRef = "";
                if (selectedGooglePlace != null && selectedGooglePlace.getPhotos() != null) {
                    photoRef = selectedGooglePlace.getPhotos().get(0).getPhoto_reference();
                }
                this.selectedFavourite = db.insertFavourite(selectedGooglePlace.getPlace_id(), userID, selectedGooglePlace.getName()
                        , photoRef, selectedGooglePlace.getRating(), selectedGooglePlace.getGeometry().getLocation().getLat()
                        , selectedGooglePlace.getGeometry().getLocation().getLng());
                Toast.makeText(EateryActivity.this, selectedGooglePlace.getName() + " added to favourites!", Toast.LENGTH_SHORT).show();
            }
        } else {
            item.setIcon(R.drawable.heart_black);
            if (selectedFavourite != null) {
                db.removeFavourite(selectedFavourite.getFavouriteID());
            }
        }
    }




    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            if (item.getTitle() != null) {
                String title = item.getTitle().toString();
                switch (title) {
                    case "Add to favourites":
                        addToFavourites(item);
                        break;
                    case "Go to Map":
                        MapPointer mp = new MapPointer(this.selectedGooglePlace);
                        Intent backIntent = new Intent();
                        backIntent.putExtra("SelectedPlaceMap", mp);
                        setResult(RESULT_OK, backIntent);
                        finish();
                        break;
                }
                return super.onOptionsItemSelected(item);
            }
            finish();
            return false;
        } else {
            finish();
            return false;
        }
    }

    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
/*                    Cursor cursor = null;
                    String email = "";
                    String name = "";
                    try {
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();
                        cursor = getBaseContext().getContentResolver().
                                query(ContactsContract.CommonDataKinds.
                                                Email.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.
                                                Email.CONTACT_ID + "=?",
                                        new String[]{id}, null);
                        int emailIdx = cursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.DATA);
                        int nameIdx = cursor.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME);
                        if (cursor.moveToFirst()) {
                            email = cursor.getString(emailIdx);
                            name = cursor.getString(nameIdx);
                            Contact contact = new Contact(name, email);
                            sendToContact(contact);
                        }
                    } catch (Exception e) {
                        Log.i("Share Error", "Exception getting contact " + e.getMessage());
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }*/
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, "Hey, check this place out! " + selectedGooglePlace.getUrl() );
                    i.putExtra(Intent.EXTRA_SUBJECT, selectedGooglePlace.getName());
                    startActivity(i);
                    break;
                case 2:
                  /*  Cursor cursor1 = null;
                    String email1 = "";
                    String name1 = "";
                    try {
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();
                        cursor1 = getBaseContext().getContentResolver().
                                query(ContactsContract.CommonDataKinds.
                                                Email.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.
                                                Email.CONTACT_ID + "=?",
                                        new String[]{id}, null);
                        int emailIdx = cursor1.getColumnIndex(
                                ContactsContract.CommonDataKinds.Email.DATA);
                        int nameIdx = cursor1.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME);
                        if (cursor1.moveToFirst()) {
                            email1 = cursor1.getString(emailIdx);
                            name1 = cursor1.getString(nameIdx);
                            Contact contact = new Contact(name1, email1);
                            meetUpWithContact(contact, meetUp);
                        }
                    } catch (Exception e) {
                        Log.i("Share Error", "Exception getting contact " + e.getMessage());
                    } finally {
                        if (cursor1 != null) {
                            cursor1.close();
                        }
                    }*/
                    Intent meetUpIntent = new Intent(Intent.ACTION_SEND);
                    meetUpIntent.setType("text/plain");
                    meetUpIntent.putExtra(Intent.EXTRA_TEXT, "Hey, can you meet me at this place at " + meetUp.getHours() + ":" + meetUp.getMinutes() + "? " + selectedGooglePlace.getUrl());
                    meetUpIntent.putExtra(Intent.EXTRA_SUBJECT, selectedGooglePlace.getName());
                    startActivity(meetUpIntent);
                    break;
                default:
                    Toast.makeText(getBaseContext(), "Not Yet Implemented code=" + requestCode, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void meetUpWithContact(Contact contact, Date date) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        if (selectedGooglePlace != null) {
            String lat = Double.toString(selectedGooglePlace.getGeometry().getLocation().getLat());
            String lng = Double.toString(selectedGooglePlace.getGeometry().getLocation().getLng());
            String message = "Meet me by " + selectedGooglePlace.getName() + " at " + date.toString() +
                    " Here is its location! " + "https://www.google.co.uk/maps/place/" + selectedFavourite.getPlaceName() + "/@"
                    + lat + "," + lng + ",17z";
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.putExtra(Intent.EXTRA_SUBJECT, "I've found a new place to eat!");
        } else {
            String lat = Double.toString(selectedFavourite.getLatitude());
            String lng = Double.toString(selectedFavourite.getLongitude());
            String name = selectedFavourite.getPlaceName().replace(" ", "+");
            String message = "Check out this place called " + selectedFavourite.getPlaceName() +
                    " Here is its location! " + "https://www.google.co.uk/maps/place/" + name + "/@"
                    + lat + "," + lng + ",17z";
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.putExtra(Intent.EXTRA_SUBJECT, "I've found a new place to eat!");
        }


        if (contact != null) {
            i.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{contact.getEmail()});
        }
        startActivity(i);
    }

    private void sendToContact(Contact contact) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        if (selectedGooglePlace != null) {
            String lat = Double.toString(selectedGooglePlace.getGeometry().getLocation().getLat());
            String lng = Double.toString(selectedGooglePlace.getGeometry().getLocation().getLng());
            String message = "Check out this place called " + selectedGooglePlace.getName() +
                    " Here is its location! " + "https://www.google.co.uk/maps/place/" + selectedFavourite.getPlaceName() + "/@"
                    + lat + "," + lng + ",17z";
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.putExtra(Intent.EXTRA_SUBJECT, "I've found a new place to eat!");
        } else {
            String lat = Double.toString(selectedFavourite.getLatitude());
            String lng = Double.toString(selectedFavourite.getLongitude());
            String name = selectedFavourite.getPlaceName().replace(" ", "+");
            String message = "Check out this place called " + selectedFavourite.getPlaceName() +
                    " Here is its location! " + "https://www.google.co.uk/maps/place/" + name + "/@"
                    + lat + "," + lng + ",17z";
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.putExtra(Intent.EXTRA_SUBJECT, "I've found a new place to eat!");
        }

        if (contact != null) {
            i.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{contact.getEmail()});
        }
        startActivity(i);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            ((EateryActivity) getContext()).getMeetUp().setHours(hourOfDay);
            ((EateryActivity) getContext()).getMeetUp().setMinutes(minute);
            ((EateryActivity) getContext()).requestMeet();
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            ((EateryActivity) getContext()).getMeetUp().setDate(day);
            ((EateryActivity) getContext()).getMeetUp().setMonth(month);
            ((EateryActivity) getContext()).getMeetUp().setYear(year);
        }
    }

    public void setMeetUp(Date date) {
        this.meetUp = date;
    }

    public Date getMeetUp()
    {
        return meetUp;
    }


}
