package com.example.intelligenttourist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    TabLayout t;
    ViewPager page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = findViewById(R.id.tab);
        page = findViewById(R.id.pager);
        t.addTab(t.newTab().setText("Login"));
        t.addTab(t.newTab().setText("Register"));
        t.setTabGravity(t.GRAVITY_FILL);
        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, t.getTabCount());
        page.setAdapter(adapter);
        page.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(t));
    }

}