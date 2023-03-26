package com.bauet.btais.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bauet.btais.Model.HotelModel;
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.R;
import com.bauet.btais.adapters.RoomFeatureAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelInformationActivity extends AppCompatActivity {

    ImageView roomImage;
    TextView hotelName, address, costPerNight, rating, ratingCount,
            description, discount, roomType;
    RecyclerView servicesList;
    Button bookNow;

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

    }
}