package com.dwiromadon.myapplication.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UbahJamBuka extends AppCompatActivity {

    Intent i;
    String _id, jamBuka;

    private RequestQueue mRequestQueue;

    EditText edtJamSenin, edtJamSelasa,edtJamRabu, edtJamKamis, edtJamJumat, edtJamSabtu, edtJamMinggu;
    FloatingActionButton fabButtonSimpanJamSenin, fabButtonSimpanJamSelasa, fabButtonSimpanJamRabu, fabButtonSimpanJamKamis,
            fabButtonSimpanJamJumat, fabButtonSimpanJamSabtu, fabButtonSimpanJamMinggu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubah_jam_buka);
        getSupportActionBar().hide();

        mRequestQueue = Volley.newRequestQueue(this);

        edtJamSenin = (EditText) findViewById(R.id.edtJamSenin);
        edtJamSelasa = (EditText) findViewById(R.id.edtJamSelasa);
        edtJamRabu = (EditText) findViewById(R.id.edtJamRabu);
        edtJamKamis = (EditText) findViewById(R.id.edtJamKamis);
        edtJamJumat = (EditText) findViewById(R.id.edtJamJumat);
        edtJamSabtu = (EditText) findViewById(R.id.edtJamSabtu);
        edtJamMinggu = (EditText) findViewById(R.id.edtJamMinggu);

        fabButtonSimpanJamSenin = (FloatingActionButton) findViewById(R.id.fabButtonSimpanJamSenin);
        fabButtonSimpanJamSelasa = (FloatingActionButton) findViewById(R.id.fabButtonJamSelasa);
        fabButtonSimpanJamRabu = (FloatingActionButton) findViewById(R.id.fabButtonJamRabu);
        fabButtonSimpanJamKamis = (FloatingActionButton) findViewById(R.id.fabButtonJamKamis);
        fabButtonSimpanJamJumat = (FloatingActionButton) findViewById(R.id.fabButtonJamJumat);
        fabButtonSimpanJamSabtu = (FloatingActionButton) findViewById(R.id.fabButtonJamSabtu);
        fabButtonSimpanJamMinggu = (FloatingActionButton) findViewById(R.id.fabButtonJamMinggu);

        i = getIntent();
        _id = i.getStringExtra("_id");
        jamBuka = i.getStringExtra("jamBuka");

        JSONArray arrayJamBuka = null;
        try {
            arrayJamBuka = new JSONArray(jamBuka);
            for (int i = 0; i < arrayJamBuka.length(); i++) {
                JSONObject objJamBuka = arrayJamBuka.getJSONObject(i);
                if(objJamBuka.getString("hari").equals("Senin")){
                    edtJamSenin.setText(objJamBuka.getString("jam"));
                }else if(objJamBuka.getString("hari").equals("Selasa")){
                    edtJamSelasa.setText(objJamBuka.getString("jam"));
                }else if(objJamBuka.getString("hari").equals("Rabu")){
                    edtJamRabu.setText(objJamBuka.getString("jam"));
                }else if(objJamBuka.getString("hari").equals("Kamis")){
                    edtJamKamis.setText(objJamBuka.getString("jam"));
                }else if(objJamBuka.getString("hari").equals("Jum'at")){
                    edtJamJumat.setText(objJamBuka.getString("jam"));
                }else if(objJamBuka.getString("hari").equals("Sabtu")){
                    edtJamSabtu.setText(objJamBuka.getString("jam"));
                }else if(objJamBuka.getString("hari").equals("Minggu")){
                    edtJamMinggu.setText(objJamBuka.getString("jam"));
                }else {
                    Toast.makeText(getApplicationContext(), "Tidak ada data", Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fabButtonSimpanJamSenin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObj1=new JSONObject();
                    jsonObj1.put("jam", edtJamSenin.getText().toString());
                    ubahJamBuka(jsonObj1, "Senin");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fabButtonSimpanJamSelasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObj1=new JSONObject();
                    jsonObj1.put("jam", edtJamSelasa.getText().toString());
                    ubahJamBuka(jsonObj1, "Selasa");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fabButtonSimpanJamRabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObj1=new JSONObject();
                    jsonObj1.put("jam", edtJamRabu.getText().toString());
                    ubahJamBuka(jsonObj1, "Rabu");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fabButtonSimpanJamKamis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObj1=new JSONObject();
                    jsonObj1.put("jam", edtJamKamis.getText().toString());
                    ubahJamBuka(jsonObj1, "Kamis");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fabButtonSimpanJamJumat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObj1=new JSONObject();
                    jsonObj1.put("jam", edtJamJumat.getText().toString());
                    ubahJamBuka(jsonObj1, "Jum'at");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fabButtonSimpanJamSabtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObj1=new JSONObject();
                    jsonObj1.put("jam", edtJamSabtu.getText().toString());
                    ubahJamBuka(jsonObj1, "Sabtu");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        fabButtonSimpanJamMinggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObj1=new JSONObject();
                    jsonObj1.put("jam", edtJamMinggu.getText().toString());
                    ubahJamBuka(jsonObj1, "Minggu");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(UbahJamBuka.this, DataPetshop.class);
        startActivity(i);
        finish();
    }

    public void ubahJamBuka(JSONObject datas, String hari){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,BaseURL.updatePetDataShhop+ _id +"/" +hari, datas,
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
}
