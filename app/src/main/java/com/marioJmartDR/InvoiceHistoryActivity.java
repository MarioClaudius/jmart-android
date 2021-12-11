package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marioJmartDR.model.Account;
import com.marioJmartDR.model.Payment;
import com.marioJmartDR.model.Product;
import com.marioJmartDR.request.GetInvoiceRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity untuk menampilkan list Invoice berdasarkan akun maupun toko
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class InvoiceHistoryActivity extends AppCompatActivity {
    private RecyclerView rvPaymentInvoice;
    private TextView tvAccountOrStore;
    private List<Payment> paymentList = new ArrayList<>();
    private static final Gson gson = new Gson();
    private Account account;
    private boolean byAccount;

    /**
     * Method untuk nisialisasi serta event handler pada activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);
        byAccount = getIntent().getBooleanExtra("byAccount", false);            //mengambil nilai value dari intent
        account = LoginActivity.getLoggedAccount();                         //mengambil informasi akun dari LoginActivity
        //inisialisasi
        tvAccountOrStore = findViewById(R.id.tv_account_or_store);
        if(byAccount){
            tvAccountOrStore.setText("Account");
        }
        else {
            tvAccountOrStore.setText("Store");
        }
        rvPaymentInvoice = findViewById(R.id.rv_row_invoice_history);
        rvPaymentInvoice.setLayoutManager(new LinearLayoutManager(this));

        Response.Listener<String> listener = new Response.Listener<String>() {      //listener
            @Override
            public void onResponse(String response) {
                try {
                    paymentList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>(){}.getType();     //mengambil tipe list Payment
                    paymentList = gson.fromJson(response, paymentListType);         //mengambil list Payment dari response
                    ListInvoiceAdapter listInvoiceAdapter = new ListInvoiceAdapter(paymentList, byAccount);     //membuat adapter (menggunakan recycler view adapter)
                    rvPaymentInvoice.setAdapter(listInvoiceAdapter);            //memasukkan adapter nya ke recycler view yang menampilkan list
                } catch (JSONException e) {     //jika response null
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InvoiceHistoryActivity.this, "Get List Failed due to Connection", Toast.LENGTH_SHORT).show();
            }
        };

        GetInvoiceRequest getInvoiceRequest = new GetInvoiceRequest(account.id, byAccount, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(InvoiceHistoryActivity.this);
        queue.add(getInvoiceRequest);
    }

}