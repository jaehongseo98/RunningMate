package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class UserUpdateActivity extends AppCompatActivity {

    Button updatepw, btnjuso;
    private WebView webView;
    private TextView txt_address;
    private Handler handler;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        txt_address = (TextView)findViewById(R.id.txt_address);
        updatepw = (Button)findViewById(R.id.updatepw);
        btnjuso = (Button)findViewById(R.id.btnjuso);


        btnjuso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WebView 초기화
                init_webView();

                // 핸들러를 통한 JavaScript 이벤트 반응
                handler = new Handler();
            }
        });




        updatepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUpdateActivity.this, DBPWUPActivity.class);
                startActivity(intent);
            }
        });
    }

    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webView_address);

//        webView.setWebViewClient(new WebViewClient());
//
//        webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());

        // webview url load. php 파일 주소
        webView.loadUrl("http://localhost:8080/daumjuso.html");

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    txt_address.setText(String.format("(%s) %s %s", arg1, arg2, arg3));

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }
}