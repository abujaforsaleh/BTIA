package com.bauet.btais.activities;

import static com.bauet.btais.constants.fromButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.R;
import com.bauet.btais.adapters.LocationListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView locationList;
    LocationListAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationList = findViewById(R.id.locationListId);
        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        searchView = findViewById(R.id.search_bar_id);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);

        try{
            Log.d("frombutton", fromButton);
            if(fromButton.equals("user")){
                Log.d("title", "0");
                /*Bundle bundle = getIntent().getExtras();
                Log.d("title", bundle.getString("title"));*/
                toolbar.setTitle("Home");
                Log.d("title", "1");

                navigationView.setNavigationItemSelectedListener(this);
                Log.d("title", "2");
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

                drawerLayout.addDrawerListener(toggle);
                Log.d("title", "3");
                toggle.syncState();
                Log.d("allok", "all ok for user");
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
        mbase = FirebaseDatabase.getInstance().getReference().child("Locations");
        //location recyclerview section

        locationList.setLayoutManager(
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
                Query query1 = FirebaseDatabase.getInstance().getReference().child("Locations").orderByChild("placeName").startAt(newText).endAt(newText + "\uf8ff");
                Query query2 = FirebaseDatabase.getInstance().getReference().child("Locations").orderByChild("division").startAt(newText).endAt(newText + "\uf8ff");
                Set<DataSnapshot> resultSet = new HashSet<>();
                // Attach a ValueEventListener to each query to get the results
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        resultSet.add(dataSnapshot);
                        // Update the UI or perform other actions
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });

                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        resultSet.add(dataSnapshot);
                        // Update the UI or perform other actions
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });
                FirebaseRecyclerOptions<LocationModel> options
                        = new FirebaseRecyclerOptions.Builder<LocationModel>()
                        .setQuery(query1, LocationModel.class)
                        .build();
                /*FirebaseRecyclerOptions<DataModel> options = new FirebaseRecyclerOptions.Builder<DataModel>()
                        .setQuery(query, DataModel.class)
                        .build();*/

                adapter = new LocationListAdapter(options, HomeActivity.this);
                locationList.setAdapter(adapter);
                adapter.startListening();

                return true;
            }
        });
        // Connecting Adapter class with the Recycler view*/


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
        Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
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