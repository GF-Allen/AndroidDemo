package com.alenbeyond.systemmanager;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alenbeyond.systemmanager.utils.SystemManager;

public class MainActivity extends AppCompatActivity {

    private TextView tvSystem;
    private SystemManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSystem = (TextView) findViewById(R.id.tv_system);
        tvSystem.setText("手机品牌" + Build.MANUFACTURER);
        manager = SystemManager.getInstance();
    }

    /**
     * 自启动
     *
     * @param view
     */
    public void autoStart(View view) {
        manager.openAutoStart(this);
    }

    /**
     * 定位
     *
     * @param view
     */
    public void location(View view) {
        manager.openSettingLocation(this);
    }

    /**
     * 性能模式
     *
     * @param view
     */
    public void performanceMode(View view) {
        manager.openPowerMode(this);
    }

    /**
     * 权限
     *
     * @param view
     */
    public void authority(View view) {
        manager.openPermission(this);
    }

    /**
     * 受保护
     *
     * @param view
     */
    public void Protect(View view) {
        manager.openProtect(this);
    }

    public void viewMonitor(View view) {
        manager.openViewMonitor(this);
    }
}
