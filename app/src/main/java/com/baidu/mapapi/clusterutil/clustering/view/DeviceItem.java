package com.baidu.mapapi.clusterutil.clustering.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;

import fire.cloud.smart.com.smartcloudfire.R;

/**
 * Created by Rain on 2018/9/13.
 */
public class DeviceItem implements ClusterItem {
    private Context context;
    private final LatLng mPosition;
    public DeviceItem(LatLng latLng) {
        mPosition = latLng;
    }

    // 返回标记点的坐标<span style="white-space:pre">	</span>
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    // 返回标记点的标记图标，可以自定义一个View上面叠加自己的内容
    @Override
    public BitmapDescriptor getBitmapDescriptor() {

        return BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
    }
}
