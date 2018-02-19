package com.aaronmeaney.busstop;

import com.google.gson.annotations.SerializedName;

public class BusDrivingResult {
    @SerializedName("success")
    private boolean success;

    @SerializedName("is_driving")
    private boolean driving;

    boolean isSuccess() {
        return success;
    }

    boolean isDriving() {
        return driving;
    }

    @Override
    public String toString() {
        if (success)
        {
            return "success: " + isSuccess() + ", bus_driving: " + isDriving();
        }
        else
        {
            return "success: " + isSuccess();
        }
    }
}
