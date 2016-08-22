package com.alenbeyond.dashboardview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alenbeyond.dashboardview.R;

/**
 * Created by Alen on 2016/8/18.
 */
public class CustomDashboardView extends RelativeLayout {

    private static final String TAG = "DashboardView";
    private Context context;
    private ImageView ivArrow;
    private float angle;
    private RotateAnimation anim;
    private int maxScore = 950;
    private int minScore = 350;

    public CustomDashboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.custom_dashboard, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivArrow = (ImageView) findViewById(R.id.iv_arrow);
        RotateAnimation init = new RotateAnimation(0, -85, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        init.setDuration(0);
        init.setFillAfter(true);
        ivArrow.startAnimation(init);
    }

    public void setScore(int score) {
        angle = (float) (score - minScore) / (maxScore - minScore) * 180 + -90;
        startRotateAnimation();
    }

    private void startRotateAnimation() {

        if (angle > 85) {
            angle = 85;
        }

        if (angle < -85) {
            angle = -85;
        }

        if (anim == null) {
            anim = new RotateAnimation(-85, angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        anim.setDuration(2000);
        if (angle < 90) {
            anim.setInterpolator(new OvershootInterpolator(0.9f));
        }
        anim.setFillAfter(true);
        ivArrow.startAnimation(anim);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int height = width / 2;
        int spec = MeasureSpec.makeMeasureSpec(height, mode);
        super.onMeasure(widthMeasureSpec, spec);
    }
}
