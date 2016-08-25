package com.alenbeyond.nestedscrollingdemo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.alenbeyond.nestedscrollingdemo.R;

/**
 * Created by Alen on 2016/8/15.
 */
@SuppressLint("NewApi")
public class NestedScrollLayout extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = "NestedScrollLayout";
    private NestedScrollingChildHelper mChildHelper;

    public NestedScrollLayout(Context context) {
        super(context);
        init();
    }

    public NestedScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        mChildHelper = new NestedScrollingChildHelper(this);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.e(TAG, "setNestedScrollingEnabled");
        super.setNestedScrollingEnabled(enabled);
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        Log.e(TAG, "startNestedScroll");
        return true;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling");
        return true;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll:" + "dx:" + dx + ":" + "dy:" + dy);
    }
}
