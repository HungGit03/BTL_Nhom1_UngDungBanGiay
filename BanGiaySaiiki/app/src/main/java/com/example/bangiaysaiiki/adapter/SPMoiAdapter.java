package com.example.bangiaysaiiki.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.bangiaysaiiki.activity.DetailActivity;
import com.example.bangiaysaiiki.model.SPMoi;

import java.text.DecimalFormat;
import java.util.List;

public class SPMoiAdapter extends RecyclerView.Adapter<SPMoiAdapter.MyViewHolder> {
    Context context;

    public SPMoiAdapter(Context context, List<SPMoi> arr) {
        this.context = context;
        this.arr = arr;
    }

    List<SPMoi> arr;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spmoi,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SPMoi spMoiModel = arr.get(position);
        holder.txtTen.setText(spMoiModel.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá: "+decimalFormat.format(Double.parseDouble(spMoiModel.getGiatien()))+"Đ");
        Glide.with(context).load(spMoiModel.getHinhanh()).into(holder.imgHinhAnh);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    //click
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("detail",spMoiModel);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtGia,txtTen;
        ImageView imgHinhAnh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGia = itemView.findViewById(R.id.itemsp_gia);
            txtTen = itemView.findViewById(R.id.itemsp_tensp);
            imgHinhAnh = itemView.findViewById(R.id.itemsp_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}
