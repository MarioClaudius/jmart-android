package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk mendaftarkan toko pada suatu akun untuk menjual suatu produk
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class RegisterStoreRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:7901/account/%d/registerStore";
    private final Map<String, String> params;

    /**
     * Constructor request untuk mendaftarkan toko pada suatu akun dengan melakukan POST informasi toko seperti
     * id akun, nama toko, alamat toko, dan nomor telepon dari toko
     * @param id id akun & id toko
     * @param storeName nama toko
     * @param storeAddress alamat toko
     * @param storePhoneNumber nomor telepon toko
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public RegisterStoreRequest(int id, String storeName, String storeAddress, String storePhoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("name", storeName);
        params.put("address", storeAddress);
        params.put("phoneNumber", storePhoneNumber);
    }

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan POST
     */
    public Map<String, String> getParams(){
        return params;
    }
}
