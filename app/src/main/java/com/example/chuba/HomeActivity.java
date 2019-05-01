package com.example.chuba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chuba.Fragments.AboutusFragment;
import com.example.chuba.Fragments.indexFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button home_btn,map_btn,about_btn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.map);
        navigationView.setNavigationItemSelectedListener(this);

        ini();
        events();

        updateUI();
    }


    // initialization | attachment
    private void ini() {

        home_btn = findViewById(R.id.home_btn);
        map_btn = findViewById(R.id.map_btn);
        about_btn = findViewById(R.id.about_btn);

        getSupportActionBar().setTitle("Chuba");
        getSupportFragmentManager().beginTransaction().replace(R.id.load, new indexFragment()).commit();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }
    // event binding
    private void events() {

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setTitle("Chuba");
                getSupportFragmentManager().beginTransaction().replace(R.id.load, new indexFragment()).commit();
            }
        });

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setTitle("Map");
                getSupportFragmentManager().beginTransaction().replace(R.id.load, new MapFragment()).commit();
            }
        });

        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setTitle("About Us");
                getSupportFragmentManager().beginTransaction().replace(R.id.load, new AboutusFragment()).commit();
            }
        });


    }

    // Notifcatin center
    private void notify(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    // Redirection
    private  void switchToActivity(){

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homi) {
            // Handle the camera action
        } else if (id == R.id.mapi) {

        } else if (id == R.id.contact) {

        } else if (id == R.id.account) {

        } else if (id == R.id.logout) {
            MapFragment mapFragment= new MapFragment();
            FragmentManager manager= getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.map,mapFragment).commit();
            signout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateUI(){

        NavigationView slideOutView = findViewById(R.id.map);
        View this_ = slideOutView.getHeaderView(0);

        TextView names = this_.findViewById(R.id.user_names);
        ImageView images = this_.findViewById(R.id.user_profile);

        names.setText(currentUser.getDisplayName());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(images);
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void signout() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            FirebaseAuth.getInstance().signOut();

        }

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);


    }
}
