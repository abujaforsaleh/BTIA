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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashSet;
import java.util.Set;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView searchItemsList;
    DatabaseReference mbase;
    SearchView searchView;
    String searchOption = "Place";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        
        mbase = FirebaseDatabase.getInstance().getReference().child("Locations");

        switchSearchOptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tvSwitchHotel.setTextColor(ContextCompat.getColor(HomeActivity.this,R.color.blue_color));
                    tvSwitchPlace.setTextColor(ContextCompat.getColor(HomeActivity.this,R.color.white));
                    searchOption = "Place";
                    updateMyList("");
                }else{
                    tvSwitchHotel.setTextColor(ContextCompat.getColor(HomeActivity.this,R.color.white));
                    tvSwitchPlace.setTextColor(ContextCompat.getColor(HomeActivity.this,R.color.blue_color));
                    searchOption = "Hotel";
                    updateMyList("");
                }
            }
        });
        
        //location recyclerview section

        searchItemsList.setLayoutManager(
                new LinearLayoutManager(this));

        //adapter = new LocationListAdapter(options, this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query here
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query here

                updateMyList(newText);

                return true;
            }
        });
        // Connecting Adapter class with the Recycler view*/
    }

    public void updateMyList(String newText){
        Query queryPlace = FirebaseDatabase.getInstance().getReference().child("Locations").orderByChild("placeName");
        Query queryHotel = FirebaseDatabase.getInstance().getReference().child("Hotels").orderByChild("hotelName");
        //Query query2 = FirebaseDatabase.getInstance().getReference().child("Locations").orderByChild("division").startAt(newText).endAt(newText + "\uf8ff");
        if(searchOption.equals("Hotel")){
            FirebaseRecyclerOptions<HotelModel> options
                    = new FirebaseRecyclerOptions.Builder<HotelModel>()
                    .setQuery(queryHotel, HotelModel.class)
                    .build();

            HotelListAdapter adapter = new HotelListAdapter(options, HomeActivity.this);
            searchItemsList.setAdapter(adapter);
            adapter.startListening();

        }else {
            FirebaseRecyclerOptions<LocationModel> options
                    = new FirebaseRecyclerOptions.Builder<LocationModel>()
                    .setQuery(queryPlace, LocationModel.class)
                    .build();

            LocationListAdapter adapter = new LocationListAdapter(options, HomeActivity.this);
            searchItemsList.setAdapter(adapter);
            adapter.startListening();
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

}