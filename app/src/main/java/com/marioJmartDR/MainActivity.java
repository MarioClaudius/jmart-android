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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        int pageNumber = Integer.valueOf(edtPage.getText().toString());
        cvProduct.setVisibility(View.VISIBLE);
        cvFilter.setVisibility(View.GONE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Toast.makeText(MainActivity.this, "POSITION 0", Toast.LENGTH_SHORT).show();
                        cvProduct.setVisibility(View.VISIBLE);
                        cvFilter.setVisibility(View.GONE);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "POSITION 1", Toast.LENGTH_SHORT).show();
                        cvProduct.setVisibility(View.GONE);
                        cvFilter.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "KAGA MASUK KE POSITION MANAPUN", Toast.LENGTH_SHORT).show();
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
        account = LoginActivity.getLoggedAccount();
        getProductList(pageNumber - 1, 1);          //page di backend dimulai dari 0

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageNow = Integer.valueOf(edtPage.getText().toString());
                edtPage.setText((pageNow + 1) + "");
                int pageNext = Integer.valueOf(edtPage.getText().toString());
                if(applyFilter){
                    getFilteredProductList(pageNext - 1, 1);
                }
                else {
                    getProductList(pageNext - 1, 1);
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageNow = Integer.valueOf(edtPage.getText().toString());
                edtPage.setText((pageNow - 1) + "");
                int pagePrev = Integer.valueOf(edtPage.getText().toString());
                if(applyFilter){
                    getFilteredProductList(pagePrev - 1, 1);
                }
                else{
                    getProductList(pagePrev - 1, 1);
                }
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageNow = Integer.valueOf(edtPage.getText().toString());
                if(applyFilter){
                    getFilteredProductList(pageNow - 1, 1);
                }
                else {
                    getProductList(pageNow - 1, 1);
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFilteredProductList(0, null);
                applyFilter = true;
                edtPage.setText("" + 1);
                Toast.makeText(MainActivity.this, "Filter applied successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProductList(0, 1);
                edtPage.setText("" + 1);
                applyFilter = false;
                Toast.makeText(MainActivity.this, "Filter cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getFilteredProductList(int page, Integer pageSize){
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
        ProductCategory category = getProductCategory(spinner);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    JSONArray object = new JSONArray(response);
                    Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productList = gson.fromJson(response, productListType);
                    setListView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        FilterRequest filterRequest;
        if (maxPrice == null && minPrice == null){
            Log.d("MASUK IF 1", "CEK 1");
            filterRequest = new FilterRequest(page, account.id, productName, category, listener, errorListener);
        }
        else if(maxPrice == null){
            Log.d("MASUK IF 2", "CEK 2");
            filterRequest = new FilterRequest(page, account.id, productName, minPrice, category, listener, errorListener);
        }
        else if(minPrice == null){
            Log.d("MASUK IF 3", "CEK 3");
            filterRequest = new FilterRequest(productName, page, account.id, maxPrice, category, listener, errorListener);
        }
        else {
            Log.d("MASUK IF 4", "CEK 4");
            filterRequest = new FilterRequest(page, account.id, productName, minPrice, maxPrice, category, listener, errorListener);
        }
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(filterRequest);
    }

    public void getProductList(int page, int pageSize){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    JSONArray object = new JSONArray(response);
                    Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productList = gson.fromJson(response, productListType);
                    setListView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getPage("product", page, pageSize, listener, errorListener));
    }

    public void setListView(){
//        List<String> productNameList = new ArrayList<>();
//        for(Product p : productList){
//            productNameList.add(p.name);
//        }
//        Log.d("UKURAN ARRAY", "" + productNameList.size());
//        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.row_product_list, productNameList);
//        listView.setAdapter(adapter);
        customAdapter = new CustomAdapter(productList, this);
        listView.setAdapter(customAdapter);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem addMenu = menu.findItem(R.id.addbox);
        if(account.store == null){
            addMenu.setVisible(false);
        }

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        setActivityMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

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

    public class CustomAdapter extends BaseAdapter implements Filterable {
        private List<Product> productList;
        private List<Product> productListFiltered;
        private Context context;

        public CustomAdapter(List<Product> productList, Context context) {
            this.productList = productList;
            this.productListFiltered = productList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return productListFiltered.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.row_list_product, null);

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

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btnBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            productSelectedToBuy = productSelected;
                            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
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

                    if(charSequence == null || charSequence.length() == 0){
                        filterResults.count = productList.size();
                        filterResults.values = productList;
                    }
                    else {
                        String searchStr = charSequence.toString().toLowerCase();
                        Log.d("CHAR DI SEARCH", searchStr);
                        List<Product> resultData = new ArrayList<>();

                        for(Product p : productList){
                            Log.d("NAMA PRODUK", p.name);
                            if(p.name.toLowerCase().contains(searchStr)){
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
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }

    public static Product getProductSelectedToBuy(){
        return productSelectedToBuy;
    }
}