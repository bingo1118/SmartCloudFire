package com.smart.cloud.fire.order.DealOrder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.blankj.utilcode.util.StringUtils;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.order.JobOrder;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.UploadUtil;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fire.cloud.smart.com.smartcloudfire.R;

public class CheakOrderActivity extends Activity {

    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.memo_name)
    EditText memo_name;//@@备注
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;
    @Bind(R.id.state_rg)
    RadioGroup state_rg;
    @Bind(R.id.no_rb)
    RadioButton no_rb;

    @Bind(R.id.change_line)
    LinearLayout change_line;
    @Bind(R.id.change_no_rb)
    RadioButton change_no_rb;
    @Bind(R.id.change_yes_rb)
    RadioButton change_yes_rb;
    @Bind(R.id.change_rg)
    RadioGroup change_rg;
    @Bind(R.id.spinner)
    Spinner spinner;

    private Context mContext;
    private JobOrder order;
    ArrayList<DealerBean> dealerBeans;
    String userid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheak_order);

        ButterKnife.bind(this);
        mContext = this;

        order= (JobOrder) getIntent().getSerializableExtra("order");
        getDealerList();
        initView();
    }




    private void initView() {

        addFireDevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state="1";
                if(no_rb.isChecked()){
                    state="2";
                }
                if(state.equals("2")&&change_yes_rb.isChecked()){
                    userid=userid;
                }else{
                    userid=order.getDealUserId();
                }
                VolleyHelper helper = VolleyHelper.getInstance(mContext);
                RequestQueue mQueue = helper.getRequestQueue();
                String url = ConstantValues.SERVER_IP_NEW + "checkOrder?jkey=" + order.getJkey()
                        + "&status=" +state
                        + "&description=" + URLEncoder.encode(memo_name.getText().toString())
                        + "&preUserId=" + order.getDealUserId()
                        + "&nowUserId=" + userid
                        + "&loginUserId=" + MyApp.getUserID()
                        + "&type="+order.getType();


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int errorCode = response.getInt("errorcode");
                                    String error = response.getString("error");
                                    if (errorCode == 0) {
                                        T.showShort(mContext, "成功");
                                        clearView();
                                    } else {
                                        T.showShort(mContext, error);
                                    }
                                } catch (JSONException e) {
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
        });

        state_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.yes_rb){
                    change_line.setVisibility(View.GONE);
                }else{
                    change_line.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void clearView() {
        memo_name.setText("");
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
