package com.gameserver.model;

import java.io.Serializable;

public class LiveUpDictCla implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7187888500504984931L;
	private String 	livecode;
	private String 	livetype;
	private int 	hpmax;
	private int hpmin;
	private int atkmax;
	private int atkmin;
	private int defmax;
	private int defmin;
	private int cust;
	private int gold;
	private int vipgrade;

	public LiveUpDictCla(){
		
	}
	public String getLivecode() {
		return livecode;
	}
	public void setLivecode(String livecode) {
		this.livecode = livecode;
	}
	public String getLivetype() {
		return livetype;
	}
	public void setLivetype(String livetype) {
		this.livetype = livetype;
	}
	public int getHpmax() {
		return hpmax;
	}
	public void setHpmax(int hpmax) {
		this.hpmax = hpmax;
	}
	public int getHpmin() {
		return hpmin;
	}
	public void setHpmin(int hpmin) {
		this.hpmin = hpmin;
	}
	public int getAtkmax() {
		return atkmax;
	}
	public void setAtkmax(int atkmax) {
		this.atkmax = atkmax;
	}
	public int getAtkmin() {
		return atkmin;
	}
	public void setAtkmin(int atkmin) {
		this.atkmin = atkmin;
	}
	public int getDefmax() {
		return defmax;
	}
	public void setDefmax(int defmax) {
		this.defmax = defmax;
	}
	public int getDefmin() {
		return defmin;
	}
	public void setDefmin(int defmin) {
		this.defmin = defmin;
	}
	public int getCust() {
		return cust;
	}
	public void setCust(int cust) {
		this.cust = cust;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getVipgrade() {
		return vipgrade;
	}
	public void setVipgrade(int vipgrade) {
		this.vipgrade = vipgrade;
	}
}
