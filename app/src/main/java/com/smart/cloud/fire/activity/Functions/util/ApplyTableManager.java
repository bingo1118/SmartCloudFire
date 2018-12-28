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
    public static List<ApplyTable> loadNewsChannelsMore() {
        List<String> channelName = Arrays.asList(MyApp.app.getResources().getStringArray(R.array.all_apply_name));
        List<String> channelId = Arrays.asList(MyApp.app.getResources().getStringArray(R.array.all_apply_id));
        List<Integer> channelImgRes = new ArrayList<Integer>();
        channelImgRes.add(R.drawable.shouye_anniu_tjsb_android);
        channelImgRes.add(R.drawable.shouye_anniu_sxcs_android);
        channelImgRes.add(R.drawable.shouye_anniu_zddw_android);
        channelImgRes.add(R.drawable.shouye_anniu_dqfh_android);
        channelImgRes.add(R.drawable.shouye_anniu_xfwl_android);
        channelImgRes.add(R.drawable.shouye_anniu_spjk_android);
        channelImgRes.add(R.drawable.shouye_anniu_wbxt_android);
        channelImgRes.add(R.drawable.shouye_anniu_zjgl);
        ArrayList<ApplyTable> newsChannelTables = new ArrayList<>();
        for (int i = 0; i < channelName.size(); i++) {
            ApplyTable entity = new ApplyTable(
                    channelName.get(i),
                    channelId.get(i),
                    i,
                    isFixed(i),
                    channelImgRes.get(i),
                    i < 8 ? 0 : 1);
            newsChannelTables.add(entity);
        }
        return newsChannelTables;
    }

    /**
     * 加载固定新闻类型
     *
     * @return
     */

    public static List<ApplyTable> loadNewsChannelsStatic() {
        List<String> channelName = Arrays.asList(MyApp.app.getResources().getStringArray(R.array.all_apply_name_s));
        System.out.println(channelName);
        List<String> channelId = Arrays.asList(MyApp.app.getResources().getStringArray(R.array.all_apply_id_s));
        List<Integer> channelImgRes = new ArrayList<Integer>();
        channelImgRes.add(R.drawable.shouye_anniu_tjsb_android);
        channelImgRes.add(R.drawable.shouye_anniu_sxcs_android);
        channelImgRes.add(R.drawable.shouye_anniu_zddw_android);
        channelImgRes.add(R.drawable.shouye_anniu_dqfh_android);
        channelImgRes.add(R.drawable.shouye_anniu_xfwl_android);
        channelImgRes.add(R.drawable.shouye_anniu_spjk_android);
        channelImgRes.add(R.drawable.shouye_anniu_wbxt_android);
        channelImgRes.add(R.drawable.shouye_anniu_zjgl);
        ArrayList<ApplyTable> newsChannelTables = new ArrayList<>();
        for (int i = 0; i < channelName.size(); i++) {
            ApplyTable entity = new ApplyTable(channelName.get(i), channelId.get(i)
                    , i, isFixed(i), channelImgRes.get(i), 0);
            newsChannelTables.add(entity);
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
