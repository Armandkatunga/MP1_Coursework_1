package com.example.chuba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TwoFactorAuthenficationActivity extends AppCompatActivity {

    private EditText phone_number,verification;
    private Button save,done;
    private ProgressDialog loader;

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

    }

    private void events() {

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String number = phone_number.getText().toString().trim();

                if (number.isEmpty()) {
                    notify_up("Phone number is required !");

                }else{

                    notify_up("A code have sent to "+ number);

                    phone_number.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.INVISIBLE);


                    verification.setVisibility(View.VISIBLE);
                    done.setVisibility(View.VISIBLE);


                }


            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String verify = verification.getText().toString().trim();

                if (verify.isEmpty())
                {
                    notify_up("Enter the code to verify your phone number");

                }else{


                    phone_number.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);


                    verification.setVisibility(View.INVISIBLE);
                    done.setVisibility(View.INVISIBLE);

                    go_to_home();
                }



            }
        });

    }



    // Notifcatin center
    private void notify_up(String msg){
        Toast.makeText(TwoFactorAuthenficationActivity.this, msg, Toast.LENGTH_LONG).show();
    }
    private void go_to_home(){
        Intent intent = new Intent(TwoFactorAuthenficationActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
