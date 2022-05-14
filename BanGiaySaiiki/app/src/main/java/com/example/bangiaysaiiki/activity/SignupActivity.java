package com.example.bangiaysaiiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.retrofit.ApiSaiiki;
import com.example.bangiaysaiiki.retrofit.RetrofitClient;
import com.example.bangiaysaiiki.util.Util;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity {

    private EditText et_pass, et_repPass, et_email;
    private String str_std;
    private Button btn_dangky;
    private ApiSaiiki apiSaiiki;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        unitUi();

        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_email.getText().toString().trim().length() == 0 ||
                        et_pass.getText().toString().trim().length() == 0||
                        et_repPass.getText().toString().trim().length() == 0){
                    Toast.makeText(SignupActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else if(et_pass.getText().toString().trim().equals(et_repPass.getText().toString().trim())){
                    String pass = et_pass.getText().toString().trim();
                    compositeDisposable.add(apiSaiiki.dangky(str_std,pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    taiKhoanModel ->{
                                        if(taiKhoanModel.isSuccess()){
                                            Util.user_current.setTendangnhap(str_std);
                                            Util.user_current.setMatkhau(pass);

                                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                            i.putExtra("email", et_email.getText().toString().trim());
                                            startActivity(i);
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),taiKhoanModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable ->{
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
                else{
                    Toast.makeText(SignupActivity.this,"Mật khẩu không khớp", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



    private void unitUi() {
        apiSaiiki = RetrofitClient.getInstance(Util.BASE_URL).create(ApiSaiiki.class);

        et_email = findViewById(R.id.et_Email);
        et_repPass = findViewById(R.id.et_rep_matkhau);
        et_pass = findViewById(R.id.et_matKhau);
        btn_dangky = findViewById(R.id.bt_dangki);
        str_std = getIntent().getStringExtra("phone_number");
    }
    private void SetTaiKhoan(){

    }

    private void dangKy() {
        String str_pass = et_pass.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}