package com.example.bangiaysaiiki.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.adapter.NikeAdapter;
import com.example.bangiaysaiiki.model.SPMoi;
import com.example.bangiaysaiiki.retrofit.ApiSaiiki;
import com.example.bangiaysaiiki.retrofit.RetrofitClient;
import com.example.bangiaysaiiki.util.Util;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NikeActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiSaiiki apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    NikeAdapter nikeAdapter;
    List<SPMoi> list;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nike);
        loai = getIntent().getIntExtra("loai",1);

        apiBanHang = RetrofitClient.getInstance(Util.BASE_URL).create(ApiSaiiki.class);
        Anhxa();
        ActionToolBar();
        getData(page);
        addEventLoading();
    }

    private void addEventLoading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading==false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition()==list.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                list.add(null);
                nikeAdapter.notifyItemChanged(list.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remove null
                list.remove(list.size()-1);
                nikeAdapter.notifyItemChanged(list.size());
                page = page+1;
                getData(page);
                nikeAdapter .notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSpDetail(page,loai)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                spMoiModel -> {
                    if (spMoiModel.isSuccess()){
                        if(nikeAdapter == null){
                            list = spMoiModel.getResult();
                            nikeAdapter = new NikeAdapter(getApplicationContext(),list);
                            recyclerView.setAdapter(nikeAdapter);
                        }else {
                            int viTri = list.size()-1;
                            int soLuongAdd = spMoiModel.getResult().size();
                            for (int i=0;i<soLuongAdd;i++){
                                list.add(spMoiModel.getResult().get(i));
                            }
                            nikeAdapter.notifyItemRangeChanged(viTri,soLuongAdd);
                        }

                    }else {
                        Toast.makeText(getApplicationContext(),"Hết!",Toast.LENGTH_LONG).show();
                        isLoading = true;
                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"Khong ket noi duoc voi server   !",Toast.LENGTH_LONG).show();
                }
        ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarNike);
        recyclerView = findViewById(R.id.recyclerviewNike);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}