package com.itheima.bridgewebviewdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itheima.bridgewebviewdemo.view.BridgeWebView;


public class MainActivity extends AppCompatActivity {

    private BridgeWebView mWeview;
    private static final String LOT_URL = "\"file:///android_asset/BridgeWebView/index.html\"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setWebViewClient();
    }


    private void initView() {
        mWeview = (BridgeWebView) findViewById(R.id.weview);

        mWeview.addBridgeInterface(new JavaSctiptMethods(MainActivity.this, mWeview));//设置js和android通信桥梁方法
        //mWeview.loadUrl("http://10.0.3.2:8080/BridgeWebView/index.html");//显示网页,在线模板
        mWeview.loadUrl(LOT_URL);//本地模板

        initState();
        //mWeview.loadUrl(LOT_URL);


    }

    private void setWebViewClient() {

//        mWeview.setWebViewClient(new WebViewClient());
//        mWeview.setWebChromeClient(new WebChromeClient());
        mWeview.setWebChromeClient(new WebChromeClient());
        mWeview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("111","url:"+url);
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
  


    private void initState() {
        mWeview.canGoBack();
        mWeview.canGoForward();
        WebSettings settings = mWeview.getSettings();
        //设置支持JS代码
        settings.setJavaScriptEnabled(true);
        //设置自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        //缩放操作
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        settings.setAllowFileAccess(true); //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //获取当前的url
        Log.d("111","url:"+mWeview.getUrl());

        if (keyCode == KeyEvent.KEYCODE_BACK && mWeview.getUrl().equals(LOT_URL)) {
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && mWeview.canGoBack()) {
            mWeview.goBack();
            return true;//点击返回键 是返回上一个页面
        }

        if (mWeview.getUrl().equals(LOT_URL)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWeview.destroy();
    }

   /* @Override
    protected void onResume() {
        super.onResume();
       // initWebTokenData();//当页面进行显示的时候 重新初始化数据
        //mWeview.reload();//登录完毕之后 重新刷入当前页面
       // mWeview.onResume(); //置为活跃状态   执行webview的响应
    }*/
}
