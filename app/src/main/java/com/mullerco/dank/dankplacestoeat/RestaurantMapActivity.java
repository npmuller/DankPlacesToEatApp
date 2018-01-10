package com.mullerco.dank.dankplacestoeat;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mullerco.dank.dankplacestoeat.user.User;

public class RestaurantMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private float mCenterLat;
    private float mCenterLon;
    private float mMapRadius;

    private GoogleMap mMap;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);
        // TODO : implement loading spinner
        // TODO : synchronously get user's settings (if a user exists). Else use defaults
        getUserSettings();
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
        // TODO : get the restaurants for the current map view
        getRestaurantsForCurrentMapView();
        // TODO : display dank places
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void getUserSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String mapCenter = "nate";
        //String mapCenter = sharedPref.getString(getString(), "");
        if(!mapCenter.isEmpty()) {
        } else {

        }
    }

    public void getRestaurantsForCurrentMapView() {
        if(mMap != null) {

        } else {
            // TODO : error condition. Handle it.
        }
    }
}
