package com.smart.cloud.fire.adapter;

import android.content.Context;
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

public class ElectricActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ElectricValue> electricList;
    private ElectricPresenter electricPresenter;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (ElectricValue) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ElectricValue data);
    }

    public ElectricActivityAdapter(Context mContext, List<ElectricValue> electricList, ElectricPresenter electricPresenter) {
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
        View view = mInflater.inflate(R.layout.electric_activity_adapter, parent, false);
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
        ElectricValue electric = electricList.get(position);
        int electricType = electric.getElectricType();
        List<ElectricValue.ElectricValueBean> electricValueBeen = electric.getElectricValue();
        switch (electricType) {
            case 6:
                String value = electricValueBeen.get(0).getValue();
                String value2 = electricValueBeen.get(1).getValue();
                String value3 = electricValueBeen.get(2).getValue();
                if (value.length() > 0) {
                    ((ItemViewHolder) holder).tvImage.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricName.setText("电压1");
                    ((ItemViewHolder) holder).electricAlarmValue.setText("220V");
                    ((ItemViewHolder) holder).electricCurrentValue.setText(value+"V");
                    if(Double.parseDouble(value)-220>0){
                        ((ItemViewHolder) holder).electricStates.setText("高压");
                    }else if(Double.parseDouble(value)-220<0){
                        ((ItemViewHolder) holder).electricStates.setText("欠压");
                    }else{
                        ((ItemViewHolder) holder).electricStates.setText("正常");
                    }
                }
                if (value3.length() > 0) {
                    ((ItemViewHolder) holder).tvImage2.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin3.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricAlarmValue3.setText("220V");
                    ((ItemViewHolder) holder).electricName3.setText("电压3");
                    ((ItemViewHolder) holder).electricCurrentValue3.setText(value3+"V");
                    if(Double.parseDouble(value3)-220>0){
                        ((ItemViewHolder) holder).electricStates3.setText("高压");
                    }else if(Double.parseDouble(value3)-220<0){
                        ((ItemViewHolder) holder).electricStates3.setText("欠压");
                    }else{
                        ((ItemViewHolder) holder).electricStates3.setText("正常");
                    }
                }
                if (value2.length() > 0) {
                    ((ItemViewHolder) holder).tvImage1.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin2.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricAlarmValue2.setText("220V");
                    ((ItemViewHolder) holder).electricName2.setText("电压2");
                    ((ItemViewHolder) holder).electricCurrentValue2.setText(value2+"V");
                    ((ItemViewHolder) holder).electricStates2.setText("正常");
                    if(Double.parseDouble(value2)-220>0){
                        ((ItemViewHolder) holder).electricStates2.setText("高压");
                    }else if(Double.parseDouble(value2)-220<0){
                        ((ItemViewHolder) holder).electricStates2.setText("欠压");
                    }else{
                        ((ItemViewHolder) holder).electricStates2.setText("正常");
                    }
                }
                break;
            case 7:
                String value7 = electricValueBeen.get(0).getValue();
                String value72 = electricValueBeen.get(1).getValue();
                String value73 = electricValueBeen.get(2).getValue();
                if (value7.length() > 0) {
                    ((ItemViewHolder) holder).tvImage.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricName.setText("电流1");
                    ((ItemViewHolder) holder).electricAlarmValue.setText("2A");
                    ((ItemViewHolder) holder).electricCurrentValue.setText(value7+"A");
                    ((ItemViewHolder) holder).electricStates.setText("正常");
                    if(Double.parseDouble(value7)-2>0){
                        ((ItemViewHolder) holder).electricStates.setText("过流");
                    }else if(Double.parseDouble(value7)-2<0){
                        ((ItemViewHolder) holder).electricStates.setText("低流");
                    }else{
                        ((ItemViewHolder) holder).electricStates.setText("正常");
                    }
                }
                if (value73.length() > 0) {
                    ((ItemViewHolder) holder).tvImage2.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin3.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricAlarmValue3.setText("2A");
                    ((ItemViewHolder) holder).electricName3.setText("电流3");
                    ((ItemViewHolder) holder).electricCurrentValue3.setText(value73+"A");
                    if(Double.parseDouble(value73)-2>0){
                        ((ItemViewHolder) holder).electricStates3.setText("过流");
                    }else if(Double.parseDouble(value73)-2<0){
                        ((ItemViewHolder) holder).electricStates3.setText("低流");
                    }else{
                        ((ItemViewHolder) holder).electricStates3.setText("正常");
                    }
                }
                if (value72.length() > 0) {
                    ((ItemViewHolder) holder).tvImage1.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricLin2.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).electricAlarmValue2.setText("2A");
                    ((ItemViewHolder) holder).electricName2.setText("电流2");
                    ((ItemViewHolder) holder).electricCurrentValue2.setText(value72+"A");
                    if(Double.parseDouble(value72)-2>0){
                        ((ItemViewHolder) holder).electricStates2.setText("过流");
                    }else if(Double.parseDouble(value72)-2<0){
                        ((ItemViewHolder) holder).electricStates2.setText("低流");
                    }else{
                        ((ItemViewHolder) holder).electricStates2.setText("正常");
                    }
                }
                break;
//            case 9:
//                String value97 = electricValueBeen.get(0).getValue();
//                String value92 = electricValueBeen.get(1).getValue();
//                String value93 = electricValueBeen.get(2).getValue();
//                if (value97.length() > 0) {
//                    ((ItemViewHolder) holder).tvImage.setVisibility(View.VISIBLE);
//                    ((ItemViewHolder) holder).electricLin.setVisibility(View.VISIBLE);
//                    ((ItemViewHolder) holder).electricName.setText("温度1");
//                    ((ItemViewHolder) holder).electricAlarmValue.setText("220V");
//                    ((ItemViewHolder) holder).electricCurrentValue.setText(value97);
//                    ((ItemViewHolder) holder).electricStates.setText("正常");}
//                if (value93.length() > 0) {
//                    ((ItemViewHolder) holder).tvImage2.setVisibility(View.VISIBLE);
//                    ((ItemViewHolder) holder).electricLin3.setVisibility(View.VISIBLE);
//                    ((ItemViewHolder) holder).electricAlarmValue3.setText("220V");
//                    ((ItemViewHolder) holder).electricName3.setText("温度3");
//                    ((ItemViewHolder) holder).electricCurrentValue3.setText(value93);
//                    ((ItemViewHolder) holder).electricStates3.setText("正常");
//                }
//                if (value92.length() > 0) {
//                    ((ItemViewHolder) holder).tvImage1.setVisibility(View.VISIBLE);
//                    ((ItemViewHolder) holder).electricLin2.setVisibility(View.VISIBLE);
//                    ((ItemViewHolder) holder).electricAlarmValue2.setText("220V");
//                    ((ItemViewHolder) holder).electricName2.setText("温度2");
//                    ((ItemViewHolder) holder).electricCurrentValue2.setText(value92);
//                    ((ItemViewHolder) holder).electricStates2.setText("正常");
//                }
//                break;
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
        @Bind(R.id.electric_name2)
        TextView electricName2;
        @Bind(R.id.electric_alarm_value2)
        TextView electricAlarmValue2;
        @Bind(R.id.electric_current_value2)
        TextView electricCurrentValue2;
        @Bind(R.id.electric_states2)
        TextView electricStates2;
        @Bind(R.id.electric_lin2)
        LinearLayout electricLin2;
        @Bind(R.id.electric_name3)
        TextView electricName3;
        @Bind(R.id.electric_alarm_value3)
        TextView electricAlarmValue3;
        @Bind(R.id.electric_current_value3)
        TextView electricCurrentValue3;
        @Bind(R.id.electric_states3)
        TextView electricStates3;
        @Bind(R.id.electric_lin3)
        LinearLayout electricLin3;
        @Bind(R.id.tv_image)
        TextView tvImage;
        @Bind(R.id.tv_image1)
        TextView tvImage1;
        @Bind(R.id.tv_image2)
        TextView tvImage2;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加数据
    public void addItem(List<ElectricValue> electrics) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        electrics.addAll(electricList);
        electricList.removeAll(electricList);
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<ElectricValue> electrics) {
        electricList.addAll(electrics);
        notifyDataSetChanged();
    }


}
