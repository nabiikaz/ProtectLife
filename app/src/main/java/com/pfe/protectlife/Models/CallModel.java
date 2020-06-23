package com.pfe.protectlife.Models;

import androidx.annotation.NonNull;

public class CallModel {


    public Gps_coordonneeModel getGps_coordonnee() {
        return gps_coordonnee;
    }

    public void setGps_coordonnee(Gps_coordonneeModel gps_coordonnee) {
        this.gps_coordonnee = gps_coordonnee;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    Gps_coordonneeModel gps_coordonnee;

    String numTel   ;





}
