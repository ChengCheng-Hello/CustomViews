package com.cc.custom.stepview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.cc.custom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/9.
 */
public class TXStepViewDemoActivity extends FragmentActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXStepViewDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step_view_demo);

        List<StepItem> ld = new ArrayList<>(4);
        ld.add(new StepItem("申请已提交", "2016-06-01 14:49", true));
        ld.add(new StepItem("审核通过", "2016-06-07 14:49", true));
        ld.add(new StepItem("银行受理提现", "2016-11-20 14:49", true));
        ld.add(new StepItem("审核通过", "2016-06-23 14:49", true));
        ld.add(new StepItem("审核通过", "2016-06-23 14:49", true));

        StepView stepView = (StepView) findViewById(R.id.stepView);
        stepView.initData(ld.size(), 2, ld);

        // StepView2 stepView2 = (StepView2) findViewById(R.id.stepView2);
        // stepView2.initData(5, 2, mStatusData, mDateData);
    }
}
