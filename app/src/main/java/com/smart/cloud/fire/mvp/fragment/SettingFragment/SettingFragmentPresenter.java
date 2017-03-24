package com.smart.cloud.fire.mvp.fragment.SettingFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.MainThread;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/21.
 */
public class SettingFragmentPresenter extends BasePresenter<SettingFragmentView> {
    AlertDialog dialog;
    @Bind(R.id.bind_smoke_id)
    EditText bindSmokeId;
    @Bind(R.id.bind_camera_id)
    EditText bindCameraId;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;

    public SettingFragmentPresenter(SettingFragmentView view) {
        attachView(view);
    }

    public void checkUpdate(final Context mContext) {
        mvpView.showLoading();
        new MyTast().execute(mContext);
    }

    class MyTast extends AsyncTask<Context, Integer, Integer> {

        @Override
        protected Integer doInBackground(Context... params) {
            // TODO Auto-generated method stub\
            Context context = params[0];
            long ll = -1;
            int result = new MainThread(context).checkUpdate(ll);
            return result;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            mvpView.hideLoading();
        }
    }

    public void bindDialog(Context mContext) {
        final View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_bind, null);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        dialog = builder.create();
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @OnClick({R.id.button2_text, R.id.button1_text})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.button2_text:
                ButterKnife.unbind(view);
                dialog.dismiss();
                break;
            case R.id.button1_text:
                String cameraId = bindCameraId.getText().toString().trim();
                String smokeId = bindSmokeId.getText().toString().trim();
                if (cameraId.length() == 0 || bindSmokeId.length() == 0) {
                    mvpView.bindResult("摄像头ID和烟感ID不能为空");
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    Observable mObservable = apiStores1.bindCameraSmoke(cameraId, smokeId);
                    addSubscription(mObservable, new SubscriberCallBack<>(new ApiCallback<HttpError>() {
                        @Override
                        public void onSuccess(HttpError model) {
                            int resultCode = model.getErrorCode();
                            if(resultCode==0){
                                mvpView.bindResult("绑定成功");
                                ButterKnife.unbind(view);
                                dialog.dismiss();
                            }else{
                                mvpView.bindResult("绑定失败，请重新绑定");
                            }
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            mvpView.bindResult("绑定失败");
                        }

                        @Override
                        public void onCompleted() {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }));
                }
                break;
            default:
                break;
        }
    }
}
