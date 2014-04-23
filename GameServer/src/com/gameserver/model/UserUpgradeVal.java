package com.gameserver.model;

import java.io.Serializable;

public class UserUpgradeVal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2926130175577244166L;
	private int grade;
	private int upsum;
	private int maxpower;
	private int members;
	private int perpoint;
	private int cardpercent;
	private int upcount;//卡牌升到此等级所需总经验
	
	
	public int getUpcount() {
		return upcount;
	}
	public void setUpcount(int upcount) {
		this.upcount = upcount;
	}
	public int getCardpercent() {
		return cardpercent;
	}
	public void setCardpercent(int cardpercent) {
		this.cardpercent = cardpercent;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getUpsum() {
		return upsum;
	}
	public void setUpsum(int upsum) {
		this.upsum = upsum;
	}
	public int getMaxpower() {
		return maxpower;
	}
	public void setMaxpower(int maxpower) {
		this.maxpower = maxpower;
	}
	public int getMembers() {
		return members;
	}
	public void setMembers(int members) {
		this.members = members;
	}
	public int getPerpoint() {
		return perpoint;
	}
	public void setPerpoint(int perpoint) {
		this.perpoint = perpoint;
	}


}
