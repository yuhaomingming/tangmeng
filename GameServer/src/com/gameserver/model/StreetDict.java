package com.gameserver.model;

import java.io.Serializable;

public class StreetDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1204573503456070537L;
	private int streetid;
	private String streetdesc;
	private String streetname;
	private int giveexp;
	private int givemoeny;
	private int nextid;
	private int gold;//重置消耗
	private int goods1;//指定物品1掉率
	private int goods2;
	private String goods1code;//指定物品1编码
	private String goods2code;//指定物品2编码
	private int marks;//符文掉率
	
	public int getStreetid() {
		return streetid;
	}
	public void setStreetid(int streetid) {
		this.streetid = streetid;
	}
	public String getStreetdesc() {
		return streetdesc;
	}
	public void setStreetdesc(String streetdesc) {
		this.streetdesc = streetdesc;
	}
	public int getGiveexp() {
		return giveexp;
	}
	public void setGiveexp(int giveexp) {
		this.giveexp = giveexp;
	}
	public int getGivemoeny() {
		return givemoeny;
	}
	public void setGivemoeny(int givemoeny) {
		this.givemoeny = givemoeny;
	}
	public int getNextid() {
		return nextid;
	}
	public void setNextid(int nextid) {
		this.nextid = nextid;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public String getStreetname() {
		return streetname;
	}
	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}
	public int getGoods1() {
		return goods1;
	}
	public void setGoods1(int goods1) {
		this.goods1 = goods1;
	}
	public int getGoods2() {
		return goods2;
	}
	public void setGoods2(int goods2) {
		this.goods2 = goods2;
	}
	public String getGoods1code() {
		return goods1code;
	}
	public void setGoods1code(String goods1code) {
		this.goods1code = goods1code;
	}
	public String getGoods2code() {
		return goods2code;
	}
	public void setGoods2code(String goods2code) {
		this.goods2code = goods2code;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	
	

}
