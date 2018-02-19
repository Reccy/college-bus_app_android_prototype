package com.aaronmeaney.busstop;

import com.google.gson.annotations.SerializedName;

public class BusPositionResult {
    @SerializedName("success")
    private boolean success;

    @SerializedName("bus_position")
    private BusPosition busPosition;

    boolean isSuccess() {
        return success;
    }

    BusPosition getBusPosition() {
        return busPosition;
    }

    @Override
    public String toString() {
        if (busPosition != null)
        {
            return "success: " + isSuccess() + ", bus_position: " + getBusPosition().toString();
        }
        else
        {
            return "success: " + isSuccess();
        }
    }
}
