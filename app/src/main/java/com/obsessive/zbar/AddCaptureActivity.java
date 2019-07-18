package com.obsessive.zbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.smart.cloud.fire.activity.AddDev.AddDevActivity;
import com.smart.cloud.fire.activity.AddNFC.AddNFCActivity;
import com.smart.cloud.fire.global.DeviceType;
import com.smart.cloud.fire.utils.JsonUtils;
import com.smart.cloud.fire.utils.TestAuthorityUtil;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.IOException;
import java.lang.reflect.Field;

import fire.cloud.smart.com.smartcloudfire.R;

import android.hardware.Camera;
import android.hardware.camera2.CameraManager;

public class AddCaptureActivity  extends Activity implements View.OnClickListener{

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private com.obsessive.zbar.CameraManager mCameraManager;

    private Context mContext;
    private FrameLayout scanPreview;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;

    private Rect mCropRect = null;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private ImageScanner mImageScanner = null;

    private ImageButton sdsr_btn,light_btn;

    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_capture);
        mContext=this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewById();
        testCameraOpen();
        //initViews();
    }

    private void findViewById() {
        scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        sdsr_btn=(ImageButton)findViewById(R.id.sdsr_btn);
        light_btn=(ImageButton)findViewById(R.id.light_btn);
        sdsr_btn.setOnClickListener(this);
        light_btn.setOnClickListener(this);
    }

    private CameraManager manager;// 声明CameraManager对象
    boolean lightStatus=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdsr_btn:
                Intent intent = new Intent(mContext, AddDevActivity.class);
                startActivity(intent);
                break;
            case R.id.light_btn:
                if (lightStatus) { // 关闭手电筒
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            manager.setTorchMode("0", false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (mCamera != null) {
//                            mCamera.stopPreview();
//                            mCamera.release();
                            final Camera.Parameters parameter = mCamera.getParameters();
                            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            mCamera.setParameters(parameter);
//                            mCamera = null;
                        }
                    }
                    lightStatus=false;
                } else { // 打开手电筒
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            manager.setTorchMode("0", true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        final PackageManager pm = getPackageManager();
                        final FeatureInfo[] features = pm.getSystemAvailableFeatures();
                        for (final FeatureInfo f : features) {
                            if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) { // 判断设备是否支持闪光灯
                                if (null == mCamera) {
                                    mCamera = Camera.open();
                                }
                                final Camera.Parameters parameters = mCamera.getParameters();
                                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                mCamera.setParameters(parameters);
                                mCamera.startPreview();
                            }
                        }
                        lightStatus=true;
                    }
                    break;
                }
        }
    }

    private void testCameraOpen(){
        if (TestAuthorityUtil.testCamera(mContext)) {
            initViews();
        }else {
            finish();
        }
    }

    private void initViews() {
        mImageScanner = new ImageScanner();
        mImageScanner.setConfig(0, Config.X_DENSITY, 3);
        mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

        autoFocusHandler = new Handler();
        mCameraManager = new com.obsessive.zbar.CameraManager(this);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera = mCameraManager.getCamera();
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        scanPreview.addView(mPreview);

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.85f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        scanLine.startAnimation(animation);
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        finish();
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };


    private boolean ifGetData=false;//@@2018.06.08 是否已经扫描到数据

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Size size = camera.getParameters().getPreviewSize();

            // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < size.height; y++) {
                for (int x = 0; x < size.width; x++)
                    rotatedData[x * size.height + size.height - y - 1] = data[x
                            + y * size.width];
            }

            // 宽高也要调整
            int tmp = size.width;
            size.width = size.height;
            size.height = tmp;

            initCrop();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(rotatedData);
            barcode.setCrop(mCropRect.left, mCropRect.top, mCropRect.width(),
                    mCropRect.height());

            int result = mImageScanner.scanImage(barcode);
            String resultStr = null;

            if (result != 0) {
                SymbolSet syms = mImageScanner.getResults();
                for (Symbol sym : syms) {
                    resultStr = sym.getData();
                }
            }

            if (!TextUtils.isEmpty(resultStr)&&!ifGetData) {
                String temp= JsonUtils.isJson(resultStr);
                if(temp!=null){
                    resultStr=temp;
                }
                DeviceType devType=getDevType(resultStr);
                Intent intent=new Intent(mContext,AddDevActivity.class);
                intent.putExtra("devType",devType.getDeviceName());
                intent.putExtra("mac",resultStr);
                startActivity(intent);
                ifGetData=true;
                finish();
            }
        }
    };



    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = mCameraManager.getCameraResolution().y;
        int cameraHeight = mCameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    private DeviceType getDevType(String smokeMac) {
        int deviceType = 1;//烟感。。
        String deviceName="";

        String macStr = (String) smokeMac.subSequence(0, 1);
        if (smokeMac.length() == 15) {
//            deviceType="14";//GPS
            deviceType = 41;//海曼NB
            deviceName="HM烟感";
        } else if (smokeMac.length() == 12) {
            deviceType = 51;//创安
            deviceName="CA燃气";
        } else if (smokeMac.length() == 16 || smokeMac.length() == 18) {
            switch (macStr) {
                case "A":
                    smokeMac = smokeMac.substring(1, smokeMac.length());//三江无线传输设备
                    deviceType = 119;
                    deviceName="无线传输装置";
                    break;
                case "W":
                    if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("W")) {
                        deviceType =19;//@@水位2018.01.02
                        deviceName="水位传感器";
                    } else if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("A")) {
                        deviceType = 124;//@@拓普水位2018.01.30
                        deviceName="水位传感器";
                    } else if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("B")) {
                        deviceType = 125;//@@拓普水压2018.01.30
                        deviceName="水压传感器";

                    } else {
                        deviceType = 10;//@@水压
                        deviceName="水压传感器";
                    }
                    break;
                case "Z":
                    deviceType = 55;
                    deviceName="JD烟感";
                    break;
                default:
                    deviceType = 21;//loraOne烟感
                    deviceName="烟感";
                    break;
            }
        } else if (smokeMac.contains("-")) {
            deviceType = 31;//三江nb烟感
            deviceName="SJ烟感";
        } else {
            switch (macStr) {
                case "R":
                    if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("R")) {//@@9.29 区分NB
                        deviceType = 16;//@@NB燃气
                        deviceName="NB燃气探测器";
                    } else {
                        deviceType = 2;//@@燃气
                        deviceName="燃气探测器";
                    }
                    smokeMac = smokeMac.replace("R", "");//燃气
                    break;
                case "Q":
                    deviceType = 5;
                    deviceName="电气设备";
                    if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("Q")) {
//                        electrState=1;
                    }//@@8.26
                    if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("S")) {
//                        electrState=3;
                    }//@@2018.01.18 三相设备
                    if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("L")) {
//                        electrState=1;
                        deviceType = 52;
                        deviceName="Lora电气设备";
                    }//@@2018.05.15 Lara电气设备
                    if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("N")) {
//                        electrState=1;
                        deviceType = 53;
                        deviceName="NB电气设备";
                    }//@@2018.05.15 Lara电气设备

                    break;
                case "T":
                    smokeMac = smokeMac.replace("T", "");//温湿度设备
                    deviceType = 25;
                    deviceName="温湿度传感器";
                    break;
                case "A":
                    smokeMac = smokeMac.substring(1, smokeMac.length());
                    deviceType = 119;
                    deviceName="有线主机";
                    break;
                case "G":
                    smokeMac = smokeMac.replace("G", "");//声光报警器 6
                    deviceType = 7;
                    deviceName="声光报警器";
                    break;
                case "K":
                    smokeMac = smokeMac.replace("K", "");//@@无线输出输入模块2018.01.24
                    deviceType = 20;
                    deviceName="无线输入输出模块";
                    break;
                case "S":
                    smokeMac = smokeMac.replace("S", "");//手动报警，显示 7
                    deviceType = 8;
                    deviceName="手动报警器";
                    break;
                case "J":
                    smokeMac = smokeMac.replace("J", "");//三江设备
                    deviceType = 9;
                    deviceName="三江设备";
                    break;
                case "W":
                    if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("W")) {
                        deviceType = 19;//@@水位2018.01.02
                        deviceName="水位传感器";
                    } else if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("C")) {
                        deviceType = 42;//@@NB水压
                        deviceName="水压传感器";
                        smokeMac = smokeMac.substring(0, smokeMac.length() - 1);
                    } else if ((smokeMac.charAt(smokeMac.length() - 1) + "").equals("L")) {
                        deviceType = 43;//@@Lara水压
                        deviceName="Lora水压探测器";
                        smokeMac = smokeMac.substring(0, smokeMac.length() - 1);
                    } else {
                        deviceType = 10;//@@水压
                        deviceName="水压探测器";
                    }
                    break;
                case "L":
                    smokeMac = smokeMac.replace("L", "");//红外设备
                    deviceType = 11;
                    deviceName="红外探测器";
                    break;
                case "M":
                    smokeMac = smokeMac.replace("M", "");//门磁设备
                    deviceType = 12;
                    deviceName="门磁探测器";
                    break;
                case "N":
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("N")){
                        deviceType = 56;
                        deviceName="NB烟感";//@@NB-iot烟感
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("O")){
                        deviceType = 57;
                        deviceName="Onet烟感";//@@NB-iot烟感
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("Z")){
                        deviceType = 58;
                        deviceName="JD烟感";//@@NB-iot烟感
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("R")){
                        deviceType = 45;
                        deviceName="HM气感";//@@NB-iot烟感
                    }else{
                        deviceName="NB烟感";
                    }
                    break;
                case "H":
                    smokeMac = smokeMac.replace("H", "");//空气探测器
                    deviceType = 13;
                    deviceName="环境探测器";
                    break;
                case "Y":
                    smokeMac = smokeMac.replace("Y", "");//水禁
                    deviceType = 15;
                    deviceName="水浸探测器";
                    break;
                case "P":
                    smokeMac = smokeMac.replace("P", "");//10.31喷淋
                    deviceType = 18;
                    deviceName="喷淋设备";
                    break;
            }
        }
        return new DeviceType(deviceType,deviceName);
        }
    }
