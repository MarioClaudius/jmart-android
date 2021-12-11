package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.marioJmartDR.model.ProductCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk membuat Product untuk didaftarkan pada aplikasi Jmart oleh toko
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class CreateProductRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:7901/product/create";
    private final Map<String, String> params;

    /**
     * Constructor request untuk membuat dan mendaftarkan produk toko ke aplikasi Jmart serta melakukan POST informasi produk seperti
     * id toko yang memiliki produk, nama produk, berat produk, kondisi penggunaaan produk, harga produk, diskon produk,
     * kategori produk, dan metode pengiriman produk
     * @param accountId id toko yang memiliki produk
     * @param name nama produk
     * @param weight berat produk
     * @param conditionUsed kondisi penggunaan produk
     * @param price harga produk
     * @param discount diskon produk
     * @param category kategori produk
     * @param shipmentPlans metode pengiriman produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public CreateProductRequest(int accountId, String name, int weight, boolean conditionUsed, double price, double discount, ProductCategory category, byte shipmentPlans, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("accountId", String.valueOf(accountId));
        params.put("name", name);
        params.put("weight", String.valueOf(weight));
        params.put("conditionUsed", String.valueOf(conditionUsed));
        params.put("price", String.valueOf(price));
        params.put("discount", String.valueOf(discount));
        params.put("category", String.valueOf(category));
        params.put("shipmentPlans", String.valueOf(shipmentPlans));
    }

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan POST
     */
    public Map<String, String> getParams(){
        return params;
    }
}