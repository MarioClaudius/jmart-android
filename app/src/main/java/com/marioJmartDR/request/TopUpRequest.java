package com.marioJmartDR.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TopUpRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:7901/account/%d/topUp";
    private final Map<String, String> params;

    public TopUpRequest(double balance, int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("balance", String.valueOf(balance));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
