package com.smart.cloud.fire.order.DealOrder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;

public class ChangeDealerActivity extends Activity {

    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.state_rg)
    RadioGroup state_rg;
    @Bind(R.id.no_rb)
    RadioButton no_rb;

    Context mContext;
    JobOrder order;
    ArrayList<DealerBean> dealerBeans;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dealer);

        mContext = this;
        order = (JobOrder) getIntent().getSerializableExtra("order");
        ButterKnife.bind(this);
        getDealerList();
    }

    @OnClick({R.id.commit_btn})
    public void onClick(View v) {
        commit();
    }

    private void commit() {
        String state="1";
        if(no_rb.isChecked()){
            state="2";
        }
        VolleyHelper helper = VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url ="";
        if(order.getState()==1){
            url = ConstantValues.SERVER_IP_NEW + "sendOrders?jkeys=" + order.getJkey()
                    + "&userId=" + userid
                    + "&ifCheck=" + state
                    + "&loginUserId=" + MyApp.getUserID();
        }else{
            url = ConstantValues.SERVER_IP_NEW + "updateSendUser?jkey=" + order.getJkey()
                    + "&repDealUserId=" + order.getDealUserId()
                    + "&nowUserId=" + userid
                    + "&ifCheck=" + state
                    + "&loginUserId=" + MyApp.getUserID();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            T.showShort(mContext, response.getString("error"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext, "网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }


    private void getDealerList() {
        VolleyHelper helper = VolleyHelper.getInstance(mContext);
        RequestQueue mQueue = helper.getRequestQueue();
        String url = ConstantValues.SERVER_IP_NEW + "getDealOrderUser?areaId=" + order.getAreaId();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int errorCode = response.getInt("errorcode");
                            String error = response.getString("error");
                            if (errorCode == 0) {
                                dealerBeans = new ArrayList<>();
                                JSONArray jsonArray = response.getJSONArray("list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DealerBean bean = new DealerBean(jsonArray.getJSONObject(i).getString("userId")
                                            , jsonArray.getJSONObject(i).getString("named"));
                                    dealerBeans.add(bean);
                                }
                                DealerAdapter adapter = new DealerAdapter(mContext, dealerBeans);
                                spinner.setAdapter(adapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                        userid = ((DealerBean) adapter.getItem(pos)).getUserId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            } else {
                                T.showShort(mContext, error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                T.showShort(mContext, "网络错误");
            }
        });
        mQueue.add(jsonObjectRequest);
    }
}
