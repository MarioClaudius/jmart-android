package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk memverifikasi kredensial untuk melakukan login
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class LoginRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:7901/account/login";
    private final Map<String, String> params;

    /**
     * Constructor request untuk memverifikasi kredensial untuk melakukan login dengan melakukan POST informasi kredensial suatu akun yaitu email dan password
     * @param email email suatu akun
     * @param password password suatu akun
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public LoginRequest(String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan POST
     */
    public Map<String, String> getParams(){
        return params;
    }
}
