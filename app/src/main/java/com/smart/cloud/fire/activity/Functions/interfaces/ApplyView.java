package com.smart.cloud.fire.activity.Functions.interfaces;




import com.smart.cloud.fire.activity.Functions.model.ApplyTable;

import java.util.List;

/**
 * 通知界面更新接口
 */
public interface ApplyView {
    void returnMineApply(List<ApplyTable> newsChannelsMine);

    void returnMoreNewsApply(List<ApplyTable> newsChannelsMore);
}
