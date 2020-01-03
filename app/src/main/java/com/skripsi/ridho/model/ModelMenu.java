package com.skripsi.ridho.model;

/**
 * Created by gandhi on 7/26/17.
 */

public class ModelMenu {
    private String namaMenu;
    private String icon;
    public ModelMenu(){}
    public ModelMenu(String namaMenu, String icon){
        this.namaMenu = namaMenu;
        this.icon = icon;
    }
    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
