package com.smart.cloud.fire.order.DealOrder;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.blankj.utilcode.util.StringUtils;
import com.smart.cloud.fire.activity.UploadNFCInfo.FileUtil;
import com.smart.cloud.fire.activity.UploadNFCInfo.FormFile;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.utils.NFCHelper;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.UploadUtil;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class DealOrderActivity extends Activity {

    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.uid_name)
    EditText uid_name;//@@uid
    @Bind(R.id.memo_name)
    EditText memo_name;//@@备注
    @Bind(R.id.photo_image)
    ImageView photo_image;//@@拍照上传
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;
    @Bind(R.id.state_rg)
    RadioGroup state_rg;
    @Bind(R.id.no_rb)
    RadioButton no_rb;

    @Bind(R.id.bmapView)
    MapView mMapView;

    @Bind(R.id.uid_edit)
    EditText uid_edit;//MAC
    @Bind(R.id.uid_tv)
    TextView uid_tv;
    @Bind(R.id.nfc_rela)
    RelativeLayout nfc_rela;//MAC
    private NFCHelper nfcHelper;

    private Context mContext;

    private String uploadTime;
    private JobOrder order;
    private int type;


    private String imageFilePath;
    File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/filename.jpg");//@@9.30

    private LocationClient mLocationClient;

    LatLng point_device;
    LatLng point_dealer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_order);

        ButterKnife.bind(this);
        mContext = this;

        order = (JobOrder) getIntent().getSerializableExtra("order");
        type=order.getType();


        initView();
        getDeviceData();
        if (f.exists()) {
            f.delete();
        }//@@9.30
    }


    private BaiduMap mBaiduMap;
    public MyLocationListener mMyLocationListener;
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    private volatile boolean isFristLocation = true;

    GeoCoder geoCoder;
    private void initView() {
        mBaiduMap = mMapView.getMap();// 获得MapView
        initMyLocation();
        if(order.getType()==6){
            mMapView.setVisibility(View.VISIBLE);
            geoCoder = GeoCoder.newInstance();
        }else{
            mMapView.setVisibility(View.GONE);
        }

        if(order.getType()==5){
            nfc_rela.setVisibility(View.VISIBLE);
            nfcHelper=NFCHelper.getInstance(this);
            if (!nfcHelper.isSupportNFC()) {
                T.showShort(mContext,"设备不支持NFC功能");
                return;
            }
        }else{
            nfc_rela.setVisibility(View.GONE);
        }

        if(type==5||type==6){{
            state_rg.setVisibility(View.VISIBLE);
        }}else{
            state_rg.setVisibility(View.GONE);
        }
        addFireDevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order.getType()==5){
                    if(!uid_tv.getText().toString().equals(uid_edit.getText().toString())){
                        T.showShort(mContext, "请先扫描对应标签信息");
                        dismissProgressBarOnUiThread();
                        return;
                    }
                }

                if(order.getType()==6){
                    if(DistanceUtil. getDistance(point_dealer, point_dealer)>distance){
                        T.showShort(mContext, "请先移动到正确距离范围内");
                        dismissProgressBarOnUiThread();
                        return;
                    }
                }

                showProgressBarOnUiThread();
                if (uid_name.getText() == null) {
                    T.showShort(mContext, "请先录入处理信息");
                    dismissProgressBarOnUiThread();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSuccess = false;
                        boolean isHavePhoto = false;
                        if (imageFilePath != null) {
                            File file = new File(imageFilePath); //这里的path就是那个地址的全局变量
                            uploadTime = System.currentTimeMillis() + "";
                            if (f.exists()) {
                                isHavePhoto = true;
                            }//@@11.07
                            isSuccess = UploadUtil.uploadFile(file, MyApp.getUserID(), "1", uploadTime, "", "orderDealImg");
                        }
                        VolleyHelper helper = VolleyHelper.getInstance(mContext);
                        RequestQueue mQueue = helper.getRequestQueue();
                        String url = "";
                        if (isHavePhoto && isSuccess) {
                            File file = new File(imageFilePath);//9.29
                            file.delete();//@@9.29
                            if(type==5||type==6){
                                String state="1";
                                if(no_rb.isChecked()){
                                    state="2";
                                }
                                url = ConstantValues.SERVER_IP_NEW + "uploadOrderResult?jkey=" + order.getJkey() + "&result=" + URLEncoder.encode(uid_name.getText().toString())
                                        + "&description=" + URLEncoder.encode(memo_name.getText().toString())
                                        + "&ifcheck=" + order.getIfcheck()
                                        + "&picture=" + uploadTime
                                        +"&state="+state
                                        +"&type="+type
                                        + "&userId="+MyApp.getUserID()
                                        +"&sendUser="+order.getPrincipalUser();
                            }else{
                                url = ConstantValues.SERVER_IP_NEW + "uploadOrderResult?jkey=" + order.getJkey() + "&result=" + URLEncoder.encode(uid_name.getText().toString())
                                        + "&description=" + URLEncoder.encode(memo_name.getText().toString())
                                        + "&ifcheck=" + order.getIfcheck()
                                        + "&picture=" + uploadTime
                                        + "&userId="+MyApp.getUserID()
                                        +"&sendUser="+order.getPrincipalUser();
                            }
                        } else {
                            if (isHavePhoto && !isSuccess) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        T.showShort(mContext, "图片上传失败");
                                    }
                                });
                            }
                            url = ConstantValues.SERVER_IP_NEW + "uploadOrderResult?jkey=" + order.getJkey() + "&result=" + URLEncoder.encode(uid_name.getText().toString())
                                    + "&description=" + URLEncoder.encode(memo_name.getText().toString())
                                    + "&ifcheck=" + order.getIfcheck()
                                    + "&picture="
                                    + "&userId="+MyApp.getUserID()
                                    + "&sendUser="+order.getPrincipalUser();
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            int errorCode = response.getInt("errorcode");
                                            String error = response.getString("error");
                                            if (errorCode == 0) {
                                                T.showShort(mContext, "记录上传成功");
                                                clearView();
                                                if (f.exists()) {
                                                    f.delete();
                                                }//@@9.30
                                            } else {
                                                T.showShort(mContext, error);
                                            }
                                            dismissProgressBarOnUiThread();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            dismissProgressBarOnUiThread();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                T.showShort(mContext, "网络错误");
                                dismissProgressBarOnUiThread();
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                    }
                }).start();

            }
        });

        photo_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/filename.jpg";
                File temp = new File(imageFilePath);
                if (!temp.exists()) {
                    Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
                    it.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
                    startActivityForResult(it, 102);
                } else {
                    //使用Intent
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(temp), "image/*");
                    startActivity(intent);
                }
            }
        });
    }

    private void clearView() {
        uid_name.setText("");
        memo_name.setText("");
        photo_image.setBackgroundResource(R.drawable.add_nfc_recor);
        imageFilePath = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bmp = BitmapFactory.decodeFile(imageFilePath);
                    try {
                        saveFile(compressBySize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/devimage.jpg", 1500, 2000), Environment.getExternalStorageDirectory().getAbsolutePath() + "/devimage.jpg");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int screenWidth = dm.widthPixels;
                    if (bmp.getWidth() <= screenWidth) {
                        photo_image.setImageBitmap(bmp);
                    } else {
                        Bitmap mp = Bitmap.createScaledBitmap(bmp, screenWidth, bmp.getHeight() * screenWidth / bmp.getWidth(), true);
                        photo_image.setImageBitmap(mp);
                    }
                }
                break;
            case 103:
                Bitmap bm = null;
                // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                ContentResolver resolver = getContentResolver();

                try {
                    Uri originalUri = data.getData(); // 获得图片的uri

                    bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片

                    // 这里开始的第二部分，获取图片的路径：

                    String[] proj = {MediaStore.Images.Media.DATA};

                    // 好像是android多媒体数据库的封装接口，具体的看Android文档
                    @SuppressWarnings("deprecation")
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    // 按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    // 最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
                    photo_image.setImageURI(originalUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //@@10.12压缩图片尺寸
    public Bitmap compressBySize(String pathName, int targetWidth,
                                 int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);// 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }//设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    //@@10.12存储文件到sd卡
    public void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);//检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));//100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }


    public static boolean uploadFile(File imageFile, String userId, String areaId, String uploadtime) {
        try {
            String requestUrl = ConstantValues.SERVER_IP_NEW + "UploadFileAction";
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userId);
            params.put("areaId", areaId);
            params.put("time", uploadtime);
            FormFile formfile = new FormFile(imageFile.getName(), imageFile, "image", "application/octet-stream");
            FileUtil.post(requestUrl, params, formfile);
            System.out.println("Success");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail");
            return false;
        }
    }

    private void showProgressBarOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void dismissProgressBarOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initMyLocation()
    {
        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }


    public class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置自定义图标
            View viewA = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.image_mark, null);
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromView(viewA);
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation)
            {
                isFristLocation = false;
                point_dealer = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point_dealer);
//                mBaiduMap.animateMapStatus(u);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(point_dealer).zoom(18).build()));//@@6.28
            }
        }

    }

    @Override
    protected void onStart()
    {
        // 开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();
        }
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        super.onStop();
    }

    double lat;
    double lon;
    double distance;

    private void getDeviceData() {
        if(order.getUid()==null){
            uid_tv.setVisibility(View.GONE);
            return;
        }else{
            uid_tv.setVisibility(View.VISIBLE);
        }

        VolleyHelper helper = VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url = ConstantValues.SERVER_IP_NEW + "getNfcTaskInfo?uid=" + order.getUid();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode = response.getInt("errorcode");
                            String error = response.getString("error");
                            if (errorCode == 0) {
                                lat=response.getJSONObject("object").getDouble("latitude");
                                lon=response.getJSONObject("object").getDouble("longitude");
                                distance=response.getJSONObject("object").getDouble("distance");
                                if(order.getType()==5){
                                    uid_tv.setText("请使用NFC扫描UID为"+order.getUid()+"的设备");
                                }
                                if(order.getType()==6){
                                    uid_tv.setText("请移动至距离设备"+distance+"米的范围内");
                                }
                                // 定义Maker坐标点
                                point_device = new LatLng(lat, lon);
                                // 构建MarkerOption，用于在地图上添加Marker
                                View viewA = LayoutInflater.from(MyApp.app).inflate(
                                        R.layout.image_mc_mark, null);
                                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                                        .fromView(viewA);
                                MarkerOptions options = new MarkerOptions().position(point_device)
                                        .icon(mCurrentMarker);
                                // 在地图上添加Marker，并显示
                                mBaiduMap.addOverlay(options);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext, "网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    @Override
    public void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if (!nfcHelper.ismWriteMode() && (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
                ||NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))) {//@@10.19
            byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String UID = Utils.ByteArrayToHexString(myNFCID);
            uid_edit.setText(UID);
        }
        // Tag writing mode
        if (nfcHelper.ismWriteMode() && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcHelper.writeTag(detectedTag);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcHelper.isSupportNFC()) {
            nfcHelper.changeToReadMode();
        }
    }

}
