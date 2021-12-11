package com.marioJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.marioJmartDR.model.ProductCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Request untuk melakukan filter terhadap list berdasarkan parameter tertentu untuk ditampilkan pada halaman utama aplikasi Jmart
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class FilterRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:7901/product/getFiltered?page=%s&pageSize=%s&accountId=%s&search=%s&minPrice=%s&maxPrice=%s&category=%s";
    private final Map<String, String> params;

    /**
     * Constructor request untuk melakukan filter dengan informasi lengkap
     * @param page index page
     * @param pageSize ukuran page (jumlah elemen yang dapat dikandung dalam 1 page)
     * @param accountId id toko yang memiliki produk
     * @param search string yang dicari oleh user untuk dibandingkan dengan nama produk
     * @param minPrice harga minimum
     * @param maxPrice harga maksimum
     * @param category kategori produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
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

    /**
     * Constructor request untuk melakukan filter dengan informasi lengkap namun tanpa ukuran page default
     * @param page index page
     * @param accountId id toko yang memiliki produk
     * @param search string yang dicari oleh user untuk dibandingkan dengan nama produk
     * @param minPrice harga minimum
     * @param maxPrice harga maksimum
     * @param category kategori produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
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

    /**
     * Constructor request untuk melakukan filter dengan informasi lengkap namun tanpa informasi harga minimum dan maksimum
     * @param page index page
     * @param pageSize ukuran page
     * @param accountId id toko yang memiliki produk
     * @param search string yang dicari oleh user untuk dibandingkan dengan nama produk
     * @param category kategori produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
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

    /**
     * Constructor request untuk melakukan filter dengan informasi lengkap namun tanpa informasi harga maksimum
     * @param page index page
     * @param pageSize ukuran page
     * @param accountId id toko yang memiliki produk
     * @param minPrice harga minimum
     * @param search string yang dicari oleh user untuk dibandingkan dengan nama produk
     * @param category kategori produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
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

    /**
     * Constructor request untuk melakukan filter dengan informasi lengkap namun tanpa informasi harga minimum
     * @param category kategori produk
     * @param page index page
     * @param pageSize ukuran page
     * @param accountId id toko yang memiliki produk
     * @param maxPrice harga maksimum
     * @param search string yang dicari oleh user untuk dibandingkan dengan nama produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
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

    /**
     * Constructor request untuk melakukan filter dengan informasi lengkap namun tanpa informasi harga minimum dan maksimum serta ukuran page default
     * @param page index page
     * @param accountId id toko yang memiliki produk
     * @param search string yang dicari oleh user untuk dibandingkan dengan nama produk
     * @param category kategori produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
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

    /**
     * Constructor request untuk melakukan filter dengan informasi lengkap namun tanpa informasi harga maksimum serta ukuran page default
     * @param page index page
     * @param accountId id toko yang memiliki produk
     * @param search string yang dicari oleh user untuk dibandingkan dengan nama produk
     * @param minPrice harga minimum
     * @param category kategori produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
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

    /**
     * Constructor request untuk melakukan filter dengan informasi lengkap namun tanpa informasi harga minimum serta ukuran page default
     * @param search string yang dicari oleh user untuk dibandingkan dengan nama produk
     * @param page index page
     * @param accountId id toko yang memiliki produk
     * @param maxPrice harga maksimum
     * @param category kategori produk
     * @param listener listener jika berhasil terkoneksi ke backend
     * @param errorListener listener jika error tidak terkoneksi ke backend
     */
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

    /**
     * Method untuk mendapatkan parameter yang sudah diberikan
     * @return Hashmap berisi parameter yang diperlukan untuk keperluan GET
     */
    public Map<String, String> getParams(){
        return params;
    }
}
