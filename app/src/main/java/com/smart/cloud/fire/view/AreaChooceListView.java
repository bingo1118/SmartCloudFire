package com.smart.cloud.fire.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.base.presenter.BasePresenter;
import com.smart.cloud.fire.global.Area;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.ShopType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2017/8/31.
 */
public class AreaChooceListView extends LinearLayout {

    private BasePresenter basePresenter;
    private EditText editText;
    private ImageView imageView;
    private PopupWindow popupWindow = null;
    private ArrayList<Object> dataList =  new ArrayList<Object>();
    private View mView;
    private Context mContext;
    ProgressBar loading_prg_monitor;
    private RelativeLayout clear_choice;

    List<Area> parent = null;
    Map<String, List<Area>> map = null;

    Activity a;
    public void setActivity(Activity a){
        this.a=a;
    }

    private boolean ifHavaChooseAll=true;

    public AreaChooceListView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }
    public AreaChooceListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public AreaChooceListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView();
    }


    public void initView(){
        mContext = getContext();
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater =  (LayoutInflater) getContext().getSystemService(infServie);
        View view  = layoutInflater.inflate(R.layout.dropdownlist_view_fire, this,true);
        loading_prg_monitor = (ProgressBar) findViewById(R.id.loading_prg_monitor);
        editText= (EditText)findViewById(R.id.text);
        imageView = (ImageView)findViewById(R.id.btn);
        clear_choice = (RelativeLayout) findViewById(R.id.clear_choice);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (popupWindow == null) {
                    showPopWindow();
                } else {
                    closePopWindow();
                }
            }
        });
        clear_choice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.size() > 0) {
                    Object object = dataList.get(0);
                    if (object instanceof Area) {
                        basePresenter.getArea(new Area());
                    }
                    if (object instanceof ShopType) {
                        basePresenter.getShop(new ShopType());
                    }
                    imageView.setVisibility(View.VISIBLE);
                    clear_choice.setVisibility(View.GONE);
                    editText.setText("");
                }
            }
        });
    }

    public void addFinish(){
        imageView.setVisibility(View.VISIBLE);
        clear_choice.setVisibility(View.GONE);
    }

    public boolean ifShow(){
        if (popupWindow == null) {
            return false;
        }else{
            return true;
        }
    }

    public void showLoading(){
        imageView.setVisibility(View.GONE);
        clear_choice.setVisibility(View.GONE);
        loading_prg_monitor.setVisibility(View.VISIBLE);
    }

    public void closeLoading(){
        loading_prg_monitor.setVisibility(View.GONE);
        clear_choice.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }

    public void searchClose(){
        imageView.setVisibility(View.VISIBLE);
        clear_choice.setVisibility(View.GONE);
    }


    //@@12.20
    public void backgroundAlpha(float bgAlpha)
    {
        if(a==null){
            return;
        }
        WindowManager.LayoutParams lp = a.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        a.getWindow().setAttributes(lp);
    }

    /**
     * 打开下拉列表弹窗
     */
    public void showPopWindow() {
        // 加载popupWindow的布局文件
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater =  (LayoutInflater) getContext().getSystemService(infServie);
        View contentView  = layoutInflater.inflate(R.layout.area_chooce_listview, null,false);
        ExpandableListView mainlistview = (ExpandableListView) contentView
                .findViewById(R.id.main_expandablelistview);
        mainlistview.setAdapter(new MyAdapter());
        popupWindow = new PopupWindow(contentView,LayoutParams.WRAP_CONTENT,800,true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable( R.drawable.list_item_color_bg));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow=null;
            }
        });//@@12.20
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                popupWindow = null;
            }
        });
        popupWindow.showAsDropDown(this);
        editText.setOnClickListener(new OnClickListener() {//@@9.12
            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    showPopWindow();
                } else {
                    closePopWindow();
                }
            }
        });
    }

    /**
     * 关闭下拉列表弹窗
     */
    public void closePopWindow(){
        if(popupWindow!=null){
            popupWindow.dismiss();
            popupWindow = null;
            backgroundAlpha(1f);
        }
    }
    /**
     * 设置数据
     * @param list
     */
    public void setItemsData(ArrayList<Object> list,BasePresenter basePresenter){
        dataList = list;
        this.basePresenter = basePresenter;
    }


    public void setItemsData2( List<Area> parent,Map<String, List<Area>> map,BasePresenter basePresenter){
        this.parent = parent;
        this.map=map;
        this.basePresenter = basePresenter;
    }

    public void setIfHavaChooseAll(boolean ifHavaChooseAll) {
        this.ifHavaChooseAll = ifHavaChooseAll;
    }

    /**
     * 数据适配器
     * @author caizhiming
     *
     */
    class MyAdapter extends BaseExpandableListAdapter {

        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition).getAreaName();
            return (map.get(key).get(childPosition));
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parentView) {
            String key = parent.get(groupPosition).getAreaName();
            final Area info = map.get(key).get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.second_textview);
            tv.setText(info.getAreaName());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(info.getAreaName());
                    imageView.setVisibility(View.GONE);
                    if(clear_choice_isShow){//@@9.12
                        clear_choice.setVisibility(View.VISIBLE);
                    }
                    if(basePresenter!=null){
                        basePresenter.getArea(info);
                    }
                    if(onChildAreaChooceClickListener!=null){
                        onChildAreaChooceClickListener.OnChildClick(info);//@@9.12
                    }
                    closePopWindow();
                }
            });
            return tv;
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition).getAreaName();
            if(map.get(key)==null){
                return 0;
            }else{
                int size=map.get(key).size();
                return size;
            }
        }
        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parentView) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.parent_textview);
            ImageView iv = (ImageView) convertView
                    .findViewById(R.id.all_cheak);
            final Area info=parent.get(groupPosition);
            tv.setText(info.getAreaName());
            if(ifHavaChooseAll){
                if(isExpanded){
                    iv.setVisibility(VISIBLE);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editText.setText(info.getAreaName());
                            imageView.setVisibility(View.GONE);
                            if(clear_choice_isShow){//@@9.12
                                clear_choice.setVisibility(View.VISIBLE);
                            }
                            if(basePresenter!=null){
                                basePresenter.getArea(info);
                            }
                            if(onChildAreaChooceClickListener!=null){
                                onChildAreaChooceClickListener.OnChildClick(info);
                            }
                            closePopWindow();
                        }
                    });
                }else{
                    iv.setVisibility(GONE);
                }
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
    private static class ListItemView{
        TextView tv;
        LinearLayout layout;
    }

    public String getTv(){
        String editTextStr = editText.getText().toString().trim();
        return editTextStr;
    }

    public void setEditText(String editTivew){
        editText.setText(editTivew);
    }

    public void setEditTextHint(String textStr){
        editText.setHint(textStr);
    }

    OnChildAreaChooceClickListener onChildAreaChooceClickListener=null;//@@9.12
    public interface OnChildAreaChooceClickListener{
        void OnChildClick(Area info);
    }

    public void setOnChildAreaChooceClickListener(OnChildAreaChooceClickListener o){
        this.onChildAreaChooceClickListener=o;
    }


    //@@9.12设置字体颜色
    public void seteditTextColor(String c){
        editText.setTextColor(Color.parseColor(c));
    }

    //@@9.12设置提示字体颜色
    public void setHintTextColor(String c){
        editText.setHintTextColor(Color.parseColor(c));
    }

    boolean clear_choice_isShow=true;
    //@@9.12设置清除图标
    public void setclear_choice(Drawable d,boolean isshow){
        clear_choice.setBackground(d);
        clear_choice_isShow=isshow;
    }


}
