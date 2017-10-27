package com.cc.custom.seek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cc.custom.R;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/26.
 */
public class TXSeekDemoActivity extends FragmentActivity {

    private RangeSeekbar mSeekBar;
    private TXFrameSeekBar mFrameBar;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXSeekDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seek_demo);

        mSeekBar = (RangeSeekbar) findViewById(R.id.seekbar);
        mSeekBar.setLeftSelection(2);
        mSeekBar.setRightSelection(4);

        findViewById(R.id.set_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setLeftSelection(0);
            }
        });

        findViewById(R.id.set_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setRightSelection(5);
            }
        });

        mFrameBar = (TXFrameSeekBar) findViewById(R.id.frameBar);
//        mFrameBar.setVideoPath("/mnt/sdcard/3.mp4");
        mFrameBar.setVideoPath("/mnt/sdcard/3.mp4");
    }
}
