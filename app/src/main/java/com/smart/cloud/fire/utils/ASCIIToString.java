package com.smart.cloud.fire.utils;

import java.util.Locale;

/**
 * Created by Rain on 2018/5/14.
 */
public class ASCIIToString {
    private final static String mHexStr = "0123456789ABCDEF";

    public static String hexStr2Str(String hexStr){

        hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int iTmp = 0x00;;

        for (int i = 0; i < bytes.length; i++){
            iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;
            iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (iTmp & 0xFF);
        }
        return new String(bytes);
    }
}

