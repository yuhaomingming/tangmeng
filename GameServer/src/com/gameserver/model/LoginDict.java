package com.gameserver.model;

import java.io.Serializable;

public class LoginDict implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7264412268910989468L;
	private int continuelog;
	private int  money;
	private int gold;
	private int innercoin;
	private int god;
	private int ball;
	private String downcode;
	private int downnum;
	
	
	
	public int getDownnum() {
		return downnum;
	}
	public void setDownnum(int downnum) {
		this.downnum = downnum;
	}
	public String getDowncode() {
		return downcode;
	}
	public void setDowncode(String downcode) {
		this.downcode = downcode;
	}
	public int getContinuelog() {
		return continuelog;
	}
	public void setContinuelog(int continuelog) {
		this.continuelog = continuelog;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getInnercoin() {
		return innercoin;
	}
	public void setInnercoin(int innercoin) {
		this.innercoin = innercoin;
	}
	public int getGod() {
		return god;
	}
	public void setGod(int god) {
		this.god = god;
	}
	public int getBall() {
		return ball;
	}
	public void setBall(int ball) {
		this.ball = ball;
	}


}
