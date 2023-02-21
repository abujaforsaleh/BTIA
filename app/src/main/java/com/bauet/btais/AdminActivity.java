package com.bauet.btais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    CardView logout, addLocation, updateInfo, viewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        logout = findViewById(R.id.log_out);
        addLocation = findViewById(R.id.add_location);
        updateInfo = findViewById(R.id.update_location);
        viewContent = findViewById(R.id.view_content_btn_id);

        logout.setOnClickListener(this);
        addLocation.setOnClickListener(this);
        updateInfo.setOnClickListener(this);
        viewContent.setOnClickListener(this);

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
        }else if(v.getId()==R.id.view_content_btn_id){
            Intent intent=new Intent(AdminActivity.this,HomeActivity.class);
            intent.putExtra("title", "Locations");
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }

        else{
            Toast.makeText(this, "Under Constraction", Toast.LENGTH_SHORT).show();
        }
    }
}