package com.alenbeyond.spiderwebchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alenbeyond.spiderwebchart.view.SpiderWebChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SpiderWebChart spiderWebChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spiderWebChart = (SpiderWebChart) findViewById(R.id.spider_web_chart);
        spiderWebChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeData();
            }
        });
        changeData();
    }

    private void changeData() {
        List<Float> values = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            float value = rand.nextFloat();
            values.add(value);
        }
        spiderWebChart.setDatas(values);
    }
}
