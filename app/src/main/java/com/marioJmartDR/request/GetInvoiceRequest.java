package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk mengambil list payment dan invoice yang terdaftar
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class GetInvoiceRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:7901/payment/%s?%s=%s";  //http://10.0.2.2:7901/payment/getByAccountId?buyerId=%s
    private final Map<String, String> params;

    /**
     * Constructor request untuk mengambil list payment dan invoice yang terdaftar dengan melakukan GET berdasarkan id akun pembeli atau id toko
     * @param id id akun pembeli atau id toko
     * @param byAccount boolean untuk mendapatkan informasi apakah request ini dilakukan oleh pembeli atau toko
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public GetInvoiceRequest(int id, boolean byAccount, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, byAccount ? "getByAccountId" : "getByStoreId", byAccount ? "buyerId" : "storeId", id), listener, errorListener);
        params = new HashMap<>();
        params.put(byAccount ? "buyerId" : "storeId", String.valueOf(id));
    }

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan GET
     */
    public Map<String, String> getParams(){
        return params;
    }
}
