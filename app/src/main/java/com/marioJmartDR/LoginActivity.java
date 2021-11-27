package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity{
    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtEmailLogin = findViewById(R.id.edittext_email_login);
        EditText edtPasswordLogin = findViewById(R.id.edittext_password_login);
        Button btnLogin = findViewById(R.id.button_login);
        TextView tvCreateAccount = findViewById(R.id.textview_register);
        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            JSONObject object = new JSONObject(response);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            loggedAccount = gson.fromJson(object.toString(), Account.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                };
                String email = edtEmailLogin.getText().toString();
                String password = edtPasswordLogin.getText().toString();
                LoginRequest loginRequest = new LoginRequest(email, password, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    public static Account getLoggedAccount(){
        return loggedAccount;
    }
}