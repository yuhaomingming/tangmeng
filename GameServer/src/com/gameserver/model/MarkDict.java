package com.gameserver.model;

import java.io.Serializable;

public class MarkDict implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6109979901162572453L;
	private String markcode;
	private String markname;  //符文名
	private String markdesc;  //描述
	private String marktype;  //符文类型 攻防血
	private double atk;  //增攻
	private double hp;   //增血
	private double def;  //增防
	private int downrand;//掉率
	
	public MarkDict(){
		
	}
	
		
	public String getMarkcode() {
		return markcode;
	}


	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}

	public String getMarkname() {
		return markname;
	}
	public void setMarkname(String markname) {
		this.markname = markname;
	}
	public String getMarkdesc() {
		return markdesc;
	}
	public void setMarkdesc(String markdesc) {
		this.markdesc = markdesc;
	}
	public String getMarktype() {
		return marktype;
	}
	public void setMarktype(String marktype) {
		this.marktype = marktype;
	}
	public double getAtk() {
		return atk;
	}
	public void setAtk(double atk) {
		this.atk = atk;
	}
	public double getHp() {
		return hp;
	}
	public void setHp(double hp) {
		this.hp = hp;
	}
	public double getDef() {
		return def;
	}
	public void setDef(double def) {
		this.def = def;
	}


	public int getDownrand() {
		return downrand;
	}


	public void setDownrand(int downrand) {
		this.downrand = downrand;
	}
	
}