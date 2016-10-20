package com.example.deviceIddemo;

import android.content.Context;
import android.graphics.Path;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvImei;

    private static String PATH = Environment.getExternalStorageDirectory()+"/Android/data/.system_config";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvImei = (TextView) findViewById(R.id.tv_imei);
    }

    public void click(View view) {
        String imei = FileUtil.readFileContent(PATH);
        if (TextUtils.isEmpty(imei)) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                FileUtil.writeFile(PATH, imei, false);
            }
        }
        tvImei.setText(imei);
    }
}
