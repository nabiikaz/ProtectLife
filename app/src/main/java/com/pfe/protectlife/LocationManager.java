package com.pfe.protectlife;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.pfe.protectlife.Models.CallModel;
import com.pfe.protectlife.Models.Gps_coordonneeModel;
import com.pfe.protectlife.Repositories.CallRepository;

public class LocationManager {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean requestingLocationUpdates;

    private static final String TAG = "LocationManager";

    //the context of the activity that created this com.pfe.protectlife.LocationManager Instance
    private Context context;

    public LocationManager(Context context, LocationCallback locationCallback){

        this.context = context;


        createLocationRequest();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        this.locationCallback = locationCallback;



    }

    public LocationManager(Context context){

        this.context = context;


        createLocationRequest();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);




        createLocationRequest();




    }




    private boolean checkPlayServices() {
        /**GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                //finish();
            }

            return false;
        }
        */
        return true;
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void resumeRequestingLocationUpdates(){
        if(requestingLocationUpdates){
            startLocationUpdates();
        }else{
            stopLocationUpdates();
        }
    }


    public void getLastLocation(){
        final Location[] loc = {null};

        /*fusedLocationClient.getLastLocation()
                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Toast.makeText(context,"Latitude"+ location.getLatitude()+" , Longitude"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location = task.getResult();
                    if(location != null){

                        //Toast.makeText(context,"Latitude"+ location.getLatitude()+" , Longitude"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }


        });



    }

    public void sendCallWithLastLocation(final String phoneNumber){



        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: ");
                    Location location = task.getResult();
                    if(location != null){

                        Log.d(TAG, "onComplete: location != null ");



                        CallModel callModel = new CallModel();

                        Gps_coordonneeModel gps_coordonneeModel = new Gps_coordonneeModel();
                        gps_coordonneeModel.setLat(location.getLatitude());
                        gps_coordonneeModel.setLng(location.getLongitude());

                        callModel.setGps_coordonnee(gps_coordonneeModel);
                        callModel.setNumTel(phoneNumber);

                        CallRepository.getInstance().initiateCall(callModel);

                        Toast.makeText(context,"Latitude"+ location.getLatitude()+" , Longitude"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try {
                        throw task.getException();

                    }catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }


        });



    }


    
}
