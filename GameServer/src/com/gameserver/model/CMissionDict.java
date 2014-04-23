package com.gameserver.model;

import java.io.Serializable;

public class CMissionDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103321097512880551L;

	private int missionid; //关卡id
	private int sceneno;//场景id
	private String scenecode;//场景code
	private int missionseq;//关卡序号1-8 9为挑战关
	private String scenename;//场景名称
	private int missions;//任务数
	private int missionmp;//体力消耗
	private int copymp;//副本体力消耗
	private int skpage;//技能书页掉率
	private int devone;//精良装备掉率
	private int devtwo;//稀有装备掉率
	private int devthr;//传说装备掉率
	private int star1;//1星卡
	private int star2;//2星卡
	private int star3;//3星卡
	private int star4;//4星卡
	private int star5;//5星卡
	private int marks;//符文掉率
	private int grade;//建议等级或挑战关所需等级
	private String mdesc;//描述
	private int giveexp;
	private int givemoeny;
	private int losepercent;
	private int curnum;
	private int nextmission; //下一关id
	private int goods1;//指定物品1掉率
	private int goods2;
	private String goods1code;//指定物品编码
	private String goods2code;//指定物品编码
	private int prop1;//道具掉率
	private int prop2;
	private int prop3;
	private String missname;//关卡名称
	
	public int getNextmission() {
		return nextmission;
	}
	public void setNextmission(int nextmission) {
		this.nextmission = nextmission;
	}
	public int getCurnum() {
		return curnum;
	}
	public void setCurnum(int curnum) {
		this.curnum = curnum;
	}
	public int getGiveexp() {
		return giveexp;
	}
	public void setGiveexp(int giveexp) {
		this.giveexp = giveexp;
	}
	public int getGivemoeny() {
		return givemoeny;
	}
	public void setGivemoeny(int givemoeny) {
		this.givemoeny = givemoeny;
	}
	public int getLosepercent() {
		return losepercent;
	}
	public void setLosepercent(int losepercent) {
		this.losepercent = losepercent;
	}
	public String getScenecode() {
		return scenecode;
	}
	public void setScenecode(String scenecode) {
		this.scenecode = scenecode;
	}
	public int getMissionid() {
		return missionid;
	}
	public void setMissionid(int missionid) {
		this.missionid = missionid;
	}

	public int getSceneno() {
		return sceneno;
	}
	public void setSceneno(int sceneno) {
		this.sceneno = sceneno;
	}
	public int getMissionseq() {
		return missionseq;
	}
	public void setMissionseq(int missionseq) {
		this.missionseq = missionseq;
	}
	public String getScenename() {
		return scenename;
	}
	public void setScenename(String scenename) {
		this.scenename = scenename;
	}
	public int getMissions() {
		return missions;
	}
	public void setMissions(int missions) {
		this.missions = missions;
	}
	public int getMissionmp() {
		return missionmp;
	}
	public void setMissionmp(int missionmp) {
		this.missionmp = missionmp;
	}
	public int getCopymp() {
		return copymp;
	}
	public void setCopymp(int copymp) {
		this.copymp = copymp;
	}
	public int getSkpage() {
		return skpage;
	}
	public void setSkpage(int skpage) {
		this.skpage = skpage;
	}
	public int getDevone() {
		return devone;
	}
	public void setDevone(int devone) {
		this.devone = devone;
	}
	public int getDevtwo() {
		return devtwo;
	}
	public void setDevtwo(int devtwo) {
		this.devtwo = devtwo;
	}
	public int getDevthr() {
		return devthr;
	}
	public void setDevthr(int devthr) {
		this.devthr = devthr;
	}
	public int getStar1() {
		return star1;
	}
	public void setStar1(int star1) {
		this.star1 = star1;
	}
	public int getStar2() {
		return star2;
	}
	public void setStar2(int star2) {
		this.star2 = star2;
	}
	public int getStar3() {
		return star3;
	}
	public void setStar3(int star3) {
		this.star3 = star3;
	}
	public int getStar4() {
		return star4;
	}
	public void setStar4(int star4) {
		this.star4 = star4;
	}
	public int getStar5() {
		return star5;
	}
	public void setStar5(int star5) {
		this.star5 = star5;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getMdesc() {
		return mdesc;
	}
	public void setMdesc(String mdesc) {
		this.mdesc = mdesc;
	}
	public int getGoods1() {
		return goods1;
	}
	public void setGoods1(int goods1) {
		this.goods1 = goods1;
	}
	public int getGoods2() {
		return goods2;
	}
	public void setGoods2(int goods2) {
		this.goods2 = goods2;
	}
	public String getGoods1code() {
		return goods1code;
	}
	public void setGoods1code(String goods1code) {
		this.goods1code = goods1code;
	}
	public String getGoods2code() {
		return goods2code;
	}
	public void setGoods2code(String goods2code) {
		this.goods2code = goods2code;
	}
	public int getProp1() {
		return prop1;
	}
	public void setProp1(int prop1) {
		this.prop1 = prop1;
	}
	public int getProp2() {
		return prop2;
	}
	public void setProp2(int prop2) {
		this.prop2 = prop2;
	}
	public int getProp3() {
		return prop3;
	}
	public void setProp3(int prop3) {
		this.prop3 = prop3;
	}
	public String getMissname() {
		return missname;
	}
	public void setMissname(String missname) {
		this.missname = missname;
	}
	
	

}
