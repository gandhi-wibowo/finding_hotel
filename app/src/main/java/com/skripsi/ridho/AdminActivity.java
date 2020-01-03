package com.skripsi.ridho;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.skripsi.ridho.adapter.ListMenu;
import com.skripsi.ridho.model.ModelMenu;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager lLayout;
    SessionManager sessionManager;
    String[] namaMenu={"HOTEL","USER","PASSWORD","POSISI","LOG-OUT"};
    String[] icon ={
            Constant.Icon+"hotel.png",
            Constant.Icon+"user.png",
            Constant.Icon+"password.png",
            Constant.Icon+"position.png",
            Constant.Icon+"logout.png",
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        lLayout = new GridLayoutManager(AdminActivity.this,2);
        rvView.setLayoutManager(lLayout);
        adapter = new ListMenu(this,getMenu());
        rvView.setAdapter(adapter);
    }
    private ArrayList<ModelMenu> getMenu(){
        ArrayList<ModelMenu> modelMenus = new ArrayList<ModelMenu>();
        modelMenus.add(new ModelMenu(namaMenu[0],icon[0]));
        modelMenus.add(new ModelMenu(namaMenu[1],icon[1]));
        modelMenus.add(new ModelMenu(namaMenu[2],icon[2]));
        modelMenus.add(new ModelMenu(namaMenu[3],icon[3]));
        modelMenus.add(new ModelMenu(namaMenu[4],icon[4]));
        return  modelMenus;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
