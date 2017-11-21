package com.aaronmeaney.busstopappprototype;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface BusAppService {

    @GET("/api/bus_position")
    Observable<BusPositionResult> busPosition();
}
