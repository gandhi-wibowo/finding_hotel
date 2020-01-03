package com.skripsi.ridho.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.skripsi.ridho.Constant;
import com.skripsi.ridho.Detail;
import com.skripsi.ridho.R;
import com.skripsi.ridho.app.AppController;
import com.skripsi.ridho.model.ModelHotel;

import java.util.List;

/**
 * Created by gandhi on 7/25/17.
 */

public class ListHotel extends RecyclerView.Adapter<ListHotel.ViewHolder> {
    private List<ModelHotel> modelHotels;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ListHotel(List<ModelHotel> modelHotels){
        this.modelHotels = modelHotels;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hotel, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelHotel modelHotel = modelHotels.get(position);
        if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();
        holder.GambarHotel.setImageUrl(Constant.Image+modelHotel.getGambarHotel(),imageLoader);
        holder.NamaHotel.setText(modelHotel.getNamaHotel());
        holder.BintangHotel.setRating(Float.parseFloat(modelHotel.getBintangHotel()));
        holder.AlamatHotel.setText(modelHotel.getAlamatHotel());
        holder.Jarak.setText(modelHotel.getJarak());
        holder.Waktu.setText(modelHotel.getWaktu());
        holder.JarakK.setText(modelHotel.getJarakK());
        holder.WaktuU.setText(modelHotel.getWaktuU());
    }

    @Override
    public int getItemCount() {
        return this.modelHotels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView GambarHotel;
        public TextView NamaHotel,AlamatHotel,DeskripsiHotel,Jarak,Waktu,JarakK,WaktuU;
        public RatingBar BintangHotel;

        public ViewHolder(View itemView) {
            super(itemView);
            GambarHotel = (NetworkImageView) itemView.findViewById(R.id.gambar_hotel);
            NamaHotel = (TextView) itemView.findViewById(R.id.namaHotel);
            BintangHotel = (RatingBar) itemView.findViewById(R.id.hotelBintang);
            AlamatHotel = (TextView) itemView.findViewById(R.id.alamatHotel);
            Jarak = (TextView) itemView.findViewById(R.id.Jarak);
            JarakK = (TextView) itemView.findViewById(R.id.JarakK);
            Waktu = (TextView) itemView.findViewById(R.id.Waktu);
            WaktuU = (TextView) itemView.findViewById(R.id.WaktuU);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ModelHotel modelHotel = modelHotels.get(position);
                    Intent intent = new Intent(v.getContext(), Detail.class);
                    intent.putExtra("idHotel",modelHotel.getIdHotel());
                    intent.putExtra("namaHotel",modelHotel.getNamaHotel());
                    intent.putExtra("alamatHotel",modelHotel.getAlamatHotel());
                    intent.putExtra("gambarHotel",modelHotel.getGambarHotel());
                    intent.putExtra("latHotel",modelHotel.getLatHotel());
                    intent.putExtra("longHotel",modelHotel.getLongHotel());
                    intent.putExtra("deskripsiHotel",modelHotel.getDeskripsiHotel());
                    intent.putExtra("bintangHotel",modelHotel.getBintangHotel());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
