package com.example.bangiaysaiiki.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class NikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SPMoi> arr;
    private static final int VIEW_TYPE_DATA=0;
    private static final int VIEW_TYPE_LOADING=1;

    public NikeAdapter(Context context, List<SPMoi> arr) {
        this.context = context;
        this.arr = arr;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nike,parent,false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewGHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder viewHolder = (MyViewHolder) holder;
            SPMoi spMoi = arr.get(position);
            viewHolder.tenSp.setText(spMoi.getTensp().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            viewHolder.giaSp.setText("Giá: "+decimalFormat.format(Double.parseDouble(spMoi.getGiatien()))+"Đ");
            viewHolder.motaSp.setText(spMoi.getMota());
            Glide.with(context).load(spMoi.getHinhanh()).into(viewHolder.hinhanh);
            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if(!isLongClick){
                        //click
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("detail",spMoi);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
;
                    }
                }
            });
        }else {
            LoadingViewGHolder loadingViewGHolder = (LoadingViewGHolder) holder;
            loadingViewGHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return arr.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class LoadingViewGHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewGHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_loading);
        }
    }


    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tenSp,  giaSp, motaSp;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {


            super(itemView);
            tenSp = itemView.findViewById(R.id.itemNike_tensp);
            giaSp = itemView.findViewById(R.id.itemNike_gia);
            motaSp = itemView.findViewById(R.id.itemNike_mota);
            hinhanh = itemView.findViewById(R.id.itemNike_image);
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
