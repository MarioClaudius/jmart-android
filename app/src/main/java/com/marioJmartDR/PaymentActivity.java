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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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
        Log.d("BALANCE BEFORE", "" + account.balance);
        refreshAccountInformation();
        Log.d("BALANCE AFTER", "" + account.balance);
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalItem = Integer.valueOf(edtTotalItem.getText().toString());
                totalItem++;
                edtTotalItem.setText("" + totalItem);
                tvProductPrice.setText("Rp " + (productSelected.price * totalItem));
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
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
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double totalPrice = Double.valueOf(tvProductPrice.getText().toString().substring(3));
                int itemCount = Integer.valueOf(edtTotalItem.getText().toString());
                String address = edtAddress.getText().toString();
                if(account.balance >= totalPrice){
                    dialog.setContentView(R.layout.dialog_confirmation);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnYes = dialog.findViewById(R.id.btn_yes_payment_confirmation);
                    Button btnNo = dialog.findViewById(R.id.btn_no_payment_confirmation);
                    btnYes.setOnClickListener(new View.OnClickListener() {
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
                                        payment = gson.fromJson(response, Payment.class);
                                        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            Response.ErrorListener errorListenerCreatePayment = new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(PaymentActivity.this, "Create Payment Failed Connection", Toast.LENGTH_SHORT).show();
                                    Log.d("ERROR", error.toString());
                                }
                            };
                            CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(account.id, productSelected.id, itemCount, address, productSelected.shipmentPlans, listenerCreatePayment, errorListenerCreatePayment);
                            RequestQueue queue = Volley.newRequestQueue(PaymentActivity.this);
                            queue.add(createPaymentRequest);
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {
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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getProductCategory(ProductCategory category){
        String categoryStr = "-";                                //inisialisasi asal
        for(ProductCategory pc : ProductCategory.values()){
            if(pc == category){
                categoryStr = pc.toString();
            }
        }
        return categoryStr;
    }

    public String getShipmentPlan(byte plan){
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

    public void refreshAccountInformation(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    tvAccountBalance.setText("Rp " + account.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaymentActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(PaymentActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }
}