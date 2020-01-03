package com.skripsi.ridho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.skripsi.ridho.app.AppController;

public class Detail extends AppCompatActivity {
    NetworkImageView GambarHotel;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    TextView NamaHotel,AlamatHotel,DeskripsiHotel,LihatMap;
    RatingBar HotelBIntang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Bundle b = getIntent().getExtras();

        GambarHotel = (NetworkImageView) findViewById(R.id.gambarHotel);
        NamaHotel = (TextView) findViewById(R.id.namaHotel);
        AlamatHotel = (TextView) findViewById(R.id.alamatHotel);
        DeskripsiHotel = (TextView) findViewById(R.id.deskripsiHotel);
        LihatMap = (TextView) findViewById(R.id.lihatMap);
        HotelBIntang = (RatingBar) findViewById(R.id.hotelBintang);
        GambarHotel.setImageUrl(Constant.Image+b.getString("gambarHotel"),imageLoader);
        NamaHotel.setText(b.getString("namaHotel"));
        AlamatHotel.setText(b.getString("alamatHotel"));
        DeskripsiHotel.setText(b.getString("deskripsiHotel"));
        DeskripsiHotel.setMovementMethod(new ScrollingMovementMethod());
        HotelBIntang.setRating(Float.parseFloat(b.getString("bintangHotel")));
        LihatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("id_hotel",b.getString("idHotel"));
                intent.putExtra("longitude",b.getString("longHotel"));
                intent.putExtra("latitude",b.getString("latHotel"));
                intent.putExtra("title",b.getString("namaHotel"));
                intent.putExtra("snippet",b.getString("alamatHotel"));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
