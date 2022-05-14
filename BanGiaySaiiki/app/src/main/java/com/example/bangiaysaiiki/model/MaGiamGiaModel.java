package com.example.bangiaysaiiki.model;

import java.util.List;

public class MaGiamGiaModel {
    boolean success;
    String message;
    List<MaGiamGia> result;

    public List<MaGiamGia> getResult() {
        return result;
    }

    public void setResult(List<MaGiamGia> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
