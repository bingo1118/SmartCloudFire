package com.smart.cloud.fire.activity.Functions.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 应用描述
 */
public class ApplyTable implements Serializable {
    private String name;//显示的文字，应用的名称
    private String id;
    private boolean fixed;//点击是否可以进行增删
    private int index;//控件的初始位置，可以用于判断长按是否可以拖拽
    private String imgRes;//显示的图片的名称
    private int state;//是否处于头部的状态,0：在头部，1：不在头部

    public ApplyTable() {
    }

    public ApplyTable(String name) {
        this.name = name;
    }

    public ApplyTable(String name, String id,
                      int index, boolean fixed, String imgRes, int state) {
        this.name = name;
        this.id = id;
        this.fixed = fixed;
        this.index = index;
        this.imgRes = imgRes;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImgRes() {
        return imgRes;
    }

    public void setImgRes(String imgRes) {
        this.imgRes = imgRes;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ApplyTable{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", fixed=" + fixed +
                ", index=" + index +
                ", imgRes=" + imgRes +
                ", state=" + state +
                '}';
    }
}
