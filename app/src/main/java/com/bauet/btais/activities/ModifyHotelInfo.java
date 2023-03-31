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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bauet.btais.Model.HotelModel;
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.R;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ModifyHotelInfo extends AppCompatActivity {

    ImageView hotelImage;
    EditText division, district, upazila;
    EditText hotelName, roomType, costPerNight, bonus, hotelInfo, services, rating;
    Button updateHotelBtn, deleteHotelBtn;
    private Uri imageUri;
    private ProgressDialog loadingBar;
    StorageReference imagePath;
    UploadTask imageUploadTask;
    private StorageReference locationImagesRef;
    private DatabaseReference hotelDataRef;
    boolean isImgUpdated = false;
    HotelModel hotelInformation;

    String downloadImageUrl, productRandomKey, saveCurrentTime, saveCurrentDate, divisionStr, districtStr, upazilaStr, hotelNameStr, roomTypeStr, costPerNightStr, bonusStr, hotelInfoStr;
    String path;
    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_hotel_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF026868));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Update Hotel Information" + "</font>")));
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        hotelInformation = (HotelModel) getIntent().getSerializableExtra("hotel_data");
        path = getIntent().getStringExtra("reference");

        locationImagesRef = FirebaseStorage.getInstance().getReference().child("Hotel Graphics");
        hotelDataRef = FirebaseDatabase.getInstance().getReference().child("Hotels");

        loadingBar = new ProgressDialog(this);
        hotelImage = findViewById(R.id.hotel_image);
        division = findViewById(R.id.division_id);
        district = findViewById(R.id.district_id);
        upazila = findViewById(R.id.upazilaId);
        hotelName = findViewById(R.id.hotelNameId);
        roomType = findViewById(R.id.roomTypeId);
        costPerNight = findViewById(R.id.costPerNight);
        bonus = findViewById(R.id.bonusId);
        hotelInfo = findViewById(R.id.hotelInfo);
        updateHotelBtn = findViewById(R.id.add_new_hotel_id);
        deleteHotelBtn = findViewById(R.id.delete_hotel_id);
        services = findViewById(R.id.room_services);
        rating = findViewById(R.id.rating);

        //settings default value
        division.setText(hotelInformation.getDivision());
        district.setText(hotelInformation.getDistrict());
        upazila.setText(hotelInformation.getUpazila());
        hotelName.setText(hotelInformation.getHotelName());
        roomType.setText(hotelInformation.getRoomType());
        costPerNight.setText(hotelInformation.getCostPerNight());
        bonus.setText(hotelInformation.getBonus());
        //totalRooms.setText(hotelInformation.());///todo need to check this
        hotelInfo.setText(hotelInformation.getHotelInformation());
        loadImage(hotelInformation.getImage());
        services.setText(hotelInformation.getServices());
        rating.setText(hotelInformation.getRating());

        updateHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateLocationData();
            }
        });
        hotelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        deleteHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Convert a string to a DatabaseReference
                DatabaseReference myRef = database.getReference(path);
                myRef.removeValue();
                finish();
            }
        });

    }
    private void loadImage(String url) {
        Picasso.get()
                .load(url)
                .into(hotelImage);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            hotelImage.setImageURI(imageUri);
            isImgUpdated = true;

        }
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        startActivityForResult(galleryIntent, GalleryPick);
    }

    private void ValidateLocationData() {
        divisionStr = division.getText().toString();
        districtStr = district.getText().toString();
        upazilaStr = upazila.getText().toString();
        hotelNameStr = hotelName.getText().toString();
        roomTypeStr = roomType.getText().toString();
        costPerNightStr = costPerNight.getText().toString();
        bonusStr = bonus.getText().toString();

        hotelInfoStr = hotelInfo.getText().toString();


        if (divisionStr.equals("") || districtStr.equals("") || upazilaStr.equals("") ||
                hotelNameStr.equals("") || roomTypeStr.equals("") || costPerNightStr.equals("") ||
                bonusStr.equals("") || hotelInfoStr.equals("")) {
            Toast.makeText(this, "Please fill all the information correctly", Toast.LENGTH_SHORT).show();

        } else {
            storeLocationGraphicsToDB();
            //Toast.makeText(this, "Everything ok", Toast.LENGTH_SHORT).show();
        }

    }


    private void storeLocationGraphicsToDB() {
        try{
            loadingBar.setTitle("Updating Hotel Info");
            loadingBar.setMessage("Wait while we are updating information.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            productRandomKey = hotelInformation.getHid();
            downloadImageUrl = hotelInformation.getImage();
            if(isImgUpdated){
                imagePath = locationImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");//image one
                imageUploadTask = imagePath.putFile(imageUri);

                uploadFile(imageUploadTask);
            }else{
                SaveLocationDetailsToDatabase();
            }

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
                Toast.makeText(ModifyHotelInfo.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                        return imagePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            SaveLocationDetailsToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveLocationDetailsToDatabase() {
        HashMap<String, Object> locationMap = new HashMap<>();
        locationMap.put("hid", productRandomKey);
        locationMap.put("date", saveCurrentDate);
        locationMap.put("time", saveCurrentTime);

        locationMap.put("image", downloadImageUrl);
        locationMap.put("division", divisionStr);
        locationMap.put("district", districtStr);
        locationMap.put("upazila", upazilaStr);

        locationMap.put("hotelName", hotelNameStr);
        locationMap.put("roomType", roomTypeStr);
        locationMap.put("costPerNight", costPerNightStr);
        locationMap.put("bonus", bonusStr);
        locationMap.put("hotelInformation", hotelInfoStr);
        locationMap.put("services", services.getText().toString());

        hotelDataRef.child(productRandomKey).updateChildren(locationMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(ModifyHotelInfo.this, "Hotel info updated successfully..", Toast.LENGTH_SHORT).show();
                            /*Intent intent = new Intent(AddHotelsActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();*/
                            finish();

                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(ModifyHotelInfo.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}