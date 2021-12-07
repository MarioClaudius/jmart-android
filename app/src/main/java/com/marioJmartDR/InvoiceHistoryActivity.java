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

public class InvoiceHistoryActivity extends AppCompatActivity {
    private RecyclerView rvPaymentInvoice;
    private TextView tvAccountOrStore;
    private List<Payment> paymentList = new ArrayList<>();
    private static final Gson gson = new Gson();
    private Account account;
    private boolean byAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);
        byAccount = getIntent().getBooleanExtra("byAccount", false);
        account = LoginActivity.getLoggedAccount();
        tvAccountOrStore = findViewById(R.id.tv_account_or_store);
        if(byAccount){
            tvAccountOrStore.setText("Account");
        }
        else {
            tvAccountOrStore.setText("Store");
        }
        rvPaymentInvoice = findViewById(R.id.rv_row_invoice_history);
        rvPaymentInvoice.setLayoutManager(new LinearLayoutManager(this));
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    paymentList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>(){}.getType();
                    paymentList = gson.fromJson(response, paymentListType);
                    ListInvoiceAdapter listInvoiceAdapter = new ListInvoiceAdapter(paymentList);
                    rvPaymentInvoice.setAdapter(listInvoiceAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
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