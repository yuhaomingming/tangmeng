package com.gameserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StoryTalkDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8028808686695421596L;
	private int missionid;
	private int talktype;
	private int talkseq;
	private int rolestyle;
	private int talkloc;
	private String talkrole;
	private String talkcont;
	
	private HashMap<Integer,ArrayList<HashMap<String,Object>>> starttalktypeMap;//战斗前
	private HashMap<Integer,ArrayList<HashMap<String,Object>>> endtalktypeMap;//战斗后
	public StoryTalkDict(){
		this.starttalktypeMap=new HashMap<Integer,ArrayList<HashMap<String,Object>>>();
	}
	
	
	public int getMissionid() {
		return missionid;
	}
	public void setMissionid(int missionid) {
		this.missionid = missionid;
	}
	public int getTalktype() {
		return talktype;
	}
	public void setTalktype(int talktype) {
		this.talktype = talktype;
	}
	public int getTalkseq() {
		return talkseq;
	}
	public void setTalkseq(int talkseq) {
		this.talkseq = talkseq;
	}
	public int getRolestyle() {
		return rolestyle;
	}
	public void setRolestyle(int rolestyle) {
		this.rolestyle = rolestyle;
	}
	public int getTalkloc() {
		return talkloc;
	}
	public void setTalkloc(int talkloc) {
		this.talkloc = talkloc;
	}
	public String getTalkrole() {
		return talkrole;
	}
	public void setTalkrole(String talkrole) {
		this.talkrole = talkrole;
	}
	public String getTalkcont() {
		return talkcont;
	}
	public void setTalkcont(String talkcont) {
		this.talkcont = talkcont;
	}


	public HashMap<Integer, ArrayList<HashMap<String, Object>>> getStarttalktypeMap() {
		return starttalktypeMap;
	}


	public void setStarttalktypeMap(
			HashMap<Integer, ArrayList<HashMap<String, Object>>> starttalktypeMap) {
		this.starttalktypeMap = starttalktypeMap;
	}


	public HashMap<Integer, ArrayList<HashMap<String, Object>>> getEndtalktypeMap() {
		return endtalktypeMap;
	}


	public void setEndtalktypeMap(
			HashMap<Integer, ArrayList<HashMap<String, Object>>> endtalktypeMap) {
		this.endtalktypeMap = endtalktypeMap;
	}


	
	


}
