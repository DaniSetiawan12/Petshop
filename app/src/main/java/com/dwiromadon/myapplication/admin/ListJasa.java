package com.dwiromadon.myapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.dwiromadon.myapplication.adapter.AdapterProduk;
import com.dwiromadon.myapplication.model.ModelProduk;
import com.dwiromadon.myapplication.server.BaseURL;
import com.vistrav.pop.Pop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListJasa extends AppCompatActivity {


    Intent i;
    String jasa, _id, namaPetshop, idUser;

    AdapterProduk adapter;
    ListView list;

    ArrayList<ModelProduk> newsList = new ArrayList<ModelProduk>();

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jasa);

        getSupportActionBar().setTitle("Edit / Hapus Jasa");
        mRequestQueue = Volley.newRequestQueue(this);

        i = getIntent();
        jasa = i.getStringExtra("jasa");
        _id = i.getStringExtra("_id");
        idUser = i.getStringExtra("idUser");
        namaPetshop = i.getStringExtra("namaPetshop");
        list = (ListView) findViewById(R.id.array_list);
        newsList.clear();
        adapter = new AdapterProduk(ListJasa.this, newsList);
        list.setAdapter(adapter);
        jasa();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Pop.on(ListJasa.this)
                        .with()
                        .title("Edit / Hapus Jasa")
                        .cancelable(false)
                        .layout(R.layout.edit_hapus_produk)
                        .when(new Pop.Nah() { // ignore if dont need negative button
                            @Override
                            public void clicked(DialogInterface dialog, View view) {
                            }
                        })
                        .show(new Pop.View() { // assign value to view element
                            @Override
                            public void prepare(View view) {
                                TextView txtJudul = (TextView) view.findViewById(R.id.txtJudul);
                                txtJudul.setText("Edit / Hapus Jasa");
                                final EditText edtNnama = (EditText) view.findViewById(R.id.edtNamaProduk);
                                final EditText edtHarga = (EditText) view.findViewById(R.id.edtHarga);
                                Button btnEdit    = (Button) view.findViewById(R.id.btnEdit);
                                Button btnHapus    = (Button) view.findViewById(R.id.btnHapus);
                                edtNnama.setText(newsList.get(position).getNamaProduk());
                                edtHarga.setText(newsList.get(position).getHargaProduk());

                                btnEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String strNama = edtNnama.getText().toString();
                                        String strHarga = edtHarga.getText().toString();

                                        try {
                                            JSONObject jsonObj1=null;
                                            JSONArray array=new JSONArray();
                                            jsonObj1=new JSONObject();
                                            array.put(new JSONObject()
                                                    .put("namaJasa", strNama)
                                                    .put("hargaJasa", strHarga));
                                            jsonObj1.put("jasa", array);
                                            ubahJasa(jsonObj1, newsList.get(position).get_id());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                btnHapus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hapusJasa(newsList.get(position).get_id());
                                    }
                                });

                            }
                        });
            }
        });
    }

    public void jasa() {
        try {
            JSONArray arrayProduk = new JSONArray(jasa);
            for (int i = 0; i < arrayProduk.length(); i++){
                JSONObject objProduk = arrayProduk.getJSONObject(i);
                ModelProduk modelProduk = new ModelProduk();
                String strNamaProduk = objProduk.getString("namaJasa");
                String strHargaProduk = objProduk.getString("hargaJasa");
                String id = objProduk.getString("_id");
                modelProduk.setNamaProduk(strNamaProduk);
                modelProduk.setHargaProduk(strHargaProduk);
                modelProduk.set_id(id);

                newsList.add(modelProduk);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ubahJasa(JSONObject datas, String id){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, BaseURL.updatePetDataJasa+ _id +"/" +id, datas,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ListJasa.this, DataPetshop.class);
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

    public void hapusJasa(String id){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, BaseURL.hapusPetDataJasa+ _id +"/" +id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ListJasa.this, DataPetshop.class);
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
