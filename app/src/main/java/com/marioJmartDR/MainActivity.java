package com.marioJmartDR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.marioJmartDR.fragment.FilterFragment;
import com.marioJmartDR.fragment.ProductFragment;
import com.marioJmartDR.model.Account;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        ViewPageAdapter vpAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new ProductFragment(), "PRODUCTS");
        vpAdapter.addFragment(new FilterFragment(), "FILTER");
        viewPager.setAdapter(vpAdapter);
        account = LoginActivity.getLoggedAccount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        MenuItem addMenu = menu.findItem(R.id.addbox);
        if(account.store == null){
            addMenu.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        setActivityMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setActivityMode(int selectMode){
        Intent intent;
        switch (selectMode){
            case R.id.search:
                break;

            case R.id.addbox:
                intent = new Intent(MainActivity.this, CreateProductActivity.class);
                startActivity(intent);
                break;

            case R.id.profile:
                intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
                break;
        }
    }
}