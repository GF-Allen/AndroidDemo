package com.alenbeyond.linechart.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alenbeyond.linechart.R;
import com.alenbeyond.linechart.ViewUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Alen on 2016/8/19.
 */
public class LineChartView extends View {

    private final String TAG = "LineChartView";
    private Context context;
    private Paint mXAxisTextPaint;// X坐标
    private Paint mYAxisTextPaint;// Y坐标
    private Paint mDashedLinePaint;//中间的虚线

    private Paint mLinePaint;//折线

    private Paint mLoopPaint; // 圆环

    private Paint mCirclePaint; // 圆

    private Paint mTouchLoopPaint;

    private Paint mTouchCirclePaint;

    private Paint mTouchTextPaint;

    private Paint mPopPaint;

    private Paint mPopTextPaint;

    private static final int DEFAULT_LINE_COLOR = 0xFFFF5F1C; // 折线颜色

    private static final int DEFAULT_LOOP_COLOR = 0xFFFF5F1C; // 圆环颜色

    private static final int DEFAULT_CIRCLE_COLOR = 0xFFFFE7E1; // 圆环颜色

    private static final int DEFAULT_X_AXIS_TEXT_COLOR = 0xFF444444; // X轴颜色

    private static final int DEFAULT_Y_AXIS_TEXT_COLOR = 0xFFC1C1C1; // Y轴颜色

    private static final int DEFAULT_DASHED_LINE_COLOR = 0xFFE7EAEF; // 需要颜色

    private static final int DEFAULT_ANIM_DURATION = 300; //动画时长

    private static final boolean DEFAULT_IS_ANIMATED = true; //是否执行动画

    private static final float DEFAULT_Y_AXIS_TEXT_SIZE = 45;

    private static final float DEFAULT_X_AXIS_TEXT_SIZE = 30;

    private static final float DEFAULT_RADIUS = 4;

    private static final float DEFAULT_Y_SPACING = 120;

    private int mLineColor = DEFAULT_LINE_COLOR;

    private int mLoopColor = DEFAULT_LOOP_COLOR;

    private int mCircleColor = DEFAULT_CIRCLE_COLOR;

    private int mXAxisTextColor = DEFAULT_X_AXIS_TEXT_COLOR;
    private int mYAxisTextColor = DEFAULT_Y_AXIS_TEXT_COLOR;
    private int mDashedLineColor = DEFAULT_DASHED_LINE_COLOR;

    private float mYAxisTextSize = DEFAULT_Y_AXIS_TEXT_SIZE;
    private float mXAxisTextSize = DEFAULT_X_AXIS_TEXT_SIZE;

    private int mDuration = DEFAULT_ANIM_DURATION;

    private int width;

    private int height;
    private float mLeftMargin = ViewUtils.dip2px(getContext(), 15);
    private float mRightMargin = ViewUtils.dip2px(getContext(), 15);

    private float mBottomMargin = ViewUtils.dip2px(getContext(), 15);

    private String[] xValues = {"3月", "4月", "5月", "6月", "7月", "8月"}; // x轴参数
    private int[] yValues = {350, 650, 950}; //y轴参数
    private int[] datas = {400, 950, 600, 900, 600, 800};
    private float xSpacing;
    private float ySpacing = DEFAULT_Y_SPACING; // y轴间隔
    private float mRadius = ViewUtils.dip2px(getContext(), DEFAULT_RADIUS);
    private boolean isTouch;
    private int mTouchPos; // 点击的位置
    private LineChartPop pop;

    private ValueAnimator mAnimator; //动画
    private boolean mIsAnimated; //是否开启动画

    public LineChartView setDatas(int[] datas) {
        this.datas = datas;
        return this;
    }

    public LineChartView setxValues(String[] xValues) {
        this.xValues = xValues;
        return this;
    }

    public LineChartView setyValues(int[] yValues) {
        this.yValues = yValues;
        return this;
    }

    public LineChartView refresh() {
        if (mIsAnimated) {
            startAnim();
        } else {
            invalidate();
        }
        return this;
    }

    private LineChartView startAnim() {
        finishPos = 0;
        mAnimator.start();
        return this;
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
        ySpacing = array.getDimension(R.styleable.LineChartView_lcvYSpacing, DEFAULT_Y_SPACING);
        mRadius = array.getDimension(R.styleable.LineChartView_lcvRadius, ViewUtils.dip2px(getContext(), DEFAULT_RADIUS));
        mLeftMargin = array.getDimension(R.styleable.LineChartView_lcvLeftMargin, ViewUtils.dip2px(getContext(), 15));
        mRightMargin = array.getDimension(R.styleable.LineChartView_lcvRightMargin, ViewUtils.dip2px(getContext(), 15));
        mBottomMargin = array.getDimension(R.styleable.LineChartView_lcvBottomMargin, ViewUtils.dip2px(getContext(), 15));
        mYAxisTextSize = array.getDimension(R.styleable.LineChartView_lcvYAxisTextSize, DEFAULT_Y_AXIS_TEXT_SIZE);
        mXAxisTextSize = array.getDimension(R.styleable.LineChartView_lcvXAxisTextSize, DEFAULT_X_AXIS_TEXT_SIZE);

        mYAxisTextColor = array.getColor(R.styleable.LineChartView_lcvYAxisTextColor, DEFAULT_Y_AXIS_TEXT_COLOR);
        mXAxisTextColor = array.getColor(R.styleable.LineChartView_lcvXAxisTextColor, DEFAULT_X_AXIS_TEXT_COLOR);
        mDashedLineColor = array.getColor(R.styleable.LineChartView_lcvDashedLineColor, DEFAULT_DASHED_LINE_COLOR);
        mCircleColor = array.getColor(R.styleable.LineChartView_lcvCircleColor, DEFAULT_CIRCLE_COLOR);
        mLoopColor = array.getColor(R.styleable.LineChartView_lcvLoopColor, DEFAULT_LOOP_COLOR);
        mLineColor = array.getColor(R.styleable.LineChartView_lcvLineColor, DEFAULT_LINE_COLOR);

        mDuration = array.getInteger(R.styleable.LineChartView_lcvDuration, DEFAULT_ANIM_DURATION);

        mIsAnimated = array.getBoolean(R.styleable.LineChartView_lcvIsAnimated, DEFAULT_IS_ANIMATED);

        init();
    }

    private void init() {

//        pop = new LineChartPop(context);
//        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                invalidate();
//            }
//        });

        mXAxisTextPaint = new Paint();
        mXAxisTextPaint.setAntiAlias(true);
        mXAxisTextPaint.setTextSize(mXAxisTextSize);
        mXAxisTextPaint.setColor(mXAxisTextColor);

        mYAxisTextPaint = new Paint();
        mYAxisTextPaint.setAntiAlias(true);
        mYAxisTextPaint.setTextSize(mYAxisTextSize);
        mYAxisTextPaint.setColor(mYAxisTextColor);

        mDashedLinePaint = new Paint();
        mDashedLinePaint.setStyle(Paint.Style.STROKE);
        mDashedLinePaint.setAntiAlias(true);
        mDashedLinePaint.setStrokeWidth(2f);
        mDashedLinePaint.setColor(mDashedLineColor);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setColor(mLineColor);

        mLoopPaint = new Paint();
        mLoopPaint.setStyle(Paint.Style.FILL);
        mLoopPaint.setColor(mLoopColor);
        mLoopPaint.setDither(true);
        mLinePaint.setAntiAlias(true);

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setDither(true);
        mCirclePaint.setAntiAlias(true);

        mTouchLoopPaint = new Paint();
        mTouchLoopPaint.setStyle(Paint.Style.FILL);
        mTouchLoopPaint.setColor(mLoopColor);
        mTouchLoopPaint.setDither(true);
        mTouchLoopPaint.setAntiAlias(true);

        mTouchCirclePaint = new Paint();
        mTouchCirclePaint.setStyle(Paint.Style.FILL);
        mTouchCirclePaint.setColor(mCircleColor);
        mTouchCirclePaint.setDither(true);
        mTouchCirclePaint.setAntiAlias(true);

        mTouchTextPaint = new Paint();
        mTouchTextPaint.setAntiAlias(true);
        mTouchTextPaint.setColor(mLineColor);
        mTouchTextPaint.setDither(true);
        mTouchTextPaint.setTextSize(mXAxisTextSize);

        mPopPaint = new Paint();
        mPopPaint.setStyle(Paint.Style.FILL);
        mPopPaint.setDither(true);
        mPopPaint.setAntiAlias(true);
        mPopPaint.setColor(Color.WHITE);

        mPopTextPaint = new Paint();
        mPopTextPaint.setAntiAlias(true);
        mPopTextPaint.setDither(true);
        mPopTextPaint.setTextSize(ViewUtils.dip2px(context, 10));
        mPopTextPaint.setColor(Color.WHITE);

        if (mIsAnimated) {
            mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (finishPos == datas.length) {
                        mAnimator.cancel();
                        finishPos = 0;
                    } else {
                        invalidate();
                    }
                }
            });
            mAnimator.setDuration(mDuration / datas.length);
            mAnimator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mXPoint.clear();
        mYPoint.clear();
        mDataPoint.clear();

        super.onDraw(canvas);
        drawYAxisText(canvas);
        drawXAxisText(canvas);
        drawDashedLine(canvas);
        drawLoop(canvas);
        drawLine(canvas);
        drawCircle(canvas);
        drawTouch(canvas);
        drawPop(canvas);
        isTouch = false;
    }

    private ArrayList<PointF> mXPoint = new ArrayList<>();

    private ArrayList<PointF> mYPoint = new ArrayList<>();

    private ArrayList<PointF> mDataPoint = new ArrayList<>();

    /**
     * Y轴文字
     *
     * @param canvas
     */
    private void drawYAxisText(Canvas canvas) {
        PointF textX = getTextWidthAndHeight(xValues[0], mXAxisTextPaint);
        if (yValues != null) {
            for (int i = 0; i < yValues.length; i++) {
                float x = mLeftMargin;
                float y = height - (i * ySpacing) - mBottomMargin * 2 - textX.y;
                canvas.drawText(String.valueOf(yValues[i]), x, y, mYAxisTextPaint);
                //保存当前文字的y坐标
                PointF point = getTextWidthAndHeight(String.valueOf(yValues[i]), mYAxisTextPaint);
                mYPoint.add(new PointF(x + point.x / 2, y - point.y / 2));
            }
        }
    }

    /**
     * X轴文字
     *
     * @param canvas
     */
    private void drawXAxisText(Canvas canvas) {
        if (xValues != null) {
            //获取Y轴字体的宽度
            int length = String.valueOf(yValues[0]).length();
            Rect yRect = new Rect();
            mYAxisTextPaint.getTextBounds(String.valueOf(yValues[0]), 0, length, yRect);
            int yw = yRect.width();

            xSpacing = (width - mYPoint.get(0).x - yw) / xValues.length;
            for (int i = 0; i < xValues.length; i++) {
                float y = height - mBottomMargin;
                float x = mYPoint.get(0).x + yw + i * xSpacing;
                canvas.drawText(xValues[i], x, y, mXAxisTextPaint);

                // 保存当前文字的x坐标
                PointF point = getTextWidthAndHeight(xValues[i], mXAxisTextPaint);
                mXPoint.add(new PointF(x + point.x / 2, y - point.y / 2));
            }
        }
    }

    /**
     * 中间线条
     *
     * @param canvas
     */
    private void drawDashedLine(Canvas canvas) {
        PointF pointY = getTextWidthAndHeight(String.valueOf(yValues[0]), mYAxisTextPaint);
        float startX = mYPoint.get(0).x + pointY.x / 2 + 10;
        float endX = width - mRightMargin;
        for (int i = 0; i < mYPoint.size(); i++) {
            float y = mYPoint.get(i).y;
            Path path = new Path();
            path.moveTo(startX, y);
            path.lineTo(endX, y);
            if (i != 0) {
                //绘制虚线
                PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                mDashedLinePaint.setPathEffect(effects);
                canvas.drawPath(path, mDashedLinePaint);
            } else {
                canvas.drawLine(startX, y, endX, y, mDashedLinePaint);
            }
        }
    }

    /**
     * 画圆圈的环
     *
     * @param canvas
     */
    private void drawLoop(Canvas canvas) {
        float minY = mYPoint.get(0).y; // 最小值的y
        float maxY = mYPoint.get(yValues.length - 1).y; // 最大值的y
        float min = yValues[0];
        float max = yValues[yValues.length - 1];

        if (datas != null) {
            for (int i = 0; i < datas.length; i++) {
                float x = mXPoint.get(i).x;
                int data = datas[i];
                float p = (data - min) / (max - min);
                float y = (maxY - minY) * p + minY;
                mDataPoint.add(new PointF(x, y));
                canvas.drawCircle(x, y, mRadius, mLoopPaint);
            }
        }
    }

    private int finishPos = 0;

    /**
     * 折线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        PointF start = null;
        PointF end;
        for (int i = 0; i < mDataPoint.size(); i++) {
            if (i == 0) {
                start = mDataPoint.get(i);
                continue;
            }
            end = mDataPoint.get(i);
            if (mIsAnimated) {
                if (i < finishPos) {
                    canvas.drawLine(start.x, start.y, end.x, end.y, mLinePaint);
                }
                if (i == finishPos) {
                    canvas.drawLine(start.x, start.y,
                            start.x + (end.x - start.x) * (float) mAnimator.getAnimatedValue(),
                            start.y + (end.y - start.y) * (float) mAnimator.getAnimatedValue(),
                            mLinePaint);
                }
                if ((float) mAnimator.getAnimatedValue() == 1.0f && finishPos != datas.length - 1) {
                    mAnimator.start();
                    finishPos++;
                }
            } else {
                canvas.drawLine(start.x, start.y, end.x, end.y, mLinePaint);
            }
            start = end;
        }
    }

    /**
     * 圆圈的实心
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        for (PointF point : mDataPoint) {
            canvas.drawCircle(point.x, point.y, mRadius - ViewUtils.dip2px(getContext(), 1), mCirclePaint);
        }
    }

    /**
     * 触摸时X轴文字
     *
     * @param canvas
     */
    private void drawTouch(Canvas canvas) {
        if (isTouch) {
            PointF point = mXPoint.get(mTouchPos);
            PointF textP = getTextWidthAndHeight(xValues[0], mXAxisTextPaint);
            RectF rectLoop = new RectF(point.x - textP.x / 2 - ViewUtils.dip2px(context, 8),
                    point.y - textP.y / 2 - ViewUtils.dip2px(context, 3),
                    point.x + textP.x / 2 + ViewUtils.dip2px(context, 8),
                    point.y + textP.y / 2 + ViewUtils.dip2px(context, 3));
            canvas.drawRoundRect(rectLoop, ViewUtils.dip2px(context, 8), ViewUtils.dip2px(context, 8), mTouchLoopPaint);

            RectF rectCircle = new RectF(point.x - textP.x / 2 - ViewUtils.dip2px(context, 6.5f),
                    point.y - textP.y / 2 - ViewUtils.dip2px(context, 1.5f),
                    point.x + textP.x / 2 + ViewUtils.dip2px(context, 6.5f),
                    point.y + textP.y / 2 + ViewUtils.dip2px(context, 1.5f));
            canvas.drawRoundRect(rectCircle, ViewUtils.dip2px(context, 6.5f), ViewUtils.dip2px(context, 6.5f), mTouchCirclePaint);

            PointF text = getTextWidthAndHeight(xValues[mTouchPos], mTouchTextPaint);
            canvas.drawText(xValues[mTouchPos], point.x - text.x / 2, point.y + text.y / 2, mTouchTextPaint);
        }
    }

    /**
     * 触摸时弹出
     *
     * @param canvas
     */
    private void drawPop(Canvas canvas) {
        if (isTouch) {
            PointF point = mDataPoint.get(mTouchPos);
            PointF textWidthAndHeight = getTextWidthAndHeight(String.valueOf(datas[mTouchPos]), mPopTextPaint);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sore_plate);
            float left = point.x - bitmap.getWidth() / 2;
            float top = point.y - bitmap.getHeight() * 3 / 2;
            canvas.drawBitmap(bitmap, left, top, mPopPaint);
            canvas.drawText(String.valueOf(datas[mTouchPos]), point.x - textWidthAndHeight.x / 2,
                    top + textWidthAndHeight.y + ViewUtils.dip2px(context, 2), mPopTextPaint);
        }
    }

    private Rect rect = new Rect(); //临时保存

    /**
     * 获取String的宽高
     *
     * @param text
     * @param paint
     * @return
     */
    private PointF getTextWidthAndHeight(String text, Paint paint) {
        paint.getTextBounds(text, 0, text.length(), rect);
        float width = rect.width();
        float height = rect.height();
        return new PointF(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float ex = event.getX();
                float ey = event.getY();
                for (int i = 0; i < mDataPoint.size(); i++) {
                    PointF pointCircle = mDataPoint.get(i);
                    RectF rectCircle = new RectF(pointCircle.x - mRadius * 3, pointCircle.y - mRadius * 3, pointCircle.x + mRadius * 3, pointCircle.y + mRadius * 3);
                    PointF xPoint = mXPoint.get(i);
                    RectF xRect = new RectF(xPoint.x - mRadius * 3, xPoint.y - mRadius * 3, xPoint.x + mRadius * 3, xPoint.y + mRadius * 3);
                    if (rectCircle.contains(ex, ey) || xRect.contains(ex, ey)) {
//                    pop.setText(String.valueOf(datas[i]));
//                    pop.showAsDropDown(LineChartView.this, (int) rect.left, (int) (-height + rect.bottom - mBottomMargin * 4));
                        isTouch = true;
                        mTouchPos = i;
                        invalidate();
                    }
                }
                break;
        }

        return true;
    }

    public class LineChartPop extends PopupWindow {

        private TextView tvValue;

        public LineChartPop(Context context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater
                    .inflate(R.layout.custom_line_chart_pop, null);
            this.setContentView(view);
            tvValue = (TextView) view.findViewById(R.id.tv_value);
            this.setOutsideTouchable(true);
            this.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0x00000000);
            this.setBackgroundDrawable(dw);
        }

        public void setText(String textValue) {
            tvValue.setText(textValue);
        }
    }
}
