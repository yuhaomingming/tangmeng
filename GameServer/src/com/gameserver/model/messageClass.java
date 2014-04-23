package com.gameserver.model;

import java.io.Serializable;

public class messageClass implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4988231547672900351L;
	private int messid;//
	private String serverno;//服务器号
	private int messtype;//0通知1封号踢人2登录消息
	private String messcont;//内容
	private int exptime;//0为失效  生效的登录消息设为1
	private int usemess;//@为全部、指定角色名
	private int starttime;//消息生效时间
	private int pubnum;//播出次数
	private String messtitle;
	
	public String getMesstitle() {
		return messtitle;
	}
	public void setMesstitle(String messtitle) {
		this.messtitle = messtitle;
	}
	public int getMessid() {
		return messid;
	}
	public void setMessid(int messid) {
		this.messid = messid;
	}
	public String getServerno() {
		return serverno;
	}
	public void setServerno(String serverno) {
		this.serverno = serverno;
	}
	public int getMesstype() {
		return messtype;
	}
	public void setMesstype(int messtype) {
		this.messtype = messtype;
	}
	public String getMesscont() {
		return messcont;
	}
	public void setMesscont(String messcont) {
		this.messcont = messcont;
	}
	public int getExptime() {
		return exptime;
	}
	public void setExptime(int exptime) {
		this.exptime = exptime;
	}
	public int getUsemess() {
		return usemess;
	}
	public void setUsemess(int usemess) {
		this.usemess = usemess;
	}
	public int getStarttime() {
		return starttime;
	}
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}
	public int getPubnum() {
		return pubnum;
	}
	public void setPubnum(int pubnum) {
		this.pubnum = pubnum;
	}

	

}
