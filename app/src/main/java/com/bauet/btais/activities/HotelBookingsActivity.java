package com.bauet.btais.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bauet.btais.Model.BookingModel;
import com.bauet.btais.Model.HotelModel;
import com.bauet.btais.R;
import com.bauet.btais.adapters.BookingListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HotelBookingsActivity extends AppCompatActivity {
    RecyclerView bookingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_bookings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF026868));
        actionBar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Hotel Bookings" + "</font>")));
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        List<BookingModel> bookingModelList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookingRef = database.getReference("Bookings");

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot hotelSnapshot : snapshot.getChildren()) {
                    // Get the hotel information
                    bookingModelList.add(hotelSnapshot.getValue(BookingModel.class));
                }
                Log.d("length of bookings", bookingModelList.size()+"");
                bookingListView = findViewById(R.id.booking_list_view);
                Log.d("before send", bookingModelList.size()+"");
                BookingListAdapter adapter = new BookingListAdapter(bookingModelList);
                bookingListView.setLayoutManager(new LinearLayoutManager(HotelBookingsActivity.this));
                bookingListView.setAdapter(adapter);
                adapter.notifyItemInserted(0);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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