package com.gameserver.model;

import java.io.Serializable;

public class ActDict  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2233594528665883728L;

	private String serverno;
	private int acttype;
	private double actvalue;
	private int btime;
	private int etime;
	public String getServerno() {
		return serverno;
	}
	public void setServerno(String serverno) {
		this.serverno = serverno;
	}
	public int getActtype() {
		return acttype;
	}
	public void setActtype(int acttype) {
		this.acttype = acttype;
	}
	public double getActvalue() {
		return actvalue;
	}
	public void setActvalue(double actvalue) {
		this.actvalue = actvalue;
	}
	public int getBtime() {
		return btime;
	}
	public void setBtime(int btime) {
		this.btime = btime;
	}
	public int getEtime() {
		return etime;
	}
	public void setEtime(int etime) {
		this.etime = etime;
	}
	
	

}
