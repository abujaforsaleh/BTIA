package com.bauet.btia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    CardView logout, addLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        logout = findViewById(R.id.log_out);
        addLocation = findViewById(R.id.add_location);

        logout.setOnClickListener(this);
        addLocation.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.log_out){
            Paper.book().destroy();

            Intent intent=new Intent(AdminActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
            finish();
        }else if(v.getId()==R.id.add_location){
            Intent intent=new Intent(AdminActivity.this,AddPlaces.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }
    }
}