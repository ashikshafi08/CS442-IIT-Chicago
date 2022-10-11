package com.christopherhield.impliedintentwebbrowser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String cnnURL = "https://www.cnn.com";
    private static final String androidURL = "https://developer.android.com";
    private static final String googleURL = "https://www.google.com";
    private static final String tribURL = "https://www.chicagotribune.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickCNN(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(cnnURL));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public void clickAndroid(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(androidURL));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void clickGoogle(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleURL));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void clickTrib(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(tribURL));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void doStock(View v) {
        String symbol = ((EditText) findViewById(R.id.goToStock)).getText().toString();
        if (symbol.trim().isEmpty()) {
            return;
        }

        String url = "https://www.marketwatch.com/investing/stock/" + symbol;
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d(TAG, "onReceivedError: ");
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                Log.d(TAG, "onReceivedHttpError: " +
                        errorResponse.getStatusCode() + ": " +
                        errorResponse.getReasonPhrase() + " - " +
                        request.getUrl());
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d(TAG, "onReceivedSslError: " +
                        error.getUrl());
                super.onReceivedSslError(view, handler, error);
            }
        });


        webView.loadUrl(Uri.parse(url).toString());
    }
}
