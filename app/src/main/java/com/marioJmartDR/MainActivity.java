package com.marioJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.marioJmartDR.fragment.FilterFragment;
import com.marioJmartDR.fragment.ProductFragment;
import com.marioJmartDR.model.Account;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        Account account = LoginActivity.getLoggedAccount();
    }
}