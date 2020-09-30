package com.dwiromadon.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwiromadon.myapplication.R;
import com.dwiromadon.myapplication.model.ModelPetshop;
import com.dwiromadon.myapplication.model.ModelProduk;
import com.dwiromadon.myapplication.server.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProduk extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelProduk> item;

    public AdapterProduk(Activity activity, List<ModelProduk> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_produk, null);


        TextView namaProduk = (TextView) convertView.findViewById(R.id.txtNamaProduk);
        TextView hargaProduk      = (TextView) convertView.findViewById(R.id.txtHarga);

        namaProduk.setText(item.get(position).getNamaProduk());
        hargaProduk.setText("Rp. " + item.get(position).getHargaProduk());
        return convertView;
    }
}