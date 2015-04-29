package com.prd.aiyi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;

import com.lifuz.fragment.CiDianFragment;
import com.lifuz.fragment.DanCiBenFragment;
import com.lifuz.fragment.FanYiFragment;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //用于词典的Fragment
    private CiDianFragment cf;

    //用于单词本的fragment
    private DanCiBenFragment df;

    //用于翻译的fragment
    private FanYiFragment ff;

    //词典布局
    private View cidian_layout;

    //翻译布局
    private View fanyi_layout;

    //单词本布局
    private View danciben_layout;

    //用于管理fragment
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        initViews();

        fm = getSupportFragmentManager();
        setTabSelection(0);

    }

    public void initViews() {
        cidian_layout = findViewById(R.id.cidian_bottom);
        fanyi_layout = findViewById(R.id.fanyi_bottom);
        danciben_layout = findViewById(R.id.danciben_bottom);
        cidian_layout.setOnClickListener(this);
        fanyi_layout.setOnClickListener(this);
        danciben_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cidian_bottom:
                setTabSelection(0);
                break;
            case R.id.fanyi_bottom:
                setTabSelection(1);
                break;
            case R.id.danciben_bottom:
                setTabSelection(2);
                break;
        }

    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab对应的下标，0表示词典，1表示翻译，2表示单词本
     */
    private void setTabSelection(int index) {

        clearSelection();

        // 开启一个Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index){
            case 0:
                cidian_layout.setBackgroundColor(Color.DKGRAY);
                if(null == cf) {
                    cf = new CiDianFragment();
                    transaction.add(R.id.content_fragment,cf);
                } else {
                    transaction.show(cf);
                }
                break;
            case 1:
                fanyi_layout.setBackgroundColor(Color.DKGRAY);
                if(null == ff) {
                    ff = new FanYiFragment();
                    transaction.add(R.id.content_fragment,ff);
                } else {
                    transaction.show(ff);
                }
                break;
            case 2:
                danciben_layout.setBackgroundColor(Color.DKGRAY);
                if(null == df) {
                    df = new DanCiBenFragment();
                    transaction.add(R.id.content_fragment,df);
                } else {
                    transaction.show(df);
                }
                break;
        }

        transaction.commit();


    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (df != null) {
            // 隐藏Fragment
            transaction.hide(df);
        }
        if (cf != null) {
            transaction.hide(cf);
        }
        if (ff != null) {
            transaction.hide(ff);
        }
    }

    private void clearSelection() {
        cidian_layout.setBackgroundColor(Color.WHITE);
        fanyi_layout.setBackgroundColor(Color.WHITE);
        danciben_layout.setBackgroundColor(Color.WHITE);

    }
}
