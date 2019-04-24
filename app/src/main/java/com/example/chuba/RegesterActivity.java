package com.example.chuba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegesterActivity extends AppCompatActivity {

    private EditText email_field,password_field,confirm_password_field;
    private Button signup;
    private TextView goback;

    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);

        ini();
        events();
    }

    private void ini() {

        email_field  =  findViewById(R.id.email_field);
        password_field  =  findViewById(R.id.password_field);
        confirm_password_field =  findViewById(R.id.confirm_password_field);

        signup = findViewById(R.id.sign_up);
        goback    = findViewById(R.id.go_back);

        loader = new ProgressDialog(RegesterActivity.this);
    }

    private void events() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.setMessage("Signing up..");
                loader.show();

                signup_proccess();
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

    }

    private void signup_proccess() {
        final String email    = email_field.getText().toString().trim();
        final String password = password_field.getText().toString().trim();
        final String confirm_password = confirm_password_field.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirm_password.isEmpty())
        {
            loader.dismiss();
            notify("All fields are required !");
        }else{

            if (password.equals(confirm_password)) {
                switch_to_profile();
            }else{
                loader.dismiss();
                notify("Password does not match , try again !");
            }

        }

        loader.dismiss();
    }


    // Notifcatin center
    private void notify(String msg){
        Toast.makeText(RegesterActivity.this, msg, Toast.LENGTH_LONG).show();
    }
    private void goBack(){
        Intent intent = new Intent(RegesterActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void switch_to_profile() {
        loader.dismiss();
        Intent intent = new Intent(RegesterActivity.this, ProfileActivity.class);
        startActivity(intent);
    }



}
