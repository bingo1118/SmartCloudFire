package com.smart.cloud.fire.SQLEntity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Rain on 2018/8/20.
 */
public class UserInfo extends LitePalSupport {

    private String userid;

    private String psw;

    private int privilege;

    private String name;


    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
