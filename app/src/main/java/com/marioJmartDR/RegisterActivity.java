package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.marioJmartDR.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<String>, Response.ErrorListener{
    private EditText edtNameRegister;
    private EditText edtEmailRegister;
    private EditText edtPasswordRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtNameRegister = findViewById(R.id.edittext_name_register);
        edtEmailRegister = findViewById(R.id.edittext_email_register);
        edtPasswordRegister = findViewById(R.id.edittext_password_register);
        Button btnRegister = findViewById(R.id.button_register);
        btnRegister.setOnClickListener(this::onClick);


//        Response.Listener<String> listener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if(object != null){
//                        Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                    }
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(RegisterActivity.this, "Register Failed 1", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//
//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(RegisterActivity.this, "Register Failed 2", Toast.LENGTH_SHORT).show();
//            }
//        };
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Response.Listener<String> listener = new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String response) {
////                        try {
////                            JSONObject object = new JSONObject(response);
////                            if(object != null){
////                                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
////                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
////                                startActivity(intent);
////                            }
////                        }catch (JSONException e) {
////                            e.printStackTrace();
////                            Toast.makeText(RegisterActivity.this, "Register Failed 1", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                };
////                Response.ErrorListener errorListener = new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError error) {
////                        Toast.makeText(RegisterActivity.this, "Register Failed 2", Toast.LENGTH_SHORT).show();
////                    }
////                };
//                String name = edtNameRegister.getText().toString();
//                String email = edtEmailRegister.getText().toString();
//                String password = edtPasswordRegister.getText().toString();
//                RegisterRequest registerRequest = new RegisterRequest(name, email, password, listener, errorListener);
//                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
//                queue.add(registerRequest);
//            }
//        });
    }

    public void onClick(View view) {
//        Response.Listener<String> listener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if(object != null){
//                        Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                    }
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(RegisterActivity.this, "Register Failed 1", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };

//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(RegisterActivity.this, "Register Failed 2", Toast.LENGTH_SHORT).show();
//            }
//        };
//        edtNameRegister = findViewById(R.id.edittext_name_register);
//        edtEmailRegister = findViewById(R.id.edittext_email_register);
//        edtPasswordRegister = findViewById(R.id.edittext_password_register);
        String name = edtNameRegister.getText().toString();
        String email = edtEmailRegister.getText().toString();
        String password = edtPasswordRegister.getText().toString();
        Log.d("TEST", name + email + password);
        RegisterRequest registerRequest = new RegisterRequest(name, email, password, this::onResponse, this::onErrorResponse);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }

    @Override
    public void onResponse(String response){
        try {
            JSONObject object = new JSONObject(response);
            if(object != null){
                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }catch (JSONException e) {
            Log.d("ERROR", e.toString());//e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "Register Failed 1", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(RegisterActivity.this, "Register Failed 2", Toast.LENGTH_SHORT).show();
    }
}