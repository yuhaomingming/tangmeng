package com.gameserver.model;

import java.io.Serializable;

public class MarkStringCA implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3488814777935692265L;
	private String mkstring;
	private double mksaddhp;
	private double mksaddatk;
	private double mksadddef;
	private String mkdesc;
	private String mktype;
	private int mkflag;
	
	public int getMkflag() {
		return mkflag;
	}

	public void setMkflag(int mkflag) {
		this.mkflag = mkflag;
	}

	public String getMkstring() {
		return mkstring;
	}

	public String getMkdesc() {
		return mkdesc;
	}

	public void setMkdesc(String mkdesc) {
		this.mkdesc = mkdesc;
	}

	public String getMktype() {
		return mktype;
	}

	public void setMktype(String mktype) {
		this.mktype = mktype;
	}

	public void setMkstring(String mkstring) {
		this.mkstring = mkstring;
	}

	public double getMksaddhp() {
		return mksaddhp;
	}

	public void setMksaddhp(double mksaddhp) {
		this.mksaddhp = mksaddhp;
	}

	public double getMksaddatk() {
		return mksaddatk;
	}

	public void setMksaddatk(double mksaddatk) {
		this.mksaddatk = mksaddatk;
	}

	public double getMksadddef() {
		return mksadddef;
	}

	public void setMksadddef(double mksadddef) {
		this.mksadddef = mksadddef;
	}

	public MarkStringCA(){
		
	}

}
