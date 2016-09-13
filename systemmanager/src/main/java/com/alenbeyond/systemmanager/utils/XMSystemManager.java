package com.alenbeyond.systemmanager.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by Alen on 2016/9/7.
 * <p/>
 * 小米系统调用设置
 */
public class XMSystemManager extends BaseManager {

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

    public XMSystemManager(String mSystemType) {
        super(mSystemType);
    }

    @Override
    public void openAutoStart(Context context) {
        openSettingForComp(context, PKG_MIUI_SECURITY_CENTER, TARGET_MIUI_AUTO_START);
    }

    @Override
    public void openPowerMode(Context context) {

    }

    @Override
    public void openPermission(Context context) {
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

    @Override
    public void openProtect(Context context) {
//        openSettingForIntent(context, INTENT_MIUI_POWER_HIDE_MODE);
        openSettingForComp(context,"com.miui.powerkeeper","com.miui.powerkeeper.ui.PowerHideModeActivity");
    }

    @Override
    public void openViewMonitor(Context context) {

    }
}
