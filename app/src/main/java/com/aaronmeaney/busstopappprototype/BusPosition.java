package com.aaronmeaney.busstopappprototype;

import com.google.gson.annotations.SerializedName;

public class BusPosition {
    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Latitude: " + getLatitude() + ", Longitude: " + getLongitude();
    }
}
