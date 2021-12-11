package com.marioJmartDR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Product;
import com.marioJmartDR.model.ProductCategory;
import com.marioJmartDR.request.FilterRequest;
import com.marioJmartDR.request.RequestFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity untuk menampilkan list Product yang tersedia di file json
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private CardView cvProduct, cvFilter;
    private EditText edtPage, edtName, edtLowestPrice, edtHighestPrice;
    private CheckBox checkBoxNew, checkBoxUsed;
    private Spinner spinner;
    private Button prevBtn, nextBtn, goBtn, btnApply, btnCancel;
    private ListView listView;
    private static final Gson gson = new Gson();
    List<Product> productList = new ArrayList<>();
    private Account account;
    private CustomAdapter customAdapter;
    public boolean applyFilter = false;
    private Dialog dialog;
    public static Product productSelectedToBuy = null;
    public int pageNumber;

    /**
     * Method untuk inisialisasi serta event handler pada activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inisialisasi
        tabLayout = findViewById(R.id.tab_layout);
        cvProduct = findViewById(R.id.cv_product);
        cvFilter = findViewById(R.id.cv_filter);
        edtPage = findViewById(R.id.edt_page);
        prevBtn = findViewById(R.id.prev_btn);
        nextBtn = findViewById(R.id.next_btn);
        listView = findViewById(R.id.product_list);
        goBtn = findViewById(R.id.go_btn);
        edtName = findViewById(R.id.edt_name_filter);
        edtLowestPrice = findViewById(R.id.edt_lowest_price_filter);
        edtHighestPrice = findViewById(R.id.edt_highest_price_filter);
        checkBoxNew = findViewById(R.id.checkbox_new);
        checkBoxUsed = findViewById(R.id.checkbox_used);
        spinner = findViewById(R.id.spinner);
        btnApply = findViewById(R.id.btn_apply_filter);
        btnCancel = findViewById(R.id.btn_cancel_filter);

        //List buat dimasukkan ke spinner di tab filter
        List<String> list = new ArrayList<>();
        for(ProductCategory category : ProductCategory.values()){
            list.add(category.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, R.layout.row_product_category, list);
        spinner.setAdapter(adapter);

        pageNumber = Integer.valueOf(edtPage.getText().toString());
        cvProduct.setVisibility(View.VISIBLE);
        cvFilter.setVisibility(View.GONE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {      //event handler ketika tab ditekan untuk menghilangkan atau memunculkan cardview product dan filter
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        cvProduct.setVisibility(View.VISIBLE);
                        cvFilter.setVisibility(View.GONE);
                        break;
                    case 1:
                        cvProduct.setVisibility(View.GONE);
                        cvFilter.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Position Invalid", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        account = LoginActivity.getLoggedAccount();         //mengambil informasi object Account dari LoginActivity
        getProductList(pageNumber - 1, 10, 1);          //page di backend dimulai dari 0

        nextBtn.setOnClickListener(new View.OnClickListener() {     //event handler untuk tombol next
            @Override
            public void onClick(View view) {
                int pageBeforeNext = pageNumber++;          //nyimpen index page sebelum di-next untuk digunakan jika list page next null
                if(applyFilter){            //cek apakah filter di-apply atau tidak
                    getFilteredProductList(pageNumber - 1, pageBeforeNext);
                }
                else {
                    getProductList(pageNumber - 1, 10, pageBeforeNext);
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {     //event handler untuk tombol prev
            @Override
            public void onClick(View view) {
                int pageBeforePrev = pageNumber--;          //nyimpen index page sebelum di-prev untuk digunakan jika list page prev null
                if(applyFilter){                            //cek apakah filter di-apply atau tidak
                    getFilteredProductList(pageNumber - 1, pageBeforePrev);
                }
                else{
                    getProductList(pageNumber - 1, 10, pageBeforePrev);
                }
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {       //event handler untuk tombol go
            @Override
            public void onClick(View view) {
                int pageBeforeGo = pageNumber;              //nyimpen index page sebelum di-go untuk digunakan jika list page go null
                pageNumber = Integer.valueOf(edtPage.getText().toString());         //ngambil nilai index dari edittext yang dimasukkan user
                if(applyFilter){                    //cek apakah filter di-apply atau tidak
                    getFilteredProductList(pageNumber - 1, pageBeforeGo);
                }
                else {
                    getProductList(pageNumber - 1, 10, pageBeforeGo);
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {        //event handler untuk tombol apply pada tab filter
            @Override
            public void onClick(View view) {
                getFilteredProductList(0, pageNumber);          //saat filter di-apply, kembalikan list ke page 1 lalu tampilkan list yang sudah difilter
                applyFilter = true;                         //memberi tanda bahwa filter telah di-apply
                edtPage.setText("" + 1);                    //mengembalikan index yang ditampilkan menjadi 1
                Toast.makeText(MainActivity.this, "Filter applied successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {       //event handler untuk tombol cancel pada tab filter
            @Override
            public void onClick(View view) {
                getProductList(0, 10, pageNumber);       //setelah di-cancel, list dikembalikan ke page 1 dan menampilkan list tanpa filter
                edtPage.setText("" + 1);                //mengembalikan index yang ditampilkan menjadi 1
                applyFilter = false;                    //memberi tanda bahwa filter telah di-cancel
                Toast.makeText(MainActivity.this, "Filter cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method untuk mengambil list Product yang sudah difilter
     * @param page index page
     * @param pageBefore index page sebelum pindah page
     */
    public void getFilteredProductList(int page, int pageBefore){
        String productName = edtName.getText().toString();
        Integer minPrice;
        Integer maxPrice;
        if(edtLowestPrice.getText().toString().equals("")){
            minPrice = null;
        }
        else {
            minPrice = Integer.valueOf(edtLowestPrice.getText().toString());
        }

        if(edtHighestPrice.getText().toString().equals("")){
            maxPrice = null;
        }
        else {
            maxPrice = Integer.valueOf(edtHighestPrice.getText().toString());
        }
        ProductCategory category = getProductCategory(spinner);                     //mengambil seluruh informasi dari filter
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();                            //menghapus seluruh list pada variabel productList
                    JSONArray object = new JSONArray(response);
                    Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();     //mengambil tipe dari list Product
                    productList = gson.fromJson(response, productListType);     //memasukkan seluruh elemen dari response ke list yang sudah kosong
                    if(!productList.isEmpty()){
                        edtPage.setText("" + (page + 1));       //page ganti index
                        setListView();                          //listview di-set untuk menampilkan list jika tidak null
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {            //seandainya gagal, maka page kembali ke page awal sebelum masuk ke page error
                pageNumber = pageBefore;
                edtPage.setText("" + pageNumber);
                //Toast.makeText(MainActivity.this, "Failed Connection", Toast.LENGTH_SHORT).show();
            }
        };
        FilterRequest filterRequest;
        //bergantung dari informasi yang null, FilterRequest yang dibentuk menggunakan constructor berbeda
        if (maxPrice == null && minPrice == null){
            filterRequest = new FilterRequest(page, account.id, productName, category, listener, errorListener);
        }
        else if(maxPrice == null){
            filterRequest = new FilterRequest(page, account.id, productName, minPrice, category, listener, errorListener);
        }
        else if(minPrice == null){
            filterRequest = new FilterRequest(productName, page, account.id, maxPrice, category, listener, errorListener);
        }
        else {
            filterRequest = new FilterRequest(page, account.id, productName, minPrice, maxPrice, category, listener, errorListener);
        }
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(filterRequest);
    }

    /**
     * Method untuk mengambil list Product tanpa filter
     * @param page index page
     * @param pageSize ukuran page
     * @param pageBefore index page sebelum pindah
     */
    public void getProductList(int page, int pageSize, int pageBefore){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();                        //hapus seluruh elemen dari list
                    JSONArray object = new JSONArray(response);
                    Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();     //mengambil tipe dari list Product
                    productList = gson.fromJson(response, productListType);             //memasukkan seluruh elemen dari response ke list yang sudah kosong
                    if(!productList.isEmpty()){
                        Log.d("CEK VALUE PAGENUMBER", "" + pageNumber);
                        edtPage.setText("" + (page + 1));       //page ganti index
                        setListView();                  //listview di-set untuk menampilkan list jika tidak null
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {            //seandainya gagal, maka page kembali ke page awal sebelum masuk ke page error
                pageNumber = pageBefore;
                edtPage.setText("" + pageNumber);
                //Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getPage("product", page, pageSize, listener, errorListener));
    }

    /**
     * Method untuk membuat adapter lalu melakukan set adapter pada listview
     */
    public void setListView(){
        customAdapter = new CustomAdapter(productList, this);
        listView.setAdapter(customAdapter);
    }

    /**
     * Method untuk mengambil informasi enum ProductCategory dari spinner
     * @param spinner object spinner kategori
     * @return enum ProductCategory sesuai dengan spinner
     */
    public ProductCategory getProductCategory(Spinner spinner){
        ProductCategory category = ProductCategory.BOOK;                                //inisialisasi asal
        String productCategory = spinner.getSelectedItem().toString();
        for(ProductCategory pc : ProductCategory.values()){
            if(pc.toString().equals(productCategory)){
                category = pc;
            }
        }
        return category;
    }

    //Method untuk membuat menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem addMenu = menu.findItem(R.id.addbox);
        if(account.store == null){
            addMenu.setVisible(false);              //jika belum ada store yang terdaftar pada akun, maka menu menambahkan product tidak ditampilkan
        }

        //implementasi searchView pada menu search
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Method untuk mengatur logic ketika menu dipilih
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        setActivityMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    //method untuk pindah activity ketika suatu menu tertentu dipilih
    public void setActivityMode(int selectMode){
        Intent intent;
        switch (selectMode){
            case R.id.search:
                break;

            case R.id.addbox:
                intent = new Intent(MainActivity.this, CreateProductActivity.class);
                startActivity(intent);
                break;

            case R.id.profile:
                intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * Class Custom Adapter untuk listview
     */
    public class CustomAdapter extends BaseAdapter implements Filterable {
        private List<Product> productList;
        private List<Product> productListFiltered;
        private Context context;

        /**
         * Constructor untuk membuat CustomAdapter
         * @param productList list yang akan dimasukkan ke listview
         * @param context context dari listview
         */
        public CustomAdapter(List<Product> productList, Context context) {
            this.productList = productList;
            this.productListFiltered = productList;
            this.context = context;
        }

        @Override
        public int getCount() {         //Method untuk mendapatkan ukuran list yang difilter berdasarkan search
            return productListFiltered.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {      //Method untuk mendapatkan id dari item pada list
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {            //Method untuk mendapatkan setiap view dari listview (seperti holder pada recyclerview)
            View v = getLayoutInflater().inflate(R.layout.row_list_product, null);
            //inisialisasi
            TextView tvProductName = v.findViewById(R.id.tv_row_product_name);
            TextView tvProductPrice = v.findViewById(R.id.tv_row_product_price);
            String productConditionUsed;

            if(productListFiltered.get(i).conditionUsed){
                productConditionUsed = "NEW";
            }
            else {
                productConditionUsed = "USED";
            }

            tvProductName.setText(productListFiltered.get(i).name + " (" + productConditionUsed + ")");
            tvProductPrice.setText("Rp " + productListFiltered.get(i).price);

            v.setOnClickListener(new View.OnClickListener() {           //event handler untuk view (bisa ditekan)
                @Override
                public void onClick(View view) {        //saat diklik membuat pop up
                    dialog = new Dialog(view.getContext());
                    dialog.setContentView(R.layout.dialog_product_detail);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView tvDetailProductName = dialog.findViewById(R.id.tv_product_name_detail);
                    TextView tvDetailProductWeight = dialog.findViewById(R.id.tv_product_weight_detail);
                    TextView tvDetailProductCondition = dialog.findViewById(R.id.tv_product_condition_detail);
                    TextView tvDetailProductCategory = dialog.findViewById(R.id.tv_product_category_detail);
                    TextView tvDetailProductPrice = dialog.findViewById(R.id.tv_product_price_detail);
                    TextView tvDetailProductDiscount = dialog.findViewById(R.id.tv_product_discount_detail);
                    Button btnBuy = dialog.findViewById(R.id.btn_buy_detail_product);
                    Button btnCancel = dialog.findViewById(R.id.btn_cancel_product_detail);
                    Product productSelected = productListFiltered.get(i);
                    tvDetailProductName.setText(productSelected.name);
                    tvDetailProductWeight.setText(productSelected.weight + " Kg");
                    if(productSelected.conditionUsed){
                        tvDetailProductCondition.setText("NEW");
                    }
                    else {
                        tvDetailProductCondition.setText("USED");
                    }
                    tvDetailProductCategory.setText(productSelected.category.toString());
                    tvDetailProductPrice.setText("Rp " + productSelected.price);
                    tvDetailProductDiscount.setText(productSelected.discount + "%");
                    btnCancel.setOnClickListener(new View.OnClickListener() {       //event handler jika tombol cancel ditekan
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();                   //pop up hilang dan tidak terjadi apa-apa
                        }
                    });

                    btnBuy.setOnClickListener(new View.OnClickListener() {      //event handler jika tombol buy ditekan
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();                           //pop up hilang
                            productSelectedToBuy = productSelected;     //mengambil informasi product
                            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);       //pindah activity menggunakan intent
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                    Toast.makeText(MainActivity.this, productListFiltered.get(i).name, Toast.LENGTH_SHORT).show();
                }
            });
            return v;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();

                    if(charSequence == null || charSequence.length() == 0){     //charsequence adalah string yang dimasukkan pada searchview
                        filterResults.count = productList.size();               //cek apakah ada huruf atau kata yang dimasukkan
                        filterResults.values = productList;
                    }
                    else {              // kalo ada huruf atau kata yang dimasukkan
                        String searchStr = charSequence.toString().toLowerCase();
                        List<Product> resultData = new ArrayList<>();

                        for(Product p : productList){
                            if(p.name.toLowerCase().contains(searchStr)){           //memasukkan ke list elemen yang memiliki nama yang mengandung charsequence
                                resultData.add(p);
                            }
                        }

                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    productListFiltered = (List<Product>) filterResults.values;
                    notifyDataSetChanged();         //mengubah data secara real time
                }
            };
            return filter;
        }
    }

    public static Product getProductSelectedToBuy(){        //method untuk mengambil product yang dibeli dari activity lain
        return productSelectedToBuy;
    }

}