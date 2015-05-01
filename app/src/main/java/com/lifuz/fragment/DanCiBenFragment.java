package com.lifuz.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lifuz.bean.DanCiBen;
import com.lifuz.sqlite.DanCiBenService;
import com.prd.aiyi.R;

import java.util.List;


/**
 * Created by 半夏微凉 on 2015/4/26.
 */
public class DanCiBenFragment extends Fragment implements View.OnClickListener {

    private TextView danciben_add;

    private ListView danciben_lv;

    private MyListAdapter adapter;

    private List<DanCiBen> listItems;
    private DanCiBenService ds;

    private EditText addname;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View messageLayout = inflater.inflate(R.layout.danciben_layout,
                container, false);

        return messageLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        danciben_add = (TextView) view.findViewById(R.id.danciben_add);
        danciben_add.setOnClickListener(this);
        danciben_lv = (ListView) view.findViewById(R.id.danciben_lv);

        ds = new DanCiBenService(getActivity());
        listItems = ds.queryDanciben();

        addname = (EditText) getActivity().getLayoutInflater().inflate(R.layout.danciben_add_item, null);

        adapter = new MyListAdapter();
        danciben_lv.setAdapter(adapter);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //添加事件被点击
            case R.id.danciben_add:

                final EditText addname = (EditText) getActivity().getLayoutInflater().inflate(R.layout.danciben_add_item, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("添加单词本")
                        .setView(addname)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DanCiBenService ds = new DanCiBenService(getActivity());
                                ds.insert(addname.getText().toString());
                                listItems = ds.queryDanciben();
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder.create().show();


                break;
        }
    }


    private class MyListAdapter extends BaseAdapter {

        public class ListItemView {
            public TextView danciben_name;
            public TextView danciben_opreate;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ListItemView listItemView = null;
            if (convertView == null) {
                listItemView = new ListItemView();
                // 获取list_item布局文件的视图
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.danciben_item, null);
                listItemView.danciben_name = (TextView) convertView.findViewById(R.id.danciben_name);
                listItemView.danciben_opreate = (TextView) convertView.findViewById(R.id.danciben_opreate);
                // 设置控件集到convertView
                convertView.setTag(listItemView);
            } else {
                listItemView = (ListItemView) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tag", listItems.get(position).getName());
                }
            });

            listItemView.danciben_name.setText(listItems.get(position).getName());

            listItemView.danciben_opreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] strArr = new String[]{"重命名单词本", "删除单词本"};

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("词组")
                            .setItems(strArr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("tag", strArr[which]);

                                    dialog.dismiss();


                                    if (strArr[which].equals("重命名单词本")) {

//                                        addname = (EditText) getActivity().getLayoutInflater().inflate(R.layout.danciben_add_item, null);
//                                        ((ViewGroup) addname.getParent()).addView(addname);
                                        AlertDialog.Builder update = new AlertDialog.Builder(getActivity())
                                                .setTitle("你想将词组重命名为")
                                                .setView(addname)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        DanCiBen dc = new DanCiBen();
                                                        dc.setId(listItems.get(position).getId());
                                                        dc.setName(addname.getText().toString());

                                                        ds.updateDanciben(dc);
                                                        listItems = ds.queryDanciben();

                                                        adapter.notifyDataSetChanged();

                                                    }
                                                });

                                        update.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                ((ViewGroup) addname.getParent()).removeView(addname);
                                            }
                                        });


                                        update.create().show();




                                    } else if (strArr[which].equals("删除单词本")) {


                                        AlertDialog.Builder delete = new AlertDialog.Builder(getActivity())

                                                .setMessage("确定删除该分类吗？")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ds.deleteDanciben(listItems.get(position).getId());
                                                        listItems = ds.queryDanciben();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });

                                        delete.create().show();
                                    }

                                    dialog.dismiss();

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                    builder.create().show();
                }
            });
            return convertView;
        }
    }

}
