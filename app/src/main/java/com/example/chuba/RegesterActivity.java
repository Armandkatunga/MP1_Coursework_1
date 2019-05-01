package com.example.chuba;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegesterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email_field, password_field, confirm_password_field;
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

        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        confirm_password_field = findViewById(R.id.confirm_password_field);

        signup = findViewById(R.id.sign_up);
        goback = findViewById(R.id.go_back);

        loader = new ProgressDialog(RegesterActivity.this);
        mAuth = FirebaseAuth.getInstance();
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
        final String email = email_field.getText().toString().trim();
        final String password = password_field.getText().toString().trim();
        final String confirm_password = confirm_password_field.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
            loader.dismiss();
            notify_it("All fields are required !");
        } else {

            if (password.equals(confirm_password)) {

                makeRegistration(email,password);

            } else {
                loader.dismiss();
                notify_it("Password does not match , try again !");
            }

        }

        loader.dismiss();
    }




    public void makeRegistration(String email ,String pass)
    {
        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            switch_to_profile();

                        }else {
                            loader.dismiss();
                            notify_it("Something happened : " + task.getException().getMessage().toString() );
                        }

                    }
                });
    }



    // Notifcatin center
    private void notify_it(String msg){
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
