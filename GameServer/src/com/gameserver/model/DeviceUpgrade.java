package com.gameserver.model;

import java.io.Serializable;

public class DeviceUpgrade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 347109427845360450L;
	private String devicetype;
	private int devicelevel;
	private String devicequality;
	private int addhp;
	private int addatk;
	private int adddef;
	private int moneyvalue;
	private int succval;
	private int upgold;
	private int upsucval;
	private int addprice;
	private int coinval;
	private int tearval;
	public int getTearval() {
		return tearval;
	}
	public void setTearval(int tearval) {
		this.tearval = tearval;
	}
	public String getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	public int getDevicelevel() {
		return devicelevel;
	}
	public void setDevicelevel(int devicelevel) {
		this.devicelevel = devicelevel;
	}
	public int getAddhp() {
		return addhp;
	}
	public void setAddhp(int addhp) {
		this.addhp = addhp;
	}
	public int getAddatk() {
		return addatk;
	}
	public void setAddatk(int addatk) {
		this.addatk = addatk;
	}
	public int getAdddef() {
		return adddef;
	}
	public void setAdddef(int adddef) {
		this.adddef = adddef;
	}
	public int getMoneyvalue() {
		return moneyvalue;
	}
	public void setMoneyvalue(int moneyvalue) {
		this.moneyvalue = moneyvalue;
	}
	public int getSuccval() {
		return succval;
	}
	public void setSuccval(int succval) {
		this.succval = succval;
	}
	public int getUpgold() {
		return upgold;
	}
	public void setUpgold(int upgold) {
		this.upgold = upgold;
	}
	public int getUpsucval() {
		return upsucval;
	}
	public void setUpsucval(int upsucval) {
		this.upsucval = upsucval;
	}
	public int getAddprice() {
		return addprice;
	}
	public void setAddprice(int addprice) {
		this.addprice = addprice;
	}
	public int getCoinval() {
		return coinval;
	}
	public void setCoinval(int coinval) {
		this.coinval = coinval;
	}

	public String getDevicequality() {
		return devicequality;
	}
	public void setDevicequality(String devicequality) {
		this.devicequality = devicequality;
	}


}
