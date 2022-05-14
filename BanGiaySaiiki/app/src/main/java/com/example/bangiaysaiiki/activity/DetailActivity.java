package com.example.bangiaysaiiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.model.GioHang;
import com.example.bangiaysaiiki.model.SPMoi;
import com.example.bangiaysaiiki.util.Util;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

import io.paperdb.Paper;

public class DetailActivity extends AppCompatActivity {
    TextView tenspdetail,giaspdetail,motadetail,giamgia,giagiagiam;
    Button btgiohang;
    ImageView imageViewDetail;
    Spinner spinnerDetail;
    Toolbar toolbar;
    SPMoi spMoi;
    NotificationBadge badge;
    FrameLayout frameLayoutCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_acvivity);

        Anhxa();
        ActionToolBar();
        InitData();
        InitControl();

    }

    private void InitControl() {
        btgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Paper.book().read("matk_current") == null){
                    Intent i = new Intent(DetailActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    themGioHang();
                }
            }
        });
    }

    private void themGioHang() {

        if(Util.mangGioHang.size()>0){
            boolean flag = false;
            int soLuong = Integer.parseInt(spinnerDetail.getSelectedItem().toString());
            for (int i=0;i<Util.mangGioHang.size();i++){
                if (Util.mangGioHang.get(i).getIdsp() == spMoi.getId()){
                    Util.mangGioHang.get(i).setSoluong(soLuong+Util.mangGioHang.get(i).getSoluong());
                    long gia = Long.parseLong(spMoi.getGiatien());
                    Util.mangGioHang.get(i).setGiasp(gia);

                    flag = true;
                }
            }
            if (flag==false){
                long gia = Long.parseLong(spMoi.getGiatien());
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setGiamgia(spMoi.getGiamgia());
                gioHang.setSoluong(soLuong);
                gioHang.setTensp(spMoi.getTensp());
                gioHang.setHinhsp(spMoi.getHinhanh());
                gioHang.setIdsp(spMoi.getId());
                Util.mangGioHang.add(gioHang);
            }
        }else {
            int soLuong = Integer.parseInt(spinnerDetail.getSelectedItem().toString());
            long gia = Long.parseLong(spMoi.getGiatien());
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soLuong);
            gioHang.setGiamgia(spMoi.getGiamgia());
            gioHang.setTensp(spMoi.getTensp());
            gioHang.setHinhsp(spMoi.getHinhanh());
            gioHang.setIdsp(spMoi.getId());
            Util.mangGioHang.add(gioHang);
        }
        int total = 0;
        for (int i =0;i<Util.mangGioHang.size();i++){
            total = total+ Util.mangGioHang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(total));
        //badge.setText(String.valueOf(Utils.mangGioHang.size()));
    }

    private void InitData() {

        spMoi = (SPMoi) getIntent().getSerializableExtra("detail");
        tenspdetail.setText(spMoi.getTensp());
        motadetail.setText(spMoi.getMota());
        Glide.with(getApplicationContext()).load(spMoi.getHinhanh()).into(imageViewDetail);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        if(spMoi.getGiamgia()!=0){

            giamgia.setText("-"+decimalFormat.format(spMoi.getGiamgia())+"%");
            giagiagiam.setText("Giá khuyến mãi: "+decimalFormat.format(Double.parseDouble(spMoi.getGiatien())-(Double.parseDouble(spMoi.getGiatien())/100*(spMoi.getGiamgia())))+"Đ");
            giaspdetail.setText("Giá: "+decimalFormat.format(Double.parseDouble(spMoi.getGiatien()))+"Đ");

        }else {
            giamgia.setText("");
            giagiagiam.setText("");
            giaspdetail.setText("Giá: "+decimalFormat.format(Double.parseDouble(spMoi.getGiatien()))+"Đ");
        }
        //Double gia1 = Double.parseDouble(String.valueOf(Double.parseDouble(spMoi.getGiatien())-(Double.parseDouble(spMoi.getGiatien())/100*(spMoi.getGiamgia()))));
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayspinner = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinnerDetail.setAdapter(arrayspinner);

    }

    private void Anhxa() {
        tenspdetail = findViewById(R.id.txttenspdetail);
        giaspdetail = findViewById(R.id.txtgiadetail);
        motadetail = findViewById(R.id.txtmotadetail);
        imageViewDetail = findViewById(R.id.detail_image);
        btgiohang = findViewById(R.id.bt_giohang);
        spinnerDetail = findViewById(R.id.spinnerdetail);
        toolbar = findViewById(R.id.toolbardetail);
        giamgia = findViewById(R.id.txtgiamgia);
        giagiagiam = findViewById(R.id.txtGiagiamgia);
        badge = findViewById(R.id.menu_sl);
        frameLayoutCart = findViewById(R.id.framecart);
        frameLayoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), MainActivity.class);
                giohang.putExtra("fragment", 1);
                startActivity(giohang);
            }
        });
        if(Util.mangGioHang != null){
            int total = 0;
            for (int i =0;i<Util.mangGioHang.size();i++){
                total = total+ Util.mangGioHang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(total));
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.mangGioHang != null){
            int total = 0;
            for (int i =0;i<Util.mangGioHang.size();i++){
                total = total+ Util.mangGioHang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(total));
        }
    }
}