package com.marioJmartDR.request;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.marioJmartDR.model.ProductCategory;

import java.util.HashMap;
import java.util.Map;

public class FilterRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:7901/product/getFiltered?page=%s&pageSize=%s&accountId=%s&search=%s&minPrice=%s&maxPrice=%s&category=%s";
    private final Map<String, String> params;

    public FilterRequest(int page, int pageSize, int accountId, String search, int minPrice, int maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, minPrice, maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    //Request ketika hanya page yang tidak diisi
    public FilterRequest(int page, int accountId, String search, int minPrice, int maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, "", accountId, search, minPrice, maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", "");
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    //Request jika minPrice dan maxPrice tidak diisi
    public FilterRequest(int page, int pageSize, int accountId, String search, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, "", "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    //Request jika hanya maxPrice yang tidak diisi
    public FilterRequest(int page, int pageSize, int accountId, int minPrice, String search, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, minPrice, "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    //Request jika hanya minPrice yang tidak diisi
    public FilterRequest(ProductCategory category, int page, int pageSize, int accountId, int maxPrice, String search, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, "", maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    //Request jika minPrice dan maxPrice tidak diisi serta pagenya tidak diisi
    public FilterRequest(int page, int accountId, String search, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, "", accountId, search, "", "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", "");
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    //Request jika hanya maxPrice yang tidak diisi serta pagenya tidak diisi
    public FilterRequest(int page, int accountId, String search, int minPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, "", accountId, search, minPrice, "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", "");
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    //Request jika hanya minPrice yang tidak diisi serta pagenya tidak diisi
    public FilterRequest(String search, int page, int accountId, int maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, "", accountId, search, "", maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", "");
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    public Map<String, String> getParams(){
        return params;
    }
}
