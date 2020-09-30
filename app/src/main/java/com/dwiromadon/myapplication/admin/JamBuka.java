package com.dwiromadon.myapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
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

import java.util.Calendar;

public class JamBuka extends AppCompatActivity {

    EditText edtJamSenin, edtJamSelasa,edtJamRabu, edtJamKamis, edtJamJumat, edtJamSabtu, edtJamMinggu;
    Button btnNext;

    private RequestQueue mRequestQueue;
    ProgressDialog pDialog;

    String _id, namaPetshop, idUser;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jam_buka);
        getSupportActionBar().hide();

        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        edtJamSenin = (EditText) findViewById(R.id.edtJamSenin);
        edtJamSelasa = (EditText) findViewById(R.id.edtJamSelasa);
        edtJamRabu = (EditText) findViewById(R.id.edtJamRabu);
        edtJamKamis = (EditText) findViewById(R.id.edtJamKamis);
        edtJamJumat = (EditText) findViewById(R.id.edtJamJumat);
        edtJamSabtu = (EditText) findViewById(R.id.edtJamSabtu);
        edtJamMinggu = (EditText) findViewById(R.id.edtJamMinggu);

        i = getIntent();
        _id = i.getStringExtra("_id");
        namaPetshop = i.getStringExtra("namaPetshop");
        idUser = i.getStringExtra("idUser");

        btnNext = (Button) findViewById(R.id.btnSubmit);
        btnNext.setText("Next");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jamSenin = edtJamSenin.getText().toString();
                String jamSelasa = edtJamSelasa.getText().toString();
                String jamRabu = edtJamRabu.getText().toString();
                String jamKamis = edtJamKamis.getText().toString();
                String jamJumat = edtJamJumat.getText().toString();
                String jamSabtu = edtJamSabtu.getText().toString();
                String jamMinggu = edtJamMinggu.getText().toString();

                try {
                    JSONObject jsonObj1=null;
                    JSONArray array=new JSONArray();
                    jsonObj1=new JSONObject();
                    array.put(new JSONObject().put("hari", "Senin").put("jam", jamSenin))
                            .put(new JSONObject().put("hari", "Selasa").put("jam", jamSelasa))
                            .put(new JSONObject().put("hari", "Rabu").put("jam", jamRabu))
                            .put(new JSONObject().put("hari", "Kamis").put("jam", jamKamis))
                            .put(new JSONObject().put("hari", "Jum'at").put("jam", jamJumat))
                            .put(new JSONObject().put("hari", "Sabtu").put("jam", jamSabtu))
                            .put(new JSONObject().put("hari", "Minggu").put("jam", jamMinggu));
                    jsonObj1.put("jamBuka", array);

                    Log.d("Data = ", jsonObj1.toString());
                    inputJamBuka(jsonObj1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void inputJamBuka(JSONObject datas){
        pDialog.setMessage("Mohon Tunggu .........");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,BaseURL.updatePetShhop+ _id, datas,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            if(status == false){
                                Intent i = new Intent(JamBuka.this, Produk.class);
                                i.putExtra("_id", _id);
                                i.putExtra("idUser", idUser);
                                i.putExtra("namaPetshop", namaPetshop);
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
