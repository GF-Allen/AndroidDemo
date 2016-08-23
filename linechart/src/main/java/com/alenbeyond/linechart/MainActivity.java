package com.alenbeyond.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alenbeyond.linechart.view.LineChartView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LineChartView lcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lcv = (LineChartView) findViewById(R.id.lcv);
    }

    public void click(View view) {
        Random random = new Random();
        lcv.setDatas(new int[]{
                350 + random.nextInt(600),
                350 + random.nextInt(600),
                350 + random.nextInt(600),
                350 + random.nextInt(600),
                350 + random.nextInt(600),
                350 + random.nextInt(600)})
                .refresh();
    }
}
