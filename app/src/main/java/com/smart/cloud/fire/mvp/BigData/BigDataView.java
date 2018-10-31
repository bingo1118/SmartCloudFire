package com.smart.cloud.fire.mvp.BigData;

import com.smart.cloud.fire.global.SafeScore;
import com.smart.cloud.fire.global.SmokeSummary;

/**
 * Created by Rain on 2018/9/21.
 */
public interface BigDataView {

    void getOnlineSummary(SmokeSummary model);

    void getSafeScore(SafeScore model);
}
