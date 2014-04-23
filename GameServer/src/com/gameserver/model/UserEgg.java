package com.gameserver.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

import com.gameserver.handler.GameServerHandler;

public class UserEgg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8345433828166041587L;
	private static final Logger logger = Logger.getLogger(UserEgg.class.getName());
	private String rolename;
	private String serverno;
	private String eggcode;
	private int starttime;
	private int remainnum;
	private int remaintime;
	private int opennum;
	private String eggname;
	private int star1;
	private int star2;
	private int star3;
	private int star4;
	private int star5;
	private int star6;
	private int daynum;
	private int stepsec;
	private int cost;
	private int ifopen;
	private int firstdown;
	private int discount;
	private int uprand;
	private int stime;
	private int endtime;

	/*public int openEggDown(){
		int starno=0;
		if(this.ifopen>0){//可以开蛋
			this.ifopen=0;
			if(this.daynum>0&&this.remainnum>0) {//标示此蛋每日开蛋有次数限制,用完不计时
				this.remainnum=this.remainnum-1;
				if(this.remainnum==0) this.starttime=0;
				else this.starttime=(int)(System.currentTimeMillis()/1000);
			}else{//此类蛋remainnum始终为1，无次数限制
				if(this.starttime==0&&this.eggcode.equals("e3")) {//金蛋第一次免费开蛋掉4星卡
					this.starttime=(int)(System.currentTimeMillis()/1000);
					this.remainnum=0;
					this.remaintime=this.stepsec;
					//logger.info("首次开蛋掉："+4);
					return 4;
				}else{
					this.starttime=(int)(System.currentTimeMillis()/1000);
					this.remainnum=0;
				}
				
			}
			if(this.starttime>0){//计时中
				int k=(int)(System.currentTimeMillis()/1000);
				this.remaintime=this.stepsec+this.starttime-k;
			}
		}else{//直接金条开蛋
			this.opennum=this.opennum+1;
			if(this.firstdown>0){
				//logger.info("金条首开："+firstdown);
				if(this.opennum==1){
					return this.firstdown;
				}
			}
		}
		int[] a={star1,star2,star3,star4,star5};
		int[] loc={0,1,2,3,4};
		int mloc=0,maxloc=0,t=0;
		for(int i=0;i<a.length;i++){
			t=a[i];
			maxloc=i;
			for(int j=i+1;j<a.length;j++){
				if(t<a[j]){
					t=a[j];
					maxloc=j;
				}
			}
			a[maxloc]=a[i];
			a[i]=t;
			mloc=loc[i];
			loc[i]=loc[maxloc];
			loc[maxloc]=mloc;
		}
		int maxval=0;
	    int minval=0;
		Random rd = new Random();
		int k=rd.nextInt(100);
		for(int i=0;i<a.length;i++){
			minval=0;
			maxval=maxval+a[i];
			if(k>=minval && k<maxval){
				starno=loc[i];
				break;
			}
		}
		return starno+1;
	}*/
	/*@Override  
	public boolean equals(Object other) {
	    // Not strictly necessary, but often a good optimization
	    if (this == other)	      return true;
	    if (other == null) return false;
	    if (getClass() != other.getClass()) return false;
	    final UserEgg otherA = (UserEgg) other;
	   if(otherA.rolename.equals(this.rolename)&&otherA.serverno.equals(this.serverno)&&otherA.eggcode.equals(eggcode)) return true;
	   else return false;
	  }
	@Override  
    public int hashCode()   
    {   
        final int prime = 31;   
        int result = 17;   
        result = prime * result + ((rolename == null) ? 0 : rolename.hashCode());   
        result = prime * result + ((serverno == null) ? 0 : serverno.hashCode());
        result = prime * result + ((eggcode == null) ? 0 : eggcode.hashCode());
        return result;   
       
    }   */
	
	public int getFirstdown() {
		return firstdown;
	}

	public void setFirstdown(int firstdown) {
		this.firstdown = firstdown;
	}

	public int getStarttime() {
		return starttime;
	}
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}
	public int getRemainnum() {
		return remainnum;
	}
	public void setRemainnum(int remainnum) {
		this.remainnum = remainnum;
	}
	public int getIfopen() {
		return ifopen;
	}
	public void setIfopen(int ifopen) {
		this.ifopen = ifopen;
	}
	public String getEggname() {
		return eggname;
	}
	public void setEggname(String eggname) {
		this.eggname = eggname;
	}
	public int getStar1() {
		return star1;
	}
	public void setStar1(int star1) {
		this.star1 = star1;
	}
	public int getStar2() {
		return star2;
	}
	public void setStar2(int star2) {
		this.star2 = star2;
	}
	public int getStar3() {
		return star3;
	}
	public void setStar3(int star3) {
		this.star3 = star3;
	}
	public int getStar4() {
		return star4;
	}
	public void setStar4(int star4) {
		this.star4 = star4;
	}
	public int getStar5() {
		return star5;
	}
	public void setStar5(int star5) {
		this.star5 = star5;
	}
	public int getDaynum() {
		return daynum;
	}
	public void setDaynum(int daynum) {
		this.daynum = daynum;
	}
	public int getStepsec() {
		return stepsec;
	}
	public void setStepsec(int stepsec) {
		this.stepsec = stepsec;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
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
	public String getEggcode() {
		return eggcode;
	}
	public void setEggcode(String eggcode) {
		this.eggcode = eggcode;
	}
	public int getRemaintime() {
		return remaintime;
	}
	public void setRemaintime(int remaintime) {
		this.remaintime = remaintime;
	}
	public int getOpennum() {
		return opennum;
	}
	public void setOpennum(int opennum) {
		this.opennum = opennum;
	}

	public int getStar6() {
		return star6;
	}

	public void setStar6(int star6) {
		this.star6 = star6;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getUprand() {
		return uprand;
	}

	public void setUprand(int uprand) {
		this.uprand = uprand;
	}

	public int getStime() {
		return stime;
	}

	public void setStime(int stime) {
		this.stime = stime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	

}
