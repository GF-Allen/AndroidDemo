package com.alenbeyond.gpsstatus;

import android.content.Context;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getContentResolver()
                .registerContentObserver(
                        Settings.Secure
                                .getUriFor(android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED),
                        false, mGpsMonitor);
        tvStatus = (TextView) findViewById(R.id.tv_status);
//        tvStatus.setText(isGPSOpen(this) ? "开" : "关");
    }

    private boolean isGPSOpen(Context context) {
        boolean b = false;
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            b = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
        }
        return b;
    }

    /**
     * 用于监听GPS的状态变化
     */
    private final ContentObserver mGpsMonitor = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d("---",isGPSOpen(MainActivity.this) ? "GPS===>开" : "GPS===>关");
            tvStatus.setText(isGPSOpen(MainActivity.this) ? "开" : "关");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mGpsMonitor);
    }
}
