package com.gameserver.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.gameserver.comm.CommTools;

public class WorldBossDict {
	private int bhour;
	private int bminute;
	private int ehour;
	private int eminute;	
	private String srvno;
	private String bossID;
	private int basehp;
	private int curhp;
	private int sumhp;
	private int bossval;//boss等级
	private int deadTime;
	private String curtime;
	private boolean isnew;
	private String bossname;
	private ArrayList<HashMap<String,String>> lastrank;
	private ArrayList<HashMap<String,String>> killer;
	
	public WorldBossDict(String bossexp){
		lastrank = new ArrayList<HashMap<String,String>>(3);
		killer = new ArrayList<HashMap<String,String>>(1);
		String datestr[]=bossexp.split(",");
		isnew = true;
		basehp = 0;
		bossval = 0;
		deadTime = 0;
		curtime = "";
		bhour = Integer.parseInt(datestr[0]);
		bminute = Integer.parseInt(datestr[1]);
		ehour = Integer.parseInt(datestr[2]);
		eminute = Integer.parseInt(datestr[3]);
		sumhp = 0;
		curhp=0;
	}
	
	//hart 0boss已经死了, 1打了boss 2打死了boss
	public synchronized int BossHurt(int value){
		int hart = 0;
		if(curhp<=0){
			hart = 0;//0伤害 boss已经死了
		}else{
			curhp-=value;
			if(curhp<=0){
				deadTime = (int)(System.currentTimeMillis()/1000) - CommTools.GetTimeInMillis(0,this.bhour, this.bminute);
				hart = 2;
			}else hart = 1;
			
		}
		if(isnew){
			isnew = false;
		}
		return hart;
	}
	
	public String getSrvno() {
		return srvno;
	}
	public void setSrvno(String srvno) {
		this.srvno = srvno;
	}
	public String getBossID() {
		return bossID;
	}
	public void setBossID(String bossID) {
		this.bossID = bossID;
	}

	public int getBasehp() {
		return basehp;
	}

	public void setBasehp(int basehp) {
		this.basehp = basehp;
	}

	public int getCurhp() {
		return curhp;
	}

	public void setCurhp(int curhp) {
		this.curhp = curhp;
	}

	public int getDeadTime() {
		return deadTime;
	}
	public void setDeadTime(int deadTime) {
		this.deadTime = deadTime;
	}
	public int getBossval() {
		return bossval;
	}
	public void setBossval(int bossval) {
		this.bossval = bossval;
	}
	public String getCurtime() {
		return curtime;
	}
	public void setCurtime(String curtime) {
		this.curtime = curtime;
	}
	public ArrayList<HashMap<String, String>> getLastrank() {
		return lastrank;
	}
	public void setLastrank(ArrayList<HashMap<String, String>> lastrank) {
		this.lastrank = lastrank;
	}
	public boolean isIsnew() {
		return isnew;
	}
	public void setIsnew(boolean isnew) {
		this.isnew = isnew;
	}

	public ArrayList<HashMap<String, String>> getKiller() {
		return killer;
	}

	public void setKiller(ArrayList<HashMap<String, String>> killer) {
		this.killer = killer;
	}

	public int getBhour() {
		return bhour;
	}

	public void setBhour(int bhour) {
		this.bhour = bhour;
	}

	public int getBminute() {
		return bminute;
	}

	public void setBminute(int bminute) {
		this.bminute = bminute;
	}

	public int getEhour() {
		return ehour;
	}

	public void setEhour(int ehour) {
		this.ehour = ehour;
	}

	public int getEminute() {
		return eminute;
	}

	public void setEminute(int eminute) {
		this.eminute = eminute;
	}

	public int getSumhp() {
		return sumhp;
	}

	public void setSumhp(int sumhp) {
		this.sumhp = sumhp;
	}

	public String getBossname() {
		return bossname;
	}

	public void setBossname(String bossname) {
		this.bossname = bossname;
	}
	
}
