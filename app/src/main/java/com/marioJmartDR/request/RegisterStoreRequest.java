package com.marioJmartDR.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterStoreRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:7901/account/%d/registerStore";
    private final Map<String, String> params;

    public RegisterStoreRequest(int id, String storeName, String storeAddress, String storePhoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("name", storeName);
        params.put("address", storeAddress);
        params.put("phoneNumber", storePhoneNumber);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
