package com.example.bangiaysaiiki.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.adapter.ViewPagerAdapter;
import com.example.bangiaysaiiki.retrofit.ApiSaiiki;
import com.example.bangiaysaiiki.retrofit.RetrofitClient;
import com.example.bangiaysaiiki.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private ViewPager2 mViewPager;
    public static ApiSaiiki apiSaiiki;
    private int fragment_int;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_nav);
        mViewPager = findViewById(R.id.view_pager);

        unitUi();
        setUpViewPager();
    }
    public void setUpViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(viewPagerAdapter);
        if(fragment_int == 1){
            mBottomNavigationView.getMenu().findItem(R.id.cart).setChecked(true);
            mViewPager.setCurrentItem(1);
            fragment_int = 0;
        }
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.cart).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.person).setChecked(true);
                        break;
                }
            }
        });
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.cart:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.person:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

    }

    private boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;

    }
    private void unitUi(){
        Paper.init(this);
        fragment_int = getIntent().getIntExtra("fragment", 0);
        if(Util.mangGioHang==null){
            Util.mangGioHang = new ArrayList<>();
        }else {
            int total = 0;
            for (int i =0;i<Util.mangGioHang.size();i++){
                total = total+ Util.mangGioHang.get(i).getSoluong();
            }
        }
        apiSaiiki = RetrofitClient.getInstance(Util.BASE_URL).create(ApiSaiiki.class);
    }
}