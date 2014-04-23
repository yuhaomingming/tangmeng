package com.gameserver.model;

import java.io.Serializable;

public class Cpointdict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6906640773949004739L;
	private int exchid;
	private String exchcode;
	private String exchdesc;
	private int goodstype;
	private int costpoint;
	private int takecond;
	public int getExchid() {
		return exchid;
	}
	public void setExchid(int exchid) {
		this.exchid = exchid;
	}
	public String getExchcode() {
		return exchcode;
	}
	public void setExchcode(String exchcode) {
		this.exchcode = exchcode;
	}
	public String getExchdesc() {
		return exchdesc;
	}
	public void setExchdesc(String exchdesc) {
		this.exchdesc = exchdesc;
	}
	public int getGoodstype() {
		return goodstype;
	}
	public void setGoodstype(int goodstype) {
		this.goodstype = goodstype;
	}
	public int getCostpoint() {
		return costpoint;
	}
	public void setCostpoint(int costpoint) {
		this.costpoint = costpoint;
	}
	public int getTakecond() {
		return takecond;
	}
	public void setTakecond(int takecond) {
		this.takecond = takecond;
	}
	
}
