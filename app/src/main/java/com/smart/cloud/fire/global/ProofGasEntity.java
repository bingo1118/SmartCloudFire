package com.smart.cloud.fire.global;

/**
 * Created by Rain on 2018/9/3.
 */
public class ProofGasEntity {
    private String proofGasMac;// 'mac地址',
    private String proofGasType;// '探测器类型',
    private String proofGasMmol;// '溶度值',
    private String proofGasTemp;// '温度值',
    private String proofGasModel = "ZY003";//  '型号',
    private String proofGasUnit;// '单位',
    private String proofGasState;// '状态',   4\5\7\8\10  报警
    private String proofGasTime;// '时间',
    private String proofGasliangcheng;//量程
    private String proofGasA1Alarm;//A1报警点
    private String proofGasA2Alarm;//A2报警点

    public String getProofGasMac() {
        return proofGasMac;
    }
    public void setProofGasMac(String proofGasMac) {
        this.proofGasMac = proofGasMac;
    }
    public String getProofGasType() {
        return proofGasType;
    }
    public void setProofGasType(String proofGasType) {
        this.proofGasType = proofGasType;
    }
    public String getProofGasMmol() {
        return proofGasMmol;
    }
    public void setProofGasMmol(String proofGasMmol) {
        this.proofGasMmol = proofGasMmol;
    }
    public String getProofGasTemp() {
        return proofGasTemp;
    }
    public void setProofGasTemp(String proofGasTemp) {
        this.proofGasTemp = proofGasTemp;
    }
    public String getProofGasModel() {
        return proofGasModel;
    }
    public void setProofGasModel(String proofGasModel) {
        this.proofGasModel = proofGasModel;
    }
    public String getProofGasUnit() {
        return proofGasUnit;
    }
    public void setProofGasUnit(String proofGasUnit) {
        this.proofGasUnit = proofGasUnit;
    }
    public String getProofGasState() {
        return proofGasState;
    }
    public void setProofGasState(String proofGasState) {
        this.proofGasState = proofGasState;
    }
    public String getProofGasTime() {
        return proofGasTime;
    }
    public void setProofGasTime(String proofGasTime) {
        this.proofGasTime = proofGasTime;
    }
    public String getProofGasliangcheng() {
        return proofGasliangcheng;
    }
    public void setProofGasliangcheng(String proofGasliangcheng) {
        this.proofGasliangcheng = proofGasliangcheng;
    }
    public String getProofGasA1Alarm() {
        return proofGasA1Alarm;
    }
    public void setProofGasA1Alarm(String proofGasA1Alarm) {
        this.proofGasA1Alarm = proofGasA1Alarm;
    }
    public String getProofGasA2Alarm() {
        return proofGasA2Alarm;
    }
    public void setProofGasA2Alarm(String proofGasA2Alarm) {
        this.proofGasA2Alarm = proofGasA2Alarm;
    }
}
