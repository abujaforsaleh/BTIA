package com.bauet.btais.activities;

import static com.bauet.btais.constants.fromButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bauet.btais.R;

import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    CardView logout, addLocation, updateDelete, viewContent, addNewHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        logout = findViewById(R.id.log_out);
        addLocation = findViewById(R.id.add_location);
        updateDelete = findViewById(R.id.update_delete_content_id);
        viewContent = findViewById(R.id.view_content_id);
        addNewHotel = findViewById(R.id.add_hotel);

        viewContent.setOnClickListener(this);
        logout.setOnClickListener(this);
        addLocation.setOnClickListener(this);
        updateDelete.setOnClickListener(this);
        addNewHotel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.log_out){
            fromButton = "log_out";

            Paper.book().destroy();
            Intent intent=new Intent(AdminActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
            finish();
        }else if(v.getId()==R.id.add_location){
            fromButton = "add_location";
            Intent intent=new Intent(AdminActivity.this,AddPlaces.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }else if(v.getId()==R.id.update_delete_content_id){
            fromButton = "update_delete";
            Intent intent=new Intent(AdminActivity.this,HomeActivity.class);
            intent.putExtra("title", "Locations");
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        } else if(v.getId()==R.id.view_content_id){
            fromButton = "view_content";
            Intent intent=new Intent(AdminActivity.this,HomeActivity.class);
            intent.putExtra("title", "Locations");
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }else if(v.getId()==R.id.add_hotel){
            Intent intent=new Intent(AdminActivity.this,AddHotelsActivity.class);
            //intent.putExtra("title", "Locations");
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }
    }
}