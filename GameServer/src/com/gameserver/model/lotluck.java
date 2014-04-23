package com.gameserver.model;

import java.io.Serializable;

public class lotluck implements  Serializable{//缘分buff
	/**
	 * 
	 */
	private static final long serialVersionUID = 5834842229776613370L;
	private double addhp;  //血量
	private double addatk; //攻
	private double adddef;  //防
	private String somename;
	private String luckname;
	private String luckdesc;
	public double getAddhp() {
		return addhp;
	}
	public void setAddhp(double addhp) {
		this.addhp = addhp;
	}
	public double getAddatk() {
		return addatk;
	}
	public void setAddatk(double addatk) {
		this.addatk = addatk;
	}
	public double getAdddef() {
		return adddef;
	}
	public void setAdddef(double adddef) {
		this.adddef = adddef;
	}
	public String getSomename() {
		return somename;
	}
	public void setSomename(String somename) {
		this.somename = somename;
	}
	public String getLuckname() {
		return luckname;
	}
	public void setLuckname(String luckname) {
		this.luckname = luckname;
	}
	public String getLuckdesc() {
		return luckdesc;
	}
	public void setLuckdesc(String luckdesc) {
		this.luckdesc = luckdesc;
	}
	
	

}
