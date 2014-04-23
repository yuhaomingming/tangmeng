package com.gameserver.model;

import java.io.Serializable;

public class Pkexpmoney implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -155186085455597282L;
	private int ulevel;
	private int exp;
	private int money;
	public int getUlevel() {
		return ulevel;
	}
	public void setUlevel(int ulevel) {
		this.ulevel = ulevel;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
}
