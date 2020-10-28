package com.smart.cloud.fire.order;

public class OrderInfo {

    private int id;
    private String jkey;
    private int operate;
    private String sendUser;
    private String sendTime;
    private String receiveUser;
    private String description;
    private String result;
    private String picture;
    private String video;
    private String operateName;
    private String sendUserName;
    private String receiveUserName;
    private int state;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getJkey() {
        return jkey;
    }
    public void setJkey(String jkey) {
        this.jkey = jkey;
    }
    public int getOperate() {
        return operate;
    }
    public void setOperate(int operate) {
        this.operate = operate;
    }
    public String getSendUser() {
        return sendUser;
    }
    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }
    public String getSendTime() {
        return sendTime;
    }
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
    public String getReceiveUser() {
        return receiveUser;
    }
    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getVideo() {
        return video;
    }
    public void setVideo(String video) {
        this.video = video;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
