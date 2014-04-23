package com.gameserver.model;

import java.io.Serializable;

public class UserMark implements Serializable {

	private static final long serialVersionUID = 8274966801248449302L;

	private Integer markid; //符文id
	private String markcode;
	private String markname;  //符文名
	private String markdesc;  //描述
	private String marktype;  //符文类型 攻防血
	private double atk;  //增攻
	private double hp;   //增血
	private double def;  //增防
	private int deviceid;  //插入装备id
	private int cardid; //卡牌id
	private int markloc;   //符文位置
	
	public UserMark(){
		
	}
	/*@Override  
	public boolean equals(Object other) {
	    // Not strictly necessary, but often a good optimization
	    if (this == other)	      return true;
	    if (other == null) return false;
	    if (getClass() != other.getClass()) return false;
	    final UserMark otherA = (UserMark) other;
	   if(otherA.markid==this.markid) return true;
	   else return false;
	  }
	@Override  
    public int hashCode()   
    {   
        final int prime = 31;   
        int result = 17;   
        result = prime * result + ((markid == 0) ? 0 : markid.hashCode());   
        return result;   
    }   */

	public String getMarkcode() {
		return markcode;
	}


	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}


	public int getMarkid() {
		return markid;
	}
	public void setMarkid(int markid) {
		this.markid = markid;
	}
	public String getMarkname() {
		return markname;
	}
	public void setMarkname(String markname) {
		this.markname = markname;
	}
	public String getMarkdesc() {
		return markdesc;
	}
	public void setMarkdesc(String markdesc) {
		this.markdesc = markdesc;
	}
	public String getMarktype() {
		return marktype;
	}
	public void setMarktype(String marktype) {
		this.marktype = marktype;
	}
	public double getAtk() {
		return atk;
	}
	public void setAtk(double atk) {
		this.atk = atk;
	}
	public double getHp() {
		return hp;
	}
	public void setHp(double hp) {
		this.hp = hp;
	}
	public double getDef() {
		return def;
	}
	public void setDef(double def) {
		this.def = def;
	}
	public int getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}
	public int getMarkloc() {
		return markloc;
	}
	public void setMarkloc(int markloc) {
		this.markloc = markloc;
	}


	public int getCardid() {
		return cardid;
	}
	public void setCardid(int cardid) {
		this.cardid = cardid;
	}
	
}