package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk melakukan tindakan accept pada Payment dengan status WAITING_CONFIRMATION oleh toko
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class AcceptPaymentRequest extends StringRequest {
    public static final String URL_FORMAT = "http://10.0.2.2:7901/payment/%d/accept";
    private final Map<String, String> params;

    /**
     * Constructor request untuk melakukan POST id payment untuk melakukan tindakan accept
     * @param id id dari payment yang akan di-accept
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public AcceptPaymentRequest(int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
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
