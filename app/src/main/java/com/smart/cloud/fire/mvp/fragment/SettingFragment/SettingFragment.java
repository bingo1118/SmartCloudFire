package com.smart.cloud.fire.mvp.fragment.SettingFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smart.cloud.fire.activity.AddNFC.AddNFCMacActivity;
import com.smart.cloud.fire.activity.ChuangAnWifiSet.ChuanganWifiStep1Activity;
import com.smart.cloud.fire.activity.UploadNFCInfo.UploadNFCInfoActivity;
import com.smart.cloud.fire.activity.WebServer.WebServerActivity;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.camera.AddCameraFirstActivity;
import com.smart.cloud.fire.ui.AboutActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/9/21.
 */
public class SettingFragment extends MvpFragment<SettingFragmentPresenter> implements SettingFragmentView {
    @Bind(R.id.setting_user_id)
    TextView settingUserId;
    @Bind(R.id.setting_user_code)
    TextView settingUserCode;
    @Bind(R.id.setting_help_rela)
    RelativeLayout settingHelpRela;
    @Bind(R.id.setting_webserver)
    RelativeLayout setting_webserver;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.setting_camera_relative)
    RelativeLayout settingCameraRelative;
    @Bind(R.id.nfc_mac_add)
    RelativeLayout nfc_mac_add;//@@11.17
    @Bind(R.id.line_state)
    TextView lineState;
    @Bind(R.id.nfc_radiogroup)
    RadioGroup nfc_radiogroup;
    @Bind(R.id.everymonth)
    RadioButton everymonth;
    @Bind(R.id.everyweek)
    RadioButton everyweek;
    @Bind(R.id.everyday)
    RadioButton everyday;
    @Bind(R.id.setting_help_wifi)
    RelativeLayout setting_help_wifi;//@@2018.05.14 WiFi设置
    private Context mContext;

    @Bind(R.id.setting_pay)
    RelativeLayout setting_pay;//@@2018.03.14

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container,
                false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        init();
    }

    private void init() {
        String userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
        String username = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        int peroid=SharedPreferencesManager.getInstance().getIntData(mContext,"NFC_period");//@@10.24
        switch (peroid){
            case 0:
                everymonth.setChecked(true);
                break;
            case 1:
                everyweek.setChecked(true);
                break;
            case 2:
                everyday.setChecked(true);
                break;
            default:
                everymonth.setChecked(true);
                break;
        }//@@10.24
        settingUserId.setText(userID);
        settingUserCode.setText(username);
        String state = MyApp.app.getPushState();
        if(state!=null&&state.length()>0){
            if(state.equals("Online")){
                lineState.setText("在线");
            }
            if(state.equals("Offline")){
                lineState.setText("离线");
            }
        }
        int privilege = MyApp.app.getPrivilege();
        if (privilege == 3) {
            settingHelpRela.setVisibility(View.VISIBLE);//显示添加摄像机。。
            settingCameraRelative.setVisibility(View.VISIBLE);//显示绑定摄像机。。
        }
        if (privilege == 4) {
            setting_webserver.setVisibility(View.VISIBLE);
            nfc_mac_add .setVisibility(View.VISIBLE);
        }
        nfc_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int period=0;
                switch (checkedId){
                    case R.id.everymonth:
                        period=0;
                        break;
                    case R.id.everyweek:
                        period=1;
                        break;
                    case R.id.everyday:
                        period=2;
                        break;
                    default:
                        period=0;
                        break;
                }
                SharedPreferencesManager.getInstance().putData(mContext,"NFC_period",period);
                T.showShort(mContext,"设置成功");
            }
        });
    }

    @OnClick({R.id.app_update, R.id.setting_help_about, R.id.setting_help_rela, R.id.setting_help_exit,
            R.id.setting_camera_relative,R.id.setting_nfc,R.id.nfc_mac_add,R.id.setting_pay,R.id.setting_help_wifi
            ,R.id.setting_image,R.id.setting_webserver})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_webserver:
                Intent intent11=new Intent(getActivity(), WebServerActivity.class);
                getActivity().startActivity(intent11);
                break;
            case R.id.app_update:
                if(ConstantValues.SERVER_IP_NEW.contains("119.29.155.148")
                        ||ConstantValues.SERVER_IP_NEW.contains("193.112.150.195")){
                    mvpPresenter.checkUpdate(mContext);
                }else{
                    T.showShort(mContext,"该版本不支持版本更新");
                }
//                getActivity().finish();//@@7.13
                break;
//            case R.id.setting_pay:
//                Intent intent9 = new Intent(mContext, PayActivity.class);
//                startActivity(intent9);
//                break;
            case R.id.setting_help_about:
                Intent intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_help_wifi:
                Intent intent_wifi = new Intent(mContext, ChuanganWifiStep1Activity.class);
                startActivity(intent_wifi);
                break;
            case R.id.setting_help_rela:
                Intent intent2 = new Intent(mContext, AddCameraFirstActivity.class);
                startActivity(intent2);
                break;
            case R.id.setting_help_exit:
                Intent in = new Intent();
                in.setAction("APP_EXIT");
                in.setPackage("fire.cloud.smart.com.smartcloudfire");//@@7.13只传当前应用
                mContext.sendBroadcast(in);
                getActivity().finish();//@@7.17
                break;
            case R.id.setting_camera_relative:
                mvpPresenter.bindDialog(mContext);
                break;
            case R.id.setting_nfc:
                Intent intent3 = new Intent(mContext, UploadNFCInfoActivity.class);
                startActivity(intent3);
                break;
            case R.id.nfc_mac_add:
                Intent intent6 = new Intent(mContext, AddNFCMacActivity.class);
                startActivity(intent6);
                break;
            default:
                break;
        }
    }

    @Override
    protected SettingFragmentPresenter createPresenter() {
        SettingFragmentPresenter mMapFragmentPresenter = new SettingFragmentPresenter(SettingFragment.this);
        return mMapFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "settingFragment";
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
    public void bindResult(String msg) {
        T.showShort(mContext, msg);
    }
}
