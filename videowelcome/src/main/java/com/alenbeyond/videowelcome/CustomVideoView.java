package com.alenbeyond.videowelcome;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Alen on 2016/8/25.
 */
public class CustomVideoView extends VideoView {
    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width / 0.56f);
        setMeasuredDimension(width, height);
    }
}
