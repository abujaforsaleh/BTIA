package com.bauet.btia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

public class AddPlaces extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);
        String divisions = "Dhaka, Chittagong, Rajshahi, Rangpur, Khulna, Barisal, Sylhet, Mymensingh";
        String districts = "Dhaka, Faridpur, Gazipur, Gopalganj, Kishoreganj, Madaripur, Manikganj, Munshiganj, Narayanganj, Narsingdi, Rajbari, Shariatpur, Tangail, Bandarban, Brahmanbaria, Chandpur, Chittagong, Comilla, Cox's Bazar, Feni, Khagrachhari, Lakshmipur, Noakhali, Rangamati, Bogra, Chapai Nawabganj, Joypurhat, Naogaon, Natore, Nawabganj, Pabna, Rajshahi, Sirajganj, Dinajpur, Gaibandha, Kurigram, Lalmonirhat, Nilphamari, Panchagarh, Rangpur, Thakurgaon, Bagerhat, Chuadanga, Jessore, Jhenaidah, Khulna, Kushtia, Magura, Meherpur, Narail, Satkhira, Barisal, Bhola, Jhalokati, Patuakhali, Pirojpur, Habiganj, Moulvibazar, Sunamganj, Sylhet, Jamalpur, Mymensingh, Netrakona, Sherpur";
        String thanas = "";

        AutoCompleteTextView actv, actv2;
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);

        ArrayAdapter<String>adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, divisions.split(", "));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<String>adapter2= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts.split(", "));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);


        actv.setAdapter(adapter);
        actv2.setAdapter(adapter2);

    }
}