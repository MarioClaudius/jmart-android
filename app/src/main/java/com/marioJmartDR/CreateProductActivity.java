package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Product;
import com.marioJmartDR.model.ProductCategory;
import com.marioJmartDR.request.CreateProductRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Activity untuk membuat objek Product
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class CreateProductActivity extends AppCompatActivity {
    private EditText edtProductName, edtProductWeight, edtProductPrice, edtProductDiscount;
    private RadioButton radioButtonNew, radioButtonUsed;
    private Spinner spinnerCategory, spinnerShipmentPlan;
    private Button btnCreateProduct, btnCancel;
    private Product product;
    private static final Gson gson = new Gson();

    /**
     * Method untuk inisialisasi serta event handler pada activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        edtProductName = findViewById(R.id.edt_product_name);
        edtProductWeight = findViewById(R.id.edt_product_weight);
        edtProductPrice = findViewById(R.id.edt_product_price);
        edtProductDiscount = findViewById(R.id.edt_product_discount);
        radioButtonNew = findViewById(R.id.radio_new);
        radioButtonUsed = findViewById(R.id.radio_used);
        spinnerCategory = findViewById(R.id.spinner_category_create_product);
        spinnerShipmentPlan = findViewById(R.id.spinner_shipment_plan);
        btnCreateProduct = findViewById(R.id.btn_create_product);
        btnCancel = findViewById(R.id.btn_cancel_create_product);

        List<String> productCategoryList = new ArrayList<>();
        for(ProductCategory category : ProductCategory.values()){       //membentuk list string category untuk dimasukkan dalam spinner
            productCategoryList.add(category.toString());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter(this, R.layout.row_product_category, productCategoryList);
        spinnerCategory.setAdapter(categoryAdapter);            //memberi ArrayAdapter dengan list category untuk spinner kategori

        ArrayAdapter<String> shipmentPlanAdapter = new ArrayAdapter(this, R.layout.row_product_category, getResources().getStringArray(R.array.shipmentPlans));
        spinnerShipmentPlan.setAdapter(shipmentPlanAdapter);    //memberi ArrayAdapter dengan list metode pengiriman untuk spinner shipment

        //event handler untuk tombol cancel untuk kembali ke MainActivity (tidak jadi membuat Product)
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateProductActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnCreateProduct.setOnClickListener(new View.OnClickListener() {        //event handler untuk tombol createProduct
            @Override
            public void onClick(View view) {
                Account account = LoginActivity.getLoggedAccount();         //mengambil informasi akun dari Login Activity
                String name = edtProductName.getText().toString();
                String weight = edtProductWeight.getText().toString();
                String price = edtProductPrice.getText().toString();
                String discount = edtProductDiscount.getText().toString();
                int productWeight = Integer.valueOf(weight);
                double productPrice = Double.valueOf(price);
                double productDiscount = Double.valueOf(discount);
                boolean conditionUsed = checkRadioButton(radioButtonNew, radioButtonUsed);
                ProductCategory category = getProductCategory(spinnerCategory);
                byte shipmentPlan = getShipmentPlan(spinnerShipmentPlan);           //mengambil informasi untuk membuat Product
                Response.Listener<String> listener = new Response.Listener<String>() {      //listener
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(CreateProductActivity.this, "Product created successfully", Toast.LENGTH_SHORT).show();
                            }
                            product = gson.fromJson(response, Product.class);       //membentuk object Product dari response request
                            Intent intent = new Intent(CreateProductActivity.this, MainActivity.class);
                            startActivity(intent);      //kembali ke MainActivity setelah berhasil membuat Product
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {   //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateProductActivity.this, "Create Product Failed Connection", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                };

                CreateProductRequest createProductRequest = new CreateProductRequest(account.id, name, productWeight, conditionUsed, productPrice, productDiscount, category, shipmentPlan, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(CreateProductActivity.this);
                queue.add(createProductRequest);
            }
        });
    }

    /**
     * Method untuk mengecek nilai dari radioButton untuk menentukan boolean conditionUsed
     * @param newButton radioButton yang merepresentasikan NEW
     * @param usedButton radioButton yang merepresentasikan USED
     * @return boolean isUsed
     */
    public boolean checkRadioButton(RadioButton newButton, RadioButton usedButton){
        boolean isUsed = false;
        if(newButton.isChecked()){
            isUsed = true;
        }
        else if(usedButton.isChecked()){
            isUsed = false;
        }
        return isUsed;
    }

    /**
     * Method untuk mendapatkan metode pengiriman yang dipilih berdasarkan nilai dari spinner
     * @param shipmentPlanSpinner object spinner
     * @return byte yang merepresentasikan metode pengiriman
     */
    public byte getShipmentPlan(Spinner shipmentPlanSpinner){
        String shipmentPlan = shipmentPlanSpinner.getSelectedItem().toString();
        if(shipmentPlan.equals("INSTANT")){
            return 1;           // (1 << 0)
        }
        else if(shipmentPlan.equals("SAME DAY")){
            return 2;           // (1 << 1)
        }
        else if(shipmentPlan.equals("NEXT DAY")){
            return 4;           // (1 << 2)
        }
        else if(shipmentPlan.equals("REGULER")){
            return 8;           // (1 << 3)
        }
        else if(shipmentPlan.equals("KARGO")){
            return 16;          // (1 << 4)
        }
        else {
            return 0;
        }
    }

    /**
     * Metode untuk mendapatkan kategori produk dari object spinner
     * @param productCategorySpinner object spinner
     * @return enum ProductCategory sesuai nilai spinner
     */
    public ProductCategory getProductCategory(Spinner productCategorySpinner){
        ProductCategory category = ProductCategory.BOOK;                                //inisialisasi asal
        String productCategory = productCategorySpinner.getSelectedItem().toString();
        for(ProductCategory pc : ProductCategory.values()){
            if(pc.toString().equals(productCategory)){
                category = pc;
            }
        }
        return category;
    }
}