package com.alenbeyond.spiderwebchart.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.alenbeyond.spiderwebchart.R;

import java.util.ArrayList;
import java.util.List;

public class SpiderWebChart extends View {

    private static final int DEFAULT_NUM_DATA = 5;

    private static final int DEFAULT_NUM_SCALE = 0;

    private static final float DEFAULT_LINE_STROKE_WIDTH = 3.0f;

    private static final float DEFAULT_SCALE_LENGTH = 10;

    private static final int DEFAULT_CHART_COLOR = 0x66FF0000;

    private static final boolean DEFAULT_IS_ANIMATED = true;

    private static final int DEFAULT_DURATION = 300;

    private static final float DEFAULT_TEXT_SIZE = 30;

    private static final float DEFAULT_LABEL_PADDING = 100;

    private static final float DEFAULT_CENTER_TEXT_SIZE = 100;

    private static final int DEFAULT_TEXT_COLOR = 0xFF000000;

    private static final int DEFAULT_LINE_COLOR = 0xFF000000;

    private Paint mFramePaint;

    private Paint mChartPaint;

    private Paint mTextPaint;

    private Paint mBitmapPaint;

    private Paint mCenterTextPaint;

    private int mNumData;

    private int mNumScale;

    private float mLineStrokeWidth;

    private float mScaleLength;

    private int mTextColor;

    private float mTextSize;

    private int mLineColor;

    private int mChartColor;

    private boolean mIsAnimated;

    private List<Float> mDatas;

    private List<Float> mPrevValues;

    private List<String> mLabels;

    private ValueAnimator mAnimator;

    private String mCenterText;

    private float mCenterTextSize;

    private int mDuration;
    //图片
    private List<Integer> mDrawables;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }

    public SpiderWebChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SpiderWebChart);
        mNumData = array.getInteger(R.styleable.SpiderWebChart_numData, DEFAULT_NUM_DATA);
        mNumScale = array.getInteger(R.styleable.SpiderWebChart_numScale, DEFAULT_NUM_SCALE);
        mLineStrokeWidth = array.getFloat(R.styleable.SpiderWebChart_lineStrokeWidth, DEFAULT_LINE_STROKE_WIDTH);
        mScaleLength = array.getFloat(R.styleable.SpiderWebChart_scaleLength, DEFAULT_SCALE_LENGTH);
        mChartColor = array.getColor(R.styleable.SpiderWebChart_chartColor, DEFAULT_CHART_COLOR);
        mIsAnimated = array.getBoolean(R.styleable.SpiderWebChart_isAnimated, DEFAULT_IS_ANIMATED);
        mDuration = array.getInteger(R.styleable.SpiderWebChart_duration, DEFAULT_DURATION);
        mCenterText = array.getString(R.styleable.SpiderWebChart_centerText);
        mCenterTextSize = array.getDimension(R.styleable.SpiderWebChart_centerTextSize, DEFAULT_CENTER_TEXT_SIZE);

        mTextColor = array.getColor(R.styleable.SpiderWebChart_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = array.getDimension(R.styleable.SpiderWebChart_textSize, DEFAULT_TEXT_SIZE);
        mLineColor = array.getColor(R.styleable.SpiderWebChart_lineColor, DEFAULT_LINE_COLOR);

        mDatas = new ArrayList<>();
        for (int i = 0; i < mNumData; i++) {
            mDatas.add(0.0f);
        }

        initPaint();

    }

    private void initPaint() {

        //网格
        if (mFramePaint == null) {
            mFramePaint = new Paint();
            mFramePaint.setColor(mLineColor);
            mFramePaint.setAntiAlias(true);
            mFramePaint.setStrokeWidth(mLineStrokeWidth);
        }

        //遮罩
        if (mChartPaint == null) {
            mChartPaint = new Paint();
            mChartPaint.setColor(mChartColor);
            mChartPaint.setStyle(Paint.Style.FILL);
            mChartPaint.setAntiAlias(true);
        }

        //周围文字
        if (mTextPaint == null) {
            mTextPaint = new Paint();
            mTextPaint.setColor(mTextColor);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setAntiAlias(true);
        }

        //中央文字
        if (mCenterTextPaint == null) {
            mCenterTextPaint = new Paint();
            mCenterTextPaint.setColor(mTextColor);
            mCenterTextPaint.setTextSize(mCenterTextSize);
            mCenterTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mCenterTextPaint.setTextAlign(Paint.Align.CENTER);//文字剧中
            mCenterTextPaint.setAntiAlias(true);
        }

        if (mBitmapPaint == null) {
            mBitmapPaint = new Paint();
            mBitmapPaint.setAntiAlias(true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float maxLabelLength = 0;
        if (mLabels != null) {
            for (String label : mLabels) {
                int labelLength = label.length();
                if (labelLength > maxLabelLength) {
                    maxLabelLength = labelLength;
                }
            }
        }
        maxLabelLength *= mTextSize;

        float width = getWidth();
        float height = getHeight();
        float centerX = width / 2;
        float centerY = height / 2;
        float r = ((width > height) ? (height - maxLabelLength) / 2 : (width - maxLabelLength) / 2) * 0.8f;
        float uRad = (float) (2.0f * Math.PI / mNumData);
        float rad = -(float) (2.0f * Math.PI / 4) - uRad;


        Path path = null;

        for (int i = 0; i < mNumData; i++) {

            float x = (float) (r * Math.cos(rad));
            float y = (float) (r * Math.sin(rad));

            float toX = x + centerX;
            float toY = y + centerY;

            //中间的直线
            canvas.drawLine(centerX, centerY, toX, toY, mFramePaint);

            //绘制多少段
            float normalRad = rad - (float) Math.PI / 2;
            float scaleX = (float) (mScaleLength / 2 * Math.cos(normalRad));
            float scaleY = (float) (mScaleLength / 2 * Math.sin(normalRad));
            for (int j = 1; j <= mNumScale; j++) {
                float pivotX = x / (mNumScale + 1) * j + centerX;
                float pivotY = y / (mNumScale + 1) * j + centerY;
                float fromScaleX = pivotX - scaleX;
                float fromScaleY = pivotY - scaleY;
                float toScaleX = pivotX + scaleX;
                float toScaleY = pivotY + scaleY;
                canvas.drawLine(fromScaleX, fromScaleY, toScaleX, toScaleY, mFramePaint);
            }

            float nextRad = rad + uRad;
            float nextX = (float) (r * Math.cos(nextRad));
            float nextY = (float) (r * Math.sin(nextRad));
            float nextToX = nextX + centerX;
            float nextToY = nextY + centerY;

            //正变形边框
            canvas.drawLine(toX, toY, nextToX, nextToY, mFramePaint);

            //画周围的字
            if (mLabels != null && mLabels.size() == mDatas.size()) {
                float tw = mTextSize / 2 * mLabels.get(i).length();
                float th = mTextSize / 2;
                float textX = nextToX - tw / 2 + DEFAULT_LABEL_PADDING * (float) Math.cos(nextRad);
                float textY = nextToY + th / 2 + DEFAULT_LABEL_PADDING * (float) Math.sin(nextRad);
                canvas.drawText(mLabels.get(i), textX, textY, mTextPaint);
            }

            if (mDrawables != null && mDrawables.size() == mDatas.size()) {

                float textX = nextToX + DEFAULT_LABEL_PADDING * (float) Math.cos(nextRad);
                float textY = nextToY + DEFAULT_LABEL_PADDING * (float) Math.sin(nextRad);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mDrawables.get(i));
                canvas.drawBitmap(bitmap, textX + bitmap.getWidth() / 2, textY + bitmap.getHeight() / 2, mBitmapPaint);
            }

            // 遮罩层
            if (mDatas != null && mDatas.size() != 0) {

                float currentValue = mDatas.get(i) * (Float) mAnimator.getAnimatedValue();
                float prevValue;
                if (mPrevValues != null && mPrevValues.size() == mDatas.size()) {
                    prevValue = mPrevValues.get(i) * (1 - (Float) mAnimator.getAnimatedValue());
                } else {
                    prevValue = mDatas.get(i) * (1 - (Float) mAnimator.getAnimatedValue());
                }

                float valueX = nextX * (currentValue + prevValue) + centerX;
                float valueY = nextY * (currentValue + prevValue) + centerY;
                if (path == null) {
                    path = new Path();
                    path.moveTo(valueX, valueY);
                }
                path.lineTo(valueX, valueY);

                rad += uRad;
            }
        }

        path.close();
        canvas.drawPath(path, mChartPaint);

        //绘制中间的字
        if (mCenterText != null) {

            Paint.FontMetrics fontMetrics = mCenterTextPaint.getFontMetrics();
            //计算文字高度
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            //计算文字baseline
            float textBaseY = height - (height - fontHeight) / 2 - fontMetrics.bottom;
            canvas.drawText(mCenterText, centerX, textBaseY, mCenterTextPaint);
        }
    }

    public void setDatas(List<Float> datas) {
        mPrevValues = new ArrayList<>(mDatas);
        mDatas = new ArrayList<>(datas);
        mNumData = datas.size();

        mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mAnimator.setDuration(mDuration);
        mAnimator.addUpdateListener(getListener);
        mAnimator.start();

    }

    private ValueAnimator.AnimatorUpdateListener getListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    };

    public void setmCenterText(String mCenterText) {
        this.mCenterText = mCenterText;
        invalidate();
    }

    public void setmCenterTextSize(float mCenterTextSize) {
        this.mCenterTextSize = mCenterTextSize;
        invalidate();
    }
}
