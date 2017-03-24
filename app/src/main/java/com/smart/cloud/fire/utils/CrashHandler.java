package com.smart.cloud.fire.utils;

/**
 * Created by Administrator on 2016/8/26.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import com.smart.cloud.fire.global.ConstantValues;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告
 *
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    public static File zipFile;
    private static long timetamp = System.currentTimeMillis();

    private static final boolean isUploadError = true;
    // 错误日志记录位置
    public static String path = "/sdcard/UploadAppError/log/";
    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
    private final Map<String, String> infos = new HashMap<String, String>();

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {

            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                T.showShort(mContext, "很抱歉,程序出现异常,即将退出.");
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfoile(ex);
        return true;
    }

    /**
     * 1、收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());

            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 2、保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfoile(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat logFt = new SimpleDateFormat("yyyyMMdd");
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("TIME")) {
                Date d = new Date(Long.valueOf(value + ""));
                sb.append(key + "=" + format.format(d) + "\n");
            } else {
                sb.append(key + "=" + value + "\n");
            }

        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            Date d = new Date(timestamp);
            // String time = formatter.format(new Date());
            String time = logFt.format(d);
            // String fileName = "crash-" + time + "-" + timestamp + ".txt";
            String fileName = "smartcloudfire-" + time + timetamp+ ".log";
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return null;
            }
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(path + fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(f, true);
            fos.write(sb.toString().getBytes());
            fos.close();
            if (!isUploadError)
                return fileName;
            sendErrorLogToServer(f, ConstantValues.ERROR_URL);
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 3、发错误日志到服务器
     *
     * @param
     * @param serverURL
     */
    public static void sendErrorLogToServer(File logFile, String serverURL) {
        SimpleDateFormat logFt = new SimpleDateFormat("yyyyMMdd");
        List<File> files = new ArrayList<File>();
        FileUtils.list(logFile.getParentFile(), "smartcloudfire", ".log", "3", files);
        String time = logFt.format(new Date());
        try {
            if (files.isEmpty())
                return;
            System.out.println("send start....");
            zipFile = new File(logFile.getParent() + "/smartcloudfire_" + time+timetamp
                    + ".rar");
            System.out.println("zipFile="+zipFile);
            ZipUtils.zipFiles(files, zipFile);
            System.out.println("serverURL="+serverURL);

            new MyTask().execute(serverURL);

        } catch (Exception e) {
            return;
        }
    }

    static class MyTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            UploadUtil uploadUtil = new UploadUtil();
            uploadUtil.uploadFile(zipFile, serverURL);
            return null;
        }

    }
}
