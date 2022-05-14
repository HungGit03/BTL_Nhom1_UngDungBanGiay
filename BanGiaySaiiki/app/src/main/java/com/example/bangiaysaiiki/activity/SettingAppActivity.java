package com.example.bangiaysaiiki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bangiaysaiiki.R;

import io.paperdb.Paper;

public class SettingAppActivity extends AppCompatActivity {

    private TextView tv_tttk, tv_dx, tv_toobar;
    private ImageView iv_back, iv_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_app);

        tv_tttk = findViewById(R.id.tv_tttk);
        tv_dx = findViewById(R.id.tv_dangxuat);
        tv_toobar = findViewById(R.id.tv_toobar);
        iv_back = findViewById(R.id.iv_back);
        iv_setting = findViewById(R.id.iv_setting);
        Paper.init(this);
        tv_toobar.setText("Cài đặt");
        iv_setting.setVisibility(View.INVISIBLE);

        tv_tttk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Paper.book().read("matk_current") == null){
                    Intent i = new Intent(SettingAppActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SettingAppActivity.this, SettingInfoActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });
        tv_dx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.init(getApplication());
                Intent i = new Intent(SettingAppActivity.this, MainActivity.class);
                Paper.book().delete("matk_current");
                startActivity(i);
                finish();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}