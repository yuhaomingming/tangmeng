package com.gameserver.model;

import java.io.Serializable;

public class packageGoods implements Serializable ,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2266511390837620417L;

	private String goodscode;
	private String goodsname;
	private int goodsnum;
	private int innertype;//包内物品类型
	private String goodspic;
	
	public String getGoodspic() {
		return goodspic;
	}
	public void setGoodspic(String goodspic) {
		this.goodspic = goodspic;
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
	public int getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(int goodsnum) {
		this.goodsnum = goodsnum;
	}
	public int getInnertype() {
		return innertype;
	}
	public void setInnertype(int innertype) {
		this.innertype = innertype;
	}
	public packageGoods clone()    
    {    
		packageGoods o=null;    
       try    
        {    
        o=(packageGoods)super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。    
        }    
       catch(CloneNotSupportedException e)    
        {    
            System.out.println(e.toString());    
        }    
       return o;    
    }    
	
	
}
