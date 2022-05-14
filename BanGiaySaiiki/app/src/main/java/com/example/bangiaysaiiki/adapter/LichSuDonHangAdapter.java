package com.example.bangiaysaiiki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bangiaysaiiki.Interface.ItemClickListener;
import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.model.Ctdh;

import java.text.DecimalFormat;
import java.util.List;


public class LichSuDonHangAdapter  extends RecyclerView.Adapter<LichSuDonHangAdapter.MyViewHolder> {
    private List<Ctdh> array;
    private Context context;

    public LichSuDonHangAdapter(Context context,List<Ctdh> array){
        this.array = array;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        double tongtien;
        Ctdh ctdh = array.get(position);
        Double gia = Double.parseDouble(ctdh.getGiatien());
        holder.tv_tensp.setText(ctdh.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_gia.setText(decimalFormat.format(gia));
        holder.tv_chietkhau.setText("- " + String.valueOf(ctdh.getChietkhau()) + " %");
        holder.tv_soluong.setText(String.valueOf(ctdh.getSoluong()));
        tongtien = gia *(1 - ctdh.getChietkhau()) * ctdh.getSoluong();
        holder.tv_tongtien.setText(decimalFormat.format(tongtien));
        holder.tv_tongsl.setText("Tổng cộng (" + String.valueOf(ctdh.getSoluong()) + " sản phẩm): ");
        Glide.with(context).load(ctdh.getHinhanh()).into(holder.iv_sanpham);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_tensp, tv_gia,tv_soluong,tv_tongtien, tv_tongsl, tv_chietkhau;
        ImageView iv_sanpham;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tensp = itemView.findViewById(R.id.tv_tensp);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            tv_soluong = itemView.findViewById(R.id.tv_soluong);
            tv_tongtien = itemView.findViewById(R.id.tv_tongtien);
            iv_sanpham = itemView.findViewById(R.id.iv_sanpham);
            tv_tongsl = itemView.findViewById(R.id.title_tongtien);
            tv_chietkhau = itemView.findViewById(R.id.tv_chietkhau);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

    }


}
