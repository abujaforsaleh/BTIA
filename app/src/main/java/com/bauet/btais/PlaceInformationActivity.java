package com.bauet.btais;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.bauet.btais.Model.LocationModel;
import com.squareup.picasso.Picasso;

public class PlaceInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);

        ImageView mImageView = findViewById(R.id.imageView);
        TextView mTitleTextView = findViewById(R.id.titleTextView);
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

        mTitleTextView.setText(locationInfo.getPlaceName());

        // Load the image
        Picasso.get().load(locationInfo.getImage()).into(mImageView);

        // Set the nearby hotels
        mNearbyHotelsTextView.setText("sdfadf");
        locationInfoTv.setText(locationInfo.getInformation());

    }
}