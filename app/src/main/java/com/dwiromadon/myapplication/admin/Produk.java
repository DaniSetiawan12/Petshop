package com.dwiromadon.myapplication.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Produk extends AppCompatActivity {

    String _id, namaPetshop, idUser;
    Intent i;

    private RequestQueue mRequestQueue;
    ProgressDialog pDialog;

    EditText edtNamaProduk, edtHargaProduk;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);

        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        i = getIntent();
        _id = i.getStringExtra("_id");
        namaPetshop = i.getStringExtra("namaPetshop");
        idUser = i.getStringExtra("idUser");

        edtNamaProduk = (EditText) findViewById(R.id.edtNamaProduk);
        edtHargaProduk = (EditText) findViewById(R.id.edtHarga);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = edtNamaProduk.getText().toString();
                String harga = edtHargaProduk.getText().toString();

                try {
                    JSONObject jsonObj1=null;
                    JSONArray array=new JSONArray();
                    jsonObj1=new JSONObject();
                    array.put(new JSONObject().put("namaProduk", nama).put("hargaProduk", harga));
                    jsonObj1.put("produk", array);

                    Log.d("Data = ", jsonObj1.toString());
                    inputProduk(jsonObj1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void inputProduk(JSONObject datas){
        pDialog.setMessage("Mohon Tunggu .........");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, BaseURL.updatePetShhop+ _id, datas,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            if(status == false){
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Produk.this);
                                builder1.setMessage("Ingin menambah produk ? ");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(
                                        "Ya",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                edtNamaProduk.setText(null);
                                                edtHargaProduk.setText(null);
                                                dialog.cancel();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Tidak",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(Produk.this, Jasa.class);
                                                i.putExtra("_id", _id);
                                                i.putExtra("idUser", idUser);
                                                i.putExtra("namaPetshop", namaPetshop);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
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
                hideDialog();
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        mRequestQueue.add(req);
    }

    private void showDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}
