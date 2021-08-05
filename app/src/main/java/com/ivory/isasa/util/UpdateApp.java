package com.ivory.isasa.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.azhon.appupdate.manager.DownloadManager;
import com.bigkoo.alertview.AlertView;
import com.ivory.isasa.R;
import com.ivory.isasa.model.AndroidVersion;
import com.ivory.isasa.model.LinkEnum;
import com.ivory.isasa.model.ResultInfo;

import java.util.Map;

public class UpdateApp {
    Context context;
    Activity activity;
    AndroidVersion androidVersion;

    public UpdateApp(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        getInfo();
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            ResultInfo resultInfo = (ResultInfo) msg.obj;
            if (resultInfo != null&&resultInfo.getCode().equals(200)) {
                androidVersion= JsonUtil.jsonStr2Object(JsonUtil.object2JsonStr(resultInfo.getData()),AndroidVersion.class);
                updateApp();
            } else {
                new AlertView.Builder().setContext(context).setStyle(AlertView.Style.Alert).setTitle("检查更新有误").setOthers(new String[]{"下载"})
                        .setOnItemClickListener((o, position) -> {
                            DownloadManager manager = DownloadManager.getInstance(context);
                            manager.setApkName("ivory.apk")
                                    .setApkVersionName(androidVersion.getUpdateLog())
                                    .setApkUrl(LinkEnum.APK_LINk.getLink())
                                    .setSmallIcon(R.mipmap.circular_logo)
                                    .download();
                        }).build().show();
            }
            return false;
        }
    });

    public void updateApp() {
        System.out.println("androidVersion.getVersionCode() = " + androidVersion.getVersionCode());
        System.out.println("getAppVersionName(context) = " + getAppVersionName(context));
        int newVersionCode = Integer.parseInt(androidVersion.getVersionCode().replace(".", ""));
        int versionCode = Integer.parseInt(getAppVersionName(context).replace(".", ""));
//        String[] updateLog = androidVersion.getUpdateLog().split("-");
        androidVersion.setUpdateLog(androidVersion.getUpdateLog().replace("-", "\n"));
        String msg = "当前版本 : " + getAppVersionName(context) + "\n"
                + "最新版本 : " + androidVersion.getVersionCode() + "\n"
                + "更新内容 : \n"
                + androidVersion.getUpdateLog();
        if (newVersionCode > versionCode) {
            new AlertView.Builder().setContext(context).setStyle(AlertView.Style.Alert).setTitle("检查更新").setMessage(msg).setOthers(new String[]{"下载更新"})
                    .setOnItemClickListener((o, position) -> {
                        DownloadManager manager = DownloadManager.getInstance(context);
                        manager.setApkName("ivory.apk")
                                .setApkVersionName(androidVersion.getUpdateLog())
                                .setApkUrl(LinkEnum.APK_LINk.getLink())
                                .setSmallIcon(R.mipmap.circular_logo)
                                .download();
                    }).build().show();


        }

    }

    //查询版本信息
    public Map<String, Object> getInfo() {
        new Thread(()-> {
            Message message = new Message();
            message.what = 1;
                String result = RequestUtil.requestGet(LinkEnum.INTERFACE_LINK.getLink() + "/login/queryNewAndroidVersion", null);
                if (result != null) {
                    message.obj = JsonUtil.jsonStr2Object(result, ResultInfo.class);
                } else {
                    message.obj = null;
                }
                handler.sendMessage(message);
        }).start();

        return null;
    }

    //获取当前版本号 1
    public long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }

    //获取当前版本名 1.0.5
    public String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }
}
