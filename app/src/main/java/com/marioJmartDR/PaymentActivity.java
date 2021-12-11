package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Payment;
import com.marioJmartDR.model.Product;
import com.marioJmartDR.model.ProductCategory;
import com.marioJmartDR.request.CreatePaymentRequest;
import com.marioJmartDR.request.RequestFactory;
import com.marioJmartDR.request.TopUpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity untuk membuat dan mendaftarkan object Payment
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class PaymentActivity extends AppCompatActivity {
    private TextView tvProductName, tvProductWeight, tvProductPrice, tvProductDiscount, tvProductCondition, tvProductCategory, tvProductShipmentPlan, tvAccountBalance;
    private Button btnPay, btnCancel;
    private Account account;
    private Product productSelected;
    private ImageButton btnAdd, btnRemove;
    private EditText edtTotalItem, edtAddress;
    String conditionUsed;
    private Dialog dialog;
    private static final Gson gson = new Gson();
    private Payment payment;

    /**
     * Method untuk inisialisasi serta event handler pada activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //inisialisasi
        tvProductName = findViewById(R.id.tv_product_name_payment);
        tvProductWeight = findViewById(R.id.tv_product_weight_payment);
        tvProductCondition = findViewById(R.id.tv_product_condition_payment);
        tvProductCategory = findViewById(R.id.tv_product_category_payment);
        tvProductPrice = findViewById(R.id.tv_product_price_payment);
        tvProductDiscount = findViewById(R.id.tv_product_discount_payment);
        tvProductShipmentPlan = findViewById(R.id.tv_product_shipmentplan_payment);
        tvAccountBalance = findViewById(R.id.tv_account_balance_payment);
        btnPay = findViewById(R.id.btn_pay_payment);
        btnCancel = findViewById(R.id.btn_cancel_payment);
        btnAdd = findViewById(R.id.btn_add_payment);
        btnRemove = findViewById(R.id.btn_remove_payment);
        edtTotalItem = findViewById(R.id.edt_total_item);
        edtAddress = findViewById(R.id.edt_address_payment);
        account = LoginActivity.getLoggedAccount();
        refreshAccountInformation();
        productSelected = MainActivity.getProductSelectedToBuy();
        tvProductName.setText(productSelected.name);
        tvProductWeight.setText(productSelected.weight + " Kg");
        if(productSelected.conditionUsed){
            conditionUsed = "NEW";
        }
        else {
            conditionUsed = "USED";
        }
        tvProductCondition.setText(conditionUsed);
        tvProductCategory.setText(getProductCategory(productSelected.category));
        tvProductPrice.setText("Rp " + productSelected.price);
        tvProductDiscount.setText(productSelected.discount + "%");
        tvProductShipmentPlan.setText(getShipmentPlan(productSelected.shipmentPlans));
        tvAccountBalance.setText("Rp " + account.balance);

        btnAdd.setOnClickListener(new View.OnClickListener() {      //event handler untuk tombol add (menambahkan jumlah produk yang ingin dibeli)
            @Override
            public void onClick(View view) {
                int totalItem = Integer.valueOf(edtTotalItem.getText().toString());
                totalItem++;
                edtTotalItem.setText("" + totalItem);
                tvProductPrice.setText("Rp " + (productSelected.price * totalItem));
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {   //event handler untuk tombol remove (mengurangi jumlah produk yang ingin dibeli)
            @Override
            public void onClick(View view) {
                int totalItem = Integer.valueOf(edtTotalItem.getText().toString());
                if(totalItem != 1){
                    totalItem--;
                }
                edtTotalItem.setText("" + totalItem);
                tvProductPrice.setText("Rp " + (productSelected.price * totalItem));
            }
        });

        dialog = new Dialog(PaymentActivity.this);
        btnPay.setOnClickListener(new View.OnClickListener() {      //event handler untuk tombol pay (membuat object Payment)
            @Override
            public void onClick(View view) {
                double totalPrice = Double.valueOf(tvProductPrice.getText().toString().substring(3));
                int itemCount = Integer.valueOf(edtTotalItem.getText().toString());
                String address = edtAddress.getText().toString();           //mengambil informasi untuk pembuatan payment
                if(account.balance >= totalPrice){              //pengecekan jika balance account melebihi harga total
                    dialog.setContentView(R.layout.dialog_confirmation);        //jika iya membentuk pop up konfirmasi
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnYes = dialog.findViewById(R.id.btn_yes_payment_confirmation);
                    Button btnNo = dialog.findViewById(R.id.btn_no_payment_confirmation);
                    btnYes.setOnClickListener(new View.OnClickListener() {      //event handler untuk tombol yes pada pop up
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            Response.Listener<String> listenerCreatePayment = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject object = null;
                                    try {
                                        object = new JSONObject(response);
                                        if(object != null){
                                            Toast.makeText(PaymentActivity.this, "Payment created successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        payment = gson.fromJson(response, Payment.class);       //mengambil object Payment dari response request
                                        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                                        startActivity(intent);      //setelah mendapatkan informasi Payment, kembali ke Main Activity
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            Response.ErrorListener errorListenerCreatePayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(PaymentActivity.this, "Create Payment due to Failed Connection", Toast.LENGTH_SHORT).show();
                                    Log.d("ERROR", error.toString());
                                }
                            };
                            CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(account.id, productSelected.id, itemCount, address, productSelected.shipmentPlans, productSelected.accountId, listenerCreatePayment, errorListenerCreatePayment);
                            RequestQueue queue = Volley.newRequestQueue(PaymentActivity.this);
                            queue.add(createPaymentRequest);
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {       //event handler untuk tombol no pada pop up
                        @Override
                        public void onClick(View view) {            //fungsinya menghilangkan pop up
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {                  //seandainya balance akun lebih kecil dari total harga, maka muncul pop up fail
                    dialog.setContentView(R.layout.dialog_failed_payment);
                    MaterialButton btnOk = dialog.findViewById(R.id.btn_ok_failed);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {           //event handler untuk tombol cancel (pindah activity)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getProductCategory(ProductCategory category){     //method untuk mengambil value string kategori produk
        String categoryStr = "-";                                //inisialisasi asal
        for(ProductCategory pc : ProductCategory.values()){
            if(pc == category){
                categoryStr = pc.toString();
            }
        }
        return categoryStr;
    }

    public String getShipmentPlan(byte plan){           //method untuk mengambil value string metode pengiriman dari value byte
        String shipmentPlan;
        if(plan == 1){
            shipmentPlan = "INSTANT";
        }
        else if(plan == 2){
            shipmentPlan = "SAME DAY";
        }
        else if(plan == 4){
            shipmentPlan = "NEXT DAY";
        }
        else if(plan == 8){
            shipmentPlan = "REGULER";
        }
        else {
            shipmentPlan = "KARGO";
        }
        return shipmentPlan;
    }

    /**
     * Method untuk mengambil informasi dari object Account
     */
    public void refreshAccountInformation(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);       //mengambil object Account
                    tvAccountBalance.setText("Rp " + account.balance);      //set ulang balance account
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika terkoneksi ke backend
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaymentActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(PaymentActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }
}