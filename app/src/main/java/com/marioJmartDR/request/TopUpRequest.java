package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk melakukan top up atau menambahkan balance pada akun yang akan digunakan untuk membeli produk
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class TopUpRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:7901/account/%d/topUp";
    private final Map<String, String> params;

    /**
     * Constructor request top up dengan melakukan POST informasi id akun yang ingin melakukan top up serta jumlah top up balance-nya
     * @param balance jumlah balance yang ingin di top up
     * @param id id akun yang akan melakukan top up
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public TopUpRequest(double balance, int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("balance", String.valueOf(balance));
    }

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan POST
     */
    public Map<String, String> getParams(){
        return params;
    }
}
