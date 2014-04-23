package com.gameserver.model;

import java.io.Serializable;

public class CNpcDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6322947980345715113L;
	private int npcid; //npc ID
	private String npcprop;//npc属性邪、霸...
	private int npcgrade;//npc等级
	private String npctype;//npc类型杂碎，精英
	private String npccode;//npc编码，图片用
	private int HP;//血
	private int ATK;//攻
	private int DEF;//防
	private int hpalter;//增减
	private int atkalter;//
	private int defalter;//
	private int giveexp;//提供经验
	private int givemoeny;//提供money
	private String skillcode;//技能编码
	private int skillgrade;//技能等级
	public int getNpcid() {
		return npcid;
	}
	public void setNpcid(int npcid) {
		this.npcid = npcid;
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
	public String getNpctype() {
		return npctype;
	}
	public void setNpctype(String npctype) {
		this.npctype = npctype;
	}
	public String getNpccode() {
		return npccode;
	}
	public void setNpccode(String npccode) {
		this.npccode = npccode;
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



}
