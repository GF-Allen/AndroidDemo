package com.alenbeyond.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alenbeyond.linechart.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    private LineChartView lcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lcv = (LineChartView) findViewById(R.id.lcv);
    }

    public void click(View view) {
        lcv.setxValues(new String[]{"4月", "5月", "6月", "7月", "8月", "9月"})
                .setDatas(new int[]{600, 800, 400, 850, 900, 750})
                .invalidate();
    }
}
