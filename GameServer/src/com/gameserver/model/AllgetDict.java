package com.gameserver.model;

import java.io.Serializable;

public class AllgetDict  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 40169235848053128L;
	private String cardtype;
	private String serverno;
	private Double addhp;
	private Double addatk;
	private Double adddef;
	private String alldesc;
	private String typedesc;
	
	public String getTypedesc() {
		return typedesc;
	}
	public void setTypedesc(String typedesc) {
		this.typedesc = typedesc;
	}
	public String getCardtype() {
		return cardtype;
	}
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	public String getServerno() {
		return serverno;
	}
	public void setServerno(String serverno) {
		this.serverno = serverno;
	}
	public Double getAddhp() {
		return addhp;
	}
	public void setAddhp(Double addhp) {
		this.addhp = addhp;
	}
	public Double getAddatk() {
		return addatk;
	}
	public void setAddatk(Double addatk) {
		this.addatk = addatk;
	}
	public Double getAdddef() {
		return adddef;
	}
	public void setAdddef(Double adddef) {
		this.adddef = adddef;
	}
	public String getAlldesc() {
		return alldesc;
	}
	public void setAlldesc(String alldesc) {
		this.alldesc = alldesc;
	}
	


}
