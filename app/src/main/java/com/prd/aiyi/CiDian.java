package com.prd.aiyi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lifuz.utils.IsChinese;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 半夏微凉 on 2015/5/1.
 */
public class CiDian extends BaseActivity {

    private String url;
    private TextView tv;
    private TextView word_name, ph_am, ph_en, ph_zh, means;
    private TextView ph_am_yin, ph_en_yin, ph_zh_yin;
    private TextView cidian_history, cidian_add;
    private RequestQueue mqueue;
    private String[] arr;

    private LinearLayout cidian_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cidian_layout);

        //初始化访问网络的栈
        mqueue = Volley.newRequestQueue(this);
        initViews();

        Intent it = getIntent();

        String name = it.getStringExtra("danci");

        url = getUrl(name);
        Log.i("tag", url);

        //获取翻译的内容
        getContent(name, url);


    }

    //跟百度服务器进行交互，获取翻译内容
    private void getContent(final String content, String url) {

        //封装网络访问的方法，根据返回值选择相应的类，如果返回的是JsonObject，则用JsonObjectRequest类进行网络访问
        //如果返回的是JsonArray，则用JsonArrayRequest类进行访问，这两个类在初始化时，都有相同的五个参数，分别是：
        //第一个参数：是http访问网络是的方式；第二个参数：是访问网络的url；第三个参数：这是访问网络的参数，如果
        //是get请求，则应该把他设为空，如果是post请求则填写post请求的参数；第四个参数：如果网络返回成功，并有返回值
        //则在这个类的方法里处理；第五个参数：如果网络访问失败，则在这个类里，进行相应的处理


        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("tag", response.toString());

                try {
                    String error = response.getString("errno");

                    if ("0".equals(error)) {

                        JSONObject data = response.getJSONObject("data");

                        Log.i("tag", data.toString());
                        word_name.setVisibility(View.VISIBLE);
                        Log.i("tag", content);
                        word_name.setText(content);

                        JSONArray symbols = data.getJSONArray("symbols");
                        JSONObject sym = symbols.getJSONObject(0);

                        //根据输入的字符串是否包含中文字符，来确定返回的json里的参数
                        boolean flag = IsChinese.isChinese(content);

                        StringBuilder sb = new StringBuilder();

                        if (flag) {
                            //如果是中英方向的，则在这个部分进行处理
                            String zh = sym.getString("ph_zh");

                            //这个是获取中文发音
                            if (null != zh) {
                                ph_zh.setVisibility(View.VISIBLE);
                                ph_zh_yin.setVisibility(View.VISIBLE);
                                ph_zh_yin.setText("[" + zh + "]");
                            }

                            //这个是中文译成英文部分
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

                            //这个是英中方向的
                            //获取美式发音
                            String am = sym.getString("ph_am");
                            if (null != am) {
                                ph_am.setVisibility(View.VISIBLE);
                                ph_am_yin.setVisibility(View.VISIBLE);
                                ph_am_yin.setText("[" + am + "]");
                            }

                            //获取英式发音
                            String en = sym.getString("ph_en");
                            if (null != en) {
                                ph_en.setVisibility(View.VISIBLE);
                                ph_en_yin.setVisibility(View.VISIBLE);
                                ph_en_yin.setText("[" + en + "]");
                            }

                            //获取单词的中文意思
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


                        }

                        means.setVisibility(View.VISIBLE);
                        means.setText(sb.toString());

                        cidian_add.setVisibility(View.VISIBLE);

                    } else {
                        //从百度服务器获取信息失败
                        Toast.makeText(getApplicationContext(), "服务器出错", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    //从百度服务器获取信息，data的值为空
                    word_name.setVisibility(View.VISIBLE);
                    word_name.setText(content);

                    means.setVisibility(View.VISIBLE);
                    means.setText("没有本地释义");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //网络访问失败，把失败的原因输出到控制台
                Log.i("tag", error.getMessage());
            }
        });


        //进行网络访问
        mqueue.add(jr);
    }

    /**
     * 对布局文件里的组件进行初始化
     */
    public void initViews() {
        word_name = (TextView) findViewById(R.id.word_name);
        ph_am = (TextView) findViewById(R.id.ph_am);
        ph_en = (TextView) findViewById(R.id.ph_en);
        ph_zh = (TextView) findViewById(R.id.ph_zh);
        means = (TextView) findViewById(R.id.means);
        ph_am_yin = (TextView) findViewById(R.id.ph_am_yin);
        ph_en_yin = (TextView) findViewById(R.id.ph_en_yin);
        ph_zh_yin = (TextView) findViewById(R.id.ph_zh_yin);
        tv = (TextView) findViewById(R.id.tv);

        cidian_layout = (LinearLayout) findViewById(R.id.search_top_layout);
        cidian_layout.setVisibility(View.GONE);


        cidian_history = (TextView) findViewById(R.id.cidian_history);
        cidian_add = (TextView) findViewById(R.id.cidian_add);

        cidian_add.setVisibility(View.GONE);
        cidian_history.setText("返回");
        cidian_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    /**
     * 根据用户输入的值确定访问百度的url
     *
     * @return 访问百度的url
     */
    public String getUrl(String content) {
        //?client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=do&from=en&to=zh
        //定义一个统一的url
        url = "http://openapi.baidu.com/public/2.0/translate/dict/simple?client_id=E4H1r1qNL5XUo64s2o9SUK7F&q=danci&from=fm&to=top";


        //判断输入的值有没有中文字符串
        boolean flag = IsChinese.isChinese(content);

        //如果flag为真，则输入的值里包含中文字符串；反之，则不包括
        if (flag) {
            try {
                //对含有中文字符串的进行URLCncode编码
                content = URLEncoder.encode(content, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            //对含有中文字符串的输入进行url进行封装，并返回
            url = url.replace("danci", content);
            url = url.replace("fm", "zh");

            url = url.replace("top", "en");

            return url;


        } else {
            //对不含有中文字符串的输入进行封装
            url = url.replace("danci", content);

            url = url.replace("fm", "en");

            url = url.replace("top", "zh");
            return url;
        }


    }


}
