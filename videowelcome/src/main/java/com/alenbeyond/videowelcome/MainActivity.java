package com.alenbeyond.videowelcome;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ViewPager mVpWelcome;
    private CustomVideoView mVideoView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startVideo(msg.what);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mVpWelcome = (ViewPager) findViewById(R.id.vp_welcome);
        mVideoView = (CustomVideoView) findViewById(R.id.video_view);
    }

    private void initData() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);
        mVideoView.setVideoURI(uri);
        mVpWelcome.setAdapter(new VideoAdapter(this));
        mVpWelcome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                startVideo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        startVideo(0);
    }

    private void startVideo(int pos) {
        mHandler.removeCallbacksAndMessages(null);
        int max = 18;
        mVideoView.seekTo(pos * max / 3 * 1000);
        mVideoView.start();
        mHandler.sendEmptyMessageDelayed(pos, max / 3 * 1000);
    }

    static class VideoAdapter extends PagerAdapter {

        private Context context;

        public VideoAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(context, R.layout.item_welcome, null);
            TextView tvText = (TextView) view.findViewById(R.id.tv_text);
            String text = "第" + position + "页";
            tvText.setText(text);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
