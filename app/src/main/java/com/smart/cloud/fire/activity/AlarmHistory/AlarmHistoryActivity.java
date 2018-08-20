package com.smart.cloud.fire.activity.AlarmHistory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.smart.cloud.fire.mvp.fragment.CollectFragment.CollectFragment;

import fire.cloud.smart.com.smartcloudfire.R;

public class AlarmHistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_history);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CollectFragment a=(CollectFragment)getFragmentManager().findFragmentById(R.id.fragment_content);
        a.onActivityResult(requestCode,resultCode,data);
    }

}
