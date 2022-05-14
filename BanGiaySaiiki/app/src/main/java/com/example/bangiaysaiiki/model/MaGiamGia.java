package com.example.bangiaysaiiki.model;


import java.io.Serializable;

public class MaGiamGia  implements Serializable {
    private String id;
    private double chietkhau;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getChietkhau() {
        return chietkhau;
    }

    public void setChietkhau(double chietkhau) {
        this.chietkhau = chietkhau;
    }

    }
