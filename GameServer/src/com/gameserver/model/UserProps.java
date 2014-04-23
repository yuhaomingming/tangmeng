package com.gameserver.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProps implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3361731758310520497L;
	private String propscode;//道具code
	private String propsname;   //道具名称
	private String propsdesc;   //道具描述
	private int propstype;   //道具类型
	private int propsgrade;    // 道具等级
	private int propsnum;  //数量
	private int propsuse;//使用效果0可用类道具：1体力、2夺计次数等
	private int propsadd;//道具加成1增加体力 2增加夺计次数
	private String usecond;//宝箱钥匙code
	private String propspic;
	private ArrayList<packageGoods> packgoods;
	
	public UserProps(){
		this.packgoods=new  ArrayList<packageGoods>();
	}
	/*@Override  
	public boolean equals(Object other) {
	    // Not strictly necessary, but often a good optimization
	    if (this == other)	      return true;
	    if (other == null) return false;
	    if (getClass() != other.getClass()) return false;
	    final UserProps otherA = (UserProps) other;
	   if(otherA.propscode.equals(this.propscode)) return true;
	   else return false;
	  }
	@Override  
    public int hashCode()   
    {   
        final int prime = 31;   
        int result = 17;   
        result = prime * result + ((propscode == null) ? 0 : propscode.hashCode());   
        return result;   
       
    }   */
	public String getPropspic() {
		return propspic;
	}

	public void setPropspic(String propspic) {
		this.propspic = propspic;
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
	public int getPropstype() {
		return propstype;
	}
	public void setPropstype(int propstype) {
		this.propstype = propstype;
	}
	public int getPropsgrade() {
		return propsgrade;
	}
	public void setPropsgrade(int propsgrade) {
		this.propsgrade = propsgrade;
	}
	public int getPropsnum() {
		return propsnum;
	}
	public void setPropsnum(int propsnum) {
		this.propsnum = propsnum;
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

}


