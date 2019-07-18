package com.smart.cloud.fire.view;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.smart.cloud.fire.activity.UploadNFCInfo.FileUtil;
import com.smart.cloud.fire.activity.UploadNFCInfo.FormFile;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.mvp.register.RegisterPhoneActivity;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.UploadUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fire.cloud.smart.com.smartcloudfire.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectPhotoView extends FrameLayout {

    Context mContext;
    ImageView imageView,clear_photo;
    private String imageFilePath;
    private Activity activity;
    File temp;

    public SelectPhotoView(@NonNull Context context) {
        super(context);
        initView(context);
    }


    public SelectPhotoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SelectPhotoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectPhotoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        mContext=context;
        imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/devimage.jpg";
        temp = new File(imageFilePath);
        deleteTempPhoto();
        if(isPhotoExist()){
            deleteTempPhoto();
        }
        View view=LayoutInflater.from(mContext).inflate(R.layout.select_photo_view,this,false);
        imageView=(ImageView)view.findViewById(R.id.photo_iv) ;
        clear_photo=(ImageView)view.findViewById(R.id.clear_photo) ;
        clear_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTempPhoto();
//                upload("123","oriImgs");
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity==null){
                    return;
                }
                if(!isPhotoExist()){
                    File temp = new File(imageFilePath);
                    Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
                    it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
                    activity.startActivityForResult(it, 102);
                }else{
                    //使用Intent
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(temp), "image/*");
                    mContext.startActivity(intent);
                }

            }
        });

        addView(view);

    }


    public boolean isPhotoExist(){
        if(temp.exists()){
            return  true;
        }else{
            return false;
        }
    }

    public void deleteTempPhoto(){
        File temp = new File(imageFilePath);
        if(temp.exists()){
            temp.delete();
            if(clear_photo!=null){
                clear_photo.setVisibility(GONE);
            }
            if(imageView!=null){
                imageView.setImageBitmap(null);
            }
        }
    }

    public boolean upload(String name,String location){
        File temp = new File(imageFilePath);
        if(isPhotoExist()){
            boolean isSuccess=UploadUtil.uploadFile(temp,"","",name,"",location);
            return isSuccess;
        }else{
            return false;
        }
    }

//    public boolean upload(String name,String location){
//        File temp = new File(imageFilePath);
//        if(isPhotoExist()){
//            Observable.create(new Observable.OnSubscribe<String>() {
//                @Override public void call(Subscriber<? super String> subscriber) {
////                    boolean isSuccess=UploadUtil.uploadFile(temp,name,location);
//                    boolean isSuccess=UploadUtil.uploadFile(temp,"","",System.currentTimeMillis()+"","",location);
//                    if(isSuccess){
//                        subscriber.onNext("图片上传成功");
//                    }else{
//                        subscriber.onNext("图片上传失败");
//                    }
//                }
//            }).subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<String>()
//                    {
//                        @Override public void onCompleted() {
//                        }
//                        @Override public void onError(Throwable e) {
//                        }
//                        @Override public void onNext(String s) {
//                            T.showShort(mContext,s);
//                        }
//                    });
//        }
//        return true;
//
//    }


    /**
     * 提供给外部调用用来再Activity返回时获取图片信息
     * @param data
     */
    public void onActivityResult(Intent data) {

        try {
            Bitmap bmp = BitmapFactory.decodeFile(imageFilePath);
            try {
                saveFile(compressBySize(Environment.getExternalStorageDirectory().getAbsolutePath()+"/devimage.jpg",1500,2000),Environment.getExternalStorageDirectory().getAbsolutePath()+"/devimage.jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth=dm.widthPixels;
            if(bmp.getWidth()<=screenWidth){
                imageView.setImageBitmap(bmp);
            }else{
                Bitmap mp=Bitmap.createScaledBitmap(bmp, screenWidth, bmp.getHeight()*screenWidth/bmp.getWidth(), true);
                imageView.setImageBitmap(mp);
            }
            clear_photo.setVisibility(VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    //@@10.12存储文件到sd卡
    public void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);//检测图片是否存在
        if(dirFile.exists()){
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));//100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    //@@10.12压缩图片尺寸
    public Bitmap compressBySize(String pathName, int targetWidth,
                                 int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);// 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }//设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }
}
