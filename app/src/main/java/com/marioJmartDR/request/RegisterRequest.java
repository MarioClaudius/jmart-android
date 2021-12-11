package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk mendaftarkan suatu akun untuk digunakan pada aplikasi Jmart
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class RegisterRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:7901/account/register";
    private final Map<String, String> params;

    /**
     * Constructor request untuk mendaftarkan akun dengan melakukan POST kredensial yaitu nama, email, dan password
     * @param name nama akun
     * @param email email akun
     * @param password password akun
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
    public RegisterRequest(String name, String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
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
