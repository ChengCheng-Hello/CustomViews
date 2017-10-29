package com.cc.custom.seek;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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

        String path = "/mnt/sdcard/3.mp4";
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setVideoPath(path);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d(TAG, "onPrepared");
            }
        });
        videoStart();

        mFrameBar = (TXFrameSeekBar) findViewById(R.id.frameBar);
//        mFrameBar.setVideoPath("/mnt/sdcard/3.mp4");
        mFrameBar.setVideoPath(path);
        mFrameBar.setListener(this);
    }

    private void videoStart() {
        Log.d(TAG, "----videoStart----->>>>>>>");
        mVideoView.start();
//        positionIcon.clearAnimation();
//        if (animator != null && animator.isRunning()) {
//            animator.cancel();
//        }
//        anim();
//        handler.removeCallbacks(run);
//        handler.post(run);
    }

    private void videoPause() {
//        isSeeking = false;
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
//            handler.removeCallbacks(run);
        }
        Log.d(TAG, "----videoPause----->>>>>>>");
//        if (positionIcon.getVisibility() == View.VISIBLE) {
//            positionIcon.setVisibility(View.GONE);
//        }
//        positionIcon.clearAnimation();
//        if (animator != null && animator.isRunning()) {
//            animator.cancel();
//        }
    }

    @Override
    public void onChanged(int startTime, int endTime, int status) {
        if (status == TXRangeSeekBar.STATUS_DOWN) {
            videoPause();
        } else if (status == TXRangeSeekBar.STATUS_MOVE) {
//            mVideoView.seekTo(startTime);
        } else if (status == TXRangeSeekBar.STATUS_UP) {
            mVideoView.seekTo(startTime);
            mVideoView.start();
//            mVideoView.resume();
        }
    }
}
