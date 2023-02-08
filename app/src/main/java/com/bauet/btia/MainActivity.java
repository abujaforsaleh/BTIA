package com.bauet.btia;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bauet.btia.Model.Users;
import com.bauet.btia.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    String UserPhoneKey, UserPasswordKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);
        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, com.bauet.btia.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (!Objects.equals(UserPhoneKey, "") && !Objects.equals(UserPasswordKey, ""))
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey, UserPasswordKey, "Users");

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }
    private void AllowAccess(final String phone, final String password, String mode)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mode).child(phone).exists()){

                    Users usersData = dataSnapshot.child(mode).child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent();
                            if(mode.equals("Users")){
                                intent = new Intent(MainActivity.this, HomeActivity.class);
                            }else{
                                intent = new Intent(MainActivity.this, AdminActivity.class);
                            }

                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                            finish();

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Password is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    if(mode.equals("Users")){
                        AllowAccess(UserPhoneKey, UserPasswordKey, "Admins");

                    }else{
                        Toast.makeText(MainActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
