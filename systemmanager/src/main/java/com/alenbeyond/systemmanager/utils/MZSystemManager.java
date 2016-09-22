package com.alenbeyond.systemmanager.utils;

import android.content.Context;

/**
 * Created by alen on 16/9/14.
 */
public class MZSystemManager extends BaseManager {

    private static final String PKG_MEIZU_SYSTEM_MANGER = "com.meizu.safe";

    public MZSystemManager(String mSystemType) {
        super(mSystemType);
    }

    @Override
    public void openAutoStart(Context context) {

    }

    @Override
    public void openPowerMode(Context context) {

    }

    /**
     * 受保护,允许后台运行
     * @param context
     */
    @Override
    public void openProtect(Context context) {
        openSettingForComp(context,PKG_MEIZU_SYSTEM_MANGER,"com.meizu.safe.powerui.AppPowerManagerActivity");
    }

    @Override
    public void openViewMonitor(Context context) {

    }
}
