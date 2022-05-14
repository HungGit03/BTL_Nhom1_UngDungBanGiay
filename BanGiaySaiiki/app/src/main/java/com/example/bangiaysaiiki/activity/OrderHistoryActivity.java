package com.example.bangiaysaiiki.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.adapter.LichSuDonHangAdapter;
import com.example.bangiaysaiiki.model.Ctdh;
import com.example.bangiaysaiiki.retrofit.ApiSaiiki;
import com.example.bangiaysaiiki.retrofit.RetrofitClient;
import com.example.bangiaysaiiki.util.Util;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderHistoryActivity extends AppCompatActivity {

    DrawerLayout drawerLayout_mhc;
    RecyclerView recyclerView_mhc;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSaiiki apiBanHang;
    List<Ctdh> mangCtdh;
    private ImageView iv_back, iv_setting;
    private TextView tv_toobar;




    int mand = 0;
    LichSuDonHangAdapter lichSuDonHangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        apiBanHang = RetrofitClient.getInstance(Util.BASE_URL).create(ApiSaiiki.class);
        Anhxa();

        if(isConnected(this)){
            getLichSuDH();
        }else {
            Toast.makeText(getApplicationContext(),"Khong co internet!",Toast.LENGTH_LONG).show();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_toobar.setText("Lịch sử giao dịch");
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), SettingAppActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void getLichSuDH() {
        if( mand != 0){
            compositeDisposable.add(apiBanHang.getlichsumuahang(mand).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            ctdhModel ->{
                                if(ctdhModel.isSuccess()){
                                    mangCtdh =ctdhModel.getResult();
                                    lichSuDonHangAdapter = new LichSuDonHangAdapter(getApplicationContext(), mangCtdh);
                                    recyclerView_mhc.setAdapter(lichSuDonHangAdapter);
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),"Khong ket noi duoc voi server   !"+ throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    ));
        }

    }

    private void Anhxa() {
        drawerLayout_mhc = findViewById(R.id.drawerlayout_manhinhchinhls);
        recyclerView_mhc = findViewById(R.id.recycler_viewls);
        iv_back = findViewById(R.id.iv_back);
        iv_setting = findViewById(R.id.iv_setting);
        tv_toobar = findViewById(R.id.tv_toobar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_mhc.setLayoutManager(linearLayoutManager);
        recyclerView_mhc.setHasFixedSize(true);
        Paper.init(this);
        //khoi tao adapter
        mangCtdh = new ArrayList<>();

        mand = getIntent().getIntExtra("mand", 0);


    }
    private boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}