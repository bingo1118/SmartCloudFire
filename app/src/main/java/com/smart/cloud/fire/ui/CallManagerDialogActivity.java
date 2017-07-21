package com.smart.cloud.fire.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;
import com.smart.cloud.fire.view.NormalDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class CallManagerDialogActivity extends Activity {

    @Bind(R.id.people1_text)
    TextView people1_text;
    @Bind(R.id.people2_text)
    TextView people2_text;
    @Bind(R.id.phone1_text)
    TextView phone1_text;
    @Bind(R.id.phone2_text)
    TextView phone2_text;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_manager_dialog);
        ButterKnife.bind(this);
        initView();
        mContext=this;
    }

    private void initView() {
        Intent intent=getIntent();
        people1_text.setText(intent.getStringExtra("people1"));
        people2_text.setText(intent.getStringExtra("people2"));
        phone1_text.setText(intent.getStringExtra("phone1"));
        phone2_text.setText(intent.getStringExtra("phone2"));
        phone1_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum=getIntent().getStringExtra("phone1");
                    if(Utils.isPhoneNumber(phoneNum)){
                        NormalDialog mNormalDialog = new NormalDialog(mContext, "是否需要拨打电话：", phoneNum,
                                "是", "否");
                        mNormalDialog.showNormalDialog();//拨号过程封装在NormalDialog中。。
                    }else{
                        T.showShort(mContext, "电话号码不合法");
                    }
                }

        });
        phone2_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum=getIntent().getStringExtra("phone2");
                if(Utils.isPhoneNumber(phoneNum)){
                    NormalDialog mNormalDialog = new NormalDialog(mContext, "是否需要拨打电话：", phoneNum,
                            "是", "否");
                    mNormalDialog.showNormalDialog();//拨号过程封装在NormalDialog中。。
                }else{
                    T.showShort(mContext, "电话号码不合法");
                }
            }

        });
    }
}
