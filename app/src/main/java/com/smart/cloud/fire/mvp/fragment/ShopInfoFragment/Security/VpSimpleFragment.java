package com.smart.cloud.fire.mvp.fragment.ShopInfoFragment.Security;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fire.cloud.smart.com.smartcloudfire.R;

public class VpSimpleFragment extends Fragment {
	private String mTitle;
	public static final String BUNDLE_TITLE="title";
	public static final String BUNDLE_MAC="mac";
	String devMac;
	private TextView date1,date2,date3,date4,date5,date6;
	private TextView min1,min2,min3,min4,min5,min6;
	private TextView max1,max2,max3,max4,max5,max6;
	private TextView iaq1,iaq2,iaq3,iaq4,iaq5,iaq6;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle=getArguments();
		if(bundle!=null){
			mTitle=bundle.getString(BUNDLE_TITLE);
			devMac=bundle.getString(BUNDLE_MAC);
		}
		View view =inflater.inflate(R.layout.air_info_list, container, false);
		initView(view);
		initData(devMac);
		return view;
	}
	
	private void initData(String mac) {
		String url= ConstantValues.SERVER_IP_NEW+"getEnvironmentHistory"+"?airMac="+mac;
		VolleyHelper helper=VolleyHelper.getInstance(getActivity());
		RequestQueue mQueue = helper.getRequestQueue();
//		RequestQueue mQueue = Volley.newRequestQueue(getActivity());
		JsonObjectRequest mJsonRequest = new JsonObjectRequest(Method.GET,
				url, 
				null, 
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						int errorCode;
						try {
							errorCode = jsonObject.getInt("errorCode");
							if(errorCode==0){
								JSONArray info=jsonObject.getJSONArray(mTitle);
								TextView[] date={date1,date2,date3,date4,date5,date6};
								TextView[] min={min1,min2,min3,min4,min5,min6};
								TextView[] max={max1,max2,max3,max4,max5,max6};
								TextView[] iaq={iaq1,iaq2,iaq3,iaq4,iaq5,iaq6};
								for(int i=0;i<info.length();i++){
									JSONObject every_info=info.getJSONObject(i);
									date[i].setText(every_info.getString("time"));
									min[i].setText(every_info.getString("min"));
									max[i].setText(every_info.getString("max"));
									iaq[i].setText(changeQuality(every_info.getString("quality")));
								}
							}else{
								Toast.makeText(getActivity(),"无数据", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getActivity(),"获取失败" , Toast.LENGTH_SHORT).show();
						}
						
						
					}

					
				}, 
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
					}
				});
		mQueue.add(mJsonRequest);
	}

	private void initView(View view) {
		date1=(TextView)view.findViewById(R.id.date_1);
		date2=(TextView)view.findViewById(R.id.date_2);
		date3=(TextView)view.findViewById(R.id.date_3);
		date4=(TextView)view.findViewById(R.id.date_4);
		date5=(TextView)view.findViewById(R.id.date_5);
		date6=(TextView)view.findViewById(R.id.date_6);
		min1=(TextView)view.findViewById(R.id.min_1);
		min2=(TextView)view.findViewById(R.id.min_2);
		min3=(TextView)view.findViewById(R.id.min_3);
		min4=(TextView)view.findViewById(R.id.min_4);
		min5=(TextView)view.findViewById(R.id.min_5);
		min6=(TextView)view.findViewById(R.id.min_6);
		max1=(TextView)view.findViewById(R.id.max_1);
		max2=(TextView)view.findViewById(R.id.max_2);
		max3=(TextView)view.findViewById(R.id.max_3);
		max4=(TextView)view.findViewById(R.id.max_4);
		max5=(TextView)view.findViewById(R.id.max_5);
		max6=(TextView)view.findViewById(R.id.max_6);
		iaq1=(TextView)view.findViewById(R.id.iaq_1);
		iaq2=(TextView)view.findViewById(R.id.iaq_2);
		iaq3=(TextView)view.findViewById(R.id.iaq_3);
		iaq4=(TextView)view.findViewById(R.id.iaq_4);
		iaq5=(TextView)view.findViewById(R.id.iaq_5);
		iaq6=(TextView)view.findViewById(R.id.iaq_6);
	}

	public static VpSimpleFragment newInstance(String title, String devMac){
		Bundle bundle=new Bundle();
		bundle.putString(BUNDLE_TITLE, title);
		bundle.putString(BUNDLE_MAC, devMac);
		VpSimpleFragment vpSimpleFragment=new VpSimpleFragment();
		vpSimpleFragment.setArguments(bundle);
		return vpSimpleFragment;
	}

	public VpSimpleFragment() {
		super();
	}


	
	private CharSequence changeQuality(String string) {
		String quality="--";
		switch (string) {
		case "1":
			quality="优";
			break;
		case "2":
			quality="良";
			break;
		case "3":
			quality="中";
			break;
		case "4":
			quality="差";
			break;

		default:
			break;
		}
		return quality;
	}

}
