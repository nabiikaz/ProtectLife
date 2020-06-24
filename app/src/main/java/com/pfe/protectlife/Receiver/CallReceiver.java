package com.pfe.protectlife.Receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pfe.protectlife.LocationManager;
import com.pfe.protectlife.MainActivity;
import com.pfe.protectlife.Models.CallModel;
import com.pfe.protectlife.Models.Gps_coordonneeModel;
import com.pfe.protectlife.Repositories.CallRepository;

import java.util.Date;

public class CallReceiver extends BroadcastReceiver {


    LocationManager locationManager ;

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;

    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;

    private static final String TAG = "CallReceiver";

    private Context context ;

    private Boolean locationRequested = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.w("intent " , intent.getAction().toString());

        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

            this.context  = context;







            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if(user == null )
                return;

            String mPhoneNumber = user.getPhoneNumber();





            sendCallWithLastLocation(mPhoneNumber.replace("+213","0"));




            //Toast.makeText(context, "outgoing Call Ringing" + savedNumber , Toast.LENGTH_SHORT).show();




        }
        else{
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }


        }
    }


    public void onCallStateChanged(Context context, int state) {
        if(lastState == state){
            //No change
            return;
        }


        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();


                Toast.makeText(context, "Incoming Call Ringing" , Toast.LENGTH_SHORT).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:

                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();




                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:

                if(lastState == TelephonyManager.CALL_STATE_RINGING){

                    Toast.makeText(context, "Ringing but no pickup" + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();
                }
                else if(isIncoming){

                    Toast.makeText(context, "Incoming " + savedNumber + " Call time " + callStartTime  , Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(context, "outgoing " + savedNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();

                }

                break;
        }
        lastState = state;

        savedNumber = "";
    }


    public void sendCallWithLastLocation(final String phoneNumber){

        if(!locationRequested){
            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            //TODO: UI updates.
                        }
                    }
                }
            };
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

            locationRequested = true;
        }




        LocationServices.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){

                    CallModel callModel = new CallModel();

                    Gps_coordonneeModel gps_coordonneeModel = new Gps_coordonneeModel();
                    gps_coordonneeModel.setLat(location.getLatitude());
                    gps_coordonneeModel.setLng(location.getLongitude());

                    callModel.setGps_coordonnee(gps_coordonneeModel);
                    callModel.setNumTel(phoneNumber);

                    CallRepository.getInstance().initiateCall(callModel);

                    Toast.makeText(context, "mmmmmPhoneNumber "+phoneNumber , Toast.LENGTH_LONG).show();


                }
            }
        });



    }



}
