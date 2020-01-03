package com.skripsi.ridho;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.skripsi.ridho.adapter.ListHotel;
import com.skripsi.ridho.model.ModelHotel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager lLayout;
    private List<ModelHotel> modelHotels= new ArrayList<ModelHotel>();
    LatLng origin, dest;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        GetHotel();
        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        lLayout = new GridLayoutManager(MainActivity.this,2);
        rvView.setLayoutManager(lLayout);
        adapter = new ListHotel(modelHotels);
        rvView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CariHotel(query.replace(" ","%20"));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                CariHotel(newText.replace(" ","%20"));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login) {
            Intent intent = new Intent(this,AdminActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void CariHotel(String keyword){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.CariHotel+keyword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            modelHotels.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelHotel modelHotel = new ModelHotel();
                                modelHotel.setNamaHotel(jsonObject.getString("nama_hotel"));
                                modelHotel.setGambarHotel(jsonObject.getString("gambar_hotel"));
                                modelHotel.setBintangHotel(jsonObject.getString("bintang_hotel"));
                                modelHotel.setAlamatHotel(jsonObject.getString("alamat_hotel"));
                                modelHotel.setDeskripsiHotel(jsonObject.getString("deskripsi_hotel"));
                                modelHotel.setLatHotel(jsonObject.getString("lat_hotel"));
                                modelHotel.setLongHotel(jsonObject.getString("long_hotel"));
                                dest = new LatLng(
                                        Double.parseDouble(jsonObject.getString("lat_hotel")),
                                        Double.parseDouble(jsonObject.getString("long_hotel"))
                                );
                                if(dest!=null && origin != null){
                                    String url = getUrl(origin,dest);
                                    String download = downloadUrl(url);
                                    JSONObject jObject;
                                    List<List<HashMap<String, String>>> disDur = null;
                                    try {
                                        jObject = new JSONObject(download);
                                        DataParser parser = new DataParser();
                                        disDur = parser.parsing(jObject);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    for(int ix=0; ix< disDur.size(); ix++){
                                        List<HashMap<String, String>> DD = disDur.get(ix);
                                        for(int j=0; j < DD.size(); j++){
                                            HashMap<String, String> point = DD.get(j);
                                            modelHotel.setWaktu(point.get("durText"));
                                            modelHotel.setJarak(point.get("disText"));
                                            modelHotel.setDisVal(point.get("disVal"));
                                            modelHotel.setWaktuU("Waktu : ");
                                            modelHotel.setJarakK("Jarak : ");
                                        }
                                    }
                                }
                                else{
                                    modelHotel.setDisVal("99999999");
                                }
                                modelHotels.add(modelHotel);
                            }
                            Collections.sort(modelHotels, new CustomComparator());
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getCurrentFocus(), "Terjadi Galat Saat Mengambil data", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void GetHotel(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Hotel,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            modelHotels.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ModelHotel modelHotel = new ModelHotel();
                                modelHotel.setNamaHotel(jsonObject.getString("nama_hotel"));
                                modelHotel.setGambarHotel(jsonObject.getString("gambar_hotel"));
                                modelHotel.setBintangHotel(jsonObject.getString("bintang_hotel"));
                                modelHotel.setAlamatHotel(jsonObject.getString("alamat_hotel"));
                                modelHotel.setDeskripsiHotel(jsonObject.getString("deskripsi_hotel"));
                                modelHotel.setLatHotel(jsonObject.getString("lat_hotel"));
                                modelHotel.setLongHotel(jsonObject.getString("long_hotel"));
                                dest = new LatLng(
                                        Double.parseDouble(jsonObject.getString("lat_hotel")),
                                        Double.parseDouble(jsonObject.getString("long_hotel"))
                                );
                                if(dest!=null && origin != null){
                                    String url = getUrl(origin,dest);
                                    String download = downloadUrl(url);
                                    JSONObject jObject;
                                    List<List<HashMap<String, String>>> disDur = null;
                                    try {
                                        jObject = new JSONObject(download);
                                        DataParser parser = new DataParser();
                                        disDur = parser.parsing(jObject);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    for(int ix=0; ix< disDur.size(); ix++){
                                        List<HashMap<String, String>> DD = disDur.get(ix);
                                        for(int j=0; j < DD.size(); j++){
                                            HashMap<String, String> point = DD.get(j);
                                            modelHotel.setWaktu(point.get("durText"));
                                            modelHotel.setJarak(point.get("disText"));
                                            modelHotel.setWaktuU("Waktu : ");
                                            modelHotel.setJarakK("Jarak : ");

                                        }
                                    }
                                }

                                modelHotels.add(modelHotel);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getCurrentFocus(), "Terjadi Galat Saat Mengambil data", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            origin = new LatLng(location.getLatitude(),location.getLongitude());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,
                            "permission was granted, :)",
                            Toast.LENGTH_LONG).show();

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, this);
                    }catch(SecurityException e){
                        Toast.makeText(MainActivity.this,
                                "SecurityException:\n" + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "permission denied, ...:(",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this,
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
    }

    private String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException, IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;

    }

    public class CustomComparator implements Comparator<ModelHotel> {// may be it would be Model
        @Override
        public int compare(ModelHotel o1, ModelHotel o2) {
            if (o1 != null && o2 != null && o2.getDisVal() != null && o1.getDisVal() != null){
                return o2.getDisVal().compareTo(o1.getDisVal());
            }
            else{
                return -1;
            }
        }
    }
}
