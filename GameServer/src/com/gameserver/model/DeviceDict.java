package com.gameserver.model;

import java.io.Serializable;

public class DeviceDict implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8044657601890200096L;
	private String devicecode; //
	private String devicename; //装备名称
	private String devicedesc; //描述
	private String devicetype;  //类型 攻防饰
	private String quality;  //质量
	private int dhp;  //血增
	private int datk;  //攻增
	private int ddef;  //防增
	private int baseprice;  //基础价
	private int devcolor;//装备颜色
	
	


	public int getDevcolor() {
		return devcolor;
	}
	public void setDevcolor(int devcolor) {
		this.devcolor = devcolor;
	}
	public int getDhp() {
		return dhp;
	}
	public void setDhp(int dhp) {
		this.dhp = dhp;
	}
	public int getDatk() {
		return datk;
	}
	public void setDatk(int datk) {
		this.datk = datk;
	}
	public int getDdef() {
		return ddef;
	}
	public void setDdef(int ddef) {
		this.ddef = ddef;
	}
	public String getDevicecode() {
		return devicecode;
	}
	public void setDevicecode(String devicecode) {
		this.devicecode = devicecode;
	}
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getDevicedesc() {
		return devicedesc;
	}
	public void setDevicedesc(String devicedesc) {
		this.devicedesc = devicedesc;
	}
	public String getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public int getBaseprice() {
		return baseprice;
	}
	public void setBaseprice(int baseprice) {
		this.baseprice = baseprice;
	}
	
}
