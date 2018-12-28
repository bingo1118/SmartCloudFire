package com.smart.cloud.fire.activity.Functions.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.smart.cloud.fire.activity.Functions.FunctionsActivity;
import com.smart.cloud.fire.activity.Functions.model.ApplyTable;
import com.smart.cloud.fire.activity.Functions.model.MainModel;

import java.util.List;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * 首页适配器
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private List<MainModel> mainModels;
    private List<ApplyTable> applyTables;
    //头布局模式
    private static final int TYPE_HEADER = 1;
    //数据
    private static final int TYPE_LIST = 2;

    public MainAdapter(Context context, List<MainModel> list, List<ApplyTable> tables) {
        this.context = context;
        this.mainModels = list;
        this.applyTables = tables;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 设置头部数据
     * @param tables
     */
    public void setTables(List<ApplyTable> tables){
        this.applyTables = tables;
    }
    @Override
    public int getItemViewType(int position) {
        if (mainModels.get(position).getTyp() == 0)
            return TYPE_HEADER;
        else
            return TYPE_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.item_main_head, parent, false));
            case TYPE_LIST:
                return new MainViewHolder(inflater.inflate(R.layout.item_list_string, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            setHeaderItemValues((HeaderViewHolder) holder);
        } else if (holder instanceof MainViewHolder) {
            setDataList((MainViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mainModels == null ? 0 : mainModels.size();
    }

    private void setDataList(MainViewHolder holder, int position) {
        holder.tv.setText(mainModels.get(position).getName());
    }

    //设置头布局的值
    private void setHeaderItemValues(HeaderViewHolder holder) {
        ApplyGridAdapter mAdapter = new ApplyGridAdapter(applyTables, context);
        holder.mGridView.setAdapter(mAdapter);
        //解决GridView只显示一行的原因 主动设置GridView的高度
        ViewGroup.LayoutParams params = holder.mGridView.getLayoutParams();
        View view = mAdapter.getView(0, null, holder.mGridView);
        view.measure(0, 0);
        int height = view.getMeasuredHeight();
        int num;
        if (applyTables.size() % 4 == 0)
            num = applyTables.size() / 4;
        else num = applyTables.size() / 4 + 1;
        Log.i("eee", "mTables.size()=" + applyTables.size());
        Log.i("eee", "num=" + num);
        int totalHeight = holder.mGridView.getVerticalSpacing() * num + height * num;
        params.height = totalHeight;
        holder.mGridView.setLayoutParams(params);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private GridView mGridView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mGridView = (GridView) itemView.findViewById(R.id.gv_news_channel);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    String name = applyTables.get(position).getName();
                    if (name.contains("全部")) {
                        Toast.makeText(context, "切换到全部页面：" + name, Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, FunctionsActivity.class));
                    } else {
                        //跳转到相应界面
                        Toast.makeText(context, "跳转到相应界面：" + name, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public MainViewHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.tv);
        }
    }
}
