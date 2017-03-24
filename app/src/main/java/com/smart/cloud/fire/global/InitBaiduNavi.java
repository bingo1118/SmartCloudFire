package com.smart.cloud.fire.global;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.ui.BNDemoGuideActivity;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.TestAuthorityUtil;
import com.smart.cloud.fire.view.NormalDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class InitBaiduNavi {
    private Activity mActivity;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    private String mSDCardPath = null;
    String authinfo = null;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private double lati;
    private double lon;
    private double toLat;
    private double toLon;
    private Smoke normalSmoke;
    private NormalDialog mNormalDialog;

    public InitBaiduNavi(Activity mActivity, Smoke normalSmoke){
        this.mActivity = mActivity;
        if(!TestAuthorityUtil.testLocation(mActivity.getApplicationContext())){
            return;
        }
        this.normalSmoke = normalSmoke;
        if(mNormalDialog==null){
            mNormalDialog = new NormalDialog(mActivity);
        }
        mNormalDialog.showLoadingDialog("路线规划中...");
        mLocationClient = new LocationClient(mActivity.getApplicationContext());     //声明LocationClient类
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(3000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setOpenGps(true);// 打开GPS
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        BNOuterLogUtil.setLogSwitcher(true);
        if (initDirs()) {
            initNavi();
        }
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            int result = bdLocation.getLocType();
            switch (result){
                case 61:
                    lati = bdLocation.getLatitude();
                    lon =bdLocation.getLongitude();
                    routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
                    break;
                case 62:
                    T.showShort(mActivity,"定位失败,请检查运营商网络或者wifi网络是否正常开启");
                    break;
                case 63:
                    T.showShort(mActivity,"网络异常，请确认当前测试手机网络是否通畅");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 65:
                    T.showShort(mActivity,"定位缓存的结果");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 66:
                    T.showShort(mActivity,"离线定位结果");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 67:
                    T.showShort(mActivity,"离线定位失败");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 68:
                    T.showShort(mActivity," 网络连接失败时，查找本地离线定位时对应的返回结果");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 161:
                    lati = bdLocation.getLatitude();
                    lon =bdLocation.getLongitude();
                    routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
                    break;
                case 162:
                    T.showShort(mActivity,"定位失败,请检查运营商网络或者wifi网络是否正常开启");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 167:
                    T.showShort(mActivity,"服务端定位失败，请您检查是否禁用获取位置信息权限");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 502:
                    T.showShort(mActivity,"定位失败,请检查运营商网络或者wifi网络是否正常开启");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 505:
                    T.showShort(mActivity,"定位失败,请检查运营商网络或者wifi网络是否正常开启");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 601:
                    T.showShort(mActivity,"定位失败,请检查运营商网络或者wifi网络是否正常开启");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                case 602:
                    T.showShort(mActivity,"定位失败,请检查运营商网络或者wifi网络是否正常开启");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
                default:
                    T.showShort(mActivity,"定位失败,请检查运营商网络或者wifi网络是否正常开启");
                    if(mNormalDialog!=null){
                        mNormalDialog.dismiss();
                        mNormalDialog=null;
                    }
                    break;
            }
        }
    }

    private void initNavi() {
        BaiduNaviManager.getInstance().init(mActivity, mSDCardPath, APP_FOLDER_NAME, mNaviInitListener, null, ttsHandler, null);
    }


    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }


    private BaiduNaviManager.NaviInitListener mNaviInitListener = new BaiduNaviManager.NaviInitListener(){
        @Override
        public void onAuthResult(int status, String msg) {
            if (0 == status) {
                authinfo = "key校验成功!";
            } else {
                authinfo = "key校验失败, " + msg;
            }

        }

        public void initSuccess() {
            //Toast.makeText(mActivity, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
            initSetting();
        }

        public void initStart() {
            //Toast.makeText(mActivity, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
        }

        public void initFailed() {
            //Toast.makeText(mActivity, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
        }
    };

    private void initSetting(){
        BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        double latitude = Double.parseDouble(normalSmoke.getLatitude());
        double longitude = Double.parseDouble(normalSmoke.getLongitude());
        GetLoad(latitude,longitude);
    }

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    //T.showShort(mActivity, "Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    //T.showShort(mActivity, "Handler : TTS play end");
                    break;
                }
                default :
                    break;
            }
        }
    };

    private void GetLoad(double fromLat,double formLon){
        toLat = fromLat;
        toLon = formLon;

        if (BaiduNaviManager.isNaviInited()) {
            mLocationClient.start();
        }
    }


    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch (coType) {
            case BD09LL: {
                sNode = new BNRoutePlanNode(lon,lati, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(toLon, toLat, "北京天安门", null, coType);
                break;
            }
            default:
                break;
        }
        if (sNode != null && eNode != null) {
            stopLead();
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(mActivity, list, 1, true, new DemoRoutePlanListener(sNode));

        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */
            Intent intent = new Intent(mActivity, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
            if(mNormalDialog!=null){
                mNormalDialog.dismiss();
                mNormalDialog=null;
            }
            Intent i = new Intent();
            i.setAction("CLOSE_ALARM_ACTIVITY");
            mActivity.sendBroadcast(i);
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(mActivity, "算路失败", Toast.LENGTH_SHORT).show();
            if(mNormalDialog!=null){
                mNormalDialog.dismiss();
                mNormalDialog=null;
            }
        }
    }

    private void stopLead(){
        if(mLocationClient!=null){
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
            mLocationClient=null;
        }
    }

}
