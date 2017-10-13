package com.example.hellojni;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/13.
 */
public class HelloJni {

    static {
        System.loadLibrary("hello-jni");
    }


    public native static String stringFromJNI();
}
