package com.gameserver.model;

import java.io.Serializable;

public class UserSkill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8204400452581268768L;
	private Integer skillid;  //技能id
	private String skillcode;
	private String skillname;   //技能名称
	private String skilldesc;   //技能描述
	private String skilltype;   //技能类型 主动、被动
	private int skillgrade;    // 技能等级
	private double shp; //增血
	private double satk; //增攻
	private double sdef; //增防
	private int cardid;  //装备卡id
	private double hurt; //直接伤害值  >0有直接伤害 1表示hp+atk 2 atk+def 3 hp+def 4 hp 5 atk 6 def
	private int svs; //伤害系数
	private int skillto; //0群攻 1单体
	private int skillrand;//发动几率
	private double hurtpert;//伤害百分比
	private double uphurtper;//升级伤害百分比
	private double uphurt;//升级伤害附加百分比
	private int skillcolor;
	private int skillloc;//技能安装位置
	private int iflife;//是否出生技能
	//伤害值=（hp+atk）*等级/系数
	
	/*@Override  
	public boolean equals(Object other) {
	    // Not strictly necessary, but often a good optimization
	    if (this == other)	      return true;
	    if (other == null) return false;
	    if (getClass() != other.getClass()) return false;
	   final UserSkill otherA = (UserSkill) other;
	   if(otherA.getSkillid()==this.skillid) return true;
	   else return false;
	  }
	@Override  
    public int hashCode()   
    {   
        final int prime = 31;   
        int result = 17;   
        result = prime * result + ((skillid == 0) ? 0 : skillid.hashCode());   
        return result;   
    }   */
	
	public int getSkillto() {
		return skillto;
	}
	public int getSkillcolor() {
		return skillcolor;
	}
	public void setSkillcolor(int skillcolor) {
		this.skillcolor = skillcolor;
	}
	public double getHurtpert() {
		return hurtpert;
	}
	public void setHurtpert(double hurtpert) {
		this.hurtpert = hurtpert;
	}
	public double getUphurtper() {
		return uphurtper;
	}
	public void setUphurtper(double uphurtper) {
		this.uphurtper = uphurtper;
	}
	public double getUphurt() {
		return uphurt;
	}
	public void setUphurt(double uphurt) {
		this.uphurt = uphurt;
	}
	public void setSkillto(int skillto) {
		this.skillto = skillto;
	}
	public int getSkillrand() {
		return skillrand;
	}
	public void setSkillrand(int skillrand) {
		this.skillrand = skillrand;
	}
	public int getSvs() {
		return svs;
	}
	public void setSvs(int svs) {
		this.svs = svs;
	}
	public double getHurt() {
		return hurt;
	}
	public void setHurt(double hurt) {
		this.hurt = hurt;
	}
	public String getSkillcode() {
		return skillcode;
	}
	public void setSkillcode(String skillcode) {
		this.skillcode = skillcode;
	}
	public int getSkillid() {
		return skillid;
	}
	public void setSkillid(int skillid) {
		this.skillid = skillid;
	}
	public String getSkillname() {
		return skillname;
	}
	public void setSkillname(String skillname) {
		this.skillname = skillname;
	}
	public String getSkilldesc() {
		return skilldesc;
	}
	public void setSkilldesc(String skilldesc) {
		this.skilldesc = skilldesc;
	}
	public String getSkilltype() {
		return skilltype;
	}
	public void setSkilltype(String skilltype) {
		this.skilltype = skilltype;
	}
	public int getSkillgrade() {
		return skillgrade;
	}
	public void setSkillgrade(int skillgrade) {
		this.skillgrade = skillgrade;
	}
	public double getShp() {
		return shp;
	}
	public void setShp(double shp) {
		this.shp = shp;
	}
	public double getSatk() {
		return satk;
	}
	public void setSatk(double satk) {
		this.satk = satk;
	}
	public double getSdef() {
		return sdef;
	}
	public void setSdef(double sdef) {
		this.sdef = sdef;
	}
	public int getCardid() {
		return cardid;
	}
	public void setCardid(int cardid) {
		this.cardid = cardid;
	}
	public int getSkillloc() {
		return skillloc;
	}
	public void setSkillloc(int skillloc) {
		this.skillloc = skillloc;
	}
	public int getIflife() {
		return iflife;
	}
	public void setIflife(int iflife) {
		this.iflife = iflife;
	}
	public void setSkillid(Integer skillid) {
		this.skillid = skillid;
	}
	
	
	
}


