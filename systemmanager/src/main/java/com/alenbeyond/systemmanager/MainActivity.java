package com.alenbeyond.systemmanager;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alenbeyond.systemmanager.utils.SystemManagerUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 自启动
     *
     * @param view
     */
    public void autoStart(View view) {
        SystemManagerUtils.openHuaweiAutoStart(this);
        SystemManagerUtils.openMiuiAutoStart(this);
    }

    /**
     * 定位
     *
     * @param view
     */
    public void location(View view) {
        SystemManagerUtils.openSettingLocation(this);
    }

    /**
     * 性能模式
     *
     * @param view
     */
    public void performanceMode(View view) {
        SystemManagerUtils.openHueweiPowerMode(this);
    }

    /**
     * 权限
     *
     * @param view
     */
    public void authority(View view) {
        SystemManagerUtils.openHuaweiPermissionManager(this);
        SystemManagerUtils.openMiuiPermission(this);
    }

    /**
     * 受保护
     *
     * @param view
     */
    public void Protect(View view) {
        SystemManagerUtils.openHuaweiProtect(this);
        SystemManagerUtils.openMiuiPowerHide(this);
    }

    public void viewMonitor(View view) {
        SystemManagerUtils.openHuaweiViewMonitor(this);
    }
}
