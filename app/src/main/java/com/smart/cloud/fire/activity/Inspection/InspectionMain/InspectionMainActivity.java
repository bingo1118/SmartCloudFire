package com.smart.cloud.fire.activity.Inspection.InspectionMain;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNFCItemActivity;
import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNormalItemActivity;
import com.smart.cloud.fire.activity.Inspection.InspectionMap.InspectionMapActivity;
import com.smart.cloud.fire.activity.Inspection.PointList.PointListActivity;
import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadInspectionInfoActivity;
import com.smart.cloud.fire.activity.Inspection.UploadInspectionInfo.UploadProblemActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class InspectionMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_main);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.insp_add_normal_ib,R.id.insp_add_nfc_ib,R.id.insp_task_ib
            ,R.id.insp_points_ib,R.id.insp_map_ib,R.id.insp_upload_problem_ib
            ,R.id.insp_quickly_ib})
    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.insp_add_normal_ib:
                intent=new Intent(this, AddInspectionNormalItemActivity.class);
                break;
            case R.id.insp_add_nfc_ib:
                intent=new Intent(this, AddInspectionNFCItemActivity.class);
                break;
            case R.id.insp_task_ib:
                intent=new Intent(this, TaskListActivity.class);
                break;
            case R.id.insp_points_ib:
                intent=new Intent(this, PointListActivity.class);
                break;
            case R.id.insp_map_ib:
                intent=new Intent(this, InspectionMapActivity.class);
                break;
            case R.id.insp_upload_problem_ib:
                intent=new Intent(this, UploadProblemActivity.class);
                break;
            case R.id.insp_quickly_ib:
                intent=new Intent(this, UploadInspectionInfoActivity.class);
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }
}
