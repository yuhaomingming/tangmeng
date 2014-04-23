package com.gameserver.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserCard implements Serializable,Cloneable{
	private static final long serialVersionUID = -8482200351084734683L;
	private Integer cardid;  //玩家卡牌id
	private String cardcode; //卡牌编码
	private String cardname; //卡牌名称
	private String carddesc; //描述
	private String cardprop;  //属性邪、萌、冷、霸
	private int starlevel;  //星级
	private String cardtype; //系列 同系2个以上有增益
	private int upexp;     //升级经验
	private int curexp;   //当前经验
	private int curvalue;  //魅力值
	private double basehp; //基础血成长
	private double baseatk;   //基础攻成长
	private double basedef;   //基础防成长
	private double alterhp;   //基础转生血成长
	private double alteratk;  //基础转生攻成长
	private double alterdef;  //基础转生防成长
	private int cardlevel; //卡牌等级
	private int Bcardlevel; //战前卡牌等级
	private int hp;  //血量
	private int atk; //攻
	private int def;  //防
	private int curstyle;  //当前形态
	private int ifuse;   //是否阵容中
	private int locate;   //阵容中位置
	private String setid; //套装ID '@' 无套装
	private int sameflag=0; //阵容同系卡
	private int havedevice;
	private int haveskill;
	private int orighp;  //裸身血
	private int origatk;
	private int origdef;
	private int add_hp=0; //补正增益
	private int add_atk=0;//同系出战人数
	private int add_def=0;
	private int livehp; //培养值
	private int liveatk;
	private int livedef;
	private int devhp; //装备带来的buff值
	private int devatk;
	private int defdef;
	private int skhp; //技能带来的buff值
	private int skatk;
	private int skdef;
	private int curhp; //战斗时血攻防
	private int curatk;
	private int curdef;
	private int tlivehp;//临时记录培养血攻防，保存后清0
	private int tliveatk;
	private int tlivedef;
	private int altercurstyle;//当前形态变化值
	private int sameaddhp;//同系补正增血
	private int sameaddatk;//同系补正曾攻
	private int sameadddef;//同系补正增防
	private String lifeskill;//天赋技能
	private int curpointval;//魅力点数
	
	private Map<Integer,UserDevice> deviceMap;//装备列表 <装备ID，装备>
	private Map<Integer,UserSkill> skillMap; //技能列表
 
	public UserCard(){
		this.deviceMap= new HashMap<Integer,UserDevice>();
		this.skillMap = new HashMap<Integer,UserSkill>();
		this.curhp=0;
		this.curatk=0;
		this.curdef=0;
		this.cardid=0;
		this.altercurstyle=0;
		this.sameaddatk=0;
		this.sameadddef=0;
		this.sameaddhp=0;
		this.curpointval=0;
	}

	/*public int PreCheckCardVal(int transval,int nowpointval,int nowval){
		int remainval=0;
		int upval=0;
		if((transval+nowpointval)>=(nowval+1)*10){
			remainval=transval+nowpointval-(nowval+1)*10;
			upval=upval+1;
			nowval=nowval+1;
			if(remainval>=(nowval+1)*10){
				upval=upval+PreCheckCardVal(remainval,0,nowval);
			}
		}else
			upval=0;
		return upval;
	}*/
	public int CheckCardVal(int transval){
		int remainval=0;
		int upval=0;
		if((transval+this.curpointval)>=(this.curvalue+1)*10){
			upval=upval+1;
			remainval=transval+this.curpointval-(this.curvalue+1)*10;
			this.curpointval=remainval;
			this.curvalue=this.curvalue+1;
			if(remainval>=(this.curvalue+1)*10){
				upval=upval+CheckCardVal(0);
			}
		}else{
			this.curpointval=this.curpointval+transval;
			upval=0;
		}
		return upval;
	}
	/*@Override  
	public boolean equals(Object other) {
	    // Not strictly necessary, but often a good optimization
	    if (this == other)	      return true;
	    if (other == null) return false;
	    if (getClass() != other.getClass()) return false;
	    final UserCard otherA = (UserCard) other;
	   if(otherA.getCardid()==this.cardid) return true;
	   else return false;
	  }
	@Override  
    public int hashCode()   
    {   
        final int prime = 31;   
        int result = 17;   
        result = prime * result + ((cardid == 0) ? 0 : cardid.hashCode());   
        return result;   
       
    }   */
	
	public String getCardcode() {
		return cardcode;
	}

	public void setCardcode(String cardcode) {
		this.cardcode = cardcode;
	}

	public int getCardid() {
		return cardid;
	}

	public void setCardid(int cardid) {
		this.cardid = cardid;
	}

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public String getCarddesc() {
		return carddesc;
	}

	public void setCarddesc(String carddesc) {
		this.carddesc = carddesc;
	}

	public String getCardprop() {
		return cardprop;
	}

	public void setCardprop(String cardprop) {
		this.cardprop = cardprop;
	}

	public int getStarlevel() {
		return starlevel;
	}

	public void setStarlevel(int starlevel) {
		this.starlevel = starlevel;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public int getUpexp() {
		return upexp;
	}

	public void setUpexp(int upexp) {
		this.upexp = upexp;
	}

	public int getCurexp() {
		return curexp;
	}

	public void setCurexp(int curexp) {
		this.curexp = curexp;
	}

	public int getCurvalue() {
		return curvalue;
	}

	public void setCurvalue(int curvalue) {
		this.curvalue = curvalue;
	}

	public double getBasehp() {
		return basehp;
	}

	public void setBasehp(double basehp) {
		this.basehp = basehp;
	}

	public double getBaseatk() {
		return baseatk;
	}

	public void setBaseatk(double baseatk) {
		this.baseatk = baseatk;
	}

	public double getBasedef() {
		return basedef;
	}

	public void setBasedef(double basedef) {
		this.basedef = basedef;
	}

	public double getAlterhp() {
		return alterhp;
	}

	public void setAlterhp(double alterhp) {
		this.alterhp = alterhp;
	}

	public double getAlteratk() {
		return alteratk;
	}

	public void setAlteratk(double alteratk) {
		this.alteratk = alteratk;
	}

	public double getAlterdef() {
		return alterdef;
	}

	public void setAlterdef(double alterdef) {
		this.alterdef = alterdef;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getCurstyle() {
		return curstyle;
	}

	public void setCurstyle(int curstyle) {
		this.curstyle = curstyle;
	}

	public int getIfuse() {
		return ifuse;
	}

	public void setIfuse(int ifuse) {
		this.ifuse = ifuse;
	}

	public int getLocate() {
		return locate;
	}

	public void setLocate(int locate) {
		this.locate = locate;
	}

	public String getSetid() {
		return setid;
	}

	public void setSetid(String setid) {
		this.setid = setid;
	}

	public Map<Integer, UserDevice> getDeviceMap() {
		return deviceMap;
	}

	public void setDeviceMap(Map<Integer, UserDevice> deviceMap) {
		this.deviceMap = deviceMap;
	}

	public int getSameflag() {
		return sameflag;
	}

	public void setSameflag(int sameflag) {
		this.sameflag = sameflag;
	}

	public Map<Integer, UserSkill> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(Map<Integer, UserSkill> skillMap) {
		this.skillMap = skillMap;
	}

	public int getHavedevice() {
		return havedevice;
	}

	public void setHavedevice(int havedevice) {
		this.havedevice = havedevice;
	}

	public int getHaveskill() {
		return haveskill;
	}

	public void setHaveskill(int haveskill) {
		this.haveskill = haveskill;
	}

	public int getOrighp() {
		return orighp;
	}

	public void setOrighp(int orighp) {
		this.orighp = orighp;
	}

	public int getOrigatk() {
		return origatk;
	}

	public void setOrigatk(int origatk) {
		this.origatk = origatk;
	}

	public int getOrigdef() {
		return origdef;
	}

	public void setOrigdef(int origdef) {
		this.origdef = origdef;
	}

	public int getAdd_hp() {
		return add_hp;
	}

	public void setAdd_hp(int add_hp) {
		this.add_hp = add_hp;
	}

	public int getAdd_atk() {
		return add_atk;
	}

	public void setAdd_atk(int add_atk) {
		this.add_atk = add_atk;
	}

	public int getAdd_def() {
		return add_def;
	}

	public void setAdd_def(int add_def) {
		this.add_def = add_def;
	}

	public int getLivehp() {
		return livehp;
	}

	public void setLivehp(int livehp) {
		this.livehp = livehp;
	}

	public int getLiveatk() {
		return liveatk;
	}

	public void setLiveatk(int liveatk) {
		this.liveatk = liveatk;
	}

	public int getLivedef() {
		return livedef;
	}

	public void setLivedef(int livedef) {
		this.livedef = livedef;
	}

	public int getCardlevel() {
		return cardlevel;
	}

	public void setCardlevel(int cardlevel) {
		this.cardlevel = cardlevel;
	}

	public int getDevhp() {
		return devhp;
	}

	public void setDevhp(int devhp) {
		this.devhp = devhp;
	}

	public int getDevatk() {
		return devatk;
	}

	public void setDevatk(int devatk) {
		this.devatk = devatk;
	}

	public int getDefdef() {
		return defdef;
	}

	public void setDefdef(int defdef) {
		this.defdef = defdef;
	}

	public int getSkhp() {
		return skhp;
	}

	public void setSkhp(int skhp) {
		this.skhp = skhp;
	}

	public int getSkatk() {
		return skatk;
	}

	public void setSkatk(int skatk) {
		this.skatk = skatk;
	}

	public int getSkdef() {
		return skdef;
	}

	public void setSkdef(int skdef) {
		this.skdef = skdef;
	}

	public int getCurhp() {
		return curhp;
	}

	public void setCurhp(int curhp) {
		this.curhp = curhp;
	}

	public int getCuratk() {
		return curatk;
	}

	public void setCuratk(int curatk) {
		this.curatk = curatk;
	}

	public int getCurdef() {
		return curdef;
	}

	public void setCurdef(int curdef) {
		this.curdef = curdef;
	}

	public int getTlivehp() {
		return tlivehp;
	}

	public void setTlivehp(int tlivehp) {
		this.tlivehp = tlivehp;
	}

	public int getTliveatk() {
		return tliveatk;
	}

	public void setTliveatk(int tliveatk) {
		this.tliveatk = tliveatk;
	}

	public int getTlivedef() {
		return tlivedef;
	}

	public void setTlivedef(int tlivedef) {
		this.tlivedef = tlivedef;
	}
	
	public int getAltercurstyle() {
		return altercurstyle;
	}

	public void setAltercurstyle(int altercurstyle) {
		this.altercurstyle = altercurstyle;
	}

	public int getSameaddhp() {
		return sameaddhp;
	}

	public void setSameaddhp(int sameaddhp) {
		this.sameaddhp = sameaddhp;
	}

	public int getSameaddatk() {
		return sameaddatk;
	}

	public void setSameaddatk(int sameaddatk) {
		this.sameaddatk = sameaddatk;
	}

	public int getSameadddef() {
		return sameadddef;
	}

	public void setSameadddef(int sameadddef) {
		this.sameadddef = sameadddef;
	}

	public String getLifeskill() {
		return lifeskill;
	}

	public void setLifeskill(String lifeskill) {
		this.lifeskill = lifeskill;
	}
	
	public int getBcardlevel() {
		return Bcardlevel;
	}

	public void setBcardlevel(int bcardlevel) {
		Bcardlevel = bcardlevel;
	}

	public int getCurpointval() {
		return curpointval;
	}

	public void setCurpointval(int curpointval) {
		this.curpointval = curpointval;
	}

	public UserCard clone()    
    {    
		UserCard o=null;    
       try    
        {    
        o=(UserCard)super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。    
        }    
       catch(CloneNotSupportedException e)    
        {    
            System.out.println(e.toString());    
        }    
       return o;    
    }    

	
	
}
