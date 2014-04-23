package com.gameserver.model;

import java.io.Serializable;
import java.util.ArrayList;


public class UserStreet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 644488650978952213L;
	private String rolename;
	private String serverno;
	private String curtime;//最近任务时间
	private int winlose; //胜负
	private Integer streetid; //关卡id
	private String streetname;
	private String streetdesc;//描述
	private int ifcanuse;//是否开启
	private int iflock;//是否已锁 挑战成功锁定
	private int ifgold;//是否重置
	private int giveexp;
	private int givemoeny;
	private int nextid;//下一关id
	private int gold;//重置消耗
	private int goods1;//指定物品1掉率
	private int goods2;
	private String goods1code;//指定物品1编码
	private String goods2code;//指定物品2编码
	private int marks;//符文掉率
	
	
    private ArrayList<CMissionNpcDict> snpclist;//NPC阵容
	
    public UserStreet(){
    	this.snpclist= new ArrayList<CMissionNpcDict>();
    }
   /* @Override  
	public boolean equals(Object other) {
	    // Not strictly necessary, but often a good optimization
	    if (this == other)	      return true;
	    if (other == null) return false;
	    if (getClass() != other.getClass()) return false;
	    final UserStreet otherA = (UserStreet) other;
	   if(otherA.streetid==this.streetid) return true;
	   else return false;
	  }
	@Override  
    public int hashCode()   
    {   
        final int prime = 31;   
        int result = 17;   
        result = prime * result + ((streetid == 0) ? 0 : streetid.hashCode());   
        return result;   
       
    }   */
	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getServerno() {
		return serverno;
	}

	public void setServerno(String serverno) {
		this.serverno = serverno;
	}

	public String getCurtime() {
		return curtime;
	}

	public void setCurtime(String curtime) {
		this.curtime = curtime;
	}

	public int getWinlose() {
		return winlose;
	}

	public void setWinlose(int winlose) {
		this.winlose = winlose;
	}

	public int getStreetid() {
		return streetid;
	}

	public void setStreetid(int streetid) {
		this.streetid = streetid;
	}

	public String getStreetdesc() {
		return streetdesc;
	}

	public void setStreetdesc(String streetdesc) {
		this.streetdesc = streetdesc;
	}

	public int getIfcanuse() {
		return ifcanuse;
	}

	public void setIfcanuse(int ifcanuse) {
		this.ifcanuse = ifcanuse;
	}

	public int getGiveexp() {
		return giveexp;
	}

	public void setGiveexp(int giveexp) {
		this.giveexp = giveexp;
	}

	public int getGivemoeny() {
		return givemoeny;
	}

	public void setGivemoeny(int givemoeny) {
		this.givemoeny = givemoeny;
	}
	public int getNextid() {
		return nextid;
	}

	public void setNextid(int nextid) {
		this.nextid = nextid;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}


	public String getStreetname() {
		return streetname;
	}

	public void setStreetname(String streetname) {
		this.streetname = streetname;
	}

	public ArrayList<CMissionNpcDict> getSnpclist() {
		return snpclist;
	}

	public void setSnpclist(ArrayList<CMissionNpcDict> snpclist) {
		this.snpclist = snpclist;
	}

	public int getIfgold() {
		return ifgold;
	}

	public void setIfgold(int ifgold) {
		this.ifgold = ifgold;
	}

	public int getIflock() {
		return iflock;
	}

	public void setIflock(int iflock) {
		this.iflock = iflock;
	}

	public int getGoods1() {
		return goods1;
	}

	public void setGoods1(int goods1) {
		this.goods1 = goods1;
	}

	public int getGoods2() {
		return goods2;
	}

	public void setGoods2(int goods2) {
		this.goods2 = goods2;
	}

	public String getGoods1code() {
		return goods1code;
	}

	public void setGoods1code(String goods1code) {
		this.goods1code = goods1code;
	}

	public String getGoods2code() {
		return goods2code;
	}

	public void setGoods2code(String goods2code) {
		this.goods2code = goods2code;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}
    
	

}
