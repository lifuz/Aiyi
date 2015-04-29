package com.prd.testsql;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.prd.sqlite.DanCiBenService;


public class MainActivity extends ActionBarActivity {

    private TextView tv;

    private DanCiBenService ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        ds = new DanCiBenService(getApplicationContext());
        ds.insertDan();

        Log.i("tag","插入成功");
    }

    public void click(View v) {

        tv.setText(ds.queryAll());
    }

}
