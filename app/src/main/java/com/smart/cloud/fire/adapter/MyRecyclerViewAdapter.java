package com.smart.cloud.fire.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.cloud.fire.activity.Functions.model.ApplyTable;
import com.smart.cloud.fire.activity.Functions.util.ApplyTableManager;
import com.smart.cloud.fire.global.ItemBean;
import com.smart.cloud.fire.global.MyApp;

import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2018/12/7.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{
    private List<ApplyTable> mList;

    static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private MyItemClickListener mListener;

        View myView;
        ImageView imageView;
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }
        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            myView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    public MyRecyclerViewAdapter(List<ApplyTable> list){
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_main_notice_list,null);
        final ViewHolder holder = new ViewHolder(view,mItemClickListener);
        return holder;
    }

    //将数据绑定到控件上
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApplyTable bean = mList.get(position);
        holder.imageView.setImageBitmap(ApplyTableManager.getImageFromAssetsFile(MyApp.app,bean.getImgRes()));
        holder.title.setText(bean.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<ApplyTable> addMessageList) {
        //增加数据
        int position = mList.size();
        mList.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<ApplyTable> newList) {
        //刷新数据
        mList.removeAll(mList);
        mList.addAll(newList);
        notifyDataSetChanged();
    }

    private MyItemClickListener mItemClickListener;
    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

}
