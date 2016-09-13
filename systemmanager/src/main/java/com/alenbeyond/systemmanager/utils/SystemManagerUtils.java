package com.alenbeyond.systemmanager.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

/**
 * 不同系统的权限设置
 * <p/>
 * Created by Alen on 2016/8/24.
 */
public class SystemManagerUtils {

    // ================================安卓通用设置==========================================

    private static final String PKG_ANDROID_SETTING = "com.android.settings";

    /**
     * 定位
     */
    private static final String TARGET_ANDROID_LOCATION = "com.android.settings.Settings$LocationSettingsActivity";

    public static void openSettingLocation(Context context) {
        openSettingForComp(context, PKG_ANDROID_SETTING, TARGET_ANDROID_LOCATION);
    }

    //==================================安卓通用设置==========================================

    // ====================================华为===============================================

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
     * 打开华为权限管理
     *
     * @param context
     */
    public static void openHuaweiPermissionManager(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_PERMISSION_MANAGER);
    }


    /**
     * 悬浮窗
     */
    private static final String TARGET_HUAWEI_VIEW_MONITOR = "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity";

    /**
     * 打开华为受保护
     *
     * @param context
     */
    public static void openHuaweiProtect(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_PROTECT);
    }

    /**
     * 打开华为自启动
     *
     * @param context
     */
    public static void openHuaweiAutoStart(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_AUTO_START);
    }

    /**
     * 打开华为悬浮窗
     *
     * @param context
     */
    public static void openHuaweiViewMonitor(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_VIEW_MONITOR);
    }

    /**
     * 打开华为性能模式
     *
     * @param context
     */
    public static void openHueweiPowerMode(Context context) {
        openSettingForComp(context, PKG_HUAWEI_SYSTEM_MANGER, TARGET_HUAWEI_POWER_MODE);
    }

    //==========================================华为==========================================

    // ==========================================MIUI==========================================

    private static final String PKG_MIUI_SECURITY_CENTER = "com.miui.securitycenter";

    private static final String PKG_MIUI_POWER_KEEPER = "com.miui.powerkeeper";

    /**
     * MIUI自启动
     */
    private static final String TARGET_MIUI_AUTO_START = "com.miui.permcenter.autostart.AutoStartManagementActivity";

    /**
     * 神隐模式
     */
    private static final String INTENT_MIUI_POWER_HIDE_MODE = "miui.intent.action.POWER_HIDE_MODE_ACTIVITY";

    /**
     * 权限
     */
    private static final String TARGET_MIUI_PERMISSON = "com.miui.permcenter.permissions.AppPermissionsEditorActivity";

    private static final String INTENT_MIUI_PERM_EDITOR = "miui.intent.action.APP_PERM_EDITOR";

    /**
     * Miui自启动
     */
    public static void openMiuiAutoStart(Context context) {
        openSettingForComp(context, PKG_MIUI_SECURITY_CENTER, TARGET_MIUI_AUTO_START);
    }

    /**
     * MIUI神隐模式
     *
     * @param context
     */
    public static void openMiuiPowerHide(Context context) {
        openSettingForIntent(context, INTENT_MIUI_POWER_HIDE_MODE);
    }

    /**
     * miui权限设置
     *
     * @param context
     */
    public static void openMiuiPermission(Context context) {
        // 之兼容miui v5/v6  的应用权限设置页面，否则的话跳转应用设置页面（权限设置上一级页面）
        try {
            Intent localIntent = new Intent(INTENT_MIUI_PERM_EDITOR);
            localIntent.setClassName(PKG_MIUI_SECURITY_CENTER, TARGET_MIUI_PERMISSON);
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }
    }

    // ========================================MIUI=================================================

    private static void openSettingForComp(Context context, String packageName, String target) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName(packageName, target);
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "异常异常", Toast.LENGTH_SHORT).show();
        }
    }

    private static void openSettingForIntent(Context context, String intentName) {
        Intent intent = new Intent(intentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
