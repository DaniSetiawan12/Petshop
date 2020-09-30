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
import com.dwiromadon.myapplication.server.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPenggunaPetshop extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelPetshop> item;

    public AdapterPenggunaPetshop(Activity activity, List<ModelPetshop> item) {
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
            convertView = inflater.inflate(R.layout.content_petshop, null);


        TextView namaPetshop = (TextView) convertView.findViewById(R.id.txtNamaPetshop);
        ImageView gambar     = (ImageView) convertView.findViewById(R.id.gambarPetshop);
        TextView notelp      = (TextView) convertView.findViewById(R.id.txtNoTelp);
        TextView txtDuration      = (TextView) convertView.findViewById(R.id.txtDuration);
        TextView jarak      = (TextView) convertView.findViewById(R.id.txtJarak);

        namaPetshop.setText(item.get(position).getNamaPetshop());
        notelp.setText(item.get(position).getNotelp());
        txtDuration.setText(item.get(position).getDuration().replace("mins", "menit"));
        jarak.setText(item.get(position).getJarak());
        Picasso.get().load(BaseURL.baseUrl + "gambar/" + item.get(position).getGambar())
                .resize(40, 40)
                .centerCrop()
                .into(gambar);
        return convertView;
    }
}