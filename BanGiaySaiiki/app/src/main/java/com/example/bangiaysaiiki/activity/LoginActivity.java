package com.example.bangiaysaiiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.retrofit.ApiSaiiki;
import com.example.bangiaysaiiki.retrofit.RetrofitClient;
import com.example.bangiaysaiiki.util.Util;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private EditText et_tdn, et_matkhau;
    private Button bt_dangNhap;
    private TextView tv_taoTaiKhoan;
    private ApiSaiiki apiSaiiki;
    private CompositeDisposable compositeDisposable;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unitUi();

        bt_dangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_sdt = et_tdn.getText().toString().trim();
                String str_matKhau = et_matkhau.getText().toString().trim();
                if(str_sdt.length() == 0 || str_matKhau.length() == 0){
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    onClickDangNhap(str_sdt, str_matKhau);
                }
            }
        });

        tv_taoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, EnterSdtActivity.class);
                startActivity(i);
            }
        });

    }

    private void unitUi(){
        Paper.init(this);
        compositeDisposable = new CompositeDisposable();
        et_tdn = findViewById(R.id.et_TenDangNhap);
        et_matkhau = findViewById(R.id.et_MatKhau);
        bt_dangNhap = findViewById(R.id.bt_DangNhap);
        tv_taoTaiKhoan = findViewById(R.id.tv_TaoTaiKhoan);
        email = getIntent().getStringExtra("email");

        apiSaiiki = RetrofitClient.getInstance(Util.BASE_URL).create(ApiSaiiki.class);
    }

    private void onClickDangNhap(String str_sdt, String str_matKhau) {
        compositeDisposable.add(apiSaiiki.dangnhap(str_sdt,str_matKhau)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                taiKhoanModel -> {
                    if(taiKhoanModel.isSuccess()){
                        Util.user_current = taiKhoanModel.getResult().get(0);
                        Paper.book().write("matk_current", Util.user_current.getId());
                        compositeDisposable.add(apiSaiiki.dangkynguoidung(str_sdt, email, Util.user_current.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                nguoiDungModel -> {

                                },
                                throwable -> {

                                }
                        ));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.user_current.getTendangnhap() != null && Util.user_current.getMatkhau() != null){
            et_tdn.setText(Util.user_current.getTendangnhap());
            et_matkhau.setText(Util.user_current.getMatkhau());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}