package com.mullerco.dank.dankplacestoeat;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mullerco.dank.dankplacestoeat.user.User;
import com.mullerco.dank.dankplacestoeat.utils.NotificationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static int EQUATOR_LENGTH_METERS = 40075004;

    private float mCenterLat;
    private float mCenterLon;
    private float mMapRadius;

    private GoogleMap mMap;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);
        // TODO : show loading spinner
        getUserMapSettings();
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
        LatLng mapCenter = new LatLng(mCenterLat, mCenterLon);
        float mapZoomLevel = getMapZoomForMetersWide(mMapRadius * 1600, mCenterLat);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(mapZoomLevel));
        getRestaurantsForCurrentMapView();
        // TODO : hide loading spinner
    }

    /**
     * Get stored settings related to the map.
     */
    public void getUserMapSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String mapCenterStr = sharedPref.getString("pref_key_map_center", "33.7676338, -84.5606888");
        String[] mapCenter = mapCenterStr.split(",");
        mCenterLat = Float.parseFloat(mapCenter[0]);
        mCenterLon = Float.parseFloat(mapCenter[1]);
        mMapRadius = Float.parseFloat(sharedPref.getString("pref_key_map_radius", "5"));
    }

    /**
     * Grab the dank places from the server and put them on the map.
     */
    public void getRestaurantsForCurrentMapView() {
        if(mMap != null) {
            RequestQueue queue = Volley.newRequestQueue(this);
            // TODO : build listing of API functions
            String requestUrl = getResources().getString(R.string.API_BASE_URL) + "getRestaurants";
            // Request a string response from the provided URL.
            JsonArrayRequest rqst = new JsonArrayRequest(requestUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Don't display more than 20 restaurants on the map at once
                        for(int i = 0; (i < response.length() && i < 20); i++) {
                            try {
                                JSONObject restaurant = (JSONObject) response.get(i);
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(restaurant.getDouble("la"), restaurant.getDouble("lo")))
                                        .title(restaurant.getString("n")));
                                        //.icon(BitmapDescriptorFactory.fromResource(iconResID)));
                            } catch (JSONException e) {
                                Log.e("ERROR", "Error adding restaurant to map: " + e.getMessage());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        NotificationUtils.ShowSimpleToastNotification(getApplicationContext(), getResources().getString(R.string.error_loading_restaurants));
                        Log.e("ERROR", "Error adding restaurant to map: " + e.getMessage());
                    }
            });
            // Add the request to the RequestQueue.
            queue.add(rqst);
        } else {
            // TODO : error condition. Handle it.
        }
    }

    private float getMapZoomForMetersWide(final float desiredMeters, final double latitude) {
        final float latitudinalAdjustment = (float)Math.cos( Math.PI * latitude / 180.0f );
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        float mapWidth = width / density;
        final float arg = EQUATOR_LENGTH_METERS * mapWidth * latitudinalAdjustment / ( desiredMeters * 256.0f );
        return (float)Math.log( arg ) / (float)Math.log( 2.0f );
    }
}
