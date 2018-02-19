package com.aaronmeaney.busstop;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BusRouteResult {
    @SerializedName("success")
    boolean success;

    @SerializedName("route")
    ArrayList<BusPosition> route;

    boolean isSuccess() {
        return success;
    }

    ArrayList<BusPosition> getRoute() {
        return route;
    }

    @Override
    public String toString() {
        if (route != null)
        {
            return "success: " + isSuccess() + ", bus_position: " + route.toString();
        }
        else
        {
            return "success: " + isSuccess();
        }

    }
}