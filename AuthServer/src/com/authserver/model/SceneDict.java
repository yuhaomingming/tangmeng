package com.authserver.model;

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
	
	

	 
}
