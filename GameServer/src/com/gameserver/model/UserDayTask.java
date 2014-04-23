package com.gameserver.model;

import java.io.Serializable;

public class UserDayTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1274455338503957971L;
	private int taskid;
	private String 	taskdesc;
	private int taskcond;
	private String tasktype;
	private int curcond;
	private int ifsuccess;
	private int money;
	private int gold;
	private int ball;
	
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public String getTaskdesc() {
		return taskdesc;
	}
	public void setTaskdesc(String taskdesc) {
		this.taskdesc = taskdesc;
	}
	public int getTaskcond() {
		return taskcond;
	}
	public void setTaskcond(int taskcond) {
		this.taskcond = taskcond;
	}
	public String getTasktype() {
		return tasktype;
	}
	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}
	public int getCurcond() {
		return curcond;
	}
	public void setCurcond(int curcond) {
		this.curcond = curcond;
	}
	public int getIfsuccess() {
		return ifsuccess;
	}
	public void setIfsuccess(int ifsuccess) {
		this.ifsuccess = ifsuccess;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getBall() {
		return ball;
	}
	public void setBall(int ball) {
		this.ball = ball;
	}
	
	

}
