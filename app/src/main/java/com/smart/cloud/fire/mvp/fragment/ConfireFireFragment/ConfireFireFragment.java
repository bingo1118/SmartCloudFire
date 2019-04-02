package com.smart.cloud.fire.mvp.fragment.ConfireFireFragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.jakewharton.rxbinding.view.RxView;
import com.obsessive.zbar.CaptureActivity;
import com.smart.cloud.fire.GetLocationActivity;
import com.smart.cloud.fire.activity.UploadNFCInfo.FileUtil;
import com.smart.cloud.fire.activity.UploadNFCInfo.FormFile;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.XCDropDownListView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ConfireFireFragment extends MvpFragment<ConfireFireFragmentPresenter> implements ConfireFireFragmentView {
    @Bind(R.id.add_repeater_mac)
    EditText addRepeaterMac;//集中器。。
    @Bind(R.id.add_fire_mac)
    EditText addFireMac;//探测器。。
    @Bind(R.id.add_fire_name)
    EditText addFireName;//设备名称。。
    @Bind(R.id.add_fire_lat)
    EditText addFireLat;//经度。。
    @Bind(R.id.add_fire_lon)
    EditText addFireLon;//纬度。。
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;//设备地址。。
    @Bind(R.id.add_fire_man)
    EditText addFireMan;//负责人姓名。。
    @Bind(R.id.add_fire_man_phone)
    EditText addFireManPhone;//负责人电话。。
    @Bind(R.id.add_fire_man_two)
    EditText addFireManTwo;//负责人2.。
    @Bind(R.id.add_fire_man_phone_two)
    EditText addFireManPhoneTwo;//负责人电话2.。
    @Bind(R.id.scan_repeater_ma)
    ImageView scanRepeaterMa;
    @Bind(R.id.scan_er_wei_ma)
    ImageView scanErWeiMa;
    @Bind(R.id.location_image)
    ImageView locationImage;
    @Bind(R.id.add_fire_zjq)
    XCDropDownListView addFireZjq;//选择区域。。
    @Bind(R.id.add_fire_type)
    XCDropDownListView addFireType;//选择类型。。
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;//添加设备按钮。。
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;//加载进度。。
    @Bind(R.id.add_camera_name)
    EditText addCameraName;
    @Bind(R.id.add_camera_relative)
    RelativeLayout addCameraRelative;
    @Bind(R.id.device_type_name)
    TextView device_type_name;
    @Bind(R.id.photo_image)
    ImageView photo_image;//@@拍照上传
    @Bind(R.id.tip_line)
    LinearLayout tip_line;
    @Bind(R.id.clean_all)
    TextView clean_all;

    private Context mContext;
    private int scanType = 0;//0表示扫描中继器，1表示扫描烟感
    private int privilege;
    private String userID;
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";
    private String camera = "";

    String mac="";
    String devType="";

    private String uploadTime;
    private String imageFilePath;
    File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/devimage.jpg");//@@9.30

    Handler handler = new Handler() {//@@9.29
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case 1:
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    T.showShort(mContext,"图片上传失败");
                    break;
                case 3:
                    T.showShort(mContext,"图片上传成功");
                    break;
                case 4:
                    T.showShort(mContext,"添加成功");
                    break;
                case 5:
                    T.showShort(mContext,msg.obj.toString());
                    photo_image.setImageResource(R.drawable.add_photo);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_fire, null);
        ButterKnife.bind(this, view);
        if(f.exists()){
            f.delete();
        }//@@9.30
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        init();
    }

    private void init() {
        addFireMac.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus&&addFireMac.getText().toString().length()>0) {
                    mvpPresenter.getOneSmoke(userID, privilege + "", addFireMac.getText().toString());//@@5.5如果添加过该烟感则显示出原来的信息
                }
            }
        });//@@10.18
        addCameraRelative.setVisibility(View.VISIBLE);
        addFireZjq.setEditTextHint("区域");
        addFireType.setEditTextHint("类型");
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                addFire();
            }
        });
        Intent intent=getActivity().getIntent();
        String mac=intent.getStringExtra("mac");
        String devType=intent.getStringExtra("devType");
        if (mac!=null){
            addFireMac.setText(mac);
            device_type_name.setVisibility(View.VISIBLE);
            device_type_name.setText("设备类型:"+devType);
            mvpPresenter.getOneSmoke(userID, privilege + "", mac);
        }
        photo_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/devimage.jpg";
                File temp = new File(imageFilePath);
                if(!temp.exists()){
                    Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
                    it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
                    startActivityForResult(it, 102);
                }else{
                    //使用Intent
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(temp), "image/*");
                    startActivity(intent);
                }

            }
        });

        clean_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanAllView();
            }
        });
    }

    /**
     * 添加设备，提交设备信息。。
     */
    private void addFire() {
        if (mShopType != null) {
            shopTypeId = mShopType.getPlaceTypeId();
        }
        if (mArea != null) {
            areaId = mArea.getAreaId();
        }
        final String longitude = addFireLon.getText().toString().trim();
        final String latitude = addFireLat.getText().toString().trim();
        final String smokeName = addFireName.getText().toString().trim();
        final String smokeMac = addFireMac.getText().toString().trim();
        final String address = addFireAddress.getText().toString().trim();
        final String placeAddress = "";
        final String principal1 = addFireMan.getText().toString().trim();
        final String principal2 = addFireManTwo.getText().toString().trim();
        final String principal1Phone = addFireManPhone.getText().toString().trim();
        final String principal2Phone = addFireManPhoneTwo.getText().toString().trim();
        final String repeater = addRepeaterMac.getText().toString().trim();
        camera = addCameraName.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess=false;
                boolean isHavePhoto=false;
                uploadTime=System.currentTimeMillis()+"";
                if(imageFilePath!=null){
                    File file = new File(imageFilePath); //这里的path就是那个地址的全局变量
                    if(f.exists()){
                        isHavePhoto=true;
                    }else{
                        isHavePhoto=false;
                    }
                    if(isHavePhoto){
                        isSuccess=uploadFile(file,userID,areaId,uploadTime,getdevmac(smokeMac),"devimages");
                        if(isSuccess){
                            Message message = new Message();
                            message.what = 3;
                            handler.sendMessage(message);
                        }else{
                            Message message = new Message();
                            message.what =2 ;
                            handler.sendMessage(message);
                        }
                    }


                    if(f.exists()){
                        f.delete();
                    }//@@9.30
                }
                mvpPresenter.addSmoke(userID, privilege + "", smokeName, smokeMac, address, longitude,
                        latitude, placeAddress, shopTypeId, principal1, principal1Phone, principal2,
                        principal2Phone, areaId, repeater, camera,isSuccess);
            }
        }).start();

//        mvpPresenter.addSmoke(userID, privilege + "", smokeName, smokeMac, address, longitude,
//                latitude, placeAddress, shopTypeId, principal1, principal1Phone, principal2,
//                principal2Phone, areaId, repeater, camera);
    }

    @Override
    protected ConfireFireFragmentPresenter createPresenter() {
        ConfireFireFragmentPresenter mConfireFireFragmentPresenter = new ConfireFireFragmentPresenter(ConfireFireFragment.this);
        return mConfireFireFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "ConfireFireFragment";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (addFireZjq.ifShow()) {
            addFireZjq.closePopWindow();
        }
        if (addFireType.ifShow()) {
            addFireType.closePopWindow();
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        mvpPresenter.stopLocation();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        mvpPresenter.initLocation();
        super.onStart();
    }

    @OnClick({R.id.scan_repeater_ma, R.id.scan_er_wei_ma, R.id.location_image, R.id.add_fire_zjq, R.id.add_fire_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_repeater_ma:
                scanType = 0;
                Intent scanRepeater = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(scanRepeater, 0);
                break;
            case R.id.scan_er_wei_ma:
                scanType = 1;
                Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.location_image:
                mvpPresenter.startLocation();
                Intent intent=new Intent(mContext, GetLocationActivity.class);
                startActivityForResult(intent,1);//@@6.20
                break;
            case R.id.add_fire_zjq:
                if (addFireZjq.ifShow()) {
                    addFireZjq.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    addFireZjq.setClickable(false);
                    addFireZjq.showLoading();
                }
                break;
            case R.id.add_fire_type:
                if (addFireType.ifShow()) {
                    addFireType.closePopWindow();
                } else {
                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
                    addFireType.setClickable(false);
                    addFireType.showLoading();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getLocationData(BDLocation location) {
        addFireLon.setText(location.getLongitude() + "");
        addFireAddress.setText(location.getAddrStr());
        addFireLat.setText(location.getLatitude() + "");
    }

    @Override
    public void showLoading() {
//        mProgressBar.setVisibility(View.VISIBLE);
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }

    @Override
    public void hideLoading() {
//        mProgressBar.setVisibility(View.GONE);
        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext, msg);
    }

    @Override
    public void getDataSuccess(Smoke smoke) {
        tip_line.setVisibility(View.VISIBLE);
        addFireLon.setText(smoke.getLongitude() + "");
        addFireLat.setText(smoke.getLatitude() + "");
        addFireAddress.setText(smoke.getAddress());
        addFireName.setText(smoke.getName());
        addFireMan.setText(smoke.getPrincipal1());
        addFireManPhone.setText(smoke.getPrincipal1Phone());
        addFireManTwo.setText(smoke.getPrincipal2());
        addFireManPhoneTwo.setText(smoke.getPrincipal2Phone());
        addFireZjq.setEditTextData(smoke.getAreaName());
        addFireType.setEditTextData(smoke.getPlaceType());//@@10.18
        areaId=smoke.getAreaId()+"";
        shopTypeId=smoke.getPlaceTypeId();//@@10.18
        Camera mCamera = smoke.getCamera();
        if (mCamera != null) {
            addCameraName.setText(mCamera.getCameraId());
        }
        addRepeaterMac.setText(smoke.getRepeater().trim());
    }

    @Override
    public void getShopType(ArrayList<Object> shopTypes) {
        addFireType.setItemsData(shopTypes,mvpPresenter);
        addFireType.showPopWindow();
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getShopTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        addFireZjq.setItemsData(shopTypes,mvpPresenter);
        addFireZjq.showPopWindow();
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void addSmokeResult(String msg, int errorCode) {
//        T.showShort(mContext, msg);
        if (errorCode == 0) {
            cleanAllView();
            Message message = new Message();
            message.what = 4;
            handler.sendMessage(message);
        }else{
            imageFilePath=null;
            Message message = new Message();
            message.what = 5;
            message.obj=msg;
            handler.sendMessage(message);
        }
        tip_line.setVisibility(View.GONE);
    }

    private void cleanAllView() {
        mShopType = null;
        mArea = null;
        clearText();
        areaId = "";
        shopTypeId = "";
        camera = "";
        addFireMac.setText("");
        addFireZjq.addFinish();
        addFireType.addFinish();
        tip_line.setVisibility(View.GONE);
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
    }

    @Override
    public void getChoiceShop(ShopType shopType) {
        mShopType = shopType;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == getActivity().RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    if (scanType == 0) {
                        addRepeaterMac.setText(scanResult);
                    } else {
                        if(scanResult.contains("-")){
                            scanResult=scanResult.substring(scanResult.lastIndexOf("=")+1);
                        }//@@12.26三江nb-iot烟感
                        addFireMac.setText(scanResult);
                        clearText();
                        mvpPresenter.getOneSmoke(userID, privilege + "", scanResult);//@@5.5如果添加过该烟感则显示出原来的信息
                    }
                }
                break;
            case 1://@@6.20
                if (resultCode == getActivity().RESULT_OK) {
                    Bundle bundle=data.getBundleExtra("data");
                    try{
                        addFireLat.setText(String.format("%.8f",bundle.getDouble("lat")));
                        addFireLon.setText(String.format("%.8f",bundle.getDouble("lon")));
                        addFireAddress.setText(bundle.getString("address"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case 102:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bmp = BitmapFactory.decodeFile(imageFilePath);
                    try {
                        saveFile(compressBySize(Environment.getExternalStorageDirectory().getAbsolutePath()+"/devimage.jpg",1500,2000),Environment.getExternalStorageDirectory().getAbsolutePath()+"/devimage.jpg");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DisplayMetrics dm = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int screenWidth=dm.widthPixels;
                    if(bmp.getWidth()<=screenWidth){
                        photo_image.setImageBitmap(bmp);
                    }else{
                        Bitmap mp=Bitmap.createScaledBitmap(bmp, screenWidth, bmp.getHeight()*screenWidth/bmp.getWidth(), true);
                        photo_image.setImageBitmap(mp);
                    }
//                    photo_image.setImageBitmap(bmp);
                }
                break;
            case 103:
                Bitmap bm = null;
                // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                ContentResolver resolver = getActivity().getContentResolver();

                try {
                    Uri originalUri = data.getData(); // 获得图片的uri

                    bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片

                    // 这里开始的第二部分，获取图片的路径：

                    String[] proj = {MediaStore.Images.Media.DATA};

                    // 好像是android多媒体数据库的封装接口，具体的看Android文档
                    @SuppressWarnings("deprecation")
                    Cursor cursor = getActivity().managedQuery(originalUri, proj, null, null, null);
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

    }

    /**
     * 清空其他编辑框内容。。
     */
    private void clearText() {
        addFireLon.setText("");
        addFireLat.setText("");
        addFireAddress.setText("");
        addFireName.setText("");
        addFireMan.setText("");
        addFireManPhone.setText("");
        addFireManTwo.setText("");
        addFireManPhoneTwo.setText("");
        addFireZjq.setEditTextData("");
        addFireType.setEditTextData("");
        addCameraName.setText("");
        photo_image.setImageResource(R.drawable.add_photo);
        imageFilePath=null;
    }

    public static boolean uploadFile(File imageFile,String userId,String areaId,String uploadtime) {
        try {
            String requestUrl = ConstantValues.SERVER_IP_NEW+"UploadFileAction";
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

    public static boolean uploadFile(File imageFile,String userId,String areaId,String uploadtime,String mac,String location) {
        try {
            String requestUrl = ConstantValues.SERVER_IP_NEW+"UploadFileAction";
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userId);
            params.put("areaId", areaId);
            params.put("time", uploadtime);
            params.put("mac", mac);
            params.put("location", location);
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

    //@@10.12存储文件到sd卡
    public void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);//检测图片是否存在
        if(dirFile.exists()){
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));//100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
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

    public String getdevmac(String smokeMac){
        String deviceType="";
        String macStr = (String) smokeMac.subSequence(0, 1);
        if(smokeMac.length()==15){
//            deviceType="14";//GPS
            deviceType="41";//海曼NB
        }else if (smokeMac.length()==6){
            deviceType="70";//恒星水压
        }else if (smokeMac.length()==7){
            if (macStr.equals("W")){//@@9.29 区分NB
                deviceType="69";//@@恒星水位
            }
            smokeMac = smokeMac.substring(1, smokeMac.length());
        }else if (smokeMac.length()==12){
            deviceType="51";//创安
        }else if(smokeMac.length()==16||smokeMac.length()==18){
            switch(macStr){
                case "A":
                    smokeMac = smokeMac.substring(1, smokeMac.length());//三江无线传输设备
                    deviceType="119";
                    break;
                case "W":
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("W")){
                        deviceType="19";//@@水位2018.01.02
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("A")){
                        deviceType="124";//@@拓普水位2018.01.30
                        smokeMac =smokeMac.substring(0,smokeMac.length()-1);
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("B")){
                        deviceType="125";//@@拓普水压2018.01.30
                        smokeMac =smokeMac.substring(0,smokeMac.length()-1);
                    }else{
                        deviceType="10";//@@水压
                    }
                    smokeMac = smokeMac.replace("W","");//水压设备
                    break;
                case "Z":
                    smokeMac = smokeMac.substring(1, smokeMac.length());//嘉德烟感
                    deviceType="55";
                    break;
                default:
                    deviceType="21";//loraOne烟感
                    break;
            }
        }else{
            switch (macStr){
                case "R":
                    if ((smokeMac.charAt(smokeMac.length()-1)+"").equals("R")){//@@9.29 区分NB
                        deviceType="16";//@@NB燃气
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("N")){
                        deviceType="22";
                    }else{
                        deviceType="2";//@@燃气
                    }
                    smokeMac = smokeMac.replace("R","");//燃气
                    smokeMac = smokeMac.replace("N","");//燃气
                    break;
                case "Q":
                    deviceType="5";
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("Q")){
                    }//@@8.26
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("S")){
                    }//@@2018.01.18 三相设备
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("L")){
                        deviceType="52";
                    }//@@2018.05.15 Lara电气设备
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("N")){
                        deviceType="53";
                    }//@@2018.05.15 Lara电气设备
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("G")){
                        deviceType="59";
                    }//@@NB北秦电气设备
                    smokeMac = smokeMac.replace("Q","");//电气火灾
                    smokeMac = smokeMac.replace("S","");//电气火灾
                    smokeMac = smokeMac.replace("L","");//电气火灾
                    smokeMac = smokeMac.replace("N","");//电气火灾
                    smokeMac = smokeMac.replace("G","");//电气火灾

                    break;
                case "T":
                    smokeMac = smokeMac.replace("T","");//温湿度设备
                    deviceType="25";
                    break;
                case "A":
                    smokeMac = smokeMac.substring(1, smokeMac.length());
                    deviceType="119";
                    break;
                case "G":
                    smokeMac = smokeMac.replace("G","");//声光报警器 6
                    deviceType="7";
                    break;
                case "K":
                    smokeMac = smokeMac.replace("K","");//@@无线输出输入模块2018.01.24
                    deviceType="20";
                    break;
                case "S":
                    smokeMac = smokeMac.replace("S","");//手动报警，显示 7
                    deviceType="8";
                    break;
                case "J":
                    smokeMac = smokeMac.replace("J","");//三江设备
                    deviceType="9";
                    break;
                case "W":
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("W")){
                        deviceType="19";//@@水位2018.01.02
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("C")){
                        deviceType="42";//@@NB水压
                        smokeMac =smokeMac.substring(0,smokeMac.length()-1);
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("L")){
                        deviceType="43";//@@Lara水压
                        smokeMac =smokeMac.substring(0,smokeMac.length()-1);
                    }else{
                        deviceType="10";//@@水压
                    }
                    smokeMac = smokeMac.replace("W","");//水压设备
                    smokeMac = smokeMac.replace("L","");//水压设备
                    break;
                case "L":
                    smokeMac = smokeMac.replace("L","");//红外设备
                    deviceType="11";
                    break;
                case "M":
                    smokeMac = smokeMac.replace("M","");//门磁设备
                    deviceType="12";
                    break;
                case "N":
                    if((smokeMac.charAt(smokeMac.length()-1)+"").equals("N")){
                        deviceType="56";//@@NB-iot烟感
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("O")){
                        deviceType="57";//@@onet烟感
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("R")){
                        deviceType="45";//@@海曼气感
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("Z")){
                        deviceType="58";//@@嘉德移动烟感
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("H")){
                        deviceType="35";//@@电弧 电信
                    }else if((smokeMac.charAt(smokeMac.length()-1)+"").equals("I")){
                        deviceType="36";//@@嘉德移动烟感
                    }else{
                        deviceType="41";
                    }
                    smokeMac = smokeMac.replace("N","");//NB烟感设备
                    smokeMac = smokeMac.replace("O","");
                    smokeMac = smokeMac.replace("R","");
                    smokeMac = smokeMac.replace("Z","");
                    smokeMac = smokeMac.replace("H","");
                    smokeMac = smokeMac.replace("I","");
                    break;
                case "H":
                    smokeMac = smokeMac.replace("H","");//空气探测器
                    deviceType="13";
                    break;
                case "Y":
                    smokeMac = smokeMac.replace("Y","");//水禁
                    deviceType="15";
                    break;
                case "P":
                    smokeMac = smokeMac.replace("P","");//10.31喷淋
                    deviceType="18";
                    break;
            }
        }
        return smokeMac;
    }


}
