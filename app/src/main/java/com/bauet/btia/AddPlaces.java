package com.bauet.btia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

public class AddPlaces extends AppCompatActivity implements View.OnClickListener {
    private ImageView inputLocationImage1, inputLocationImage2;
    private Uri imageUri1, imageUri2;
    private static final int GalleryPick = 1;
    private int image_btn_selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);

        String divisions = "Dhaka, Chittagong, Rajshahi, Rangpur, Khulna, Barisal, Sylhet, Mymensingh";
        String districts = "Dhaka, Faridpur, Gazipur, Gopalganj, Kishoreganj, Madaripur, Manikganj, Munshiganj, Narayanganj, Narsingdi, Rajbari, Shariatpur, Tangail, Bandarban, Brahmanbaria, Chandpur, Chittagong, Comilla, Cox's Bazar, Feni, Khagrachhari, Lakshmipur, Noakhali, Rangamati, Bogra, Chapai Nawabganj, Joypurhat, Naogaon, Natore, Nawabganj, Pabna, Rajshahi, Sirajganj, Dinajpur, Gaibandha, Kurigram, Lalmonirhat, Nilphamari, Panchagarh, Rangpur, Thakurgaon, Bagerhat, Chuadanga, Jessore, Jhenaidah, Khulna, Kushtia, Magura, Meherpur, Narail, Satkhira, Barisal, Bhola, Jhalokati, Patuakhali, Pirojpur, Habiganj, Moulvibazar, Sunamganj, Sylhet, Jamalpur, Mymensingh, Netrakona, Sherpur";
        String upozila = "Dhaka, Narayanganj, Gazipur, Narsingdi, Manikganj, Munshiganj, Tangail, Kishoreganj, Madaripur, Shariatpur, Faridpur, Gopalganj, Rajbari, Chandpur, Comilla, Brahmanbaria, Chandgaon, Pabna, Lalmonirhat, Nilphamari, Rangpur, Dinajpur, Thakurgaon, Panchagarh, Bogra, Sirajganj, Naogaon, Joypurhat, Natore, Chapai Nawabganj, Rajshahi, Ishwardi, Pabna, Sathkhira, Barisal, Bhola, Jhalokati, Patuakhali, Pirojpur, Bandarban, Brahmanbaria, Chandpur, Chittagong, Cox's Bazar, Feni, Lakshmipur, Noakhali, Rangamati, Sylhet, Habiganj, Moulvibazar, Sunamganj, Barguna, Barisal, Bhola, Jhalokati, Patuakhali, Pirojpur, Bandarban, Brahmanbaria, Chandpur, Chittagong, Cox's Bazar, Feni, Lakshmipur, Noakhali, Rangamati, Sylhet, Habiganj, Moulvibazar, Sunamganj, Bandarban, Brahmanbaria, Chandpur, Chittagong, Cox's Bazar, Feni, Lakshmipur, Noakhali, Rangamati, Bagerhat, Chuadanga, Jessore, Jhenaidah, Khulna, Kushtia, Magura, Meherpur, Narail, Satkhira, Jamalpur, Mymensingh, Netrakona, Sherpur, Tangail";

        String[] va = upozila.split(", ");
        AutoCompleteTextView actv, actv2, actv3;
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        actv3 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView3);

        inputLocationImage1 = (ImageView) findViewById(R.id.select_img_1);
        inputLocationImage2 = (ImageView) findViewById(R.id.select_img_2);

        ArrayAdapter<String>adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, divisions.split(", "));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<String>adapter2= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts.split(", "));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);

        ArrayAdapter<String>adapter3= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, upozila.split(", "));
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);


        Toast.makeText(this, va.length+"", Toast.LENGTH_SHORT).show();

        actv.setAdapter(adapter);
        actv2.setAdapter(adapter2);
        actv3.setAdapter(adapter3);


        inputLocationImage1.setOnClickListener(this);
        inputLocationImage2.setOnClickListener(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            if(image_btn_selected==1){
                imageUri1 = data.getData();
                inputLocationImage1.setImageURI(imageUri1);
            }else if(image_btn_selected==2){

                imageUri2 = data.getData();
                inputLocationImage2.setImageURI(imageUri2);
            }

        }
    }

    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.select_img_1){
            image_btn_selected = 1;
        }else if(v.getId()==R.id.select_img_2){
            image_btn_selected = 2;
        }
        OpenGallery();
    }
}