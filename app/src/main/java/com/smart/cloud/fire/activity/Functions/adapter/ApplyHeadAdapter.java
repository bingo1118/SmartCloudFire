package com.smart.cloud.fire.activity.Functions.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.smart.cloud.fire.activity.Functions.callback.ItemDragHelperCallback;
import com.smart.cloud.fire.activity.Functions.constant.Constant;
import com.smart.cloud.fire.activity.Functions.interfaces.LoadApplyInterface;
import com.smart.cloud.fire.activity.Functions.model.ApplyTable;
import com.smart.cloud.fire.activity.Functions.util.ACache;
import com.smart.cloud.fire.activity.Functions.util.ApplyTableManager;
import com.smart.cloud.fire.global.MyApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * 头部应用适配器
 */
public class ApplyHeadAdapter extends RecyclerView.Adapter<ApplyHeadAdapter.ChannelViewHolder> implements ItemDragHelperCallback.OnItemMoveListener {
    private final Context context;
    private final LayoutInflater inflater;
    private List<ApplyTable> mTables;
    private boolean edit = false;//编辑
    private ItemDragHelperCallback mItemDragHelperCallback;
    private ApplyAdapter.OnItemClickListenerEdit mOnItemClickListenerEeit;
    private ApplyAdapter.OnItemClickListener mOnItemClickListener;


    public ApplyHeadAdapter(Context context, List<ApplyTable> tables) {
        this.context = context;
        this.mTables = tables;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 添加数据
     *
     * @param tables 需要添加的数据
     */
    public void addData(List<ApplyTable> tables) {
        this.mTables = tables;
    }

    /**
     * 设置编辑状态
     *
     * @param edit
     */
    public void setEdit(boolean edit) {
        this.edit = edit;
        this.notifyDataSetChanged();
    }

    /**
     * 获取当前的编辑状态
     * @return 当前的编辑状态
     */
    public boolean getEdit() {
        return edit;
    }

    public void setItemDragHelperCallback(ItemDragHelperCallback itemDragHelperCallback) {
        mItemDragHelperCallback = itemDragHelperCallback;
    }

    /**
     * 设置状态编辑的点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListenerEdit(ApplyAdapter.OnItemClickListenerEdit onItemClickListener) {
        mOnItemClickListenerEeit = onItemClickListener;
    }
    /**
     * 设置状态编辑的点击事件
     * @param onItemClickListener
     */
    public void setOnItemClickListener(ApplyAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChannelViewHolder(inflater.inflate(R.layout.item_apply, parent, false));
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        final ApplyTable table = mTables.get(position);
        holder.mTextView.setText(table.getName());
        holder.mImageView.setImageBitmap(ApplyTableManager.getImageFromAssetsFile(MyApp.app,table.getImgRes()));
        holder.iv_add.setVisibility(View.VISIBLE);
        if (table.getState() == 0) {
            holder.iv_add.setImageResource(R.drawable.item_del);
            handleOnClick(holder, table);//设置点击监听
        }
        handleLongPress(holder, table);//设置长按监听
        if (edit) {
            holder.iv_add.setVisibility(View.GONE);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view,table.getName());
                }
            });
        } else {
            holder.mImageView.setOnClickListener(null);
        }
    }

    //处理点击事件
    private void handleOnClick(final ChannelViewHolder holder, final ApplyTable table) {
        if (mOnItemClickListener != null) {
            holder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!table.getFixed()) {//点击是否可以进行增删
                        //对项目点击后增删操作的监听
                        mOnItemClickListenerEeit.onItemClick(view, holder.getLayoutPosition());
                    }
                }
            });
        }
    }

    //处理长按事件 开启ItemDragHelperCallBack拖拽
    private void handleLongPress(ChannelViewHolder holder, final ApplyTable table) {
        if (mItemDragHelperCallback != null) {
            holder.mLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mItemDragHelperCallback.setLongPressEnabled(table.getIndex() == 0 ? false : true);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTables == null ? 0 : mTables.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (isChannelFixed(fromPosition, toPosition)) {
            return false;
        }
        //在我的频道子频道的移动
//        Collections.swap(getAdapterData(), fromPosition, toPosition);

        ApplyTable temp=getAdapterData().get(fromPosition);
        getAdapterData().remove(fromPosition);
        getAdapterData().add(toPosition,temp);

        notifyItemMoved(fromPosition, toPosition);
        //通知顺序变换，存储，设置频道顺序，以及显示的顺序

        ACache.get(MyApp.app).put(Constant.APPLY_MINE,(ArrayList<ApplyTable>) mTables);
//        EventBus.getDefault().post(new ChannelBean(getAdapterData()));
        return true;
    }

    //不能移动头条
    private boolean isChannelFixed(int fromPosition, int toPosition) {
        return fromPosition == 0 || toPosition == 0;
    }


    public class ChannelViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;//文字描述
        private ImageView mImageView, iv_add;//图片,添加删除按钮
        private RelativeLayout mLayout;//整个item布局

        public ChannelViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_channel_logo);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            mTextView = (TextView) itemView.findViewById(R.id.tv_channel_name);
            mLayout = (RelativeLayout) itemView.findViewById(R.id.rl_root);
        }
    }

    public List<ApplyTable> getAdapterData() {
        return mTables;
    }


    //状态编辑Item点击事件的监听接口
    public interface OnItemClickListenerEdit {
        void onItemClick(View view, int position);
    }
    //Item点击事件的监听接口
    public interface OnItemClickListener {
        void onItemClick(View view, String name);
    }
}
