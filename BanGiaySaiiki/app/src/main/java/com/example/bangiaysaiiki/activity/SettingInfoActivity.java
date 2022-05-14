package com.example.bangiaysaiiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.model.NguoiDung;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingInfoActivity extends AppCompatActivity {

    private TextView tv_tdTen, tv_tdmk, tv_tdsdt, tv_tdEmail, tv_tdgt, tv_tdns, tv_toobar;
    private TextView tv_Ten, tv_sdt, tv_Email, tv_gt, tv_ns;
    private ImageView iv_back, iv_setting;
    private CompositeDisposable compositeDisposable;

    String ten, sdt, email, gt, matk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);

        unitUi();

        setThongTinTaiKhoan(compositeDisposable, Paper.book().read("matk_current"));

        tv_toobar.setText("Thông tin tài khoản");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), SettingAppActivity.class);
                startActivity(i);
                finish();
            }
        });

        matk = Paper.book().read("matk_current").toString();

        tv_tdTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBottomSheetDialog(0,matk,ten,sdt,email,gt);
            }
        });

        tv_tdsdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBottomSheetDialog(1,matk,ten,sdt,email,gt);
            }
        });

        tv_tdEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBottomSheetDialog(2,matk,ten,sdt,email,gt);
            }
        });

        tv_tdgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBottomSheetDialog(3,matk,ten,sdt,email,gt);
            }
        });
    }



    private void unitUi(){
        Paper.init(SettingInfoActivity.this);

        tv_tdTen = findViewById(R.id.tv_tdTen);
        tv_tdmk = findViewById(R.id.tv_tdmk);
        tv_tdsdt = findViewById(R.id.tv_tdsdt);
        tv_tdEmail = findViewById(R.id.tv_tdEmail);
        tv_tdgt = findViewById(R.id.tv_tdGt);
        tv_toobar = findViewById(R.id.tv_toobar);
        iv_back = findViewById(R.id.iv_back);
        iv_setting = findViewById(R.id.iv_setting);

        tv_Ten = findViewById(R.id.tv_ten);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_Email = findViewById(R.id.tv_email);
        tv_gt = findViewById(R.id.tv_gt);
        compositeDisposable = new CompositeDisposable();
    }

    private void setThongTinTaiKhoan(CompositeDisposable compositeDisposable, int id) {
        compositeDisposable.add(MainActivity.apiSaiiki.getnguoidungbymatk(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    nguoiDungModel -> {
                        if(nguoiDungModel.isSuccess()){
                            NguoiDung model = nguoiDungModel.getResult().get(0);
                            tv_Ten.setText(model.getTennguoidung());
                            tv_sdt.setText(model.getSdt());
                            tv_Email.setText(model.getEmail());
                            tv_gt.setText(model.getGioitinh());

                            ten = tv_Ten.getText().toString().trim();
                            sdt = tv_sdt.getText().toString().trim();
                            email = tv_Email.getText().toString().trim();
                            gt = tv_gt.getText().toString().trim();
                        }
                    },
                    throwable -> {}
            ));
    }

    private void thayDoiThongTin(String matk, String ten, String sdt, String email, String gt) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(MainActivity.apiSaiiki.updatenguoidung(matk, ten, gt, sdt, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        nguoiDungModel -> {
                            if(nguoiDungModel.isSuccess()){
                                NguoiDung model = nguoiDungModel.getResult().get(0);
                                tv_Ten.setText(model.getTennguoidung());
                                tv_sdt.setText(model.getSdt());
                                tv_Email.setText(model.getEmail());
                                tv_gt.setText(model.getGioitinh());

                            }
                            else{
                                Toast.makeText(SettingInfoActivity.this, "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(SettingInfoActivity.this, "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
                        }
                ));
        setThongTinTaiKhoan(compositeDisposable,Paper.book().read("matk_current"));
    }

    public void setBottomSheetDialog(int stt, String matk, String ten, String sdt, String email, String gt)
    {
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SettingInfoActivity.this);
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();

        ImageView iv_cancel = viewDialog.findViewById(R.id.iv_cancel);
        Button bt_luu = viewDialog.findViewById(R.id.bt_luu);
        EditText et_input = viewDialog.findViewById(R.id.et_input);
        TextView tv_title = viewDialog.findViewById(R.id.tv_title);
        TextView tv_note = viewDialog.findViewById(R.id.tv_note);
        final String dulieu;

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        if(stt == 0){
            et_input.setText(ten);
            tv_title.setText("Tên");
            tv_note.setText("Họ Tên");
            bt_luu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String input = et_input.getText().toString().trim();
                    if(ten.equals(input) || input.length() == 0){
                        bottomSheetDialog.dismiss();
                    }
                    else{
                        thayDoiThongTin(matk,input,sdt,email,gt);
                    }
                    bottomSheetDialog.dismiss();
                }
            });
        }
        if(stt == 1){

            tv_title.setText("Số điện thoại");
            tv_note.setText("Số Điện Thoại");
            dulieu = sdt;
            et_input.setText(dulieu);

            bt_luu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String input = et_input.getText().toString().trim();
                    if(dulieu.equals(input) || input.length() == 0){
                        bottomSheetDialog.dismiss();
                    }
                    else{
//                        thayDoiThongTin(matk,ten,input,email,gt);
                    }
                    bottomSheetDialog.dismiss();
                }
            });
        }
        if(stt == 2){
            tv_title.setText("Email");
            tv_note.setText("Địa Chỉ Email");
            et_input.setText(email);
            bt_luu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String input = et_input.getText().toString().trim();
                    if(email.equals(input) || input.length() == 0){
                        bottomSheetDialog.dismiss();
                    }
                    else{
                        thayDoiThongTin(matk,ten,sdt,input,gt);
                    }
                    bottomSheetDialog.dismiss();
                }
            });
        }
        if(stt == 3){
            tv_title.setText("Giới tính");
            tv_note.setText("Giới Tính");
            et_input.setText(gt);

            bt_luu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String input = et_input.getText().toString().trim();
                    if(gt.equals(input) || input.length() == 0){
                        bottomSheetDialog.dismiss();
                    }
                    else{
                        thayDoiThongTin(matk,ten,sdt,email,input);
                    }
                    bottomSheetDialog.dismiss();
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}