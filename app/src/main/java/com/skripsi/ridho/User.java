package com.skripsi.ridho;

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

public class User extends AppCompatActivity {
    TextView Update;
    EditText Username,Hp,Email;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        sessionManager = new SessionManager(getApplicationContext());
        final HashMap<String, String> user = sessionManager.getUserDetails();
        Username = (EditText) findViewById(R.id.username);
        Hp = (EditText) findViewById(R.id.noHp);
        Email = (EditText) findViewById(R.id.email);

        Username.setText(user.get(SessionManager.KEY_NAME));
        Hp.setText(user.get(SessionManager.KEY_HP));
        Email.setText(user.get(SessionManager.KEY_EMAIL));

        Update = (TextView) findViewById(R.id.update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constant.Update, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(!jsonObject.getString("nama_user").equals("gagal_update_data")){
                                    sessionManager.createLoginSession(jsonObject.getString("nama_user"),jsonObject.getString("email_user"),user.get(SessionManager.KEY_ID),jsonObject.getString("hp_user"),user.get(SessionManager.KEY_PASSWORD));
                                    Snackbar.make(v, "Data pengguna Sudah diupdate", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else{
                                    Snackbar.make(v, "Gaga Merubah Data Pengguna", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(v, "Terjadi Galat Saat merubah data pengguna", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("upData", "y");
                        params.put("id_user",user.get(SessionManager.KEY_ID));
                        params.put("nama_user", Username.getText().toString());
                        params.put("email_user", Email.getText().toString());
                        params.put("hp_user", Hp.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });


    }
}
