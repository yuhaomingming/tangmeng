package com.gameserver.model;

import java.io.Serializable;

public class UpLevelDict implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5995079261866984152L;
	private int curlevle; //等级
	private int downmoney;
	private int downgold;
	private String downcode; //掉率物品码
	private int goodstype; //0装备1武器2防具3饰品4技能5技能书页6道具7符文8卡牌
	private int goodslevel;//1精良2稀有3传说  或技能等级 道具等级
	private int downnum;//掉落数量
	
	
	public int getCurlevle() {
		return curlevle;
	}
	public void setCurlevle(int curlevle) {
		this.curlevle = curlevle;
	}
	public int getDownmoney() {
		return downmoney;
	}
	public void setDownmoney(int downmoney) {
		this.downmoney = downmoney;
	}
	public int getDowngold() {
		return downgold;
	}
	public void setDowngold(int downgold) {
		this.downgold = downgold;
	}
	public String getDowncode() {
		return downcode;
	}
	public void setDowncode(String downcode) {
		this.downcode = downcode;
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
	public int getDownnum() {
		return downnum;
	}
	public void setDownnum(int downnum) {
		this.downnum = downnum;
	}

	

}
