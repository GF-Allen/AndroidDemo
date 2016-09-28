package com.example.customdialog.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.customdialog.R;

/**
 * Created by alen on 16/9/28.
 */

public class CusomDialog extends Dialog {
    public CusomDialog(Context context) {
        super(context,R.style.Dialog_Fullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉头
        Window window = getWindow();
        window.setContentView(R.layout.custom_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = WindowManager.LayoutParams.ALPHA_CHANGED;
        window.setAttributes(params);
        ColorDrawable dw = new ColorDrawable(0x000000); // 背景色
        window.setBackgroundDrawable(dw);
    }
}
