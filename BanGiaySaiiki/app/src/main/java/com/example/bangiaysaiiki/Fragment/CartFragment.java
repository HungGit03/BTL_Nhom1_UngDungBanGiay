package com.example.bangiaysaiiki.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.activity.PaymentActivity;
import com.example.bangiaysaiiki.adapter.GioHangAdapter;
import com.example.bangiaysaiiki.model.EventBus.TinhTong;
import com.example.bangiaysaiiki.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class CartFragment extends Fragment {

    private Button bt_thanhtoan;
    private TextView giohangtrong, total, title_tt;
    private RecyclerView recyclerView;
    private ImageView  iv_back;
    private View view;
    GioHangAdapter gioHangAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart,container,false);


        Anhxa();
        InitData();
        InitControll();
        bt_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PaymentActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    private void InitControll() {

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if (Util.mangGioHang.size()==0){
            giohangtrong.setVisibility(View.VISIBLE);
            bt_thanhtoan.setVisibility(View.INVISIBLE);
            title_tt.setVisibility(View.INVISIBLE);
            total.setVisibility(View.INVISIBLE);

        }else {
            bt_thanhtoan.setVisibility(View.VISIBLE);
            title_tt.setVisibility(View.VISIBLE);
            total.setVisibility(View.VISIBLE);
            gioHangAdapter = new GioHangAdapter(getActivity(),Util.mangGioHang);
            recyclerView.setAdapter(gioHangAdapter);
        }

    }

    private void InitData() {
        long tongtien = 0;
        for(int i =0;i<Util.mangGioHang.size();i++){
            tongtien = tongtien + ((Util.mangGioHang.get(i).getGiasp()-(Util.mangGioHang.get(i).getGiasp()/100*Util.mangGioHang.get(i).getGiamgia()))*Util.mangGioHang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        total.setText(decimalFormat.format(tongtien));
    }

    private void Anhxa() {
        iv_back = view.findViewById(R.id.iv_back);
        giohangtrong = view.findViewById(R.id.txtgiohangtrong);
        bt_thanhtoan = view.findViewById(R.id.bt_thanhtoan);
        total = view.findViewById(R.id.txttotal);
        title_tt = view.findViewById(R.id.tv_title_tt);
        recyclerView = view.findViewById(R.id.recycler_cart);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTong tinhTong){
        if (tinhTong!=null){
            InitData();
        }
    }
}
