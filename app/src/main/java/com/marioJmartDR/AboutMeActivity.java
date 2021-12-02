package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Store;
import com.marioJmartDR.request.RegisterStoreRequest;
import com.marioJmartDR.request.RequestFactory;
import com.marioJmartDR.request.TopUpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutMeActivity extends AppCompatActivity {
    private CardView cvRegisterStore, cvStore;
    private Button registerStoreBtn, topUpBtn, registerBtn, cancelBtn;
    private TextView tvName, tvEmail, tvBalance, tvStoreName, tvStoreAddress, tvStorePhoneNumber;
    private EditText edtTopUp, edtName, edtAddress, edtPhoneNumber;
    private Account account;
    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        cvRegisterStore = findViewById(R.id.cv_register_store);
        cvStore = findViewById(R.id.cv_store_information);
        registerStoreBtn = findViewById(R.id.btn_register_store);
        topUpBtn = findViewById(R.id.btn_top_up);
        registerBtn = findViewById(R.id.btn_register_account);
        cancelBtn = findViewById(R.id.btn_cancel_account);
        tvName = findViewById(R.id.tv_name_content_account);
        tvEmail = findViewById(R.id.tv_email_content_account);
        tvBalance = findViewById(R.id.tv_balance_content_account);
        tvStoreName = findViewById(R.id.tv_name_content_store);
        tvStoreAddress = findViewById(R.id.tv_address_content_store);
        tvStorePhoneNumber = findViewById(R.id.tv_phonenumber_content_store);
        edtTopUp = findViewById(R.id.edt_top_up);
        edtName = findViewById(R.id.edt_name_store_account);
        edtAddress = findViewById(R.id.edt_address_store_account);
        edtPhoneNumber = findViewById(R.id.edt_phonenumber_store_account);
        account = LoginActivity.getLoggedAccount();
        tvName.setText(account.name);
        tvEmail.setText(account.email);
        tvBalance.setText("" + account.balance);
        if(account.store != null){
            cvStore.setVisibility(View.VISIBLE);
            cvRegisterStore.setVisibility(View.GONE);
            registerStoreBtn.setVisibility(View.GONE);
            tvStoreName.setText(account.store.name);
            tvStoreAddress.setText(account.store.address);
            tvStorePhoneNumber.setText(account.store.phoneNumber);
        }
        else{
            registerStoreBtn.setVisibility(View.VISIBLE);
            cvRegisterStore.setVisibility(View.GONE);
            cvStore.setVisibility(View.GONE);
        }

        registerStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerStoreBtn.setVisibility(View.GONE);
                cvRegisterStore.setVisibility(View.VISIBLE);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvRegisterStore.setVisibility(View.GONE);
                registerStoreBtn.setVisibility(View.VISIBLE);
            }
        });

        topUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountTopUp = edtTopUp.getText().toString().trim();
                double amount = Double.valueOf(amountTopUp);
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean object = Boolean.valueOf(response);
                        if(object){
                            Toast.makeText(AboutMeActivity.this, "Top Up Successful", Toast.LENGTH_SHORT).show();
                            refreshBalance();
                            edtTopUp.getText().clear();
                        }
                        else {
                            Toast.makeText(AboutMeActivity.this, "Top Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Top Up Failed Connection", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                };

                TopUpRequest topUpRequest = new TopUpRequest(amount, account.id, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(topUpRequest);
            }
        });

        //registerStore
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String storeName = edtName.getText().toString();
                String storeAddress = edtAddress.getText().toString();
                String storePhoneNumber = edtPhoneNumber.getText().toString();
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;
                        try {
                            Log.d("RESPONSE NYA ADALAH 1", response);
                            object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(AboutMeActivity.this, "Store registered successfully", Toast.LENGTH_SHORT).show();
                            }
                            account.store = gson.fromJson(object.toString(), Store.class);
                            tvStoreName.setText(account.store.name);
                            tvStoreAddress.setText(account.store.address);
                            tvStorePhoneNumber.setText(account.store.phoneNumber);
                            cvRegisterStore.setVisibility(View.GONE);
                            cvStore.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            Toast.makeText(AboutMeActivity.this, "Register Store Failed", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.d("RESPONSE NYA ADALAH 2", response);
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Register Store Failed Connection", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                };

                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(account.id, storeName, storeAddress, storePhoneNumber, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(registerStoreRequest);
            }
        });
    }

    public void refreshBalance(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("MASUK REFRESH", response);
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    tvBalance.setText("" + account.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AboutMeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }
}