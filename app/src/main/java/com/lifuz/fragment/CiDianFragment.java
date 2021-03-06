package com.lifuz.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.lifuz.bean.DanCiBen;
import com.lifuz.sqlite.DanCiBenService;
import com.lifuz.utils.IsChinese;
import com.prd.aiyi.HistoryActivity;
import com.prd.aiyi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


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
    private TextView cidian_history, cidian_add;

    private RequestQueue mqueue;

    private SharedPreferences share;
    private Editor editor;

    private String[] arr;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //获取页面布局
        View messageLayout = inflater.inflate(R.layout.cidian_layout,
                container, false);

        return messageLayout;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //搜索点击事件
            case R.id.search_button:

                clearView();
                //getUrl()方法封装了访问百度的url；
                url = getUrl(search_auto.getText().toString());
                Log.i("tag", url);

                //获取翻译的内容
                getContent(search_auto.getText().toString(), url);

                //把搜索的内容保存到sharePreferences文件中
                setHistory();


                break;

            //历史点击事件
            case R.id.cidian_history:

                startActivity(new Intent(getActivity(), HistoryActivity.class));

                break;

            case R.id.cidian_add:

                // 把单词放入单词本
                setDanci();


                break;


        }


    }

    /**
     * 把单词放入单词本，首先弹出对话框，选择单词本，如何检查单词本是否存在这个单词，把单词放入单词本
     */
    private void setDanci() {
        //连接数据库，并调用数据库的服务类
        final DanCiBenService ds = new DanCiBenService(getActivity());

        //获取单词本
        final List<DanCiBen> list = ds.queryDanciben();


        //如果单词本的个数为零，则新建一个单词本
        if (list.size() == 0) {
            ds.insert("默认单词本");
            arr = new String[]{"默认单词本"};
        } else {
            arr = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i).getName();
            }
        }


        //弹出一个对话框，选择要加入的单词本
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("添加到单词本")
                .setSingleChoiceItems(arr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("tag", arr[which]);

                        //检查这个单词在这个单词本中是否存在

                        boolean flag = ds.queryCheck(list.get(which).getId(), word_name.getText().toString());
                        if (!flag) {
                            //如果不存在，则添加到单词本
                            ds.insertDan(word_name.getText().toString(), list.get(which).getId());
                            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();

                        } else {
                            //若存在则告诉用户已存在
                            Toast.makeText(getActivity(), "单词本已包含这个词语", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();

                    }
                });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        builder.create().show();
    }

    /**
     * 封装了把内容放入sharePreferences中，并且去重
     */
    private void setHistory() {
        //把搜索的内容保存到sharePreferences文件中，并且去重
        StringBuilder sb = new StringBuilder();
        String history = share.getString("history", "");
        String[] strs = history.split(",");
        sb.append(search_auto.getText().toString());
        for (String str : strs) {

            if (!search_auto.getText().toString().equals(str)) {
                sb.append("," + str);
            }
        }

        editor.putString("history", sb.toString());
        editor.commit();
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
                        Toast.makeText(getActivity(), "服务器出错", Toast.LENGTH_SHORT).show();
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



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 将对象注册到事件总线中， ****** 注意要在onDestory中进行注销 ****
        EventBus.getDefault().register(this);

        //初始化访问网络的栈
        mqueue = Volley.newRequestQueue(getActivity());

        //获取sharpreferences对我们查找的容纳进行保存，文件的模式为私有模式，保存的文件是history.xml
        share = getActivity().getSharedPreferences("history", Activity.MODE_PRIVATE);
        //获取sharedpreferences的编辑器，对share文件进行编辑
        editor = share.edit();


        initViews(view);


    }

    /**
     * 在重新查找的时候，把当前显示的内容清除
     */
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

    /**
     * 对布局文件里的组件进行初始化
     *
     * @param view
     */
    public void initViews(View view) {
        word_name = (TextView) view.findViewById(R.id.word_name);
        ph_am = (TextView) view.findViewById(R.id.ph_am);
        ph_en = (TextView) view.findViewById(R.id.ph_en);
        ph_zh = (TextView) view.findViewById(R.id.ph_zh);
        means = (TextView) view.findViewById(R.id.means);
        ph_am_yin = (TextView) view.findViewById(R.id.ph_am_yin);
        ph_en_yin = (TextView) view.findViewById(R.id.ph_en_yin);
        ph_zh_yin = (TextView) view.findViewById(R.id.ph_zh_yin);
        tv = (TextView) view.findViewById(R.id.tv);
        search_button = (ImageButton) view.findViewById(R.id.search_button);
        search_auto = (AutoSearch) view.findViewById(R.id.search_auto);
        search_button.setOnClickListener(this);

        cidian_history = (TextView) view.findViewById(R.id.cidian_history);
        cidian_add = (TextView) view.findViewById(R.id.cidian_add);
        cidian_add.setOnClickListener(this);

        cidian_history.setOnClickListener(this);


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
