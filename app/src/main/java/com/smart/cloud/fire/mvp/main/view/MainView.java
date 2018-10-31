package com.smart.cloud.fire.mvp.main.view;

import com.smart.cloud.fire.global.SafeScore;
import com.smart.cloud.fire.global.SmokeSummary;

/**
 * Created by Administrator on 2016/9/21.
 */
public interface MainView {
    void exitBy2Click(boolean isExit);
    void getOnlineSummary(SmokeSummary model);
    void getSafeScore(SafeScore model);
}
