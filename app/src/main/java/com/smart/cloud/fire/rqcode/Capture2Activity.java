package com.smart.cloud.fire.rqcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.smart.cloud.fire.activity.AddDev.AddDevActivity;
import com.smart.cloud.fire.global.DeviceType;
import com.smart.cloud.fire.utils.DeviceTypeUtils;
import com.smart.cloud.fire.utils.JsonUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import fire.cloud.smart.com.smartcloudfire.R;

public class Capture2Activity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    boolean lightStatus=false;
    private ImageButton sdsr_btn,light_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture2);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext=this;
        sdsr_btn=(ImageButton)findViewById(R.id.sdsr_btn);
        light_btn=(ImageButton)findViewById(R.id.light_btn);
        sdsr_btn.setOnClickListener(this);
        light_btn.setOnClickListener(this);

        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.camera_zxing);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            if(result.contains("-")){
                result=result.substring(result.lastIndexOf("=")+1);
            }//@@12.26三江nb-iot烟感
            String temp= JsonUtils.isJson(result);
            if(temp!=null){
                result=temp;
            }
            boolean isNeedResult=getIntent().getBooleanExtra("isNeedResult",false);
            if(isNeedResult){
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                bundle.putString("result", result);
                resultIntent.putExtras(bundle);
                Capture2Activity.this.setResult(RESULT_OK, resultIntent);
                Capture2Activity.this.finish();
            }else{
                DeviceType devType= DeviceTypeUtils.getDevType(result,"");
                Intent intent=new Intent(mContext,AddDevActivity.class);
                intent.putExtra("devType",devType.getDevTypeName());
                intent.putExtra("mac",result);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            Capture2Activity.this.setResult(RESULT_OK, resultIntent);
            Capture2Activity.this.finish();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdsr_btn:
                Intent intent = new Intent(mContext, AddDevActivity.class);
                startActivity(intent);
                break;
            case R.id.light_btn:
                if (lightStatus) { // 关闭手电筒
                    CodeUtils.isLightEnable(false);
                    lightStatus=false;
                } else { // 打开手电筒
                    CodeUtils.isLightEnable(true);
                    lightStatus=true;
                }
                break;
        }
    }

}
