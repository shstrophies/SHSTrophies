package com.shs.trophiesapp;

import android.os.Bundle;

import com.shs.trophiesapp.databinding.ActivityReportBugBinding;
import com.shs.trophiesapp.utils.Constants;

import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ReportBugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityReportBugBinding binding = ActivityReportBugBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.content.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }

            //TODO: Perhaps add this back later, but for now it works
            /*@Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                if(url.equals(Constants.BUG_REPORT_FORM_END_URL)) {
                    startActivity(new Intent(ReportBugActivity.this, SportsActivity.class));
                }
                super.doUpdateVisitedHistory(view, url, isReload);
            }*/
        });
        binding.content.webview.loadUrl(Constants.BUG_REPORT_FORM_URL);
    }

}
