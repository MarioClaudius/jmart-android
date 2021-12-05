package com.marioJmartDR.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreatePaymentRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:7901/payment/create";
    private final Map<String, String> params;

    public CreatePaymentRequest(int buyerId, int productId, int productCount, String shipmentAddress, byte shipmentPlan, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", String.valueOf(buyerId));
        params.put("productId", String.valueOf(productId));
        params.put("productCount", String.valueOf(productCount));
        params.put("shipmentAddress", shipmentAddress);
        params.put("shipmentPlan", String.valueOf(shipmentPlan));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
