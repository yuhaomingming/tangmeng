package com.gameserver.model;

import java.io.Serializable;

public class UserMailBox implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2059509354177055935L;
	private int mailid;
	private String rolename;
	private String serverno;
	private String mailtop;
	private String messcont;
	private String goodscode;
	private String piccode;
	private int ifuse;
	private int givenum;
	private int goodstype;//0道具1卡2装备3技能4符文5转生丹
	private int givegold;
	private int givemoney;
	
	
	public int getGivegold() {
		return givegold;
	}
	public void setGivegold(int givegold) {
		this.givegold = givegold;
	}
	public int getGivemoney() {
		return givemoney;
	}
	public void setGivemoney(int givemoney) {
		this.givemoney = givemoney;
	}
	public int getMailid() {
		return mailid;
	}
	public void setMailid(int mailid) {
		this.mailid = mailid;
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
	public String getMesscont() {
		return messcont;
	}
	public void setMesscont(String messcont) {
		this.messcont = messcont;
	}
	public String getGoodscode() {
		return goodscode;
	}
	public void setGoodscode(String goodscode) {
		this.goodscode = goodscode;
	}
	public String getPiccode() {
		return piccode;
	}
	public void setPiccode(String piccode) {
		this.piccode = piccode;
	}
	public int getIfuse() {
		return ifuse;
	}
	public void setIfuse(int ifuse) {
		this.ifuse = ifuse;
	}
	public int getGivenum() {
		return givenum;
	}
	public void setGivenum(int givenum) {
		this.givenum = givenum;
	}
	public int getGoodstype() {
		return goodstype;
	}
	public void setGoodstype(int goodstype) {
		this.goodstype = goodstype;
	}
	public String getMailtop() {
		return mailtop;
	}
	public void setMailtop(String mailtop) {
		this.mailtop = mailtop;
	}
	


}
