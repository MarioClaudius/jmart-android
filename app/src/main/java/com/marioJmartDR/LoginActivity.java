package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;

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
import com.marioJmartDR.model.Account;
import com.marioJmartDR.request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity untuk Login
 * @author Mario Claudius
 * @version 11 Desember 2021
 */
public class LoginActivity extends AppCompatActivity{
    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;

    /**
     * Method untuk inisialisasi serta event handler pada activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtEmailLogin = findViewById(R.id.edittext_email_login);
        EditText edtPasswordLogin = findViewById(R.id.edittext_password_login);
        Button btnLogin = findViewById(R.id.button_login);
        TextView tvCreateAccount = findViewById(R.id.textview_register);
        tvCreateAccount.setOnClickListener(new View.OnClickListener() {     //eventhandler untuk text createAccount
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);      //pindah activity ke register
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {            //eventhandler untuk tombol login
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            loggedAccount = gson.fromJson(response, Account.class);     //mengambil object Account dari response
                            startActivity(intent);
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Login Failed due to Connection", Toast.LENGTH_SHORT).show();
                    }
                };
                String email = edtEmailLogin.getText().toString();
                String password = edtPasswordLogin.getText().toString();        //mengambil informasi kredensial akun yaitu email dan password dari edittext
                LoginRequest loginRequest = new LoginRequest(email, password, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    /**
     * Method untuk mengambil object Account yang dimasukkan saat Login
     * @return object Account yang dimasukkan saat Login
     */
    public static Account getLoggedAccount(){
        return loggedAccount;
    }
}