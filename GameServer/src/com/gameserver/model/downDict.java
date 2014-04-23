package com.gameserver.model;

import java.io.Serializable;

public class downDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1504926232349529664L;
	private int downid; //0:关公 1宝箱
	private String downcode; //掉率物品码 @为不指定具体物品
	private int goodstype; //0装备1武器2防具3饰品4技能5技能书页6道具7符文8卡牌
	private int goodslevel;//1精良2稀有3传说  或技能等级 道具等级
	private int downrand; //掉落概率
	private int downnum;//掉落数量
	
	
	
	public int getDownid() {
		return downid;
	}
	public void setDownid(int downid) {
		this.downid = downid;
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

	

}
