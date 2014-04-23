package com.gameserver.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserDevice implements Serializable {

	private static final long serialVersionUID = 4407288748917682030L;
	private Integer deviceid; //装备id
	private String devicecode; //
	private String devicename; //装备名称
	private String devicedesc; //描述
	private String devicetype;  //类型 攻防饰
	private String quality;  //质量
	private int grade;  //等级
	private int dhp;  //血增
	private int datk;  //攻增
	private int ddef;  //防增
	private int baseprice;  //基础价
	private int price;  //实际价
	private int cardid;  //装备卡牌id
	private String markstring; //符文组合串 空：无组合
	private int havemark; //是否有符文
	private int devcolor;
	
	private Map<Integer,UserMark> markMap;//符文列表<符文id，符文类>
	private Map<Integer,DeviceUpgrade> dupgradelist; //装备精炼字典
	
	public UserDevice(){
		this.markMap= new HashMap<Integer,UserMark>();
		this.dupgradelist = new  HashMap<Integer,DeviceUpgrade>();
	}
	/*@Override  
	public boolean equals(Object other) {
	    // Not strictly necessary, but often a good optimization
	    if (this == other)	      return true;
	    if (other == null) return false;
	    if (getClass() != other.getClass()) return false;
	    final UserDevice otherA = (UserDevice) other;
	   if(otherA.deviceid==this.deviceid) return true;
	   else return false;
	  }
	@Override  
    public int hashCode()   
    {   
        final int prime = 31;   
        int result = 17;   
        result = prime * result + ((deviceid == 0) ? 0 : deviceid.hashCode());   
        return result;   
       
    }   */
	
	public String getDevicecode() {
		return devicecode;
	}


	public void setDevicecode(String devicecode) {
		this.devicecode = devicecode;
	}


	public int getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getDevicedesc() {
		return devicedesc;
	}
	public void setDevicedesc(String devicedesc) {
		this.devicedesc = devicedesc;
	}
	public String getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getDhp() {
		return dhp;
	}


	public void setDhp(int dhp) {
		this.dhp = dhp;
	}


	public int getDatk() {
		return datk;
	}


	public void setDatk(int datk) {
		this.datk = datk;
	}


	public int getDdef() {
		return ddef;
	}


	public void setDdef(int ddef) {
		this.ddef = ddef;
	}


	public int getBaseprice() {
		return baseprice;
	}
	public void setBaseprice(int baseprice) {
		this.baseprice = baseprice;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getCardid() {
		return cardid;
	}
	public void setCardid(int cardid) {
		this.cardid = cardid;
	}
	public String getMarkstring() {
		return markstring;
	}
	public void setMarkstring(String markstring) {
		this.markstring = markstring;
	}
	public Map<Integer, UserMark> getMarkMap() {
		return markMap;
	}
	public void setMarkMap(Map<Integer, UserMark> markMap) {
		this.markMap = markMap;
	}


	public int getHavemark() {
		return havemark;
	}


	public void setHavemark(int havemark) {
		this.havemark = havemark;
	}

	public Map<Integer, DeviceUpgrade> getDupgradelist() {
		return dupgradelist;
	}


	public void setDupgradelist(Map<Integer, DeviceUpgrade> dupgradelist) {
		this.dupgradelist = dupgradelist;
	}

	public int getDevcolor() {
		return devcolor;
	}

	public void setDevcolor(int devcolor) {
		this.devcolor = devcolor;
	}
	
}
