package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk melakukan tindakan cancel pada Payment dengan status WAITING_CONFIRMATION atau ON_PROGRESS oleh pembeli maupun toko
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class CancelPaymentRequest extends StringRequest {
    public static final String URL_FORMAT = "http://10.0.2.2:7901/payment/%d/cancel";
    private final Map<String, String> params;

    /**
     * Constructor request untuk melakukan method POST id payment yang akan di-cancel
     * @param id id payment yang akan di-cancel
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public CancelPaymentRequest(int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
    }

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan POST
     */
    public Map<String, String> getParams(){
        return params;
    }
}
