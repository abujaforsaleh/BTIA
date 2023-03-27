package com.bauet.btais.activities;

import static com.bauet.btais.constants.fromButton;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bauet.btais.Model.HotelModel;
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.Prevalent.Prevalent;
import com.bauet.btais.R;
import com.bauet.btais.adapters.HotelListAdapter;
import com.bauet.btais.adapters.LocationListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView searchItemsList;
    SearchView searchView;
    String searchOption = "Place";
    List<HotelModel> hotelModelList;
    List<LocationModel> locationModelList;
    List<String> hotelRefs;
    List<String> locationRefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        hotelModelList = new ArrayList<>();
        locationModelList = new ArrayList<>();
        hotelRefs = new ArrayList<>();
        locationRefs = new ArrayList<>();

        SwitchCompat switchSearchOptions;
        TextView tvSwitchHotel, tvSwitchPlace;
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);

        searchItemsList = findViewById(R.id.locationListId);
        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        searchView = findViewById(R.id.search_bar_id);
        
        try{
            if(fromButton.equals("user")){

                /*Bundle bundle = getIntent().getExtras();
                Log.d("title", bundle.getString("title"));*/
                View headerView = navigationView.getHeaderView(0);
                TextView navUsername = (TextView) headerView.findViewById(R.id.user_profile_name);
                TextView navUserPhone = (TextView) headerView.findViewById(R.id.user_profile_phone);
                navUsername.setText(Paper.book().read(Prevalent.UserNameKey));
                navUserPhone.setText(Paper.book().read(Prevalent.UserPhoneKey));

                toolbar.setTitle("Home");
                navigationView.setNavigationItemSelectedListener(this);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();

            }else{

                //Toast.makeText(this, "hiding title", Toast.LENGTH_SHORT).show();
                String title = "";
                //toolbar.setNavigationIcon(null);
                if(fromButton.equals("update_delete")){
                    title = "Update/Delete Locations";
                }else{
                    title = "User View";
                }
                toolbar.setTitle(title);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }

        }catch (Exception e){
            toolbar.setTitle("Home");
        }
        
        switchSearchOptions = findViewById(R.id.switchMaleFemale);
        tvSwitchHotel = findViewById(R.id.tvSwitchHotel);
        tvSwitchPlace = findViewById(R.id.tvSwitchPlace);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hotelsRef = database.getReference("Hotels");
        DatabaseReference locationRef = database.getReference("Locations");

        hotelsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotelModelList = new ArrayList<>();
                hotelRefs = new ArrayList<>();
                for (DataSnapshot hotelSnapshot : snapshot.getChildren()) {
                    // Get the hotel information
                    hotelModelList.add(hotelSnapshot.getValue(HotelModel.class));
                    hotelRefs.add(hotelSnapshot.getRef().getPath().toString());
                    Log.d("ref", "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationModelList = new ArrayList<>();
                locationRefs = new ArrayList<>();
                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                    // Get the hotel information
                    locationModelList.add(locationSnapshot.getValue(LocationModel.class));
                    locationRefs.add(locationSnapshot.getRef().getPath().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        switchSearchOptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tvSwitchHotel.setTextColor(ContextCompat.getColor(HomeActivity.this,R.color.blue_color));
                    tvSwitchPlace.setTextColor(ContextCompat.getColor(HomeActivity.this,R.color.white));
                    searchOption = "Place";
                    updateMyList("");
                    searchView.clearFocus();
                }else{
                    tvSwitchHotel.setTextColor(ContextCompat.getColor(HomeActivity.this,R.color.white));
                    tvSwitchPlace.setTextColor(ContextCompat.getColor(HomeActivity.this,R.color.blue_color));
                    searchOption = "Hotel";
                    updateMyList("");
                    searchView.clearFocus();
                }
            }
        });
        //location recyclerview section

        searchItemsList.setLayoutManager(
                new LinearLayoutManager(this));

        //adapter = new LocationListAdapter(options, this);
        updateMyList("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query here
                updateMyList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query here
                if(newText.equals("")){
                    updateMyList("");
                }

                return false;
            }
        });
        // Connecting Adapter class with the Recycler view*/
    }

    public void updateMyList(String newText){
        Pattern pattern = Pattern.compile("(\\D+)(\\d+)");
        Matcher matcher = pattern.matcher(newText);
        int maxBudget = 10000000;
        if(matcher.matches()){
            newText = Objects.requireNonNull(matcher.group(1)).trim();
            maxBudget = Integer.parseInt(Objects.requireNonNull(matcher.group(2)));
        }
        if(isNumeric(newText)){
            maxBudget = Integer.parseInt(newText);
            newText = "";

        }
        Log.d("query", newText+" "+maxBudget);

        if(searchOption.equals("Hotel")){
            List<HotelModel> filteredHotel = new ArrayList<>();
            List<String> filteredHotelDataPath = new ArrayList<>();

            for(int i = 0;i<hotelModelList.size();i++){
                HotelModel mdl = hotelModelList.get(i);
                if(mdl.getDivision().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT)) ||
                        mdl.getDistrict().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))||
                        mdl.getUpazila().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))||
                        mdl.getHotelName().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))){

                    if(maxBudget>=Integer.parseInt(mdl.getCostPerNight())) {
                        filteredHotel.add(mdl);
                        filteredHotelDataPath.add(hotelRefs.get(i));
                    }
                }

            }

            HotelListAdapter adapter = new HotelListAdapter(filteredHotel, filteredHotelDataPath, HomeActivity.this);
            searchItemsList.setAdapter(adapter);
            //adapter.notify();

        }
        else {
            List<LocationModel> filteredLocation = new ArrayList<>();
            for(int i = 0;i<locationModelList.size();i++){
                LocationModel mdl = locationModelList.get(i);
                if(mdl.getDivision().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT)) ||
                        mdl.getDistrict().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))||
                        mdl.getUpazila().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))||
                        mdl.getPlaceName().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))){
                    if(maxBudget>=Integer.parseInt(mdl.getTravelCost())) filteredLocation.add(mdl);
                    //Log.d("hotel info "+i, hotelModelList.get(i).getDivision()+" "+hotelModelList.get(i).getDistrict()+" "+hotelModelList.get(i).getUpazila()+" "+" "+hotelModelList.get(i).getHotelName());
                }

            }

            LocationListAdapter adapter = new LocationListAdapter(filteredLocation, locationRefs, HomeActivity.this);
            searchItemsList.setAdapter(adapter);
            //Toast.makeText(this, "sadflkjal;dfjk", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed(){

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //if(fromButton.equals("user"))
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_search) {
        }else if (id == R.id.nav_logout) {
            Paper.book().destroy();
            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}