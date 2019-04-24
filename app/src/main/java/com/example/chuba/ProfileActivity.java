package com.example.chuba;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private EditText full_name;
    private Button save;
    private ProgressDialog loader;

    private ImageView user_img;
    static int PermissionRequestCode = 1 ;
    static int REUQEST_CODE = 1 ;

    // Img url
    Uri saveSelectedImgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ini_set();
        events();
    }

    private void ini_set() {
        user_img   = findViewById(R.id.user_image);
        full_name  =  findViewById(R.id.full_name);
        save       =  findViewById(R.id.save);
        getSupportActionBar().setTitle("Account Profile");
        loader = new ProgressDialog(ProfileActivity.this);
    }

    private void events() {

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22) {
                    galleryCheckAndAskForPermission();
                }else{
                    gallery();
                }

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loader.setMessage("Saving information...");
                loader.show();

                saveUsetDatas();
            }
        });

    }

    private void saveUsetDatas() {

        final String fullNAame    = full_name.getText().toString().trim();

        if (fullNAame.isEmpty())
        {
            loader.dismiss();
            notify_it("Provide your full name please !");
        }else{
            switch_to_home();
        }
        loader.dismiss();
    }





    private void galleryCheckAndAskForPermission()
    {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE ))
            {
                notify_it("Accept for required permission");
            }else {
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionRequestCode);
            }

        }else {
            gallery();
        }

    }


    // Open the Gallery image and wait for the user to pick or select an image
    private void gallery(){

        Intent intentToGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentToGallery.setType("image/*");
        //Open up an activity
        startActivityForResult(intentToGallery, REUQEST_CODE);

    }

    // This method take action when the application has resume from the Gallery image
    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode ==RESULT_OK && requestCode == REUQEST_CODE && data != null) {

            // Image successfully selected or picked
            saveSelectedImgUri = data.getData();
            // Showing the selected image on the screen
            user_img.setImageURI(saveSelectedImgUri);

        }
    }








    // Notifcatin center
    private void notify_it(String msg){
        Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void switch_to_home() {
        loader.dismiss();
        Intent intent = new Intent(ProfileActivity.this, TwoFactorAuthenficationActivity.class);
        startActivity(intent);
    }

}
