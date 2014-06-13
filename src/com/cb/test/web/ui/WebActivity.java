package com.cb.test.web.ui;

import com.cb.R;
import com.cb.utils.BaseActivity;
import com.cb.utils.LogUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 加载web页面
 */
public class WebActivity extends BaseActivity
{
    public static final String LINK = "link";

    private WebView mWebView;

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        link = getIntent().getStringExtra(LINK);
        if (link == null)
        {
            finish();
            return;
        }

        setContentView(R.layout.web_activity);
        mWebView = (WebView) findViewById(R.id.webView);
        initWebView();
    }

    @Override
    public void onBackPressed()
    {
        if (mWebView != null && mWebView.canGoBack())
        {
            mWebView.goBack();
        }
        else
        {
            this.finish();
            super.onBackPressed();
        }

    }

    private void initWebView()
    {

        mWebView.getSettings().setJavaScriptEnabled(true);// 可用JS
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // 使用当前WebView处理跳转
                view.loadUrl(url);
                return true;// true表示此事件在此处被处理，不需要再广播
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                showProgressDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                dismissProgressDialog();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                LogUtils.info("onReceivedSslError");
                // super.onReceivedSslError(view, handler, error);

                // Android webview访问HTTPS web page忽略验证 .
                // handler.cancel(); // Android默认的处理方式
                handler.proceed(); // 接受所有网站的证书
                // handleMessage(Message msg); // 进行其他处理
            }
        });

        // 开启 HTML5 Web Storage 功能
        // the WebView should use the DOM storage API
        mWebView.getSettings().setDomStorageEnabled(true);

        // 不设置setDatabasePath，html5数据只会保存在内存
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDatabasePath("/data/data/" + mWebView.getContext().getPackageName() + "/databases/");

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上

        // 播放 JS 对象
        mWebView.addJavascriptInterface(null, "javascript");

        mWebView.getSettings().setSupportZoom(false);
        mWebView.loadUrl(link);

        // 加入下载支持
        mWebView.setDownloadListener(new DownloadListener()
        {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                    long contentLength)
            {
                LogUtils.error(url + "," + userAgent + "," + contentDisposition + "," + mimetype + "," + contentLength);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private static final int LOAD_START = 0;

    private static final int LOAD_OVER = 1;

    protected void showProgressDialog()
    {
        handler.sendEmptyMessage(LOAD_START);
    }

    protected void dismissProgressDialog()
    {
        handler.sendEmptyMessage(LOAD_OVER);
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case LOAD_START:
                    // 开始加载
                    findViewById(R.id.progress).setVisibility(View.VISIBLE);// 显示进度对话框
                    break;

                case LOAD_OVER:
                    // 加载完毕
                    findViewById(R.id.progress).setVisibility(View.GONE);
                    break;
            }
        }
    };
}
