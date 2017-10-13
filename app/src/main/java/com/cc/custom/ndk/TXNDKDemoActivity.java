package com.cc.custom.ndk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.cc.custom.R;
import com.example.hellojni.HelloJni;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/13.
 */
public class TXNDKDemoActivity extends FragmentActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXNDKDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ndk_demo);

        String content = HelloJni.stringFromJNI();

        TextView tvContent = (TextView) findViewById(R.id.tv_content);
        tvContent.setText(content);
    }

}
