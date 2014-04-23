package com.gameserver.model;

import java.io.Serializable;

public class SceneDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7337953676859612614L;
	private int sceneno;
	private String scecode;
	private String scenename;
	private int missions;
	private String awardcode;
	private int awardtype;
	private int awardnum;
	private String awardname;
	public int getSceneno() {
		return sceneno;
	}
	public void setSceneno(int sceneno) {
		this.sceneno = sceneno;
	}
	public String getScecode() {
		return scecode;
	}
	public void setScecode(String scecode) {
		this.scecode = scecode;
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
	public String getAwardcode() {
		return awardcode;
	}
	public void setAwardcode(String awardcode) {
		this.awardcode = awardcode;
	}
	public int getAwardtype() {
		return awardtype;
	}
	public void setAwardtype(int awardtype) {
		this.awardtype = awardtype;
	}
	public int getAwardnum() {
		return awardnum;
	}
	public void setAwardnum(int awardnum) {
		this.awardnum = awardnum;
	}
	public String getAwardname() {
		return awardname;
	}
	public void setAwardname(String awardname) {
		this.awardname = awardname;
	}
		 
}
