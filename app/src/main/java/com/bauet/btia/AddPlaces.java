package com.bauet.btia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    private ImageView inputLocationImage1, inputLocationImage2, inputLocationVideo;
    private Uri imageUri1, imageUri2, videoUri;
    private static final int GalleryPick = 1;
    private int image_btn_selected = 0;
    private EditText etPlaceName, etCostFromDhaka, etTravelCost, etLocationInformation;
    private String placeName, costFromDhaka, travelCost, locationInformation, saveCurrentTime, saveCurrentDate, division, district, upazila;
    private String productRandomKey, downloadImage1Url, downloadImage2Url, downloadVideoUrl;
    private Button addInformationBtn;
    private ProgressDialog loadingBar;
    UploadTask uploadTask, uploadTask2, uploadTask3;
    private int fileUploadCount = 0;
    StorageReference filePath, filePath2, filePath3;
    AutoCompleteTextView etDivision, etDistrict, etUpazila;
    private StorageReference locationImagesRef;
    private DatabaseReference locationDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);

        loadingBar = new ProgressDialog(this);

        locationImagesRef = FirebaseStorage.getInstance().getReference().child("Location Graphics");
        locationDataRef = FirebaseDatabase.getInstance().getReference().child("Locations");

        String divisions = "Dhaka, Chittagong, Rajshahi, Rangpur, Khulna, Barisal, Sylhet, Mymensingh";
        String districts = "Dhaka, Faridpur, Gazipur, Gopalganj, Kishoreganj, Madaripur, Manikganj, Munshiganj, Narayanganj, Narsingdi, Rajbari, Shariatpur, Tangail, Bandarban, Brahmanbaria, Chandpur, Chittagong, Comilla, Cox's Bazar, Feni, Khagrachhari, Lakshmipur, Noakhali, Rangamati, Bogra, Chapai Nawabganj, Joypurhat, Naogaon, Natore, Nawabganj, Pabna, Rajshahi, Sirajganj, Dinajpur, Gaibandha, Kurigram, Lalmonirhat, Nilphamari, Panchagarh, Rangpur, Thakurgaon, Bagerhat, Chuadanga, Jessore, Jhenaidah, Khulna, Kushtia, Magura, Meherpur, Narail, Satkhira, Barisal, Bhola, Jhalokati, Patuakhali, Pirojpur, Habiganj, Moulvibazar, Sunamganj, Sylhet, Jamalpur, Mymensingh, Netrakona, Sherpur";
        String upozila = "Dhaka, Narayanganj, Gazipur, Narsingdi, Manikganj, Munshiganj, Tangail, Kishoreganj, Madaripur, Shariatpur, Faridpur, Gopalganj, Rajbari, Chandpur, Comilla, Brahmanbaria, Chandgaon, Pabna, Lalmonirhat, Nilphamari, Rangpur, Dinajpur, Thakurgaon, Panchagarh, Bogra, Sirajganj, Naogaon, Joypurhat, Natore, Chapai Nawabganj, Rajshahi, Ishwardi, Pabna, Sathkhira, Barisal, Bhola, Jhalokati, Patuakhali, Pirojpur, Bandarban, Brahmanbaria, Chandpur, Chittagong, Cox's Bazar, Feni, Lakshmipur, Noakhali, Rangamati, Sylhet, Habiganj, Moulvibazar, Sunamganj, Barguna, Barisal, Bhola, Jhalokati, Patuakhali, Pirojpur, Bandarban, Brahmanbaria, Chandpur, Chittagong, Cox's Bazar, Feni, Lakshmipur, Noakhali, Rangamati, Sylhet, Habiganj, Moulvibazar, Sunamganj, Bandarban, Brahmanbaria, Chandpur, Chittagong, Cox's Bazar, Feni, Lakshmipur, Noakhali, Rangamati, Bagerhat, Chuadanga, Jessore, Jhenaidah, Khulna, Kushtia, Magura, Meherpur, Narail, Satkhira, Jamalpur, Mymensingh, Netrakona, Sherpur, Tangail";

        String[] va = upozila.split(", ");

        etDivision = (AutoCompleteTextView) findViewById(R.id.division_id);
        etDistrict = (AutoCompleteTextView) findViewById(R.id.district_id);
        etUpazila = (AutoCompleteTextView) findViewById(R.id.upazilaId);

        etPlaceName = findViewById(R.id.placeNameId);
        etCostFromDhaka = findViewById(R.id.costFromDhakaId);
        etTravelCost = findViewById(R.id.tourCostId);
        etLocationInformation = findViewById(R.id.locationInfoId);

        addInformationBtn = findViewById(R.id.add_new_place_id);

        inputLocationImage1 = (ImageView) findViewById(R.id.select_img_1);
        inputLocationImage2 = (ImageView) findViewById(R.id.select_img_2);
        inputLocationVideo = (ImageView) findViewById(R.id.select_video_id);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, divisions.split(", "));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts.split(", "));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, upozila.split(", "));
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);



        etDivision.setAdapter(adapter);
        etDistrict.setAdapter(adapter2);
        etUpazila.setAdapter(adapter3);


        inputLocationImage1.setOnClickListener(this);
        inputLocationImage2.setOnClickListener(this);
        inputLocationVideo.setOnClickListener(this);

        addInformationBtn.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            if (image_btn_selected == 1) {
                imageUri1 = data.getData();
                inputLocationImage1.setImageURI(imageUri1);
            } else if (image_btn_selected == 2) {

                imageUri2 = data.getData();
                inputLocationImage2.setImageURI(imageUri2);
            } else if (image_btn_selected == 3) {
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
        } else if (v.getId() == R.id.select_img_2) {
            image_btn_selected = 2;
            OpenGallery();
        } else if (v.getId() == R.id.select_video_id) {
            image_btn_selected = 3;
            OpenGallery();
        } else if (v.getId() == R.id.add_new_place_id) {
            fileUploadCount = 0;
            ValidateLocationData();
        }

    }

    private void ValidateLocationData() {
        placeName = etPlaceName.getText().toString();
        costFromDhaka = etCostFromDhaka.getText().toString();
        travelCost = etTravelCost.getText().toString();
        locationInformation = etLocationInformation.getText().toString();

        division = etDivision.getText().toString();
        district = etDistrict.getText().toString();
        upazila = etUpazila.getText().toString();

        if (imageUri1 == null || imageUri2 == null) {
            Toast.makeText(this, "Select two image properly.", Toast.LENGTH_SHORT).show();
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
            loadingBar.setTitle("Add New Location");
            loadingBar.setMessage("Wait while we are adding the new location.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());
            productRandomKey = saveCurrentDate + saveCurrentTime;

            filePath = locationImagesRef.child(imageUri1.getLastPathSegment() + productRandomKey + ".jpg");//image one
            filePath2 = locationImagesRef.child(imageUri2.getLastPathSegment() + productRandomKey + ".jpg");//image two
            filePath3 = locationImagesRef.child(videoUri.getLastPathSegment() + productRandomKey + ".mp4");//video

            uploadTask = filePath.putFile(imageUri1);
            uploadTask2 = filePath2.putFile(imageUri2);
            uploadTask3 = filePath3.putFile(videoUri);


            uploadFile(uploadTask);

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
                        if(fileUploadCount==0){
                            return filePath.getDownloadUrl();
                        }else if(fileUploadCount==1){
                            return filePath2.getDownloadUrl();
                        }else{
                            return filePath3.getDownloadUrl();
                        }

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            if (fileUploadCount == 0) {
                                downloadImage1Url = task.getResult().toString();
                                fileUploadCount++;
                                uploadFile(uploadTask2);

                            } else if (fileUploadCount == 1) {
                                downloadImage2Url = task.getResult().toString();
                                fileUploadCount++;
                                uploadFile(uploadTask3);

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

        locationMap.put("image1", downloadImage1Url);
        locationMap.put("image2", downloadImage2Url);
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
                        /*Intent intent = new Intent(AddPlaces.this, AdminActivity.class);
                        startActivity(intent);*/

                        loadingBar.dismiss();
                        Toast.makeText(AddPlaces.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingBar.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AddPlaces.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }


}