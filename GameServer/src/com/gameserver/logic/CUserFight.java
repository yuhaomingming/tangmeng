package com.gameserver.logic;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.gameserver.comm.CommTools;
import com.gameserver.dao.GameServerDao;
import com.gameserver.manager.GameManager;
import com.gameserver.model.CMissionNpcDict;
import com.gameserver.model.Pkexpmoney;
import com.gameserver.model.SkillPages;
import com.gameserver.model.User;
import com.gameserver.model.UserCard;
import com.gameserver.model.UserSkill;

public class CUserFight implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7316678545111847903L;


	//玩家  ，决斗对方角色名，对方类型npc或玩家 , 对方排名， 包  对方等级
	public void ActFight(User p,String rolename,int ftype,int rankseq,Map<String,Object> smap,int otherlevel)throws SQLException{
		ArrayList<UserCard> ul=new ArrayList<UserCard>();
		ArrayList<UserCard> enul=new ArrayList<UserCard>();//对方阵容
		ArrayList<CMissionNpcDict> al=new ArrayList<CMissionNpcDict>();
		for(int i=0;i<6;i++){ //自己战斗阵容
			if(p.getCardlocid()[i]<=0) break;
			UserCard uu=p.getCardlist().get(p.getCardlocid()[i]);
			uu.setCurhp(uu.getHp()+uu.getSameaddhp());
			uu.setCuratk(uu.getAtk()+uu.getSameaddatk());
			uu.setCurdef(uu.getDef()+uu.getSameadddef());
			ul.add(uu);
		}
		int winloseflag=-1;
		try{
			if(ftype>0){//对方玩家
				UPkLogic ulogic=new UPkLogic();
				ulogic.GetEnCardList(rolename, p.getSvrcode(), enul);
				winloseflag=ulogic.StartBattle(ul,enul,smap);
				GetPMemberMap(ul,enul,smap,ulogic.getActskill());//双方阵容
				ArrayList<String> sklist = new ArrayList<String>();
				for(int i=0;i<ulogic.getActskill().size();i++){
					int kkk=0;
					for(int j=0;j<sklist.size();j++){
						if(ulogic.getActskill().get(i).equals(sklist.get(j)))
							kkk=1;
					}
					if(kkk==0)	sklist.add(ulogic.getActskill().get(i));
				}
				smap.put("AK",sklist.toArray());
			}else{//对方NPC
				MissionLogic mlogic= new MissionLogic();
				GameServerDao dao =new GameServerDao();
				dao.getNpcList( rolename, al);
				for(int i=0;i<al.size();i++){
					CMissionNpcDict mnpc=al.get(i);
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
				}
				GetNMemberMap(ul,al,smap,mlogic.getActskill());//双方阵容
				winloseflag=mlogic.StartBattle(ul,al,smap);//开始战斗逻辑运算
				ArrayList<String> sklist = new ArrayList<String>();
				for(int i=0;i<mlogic.getActskill().size();i++){
					int kkk=0;
					for(int j=0;j<sklist.size();j++){
						if(mlogic.getActskill().get(i).equals(sklist.get(j)))
							kkk=1;
					}
					if(kkk==0)	sklist.add(mlogic.getActskill().get(i));
				}
				smap.put("AK",sklist.toArray());
			}
			smap.put("AA", -1);
			smap.put("AB", winloseflag);
			int pkexp=0;
			int pkmoney=0;
			if(winloseflag==0){
				smap.put("AD", 0);//经验=等级*10-20 钱=（等级*10-20）*10	不扣=钱/1.5
				smap.put("AR",0);
				smap.put("AC",p.getRankseq());
			}else
			{
				Pkexpmoney pkem=GameManager.getInstance().getPkexpmon().get(p.getLevel());
				smap.put("AD", pkem.getExp());
				smap.put("AR",pkem.getMoney());
				pkexp=pkem.getExp();
				pkmoney=pkem.getMoney();
				smap.put("AC", rankseq);//新排名
			}
			smap.put("AE", -1);
			smap.put("AF", "@");
			smap.put("AG", "@");
			GameServerDao dao = new GameServerDao();
			dao.FinishFight(winloseflag,p,rankseq,rolename,ftype,pkexp,pkmoney,smap,otherlevel);
			smap.put("AS",p.getLevel());
			smap.put("AT",p.getPknum());
			smap.put("AV", p.getExp());//用户当前经验
			smap.put("AU", p.getLevelup());//用户升级经验
			smap.put("AW", p.getMembers());
			smap.put("AX", rolename);
			smap.put("AJ", p.getBall());
		}catch(SQLException e){
			throw e;
		}		
		
	}
	public void GetPMemberMap(ArrayList<UserCard> ul,ArrayList<UserCard> enul,Map<String,Object> Map,ArrayList<String> sklist){
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
					sklist.add(skname);
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
					sklist.add(skname);
					skmap.add(skillmap);
				}
			}
			tempmap.put("ED", skmap.toArray());
			encardmember.add(tempmap);
		}
		Map.put("AM", encardmember.toArray());
	}
	public void GetNMemberMap(ArrayList<UserCard> ul,ArrayList<CMissionNpcDict> al,Map<String,Object> Map,ArrayList<String> sklist){
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
					sklist.add(skname);
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
					sklist.add(skname);
					skmap.add(skillmap);
				}
			}
			tempmap.put("ED", skmap.toArray());
			npcmember.add(tempmap);
		}
		Map.put("AM", npcmember.toArray());
	}

	
}
