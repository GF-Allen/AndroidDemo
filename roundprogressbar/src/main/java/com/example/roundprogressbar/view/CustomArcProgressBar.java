package com.example.roundprogressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.roundprogressbar.R;

/**
 * Created by alen on 16/9/26.
 */

public class CustomArcProgressBar extends View {
    protected static final int CIRCLE_START_ANGLE = 135; // 起始角度
    protected static final int CIRCLE_SWEEP_ANGLE = 270; // 展示角度
    protected static final float CIRCLE_GAP = 2.6f;

    private static final int CIRCLE_STROKE_WIDTH = 56; // 指示圆环的宽度

    protected int mDividerWidth = 32;
    protected int mCircleBackground, mCircleGray, mCircleGreen;

    private int mWidth = 0, mHeight = 0;

    public CustomArcProgressBar(Context context) {
        this(context, null);
    }

    public CustomArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCircleBackground = getResources().getColor(R.color.circle_background);
        mCircleGray = getResources().getColor(R.color.circle_gray);
        mCircleGreen = getResources().getColor(R.color.circle_green);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomArcProgressBar);
        mDividerWidth = (int) typedArray.getDimension(R.styleable.CustomArcProgressBar_circle_out_indicator_size, mDividerWidth);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(specSize, specSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = (int) (getViewRadius() - CIRCLE_GAP * mDividerWidth);

        RectF oval = new RectF(
                getCenterX() - radius,
                getCenterY() - radius,
                getCenterX() + radius,
                getCenterY() + radius);

        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        circlePaint.setColor(mCircleGray);
        canvas.drawArc(oval, CIRCLE_START_ANGLE, CIRCLE_SWEEP_ANGLE, false, circlePaint);

        if (mEndIndicator > mStartIndicator && mProgress >= mStartIndicator) {
            circlePaint.setColor(mCircleGreen);
            float angle = CIRCLE_SWEEP_ANGLE * (mProgress - mStartIndicator) / (mEndIndicator - mStartIndicator);
            canvas.drawArc(oval, CIRCLE_START_ANGLE, angle, false, circlePaint);
        }
    }

    protected float mStartIndicator = 0f, mEndIndicator = 30f;
    private float mProgress;


    protected int getCenterX() {
        return mWidth / 2;
    }

    protected int getCenterY() {
        return mHeight / 2;
    }


    /**
     * 获取View的半径
     *
     * @return view的半径
     */
    protected int getViewRadius() {
        return getCenterY() > getCenterX() ? getCenterX() : getCenterY();
    }

    public void setIndicatorValue(float start, float end, float progress) {
        if (start >= end) {
            return;
        }
        if (progress > end || progress < start) {
            return;
        }

        mStartIndicator = start;
        mEndIndicator = end;
        mProgress = progress;
    }
}
