package com.chandrasalim.jualbuku.server;

public class BaseURL {

    public static String baseUrl = "http://192.168.43.180:5050/";

    public static String login      = baseUrl + "user/login";
    public static String register   = baseUrl + "user/registrasi";

    //buku
    public static String dataBuku = baseUrl + "buku/databuku";
    public static String editDataBuku = baseUrl + "buku/ubah/";
    public static String hapusData = baseUrl + "buku/hapus/";
    public static String inputBuku = baseUrl + "buku/input";

}
