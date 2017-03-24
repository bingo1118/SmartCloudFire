package com.smart.cloud.fire.mvp.register.model;

/**
 * Created by Administrator on 2016/9/20.
 */
public class RegisterModel {

    /**
     * error_code : 0
     * error : 操作成功
     */

    private String error_code;
    private String error;
    /**
     * UserID : -2143220464
     * P2PVerifyCode1 : 2079885744
     * P2PVerifyCode2 : 1262011778
     * DomainList :
     */

    private String UserID;
    private String P2PVerifyCode1;
    private String P2PVerifyCode2;
    private String DomainList;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getP2PVerifyCode1() {
        return P2PVerifyCode1;
    }

    public void setP2PVerifyCode1(String P2PVerifyCode1) {
        this.P2PVerifyCode1 = P2PVerifyCode1;
    }

    public String getP2PVerifyCode2() {
        return P2PVerifyCode2;
    }

    public void setP2PVerifyCode2(String P2PVerifyCode2) {
        this.P2PVerifyCode2 = P2PVerifyCode2;
    }

    public String getDomainList() {
        return DomainList;
    }

    public void setDomainList(String DomainList) {
        this.DomainList = DomainList;
    }
}
