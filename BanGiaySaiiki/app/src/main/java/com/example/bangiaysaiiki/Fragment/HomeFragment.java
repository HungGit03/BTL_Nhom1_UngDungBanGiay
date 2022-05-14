package com.example.bangiaysaiiki.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.adapter.SPMoiAdapter;
import com.example.bangiaysaiiki.model.SPMoi;
import com.example.bangiaysaiiki.sanpham.SpViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    List<SPMoi> mangSpMoi;
    SPMoiAdapter spMoiAdapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        Anhxa(view);
        SpViewPagerAdapter adapter = new SpViewPagerAdapter(getActivity());
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Nike");
                    break;

                case 1:
                    tab.setText("Adidas");
                    break;
                case 2:
                    tab.setText("Convert");
                    break;

            }
        }).attach();

        return view;
    }


    private void Anhxa(View view) {
        //khoi tao adapter
        mangSpMoi = new ArrayList<>();

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.sp_viewpager);


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
