package com.smart.cloud.fire.order.DealOrder;

public class DealerBean {

    public DealerBean(String userId, String named) {
        this.userId = userId;
        this.named = named;
    }

    public String userId;
    public String named;

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

}
