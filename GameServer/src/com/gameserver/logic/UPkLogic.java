package com.gameserver.logic;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.gameserver.comm.CommTools;
import com.gameserver.dao.GameServerDao;
import com.gameserver.manager.GameManager;
import com.gameserver.model.CMissionNpcDict;
import com.gameserver.model.Pkexpmoney;
import com.gameserver.model.SkillPages;
import com.gameserver.model.User;
import com.gameserver.model.UserCard;
import com.gameserver.model.UserMission;
import com.gameserver.model.UserSkill;
import com.gameserver.model.downDict;

public class UPkLogic implements Serializable{

	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(UPkLogic.class.getName());
	private static final long serialVersionUID = -4790323913687172826L;
	private ArrayList<String> actskill;
	public UPkLogic(){
		this.actskill=new ArrayList<String>();
	}
	public ArrayList<String> getActskill() {
		return actskill;
	}
	public void setActskill(ArrayList<String> actskill) {
		this.actskill = actskill;
	}

	//进入夺技战斗
	public void actpk(User p,String rname,String skcode,int pageno,int enlevel,Map<String,Object> smap) throws SQLException{
		//获取要夺技玩家的阵容列表
		ArrayList<UserCard> enul=new ArrayList<UserCard>();
		GetEnCardList(rname,p.getSvrcode(),enul);
		ArrayList<UserCard> ul=new ArrayList<UserCard>();
		for(int i=0;i<6;i++){ //战斗阵容
			if(p.getCardlocid()[i]<=0) break;
			UserCard uu=p.getCardlist().get(p.getCardlocid()[i]);
			uu.setCurhp(uu.getHp()+uu.getSameaddhp());
			uu.setCuratk(uu.getAtk()+uu.getSameaddatk());
			uu.setCurdef(uu.getDef()+uu.getSameadddef());
			ul.add(uu);			
		}
		int winloseflag=-1;
		try{
			winloseflag=StartBattle(ul,enul,smap);
			smap.put("AA", -1);
			smap.put("AB", winloseflag);
			smap.put("AC", "@");
			
			if(winloseflag==0){
				smap.put("AD", 0);//经验=等级*10-20 钱=（等级*10-20）*10	不扣=钱/1.5
				smap.put("AR",0);
			}else
			{
				Pkexpmoney pkem=GameManager.getInstance().getPkexpmon().get(p.getLevel());
				smap.put("AD", pkem.getExp());
				smap.put("AR", pkem.getMoney());
			}
			smap.put("AE", -1);
			smap.put("AF", "@");
			smap.put("AG", "@");
			FinGetSkill(winloseflag,p,enlevel,skcode,pageno,rname,smap);
			smap.put("AS",p.getLevel());
			smap.put("AV", p.getExp());//用户当前经验
			smap.put("AU", p.getLevelup());//用户升级经验
			smap.put("AW", p.getMembers());
			smap.put("AX", rname);
			smap.put("AZ", p.getGetbook());
			smap.put("AJ", p.getBall());
			GetMemberMap(ul,enul,smap);//双方阵容
			ArrayList<String> sklist = new ArrayList<String>();
			for(int i=0;i<this.actskill.size();i++){
				int kkk=0;
				for(int j=0;j<sklist.size();j++){
					if(this.actskill.get(i).equals(sklist.get(j)))
						kkk=1;
				}
				if(kkk==0)	sklist.add(this.actskill.get(i));
			}
			smap.put("AK",sklist.toArray());
		}catch(SQLException e){
			throw e;
		}
	}
	public void GetEnCardList(String rolename,String svrcode,ArrayList<UserCard> enl) throws SQLException{
		try{
			GameServerDao dao=new GameServerDao();
			//获取玩家阵容卡牌及卡牌上技能
			dao.GetUserCard(rolename,svrcode, enl);
		}catch(SQLException e){
			throw e;
		}
		
	}
	public void GetMemberMap(ArrayList<UserCard> ul,ArrayList<UserCard> enul,Map<String,Object> Map){
		ArrayList<Map<String,Object>> cardmember = new ArrayList<Map<String,Object>>();
		ArrayList<Map<String,Object>> encardmember = new ArrayList<Map<String,Object>>();
		String skname,skdesc;
		for(int i=0;i<ul.size();i++){
			skname="@";
			skdesc="@";
			Map<String,Object> tempmap = new HashMap<String,Object>();
			tempmap.put("DA", ul.get(i).getCardid());
			tempmap.put("DB", ul.get(i).getLocate());
			tempmap.put("DC", ul.get(i).getCardlevel());
			ArrayList<Map<String,Object>> skmap=new ArrayList<Map<String,Object>>();
			Iterator<UserSkill> it = ul.get(i).getSkillMap().values().iterator();
			while(it.hasNext()){
				UserSkill sk=it.next();
				if(sk.getSkillto()<0){//被动技能
					Map<String,Object> skillmap = new HashMap<String,Object>();
					skname=sk.getSkillcode();
					if(sk.getShp()>0) skdesc="HP";
					if(sk.getSatk()>0) skdesc="ATK";
					if(sk.getSdef()>0) skdesc="DEF";
					skillmap.put("DE", skname);
					skillmap.put("DF", skdesc);
					this.actskill.add(skname);
					skmap.add(skillmap);
				}
			}
			tempmap.put("DD", skmap.toArray());
			cardmember.add(tempmap);
		}
		Map.put("AL", cardmember.toArray());
		for(int i=0;i<enul.size();i++){
			skname="@";
			skdesc="@";
			Map<String,Object> tempmap = new HashMap<String,Object>();
			tempmap.put("EA", enul.get(i).getCardid());
			tempmap.put("EB", enul.get(i).getLocate());
			tempmap.put("EC", enul.get(i).getCardcode());
			tempmap.put("ER", enul.get(i).getStarlevel());
			tempmap.put("EI", enul.get(i).getCurstyle());
			ArrayList<Map<String,Object>> skmap=new ArrayList<Map<String,Object>>();
			Iterator<UserSkill> it =enul.get(i).getSkillMap().values().iterator();
			while(it.hasNext()){
				UserSkill sk=it.next();
				if(sk.getSkillto()<0){//被动技能
					Map<String,Object> skillmap = new HashMap<String,Object>();
					skname=sk.getSkillcode();
					if(sk.getShp()>0) skdesc="HP";
					if(sk.getSatk()>0) skdesc="ATK";
					if(sk.getSdef()>0) skdesc="DEF";
					skillmap.put("EE", skname);
					skillmap.put("EF", skdesc);
					this.actskill.add(skname);
					skmap.add(skillmap);
				}
			}
			tempmap.put("ED", skmap.toArray());
			encardmember.add(tempmap);
		}
		Map.put("AM", encardmember.toArray());
	}

	//开始互攻
	public int StartBattle(ArrayList<UserCard> ul,ArrayList<UserCard> enul,Map<String,Object> mapgame){
		int sumflag=0;
		int checkflagp=1,checkflagn=1;
		int winloseflag=-1;
		ArrayList<Map<String,Object>> misslist=new ArrayList<Map<String,Object>>();
		
		int[] curpoint={0,0};//玩家总血，对方总血
		for(int i=0;i<ul.size();i++){
			curpoint[0]=curpoint[0]+ul.get(i).getCurhp();		
		}
		for(int i=0;i<enul.size();i++){
			curpoint[1]=curpoint[1]+enul.get(i).getCurhp();		
		}
		mapgame.put("HA", curpoint[0]);//玩家总血
		mapgame.put("HB", curpoint[1]);//对方总血
		
		System.out.println("HA="+curpoint[0]);
		System.out.println("HB="+curpoint[1]);
		int totalha=curpoint[0];
		int totalhb=curpoint[1];
		
		for(int k=0;k<20;k++){//最多20回合		
			//logger.info("回合数：##########################################"+k);
			for(int i=0;i<6;i++){
				if(i<ul.size()){//玩家攻
					if(ul.get(i).getCurhp()>0){
						Map<String,Object> newmap = new HashMap<String,Object>();
						sumflag=sumflag+1;
						newmap.put("CA", sumflag);
						newmap.put("CB", "P");
						newmap.put("CC", ul.get(i).getCardid());
						newmap.put("CD", i);
						checkflagp=PlayerRun(ul,enul,i,newmap,"N");
						//logger.info("玩家攻::::::::::::"+i+"+++攻击结果值："+checkflagp);
						misslist.add(newmap);
						
						
						Object [] mmap=(Object[])(newmap.get("CH"));
						for(int j=0;j<mmap.length;j++){
							totalhb-=Integer.parseInt(((Map)mmap[j]).get("KRH").toString());
							((Map)mmap[j]).put("KJ", totalhb+"");
							
							
							System.out.println("totalhb");
							Set<String> key = ((Map)mmap[j]).keySet();
							for (Iterator<String> it = key.iterator(); it.hasNext();) {
								String s = it.next();
								System.out.print(s+":"+((Map)mmap[j]).get(s)+",");//这里的s就是map中的key，map.get(s)就是key对应的value。
							}
							System.out.println("");
						}
						System.out.println("");
						
					}	
				}
				if(checkflagp<1){//玩家胜
					winloseflag=1;
					//logger.info("玩家胜");
					break;
				}
				if(i<enul.size()){//对家攻
					if(enul.get(i).getCurhp()>0){
						Map<String,Object> newmap = new HashMap<String,Object>();
						sumflag=sumflag+1;
						newmap.put("CA", sumflag);
						newmap.put("CB", "N");
						newmap.put("CC", enul.get(i).getCardid());
						newmap.put("CD", i);
						checkflagn=PlayerRun(enul,ul,i,newmap,"P");
						//logger.info("好友攻::::::::::::"+i+"+++攻击结果值："+checkflagn);
						misslist.add(newmap);
						
						Object [] mmap=(Object[])(newmap.get("CH"));
						for(int j=0;j<mmap.length;j++){
							totalha-=Integer.parseInt(((Map)mmap[j]).get("KRH").toString());
							((Map)mmap[j]).put("KJ", totalha+"");
							Set<String> key = ((Map)mmap[j]).keySet();
							
							System.out.println("totalha");
							for (Iterator<String> it = key.iterator(); it.hasNext();) {
								String s = it.next();
								System.out.print(s+":"+((Map)mmap[j]).get(s)+",");//这里的s就是map中的key，map.get(s)就是key对应的value。
							}
							System.out.println("");
						}
						System.out.println("");
					}						
				}				
				if(checkflagn<1){//对方胜
					winloseflag=0;
					//logger.info("玩家bai");
					break;
				}
			}			
			if(winloseflag>=0){
				break;
			}else if(k==19){
				winloseflag=0;
			}			
		}
		mapgame.put("AN", misslist.toArray());
		return winloseflag;
	}
	//攻击
	public int PlayerRun(ArrayList<UserCard> ul,ArrayList<UserCard> enul,int locflag,Map<String,Object> tmap,String targ){
		UserCard ucb,encard;
		int hurt=0;
		int allhurt=-1;
		int skillrd=0;
		int skillgrade=0;
		String skillcode="";
		ucb=ul.get(locflag);
		/*Iterator<UserSkill> it = ucb.getSkillMap().values().iterator();
		while(it.hasNext()){
			UserSkill sk=it.next();
			if(sk.getSkillto()>=0){//主动技能
				allhurt=sk.getSkillto();//技能发动方式
				skillrd=sk.getSkillrand();//发动概率
				skillcode=sk.getSkillcode();
				skillgrade=sk.getSkillgrade();
				break;
			}
		}*/
		int locate=-1;
		int actflag=0;
		skillrd=CheckRandom(ucb);
		ArrayList<Map<String,Object>> mlist=new ArrayList<Map<String,Object>>();
		if(skillrd>0){//有技能 0群攻 1 对位攻，2行，3列
			UserSkill sk=ucb.getSkillMap().get(skillrd);
			allhurt=sk.getSkillto();//技能发动方式
			skillcode=sk.getSkillcode();
			skillgrade=sk.getSkillgrade();
			tmap.put("CE", 1);
			tmap.put("CG",skillcode);
			this.actskill.add(skillcode);
			switch(allhurt){
			case 0://群攻
				actflag=0;
				actflag=this.PlayerAll(ucb, enul, skillcode, skillgrade, mlist, locflag,targ);
				break;
			case 1:
				actflag=0;
				actflag=PlayerSingle(ul, enul, skillcode, skillgrade, mlist, locflag,targ);	
				break;
			case 2://按排攻击，对方所在排
				actflag=0;
				if(locflag<3){//先打第一排
					actflag=this.ActLineFront(ucb, enul, skillcode, skillgrade, mlist,targ);
					if(actflag<0){//打第二排
						actflag=this.ActLineBack(ucb, enul, skillcode, skillgrade, mlist,targ);
					}
					if(actflag<=0) actflag=0;
				}else{//先打第二排
					actflag=this.ActLineBack(ucb, enul, skillcode, skillgrade, mlist,targ);
					if(actflag<0){//打第1排
						actflag=this.ActLineFront(ucb, enul, skillcode, skillgrade, mlist,targ);
					}
					if(actflag<=0) actflag=0;
				}
				break;
			case 3://按列攻击 对方所在列
				actflag=0;
				actflag=ActCol(ucb, enul, skillcode, skillgrade, mlist, locflag,targ);				
				break;
			}	
			tmap.put("CH", mlist.toArray());
		}else {//正常攻击
			tmap.put("CE", 0);
			locate=CheckNext(ul,enul,locflag);//
			Map<String,Object> mmm=new HashMap<String,Object>();
			if(locate>=0){
				actflag=1;
				encard=enul.get(locate);
				hurt=ucb.getCuratk()-encard.getCurdef();
				if(hurt<=0) hurt=1;	
				if(CheckControl(ucb.getCardprop(),encard.getCardprop())>0){//本位有克制，伤害增加0.05倍
					hurt=(int)(hurt*1.05);
				}
				if(encard.getCurhp()-hurt<0) {
					mmm.put("KRH", encard.getCurhp());
					encard.setCurhp(0);
					mmm.put("KE", 0);
				}
				else{
					mmm.put("KRH", hurt);
					encard.setCurhp(encard.getCurhp()-hurt);
					mmm.put("KE", encard.getCurhp());
				}
				mmm.put("KA", targ);
				mmm.put("KB", encard.getCardid());
				mmm.put("KC",encard.getLocate());
				mmm.put("KD", hurt);
				mmm.put("KF", encard.getHp()+encard.getSameaddhp());
				mmm.put("KG", encard.getCardcode());
				mmm.put("KH", encard.getStarlevel());
				mmm.put("KI", encard.getCurstyle());
				mlist.add(mmm);		
				actflag=CheckUserWinLose(enul);
				//logger.info("玩家正常攻击"+ucb.getCardid()+"--->"+encard.getCardid()+"伤害："+hurt+"位置："+locate);
			}
			tmap.put("CH", mlist.toArray());
		}
		return actflag;
		
	}
	//玩家群攻技能
	public int PlayerAll(UserCard ucb,ArrayList<UserCard> enul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag,String targ){
		int actflag=0;
		int atkval=0,hurt=0;
		UserCard encard;
		for(int i=0;i<enul.size();i++){
			encard=enul.get(i);
			Map<String,Object> mmm=new HashMap<String,Object>();
			if(encard.getCurhp()>0){
				actflag=1;
				atkval=CheckSkillHurt(ucb.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
				hurt=atkval-encard.getCurdef();
				if(hurt<=0) hurt=1;
				if(CheckControl(ucb.getCardprop(),encard.getCardprop())>0){//本位有克制，伤害增加0.05倍
					hurt=(int)(hurt*1.05);
				}
				
				if(encard.getCurhp()-hurt<=0) {
					mmm.put("KRH", encard.getCurhp());
					encard.setCurhp(0);
					mmm.put("KE", 0);
				}else {
					mmm.put("KRH", hurt);
					encard.setCurhp(encard.getCurhp()-hurt);
					mmm.put("KE", encard.getCurhp());
				}
				mmm.put("KA", targ);
				mmm.put("KB", encard.getCardid());
				mmm.put("KC", encard.getLocate());
				mmm.put("KD", hurt);
				mmm.put("KF", encard.getHp()+encard.getSameaddhp());
				mmm.put("KG", encard.getCardcode());
				mmm.put("KH", encard.getStarlevel());
				mmm.put("KI", encard.getCurstyle());
				mlist.add(mmm);		
				actflag=CheckUserWinLose(enul);
				if(actflag<1) break;
			}
		}
		return actflag;
	}
	//玩家单攻技能
	public int PlayerSingle(ArrayList<UserCard> ul,ArrayList<UserCard> enul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag,String targ){
		int actflag=0;
		int locate=0,atkval=0,hurt=0;
		Map<String,Object> tm=new HashMap<String,Object>();
		locate=CheckNext(ul,enul,locflag);//查找可攻击位置 从对位开始
		if(locate>=0){
			UserCard encard=enul.get(locate);
			actflag=1;
			atkval=CheckSkillHurt(ul.get(locflag).getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
			hurt=atkval-encard.getCurdef();
			if(hurt<=0) hurt=1;	
			if(CheckControl(ul.get(locflag).getCardprop(),encard.getCardprop())>0){//本位有克制，伤害增加0.05倍
				hurt=(int)(hurt*1.05);
			}
			
			if(encard.getCurhp()-hurt<=0) {
				 tm.put("KRH",encard.getCurhp());
				encard.setCurhp(0);
				tm.put("KE", 0);
			}else{
				tm.put("KRH",hurt);
				encard.setCurhp(encard.getCurhp()-hurt);
				tm.put("KE",encard.getCurhp());
			}
			tm.put("KA", targ);
			tm.put("KB", encard.getCardid());
			tm.put("KC", encard.getLocate());
			tm.put("KD", hurt);
			tm.put("KF", encard.getHp()+encard.getSameaddhp());
			tm.put("KG", encard.getCardcode());
			tm.put("KH", encard.getStarlevel());
			tm.put("KI", encard.getCurstyle());
			mlist.add(tm);	
			actflag=CheckUserWinLose(enul);
		}
		return actflag;
	}
	//玩家前排攻击
	public int  ActLineFront(UserCard ucb,ArrayList<UserCard> enul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,String targ){
		UserCard encard;
		int hurt=0,atkval=0,actflag=-1;
		for(int i=0;i<3;i++){
			Map<String,Object> tm1=new HashMap<String,Object>();
			if(i<enul.size()){
				encard=enul.get(i);
				if(encard.getCurhp()>0){
					actflag=1;
					atkval=CheckSkillHurt(ucb.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
					hurt=atkval-encard.getCurdef();
					if(hurt<=0) hurt=1;
					if(CheckControl(ucb.getCardprop(),encard.getCardprop())>0){//本位有克制，伤害增加0.05倍
						hurt=(int)(hurt*1.05);
					}
					
					if(encard.getCurhp()-hurt<=0) {
						tm1.put("KRH", encard.getCurhp());
						encard.setCurhp(0);
						tm1.put("KE", 0);
					}else{
						tm1.put("KRH", hurt);
						encard.setCurhp(encard.getCurhp()-hurt);
						tm1.put("KE", encard.getCurhp());
					} 
					tm1.put("KA", targ);
					tm1.put("KB", encard.getCardid());
					tm1.put("KC", encard.getLocate());
					tm1.put("KD", hurt);
					tm1.put("KF", encard.getHp()+encard.getSameaddhp());
					tm1.put("KG", encard.getCardcode());
					tm1.put("KH", encard.getStarlevel());
					tm1.put("KI", encard.getCurstyle());
					mlist.add(tm1);	
					actflag=CheckUserWinLose(enul);
					if(actflag<1) break;
				}
			}else break;
		}
		return actflag;
	}
	//玩家后排攻击
	public int ActLineBack(UserCard ucb,ArrayList<UserCard> enul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,String targ){
		UserCard encard;
		int hurt=0,atkval=0,actflag=-1;
		for(int i=3;i<6;i++){
			Map<String,Object> tm1=new HashMap<String,Object>();
			if(i<enul.size()){
				encard=enul.get(i);
				if(encard.getCurhp()>0){
					actflag=1;
					atkval=CheckSkillHurt(ucb.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
					hurt=atkval-encard.getCurdef();
					if(hurt<=0) hurt=1;
					if(CheckControl(ucb.getCardprop(),encard.getCardprop())>0){//本位有克制，伤害增加0.05倍
						hurt=(int)(hurt*1.05);
					}
					
					if(encard.getCurhp()-hurt<=0) {
						 tm1.put("KRH", encard.getCurhp());
						encard.setCurhp(0);
						tm1.put("KE", 0);
					}else{
						tm1.put("KRH", hurt);
						encard.setCurhp(encard.getCurhp()-hurt);
						tm1.put("KE", encard.getCurhp());
					}
					tm1.put("KA", targ);
					tm1.put("KB", encard.getCardid());
					tm1.put("KC", encard.getLocate());
					tm1.put("KD", hurt);
					tm1.put("KF", encard.getHp()+encard.getSameaddhp());
					tm1.put("KG", encard.getCardcode());
					tm1.put("KH", encard.getStarlevel());
					tm1.put("KI", encard.getCurstyle());
					mlist.add(tm1);	
					actflag=CheckUserWinLose(enul);
					if(actflag<1) break;
				}
			}else break;
		}
		return actflag;
	}
	//玩家技能攻击列
	public int ActCol(UserCard ucb,ArrayList<UserCard> enul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag,String targ){
		int actflag=0,winflag=0;
		int lineflag=0;
		int curloc=locflag;
		int atkval=0,hurt=0;
		int calflag=0;
		do{
			do{
				lineflag=lineflag+1;
				Map<String,Object> tm2=new HashMap<String,Object>();
				if(curloc<enul.size()){
					if(enul.get(curloc).getCurhp()>0){
						UserCard encard=enul.get(curloc);
						actflag=1;
						atkval=CheckSkillHurt(ucb.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
						hurt=atkval-encard.getCurdef();
						if(hurt<=0) hurt=1;	
						if(CheckControl(ucb.getCardprop(),encard.getCardprop())>0){//本位有克制，伤害增加0.05倍
							hurt=(int)(hurt*1.05);
						}
						
						if(encard.getCurhp()-hurt<=0) {
							tm2.put("KRH", encard.getCurhp());
							encard.setCurhp(0);
							tm2.put("KE", 0);
						}else{
							tm2.put("KRH", hurt);
							encard.setCurhp(encard.getCurhp()-hurt);
							tm2.put("KE", encard.getCurhp());
						} 
						tm2.put("KA", targ);
						tm2.put("KB", encard.getCardid());
						tm2.put("KC", encard.getLocate());
						tm2.put("KD", hurt);
						tm2.put("KF", encard.getHp()+encard.getSameaddhp());
						tm2.put("KG", encard.getCardcode());
						tm2.put("KH", encard.getStarlevel());
						tm2.put("KI", encard.getCurstyle());
						mlist.add(tm2);	
						winflag=CheckUserWinLose(enul);
						if(winflag<1) break;
					}									
				}
				if(curloc==0) curloc=3;
				if(curloc==1) curloc=4;
				if(curloc==2) curloc=5;
				if(curloc==3) curloc=0;
				if(curloc==4) curloc=1;
				if(curloc==5) curloc=2;
			}while(lineflag<2);
			if(actflag>0 || winflag<1 ) break;
			if(actflag<1){//换下一列
				curloc=locflag+1;
			}
			calflag=calflag+1;
		}while(calflag<3);
		return winflag;
	}
	public int CheckUserWinLose(ArrayList<UserCard> ul){//攻击后检查是否胜
		int resultflag=1;
		for(int i=0;i<ul.size();i++){
			if(ul.get(i).getCurhp()>0)
				return 1;
			else
				resultflag=0;
		}
		return resultflag;
	}
	//战斗结束，计算收益
	public void FinGetSkill(int wlflag,User p,int elevel,String skcode,int pageno,String rname,Map<String,Object> smap) throws SQLException{
		int flag=0;
		if(wlflag>0){//玩家胜概率掉落书页 30%+(敌方-我方)%
			Random rd = new Random();
			int k=rd.nextInt(100);
			int downval=elevel-p.getLevel()+30;
			//根据活动提高夺计成功率
			int cursecond=(int)(System.currentTimeMillis()/1000);
			if(GameManager.getInstance().getSvractdict().containsKey(3)){//针对夺计活动
				if(cursecond>=GameManager.getInstance().getSvractdict().get(3).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(3).getEtime()){
					downval=(int)(downval*GameManager.getInstance().getSvractdict().get(3).getActvalue());
				}
			}
			if(100-downval>downval){
				if(k>=100-downval)	flag=1;//掉落书页	
			}else{
				if(k<downval) flag=1;
			}	
		}
		flag=flag+wlflag;//1 胜没掉落 2 胜掉落 0 输
		GameServerDao dao = new GameServerDao();
		try {
			dao.FinGetSkill(p,flag,skcode,pageno,rname,elevel,smap);//
		} catch (SQLException e) {
			throw e;
		}
	}	
	//概率是否命中
	public int CheckRandom(int percentval){
		int flag=0;
		int rndval=-1;
		rndval=CommTools.randomval(0,100);
		if(100-percentval>=percentval){
			if(rndval>=(100-percentval) && rndval<100){
				flag=1;
			}
		}else{
			if(rndval<percentval) flag=1;
		}
		return flag;
	}
	//概率是否命中 返回发动的技能id 0不发动
	public int CheckRandom(UserCard uc){
		int flag=0;
		int rndval=CommTools.randomval(0,100);
		Iterator<UserSkill> it = uc.getSkillMap().values().iterator();
		int maxval=0,minval=0;
		while(it.hasNext()){
			minval=maxval;
			UserSkill sk=it.next();
			if(sk.getSkillto()>=0){//主动技能
				maxval=maxval+sk.getSkillrand();
				if(rndval>=minval && rndval<maxval){
					flag=sk.getSkillid();
					break;
				}	
			}					
		}
		return flag;
	}
	public int CheckControl(String o,String t){
		/*int flag=0;
		//邪克萌、萌克冷、冷克霸、霸克邪
		if(o.equals("邪")&&t.equals("萌")) flag=1;
		if(o.equals("萌")&&t.equals("冷")) flag=1;
		if(o.equals("冷")&&t.equals("霸")) flag=1;
		if(o.equals("霸")&&t.equals("邪")) flag=1;
		return flag;*/
		return 0;
		
	}
	//计算技能发动后的攻击力
	public int CheckSkillHurt(int atkval,String skcode,int grade){
		int flag=0;
		double perhurt=0,hurtadd=0;
		SkillPages sp= GameManager.getInstance().getSkilldict().get(skcode);
		perhurt=sp.getHurtpert()+sp.getUphurtper()*grade;
		//（伤害百分比+Lv*百分比增幅）+$（附加值+Lv*附加值增幅）
		hurtadd=sp.getHurt()+sp.getUphurt()*grade;
		if((int)(perhurt*atkval)==0) flag=atkval+1;
		else flag=(int)(perhurt*atkval);
		if(sp.getHurt()>0&&sp.getHurt()<1){
			flag=flag+(int)(hurtadd*atkval);
		}else flag=flag+(int)hurtadd;
		
		return flag;
	}
	//对位攻击时返回可攻击的位置
	public int CheckNext(ArrayList<UserCard> ul,ArrayList<UserCard> enul,int curloc){
		int k=-1;
		int stopflag=0;
		int locflag=curloc;
		stopflag=locflag;
		do{
			if(locflag<enul.size()){
				if(enul.get(locflag).getCurhp()>0){
					k=locflag;
					break;
				}else locflag=locflag+1;
			}else{
				locflag=0;
			}	
		}while(stopflag!=locflag);
		return k;
	}
	public void friendmatch(User p,String rname,int enlevel,Map<String,Object> smap) throws SQLException{
		//获取要切磋好友的阵容列表
		ArrayList<UserCard> enul=new ArrayList<UserCard>();
		GetEnCardList(rname,p.getSvrcode(),enul);
		ArrayList<UserCard> ul=new ArrayList<UserCard>();
		for(int i=0;i<p.getCardlocid().length;i++){ //战斗阵容
			if(p.getCardlocid()[i]<=0) break;
			UserCard uu=p.getCardlist().get(p.getCardlocid()[i]);
			uu.setCurhp(uu.getHp()+uu.getSameaddhp());
			uu.setCuratk(uu.getAtk()+uu.getSameaddatk());
			uu.setCurdef(uu.getDef()+uu.getSameadddef());
			ul.add(uu);			
		}
		int winloseflag=-1;
		try{
			winloseflag=StartBattle(ul,enul,smap);
			smap.put("AA", "friendmatch");
			smap.put("AB", winloseflag);
			smap.put("AC", "@");
			int pkexp=0,pkmoney=0;
			if(winloseflag==0){
				smap.put("AD", 0);//经验=等级*10-20 钱=（等级*10-20）*10	不扣=钱/1.5
				smap.put("AR",0);
			}else
			{
				Pkexpmoney pkem=GameManager.getInstance().getPkexpmon().get(p.getLevel());
				smap.put("AD", pkem.getExp());
				smap.put("AR", pkem.getMoney());
				pkexp=pkem.getExp();
				pkmoney=pkem.getMoney();
			}
			smap.put("AE", -1);
			smap.put("AF", "@");
			smap.put("AG", "@");
			FinMatch(winloseflag,p,enlevel,rname,smap,pkexp,pkmoney);
			smap.put("AR",p.getLevel());
			smap.put("AV", p.getExp());//用户当前经验
			smap.put("AU", p.getLevelup());//用户升级经验
			smap.put("AW", p.getMembers());
			smap.put("AX",rname);
			smap.put("AJ", p.getBall());
			GetMemberMap(ul,enul,smap);//双方阵容
			ArrayList<String> sklist = new ArrayList<String>();
			for(int i=0;i<this.actskill.size();i++){
				int kkk=0;
				for(int j=0;j<sklist.size();j++){
					if(this.actskill.get(i).equals(sklist.get(j)))
						kkk=1;
				}
				if(kkk==0)	sklist.add(this.actskill.get(i));
			}
			smap.put("AK",sklist.toArray());
		}catch(SQLException e){
			throw e;
		}
		
	}
	//战斗结束，计算收益
	public void FinMatch(int wlflag,User p,int elevel,String rname,Map<String,Object> smap,int exp,int mon) throws SQLException{
		GameServerDao dao = new GameServerDao();
		try {
			dao.FinFriendMatch(p,wlflag,rname,elevel,smap,exp,mon);//
		} catch (SQLException e) {
			throw e;
		}
	}	
}
