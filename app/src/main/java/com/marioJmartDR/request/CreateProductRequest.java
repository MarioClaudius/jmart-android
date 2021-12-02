package com.marioJmartDR.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.marioJmartDR.model.ProductCategory;

import java.util.HashMap;
import java.util.Map;

public class CreateProductRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:7901/product/create";
    private final Map<String, String> params;

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

    public Map<String, String> getParams(){
        return params;
    }
}