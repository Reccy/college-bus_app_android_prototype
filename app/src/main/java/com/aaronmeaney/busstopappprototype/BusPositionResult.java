package com.aaronmeaney.busstopappprototype;

import com.google.gson.annotations.SerializedName;

public class BusPositionResult {
    @SerializedName("success")
    boolean success;

    @SerializedName("bus_position")
    BusPosition busPosition;

    public boolean isSuccess() {
        return success;
    }

    public BusPosition getBusPosition() {
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
