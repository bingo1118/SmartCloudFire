package com.smart.cloud.fire.global;


public class ElectricEnergyEntity {
	private String imei = "";//IMEI(15)	
	private String imsi = "";//IMSI(15)	
	private String rssi = "";//RSSI(2)	
	private String BatteryPower;//电池电量(1)
	private int DevType = 4;//4	0x0004
	private int cmd = 0;//(1)0x30
	private int shunt = 0;//(1)	分励
	private int shuntRelevanceTime = 0;//分励时间：0 - 无输出; 0x1F – 常开; ’1-30’ - 分励保持时
	private int shuntCuPer = 0;//Bit5: 1 - 电压/电流分励;
	private int shuntTemp = 0;//Bit6: 1 - 温度/漏电分励;
	private int shuntLink = 0;//Bit7: 1 – 联动分励;
	private String activePowerA = "";//有功功率A相(2)
	private String activePowerB = "";//(2)有功功率B相
	private String activePowerC = "";//	有功功率C相
	private String reactivePowerA = "";//(2)无功功率A相
	private String reactivePowerB = "";//(2)无功功率B相
	private String reactivePowerC = "";//(2)无功功率C相
	private String apparentPowerA = "";//(2)视在功率A相
	private String apparentPowerB = "";//(2)视在功率B相
	private String apparentPowerC = "";//(2)视在功率C相
	private String powerFactorA = "";//(2)功率因素A相
	private String powerFactorB = "";//(2)功率因素B相
	private String powerFactorC = "";//(2)功率因素C相
	private String activeEnergyA = "";//(2)有功电能A相
	private String activeEnergyB = "";//(2)有功电能B相
	private String activeEnergyC = "";//(2)有功电能C相
	private String reactiveEnergyA = "";//(2)无功电能A相
	private String reactiveEnergyB = "";//(2)无功电能B相
	private String reactiveEnergyC = "";//(2)无功电能C相
	private String apparentEnergyA = "";//(2)视在电能A相
	private String apparentEnergyB = "";//(2)视在电能B相
	private String apparentEnergyC = "";//(2)视在电能C相
	private String dataTime = "";	//	心跳时间


	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public String getBatteryPower() {
		return BatteryPower;
	}

	public void setBatteryPower(String batteryPower) {
		BatteryPower = batteryPower;
	}

	public int getDevType() {
		return DevType;
	}

	public void setDevType(int devType) {
		DevType = devType;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public int getShunt() {
		return shunt;
	}

	public void setShunt(int shunt) {
		this.shunt = shunt;
	}

	public int getShuntRelevanceTime() {
		return shuntRelevanceTime;
	}

	public void setShuntRelevanceTime(int shuntRelevanceTime) {
		this.shuntRelevanceTime = shuntRelevanceTime;
	}

	public int getShuntCuPer() {
		return shuntCuPer;
	}

	public void setShuntCuPer(int shuntCuPer) {
		this.shuntCuPer = shuntCuPer;
	}

	public int getShuntTemp() {
		return shuntTemp;
	}

	public void setShuntTemp(int shuntTemp) {
		this.shuntTemp = shuntTemp;
	}

	public int getShuntLink() {
		return shuntLink;
	}

	public void setShuntLink(int shuntLink) {
		this.shuntLink = shuntLink;
	}

	public String getActivePowerA() {
		return activePowerA;
	}

	public void setActivePowerA(String activePowerA) {
		this.activePowerA = activePowerA;
	}

	public String getActivePowerB() {
		return activePowerB;
	}

	public void setActivePowerB(String activePowerB) {
		this.activePowerB = activePowerB;
	}

	public String getActivePowerC() {
		return activePowerC;
	}

	public void setActivePowerC(String activePowerC) {
		this.activePowerC = activePowerC;
	}

	public String getReactivePowerA() {
		return reactivePowerA;
	}

	public void setReactivePowerA(String reactivePowerA) {
		this.reactivePowerA = reactivePowerA;
	}

	public String getReactivePowerB() {
		return reactivePowerB;
	}

	public void setReactivePowerB(String reactivePowerB) {
		this.reactivePowerB = reactivePowerB;
	}

	public String getReactivePowerC() {
		return reactivePowerC;
	}

	public void setReactivePowerC(String reactivePowerC) {
		this.reactivePowerC = reactivePowerC;
	}

	public String getApparentPowerA() {
		return apparentPowerA;
	}

	public void setApparentPowerA(String apparentPowerA) {
		this.apparentPowerA = apparentPowerA;
	}

	public String getApparentPowerB() {
		return apparentPowerB;
	}

	public void setApparentPowerB(String apparentPowerB) {
		this.apparentPowerB = apparentPowerB;
	}

	public String getApparentPowerC() {
		return apparentPowerC;
	}

	public void setApparentPowerC(String apparentPowerC) {
		this.apparentPowerC = apparentPowerC;
	}

	public String getPowerFactorA() {
		return powerFactorA;
	}

	public void setPowerFactorA(String powerFactorA) {
		this.powerFactorA = powerFactorA;
	}

	public String getPowerFactorB() {
		return powerFactorB;
	}

	public void setPowerFactorB(String powerFactorB) {
		this.powerFactorB = powerFactorB;
	}

	public String getPowerFactorC() {
		return powerFactorC;
	}

	public void setPowerFactorC(String powerFactorC) {
		this.powerFactorC = powerFactorC;
	}



	public String getActiveEnergyB() {
		return activeEnergyB;
	}

	public void setActiveEnergyB(String activeEnergyB) {
		this.activeEnergyB = activeEnergyB;
	}

	public String getActiveEnergyC() {
		return activeEnergyC;
	}

	public void setActiveEnergyC(String activeEnergyC) {
		this.activeEnergyC = activeEnergyC;
	}

	public String getReactiveEnergyA() {
		return reactiveEnergyA;
	}

	public void setReactiveEnergyA(String reactiveEnergyA) {
		this.reactiveEnergyA = reactiveEnergyA;
	}

	public String getReactiveEnergyB() {
		return reactiveEnergyB;
	}

	public void setReactiveEnergyB(String reactiveEnergyB) {
		this.reactiveEnergyB = reactiveEnergyB;
	}

	public String getReactiveEnergyC() {
		return reactiveEnergyC;
	}

	public void setReactiveEnergyC(String reactiveEnergyC) {
		this.reactiveEnergyC = reactiveEnergyC;
	}

	public String getApparentEnergyA() {
		return apparentEnergyA;
	}

	public void setApparentEnergyA(String apparentEnergyA) {
		this.apparentEnergyA = apparentEnergyA;
	}

	public String getApparentEnergyB() {
		return apparentEnergyB;
	}

	public void setApparentEnergyB(String apparentEnergyB) {
		this.apparentEnergyB = apparentEnergyB;
	}

	public String getApparentEnergyC() {
		return apparentEnergyC;
	}

	public void setApparentEnergyC(String apparentEnergyC) {
		this.apparentEnergyC = apparentEnergyC;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public String getActiveEnergyA() {
		return activeEnergyA;
	}

	public void setActiveEnergyA(String activeEnergyA) {
		this.activeEnergyA = activeEnergyA;
	}
}
