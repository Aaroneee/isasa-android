package com.ivory.isasa.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ivory.isasa.R;
import com.ivory.isasa.databinding.MainBinding;
import com.ivory.isasa.model.LinkEnum;
import com.ivory.isasa.util.SharedPreferencesUtil;
import com.just.agentweb.AgentWeb;
import com.leaf.library.StatusBarUtil;
import com.nostra13.universalimageloader.utils.L;

import org.apache.commons.lang3.ClassUtils;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private MainBinding mainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = MainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        StatusBarUtil.setColor(this, getColor(R.color.white));
        StatusBarUtil.setDarkMode(this);
        String tenantCrop=SharedPreferencesUtil.getString(this,"tenantCrop");
        String empId=String.valueOf(SharedPreferencesUtil.getInt(this,"id"));
        boolean loginStatus=SharedPreferencesUtil.getBoolean(this,"loginStatus");
        //判断登陆状态
        if (!loginStatus) {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        AgentWeb agentWeb = AgentWeb.with(this)
                .setAgentWebParent(mainBinding.parentView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(0,0)//使用默认进度条
                .createAgentWeb()
                .ready()
                .go(LinkEnum.VIEW_LINK.getLink());
        agentWeb.getWebCreator().getWebView().addJavascriptInterface(this, "androidMethod");

        agentWeb.getWebCreator().getWebView().setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                agentWeb.getJsAccessEntrace().quickCallJs("setLocalStorage",tenantCrop,empId);
            }
        });



    }

    /**
     * describe: 监听JS 退出登录方法
     *
     * @Author: Aaron
     * @Date: 2021/3/17 12:20
     */
    @JavascriptInterface
    public void logOut(){
        Toasty.success(this,"调用成功").show();
        SharedPreferencesUtil.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
//        this.onBackPressed();
    }


    /**
     * describe: 禁止系统手势
     * 因为系统级 滑动返回在网页端会直接退出APP 所以禁止
     * @Author: Aaron
     * @Date: 2021/3/17 12:19
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }
}
