package com.aaronmeaney.busstop;

import com.google.gson.annotations.SerializedName;

public class DrivingRequest {

    @SerializedName("is_driving")
    private boolean isDriving;

    public DrivingRequest(boolean isDriving) {
        this.isDriving = isDriving;
    }
}
