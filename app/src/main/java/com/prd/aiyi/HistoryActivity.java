package com.prd.aiyi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 搜索历史activity
 *
 * Created by 半夏微凉 on 2015/4/29.
 */
public class HistoryActivity extends  BaseActivity  implements View.OnClickListener{

    private ArrayAdapter<String> adapter;

    private SharedPreferences share;
    private Editor editor;

    private ListView history_list;
    private TextView history_back,history_clear;
    private String[] strs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        share = getSharedPreferences("history", Activity.MODE_PRIVATE);
        editor = share.edit();

        history_list = (ListView) findViewById(R.id.history_list);
        history_back = (TextView) findViewById(R.id.history_back);
        history_clear = (TextView) findViewById(R.id.history_clear);
        history_back.setOnClickListener(this);
        history_clear.setOnClickListener(this);



        //给listView加载适配器
        String history = share.getString("history", "");
        System.out.println(history);
        strs = history.split(",");


        adapter = new ArrayAdapter<String>(this,R.layout.history_item,strs);

        history_list.setAdapter(adapter);

        history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(HistoryActivity.this,MainActivity.class);
                it.putExtra("type",0);
                it.putExtra("content",strs[position]);
                startActivity(it);
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.history_back:
                finish();
                break;
            case R.id.history_clear:

                Builder builder = new Builder(this);
                builder.setTitle("提示");
                builder.setMessage("你确定要删除所有历史记录吗？");
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if (arg1 == DialogInterface.BUTTON_POSITIVE) {
                            arg0.cancel();
                        } else if (arg1 == DialogInterface.BUTTON_NEGATIVE) {

                            editor.putString("history","");
                            editor.commit();
                            strs = new String[]{};
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "删除完成！", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                builder.setPositiveButton("取消", dialog);
                builder.setNegativeButton("确定", dialog);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
        }

    }
}
