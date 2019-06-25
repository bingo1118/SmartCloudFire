package com.smart.cloud.fire.activity.Functions.util;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.smart.cloud.fire.activity.Functions.model.ApplyTable;
import com.smart.cloud.fire.global.MyApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ApplyTableManager {
    private static int[] fixed = new int[]{0, 1};

    /**
     * 加载新闻类型
     *
     * @return
     */
    public static List<ApplyTable> loadNewsChannelsMore(int privilege) {

        ArrayList<ApplyTable> newsChannelTables = new ArrayList<>();
        newsChannelTables.add(new ApplyTable("添加设备","0", 0, isFixed(0),"tjsb.png", 0));
        newsChannelTables.add(new ApplyTable("重点单位","1", 1, isFixed(1),"sxcs.png", 0));
        newsChannelTables.add(new ApplyTable("传输装置","2", 2, isFixed(2),"zddw.png", 0));
        newsChannelTables.add(new ApplyTable("电气防火","3", 3, isFixed(3), "dqfh.png", 0));
        newsChannelTables.add(new ApplyTable("消防物联","4", 4, isFixed(4),  "xfwl.png", 0));
        newsChannelTables.add(new ApplyTable("视频监控","5", 5, isFixed(5),  "spjk.png", 0));
        newsChannelTables.add(new ApplyTable("维保系统","6", 6, isFixed(6),  "wbxt.png", 0));
        newsChannelTables.add(new ApplyTable("巡检系统","8", 8, isFixed(8), "xunjian.png", 0));
        if(privilege!=51){
            newsChannelTables.add(new ApplyTable("主机管理","7", 7, isFixed(7), "zjgl.png", 0));
        }
        return newsChannelTables;
    }

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 加载固定新闻类型
     *
     * @return
     */

    public static List<ApplyTable> loadNewsChannelsStatic(int privilege) {
        ArrayList<ApplyTable> newsChannelTables = new ArrayList<>();
        newsChannelTables.add(new ApplyTable("添加设备","0", 0, isFixed(0),"tjsb.png", 0));
        newsChannelTables.add(new ApplyTable("重点单位","1", 1, isFixed(1),"sxcs.png", 0));
        newsChannelTables.add(new ApplyTable("传输装置","2", 2, isFixed(2),"zddw.png", 0));
        newsChannelTables.add(new ApplyTable("电气防火","3", 3, isFixed(3), "dqfh.png", 0));
        newsChannelTables.add(new ApplyTable("消防物联","4", 4, isFixed(4),  "xfwl.png", 0));
        newsChannelTables.add(new ApplyTable("视频监控","5", 5, isFixed(5),  "spjk.png", 0));
        newsChannelTables.add(new ApplyTable("维保系统","6", 6, isFixed(6),  "wbxt.png", 0));
        newsChannelTables.add(new ApplyTable("巡检系统","8", 8, isFixed(8), "xunjian.png", 0));
        if(privilege!=51){
            newsChannelTables.add(new ApplyTable("主机管理","7", 7, isFixed(7), "zjgl.png", 0));
        }
        return newsChannelTables;
    }

    public static boolean isFixed(int i) {
        for (int j = 0; j < fixed.length; j++) {
            return i == fixed[j] ? true : false;
        }
        return false;
    }
}
