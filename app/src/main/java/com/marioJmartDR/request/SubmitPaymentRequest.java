package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk melakukan tindakan submit pada Payment dengan status ON_PROGRESS
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class SubmitPaymentRequest extends StringRequest {
    public static final String URL_FORMAT = "http://10.0.2.2:7901/payment/%d/submit?receipt=%s";
    private final Map<String, String> params;

    /**
     * Constructor request untuk melakukan submit Payment dengan melakukan POST informasi id payment dan receipt yang akan dimasukkan
     * @param id id payment yang akan di-submit
     * @param receipt receipt yang akan dimasukkan
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public SubmitPaymentRequest(int id, String receipt, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id, receipt), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
//        params.put("receipt", receipt);
    }

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan POST
     */
    public Map<String, String> getParams(){
        return params;
    }
}
