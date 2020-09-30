package com.dwiromadon.myapplication.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dwiromadon.myapplication.R;
import com.dwiromadon.myapplication.server.BaseURL;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;
import com.vistrav.pop.Pop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DetailPetshop extends AppCompatActivity {

    Intent i;
    String _id, namaPetshop, alamat, noTelp, gambar, jamBuka, produk, jasa, lat, lon, idUser;
    ArrayList gam = new ArrayList();
    ArrayList listJamBuka = new ArrayList();
    ArrayList listProduk = new ArrayList();
    ArrayList listJasa = new ArrayList();
    CarouselView carouselView;

    FloatingActionButton btnEditNama, btnSimpanNama, btnEditAlamat,
            btnSimpanAlamst, btnEditNoTelp, btnSimpanNoTelp,
            floatingAddProduk, fabTambahJasa, fabUbahJamBuka, fabEditProduk, fabEditJasa;

    EditText edtPetshop, edtAlamat, edtNoTelp;
    Spinner spnJamBuka, spnProduk, spnJasa;
    FloatingActionButton fabAddGambar;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_petshop);

//        getSupportActionBar().setTitle("Detail Petshop");
        getSupportActionBar().hide();
        mRequestQueue = Volley.newRequestQueue(this);

        i = getIntent();
        _id = i.getStringExtra("_id");
        namaPetshop = i.getStringExtra("namaPetshop");
        alamat = i.getStringExtra("alamat");
        noTelp = i.getStringExtra("noTelp");
        gambar = i.getStringExtra("gambar");
        jamBuka = i.getStringExtra("jambuka");
        produk = i.getStringExtra("produk");
        jasa = i.getStringExtra("jasa");
        lat = i.getStringExtra("lat");
        lon = i.getStringExtra("lon");
        idUser = i.getStringExtra("idUser");

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        edtPetshop = (EditText) findViewById(R.id.edtPetshop);
        edtAlamat = (EditText) findViewById(R.id.edtAlamat);
        edtNoTelp = (EditText) findViewById(R.id.edtNotelp);

        btnEditNama = (FloatingActionButton) findViewById(R.id.fabButtonEditNama);
        btnEditAlamat = (FloatingActionButton) findViewById(R.id.fabButtonEditAlamat);
        btnEditNoTelp = (FloatingActionButton) findViewById(R.id.fabButtonEditNotelp);
        btnSimpanNama = (FloatingActionButton) findViewById(R.id.fabButtonSimpanNama);
        btnSimpanAlamst = (FloatingActionButton) findViewById(R.id.fabButtonSimpanAlamat);
        btnSimpanNoTelp = (FloatingActionButton) findViewById(R.id.fabButtonSimpanNotelp);
        floatingAddProduk = (FloatingActionButton) findViewById(R.id.floatingAddProduk);
        fabTambahJasa = (FloatingActionButton) findViewById(R.id.fabTambahJasa);
        fabUbahJamBuka = (FloatingActionButton) findViewById(R.id.fabUbahJamBuka);
        fabEditProduk   = (FloatingActionButton) findViewById(R.id.fabEditProduk);
        fabEditJasa   = (FloatingActionButton) findViewById(R.id.fabEditJasa);

        fabEditProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailPetshop.this, ListProduk.class);
                i.putExtra("produk", produk);
                i.putExtra("_id", _id);
                i.putExtra("namaPetshop", namaPetshop);
                i.putExtra("idUser", idUser);
                startActivity(i);
                finish();
            }
        });

        fabEditJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailPetshop.this, ListJasa.class);
                i.putExtra("jasa", jasa);
                i.putExtra("_id", _id);
                i.putExtra("idUser", idUser);
                i.putExtra("namaPetshop", namaPetshop);
                startActivity(i);
                finish();
            }
        });

        btnEditNama.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                btnSimpanNama.setVisibility(View.VISIBLE);
                btnEditNama.setVisibility(View.GONE);
                edtPetshop.setFocusableInTouchMode(true);
                edtPetshop.requestFocus();

                if (edtPetshop.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        btnEditAlamat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Pop.on(DetailPetshop.this)
                        .with()
                        .title("Ubah Lokasi")
                        .cancelable(false)
                        .layout(R.layout.pop_up)
                        .when(new Pop.Yah() {
                            @Override
                            public void clicked(DialogInterface dialog, View view) {
                                EditText lat = (EditText) view.findViewById(R.id.edtLat);
                                EditText lon = (EditText) view.findViewById(R.id.edtLon);

                                String lt = lat.getText().toString();
                                String lg = lon.getText().toString();
                                try {
                                    JSONObject jsonObj1 = null;
                                    jsonObj1 = new JSONObject();
                                    jsonObj1.put("lat", lt);
                                    jsonObj1.put("lon", lg);
                                    updateData(jsonObj1);
                                    Intent i = new Intent(DetailPetshop.this, DataPetshop.class);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .when(new Pop.Nah() { // ignore if dont need negative button
                            @Override
                            public void clicked(DialogInterface dialog, View view) {
                            }
                        })
                        .show(new Pop.View() { // assign value to view element
                            @Override
                            public void prepare(View view) {
                            }
                        });
            }
        });

        floatingAddProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pop.on(DetailPetshop.this)
                        .with()
                        .title("Tambah Produk")
                        .cancelable(false)
                        .layout(R.layout.pop_up_produk)
                        .when(new Pop.Yah() {
                            @Override
                            public void clicked(DialogInterface dialog, View view) {
                                EditText edtNnama = (EditText) view.findViewById(R.id.edtNamaProduk);
                                EditText edtHarga = (EditText) view.findViewById(R.id.edtHarga);

                                String nama = edtNnama.getText().toString();
                                String harga = edtHarga.getText().toString();
                                try {
                                    JSONObject jsonObj1 = null;
                                    JSONArray array = new JSONArray();
                                    jsonObj1 = new JSONObject();
                                    array.put(new JSONObject().put("namaProduk", nama).put("hargaProduk", harga));
                                    jsonObj1.put("produk", array);

                                    Log.d("Data = ", jsonObj1.toString());
                                    tambahData(jsonObj1);
                                    Intent i = new Intent(DetailPetshop.this, DataPetshop.class);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .when(new Pop.Nah() { // ignore if dont need negative button
                            @Override
                            public void clicked(DialogInterface dialog, View view) {
                            }
                        })
                        .show(new Pop.View() { // assign value to view element
                            @Override
                            public void prepare(View view) {
                                TextView txtJudul = (TextView) view.findViewById(R.id.txtJudul);
                                txtJudul.setText("Tambah Produk");
                            }
                        });
            }
        });

        fabTambahJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pop.on(DetailPetshop.this)
                        .with()
                        .title("Tambah Jasa")
                        .cancelable(false)
                        .layout(R.layout.pop_up_produk)
                        .when(new Pop.Yah() {
                            @Override
                            public void clicked(DialogInterface dialog, View view) {
                                EditText edtNnama = (EditText) view.findViewById(R.id.edtNamaProduk);
                                EditText edtHarga = (EditText) view.findViewById(R.id.edtHarga);

                                String nama = edtNnama.getText().toString();
                                String harga = edtHarga.getText().toString();
                                try {
                                    JSONObject jsonObj1 = null;
                                    JSONArray array = new JSONArray();
                                    jsonObj1 = new JSONObject();
                                    array.put(new JSONObject().put("namaJasa", nama).put("hargaJasa", harga));
                                    jsonObj1.put("jasa", array);

                                    Log.d("Data = ", jsonObj1.toString());
                                    tambahData(jsonObj1);
                                    Intent i = new Intent(DetailPetshop.this, DataPetshop.class);
                                    startActivity(i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .when(new Pop.Nah() { // ignore if dont need negative button
                            @Override
                            public void clicked(DialogInterface dialog, View view) {
                            }
                        })
                        .show(new Pop.View() { // assign value to view element
                            @Override
                            public void prepare(View view) {
                                TextView txtJudul = (TextView) view.findViewById(R.id.txtJudul);
                                txtJudul.setText("Tambah Jasa");
                            }
                        });
            }
        });

        btnEditNoTelp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                btnSimpanNoTelp.setVisibility(View.VISIBLE);
                btnEditNoTelp.setVisibility(View.GONE);
                edtNoTelp.setFocusableInTouchMode(true);
                edtNoTelp.requestFocus();

                if (edtNoTelp.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        btnSimpanNama.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                btnSimpanNama.setVisibility(View.GONE);
                btnEditNama.setVisibility(View.VISIBLE);
                edtPetshop.setFocusableInTouchMode(false);
                edtPetshop.setFocusable(false);
                String namaPet = edtPetshop.getText().toString();
                try {
                    JSONObject jsonObj1 = null;
                    jsonObj1 = new JSONObject();
                    jsonObj1.put("namaPetshop", namaPet);
                    updateData(jsonObj1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSimpanAlamst.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                btnSimpanAlamst.setVisibility(View.GONE);
                btnEditAlamat.setVisibility(View.VISIBLE);
                edtAlamat.setFocusableInTouchMode(false);
                edtAlamat.setFocusable(false);
            }
        });

        btnSimpanNoTelp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                btnSimpanNoTelp.setVisibility(View.GONE);
                btnEditNoTelp.setVisibility(View.VISIBLE);
                edtNoTelp.setFocusableInTouchMode(false);
                edtNoTelp.setFocusable(false);
                String noTel = edtNoTelp.getText().toString();
                try {
                    JSONObject jsonObj1 = null;
                    jsonObj1 = new JSONObject();
                    jsonObj1.put("noTelp", noTel);
                    updateData(jsonObj1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        spnJamBuka = (Spinner) findViewById(R.id.jamBuka);
        spnProduk = (Spinner) findViewById(R.id.produk);
        spnJasa = (Spinner) findViewById(R.id.jasa);

        fabAddGambar = (FloatingActionButton) findViewById(R.id.fabAddGambar);

        edtPetshop.setText(namaPetshop);
        edtAlamat.setText(alamat);
        edtNoTelp.setText(noTelp);

        try {
            JSONArray arrayGambar = new JSONArray(gambar);
            JSONArray arrayJamBuka = new JSONArray(jamBuka);
            JSONArray arrayProduk = new JSONArray(produk);
            JSONArray arrayJasa = new JSONArray(jasa);
            for (int i = 0; i < arrayGambar.length(); i++) {
                gam.add(BaseURL.baseUrl + "gambar/" + arrayGambar.get(i).toString());
            }

//            listJamBuka.add("--Jam Buka--");
            for (int i = 0; i < arrayJamBuka.length(); i++) {
                JSONObject objJamBuka = arrayJamBuka.getJSONObject(i);
                listJamBuka.add(objJamBuka.getString("hari") + " / " + objJamBuka.getString("jam"));
            }

//            listProduk.add("--Produk--");
            for (int i = 0; i < arrayProduk.length(); i++) {
                JSONObject objProduk = arrayProduk.getJSONObject(i);
                listProduk.add(objProduk.getString("namaProduk") + " / Rp." + objProduk.getString("hargaProduk"));
            }

//            listJasa.add("--Jasa--");
            for (int i = 0; i < arrayJasa.length(); i++) {
                JSONObject objJasa = arrayJasa.getJSONObject(i);
                listJasa.add(objJasa.getString("namaJasa") + " / Rp." + objJasa.getString("hargaJasa"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listJamBuka);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnJamBuka.setAdapter(adapter);

            ArrayAdapter<String> adapterProduk = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listProduk);
            adapterProduk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnProduk.setAdapter(adapterProduk);

            ArrayAdapter<String> adapterJasa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listJasa);
            adapterJasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnJasa.setAdapter(adapterJasa);

            carouselView.setPageCount(gam.size());
            carouselView.setImageListener(imageListener);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        fabAddGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailPetshop.this, TambahGambar.class);
                i.putExtra("_id", _id);
                startActivity(i);
                finish();
            }
        });

        fabUbahJamBuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailPetshop.this, UbahJamBuka.class);
                i.putExtra("_id", _id);
                i.putExtra("jamBuka", jamBuka);

                startActivity(i);
                finish();
            }
        });

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(String.valueOf(gam.get(position))).fit().centerCrop().into(imageView);
            //imageView.setImageResource(sampleImages[position]);
        }
    };

    View.OnClickListener pauseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            carouselView.pauseCarousel();
        }
    };

    public void updateData(JSONObject datas){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, BaseURL.updatePetDataShhop+ _id, datas,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        mRequestQueue.add(req);
    }

    public void tambahData(JSONObject datas){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, BaseURL.updatePetShhop+ _id, datas,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(DetailPetshop.this, DataPetshop.class);
                                i.putExtra("_id", _id);
                                i.putExtra("namaPetshop", namaPetshop);
                                i.putExtra("idUser", idUser);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        mRequestQueue.add(req);
    }
}
