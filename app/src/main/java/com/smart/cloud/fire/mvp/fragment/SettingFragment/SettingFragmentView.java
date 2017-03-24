package com.smart.cloud.fire.mvp.fragment.SettingFragment;

/**
 * Created by Administrator on 2016/9/21.
 */
public interface SettingFragmentView {
    void showLoading();

    void hideLoading();

    void bindResult(String msg);
}
