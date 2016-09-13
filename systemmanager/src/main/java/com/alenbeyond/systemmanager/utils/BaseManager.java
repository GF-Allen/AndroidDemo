package com.alenbeyond.systemmanager.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Alen on 2016/9/7.
 */
public abstract class BaseManager implements ISystemAction {

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
