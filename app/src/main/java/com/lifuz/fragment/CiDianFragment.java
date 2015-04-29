package com.lifuz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prd.aiyi.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by 半夏微凉 on 2015/4/26.
 */
public class CiDianFragment extends Fragment implements View.OnClickListener {

    private String url;

    private ImageButton search_button;

    private TextView tv;

    private RequestQueue mqueue;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View messageLayout = inflater.inflate(R.layout.cidian_layout,
                container, false);

        return messageLayout;
    }

    @Override
    public void onClick(View v) {


        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("tag", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag", error.getMessage());
            }
        });


        mqueue.add(jr);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mqueue = Volley.newRequestQueue(getActivity());

        tv = (TextView) view.findViewById(R.id.tv);
        search_button = (ImageButton) view.findViewById(R.id.search_button);
        search_button.setOnClickListener(this);

        String str = null;

        try {
            str = URLEncoder.encode("我", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.i("tag",str);

        //?client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=do&from=en&to=zh
        url = "http://openapi.baidu.com/public/2.0/translate/dict/simple?client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=%e4%bd%a0&from=zh&to=en";


    }
}
