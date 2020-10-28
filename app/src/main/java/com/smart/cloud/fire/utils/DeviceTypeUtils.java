package com.smart.cloud.fire.utils;

import com.smart.cloud.fire.global.DeviceType;

public class DeviceTypeUtils {

    public static DeviceType getDevType(String smokeMac, String repeater) {
        DeviceType devType=new DeviceType();
        int electrState = 0;
        String deviceType = "1";
        String deviceName="";

        int macLenghth=smokeMac.length();

        String macStr = (String) smokeMac.subSequence(0, 1);
        if(macLenghth==4){
            deviceType="68";//恒星法兰盘水压
            deviceName="恒星法兰盘水压";
        }else if (macLenghth==6){
            deviceType="70";//恒星水压
            deviceName="恒星水压";
        }else if (macLenghth==7){
            if (macStr.equals("W")){//@@9.29 区分NB
                deviceType="69";//@@恒星水位
                deviceName="恒星水位";
                smokeMac = smokeMac.substring(1, macLenghth);
            }
        }else if (macLenghth==12){
            deviceType="51";//创安
            deviceName="创安燃气";
        }else if(macLenghth==16||macLenghth==18){
            switch(macStr){
                case "N":
                    smokeMac = smokeMac.substring(1, macLenghth);//直连设备
                    deviceType="41";
                    deviceName="NB烟感";
                    break;
                case "A":
                    smokeMac = smokeMac.substring(1, macLenghth);//三江无线传输设备
                    deviceType="119";
                    deviceName="有线传输装置";
                    break;
                case "W":
                    if(smokeMac.endsWith("W")){
                        deviceType="19";//@@水位2018.01.02
                        deviceName="水位设备";
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }else if(smokeMac.endsWith("A")){
                        deviceType="124";//@@拓普水位2018.01.30
                        deviceName="水位设备";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("B")){
                        deviceType="125";//@@拓普水压2018.01.30
                        deviceName="水压设备";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else{
                        deviceType="10";//@@水压
                        deviceName="水压设备";
                    }
                    smokeMac = smokeMac.replace("W","");//水压设备
                    break;
                case "Z":
                    smokeMac = smokeMac.substring(1, macLenghth);//嘉德烟感
                    deviceName="NB烟感";
                    deviceType="55";
                    break;
                default:
                    deviceName="烟感";
                    deviceType="21";//loraOne烟感
                    break;
            }
        }else if(smokeMac.equals(repeater)){
            deviceType="126";//海湾主机
            deviceName="海湾主机";
        }else if(smokeMac.contains("-")){
            deviceType="31";//三江nb烟感
            deviceName="NB烟感";
        }else{
            switch (macStr){
                case "R":
                    if (smokeMac.endsWith("R")){//@@9.29 区分NB
                        deviceType="16";//@@NB燃气
                        smokeMac = smokeMac.substring(1, macLenghth);
                        deviceName="燃气探测器";
                    }else if(smokeMac.endsWith("N")){
                        deviceType="22";
                        deviceName="燃气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("H")){
                        deviceType="23";
                        deviceName="燃气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    } else if(smokeMac.endsWith("P")){
                        deviceType="72";
                        deviceName="燃气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("J")){
                        deviceType="73";
                        deviceName="燃气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("G")){
                        deviceType="96";
                        deviceName="燃气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("K")){
                        deviceType="106";
                        deviceName="燃气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("W")){
                        deviceType="116";
                        deviceName="创安燃气";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        deviceType="2";//@@燃气
                        deviceName="燃气探测器";
                        smokeMac = smokeMac.replace("R","");//燃气
                    }
                    break;
                case "Q":
                    deviceType="5";
                    if(smokeMac.endsWith("Q")){
                        electrState=1;
                        deviceName="电气设备";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }//@@8.26
                    if(smokeMac.endsWith("S")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=3;
                        deviceName="电气设备";
                    }//@@2018.01.18 三相设备
                    if(smokeMac.endsWith("L")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceName="电气设备";
                        deviceType="52";
                    }//@@2018.05.15 Lara电气设备
                    if(smokeMac.endsWith("N")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceName="电气设备";
                        deviceType="53";
                    }//@@2018.05.15 Lara电气设备
                    if(smokeMac.endsWith("G")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=0;
                        deviceName="电气设备";
                        deviceType="59";
                    }//@@NB北秦电气设备
                    if(smokeMac.endsWith("Z")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=0;
                        deviceName="电气设备";
                        deviceType="76";
                    }//@@NB直连三相电气设备
                    if(smokeMac.endsWith("Y")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;;
                        deviceName="电气设备";
                        deviceType="77";
                    }//@@NB南京三相电气设备
                    if(smokeMac.endsWith("U")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceName="电气设备";
                        deviceType="80";
                    }//@@NB南京优特电气设备
                    if(smokeMac.endsWith("U")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceName="电气设备";
                        deviceType="81";
                    }//@@lora优特电气设备
                    if(smokeMac.endsWith("H")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceName="电气设备";
                        deviceType="83";
                    }//@@南京平台中电电气350
                    if(smokeMac.endsWith("V")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceName="电气设备";
                        deviceType="88";
                    }//@@lora中电电气
                    if(smokeMac.endsWith("J")){
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        electrState=1;
                        deviceName="电气设备";
                        deviceType="115";
                    }//@@南京优特电气
                    smokeMac =smokeMac.replace("Q","");
                    break;
                case "T":
                    if(smokeMac.endsWith("N")){
                        deviceType="79";
                        deviceName="温湿度探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Z")){
                        deviceType="104";
                        deviceName="热电偶温度器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        deviceType="25";
                        deviceName="温湿度探测器";
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }
                    break;
                case "A":
                    smokeMac = smokeMac.substring(1, macLenghth);
                    deviceType="119";
                    deviceName="传输装置";
                    break;
                case "G":
                    if(smokeMac.endsWith("N")){
                        deviceType="102";
                        deviceName="NB声光报警器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        smokeMac = smokeMac.substring(1, macLenghth);//声光报警器 6
                        deviceName="声光报警器";
                        deviceType="7";
                    }
                    break;
                case "K":
                    smokeMac = smokeMac.substring(1, macLenghth);//@@无线输出输入模块2018.01.24
                    deviceType="20";
                    deviceName="无线输出输入模块";
                    break;
                case "S":
                    if(smokeMac.endsWith("N")){//@@直连NB手报
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                        deviceType="84";
                        deviceName="手动报警器";
                    }else if(smokeMac.endsWith("J")){
                        deviceType="103";//@@南京手报
                        deviceName="NB手动报警器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Y")){
                        deviceType="108";//@@南京手报
                        deviceName="NB手动报警器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        smokeMac = smokeMac.substring(1, macLenghth);//手动报警，显示 7
                        deviceType="8";
                        deviceName="手动报警器";
                    }
                    break;
                case "J":
                    if(smokeMac.endsWith("V")){
                        deviceType="91";//@@金特莱电气
                        deviceName="电气";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("S")){
                        deviceType="92";//@@金特莱烟感
                        deviceName="手动";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("R")){
                        deviceType="93";//@@金特莱燃气
                        deviceName="燃气";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("W")){
                        deviceType="94";//@@金特莱水压
                        deviceName="水压";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Y")){
                        deviceType="95";//@@金特莱水位
                        deviceName="水位";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        smokeMac = smokeMac.substring(1, macLenghth);//三江设备
                        deviceType="9";
                        deviceName="三江设备";
                    }
                    break;
                case "W":
                    if(smokeMac.endsWith("W")){
                        deviceType="19";//@@水位2018.01.02
                        deviceName="水位探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("C")){
                        deviceType="42";//@@NB水压
                        deviceName="水压探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("L")){
                        deviceType="43";//@@Lara水压
                        deviceName="水压探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("Y")){
                        deviceType="46";//@@NB防爆直连水位
                        deviceName="水位探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("Z")){
                        deviceType="44";//@@NB防爆直连水位（万科）
                        deviceName="水位探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("N")){
                        deviceType="78";//@@南京NB普通水压
                        deviceName="水压探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("G")){
                        deviceType="47";//@@NB直连水压
                        deviceName="水压探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("H")){
                        deviceType="48";//@@NB直连水位
                        deviceName="水位探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("J")){
                        deviceType="85";//@@南京普通
                        deviceName="水位探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("O")){
                        deviceType="97";//@@南京普通水压
                        deviceName="水压探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("P")){
                        deviceType="98";//@@南京普通水位
                        deviceName="水位探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("Q")){
                        deviceType="99";//@@南京普通温湿度
                        deviceName="温湿度探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("I")){
                        deviceType="100";//@@南京防爆水压
                        deviceName="水压探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("K")){
                        deviceType="101";//@@南京防爆水位
                        deviceName="水位探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else if(smokeMac.endsWith("X")){
                        deviceType="27";//@@南京防爆水位
                        deviceName="水浸探测器";
                        smokeMac =smokeMac.substring(1,macLenghth-1);
                    }else{
                        deviceType="10";//@@水压
                        deviceName="水压探测器";
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }
                    break;
                case "L":
                    smokeMac = smokeMac.substring(1, macLenghth);//红外设备
                    deviceType="11";
                    deviceName="红外探测器";
                    break;
                case "M":
                    smokeMac = smokeMac.substring(1, macLenghth);//门磁设备
                    deviceType="12";
                    deviceName="门磁探测器";
                    break;
                case "N":
                    if(smokeMac.endsWith("N")){
                        deviceType="56";//@@NB-iot烟感
                        deviceName="烟感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("O")){
                        deviceType="57";//@@onet烟感
                        deviceName="烟感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("R")){
                        deviceType="45";//@@海曼气感
                        deviceName="气感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Z")){
                        deviceType="58";//@@嘉德移动烟感
                        deviceName="烟感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("H")){
                        deviceType="35";//@@电弧 电信
                        deviceName="电弧探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("I")){
                        deviceType="36";//电弧
                        deviceName="电弧探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("J")){
                        deviceType="61";//@@嘉德南京平台烟感
                        deviceName="烟感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Q")){
                        deviceType="75";
                        electrState=1;
                        deviceName="电气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("S")){
                        deviceType="86";//@@赛特威尔南京平台烟感
                        deviceName="烟感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("L")){
                        deviceType="87";//@@三江HN388南京平台烟感
                        deviceName="烟感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("K")){
                        deviceType="89";//@@赛特威尔移动烟感
                        deviceName="烟感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        deviceType="41";
                        deviceName="烟感探测器";
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }
                    break;
                case "H":
                    smokeMac = smokeMac.substring(1, macLenghth);//空气探测器
                    deviceType="13";
                    deviceName="环境探测器";
                    break;
                case "Y":
                    deviceName="水浸探测器";
                    smokeMac = smokeMac.substring(1, macLenghth);//水禁
                    deviceType="15";
                    break;
                case "Z":
                    if(smokeMac.endsWith("Q")){
                        deviceType="105";//中电Lora
                        deviceName="电气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("K")){
                        deviceType="107";//中电Lora
                        deviceName="电气探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("P")){
                        deviceType="131";//中电Lora
                        deviceName="标签";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }
                    break;
                case "P":
                    if(smokeMac.endsWith("N")){
                        deviceType="82";//2019.03.08NB直连喷淋
                        deviceName="喷淋设备";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("J")){
                        deviceType="90";//2019.03.08NB直连喷淋
                        deviceName="喷淋设备";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else{
                        deviceType="18";
                        deviceName="喷淋设备";
                        smokeMac = smokeMac.substring(1, macLenghth);
                    }
                    electrState=2;//@@11.01 1开2关
                    break;
                case "V":
                    if(smokeMac.endsWith("X")){
                        deviceType="27";//@@万科水浸
                        deviceName="水浸探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("Z")){
                        deviceType="44";//@@万科水位
                        deviceName="水位探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }else if(smokeMac.endsWith("T")){
                        deviceType="26";//@@万科温湿度
                        deviceName="温湿度探测器";
                        smokeMac = smokeMac.substring(1, macLenghth-1);
                    }
                    break;
            }
            if(smokeMac.length()<8){
                devType.setError("设备MAC号长度不正确");
                devType.setErrorCode(1);
            }//@@11.06限制MAC长度
            if(!Utils.isNumOrEng(smokeMac)){
                devType.setError("设备MAC仅能含有数字或字母");
                devType.setErrorCode(1);
            }
        }
        devType.setDevType(deviceType);
        devType.setElectrState(electrState);
        devType.setMac(smokeMac);
        devType.setDevTypeName(deviceName);
        return devType;
    }

}
