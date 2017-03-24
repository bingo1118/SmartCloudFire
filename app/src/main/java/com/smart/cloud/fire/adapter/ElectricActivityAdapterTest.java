package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.cloud.fire.global.ElectricValue;
import com.smart.cloud.fire.mvp.electric.ElectricPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/11/15.
 */
public class ElectricActivityAdapterTest extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ElectricValue.ElectricValueBean> electricList;
    private ElectricPresenter electricPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private int ElectricOne=0;
    private int ElectricTwo=0;
    private int ElectricFour=0;
    private int ElectricThree = 0;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (ElectricValue.ElectricValueBean) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ElectricValue.ElectricValueBean data);
    }

    public ElectricActivityAdapterTest(Context mContext, List<ElectricValue.ElectricValueBean> electricList, ElectricPresenter electricPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.electricList = electricList;
        this.mContext = mContext;
        this.electricPresenter = electricPresenter;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        View view = mInflater.inflate(R.layout.electric_activity_adapter_test, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ElectricValue.ElectricValueBean electric = electricList.get(position);
        int electricType = electric.getElectricType();
        switch (electricType) {
            case 6:
                String value = electric.getValue();
                if (value.length() > 0) {
                    ElectricOne = ElectricOne+1;
                    String[] electricThreshold = electric.getElectricThreshold().split("\\\\");
                    String electricThresholdOne =null;
                    String electricThresholdTwo= null;
                    if(electricThreshold.length==2){
                        electricThresholdOne = electricThreshold[0];
                        electricThresholdTwo=electricThreshold[1];
                    }else{
                        electricThresholdTwo=electricThreshold[0];
                    }
                    ((ItemViewHolder) holder).tvImage.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricName.setText("电压"+ElectricOne);
                    ((ItemViewHolder) holder).electricAlarmValue.setText(electric.getElectricThreshold()+"V");
                    ((ItemViewHolder) holder).electricCurrentValue.setText(value + "V");
                    if (Double.parseDouble(value) - Double.parseDouble(electricThresholdTwo) > 0) {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setText("高压");
                    } else if (electricThresholdOne!=null&&Double.parseDouble(value) - Double.parseDouble(electricThresholdOne) < 0) {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setText("欠压");
                    } else {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(18, 184, 245));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(18, 184, 245));
                        ((ItemViewHolder) holder).electricStates.setText("正常");
                    }
                }
                break;
            case 7:
                String value7 = electric.getValue();
                if (value7.length() > 0) {
                    ElectricTwo = ElectricTwo+1;
                    double electricThreshold = Double.parseDouble(electric.getElectricThreshold());
                    ((ItemViewHolder) holder).tvImage.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricName.setText("电流"+ElectricTwo);
                    ((ItemViewHolder) holder).electricAlarmValue.setText(electricThreshold+ "A");
                    ((ItemViewHolder) holder).electricCurrentValue.setText(value7 + "A");
                    if (Double.parseDouble(value7) - electricThreshold > 0) {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setText("过流");
                    } else {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(18, 184, 245));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(18, 184, 245));
                        ((ItemViewHolder) holder).electricStates.setText("正常");
                    }
                }
                break;
            case 8:
                String value8 = electric.getValue();
                if (value8.length() > 0) {
                    double electricThreshold = Double.parseDouble(electric.getElectricThreshold());
                    ((ItemViewHolder) holder).tvImage.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricName.setText("漏电流");
                    ((ItemViewHolder) holder).electricAlarmValue.setText(electricThreshold+ "mA");
                    ((ItemViewHolder) holder).electricCurrentValue.setText(value8 + "mA");
                    if (Double.parseDouble(value8) - electricThreshold > 0) {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setText("漏电流");
                    } else {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(18, 184, 245));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(18, 184, 245));
                        ((ItemViewHolder) holder).electricStates.setText("正常");
                    }
                }
                break;
            case 9:
                String value97 = electric.getValue();
                if (value97.length() > 0) {
                    ElectricFour = ElectricFour+1;
                    double electricThreshold = Double.parseDouble(electric.getElectricThreshold());
                    ((ItemViewHolder) holder).tvImage.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricName.setText("温度"+ElectricFour);
                    ((ItemViewHolder) holder).electricAlarmValue.setText(electricThreshold+ "℃");
                    ((ItemViewHolder) holder).electricCurrentValue.setText(value97 + "℃");
                    if (Double.parseDouble(value97) - electricThreshold > 0) {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(224, 47, 27));
                        ((ItemViewHolder) holder).electricStates.setText("高温");
                    } else {
                        ((ItemViewHolder) holder).electricCurrentValue.setTextColor(Color.rgb(18, 184, 245));
                        ((ItemViewHolder) holder).electricStates.setTextColor(Color.rgb(18, 184, 245));
                        ((ItemViewHolder) holder).electricStates.setText("正常");
                    }
                }
                break;
        }

        holder.itemView.setTag(electric);
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return electricList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.electric_name)
        TextView electricName;
        @Bind(R.id.electric_alarm_value)
        TextView electricAlarmValue;
        @Bind(R.id.electric_current_value)
        TextView electricCurrentValue;
        @Bind(R.id.electric_states)
        TextView electricStates;
        @Bind(R.id.electric_lin)
        LinearLayout electricLin;
        @Bind(R.id.tv_image)
        TextView tvImage;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加数据
    public void addItem(List<ElectricValue.ElectricValueBean> electrics) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        electrics.addAll(electricList);
        electricList.removeAll(electricList);
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<ElectricValue.ElectricValueBean> electrics) {
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }

}
