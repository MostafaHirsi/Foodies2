package com.abstractcoders.mostafa.foodies.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.abstractcoders.mostafa.foodies.NavigationDrawerActivity;
import com.abstractcoders.mostafa.foodies.R;
import com.github.ybq.android.spinkit.SpinKitView;

/**
 * Created by Mostafa on 19/02/2016.
 */
public class DealsFragment extends Fragment {
    String tabName;
    ListView lv;
    SwipeRefreshLayout refreshLayout;
    View v;
    NavigationDrawerActivity activity;
    String webUrl;
    public SpinKitView spinKitView;
    private WebView mWebView;
    public DealsFragment(String tabName, NavigationDrawerActivity activity) {
        // Required empty public constructor
        this.tabName = tabName;
        this.activity = activity;
        webUrl = "https://www.groupon.co.uk/vouchers/food-and-drink";
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_deals, container, false);

        spinKitView = (SpinKitView) rootView.findViewById(R.id.spin_kit_web);
            refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
            refreshLayout.setRefreshing(true);
            v = rootView;
            mWebView = (WebView) rootView.findViewById(R.id.activity_deals_webview);
            mWebView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    refreshLayout.setRefreshing(false);
                    spinKitView.setVisibility(View.GONE
                    );
                }
            });
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.loadUrl(webUrl);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    spinKitView.setVisibility(View.VISIBLE);
                    mWebView.loadUrl(webUrl);
                }
            });

            return rootView;
    }



}
