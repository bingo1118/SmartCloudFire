package com.smart.cloud.fire.global;

import android.os.Environment;

import com.p2p.core.global.Config;

import java.io.File;

/**
 * Created by Administrator on 2016/7/29.
 */
public class AppConfig {
    public static int VideoMode=2;//0:流畅 1:高清 2标清

    public static class DeBug{
        public static final boolean isWrightAllLog=true;//是否写所有日志到SD�?
        public static final boolean isWrightErroLog=true;//是否记录错误日志到SD�?
    }

    public static class Relese {
        public static final String VERSION = Config.AppConfig.VERSION;
        public static final String APTAG = "GW_IPC_";
        public static final String PREPOINTPATH = Environment
                .getExternalStorageDirectory().getPath()
                + File.separator
                + "prepoint" + File.separator + NpcCommon.mThreeNum;
        public static final String SCREENSHORT = Environment
                .getExternalStorageDirectory().getPath()
                + File.separator
                + "screenshot";
    }
}
