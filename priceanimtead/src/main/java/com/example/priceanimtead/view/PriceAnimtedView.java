package com.example.priceanimtead.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.priceanimtead.R;

import java.util.ArrayList;
import java.util.List;

public class PriceAnimtedView extends View {
    private String mNumberString = "";

    private TextPaint mTextPaint;

    private float mTextWidth;
    private float mTextHeight;
    private float mTextSize = 0;
    private float mCharWidth;
    private float mExtraSpace;

    private int ANIM_TIME = 1000;
    private int mNumberColor = Color.BLACK;

    private List<ValueAnimator> animatorList = new ArrayList<>();
    private List<Integer> startNumber = new ArrayList<>();

    public PriceAnimtedView(Context context) {
        super(context);
        init(null, 0);
    }

    public PriceAnimtedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PriceAnimtedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PriceAnimtedView, defStyle, 0);
        mNumberColor = a.getColor(
                R.styleable.PriceAnimtedView_numberColor,
                mNumberColor);
        mExtraSpace = a.getDimension(
                R.styleable.PriceAnimtedView_charSpace,
                mExtraSpace);
        mTextSize = a.getDimension(
                R.styleable.PriceAnimtedView_textSize,
                mTextSize);
        if (a.hasValue(R.styleable.PriceAnimtedView_number)) {
            mNumberString = a.getString(
                    R.styleable.PriceAnimtedView_number);
        }
        a.recycle();

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mNumberColor);
        mTextWidth = mTextPaint.measureText(mNumberString);
        mCharWidth = mTextWidth / mNumberString.length();

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent;
        startNumber.clear();
        for (int i = 0; i < mNumberString.length(); i++) {
            int num = Integer.parseInt("" + mNumberString.charAt(i));
            startNumber.add(i, calcNum(num, 5));
        }
        int heightSize = (int) mTextSize;
        int widthSize = (int) mTextWidth + 5;
        setMeasuredDimension(widthSize, heightSize); // 重新设置控件大小
    }

    private int calcNum(int num, int i) {
        int result = 0;
        if (num + i > 9) {
            result = num + i - 10;
        } else if (num + i < 0) {
            result = num + i + 10;
        } else {
            result = num + i;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) mTextSize;
        }
        int widthSize = (int) mTextWidth + 5;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int contentWidth = getWidth() - paddingLeft - paddingRight;

        float startX = paddingLeft + (contentWidth - mTextWidth) / 2;
        startX -= mExtraSpace * (startNumber.size() - 1) / 2;
        float startY;

        for (int i = 0; i < startNumber.size(); i++) {
            float progress = 1;
            if (i < animatorList.size()) {
                progress = (float) animatorList.get(i).getAnimatedValue();
            }
            int dy = (int) ((mTextSize * 5) * progress);
            startY = mTextSize - mTextHeight / 2 - dy;
            Integer num = startNumber.get(i);
            for (int j = 0; j < 6; j++) {
                String c = "" + calcNum(num, -j);
                canvas.drawText(c,
                        startX,
                        startY,
                        mTextPaint);
                startY += mTextSize;

            }
            startX += mCharWidth + mExtraSpace;
        }

        boolean end = true;
        for (ValueAnimator valueAnimator : animatorList) {
            if (valueAnimator.isRunning()) {
                end = false;
            }
        }
        if (!end) {
            invalidate();
        }
    }

    public void play() {
        for (ValueAnimator valueAnimator : animatorList) {
            valueAnimator.cancel();
        }
        animatorList.clear();
        for (int i = 0; i < mNumberString.length(); i++) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(ANIM_TIME + 100 * i);
            valueAnimator.start();
            animatorList.add(valueAnimator);
        }
        invalidate();
    }

    public void setNumberText(String number) {
        mNumberString = number;
        invalidateTextPaintAndMeasurements();
    }

}
