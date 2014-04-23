package com.gameserver.model;

import java.io.Serializable;

public class CMissionNpcDict implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -766871500526994853L;
	private int missionid;//关卡id
	private int npcid;//npc id
	private int npcloc;//npc 位置
	private String npccode;//npc编码，图片用
	private String npcname;//npc名称
	private String npcprop;//npc属性邪、霸...
	private String npctype;//npc类型杂碎，精英
	private int npcgrade;//npc等级
	private int HP;//血
	private int ATK;//攻
	private int DEF;//防
	private int hpalter;//增减
	private int atkalter;//
	private int defalter;//
	private String skillcode;//技能编码
	private int skillgrade;//技能等级
	private int curhp;//当前血每次加载随机运算
	private int curatk;//攻
	private int curdef;//防
	private int allhp;//战斗时未开打前血
	private int allatk;//攻
	private int alldef;//防
	
	
	public int getAllhp() {
		return allhp;
	}
	public void setAllhp(int allhp) {
		this.allhp = allhp;
	}
	public int getAllatk() {
		return allatk;
	}
	public void setAllatk(int allatk) {
		this.allatk = allatk;
	}
	public int getAlldef() {
		return alldef;
	}
	public void setAlldef(int alldef) {
		this.alldef = alldef;
	}
	public int getCurhp() {
		return curhp;
	}
	public void setCurhp(int curhp) {
		this.curhp = curhp;
	}
	public int getCuratk() {
		return curatk;
	}
	public void setCuratk(int curatk) {
		this.curatk = curatk;
	}
	public int getCurdef() {
		return curdef;
	}
	public void setCurdef(int curdef) {
		this.curdef = curdef;
	}
	public String getNpctype() {
		return npctype;
	}
	public void setNpctype(String npctype) {
		this.npctype = npctype;
	}
	public int getMissionid() {
		return missionid;
	}
	public void setMissionid(int missionid) {
		this.missionid = missionid;
	}
	public int getNpcid() {
		return npcid;
	}
	public void setNpcid(int npcid) {
		this.npcid = npcid;
	}
	public int getNpcloc() {
		return npcloc;
	}
	public void setNpcloc(int npcloc) {
		this.npcloc = npcloc;
	}
	public String getNpccode() {
		return npccode;
	}
	public void setNpccode(String npccode) {
		this.npccode = npccode;
	}
	public String getNpcname() {
		return npcname;
	}
	public void setNpcname(String npcname) {
		this.npcname = npcname;
	}
	public String getNpcprop() {
		return npcprop;
	}
	public void setNpcprop(String npcprop) {
		this.npcprop = npcprop;
	}
	public int getNpcgrade() {
		return npcgrade;
	}
	public void setNpcgrade(int npcgrade) {
		this.npcgrade = npcgrade;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int hp) {
		HP = hp;
	}
	public int getATK() {
		return ATK;
	}
	public void setATK(int atk) {
		ATK = atk;
	}
	public int getDEF() {
		return DEF;
	}
	public void setDEF(int def) {
		DEF = def;
	}
	public int getHpalter() {
		return hpalter;
	}
	public void setHpalter(int hpalter) {
		this.hpalter = hpalter;
	}
	public int getAtkalter() {
		return atkalter;
	}
	public void setAtkalter(int atkalter) {
		this.atkalter = atkalter;
	}
	public int getDefalter() {
		return defalter;
	}
	public void setDefalter(int defalter) {
		this.defalter = defalter;
	}
	public String getSkillcode() {
		return skillcode;
	}
	public void setSkillcode(String skillcode) {
		this.skillcode = skillcode;
	}
	public int getSkillgrade() {
		return skillgrade;
	}
	public void setSkillgrade(int skillgrade) {
		this.skillgrade = skillgrade;
	}
	public CMissionNpcDict clone()    
    {    
		CMissionNpcDict o=null;    
       try    
        {    
        o=(CMissionNpcDict)super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。    
        }    
       catch(CloneNotSupportedException e)    
        {    
            System.out.println(e.toString());    
        }    
       return o;    
    }    

}
