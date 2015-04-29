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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prd.aiyi.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 半夏微凉 on 2015/4/26.
 */
public class CiDianFragment extends Fragment implements View.OnClickListener {

    private AsyncHttpClient client;
    private String url;
    private RequestParams params;

    private ImageButton search_button;

    private TextView tv;

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

        tv.setText("点击事件发生");
        client.get("http://openapi.baidu.com/public/2.0/translate/dict/simple?client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=do&from=en&to=zh", new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                tv.setText("网络访问失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Toast.makeText(getActivity(),"网络访问成功",Toast.LENGTH_SHORT).show();

                tv.setText("网络访问成功");

                try {
                    JSONObject object = response.getJSONObject(0);
                    JSONObject data = object.getJSONObject("data");
                    String word_name = data.getString("word_name");
                    Log.i("word", word_name);

                    tv.setText(word_name);
                } catch (JSONException e){
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = new AsyncHttpClient();
        params = new RequestParams();

        tv =(TextView) view.findViewById(R.id.tv);
        search_button = (ImageButton)view.findViewById(R.id.search_button);
        search_button.setOnClickListener(this);

        //?client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=do&from=en&to=zh
        url = "http://openapi.baidu.com/public/2.0/translate/dict/simple";

        params.add("client_id", "E4H1r1qNL5XUo64s2o9SUK7F");
        params.add("q", "do");
        params.add("from", "en");
        params.add("to", "zn");



    }
}
