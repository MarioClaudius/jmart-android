package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk membuat Payment ketika membeli suatu produk oleh pembeli
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class CreatePaymentRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:7901/payment/create";
    private final Map<String, String> params;

    /**
     * Constructor request untuk melakukan method POST id pembeli, id produk yang akan dibeli, jumlah produk, alamat pengiriman,
     * metode pengiriman, dan id toko yang memiliki produk
     * @param buyerId id pembeli
     * @param productId id produk yang ingin dibeli
     * @param productCount jumlah produk yang ingin dibeli
     * @param shipmentAddress alamat pengiriman produk
     * @param shipmentPlan metode pengiriman produk
     * @param storeId id toko yang memiliki produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public CreatePaymentRequest(int buyerId, int productId, int productCount, String shipmentAddress, byte shipmentPlan, int storeId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", String.valueOf(buyerId));
        params.put("productId", String.valueOf(productId));
        params.put("productCount", String.valueOf(productCount));
        params.put("shipmentAddress", shipmentAddress);
        params.put("shipmentPlan", String.valueOf(shipmentPlan));
        params.put("storeId", String.valueOf(storeId));
    }

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan POST
     */
    public Map<String, String> getParams(){
        return params;
    }
}
