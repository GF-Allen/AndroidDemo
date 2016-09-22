package com.alenbeyond.systemmanager.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * Created by Alen on 2016/9/7.
 */
public class BaseManager implements ISystemAction {

    // ================================安卓通用设置==========================================

    private static final String PKG_ANDROID_SETTING = "com.android.settings";

    /**
     * 定位
     */
    private static final String TARGET_ANDROID_LOCATION = "com.android.settings.Settings$LocationSettingsActivity";

    private String mSystemType;

    public BaseManager(String mSystemType) {
        this.mSystemType = mSystemType;
    }

    @Override
    public void openSettingLocation(Context context) {
        openSettingForComp(context, PKG_ANDROID_SETTING, TARGET_ANDROID_LOCATION);
    }

    @Override
    public void openPermission(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    @Override
    public void openAutoStart(Context context) {

    }

    @Override
    public void openPowerMode(Context context) {

    }

    @Override
    public void openProtect(Context context) {

    }

    @Override
    public void openViewMonitor(Context context) {

    }

    public void openSettingForComp(Context context, String packageName, String target) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName(packageName, target);
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onException(mSystemType);
            }
        }
    }

    public void openSettingForIntent(Context context, String intentName) {
        Intent intent = new Intent(intentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private OnExceptionListener onExceptionListener;

    public interface OnExceptionListener {
        void onException(String systemType);
    }

    public void setOnExceptionListener(OnExceptionListener onExceptionListener) {
        this.onExceptionListener = onExceptionListener;
    }
}
