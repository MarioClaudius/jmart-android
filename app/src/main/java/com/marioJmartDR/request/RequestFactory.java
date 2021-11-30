package com.marioJmartDR.request;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;

public class RequestFactory {
    public static final String URL_FORMAT_ID = "http://10.0.2.2:7901/%s/%d";
    public static final String URL_FORMAT_PAGE = "http://10.0.2.2:7901/%s/page?page=%s&pageSize=%s";

    public static StringRequest getById(String parentURI, int id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_ID, parentURI, id);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    public static StringRequest getPage(String parentURI, int page, int pageSize, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_PAGE, parentURI, page, pageSize);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }
}
