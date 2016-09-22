package com.alenbeyond.systemmanager.utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by Alen on 2016/9/7.
 */
public class SystemManager implements ISystemAction, BaseManager.OnExceptionListener {

    private static final String HW = "HUAWEI";
    private static final String XM = "xiaomi";
    private static final String MZ = "Meizu";

    private static SystemManager mSystemManager;

    private BaseManager mManager;

    private BaseManager.OnExceptionListener onExceptionListener;

    private SystemManager() {
        if (Build.MANUFACTURER.equalsIgnoreCase(HW)) {
            mManager = new HWSystemManager(HW);
        } else if (Build.MANUFACTURER.equalsIgnoreCase(XM)) {
            mManager = new XMSystemManager(XM);
        }else if (Build.MANUFACTURER.equalsIgnoreCase(MZ)){
            mManager = new MZSystemManager(MZ);
        } else {
            mManager = new BaseManager(Build.MANUFACTURER);
        }
        mManager.setOnExceptionListener(this);
    }

    public static SystemManager getInstance() {
        if (mSystemManager == null) {
            mSystemManager = new SystemManager();
        }
        return mSystemManager;
    }

    public void setOnExceptionListener(BaseManager.OnExceptionListener onExceptionListener) {
        this.onExceptionListener = onExceptionListener;
    }

    @Override
    public void openAutoStart(Context context) {
        mManager.openAutoStart(context);
    }

    @Override
    public void openSettingLocation(Context context) {
        mManager.openSettingLocation(context);
    }

    @Override
    public void openPowerMode(Context context) {
        mManager.openPowerMode(context);
    }

    @Override
    public void openPermission(Context context) {
        mManager.openPermission(context);
    }

    @Override
    public void openProtect(Context context) {
        mManager.openProtect(context);
    }

    @Override
    public void openViewMonitor(Context context) {
        mManager.openViewMonitor(context);
    }

    @Override
    public void onException(String systemType) {
        if (onExceptionListener != null) {
            onExceptionListener.onException(systemType);
        }
    }
}
