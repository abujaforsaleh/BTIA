package com.bauet.btais.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bauet.btais.Model.HotelModel;
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.Prevalent.Prevalent;
import com.bauet.btais.R;
import com.bauet.btais.adapters.RoomFeatureAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class HotelInformationActivity extends AppCompatActivity {

    ImageView roomImage;
    TextView hotelName, address, costPerNight, rating, ratingCount,
            description, discount, roomType;
    RecyclerView servicesList;
    Button bookNow;
    private DatabaseReference bookingRef;
    private ProgressDialog loadingBar;
    private String saveCurrentTime, saveCurrentDate, booking_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_information);

        HotelModel hotelInfo = (HotelModel) getIntent().getSerializableExtra("hotel_data");
        Log.d("room information ", hotelInfo.toString());

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF026868));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Hotel Information" + "</font>")));
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadingBar = new ProgressDialog(this);
        bookingRef = FirebaseDatabase.getInstance().getReference().child("Bookings");

        roomImage = findViewById(R.id.room_img);
        hotelName = findViewById(R.id.hotelName);
        address = findViewById(R.id.hotel_address);
        costPerNight = findViewById(R.id.room_cost_per_night);
        rating = findViewById(R.id.rating);
        ratingCount = findViewById(R.id.rating_count);
        description = findViewById(R.id.room_description);
        discount = findViewById(R.id.booking_discount);
        roomType = findViewById(R.id.room_catagory);
        servicesList = findViewById(R.id.service_list);
        bookNow = findViewById(R.id.book_button);

        String addressStr = hotelInfo.getUpazila()+", "+hotelInfo.getDistrict()+", "+hotelInfo.getDivision();
        //setting data
        Picasso.get().load(hotelInfo.getImage()).into(roomImage);
        hotelName.setText(hotelInfo.getHotelName());
        address.setText(addressStr);
        costPerNight.setText(hotelInfo.getCostPerNight());
        roomType.setText(hotelInfo.getRoomType());
        discount.setText(hotelInfo.getBonus()+" % discount");

        description.setText(hotelInfo.getHotelInformation());
        hotelName.setText(hotelInfo.getHotelName());

        RoomFeatureAdapter roomFeatureAdapter = new RoomFeatureAdapter(Arrays.asList(hotelInfo.getServices().split(", ")));
        servicesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        servicesList.setAdapter(roomFeatureAdapter);

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Add New Content");
                loadingBar.setMessage("Wait while we are adding the new Content.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calendar.getTime());
                booking_id = saveCurrentDate + saveCurrentTime;

                HashMap<String, Object> locationMap = new HashMap<>();
                locationMap.put("bookingId", booking_id);
                locationMap.put("date", saveCurrentDate);
                locationMap.put("time", saveCurrentTime);
                locationMap.put("room_id", hotelInfo.getHid());
                locationMap.put("hotel_name", hotelInfo.getHotelName());
                locationMap.put("room_rate", hotelInfo.getCostPerNight());
                locationMap.put("discount", hotelInfo.getDistrict());
                locationMap.put("userName", Paper.book().read(Prevalent.UserNameKey));
                locationMap.put("userPhone", Paper.book().read(Prevalent.UserPhoneKey));

                bookingRef.child(booking_id).updateChildren(locationMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    loadingBar.dismiss();
                                    Toast.makeText(HotelInformationActivity.this, "Booking logged. We will contact soon...", Toast.LENGTH_LONG).show();
                            /*Intent intent = new Intent(AddHotelsActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();*/

                                } else {
                                    loadingBar.dismiss();
                                    String message = task.getException().toString();
                                    Toast.makeText(HotelInformationActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

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