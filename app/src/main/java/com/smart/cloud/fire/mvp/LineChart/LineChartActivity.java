package com.smart.cloud.fire.mvp.LineChart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.cloud.fire.base.ui.MvpActivity;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.global.TemperatureTime;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fire.cloud.smart.com.smartcloudfire.R;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;

/**
 * Created by Administrator on 2016/11/1.
 */
public class LineChartActivity extends MvpActivity<LineChartPresenter> implements LineChartView {
    /*=========== 控件相关 ==========*/
    @Bind(R.id.lvc_main)
    lecho.lib.hellocharts.view.LineChartView mLineChartView;//线性图表控件
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.btn_next)
    ImageView btnNext;
    @Bind(R.id.btn_before)
    ImageView btnBefore;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.btn_new)
    ImageView btnNew;
    private LineChartPresenter lineChartPresenter;

    /*=========== 数据相关 ==========*/
    private LineChartData mLineData;                    //图表数据
    private int numberOfLines = 1;                      //图上折线/曲线的显示条数
    private int maxNumberOfLines = 4;                   //图上折线/曲线的最多条数
    private int numberOfPoints = 8;                    //图上的节点数

    /*=========== 状态相关 ==========*/
    private boolean isHasAxes = true;                   //是否显示坐标轴
    private boolean isHasAxesNames = true;              //是否显示坐标轴名称
    private boolean isHasLines = true;                  //是否显示折线/曲线
    private boolean isHasPoints = true;                 //是否显示线上的节点
    private boolean isFilled = true;                   //是否填充线下方区域
    private boolean isHasPointsLabels = false;          //是否显示节点上的标签信息
    private boolean isCubic = false;                    //是否是立体的
    private boolean isPointsHasSelected = false;        //设置节点点击后效果(消失/显示标签)
    private boolean isPointsHaveDifferentColor;         //节点是否有不同的颜色

    /*=========== 其他相关 ==========*/
    private ValueShape pointsShape = ValueShape.CIRCLE; //点的形状(圆/方/菱形)
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints]; //将线上的点放在一个数组中
    private Context context;
    private String userID;
    private int privilege;
    private String electricMac;
    private String electricType;
    private String electricNum;
    private int page = 1;
    private List<TemperatureTime.ElectricBean> electricBeen;
    private boolean haveDataed = true;
    private Map<Integer, String> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.bind(this);
        context = this;
        userID = SharedPreferencesManager.getInstance().getData(context,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        privilege = MyApp.app.getPrivilege();
        electricMac = getIntent().getExtras().getString("electricMac");
        electricType = getIntent().getExtras().getInt("electricType") + "";
        electricNum = getIntent().getExtras().getInt("electricNum") + "";
        electricBeen = new ArrayList<>();
        mvpPresenter.getElectricTypeInfo(userID, privilege + "", electricMac, electricType, electricNum, page + "", false);
        initView();
        initListener();
    }

    private void initListener() {
        //节点点击事件监听
        mLineChartView.setOnValueTouchListener(new ValueTouchListener());
    }

    private void initView() {
        /**
         * 禁用视图重新计算 主要用于图表在变化时动态更改，不是重新计算
         * 类似于ListView中数据变化时，只需notifyDataSetChanged()，而不用重新setAdapter()
         */
        mLineChartView.setViewportCalculationEnabled(false);
        mLineChartView.setZoomEnabled(false);
    }

    /**
     * 利用随机数设置每条线对应节点的值
     */
    private void setPointsValues(List<TemperatureTime.ElectricBean> list) {
        data.clear();
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                if (j > 0 && j < 7) {
                    String str = list.get(j - 1).getElectricValue();
                    if (electricType.equals("7")) {
                        data.put(j, str);
                    }
                    float f = new BigDecimal(str).floatValue();
                    randomNumbersTab[i][j] = (f);
                }
            }
        }
    }

    /**
     * 设置线的相关数据
     */
    private void setLinesDatas(List<TemperatureTime.ElectricBean> list) {
        List<Line> lines = new ArrayList<>();
        ArrayList<AxisValue> axisValuesX = new ArrayList<>();
        //循环将每条线都设置成对应的属性
        for (int i = 0; i < numberOfLines; ++i) {
            //节点的值
            List<PointValue> values = new ArrayList<>();
            for (int j = 0; j < numberOfPoints; ++j) {
                if (j > 0 && j < 7) {
                    values.add(new PointValue(j, randomNumbersTab[i][j]));
                    axisValuesX.add(new AxisValue(j).setLabel(getTime(list.get(j-1).getElectricTime())));
                }
            }

            Line line = new Line(values);               //根据值来创建一条线
            line.setColor(ChartUtils.COLORS[i]);        //设置线的颜色
            line.setShape(pointsShape);                 //设置点的形状
            line.setHasLines(isHasLines);               //设置是否显示线
            line.setHasPoints(isHasPoints);             //设置是否显示节点
            line.setCubic(isCubic);                     //设置线是否立体或其他效果
            line.setFilled(isFilled);                   //设置是否填充线下方区域
            line.setHasLabels(isHasPointsLabels);       //设置是否显示节点标签
            //设置节点点击的效果
            line.setHasLabelsOnlyForSelected(isPointsHasSelected);
            //如果节点与线有不同颜色 则设置不同颜色
            if (isPointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        Axis axisX = new Axis().setHasLines(true);                    //X轴
        Axis axisY = new Axis().setHasLines(true);  //Y轴          //设置名称
        switch (electricType) {
            case "6":
                axisY.setName("电压值(V)");
                titleTv.setText("电压折线图");
                break;
            case "7":
                axisY.setName("电流值(A)");
                titleTv.setText("电流折线图");
                break;
            case "8":
                axisY.setName("漏电流(mA)");
                titleTv.setText("漏电流线图");
                break;
            case "9":
                axisY.setName("温度值(℃)");
                titleTv.setText("温度折线图");
                break;
        }
        axisX.setTextColor(Color.GRAY);//X轴灰色
        axisX.setMaxLabelChars(3);
        axisX.setValues(axisValuesX);
        axisX.setHasTiltedLabels(true);
        axisX.setTextSize(10);
        axisX.setInside(true);
        axisY.setTextColor(Color.GRAY);

        mLineData = new LineChartData(lines);                      //将所有的线加入线数据类中
        mLineData.setBaseValue(Float.NaN);
        mLineData.setAxisXBottom(axisX);            //设置X轴位置 下方
        mLineData.setAxisYLeft(axisY);
        mLineData.setValueLabelBackgroundColor(Color.BLUE);     //设置数据背景颜色
        mLineData.setValueLabelTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        //设置基准数(大概是数据范围)
        /* 其他的一些属性方法 可自行查看效果
         * mLineData.setValueLabelBackgroundAuto(true);            //设置数据背景是否跟随节点颜色
         * mLineData.setValueLabelBackgroundColor(Color.BLUE);     //设置数据背景颜色
         * mLineData.setValueLabelBackgroundEnabled(true);         //设置是否有数据背景
         * mLineData.setValueLabelsTextColor(Color.RED);           //设置数据文字颜色
         * mLineData.setValueLabelTextSize(15);                    //设置数据文字大小
         * mLineData.setValueLabelTypeface(Typeface.MONOSPACE);    //设置数据文字样式
        */

        mLineChartView.setLineChartData(mLineData);    //设置图表控件
    }

//    private String getTime(String str) {
//        String[] strings = str.split(" ");
//        return strings[1];
//    }

    private String getTime(String str) {
        String strings = str.substring(5, str.length());
        return strings;
    }

    /**
     * 重点方法，计算绘制图表
     */
    private void resetViewport(List<TemperatureTime.ElectricBean> tem) {
        //创建一个图标视图,大小为控件的最大大小
        int value=0;
        if(tem!=null&&tem.size()>0){
            value = Utils.getMax(tem)*2;
        }
        final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.left = 0;                             //坐标原点在左下
        v.bottom = 0;
        v.top = value;
        if(value==0){
            switch (electricType) {
                case "6":
                    v.top = 400;
                    break;
                case "7":
                    v.top = 50;
                    break;
                case "8":
                    v.top = 700;
                    break;
                case "9":
                    v.top = 80;
                    break;
            }
        }

        //最高点为100
        v.right = numberOfPoints - 1;           //右边为点 坐标从0开始 点号从1 需要 -1
        mLineChartView.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        mLineChartView.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }

    @Override
    public void getDataSuccess(List<TemperatureTime.ElectricBean> temperatureTimes) {
        int len = temperatureTimes.size();
        if (len == 6) {
            btnNext.setClickable(true);
            btnNext.setBackgroundResource(R.drawable.next_selector);
            electricBeen.clear();
            electricBeen.addAll(temperatureTimes);
        } else if (len < 6&&electricBeen.size()>0) {
            btnNext.setClickable(false);
            btnNext.setBackgroundResource(R.mipmap.next_an);
            for (int i = 0; i < len; i++) {
                electricBeen.remove(0);
                TemperatureTime.ElectricBean tElectricBean = temperatureTimes.get(i);
                electricBeen.add(tElectricBean);
            }
        }else if(len < 6&&electricBeen.size()==0){
            T.showShort(mActivity,"无数据");
        }
        setPointsValues(electricBeen);
        setLinesDatas(electricBeen);
        resetViewport(electricBeen);
    }

    @Override
    public void getDataFail(String msg) {
//        page= page-1;
        btnNext.setClickable(false);
        btnNext.setBackgroundResource(R.mipmap.next_an);
        T.showShort(context, msg);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 节点触摸监听
     */
    private class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            switch (electricType) {
                case "6":
                    Toast.makeText(LineChartActivity.this, "电压值为: " + value.getY() + "V", Toast.LENGTH_SHORT).show();
                    break;
                case "7":
                    int i = (int) value.getX();
                    String str = data.get(i);
                    Toast.makeText(LineChartActivity.this, "电流值为: " + str + "A", Toast.LENGTH_SHORT).show();
                    break;
                case "8":
                    Toast.makeText(LineChartActivity.this, "漏电流值为: " + value.getY() + "mA", Toast.LENGTH_SHORT).show();
                    break;
                case "9":
                    Toast.makeText(LineChartActivity.this, "温度值为: " + value.getY() + "℃", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onValueDeselected() {
        }
    }

    @Override
    protected LineChartPresenter createPresenter() {
        lineChartPresenter = new LineChartPresenter(this);
        return lineChartPresenter;
    }

    @OnClick({R.id.btn_next, R.id.btn_before,R.id.btn_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                page = page + 1;
                if (page == 2) {
                    btnBefore.setClickable(true);
                    btnBefore.setBackgroundResource(R.drawable.before_selector);
                }
                mvpPresenter.getElectricTypeInfo(userID, privilege + "", electricMac, electricType, electricNum, page + "", false);
                break;
            case R.id.btn_before:
                if (page > 1) {
                    page = page - 1;
                    if (page == 1) {
                        btnBefore.setClickable(false);
                        btnBefore.setBackgroundResource(R.mipmap.prve_an);
                    }
                    mvpPresenter.getElectricTypeInfo(userID, privilege + "", electricMac, electricType, electricNum, page + "", false);
                }
                break;
            case R.id.btn_new:
                page = 1;
                btnBefore.setClickable(false);
                btnBefore.setBackgroundResource(R.mipmap.prve_an);
                btnNext.setClickable(true);
                btnNext.setBackgroundResource(R.drawable.next_selector);
                mvpPresenter.getElectricTypeInfo(userID, privilege + "", electricMac, electricType, electricNum, page + "", false);
                break;
        }
    }
}
