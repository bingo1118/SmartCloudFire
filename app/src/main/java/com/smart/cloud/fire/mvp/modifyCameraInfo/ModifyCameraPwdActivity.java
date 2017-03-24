package com.smart.cloud.fire.mvp.modifyCameraInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.Contact;
import com.smart.cloud.fire.ui.ApMonitorActivity;
import com.smart.cloud.fire.utils.T;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class ModifyCameraPwdActivity extends MvpActivity<ModifyCameraPwdPresenter> implements ModifyCameraPwdView {

    @Bind(R.id.camera_ex_pwd)
    EditText cameraExPwd;
    @Bind(R.id.camera_pwd_now)
    EditText cameraPwdNow;
    @Bind(R.id.camera_pwd_now2)
    EditText cameraPwdNow2;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private Context mContext;
    private Contact contact;
    private String newPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_camera_pwd);
        ButterKnife.bind(this);
        mContext = this;
        contact = (Contact) getIntent().getExtras().getSerializable("contact");
        regFilter();
    }

    @Override
    protected ModifyCameraPwdPresenter createPresenter() {
        return new ModifyCameraPwdPresenter(this);
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.P2P.ACK_RET_SET_DEVICE_PASSWORD);
        filter.addAction(ConstantValues.P2P.RET_SET_DEVICE_PASSWORD);
        filter.addAction(ConstantValues.P2P.RET_DEVICE_NOT_SUPPORT);
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(ConstantValues.P2P.RET_SET_DEVICE_PASSWORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.DEVICE_PASSWORD_SET.SETTING_SUCCESS) {
                    mvpPresenter.modifyCameraPwdToServer(contact.contactId,newPwd);
                } else {
                    hideLoading();
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(ConstantValues.P2P.ACK_RET_SET_DEVICE_PASSWORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    hideLoading();
                    T.showShort(mContext, "旧密码错误");
                } else if (result == ConstantValues.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    hideLoading();
                    T.showShort(mContext,"网络异常，操作失败");
                }
            } else if (intent.getAction().equals(ConstantValues.P2P.RET_DEVICE_NOT_SUPPORT)) {
                hideLoading();
                finish();
                T.showShort(mContext, R.string.not_support);
            }
        }
    };

    @OnClick(R.id.modify_camera_pwd)
    public void onClick(){
        String oldPwd = cameraExPwd.getText().toString().trim();
        newPwd = cameraPwdNow.getText().toString().trim();
        String newPwd2 = cameraPwdNow2.getText().toString().trim();
        mvpPresenter.ModifyCameraPwd(contact.contactId,oldPwd,newPwd,newPwd2);
    }


    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void modifyCameraPwdResult(String msg,String pwd) {
        T.showShort(mContext,msg);
        contact.contactPassword = pwd;
        Intent in = new Intent(mContext, ApMonitorActivity.class);
        in.putExtra("contact",contact);
        in.putExtra("ifRefreshPwd",true);
        startActivity(in);
        finish();
    }

    @Override
    public void errorMessage(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(mContext,ApMonitorActivity.class);
            i.putExtra("contact",contact);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
