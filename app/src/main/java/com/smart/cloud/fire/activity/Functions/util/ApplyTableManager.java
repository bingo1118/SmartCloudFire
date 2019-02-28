package com.smart.cloud.fire.activity.Functions.util;


import com.smart.cloud.fire.activity.Functions.constant.Constant;
import com.smart.cloud.fire.activity.Functions.model.ApplyTable;
import com.smart.cloud.fire.global.MyApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

public class ApplyTableManager {
    private static int[] fixed = new int[]{0, 1};

    /**
     * 加载新闻类型
     *
     * @return
     */
    public static List<ApplyTable> loadNewsChannelsMore(int privilege) {
//        List<String> channelName = Arrays.asList(MyApp.app.getResources().getStringArray(R.array.all_apply_name));
//        List<String> channelId = Arrays.asList(MyApp.app.getResources().getStringArray(R.array.all_apply_id));
//        List<Integer> channelImgRes = new ArrayList<Integer>();
//        channelImgRes.add(R.drawable.shouye_anniu_tjsb_android);
//        channelImgRes.add(R.drawable.shouye_anniu_sxcs_android);
//        channelImgRes.add(R.drawable.shouye_anniu_zddw_android);
//        channelImgRes.add(R.drawable.shouye_anniu_dqfh_android);
//        channelImgRes.add(R.drawable.shouye_anniu_xfwl_android);
//        channelImgRes.add(R.drawable.shouye_anniu_spjk_android);
//        channelImgRes.add(R.drawable.shouye_anniu_wbxt_android);
//        if(privilege!=51){
//            channelImgRes.add(R.drawable.shouye_anniu_zjgl);
//        }
//        ArrayList<ApplyTable> newsChannelTables = new ArrayList<>();
//        for (int i = 0; i < channelName.size(); i++) {
//            ApplyTable entity = new ApplyTable(
//                    channelName.get(i),
//                    channelId.get(i),
//                    i,
//                    isFixed(i),
//                    channelImgRes.get(i),
//                    i < 8 ? 0 : 1);
//            newsChannelTables.add(entity);
//        }
        ArrayList<ApplyTable> newsChannelTables = new ArrayList<>();
        newsChannelTables.add(new ApplyTable("添加设备","0", 0, isFixed(0), R.drawable.shouye_anniu_tjsb_android, 0));
        newsChannelTables.add(new ApplyTable("重点单位","1", 1, isFixed(1), R.drawable.shouye_anniu_sxcs_android, 0));
        newsChannelTables.add(new ApplyTable("传输装置","2", 2, isFixed(2), R.drawable.shouye_anniu_zddw_android, 0));
        newsChannelTables.add(new ApplyTable("电气防火","3", 3, isFixed(3), R.drawable.shouye_anniu_dqfh_android, 0));
        newsChannelTables.add(new ApplyTable("消防物联","4", 4, isFixed(4), R.drawable.shouye_anniu_xfwl_android, 0));
        newsChannelTables.add(new ApplyTable("视频监控","5", 5, isFixed(5), R.drawable.shouye_anniu_zddw_android, 0));
        newsChannelTables.add(new ApplyTable("维保系统","6", 6, isFixed(6), R.drawable.shouye_anniu_xfwl_android, 0));
        if(privilege!=51){
            newsChannelTables.add(new ApplyTable("主机管理","7", 7, isFixed(7), R.drawable.shouye_anniu_zddw_android, 0));
        }
        return newsChannelTables;
    }

    /**
     * 加载固定新闻类型
     *
     * @return
     */

    public static List<ApplyTable> loadNewsChannelsStatic(int privilege) {
//        List<String> channelName = Arrays.asList(MyApp.app.getResources().getStringArray(R.array.all_apply_name_s));
//        System.out.println(channelName);
//        List<String> channelId = Arrays.asList(MyApp.app.getResources().getStringArray(R.array.all_apply_id_s));
//        List<Integer> channelImgRes = new ArrayList<Integer>();
//        channelImgRes.add(R.drawable.shouye_anniu_tjsb_android);
//        channelImgRes.add(R.drawable.shouye_anniu_sxcs_android);
//        channelImgRes.add(R.drawable.shouye_anniu_zddw_android);
//        channelImgRes.add(R.drawable.shouye_anniu_dqfh_android);
//        channelImgRes.add(R.drawable.shouye_anniu_xfwl_android);
//        channelImgRes.add(R.drawable.shouye_anniu_spjk_android);
//        channelImgRes.add(R.drawable.shouye_anniu_wbxt_android);
//        if(privilege!=51){
//            channelImgRes.add(R.drawable.shouye_anniu_zjgl);
//        }

        ArrayList<ApplyTable> newsChannelTables = new ArrayList<>();
        newsChannelTables.add(new ApplyTable("添加设备","0", 0, isFixed(0), R.drawable.shouye_anniu_tjsb_android, 0));
        newsChannelTables.add(new ApplyTable("重点单位","1", 1, isFixed(1), R.drawable.shouye_anniu_sxcs_android, 0));
        newsChannelTables.add(new ApplyTable("传输装置","2", 2, isFixed(2), R.drawable.shouye_anniu_zddw_android, 0));
        newsChannelTables.add(new ApplyTable("电气防火","3", 3, isFixed(3), R.drawable.shouye_anniu_dqfh_android, 0));
        newsChannelTables.add(new ApplyTable("消防物联","4", 4, isFixed(4), R.drawable.shouye_anniu_xfwl_android, 0));
        newsChannelTables.add(new ApplyTable("视频监控","5", 5, isFixed(5), R.drawable.shouye_anniu_zddw_android, 0));
        newsChannelTables.add(new ApplyTable("维保系统","6", 6, isFixed(6), R.drawable.shouye_anniu_xfwl_android, 0));
        if(privilege!=51){
            newsChannelTables.add(new ApplyTable("主机管理","7", 7, isFixed(7), R.drawable.shouye_anniu_zddw_android, 0));
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
