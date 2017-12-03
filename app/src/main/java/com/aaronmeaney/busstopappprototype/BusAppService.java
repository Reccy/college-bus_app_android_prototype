package com.aaronmeaney.busstopappprototype;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BusAppService {

    @GET("/api/bus_position")
    Observable<BusPositionResult> busPosition();

    @GET("/api/bus_route")
    Observable<BusRouteResult> busRoute();

    @GET("/api/bus_driving")
    Observable<BusDrivingResult> busDriving();

    @POST("/api/bus_driving")
    Call<BusDrivingResult> setBusDriving(@Body DrivingRequest drivingRequest);
}
