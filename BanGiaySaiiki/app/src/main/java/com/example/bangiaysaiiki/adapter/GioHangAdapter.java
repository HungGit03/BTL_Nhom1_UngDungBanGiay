package com.example.bangiaysaiiki.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bangiaysaiiki.Interface.ImageClickListener;
import com.example.bangiaysaiiki.R;
import com.example.bangiaysaiiki.model.EventBus.TinhTong;
import com.example.bangiaysaiiki.model.GioHang;
import com.example.bangiaysaiiki.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong()+ " ");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        //Double.parseDouble(spMoi.getGiatien())-(Double.parseDouble(spMoi.getGiatien())/100*(spMoi.getGiamgia()))
        holder.item_giohang_gia.setText(decimalFormat.format(gioHang.getGiasp()-(gioHang.getGiasp()/100*(gioHang.getGiamgia())))+"Đ");
        long gia = gioHang.getSoluong() * (gioHang.getGiasp()-(gioHang.getGiasp()/100*(gioHang.getGiamgia())));
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
        holder.setImageClickListener(new ImageClickListener() {
            @Override
            public void OnImageClick(View view, int pos, int i) {
                Log.d("TAG","OnImageClick: "+ pos+"...."+i);
                if(i==1){
                    if(gioHangList.get(pos).getSoluong()>1){
                        int soLuongMoi = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soLuongMoi);


                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+ " ");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp() * (100 - gioHangList.get(pos).getGiamgia()) / 100;
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTong());


                    }else if(gioHangList.get(pos).getSoluong()==1) {
                        AlertDialog.Builder builder =  new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm ra giỏ hàng?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Util.mangGioHang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTong());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }
                }else if (i==2){
                    if(gioHangList.get(pos).getSoluong()<10){
                        int soLuongMoi = gioHangList.get(pos).getSoluong()+ 1;
                        gioHangList.get(pos).setSoluong(soLuongMoi);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+ " ");
                    long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp() * (100 - gioHangList.get(pos).getGiamgia()) / 100;
                    holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTong());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView item_giohang_image, img_tru, img_cong;
        TextView item_giohang_tensp, item_giohang_gia,item_giohang_soluong, item_giohang_giasp2;
        ImageClickListener imageClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            img_tru = itemView.findViewById(R.id.item_giohang_tru);
            img_cong = itemView.findViewById(R.id.item_giohang_cong);
            //event click
            img_cong.setOnClickListener(this);
            img_tru.setOnClickListener(this);

        }

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        @Override
        public void onClick(View view) {
            if (view== img_tru){
                imageClickListener.OnImageClick(view,getAdapterPosition(),1);
            }else if (view == img_cong){
                imageClickListener.OnImageClick(view,getAdapterPosition(),2);
            }
        }
    }
}
