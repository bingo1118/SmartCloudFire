
package com.lib.imagesee;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;


public class FileTouchImageView extends RelativeLayout {
	protected TouchImageView mImageView;
	protected Context mContext;
	private ImageLoader mImageLoader;

	public FileTouchImageView(Context ctx) {
		super(ctx);
		mContext = ctx;
		init();
	}

	public FileTouchImageView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		mContext = ctx;
		init();
	}

	public TouchImageView getImageView() {
		return mImageView;
	}

	protected void init() {
		mImageLoader = ImageTools.getImageLoaderInstance(mContext);
		mImageView = new TouchImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mImageView.setLayoutParams(params);
		this.addView(mImageView);
	}

	public void setScaleType(ScaleType scaleType) {
		mImageView.setScaleType(scaleType);
	}

	public void setUrl(String imagePath) {
		// mImageLoader.cancelDisplayTask(mImageView);
		mImageLoader.displayImage("file://" + imagePath, mImageView);
		// mImageLoader.displayImage(imagePath, mImageView,
		// new ImageLoadingListener() {
		//
		// @Override
		// public void onLoadingStarted(String arg0, View arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onLoadingFailed(String arg0, View arg1,
		// FailReason arg2) {
		// Log.i("imageLoder", "Error:" + arg2.toString());
		//
		// }
		//
		// @Override
		// public void onLoadingComplete(String arg0, View arg1,
		// Bitmap arg2) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onLoadingCancelled(String arg0, View arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

}
