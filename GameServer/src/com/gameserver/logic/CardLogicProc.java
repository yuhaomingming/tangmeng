package com.gameserver.logic;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.gameserver.comm.CommTools;
import com.gameserver.comm.PacketCommandType;
import com.gameserver.dao.GameServerDao;
import com.gameserver.manager.GameManager;
import com.gameserver.model.AllgetDict;
import com.gameserver.model.CLifeUpDict;
import com.gameserver.model.DeviceDict;
import com.gameserver.model.DeviceSetDict;
import com.gameserver.model.DeviceUpgrade;
import com.gameserver.model.MarkStringCA;
import com.gameserver.model.MergeCardDict;
import com.gameserver.model.SkillPages;
import com.gameserver.model.User;
import com.gameserver.model.UserCard;
import com.gameserver.model.UserDevice;
import com.gameserver.model.UserMark;
import com.gameserver.model.UserSkill;
import com.gameserver.model.lotluck;


public class CardLogicProc implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8829226218658855641L;
	private static final Logger logger = Logger.getLogger(CardLogicProc.class.getName());
	
	public int calvalue(int orival,double nowval){
		int val=0;
		if(nowval>0){ //判断是增值还是增百分比
			if(nowval<1){ //百分比
				val=(int)Math.floor(orival*nowval);
			}else
				val=(int)Math.floor(nowval);
		}
		return val;
	}
	//计算装备带来的buff
	public String CalcDeviceAdd(User p,UserCard uc,int did){//计算装备带来的buff
		int uchp=0,ucatk=0,ucdef=0; //变化值
		double ucahp=0,ucaatk=0,ucadef=0;
		int t1=uc.getOrighp();//卡牌裸值
		int t2=uc.getOrigatk();
		int t3=uc.getOrigdef();
		String dsetid="@"; //是否形成套装
		UserDevice ud=p.getUdevicelist().get(did);
		uchp=calvalue(t1,ud.getDhp());//变化值
		ucatk=calvalue(t2,ud.getDatk());
		ucdef=calvalue(t3,ud.getDdef());
		/*//判断是否形成套装
		String w="",d="",r="",devicestring=""; //武器，防护，饰品
		Iterator<UserDevice> its = uc.getDeviceMap().values().iterator();
		while(its.hasNext()){
			UserDevice um = its.next();
			if(um.getDevicetype().equals("攻")) w=um.getDevicecode();
			if(um.getDevicetype().equals("防")) d=um.getDevicecode();
			if(um.getDevicetype().equals("饰")) r=um.getDevicecode();					
		}
		if(ud.getDevicetype().equals("攻")) w=ud.getDevicecode();
		if(ud.getDevicetype().equals("防")) d=ud.getDevicecode();
		if(ud.getDevicetype().equals("饰")) r=ud.getDevicecode();
		devicestring=w+","+d+","+r;	
		if(GameManager.getInstance().getDevicesetdict().containsKey(devicestring)){
			DeviceSetDict dsd=GameManager.getInstance().getDevicesetdict().get(devicestring);
			ucahp=dsd.getDdhp();
			ucaatk=dsd.getDdatk();
			ucadef=dsd.getDddef();
			uchp=uchp+calvalue(t1,ucahp);
			ucatk=ucatk+calvalue(t2,ucaatk);
			ucdef=ucdef+calvalue(t3,ucadef);
			dsetid=devicestring; //套装id
		}*/
		if(ud.getHavemark()>0){ //有符文 计算符文增益
			if(!ud.getMarkstring().equals("@")){ //有符文串计算符文组增益
				MarkStringCA msca=GameManager.getInstance().getMarksetdict().get(ud.getMarkstring());
				ucahp=msca.getMksaddhp();
				ucaatk=msca.getMksaddatk();
				ucadef=msca.getMksadddef();
				uchp=uchp+calvalue(t1,ucahp);
				ucatk=ucatk+calvalue(t2,ucaatk);
				ucdef=ucdef+calvalue(t3,ucadef);
			}
			//计算各符文增量
			Iterator<UserMark> it = ud.getMarkMap().values().iterator();
			while(it.hasNext()){
				UserMark um = it.next();
				uchp=uchp+calvalue(t1,um.getHp());
				ucatk=ucatk+calvalue(t2,um.getAtk());
				ucdef=ucdef+calvalue(t3,um.getDef());
			}
		}
		//计算卡牌对应装备缘分
		if(GameManager.getInstance().getCarddevicedict().containsKey(uc.getCardcode())){
			Map<String,lotluck> dmap=GameManager.getInstance().getCarddevicedict().get(uc.getCardcode());
			if(dmap.containsKey(ud.getDevicecode())){
				lotluck ll = dmap.get(ud.getDevicecode());
				uchp=uchp+calvalue(t1,ll.getAddhp());
				ucatk=ucatk+calvalue(t2,ll.getAddatk());
				ucdef=ucdef+calvalue(t3,ll.getAdddef());
			}
		}		
		uc.setDevhp(uchp); //穿上装备带来的增量
		uc.setDevatk(ucatk);
		uc.setDefdef(ucdef);
		return dsetid;
	}

	//穿上装备
	public void LoadDev(User p,UserCard uc,int did) {
		String dsetid="@";
		dsetid=CalcDeviceAdd(p,uc,did);
		int uchp=0,ucatk=0,ucdef=0;
		uchp=uc.getDevhp();
		ucatk=uc.getDevatk();
		ucdef=uc.getDefdef();
		UserDevice ud=p.getUdevicelist().get(did);
		uc.setHp(uc.getHp()+uchp);
		uc.setAtk(uc.getAtk()+ucatk);
		uc.setDef(uc.getDef()+ucdef);
		uc.setSetid(dsetid);
		uc.setHavedevice(1);
		/*if(uc.getIfuse()>0){//卡牌在阵容中，引起用户总血攻防变化
			p.setUhp(p.getUhp()+uchp);
			p.setUatk(p.getUatk()+ucatk);
			p.setUdef(p.getUdef()+ucdef);
		}		*/		
		ud.setCardid(uc.getCardid());//装备对应卡牌id	
		uc.getDeviceMap().put(did, ud); //卡牌对应装备列表
					
	}
	//安装技能
	public void InstSkill(User p,UserCard uc,int sid,int skloc){
		int uchp=0,ucatk=0,ucdef=0;
		CalcSkillAdd(p,uc,sid);
		UserSkill us=p.getUskilllist().get(sid);
		us.setSkillloc(skloc);
		uchp=uc.getSkhp();//增加值
		ucatk=uc.getSkatk();
		ucdef=uc.getSkdef();
		//logger.info("技能增血:"+uchp+"攻："+ucatk+"防："+ucdef);
		uc.setHp(uc.getHp()+uchp);
		uc.setAtk(uc.getAtk()+ucatk);
		uc.setDef(uc.getDef()+ucdef);
		uc.setHaveskill(1);
		/*if(uc.getIfuse()>0){//卡牌在阵容中，引起用户总血攻防变化
			p.setUhp(p.getUhp()+uchp);
			p.setUatk(p.getUatk()+ucatk);
			p.setUdef(p.getUdef()+ucdef);
		}		*/		
		us.setCardid(uc.getCardid());//技能对应卡牌id
		uc.getSkillMap().put(sid, us);
	}
	//计算技能buff值
	public void CalcSkillAdd(User p,UserCard uc,int sid){
		int t1=uc.getOrighp();//卡牌裸值
		int t2=uc.getOrigatk();
		int t3=uc.getOrigdef();
		int uchp=0,ucatk=0,ucdef=0;
		int flag=0;
		UserSkill us=p.getUskilllist().get(sid);
		if(us.getSkilltype().equals("被动")){
			if(us.getShp()>0)	uchp=calvalue(t1,us.getShp()+us.getUphurtper()*us.getSkillgrade());//变化值
			if(us.getSatk()>0)  ucatk=calvalue(t2,us.getSatk()+us.getUphurtper()*us.getSkillgrade());
			if(us.getSdef()>0)  ucdef=calvalue(t3,us.getSdef()+us.getUphurtper()*us.getSkillgrade());
		}		
		if(GameManager.getInstance().getCardskilldict().containsKey(uc.getCardcode())){
			//计算卡牌对应技能缘分
			Map<String,lotluck> smap=GameManager.getInstance().getCardskilldict().get(uc.getCardcode());
			if(smap.containsKey(us.getSkillcode())){
				Iterator<UserSkill> it = uc.getSkillMap().values().iterator();
				while(it.hasNext()){
					UserSkill usk=it.next();
					if(usk.getSkillcode().equals(us.getSkillcode())&&usk.getSkillid()!=us.getSkillid()){
						flag=1;
						break;
					}
				}
				if(flag<1){
					lotluck ll = smap.get(us.getSkillcode());
					uchp=uchp+calvalue(t1,ll.getAddhp());
					ucatk=ucatk+calvalue(t2,ll.getAddatk());
					ucdef=ucdef+calvalue(t3,ll.getAdddef());
				}				
			}			
		}		
		uc.setSkhp(uchp);
		uc.setSkdef(ucdef);
		uc.setSkatk(ucatk);
	}
	//卡牌更换
	public void AlterUserCard(int ocardid,int tcardid,User p,int flag) throws SQLException{
		UserCard ouc=null,tuc=null;
		int lostskillid=0,oriskid=0;
		int locflag=flag;
		tuc=p.getCardlist().get(tcardid);//换上的卡牌
		//换上的卡牌有天赋技能
		if(!tuc.getLifeskill().equals("@")&&tuc.getStarlevel()>5){
			if(tuc.getSkillMap().size()==0){//装备天赋技能
				Iterator<UserSkill> tarit = p.getUskilllist().values().iterator();
				while(tarit.hasNext()){
					UserSkill uk=tarit.next();
					if(uk.getSkillcode().equals(tuc.getLifeskill())){
						InstSkill(p, tuc, uk.getSkillid(),0);
						break;
					}
				}
			}
		}
		if(ocardid>0){ //从阵容中换下 原卡牌携带的装备技能转到目标卡牌上//换下的卡牌属性恢复为裸身值
			int valadd=0;
			ouc=p.getCardlist().get(ocardid);
			locflag=ouc.getLocate();
			if(locflag<0) return;
			if(ouc.getCurstyle()==2) valadd=200;
			if(ouc.getCurstyle()==3) valadd=300;
			ouc.setHp(ouc.getOrighp()+ouc.getLivehp()+valadd);//恢复卡牌为裸身值+培养值+形态加成
			ouc.setAtk(ouc.getOrigatk()+ouc.getLiveatk()+valadd);
			ouc.setDef(ouc.getOrigdef()+ouc.getLivedef()+valadd);
			//换下卡牌的全部装备都转到换上的卡牌
			Iterator<UserDevice> its = ouc.getDeviceMap().values().iterator();
			while(its.hasNext()){
				UserDevice udtemp=its.next();
				LoadDev(p,tuc,udtemp.getDeviceid());
			}
			ouc.getDeviceMap().clear();
			//换下卡牌的全部技能都转到换上的卡牌
			if(ouc.getLifeskill().equals("@")){//原卡无天赋技能，全部转移，如果换上卡有天赋，则主动技能不能转移自动写下
				Iterator<UserSkill> it = ouc.getSkillMap().values().iterator();
				while(it.hasNext()){
					UserSkill uktemp=it.next();
					if(tuc.getLifeskill().equals("@")){ //目标卡无天赋技能，技能转移安装
						InstSkill(p,tuc,uktemp.getSkillid(),uktemp.getSkillloc());
					}
					else{//目标卡有天赋技能，主动技能安装天赋技能，被动可转移
						if(uktemp.getSkillloc()>0) InstSkill(p,tuc,uktemp.getSkillid(),uktemp.getSkillloc());
						else lostskillid=uktemp.getSkillid();
					}
				}
				ouc.getSkillMap().clear();
			}else{//原卡有天赋技能，不能转移
				Iterator<UserSkill> it = ouc.getSkillMap().values().iterator();
				while(it.hasNext()){
					UserSkill uktemp=it.next();
					if(uktemp.getSkillloc()>0){
						InstSkill(p,tuc,uktemp.getSkillid(),uktemp.getSkillloc());
					}else oriskid=uktemp.getSkillid();
				}
				ouc.getSkillMap().clear();
				InstSkill(p, ouc, oriskid,0);//重新计算天赋技能buff			
			}
			ouc.setHavedevice(0);
			ouc.setHaveskill(0);
			ouc.setLocate(-1);//位置设空
			ouc.setIfuse(0);	//设为不用
			ouc.setSameaddhp(0);
			ouc.setSameaddatk(0);
			ouc.setSameadddef(0);
			p.getInusecards().remove(ocardid); //从阵容列表中移除此卡
		}
		//logger.info("卡位置："+locflag);
		tuc.setLocate(locflag);
		tuc.setSameaddhp(0);
		tuc.setSameaddatk(0);
		tuc.setSameadddef(0);
		tuc.setIfuse(1);		
		p.getCardlocid()[locflag]=tuc.getCardid(); //阵容位置
		p.getInusecards().put(tuc.getCardid(), tuc);//放入阵容列表
		this.checktype(p);
		GameServerDao dao = new GameServerDao();
		try{
			dao.AlterUserCard(ocardid, tcardid, p,lostskillid);
		}catch(SQLException e){
			throw e;
		}
	}
	//检查同系补正 缘buff
	public int checktype(User p){
		//int flag=1;
		//String tempstr1="";
		//int checkflag=0;
		int addall=0; //最终补正增加百分比 addall%
		//ArrayList<UserCard> ul = new ArrayList<UserCard>();
		int ahp=0,aatk=0,adef=0;
		Iterator<UserCard> it = p.getInusecards().values().iterator();
		while(it.hasNext()){
			ahp=0;
			aatk=0;
			adef=0;
			UserCard uc = it.next();
			if(GameManager.getInstance().getCardluckdict().containsKey(uc.getCardcode())){
				Iterator<lotluck> cardit = GameManager.getInstance().getCardluckdict().get(uc.getCardcode()).values().iterator();
				while(cardit.hasNext()){
					lotluck ll=cardit.next();
					String[] luckstr=ll.getSomename().split(",");
					int luckflag=0;
					String ccode="";
					for(int i=0;i<luckstr.length;i++){
						ccode=luckstr[i];
						for(int j=0;j<p.getCardlocid().length;j++){	
							if(p.getCardlocid()[j]>0){
								if(ccode.equals(p.getCardlist().get(p.getCardlocid()[j]).getCardcode())){
									luckflag=luckflag+1;
									break;
								}
							}							
						}
					}
					if(luckflag==luckstr.length){//满足buff条件
						if(ll.getAddhp()>0&&ll.getAddhp()<1) ahp=ahp+(int)(uc.getOrighp()*ll.getAddhp());
						else ahp=ahp+(int)(ll.getAddhp()*1);
						if(ll.getAddatk()>0&&ll.getAddatk()<1) aatk=aatk+(int)(uc.getOrigatk()*ll.getAddatk());
						else aatk=aatk+(int)(ll.getAddatk()*1);						
						if(ll.getAdddef()>0&&ll.getAdddef()<1) adef=adef+(int)(uc.getOrigdef()*ll.getAdddef());
						else adef=adef+(int)(ll.getAdddef()*1);
					}
				}
			}
			//集卡buff
			Iterator  its = p.getUsergetallmap().keySet().iterator();
			while(its.hasNext()){
				String ctype=(String) its.next();
				int getallflag=p.getUsergetallmap().get(ctype);
				if(getallflag>0){//集齐
					AllgetDict ad=GameManager.getInstance().getCardgetall().get(ctype);
					if(ad.getAddhp()>0&&ad.getAddhp()<1) ahp=ahp+(int)(uc.getOrighp()*ad.getAddhp());
					else ahp=ahp+(int)(ad.getAddhp()*1);
					if(ad.getAddatk()>0&&ad.getAddatk()<1) aatk=aatk+(int)(uc.getOrigatk()*ad.getAddatk());
					else aatk=aatk+(int)(ad.getAddatk()*1);
					if(ad.getAdddef()>0&&ad.getAdddef()<1) adef=adef+(int)(uc.getOrigdef()*ad.getAdddef());
					else adef=adef+(int)(ad.getAdddef()*1);
				}
			}
			//
			uc.setSameaddhp(ahp);
			uc.setSameaddatk(aatk);
			uc.setSameadddef(adef);
			//ul.add(uc);			
		}
		/*ArrayList<Integer> sameflaglist=new ArrayList<Integer>();
		for(int i=0;i<ul.size();i++){
			flag=1;
			sameflaglist.clear();
			sameflaglist.add(i);
			tempstr1=ul.get(i).getCardtype();
			checkflag=ul.get(i).getSameflag();
			if(checkflag==0){	
				//从下一位置查找相同类型的
				for(int j=i+1;j<ul.size();j++){
					if(tempstr1.equals(ul.get(j).getCardtype())){
						flag=flag+1;
						ul.get(j).setSameflag(1);
						sameflaglist.add(j);
					}
				}
				if(flag>1){
					ul.get(i).setSameflag(1);
					//当前系列相同数量
					switch(flag){
					case 2:
						addall=2;
						break;
					case 3:
						addall=4;
						break;
					case 4:
						addall=6;
						break;
					case 5:
						addall=10;
						break;
					case 6:
						addall=15;
						break;
					}
					for(int k=0;k<sameflaglist.size();k++){
						UserCard tempuc=ul.get(sameflaglist.get(k));
						int ahp=tempuc.getOrighp()*addall/100;
						int aatk=tempuc.getOrigatk()*addall/100;
						int adef=tempuc.getOrigdef()*addall/100;
						tempuc.setSameaddhp(ahp);
						tempuc.setSameaddatk(aatk);
						tempuc.setSameadddef(adef);
						tempuc.setAdd_hp(addall);//增加百分比
						tempuc.setAdd_atk(flag);//同系数
						//logger.info("不正百分比："+addall);
					}
				}
			}	
		}*/
		return addall;
	}
	//取下装备
	public void TakeDownDev(User p,UserCard uc,int did){
		//重新计算卡牌的血攻防 
		CalcDeviceAdd(p,uc,did);
		int uchp=0,ucatk=0,ucdef=0;
		UserDevice ud=uc.getDeviceMap().get(did);	
		uchp=uc.getDevhp();//减少值
		ucatk=uc.getDevatk();
		ucdef=uc.getDefdef();			
		uc.setHp(uc.getHp()-uchp);
		uc.setAtk(uc.getAtk()-ucatk);
		uc.setDef(uc.getDef()-ucdef);
		/*if(uc.getIfuse()>0){//卡牌在阵容中，引起用户总血攻防变化
			p.setUhp(p.getUhp()-uchp);
			p.setUatk(p.getUatk()-ucatk);
			p.setUdef(p.getUdef()-ucdef);
		}		*/		
		ud.setCardid(0);//装备对应卡牌id清空
		uc.getDeviceMap().remove(did);
		p.getUdevicelist().get(did).setCardid(0);
	}
	//穿上装备
	public int LoadDevice(User p,UserCard uc,int did,int ocid) throws SQLException{
		//重新计算卡牌的血攻防 
		int flag=0;
		UserDevice ud=p.getUdevicelist().get(did);
		UserCard otheruc=null;
		//如果此装备在其他卡牌上，引起其他卡牌卸装
		if(ud.getCardid()>0){
			if(ud.getCardid()!=uc.getCardid()){//非本卡装备
				otheruc=p.getCardlist().get(ud.getCardid());
				TakeDownDev(p,otheruc,did);
				if(ocid>0){//原装备卸下
					TakeDownDev(p,uc,ocid);
				}
				LoadDev(p,uc,did);
				flag=1;
			}	
		}else{
			if(ocid>0){//原装备卸下
				TakeDownDev(p,uc,ocid);
			}
			LoadDev(p,uc,did);
			flag=1;
		}
		if(flag>0){
			GameServerDao dao = new GameServerDao();
			try{
				dao.LoadDevice(p, uc, did,otheruc,ocid);
			}catch(SQLException e){
				throw e;
			}
			
		}
		return flag;
	}
	//镶嵌符文
	public void LoadMark(User p,UserDevice ud,String mcode) throws SQLException{
		int mid=0;
		UserMark um;
		ArrayList<Integer> destroymarkl=new ArrayList<Integer>();//要摧毁的符文列表
		ArrayList<UserMark> loadmarkl=new ArrayList<UserMark>();//要镶嵌的符文列表
		GameServerDao dao = new GameServerDao();
		String[] cstr=mcode.split(",");
		Iterator<UserMark> its =ud.getMarkMap().values().iterator();
		ArrayList<Integer> tal=new ArrayList<Integer>();
		while(its.hasNext()){
			um=its.next();
			tal.add(um.getMarkid());
		}
		for(int i=0;i<tal.size();i++){
			UserMark umtemp=ud.getMarkMap().get(tal.get(i));
			if(cstr[umtemp.getMarkloc()].equals(umtemp.getMarkcode())){//相同位置相同符文不摧毁
				cstr[umtemp.getMarkloc()]="@";
				continue;					
			}
			destroymarkl.add(tal.get(i));//记录要摧毁的符文
			DesMark(p,ud,tal.get(i));//摧毁符文
		}			
		for(int i=0;i<cstr.length;i++){
			mid=0;
			if(cstr[i].equals("@")) continue;
			Iterator<UserMark> itm = p.getUmarklist().values().iterator();
			while(itm.hasNext()){
				um=itm.next();
				if(um.getMarkcode().equals(cstr[i])&&(um.getDeviceid()<1||um.getMarkloc()<0)){
					mid=um.getMarkid();
					break;
				}
			}
			if(mid<1) throw new SQLException("nomark");
			else {
				UserMark ttt=p.getUmarklist().get(mid);
				InstMark(p,ud,mid,i);//镶嵌符文
				loadmarkl.add(ttt);
			}
		}
		try{
			dao.LoadMark(p, ud, loadmarkl, destroymarkl);
		}catch(SQLException e){
			throw e;
		}
	}
	//符文镶嵌变更
	public void InstMark(User p,UserDevice ud,int mid,int lid){
		UserMark um=p.getUmarklist().get(mid);
		double umhp=0,umatk=0,umdef=0;
		int uchp=0,ucatk=0,ucdef=0;
		String markstring="@";
		ud.setHavemark(1);           //设置是否有符文镶嵌
		um.setDeviceid(ud.getDeviceid());
		um.setMarkloc(lid);
		ud.getMarkMap().put(mid, um);
		Map<Integer,String> tempmap=null;
		tempmap=setmarkstring(ud);		
		if(ud.getCardid()>0){
			int cid=ud.getCardid();
			umhp=um.getHp();
			umatk=um.getAtk();
			umdef=um.getDef();
			UserCard uc=p.getCardlist().get(cid);
			int t1=uc.getOrighp();//卡牌裸值
			int t2=uc.getOrigatk();
			int t3=uc.getOrigdef();
			uchp=calvalue(t1,umhp);
			ucatk=calvalue(t2,umatk);
			ucdef=calvalue(t3,umdef);
			if(tempmap.size()==3){ //如果有符文组，计算增量
				markstring=tempmap.get(0).toString()+","+tempmap.get(1).toString()+","+tempmap.get(2).toString();  //构造符文组合串
				if(GameManager.getInstance().getMarksetdict().containsKey(markstring)){
					MarkStringCA msca=GameManager.getInstance().getMarksetdict().get(markstring);
					umhp=msca.getMksaddhp();
					umatk=msca.getMksaddatk();
					umdef=msca.getMksadddef();
					uchp=uchp+calvalue(t1,umhp);
					ucatk=ucatk+calvalue(t2,umatk);
					ucdef=ucdef+calvalue(t3,umdef);
				}else
					markstring="@";
			}			
			uc.setHp(uc.getHp()+uchp);
			uc.setAtk(uc.getAtk()+ucatk);
			uc.setDef(uc.getDef()+ucdef);
		}else{			
			if(tempmap.size()==3){ //如果有符文组，计算增量
				markstring=tempmap.get(0).toString()+","+tempmap.get(1).toString()+","+tempmap.get(2).toString();  //构造符文组合串
				if(!GameManager.getInstance().getMarksetdict().containsKey(markstring)){
					markstring="@";
				}
			}
		}
		ud.setMarkstring(markstring);//设置装备符文串
		
	}
	//符文组合串
	public Map<Integer,String> setmarkstring(UserDevice ud){
		Iterator<UserMark> it = ud.getMarkMap().values().iterator();
		Map<Integer,String> map = new HashMap<Integer,String>();
		while(it.hasNext()){
			UserMark umtemp = it.next();
			map.put(umtemp.getMarkloc(), umtemp.getMarkcode());
		}
		return map;		
	}
	//计算摧毁符文变化 不摧毁，取下
	public void DesMark(User p,UserDevice ud,int mid){
		double umhp=0,umatk=0,umdef=0;
		UserMark um=p.getUmarklist().get(mid);
		int uchp=0,ucatk=0,ucdef=0;
		String markstring=ud.getMarkstring();
		if(ud.getMarkMap().size()==1){ ud.setHavemark(0);} //最后一个符文，摧毁后装备是否有符文设为0
		if(ud.getCardid()>0){
			int cid=ud.getCardid();
			umhp=um.getHp();
			umatk=um.getAtk();
			umdef=um.getDef();
			UserCard uc=p.getCardlist().get(cid);
			int t1=uc.getOrighp();//卡牌裸值
			int t2=uc.getOrigatk();
			int t3=uc.getOrigdef();
			uchp=calvalue(t1,umhp);
			ucatk=calvalue(t2,umatk);
			ucdef=calvalue(t3,umdef);
			if(!ud.getMarkstring().equals("@")){ //如果有符文组，符文串破，计算减量		
				//logger.info("符文组串："+markstring);
				MarkStringCA msca=GameManager.getInstance().getMarksetdict().get(markstring);
				umhp=msca.getMksaddhp();
				umatk=msca.getMksaddatk();
				umdef=msca.getMksadddef();
				uchp=uchp+calvalue(t1,umhp);
				ucatk=ucatk+calvalue(t2,umatk);
				ucdef=ucdef+calvalue(t3,umdef);
				markstring="@";				
			}
			uc.setHp(uc.getHp()-uchp); //卡牌血攻防
			uc.setAtk(uc.getAtk()-ucatk);
			uc.setDef(uc.getDef()-ucdef);
		}else{
			if(!ud.getMarkstring().equals("@")){ //如果有符文组，符文串破
				markstring="@";		
			}
		}
		um.setCardid(-1);
		um.setDeviceid(-1);
		um.setMarkloc(-1);
		ud.getMarkMap().remove(mid);
		ud.setMarkstring(markstring);
		//p.getUmarklist().remove(mid);
	}
	//技能取下
	public void DumpSk(User p,UserCard uc,int sid){
		int flag=1;
		UserSkill us=p.getUskilllist().get(sid);
		us.setCardid(0);//取下技能后cardid为0
		uc.getSkillMap().remove(sid);
		us.setSkillloc(-1);
		CalcSkillAdd(p,uc,sid);		
		int uchp=0,ucatk=0,ucdef=0;
		uchp=uc.getSkhp();//减少值
		ucatk=uc.getSkatk();
		ucdef=uc.getSkdef();
		uc.setHp(uc.getHp()-uchp);
		uc.setAtk(uc.getAtk()-ucatk);
		uc.setDef(uc.getDef()-ucdef);
		if(uc.getSkillMap().size()==1){flag=0;} //如果只有1个技能了，取下时卡牌没有技能了
		uc.setHaveskill(flag);
		/*if(uc.getIfuse()>0){//卡牌在阵容中，引起用户总血攻防变化
			p.setUhp(p.getUhp()-uchp);
			p.setUatk(p.getUatk()-ucatk);
			p.setUdef(p.getUdef()-ucdef);
		}		*/		
		
	}
	//安装技能
	public int LoadSkill(User p,UserCard uc,int sid,int osid,int skloc) throws SQLException{
		//重新计算卡牌的血攻防 
		int flag=0;
		UserSkill us=p.getUskilllist().get(sid);//要换上的技能
		UserCard otheruc=null;
		//logger.info("aaaa:"+sid+"kkkk:"+osid+"技能位置："+skloc);
		if(us.getCardid()>0){//
			if(us.getCardid()!=uc.getCardid()){//要换上的技能在其他卡牌上
				otheruc=p.getCardlist().get(us.getCardid());
				if(otheruc.getLifeskill().equals(us.getSkillcode())){//天赋技能 不能卸下
					return -1;
				}else
					DumpSk(p,otheruc,sid);
				if(osid>0){//要换下的技能
					if(p.getUskilllist().get(osid).getSkillcode().equals(uc.getLifeskill())){
						return -1;
					}
					DumpSk(p,uc,osid);//卸下原技能
				}
				InstSkill(p,uc,sid,skloc);
				flag=1;
			}
		}else{//要换上的技能未安装到卡牌上
			if(osid>0){//要换下的技能
				if(p.getUskilllist().get(osid).getSkillcode().equals(uc.getLifeskill())){
					return -1;
				}
				DumpSk(p,uc,osid);//卸下原技能
				//logger.info("卸下技能位置："+p.getUskilllist().get(osid).getSkillloc());
			}
			InstSkill(p,uc,sid,skloc);
			flag=1;
		}
		
		if(flag>0){
			GameServerDao dao = new GameServerDao();
			try{
				dao.LoadSkill(p, uc, sid,otheruc,osid);
			}catch(SQLException e){
				throw e;
			}
		}
		return flag;
		
	}
	//新卡或转生计算卡牌裸身值
	public void CalcCardOrign(UserCard uc){
		double starhp=0,staratk=0,stardef=0,cval=0;
		int uplevexp=0;//升级经验
		//logger.info("当前魅力："+uc.getCurvalue());
		cval=GameManager.getInstance().getValuedict().get(uc.getCurvalue());
		//logger.info("魅力系数："+cval);
		starhp=uc.getBasehp();//基础hp
		staratk=uc.getBaseatk();//
		stardef=uc.getBasedef();
		//升级经验 与用户升级经验按比例计算
		int upexp=GameManager.getInstance().getUseruplevel().get(uc.getCardlevel()).getUpsum();
		uplevexp=upexp*GameManager.getInstance().getUseruplevel().get(uc.getCardlevel()).getCardpercent()/100;
		int ohp,oatk,odef;//运算裸身血攻防
		/*HP：星级*25+人物基础成长*10+等级*(星级*6+人物基础成长+8)*魅力成长系数
		ATK:星级*15+人物基础成长*4+等级*(星级*2+人物基础成长)*魅力成长系数
		DEF:星级*6.5+人物基础成长*2+等级*(星级*0.5+人物基础成长)*魅力成长系数*/
		ohp=uc.getStarlevel()*25+(int)(starhp*10)+(int)(uc.getCardlevel()*(uc.getStarlevel()*6+starhp+8)*cval);
		oatk=uc.getStarlevel()*15+(int)(staratk*4)+(int)(uc.getCardlevel()*(uc.getStarlevel()*2+staratk)*cval);
		odef=(int)(uc.getStarlevel()*6.5)+(int)(stardef*2)+(int)(uc.getCardlevel()*(uc.getStarlevel()*0.5+stardef)*cval);
		uc.setOrighp(ohp);
		uc.setOrigatk(oatk);
		uc.setOrigdef(odef);
		uc.setUpexp(uplevexp);
		int valadd=0;
		if(uc.getCurstyle()==2) valadd=100;
		if(uc.getCurstyle()==3) valadd=200;
		uc.setHp(ohp+uc.getLivehp()+valadd);
		uc.setAtk(oatk+uc.getLiveatk()+valadd);
		uc.setDef(odef+uc.getLivedef()+valadd);
		uc.setSameaddhp(0);
		uc.setSameaddatk(0);
		uc.setSameadddef(0);
	}
	//确定转生前运算预期
	public void preactlifeup(UserCard uc,String flag){
		uc.setBasehp(uc.getBasehp()+uc.getAlterhp());//转生后基础hp
		uc.setBaseatk(uc.getBaseatk()+uc.getAlteratk());//
		uc.setBasedef(uc.getBasedef()+uc.getAlterdef());
		uc.setStarlevel(uc.getStarlevel()+1);//星级加1
		if(flag.equals("0")){//普通转生 
			uc.setCardlevel(1);
		}
		CalcCardOrign(uc);
	}
	//转生
	public int actlifeup(User p,UserCard uc,String flag) throws SQLException{
		int extendval=0;//继承魅力值
		int extendexp=0;//继承经验值
		int costmoney=0,costball=0;
		if(uc.getStarlevel()>6) return -5;//最高到7星
		CLifeUpDict lfud=GameManager.getInstance().getLifeupdict().get(uc.getStarlevel());
		if(p.getMoney()<lfud.getMoney()) return -1;
		if(flag.equals("0")) {
			if(p.getBall()<lfud.getBall()) return -2;
		}
		else {
			if(p.getBall()<lfud.getPerball()) return -2;
		}		
		if(uc.getCardlevel()<lfud.getGrade()) return -3;
		if(uc.getCurvalue()<lfud.getCardval()) return -4;		
		uc.setBasehp(uc.getBasehp()+uc.getAlterhp());//转生后基础hp
		uc.setBaseatk(uc.getBaseatk()+uc.getAlteratk());//
		uc.setBasedef(uc.getBasedef()+uc.getAlterdef());
		uc.setStarlevel(uc.getStarlevel()+1);//星级加1
		if(flag.equals("0")){//普通转生 
			extendval=lfud.getExtval()*uc.getCurvalue()/100;
			extendexp=lfud.getExtexp()*uc.getCurexp()/100;
			p.setMoney(p.getMoney()-lfud.getMoney());
//			p.setGold(p.getGold()-lfud.getGold());
//			p.setGod(p.getGod()-lfud.getGod());
//			p.setInnercoin(p.getInnercoin()-lfud.getInnercoin());
			p.setBall(p.getBall()-lfud.getBall());
			costmoney=lfud.getMoney();
			costball=lfud.getBall();
			uc.setCardlevel(1);
		}else{
			extendval=lfud.getPextval()*uc.getCurvalue()/100;
			extendexp=lfud.getPextexp()*uc.getCurexp()/100;
			p.setMoney(p.getMoney()-lfud.getPermoney());
			p.setBall(p.getBall()-lfud.getPerball());
			costmoney=lfud.getPermoney();
			costball=lfud.getPerball();
		}		
		CalcCardOrign(uc);
		uc.setCurexp(extendexp);
		uc.setCurvalue(extendval);
		if(uc.getIfuse()>0&&uc.getLocate()>=0){//卡牌在阵容中 重新计算装备 技能带来的buff
			int uchp=0,ucatk=0,ucdef=0; //变化值
			Iterator<UserDevice> it = uc.getDeviceMap().values().iterator();
			while(it.hasNext()){
				UserDevice ud = it.next();
				CalcDeviceAdd(p,uc,ud.getDeviceid());
				uchp=uchp+uc.getDevhp();
				ucatk=ucatk+uc.getDevatk();
				ucdef=ucdef+uc.getDefdef();
			}
			Iterator<UserSkill> itk = uc.getSkillMap().values().iterator();
			while(itk.hasNext()){
				UserSkill uk = itk.next();
				CalcSkillAdd(p,uc,uk.getSkillid());
				uchp=uchp+uc.getSkhp();
				ucatk=ucatk+uc.getSkatk();
				ucdef=ucdef+uc.getSkdef();
			}
			uc.setHp(uc.getHp()+uchp);
			uc.setAtk(uc.getAtk()+ucatk);
			uc.setDef(uc.getDef()+ucdef);
			//根据原卡牌补正关系，计算新的值
			checktype(p);
		}		
		GameServerDao dao = new GameServerDao();
		try{
			dao.actlifeup(p, uc, flag,costmoney,costball);
		}catch(SQLException e){
			throw e;
		}		
		return 1;
	}
	public int CalcExp(UserCard uc,int extexp){
		int curexp=0,tempexp=0;
		int flag=0;
		curexp=uc.getCurexp();
		tempexp=extexp;
		tempexp=tempexp-uc.getUpexp()+curexp;
		if(tempexp>=0&&uc.getCardlevel()<99){
			flag=flag+1;//升1级
			uc.setCardlevel(uc.getCardlevel()+1);
			int upexp=GameManager.getInstance().getUseruplevel().get(uc.getCardlevel()).getUpsum();
			int calcurexp=upexp*GameManager.getInstance().getUseruplevel().get(uc.getCardlevel()).getCardpercent()/100;
			uc.setUpexp(calcurexp);//升级后新等级的升级经验
			uc.setCurexp(tempexp);
			if(tempexp>=uc.getUpexp()){//继续升级
				flag=flag+CalcExp(uc,0);
			}
		}else{
			uc.setCurexp(extexp+curexp);
		}
		return flag;
	}
	//融魂 要返回转移经验 魅力值
	public void mergecard(User p,UserCard uc,UserCard tuc) throws SQLException{
		int exp=GameManager.getInstance().getUseruplevel().get(tuc.getCardlevel()).getUpcount()+tuc.getCurexp(); //散魂卡牌总经验
		int val=tuc.getCurvalue(); //魅力
		int vhp=tuc.getLivehp();//培养值
		int upflag=0; //升级数
		int transval=0;//魅力点数
		int vatk=tuc.getLiveatk();
		int vdef=tuc.getLivedef();
		int extexp=0,extval=0;
		MergeCardDict mc=GameManager.getInstance().getMergecarddict().get(tuc.getStarlevel());//融魂字典
			//VIP 1-3继承60%  4-5 80% 6-7 90% 8+ 100%
		switch(p.getViplevel()){
		case 0://不是vip
			extexp=exp*mc.getExtexp()/100; //继承经验
			transval=mc.getBreakcharm()*(1+val)+tuc.getCurpointval();			
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		case 1://Vip1
			extexp=exp*60/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip1charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		case 2://Vip2
			extexp=exp*60/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip2charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		case 3://Vip3
			extexp=exp*60/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip3charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		case 4://Vip4
			extexp=exp*80/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip4charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		case 5://Vip5
			extexp=exp*80/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip5charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		case 6://Vip6
			extexp=exp*90/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip6charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		case 7://Vip7
			extexp=exp*90/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip7charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		case 8://Vip8
		case 9://Vip9
		case 10://Vip10
		case 11://Vip11
		case 12://Vip12
			extexp=exp; //继承经验100%
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip8charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //计算获得魅力点数
			break;
		}		
		//卡牌当前经验值，魅力值计算继承经验后卡牌升级数
		upflag=CalcExp(uc,extexp);
		if(uc.getCurvalue()>99) uc.setCurvalue(99);
		//魅力变化判断是否开启新形态
		if(uc.getCurvalue()>=30&&uc.getCurvalue()<60){
			uc.setCurstyle(2);//开启二形态
			uc.setAltercurstyle(2);
		}
		if(uc.getCurvalue()>=60)  {
			uc.setCurstyle(3);//开启三形态
			uc.setAltercurstyle(3);
		}			
		if(upflag>0 || extval>0){//魅力值或登记变化计算卡牌裸身值
			CalcCardOrign(uc);
			if(uc.getIfuse()>0&&uc.getLocate()>=0){//卡牌在阵容中 重新计算装备 技能带来的buff
				int uchp=0,ucatk=0,ucdef=0; //变化值
				Iterator<UserDevice> it = uc.getDeviceMap().values().iterator();
				while(it.hasNext()){
					UserDevice ud = it.next();
					CalcDeviceAdd(p,uc,ud.getDeviceid());
					uchp=uchp+uc.getDevhp();
					ucatk=ucatk+uc.getDevatk();
					ucdef=ucdef+uc.getDefdef();
				}
				Iterator<UserSkill> itk = uc.getSkillMap().values().iterator();
				while(itk.hasNext()){
					UserSkill uk = itk.next();
					CalcSkillAdd(p,uc,uk.getSkillid());
					uchp=uchp+uc.getSkhp();
					ucatk=ucatk+uc.getSkatk();
					ucdef=ucdef+uc.getSkdef();
				}
				uc.setHp(uc.getHp()+uchp);
				uc.setAtk(uc.getAtk()+ucatk);
				uc.setDef(uc.getDef()+ucdef);
				//根据卡牌补正关系，计算新的值
				checktype(p);
			}		
		}
		//散魂卡牌清除
		int tid=tuc.getCardid();
		p.getCardlist().remove(tid);
		GameServerDao dao = new GameServerDao();
		try{
			dao.mergecard(p, uc,tid);
		}catch(SQLException e){
			throw e;
		}
	}
	//精炼
	public int UpgradeDevice(User p,UserDevice ud,int fid) throws SQLException{
		DeviceUpgrade du = GameManager.getInstance().getDeviceaddval().get(ud.getDevicetype()).get(ud.getQuality()).get(ud.getGrade()+1);
		Random rd = new Random();
		int adhp,adatk,addef;
		int sucflag=0;//成功标志
		int k=rd.nextInt(10000);
		int r=0;
		int flag=0,gnum=0,mnum=0;
		if(fid>0){//强化
			if(p.getGold()<du.getUpgold()) return -1;
			sucflag=5;
			flag=du.getUpsucval();
			if(10000-flag>=flag){//爆机强化
				if (k>=(10000-flag)){
					r=rd.nextInt(4);
					sucflag=r+6;
				}
			}else{
				if (k<=flag){
					r=rd.nextInt(4);
					sucflag=r+6;
				}
			}
			gnum=du.getUpgold();
			p.setGold(p.getGold()-du.getUpgold());
		}else{
			if(p.getMoney()<du.getMoneyvalue()) return -1;
			sucflag=1;
			flag=du.getUpsucval();
			if(10000-flag>=flag){//爆机强化
				if (k>=(10000-flag)){
					r=rd.nextInt(2);
					sucflag=r+2;
				}
			}else{
				if (k<=flag){
					r=rd.nextInt(2);
					sucflag=r+2;
				}
			}
			mnum=du.getMoneyvalue();
			p.setMoney(p.getMoney()-du.getMoneyvalue());
		}
		if(sucflag>0){//精炼成功
			if(ud.getGrade()+sucflag>297) sucflag=297-ud.getGrade();
			for(int i=1;i<=sucflag;i++){
				du=GameManager.getInstance().getDeviceaddval().get(ud.getDevicetype()).get(ud.getQuality()).get(ud.getGrade()+i);
				adhp=du.getAddhp();
				adatk=du.getAddatk();
				addef=du.getAdddef();
				//如果此装备在卡牌上，计算增量
				int cid=0;
				cid=ud.getCardid();
				if(cid>0){
					UserCard uc=p.getCardlist().get(cid);
					uc.setHp(uc.getHp()+adhp);
					uc.setAtk(uc.getAtk()+adatk);
					uc.setDef(uc.getDef()+addef);
					/*if(uc.getIfuse()>0){//卡牌在阵容中，引起用户总血攻防变化
						p.setUhp(p.getUhp()+adhp);
						p.setUatk(p.getUatk()+adatk);
						p.setUdef(p.getUdef()+addef);
					}*/
				}
				ud.setDhp(ud.getDhp()+du.getAddhp());
				ud.setDatk(ud.getDatk()+du.getAddatk());
				ud.setDdef(ud.getDdef()+du.getAdddef());
				du=GameManager.getInstance().getDeviceaddval().get(ud.getDevicetype()).get(ud.getQuality()).get(ud.getGrade()+i);
				ud.setPrice(ud.getPrice()+du.getAddprice());
			}
			ud.setGrade(ud.getGrade()+sucflag);
		}	
		GameServerDao dao = new GameServerDao();
		try{
			dao.UpgradeDevice(p,ud,fid,sucflag,gnum,mnum);
		}catch(SQLException e){
			throw e;
		}
		return sucflag;
	}
	//技能修炼
	public void produceskill(User p,SkillPages sp) throws SQLException{
		GameServerDao dao = new GameServerDao();
		try{
			int skid=dao.SkillSuccess(sp.getSkillcode(),p);
			sp.setPage1(sp.getPage1()-1); //技能数量<0不考虑，=0不够
			sp.setPage2(sp.getPage2()-1);
			sp.setPage3(sp.getPage3()-1);
			sp.setPage4(sp.getPage4()-1);
			sp.setPage5(sp.getPage5()-1);							
			//获取新技能
			dao.GetUserSkill(p,skid);
		}catch(SQLException e){
			throw e;
		}
	}
	//技能合并
	public void unionskill(User p,UserSkill sk,UserSkill tsk) throws SQLException{
		if(sk.getCardid()>0){//此技能已装备
			UserCard uc=p.getCardlist().get(sk.getCardid());
			int skloc=sk.getSkillloc();
			DumpSk(p, uc, sk.getSkillid());//先卸掉原技能
			sk.setSkillgrade(sk.getSkillgrade()+tsk.getSkillgrade());//技能合并
			if(sk.getSkillgrade()>50) sk.setSkillgrade(50);
			InstSkill(p, uc, sk.getSkillid(),skloc);//重新装备技能
		}
		int tsid=tsk.getSkillid();
		GameServerDao dao = new GameServerDao();
		try{
			dao.unionskill(p,sk,tsid);
		}catch(SQLException e){
			throw e;
		}		
		p.getUskilllist().remove(tsk.getSkillid());
	}
	
	public Map<String,Object> pscard(User p,int cardid,int pstype,int bao,int times,int needmoney,Map<String,Object> sendMap){
		sendMap.put("CMD", PacketCommandType.RETURNACTPS);
		Random rd = new Random();
		int currRand = rd.nextInt(100);
		int point = 0;
		int expaward=1;//未暴击
		if(pstype == 0){
			if(currRand >= 0 && currRand <bao){//爆机
				point = 3 * CommTools.randomval(5, 15);
				expaward=0;
				sendMap.put("AP", 1);
			}else{//未爆机
				point = CommTools.randomval(5, 15);
				sendMap.put("AP", 0);
			}
		}else{
			if(currRand >= 0 && currRand <bao){//爆机
				point = 3 * CommTools.randomval(10+times*2, 20+times*4);
				expaward=0;
				sendMap.put("AP", 1);
			}else{//未爆机
				point = CommTools.randomval(10+times*2, 20+times*4);
				sendMap.put("AP", 0);
			}
		}
		GameServerDao dao = new GameServerDao();
		UserCard uc=p.getCardlist().get(cardid);
		sendMap.put("BA", uc.getCardid());
		sendMap.put("BB", uc.getCardcode());
		sendMap.put("AB", point);
		sendMap.put("AD", uc.getCurvalue());
		int currvalue = 0;
		currvalue = uc.CheckCardVal(point);
		sendMap.put("AC", uc.getCurvalue());
		sendMap.put("AM", uc.getCurpointval());
		sendMap.put("AG", (uc.getCurvalue()+1)*10);
		if(uc.getCurvalue()>99) uc.setCurvalue(99);
		//魅力变化判断是否开启新形态
		if(uc.getCurvalue()>=30&&uc.getCurvalue()<60){
			uc.setCurstyle(2);//开启二形态
			uc.setAltercurstyle(2);
		}
		if(uc.getCurvalue()>=60)  {
			uc.setCurstyle(3);//开启三形态
			uc.setAltercurstyle(3);
		}	
		sendMap.put("AL", uc.getCurstyle());
		if(currvalue>0){//魅力值或登记变化计算卡牌裸身值
			CalcCardOrign(uc);
			if(uc.getIfuse()>0&&uc.getLocate()>=0){//卡牌在阵容中 重新计算装备 技能带来的buff
				int uchp=0,ucatk=0,ucdef=0; //变化值
				Iterator<UserDevice> it = uc.getDeviceMap().values().iterator();
				while(it.hasNext()){
					UserDevice ud = it.next();
					CalcDeviceAdd(p,uc,ud.getDeviceid());
					uchp=uchp+uc.getDevhp();
					ucatk=ucatk+uc.getDevatk();
					ucdef=ucdef+uc.getDefdef();
				}
				Iterator<UserSkill> itk = uc.getSkillMap().values().iterator();
				while(itk.hasNext()){
					UserSkill uk = itk.next();
					CalcSkillAdd(p,uc,uk.getSkillid());
					uchp=uchp+uc.getSkhp();
					ucatk=ucatk+uc.getSkatk();
					ucdef=ucdef+uc.getSkdef();
				}
				uc.setHp(uc.getHp()+uchp);
				uc.setAtk(uc.getAtk()+ucatk);
				uc.setDef(uc.getDef()+ucdef);
				//根据卡牌补正关系，计算新的值
				checktype(p);
			}		
		}
		try{
			if(pstype == 0)
				dao.actpscard(p,cardid,pstype,1000,uc,expaward);
			else
				dao.actpscard(p,cardid,pstype,times*20,uc,expaward);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
			logger.info(e.getMessage());
		}
		return sendMap;
	}
}
