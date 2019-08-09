package com.smart.cloud.fire.activity.Inspection.TaskList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.Inspection.ItemsList.ItemsListActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.InspectionTask;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.view.MyRadioGroup;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class TaskListActivity extends MvpActivity<TaskListPresenter> implements TaskListView{

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.null_data_iv)
    ImageView null_data_iv;

    @Bind(R.id.select_btn)
    ImageButton select_btn;
    @Bind(R.id.select_line)
    RelativeLayout select_line;

    @Bind(R.id.task_type_rg)
    MyRadioGroup task_type_rg;
    @Bind(R.id.task_type0)
    RadioButton task_type0;
    @Bind(R.id.task_type1)
    RadioButton task_type1;
    @Bind(R.id.task_type2)
    RadioButton task_type2;
    @Bind(R.id.task_type3)
    RadioButton task_type3;
    @Bind(R.id.task_type4)
    RadioButton task_type4;

    @Bind(R.id.task_state_rg)
    MyRadioGroup task_state_rg;
    @Bind(R.id.task_state0)
    RadioButton task_state0;
    @Bind(R.id.task_state1)
    RadioButton task_state1;
    @Bind(R.id.task_state2)
    RadioButton task_state2;

    @Bind(R.id.starttime_tv)
    TextView starttime_tv;
    @Bind(R.id.endtime_tv)
    TextView endtime_tv;

    @Bind(R.id.commit_tv)
    TextView commit_tv;


    private Context mContext;
    private TaskListPresenter mPresenter;
    private TaskListAdapter mAdapter;

    boolean isSelectLineShow=false;

    String userID;

    String state="";
    String type="";
    String startTime="";
    String endTime="";

    int mYear, mMonth, mDay;
    final int DATE_DIALOG = 1;
    int startOrEnd=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        ButterKnife.bind(this);
        mContext=this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        mPresenter.getTasks(userID,"","","","");

        initView();
    }

    private void initView() {
        starttime_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
                startOrEnd=1;
            }
        });
        endtime_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
                startOrEnd=2;
            }
        });
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getTasks(userID,"","","","");
            }
        });
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSelectLineShow){
                    select_line.setVisibility(View.VISIBLE);
                    isSelectLineShow=true;
                }else{
                    select_line.setVisibility(View.GONE);
                    isSelectLineShow=false;
                    clearData();
                }
            }
        });
        commit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task_type0.isChecked()){
                    type="0";
                }else if(task_type1.isChecked()){
                    type="1";
                }else if(task_type2.isChecked()){
                    type="2";
                }else if(task_type3.isChecked()){
                    type="3";
                }else if(task_type4.isChecked()){
                    type="4";
                }
                if(task_state0.isChecked()){
                    state="0";
                }else if(task_state1.isChecked()){
                    state="1";
                }else if(task_state2.isChecked()){
                    state="2";
                }
                if(!((startTime=starttime_tv.getText().toString()).length()>5)){
                    startTime="";
                }
                if(!((endTime=endtime_tv.getText().toString()).length()>5)){
                    endTime="";
                }
                mPresenter.getTasks(userID,type,state,startTime,endTime);
                clearData();
                select_line.setVisibility(View.GONE);
            }
        });
    }

    private void clearData() {
        state="";
        type="";
        startTime="";
        endTime="";
        task_type_rg.clearCheck();
        task_state_rg.clearCheck();

    }

    @Override
    protected TaskListPresenter createPresenter() {
        if(mPresenter==null)
            mPresenter=new TaskListPresenter(this);
        return mPresenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getTasks(userID,"","","","");
        select_line.setVisibility(View.GONE);
    }

    @Override
    public void getDataSuccess(List<InspectionTask> pointList) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
            null_data_iv.setVisibility(View.VISIBLE);
        }else{
            null_data_iv.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new TaskListAdapter(mContext, pointList, mPresenter);
        mAdapter.setOnRecyclerViewItemClickListener(new TaskListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, InspectionTask data) {
                Intent intent=new Intent(mContext, ItemsListActivity.class);
                intent.putExtra("pid",data.getPid());
                intent.putExtra("tid",data.getTid());
                startActivity(intent);
            }
        });
        recycler_view.setAdapter(mAdapter);

        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
        null_data_iv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        if(startOrEnd==1){
            starttime_tv.setText(new StringBuffer()
                    .append(mYear)
                    .append("-").append((mMonth + 1)<10?"0"+(mMonth + 1):(mMonth + 1))
                    .append("-").append(mDay<10?"0"+mDay:mDay));
        }else if(startOrEnd==2){
            endtime_tv.setText(new StringBuffer()
                    .append(mYear)
                    .append("-").append((mMonth + 1)<10?"0"+(mMonth + 1):(mMonth + 1))
                    .append("-").append(mDay<10?"0"+mDay:mDay));
        }
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

}
