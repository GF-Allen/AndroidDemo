package com.alenbeyond.systemmanager.utils;

import android.content.Context;

/**
 * Created by Alen on 2016/9/7.
 * <p/>
 * 华为调用系统设置
 */
public class HWSystemManager extends BaseManager {

    private static final String PKG_HUAWEI_SYSTEM_MANGER = "com.huawei.systemmanager";

    /**
     * 权限管理
     */
    private static final String TARGET_HUAWEI_PERMISSION_MANAGER = "com.huawei.permissionmanager.ui.MainActivity";

    /**
     * 受保护
     */
    private static final String TARGET_HUAWEI_PROTECT = "com.huawei.systemmanager.optimize.process.ProtectActivity";

    /**
     * 自启动
     */
    private static final String TARGET_HUAWEI_AUTO_START = "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity";

    /**
     * 性能模式
     */
    private static final String TARGET_HUAWEI_POWER_MODE = "com.huawei.systemmanager.power.ui.PowerSaveModeActivity";

    /**
     * 悬浮窗
     */
    private static final String TARGET_HUAWEI_VIEW_MONITOR = "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity";

    public HWSystemManager(String mSystemType) {
        super(mSystemType);
    }

    @Override
    public void openAutoStart(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_AUTO_START);
    }

    @Override
    public void openPowerMode(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_POWER_MODE);
    }

    @Override
    public void openPermission(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_PERMISSION_MANAGER);
    }

    @Override
    public void openProtect(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_PROTECT);
    }

    @Override
    public void openViewMonitor(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_VIEW_MONITOR);
    }
}
