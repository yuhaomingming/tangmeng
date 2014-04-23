package com.gameserver.model;

import java.io.Serializable;

public class SkillPages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7867927940600974752L;
	private String skillcode;
	private String skillname; //技能名称
	private int pagenum;      //书页数量
	private int page1;        //书页1数量
	private int page2;		  //书页2数量
	private int page3;		  //书页3数量
	private int page4;      //书页4数量
	private int page5;      //书页5数量  -1表示没有此书页
	private String skdesc;  //技能描述
	private String sktype;  //技能类型 主动被动
	private int skgrade;    //技能等级
	private int diycustom;  //合成消耗
	private double atks;    //攻击增量 小于1表示百分比
	private double defs;
	private double hps;
	private int svs;       //增量系数
	private double hurt;  //伤害附加值
	private int skillto; //0群攻 1单体
	private int skillrand;//发动几率
	private double hurtpert;//伤害百分比
	private double uphurtper;//升级伤害百分比
	private double uphurt;//升级伤害附加百分比
	private int skillcolor;
	
	/*@Override  
	public boolean equals(Object other) {
	    // Not strictly necessary, but often a good optimization
	    if (this == other)	      return true;
	    if (other == null) return false;
	    if (getClass() != other.getClass()) return false;
	    final SkillPages otherA = (SkillPages) other;
	   if(otherA.skillcode.equals(this.skillcode)) return true;
	   else return false;
	  }
	@Override  
    public int hashCode()   
    {   
        final int prime = 31;   
        int result = 17;   
        result = prime * result + ((skillcode == null) ? 0 : skillcode.hashCode());   
        return result;   
       
    }   */
	
	public String getSkillcode() {
		return skillcode;
	}
	public int getSkillcolor() {
		return skillcolor;
	}
	public void setSkillcolor(int skillcolor) {
		this.skillcolor = skillcolor;
	}
	public void setSkillcode(String skillcode) {
		this.skillcode = skillcode;
	}
	public String getSkillname() {
		return skillname;
	}
	public void setSkillname(String skillname) {
		this.skillname = skillname;
	}
	public int getPagenum() {
		return pagenum;
	}
	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}
	public int getPage1() {
		return page1;
	}
	public void setPage1(int page1) {
		this.page1 = page1;
	}
	public int getPage2() {
		return page2;
	}
	public void setPage2(int page2) {
		this.page2 = page2;
	}
	public int getPage3() {
		return page3;
	}
	public void setPage3(int page3) {
		this.page3 = page3;
	}
	public int getPage4() {
		return page4;
	}
	public void setPage4(int page4) {
		this.page4 = page4;
	}
	public int getPage5() {
		return page5;
	}
	public void setPage5(int page5) {
		this.page5 = page5;
	}
	public String getSkdesc() {
		return skdesc;
	}
	public void setSkdesc(String skdesc) {
		this.skdesc = skdesc;
	}
	public String getSktype() {
		return sktype;
	}
	public void setSktype(String sktype) {
		this.sktype = sktype;
	}
	public int getSkgrade() {
		return skgrade;
	}
	public void setSkgrade(int skgrade) {
		this.skgrade = skgrade;
	}
	public int getDiycustom() {
		return diycustom;
	}
	public void setDiycustom(int diycustom) {
		this.diycustom = diycustom;
	}
	public double getAtks() {
		return atks;
	}
	public void setAtks(double atks) {
		this.atks = atks;
	}
	public double getDefs() {
		return defs;
	}
	public void setDefs(double defs) {
		this.defs = defs;
	}
	public double getHps() {
		return hps;
	}
	public void setHps(double hps) {
		this.hps = hps;
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
	public int getSkillto() {
		return skillto;
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

	
	
	
}
