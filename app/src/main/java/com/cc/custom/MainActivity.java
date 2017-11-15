package com.cc.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cc.custom.calender.TXCalenderDemoActivity;
import com.cc.custom.chart.ChartItem;
import com.cc.custom.chart.LineChartView2;
import com.cc.custom.gallery.listener.TXAlbumTaskListener;
import com.cc.custom.gallery.listener.TXVideoTaskListener;
import com.cc.custom.gallery.model.TXAlbumModel;
import com.cc.custom.gallery.model.TXVideoModel;
import com.cc.custom.gallery.task.TXVideoAlbumTask;
import com.cc.custom.gallery.task.TXVideoTask;
import com.cc.custom.ndk.TXNDKDemoActivity;
import com.cc.custom.progress.BubbleProgress;
import com.cc.custom.rating.TXRatingView;
import com.cc.custom.stepview.TXStepViewDemoActivity;
import com.cc.custom.video.TXVideoEditDemoActivity;
import com.cc.custom.viewpager.TXVpDemoActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChartView2 lineChartView;
    private TXRatingView ratingView;
    private BubbleProgress mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TXContextManager.getInstance().init(getApplicationContext());

        lineChartView = (LineChartView2) findViewById(R.id.lineChart);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ChartItem> mChartData = new ArrayList<>();
                mChartData.add(new ChartItem(20, "1日", true));
                mChartData.add(new ChartItem(50, "2日", true));
                mChartData.add(new ChartItem(100, "3日", true));
                mChartData.add(new ChartItem(70, "4日", true));
                mChartData.add(new ChartItem(60, "5日", true));
                mChartData.add(new ChartItem(20, "6日", true));
                mChartData.add(new ChartItem(120, "7日", false));
                mChartData.add(new ChartItem(80, "8日", false));
                mChartData.add(new ChartItem(80, "9日", false));
                mChartData.add(new ChartItem(80, "10日", false));

                lineChartView.setData(mChartData);

                Toast.makeText(MainActivity.this, "" + ratingView.getRating(), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineChartView.setData(new ArrayList<ChartItem>());
                ratingView.setRating(450);
            }
        });

        findViewById(R.id.button_scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineChartView.smoothScrollTo(2);
            }
        });

        ratingView = (TXRatingView) findViewById(R.id.ratingView);

        findViewById(R.id.btn_vp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXVpDemoActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_calender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXCalenderDemoActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXStepViewDemoActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXVideoEditDemoActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_ndk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TXNDKDemoActivity.launch(v.getContext());
            }
        });

        findViewById(R.id.btn_open_gl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TXOpenGLDemoActivity.launch(v.getContext());
                new TXVideoAlbumTask().load(new TXAlbumTaskListener() {
                    @Override
                    public void postAlbumList(@Nullable List<TXAlbumModel> list) {
                        for (TXAlbumModel item : list) {
                            new TXVideoTask().load(item.bucketId, 0, new TXVideoTaskListener() {
                                @Override
                                public void postImages(@Nullable List<TXVideoModel> videoList, int count,
                                    String bucketId) {
                                    int i = 0;
                                }

                                @Override
                                public boolean needFilter(String path) {
                                    return false;
                                }
                            });
                        }
                    }
                });
            }
        });

        findViewById(R.id.btn_seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TXSeekDemoActivity.launch(v.getContext());
                mProgress.setProgress(150);
            }
        });

        mProgress = (BubbleProgress) findViewById(R.id.progress);
    }
}
