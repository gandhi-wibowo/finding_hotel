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

public class Login extends AppCompatActivity {
    EditText Email,Password;
    TextView Daftar,Login;
    ProgressDialog progressDialog;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(getApplicationContext());
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        Login = (TextView) findViewById(R.id.signin1);
        Daftar = (TextView) findViewById(R.id.create);
        Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Daftar.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(Email.getText().toString(),Password.getText().toString());
            }
        });
    }
    private void Login(String email, String password){
        progressDialog = ProgressDialog.show(this,"Login","Auth . .",true);
        if (!validate()) {
            progressDialog.dismiss();
            onLoginFailed();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.Login +"email="+email+"&password="+password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getString("status").equals("sukses")){
                            sessionManager.createLoginSession(jsonObject.getString("nama_user"),jsonObject.getString("email_user"),jsonObject.getString("id_user"),jsonObject.getString("hp_user"),jsonObject.getString("password_user"));
                            Intent intent = new Intent(getApplicationContext(),AdminActivity.class);
                            startActivity(intent);
                            Snackbar.make(getCurrentFocus(), "Selamat Datang " + jsonObject.getString("nama_user"), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                        else{
                            progressDialog.dismiss();
                            onLoginFailed();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void onLoginFailed() {
        Snackbar.make(getCurrentFocus(), "Login Gagal", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Login.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        String email = Email.getText().toString();
        String password = Password.getText().toString();

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
