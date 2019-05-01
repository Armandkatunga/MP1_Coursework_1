package com.example.chuba;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    private EditText full_name;
    private Button save;
    private ProgressDialog loader;

    private ImageView user_img;
    static int PermissionRequestCode = 1 ;
    static int REUQEST_CODE = 1 ;

    // Img url
    Uri saveSelectedImgUri2;
    FirebaseAuth mAuth;

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
        mAuth  = FirebaseAuth.getInstance();
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
            updateUserInfo(fullNAame,saveSelectedImgUri2,mAuth.getCurrentUser());
        }

    }


    // Associate info for the registered user (Current) : Names and Profile picture
    private void updateUserInfo(final String names, Uri saveSelectedImgUri, final FirebaseUser currentUser)
    {

        // Uploading the user image to fire storage and get back the uri
        StorageReference fire_storage = FirebaseStorage.getInstance().getReference().child("users_photo");
        final StorageReference imageFilePath = fire_storage.child(saveSelectedImgUri.getLastPathSegment());

        // On upload successfully
        imageFilePath.putFile(saveSelectedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                // The image has been successfully uploaded , Getting the Uri back which contain the image URL
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri)
                    {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(names)
                                .setPhotoUri(uri).build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>(){
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            switch_to_home();
                                        }else{
                                            loader.dismiss();
                                            notify_it("Failed to save user infos");
                                        }
                                    }
                                });
                    }

                });
            }
        });

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
            saveSelectedImgUri2 = data.getData();
            // Showing the selected image on the screen
            user_img.setImageURI(saveSelectedImgUri2);

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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null) {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}
