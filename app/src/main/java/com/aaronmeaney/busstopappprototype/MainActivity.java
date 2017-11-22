package com.aaronmeaney.busstopappprototype;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker busMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup BusAppService
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("https://bus-app-prototype-api-reccy.c9users.io/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        BusAppService busAppService = retrofitBuilder.build().create(BusAppService.class);

        busAppService.busPosition()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .repeat()
            .subscribe(new Observer<BusPositionResult>() {
                @Override
                public void onSubscribe(Disposable d) {
                    System.out.println("BUS APP SERVICE SUBSCRIBED");
                }

                @Override
                public void onNext(BusPositionResult busPositionResult) {
                    System.out.println("BUS APP SERVICE RESULTS: " + busPositionResult.getBusPosition().toString());
                    setBusPositionMarker(busPositionResult.getBusPosition());
                }

                @Override
                public void onError(Throwable e) {
                    System.out.println("BUS APP SERVICE ERROR: " + e);
                }

                @Override
                public void onComplete() {
                    System.out.println("BUS APP SERVICE COMPLETE");
                }
            });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Set the bus marker's position by latitude/longitude. Create it if it's not been instantiated yet.
     * @param bp Bus Marker latitude/longitude object
     */
    private void setBusPositionMarker(BusPosition bp) {
        if (busMarker == null) {
            busMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(bp.getLatitude(), bp.getLongitude())).title("Bus Position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busMarker.getPosition(), 16f));
        } else {
            busMarker.setPosition(new LatLng(bp.getLatitude(), bp.getLongitude()));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("MAP READY");
    }


}
