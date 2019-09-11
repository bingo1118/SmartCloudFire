package com.smart.cloud.fire.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class BingoSerttingDialog extends Dialog{

    @Bind(R.id.title_text)
    TextView title_text;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.commit)
    Button commit;

    public Activity mActivity;
    public List<SettingItem> itemlist;
    public String mTitle;

    public OnCommitListener mOnCommitListener;
    public interface OnCommitListener{
        public void onCimmit(List<SettingItem> items, boolean isTrueData);
    }

    public void setmOnCommitListener(OnCommitListener mOnCommitListener) {
        this.mOnCommitListener = mOnCommitListener;
    }

    public BingoSerttingDialog(Activity context,List<SettingItem> items,String title) {
        super(context);
        this.itemlist=items;
        this.mActivity=context;
        this.mTitle=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_setting_custom);
        this.setCanceledOnTouchOutside(true); // 点击外部会消失
        ButterKnife.bind(this);
        Window mWindow = getWindow();
        mWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setBackgroundDrawableResource(android.R.color.white);
        mWindow.setAttributes(lp);
        initViews();
    }

    public void initViews(){
        if(mTitle!=null&&mTitle.length()>0){
            title_text.setText(mTitle);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SettingDialogAdapter adapter=new SettingDialogAdapter(mActivity,itemlist);
        recyclerView.setAdapter(adapter);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnCommitListener!=null){
                    boolean isTrueData=true;
                    try{
                        for (SettingItem item:itemlist) {
                            if(item.max!=0&&item.min!=0){
                                float content=Float.parseFloat(item.getContent());
                                if(content<item.min||content>item.max){
                                    isTrueData=false;
                                    break;
                                }
                            }
                        }
                    }catch (Exception e){
                        e.getStackTrace();
                        isTrueData=false;
                    }
                    mOnCommitListener.onCimmit(itemlist,isTrueData);
                }
            }
        });
    }

    public List<SettingItem> getItems() {
        return itemlist;
    }

    class SettingDialogAdapter extends RecyclerView.Adapter<SettingDialogAdapter.ItemViewHolder>{

        private Context mContext;
        private List<SettingItem> items;

        public SettingDialogAdapter(Context mContext, List<SettingItem> items) {
            this.mContext = mContext;
            this.items = items;
        }


        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_setting_item, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            SettingItem item=items.get(position);
            holder.tv_name.setText(item.getName()+":");
            if(item.demand.length()!=0){
                holder.tv_demind.setText("("+item.demand+")");
            }else{
                holder.tv_demind.setText("");
            }

            if(item.getContent()!=null&&item.getContent().length()!=0){
                holder.et_value.setText(item.getContent());
            }

            holder.et_value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null && !"".equals(s.toString())) {
                        itemlist.get(position).setContent(s.toString());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{

            private TextView tv_name;
            private TextView tv_demind;
            private EditText et_value;

            public ItemViewHolder(View itemView) {
                super(itemView);
                tv_name=(TextView) itemView.findViewById(R.id.tv_name);
                tv_demind=(TextView) itemView.findViewById(R.id.tv_demind);
                et_value=(EditText) itemView.findViewById(R.id.et_value);
            }
        }
    }

    public static class SettingItem{

        private String Name;
        private String content;
        private String demand;
        private int max;
        private int min;

        public SettingItem(String name, String content, String demand, int max, int min) {
            Name = name;
            this.content = content;
            this.demand = demand;
            this.max = max;
            this.min = min;
        }


        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDemand() {
            return demand;
        }

        public void setDemand(String demand) {
            this.demand = demand;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

    }
}
