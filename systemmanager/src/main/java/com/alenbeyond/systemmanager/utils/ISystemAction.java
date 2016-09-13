package com.alenbeyond.systemmanager.utils;

import android.content.Context;

/**
 * Created by Alen on 2016/9/7.
 */
public interface ISystemAction {
    /**
     * 自启动
     *
     * @param context
     */
    void openAutoStart(Context context);

    /**
     * 系统定位
     *
     * @param context
     */
    void openSettingLocation(Context context);

    /**
     * 性能模式
     *
     * @param context
     */
    void openPowerMode(Context context);

    /**
     * 权限设置
     *
     * @param context
     */
    void openPermission(Context context);

    /**
     * 受保护
     *
     * @param context
     */
    void openProtect(Context context);

    /**
     * 悬浮窗
     *
     * @param context
     */
    void openViewMonitor(Context context);
}
