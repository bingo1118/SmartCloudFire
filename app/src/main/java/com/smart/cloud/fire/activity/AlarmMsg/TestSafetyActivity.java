package com.smart.cloud.fire.activity.AlarmMsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.smart.cloud.fire.global.ConstantValues;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class TestSafetyActivity extends Activity {

    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.video_view)
    VideoView videoview;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.safety_progressbar)
    ProgressBar safety_progressbar;
    @Bind(R.id.video_lin)
    RelativeLayout video_lin;
    @Bind(R.id.text_lin)
    LinearLayout text_lin;
    @Bind(R.id.progress_lin)
    RelativeLayout progress_lin;
    @Bind(R.id.progress_text)
    TextView progress_text;


    String filepath;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_safety);
        ButterKnife.bind(this);
        mContext=this;
        progress_lin.setVisibility(View.VISIBLE);


        filepath=getIntent().getStringExtra("filepath");
        title_tv.setText("资源详情");

        String fileType=getExtensionName(filepath);
        if(fileType.equals("jpg")){

            webView.getSettings().setDefaultTextEncodingName("gbk");
            // 开启支持视频
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setGeolocationEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    safety_progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    safety_progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    safety_progressbar.setVisibility(View.GONE);
                }
            });
            webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.loadUrl(filepath);

        }else if (fileType.equals("mp4")){
            video_lin.setVisibility(View.VISIBLE);
            videoview.setVideoURI(Uri.parse(filepath));
            MediaController mediaController=new MediaController(this);
            mediaController.show();
            videoview.setMediaController(new MediaController(this));
            videoview.requestFocus();
            videoview.start();
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    safety_progressbar.setVisibility(View.GONE);
                }
            });
        }
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
