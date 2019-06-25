package com.smart.cloud.fire.activity.Inspection.TaskList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.Inspection.ItemsList.ItemsListActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.InspectionTask;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class TaskListActivity extends MvpActivity<TaskListPresenter> implements TaskListView{

    @Bind(R.id.more)
    ImageButton more;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    private Context mContext;
    private TaskListPresenter mPresenter;
    private TaskListAdapter mAdapter;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        ButterKnife.bind(this);
        mContext=this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        mPresenter.getTasks(userID);
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getTasks(userID);
            }
        });
    }

    @Override
    protected TaskListPresenter createPresenter() {
        if(mPresenter==null)
            mPresenter=new TaskListPresenter(this);
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<InspectionTask> pointList) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7

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
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
