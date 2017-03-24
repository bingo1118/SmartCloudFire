package com.smart.cloud.fire.utils;

import com.smart.cloud.fire.global.NormalSmoke;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/15.
 * 序列化map供Bundle传递map使用
 */
public class SerializableMap implements Serializable {

    private Map<String,String> map;
    private Map<String,Integer> intMap;
    private Map<String,NormalSmoke> objectMap;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, Integer> getIntMap() {
        return intMap;
    }

    public void setIntMap(Map<String, Integer> intMap) {
        this.intMap = intMap;
    }

    public Map<String, NormalSmoke> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, NormalSmoke> objectMap) {
        this.objectMap = objectMap;
    }
}