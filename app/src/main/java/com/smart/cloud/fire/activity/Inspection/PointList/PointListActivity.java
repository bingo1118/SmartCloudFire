package com.smart.cloud.fire.activity.Inspection.PointList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNFCItemActivity;
import com.smart.cloud.fire.activity.Inspection.AddInspectionItem.AddInspectionNormalItemActivity;
import com.smart.cloud.fire.activity.Inspection.ItemsList.ItemsListActivity;
import com.smart.cloud.fire.activity.Inspection.TaskList.TaskListActivity;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Point;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class PointListActivity extends MvpActivity<PointListPresenter> implements PointListView {

    @Bind(R.id.more)
    ImageButton more;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.task_msg)
    ImageView task_msg;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;

    private Context mContext;
    private PointListPresenter mPresenter;
    private PointListAdapter pointListAdapter;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);

        ButterKnife.bind(this);
        mContext=this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        mPresenter.getPoints(userID);
        initView();
    }

    private void initView() {
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        task_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PointListActivity.this, TaskListActivity.class);
                startActivity(intent);
            }
        });
        registerForContextMenu(more);
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getPoints(userID);
            }
        });
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_point_list_more, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.add_nfc:
                        intent=new Intent(PointListActivity.this, AddInspectionNFCItemActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.add_dev:
                        intent=new Intent(PointListActivity.this, AddInspectionNormalItemActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.all_items:
                        intent=new Intent(PointListActivity.this, ItemsListActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });

        popupMenu.show();
    }

    @Override
    protected PointListPresenter createPresenter() {
        mPresenter=new PointListPresenter(this);
        return mPresenter;
    }

    @Override
    public void getDataSuccess(List<Point> pointList) {
        if(pointList.size()==0){
            Toast.makeText(mContext,"无数据",Toast.LENGTH_SHORT).show();
        }//@@7.7

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        pointListAdapter = new PointListAdapter(mContext, pointList, mPresenter);
        pointListAdapter.setOnRecyclerViewItemClickListener(new PointListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Point data) {
                Intent intent=new Intent(mContext, ItemsListActivity.class);
                intent.putExtra("pid",data.getPid());
                startActivity(intent);
            }
        });
        recycler_view.setAdapter(pointListAdapter);
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
