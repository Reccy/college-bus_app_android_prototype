package com.aaronmeaney.busstopappprototype;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker busPositionMarker;
    private Polyline busRoutePolyline;
    private ArrayList<BusPosition> busRouteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate member variables
        busRouteList = new ArrayList<>();

        // Setup BusAppService
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("https://bus-app-prototype-api-reccy.c9users.io/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        BusAppService busAppService = retrofitBuilder.build().create(BusAppService.class);

        // Subscribe to the bus position
        busAppService.busPosition()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .repeat()
            .retry()
            .subscribe(new Observer<BusPositionResult>() {
                @Override
                public void onSubscribe(Disposable d) {
                    System.out.println("BUS APP SERVICE SUBSCRIBED -> BUS_POSITION");
                }

                @Override
                public void onNext(BusPositionResult busPositionResult) {
                    System.out.println("BUS APP SERVICE RESULTS -> BUS_POSITION: " + busPositionResult.toString());
                    if (busPositionResult.getBusPosition() != null)
                    {
                        setBusPositionMarker(busPositionResult.getBusPosition());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    System.out.println("BUS APP SERVICE ERROR -> BUS_POSITION: " + e);
                }

                @Override
                public void onComplete() {
                    System.out.println("BUS APP SERVICE COMPLETE -> BUS_POSITION");
                }
            });

        // Subscribe to the bus route updates
        busAppService.busRoute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeat()
                .retry()
                .subscribe(new Observer<BusRouteResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("BUS APP SERVICE SUBSCRIBED -> BUS_ROUTE");
                    }

                    @Override
                    public void onNext(BusRouteResult busRouteResult) {
                        System.out.println("BUS APP SERVICE RESULTS -> BUS_ROUTE: " + busRouteResult.toString());
                        if (!busRouteList.equals(busRouteResult.getRoute()) && busRouteResult.getRoute() != null) {
                            busRouteList = busRouteResult.getRoute();
                            setBusRoutePolyline(busRouteResult.getRoute());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("BUS APP SERVICE ERROR -> BUS_ROUTE: " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("BUS APP SERVICE COMPLETE -> BUS_ROUTE");
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
        String snippetData = "Latitude: " + bp.getLatitude() + "\nLongitude: " + bp.getLongitude();
        if (busPositionMarker == null) {
            busPositionMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(bp.getLatitude(), bp.getLongitude()))
                    .title("Bus Position")
                    .snippet(snippetData));
            busPositionMarker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busPositionMarker.getPosition(), 16f));
        } else {
            busPositionMarker.setPosition(new LatLng(bp.getLatitude(), bp.getLongitude()));
            busPositionMarker.setSnippet(snippetData);

            // If the info window is open, update it with the new info
            if (busPositionMarker.isInfoWindowShown())
            {
                busPositionMarker.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(busPositionMarker.getPosition()), 200, null);
            }
        }
    }

    /**
     * Set the polyline visualisation of the bus's route.
     * @param bpList The list of bus positions that compose the route
     */
    private void setBusRoutePolyline(ArrayList<BusPosition> bpList) {
        System.out.println("Re-rendering Bus Route Polyline!");

        if (busRoutePolyline != null) {
            busRoutePolyline.remove();
        }

        PolylineOptions busRouteOptions = new PolylineOptions();
        busRouteOptions.width(10);
        busRouteOptions.color(Color.RED);

        for (BusPosition bp : bpList) {
            LatLng point = new LatLng(bp.getLatitude(),bp.getLongitude());
            busRouteOptions.add(point);
        }

        busRoutePolyline = mMap.addPolyline(busRouteOptions);
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

        // Setup the map info window adapter
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext();

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        System.out.println("MAP READY");
    }


}
