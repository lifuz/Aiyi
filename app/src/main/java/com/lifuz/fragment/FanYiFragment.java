package com.lifuz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prd.aiyi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 翻译页面
 * Created by 半夏微凉 on 2015/4/26.
 */
public class FanYiFragment extends Fragment implements View.OnClickListener {

    private TextView fanyi_yuyan, fanyi_title, fanyi_fanyi;
    private EditText fanyi_target, fanyi_source;

    private boolean flag = false;

    private RequestQueue rqueue;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View messageLayout = inflater.inflate(R.layout.fanyi_layout,
                container, false);

        return messageLayout;
    }

    /**
     * 初始化组件
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fanyi_yuyan = (TextView) view.findViewById(R.id.fanyi_yuyan);
        fanyi_title = (TextView) view.findViewById(R.id.fanyi_title);
        fanyi_fanyi = (TextView) view.findViewById(R.id.fanyi_fanyi);

        fanyi_target = (EditText) view.findViewById(R.id.fanyi_target);
        fanyi_source = (EditText) view.findViewById(R.id.fanyi_source);

        fanyi_yuyan.setOnClickListener(this);
        fanyi_fanyi.setOnClickListener(this);

        rqueue = Volley.newRequestQueue(getActivity());


    }

    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanyi_yuyan:

                //点击语言时根据标志确认标题显示内容
                if (flag) {
                    fanyi_title.setText("英 → 中");
                    flag = false;
                } else {
                    fanyi_title.setText("中 → 英");
                    flag = true;
                }

                break;

            //点击翻译，连接百度api，获取翻译内容
            case R.id.fanyi_fanyi:

                //获取翻译url
                String url = geturl();


                //在百度交互，获取百度返回的Json，处理json，并翻译的内容显示在目标编辑框中
                getContent(url);

                break;

        }
    }

    /**
     * 在百度交互，获取百度返回的Json，处理json，并翻译的内容显示在目标编辑框中
     * @param url 传入访问的api
     */
    private void getContent(String url) {
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    JSONArray results = jsonObject.getJSONArray("trans_result");
                    JSONObject result = results.getJSONObject(0);
                    fanyi_target.setText(result.getString("dst"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("tag", "网络访问失败");
            }
        });


        rqueue.add(jr);
    }

    /**
     * 获取翻译url
     * @return 访问百度的url
     */
    private String geturl() {
        //获取百度api连接，并修改成我们需要的样式
        String url = "http://openapi.baidu.com/public/2.0/bmt/translate?" +
                "client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=content&from=fm&to=tm";

        String str = fanyi_source.getText().toString();

        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url = url.replace("content", str);

        if (flag) {


            url = url.replace("fm", "zh");
            url = url.replace("tm", "en");

        } else {
            url = url.replace("fm", "en");
            url = url.replace("tm", "zh");
        }
        return url;
    }


}
