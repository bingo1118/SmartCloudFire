package com.smart.cloud.fire.activity.AlarmMsg;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.SQLEntity.AlarmMsg;
import com.smart.cloud.fire.SQLEntity.UploadAlarmMsgTemp;
import com.smart.cloud.fire.activity.UploadAlarmInfo.UploadAlarmInfoActivity;
import com.smart.cloud.fire.adapter.AlarmMsgAdapter;
import com.smart.cloud.fire.adapter.RefreshRecyclerAdapter;
import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.NpcCommon;
import com.smart.cloud.fire.mvp.fragment.CollectFragment.AlarmMessageModel;
import com.smart.cloud.fire.pushmessage.PushAlarmMsg;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class AlarmMsgActivity extends MvpActivity<AlarmMsgPresenter> implements AlarmMsgView{

    private AlarmMsgPresenter presenter;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.demo_recycler)
    RecyclerView demoRecycler;
    @Bind(R.id.demo_swiperefreshlayout)
    SwipeRefreshLayout demoSwiperefreshlayout;
    @Bind(R.id.search_image)
    ImageView search_image;
    private String userID;
    private int privilege;
    private String page;
    private Context mContext;
    private boolean research = false;
    private List<AlarmMessageModel> messageModelList;
    private int loadMoreCount;
    private boolean wheelScrolled = false;
    private LinearLayoutManager linearLayoutManager;
    private AlarmMsgAdapter adapter;
    private int lastVisibleItem;

    //startStr, endStr, areaId, placeTypeId
    private int type=1;//@@是否是按条件查询 1 查询所有 2 条件查询 3 离线数据
    private String startStr;
    private String endStr;
    private String areaId;
    private String placeTypeId;
    private String parentId;

    List<Area> parent = null;//@@9.11
    Map<String, List<Area>> map = null;//@@9.11
    private int deal_position;

    int grade;
    int distance;
    int progress;

    int grade_t;
    int distance_t;
    int progress_t;

    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_msg);
        ButterKnife.bind(this);

        mContext = this;
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        page = "1";

        mvpPresenter.getNeedOrderMsg(userID, privilege + "", page,"","","","",1);
        if(NpcCommon.verifyNetwork(MyApp.app)){
//            mvpPresenter.getNeedAlarmMsg(userID, privilege + "", "", 3, "", "", "", "","","","","1");
            mvpPresenter.getNeedOrderMsg(userID, privilege + "", page,"","","1","",3);
            title_tv.setText("全部任务");
            search_image.setVisibility(View.VISIBLE);
        }else{
            type=3;
            title_tv.setText("离线任务");
            search_image.setVisibility(View.GONE);
        }
        init();
        dealWithTemp();
    }

    private void dealWithTemp() {
        if(NpcCommon.verifyNetwork(MyApp.app)){
            List<UploadAlarmMsgTemp> temps = LitePal.where(" mac like ? ",  "%").find(UploadAlarmMsgTemp.class);
            if(temps.size()>0){
                for (final UploadAlarmMsgTemp t:temps) {
                    if(t.getImage_path().length()>0){
                        final File imagetemp=new File(Environment.getExternalStorageDirectory() + File.separator + "SmartCloudFire/image/"+t.getImage_path()+".jpg");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UploadAlarmInfoActivity.uploadFile(imagetemp,"","","",t.getImage_path(),"devalarm",t.getImage_path());
                                imagetemp.delete();
                            }
                        }).start();
                    }
                    if(t.getVideo_path().length()>0){
                        final File imagetemp=new File(Environment.getExternalStorageDirectory() + File.separator + "SmartCloudFire/videotemp/"+t.getVideo_path()+".mp4");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UploadAlarmInfoActivity.uploadFile(imagetemp,"","","",t.getVideo_path(),"devalarm_video",t.getVideo_path());
                                imagetemp.delete();
                            }
                        }).start();
                    }

                    mvpPresenter.dealAlarmDetailTemp(userID, t.getMac(), privilege+"" ,deal_position,userID,
                            t.getAlarmTruth(),t.getDealDetail(),
                            t.getImage_path(),t.getVideo_path());
                }
                LitePal.deleteAll(UploadAlarmMsgTemp.class);
                T.showShort(mContext,"离线数据上传完成");
            }

        }
    }

    private void init() {
        //设置刷新时动画的颜色，可以设置4个
        demoSwiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        demoSwiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        demoSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        demoRecycler.setLayoutManager(linearLayoutManager);

        demoSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                research = false;
                page = "1";

                if(!NpcCommon.verifyNetwork(MyApp.app)){
                    type=3;
                    title_tv.setText("离线任务");
                    search_image.setVisibility(View.GONE);
                }else{
                    type=1;//@@7.12
                    title_tv.setText("全部任务");
                    search_image.setVisibility(View.VISIBLE);
//                    mvpPresenter.getNeedAlarmMsg(userID, privilege + "", "", 3, "", "", "", "","","","","1");
                    mvpPresenter.getNeedOrderMsg(userID, privilege + "", page,"","","","1",3);
                }
//                mvpPresenter.getNeedAlarmMsg(userID, privilege + "", page, 1, "", "", "", "","","","","");
                mvpPresenter.getNeedOrderMsg(userID, privilege + "", page,"","","","",1);

                mProgressBar.setVisibility(View.GONE);
            }
        });

        demoRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(adapter==null){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    if (loadMoreCount >= 20 && research == false) {
                    if (loadMoreCount >= 20 && type!=3) {//@@7.12
                        page = Integer.parseInt(page) + 1 + "";
                        if(type==2){
//                            mvpPresenter.getNeedAlarmMsg(userID, privilege + "", page, 2, "", "", "", "","",grade+"",distance+"",progress+"");
                            mvpPresenter.getNeedOrderMsg(userID, privilege + "", page,"","",grade+"",progress+"",2);
                        }else{
//                            mvpPresenter.getNeedAlarmMsg(userID, privilege + "", page, 1, "", "", "", "","","","","");
                            mvpPresenter.getNeedOrderMsg(userID, privilege + "", page,"","","","",1);
                        }//@@7.12 区分是否是条件查询 1 查询全部 2 条件查询
                        mProgressBar.setVisibility(View.GONE);
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

    @OnClick({R.id.search_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_image:
                showPopupWindow(view);
                break;
            default:
                break;
        }
    }


    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.alarm_msg_search_menu, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT , true);

        Button commit_btn=(Button)contentView.findViewById(R.id.commit_btn);
        RadioGroup rg_grade=(RadioGroup) contentView.findViewById(R.id.rg_grade);
        RadioGroup rg_distance=(RadioGroup) contentView.findViewById(R.id.rg_distance);
        RadioGroup rg_progress=(RadioGroup) contentView.findViewById(R.id.rg_progress);
        rg_grade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup rg,int checkedId)
            {
                switch(checkedId){
                    case R.id.rg_grade_jj:
                        grade_t=1;//紧急
                        break;
                    case R.id.rg_grade_yb:
                        grade_t=2;//一般
                        break;
                }
            }
        });

        rg_distance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup rg,int checkedId)
            {
                switch(checkedId){
                    case R.id.rg_distance_xy:
                        distance_t=1;//小于
                        break;
                    case R.id.rg_distance_dy:
                        distance_t=2;//大于
                        break;
                }
            }
        });

        rg_progress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup rg,int checkedId)
            {
                switch(checkedId){
                    case R.id.rg_progress_wcl:
                        progress_t=1;//未处理
                        break;
                    case R.id.rg_progress_ycl:
                        progress_t=3;//已处理
                        break;
                    case R.id.rg_progress_ycs:
                        progress_t=2;//超时
                        break;
                }
            }
        });

        commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page="1";
                type=2;
                research=false;
                title="";
                switch (grade_t){
                    case 1:
                        title+="紧急 ";
                        break;
                    case 2:
                        title+="一般 ";
                        break;
                }
                switch (distance_t){
                    case 1:
                        title+="<1000m ";
                        break;
                    case 2:
                        title+=">1000m ";
                        break;
                }
                switch (progress_t){
                    case 1:
                        title+="未处理 ";
                        break;
                    case 2:
                        title+="超时 ";
                        break;
                    case 3:
                        title+="已处理 ";
                        break;
                }
                title_tv.setText(title);
                grade=grade_t;
                distance=distance_t;
                progress=progress_t;
//                mvpPresenter.getNeedAlarmMsg(userID, privilege + "", page, 2, "", "", "", "","",grade+"",distance+"",progress+"");
                mvpPresenter.getNeedOrderMsg(userID, privilege + "", page,"","",grade+"",progress+"",2);
                popupWindow.dismiss();
                grade_t=0;
                progress_t=0;
                distance_t=0;
            }
        });


        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        backgroundAlpha(0.5f);//@@12.20开启时其他区域半透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable( R.drawable.list_item_color_bg));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected AlarmMsgPresenter createPresenter() {
        presenter=new AlarmMsgPresenter(this);
        return presenter;
    }

    @Override
    public void getDataSuccess(List<AlarmMessageModel> alarmMessageModels) {
        int pageInt = Integer.parseInt(page);
        if (messageModelList != null && messageModelList.size() >= 20 && pageInt > 1) {
            loadMoreCount=alarmMessageModels.size();
            messageModelList.addAll(alarmMessageModels);
            adapter.changeMoreStatus(RefreshRecyclerAdapter.NO_DATA);
        } else {
            messageModelList = new ArrayList<>();
            loadMoreCount=alarmMessageModels.size();
            messageModelList.addAll(alarmMessageModels);
            adapter = new AlarmMsgAdapter(this, messageModelList, presenter, userID, privilege + "");
            adapter.setOnClickListener(new AlarmMsgAdapter.onClickListener() {
                @Override
                public void onClick(View view, final int position) {
                    if(view.getId()==R.id.msg_deal_btn){
                        String username = SharedPreferencesManager.getInstance().getData(mContext,
                                SharedPreferencesManager.SP_FILE_GWELL,
                                SharedPreferencesManager.KEY_RECENTNAME);
                        String url= ConstantValues.SERVER_IP_NEW+"receiveOrder?userId="+username+"&smokeMac="+messageModelList.get(position).getMac();
                        VolleyHelper helper=VolleyHelper.getInstance(mContext);
                        RequestQueue mQueue = helper.getRequestQueue();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            int errorCode=response.getInt("errorCode");
                                            if(errorCode==0){
                                                Intent intent=new Intent(mContext, UploadAlarmInfoActivity.class);
                                                PushAlarmMsg mPushAlarmMsg=new PushAlarmMsg();
                                                mPushAlarmMsg.setMac(messageModelList.get(position).getMac());
                                                mPushAlarmMsg.setName(messageModelList.get(position).getName());
                                                mPushAlarmMsg.setAddress(messageModelList.get(position).getAddress());
                                                mPushAlarmMsg.setAlarmTypeName(messageModelList.get(position).getAlarmTypeName());
                                                mPushAlarmMsg.setAlarmTime(messageModelList.get(position).getAlarmTime());
                                                intent.putExtra("mPushAlarmMsg",mPushAlarmMsg);
                                                intent.putExtra("mac",messageModelList.get(position).getMac());
                                                intent.putExtra("alarm",messageModelList.get(position).getAlarmType()+"");
                                                startActivityForResult(intent,6);
                                                T.showShort(mContext,"接单成功");
                                            }else{
                                                T.showShort(mContext,response.getString("error"));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        mQueue.add(jsonObjectRequest);
                        deal_position=position;
                    }
                }
            });
            demoRecycler.setAdapter(adapter);
            demoSwiperefreshlayout.setRefreshing(false);
            adapter.changeMoreStatus(RefreshRecyclerAdapter.NO_DATA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==6){
            if(data!=null)
                mvpPresenter.dealAlarmDetail(userID, messageModelList.get(deal_position).getMac(), privilege+"" ,deal_position,userID,
                        data.getStringExtra("alarmTruth"),data.getStringExtra("dealDetail"),
                        data.getStringExtra("image_path"),data.getStringExtra("video_path"));//@@5.19添加index位置参数
        }
    }

    @Override
    public void getDataFail(String msg) {
        demoSwiperefreshlayout.setRefreshing(false);
        if(adapter!=null){
            adapter.changeMoreStatus(RefreshRecyclerAdapter.NO_DATA);
        }
        T.showShort(mContext, msg);
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
    public void dealAlarmMsgSuccess(List<AlarmMessageModel> alarmMessageModels) {
        messageModelList.clear();
        messageModelList.addAll(alarmMessageModels);
        loadMoreCount=alarmMessageModels.size();//@@7.13
        adapter = new AlarmMsgAdapter(this, messageModelList, presenter, userID, privilege + "");
        demoRecycler.setAdapter(adapter);
        adapter.changeMoreStatus(RefreshRecyclerAdapter.NO_DATA);
    }

    @Override
    public void updateAlarmMsgSuccess(int index) {
        adapter.setList(index);
    }


    @Override
    public void getDataByCondition(List<AlarmMessageModel> alarmMessageModels) {
        if(!research){
            research = true;
            messageModelList.clear();
        }//@@7.13
        int pageInt = Integer.parseInt(page);
        if (messageModelList != null && messageModelList.size() >= 20 && pageInt > 1) {
            loadMoreCount=alarmMessageModels.size();
            messageModelList.addAll(alarmMessageModels);
            adapter.changeMoreStatus(RefreshRecyclerAdapter.NO_DATA);
        } else {
            loadMoreCount=alarmMessageModels.size();
            messageModelList.addAll(alarmMessageModels);
            adapter = new AlarmMsgAdapter(this, messageModelList, presenter, userID, privilege + "");//@@9.11
            adapter.setOnClickListener(new AlarmMsgAdapter.onClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if(view.getId()==R.id.msg_deal_btn){
                        Intent intent=new Intent(mContext, UploadAlarmInfoActivity.class);
                        intent.putExtra("mac",messageModelList.get(position).getMac());
                        intent.putExtra("alarm",messageModelList.get(position).getAlarmType()+"");
                        startActivityForResult(intent,6);
                        deal_position=position;
                    }
                }
            });
            demoRecycler.setAdapter(adapter);//@@9.11
//            adapter.changeMoreStatus(RefreshRecyclerAdapter.NO_DATA);
        }//@@7.13 添加条件查询分页

    }

    @Override
    public void getOfflineDataSuccess(List<AlarmMessageModel> alarmMessageModels) {
        SQLiteDatabase db = LitePal.getDatabase();
        LitePal.deleteAll(AlarmMessageModel.class);
        for (AlarmMessageModel model:alarmMessageModels) {
            model.save();
        }
//        List<AlarmMessageModel> AlarmMsgs =LitePal.where(" mac like ? ",  "%").find(AlarmMessageModel.class);
//        System.out.print(AlarmMsgs.size());
    }

}
