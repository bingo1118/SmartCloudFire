package com.smart.cloud.fire.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.activity.NFCDev.NFCRecordBean;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ShowSmokeDialog {
    Activity context;
    @Bind(R.id.user_smoke_dialog_tv2)
    TextView userSmokeDialogTv2;
    @Bind(R.id.user_smoke_dialog_tv3)
    TextView userSmokeDialogTv3;
    @Bind(R.id.user_smoke_mark_principal)
    TextView userSmokeMarkPrincipal;
    @Bind(R.id.user_smoke_mark_phone_tv)
    TextView userSmokeMarkPhoneTv;
    @Bind(R.id.user_smoke_mark_principal2)
    TextView userSmokeMarkPrincipal2;
    @Bind(R.id.user_smoke_mark_phone_tv2)
    TextView userSmokeMarkPhoneTv2;
    @Bind(R.id.normal_lead_btn)
    Button normalLeadBtn;
    @Bind(R.id.user_smoke_dialog_tv4)
    TextView user_smoke_dialog_tv4;//@@8.7设备id
    @Bind(R.id.contact1_lin)
    LinearLayout contact1_lin;//@@8.18
    @Bind(R.id.contact2_lin)
    LinearLayout contact2_lin;//@@8.18
    private Smoke smoke;
    private AlertDialog dialog;
    private View mView;
    private NFCRecordBean nfcsmoke;

    public ShowSmokeDialog(Activity context, View mView, Smoke smoke) {
        this.context = context;
        this.smoke = smoke;
        this.mView = mView;
        ButterKnife.bind(this, mView);
        showSmokeDialog(mView);
    }

    //@@8.18
    public ShowSmokeDialog(Activity context, View mView, NFCRecordBean smoke) {
        this.context = context;
        this.nfcsmoke = smoke;
        this.mView = mView;
        ButterKnife.bind(this, mView);
        showNFCDialog(mView);
    }

    //@@8.18
    private void showNFCDialog(final View mView) {
        RxView.clicks(normalLeadBtn).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Reference<Activity> reference = new WeakReference(context);
                        Smoke smoketemp=new Smoke();
                        smoketemp.setLongitude(nfcsmoke.getLongitude());
                        smoketemp.setLatitude(nfcsmoke.getLatitude());
                        new InitBaiduNavi(reference.get(), smoketemp);
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                            ButterKnife.unbind(mView);
                        }
                    }
                });
        contact1_lin.setVisibility(View.GONE);
        contact2_lin.setVisibility(View.GONE);
        userSmokeMarkPhoneTv.setVisibility(View.GONE);
        userSmokeMarkPhoneTv2.setVisibility(View.GONE);
        userSmokeMarkPrincipal2.setVisibility(View.GONE);
        userSmokeMarkPrincipal.setVisibility(View.GONE);
        userSmokeDialogTv2.setText(nfcsmoke.getDeviceName());
        userSmokeDialogTv3.setText(nfcsmoke.getAddress());
        user_smoke_dialog_tv4.setText("ID:"+nfcsmoke.getUid());//@@8.7
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        if(!dialog.isShowing()){
            dialog.show();
        }
        dialog.setContentView(mView);
    }

    public void showSmokeDialog(final View view) {
        RxView.clicks(normalLeadBtn).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Reference<Activity> reference = new WeakReference(context);
                        new InitBaiduNavi(reference.get(), smoke);
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                            ButterKnife.unbind(view);
                        }
                    }
                });
        userSmokeMarkPhoneTv.setText(smoke.getPrincipal1Phone());
        userSmokeMarkPhoneTv2.setText(smoke.getPrincipal2Phone());
        userSmokeMarkPrincipal2.setText(smoke.getPrincipal2());
        userSmokeMarkPrincipal.setText(smoke.getPrincipal1());
        userSmokeDialogTv2.setText(smoke.getName());
        userSmokeDialogTv3.setText(smoke.getAddress());
        user_smoke_dialog_tv4.setText("ID:"+smoke.getMac());//@@8.7
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        if(!dialog.isShowing()){
            dialog.show();
        }
        dialog.setContentView(view);
    }


    @OnClick({R.id.user_phone_lin_one, R.id.user_phone_lin_two})
    public void onClick(View view){
        String phoneNum = null;
        switch (view.getId()){
            case R.id.user_phone_lin_one:
                phoneNum = smoke.getPrincipal1Phone();
                break;
            case R.id.user_phone_lin_two:
                phoneNum = smoke.getPrincipal2Phone();
                break;
            default:
                break;
        }
        if(Utils.isPhoneNumber(phoneNum)){
            if(dialog!=null){
                dialog.dismiss();
                dialog=null;
                ButterKnife.unbind(mView);
            }
            NormalDialog mNormalDialog = new NormalDialog(context, "是否需要拨打电话：", phoneNum,
                    "是", "否");
            mNormalDialog.showNormalDialog();
        }else{
            T.showShort(context, "电话号码不合法");
        }
    }
}
