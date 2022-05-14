package com.example.bangiaysaiiki.model;

import java.util.List;

public class CtdhModel {
    private boolean success;
    private String message;
    private List<Ctdh> result;

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

    public List<Ctdh> getResult() {
        return result;
    }

    public void setResult(List<Ctdh> result) {
        this.result = result;
    }
}
