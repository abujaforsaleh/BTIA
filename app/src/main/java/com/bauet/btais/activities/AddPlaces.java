package com.bauet.btais.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bauet.btais.R;
import com.bauet.btais.constants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPlaces extends AppCompatActivity implements View.OnClickListener {
    private ImageView inputLocationImage, inputLocationVideo;
    private Uri imageUri, videoUri;
    private static final int GalleryPick = 1;
    private int image_btn_selected = 0;
    private EditText etPlaceName, etCostFromDhaka, etTravelCost, etLocationInformation;
    private String placeName, costFromDhaka, travelCost, locationInformation, saveCurrentTime, saveCurrentDate, division, district, upazila;
    private String productRandomKey, downloadImageUrl, downloadVideoUrl;
    private Button addInformationBtn;
    private ProgressDialog loadingBar;
    UploadTask imageUploadTask, videoUploadTask;
    private int fileUploadCount = 1;
    StorageReference imagePath, videoPath;
    AutoCompleteTextView etDivision, etDistrict, etUpazila;
    private StorageReference locationImagesRef;
    private DatabaseReference locationDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);

        loadingBar = new ProgressDialog(this);
        /*getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF026868));
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Information" + "</font>")));*/
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF026868));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Add new Place" + "</font>")));
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        locationImagesRef = FirebaseStorage.getInstance().getReference().child("Location Graphics");
        locationDataRef = FirebaseDatabase.getInstance().getReference().child("Locations");


        String[] va = constants.upozila.split(", ");

        etDivision = (AutoCompleteTextView) findViewById(R.id.division_id);
        etDistrict = (AutoCompleteTextView) findViewById(R.id.district_id);
        etUpazila = (AutoCompleteTextView) findViewById(R.id.upazilaId);

        etPlaceName = findViewById(R.id.placeNameId);
        etCostFromDhaka = findViewById(R.id.costFromDhakaId);
        etTravelCost = findViewById(R.id.tourCostId);
        etLocationInformation = findViewById(R.id.locationInfoId);

        addInformationBtn = findViewById(R.id.add_new_place_id);

        inputLocationImage = (ImageView) findViewById(R.id.select_img_1);
        inputLocationVideo = (ImageView) findViewById(R.id.select_video_id);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, constants.divisions.split(", "));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, constants.districts.split(", "));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, constants.upozila.split(", "));
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);

        etDivision.setAdapter(adapter);
        etDistrict.setAdapter(adapter2);
        etUpazila.setAdapter(adapter3);

        inputLocationImage.setOnClickListener(this);
        inputLocationVideo.setOnClickListener(this);

        addInformationBtn.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            if (image_btn_selected == 1) {
                imageUri = data.getData();
                inputLocationImage.setImageURI(imageUri);
            }else if (image_btn_selected == 3) {
                videoUri = data.getData();
                inputLocationVideo.setImageResource(R.drawable.video_icon);
            }

        }
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (image_btn_selected == 3) {
            galleryIntent.setType("video/*");
        } else {
            galleryIntent.setType("image/*");
        }

        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_img_1) {
            image_btn_selected = 1;
            OpenGallery();
        }else if (v.getId() == R.id.select_video_id) {
            image_btn_selected = 3;
            OpenGallery();
        } else if (v.getId() == R.id.add_new_place_id) {
            fileUploadCount = 1;
            ValidateLocationData();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void ValidateLocationData() {
        placeName = etPlaceName.getText().toString();
        costFromDhaka = etCostFromDhaka.getText().toString();
        travelCost = etTravelCost.getText().toString();
        locationInformation = etLocationInformation.getText().toString();

        division = etDivision.getText().toString();
        district = etDistrict.getText().toString();
        upazila = etUpazila.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Select image properly.", Toast.LENGTH_SHORT).show();
        } else if (videoUri == null) {
            Toast.makeText(this, "Select a video.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(placeName)) {
            Toast.makeText(this, "Please write placeName", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(costFromDhaka)) {
            Toast.makeText(this, "Please write costFromDhaka", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(travelCost)) {
            Toast.makeText(this, "Please write travelCost", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(locationInformation)) {
            Toast.makeText(this, "Please write locationInformation", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(division) || TextUtils.isEmpty(district) || TextUtils.isEmpty(upazila)) {
            Toast.makeText(this, "Please fill all the location info", Toast.LENGTH_SHORT).show();
        }
        else {
            storeLocationGraphicsToDB();
        }

    }

    private void storeLocationGraphicsToDB() {
        try{
            loadingBar.setTitle("Add New Content");
            loadingBar.setMessage("Wait while we are adding the new Content.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());
            productRandomKey = saveCurrentDate + saveCurrentTime;

            imagePath = locationImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");//image one
            videoPath = locationImagesRef.child(videoUri.getLastPathSegment() + productRandomKey + ".mp4");//video

            imageUploadTask = imagePath.putFile(imageUri);
            videoUploadTask = videoPath.putFile(videoUri);


            uploadFile(imageUploadTask);

        }catch (Exception e){
            Log.d("error", "found error");
            Log.e("error", e.toString());
        }

    }

    private void uploadFile(UploadTask ut) {

        ut.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddPlaces.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                ut.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        if(fileUploadCount==1){
                            return imagePath.getDownloadUrl();
                        }else{
                            return videoPath.getDownloadUrl();
                        }

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            if (fileUploadCount == 1) {
                                downloadImageUrl = task.getResult().toString();
                                fileUploadCount++;
                                uploadFile(videoUploadTask);

                            } else if (fileUploadCount == 2) {
                                downloadVideoUrl = task.getResult().toString();
                                fileUploadCount++;
                                SaveLocationDetailsToDatabase();

                            }

                        }
                    }
                });
            }
        });
    }

    private void SaveLocationDetailsToDatabase() {
        HashMap<String, Object> locationMap = new HashMap<>();
        locationMap.put("lid", productRandomKey);
        locationMap.put("date", saveCurrentDate);
        locationMap.put("time", saveCurrentTime);

        locationMap.put("image", downloadImageUrl);
        locationMap.put("video", downloadVideoUrl);
        locationMap.put("division", division);
        locationMap.put("district", district);
        locationMap.put("upazila", upazila);

        locationMap.put("placeName", placeName);
        locationMap.put("costFromDhaka", costFromDhaka);
        locationMap.put("travelCost", travelCost);
        locationMap.put("information", locationInformation);

        locationDataRef.child(productRandomKey).updateChildren(locationMap)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        loadingBar.dismiss();
                        Toast.makeText(AddPlaces.this, "Location is added successfully..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddPlaces.this, AdminActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        loadingBar.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AddPlaces.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

}