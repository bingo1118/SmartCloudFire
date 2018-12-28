package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2018/12/7.
 */
public class ItemBean {
    int itemImage;
    String itemTitle;

    public ItemBean(int itemImage, String itemTitle) {
        this.itemImage = itemImage;
        this.itemTitle = itemTitle;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
