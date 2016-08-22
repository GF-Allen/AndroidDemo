package com.alenbeyond.dashboardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alenbeyond.dashboardview.view.CustomDashboardView;

public class MainActivity extends AppCompatActivity {

    private CustomDashboardView customDashboardView;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customDashboardView = (CustomDashboardView) findViewById(R.id.dash);
        text = (EditText) findViewById(R.id.text);
    }

    public void click(View view) {
        String s = text.getText().toString();
        int score = Integer.parseInt(s);
        customDashboardView.setScore(score);
    }
}
