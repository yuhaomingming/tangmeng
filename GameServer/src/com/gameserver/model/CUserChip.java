package com.gameserver.model;

import java.io.Serializable;

public class CUserChip implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1265967797106240092L;
	private String chipcode;
	private String chipname;
	private String rolename;
	private String serverno;
	private int randval;
	private int needsum;
	private int chiptype;
	private int chipcolor;
	private int curnum;
	public String getChipcode() {
		return chipcode;
	}
	public void setChipcode(String chipcode) {
		this.chipcode = chipcode;
	}
	public String getChipname() {
		return chipname;
	}
	public void setChipname(String chipname) {
		this.chipname = chipname;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getServerno() {
		return serverno;
	}
	public void setServerno(String serverno) {
		this.serverno = serverno;
	}
	public int getRandval() {
		return randval;
	}
	public void setRandval(int randval) {
		this.randval = randval;
	}
	public int getNeedsum() {
		return needsum;
	}
	public void setNeedsum(int needsum) {
		this.needsum = needsum;
	}
	public int getChiptype() {
		return chiptype;
	}
	public void setChiptype(int chiptype) {
		this.chiptype = chiptype;
	}
	public int getChipcolor() {
		return chipcolor;
	}
	public void setChipcolor(int chipcolor) {
		this.chipcolor = chipcolor;
	}
	public int getCurnum() {
		return curnum;
	}
	public void setCurnum(int curnum) {
		this.curnum = curnum;
	}
	
	


}
