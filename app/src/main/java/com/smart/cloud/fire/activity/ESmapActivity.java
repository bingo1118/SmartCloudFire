package com.smart.cloud.fire.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.blankj.utilcode.util.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class ESmapActivity extends Activity {

    Context mContext;
    @Bind(R.id.webview)
    WebView webView;

    String mac;
    String buildId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esmap);
        ButterKnife.bind(this);
        mContext = this;

        mac=getIntent().getStringExtra("mac");
        buildId=getIntent().getStringExtra("buildId");

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可


        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        String url="";
        if(!StringUtils.isEmpty(mac)){
            url="http://139.199.58.208/fireSystem/tridimensionalMap?mac="+mac;
        }else if(!StringUtils.isEmpty(buildId)){
            url="http://139.199.58.208/fireSystem/tridimensionalMap?buildId="+buildId;
        }else{
            url="http://139.199.58.208/fireSystem/tridimensionalMap?buildId=1";
        }
        webView.loadUrl(url);
    }
}
