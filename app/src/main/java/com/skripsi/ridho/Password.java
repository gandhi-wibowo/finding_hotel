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

public class Password extends AppCompatActivity {
    EditText OldPassword,NewPassword,RePassword;
    TextView Update;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();
        OldPassword = (EditText) findViewById(R.id.oldPwd);
        NewPassword = (EditText) findViewById(R.id.newPwd);
        RePassword = (EditText) findViewById(R.id.rePwd);
        Update = (TextView) findViewById(R.id.update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    return;
                }
                else {
                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constant.Update, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    if(jsonObject.getString("status").equals("FALSE")){
                                        Snackbar.make(getCurrentFocus(), "Gagal Merubah Password", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                    else if(jsonObject.getString("status").equals("WRONG")){
                                        Snackbar.make(getCurrentFocus(), "Password Yang lama Salah", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                    else{
                                        session.createLoginSession(user.get(SessionManager.KEY_NAME),user.get(SessionManager.KEY_EMAIL),user.get(SessionManager.KEY_ID),user.get(SessionManager.KEY_HP),jsonObject.getString("password_user"));
                                        OldPassword.setText("");
                                        NewPassword.setText("");
                                        RePassword.setText("");
                                        Snackbar.make(getCurrentFocus(), "Password Sudah Dirubah", Snackbar.LENGTH_LONG).setAction("Action", null).show();

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
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("upPwd", "y");
                            params.put("id_user",user.get(SessionManager.KEY_ID));
                            params.put("password",OldPassword.getText().toString());
                            params.put("newPassword", RePassword.getText().toString());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String old = OldPassword.getText().toString();
        String newPassword = NewPassword.getText().toString();
        String rePassword = RePassword.getText().toString();
        if(old.isEmpty()){
            OldPassword.setError("Password tidak boleh kosong");
            valid = false;
        }else {
            OldPassword.setError(null);
        }
        if(newPassword.isEmpty()){
            NewPassword.setError("Password tidak boleh kosong");
            valid = false;
        }else {
            NewPassword.setError(null);
        }
        if(!newPassword.equals(rePassword)){
            RePassword.setError("Password tidak Cocok");
            valid = false;
        }else {
            RePassword.setError(null);
        }
        if(old.equals(newPassword)){
            NewPassword.setError("Password sama dengan sebelumnya");
            valid = false;
        }else {
            NewPassword.setError(null);
        }

        return valid;
    }
}
