package com.example.bangiaysaiiki.sanpham;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.activity.MainActivity;
import com.example.bangiaysaiiki.activity.NikeActivity;
import com.example.bangiaysaiiki.adapter.LoaiSPAdapter;
import com.example.bangiaysaiiki.adapter.SPMoiAdapter;
import com.example.bangiaysaiiki.model.LoaiSP;
import com.example.bangiaysaiiki.model.SPMoi;
import com.example.bangiaysaiiki.retrofit.ApiSaiiki;
import com.example.bangiaysaiiki.retrofit.RetrofitClient;
import com.example.bangiaysaiiki.util.Util;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdidasFragment extends Fragment {

    private TextView textViewMenu;
    private ImageView imageViewMenu;
    private DrawerLayout drawerLayout_mhc;
    private ViewFlipper viewFlipper_mhc;
    private RecyclerView recyclerView_mhc;
    private NavigationView navigationView_mhc;
    private ListView listView_mhc;
    private LoaiSPAdapter loaiSPAdapter;
    private List<LoaiSP> mangloaisp;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiSaiiki apiBanHang;
    private List<SPMoi> mangSpMoi;
    private SPMoiAdapter spMoiAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nike,container,false);
        apiBanHang = RetrofitClient.getInstance(Util.BASE_URL).create(ApiSaiiki.class);
        Anhxa(view);
        ActionViewPlipper();
        if(isConnected(getActivity())){
            getLoaiSanPham();
            getSpMoi();
            getClickEvent();
        }else {
            Toast.makeText(getActivity(),"Khong co internet!",Toast.LENGTH_LONG).show();
        }
        return view;
    }
    private void getClickEvent() {
        listView_mhc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getActivity(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent nike = new Intent(getActivity(), NikeActivity.class);
                        nike.putExtra("loai",1);
                        startActivity(nike);
                        break;
                    case 2:
                        Intent adidas = new Intent(getActivity(),NikeActivity.class);
                        adidas.putExtra("loai",2);
                        startActivity(adidas);
                        break;
                    case 3:
                        Intent bitis = new Intent(getActivity(),NikeActivity.class);
                        bitis.putExtra("loai",3);
                        startActivity(bitis);
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpNew().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        spMoiModel -> {
                            if (spMoiModel.isSuccess()) {
                                mangSpMoi = spMoiModel.getResult();
                                spMoiAdapter = new SPMoiAdapter(getContext(), mangSpMoi);
                                recyclerView_mhc.setAdapter(spMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getActivity(), "Khong ket noi duoc voi server   !" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getLoaiSanPham() {

        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSPModel -> {
                            if(loaiSPModel.isSuccess()){
                                mangloaisp = loaiSPModel.getResult();
                                loaiSPAdapter = new LoaiSPAdapter(getActivity(),mangloaisp);
                                listView_mhc.setAdapter(loaiSPAdapter);

                            }
                        }
                ));
    }

    private void ActionViewPlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        for(int i = 0;i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getActivity());
            Glide.with(getActivity()).load(mangquangcao.get(i)).into(imageView);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper_mhc.addView(imageView);
        }
        viewFlipper_mhc.setFlipInterval(3000);
        viewFlipper_mhc.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_righy);
        viewFlipper_mhc.setInAnimation(slide_in);
        viewFlipper_mhc.setOutAnimation(slide_out);
    }


    private void Anhxa(View view) {
        drawerLayout_mhc = view.findViewById(R.id.drawerlayout_manhinhchinh);
        recyclerView_mhc = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView_mhc.setLayoutManager(layoutManager);
        recyclerView_mhc.setHasFixedSize(true);
        viewFlipper_mhc = view.findViewById(R.id.viewflipper_manhinhchinh);
        listView_mhc = view.findViewById(R.id.lv_manhinhchinh);
        navigationView_mhc = view.findViewById(R.id.navigation_view);
        textViewMenu = view.findViewById(R.id.item_tensp);
        imageViewMenu = view.findViewById(R.id.item_image);
        //khoi tao adapter
        mangloaisp= new ArrayList<>();
        mangSpMoi = new ArrayList<>();


    }
    private boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;

    }
}
