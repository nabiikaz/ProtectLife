package com.pfe.protectlife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.pfe.protectlife.Models.CallModel;
import com.pfe.protectlife.Models.Gps_coordonneeModel;
import com.pfe.protectlife.Repositories.CallRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermissions();

        CallModel callModel = new CallModel();

        Gps_coordonneeModel gps_coordonneeModel = new Gps_coordonneeModel();
        gps_coordonneeModel.setLat(31.4542);
        gps_coordonneeModel.setLng(-1.4542);

        callModel.setGps_coordonnee(gps_coordonneeModel);
        callModel.setNumTel("0555655100");

        CallRepository.getInstance().initiateCall(callModel);

    }

    private void initPermissions(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},1);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }

    }
}
