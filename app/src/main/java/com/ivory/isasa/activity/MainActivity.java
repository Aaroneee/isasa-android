package com.ivory.isasa.activity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.just.agentweb.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.bigkoo.alertview.AlertView;
import com.ivory.isasa.R;
import com.ivory.isasa.databinding.MainBinding;
import com.ivory.isasa.model.LinkEnum;
import com.ivory.isasa.util.SharedPreferencesUtil;
import com.just.agentweb.AgentWeb;
import com.leaf.library.StatusBarUtil;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private MainBinding mainBinding;

    private ValueCallback<Uri[]> uploadMessageAboveL;
    private  Uri photoUri;

    private String currentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = MainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        StatusBarUtil.setColor(this, getColor(R.color.white));
        StatusBarUtil.setDarkMode(this);
        String tenantCrop = SharedPreferencesUtil.getString(this, "tenantCrop");
        String empId = String.valueOf(SharedPreferencesUtil.getInt(this, "id"));
        boolean loginStatus = SharedPreferencesUtil.getBoolean(this, "loginStatus");

        //判断登陆状态
        if (!loginStatus) {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        AgentWeb agentWeb = AgentWeb.with(this)
                .setAgentWebParent(mainBinding.parentView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(0, 0)//使用默认进度条
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()
                .go(LinkEnum.VIEW_LINK.getLink());
        agentWeb.getWebCreator().getWebView().addJavascriptInterface(this, "androidMethod");
        agentWeb.getWebCreator().getWebView().setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                agentWeb.getJsAccessEntrace().quickCallJs("setLocalStorage", tenantCrop, empId);
            }
        });


    }

    private final WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            getPermissions();
            uploadMessageAboveL = filePathCallback;
            new AlertView("上传图片", null, "取消", null,
                    new String[]{"拍照", "从相册中选择"},
                    MainActivity.this, AlertView.Style.ActionSheet, (o, position) -> {
                        //相册
                        if (position == 1) {
                            Intent aIntent = new Intent();
                            aIntent.setType("image/*");
                            aIntent.setAction("android.intent.action.GET_CONTENT");
                            aIntent.addCategory("android.intent.category.OPENABLE");
                            startActivityForResult(aIntent, 100);
                        }
                        //相机
                        else if(position==0){
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File photoFile = null;

                            try {
                                photoFile = createImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (photoFile != null) {
                                photoUri = FileProvider.getUriForFile(MainActivity.this,
                                        "com.ivory.isasa.fileprovider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(takePictureIntent, 200);
                            }
                        }
                        //取消
                        else if(position==-1){
                            filePathCallback.onReceiveValue(null);
                        }
                    }).show();
            return true;
        }
    };

    /**
     * describe: 监听JS 退出登录方法
     *
     * @Author: Aaron
     * @Date: 2021/3/17 12:20
     */
    @JavascriptInterface
    public void logOut() {
        Toasty.success(this, "调用成功").show();
        SharedPreferencesUtil.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * describe: 禁止系统手势
     * 因为系统级 滑动返回在网页端会直接退出APP 所以禁止
     *
     * @Author: Aaron
     * @Date: 2021/3/17 12:19
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Uri[] results = null;
        if (requestCode==100){
            if (resultData != null) {
                String dataString = resultData.getDataString();
                ClipData clipData = resultData.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
            uploadMessageAboveL.onReceiveValue(results);
        }else if (requestCode==200){
            if (resultCode==-1){
                if (photoUri!=null){
                    results= new Uri[]{photoUri};
                }else if (currentPhotoPath!=null){
                    results= new Uri[]{Uri.parse(currentPhotoPath)};
                }
            }
            uploadMessageAboveL.onReceiveValue(results);
        }

        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void getPermissions(){
        XXPermissions.with(this)
                // 申请安装包权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 申请悬浮窗权限
                //.permission(Permission.SYSTEM_ALERT_WINDOW)
                // 申请通知栏权限
                //.permission(Permission.NOTIFICATION_SERVICE)
                // 申请系统设置权限
                //.permission(Permission.WRITE_SETTINGS)
                // 申请单个权限
                .permission(Permission.CAMERA)
                // 申请多个权限
//                .permission(Permission.Group.CALENDAR)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {

                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (permissions.contains(Permission.CAMERA)){
                            Toasty.success(MainActivity.this,"请授予APP相机权限,在上传图片时会用到").show();
                        }
                    }
                });
    }
}