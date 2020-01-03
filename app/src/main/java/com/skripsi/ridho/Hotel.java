package com.skripsi.ridho;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.skripsi.ridho.app.AppController;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Hotel extends AppCompatActivity {
    NetworkImageView Gambar;
    String IdHotel;
    TextView Update,Simpan;
    EditText Deskripsi,Alamat,Nama;
    RatingBar Bintang;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();


        Deskripsi = (EditText) findViewById(R.id.deskripsiHotel);
        Alamat = (EditText) findViewById(R.id.alamatHotel);
        Nama = (EditText) findViewById(R.id.namaHotel);
        Gambar = (NetworkImageView) findViewById(R.id.gambar);
        Bintang = (RatingBar) findViewById(R.id.hotelBintang);
        Update = (TextView) findViewById(R.id.update);
        Simpan = (TextView) findViewById(R.id.simpan);
        Gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery();
            }
        });
        GetHotel(user.get(SessionManager.KEY_ID));
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Simpan(
                        user.get(SessionManager.KEY_ID),
                        Nama.getText().toString(),
                        Alamat.getText().toString(),
                        Deskripsi.getText().toString(),
                        Bintang.getRating());

            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update(
                        IdHotel,
                        Nama.getText().toString(),
                        Alamat.getText().toString(),
                        Deskripsi.getText().toString(),
                        Bintang.getRating());
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            System.out.println(filePath);
            Glide.with(this).load(filePath).into(Gambar);
        }
    }
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
                else {
                    Snackbar.make(getCurrentFocus(), "Butuh Izin Untuk Mengambil Gambar !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        }
    }
    public void loadImagefromGallery() {
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, " "), PICK_IMAGE_REQUEST);
        }
    }

    public String getPath(Context context,Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public void GetHotel(String idUser){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.GetByIdUser+idUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()<1){
                                Gambar.setImageUrl(Constant.Image+"no_img.jpg",imageLoader);
                                Update.setVisibility(View.GONE);

                            }
                            else{
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(!jsonObject.getString("id_hotel").equals("fail")){
                                        IdHotel = jsonObject.getString("id_hotel");
                                        Nama.setText(jsonObject.getString("nama_hotel"));
                                        Alamat.setText(jsonObject.getString("alamat_hotel"));
                                        Deskripsi.setText(jsonObject.getString("deskripsi_hotel"));
                                        Gambar.setImageUrl(Constant.Image+jsonObject.getString("gambar_hotel"),imageLoader);
                                        Bintang.setRating(Float.parseFloat(jsonObject.getString("bintang_hotel")));
                                        Simpan.setVisibility(View.GONE);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getCurrentFocus(), "Gagal mengumpulkan data", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                }
        ) {
            @Override
            public String getBodyContentType() { return "application/x-www-form-urlencoded";}
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    public void Update(final String IdHotel, final String NamaHotel, final String AlamatHotel, final String DeskripsiHotel, final float BintangHotel){
        final Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
        if(filePath != null){
            String uploadId = UUID.randomUUID().toString();
            String path = getPath(getApplicationContext(),filePath);
            try {
                new MultipartUploadRequest(getApplicationContext(), uploadId, Constant.Hotel)
                        .addFileToUpload(path, "images") //Adding file
                        .addParameter("update","y")
                        .addParameter("id_hotel", IdHotel)
                        .addParameter("nama", NamaHotel)
                        .addParameter("alamat",AlamatHotel)
                        .addParameter("deskripsi",DeskripsiHotel)
                        .addParameter("bintang", String.valueOf(BintangHotel))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
                Snackbar.make(getCurrentFocus(), "Data Sudah diupdate", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(intent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Hotel,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            startActivity(intent);
                            Snackbar.make(getCurrentFocus(), "Data Sudah diupdate", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            startActivity(intent);
                            Snackbar.make(getCurrentFocus(), "Gagal update data", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("update","y");
                    params.put("id_hotel",IdHotel);
                    params.put("nama",NamaHotel);
                    params.put("alamat",AlamatHotel);
                    params.put("deskripsi",DeskripsiHotel);
                    params.put("bintang", String.valueOf(BintangHotel));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
    public void Simpan(final String IdUser, final String NamaHotel, final String AlamatHotel, final String DeskripsiHotel, final float BintangHotel){
        final Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
        if(filePath != null){
            String uploadId = UUID.randomUUID().toString();
            String path = getPath(getApplicationContext(),filePath);
            try {
                new MultipartUploadRequest(getApplicationContext(), uploadId, Constant.Hotel)
                        .addFileToUpload(path, "images") //Adding file
                        .addParameter("new","y")
                        .addParameter("id_user", IdUser)
                        .addParameter("nama", NamaHotel)
                        .addParameter("alamat",AlamatHotel)
                        .addParameter("deskripsi",DeskripsiHotel)
                        .addParameter("bintang", String.valueOf(BintangHotel))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
                Snackbar.make(getCurrentFocus(), "Data Sudah disimpan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(intent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Hotel,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            startActivity(intent);
                            Snackbar.make(getCurrentFocus(), "Data Sudah disimpan", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            startActivity(intent);
                            Snackbar.make(getCurrentFocus(), "Gagal menyimpan data", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("new","y");
                    params.put("id_user",IdUser);
                    params.put("nama",NamaHotel);
                    params.put("alamat",AlamatHotel);
                    params.put("deskripsi",DeskripsiHotel);
                    params.put("bintang", String.valueOf(BintangHotel));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}
