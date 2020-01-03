package com.skripsi.ridho;

/**
 * Created by gandhi on 5/16/17.
 */

public class Constant {
    public static final String BASE = "http://192.168.43.8/hotel/index.php/";
    public static final String Login = BASE + "User?login=y&"; // kirim email dan password untuk login : method get
    public static final String CekLogin = BASE + "User?cekLogin=y&"; // cek login berdasarkan data pada shared pref
    public static final String Daftar = BASE + "User/"; // gunakan method post untuk mendaftar, nama_user,email_user,hp_user,password_user
    public static final String Update = BASE + "User/"; // gunakan method put, untuk merubah password ataupun data pribadi
    public static final String GetUser = BASE + "User?id_user="; // gunakan method get untuk mendapatkan data user berdasarkan id user

    public static final String Hotel = BASE + "Hotel/"; // gunakan method post untuk menambah menu / Kuliner
    public static final String CariHotel = BASE + "Hotel?keyword="; // cari berdasarkan keyword
    public static final String GetById = BASE + "Hotel?id_hotel="; // dapatkan kuliner berdasarkan id
    public static final String GetByIdUser = BASE + "Hotel?id_user="; // dapatkan kuliner berdasarkan id user / pemilik
    public static final String Image = BASE +"../img/";
    public static final String Icon = BASE +"../icon/";
}
