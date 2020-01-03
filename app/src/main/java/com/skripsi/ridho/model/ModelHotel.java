package com.skripsi.ridho.model;

/**
 * Created by gandhi on 7/25/17.
 */

public class ModelHotel {
    private String IdHotel;
    private String NamaHotel;
    private String AlamatHotel;
    private String GambarHotel;
    private String LatHotel;
    private String LongHotel;
    private String DeskripsiHotel;
    private String Jarak;
    private String JarakK;
    private String WaktuU;

    private String BintangHotel;

    private String DisVal;
    private String DurVal;

    private String Waktu;

    public String getDisVal() {
        return DisVal;
    }

    public void setDisVal(String disVal) {
        DisVal = disVal;
    }

    public String getDurVal() {
        return DurVal;
    }

    public void setDurVal(String durVal) {
        DurVal = durVal;
    }

    public String getJarakK() {
        return JarakK;
    }

    public void setJarakK(String jarakK) {
        JarakK = jarakK;
    }

    public String getWaktuU() {
        return WaktuU;
    }

    public void setWaktuU(String waktuU) {
        WaktuU = waktuU;
    }

    public String getJarak() {
        return Jarak;
    }

    public void setJarak(String jarak) {
        Jarak = jarak;
    }

    public String getWaktu() {
        return Waktu;
    }

    public void setWaktu(String waktu) {
        Waktu = waktu;
    }

    public void ModelHotel(){}
    public void ModelHotel(String DisVal, String DurVal,String Jarak, String Waktu,String IdHotel, String NamaHotel, String AlamatHotel, String GambarHotel, String LatHotel, String LongHotel, String DeskripsiHotel){
        this.IdHotel = IdHotel;
        this.NamaHotel = NamaHotel;
        this.AlamatHotel = AlamatHotel;
        this.GambarHotel = GambarHotel;
        this.LatHotel = LatHotel;
        this.LongHotel = LongHotel;
        this.DeskripsiHotel = DeskripsiHotel;
        this.Jarak = Jarak;
        this.Waktu = Waktu;
        this.DurVal = DurVal;
        this.DisVal = DisVal;
    }

    public String getBintangHotel() {
        return BintangHotel;
    }

    public void setBintangHotel(String bintangHotel) {
        BintangHotel = bintangHotel;
    }
    public String getIdHotel() {
        return IdHotel;
    }

    public void setIdHotel(String idHotel) {
        IdHotel = idHotel;
    }

    public String getNamaHotel() {
        return NamaHotel;
    }

    public void setNamaHotel(String namaHotel) {
        NamaHotel = namaHotel;
    }

    public String getAlamatHotel() {
        return AlamatHotel;
    }

    public void setAlamatHotel(String alamatHotel) {
        AlamatHotel = alamatHotel;
    }

    public String getGambarHotel() {
        return GambarHotel;
    }

    public void setGambarHotel(String gambarHotel) {
        GambarHotel = gambarHotel;
    }

    public String getLatHotel() {
        return LatHotel;
    }

    public void setLatHotel(String latHotel) {
        LatHotel = latHotel;
    }

    public String getLongHotel() {
        return LongHotel;
    }

    public void setLongHotel(String longHotel) {
        LongHotel = longHotel;
    }

    public String getDeskripsiHotel() {
        return DeskripsiHotel;
    }

    public void setDeskripsiHotel(String deskripsiHotel) {
        DeskripsiHotel = deskripsiHotel;
    }


}
