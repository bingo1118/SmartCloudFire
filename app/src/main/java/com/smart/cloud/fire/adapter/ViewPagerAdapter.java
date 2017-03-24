package com.smart.cloud.fire.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smart.cloud.fire.mvp.fragment.MapFragment.Smoke;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ViewPagerAdapter extends PagerAdapter {
    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.shop_address)
    TextView shopAddress;
    @Bind(R.id.right_tv)
    TextView rightTv;
    private Activity activity;
    private List<View> list;
    private List<Smoke> smokes;

    public ViewPagerAdapter(List<View> list, Activity activity,List<Smoke> smokes) {
        this.list = list;
        this.activity = activity;
        this.smokes = smokes;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list.get(position));
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        ButterKnife.bind(this,view);
        ((ViewPager) container).addView(view, 0);
        if(list.size()==1){
            leftTv.setVisibility(View.GONE);
            rightTv.setVisibility(View.GONE);
        }
        if(position==0){
            leftTv.setVisibility(View.GONE);
        }
        if (position==list.size()-1){
            rightTv.setVisibility(View.GONE);
        }
        Smoke smoke = smokes.get(position);
        shopName.setText(smoke.getName());
        shopAddress.setText(smoke.getAddress());
        return view;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
