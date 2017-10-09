package com.cc.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cc.custom.calender.TXCalenderDemoActivity;
import com.cc.custom.chart.ChartItem;
import com.cc.custom.chart.LineChartView2;
import com.cc.custom.rating.TXRatingView;
import com.cc.custom.stepview.TXStepViewDemoActivity;
import com.cc.custom.viewpager.TXVpDemoActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LineChartView2 lineChartView;
    private TXRatingView ratingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
