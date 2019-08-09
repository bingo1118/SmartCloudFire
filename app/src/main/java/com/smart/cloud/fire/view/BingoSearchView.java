package com.smart.cloud.fire.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.utils.ListDataSave;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2019/5/8.
 */
public class BingoSearchView extends FrameLayout{

    @Bind(R.id.edit)
    EditText edit;
    @Bind(R.id.img_cancel)
    ImageView img_cancel;
    @Bind(R.id.img_goahead)
    ImageView img_goahead;

    private PopupWindow popWnd;
    private Context context;
    private ListView listview;
    private TextView clear_history;
    List<String> data = new ArrayList<>();

    public void setListener(OnGetSearchTextListener listener) {
        this.listener = listener;
    }

    private OnGetSearchTextListener listener;
    public interface OnGetSearchTextListener{
        public void onGetText(String text);
    }

    public BingoSearchView(Context context) {
        super(context);
        this.context=context;
        initView(context,null);
    }

    public BingoSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView(context,attrs);
    }

    public BingoSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initView(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BingoSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;
        initView(context,attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        if(attrs==null) return;
        View v= LayoutInflater.from(context).inflate(R.layout.bingo_search_view,this,false);
        ButterKnife.bind(this,v);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.BingoSearchView);
        String hintText=typedArray.getString(R.styleable.BingoSearchView_edit_hint_text);
        typedArray.recycle();
        edit.setHint(hintText);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    img_cancel.setVisibility(VISIBLE);
                    if(popWnd!=null&&popWnd.isShowing()){
                        changeListViewData(s.toString());
                    }else{
                        showPopupWindow(context,s.toString());
                    }
                }else{
                    img_cancel.setVisibility(GONE);
                    popWnd.dismiss();
                }
            }
        });
        addView(v);
    }

    private void changeListViewData(String string) {
        listview.setAdapter(new ArrayAdapter<String>(context, R.layout.listitem,getData(context,string)));
    }

    private void showPopupWindow(final Context context, String string) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow, null);
        if(popWnd==null){
            popWnd = new PopupWindow(context);
        }
        popWnd.setContentView(contentView);
        popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup));
        popWnd.setOutsideTouchable(true);
//        popWnd.setAnimationStyle(R.style.contextMenuAnim);
        clear_history=(TextView) contentView.findViewById(R.id.clear_history);
        clear_history.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ListDataSave(context,"bingo").setDataList("list",new ArrayList<String>());
                popWnd.dismiss();
            }
        });
        listview=(ListView) contentView.findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit.setText(data.get(position));
                edit.setSelection(data.get(position).length());
                popWnd.dismiss();
            }
        });
        listview.setAdapter(new ArrayAdapter<String>(context, R.layout.listitem,getData(context,string)));
        //相对某个控件的位置（正左下方），无偏移
        if(getData(context,string).size()>0){
            popWnd.showAsDropDown(this);
        }
    }

    private List<String> getData(Context context, String s) {
        List<String> temp = new ListDataSave(context,"bingo").getDataList("list");
        data.clear();
        for(int i=0;i<temp.size();i++){
            String c=temp.get(i);
            int b=c.indexOf(s);
            if(b>-1) data.add(temp.get(i));
        }
        return data;
    }

    @OnClick({R.id.img_goahead,R.id.img_cancel})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.img_goahead:
                List<String> temp = new ListDataSave(context,"bingo").getDataList("list");
                if(!temp.contains(edit.getText().toString())){
                    temp.add(edit.getText().toString());
                }
                new ListDataSave(context,"bingo").setDataList("list",temp);
                String text=edit.getText().toString();
                if(listener!=null){
                    listener.onGetText(text);
                }
                popWnd.dismiss();
                break;
            case R.id.img_cancel:
                edit.setText("");
                popWnd.dismiss();
                break;
        }
    }


}
