package com.gameserver.logic;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import com.gameserver.comm.CommTools;
import com.gameserver.dao.GameServerDao;
import com.gameserver.manager.GameManager;
import com.gameserver.model.CMissionNpcDict;
import com.gameserver.model.SceneDict;
import com.gameserver.model.SkillPages;
import com.gameserver.model.User;
import com.gameserver.model.UserCard;
import com.gameserver.model.UserMission;
import com.gameserver.model.UserSkill;
import com.gameserver.model.UserStreet;

public class MissionLogic implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7240614902084523185L;
	private static final Logger logger = Logger.getLogger(MissionLogic.class.getName());
	private ArrayList<String> actskill;
	public MissionLogic(){
		this.actskill=new ArrayList<String>();
	}
	public ArrayList<String> getActskill() {
		return actskill;
	}
	public void setActskill(ArrayList<String> actskill) {
		this.actskill = actskill;
	}
	//返回用户任务信息
	public int actmission(User p,Map<String, Object> sendMap,int sceneno){
		UserMission um;
		ArrayList<Map<String, Object>> umlist=new ArrayList<Map<String, Object>>();//关卡列表
		ArrayList<String> rcode=new ArrayList<String>();
		Iterator<SceneDict> it = GameManager.getInstance().getScenedict().values().iterator();
		ArrayList<Map<String,Object>> mmm=new ArrayList<Map<String,Object>>();
		while(it.hasNext()){
			SceneDict k= it.next();
			Map<String,Object> newmap = new HashMap<String,Object>();
			newmap.put("AA", k.getSceneno());
			newmap.put("AB", k.getScenename());
			newmap.put("AC", k.getScecode());
			//logger.info("任务列表："+p.getScemisson().size());
			ArrayList<UserMission> checkal =p.getScemisson().get(k.getSceneno());
			for(int i=0;i<checkal.size();i++){
				newmap.put("AH", 0);
				if(checkal.get(i).getIfcanuse()>0){
					newmap.put("AH", 1);
					break;
				}
			}
			mmm.add(newmap);
			if(k.getSceneno()==sceneno){//取同一场景下的所有关卡
				if(Integer.parseInt(newmap.get("AH").toString())==0){//此场景未开启
					return 0;
				}
				sendMap.put("AI", k.getAwardcode());
				sendMap.put("AJ", k.getAwardnum());
				sendMap.put("AK",k.getAwardname());
				//0装备1技能2道具3符文
				switch(k.getAwardtype()){
				case 0:
					sendMap.put("AR", GameManager.getInstance().getDevsetdict().get(k.getAwardcode()).getDevcolor());
					break;
				case 1:
					sendMap.put("AR", GameManager.getInstance().getSkilldict().get(k.getAwardcode()).getSkillcolor());
					break;
				case 2:
				case 3:
					sendMap.put("AR", 0);
					break;
				}
				GameServerDao dao=new GameServerDao();
				try{
					sendMap.put("AL",dao.checkgetaward(p,sceneno));//
				}catch(SQLException e){
					sendMap.put("AL",0);
				}
				ArrayList<UserMission> secl = p.getScemisson().get(sceneno);
				for(int s=0;s<secl.size();s++){
					um=secl.get(s);
					Map<String,Object> maps = new HashMap<String,Object>();
					//取关卡阵容
					ArrayList<CMissionNpcDict> al= GameManager.getInstance().getCmnpccdict().get(um.getMissionid());
					String tempstr="杂碎";
					for(int i=0;i<al.size();i++){
						CMissionNpcDict mnpc=al.get(i);
						if(mnpc.getNpctype().equals("BOSS")){
							tempstr="BOSS";
							maps.put("CD",mnpc.getNpccode());
							rcode.add(mnpc.getNpccode());
							maps.put("CE",mnpc.getNpcprop());
							maps.put("CR", GameManager.getInstance().getCarddict().get(mnpc.getNpccode()).getStarlevel());
							break;
						}else if(mnpc.getNpctype().equals("精英")){
							if(tempstr.equals("杂碎")){
								tempstr="精英";
								maps.put("CD",mnpc.getNpccode());
								rcode.add(mnpc.getNpccode());
								maps.put("CE",mnpc.getNpcprop());
								maps.put("CR", GameManager.getInstance().getCarddict().get(mnpc.getNpccode()).getStarlevel());
							}
							
						}else{
							if(tempstr.equals("杂碎")){
								maps.put("CD",mnpc.getNpccode());
								rcode.add(mnpc.getNpccode());
								maps.put("CE",mnpc.getNpcprop());
								maps.put("CR", 1);
							}
						}
						
					}
					um.getMnpclist().clear();
					for(int i=0;i<al.size();i++){
						CMissionNpcDict mnpc =al.get(i).clone();
						um.getMnpclist().add(mnpc);
					}
					maps.put("BA",um.getMissionid());
					maps.put("BB",um.getCurtime());
					maps.put("BC",um.getAssess());
					maps.put("BD",um.getWinlose());
					//if(p.getViplevel()>=4) maps.put("BE",1);//vip4以上可直接跳过
					maps.put("BE",um.getIfcopy());
					maps.put("BO", 0);
					if(um.getIfcopy()>0){//副本
						if(GameManager.getInstance().getTalkdict().containsKey(um.getMissionid())){//有对话内容
							if(GameManager.getInstance().getTalkdict().get(um.getMissionid()).containsKey(1)){//有战后对话
								maps.put("BO", 1);
							}
						}
					}
					maps.put("BF",um.getMissionseq());
					maps.put("BG",um.getMissionmp());
					maps.put("BH",um.getCopymp());
					maps.put("BI",um.getGrade());
					maps.put("BJ",um.getMdesc());
					maps.put("BK",um.getIfcanuse());
					maps.put("BV", um.getCurnum());
					maps.put("BW", um.getGiveexp());
					maps.put("BX", um.getGivemoeny());
					maps.put("BY", um.getGoods1code());
					maps.put("BZ",um.getGoods2code());
					maps.put("BL", um.getMissname());
					maps.put("BM", um.getMissnum());//当前关卡购买次数
					int goldnum=(um.getMissnum()+1)*10;
					if(goldnum>200) goldnum=200;
					maps.put("BN", goldnum);//当前关卡购买gold
					if(!um.getGoods1code().equals("@")){
						if(um.getGoods1code().substring(0, 1).equals("B")) 
							maps.put("DV", GameManager.getInstance().getDevsetdict().get(um.getGoods1code()).getDevcolor());
						else if(um.getGoods1code().substring(0, 1).equals("C")) 
							maps.put("DV", GameManager.getInstance().getSkilldict().get(um.getGoods1code()).getSkillcolor());
						else maps.put("DV", 0);
						rcode.add(um.getGoods1code());
					}
					if(!um.getGoods2code().equals("@")) {
						if(um.getGoods2code().substring(0, 1).equals("B")) 
							maps.put("DW", GameManager.getInstance().getDevsetdict().get(um.getGoods2code()).getDevcolor());
						else if(um.getGoods2code().substring(0, 1).equals("C"))
							maps.put("DW", GameManager.getInstance().getSkilldict().get(um.getGoods2code()).getSkillcolor());
						else maps.put("DW", 0);
						rcode.add(um.getGoods2code());
					}
					umlist.add(maps);
				}		
			}
		}
		Collections.sort(mmm,new Comparator<Map<String,Object>>(){
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
				return Integer.parseInt(arg0.get("AA").toString())-Integer.parseInt(arg1.get("AA").toString()); }  

		});
		sendMap.put("DD", mmm.toArray());
		sendMap.put("AD", sceneno);
		sendMap.put("AE", umlist.toArray());
		sendMap.put("AF",p.getCurmission());
		for(int i=0;i<rcode.size();i++){
			for(int j=i+1;j<rcode.size();j++){
				if(rcode.get(i).equals(rcode.get(j))){
					rcode.remove(j);
					j--;
				}				
			}
		}
		sendMap.put("AG", rcode.toArray());
		
		return 1;
		//增加AG 记录所有资源code
	}
	//开始战斗
	public void RunMission(User p,UserMission um,Map<String,Object> sendMap)throws SQLException{
		ArrayList<CMissionNpcDict> al=new ArrayList<CMissionNpcDict>();
		for(int i=0;i<um.getMnpclist().size();i++){
			al.add(um.getMnpclist().get(i));
		}
		//logger.info("Npc数量："+al.size());
		for(int i=0;i<al.size();i++){
			CMissionNpcDict mnpc =al.get(i);
			int trand=0;
			//trand=CommTools.randomval((0-mnpc.getHpalter()),mnpc.getHpalter());//随机变更血攻防
			mnpc.setCurhp(mnpc.getHP()+trand);
			mnpc.setAllhp(mnpc.getCurhp());
			//trand=CommTools.randomval((0-mnpc.getAtkalter()),mnpc.getAtkalter());
			mnpc.setCuratk(mnpc.getATK()+trand);
			mnpc.setAllatk(mnpc.getCuratk());
			//CommTools.randomval((0-mnpc.getDefalter()),mnpc.getDefalter());
			mnpc.setCurdef(mnpc.getDEF()+trand);
			mnpc.setAlldef(mnpc.getCurdef());
			//logger.info("npc序号："+mnpc.getNpccode()+"++血："+mnpc.getCurhp()+"+++攻："+mnpc.getCuratk()+"++++防"+mnpc.getCurdef());
		}
		ArrayList<UserCard> ul=new ArrayList<UserCard>();
		for(int i=0;i<6;i++){ //战斗阵容
			if(p.getCardlocid()[i]<=0) break;
			UserCard uu=p.getCardlist().get(p.getCardlocid()[i]);
			uu.setCurhp(uu.getHp()+uu.getSameaddhp());
			uu.setCuratk(uu.getAtk()+uu.getSameaddatk());
			uu.setCurdef(uu.getDef()+uu.getSameadddef());
			ul.add(uu);
			//logger.info("玩家卡牌代码："+uu.getCardcode()+"++血："+uu.getCurhp()+"+++攻："+uu.getCuratk()+"++++防"+uu.getCurdef());
		}
		int winloseflag=-1;
		try{
			winloseflag=StartBattle(ul,al,sendMap);//开始战斗逻辑运算
			sendMap.put("AA", um.getMissionid());
			sendMap.put("AB", winloseflag);
			sendMap.put("AC", "0");
			if(winloseflag==0){
				sendMap.put("AD", (int)(um.getGiveexp()*um.getLosepercent()/100));
				sendMap.put("AR", (int)(um.getGivemoeny()*um.getLosepercent()/100));
			}else
			{
				//重新计算是否经验加成
				int tempexp=um.getGiveexp();
				int cursecond=(int)(System.currentTimeMillis()/1000);
				if(GameManager.getInstance().getSvractdict().containsKey(1)&&um.getMissionid()>=5){
					if(cursecond>=GameManager.getInstance().getSvractdict().get(1).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(1).getEtime()){
						tempexp=(int)(tempexp*GameManager.getInstance().getSvractdict().get(1).getActvalue());
					}
				}
				sendMap.put("AD", tempexp);
				sendMap.put("AR", um.getGivemoeny());
			}
			String enyname="";//任务对手名字
			String tempstr="杂碎";
			for(int i=0;i<al.size();i++){
				CMissionNpcDict mnpc=al.get(i);
				if(mnpc.getNpctype().equals("BOSS")){
					enyname=mnpc.getNpcname();
					break;
				}else if(mnpc.getNpctype().equals("精英")){
					if(tempstr.equals("杂碎")){
						enyname=mnpc.getNpcname();
					}
					
				}else{
					if(tempstr.equals("杂碎")){
						enyname=mnpc.getNpcname();
					}
				}
			}
			sendMap.put("AE", -1);
			sendMap.put("AF", "@");
			//um.setMissnum(um.getMissnum()+1);
			FinishBattle(winloseflag,p,um,sendMap,ul,1);//1正常战斗 0 连闯
			sendMap.put("AS",p.getLevel());
			sendMap.put("AT",p.getPower());
			sendMap.put("AV", p.getExp());//用户当前经验
			sendMap.put("AU", p.getLevelup());//用户升级经验
			sendMap.put("AW", p.getMembers());
			sendMap.put("AX",enyname);
			sendMap.put("AJ", p.getBall());
			GetMemberMap(ul,al,sendMap);//双方阵容
			ArrayList<String> sklist = new ArrayList<String>();
			for(int i=0;i<this.actskill.size();i++){
				int kkk=0;
				for(int j=0;j<sklist.size();j++){
					if(this.actskill.get(i).equals(sklist.get(j)))
						kkk=1;
				}
				if(kkk==0)	sklist.add(this.actskill.get(i));
			}
			sendMap.put("AK",sklist.toArray());
		}catch(SQLException e){
			throw e;
		}		
	}
	//开始互攻
	public int StartBattle(ArrayList<UserCard> ul,ArrayList<CMissionNpcDict> al,Map<String,Object> mapgame){
		int sumflag=0;
		int checkflagp=1,checkflagn=1;
		int winloseflag=-1;
		ArrayList<Map<String,Object>> misslist=new ArrayList<Map<String,Object>>();
		//logger.info("开始战斗**********************************************");
		
		int[] curpoint={0,0};//玩家总血，对方总血
		for(int i=0;i<ul.size();i++){
			curpoint[0]=curpoint[0]+ul.get(i).getCurhp();		
		}
		for(int i=0;i<al.size();i++){
			curpoint[1]=curpoint[1]+al.get(i).getCurhp();		
		}	
		mapgame.put("HA", curpoint[0]);//玩家总血
		mapgame.put("HB", curpoint[1]);//对方总血
		
		System.out.println("HA="+curpoint[0]);
		System.out.println("HB="+curpoint[1]);
		
		
		for(int k=0;k<20;k++){//最多20回合	
			
			for(int i=0;i<6;i++){
				if(i<ul.size()){//玩家攻
					if(ul.get(i).getCurhp()>0){
						Map<String,Object> newmap = new HashMap<String,Object>();
						sumflag=sumflag+1;
						newmap.put("CA", sumflag);
						newmap.put("CB", "P");
						newmap.put("CC", ul.get(i).getCardid());
						newmap.put("CD", i);
						checkflagp=PlayerRun(ul,al,i,newmap);
						//logger.info("玩家攻::::::::::::"+i+"+++攻击结果值："+checkflagp);
						//logger.info("往来数：##########################################"+sumflag);
						misslist.add(newmap);
					}	
				}
				if(checkflagp<1){//玩家胜
					winloseflag=1;
					//logger.info("玩家胜");
					break;
				}
				if(i<al.size()){//npc攻
					if(al.get(i).getCurhp()>0){
						Map<String,Object> newmap = new HashMap<String,Object>();
						sumflag=sumflag+1;
						newmap.put("CA", sumflag);
						newmap.put("CB", "N");
						newmap.put("CC", al.get(i).getNpcid());
						newmap.put("CD", i);
						checkflagn=NpcAction(ul,al,i,newmap);
						//logger.info("NPC攻:::::::::::::::"+i+"+++攻击结果值："+checkflagn);
						//logger.info("往来数：##########################################"+sumflag);
						misslist.add(newmap);
					}						
				}				
				if(checkflagn<1){//NPC胜
					winloseflag=0;
					//logger.info("玩家败");
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
	//战斗结束，计算收益
	public void FinishBattle(int wlflag,User p,UserMission um,Map<String,Object> smap,ArrayList<UserCard> ul,int mtype) throws SQLException{
		//um.setMissnum(um.getMissnum()+1);//累计任务次数
		int flag=-1,sumval=0;
		 int[] a=new int[15];
		if(wlflag>0){//玩家胜	
			a[0]=um.getSkpage();
			a[1]=um.getDevone();
			a[2]=um.getDevtwo();
			a[3]=um.getDevthr();
			a[4]=um.getStar1();
			a[5]=um.getStar2();
			a[6]=um.getStar3();
			a[7]=um.getStar4();
			a[8]=um.getStar5();
			a[9]=um.getMarks();
			a[10]=um.getGoods1();
			a[11]=um.getGoods2();
			a[12]=um.getProp1();
			a[13]=um.getProp2();
			a[14]=um.getProp3();
			int[] loc={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
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
			for(int i=0;i<a.length;i++){
				sumval=sumval+a[i];
			}
		    int maxval=100000-sumval;
		    int minval=0;
			Random rd = new Random();
			int k=rd.nextInt(100000);
			for(int i=0;i<a.length;i++){
				minval=maxval;
				if(a[i]==0) continue;
				maxval=maxval+a[i];
				if(k>=minval && k<maxval){
					flag=loc[i];
					break;
				}
			}
			flag=flag+1;
			if(flag==2) flag=24;
			if(flag==3) flag=34;
			if(flag==4) flag=44;
			if(p.getLevel()<=10){
				switch(p.getMissionsum()){
				case 1:
					flag=21;//随机精良武器
					break;
				case 4:
					flag=6;//随机2星卡
					break;
				case 9:
					flag=22;//随机精良防具
					break;
				/*case 20:
					flag=101;//符文M001
					break;*/
				case 30:
					flag=7;//随机3星
					break;
				case 100:
					flag=8;//随机4星
					break;
				}
			}
			//评价
			double percenthp=0,tempdou=0;
			int livenum=0,warassess=0;
			for(int i=0;i<ul.size();i++){
				UserCard uc=ul.get(i);
				if(uc.getCurhp()>0){
					livenum=livenum+1;
					tempdou=(double)uc.getCurhp()/uc.getHp();
					DecimalFormat df1 = new DecimalFormat("0.00");
					percenthp=percenthp+Double.parseDouble(df1.format(tempdou));
				}
			}
			warassess=(int)(percenthp*80/ul.size())+(int)((double)livenum/ul.size()*20);
			//logger.info("掉落物品标示号："+flag+"   评价值："+warassess);
			if(warassess>=60) {
				smap.put("AC", "3");
				if(um.getAssess().equals("@"))	um.setAssess("3");
				else if(Integer.parseInt(um.getAssess())<3) um.setAssess("3");
			}else if(warassess>=40){
				smap.put("AC", "2");
				if(um.getAssess().equals("@"))	um.setAssess("2");
				else if(Integer.parseInt(um.getAssess())<2) um.setAssess("2");
			}else{
				smap.put("AC", "1");
				if(um.getAssess().equals("@"))	um.setAssess("1");
			}
		}
		GameServerDao dao = new GameServerDao();
		try {
			dao.FinishBattle(p, um, flag, wlflag,smap,mtype);
			smap.put("PA", 0);
			if(dao.CheckMissPass(p,um)>0) smap.put("PA", 1);
			else smap.put("PA", 0);
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
					//logger.info("卡牌id"+uc.getCardid()+"概率："+rndval+"小："+minval+"大："+maxval+"技能id"+flag);
					break;
				}	
				
			}					
		}
		return flag;
	}
	//npc攻击
	public int NpcAction(ArrayList<UserCard> ul,ArrayList<CMissionNpcDict> al,int locflag,Map<String,Object> tmap){
		UserCard ucb;
		CMissionNpcDict npcl;
		SkillPages skp;
		int allhurt=-1;//群攻技能
		int hurt=0; //
		int skillrd=100;
		int skillgrade=0;
		String skillcode="";
		npcl=al.get(locflag);	
		if(!npcl.getSkillcode().equals("@")){//有技能
			skp=GameManager.getInstance().getSkilldict().get(npcl.getSkillcode());
			if(skp.getSkillto()>=0){
				allhurt=skp.getSkillto();//技能
				skillrd=skp.getSkillrand();//发动概率
				skillcode=skp.getSkillcode();
				skillgrade=npcl.getSkillgrade();	
			}else{//计算buff
				npcl.setCurhp(npcl.getCurhp()+(int)(skp.getHps()*npcl.getSkillgrade()));
				npcl.setCuratk(npcl.getCuratk()+(int)(skp.getAtks()*npcl.getSkillgrade()));
				npcl.setCurdef(npcl.getCurdef()+(int)(skp.getDefs()*npcl.getSkillgrade()));
			}
		}
		int locate=-1;
		int actflag=0;
		ArrayList<Map<String,Object>> mlist=new ArrayList<Map<String,Object>>();
		if(allhurt>=0 && CheckRandom(skillrd)==1){//有技能 0群攻 1 对位攻，2行，3列
			tmap.put("CE", 1);
			tmap.put("CG",skillcode);
			this.actskill.add(skillcode);
			switch(allhurt){
			case 0://群攻
				actflag=0;
				actflag=NpcAll(npcl, ul, skillcode, skillgrade, mlist, locflag);
				break;
			case 1:
				actflag=0;
				actflag=this.NpcSingle(al, ul, skillcode, skillgrade, mlist, locflag);	
				break;
			case 2://按排攻击，对方所在排
				actflag=0;
				if(locflag<3){//先打第一排
					actflag=NpcActLineFront(npcl, ul, skillcode, skillgrade, mlist);
					if(actflag<0){//打第二排
						actflag=NpcLineBack(npcl, ul, skillcode, skillgrade, mlist);
					}
					if(actflag<=0) actflag=0;
				}else{//先打第二排
					actflag=NpcLineBack(npcl, ul, skillcode, skillgrade, mlist);
					if(actflag<0){//打第1排
						actflag=NpcActLineFront(npcl, ul, skillcode, skillgrade, mlist);
					}
					if(actflag<=0) actflag=0;
				}
				break;
			case 3://按列攻击 对方所在列
				actflag=0;
				actflag=NpcActRect(npcl, ul, skillcode, skillgrade, mlist, locflag);				
				break;
			}			
			tmap.put("CH", mlist.toArray());
		}else {
			tmap.put("CE", 0);
			locate=CheckNext(ul,al,locflag,0);//1：npc位置 0：player位置
			//logger.info("npc要攻击的位置："+locate);
			Map<String,Object> mmm=new HashMap<String,Object>();
			if(locate>=0){
				actflag=1;
				ucb=ul.get(locate);
				hurt=npcl.getCuratk()-ucb.getCurdef();
				if(hurt<=0) hurt=1;	
				if(CheckControl(npcl.getNpcprop(),ucb.getCardprop())>0){//本位有克制，伤害增加0.05倍
					hurt=(int)(hurt*1.05);
				}
				if(ucb.getCurhp()-hurt<=0) {
					ucb.setCurhp(0);
					mmm.put("KE", 0);
				}
				else{
					ucb.setCurhp(ucb.getCurhp()-hurt);
					mmm.put("KE", ucb.getCurhp());
				}
				mmm.put("KA", "P");
				mmm.put("KB", ucb.getCardid());
				mmm.put("KC",ucb.getLocate());
				mmm.put("KD", hurt);
				mmm.put("KF", ucb.getHp()+ucb.getSameaddhp());
				mmm.put("KG", ucb.getCardcode());
				mmm.put("KH", ucb.getStarlevel());
				mmm.put("KI", ucb.getCurstyle());
				//logger.info("npc正常攻击"+npcl.getNpcname()+"--->"+ucb.getCardid()+"伤害："+hurt+"位置："+locate);
				mlist.add(mmm);	
				actflag=CheckUserWinLose(ul);
			}
			tmap.put("CH", mlist.toArray());
		}
		int totalhp=Integer.parseInt(tmap.get("HA").toString());
		for(int i=0;i<mlist.size();i++){
			Map hpmap=mlist.get(i);
			totalhp-=Integer.parseInt(tmap.get("KD").toString());
		}
		tmap.put("KJ", totalhp+"");
		return actflag;
	}
	public void GetMemberMap(ArrayList<UserCard> ul,ArrayList<CMissionNpcDict> al,Map<String,Object> Map){
		ArrayList<Map<String,Object>> cardmember = new ArrayList<Map<String,Object>>();
		ArrayList<Map<String,Object>> npcmember = new ArrayList<Map<String,Object>>();
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
		for(int i=0;i<al.size();i++){
			skname="@";
			skdesc="@";
			Map<String,Object> tempmap = new HashMap<String,Object>();
			ArrayList<Map<String,Object>> skmap=new ArrayList<Map<String,Object>>();
			tempmap.put("EA", al.get(i).getNpcid());
			tempmap.put("EB", al.get(i).getNpcloc());
			tempmap.put("EC", al.get(i).getNpccode());
			if(!al.get(i).getNpctype().equals("杂碎")) 	tempmap.put("ER", GameManager.getInstance().getCarddict().get(al.get(i).getNpccode()).getStarlevel());
			else tempmap.put("ER", 1);
			tempmap.put("EI", 1);
			if(!al.get(i).getSkillcode().equals("@")){//有技能
				SkillPages skp=GameManager.getInstance().getSkilldict().get(al.get(i).getSkillcode());
				if(skp.getSkillto()<0){
					Map<String,Object> skillmap = new HashMap<String,Object>();
					skname=skp.getSkillcode();
					if(skp.getHps()>0) skdesc="HP";
					if(skp.getAtks()>0) skdesc="ATK";
					if(skp.getDefs()>0) skdesc="DEF";
					skillmap.put("EE", skname);
					skillmap.put("EF", skdesc);
					this.actskill.add(skname);
					skmap.add(skillmap);
				}
			}
			tempmap.put("ED", skmap.toArray());
			npcmember.add(tempmap);
		}
		Map.put("AM", npcmember.toArray());
	}

	//玩家攻击
	public int PlayerRun(ArrayList<UserCard> ul,ArrayList<CMissionNpcDict> al,int locflag,Map<String,Object> tmap){
		UserCard ucb;
		CMissionNpcDict npcl;
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
		ArrayList<Map<String,Object>> mlist=new ArrayList<Map<String,Object>>();
		skillrd=CheckRandom(ucb);
		if(skillrd>0){//有技能 0群攻 1 对位攻，2行，3列
			UserSkill sk=ucb.getSkillMap().get(skillrd);
			allhurt=sk.getSkillto();//技能发动方式
			skillcode=sk.getSkillcode();
			skillgrade=sk.getSkillgrade();
			tmap.put("CG",skillcode);
			tmap.put("CE", 1);
			this.actskill.add(skillcode);
			switch(allhurt){
			case 0://群攻
				actflag=0;
				actflag=this.PlayerAll(ucb, al, skillcode, skillgrade, mlist, locflag);
				break;
			case 1:
				actflag=0;
				//logger.info("玩家单攻");
				actflag=PlayerSingle(ul, al, skillcode, skillgrade, mlist, locflag);	
				break;
			case 2://按排攻击，对方所在排
				actflag=0;
				if(locflag<3){//先打第一排
					actflag=this.ActLineFront(ucb, al, skillcode, skillgrade, mlist);
					if(actflag<0){//打第二排
						actflag=this.ActLineBack(ucb, al, skillcode, skillgrade, mlist);
					}
					if(actflag<=0) actflag=0;
				}else{//先打第二排
					actflag=this.ActLineBack(ucb, al, skillcode, skillgrade, mlist);
					if(actflag<0){//打第1排
						actflag=this.ActLineFront(ucb, al, skillcode, skillgrade, mlist);
					}
					if(actflag<=0) actflag=0;
				}
				break;
			case 3://按列攻击 对方所在列
				actflag=0;
				actflag=ActRect(ucb, al, skillcode, skillgrade, mlist, locflag);				
				break;
			}
			tmap.put("CH", mlist.toArray());
		}else {//正常攻击
			tmap.put("CE", 0);
			locate=CheckNext(ul,al,locflag,1);//1：npc位置 0：player位置
			//logger.info("攻击位置："+locate);
			Map<String,Object> mmm=new HashMap<String,Object>();
			if(locate>=0){
				actflag=1;
				npcl=al.get(locate);
				hurt=ucb.getCuratk()-npcl.getCurdef();
				if(hurt<=0) hurt=1;	
				if(CheckControl(ucb.getCardprop(),npcl.getNpcprop())>0){//本位有克制，伤害增加0.05倍
					hurt=(int)(hurt*1.05);
				}
				if(npcl.getCurhp()-hurt<=0) {
					npcl.setCurhp(0);
					mmm.put("KE", 0);
				}
				else{
					npcl.setCurhp(npcl.getCurhp()-hurt);
					mmm.put("KE", npcl.getCurhp());
				}
				mmm.put("KA", "N");
				mmm.put("KB", npcl.getNpcid());
				mmm.put("KC",npcl.getNpcloc());
				mmm.put("KD", hurt);
				mmm.put("KF", npcl.getAllhp());
				mmm.put("KG", npcl.getNpccode());
				if(!npcl.getNpctype().equals("杂碎")) mmm.put("KH", GameManager.getInstance().getCarddict().get(npcl.getNpccode()).getStarlevel());
				else mmm.put("KH", 1);
				mmm.put("KI", 1);
				
				actflag=CheckNpcWinLose(al);
				//logger.info("玩家正常攻击"+ucb.getCardid()+"--->"+npcl.getNpcname()+"npc剩余血量："+npcl.getCurhp()+"位置："+locate);
				mlist.add(mmm);							
			}
			tmap.put("CH", mlist.toArray());
		}
		int totalhp=Integer.parseInt(tmap.get("HA").toString());
		for(int i=0;i<mlist.size();i++){
			Map hpmap=mlist.get(i);
			totalhp-=Integer.parseInt(tmap.get("KD").toString());
		}
		tmap.put("KJ", totalhp+"");
		//logger.info("受伤："+mlist.size());
		return actflag;
		
	}
	// 判断npc是否都已阵亡
	public int CheckNpcWinLose(ArrayList<CMissionNpcDict> al){//攻击后检查是否胜
		int resultflag=1;
		for(int i=0;i<al.size();i++){
			if(al.get(i).getCurhp()>0){
				resultflag=1;
				break;
			}else
				resultflag=0;
		}
		return resultflag;
	}
	//npc攻击后 判断玩家是否阵亡
	public int CheckUserWinLose(ArrayList<UserCard> ul){//攻击后检查是否胜
		int resultflag=1;
		for(int i=0;i<ul.size();i++){
			if(ul.get(i).getCurhp()>0){
				resultflag=1;
				break;
			}else
				resultflag=0;
		}
		return resultflag;
	}
	//npc群攻技能
	public int NpcAll(CMissionNpcDict npcl,ArrayList<UserCard> ul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag){
		int actflag=0;
		int atkval=0,hurt=0;
		UserCard uc;
		for(int i=0;i<ul.size();i++){
			uc=ul.get(i);
			Map<String,Object> mmm=new HashMap<String,Object>();
			if(uc.getCurhp()>0){
				actflag=1;
				atkval=CheckSkillHurt(npcl.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
				hurt=atkval-uc.getCurdef();
				if(hurt<=0) hurt=1;
				if(CheckControl(npcl.getNpcprop(),uc.getCardprop())>0){//本位有克制，伤害增加0.05倍
					hurt=(int)(hurt*1.05);
				}
				uc.setCurhp(uc.getCurhp()-hurt);
				if(uc.getCurhp()<=0) {
					uc.setCurhp(0);
					mmm.put("KE", 0);
				}else mmm.put("KE", uc.getCurhp());
				mmm.put("KA", "P");
				mmm.put("KB",uc.getCardid());
				mmm.put("KC", uc.getLocate());
				mmm.put("KD", hurt);
				mmm.put("KF", uc.getHp()+uc.getSameaddhp());
				mmm.put("KG", uc.getCardcode());
				mmm.put("KH", uc.getStarlevel());
				mmm.put("KI", uc.getCurstyle());
				//logger.info("npc群攻"+npcl.getNpcname()+"--->"+uc.getCardid()+"伤害："+hurt+"位置："+i);
				mlist.add(mmm);	
				actflag=CheckUserWinLose(ul);
				if(actflag<1) break;
			}
		}
		return actflag;
	}
	//玩家群攻技能
	public int PlayerAll(UserCard ucb,ArrayList<CMissionNpcDict> al,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag){
		int actflag=0;
		int atkval=0,hurt=0;
		CMissionNpcDict npcl;
		for(int i=0;i<al.size();i++){
			npcl=al.get(i);
			Map<String,Object> mmm=new HashMap<String,Object>();
			if(npcl.getCurhp()>0){
				actflag=1;
				atkval=CheckSkillHurt(ucb.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
				hurt=atkval-npcl.getCurdef();
				if(hurt<=0) hurt=1;
				if(CheckControl(ucb.getCardprop(),npcl.getNpcprop())>0){//本位有克制，伤害增加0.05倍
					hurt=(int)(hurt*1.05);
				}
				npcl.setCurhp(npcl.getCurhp()-hurt);
				if(npcl.getCurhp()<=0) {
					npcl.setCurhp(0);
					mmm.put("KE", 0);
				}else mmm.put("KE", npcl.getCurhp());
				mmm.put("KA", "N");
				mmm.put("KB", npcl.getNpcid());
				mmm.put("KC", npcl.getNpcloc());
				mmm.put("KD", hurt);
				mmm.put("KF", npcl.getAllhp());
				mmm.put("KG", npcl.getNpccode());
				if(!npcl.getNpctype().equals("杂碎")) mmm.put("KH", GameManager.getInstance().getCarddict().get(npcl.getNpccode()).getStarlevel());
				else mmm.put("KH", 1);
				mmm.put("KI", 1);
				//logger.info("玩家群攻"+ucb.getCardid()+"--->"+npcl.getNpcname()+"npc剩余血："+npcl.getCurhp()+"位置："+i);
				mlist.add(mmm);		
				actflag=CheckNpcWinLose(al);
				if(actflag<=0) break;
			}
		}
		
		return actflag;
	}
	//NPC技能单攻
	public int NpcSingle(ArrayList<CMissionNpcDict> al,ArrayList<UserCard> ul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag){
		int actflag=0;
		int locate=0,atkval=0,hurt=0;
		Map<String,Object> tm=new HashMap<String,Object>();
		locate=CheckNext(ul,al,locflag,0);//查找可攻击位置 从对位开始 1：npc位置 0：player位置  无位置胜？？？
		if(locate>=0){
			actflag=1;
			UserCard uc=ul.get(locate);
			CMissionNpcDict npcl=al.get(locflag);
			atkval=CheckSkillHurt(npcl.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
			hurt=atkval-uc.getCurdef();
			if(hurt<=0) hurt=1;	
			if(CheckControl(npcl.getNpcprop(),uc.getCardprop())>0){//本位有克制，伤害增加0.05倍
				hurt=(int)(hurt*1.05);
			}
			uc.setCurhp(uc.getCurhp()-hurt);
			if(uc.getCurhp()<=0) {
				uc.setCurhp(0);
				tm.put("KE", 0);
			}else tm.put("KE", uc.getCurhp());
			tm.put("KA", "P");
			tm.put("KB", uc.getCardid());
			tm.put("KC",uc.getLocate());
			tm.put("KD", hurt);
			tm.put("KF", uc.getHp()+uc.getSameaddhp());
			tm.put("KG", uc.getCardcode());
			tm.put("KH", uc.getStarlevel());
			tm.put("KI", uc.getCurstyle());
			//logger.info("npc单攻"+al.get(locflag).getNpcname()+"--->"+uc.getCardid()+"伤害："+hurt+"位置："+locate);
			mlist.add(tm);	
			actflag=CheckUserWinLose(ul);
		}
		return actflag;
	}
	//玩家技能弹弓
	public int PlayerSingle(ArrayList<UserCard> ul,ArrayList<CMissionNpcDict> al,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag){
		int actflag=0;
		int locate=0,atkval=0,hurt=0;
		Map<String,Object> tm=new HashMap<String,Object>();
		locate=CheckNext(ul,al,locflag,1);//查找可攻击位置 从对位开始 1：npc位置 0：player位置  无位置胜？？？
		if(locate>=0){
			UserCard uc=ul.get(locflag);
			CMissionNpcDict npcl=al.get(locate);
			actflag=1;
			atkval=CheckSkillHurt(uc.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
			hurt=atkval-npcl.getCurdef();
			if(hurt<=0) hurt=1;	
			if(CheckControl(uc.getCardprop(),npcl.getNpcprop())>0){//本位有克制，伤害增加0.05倍
				hurt=(int)(hurt*1.05);
			}
			npcl.setCurhp(npcl.getCurhp()-hurt);
			if(npcl.getCurhp()<=0) {
				npcl.setCurhp(0);
				tm.put("KE", 0);
			}else tm.put("KE", npcl.getCurhp());
			tm.put("KA", "N");
			tm.put("KB", npcl.getNpcid());
			tm.put("KC", npcl.getNpcloc());
			tm.put("KD", hurt);
			tm.put("KF", npcl.getAllhp());
			tm.put("KG", npcl.getNpccode());
			if(!npcl.getNpctype().equals("杂碎")) tm.put("KH", GameManager.getInstance().getCarddict().get(npcl.getNpccode()).getStarlevel());
			else tm.put("KH", 1);
			tm.put("KI", 1);
			//logger.info("玩家单攻"+ul.get(locflag).getCardid()+"--->"+npcl.getNpcname()+"npc剩余血："+npcl.getCurhp()+"位置："+locate);
			mlist.add(tm);		
			actflag=CheckNpcWinLose(al);
		}
		return actflag;
	}
	//NPC技能攻击列
	public int NpcActRect(CMissionNpcDict npcl,ArrayList<UserCard> ul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag){
		int actflag=0,winflag=1;
		int lineflag=0;
		int curloc=locflag;
		int atkval=0,hurt=0;
		int calflag=0;
		do{
			do{
				lineflag=lineflag+1;
				Map<String,Object> tm2=new HashMap<String,Object>();
				if(curloc<ul.size()){
					UserCard uc=ul.get(curloc);
					if(uc.getCurhp()>0){
						actflag=1;
						atkval=CheckSkillHurt(npcl.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
						hurt=atkval-uc.getCurdef();
						if(hurt<=0) hurt=1;	
						if(CheckControl(npcl.getNpcprop(),uc.getCardprop())>0){//本位有克制，伤害增加0.05倍
							hurt=(int)(hurt*1.05);
						}
						uc.setCurhp(uc.getCurhp()-hurt);
						if(uc.getCurhp()<=0) {
							uc.setCurhp(0);
							tm2.put("KE", 0);
						}else tm2.put("KE", uc.getCurhp());
						tm2.put("KA", "P");
						tm2.put("KB", uc.getCardid());
						tm2.put("KC", uc.getLocate());
						tm2.put("KD", hurt);
						tm2.put("KF", uc.getHp()+uc.getSameaddhp());
						tm2.put("KG", uc.getCardcode());
						tm2.put("KH", uc.getStarlevel());
						tm2.put("KI", uc.getCurstyle());
						//logger.info("npc攻列"+npcl.getNpcname()+"--->"+uc.getCardid()+"伤害："+hurt+"位置："+curloc);
						mlist.add(tm2);
						winflag=CheckUserWinLose(ul);
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
		return actflag;
	}
	//玩家技能攻击列
	public int ActRect(UserCard ucb,ArrayList<CMissionNpcDict> al,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist,int locflag){
		int actflag=0,winflag=1;
		int lineflag=0;
		int curloc=locflag;
		int atkval=0,hurt=0;
		int calflag=0;
		do{
			do{
				lineflag=lineflag+1;
				Map<String,Object> tm2=new HashMap<String,Object>();
				if(curloc<al.size()){
					CMissionNpcDict npcl=al.get(curloc);
					if(npcl.getCurhp()>0){
						actflag=1;
						atkval=CheckSkillHurt(ucb.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
						hurt=atkval-npcl.getCurdef();
						if(hurt<=0) hurt=1;	
						if(CheckControl(ucb.getCardprop(),npcl.getNpcprop())>0){//本位有克制，伤害增加0.05倍
							hurt=(int)(hurt*1.05);
						}
						npcl.setCurhp(npcl.getCurhp()-hurt);
						if(npcl.getCurhp()<=0) {
							npcl.setCurhp(0);
							tm2.put("KE", 0);
						}else tm2.put("KE", npcl.getCurhp());
						tm2.put("KA", "N");
						tm2.put("KB", npcl.getNpcid());
						tm2.put("KC", npcl.getNpcloc());
						tm2.put("KD", hurt);
						tm2.put("KF", npcl.getAllhp());
						tm2.put("KG", npcl.getNpccode());
						if(!npcl.getNpctype().equals("杂碎")) tm2.put("KH", GameManager.getInstance().getCarddict().get(npcl.getNpccode()).getStarlevel());
						else tm2.put("KH", 1);
						tm2.put("KI", 1);
						//logger.info("玩家攻列"+ucb.getCardid()+"--->"+npcl.getNpcname()+"伤害："+hurt+"位置："+curloc);
						mlist.add(tm2);	
						winflag=CheckNpcWinLose(al);
						if(winflag<=0)		break;
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
	//NPC前排攻击
	public int  NpcActLineFront(CMissionNpcDict npcl,ArrayList<UserCard> ul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist){
		UserCard uc;
		int hurt=0,atkval=0,actflag=-1;
		for(int i=0;i<3;i++){
			Map<String,Object> tm1=new HashMap<String,Object>();
			if(i<ul.size()){
				uc=ul.get(i);
				if(uc.getCurhp()>0){
					actflag=1;
					atkval=CheckSkillHurt(npcl.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
					hurt=atkval-uc.getCurdef();
					if(hurt<=0) hurt=1;
					if(CheckControl(npcl.getNpcprop(),uc.getCardprop())>0){//本位有克制，伤害增加0.05倍
						hurt=(int)(hurt*1.05);
					}
					uc.setCurhp(uc.getCurhp()-hurt);
					if(uc.getCurhp()<=0) {
						uc.setCurhp(0);
						tm1.put("KE", 0);
					}else tm1.put("KE", uc.getCurhp());
					tm1.put("KA", "P");
					tm1.put("KB", uc.getCardid());
					tm1.put("KC", uc.getLocate());
					tm1.put("KD", hurt);
					tm1.put("KF", uc.getHp()+uc.getSameaddhp());
					tm1.put("KG", uc.getCardcode());
					tm1.put("KH", uc.getStarlevel());
					tm1.put("KI", uc.getCurstyle());
					//logger.info("npc攻前排"+npcl.getNpcname()+"--->"+uc.getCardid()+"伤害："+hurt+"位置："+i);
					mlist.add(tm1);	
					actflag=CheckUserWinLose(ul);
					if(actflag<1) break;
				}
			}else break;
		}
		return actflag;
	}
	//玩家前排攻击
	public int  ActLineFront(UserCard ucb,ArrayList<CMissionNpcDict> al,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist){
		CMissionNpcDict npcl;
		int hurt=0,atkval=0,actflag=-1;
		for(int i=0;i<3;i++){
			Map<String,Object> tm1=new HashMap<String,Object>();
			if(i<al.size()){
				npcl=al.get(i);
				if(npcl.getCurhp()>0){
					actflag=1;
					atkval=CheckSkillHurt(ucb.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
					hurt=atkval-npcl.getCurdef();
					if(hurt<=0) hurt=1;
					if(CheckControl(ucb.getCardprop(),npcl.getNpcprop())>0){//本位有克制，伤害增加0.05倍
						hurt=(int)(hurt*1.05);
					}
					npcl.setCurhp(npcl.getCurhp()-hurt);
					if(npcl.getCurhp()<=0) {
						npcl.setCurhp(0);
						tm1.put("KE", 0);
					}else tm1.put("KE", npcl.getCurhp());
					tm1.put("KA", "N");
					tm1.put("KB", npcl.getNpcid());
					tm1.put("KC", npcl.getNpcloc());
					tm1.put("KD", hurt);
					tm1.put("KF", npcl.getAllhp());
					tm1.put("KG", npcl.getNpccode());
					if(!npcl.getNpctype().equals("杂碎")) tm1.put("KH", GameManager.getInstance().getCarddict().get(npcl.getNpccode()).getStarlevel());
					else tm1.put("KH", 1);
					tm1.put("KI", 1);
					//logger.info("玩家攻前排"+ucb.getCardid()+"--->"+npcl.getNpcname()+"npc剩余血："+npcl.getCurhp()+"位置："+i);
					mlist.add(tm1);	
					actflag=CheckNpcWinLose(al);
					if(actflag<1) break;
				}
			}else break;
		}
		return actflag;
	}
	//NPC后排攻击
	public int NpcLineBack(CMissionNpcDict npcl,ArrayList<UserCard> ul,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist){
		UserCard uc;
		int hurt=0,atkval=0,actflag=-1;
		for(int i=3;i<6;i++){
			Map<String,Object> tm1=new HashMap<String,Object>();
			if(i<ul.size()){
				uc=ul.get(i);
				if(uc.getCurhp()>0){
					actflag=1;
					atkval=CheckSkillHurt(npcl.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
					hurt=atkval-uc.getCurdef();
					if(hurt<=0) hurt=1;
					if(CheckControl(npcl.getNpcprop(),uc.getCardprop())>0){//本位有克制，伤害增加0.05倍
						hurt=(int)(hurt*1.05);
					}
					uc.setCurhp(uc.getCurhp()-hurt);
					if(uc.getCurhp()<=0) {
						uc.setCurhp(0);
						tm1.put("KE", 0);
					}else tm1.put("KE", uc.getCurhp());
					tm1.put("KA", "P");
					tm1.put("KB", uc.getCardid());
					tm1.put("KC", uc.getLocate());
					tm1.put("KD", hurt);
					tm1.put("KF", uc.getHp()+uc.getSameaddhp());
					tm1.put("KG", uc.getCardcode());
					tm1.put("KH", uc.getStarlevel());
					tm1.put("KI", uc.getCurstyle());
				//	logger.info("npc攻后排"+npcl.getNpcname()+"--->"+uc.getCardid()+"伤害："+hurt+"位置："+i);
					mlist.add(tm1);	
					actflag=CheckUserWinLose(ul);
					if(actflag<1) break;
				}
			}else break;
		}
		return actflag;
	}
	//玩家后排攻击
	public int ActLineBack(UserCard ucb,ArrayList<CMissionNpcDict> al,String skillcode,int skillgrade,ArrayList<Map<String,Object>> mlist){
		CMissionNpcDict npcl;
		int hurt=0,atkval=0,actflag=-1;
		for(int i=3;i<6;i++){
			Map<String,Object> tm1=new HashMap<String,Object>();
			if(i<al.size()){
				npcl=al.get(i);
				if(npcl.getCurhp()>0){
					actflag=1;
					atkval=CheckSkillHurt(ucb.getCuratk(),skillcode,skillgrade);//计算技能发动后的攻击力
					hurt=atkval-npcl.getCurdef();
					if(hurt<=0) hurt=1;
					if(CheckControl(ucb.getCardprop(),npcl.getNpcprop())>0){//本位有克制，伤害增加0.05倍
						hurt=(int)(hurt*1.05);
					}
					npcl.setCurhp(npcl.getCurhp()-hurt);
					if(npcl.getCurhp()<=0) {
						npcl.setCurhp(0);
						tm1.put("KE", 0);
					}else tm1.put("KE", npcl.getCurhp());
					tm1.put("KA", "N");
					tm1.put("KB", npcl.getNpcid());
					tm1.put("KC",npcl.getNpcloc());
					tm1.put("KD", hurt);
					tm1.put("KF", npcl.getAllhp());
					tm1.put("KG", npcl.getNpccode());
					if(!npcl.getNpctype().equals("杂碎")) tm1.put("KH", GameManager.getInstance().getCarddict().get(npcl.getNpccode()).getStarlevel());
					else tm1.put("KH", 1);
					tm1.put("KI", 1);
					//logger.info("玩家攻后排"+ucb.getCardid()+"--->"+npcl.getNpcname()+"npc淤血："+npcl.getCurhp()+"位置："+i);
					mlist.add(tm1);		
					actflag=CheckNpcWinLose(al);
					if(actflag<1) break;
				}
			}else break;
		}
		return actflag;
	}
	//对位攻击时返回可攻击的位置
	public int CheckNext(ArrayList<UserCard> ul,ArrayList<CMissionNpcDict> al,int curloc,int seq){
		int k=-1;
		int stopflag=-1;
		int locflag=curloc;
		stopflag=curloc;
		if(seq==1){//发起方玩家
			do{
				if(locflag<al.size()){
					if(al.get(locflag).getCurhp()>0){
						k=locflag;
						break;
					}else locflag=locflag+1;
				}else{
					locflag=0;
				}	
			}while(stopflag!=locflag);
		}else{
			do{
				if(locflag<ul.size()){
					if(ul.get(locflag).getCurhp()>0){
						k=locflag;
						break;
					}else locflag=locflag+1;
				}else{
					locflag=0;
				}	
			}while(stopflag!=locflag);
		}		
		return k;
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
	//街霸
	public void RunSteetMission(User p,UserStreet us,Map<String, Object> sendMap) throws SQLException{
		ArrayList<CMissionNpcDict> al=new ArrayList<CMissionNpcDict>();
		for(int i=0;i<us.getSnpclist().size();i++){
			al.add(us.getSnpclist().get(i));
		}
		//logger.info("Npc数量："+al.size());
		for(int i=0;i<al.size();i++){
			CMissionNpcDict mnpc =al.get(i);
			int trand=0;
			//int trand=CommTools.randomval((0-mnpc.getHpalter()),mnpc.getHpalter());//随机变更血攻防
			mnpc.setCurhp(mnpc.getHP()+trand);
			mnpc.setAllhp(mnpc.getCurhp());
			//trand=CommTools.randomval((0-mnpc.getAtkalter()),mnpc.getAtkalter());
			mnpc.setCuratk(mnpc.getATK()+trand);
			mnpc.setAllatk(mnpc.getCuratk());
			//CommTools.randomval((0-mnpc.getDefalter()),mnpc.getDefalter());
			mnpc.setCurdef(mnpc.getDEF()+trand);
			mnpc.setAlldef(mnpc.getCurdef());
			//logger.info("npc序号："+mnpc.getNpccode()+"++血："+mnpc.getCurhp()+"+++攻："+mnpc.getCuratk()+"++++防"+mnpc.getCurdef());
		}
		ArrayList<UserCard> ul=new ArrayList<UserCard>();
		for(int i=0;i<p.getCardlocid().length;i++){ //战斗阵容
			if(p.getCardlocid()[i]<=0) break;
			UserCard uu=p.getCardlist().get(p.getCardlocid()[i]);
			uu.setCurhp(uu.getHp()+uu.getSameaddhp());
			uu.setCuratk(uu.getAtk()+uu.getSameaddatk());
			uu.setCurdef(uu.getDef()+uu.getSameadddef());
			ul.add(uu);
			//logger.info("玩家卡牌代码："+uu.getCardcode()+"++血："+uu.getCurhp()+"+++攻："+uu.getCuratk()+"++++防"+uu.getCurdef());
		}
		int winloseflag=-1;
		try{
			winloseflag=StartBattle(ul,al,sendMap);//开始战斗逻辑运算
			sendMap.put("AA", us.getStreetid());
			sendMap.put("AB", winloseflag);
			int doubleflag=1;
			sendMap.put("AD", 0);
			sendMap.put("AR", 0);
			if(winloseflag>0){
				if(us.getIfgold()>0) doubleflag=2;//重置后经验钞票翻倍
				sendMap.put("AD", us.getGiveexp()*doubleflag);
				sendMap.put("AR", us.getGivemoeny()*doubleflag);
				us.setIflock(1);//胜利后此关当日不可再打
			}
			sendMap.put("AE", -1);
			sendMap.put("AF", "@");
			FinishStreetBattle(winloseflag,p,us,sendMap,ul);
			sendMap.put("AS",p.getLevel());
			sendMap.put("AT",p.getPower());
			sendMap.put("AV", p.getExp());//用户当前经验
			sendMap.put("AU", p.getLevelup());//用户升级经验
			sendMap.put("AW", p.getMembers());
			String enyname="";//任务对手名字
			String tempstr="杂碎";
			for(int i=0;i<al.size();i++){
				CMissionNpcDict mnpc=al.get(i);
				if(mnpc.getNpctype().equals("BOSS")){
					enyname=mnpc.getNpcname();
					break;
				}else if(mnpc.getNpctype().equals("精英")){
					if(tempstr.equals("杂碎")){
						enyname=mnpc.getNpcname();
					}
					
				}else{
					if(tempstr.equals("杂碎")){
						enyname=mnpc.getNpcname();
					}
				}
			}
			sendMap.put("AX", enyname);
			sendMap.put("AJ", p.getBall());
			GetMemberMap(ul,al,sendMap);//双方阵容
			ArrayList<String> sklist = new ArrayList<String>();
			for(int i=0;i<this.actskill.size();i++){
				int kkk=0;
				for(int j=0;j<sklist.size();j++){
					if(this.actskill.get(i).equals(sklist.get(j)))
						kkk=1;
				}
				if(kkk==0)	sklist.add(this.actskill.get(i));
			}
			sendMap.put("AK",sklist.toArray());
		}catch(SQLException e){
			throw e;
		}		
	}
	//挑战街霸战斗结束，计算收益
	public void FinishStreetBattle(int wlflag,User p,UserStreet us,Map<String,Object> smap,ArrayList<UserCard> ul) throws SQLException{
		int flag=-1,sumval=0,doubleflag=1;
		int[] a =new int[3];
		if(wlflag>0){//玩家胜
			a[0]=us.getMarks();
			//logger.info("mark："+a[0]);
			if(us.getIfgold()>0){//重置过增加掉落物品
				a[1]=us.getGoods1();
				a[2]=us.getGoods2();
				doubleflag=2;
			}else{
				a[1]=0;
				a[2]=0;
				doubleflag=1;
			}			
			int[] loc={0,1,2};
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
			for(int i=0;i<a.length;i++){
				sumval=sumval+a[i];
			}
		    int maxval=100000-sumval;
		    int minval=0;
			Random rd = new Random();
			int k=rd.nextInt(100000);
			for(int i=0;i<a.length;i++){
				minval=maxval;
				if(a[i]==0) continue;
				maxval=maxval+a[i];
				if(k>=minval && k<maxval){
					flag=loc[i];
					break;
				}
			}			
			flag=flag+1;
			smap.put("AC", "@");
			//logger.info("掉落物品标示号："+flag);
		}
		GameServerDao dao = new GameServerDao();
		try {
			dao.FinishSBattle(p, us, flag, wlflag,smap,doubleflag);
		} catch (SQLException e) {
			throw e;
		}
	}	
}
