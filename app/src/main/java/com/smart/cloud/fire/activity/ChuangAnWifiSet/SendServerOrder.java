package com.smart.cloud.fire.activity.ChuangAnWifiSet;

/**
 * Created by Rain on 2018/5/14.
 */
import com.smart.cloud.fire.utils.CRC16;
import com.smart.cloud.fire.utils.IntegerTo16;
import com.smart.cloud.fire.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.List;



public class SendServerOrder {

    public static int seq=0;

    /**
     * 手机配置包
     * @return
     */
    public static byte[] WifiSetOrder(String ssid,String psw){
        byte[] ssidByte = ssid.getBytes();
        byte[] pswByte = psw.getBytes();
        byte[] datas = new byte[11+ssidByte.length+pswByte.length];
        byte[] Crc = new byte[datas.length-4];
        datas[0] = 0x7E;
        datas[1] = 0x0A;
        datas[2] = 0x03;
        datas[3] = 0x00;
        datas[4] = (byte) seq;
        Crc[0] = 0x0A;
        Crc[1] = 0x03;
        Crc[2] = 0x00;
        Crc[3] =(byte) seq;
        datas[5] = (byte) (ssidByte.length+pswByte.length+2);
        Crc[4] =(byte) (ssidByte.length+pswByte.length+2);
        datas[6] = (byte) ssidByte.length;
        Crc[5] =(byte) ssidByte.length;
        for(int i=0;i<ssidByte.length;i++){
            datas[7+i] = ssidByte[i];
            Crc[6+i] = ssidByte[i];
        }
        datas[7+ssidByte.length] = (byte) pswByte.length;
        Crc[6+ssidByte.length] = (byte) pswByte.length;
        for(int i=0;i<pswByte.length;i++){
            datas[8+ssidByte.length+i] = pswByte[i];
            Crc[7+ssidByte.length+i] = pswByte[i];
        }
        String srcStr = String.format("%04x", CRC16.calcCrc16(Crc));
        datas[datas.length-3] = new IntegerTo16().str16ToByte(srcStr.substring(0, 2));
        datas[datas.length-2] = new IntegerTo16().str16ToByte(srcStr.substring(2, 4));
        datas[datas.length-1] = 0x7F;
        seq++;
        String a= Utils.bytesToHexString(datas);
        return datas;
    }

}
