package com.smart.cloud.fire.activity.AddNFC;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class AddNFCMacActivity extends Activity {

    @Bind(R.id.uid_edit)
    EditText uid_edit;//探测器。。
    @Bind(R.id.done_text)
    TextView done_text;
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout add_fire_dev_btn;
    private String UID;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    Context mContext;
    private int privilege;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nfcmac);

        ButterKnife.bind(this);
        mContext=this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();

        initView();
    }

    private void initView() {
        add_fire_dev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyHelper helper=VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
//                RequestQueue mQueue = Volley.newRequestQueue(mContext);
                String url= ConstantValues.SERVER_IP_NEW+"addNFC?uid="+UID+"&userId="+userID;
                StringRequest stringRequest = new StringRequest(url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    int errorCode=jsonObject.getInt("errorCode");
                                    if(errorCode==0){
                                        T.showShort(mContext,"添加成功");
                                        uid_edit.setText("");
                                    }else{
                                        T.showShort(mContext,"添加失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                mQueue.add(stringRequest);

            }
        });
    }



    /**
     * 启动Activity，界面可见时
     */
    @Override
    protected void onStart() {
        super.onStart();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
        //设置处理优于所有其他NFC的处理
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            byte[] myNFCID = getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID);
            UID = Utils.ByteArrayToHexString(myNFCID);
            uid_edit.setText(UID);
            VolleyHelper helper=VolleyHelper.getInstance(mContext);
            RequestQueue mQueue = helper.getRequestQueue();
//            RequestQueue mQueue = Volley.newRequestQueue(mContext);
            String url= ConstantValues.SERVER_IP_NEW+"ifNFCExist?uid="+UID;
            StringRequest stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("0")){
                                done_text.setVisibility(View.VISIBLE);
                                add_fire_dev_btn.setEnabled(false);
                            }else{
                                done_text.setVisibility(View.GONE);
                                add_fire_dev_btn.setEnabled(true);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            mQueue.add(stringRequest);
            setIntent(new Intent()); // Consume this intent.
        }
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    /**
     * 暂停Activity，界面获取焦点，按钮可以点击
     */
    @Override
    public void onPause() {
        super.onPause();
        //恢复默认状态
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //2.获取Ndef的实例
        Ndef ndef = Ndef.get(detectedTag);

        byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        UID = Utils.ByteArrayToHexString(myNFCID);
        uid_edit.setText(UID);

        VolleyHelper helper=VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
//        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        String url= ConstantValues.SERVER_IP_NEW+"ifNFCExist?uid="+UID;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")){
                            done_text.setVisibility(View.VISIBLE);
                            add_fire_dev_btn.setEnabled(false);
                        }else{
                            done_text.setVisibility(View.GONE);
                            add_fire_dev_btn.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);



//        readNfcTag(intent);
//        uid_edit.setText("");
    }

    /**
     * 往标签写数据的方法
     *
     * @param tag
     */
    public void writeNFCTag(Tag tag) {
        if (tag == null) {
            return;
        }
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{NdefRecord
                .createApplicationRecord("abcdefg")});
        //转换成字节获得大小
        int size = ndefMessage.toByteArray().length;
        try {
            //2.判断NFC标签的数据类型（通过Ndef.get方法）
            Ndef ndef = Ndef.get(tag);
            //判断是否为NDEF标签
            if (ndef != null) {
                ndef.connect();
                //判断是否支持可写
                if (!ndef.isWritable()) {
                    return;
                }
                //判断标签的容量是否够用
                if (ndef.getMaxSize() < size) {
                    return;
                }
                //3.写入数据
                ndef.writeNdefMessage(ndefMessage);
                Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
            } else { //当我们买回来的NFC标签是没有格式化的，或者没有分区的执行此步
                //Ndef格式类
                NdefFormatable format = NdefFormatable.get(tag);
                //判断是否获得了NdefFormatable对象，有一些标签是只读的或者不允许格式化的
                if (format != null) {
                    //连接
                    format.connect();
                    //格式化并将信息写入标签
                    format.format(ndefMessage);
                    Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
        }
    }


    /**
     * 读取NFC标签文本数据
     */
    private void readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
//                    String textRecord = parseTextRecord(record);
//                    mTagText += textRecord + "\n\ntext\n" + contentSize + " bytes";
                }
            } catch (Exception e) {
            }
        }
    }

}

