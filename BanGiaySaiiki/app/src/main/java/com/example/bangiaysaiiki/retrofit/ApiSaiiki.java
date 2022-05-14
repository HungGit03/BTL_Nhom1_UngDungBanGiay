package com.example.bangiaysaiiki.retrofit;

import com.example.bangiaysaiiki.model.CtdhModel;
import com.example.bangiaysaiiki.model.DonhangModel;
import com.example.bangiaysaiiki.model.LoaiSPModel;
import com.example.bangiaysaiiki.model.MaGiamGiaModel;
import com.example.bangiaysaiiki.model.NguoiDungModel;
import com.example.bangiaysaiiki.model.SPMoiModel;
import com.example.bangiaysaiiki.model.TaiKhoanModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiSaiiki {

    @GET("getloaisp.php")
    Observable<LoaiSPModel> getLoaiSp();

    @GET("getsp.php")
    Observable<SPMoiModel> getSpNew();


    @POST("detail.php")
    @FormUrlEncoded
    Observable<SPMoiModel> getSpDetail(
            @Field("page") int page,
            @Field("loai") int loai
    );
    @POST("getspbyid.php")
    @FormUrlEncoded
    Observable<SPMoiModel> getSpByid(
            @Field("masp") int masp
    );

    @POST("dangky.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel>  dangky(
            @Field("sdt") String sdt,
            @Field("matkhau") String matkhau
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<TaiKhoanModel>  dangnhap(
            @Field("sdt") String sdt,
            @Field("matkhau") String matkhau
    );
    @POST("themnguoidung.php")
    @FormUrlEncoded
    Observable<NguoiDungModel>  dangkynguoidung(
            @Field("sdt") String sdt,
            @Field("email") String email,
            @Field("matk") int matk
    );

    @POST("getnguoidungbymatk.php")
    @FormUrlEncoded
    Observable<NguoiDungModel>  getnguoidungbymatk(
            @Field("matk") int matk
    );

    @POST("updatenguoidung.php")
    @FormUrlEncoded
    Observable<NguoiDungModel>  updatenguoidung(
            @Field("matk") String matk,
            @Field("tennguoidung") String tennguoidung,
            @Field("gioitinh") String gioitinh,
            @Field("sdt") String sdt,
            @Field("email") String email
    );

    @POST("getchitietdonhangbyidnd.php")
    @FormUrlEncoded
    Observable<CtdhModel> getlichsumuahang(
            @Field("mand") int mand
    );

    @POST("adddonhang.php")
    @FormUrlEncoded
    Observable<DonhangModel>  adddonhang(
            @Field("mand") int mand,
            @Field("masp") String masp,
            @Field("diachigiao") String diachigiao,
            @Field("soluong") String soluong,
            @Field("chietkhau") double chietkhau
    );

    @POST("getmgg.php")
    @FormUrlEncoded
    Observable<MaGiamGiaModel>  getmagg(
            @Field("magiamgia") String magiamgia
    );



}
