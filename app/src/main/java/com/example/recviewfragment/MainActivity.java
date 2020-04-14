package com.example.recviewfragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

//https://www.youtube.com/watch?v=oBhgyiBVd3k&t=35s

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;
    Bundle extras;
    int pageIndexIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFragments();
    }

    private void addFragments(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        adapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.instagram);
        tabLayout.getTabAt(1).setIcon(R.drawable.list);
        tabLayout.getTabAt(2).setIcon(R.drawable.host);
        viewPager.setCurrentItem(1);
    }
}
