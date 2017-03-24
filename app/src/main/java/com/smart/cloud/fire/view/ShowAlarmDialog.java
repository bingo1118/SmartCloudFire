package com.smart.cloud.fire.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.smart.cloud.fire.global.InitBaiduNavi;
import com.smart.cloud.fire.mvp.fragment.MapFragment.MapFragmentPresenter;
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
public class ShowAlarmDialog {
    @Bind(R.id.do_alarm_image)
    com.smart.cloud.fire.view.MyImageView doAlarmImage;
    @Bind(R.id.close_alarm_dialog)
    ImageView closeAlarmDialog;
    @Bind(R.id.do_alarm_shop_name)
    TextView doAlarmShopName;
    @Bind(R.id.do_alarm_tv)
    TextView doAlarmTv;
    @Bind(R.id.do_smoke_mark_principal1)
    TextView doSmokeMarkPrincipal1;
    @Bind(R.id.do_smoke_mark_phone_image)
    ImageView doSmokeMarkPhoneImage;
    @Bind(R.id.do_smoke_mark_phone_tv)
    TextView doSmokeMarkPhoneTv;
    @Bind(R.id.do_phone_lin_one)
    LinearLayout doPhoneLinOne;
    @Bind(R.id.smoke_mark_principal2)
    TextView smokeMarkPrincipal2;
    @Bind(R.id.smoke_mark_phone_image)
    ImageView smokeMarkPhoneImage;
    @Bind(R.id.smoke_mark_phone_tv)
    TextView smokeMarkPhoneTv;
    @Bind(R.id.phone_lin_one)
    LinearLayout phoneLinOne;
    @Bind(R.id.do_it_btn)
    Button doItBtn;
    @Bind(R.id.lead_to_btn)
    Button leadToBtn;
    private Activity context;
    private Smoke smoke;
    private AlertDialog dialog;
    private MapFragmentPresenter mMapFragmentPresenter;
    private View mView;
    private String userId;

    public ShowAlarmDialog(Activity context, View mView, Smoke smoke, MapFragmentPresenter mMapFragmentPresenter,String userId) {
        this.context = context;
        this.smoke = smoke;
        this.mView = mView;
        this.userId = userId;
        this.mMapFragmentPresenter = mMapFragmentPresenter;
        ButterKnife.bind(this, mView);
        showAlarmDialog();
    }

    private void showAlarmDialog() {
        RxView.clicks(leadToBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Reference<Activity> reference = new WeakReference(context);
                new InitBaiduNavi(reference.get(),smoke);
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                    ButterKnife.unbind(mView);
                }
            }
        });
        String alarmInfo = smoke.getAddress();
        doAlarmTv.setText(alarmInfo);
        doAlarmShopName.setText(smoke.getName());
        doSmokeMarkPrincipal1.setText(smoke.getPrincipal1());
        smokeMarkPrincipal2.setText(smoke.getPrincipal2());
        doSmokeMarkPhoneTv.setText(smoke.getPrincipal1Phone());
        smokeMarkPhoneTv.setText(smoke.getPrincipal2Phone());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        if(!dialog.isShowing()){
            dialog.show();
        }
        dialog.setContentView(mView);
        alarmInit(doAlarmImage);
    }

    @OnClick({R.id.phone_lin_one, R.id.do_phone_lin_one,R.id.do_it_btn,R.id.close_alarm_dialog})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.phone_lin_one:
                String phoneNum = smoke.getPrincipal2Phone();
                telPhone(phoneNum);
                break;
            case R.id.do_phone_lin_one:
                String phoneNum2 = smoke.getPrincipal1Phone();
                telPhone(phoneNum2);
                break;
            case R.id.do_it_btn:
                mMapFragmentPresenter.dealAlarm(userId,smoke.getMac(),"3");
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                    ButterKnife.unbind(mView);
                }
                break;
            case R.id.close_alarm_dialog:
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                    ButterKnife.unbind(mView);
                }
                break;
            default:
                break;
        }
    }

    private void alarmInit(ImageView image) {
        final AnimationDrawable anim = (AnimationDrawable) image
                .getBackground();
        ViewTreeObserver.OnPreDrawListener opdl = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                anim.start();
                return true;
            }

        };
        image.getViewTreeObserver().addOnPreDrawListener(opdl);
    }

    private void telPhone(String phoneNum){
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
