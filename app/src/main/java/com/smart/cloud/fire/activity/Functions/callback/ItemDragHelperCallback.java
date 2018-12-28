package com.smart.cloud.fire.activity.Functions.callback;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

/***
 * ItemTouchHelper.Callback该方法实现左边侧滑删除、长按拖动功能
 */
public class ItemDragHelperCallback extends ItemTouchHelper.Callback {
    private OnItemMoveListener mOnItemMoveListener;
    private boolean mIsLongPressEnabled;

    public void setLongPressEnabled(boolean longPressEnabled) {
        mIsLongPressEnabled = longPressEnabled;
    }

    public interface OnItemMoveListener {
        boolean onItemMove(int fromPosition, int toPosition);
    }

    //相当于 set 设置监听 传入ChannelAdapter中的OnItemMoveListener对象
    public ItemDragHelperCallback(OnItemMoveListener onItemMoveListener) {
        mOnItemMoveListener = onItemMoveListener;
    }

    //返回true 允许拖拽
    @Override
    public boolean isLongPressDragEnabled() {
        return mIsLongPressEnabled;
    }

    //返回可以滑动的方向，比如说允许从右到左侧滑，允许上下拖动等
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //根据recyclerView的布局，进行设置拖拽的方向
        int dragFlags = setDragFlags(recyclerView);
        //不允许进行滑动
        int swipeFlags = 0;
        // 获取触摸响应的方向   包含两个 1.拖动dragFlags 2.侧滑删除swipeFlags
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    private int setDragFlags(RecyclerView recyclerView) {
        int dragFlags;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        return dragFlags;
    }

    //当用户拖动一个Item进行上下移动从旧的位置到新的位置的时候会调用该方法，在该方法内，
    // 我们可以调用Adapter的notifyItemMoved方法来交换两个ViewHolder的位置，最后返回true，
    // 表示被拖动的ViewHolder已经移动到了目的位置。所以，如果要实现拖动交换位置，可以重写该方法（前提是支持上下拖动）
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return mOnItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
        //当用户操作完毕某个item并且其动画也结束后会调用该方法，一般我们在该方法内恢复ItemView的初始状态，防止由于复用而产生的显示错乱问题。
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //当用户左右滑动Item达到删除条件时，会调用该方法，一般手指触摸滑动的距离达到RecyclerView宽度的一半时，
        // 再松开手指，此时该Item会继续向原先滑动方向滑过去并且调用onSwiped方法进行删除，否则会反向滑回原来的位置。
        //如果在onSwiped方法内我们没有进行任何操作，即不删除已经滑过去的Item，那么就会留下空白的地方，
        // 因为实际上该ItemView还占据着该位置，只是移出了我们的可视范围内罢了。
    }

    //拖动选择状态改变回调
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //从静止状态变为拖拽或者滑动的时候会回调该方法，参数actionState表示当前的状态。
        /**
         * ACTION_STATE_IDLE:拖拽或删除结束，这时 viewHolder 参数为 null 。
         * ACTION_STATE_DRAG:开始拖拽
         * ACTION_STATE_SWIPE:开始删除
         */
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // ItemTouchHelper.ACTION_STATE_IDLE 看看源码解释就能理解了
            // 侧滑或者拖动的时候背景设置为灰色
            viewHolder.itemView.setBackgroundColor(Color.GRAY);
        }
    }
}
