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

/**
 * Activity untuk membuat Account melalui register
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText edtNameRegister;
    private EditText edtEmailRegister;
    private EditText edtPasswordRegister;

    /**
     * Method untuk inisialisasi serta event handler pada activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtNameRegister = findViewById(R.id.edittext_name_register);
        edtEmailRegister = findViewById(R.id.edittext_email_register);
        edtPasswordRegister = findViewById(R.id.edittext_password_register);
        Button btnRegister = findViewById(R.id.button_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {     //event handler untuk tombol register
            @Override
            public void onClick(View v) {
                Response.Listener<String> listener = new Response.Listener<String>() {      //listener
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object != null){         //jika isi response tidak null, artinya Account berhasil dibuat dan terdaftar
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }catch (JSONException e) {          //jika response null
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Register Failed, Email or Password invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {           //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                };
                String name = edtNameRegister.getText().toString();
                String email = edtEmailRegister.getText().toString();
                String password = edtPasswordRegister.getText().toString();     //mengambil informasi kredensial untuk membuat Account
                RegisterRequest registerRequest = new RegisterRequest(name, email, password, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}