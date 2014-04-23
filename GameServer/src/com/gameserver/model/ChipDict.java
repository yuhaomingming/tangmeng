package com.gameserver.model;

import java.io.Serializable;

public class ChipDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -137608813268468301L;
	private String chipcode;
	private String chipname;
	private int randval;
	private int needsum;
	private int chiptype;
	private int chipcolor;
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
	

}
