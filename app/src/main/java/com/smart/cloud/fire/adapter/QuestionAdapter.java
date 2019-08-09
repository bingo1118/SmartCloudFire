package com.smart.cloud.fire.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smart.cloud.fire.global.Question;
import com.smart.cloud.fire.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<Question> list;

    public QuestionAdapter(Context mContext, List<Question> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.question_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Question question=list.get(position);
        ((MyViewHolder) holder).quession_text.setText(question.getQdetail());
        ((MyViewHolder) holder).question_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.yes_rb:
                        list.get(position).setAnswer(0);
                        break;
                    case R.id.no_rb:
                        list.get(position).setAnswer(1);
                        break;
                }
            }
        });
    }


    public String getAnwserJson(){
        List<Map<String,String>> maps=new ArrayList<>();
        Gson gson=new Gson();

        for (Question q:list) {
            if(q.getAnswer()>2) return null;
            Map<String,String> map=new HashMap<>();
            map.put(q.getQid()+"",q.getAnswer()+"");
            maps.add(map);
        }
        return gson.toJson(maps);
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }else{
            return list.size();
        }
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.quession_text)
        TextView quession_text;
        @Bind(R.id.yes_rb)
        RadioButton yes_rb;
        @Bind(R.id.no_rb)
        RadioButton no_rb;
        @Bind(R.id.question_rg)
        RadioGroup question_rg;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
