package com.cc.custom.seek;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.VideoView;

import com.cc.custom.R;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/26.
 */
public class TXSeekDemoActivity extends FragmentActivity implements TXFrameSeekBar.TXOnRangeChangedListener {

    private static final String TAG = "TXSeekDemoActivity";

    private TXFrameSeekBar mFrameBar;
    private VideoView mVideoView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXSeekDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seek_demo);

        String path = "/mnt/sdcard/3.mp4";//"/mnt/sdcard/DCIM/Camera/VID_20171031_100232.mp4";//
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setVideoPath(path);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d(TAG, "onPrepared");
                videoStart();
            }
        });
        // mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        // @Override
        // public void onCompletion(MediaPlayer mp) {
        // Log.d(TAG, "onCompletion");
        // mVideoView.seekTo(mFrameBar.getStartTime());
        // mVideoView.start();
        // }
        // });

        mFrameBar = (TXFrameSeekBar) findViewById(R.id.frameBar);
        // mFrameBar.setVideoPath("/mnt/sdcard/3.mp4");
        mFrameBar.setVideoPath(path);
        mFrameBar.setListener(this);

        TXRangeSeekBar2 seekBar2 = (TXRangeSeekBar2) findViewById(R.id.rangeBar2);
        seekBar2.setVideoPath(path);
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mVideoView.getCurrentPosition();
//            Log.d(TAG, "currentPosition " + currentPosition);
            if (currentPosition >= mFrameBar.getEndTime() - 100) {
                mVideoView.seekTo(mFrameBar.getStartTime());
                videoStart();
            }

            mHandler.postDelayed(mRunnable, 100);
        }
    };

    private void videoStart() {
        Log.d(TAG, "----videoStart----->>>>>>>");
        mVideoView.start();
        mFrameBar.anim();
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
        // positionIcon.clearAnimation();
        // if (animator != null && animator.isRunning()) {
        // animator.cancel();
        // }
        // anim();
        // handler.removeCallbacks(run);
        // handler.post(run);
    }

    private void videoPause() {
        // isSeeking = false;
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
            mHandler.removeCallbacks(mRunnable);
        }
        Log.d(TAG, "----videoPause----->>>>>>>");
        // if (positionIcon.getVisibility() == View.VISIBLE) {
        // positionIcon.setVisibility(View.GONE);
        // }
        // positionIcon.clearAnimation();
        // if (animator != null && animator.isRunning()) {
        // animator.cancel();
        // }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onChanged(int startTime, int endTime, int status) {
        Log.d(TAG, "onChanged startTime " + startTime + ", endTime " + endTime);
        if (status == Const.STATUS_PAUSE) {
            videoPause();
        } else if (status == Const.STATUS_MOVE) {
            // mVideoView.seekTo(startTime);
        } else if (status == Const.STATUS_PLAY) {
            mVideoView.seekTo(startTime);
            videoStart();
            // mVideoView.resume();
        }
    }
}
