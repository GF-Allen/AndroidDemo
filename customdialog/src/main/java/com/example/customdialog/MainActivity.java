package com.example.customdialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.customdialog.view.CusomDialog;

public class MainActivity extends AppCompatActivity {

    private CusomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        if (dialog == null) {
            dialog = new CusomDialog(this);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}
