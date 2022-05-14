package com.example.bangiaysaiiki.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.activity.LoginActivity;
import com.example.bangiaysaiiki.activity.MainActivity;
import com.example.bangiaysaiiki.activity.OrderHistoryActivity;
import com.example.bangiaysaiiki.activity.SettingAppActivity;
import com.example.bangiaysaiiki.activity.SettingInfoActivity;
import com.example.bangiaysaiiki.model.MaGiamGia;
import com.example.bangiaysaiiki.model.NguoiDung;
import com.example.bangiaysaiiki.model.TaiKhoan;
import com.example.bangiaysaiiki.retrofit.ApiSaiiki;
import com.example.bangiaysaiiki.retrofit.RetrofitClient;
import com.example.bangiaysaiiki.util.Util;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserFragment extends Fragment {

    private View mView;
    private TextView tv_tennd, tv_dienthongtin, tv_thongtindonhang;
    private CompositeDisposable compositeDisposable;
    private ApiSaiiki apiSaiiki;
    private ImageView iv_caiDat, iv_nd;
    private LinearLayout ll_donhang;
    public static Button bt_dangKy;
    public TaiKhoan tk = new TaiKhoan();

    public MaGiamGia mgg = new MaGiamGia();


    int mand;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user,container,false);

        unitUi();

        iv_caiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SettingAppActivity.class);
                startActivity(i);
            }
        });

        if(Paper.book().read("matk_current") != null){
            int matk_current = Paper.book().read("matk_current");
            ll_donhang.setVisibility(View.VISIBLE);
            compositeDisposable.add(MainActivity.apiSaiiki.getnguoidungbymatk(matk_current)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            nguoiDungModel ->{
                                if(nguoiDungModel.isSuccess()){
                                    NguoiDung model = nguoiDungModel.getResult().get(0);
                                    mand = model.getId();
                                    if(model.getTennguoidung()!=null){
                                        tv_tennd.setVisibility(View.VISIBLE);
                                        tv_tennd.setText(model.getTennguoidung());
                                    }
                                    else{
                                        tv_tennd.setVisibility(View.VISIBLE);
                                        tv_tennd.setText(model.getSdt());
                                    }
                                    tv_dienthongtin.setText("Hoàn tất thông tin. >");
                                    tv_dienthongtin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(getActivity(), SettingInfoActivity.class);
                                            startActivity(i);
                                        }
                                    });
                                    iv_nd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(getActivity(), SettingInfoActivity.class);
                                            startActivity(i);
                                        }
                                    });
                                }
                            }
                    ));
            bt_dangKy.setVisibility(View.INVISIBLE);
            tv_thongtindonhang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), OrderHistoryActivity.class);
                    i.putExtra("mand", mand);
                    startActivity(i);
                }
            });
        }
        else {
            bt_dangKy.setVisibility(View.VISIBLE);
            tv_tennd.setVisibility(View.INVISIBLE);
            ll_donhang.setVisibility(View.INVISIBLE);
            tv_dienthongtin.setText("đăng nhập để có trải nghiệm tốt hơn");
            bt_dangKy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }
            });
        }

        return mView;
    }

    private void unitUi() {
        Paper.init(getActivity());
        Paper.init(getActivity());
        apiSaiiki = RetrofitClient.getInstance(Util.BASE_URL).create(ApiSaiiki.class);
        compositeDisposable = new CompositeDisposable();
        bt_dangKy = mView.findViewById(R.id.bt_dangki);
        tv_tennd = mView.findViewById(R.id.tv_ten_ng);
        tv_dienthongtin =mView.findViewById(R.id.tv_hoan_tat_thong_tin);
        iv_caiDat = mView.findViewById(R.id.iv_setting);
        iv_nd = mView.findViewById(R.id.iv_nd);
        ll_donhang = mView.findViewById(R.id.ll_donhang);
        tv_thongtindonhang = mView.findViewById(R.id.tv_xemdonhang);
        mand = 0;
    }


}
