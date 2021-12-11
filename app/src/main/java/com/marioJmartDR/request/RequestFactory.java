package com.marioJmartDR.request;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;

/**
 * Request untuk mengambil objek atau list objek berdasarkan id atau halaman
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class RequestFactory {
    public static final String URL_FORMAT_ID = "http://10.0.2.2:7901/%s/%d";
    public static final String URL_FORMAT_PAGE = "http://10.0.2.2:7901/%s/page?page=%s&pageSize=%s";

    /**
     * Method membuat StringRequest untuk mengambil objek berdasarkan id-nya dengan melakukan GET berdasarkan URI Parent-nya dan id
     * @param parentURI parentURI untuk request
     * @param id id dari objek
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     * @return StringRequest untuk mengambil objek berdasarkan id-nya dan parentURI-nya
     */
    public static StringRequest getById(String parentURI, int id, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_ID, parentURI, id);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    /**
     * Method membuat StringRequest untuk mengambil objek berdasarkan index dan ukuran page dengan melakukan GET berdasarkan URI Parent-nya
     * @param parentURI parentURI untuk request
     * @param page index page
     * @param pageSize ukuran page
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     * @return StringRequest untuk mengambil objek berdasarkan index dan ukuran page
     */
    public static StringRequest getPage(String parentURI, int page, int pageSize, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = String.format(URL_FORMAT_PAGE, parentURI, page, pageSize);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }
}
