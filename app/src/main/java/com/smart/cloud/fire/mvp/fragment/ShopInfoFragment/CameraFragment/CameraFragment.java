package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.CameraFragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.p2p.core.P2PHandler;
import com.smart.cloud.fire.adapter.ShopCameraAdapter;
import com.smart.cloud.fire.base.ui.MvpFragment;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;
import com.smart.cloud.fire.global.SmokeSummary;
import com.smart.cloud.fire.mvp.fragment.MapFragment.Camera;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragment;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentPresenter;
import com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.ShopInfoFragmentView;
import com.smart.cloud.fire.utils.SerializableMap;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/10/28.
 */
public class CameraFragment extends MvpFragment<ShopInfoFragmentPresenter> implements ShopInfoFragmentView {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private LinearLayoutManager linearLayoutManager;
    private ShopCameraAdapter shopCameraAdapter;
    private int lastVisibleItem;
    private Context mContext;
    private List<Camera> list;
    private int loadMoreCount;
    private boolean research = false;
    private String page;
    private String userID;
    private int privilege;
    private ShopInfoFragmentPresenter mShopInfoFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_dev, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext=getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        page = "1";
        list = new ArrayList<>();
        refreshListView();
        regFilter();
        mvpPresenter.getAllCamera(userID, privilege + "", page, list,false);
    }
    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.Action.REFRESH_CAMERA_PWD);
        filter.addAction(ConstantValues.Action.GET_FRIENDS_STATE);//@@5.18
        mContext.registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub

            if(intent.getAction().equals(ConstantValues.Action.REFRESH_CAMERA_PWD)){
                page = "1";
                list.clear();
                mvpPresenter.getAllCamera(userID, privilege + "", page, list,false);
            }
            //@@5.18获取摄像机状态
            if(intent.getAction().equals(ConstantValues.Action.GET_FRIENDS_STATE)){
                SerializableMap map= (SerializableMap) intent.getSerializableExtra("contactList");
                Map<String,Integer> cameraList=map.getIntMap();
                for(int i=0;i<list.size();i++){
                    if(cameraList.containsKey(list.get(i).getCameraId())){//@@10.18
                        list.get(i).setIsOnline(cameraList.get(list.get(i).getCameraId()));
                    }
                }
                shopCameraAdapter.notifyDataSetChanged();
            }
        }
    };

    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipereFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipereFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipereFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = "1";
                list.clear();
                mvpPresenter.getAllCamera(userID, privilege + "", page, list,true);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (research) {
                    if(shopCameraAdapter!=null){
                        shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
                    }
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == shopCameraAdapter.getItemCount()) {
                    if (list != null &&loadMoreCount >= 20 && research == false) {
                        page = Integer.parseInt(page) + 1 + "";
                        mvpPresenter.getAllCamera(userID, privilege + "", page, list,true);
                    }else{
                        T.showShort(mContext,"已经没有更多数据了");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected ShopInfoFragmentPresenter createPresenter() {
        mShopInfoFragmentPresenter = new ShopInfoFragmentPresenter(this,(ShopInfoFragment)getParentFragment());
        return mShopInfoFragmentPresenter;
    }

    @Override
    public String getFragmentName() {
        return "CameraFragment";
    }

    @Override
    public void getDataSuccess(List<?> smokeList,boolean search) {
        loadMoreCount = smokeList.size();
        list.clear();
        list.addAll((List<Camera>)smokeList);
        shopCameraAdapter = new ShopCameraAdapter(mContext, list);
        recyclerView.setAdapter(shopCameraAdapter);
        swipereFreshLayout.setRefreshing(false);
        shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
        String[] cameralist=new String[smokeList.size()];//@@5.18
        for(int i=0;i<smokeList.size();i++){
            if(0==list.get(i).getVideoType()){
                cameralist[i]=list.get(i).getCameraId().length()>10?"0":list.get(i).getCameraId();
            }else{
                cameralist[i]="0";
            }
        }//@@5.18
        P2PHandler.getInstance().getFriendStatus(cameralist);//@@5.18
    }

    @Override
    public void getDataFail(String msg) {
        swipereFreshLayout.setRefreshing(false);
        T.showShort(mContext, msg);
        if(shopCameraAdapter!=null){
            shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.NO_DATA);
        }
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
    public void onLoadingMore(List<?> smokeList) {
        loadMoreCount = smokeList.size();
        List<Camera> cameraList=(List<Camera>)smokeList;
        list.addAll(cameraList);
        shopCameraAdapter.changeMoreStatus(ShopCameraAdapter.LOADING_MORE);

        String[] cameralist=new String[smokeList.size()];//@@10.18
        for(int i=0;i<cameraList.size();i++){
            if(0==cameraList.get(i).getVideoType()){
                cameralist[i]=cameraList.get(i).getCameraId().length()>10?"0":cameraList.get(i).getCameraId();
            }else{
                cameralist[i]="0";
            }
        }//@@5.18
        P2PHandler.getInstance().getFriendStatus(cameralist);//@@10.18
    }

    @Override
    public void getAreaType(ArrayList<?> shopTypes, int type) {
    }

    @Override
    public void getAreaTypeFail(String msg, int type) {
    }

    @Override
    public void unSubscribe(String type) {
    }

    @Override
    public void getLostCount(String count) {
    }

    @Override
    public void getChoiceArea(Area area) {

    }

    @Override
    public void getChoiceShop(ShopType shopType) {

    }

    @Override
    public void getSmokeSummary(SmokeSummary smokeSummary) {

    }

    @Override
    public void refreshView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mContext.unregisterReceiver(mReceiver);
    }
}
