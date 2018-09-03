package com.smart.cloud.fire.activity.AlarmMsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class DealMsgDetailActivity extends Activity {

    @Bind(R.id.mac_et)
    TextView mac_et;
    @Bind(R.id.deal_et)
    TextView deal_et;
    @Bind(R.id.alarmtype_et)
    TextView alarmtype_et;
    @Bind(R.id.stute_et)
    TextView stute_et;
    @Bind(R.id.memo_et)
    TextView memo_et;
    @Bind(R.id.image_rela)
    RelativeLayout image_rela;
    @Bind(R.id.video_rela)
    RelativeLayout video_rela;
    @Bind(R.id.photo_image)
    ImageView photo_image;
    @Bind(R.id.video_upload)
    VideoView video_upload;

    Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_msg_detail);
        ButterKnife.bind(this);
        mContext=this;
        MediaController  mediaco=new MediaController(this);
        mediaco.setVisibility(View.GONE );
        video_upload.setMediaController(mediaco);
        video_upload.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video_upload.start();
            }
        });
        initData();
    }

    private void initData() {
        Intent i=getIntent();
        VolleyHelper helper= VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url= ConstantValues.SERVER_IP_NEW+"getNeedAlarmMessage?userId=13622215085&privilege=4&id="+i.getIntExtra("id",0);
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getInt("errorCode")==0){
                                JSONObject alarm=jsonObject.getJSONArray("Alarm").getJSONObject(0);
                                initView(alarm);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error","error");
            }
        });
        mQueue.add(stringRequest);
    }

    private void initView(JSONObject alarm) {
        try {
            mac_et.setText(alarm.getString("mac"));
            alarmtype_et.setText("报警");
            if("1".equals(alarm.getString("alarmTruth"))||"4".equals(alarm.getString("alarmTruth"))){
                stute_et.setText("实报");
            }else{
                stute_et.setText("误报");
            }
            deal_et.setText(alarm.getString("dealPeople"));
            memo_et.setText(alarm.getString("dealDetail"));


            if(alarm.getString("image_path")!=null&&alarm.getString("image_path").length()>0&&!alarm.getString("image_path").equals("null")){
                image_rela.setVisibility(View.VISIBLE);
                final String temp1= ConstantValues.NFC_IMAGES+"devalarm//"+alarm.getString("image_path")+".jpg";
                image_rela.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(mContext,TestSafetyActivity.class);
                        i.putExtra("filepath",temp1);
                        mContext.startActivity(i);
                    }
                });
                Glide.with(this)
                        .load(temp1).thumbnail(0.000001f)
                        .into(photo_image);//@@9.28
            }else{
                image_rela.setVisibility(View.GONE);
            }

            if(null!=alarm.getString("video_path")&&alarm.getString("video_path").length()>0&&!alarm.getString("video_path").equals("null")){
                video_rela.setVisibility(View.VISIBLE);
                final String path = ConstantValues.NFC_IMAGES+"devalarm_video//"+ alarm.getString("video_path")+".mp4";
                video_rela.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(mContext,TestSafetyActivity.class);
                        i.putExtra("filepath",path);
                        mContext.startActivity(i);
                    }
                });


                video_upload.setVideoURI(Uri.parse(path));
                MediaController mediaController=new MediaController(this);
                mediaController.show();
                video_upload.setMediaController(new MediaController(this));
                video_upload.requestFocus();
                video_upload.start();

            }else{
                video_rela.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
