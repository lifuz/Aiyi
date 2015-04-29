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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lifuz.adapter.AutoSearch;
import com.lifuz.utils.IsChinese;
import com.prd.aiyi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 词典页面
 * <p/>
 * Created by 半夏微凉 on 2015/4/26.
 */
public class CiDianFragment extends Fragment implements View.OnClickListener {


    private String url;

    private ImageButton search_button;
    private AutoSearch search_auto;

    private TextView tv;
    private TextView word_name, ph_am, ph_en, ph_zh, means;
    private TextView ph_am_yin, ph_en_yin, ph_zh_yin;

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

        switch (v.getId()) {

            case R.id.search_button:

                clearView();
                url = getUrl();
                Log.i("tag", url);


                JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("tag", response.toString());

                        try {
                            String error = response.getString("errno");

                            if ("0".equals(error)) {

                                JSONObject data = response.getJSONObject("data");


                                word_name.setVisibility(View.VISIBLE);
                                word_name.setText(search_auto.getText().toString());

                                JSONArray symbols = data.getJSONArray("symbols");
                                JSONObject sym = symbols.getJSONObject(0);

                                boolean flag = IsChinese.isChinese(search_auto.getText().toString());

                                StringBuilder sb = new StringBuilder();

                                if (flag) {
                                    String zh = sym.getString("ph_zh");

                                    if (null != zh) {
                                        ph_zh.setVisibility(View.VISIBLE);
                                        ph_zh_yin.setVisibility(View.VISIBLE);
                                        ph_zh_yin.setText("[" + zh + "]");
                                    }

                                    JSONArray parts = sym.getJSONArray("parts");


                                    for (int i = 0; i < parts.length(); i++) {

                                        JSONObject part = parts.getJSONObject(i);

                                        JSONArray means = part.getJSONArray("means");
                                        for (int j = 0; j < means.length(); j++) {
                                            sb.append(means.getString(j));

                                            sb.append("\n");


                                        }


                                    }
                                } else {
                                    String am = sym.getString("ph_am");
                                    if (null != am) {
                                        ph_am.setVisibility(View.VISIBLE);
                                        ph_am_yin.setVisibility(View.VISIBLE);
                                        ph_am_yin.setText("[" + am + "]");
                                    }

                                    String en = sym.getString("ph_en");
                                    if (null != en) {
                                        ph_en.setVisibility(View.VISIBLE);
                                        ph_en_yin.setVisibility(View.VISIBLE);
                                        ph_en_yin.setText("[" + en + "]");
                                    }

                                    JSONArray parts = sym.getJSONArray("parts");


                                    for (int i = 0; i < parts.length(); i++) {

                                        JSONObject part = parts.getJSONObject(i);
                                        sb.append(part.getString("part"));
                                        JSONArray means = part.getJSONArray("means");
                                        for (int j = 0; j < means.length(); j++) {
                                            sb.append(means.getString(j));

                                            if (j != means.length() - 1) {
                                                sb.append(";");
                                            }

                                        }

                                        sb.append("\n");

                                    }


                                    means.setVisibility(View.VISIBLE);
                                    means.setText(sb.toString());


                                }

                            } else {
                                Toast.makeText(getActivity(), "服务器出错", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            word_name.setVisibility(View.VISIBLE);
                            word_name.setText(search_auto.getText().toString());

                            means.setVisibility(View.VISIBLE);
                            means.setText("没有本地释义");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("tag", error.getMessage());
                    }
                });


                mqueue.add(jr);

        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mqueue = Volley.newRequestQueue(getActivity());

        initViews(view);

        tv = (TextView) view.findViewById(R.id.tv);
        search_button = (ImageButton) view.findViewById(R.id.search_button);
        search_auto = (AutoSearch) view.findViewById(R.id.search_auto);
        search_button.setOnClickListener(this);


    }

    public void clearView() {
        word_name.setVisibility(View.GONE);
        ph_am.setVisibility(View.GONE);
        ph_en.setVisibility(View.GONE);
        ph_zh.setVisibility(View.GONE);
        means.setVisibility(View.GONE);
        ph_am_yin.setVisibility(View.GONE);
        ph_en_yin.setVisibility(View.GONE);
        ph_zh_yin.setVisibility(View.GONE);
    }

    public void initViews(View view) {
        word_name = (TextView) view.findViewById(R.id.word_name);
        ph_am = (TextView) view.findViewById(R.id.ph_am);
        ph_en = (TextView) view.findViewById(R.id.ph_en);
        ph_zh = (TextView) view.findViewById(R.id.ph_zh);
        means = (TextView) view.findViewById(R.id.means);
        ph_am_yin = (TextView) view.findViewById(R.id.ph_am_yin);
        ph_en_yin = (TextView) view.findViewById(R.id.ph_en_yin);
        ph_zh_yin = (TextView) view.findViewById(R.id.ph_zh_yin);


    }

    public String getUrl() {
        //?client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=do&from=en&to=zh
        url = "http://openapi.baidu.com/public/2.0/translate/dict/simple?client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=danci&from=fm&to=top";

        String str = search_auto.getText().toString();


        boolean flag = IsChinese.isChinese(str);

        if (flag) {
            try {
                str = URLEncoder.encode(str, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            url = url.replace("danci", str);
            url = url.replace("fm", "zh");

            url = url.replace("top", "en");

            return url;


        } else {
            url = url.replace("danci", str);

            url = url.replace("fm", "en");

            url = url.replace("top", "zh");
            return url;
        }


    }
}
