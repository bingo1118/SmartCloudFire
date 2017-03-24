package com.smart.cloud.fire.view;

/**
 * Created by Administrator on 2016/8/1.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * 顶部indicator
 *
 *
 */
public class TopIndicator extends RelativeLayout {

    private static final String TAG = "TopIndicator";
    private int[] mDrawableIds = new int[] { R.drawable.yg_dh_tb_all,R.drawable.yg_dh_tb_dq,
            R.drawable.yg_dh_tb_zx, R.drawable.yg_dh_tb_lx};
    private List<CheckedTextView> mCheckedList = new ArrayList<>();
    private List<LinearLayout> mLinearLayout = new ArrayList<>();
    private List<TextView> mTextView = new ArrayList<>();
    private List<View> mViewList = new ArrayList<>();
    // 顶部菜单的文字数组
    private CharSequence[] mLabels = new CharSequence[] { "探测器", "电气火灾","视频", "故障"};
    private int mScreenWidth;
    private int mUnderLineWidth;
    private View mUnderLine;
    // 底部线条移动初始位置
    private int mUnderLineFromX = 0;

    @SuppressLint("NewApi")
    public TopIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TopIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        //setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.rgb(255, 255, 255));

        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mUnderLineWidth = mScreenWidth / mLabels.length;

        mUnderLine = new View(context);
        mUnderLine.setBackgroundResource(R.drawable.yg_dh_an);
        LayoutParams underLineParams = new LayoutParams(
                mUnderLineWidth, 14);

        // 标题layout
        LinearLayout topLayout = new LinearLayout(context);
        LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.width = mUnderLineWidth;
        params.gravity = Gravity.CENTER;

        int size = mLabels.length;
        for (int i = 0; i < size; i++) {
            final int index = i;
            final View view = inflater.inflate(R.layout.top_indicator_item,
                    null);
            final MyCheckedTextView itemName = (MyCheckedTextView) view
                    .findViewById(R.id.item_name);
            final LinearLayout itemLin = (LinearLayout) view
                    .findViewById(R.id.top_lin);
            final TextView tvTop = (TextView) view
                    .findViewById(R.id.top_tv);
            itemName.setCompoundDrawablesWithIntrinsicBounds(context
                            .getResources().getDrawable(mDrawableIds[i]), null, null,
                    null);
            itemName.setCompoundDrawablePadding(5);
            itemName.setText(mLabels[i]);
            topLayout.addView(view, params);
            //topLayout.addView(mUnderLine, underLineParams);
            itemName.setTag(index);

            mCheckedList.add(itemName);
            mLinearLayout.add(itemLin);
            mTextView.add(tvTop);
            mViewList.add(view);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != mTabListener) {
                        mTabListener.onIndicatorSelected(index);
                    }
                }
            });

            // 初始化 底部菜单选中状态,默认第一个选中
            if (i == 0) {
                itemName.setChecked(true);
                mLinearLayout.get(0).setBackgroundColor(Color.rgb(241, 241, 241));
            } else {
                itemName.setChecked(false);
            }

        }
        this.addView(topLayout, topLayoutParams);
        underLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(mUnderLine, underLineParams);

    }

    /**
     * 设置底部导航中图片显示状态和字体颜色
     */
    public void setTabsDisplay(Context context, int index) {
        int size = mCheckedList.size();
        for (int i = 0; i < size; i++) {
            CheckedTextView checkedTextView = mCheckedList.get(i);
            LinearLayout linearLayout = mLinearLayout.get(i);
            TextView textView = mTextView.get(i);
            if ((Integer) (checkedTextView.getTag()) == index) {
                checkedTextView.setChecked(true);
                linearLayout.setBackgroundColor(Color.rgb(241, 241, 241));
                textView.setVisibility(View.GONE);
            } else {
                checkedTextView.setChecked(false);
                linearLayout.setBackgroundColor(Color.rgb(255, 255, 255));
                textView.setVisibility(View.VISIBLE);
            }
        }
        // 下划线动画
        doUnderLineAnimation(index);
    }

    private void doUnderLineAnimation(int index) {
        TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX,
                index * mUnderLineWidth, 0, 0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(true);
        animation.setDuration(150);
        mUnderLine.startAnimation(animation);
        mUnderLineFromX = index * mUnderLineWidth;
    }

    // 回调接口
    private OnTopIndicatorListener mTabListener;

    public interface OnTopIndicatorListener {
        void onIndicatorSelected(int index);
    }

    public void setOnTopIndicatorListener(OnTopIndicatorListener listener) {
        this.mTabListener = listener;
    }

}

