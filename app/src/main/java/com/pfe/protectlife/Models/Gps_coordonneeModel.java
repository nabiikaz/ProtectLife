package com.pfe.protectlife.Models;

import androidx.annotation.NonNull;

public class Gps_coordonneeModel {

    double lat,lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @NonNull
    @Override
    public String toString() {
        return lat+","+lng;
    }
}
