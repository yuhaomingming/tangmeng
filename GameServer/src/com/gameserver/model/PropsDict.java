package com.gameserver.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PropsDict implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5835935265623584547L;


	private String propscode;
	private String propsname;
	private String propsdesc;
	private int propsgrade;
	private int propstype;
	private int propsuse;
	private int propsadd;
	private String usecond;
	private String propspic;
	private ArrayList<packageGoods> packgoods;
	
	public PropsDict(){
		this.packgoods=new ArrayList<packageGoods>();
	}
	public String getPropscode() {
		return propscode;
	}
	public void setPropscode(String propscode) {
		this.propscode = propscode;
	}
	public String getPropsname() {
		return propsname;
	}
	public void setPropsname(String propsname) {
		this.propsname = propsname;
	}
	public String getPropsdesc() {
		return propsdesc;
	}
	public void setPropsdesc(String propsdesc) {
		this.propsdesc = propsdesc;
	}
	public int getPropsgrade() {
		return propsgrade;
	}
	public void setPropsgrade(int propsgrade) {
		this.propsgrade = propsgrade;
	}
	public int getPropstype() {
		return propstype;
	}
	public void setPropstype(int propstype) {
		this.propstype = propstype;
	}
	public int getPropsuse() {
		return propsuse;
	}
	public void setPropsuse(int propsuse) {
		this.propsuse = propsuse;
	}
	public int getPropsadd() {
		return propsadd;
	}
	public void setPropsadd(int propsadd) {
		this.propsadd = propsadd;
	}
	public String getUsecond() {
		return usecond;
	}
	public void setUsecond(String usecond) {
		this.usecond = usecond;
	}
	public ArrayList<packageGoods> getPackgoods() {
		return packgoods;
	}
	public void setPackgoods(ArrayList<packageGoods> packgoods) {
		this.packgoods = packgoods;
	}
	public String getPropspic() {
		return propspic;
	}
	public void setPropspic(String propspic) {
		this.propspic = propspic;
	}

}
