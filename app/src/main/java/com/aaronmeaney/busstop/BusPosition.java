package com.aaronmeaney.busstop;

import com.google.gson.annotations.SerializedName;

public class BusPosition {
    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Latitude: " + getLatitude() + ", Longitude: " + getLongitude();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusPosition that = (BusPosition) o;

        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
