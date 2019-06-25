package com.smart.cloud.fire.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListActivity;

import org.w3c.dom.Text;

import fire.cloud.smart.com.smartcloudfire.R;

public class TaskDialogActivity extends Activity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dialog);
        tv=(TextView)findViewById(R.id.commit) ;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(TaskDialogActivity.this, TaskListActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
