package com.dwiromadon.myapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dwiromadon.myapplication.R;
import com.dwiromadon.myapplication.adapter.AdapterPetShop;
import com.dwiromadon.myapplication.model.ModelPetshop;
import com.dwiromadon.myapplication.server.BaseURL;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataPetshop extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    ProgressDialog pDialog;

    AdapterPetShop adapter;
    ListView list;

    ArrayList<ModelPetshop> newsList = new ArrayList<ModelPetshop>();
    private RequestQueue mRequestQueue;

    Intent i;
    String idUser, namaPetshop, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_petshop);

        getSupportActionBar().setTitle("Data Petshop");
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        i = getIntent();
        idUser = i.getStringExtra("idUser");
        namaPetshop = i.getStringExtra("namaPetshop");
        username = i.getStringExtra("username");

        list = (ListView) findViewById(R.id.array_list);
        newsList.clear();
        adapter = new AdapterPetShop(DataPetshop.this, newsList);
        list.setAdapter(adapter);

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

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(DataPetshop.this, HomeAdmin.class);
        i.putExtra("_id", idUser);
        i.putExtra("namaPetshop", namaPetshop);
        startActivity(i);
        finish();
    }

    public void getAllPet(JSONObject jsonObject) {
        // Pass second argument as "null" for GET requests
        pDialog.setMessage("Loading");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BaseURL.getpetShopById + "0/" + idUser, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            boolean status = response.getBoolean("error");
                            if (status == false) {
                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    final ModelPetshop petShop = new ModelPetshop();
                                    final String _id = jsonObject.getString("_id");
                                    final String namaPetshop = jsonObject.getString("namaPetshop");
//                                    final String alamat = jsonObject.getString("alamat");
                                    final String notelp = jsonObject.getString("noTelp");
                                    final String arrGambar = jsonObject.getString("gambar");
                                    final String arrJamBuka = jsonObject.getString("jamBuka");
                                    final String arrProduk = jsonObject.getString("produk");
                                    final String arrJasa = jsonObject.getString("jasa");
                                    final String lat = jsonObject.getString("lat");
                                    final String lon = jsonObject.getString("lon");
                                    final String jarak = jsonObject.getString("jarak");
                                    JSONObject jobjJarak = new JSONObject(jarak);
                                    JSONArray arrayGambar = new JSONArray(arrGambar);
                                    String gambar = arrayGambar.get(0).toString();
                                    String jarakDistance = jobjJarak.getString("distance");
                                    String destination = jobjJarak.getString("destination");
                                    String duration = jobjJarak.getString("duration");
                                    petShop.setDuration(duration);
                                    petShop.setNamaPetshop(namaPetshop);
                                    petShop.setAlamat(destination);
                                    petShop.setNotelp(notelp);
                                    petShop.setGambar(gambar);
                                    petShop.setArrGambar(arrGambar);
                                    petShop.setJamBuka(arrJamBuka);
                                    petShop.setProduk(arrProduk);
                                    petShop.setJasa(arrJasa);
                                    petShop.set_id(_id);
                                    petShop.setLat(lat);
                                    petShop.setLon(lon);
                                    petShop.setJarak(jarakDistance);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // TODO Auto-generated method stub
                                            Intent a = new Intent(DataPetshop.this, DetailPetshop.class);
                                            a.putExtra("namaPetshop", newsList.get(position).getNamaPetshop());
                                            a.putExtra("_id", newsList.get(position).get_id());
                                            a.putExtra("alamat", newsList.get(position).getAlamat());
                                            a.putExtra("noTelp", newsList.get(position).getNotelp());
                                            a.putExtra("gambar", newsList.get(position).getArrGambar());
                                            a.putExtra("jambuka", newsList.get(position).getJamBuka());
                                            a.putExtra("produk", newsList.get(position).getProduk());
                                            a.putExtra("jasa", newsList.get(position).getJasa());
                                            a.putExtra("lat", newsList.get(position).getLat());
                                            a.putExtra("lon", newsList.get(position).getLon());
                                            a.putExtra("idUser", idUser);
                                            startActivity(a);
                                        }
                                    });
                                    newsList.add(petShop);
//                                    newsList.clear();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
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

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


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
                newsList.clear();
                JSONObject jsonObj1=null;
                jsonObj1=new JSONObject();
                jsonObj1.put("lat", String.valueOf(currentLatitude));
                jsonObj1.put("lon", String.valueOf(currentLongitude));

                Log.d("Data = ", jsonObj1.toString());
                getAllPet(jsonObj1);

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

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }
}
