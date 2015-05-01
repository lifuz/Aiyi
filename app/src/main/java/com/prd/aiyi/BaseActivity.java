package com.prd.aiyi;

import android.app.Activity;
import android.os.Bundle;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * 一个事件基类，其他activity继承这个activity，使每个继承他的activity
 * 都订阅一个事件，每当接收到这个事件时，activity执行finish()方法
 * Created by 半夏微凉 on 2015/4/29.
 */
public class BaseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将对象注册到事件总线中， ****** 注意要在onDestory中进行注销 ****
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ****** 不要忘了进行注销 ****
        EventBus.getDefault().unregister(this);
    }

    //订阅事件
    @Subscriber(tag = "csuicide")
    private void csuicideMyself(String msg) {
        finish();
    }
}
