package com.gameserver.model;

import java.io.Serializable;

public class UserLuckAward implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3812440895771492520L;
	private int awardid;
	private String serverno;
	private String awardcode;
	private int goodstype;
	private int goodslevel;
	private int downrand;
	private int downnum;
	private int ifchoose;
	private int currand;//当前中率
	private String piccode;
	
	private String awardname;
	public UserLuckAward(){
		ifchoose=0;
	}
	public int getAwardid() {
		return awardid;
	}
	public void setAwardid(int awardid) {
		this.awardid = awardid;
	}
	public String getServerno() {
		return serverno;
	}
	public void setServerno(String serverno) {
		this.serverno = serverno;
	}
	public String getAwardcode() {
		return awardcode;
	}
	public void setAwardcode(String awardcode) {
		this.awardcode = awardcode;
	}
	public int getGoodstype() {
		return goodstype;
	}
	public void setGoodstype(int goodstype) {
		this.goodstype = goodstype;
	}
	public int getGoodslevel() {
		return goodslevel;
	}
	public void setGoodslevel(int goodslevel) {
		this.goodslevel = goodslevel;
	}
	public int getDownrand() {
		return downrand;
	}
	public void setDownrand(int downrand) {
		this.downrand = downrand;
	}
	public int getDownnum() {
		return downnum;
	}
	public void setDownnum(int downnum) {
		this.downnum = downnum;
	}
	public int getIfchoose() {
		return ifchoose;
	}
	public void setIfchoose(int ifchoose) {
		this.ifchoose = ifchoose;
	}
	public String getAwardname() {
		return awardname;
	}
	public void setAwardname(String awardname) {
		this.awardname = awardname;
	}
	public int getCurrand() {
		return currand;
	}
	public void setCurrand(int currand) {
		this.currand = currand;
	}
	public String getPiccode() {
		return piccode;
	}
	public void setPiccode(String piccode) {
		this.piccode = piccode;
	}


}
