package com.smart.cloud.fire.activity.UploadNFCInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.VolleyHelper;
import com.smart.cloud.fire.utils.uploadFile;

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

public class UploadNFCInfoActivity extends Activity {

    @Bind(R.id.uid_name)
    EditText uid_name;//@@uid
    @Bind(R.id.add_fire_name)
    EditText addFireName;//设备名称。。
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;//设备地址。。
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;//添加设备按钮。。
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;//加载进度。。
    @Bind(R.id.area_name)
    EditText area_name;//@@区域
    @Bind(R.id.device_type_name)
    EditText device_type_name;//@@类型
    @Bind(R.id.memo_name)
    EditText memo_name;//@@备注
    @Bind(R.id.photo_image)
    ImageView photo_image;//@@拍照上传
    private Context mContext;
    private int privilege;
    private String userID;
    private String areaId;//@@9.27
    private String uploadTime;

    private boolean mWriteMode = false;
    NfcAdapter mNfcAdapter;
    AlertDialog alertDialog;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;
    private Tag mDetectedTag;

    private String deviceState="1";
    String lon="";
    String lat="";
    private String imageFilePath;
    File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/filename.jpg");//@@9.30

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
                    toast("图片上传失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_nfcinfo);

        ButterKnife.bind(this);
        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(f.exists()){
            f.delete();
        }//@@9.30
        if (mNfcAdapter==null) {
            toast("设备不支持NFC功能");
            return;
        }
        initView();
        initNFC();
    }

    private void initView() {
        addFireDevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                if(uid_name.getText()==null||uid_name.getText().toString().equals("")){
                    toast("请先录入设备标签信息");
                    Message message1 = new Message();
                    message.what = 0;
                    handler.sendMessage(message1);
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        boolean isSuccess=false;
                        boolean isHavePhoto=false;
                        if(imageFilePath!=null){
                            File file = new File(imageFilePath); //这里的path就是那个地址的全局变量
                            uploadTime=System.currentTimeMillis()+"";
                            if(f.exists()){
                                isHavePhoto=true;
                            }//@@11.07
                            isSuccess=uploadFile(file,userID,areaId,uploadTime);
                        }
                        VolleyHelper helper=VolleyHelper.getInstance(mContext);
                        RequestQueue mQueue = helper.getRequestQueue();
//                        RequestQueue mQueue = Volley.newRequestQueue(mContext);
                        String url="";
                        if(isHavePhoto&&isSuccess){
                            File file = new File(imageFilePath);//9.29
                            file.delete();//@@9.29
                            url= ConstantValues.SERVER_IP_NEW+"addNFCRecord?userId="+userID+"&uid="+uid_name.getText().toString()
                                    +"&longitude="+lon+"&latitude="+lat+"&devicestate="+deviceState+"&memo="+ URLEncoder.encode(memo_name.getText().toString())
                                    +"&photo1="+areaId+URLEncoder.encode("\\")+uploadTime+imageFilePath.substring(imageFilePath.lastIndexOf("."));
                        }else{
                            if(isHavePhoto&&!isSuccess){
                                Message message= new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                            }
                            url= ConstantValues.SERVER_IP_NEW+"addNFCRecord?userId="+userID+"&uid="+uid_name.getText().toString()
                                    +"&longitude="+lon+"&latitude="+lat+"&devicestate="+deviceState+"&memo="+ URLEncoder.encode(memo_name.getText().toString())
                                    +"&photo1=";
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            int errorCode=response.getInt("errorCode");
                                            String error=response.getString("error");
                                            if(errorCode==0){
                                                toast("记录上传成功");
                                                clearView();
                                                if(f.exists()){
                                                    f.delete();
                                                }//@@9.30
                                            }else{
                                                toast(error);
                                            }
                                            Message message = new Message();
                                            message.what = 0;
                                            handler.sendMessage(message);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Message message = new Message();
                                            message.what = 0;
                                            handler.sendMessage(message);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                toast("网络错误");
                                Message message = new Message();
                                message.what = 0;
                                handler.sendMessage(message);
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
                imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/filename.jpg";
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

        RadioGroup group = (RadioGroup)this.findViewById(R.id.radio_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                           @Override
                         public void onCheckedChanged(RadioGroup arg0, int arg1) {
                                // TODO Auto-generated method stub
                                 int radioButtonId = arg0.getCheckedRadioButtonId();
                               switch (radioButtonId){
                                   case R.id.radio1:
                                       deviceState="1";
                                       break;
                                   case R.id.radio2:
                                       deviceState="2";
                                       break;
                               }
                             }
                    });
    }

    private void clearView() {
        uid_name.setText("");
        addFireName.setText("");
        addFireAddress.setText("");
        area_name.setText("");
        device_type_name.setText("");
        memo_name.setText("");
        photo_image.setImageResource(R.drawable.add_photo);
        imageFilePath=null;
    }

    private void initNFC() {
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
        }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap bmp = BitmapFactory.decodeFile(imageFilePath);
                    try {
                        saveFile(compressBySize(Environment.getExternalStorageDirectory().getAbsolutePath()+"/filename.jpg",150,200),Environment.getExternalStorageDirectory().getAbsolutePath()+"/filename.jpg");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    photo_image.setImageBitmap(bmp);
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
        if(dirFile.exists()){
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));//100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mResumed = true;
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));
            setIntent(new Intent()); // Consume this intent.
        }
        if (mNfcAdapter!=null) {
            enableNdefExchangeMode();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
//        mResumed = false;
        if (mNfcAdapter!=null) {
            mNfcAdapter.disableForegroundNdefPush(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if (!mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            mDetectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            //创建Ndef对象
            NdefMessage[] msgs = getNdefMessages(intent);
            String body = new String(msgs[0].getRecords()[0].getPayload());
            setNoteBody(body);
        }

        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(getNoteAsNdef(), detectedTag);
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {
//            if (mResumed) {
//                mNfcAdapter.enableForegroundNdefPush(MainActivity.this, getNoteAsNdef());
//            }
        }
    };

    private void setNoteBody(String body) {
        try {
            JSONObject jsonObject=new JSONObject(body);
            uid_name.setText(jsonObject.getString("uid"));
            addFireName.setText(jsonObject.getString("deviceName"));
            addFireAddress.setText(jsonObject.getString("address"));
            area_name.setText(jsonObject.getString("areaName"));
            device_type_name.setText(jsonObject.getString("deviceTypeName"));
            lon=jsonObject.getString("longitude");
            lat=jsonObject.getString("latitude");
            areaId=jsonObject.getString("areaId");//@@9.27

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private NdefMessage getNoteAsNdef() {
        byte[] textBytes = "".getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
                textRecord
        });
    }

    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
                        msg
                };
            }
        } else {
            finish();
        }
        return msgs;
    }

    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundNdefPush(UploadNFCInfoActivity.this, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);
    }

    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] {
                tagDetected
        };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    toast("Tag is read-only.");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    toast("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.");
                    return false;
                }

                ndef.writeNdefMessage(message);
                toast("写入数据成功.");
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        toast("Formatted tag and wrote message");
                        return true;
                    } catch (IOException e) {
                        toast("Failed to format tag.");
                        return false;
                    }
                } else {
                    toast("Tag doesn't support NDEF.");
                    return false;
                }
            }
        } catch (Exception e) {
            toast("写入数据失败");
        }

        return false;
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


    private void toast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }
}
