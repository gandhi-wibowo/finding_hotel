package com.skripsi.ridho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Daftar extends AppCompatActivity {

    TextView Login,Daftar;
    EditText Username,Hp,Email,Password,RePassword;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        Username = (EditText) findViewById(R.id.username);
        Hp = (EditText) findViewById(R.id.noHp);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        RePassword = (EditText) findViewById(R.id.repassword);
        Login = (TextView) findViewById(R.id.create);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Daftar.this,Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Daftar = (TextView) findViewById(R.id.signin1);
        Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!validate()) {
                    onDaftarFailed("Gagal Mendaftar");
                    return;
                }
                progressDialog = ProgressDialog.show(v.getContext(),"Mendaftar","Auth . .",true);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Daftar, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(jsonObject.getString("nama_user").equals("email_sudah_terdaftar")){
                                    onDaftarFailed("Email Sudah Terdaftar");
                                }
                                else{
                                    Intent intent = new Intent(Daftar.this,Login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Snackbar.make(v, "Silahkan Login Untuk Melanjutkan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Snackbar.make(v, "Cek Koneksi Internet !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nama", Username.getText().toString());
                        params.put("email", Email.getText().toString());
                        params.put("hp", Hp.getText().toString());
                        params.put("password", RePassword.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });


    }
    public void onDaftarFailed(String pesan) {
        Snackbar.make(getCurrentFocus(), pesan, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Daftar.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String username = Username.getText().toString();
        String repasword = RePassword.getText().toString();
        if (!password.equals(repasword)) {
            RePassword.setError("Password Tidak Sesuai");
            valid = false;
        } else {
            RePassword.setError(null);
        }
        if (repasword.isEmpty() ) {
            RePassword.setError("Password Tidak Boleh Kosong");
            valid = false;
        } else {
            RePassword.setError(null);
        }
        if (username.isEmpty() ) {
            Username.setError("Nama Tidak Boleh Kosong");
            valid = false;
        } else {
            Username.setError(null);
        }
        if (email.isEmpty() ) {
            Email.setError("Email Tidak Boleh Kosong");
            valid = false;
        } else {
            Email.setError(null);
        }

        if (password.isEmpty() ) {
            Password.setError("Password Tidak Boleh Kosong");
            valid = false;
        } else {
            Password.setError(null);
        }

        return valid;
    }
}
