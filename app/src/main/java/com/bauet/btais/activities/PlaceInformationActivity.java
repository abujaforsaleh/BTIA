package com.bauet.btais.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.R;
import com.squareup.picasso.Picasso;

public class PlaceInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF026868));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Information" + "</font>")));
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView mImageView = findViewById(R.id.imageView);
        TextView mTitleTextView = findViewById(R.id.titleTextView);
        TextView tourCostView = findViewById(R.id.tour_cost);
        VideoView mVideoView = findViewById(R.id.videoView);
        TextView mNearbyHotelsTextView = findViewById(R.id.nearbyHotelsTextView);
        TextView locationInfoTv = findViewById(R.id.locationInformation);
        LocationModel locationInfo = (LocationModel) getIntent().getSerializableExtra("location_data");

        MediaController mMediaController;
        // Create a MediaController to control playback
        mMediaController = new MediaController(this);
        mMediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mMediaController);

        // Load the video
        Uri videoUri = Uri.parse(locationInfo.getVideo());
        mVideoView.setVideoURI(videoUri);
        mVideoView.start();

        tourCostView.setText("Travel cost from dhaka - "+locationInfo.getCostFromDhaka()+" taka.\nPlace visiting cost - "+locationInfo.getTravelCost()+" taka.");
        mTitleTextView.setText(locationInfo.getPlaceName());

        // Load the image
        Picasso.get().load(locationInfo.getImage()).into(mImageView);

        // Set the nearby hotels
        mNearbyHotelsTextView.setText("No hotel information available right now");
        locationInfoTv.setText(locationInfo.getInformation());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}