package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
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
import com.google.gson.reflect.TypeToken;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Invoice;
import com.marioJmartDR.model.Payment;
import com.marioJmartDR.model.Store;
import com.marioJmartDR.request.GetInvoiceRequest;
import com.marioJmartDR.request.RegisterStoreRequest;
import com.marioJmartDR.request.RequestFactory;
import com.marioJmartDR.request.TopUpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity AboutMe berisi profile dan informasi dari akun
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class AboutMeActivity extends AppCompatActivity {
    private CardView cvRegisterStore, cvStore;
    private Button registerStoreBtn, topUpBtn, registerBtn, cancelBtn, checkAccountInvoiceBtn, checkStoreInvoiceBtn;
    private TextView tvName, tvEmail, tvBalance, tvStoreName, tvStoreAddress, tvStorePhoneNumber, tvStoreBalance;
    private EditText edtTopUp, edtName, edtAddress, edtPhoneNumber;
    private Account account;
    private static final Gson gson = new Gson();
    List<Payment> paymentList = new ArrayList<>();

    /**
     * Method untuk inisialisasi serta event handler pada activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        //inisialisasi
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
        tvStoreBalance = findViewById(R.id.tv_content_store_balance);
        edtTopUp = findViewById(R.id.edt_top_up);
        edtName = findViewById(R.id.edt_name_store_account);
        edtAddress = findViewById(R.id.edt_address_store_account);
        edtPhoneNumber = findViewById(R.id.edt_phonenumber_store_account);
        checkAccountInvoiceBtn = findViewById(R.id.btn_check_account_invoice);
        checkStoreInvoiceBtn = findViewById(R.id.btn_check_store_invoice);
        account = LoginActivity.getLoggedAccount();                         //mengambil informasi akun dari Login Activity
        refreshBalance();                                                   //memperbaharui informasi balance
        tvName.setText(account.name);
        tvEmail.setText(account.email);
        tvBalance.setText("" + account.balance);
        if(account.store != null){                          //mengatur kemunculan cardview ketika store sudah terdaftar
            cvStore.setVisibility(View.VISIBLE);
            cvRegisterStore.setVisibility(View.GONE);
            registerStoreBtn.setVisibility(View.GONE);
            tvStoreName.setText(account.store.name);
            tvStoreAddress.setText(account.store.address);
            tvStorePhoneNumber.setText(account.store.phoneNumber);
        }
        else{                                               //mengatur kemunculan cardview ketika belum ada store terdaftar
            registerStoreBtn.setVisibility(View.VISIBLE);
            cvRegisterStore.setVisibility(View.GONE);
            cvStore.setVisibility(View.GONE);
        }

        registerStoreBtn.setOnClickListener(new View.OnClickListener() {        //event handler untuk tombol registerStore untuk memunculkan cardview
            @Override
            public void onClick(View view) {
                registerStoreBtn.setVisibility(View.GONE);
                cvRegisterStore.setVisibility(View.VISIBLE);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {       //event handler untuk tombol cancel pada cardview yang muncul setelah tombol registerStore ditekan
            @Override
            public void onClick(View view) {
                cvRegisterStore.setVisibility(View.GONE);
                registerStoreBtn.setVisibility(View.VISIBLE);
            }
        });

        topUpBtn.setOnClickListener(new View.OnClickListener() {        //event handler untuk tombol topup
            @Override
            public void onClick(View view) {
                String amountTopUp = edtTopUp.getText().toString().trim();
                double amount = Double.valueOf(amountTopUp);                    //mengambil informasi jumlah top up dari edittext
                Response.Listener<String> listener = new Response.Listener<String>() {  //listener untuk request top up
                    @Override
                    public void onResponse(String response) {
                        Boolean object = Boolean.valueOf(response);     //karena response dalam bentuk boolean maka parameter response dikonversi ke boolean
                        if(object){
                            Toast.makeText(AboutMeActivity.this, "Top Up Successful", Toast.LENGTH_SHORT).show();
                            refreshBalance();           //jika top up berhasil, maka perbaharui informasi balance akun
                            edtTopUp.getText().clear();     //hilangkan isi edittext
                        }
                        else {
                            Toast.makeText(AboutMeActivity.this, "Top Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika gagal terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Top Up Failed Connection", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                };

                TopUpRequest topUpRequest = new TopUpRequest(amount, account.id, listener, errorListener);      //membuat request top up dan memasukkan informasi
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);          //membuat queue
                queue.add(topUpRequest);                            //menambahkan request yang sudah dibuat ke dalam queue
            }
        });

        //event handler untuk tombol registerBtn untuk melakukan konfirmasi pendaftaran toko
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mengambil informasi dari edittext
                String storeName = edtName.getText().toString();
                String storeAddress = edtAddress.getText().toString();
                String storePhoneNumber = edtPhoneNumber.getText().toString();
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(AboutMeActivity.this, "Store registered successfully", Toast.LENGTH_SHORT).show();
                            }
                            account.store = gson.fromJson(object.toString(), Store.class); //mengambil response dari request dan mengubahnya dalam bentuk store
                            tvStoreName.setText(account.store.name);
                            tvStoreAddress.setText(account.store.address);
                            tvStorePhoneNumber.setText(account.store.phoneNumber);
                            cvRegisterStore.setVisibility(View.GONE);
                            cvStore.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {             //jika response nya null
                            Toast.makeText(AboutMeActivity.this, "Register Store Failed", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.d("RESPONSE NYA ADALAH 2", response);
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {   //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Register Store Failed due to Connection", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                };

                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(account.id, storeName, storeAddress, storePhoneNumber, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(registerStoreRequest);
            }
        });

        checkAccountInvoiceBtn.setOnClickListener(new View.OnClickListener() {      //event handler untuk cek invoice history berdasarkan akun
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutMeActivity.this, InvoiceHistoryActivity.class);     //intent pindah ke InvoiceHistoruActivity
                intent.putExtra("byAccount", true);                 //mengirim data boolean melalui intent
                startActivity(intent);
            }
        });

        checkStoreInvoiceBtn.setOnClickListener(new View.OnClickListener() {        //event handler untuk cek invoice history berdasarkan store
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutMeActivity.this, InvoiceHistoryActivity.class);     //intent pindah ke InvoiceHistoruActivity
                intent.putExtra("byAccount", false);                //mengirim data boolean melalui intent
                startActivity(intent);
            }
        });
    }

    /**
     * Method untuk memperbaharui informasi balance akun
     */
    public void refreshBalance(){
        Response.Listener<String> listener = new Response.Listener<String>() {      //listener request
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);           //mengambil ulang informasi akun
                    tvBalance.setText("Rp " + account.balance);                 //mengubah text balance akun dan balance dari store akun tersebut
                    tvStoreBalance.setText("Rp " + account.store.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AboutMeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }

    /**
     * Method yang dijalankan ketika Activity ini kembali menjadi fokus pada android
     */
    @Override
    protected void onResume() {
        refreshBalance();           //memperbaharui informasi balance pada akun
        super.onResume();
    }
}