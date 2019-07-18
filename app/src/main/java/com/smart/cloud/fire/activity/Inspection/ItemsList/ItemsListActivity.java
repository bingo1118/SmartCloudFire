package com.smart.cloud.fire.activity.Inspection.ItemsList;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.activity.Inspection.PointList.PointListAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.NFCInfoEntity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ItemsListActivity extends MvpActivity<ItemsListPresenter> implements ItemsListView {

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.sum_line)
    LinearLayout sum_line;
    @Bind(R.id.sum_tv)
    TextView sum_tv;
    @Bind(R.id.pass_tv)
    TextView pass_tv;
    @Bind(R.id.progress_tv)
    TextView progress_tv;
    @Bind(R.id.change)
    TextView change;
    @Bind(R.id.change_iv)
    ImageView change_iv;


    private ItemsListPresenter mPresenter;
    private ItemsListAdapter mAdapter;
    Context mContext;

    String pid;
    String tid;
    String userID;

    String state="0";//巡检状态  0/1 未检/已检


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        ButterKnife.bind(this);
        mContext=this;
        pid=getIntent().getStringExtra("pid");
        tid=getIntent().getStringExtra("tid");
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        if(tid!=null&&tid.length()>0){
            mPresenter.getTaskItems(tid,state);
            change.setVisibility(View.VISIBLE);
            change_iv.setVisibility(View.VISIBLE);
        }else if(pid!=null&&pid.length()>0){
//            mPresenter.getPointItems(pid);
            mPresenter.getAllItems("",pid);
        }else{
            mPresenter.getAllItems(userID,"");
        }
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(tid!=null&&tid.length()>0){
                    mPresenter.getTaskItems(tid,state);
                }else if(pid!=null&&pid.length()>0){
//            mPresenter.getPointItems(pid);
                    mPresenter.getAllItems("",pid);
                }else{
                    mPresenter.getAllItems(userID,"");
                }
        }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("0")){
                    state="1";
                    change.setText("未巡检项目");
                }else{
                    state="0";
                    change.setText("已巡检项目");
                }
                mPresenter.getTaskItems(tid,state);
            }
        });
    }

    @Override
    protected ItemsListPresenter createPresenter() {
        mPresenter=new ItemsListPresenter(this);
        return mPresenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(tid!=null&&tid.length()>0){
            mPresenter.getTaskItems(tid,state);
        }else if(pid!=null&&pid.length()>0){
//            mPresenter.getPointItems(pid);
            mPresenter.getAllItems("",pid);
        }else{
            mPresenter.getAllItems(userID,"");
        }
    }

    @Override
    public void getDataSuccess(List<NFCInfoEntity> pointList) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemsListAdapter(mContext, pointList, mPresenter,tid);
        recycler_view.setAdapter(mAdapter);

        swipereFreshLayout.setRefreshing(false);

        sum_line.setVisibility(View.GONE);
    }

    @Override
    public void getDataSuccess(List<NFCInfoEntity> pointList, int sum, int pass, int checked) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        mAdapter = new ItemsListAdapter(mContext, pointList, mPresenter,tid);
        recycler_view.setAdapter(mAdapter);

        swipereFreshLayout.setRefreshing(false);

        sum_line.setVisibility(View.VISIBLE);
        sum_tv.setText("总数:"+sum);
        progress_tv.setText("已检:"+checked);
        pass_tv.setText("合格:"+pass);


    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
    }
}
