package com.aaronmeaney.busstopappprototype;
import retrofit2.Call;
import retrofit2.http.GET;

public interface BusAppService {

    @GET("/api/bus_position")
    Call<BusPositionResult> busPosition();
}
