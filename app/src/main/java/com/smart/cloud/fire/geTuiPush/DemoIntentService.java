package com.smart.cloud.fire.geTuiPush;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.smart.cloud.fire.activity.UploadAlarmInfo.UploadAlarmInfoActivity;
import com.smart.cloud.fire.global.ConstantValues;
import com.smart.cloud.fire.global.MyApp;
import com.smart.cloud.fire.mvp.Alarm.AlarmActivity;
import com.smart.cloud.fire.mvp.Alarm.UserAlarmActivity;
import com.smart.cloud.fire.mvp.Alarm.WorkingTimeActivity;
import com.smart.cloud.fire.mvp.LineChart.LineChartActivity;
import com.smart.cloud.fire.mvp.fragment.MapFragment.HttpError;
import com.smart.cloud.fire.pushmessage.DisposeAlarm;
import com.smart.cloud.fire.pushmessage.GetUserAlarm;
import com.smart.cloud.fire.pushmessage.PushAlarmMsg;
import com.smart.cloud.fire.pushmessage.PushWiredSmokeAlarmMsg;
import com.smart.cloud.fire.retrofit.ApiStores;
import com.smart.cloud.fire.retrofit.AppClient;
import com.smart.cloud.fire.rxjava.ApiCallback;
import com.smart.cloud.fire.rxjava.SubscriberCallBack;
import com.smart.cloud.fire.ui.TaskDialogActivity;
import com.smart.cloud.fire.utils.SharedPreferencesManager;
import com.smart.cloud.fire.utils.T;
import com.smart.cloud.fire.utils.TimeFormat;
import com.smart.cloud.fire.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Random;

import fire.cloud.smart.com.smartcloudfire.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/12/2.
 */
public class DemoIntentService extends GTIntentService {
    private CompositeSubscription mCompositeSubscription;
    @Override
    public void onReceiveServicePid(Context context, int i) {

    }


    @Override
    public void onReceiveClientId(Context context, String cid) {
        String userID = SharedPreferencesManager.getInstance().getData(context,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        SharedPreferencesManager.getInstance().putData(context,SharedPreferencesManager.SP_FILE_GWELL,
                "CID",cid);//@@5.27存储个推cid
        PushManager.getInstance().bindAlias(this.getApplicationContext(),userID);
        goToServer(cid,userID);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        String msg = new String(gtTransmitMessage.getPayload());
        boolean showDateChange=false;
        try {
            JSONObject dataJson = new JSONObject(msg);
            String alarmTime=null;
            if(dataJson.has("alarmTime")){
                alarmTime=dataJson.getString("alarmTime");
            }else if(dataJson.has("masterFault")){
                alarmTime=dataJson.getJSONObject("masterFault").getString("faultTime");
            }
            int pushStatus = 0;
            if(dataJson.has("pushStatus")){
                pushStatus=dataJson.getInt("pushStatus");
            }

            //过滤30分钟前的报警
            if(null!=alarmTime&&(System.currentTimeMillis()-TimeFormat.date2TimeStamp(alarmTime))>30*60*1000){
                return;
            }

            int alarm =0;
            if(dataJson.has("alarmType")){
                alarm = dataJson.getInt("alarmType");
            }
            if(alarm==163){
                T.showShort(MyApp.app,"声光已下发");
            }
            if(alarm==241){//临时任务推送
                Intent intent=new Intent(context, TaskDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return;
            }
            if(alarm==80){
                Intent wiredIntent = new Intent(context, WorkingTimeActivity.class);
                wiredIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                wiredIntent.putExtra("time",dataJson.getString("address"));
                context.startActivity(wiredIntent);
                return;
            }
            int deviceType = dataJson.getInt("deviceType");
            switch (deviceType){
                case 224:
                case 221://@@6.30有线烟感
                    JSONObject WiredJson =dataJson.getJSONObject("masterFault");
                    String wiredMessage="发生"+WiredJson.getString("faultType");
                    PushWiredSmokeAlarmMsg mPushAlarmMsg2 = jsJson2(dataJson.getJSONObject("masterFault"));
                    Random random2 = new Random();
//                        showDownNotification(context,wiredMessage,mPushAlarmMsg2,random2.nextInt(),AlarmActivity.class);
                    Intent wiredIntent = new Intent(context, AlarmActivity.class);
                    wiredIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    wiredIntent.putExtra("mPushAlarmMsg",mPushAlarmMsg2);
                    wiredIntent.putExtra("alarmMsg",wiredMessage);
                    wiredIntent.putExtra("isWiredAlarmMsg",1);//@@6.30
                    context.startActivity(wiredIntent);
                    break;
                case 1://烟感
                case 2://燃气
                case 7://声光
                case 8://手报
                case 10://水压@@4.28
                case 11://红外
                case 12://门磁
                case 15://水禁
                case 16://NB燃气
                case 18://喷淋
                case 19://水位
                case 21://LoraOne烟感
                case 22://南京平台燃气
                case 25://@@温湿度传感器
                case 26://@@万科温度
                case 27://@@万科水浸
                case 31://三江iot烟感
                case 35://NB电弧
                case 36://联通NB电弧
                case 41://NB烟感
                case 42://@@NB水压2018.02.23
                case 43:
                case 44://万科水位
                case 45://海曼NB气感
                case 46:
                case 47://NB直连水压
                case 48://NB直连水位
                case 51://@@创安燃气
                case 55:
                case 56://NBiot烟感
                case 57://onet烟感
                case 58://嘉德移动烟感
                case 61://嘉德南京烟感
                case 68://恒星法兰盘水压
                case 69://恒星水位
                case 70://恒星水压
                case 72://防爆燃气
                case 73://南京7020燃气
                case 78://南京普通水压
                case 79://南京温湿度
                case 82://NB直连喷淋
                case 85://南京普通水位
                case 86://塞特维尔南京烟感
                case 87://三江HN388烟感
                case 89://塞特维尔移动烟感
                case 90:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 103:
                case 104:
                case 111://@@小主机，终端
                case 119://联动烟感
                case 124://@@外接水位
                case 125://@@外接水压
                    String message = null;
                    int alarmType = dataJson.getInt("alarmType");
                    switch (deviceType){
                        case 36:
                        case 35:
                            if(alarmType==53) {
                                message="发生报警";
                            }else if(alarmType==36){
                                message="发生485故障";
                            }else if(alarmType==54){
                                message="发生探测器故障";
                            }
                            break;
                        case 111:
                            message="主机处于备电状态";
                            break;
                        case 99:
                        case 79:
                        case 26:
                        case 25:
                            if(alarmType==307) {
                                message="发生低温报警";
                            }else if(alarmType==308){
                                message="发生高温报警";
                            }else if(alarmType==407){
                                message="湿度过低";
                            }else if(alarmType==408){
                                message="湿度过高";
                            }else if(alarmType==193){
                                message="烟感电量低，请更换电池";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 31:
                            if(alarmType==202) {
                                message="发生烟雾报警";
                            }else if(alarmType==67){
                                message="发生自检报警";
                            }else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 119:
                        case 92:
                        case 89:
                        case 87:
                        case 86:
                        case 41:
                        case 61:
                        case 58:
                        case 57:
                        case 56:
                        case 55:
                        case 1:
                            if(alarmType==202) {
                                message="发生烟雾报警";
                            }else if(alarmType==14){
                                message="该设备已被拆除";
                            }else if(alarmType==15||alarmType==20){
                                message="发生防拆恢复报警";
                            }else if(alarmType==67){
                                message="发生自检报警";
                            }else if(alarmType==69){
                                message="发生报警恢复";
                            }else if(alarmType==102){
                                message="发生烟雾报警恢复";
                            }else if(alarmType==103){
                                message="发生温度报警";
                            }else if(alarmType==104){
                                message="发生温度报警恢复";
                            }else if(alarmType==105){
                                message="发生烟雾低电量报警";
                            }else if(alarmType==106){
                                message="发生烟雾低电量报警恢复";
                            }else if(alarmType==107){
                                message="发生低电量报警";
                            }else if(alarmType==108){
                                message="发生低电量报警恢复";
                            }else if(alarmType==109){
                                message="发生烟雾故障报警";
                            }else if(alarmType==110){
                                message="发生烟雾故障报警恢复";
                            }else if(alarmType==111){
                                message="发生温湿度故障报警";
                            }else if(alarmType==112){
                                message="发生温湿度故障报警恢复";
                            }else if(alarmType==113){
                                message="发生手动报警";
                            }else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else if(alarmType==194){
                                message="低电压已恢复";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 124:
                        case 101://南京防爆水位
                        case 98://南京普通水位
                        case 95:
                        case 85:
                        case 69:
                        case 48:
                        case 46:
                        case 44:
                        case 19:
                            if(alarmType==207) {
                                message="发生低水位报警";
                            }else if(alarmType==208){
                                message="发生高水位报警";
                            }else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else if(alarmType==136){
                                message="发生通信故障";
                            }else if(alarmType==36){
                                message="发生故障";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 21:
                            if(alarmType==202) {
                                message="发生烟雾报警";
                            }else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 90:
                        case 82:
                        case 18://@@10.31 喷淋
                            if(alarmType==202||alarmType==66||alarmType==203) {
                                message="阀门开启";
                            }else if(alarmType==201){
                                message="阀门已关闭";
                            } else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 104://热电偶温度器
                            if(alarmType==36) {
                                message="发生故障";
                            }else if(alarmType==308){
                                message="发生高温报警";
                            }
                            break;
                        case 45://@@海曼气感
                            if(alarmType==71) {
                                message="发生轻度泄露";
                            }else if(alarmType==72){
                                message="发生重度泄露";
                            }else if(alarmType==73){
                                message="发生短路报警";
                            }else if(alarmType==74){
                                message="发生开路报警";
                            }else if(alarmType==75){
                                message="发生机械手故障";
                            } else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else if(alarmType==70){
                                message="发生报警恢复";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 96:
                        case 93:
                        case 73:
                        case 72:
                        case 51:
                        case 22:
                        case 16:
                        case 2:
                            if(alarmType==76){
                                message="发生传感器故障";
                            }else if(alarmType==77){
                                message="发生串口通讯故障";
                            }else{
                                message="燃气发生泄漏";
                            }
                            break;
                        case 7:
                            message="声光发出报警";
                            break;
                        case 103:
                        case 8:
                            if(alarmType==193){
                                message="低电压报警";
                            }else{
                                message="手动报警";
                            }
                            break;
                        case 11:
                            if(alarmType==202||alarmType==206) {
                                message="发生报警";
                            }else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 12:
                            if(alarmType==202||alarmType==205) {
                                message="发生报警";
                            }else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 27:
                        case 15://@@8.3
                            if(alarmType==202||alarmType==221) {
                                message="发生报警";
                            }else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                        case 125:
                        case 100://南京防爆水压
                        case 97://南京普通水压
                        case 94:
                        case 78:
                        case 70:
                        case 68:
                        case 47:
                        case 42:
                        case 43:
                        case 10://@@4.28
                            int alarmFamily = dataJson.getInt("alarmFamily");
                            if(alarmType==218) {
                                message="发生高水压报警 水压值："+alarmFamily+"kpa";
                            }else if(alarmType==209){
                                message="发生低水压报警 水压值："+alarmFamily+"kpa";
                            }else if(alarmType==217){
                                message="发生水压升高,水压值："+alarmFamily+"kpa";
                                showDateChange=true;
                            }else if(alarmType==210){
                                message="发生水压降低,水压值："+alarmFamily+"kpa";
                                showDateChange=true;
                            }else if(alarmType==136){
                                message="发生通信故障";
                            }else if(alarmType==36){
                                message="发生故障";
                            }else if(alarmType==193){
                                message="电量低，请更换电池";
                            }else{
                                message="发生未知类型报警";
                            }
                            break;
                    }
                    if(showDateChange==true){
                        PushAlarmMsg mPushAlarmMsg = jsJson(dataJson);
                        Random random1 = new Random();
                        showDataChangeNotification(context,message,mPushAlarmMsg,random1.nextInt(),LineChartActivity.class);
                    }else{
                        PushAlarmMsg mPushAlarmMsg = jsJson(dataJson);
                        Random random1 = new Random();
                        showDownNotification(context,message,mPushAlarmMsg,random1.nextInt(),AlarmActivity.class);
                        Intent intent1 = new Intent(context, AlarmActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("mPushAlarmMsg",mPushAlarmMsg);
                        intent1.putExtra("alarmMsg",message);
                        context.startActivity(intent1);
                    }
                    break;
                case 91:
                case 83://南京中电电气
                case 81://lora优特电气
                case 80://南京优特电气
                case 77://南京三相电气
                case 76://NB直连三相电气
                case 75://南京电气
                case 59:
                case 53:
                case 52:
                case 5://电气
                    PushAlarmMsg pushAlarmMsg1 = jsJson(dataJson);
                    int alarmFamily = pushAlarmMsg1.getAlarmFamily();
                    String alarmMsg = null;
                    switch (alarmFamily){
                        case 162:
                            alarmMsg = "电气探测器发出：错相";
                            break;
                        case 161:
                            alarmMsg = "电气探测器发出：手动分闸";
                            break;
                        case 160:
                            alarmMsg = "电气探测器发出：设备属性更改";
                            break;
                        case 159:
                            alarmMsg = "电气探测器发出：按键测试";
                            break;
                        case 158:
                            alarmMsg = "电气探测器发出：远程";
                            break;
                        case 204:
                            alarmMsg = "电气探测器发出：测试手动报警";
                            break;
                        case 151:
                            alarmMsg = "电气探测器发出：缺零报警";
                            break;
                        case 152:
                            alarmMsg = "电气探测器发出：过载报警";
                            break;
                        case 153:
                            alarmMsg = "电气探测器发出：缺相报警";
                            break;
                        case 154:
                            alarmMsg = "电气探测器发出：接地报警";
                            break;
                        case 155:
                            alarmMsg = "电气探测器发出：停电报警";
                            break;
                        case 156:
                            alarmMsg = "电气探测器发出：闭锁报警";
                            break;
                        case 157:
                            alarmMsg = "电气探测器发出：定时试验";
                            break;
                        case 43://电气报警
                            int alarmType1 = pushAlarmMsg1.getAlarmType();
                            if(alarmType1!=0){
                                alarmMsg = "电气探测器发出：过压报警";
                            }else{
                                alarmMsg = "电气探测器发出：过压报警（测试）";
                            }
                            break;
                        case 74:
                            alarmMsg = "电气探测器发出：开路故障";
                            break;
                        case 138:
                            alarmMsg = "电气探测器发出：温度探头故障";
                            break;
                        case 137:
                            alarmMsg = "电气探测器发出：剩余电流探头故障";
                            break;
                        case 136:
                        case 36:
                            int alarmType36 = pushAlarmMsg1.getAlarmType();
                            switch (alarmType36){
                                case 1:
                                    alarmMsg = "电气探测器发出：漏电流故障报警";
                                    break;
                                case 2:
                                    alarmMsg = "电气探测器发出：温度故障报警";
                                    break;
                                case 3:
                                    alarmMsg = "电气探测器发出：供电中断报警";
                                    break;
                                case 4:
                                    alarmMsg = "电气探测器发出：错相报警";
                                    break;
                                case 5:
                                    alarmMsg = "电气探测器发出：缺相报警";
                                    break;
                                case 6:
                                    alarmMsg = "电气探测器发出：电弧故障报警";
                                    break;
                                case 7:
                                    alarmMsg = "电气探测器发出：负载故障报警";
                                    break;
                                case 8:
                                    alarmMsg = "电气探测器发出：短路故障报警";
                                    break;
                                case 9:
                                    alarmMsg = "电气探测器发出：断路故障报警";
                                    break;
                                case 10://@@6.28
                                    alarmMsg = "电气探测器发出：485通信故障";
                                    break;
                                case 0://@@6.28
                                    alarmMsg = "电气探测器发出：故障报警";
                                    break;
                                default:
                                    alarmMsg = "电气探测器发出：故障报警";
                                    break;
                            }
                            break;
                        case 45://电气报警
                            int alarmType2 = pushAlarmMsg1.getAlarmType();
                            if(alarmType2!=0){
                                alarmMsg = "电气探测器发出：过流报警";
                            }else{
                                alarmMsg = "电气探测器发出：过流报警（测试）";
                            }
                            break;
                        case 44://欠压报警
                            int alarmType3 = pushAlarmMsg1.getAlarmType();
                            if(alarmType3!=0){
                                alarmMsg = "电气探测器发出：欠压报警";
                            }else{
                                alarmMsg = "电气探测器发出：欠压报警（测试）";
                            }
                            break;
                        case 46://电气报警
                            int alarmType4 = pushAlarmMsg1.getAlarmType();
                            if(alarmType4!=0){
                                alarmMsg = "电气探测器发出：漏电报警";
                            }else{
                                alarmMsg = "电气探测器发出：漏电报警（测试）";
                            }
                            break;
                        case 47://电气报警
                            int alarmType5 = pushAlarmMsg1.getAlarmType();
                            if(alarmType5!=0){
                                alarmMsg = "电气探测器发出：温度报警";
                            }else{
                                alarmMsg = "电气探测器发出：温度报警（测试）";
                            }
                            break;
                        case 48://分闸报警@@6.28
                            int alarmType6 = pushAlarmMsg1.getAlarmType();
                            if(alarmType6!=0){
                                alarmMsg = "电气探测器发出：合闸报警";
                            }else{
                                alarmMsg = "电气探测器发出：合闸报警（测试）";
                            }
                            break;
                        case 143://@@电气报警（贵州电气报警）8.11
                            int alarmType11 = pushAlarmMsg1.getAlarmType();
                            if(alarmType11!=0){
                                alarmMsg = "电气探测器发出：过压报警（线路已断开）";
                            }
                            break;
                        case 145://电气报警
                            int alarmType12 = pushAlarmMsg1.getAlarmType();
                            if(alarmType12!=0){
                                alarmMsg = "电气探测器发出：过流报警（线路已断开）";
                            }
                            break;
                        case 144://欠压报警
                            int alarmType13 = pushAlarmMsg1.getAlarmType();
                            if(alarmType13!=0){
                                alarmMsg = "电气探测器发出：欠压报警（线路已断开）";
                            }
                            break;
                        case 146://电气报警
                            int alarmType14 = pushAlarmMsg1.getAlarmType();
                            if(alarmType14!=0){
                                alarmMsg = "电气探测器发出：漏电报警（线路已断开）";
                            }
                            break;
                        case 147://电气报警
                            int alarmType15 = pushAlarmMsg1.getAlarmType();
                            if(alarmType15!=0){
                                alarmMsg = "电气探测器发出：温度报警（线路已断开）";
                            }
                            break;
                        case 148://合闸报警
                            int alarmType16 = pushAlarmMsg1.getAlarmType();
                            if(alarmType16!=0){
                                alarmMsg = "电气探测器发出：合闸报警";
                            }
                            break;
                        case 49://短路报警
                            alarmMsg = "电气探测器发出：短路报警";
                            break;
                        case 50://过热报警
                            alarmMsg = "电气探测器发出：过热报警";
                            break;
                        case 51:
                            alarmMsg = "电气探测器发出：分闸报警";
                            break;
                        case 52:
                            alarmMsg = "电气探测器发出：断路报警";
                            break;
                        default:
                            alarmMsg = "电气探测器发出：无该报警类型（测试）";
                            break;
                    }
                    Random random = new Random();
                    showDownNotification(context,alarmMsg,pushAlarmMsg1,random.nextInt(),AlarmActivity.class);
                    Intent intent2 = new Intent(context, AlarmActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.putExtra("mPushAlarmMsg",pushAlarmMsg1);
                    intent2.putExtra("alarmMsg",alarmMsg);
                    context.startActivity(intent2);
                    break;
                case 6://一键报警和报警回复
                    int alarmType1 = dataJson.getInt("alarmType");
                    if(alarmType1==3){
                        GetUserAlarm getUserAlarm = new GetUserAlarm();
                        getUserAlarm.setAddress(dataJson.getString("address"));
                        getUserAlarm.setAlarmSerialNumber(dataJson.getString("alarmSerialNumber"));
                        getUserAlarm.setAlarmTime(dataJson.getString("alarmTime"));
                        getUserAlarm.setAreaName(dataJson.getString("areaName"));
                        getUserAlarm.setCallerId(dataJson.getString("callerId"));
                        getUserAlarm.setInfo(dataJson.getString("info"));
                        getUserAlarm.setLatitude(dataJson.getString("latitude"));
                        getUserAlarm.setLongitude(dataJson.getString("longitude"));
                        getUserAlarm.setSmoke(dataJson.getString("smoke"));
                        getUserAlarm.setCallerName(dataJson.getString("callerName"));
                        Random random3 = new Random();
                        showDownNotification(context,"您收到一条紧急报警消息",getUserAlarm,random3.nextInt(),UserAlarmActivity.class);
                        Intent intent3 = new Intent(context, UserAlarmActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent3.putExtra("mPushAlarmMsg",getUserAlarm);
                        context.startActivity(intent3);
                    }else{
                        DisposeAlarm disposeAlarm = new DisposeAlarm();
                        disposeAlarm.setAlarmType(alarmType1);
                        disposeAlarm.setPolice(dataJson.getString("police"));
                        disposeAlarm.setTime(dataJson.getString("time"));
                        disposeAlarm.setPoliceName(dataJson.getString("policeName"));
                        Random random4 = new Random();
                        showDownNotification(context,disposeAlarm.getPoliceName()+"警员已处理您的消息",null,random4.nextInt(),null);
                    }
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private PushAlarmMsg jsJson(JSONObject dataJson) throws JSONException {
        PushAlarmMsg mPushAlarmMsg = new PushAlarmMsg();
        mPushAlarmMsg.setAddress(dataJson.getString("address"));
        mPushAlarmMsg.setAlarmType(dataJson.getInt("alarmType"));
        mPushAlarmMsg.setAreaId(dataJson.getString("areaId"));
        mPushAlarmMsg.setLatitude(dataJson.getString("latitude"));
        mPushAlarmMsg.setLongitude(dataJson.getString("longitude"));
        mPushAlarmMsg.setName(dataJson.getString("name"));
        mPushAlarmMsg.setPlaceAddress(dataJson.getString("placeAddress"));
        mPushAlarmMsg.setIfDealAlarm(dataJson.getInt("ifDealAlarm"));
        mPushAlarmMsg.setPrincipal1(dataJson.getString("principal1"));
        mPushAlarmMsg.setPlaceType(dataJson.getString("placeType"));
        mPushAlarmMsg.setPrincipal1Phone(dataJson.getString("principal1Phone"));
        mPushAlarmMsg.setPrincipal2(dataJson.getString("principal2"));
        mPushAlarmMsg.setPrincipal2Phone(dataJson.getString("principal2Phone"));
        mPushAlarmMsg.setAlarmTime(dataJson.getString("alarmTime"));
        mPushAlarmMsg.setDeviceType(dataJson.getInt("deviceType"));
        mPushAlarmMsg.setAlarmFamily(dataJson.getInt("alarmFamily"));
        if(dataJson.has("alarmTypeName")){
            mPushAlarmMsg.setAlarmTypeName(dataJson.getString("alarmTypeName"));
        }
        try{
            mPushAlarmMsg.setUploadpeople(dataJson.getString("uploadpeople"));
            JSONObject jsonObject =  dataJson.getJSONObject("camera");
            if(jsonObject!=null) {
                PushAlarmMsg.CameraBean cameraBean = new PushAlarmMsg.CameraBean();
                cameraBean.setCameraId(jsonObject.getString("cameraId"));
                cameraBean.setCameraPwd(jsonObject.getString("cameraPwd"));
                mPushAlarmMsg.setCamera(cameraBean);
            }
        }catch (Exception e){
        }
        mPushAlarmMsg.setMac(dataJson.getString("mac"));
        return mPushAlarmMsg;
    }

    //@@6.30有线烟感推送信息
    private PushWiredSmokeAlarmMsg jsJson2(JSONObject dataJson) throws JSONException {
        PushWiredSmokeAlarmMsg mPushAlarmMsg = new PushWiredSmokeAlarmMsg();
        mPushAlarmMsg.setFaultCode(dataJson.getString("faultCode"));
        mPushAlarmMsg.setFaultDevDesc(dataJson.getString("faultDevDesc"));
        mPushAlarmMsg.setFaultInfo(dataJson.getString("faultInfo"));
        mPushAlarmMsg.setFaultTime(dataJson.getString("faultTime"));
        mPushAlarmMsg.setFaultType(dataJson.getString("faultType"));
        mPushAlarmMsg.setRepeater(dataJson.getString("repeater"));
        return mPushAlarmMsg;
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        if(b){
            MyApp.app.setPushState("Online");
        }else{
            MyApp.app.setPushState("Offline");
        }
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }


    private void goToServer(String cid,String userId){
        ApiStores apiStores = AppClient.retrofit(ConstantValues.SERVER_PUSH).create(ApiStores.class);
        Observable observable = apiStores.bindAlias( userId,cid,"scfire");
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                MyApp.app.setPushState(model.getState());
            }

            @Override
            public void onFailure(int code, String msg) {
                int a=code;
            }

            @Override
            public void onCompleted() {
                int a=1;
//                stopSelf();
            }
        }));
    }

    private void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @SuppressWarnings("deprecation")
    private void showDownNotification(Context context, String message, Serializable mPushAlarmMsg, int id, Class clazz){
        NotificationCompat.Builder m_builder = new NotificationCompat.Builder(context);
        m_builder.setContentTitle(message); // 主标题

        //从系统服务中获得通知管理器
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //具体的通知内容

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher); // 将PNG图片转
        m_builder.setLargeIcon(icon);

        m_builder.setSmallIcon(R.mipmap.ic_launcher); //设置小图标
        m_builder.setWhen(System.currentTimeMillis());
        m_builder.setAutoCancel(true);
        if(clazz!=null){
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//设置提示音
            m_builder.setSound(uri);
            m_builder.setContentText("点击查看详情"); //设置主要内容
            //通知消息与Intent关联
            Intent it=new Intent(context,clazz);
            it.putExtra("mPushAlarmMsg",mPushAlarmMsg);
            it.putExtra("alarmMsg",message);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent=PendingIntent.getActivity(context, id, it, PendingIntent.FLAG_CANCEL_CURRENT);
            m_builder.setContentIntent(contentIntent);
        }
        //执行通知
        nm.notify(id, m_builder.build());
    }

    //@@2018.02.24水压变化通知
    private void showDataChangeNotification(Context context, String message, Serializable mPushAlarmMsg, int id, Class clazz){
        NotificationCompat.Builder m_builder = new NotificationCompat.Builder(context);
        m_builder.setContentTitle(((PushAlarmMsg)mPushAlarmMsg).getName()+message); // 主标题

        //从系统服务中获得通知管理器
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //具体的通知内容

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher); // 将PNG图片转
        m_builder.setLargeIcon(icon);

        m_builder.setSmallIcon(R.mipmap.ic_launcher); //设置小图标
        m_builder.setWhen(System.currentTimeMillis());
        m_builder.setAutoCancel(true);
        long[] vibrates = {0, 1000, 1000, 1000};
        m_builder.getNotification().vibrate = vibrates;
        if(clazz!=null){
            m_builder.setContentText("点击查看详情"); //设置主要内容
            //通知消息与Intent关联
            Intent it=new Intent(context,clazz);
            it.putExtra("electricMac",((PushAlarmMsg)mPushAlarmMsg).getMac());
            it.putExtra("isWater","1");//@@是否为水压
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent=PendingIntent.getActivity(context, id, it, PendingIntent.FLAG_CANCEL_CURRENT);
            m_builder.setContentIntent(contentIntent);
        }
        //执行通知
        nm.notify(id, m_builder.build());
    }
}
