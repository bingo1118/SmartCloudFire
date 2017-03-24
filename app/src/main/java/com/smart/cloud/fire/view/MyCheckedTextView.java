package com.smart.cloud.fire.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Administrator on 2016/8/6.
 */
public class MyCheckedTextView extends CheckedTextView {
    private int mDrawableSize;// xml文件中设置的大小
    
    public MyCheckedTextView(Context context) {
        this(context, null, 0);
    }

    public MyCheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyCheckedTextView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MyCheckedTextView_myCheckedTextViewDrawableSize:
                    mDrawableSize = a.getDimensionPixelSize(R.styleable.MyCheckedTextView_myCheckedTextViewDrawableSize, 50);
                    break;
                case R.styleable.MyCheckedTextView_myCheckeddrawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.MyCheckedTextView_myCheckeddrawableBottom:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.MyCheckedTextView_myCheckeddrawableRight:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.MyCheckedTextView_myCheckeddrawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default :
                    break;
            }
        }
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }
    
}
