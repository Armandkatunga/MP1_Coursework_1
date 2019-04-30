package com.example.chuba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class TwoFactorAuthenficationActivity extends AppCompatActivity {

    private EditText phone_number,verification;
    private Button save,done;
    private ProgressDialog loader;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerification;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_factor_authenfication);

        ini();
        events();

    }

    private void ini()
    {
        phone_number = findViewById(R.id.phone_number);
        verification = findViewById(R.id.verification);

        save = findViewById(R.id.save);
        done = findViewById(R.id.done);

        loader = new ProgressDialog(TwoFactorAuthenficationActivity.this);

        verification.setVisibility(View.INVISIBLE);
        done.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

    }

    private void events() {

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String number = phone_number.getText().toString().trim();

                if (number.isEmpty()) {
                    notify_up("Phone number is required !");

                }else{

                    loader.setMessage("Processing...");
                    loader.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            number,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            TwoFactorAuthenficationActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks



                }


            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                loader.dismiss();


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loader.dismiss();
                notify_up("Invalid phone number");
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                loader.dismiss();
                mVerification = verificationId;

                phone_number.setVisibility(View.INVISIBLE);
                save.setVisibility(View.INVISIBLE);

                verification.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);

            }
        };
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String verify = verification.getText().toString().trim();

                if (verify.isEmpty())
                {
                    notify_up("Enter the code to verify your phone number");

                }else{


                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, verify);
                    sinInWithPhoneAuthCredential(credential);

                }



            }
        });

    }

    private void sinInWithPhoneAuthCredential(PhoneAuthCredential credential) {


        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {

                     phone_number.setVisibility(View.VISIBLE);
                     save.setVisibility(View.VISIBLE);
                     verification.setVisibility(View.INVISIBLE);
                     done.setVisibility(View.INVISIBLE);

                     notify_up("Account secured with two factor verification ,now login !");
                     signout();

                     go_to_home();

                 }else{
                     loader.dismiss();
                     notify_up("the code you entered is Invalid");
                 }
            }
        });
    }


    // Notifcatin center
    private void notify_up(String msg){
        Toast.makeText(TwoFactorAuthenficationActivity.this, msg, Toast.LENGTH_LONG).show();
    }
    private void go_to_home(){
        Intent intent = new Intent(TwoFactorAuthenficationActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        signout();
    }

    private void signout() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            FirebaseAuth.getInstance().signOut();
        }
    }
}
