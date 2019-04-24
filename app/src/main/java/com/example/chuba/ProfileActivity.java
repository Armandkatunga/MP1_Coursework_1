package com.example.chuba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private EditText full_name;
    private Button save;
    private ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ini_set();
        events();
    }

    private void ini_set() {
        full_name  =  findViewById(R.id.full_name);
        save       =  findViewById(R.id.save);
        getSupportActionBar().setTitle("Account Profile");
        loader = new ProgressDialog(ProfileActivity.this);
    }

    private void events() {

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
            notify("Provide your full name please !");
        }else{
            switch_to_home();
        }
        loader.dismiss();
    }

    // Notifcatin center
    private void notify(String msg){
        Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void switch_to_home() {
        loader.dismiss();
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

}
