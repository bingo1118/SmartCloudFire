package com.smart.cloud.fire.mvp.login.model;

/**
 * Created by Administrator on 2016/9/19.
 */
public class LoginModel {
    /**
     * error_code : 0
     * UserID : -2143483675
     * P2PVerifyCode1 : 673719542
     * P2PVerifyCode2 : 985266911
     * Email :
     * NickName :
     * CountryCode : 86
     * PhoneNO : 13622215085
     * ImageID :
     * SessionID : 1291775893
     * DomainList :
     * UserLevel : 0
     */
    private String error;
    private int errorCode;
    private String name;
    private int privilege;
    private String error_code;
    private String UserID;
    private String P2PVerifyCode1;
    private String P2PVerifyCode2;
    private String Email;
    private String NickName;
    private String CountryCode;
    private String PhoneNO;
    private String ImageID;
    private String SessionID;
    private String DomainList;
    private String UserLevel;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String CountryCode) {
        this.CountryCode = CountryCode;
    }

    public String getPhoneNO() {
        return PhoneNO;
    }

    public void setPhoneNO(String PhoneNO) {
        this.PhoneNO = PhoneNO;
    }

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String ImageID) {
        this.ImageID = ImageID;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String SessionID) {
        this.SessionID = SessionID;
    }

    public String getDomainList() {
        return DomainList;
    }

    public void setDomainList(String DomainList) {
        this.DomainList = DomainList;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String UserLevel) {
        this.UserLevel = UserLevel;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
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
