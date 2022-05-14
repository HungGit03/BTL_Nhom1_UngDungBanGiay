package com.example.bangiaysaiiki.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bangiaysaiiki.MyApplication;
import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.adapter.GioHangAdapter;
import com.example.bangiaysaiiki.model.EventBus.TinhTong;
import com.example.bangiaysaiiki.model.NguoiDung;
import com.example.bangiaysaiiki.util.Util;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PaymentActivity extends AppCompatActivity {

    private final int NOTIFICATION_ID = 3432;

    private TextView tv_ten, tv_sdt, tv_pgh, tv_chietkhau, tv_tiengiam, tv_tongtien;
    private TextInputEditText dcgh, mgg;
    private Button bt_mgg, bt_dathang;
    private CompositeDisposable compositeDisposable;
    private int mand;
    public double chietkhau, tongtien;

    private ImageView iv_back, iv_setting;
    private TextView tv_toobar;

    private String masp, sl;
    RecyclerView  recyclerView;
    private GioHangAdapter gioHangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initUi();
        setUi();
        InitData();
        InitControll();
        bt_mgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickMgg();
            }
        });

        bt_dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDh();
            }
        });

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




    private void initUi(){
        tv_ten = findViewById(R.id.tv_ten);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_pgh = findViewById(R.id.tv_pgh);
        tv_chietkhau = findViewById(R.id.tv_chietkhau);
        tv_tiengiam = findViewById(R.id.tv_tiengiam);
        tv_tongtien = findViewById(R.id.tv_tongtien);

        iv_back = findViewById(R.id.iv_back);
        iv_setting = findViewById(R.id.iv_setting);
        tv_toobar = findViewById(R.id.tv_toobar);

        dcgh = findViewById(R.id.input_dcgh);
        mgg = findViewById(R.id.inputmgg);
        bt_mgg = findViewById(R.id.bt_mgg);
        bt_dathang = findViewById(R.id.bt_dathang);
        recyclerView = findViewById(R.id.recycler_viewdh);
        compositeDisposable = new CompositeDisposable();
        sl="";
        masp="";
        tongtien = 0;
        Paper.init(this);
        chietkhau = 0;


    }

    private void setUi() {
        int mtk = Paper.book().read("matk_current");
        compositeDisposable.add(MainActivity.apiSaiiki.getnguoidungbymatk(mtk)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
               nguoiDungModel -> {
                   if(nguoiDungModel.isSuccess()){
                       NguoiDung model = nguoiDungModel.getResult().get(0);
                       tv_ten.setText(model.getTennguoidung());
                       tv_sdt.setText(model.getSdt());
                       dcgh.setText(model.getDiachi());
                       mand = model.getId();
                   }
               },
               throwable -> {

               }
            ));
        
    }
    private void    onCLickMgg() {
        String str_mgg = mgg.getText().toString().trim();

        if(str_mgg.length() == 0){
            Toast.makeText(this, "Bạn chưa nhập mã giảm giá", Toast.LENGTH_SHORT).show();
        }
        else{

            compositeDisposable.add(MainActivity.apiSaiiki.getmagg("GG25")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                    maGiamGiaModel -> {
                        if(maGiamGiaModel.isSuccess()){
                            chietkhau = maGiamGiaModel.getResult().get(0).getChietkhau();
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            tv_tiengiam.setText(decimalFormat.format(tongtien * chietkhau));
                            tongtien =  tongtien * (1 - chietkhau);
                            tv_chietkhau.setText(String.valueOf( chietkhau * 100) + "%");
                            tv_tongtien.setText(String.valueOf(decimalFormat.format(tongtien)));
                        }
                    },
                    throwable -> {
                        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            ));
        }
    }
    private void onClickDh() {
        if(dcgh.getText().toString().trim().length() == 0){
            Toast.makeText(this, "Vui lòng nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();

        }
        else{
            AlertDialog.Builder builder =  new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Vui lòng xác nhận lại đơn hàng đã chính xác chưa?");
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    compositeDisposable.add(MainActivity.apiSaiiki.adddonhang(mand,masp,
                            dcgh.getText().toString().trim(),sl, chietkhau)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    donHangModel -> {
                                        if(donHangModel.isSuccess()){
                                            Toast.makeText(PaymentActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                                            sendNotification();
                                        }
                                        else{
                                            Toast.makeText(PaymentActivity.this, "thất bại1", Toast.LENGTH_SHORT).show();

                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                    Util.mangGioHang.clear();
                    Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

        }
    }



    private void InitData() {
        for(int i =0;i<Util.mangGioHang.size();i++){
            tongtien = tongtien + ((Util.mangGioHang.get(i).getGiasp()-(Util.mangGioHang.get(i).getGiasp()/100*Util.mangGioHang.get(i).getGiamgia()))*Util.mangGioHang.get(i).getSoluong());
            if(i == Util.mangGioHang.size() - 1){
                masp += String.valueOf(Util.mangGioHang.get(i).getIdsp());
                sl += String.valueOf(Util.mangGioHang.get(i).getSoluong());
            }
            else{

                masp += String.valueOf(Util.mangGioHang.get(i).getIdsp()) + ",";
                sl += String.valueOf(Util.mangGioHang.get(i).getSoluong()+ ",");
            }
        }
        if(tv_tiengiam.getText().length() > 0) {
            tongtien = tongtien - Long.parseLong(tv_tiengiam.getText().toString().trim());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tv_tongtien.setText(decimalFormat.format(tongtien));
    }
    private void InitControll() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        gioHangAdapter = new GioHangAdapter(this,Util.mangGioHang);
        recyclerView.setAdapter(gioHangAdapter);


    }
    private void sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle("Đặt hàng thành công")
                .setContentText("Bạn đã đặt hàng thành công, hãy vào lịch sử đơn hàng để kiểm tra")
                .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                .setLargeIcon(bitmap)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notification != null){
            notificationManager.notify(NOTIFICATION_ID, notification);
        }


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
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}