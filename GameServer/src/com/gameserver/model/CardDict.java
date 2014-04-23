package com.gameserver.model;

import java.io.Serializable;

public class CardDict implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3522289214904767772L;
	private String cardcode; //卡牌编码
	private String cardname; //卡牌名称
	private String carddesc; //描述
	private String cardprop;  //属性邪、萌、冷、霸
	private int starlevel;  //星级
	private String cardtype; //系列 同系2个以上有增益
	private double basehp; //基础血成长
	private double baseatk;   //基础攻成长
	private double basedef;   //基础防成长
	private double alterhp;   //基础转生血成长
	private double alteratk;  //基础转生攻成长
	private double alterdef;  //基础转生防成长
	private String lifeskill;//天赋技能

	public String getCardcode() {
		return cardcode;
	}

	public void setCardcode(String cardcode) {
		this.cardcode = cardcode;
	}

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public String getCarddesc() {
		return carddesc;
	}

	public void setCarddesc(String carddesc) {
		this.carddesc = carddesc;
	}

	public String getCardprop() {
		return cardprop;
	}

	public void setCardprop(String cardprop) {
		this.cardprop = cardprop;
	}

	public int getStarlevel() {
		return starlevel;
	}

	public void setStarlevel(int starlevel) {
		this.starlevel = starlevel;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public double getBasehp() {
		return basehp;
	}

	public void setBasehp(double basehp) {
		this.basehp = basehp;
	}

	public double getBaseatk() {
		return baseatk;
	}

	public void setBaseatk(double baseatk) {
		this.baseatk = baseatk;
	}

	public double getBasedef() {
		return basedef;
	}

	public void setBasedef(double basedef) {
		this.basedef = basedef;
	}

	public double getAlterhp() {
		return alterhp;
	}

	public void setAlterhp(double alterhp) {
		this.alterhp = alterhp;
	}

	public double getAlteratk() {
		return alteratk;
	}

	public void setAlteratk(double alteratk) {
		this.alteratk = alteratk;
	}

	public double getAlterdef() {
		return alterdef;
	}

	public void setAlterdef(double alterdef) {
		this.alterdef = alterdef;
	}

	public String getLifeskill() {
		return lifeskill;
	}

	public void setLifeskill(String lifeskill) {
		this.lifeskill = lifeskill;
	}	
	
}
