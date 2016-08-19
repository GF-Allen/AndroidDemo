package com.alenbeyond.nestedscrollingdemo.view;

import android.content.Context;
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
public class NestedScrollLayout extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = "NestedScrollLayout";
    private RelativeLayout mTop;
    private RecyclerView recyclerView;
    private int mTopHeight;
    private Scroller mScroller;

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

    private void init() {
        mScroller = new Scroller(getContext());
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return true;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed)
    {
        Log.e(TAG, "onNestedFling");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY)
    {
        Log.e(TAG, "onNestedPreFling");
        if (getScrollY() >= mTopHeight) return false;
        fling((int) velocityY);
        return true;
    }

    private void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopHeight);
        invalidate();
    }

    @Override
    public int getNestedScrollAxes()
    {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = (RelativeLayout) findViewById(R.id.rl_top);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTop.getMeasuredHeight() + recyclerView.getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopHeight = mTop.getMeasuredHeight();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopHeight) {
            y = mTopHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "dx:" + dx + ":" + "dy:" + dy);
        boolean hiddenTop = dy > 0 && getScrollY() < mTopHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }
}
