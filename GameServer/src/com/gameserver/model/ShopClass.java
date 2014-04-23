package com.gameserver.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ShopClass implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8029612712027203889L;
	private String goodscode;
	private String goodsname;
	private String goodsdesc;
	private int goodstype;
	private int goodstyle;
	private int moneyp;
	private int goldp;
	private int buycond;
	private int buysum;
	private int discount;
	private int buystart;
	private int buyend;
	private String dispic;
	private int orderinx;//显示排序字段
	private ArrayList<packageGoods> packgoods;
	
	private String uname;
	private String urole;
	private String  svrcode;
	
	public ShopClass(){
		this.packgoods=new ArrayList<packageGoods>();
	}
	
	public int getOrderinx() {
		return orderinx;
	}

	public void setOrderinx(int orderinx) {
		this.orderinx = orderinx;
	}

	public String getDispic() {
		return dispic;
	}

	public void setDispic(String dispic) {
		this.dispic = dispic;
	}

	public String getGoodscode() {
		return goodscode;
	}
	public void setGoodscode(String goodscode) {
		this.goodscode = goodscode;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getGoodsdesc() {
		return goodsdesc;
	}
	public void setGoodsdesc(String goodsdesc) {
		this.goodsdesc = goodsdesc;
	}
	public int getGoodstype() {
		return goodstype;
	}
	public void setGoodstype(int goodstype) {
		this.goodstype = goodstype;
	}
	public int getGoodstyle() {
		return goodstyle;
	}
	public void setGoodstyle(int goodstyle) {
		this.goodstyle = goodstyle;
	}
	public int getMoneyp() {
		return moneyp;
	}
	public void setMoneyp(int moneyp) {
		this.moneyp = moneyp;
	}
	public int getGoldp() {
		return goldp;
	}
	public void setGoldp(int goldp) {
		this.goldp = goldp;
	}
	public int getBuycond() {
		return buycond;
	}
	public void setBuycond(int buycond) {
		this.buycond = buycond;
	}
	public int getBuysum() {
		return buysum;
	}
	public void setBuysum(int buysum) {
		this.buysum = buysum;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public int getBuystart() {
		return buystart;
	}
	public void setBuystart(int buystart) {
		this.buystart = buystart;
	}
	public int getBuyend() {
		return buyend;
	}
	public void setBuyend(int buyend) {
		this.buyend = buyend;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUrole() {
		return urole;
	}
	public void setUrole(String urole) {
		this.urole = urole;
	}
	public String getSvrcode() {
		return svrcode;
	}
	public void setSvrcode(String svrcode) {
		this.svrcode = svrcode;
	}
	
	public ArrayList<packageGoods> getPackgoods() {
		return packgoods;
	}
	public void setPackgoods(ArrayList<packageGoods> packgoods) {
		this.packgoods = packgoods;
	}
	/*public ShopClass clone()    
    {    
		ShopClass o=null;    
       try    
        {    
        o=(ShopClass)super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。    
        //o.getPackgoods().add(arg0)this.getPackgoods().clone();
        }    
       catch(CloneNotSupportedException e)    
        {    
            System.out.println(e.toString());    
        }    
       return o;    
    }  */
	
	public ShopClass copy() throws IOException, ClassNotFoundException{
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
		 ObjectOutputStream oos = new ObjectOutputStream(bos);
		 oos.writeObject(this);
		 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		 return (ShopClass)ois.readObject();
	}


	
	

}
