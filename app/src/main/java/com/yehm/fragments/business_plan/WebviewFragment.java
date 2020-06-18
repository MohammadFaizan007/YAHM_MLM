package com.yehm.fragments.business_plan;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yehm.BuildConfig;
import com.yehm.R;
import com.yehm.constants.BaseFragment;


public class WebviewFragment extends BaseFragment {

    WebView web;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_fragment, container, false);

        web = view.findViewById(R.id.webView1);

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        web.clearCache(true);
        showLoading();
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(BuildConfig.BASE_URL_IMAGES + "Home/BusinessOpportunity");
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {

    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            hideLoading();
        }
    }

}
