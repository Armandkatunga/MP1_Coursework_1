package com.example.chuba;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    View view;

    private static final String TAG = "FragmentMap";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float ZOOM_SCALE = 15f;

    private Boolean accessGranted = false;
    private GoogleMap mapGoogle;

    private FusedLocationProviderClient fusedLocationProviderClient;


    public MapFragment() {
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapGoogle = googleMap;

        if (accessGranted) {
            getUserDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mapGoogle.setMyLocationEnabled(true);
            // mapGoogle.getUiSettings().setMyLocationButtonEnabled(false);
        }

    }

    private void iniMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);


    }
    private void getUserDeviceLocation(){

        Log.d(TAG, "getUserDeviceLocation : getting the user location" );

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (accessGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "OnComplete : Location found" );
                            Location currentTocation = (Location) task.getResult();

                            moveCameraTo(new LatLng(currentTocation.getLatitude(), currentTocation.getLongitude()) , ZOOM_SCALE);

                        }else{
                            Log.d(TAG, "OnComplete : Location not found" );
                            toastUp("Unable to get  current location");

                        }
                    }
                });
            }

        }catch (SecurityException e) {

        }
    }

    private void  moveCameraTo(LatLng latLng, float zoom){
        Log.d(TAG, "moveCameraTo : moving the map camera to lat :" + latLng.latitude + ", lng : " + latLng.longitude );
        mapGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }



    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map,container,false);

        getPermissionLocation();

        return view ;
    }


    public void getPermissionLocation(){

        String[] allowed = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                accessGranted = true ;
                iniMap();

            }else{

                ActivityCompat.requestPermissions(this.getActivity(),allowed,LOCATION_PERMISSION_REQUEST_CODE);
            }


        }else{
            ActivityCompat.requestPermissions(this.getActivity(),allowed,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        accessGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE : {
                if (grantResults.length > 0 ) {
                    for (int i =0 ; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            accessGranted = false ;
                            return;
                        }
                    }
                    accessGranted = true;
                    iniMap();
                }
            }
        }
    }

    private void toastUp(String msg)
    {
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }

}
