package com.gameserver.model;

import java.io.Serializable;

public class DeviceSetDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7759593446108475558L;
	private String devicestr;
	private double ddhp;
	private double ddatk;
	private double dddef;
	
	public String getDevicestr() {
		return devicestr;
	}
	public void setDevicestr(String devicestr) {
		this.devicestr = devicestr;
	}
	public double getDdhp() {
		return ddhp;
	}
	public void setDdhp(double ddhp) {
		this.ddhp = ddhp;
	}
	public double getDdatk() {
		return ddatk;
	}
	public void setDdatk(double ddatk) {
		this.ddatk = ddatk;
	}
	public double getDddef() {
		return dddef;
	}
	public void setDddef(double dddef) {
		this.dddef = dddef;
	}
   

}
