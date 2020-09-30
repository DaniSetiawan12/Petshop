package com.dwiromadon.myapplication.server;

public class BaseURL {

//    public static String baseUrl = "http://192.168.18.9:5050/";
//    public static String baseUrl = "http://172.31.0.136:5050/";
    public static String baseUrl = "http://192.168.43.156:5050/";
//    public static String baseUrl = "http://10.11.7.238:5050/";

    public static String inputPetShhop          = baseUrl + "petshop/input";
    public static String updatePetShhop         = baseUrl + "petshop/ubah/";
    public static String updatePetDataShhop     = baseUrl + "petshop/ubahpetshop/";
    public static String updatePetDataProduk    = baseUrl + "petshop/ubahpetproduk/";
    public static String hapusPetDataProduk     = baseUrl + "petshop/hapuspetproduk/";
    public static String updatePetDataJasa     = baseUrl + "petshop/ubahpetjasa/";
    public static String hapusPetDataJasa     = baseUrl + "petshop/hapuspetjasa/";
    public static String getDataPetShop         = baseUrl + "petshop/getdata/";
    public static String tambahGambarPetShop    = baseUrl + "petshop/ubahgambar/";
    public static String getJarak               = baseUrl + "petshop/getjarak/";
    public static String getpetShopById         = baseUrl + "petshop/getjarakbyid/";

    //input history
    public static String inputHistory           = baseUrl + "history/input";
    public static String getHistory             = baseUrl + "history/getjarak";
    public static String hapusHistory             = baseUrl + "history/hapus/";

    //users
    public static String registrasi             = baseUrl + "user/registrasi";
    public static String login                  = baseUrl + "user/login";
}