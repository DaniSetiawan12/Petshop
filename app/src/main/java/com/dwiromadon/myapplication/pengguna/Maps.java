package com.dwiromadon.myapplication.pengguna;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dwiromadon.myapplication.R;
import com.dwiromadon.myapplication.adapter.AdapterPenggunaPetshop;
import com.dwiromadon.myapplication.admin.DataPetshop;
import com.dwiromadon.myapplication.admin.DetailPetshop;
import com.dwiromadon.myapplication.model.ModelPetshop;
import com.dwiromadon.myapplication.server.BaseURL;
import com.dwiromadon.myapplication.users.LoginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vistrav.pop.Pop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Maps extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,OnMapReadyCallback, OnLocationClickListener, PermissionsListener, OnCameraTrackingChangedListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    private boolean isInTrackingMode;
    private RequestQueue mRequestQueue;
    ProgressDialog pDialog;

    ArrayList<ModelPetshop> newsList = new ArrayList<ModelPetshop>();

    String radius;
    Intent i, mapIntent;

    EditText edtSearch;
    Button btnSearch;
    FloatingActionButton btnRefresh;

    String goolgeMap = "com.google.android.apps.maps"; // identitas package aplikasi google masps android
    Uri gmmIntentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1IjoiZHdpcjQiLCJhIjoiY2syeGZhOG0zMGFjMjNuczg1MXJ1M3ptayJ9.m4xcIOu0VAbXgcmXqHnWDQ");
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setTitle("Petshop Maps");
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mapView = (MapView) findViewById(R.id.mapView);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnRefresh = (FloatingActionButton) findViewById(R.id.refrsh);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); //

        i = getIntent();
        if (i.hasExtra("radius")){
            radius = i.getStringExtra("radius");
        }else {
            radius = "0";
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String radius = edtSearch.getText().toString();
                if (radius.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Radius tidak boleh kosong", Toast.LENGTH_LONG).show();
                }else {
                    Intent i = new Intent(Maps.this, Maps.class);
                    i.putExtra("radius", radius);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Maps.this, Maps.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(Maps.this, HomePengguna.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
            }
        });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Create and customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.RED)
                    .foregroundDrawable(R.drawable.ic_navi)
                    .build();

// Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

// Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

// Add the location icon click listener
            locationComponent.addOnLocationClickListener(this);

// Add the camera tracking listener. Fires if the map camera is manually moved.
            locationComponent.addOnCameraTrackingChangedListener(this);

            findViewById(R.id.back_to_camera_tracking_mode).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isInTrackingMode) {
                        isInTrackingMode = true;
                        locationComponent.setCameraMode(CameraMode.TRACKING);
                        locationComponent.zoomWhileTracking(13f);
                    } else {
                        Toast.makeText(Maps.this, "-",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public void onLocationComponentClick() {
        if (locationComponent.getLastKnownLocation() != null) {
//            Toast.makeText(this, String.format(getString(R.string.mapbox_offline_error_region_definition_invalid),
//                    locationComponent.getLastKnownLocation().getLatitude(),
//                    locationComponent.getLastKnownLocation().getLongitude()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCameraTrackingDismissed() {
        isInTrackingMode = false;
    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {
// Empty on purpose
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Hehe", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "Hihi", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

        @Override
        protected void onResume() {
            super.onResume();
            mapView.onResume();
            mGoogleApiClient.connect();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mapView.onPause();
            Log.v(this.getClass().getSimpleName(), "onPause()");

            //Disconnect from API onPause()
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }

        @Override
        protected void onStop() {
            super.onStop();
            mapView.onStop();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            mapView.onSaveInstanceState(outState);
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mapView.onLowMemory();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();

            mapView.onDestroy();
        }

    public void getData(JSONObject jsonObject){

        final String URL = BaseURL.getJarak + radius;
        pDialog.setMessage("Loading");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            Log.d("Response = ", response.toString());
                            String result =  response.getString("data");
                            final JSONArray jsonArray = new JSONArray(result);

                            MarkerOptions markerOptions = new MarkerOptions();
                            IconFactory iconFactory = IconFactory.getInstance(Maps.this);
                            Icon icon = iconFactory.fromResource(R.drawable.marker_pet);
                            markerOptions.icon(icon);

                            for (int i =0; i < jsonArray.length(); i++){
                                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                                final ModelPetshop petShop = new ModelPetshop();
                                final String _id = jsonObject.getString("_id");
                                final String namaPetshop = jsonObject.getString("namaPetshop");
//                                final String alamat = jsonObject.getString("alamat");
                                final String notelp = jsonObject.getString("noTelp");
                                final String arrGambar = jsonObject.getString("gambar");
                                final String arrJamBuka = jsonObject.getString("jamBuka");
                                final String arrProduk = jsonObject.getString("produk");
                                final String arrJasa = jsonObject.getString("jasa");
                                final String lat = jsonObject.getString("lat");
                                final String lon = jsonObject.getString("lon");
                                final String jarak = jsonObject.getString("jarak");
                                JSONObject jobjJarak = new JSONObject(jarak);
                                String jarakDistance = jobjJarak.getString("distance");
                                final String destination = jobjJarak.getString("destination");
                                JSONArray arrayGambar = new JSONArray(arrGambar);
                                final String gambar = arrayGambar.get(0).toString();
                                markerOptions.setTitle(namaPetshop + " - " + jarakDistance);
                                markerOptions.setSnippet(destination);
                                markerOptions.position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));
                                mapboxMap.addMarker(markerOptions);
                                responseMarker();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                VolleyLog.e("Error: ", error.getMessage());
                Log.d("Error = ", error.toString());
            }
        });

        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue.add(req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            Log.d("Lat Lon = ", String.valueOf(currentLatitude) + " " + String.valueOf(currentLongitude));

            try {
                JSONObject jsonObj1=null;
                jsonObj1 =new JSONObject();
                jsonObj1.put("lat", String.valueOf(currentLatitude));
                jsonObj1.put("lon", String.valueOf(currentLongitude));

                Log.d("Data = ", jsonObj1.toString());
                final JSONObject finalJsonObj = jsonObj1;
                getData(finalJsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

//        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    public void responseMarker(){
        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                marker.hideInfoWindow();
                double dlat =marker.getPosition().getLatitude();
                double dlon =marker.getPosition().getLongitude();
                final String alamat = marker.getSnippet();
                final String nama = marker.getTitle();
                final String slat = String.valueOf(dlat);
                final String slon = String.valueOf(dlon);
                Pop.on(Maps.this)
                        .with()
                        .title("Informasi")
                        .cancelable(false)
                        .layout(R.layout.detail_maps)
                        .when(new Pop.Yah() {
                            @Override
                            public void clicked(DialogInterface dialog, View view) {

                            }
                        })
                        .show(new Pop.View() { // assign value to view element
                            @Override
                            public void prepare(View view) {
                                EditText edtNamPetsHop = (EditText) view.findViewById(R.id.edtPetshop);
                                EditText edAlamat = (EditText) view.findViewById(R.id.edtAlamat);
                                edtNamPetsHop.setText(nama);
                                edAlamat.setText(alamat);

                                Button goRoutes = (Button) view.findViewById(R.id.goRoutes);
                                goRoutes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        gmmIntentUri = Uri.parse("google.navigation:q=" + slat+","+slon);

                                        // Buat Uri dari intent gmmIntentUri. Set action => ACTION_VIEW
                                        mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                                        // Set package Google Maps untuk tujuan aplikasi yang di Intent yaitu google maps
                                        mapIntent.setPackage(goolgeMap);

                                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(mapIntent);
                                        } else {
                                            Toast.makeText(Maps.this, "Google Maps Belum Terinstal. Install Terlebih dahulu.",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                return false;
            }
        });
    }

}
