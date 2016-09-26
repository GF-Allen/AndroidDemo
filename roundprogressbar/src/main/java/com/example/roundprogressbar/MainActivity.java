package com.example.roundprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.roundprogressbar.view.CustomArcProgressBar;

public class MainActivity extends AppCompatActivity {


    int i;
    private CustomArcProgressBar cp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cp1 = (CustomArcProgressBar) findViewById(R.id.cp_1);
        cp1.setIndicatorValue(0f, 30f, 22.4f);
    }
}
