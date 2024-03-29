package com.bauet.btais.activities;

import static com.bauet.btais.constants.fromButton;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bauet.btais.Model.Users;
import com.bauet.btais.Prevalent.Prevalent;
import com.bauet.btais.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;
    private String parentDbName = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

    }
    private void LoginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {

            Toasty.error(LoginActivity.this, "Please fill all the information",
                    Toast.LENGTH_SHORT, true).show();
            //Toast.makeText(this, "Please fill all the information", Toast.LENGTH_SHORT).show();
            InputPhoneNumber.setError("Please write your phone number...");
        }
        else if (TextUtils.isEmpty(password))
        {
            //Toast.makeText(this, "Please fill all the information", Toast.LENGTH_SHORT).show();
            InputPassword.setError("Please write your phone number...");
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone, password);
        }
    }
    private void AllowAccessToAccount(final String phone, final String password)
    {

        Paper.book().write(Prevalent.UserPhoneKey, phone);

        Paper.book().write(Prevalent.UserPasswordKey, password);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Paper.book().write(Prevalent.UserNameKey, usersData.getName());
                            if(parentDbName.equals("Admins"))
                            {
                                Toasty.success(LoginActivity.this, "Welcome Admin, you are logged in Successfully...",
                                        Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Paper.book().write("user_mode", "admin");
                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                                finish();
                            }
                            else if (parentDbName.equals("Users")){
                                fromButton = "user";//for re arranging home screen because same activity using by the user and the admin.
                                Toasty.success(LoginActivity.this, "logged in Successfully...",
                                        Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                                finish();
                            }

                        }
                        else {
                            loadingBar.dismiss();
                            Toasty.warning(LoginActivity.this, "Password is incorrect",
                                    Toast.LENGTH_SHORT, true).show();
                            //Toast.makeText(LoginActivity.this,"Password is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toasty.error(LoginActivity.this, "Account with this phone number do not exists.",
                            Toast.LENGTH_SHORT, true).show();
                    //Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
