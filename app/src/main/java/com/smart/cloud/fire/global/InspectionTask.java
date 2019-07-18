package com.smart.cloud.fire.global;

public class InspectionTask {
    private String tid;
    private String areaid;
    private String area; //
    private String pid; //巡检点
    private String pname; //巡检点名称
    private String workerId; //巡检员
    private String startime;
    private String endtime;
    private String level; //区分是日常任务还是临时任务
    private String date; //巡检日期
    private String progress; //巡检完成数
    private String pass; //巡检合格数
    private String itemNum;
    private String dates; //
    private String uids;//
    private String levelName;//任务类型
    private String state;//任务状态


    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getWorkerId() {
        return workerId;
    }
    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }
    public String getStartime() {
        return startime;
    }
    public void setStartime(String startime) {
        this.startime = startime;
    }
    public String getEndtime() {
        return endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getProgress() {
        return progress;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }
    public String getItemNum() {
        return itemNum;
    }
    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }
    public String getAreaid() {
        return areaid;
    }
    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }
    public String getPname() {
        return pname;
    }
    public void setPname(String pname) {
        this.pname = pname;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getDates() {
        return dates;
    }
    public void setDates(String dates) {
        this.dates = dates;
    }
    public String getUids() {
        return uids;
    }
    public void setUids(String uids) {
        this.uids = uids;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
