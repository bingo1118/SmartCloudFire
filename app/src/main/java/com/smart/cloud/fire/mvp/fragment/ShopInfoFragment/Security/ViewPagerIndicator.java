package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security;




import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout {
	
	private Paint mPaint;
	private Path mpath;
	private int mTriangleWidth;
	private int mTriangleHeight;
	private int mTtranslationX;
	private ViewPager mViewPager;
	
	public void setmViewPager(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	private static final int COLOR_TEXT_HIGHT=0xfffff4a7;
	private static final int COLOR_TEXT_NORMAL=0xffcccbcb;
	
	

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		

		mPaint=new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#fff4a7"));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
		
	}
	
	
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		
		Path mpath1=new Path();
		mpath1.moveTo(0, 0);
		mpath1.lineTo(getWidth(), 0);
		mpath1.lineTo(getWidth(), -10);
		mpath1.lineTo(0, -10);
		mpath1.close();
		Paint mPaint1=new Paint();
		mPaint1.setStyle(Style.FILL);
		mPaint1.setPathEffect(new CornerPathEffect(3));
		mPaint1.setColor(Color.parseColor("#ff555555"));
		canvas.translate(0, getHeight()+2);
		canvas.drawPath(mpath1, mPaint1);
		canvas.restore();
		
		canvas.save();
		canvas.translate(mTtranslationX, getHeight()+2);
		canvas.drawPath(mpath, mPaint);
		canvas.restore();
		super.dispatchDraw(canvas);
	}

	public ViewPagerIndicator(Context context) {
		this(context, null);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth=(int) (w/4);
		initTriangle();
	}
	

	private void initTriangle() {
		
		mTriangleHeight=10;
		mpath=new Path();
		mpath.moveTo(0, 0);
		mpath.lineTo(mTriangleWidth, 0);
		mpath.lineTo(mTriangleWidth, -mTriangleHeight);
		mpath.lineTo(0, -mTriangleHeight);
		mpath.close();
	}


	public void scrollBy(int position, float arg1) {
		int tabWidth=getWidth()/4;
		mTtranslationX=(int)(tabWidth*(position+arg1));
		invalidate();
		hightLightTextView(position);
	}
	
	public void setPaintColor(int color){
		this.mPaint.setColor(color);
	}
	
	private void resetTextView(){
		for(int i=0;i<getChildCount();i++){
			LinearLayout view=(LinearLayout) getChildAt(i);
			if(view instanceof LinearLayout){
				for(int j=0;j<view.getChildCount();j++){
					View childview=view.getChildAt(j);
					if(childview instanceof TextView){
						((TextView)childview).setTextColor(COLOR_TEXT_NORMAL);
					}
				}
			}
		}
	}
	
	private void hightLightTextView(int pos){
		resetTextView();
		LinearLayout view=(LinearLayout) getChildAt(pos);
		if(view instanceof LinearLayout){
			for(int i=0;i<view.getChildCount();i++){
				View childview=view.getChildAt(i);
				if(childview instanceof TextView){
					((TextView)childview).setTextColor(COLOR_TEXT_HIGHT);
				}
			}
		}
	}
	
	public void setItemClickEvent(){
		int cCount=getChildCount();
		for(int i=0;i<cCount;i++){
			final int j=i;
			View view=getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

}
