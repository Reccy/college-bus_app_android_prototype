package com.aaronmeaney.busstopappprototype;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    // TODO: Add zoom only on startup
    // TODO: Add continuous update for marker
    // TODO: Change marker style to look like the user icon on Google Maps
    // TODO: Add toggle for user and GPS position

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup BusAppService
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("https://bus-app-prototype-api-reccy.c9users.io/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();

        BusAppService service = retrofit.create(BusAppService.class);
        Call<BusPositionResult> call = service.busPosition();

        call.enqueue(new Callback<BusPositionResult>() {
            @Override
            public void onResponse(Call<BusPositionResult> call, Response<BusPositionResult> response) {
                final BusPositionResult bp = response.body();

                if (bp != null) {
                    System.out.println("RESPONSE: " + response.body());
                    Toast.makeText(MainActivity.this, bp.toString(), Toast.LENGTH_LONG).show();

                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {

                            // Add a marker for the bus
                            LatLng busMarker = new LatLng(bp.getBusPosition().getLatitude(), bp.getBusPosition().getLongitude());
                            mMap.addMarker(new MarkerOptions().position(busMarker).title("Bus Position"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busMarker, 16f));
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "BusPosition is null!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BusPositionResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "BusAppService Error!", Toast.LENGTH_SHORT).show();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    }
}
