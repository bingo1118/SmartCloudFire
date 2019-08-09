package com.smart.cloud.fire.global;

import com.smart.cloud.fire.view.BingoViewModel;

public class User implements BingoViewModel{

    String userId;
    String named;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNamed() {
        return named;
    }

    public void setNamed(String named) {
        this.named = named;
    }


    @Override
    public String getModelId() {
        return userId;
    }

    @Override
    public String getModelName() {
        return named;
    }
}
