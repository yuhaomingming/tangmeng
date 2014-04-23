package com.authserver.model;

import java.io.Serializable;


public class GameServer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1460277829361189938L;
	//private final Channel channel;
	private String code = "";// 服务器编码
	private String name = "";//
	private String ipadd = "";
	private String port = "";
	private String ifrun = "0";
	private String ifnew = "";
	private String ifdef= "";
	private String gamever="";
	private String ResVersion="0";
	private String LastVersion="@";
	private String maindisp="@";
	private String topdisp="@";
	private String bottdisp="@";
	private int starttime=0;
	private int endtime=0;
	


	public String getResVersion() {
		return ResVersion;
	}

	public void setResVersion(String resVersion) {
		ResVersion = resVersion;
	}

	public String getIfdef() {
		return ifdef;
	}

	public void setIfdef(String ifdef) {
		this.ifdef = ifdef;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getIpadd() {
		return ipadd;
	}

	public String getPort() {
		return port;
	}

	public String getIfrun() {
		return ifrun;
	}

	public String getIfnew() {
		return ifnew;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIpadd(String ipadd) {
		this.ipadd = ipadd;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setIfrun(String ifrun) {
		this.ifrun = ifrun;
	}

	public void setIfnew(String ifnew) {
		this.ifnew = ifnew;
	}

	public String getGamever() {
		return gamever;
	}

	public void setGamever(String gamever) {
		this.gamever = gamever;
	}

	public String getLastVersion() {
		return LastVersion;
	}

	public void setLastVersion(String lastVersion) {
		LastVersion = lastVersion;
	}

	public String getMaindisp() {
		return maindisp;
	}

	public void setMaindisp(String maindisp) {
		this.maindisp = maindisp;
	}

	public String getTopdisp() {
		return topdisp;
	}

	public void setTopdisp(String topdisp) {
		this.topdisp = topdisp;
	}

	public String getBottdisp() {
		return bottdisp;
	}

	public void setBottdisp(String bottdisp) {
		this.bottdisp = bottdisp;
	}

	public int getStarttime() {
		return starttime;
	}

	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

}
