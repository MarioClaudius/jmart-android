package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.marioJmartDR.model.Account;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.textview_main_activity);
        Account account = LoginActivity.getLoggedAccount();
        tv.setText("Hello " + account.name);
    }
}