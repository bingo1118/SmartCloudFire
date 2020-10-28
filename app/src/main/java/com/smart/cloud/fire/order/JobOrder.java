package com.smart.cloud.fire.order;

import java.io.Serializable;

public class JobOrder implements Serializable{

    private String jkey; //工单id
    private int areaId; //区域id
    private String areaName; //区域名字
    private int type; //类型  1隐患工单2过期设备工单3报警工单
    private String title; //工单标题
    private String description; //工单具体内容
    private int state; //工单状态  1未派单2处理中3已处理
    private String addTime; // 生成时间
    private String dealUserId; //当前处理人id
    private String dealUserName; //当前处理人名字
    private int ifcheck; //是否需要复核1需要2不需要
    private String principalUser;//派单人

    private String typeName;
    private String stateName;
    private String checkName;
    private String uid;


    public String getJkey() {
        return jkey;
    }
    public void setJkey(String jkey) {
        this.jkey = jkey;
    }
    public int getAreaId() {
        return areaId;
    }
    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
    public String getAreaName() {
        return areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getDealUserId() {
        return dealUserId;
    }
    public void setDealUserId(String dealUserId) {
        this.dealUserId = dealUserId;
    }
    public String getDealUserName() {
        return dealUserName;
    }
    public void setDealUserName(String dealUserName) {
        this.dealUserName = dealUserName;
    }
    public int getIfcheck() {
        return ifcheck;
    }
    public void setIfcheck(int ifcheck) {
        this.ifcheck = ifcheck;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public String getCheckName() {
        return checkName;
    }
    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrincipalUser() {
        return principalUser;
    }

    public void setPrincipalUser(String principalUser) {
        this.principalUser = principalUser;
    }
}
