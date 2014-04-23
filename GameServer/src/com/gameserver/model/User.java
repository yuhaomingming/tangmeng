package com.gameserver.model;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.gameserver.comm.CommTools;
import com.gameserver.comm.PacketCommandType;
import com.gameserver.dao.GameServerDao;
import com.gameserver.logic.CUserFight;
import com.gameserver.logic.CardLogicProc;
import com.gameserver.logic.MissionLogic;
import com.gameserver.logic.UPkLogic;
import com.gameserver.manager.GameManager;
import com.gameserver.server.HeartBeatTask;

public class User implements Serializable{
	private static final long serialVersionUID = -6879543045402576191L;
	private static final Logger logger = Logger.getLogger(User.class.getName());
	private String uname;//玩家昵称
	private String urole;//玩家角色名
	private String svrcode;//服务器编码
	private int channelid;
	private final Channel channel;
	private String firstlogin; //首次登录时间
	private String Currlogin; //最近登录
	private int level; //等级
	private int exp; //经验
	private int power; //当前体力值
	private int maxpower;//体力值上限
	private int members; //	队伍上限	
	private int powerstart;//体力计时
	private int perpoint; //每点恢复时间 秒级时间戳
	private int levelup; //升级经验值
	private int viplevel; //vip等级
	private int money; //钞票
	private int gold; //金条
	private int innercoin; //修改为是否领取连续登陆奖
	private int god; //女神泪
	private int ball; //转生丹
	private int goldrenew; //金条恢复次数
	private int renewgold;//金条恢复价格
	private int getbook; //夺书次数
	private int goldaddnum;//金条补充次数
	private int addnumgold;//补充价格
	private int trade; //新手引导标记 1完成
	private int continuelog;//连续登录天数
	private int uhp; //总血量
	private int uatk; //总攻击
	private int udef; //总防御
	private int addvalue; //补正百分比
	private int undotask; //未完成任务
	private int curmission; //当前关卡id
	private int missionsum;//关卡任务次数
	private int pknum;//玩家pk次数
	private int getskstart;//夺技恢复计时
	private int everyskp;//夺技每点恢复时间 秒级时间戳
	private int rankseq;//争斗排名
	private int curpoints;//争斗积分
	private int friendsum;//好友上限
	private int drinknum;//剩余畅饮次数
	private int poweradd;//畅饮补充值
	private String lastvisit;//上次参拜日期
	private int visitnum;//已参拜次数
	private int visitlevle;//参拜等级
	private int buyfight;//购买争斗次数
	private int buylimit;//购买次数上限
	private int goldtree;//当日摇钱次数
	private int maxgoldtree;//最多摇钱次数
	private int treegold;//摇钱消耗金条数
	private int resetstreet;
	private int resetshop;
	private int curstreetid;
	private int curdesid;
	private int heartnum;//心跳计数
	private int curmessid;//最新消息id
	private String markcode;
	private String platcode;
	private int chargenum;//充值次数
	private int awardnum;//摇奖次数
	private int awardgold;//摇奖单价
	private int bosscdval;//世界boss CD开始计时
	private int taskcdval;//连闯任务 CD开始计时
	private int blessnum;//当日已祈愿次数
	private int blessall;//当日总次数
	private int autoboss;//自动挑战boss
	private int ifgetgold;//是否领取排名金币奖励
	private int ifgetbox;//是否领取排名钥匙奖励
	private int[] rankaward={0,0};//奖励数组 0 金币 1金钥匙
	private int advps;
	private int stdps;
	private int friendpknum;//好友pk次数
//	private int beatboss;//攻击boss次数
	
	private Map<Integer,UserCard> cardlist;//用户卡牌列表 <卡牌id，卡牌>
	private Map<Integer,UserCard> inusecards; //阵容卡牌列表 <卡牌id，卡牌>
	private Map<String,SkillPages> skillpagelist; //技能书页<技能编码，技能书页信息>
	private Map<Integer,UserDevice> udevicelist; //背包装备列表
	private Map<Integer,UserSkill> uskilllist;  // 背包技能列表
	private Map<Integer,UserMark> umarklist; // 背包符文列表
	private Map<String,Integer> umarksnumlist; // 可用符文数量列表
	private Map<Integer,UserMission> umisson;//用户任务列表<关卡id，用户任务>
	private Map<Integer,UserStreet> ustreet;//用户街霸挑战列表<街霸id，街霸>
	private Map<String,UserEgg> useregg;//用户扭蛋类
	private Map<Integer,ArrayList<UserMission>> scemisson;//用户任务关卡列表<场景id，用户任务>
	private ArrayList<ShopClass> usershop;//用户商城
	private Map<String,UserProps> userprop;//用户道具背包<道具code，道具>
	private ArrayList<UserDayTask> userdaytasklist; //用户日常任务列表
	private Map<Integer,UserMailBox> usermailbox;//用户邮箱<邮件id，邮件>
	private Map<Integer,UserLuckAward> userluckaward; //<位置，奖品>
	private Map<Integer,UserLuckAward> userluckawardrand;//<概率等级，奖品类>
	private Map<String,Integer>usergetallmap;//<系列，是否集齐>
	private ArrayList<CUserChip> userchips;//用户碎片背包
	//private Map<String,>
	private int[] cardlocid={0,0,0,0,0,0}; //阵容卡牌位置 ，记录卡牌ID 0表示空无卡牌     共6个位置
	private Date heartTime;//心跳包时间戳
	private Timer timer;//心跳包任务
	private static String[] markcodestring={"aaa","aab","aaf","aah","aam","aan","aat","aaw","aay","abc","abf","abg","abl","abn","abv","acd","acp","acy","adi","caa","cac","cah","aba","abh","act","acz","abx","adj","abp","acr"};
	//新建角色
	public User(Map<String, Object> _cmap, Channel ch,int flag){
		this.channel = ch;		
		this.uname=_cmap.get("UN").toString();
		/*String headerstr=this.uname.substring(0, 3);
		for(int k=0;k<markcodestring.length;k++){
			if (markcodestring[k].equals(headerstr)){
				this.uname=this.uname.substring(3);
				break;
			}
		}*/
		//logger.info("_camp:"+_cmap.toString());
		this.urole=_cmap.get("RN").toString();
		this.svrcode=_cmap.get("SN").toString();
		this.uhp=0;
		this.uatk=0;
		this.udef=0;
		this.undotask=0;
		this.heartnum=0;
		this.rankseq=0;
		this.useregg= new HashMap<String,UserEgg>();
		this.cardlist = new HashMap<Integer,UserCard>();
		this.inusecards = new HashMap<Integer,UserCard>();
		this.skillpagelist = new HashMap<String,SkillPages>();
		this.udevicelist = new HashMap<Integer,UserDevice>();
		this.uskilllist = new HashMap<Integer,UserSkill>();
		this.umarklist = new HashMap<Integer,UserMark>();
		this.umarksnumlist = new HashMap<String,Integer>();
		this.umisson=new HashMap<Integer,UserMission>();
		this.scemisson = new HashMap<Integer,ArrayList<UserMission>>();
		this.usershop= new ArrayList<ShopClass>();
		this.ustreet=new HashMap<Integer,UserStreet>();
		this.userprop=new HashMap<String,UserProps>();
		this.userdaytasklist = new ArrayList<UserDayTask>();
		this.usermailbox= new HashMap<Integer,UserMailBox>();
		this.userluckaward=new HashMap<Integer,UserLuckAward>();
		this.userluckawardrand =new HashMap<Integer,UserLuckAward>();
		this.usergetallmap = new HashMap<String,Integer>();
		this.userchips= new ArrayList<CUserChip>();
		this.awardnum=0;
		this.awardgold=200;
		this.ifgetbox=-1;
		this.ifgetgold=-1;
		this.heartTime = new Date();
		this.timer= new Timer();
		this.timer.schedule(new HeartBeatTask(this), 15000, 15000);//启动心跳包任务
		if(flag>0){SetUser(uname,urole,svrcode);}
	}
	/**
	 * 
	 * 心跳包，检查玩家是否断线
	 */
	public void checkPlayerDiconnStatus() {	
		this.heartnum=this.heartnum+1;//每15秒计数一次
		if ((int)(new Date().getTime() - this.getHeartTime().getTime()) > 30000) {//检查玩家是否断线
			if(this.getTimer()!=null){
				this.getTimer().cancel();
				this.timer=null;
			}
			//logger.info("玩家心跳断线"+this.urole);
			this.channel.disconnect();//断开通讯连接
		}else{
			long cursecond=System.currentTimeMillis()/1000;
			if(this.powerstart>0 && (int)cursecond-this.powerstart>=this.perpoint){//恢复体力
				if(this.power>=this.maxpower){
					this.powerstart=0;
				}else{
					this.power=this.power+1;
					this.powerstart=(int)cursecond;
				}
				
			}
			if(this.getskstart>0 && (int)cursecond-this.getskstart>=this.everyskp){//恢复夺技次数
				if(this.getbook>=10){
					this.getskstart=0;
				}else{
					this.getbook=this.getbook+1;
					this.getskstart=(int)cursecond;
				}
			}
			if(this.heartnum>=10&&this.heartnum%10>=0){//3分钟
				this.heartnum=1;
				this.GetSysMessage();				
			}
		}
	}
	//接收系统消息
	private void GetSysMessage(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		int k=0;
		try
		{
			GameServerDao dao = new GameServerDao();
			k=dao.getsysmessage(this,sendMap);
			if(k==1){//封号消息
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1098");
				this.channel.disconnect();
			}else if(k==2){//发送滚动通知消息
				sendMap.put("CMD",PacketCommandType.PUBLICMESS);
				this.send(sendMap);
			}
		}
		catch (SQLException e) {
			/*sendMap= new HashMap<String,Object>();
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据异常
			this.send(sendMap);*/
			e.printStackTrace();
		}
	}
	//
	public void getusermess(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		ArrayList<Map<String,Object>> messlist=new ArrayList<Map<String,Object>>();
		Iterator<messageClass> it = GameManager.getInstance().getLogmessdict().values().iterator();
		while(it.hasNext()){
			Map<String,Object> messmap=new HashMap<String,Object>();
			messageClass mclass = it.next();
			if(mclass.getExptime()>0){
				messmap.put("MB", mclass.getMessid());
				messmap.put("MC", mclass.getMesscont());
				messmap.put("MD", mclass.getMesstitle());
				messlist.add(messmap);
			}
		}
		Collections.sort(messlist,new Comparator<Map<String,Object>>(){
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
				return Integer.parseInt(arg1.get("MB").toString())-Integer.parseInt(arg0.get("MB").toString()); }  

		});
		sendMap.put("MA", messlist.toArray());
		sendMap.put("CMD", PacketCommandType.RETURNMESS);
		this.send(sendMap);
	}
	//创建角色
	public void CheckRole(String cname,String mcode,String pcode){
		Map<String,Object> smap ;
		try
		{
			GameServerDao dao = new GameServerDao();
			dao.CreateUser(this,this.uname,this.urole,this.svrcode,cname,mcode,pcode);
			dao.getPlayerInfo(this, uname,this.urole,this.svrcode);
			this.ReturnLogin();
		}
		catch (SQLException e) {
			//logger.info("异常");
			smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1002"); //角色重复
			this.send(smap);
			e.printStackTrace();
		}
	}
	public void SetUser(String uname,String urole,String svrcode){
		GameServerDao dao = new GameServerDao();
		Map<String,Object> smap ;
		try {
			
			dao.getPlayerInfo(this, uname,urole,svrcode);
		} catch (SQLException e) {
			smap= new HashMap<String,Object>();
			if(e.getMessage().equals("banduser")){
				smap.put("CMD", PacketCommandType.GETERROR);
				smap.put("EC", "1098"); //账号封停
			}else{
				smap.put("CMD", PacketCommandType.GETERROR);
				smap.put("EC", "1000"); //连接异常
			}
			this.send(smap);
			this.channel.disconnect();
			e.printStackTrace();
		}
	}
	//登录成功返回
	public void ReturnLogin(){
		ArrayList<Map<String, Object>> ucardlist; //阵容队列
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD", PacketCommandType.LOGINSUCCESS);
		sendMap.put("AA", this.getUname());
		sendMap.put("AB", this.getUrole());
		sendMap.put("AC", this.getSvrcode());
		sendMap.put("AD", this.getFirstlogin());
		sendMap.put("AE", this.getCurrlogin());
		sendMap.put("AF", this.getLevel());
		sendMap.put("AG", this.getExp());
		sendMap.put("AH", this.getPower());
		sendMap.put("AI", this.getMaxpower());
		sendMap.put("AJ", this.getMembers());
		sendMap.put("AK", this.getPowerstart());
		sendMap.put("AL", this.getPerpoint());
		sendMap.put("AM", this.getLevelup());
		sendMap.put("AN", this.getViplevel());
		int currtime = (int)(System.currentTimeMillis()/1000);
		sendMap.put("TT", currtime);
		if(this.getBosscdval() > 0 ){
			if (currtime - this.getBosscdval() >= Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AJ").toString())){
				this.setBosscdval(0);
				sendMap.put("CA", 0);	
			}else	
				sendMap.put("CA", this.getBosscdval()+Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AJ").toString()));
		}else sendMap.put("CA", 0);	
		if(this.getTaskcdval() > 0 ){
			if (currtime - this.getTaskcdval() >= Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AK").toString())){
				this.setTaskcdval(0);
				sendMap.put("CB",0);
			}else	
				sendMap.put("CB", this.getTaskcdval()+Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AK").toString()));
		}else sendMap.put("CB",0);
		sendMap.put("CC", this.getAutoboss());//自动挑战boss
		sendMap.put("CD", this.blessnum);
		sendMap.put("CE", this.blessall);
		sendMap.put("CF", 10);//祈愿费用
		sendMap.put("CG",this.advps);//剩余高级美容次数
		sendMap.put("CI", this.stdps);// 剩余普通美容次数
		sendMap.put("CH", this.friendpknum);// 好友pk次数
		int vpgold=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AI").toString())-this.chargenum;
		sendMap.put("VP", vpgold);
		sendMap.put("AO", this.getMoney());
		sendMap.put("AP", this.getGold());
		sendMap.put("AS", this.getBall());
		sendMap.put("AQ",Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AG").toString()));//购买体力上限	
		this.buylimit=Integer.parseInt(sendMap.get("AQ").toString());
		this.treegold=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.goldtree+1).get("AA").toString());//摇钱费用
		this.maxgoldtree=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AB").toString());//最大摇钱次数
		sendMap.put("XE", this.maxgoldtree);//最大摇钱次数
		sendMap.put("XF", this.goldtree);//已摇次数
		sendMap.put("XG", this.treegold);//摇钱费用
		sendMap.put("XH",this.resetstreet);
		sendMap.put("XI",this.resetshop);
		sendMap.put("XJ",this.curstreetid);
		sendMap.put("XK",this.curdesid);
		if(this.goldrenew<3) sendMap.put("XC", 20);//购买体力
		if(this.goldrenew>=3&&this.goldrenew<6) sendMap.put("XC", 40);
		if(this.goldrenew>=6&&this.goldrenew<9) sendMap.put("XC", 80);
		if(this.goldrenew>=9) sendMap.put("XC", 200);
		if(this.buyfight<3) sendMap.put("AR", 20);//购买争斗点
		if(this.buyfight>=3&&this.buyfight<6) sendMap.put("AR", 40);
		if(this.buyfight>=6&&this.buyfight<9) sendMap.put("AR", 80);
		if(this.buyfight>=9) sendMap.put("AR", 200);
		if(this.goldaddnum<3) sendMap.put("XB", 20);//购买夺计
		if(this.goldaddnum>=3&&this.goldaddnum<6) sendMap.put("XB", 40);
		if(this.goldaddnum>=6&&this.goldaddnum<9) sendMap.put("XB", 80);
		if(this.goldaddnum>=9) sendMap.put("XB", 200);
		sendMap.put("AT", this.goldrenew);
		sendMap.put("AV", this.goldaddnum);
		sendMap.put("AU", this.getbook);
		sendMap.put("XD", this.buyfight);
		sendMap.put("XA", this.pknum);
		sendMap.put("AW", this.getTrade());
		sendMap.put("AX", this.getContinuelog());//连续登录天数
		CardLogicProc clp=new CardLogicProc();
		clp.checktype(this);
		ucardlist= new  ArrayList<Map<String, Object>>();
		for(int i=0;i<this.cardlocid.length;i++){
			if(this.cardlocid[i]<=0) break;
			UserCard uc=this.cardlist.get(this.cardlocid[i]);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("BA", uc.getCardid());
			map.put("BB", uc.getCardname());
			map.put("BC", uc.getCarddesc());
			map.put("DD", uc.getCardcode());
			map.put("BD", uc.getCardprop());
			map.put("BE", uc.getStarlevel());
			map.put("BF", uc.getCardtype());
			map.put("BG", uc.getUpexp());
			map.put("BH", uc.getCurexp());
			map.put("BW",uc.getCardlevel());
			map.put("BI", uc.getCurvalue());
			map.put("BJ", uc.getHp()+uc.getSameaddhp());
			map.put("BK", uc.getAtk()+uc.getSameaddatk());
			map.put("BL", uc.getDef()+uc.getSameadddef());
			map.put("BM", uc.getCurstyle());
			map.put("BN", uc.getIfuse());
			map.put("BO", uc.getLocate());
			map.put("BS",uc.getSameflag());
			map.put("CA",uc.getAdd_hp());
			map.put("CB",uc.getAdd_atk());
			map.put("CC",uc.getAdd_def());
			map.put("CD", uc.getLifeskill());//天赋技能，无=@
			map.put("CE", uc.getCurpointval());//当前魅力点
			ucardlist.add(map);			
		}		
		sendMap.put("BT", this.addvalue);
		sendMap.put("AZ", ucardlist.toArray());
		sendMap.put("BP", this.uhp);
		sendMap.put("BQ", this.uatk);
		sendMap.put("BR", this.udef);
		sendMap.put("BU", this.undotask);
		sendMap.put("BX", this.friendsum);
		sendMap.put("XL", this.rankseq);
		sendMap.put("XM", this.chargenum);
		sendMap.put("XN", Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AE").toString()));//争斗
		sendMap.put("XO", Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AH").toString()));//夺计
		sendMap.put("XP", this.innercoin);//修改为是否领取连续登陆奖
		ArrayList<Map<String,Object>> messlist=new ArrayList<Map<String,Object>>();
		Iterator<messageClass> it = GameManager.getInstance().getLogmessdict().values().iterator();
		while(it.hasNext()){
			Map<String,Object> messmap=new HashMap<String,Object>();
			messageClass mclass = it.next();
			if(mclass.getExptime()>0){
				messmap.put("MB", mclass.getMessid());
				messmap.put("MC", mclass.getMesscont());
				messmap.put("MD", mclass.getMesstitle());
				messlist.add(messmap);
			}
		}
		Collections.sort(messlist,new Comparator<Map<String,Object>>(){
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
				return Integer.parseInt(arg1.get("MB").toString())-Integer.parseInt(arg0.get("MB").toString()); }  

		});
		sendMap.put("MA", messlist.toArray());	
		this.send(sendMap);
}
	
	//卡牌更换
	public void AlterCard(Map<String, Object> _umap){
		ArrayList<Map<String, Object>> ucardlist; //阵容队列
		int ocardid=Integer.parseInt(_umap.get("AA").toString());//待换卡牌id，如空位为0
		int tcardid=Integer.parseInt(_umap.get("BB").toString());//更换卡牌id
		int flag=Integer.parseInt(_umap.get("CC").toString());//空位位置 未放卡牌是记录位置值  放卡牌后此值0
		CardLogicProc clogic = new CardLogicProc();
		try {
			if(flag>=6||flag<0) {
				logger.info("card locate error"+flag);
				throw new Exception("card locate error");
			}
			clogic.AlterUserCard(ocardid,tcardid,this,flag);
			//通知用户补正增量、总血攻防值、补正百分比
			Map<String,Object> sendMap = new HashMap<String,Object>();
			sendMap.put("CMD", PacketCommandType.RETURNALTER);
			sendMap.put("WA", this.getUhp());
			sendMap.put("WB", this.getUatk());
			sendMap.put("WC", this.getUdef());
			sendMap.put("WD", this.getAddvalue());
			sendMap.put("WM", tcardid);
			ucardlist= new  ArrayList<Map<String, Object>>();
			for(int i=0;i<this.cardlocid.length;i++){
				if(this.cardlocid[i]<=0) break;
				UserCard uc=this.cardlist.get(this.cardlocid[i]);
				Map<String,Object> map = new HashMap<String,Object>();
				//logger.info("uc id："+uc.getCardid());
				map.put("BA", uc.getCardid());
				map.put("BB", uc.getCardname());
				map.put("BC", uc.getCarddesc());
				map.put("DD", uc.getCardcode());
				map.put("BD", uc.getCardprop());
				map.put("BE", uc.getStarlevel());
				map.put("BF", uc.getCardtype());
				map.put("BG", uc.getUpexp());
				map.put("BH", uc.getCurexp());
				map.put("BW",uc.getCardlevel());
				map.put("BI", uc.getCurvalue());
				map.put("BJ", uc.getHp()+uc.getSameaddhp());
				map.put("BK", uc.getAtk()+uc.getSameaddatk());
				map.put("BL", uc.getDef()+uc.getSameadddef());
				map.put("BM", uc.getCurstyle());
				map.put("BN", uc.getIfuse());
				map.put("BO", uc.getLocate());
				/*map.put("BS",uc.getSameflag());
				map.put("CA",uc.getAdd_hp());
				map.put("CB",uc.getAdd_atk());
				map.put("CC",uc.getAdd_def());*/
				map.put("CD", uc.getLifeskill());
				map.put("CE", uc.getCurpointval());//当前魅力点
				ucardlist.add(map);
			}
			sendMap.put("WE", ucardlist.toArray());
			this.send(sendMap);
		} catch (Exception e) {
			// 失败，通知客户端
			Map<String,Object> sendMap = new HashMap<String,Object>();
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1004");
			this.send(sendMap);
			logger.info(e.getMessage());
		}	
	}
	//返回卡牌装备技能等信息
	public void ReturnCardInfo(int cid){
		ArrayList<Map<String, Object>> carddevicelist; //装备队列
		ArrayList<Map<String, Object>> cardskilllist; //装备队列
		ArrayList<Map<String, Object>> cardmarklist; //装备符文队列
		Map<String,Object> sendMap = new HashMap<String,Object>();
		UserCard uc=this.cardlist.get(cid);
		carddevicelist= new  ArrayList<Map<String, Object>>();		
		Iterator<UserDevice> it = uc.getDeviceMap().values().iterator();
		while(it.hasNext()){
			UserDevice ud = it.next();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("DB", ud.getDeviceid());
			map.put("AA", ud.getDevicecode());
			map.put("DC", ud.getDevicename());
			map.put("DE", ud.getDevicedesc());
			map.put("DF", ud.getDevicetype());
			map.put("DG", ud.getQuality());
			map.put("DH", ud.getGrade());
			map.put("DI", ud.getDhp());
			map.put("DJ", ud.getDatk());
			map.put("DK", ud.getDdef());
			map.put("DL", ud.getBaseprice());
			map.put("DM", ud.getPrice());
			map.put("DN", ud.getCardid());
			map.put("DO", ud.getMarkstring());
			map.put("DP",ud.getHavemark());
			map.put("DV", ud.getDevcolor());
			if(!ud.getMarkstring().equals("@")&&!ud.getMarkstring().equals("")){
				MarkStringCA msca=GameManager.getInstance().getMarksetdict().get(ud.getMarkstring());
				map.put("DS", msca.getMktype());
				map.put("DT", msca.getMkdesc());
			}
			//logger.info("返回卡牌信息装备名称："+ud.getDevicename()+"等级："+ud.getGrade());
			cardmarklist= new  ArrayList<Map<String, Object>>();
			if(ud.getHavemark()>0){
				Iterator<UserMark> itm = ud.getMarkMap().values().iterator();
				while(itm.hasNext()){
					UserMark um= itm.next();
					Map<String,Object> newmap = new HashMap<String,Object>();
					newmap.put("FA", um.getMarkid());
					newmap.put("KK", um.getMarkcode());
					newmap.put("FB", um.getMarkname());
					newmap.put("FC", um.getMarkdesc());
					newmap.put("FD", um.getMarktype());
					newmap.put("FE", um.getAtk());
					newmap.put("FF", um.getHp());
					newmap.put("FG", um.getDef());
					newmap.put("FH", um.getDeviceid());
					newmap.put("FI", um.getCardid());
					newmap.put("FJ", um.getMarkloc());
					cardmarklist.add(newmap);
				}	
			}
			map.put("DQ", cardmarklist.toArray());
			carddevicelist.add(map);
		}
		cardskilllist = new  ArrayList<Map<String, Object>>();
		DecimalFormat df = new DecimalFormat("0.00");
		if(uc.getSkillMap().size()>0){
			Iterator<UserSkill> its = uc.getSkillMap().values().iterator();
			while(its.hasNext()){
				UserSkill uk = its.next();
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("EB", uk.getSkillid());
				map.put("GG", uk.getSkillcode());
				map.put("EC", uk.getSkillname());
				map.put("EE", uk.getSkilldesc());
				map.put("EF", uk.getSkilltype());
				map.put("EG", uk.getSkillgrade());
				map.put("EH", uk.getShp());
				map.put("EI", uk.getSatk());
				map.put("EJ", uk.getSdef());
				map.put("EK", uk.getCardid());
				map.put("EL", uk.getSkillto());
				map.put("ER", uk.getSkillcolor());
				map.put("ES", uk.getIflife());
				int perhurt=0,hurtadd=0;
				perhurt=(int)(uk.getHurtpert()+uk.getUphurtper()*uk.getSkillgrade())-1;
				//（伤害百分比+Lv*百分比增幅）+$（附加值+Lv*附加值增幅）
				hurtadd=(int)(uk.getHurt()+uk.getUphurt()*uk.getSkillgrade());
				if(uk.getSkilltype().equals("被动")) {
					if(uk.getShp()>0) map.put("EM", "命+"+df.format((uk.getShp()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
					if(uk.getSatk()>0) map.put("EM", "攻+"+df.format((uk.getSatk()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
					if(uk.getSdef()>0) map.put("EM", "防+"+df.format((uk.getSdef()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
				}
				else {
					String sendstr="";
					if(perhurt>0) {
						sendstr=perhurt+"%";
						if(hurtadd>0) sendstr=sendstr+"+"+hurtadd;
					}else{
						if(hurtadd>0) sendstr=Integer.toString(hurtadd);
					}
					map.put("EM", sendstr);
				}
				map.put("EN", uk.getSkillloc());
				cardskilllist.add(map);
			}
		}
		ArrayList<Map<String, Object>> ucardlist= new  ArrayList<Map<String, Object>>();
		for(int i=0;i<this.cardlocid.length;i++){
			if(this.cardlocid[i]<=0) break;
			UserCard memberuc=this.cardlist.get(this.cardlocid[i]);
			Map<String,Object> membermap = new HashMap<String,Object>();
			membermap.put("BA", memberuc.getCardid());
			membermap.put("BB", memberuc.getCardname());
			membermap.put("BC", memberuc.getCarddesc());
			membermap.put("DD", memberuc.getCardcode());
			membermap.put("BD", memberuc.getCardprop());
			membermap.put("BE", memberuc.getStarlevel());
			membermap.put("BF", memberuc.getCardtype());
			membermap.put("BG", memberuc.getUpexp());
			membermap.put("BH", memberuc.getCurexp());
			membermap.put("BW",memberuc.getCardlevel());
			membermap.put("BI", memberuc.getCurvalue());
			membermap.put("BJ", memberuc.getHp()+memberuc.getSameaddhp());
			membermap.put("BK", memberuc.getAtk()+memberuc.getSameaddatk());
			membermap.put("BL", memberuc.getDef()+memberuc.getSameadddef());
			membermap.put("BM", memberuc.getCurstyle());
			membermap.put("BN", memberuc.getIfuse());
			membermap.put("BO", memberuc.getLocate());
			membermap.put("BS",memberuc.getSameflag());
			membermap.put("CA",memberuc.getAdd_hp());
			membermap.put("CB",memberuc.getAdd_atk());
			membermap.put("CC",memberuc.getAdd_def());
			membermap.put("CD", memberuc.getLifeskill());
			membermap.put("CE", uc.getCurpointval());//当前魅力点
			ucardlist.add(membermap);			
		}		
		sendMap.put("AZ", ucardlist.toArray());
		sendMap.put("EA", cardskilllist.toArray());
		sendMap.put("CMD", PacketCommandType.RETURNCARDINFO);
		sendMap.put("DA", carddevicelist.toArray());
		sendMap.put("SS", uc.getCardid());
		sendMap.put("BJ",uc.getHp());
		sendMap.put("BK",uc.getAtk());
		sendMap.put("BL", uc.getDef());
		sendMap.put("BZ", uc.getLifeskill());	
		this.send(sendMap);
	}
	//卡牌换位
	public void changeCard(String[] al){
		//更新数据库
		GameServerDao dao = new GameServerDao();
		try {
			dao.cardChange(this,al);
			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("CMD", PacketCommandType.CHANGECARDSUC);
			this.send(sendMap);
		} catch (SQLException e) {
			// 更新出错换位失败
			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1003"); //换位失败
			this.send(sendMap);
			e.printStackTrace();
		}
		
	}
	//获取背包中卡牌列表
	public void GetUserCardList(){
		ArrayList<Map<String, Object>> ucardlist= new  ArrayList<Map<String, Object>>();
		Iterator<UserCard> it = this.cardlist.values().iterator();
		while(it.hasNext()){
			UserCard uc = it.next();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("BA", uc.getCardid());
			map.put("DD", uc.getCardcode());
			map.put("BB", uc.getCardname());
			map.put("BC", uc.getCarddesc());
			map.put("BD", uc.getCardprop());
			map.put("BE", uc.getStarlevel());
			map.put("BF", uc.getCardtype());
			map.put("BG", uc.getUpexp());
			map.put("BH", uc.getCurexp());
			map.put("BW", uc.getCardlevel());
			map.put("BI", uc.getCurvalue());
			map.put("BJ", uc.getHp()+uc.getSameaddhp());
			map.put("BK", uc.getAtk()+uc.getSameaddatk());
			map.put("BL", uc.getDef()+uc.getSameadddef());
			map.put("BM", uc.getCurstyle());
			map.put("BN", uc.getIfuse());
			map.put("BO", uc.getLocate());
			map.put("BS",uc.getSameflag());
			map.put("BZ", uc.getLifeskill());
			map.put("CE", uc.getCurpointval());//当前魅力点
			map.put("CF", (uc.getCurvalue()+1)*10);//升魅力所需点
			ucardlist.add(map);			
		}
		Collections.sort(ucardlist,new Comparator<Map<String,Object>>(){
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
				return Integer.parseInt(arg1.get("BN").toString())-Integer.parseInt(arg0.get("BN").toString()); }  

		});
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD",PacketCommandType.RETURNCARDLIST);
		sendMap.put("AZ", ucardlist.toArray());
		this.send(sendMap);
	}
	//获取背包中装备列表
	public void GetUserDevList(){
		ArrayList<Map<String, Object>> udevlist= new  ArrayList<Map<String, Object>>();
		Iterator<UserDevice> it = this.udevicelist.values().iterator();
		while(it.hasNext()){
			UserDevice ud = it.next();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("DB", ud.getDeviceid());
			map.put("AA", ud.getDevicecode());
			map.put("DC", ud.getDevicename());
			map.put("DE", ud.getDevicedesc());
			map.put("DF", ud.getDevicetype());
			map.put("DG", ud.getQuality());
			map.put("DH", ud.getGrade());
			map.put("DI", ud.getDhp());
			map.put("DJ", ud.getDatk());
			map.put("DK", ud.getDdef());
			map.put("DL", ud.getBaseprice());
			map.put("DM", ud.getPrice());
			map.put("DN", ud.getCardid());
			map.put("DO", ud.getMarkstring());
			map.put("DP",ud.getHavemark());
			map.put("DV", ud.getDevcolor());
			if(!ud.getMarkstring().equals("@")&&!ud.getMarkstring().equals("")){
				MarkStringCA msca=GameManager.getInstance().getMarksetdict().get(ud.getMarkstring());
				map.put("DS", msca.getMktype());
				map.put("DT", msca.getMkdesc());
			}
			Iterator<UserMark> itm =ud.getMarkMap().values().iterator();
			ArrayList<Map<String, Object>> umarklist= new  ArrayList<Map<String, Object>>();
			while(itm.hasNext()){
				UserMark um= itm.next();
				Map<String,Object> newmap = new HashMap<String,Object>();
				newmap.put("FA", um.getMarkid());
				newmap.put("KK", um.getMarkcode());
				newmap.put("FB", um.getMarkname());
				newmap.put("FC", um.getMarkdesc());
				newmap.put("FD", um.getMarktype());
				newmap.put("FE", um.getAtk());
				newmap.put("FF", um.getHp());
				newmap.put("FG", um.getDef());
				newmap.put("FH", um.getDeviceid());
				newmap.put("FI", um.getCardid());
				newmap.put("FJ", um.getMarkloc());
				umarklist.add(newmap);
			}	
			map.put("DQ", umarklist.toArray());
			udevlist.add(map);			
		}
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD",PacketCommandType.RETURNDEVICELIST);
		sendMap.put("DA", udevlist.toArray());
		this.send(sendMap);
	}
	//获取背包中技能列表
	public void GetUserSkList(){
		ArrayList<Map<String, Object>> pskilllist= new  ArrayList<Map<String, Object>>();
		Iterator<UserSkill> it = this.uskilllist.values().iterator();
		DecimalFormat df = new DecimalFormat("0.00");
		while(it.hasNext()){
			UserSkill uk = it.next();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("EB", uk.getSkillid());
			map.put("GG", uk.getSkillcode());
			map.put("EC", uk.getSkillname());
			map.put("EE", uk.getSkilldesc());
			map.put("EF", uk.getSkilltype());
			map.put("EG", uk.getSkillgrade());
			map.put("EH", uk.getShp());
			map.put("EI", uk.getSatk());
			map.put("EJ", uk.getSdef());
			map.put("EK", uk.getCardid());
			map.put("EL", uk.getSkillto());
			map.put("ER", uk.getSkillcolor());
			map.put("ES", uk.getIflife());
			int perhurt=0,hurtadd=0;
			perhurt=(int)(uk.getHurtpert()+uk.getUphurtper()*uk.getSkillgrade())-1;
			//（伤害百分比+Lv*百分比增幅）+$（附加值+Lv*附加值增幅）
			hurtadd=(int)(uk.getHurt()+uk.getUphurt()*uk.getSkillgrade());
			if(uk.getSkilltype().equals("被动")) {
				if(uk.getShp()>0) map.put("EM", "命+"+df.format((uk.getShp()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
				if(uk.getSatk()>0) map.put("EM", "攻+"+df.format((uk.getSatk()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
				if(uk.getSdef()>0) map.put("EM", "防+"+df.format((uk.getSdef()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
			}
			else {
				String sendstr="";
				if(perhurt>0) {
					sendstr=perhurt+"%";
					if(hurtadd>0) sendstr=sendstr+"+"+hurtadd;
				}else{
					if(hurtadd>0) sendstr=Integer.toString(hurtadd);
				}
				map.put("EM", sendstr);
			}
			map.put("EN", uk.getSkillloc());
			pskilllist.add(map);
		}
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD",PacketCommandType.RETURNUSKILLLIST);
		sendMap.put("EA", pskilllist.toArray());
		this.send(sendMap);
	}
	//获取背包中符文列表
	public void GetUMarkList(){
		UserMark umtemp;
		ArrayList<Map<String, Object>> pmarklist= new  ArrayList<Map<String, Object>>();		
		Iterator<MarkDict> it = GameManager.getInstance().getMarkdict().values().iterator();
		while(it.hasNext()){
			int flag=0;
			Map<String,Object> newmap = new HashMap<String,Object>();
			MarkDict md= it.next();
			Iterator<UserMark> itm = this.getUmarklist().values().iterator();
			while(itm.hasNext()){
				umtemp= itm.next();
				if(umtemp.getMarkcode().equals(md.getMarkcode())&&(umtemp.getDeviceid()<1||umtemp.getMarkloc()<0)){
					flag=flag+1;
				}
			}
			newmap.put("FA", flag);
			newmap.put("KK", md.getMarkcode());
			newmap.put("FB", md.getMarkname());
			newmap.put("FC", md.getMarkdesc());
			newmap.put("FD", md.getMarktype());
			newmap.put("FE", md.getAtk());
			newmap.put("FF", md.getHp());
			newmap.put("FG", md.getDef());
			newmap.put("FH", 0);
			newmap.put("FI", 0);
			newmap.put("FJ", -1);
			/*if(flag>0){
				newmap.put("FA", flag);
				newmap.put("KK", um.getMarkcode());
				newmap.put("FB", um.getMarkname());
				newmap.put("FC", um.getMarkdesc());
				newmap.put("FD", um.getMarktype());
				newmap.put("FE", um.getAtk());
				newmap.put("FF", um.getHp());
				newmap.put("FG", um.getDef());
				newmap.put("FH", um.getDeviceid());
				newmap.put("FI", um.getCardid());
				newmap.put("FJ", um.getMarkloc());
			}else{
				newmap.put("FA", flag);
				newmap.put("KK", md.getMarkcode());
				newmap.put("FB", md.getMarkname());
				newmap.put("FC", md.getMarkdesc());
				newmap.put("FD", md.getMarktype());
				newmap.put("FE", md.getAtk());
				newmap.put("FF", md.getHp());
				newmap.put("FG", md.getDef());
				newmap.put("FH", 0);
				newmap.put("FI", 0);
				newmap.put("FJ", 0);
			}*/	
			//logger.info("符文code："+md.getMarkcode()+"数量："+flag);
			this.umarksnumlist.put(md.getMarkcode(), flag);
			pmarklist.add(newmap);
		}
		Collections.sort(pmarklist,new Comparator<Map<String,Object>>(){
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
				return Integer.parseInt(arg0.get("KK").toString().substring(1))-Integer.parseInt(arg1.get("KK").toString().substring(1)); }  

		});
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD",PacketCommandType.RETURNUMARKLIST);
		sendMap.put("DD", pmarklist.toArray());
		this.send(sendMap);
	}

	//穿上装备
	public void LoadDevice(Map<String, Object> _umap){
		int did=Integer.parseInt(_umap.get("AA").toString());//要穿上的装备
		int cid=Integer.parseInt(_umap.get("BB").toString());//要穿装备的卡牌id
		int ocid =Integer.parseInt(_umap.get("CC").toString());//被换的装备id 0无
		UserCard uc=this.getCardlist().get(cid);
		CardLogicProc clogic = new CardLogicProc();
		try {
			Map<String,Object> sendMap = new HashMap<String,Object>();
			if(clogic.LoadDevice(this,uc,did,ocid)>0){
				sendMap.put("CMD",PacketCommandType.USEDDEVICE);
				sendMap.put("CA", uc.getHp()+uc.getSameaddhp());
				sendMap.put("CB", uc.getAtk()+uc.getSameaddatk());
				sendMap.put("CC", uc.getDef()+uc.getSameadddef());
				sendMap.put("DA", did);
				sendMap.put("DB", cid);
			}else{
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1006"); //穿上装备异常
			}
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //穿上装备异常
			this.send(smap);
			e.printStackTrace();
		}
	}
	//获取技能书页列表
	public void GetUMarkPgList(){
		ArrayList<Map<String, Object>> umpklist= new  ArrayList<Map<String, Object>>();
		Iterator<SkillPages> itm = this.getSkillpagelist().values().iterator();
		DecimalFormat df = new DecimalFormat("0.00");
		while(itm.hasNext()){
			SkillPages sp= itm.next();
			Map<String,Object> newmap = new HashMap<String,Object>();
			newmap.put("AA", sp.getSkillcode());
			newmap.put("JB", sp.getSkillname());
			newmap.put("JC", sp.getPagenum());
			newmap.put("JD", sp.getPage1());
			newmap.put("JE", sp.getPage2());
			newmap.put("JF", sp.getPage3());
			newmap.put("JG", sp.getPage4());
			newmap.put("JH", sp.getPage5());
			newmap.put("JI", sp.getSkdesc());
			newmap.put("JJ", sp.getSktype());
			newmap.put("JK", sp.getSkgrade());
			newmap.put("JL", sp.getSkillto());
			newmap.put("ER", sp.getSkillcolor());
			int perhurt=0,hurtadd=0;
			perhurt=(int)(sp.getHurtpert()+sp.getUphurtper())-1;
			//（伤害百分比+Lv*百分比增幅）+$（附加值+Lv*附加值增幅）
			hurtadd=(int)(sp.getHurt()+sp.getUphurt());
			if(sp.getSktype().equals("被动")) {
				if(sp.getHps()>0) newmap.put("JM", "命+"+df.format((sp.getHps()+sp.getUphurtper()*sp.getSkgrade())*100)+"%");
				if(sp.getAtks()>0) newmap.put("JM", "攻+"+df.format((sp.getAtks()+sp.getUphurtper()*sp.getSkgrade())*100)+"%");
				if(sp.getDefs()>0) newmap.put("JM", "防+"+df.format((sp.getDefs()+sp.getUphurtper()*sp.getSkgrade())*100)+"%");
			}
			else {
				String sendstr="";
				if(perhurt>0) {
					sendstr=perhurt+"%";
					if(hurtadd>0) sendstr=sendstr+"+"+hurtadd;
				}else{
					if(hurtadd>0) sendstr=Integer.toString(hurtadd);
				}
				newmap.put("JM", sendstr);
			}
			umpklist.add(newmap);
		}				
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD",PacketCommandType.RETURNUMPLIST);
		sendMap.put("JA", umpklist.toArray());
		this.send(sendMap);
	}

	//符文镶嵌
	public void LoadMark(Map<String, Object> _umap){
		int did=Integer.parseInt(_umap.get("AA").toString());//要镶嵌符文装备
		String mcode=_umap.get("BB").toString();//符文CODE
		UserDevice ud=this.getUdevicelist().get(did);
		CardLogicProc clogic = new CardLogicProc();
		try {
			//logger.info("镶嵌符文："+mcode);
			clogic.LoadMark(this,ud,mcode);
			Map<String,Object> sendMap = new HashMap<String,Object>();
			sendMap.put("CMD",PacketCommandType.STAMPMARK);
			/*sendMap.put("AA", this.uhp);
			sendMap.put("AB", this.uatk);
			sendMap.put("AC", this.udef);*/
			sendMap.put("DA", did);
			sendMap.put("CD", this.getGold());
			if(this.cardlist.containsKey(ud.getCardid())){//装备所在卡牌上
				UserCard uc=this.getCardlist().get(ud.getCardid());
				sendMap.put("CA", uc.getHp()+uc.getSameaddhp());
				sendMap.put("CB", uc.getAtk()+uc.getSameaddatk());
				sendMap.put("CC", uc.getDef()+uc.getSameadddef());
				sendMap.put("DN", uc.getCardid());
			}
			sendMap.put("DO", ud.getMarkstring());
			if(!ud.getMarkstring().equals("@")&&!ud.getMarkstring().equals("")){
				MarkStringCA msca=GameManager.getInstance().getMarksetdict().get(ud.getMarkstring());
				sendMap.put("DS", msca.getMktype());
				sendMap.put("DT", msca.getMkdesc());
			}
			sendMap.put("DP", ud.getHavemark());
			Iterator<UserMark> it = ud.getMarkMap().values().iterator();
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
			while(it.hasNext()){
				Map<String,Object> tmap= new HashMap<String,Object>();
				UserMark um=it.next();
				tmap.put("FA", um.getMarkid());
				tmap.put("KK", um.getMarkcode());
				tmap.put("FB", um.getMarkname());
				tmap.put("FC", um.getMarkdesc());
				tmap.put("FD", um.getMarktype());
				tmap.put("FF", um.getHp());
				tmap.put("FE", um.getAtk());
				tmap.put("FG", um.getDef());
				tmap.put("FH", um.getDeviceid());
				tmap.put("FI", um.getCardid());
				tmap.put("FJ", um.getMarkloc());
				al.add(tmap);
			}
			sendMap.put("DQ", al.toArray());
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1007"); //镶嵌符文异常
			this.send(smap);
			e.printStackTrace();
		}
	}
	//获取符文组合
	public void getMarksets(){
		String  strset="";
		Map<String,Object> sendMap = new HashMap<String,Object>();
		ArrayList<Map<String,Object>> ml=new ArrayList<Map<String,Object>>();
		Iterator<MarkStringCA> it = GameManager.getInstance().getMarksetdict().values().iterator();
		while(it.hasNext()){
			Map<String,Object> map=new HashMap<String,Object>();
			MarkStringCA mca=it.next();
			if(mca.getMkflag()==0){
				continue;
			}
			strset=mca.getMkstring();
			map.put("AB",strset);
			map.put("AD",mca.getMksaddhp());
			map.put("AE",mca.getMksaddatk());
			map.put("AF",mca.getMksadddef());
			map.put("AG",mca.getMkdesc());
			map.put("AH",mca.getMktype());
			ArrayList<Map<String,Object>> al=new ArrayList<Map<String,Object>>();
			String mstr[] = strset.split(",");
			String tempstr="";
			for(int i=0;i<mstr.length;i++){
				Map<String,Object> mapset=new HashMap<String,Object>();
				mapset.put("BA", mstr[i]);
				mapset.put("BB", GameManager.getInstance().getMarkdict().get(mstr[i]).getMarkname());
				mapset.put("BC", GameManager.getInstance().getMarkdict().get(mstr[i]).getHp());
				mapset.put("BD", GameManager.getInstance().getMarkdict().get(mstr[i]).getAtk());
				mapset.put("BE", GameManager.getInstance().getMarkdict().get(mstr[i]).getDef());
				Iterator<UserMark> its = this.umarklist.values().iterator();
				int sumflag=0;
				while(its.hasNext()){
					UserMark um=its.next();
					if(um.getMarkcode().equals(mstr[i]) && um.getDeviceid()<1){
						sumflag=sumflag+1;
					}
				}	
				mapset.put("BF", sumflag);
				mapset.put("BG",i);
				al.add(mapset);
			}
			//
			for(int i=0;i<al.size();i++){
				tempstr=al.get(i).get("BA").toString();
				int k1=Integer.parseInt(al.get(i).get("BF").toString());
				int k3=k1;
				for(int j=i+1;j<al.size();j++){
					int k2=Integer.parseInt(al.get(j).get("BF").toString());
					if(tempstr.equals(al.get(j).get("BA").toString())){
						if(k1>1){
							if(k2==k3){
								al.get(j).put("BF",1);
								al.get(i).put("BF",	k1-1);
								k1=k1-1;
							}							
						}else if(k1==1){
							al.get(j).put("BF",0);
						}
					}
				}
			}
			map.put("AC", al.toArray());
			ml.add(map);
		}
		sendMap.put("AA",ml.toArray());
		sendMap.put("CMD", PacketCommandType.RETURNMARKSETS);
		this.send(sendMap);
	}
	//技能安装
	public void LoadSkill(Map<String, Object> _umap){
		int cid=Integer.parseInt(_umap.get("AA").toString());//要安装技能的卡牌id
		int sid=Integer.parseInt(_umap.get("BB").toString());//技能id
		int osid=Integer.parseInt(_umap.get("CC").toString());//被换的技能id 0为空位
		int skloc=Integer.parseInt(_umap.get("DD").toString());//安装位置 0-2   技能在卡牌上的安装位置
		UserCard uc=this.getCardlist().get(cid);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(uc.getSkillMap().containsKey(sid)){//已有相同技能
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1076"); //
			this.send(sendMap);
		}else{
			CardLogicProc clogic = new CardLogicProc();
			try {
				
				int kflag=clogic.LoadSkill(this,uc,sid,osid,skloc);
				if(kflag>0){
					sendMap.put("CMD",PacketCommandType.SKILLACT);
					sendMap.put("CA", uc.getHp()+uc.getSameaddhp());
					sendMap.put("CB", uc.getAtk()+uc.getSameaddatk());
					sendMap.put("CC", uc.getDef()+uc.getSameadddef());
					sendMap.put("DA", cid);
					sendMap.put("DB", sid);
					sendMap.put("DC", 1);
					DecimalFormat df = new DecimalFormat("0.00");
					Iterator<UserSkill> it = uc.getSkillMap().values().iterator();
					ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
					while(it.hasNext()){
						UserSkill uk=it.next();
						Map<String,Object> map=new HashMap<String,Object>();
						map.put("EB", uk.getSkillid());
						map.put("GG", uk.getSkillcode());
						map.put("EC", uk.getSkillname());
						map.put("EE", uk.getSkilldesc());
						map.put("EF", uk.getSkilltype());
						map.put("EG", uk.getSkillgrade());
						map.put("EH", uk.getShp());
						map.put("EI", uk.getSatk());
						map.put("EJ", uk.getSdef());
						map.put("EK", uk.getCardid());
						map.put("EL", uk.getSkillto());
						map.put("EN", uk.getSkillloc());
						map.put("ER", uk.getSkillcolor());
						map.put("ES", uk.getIflife());
						int perhurt=0,hurtadd=0;
						perhurt=(int)(uk.getHurtpert()+uk.getUphurtper()*uk.getSkillgrade())-1;
						//（伤害百分比+Lv*百分比增幅）+$（附加值+Lv*附加值增幅）
						hurtadd=(int)(uk.getHurt()+uk.getUphurt()*uk.getSkillgrade());
						if(uk.getSkilltype().equals("被动")) {
							if(uk.getShp()>0) map.put("EM", "命+"+df.format((uk.getShp()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
							if(uk.getSatk()>0) map.put("EM", "攻+"+df.format((uk.getSatk()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
							if(uk.getSdef()>0) map.put("EM", "防+"+df.format((uk.getSdef()+uk.getUphurtper()*uk.getSkillgrade())*100)+"%");
						}
						else {
							String sendstr="";
							if(perhurt>0) {
								sendstr=perhurt+"%";
								if(hurtadd>0) sendstr=sendstr+"+"+hurtadd;
							}else{
								if(hurtadd>0) sendstr=Integer.toString(hurtadd);
							}
							map.put("EM", sendstr);
						}
						al.add(map);
					}
					sendMap.put("EA", al.toArray());
				}else if(kflag==0){
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1010"); //技能安装异常
				}else{
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1009"); //天赋技能不能更换
				}
				this.send(sendMap);
			} catch (SQLException e) {
				Map<String,Object> smap= new HashMap<String,Object>();
				smap.put("CMD", PacketCommandType.GETERROR);
				smap.put("EC", "1099"); //安装技能异常
				this.send(smap);
				e.printStackTrace();
			}
		}		
	}
	//返回装备精练值
	public void ReturnUpgradeInfo(Map<String, Object> _umap){
		int did=Integer.parseInt(_umap.get("AA").toString());//要精炼装备id
		UserDevice ud=this.getUdevicelist().get(did);
		String dty=ud.getDevicetype();
		String dqu=ud.getQuality();
		int nextlev=ud.getGrade()+1;
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(nextlev>this.level*3||nextlev>297){//精炼超过上限
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1059"); //强化等级已到上限
		}else{
			sendMap.put("CMD",PacketCommandType.RETURNDEVGRADE);
			sendMap.put("AA",GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(nextlev).getAddhp());
			sendMap.put("AB", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(nextlev).getAddatk());
			sendMap.put("AC", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(nextlev).getAdddef());
			sendMap.put("AD", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(nextlev).getMoneyvalue());
			sendMap.put("AE", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(nextlev).getUpgold());
			sendMap.put("AH", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(nextlev).getAddprice());
			sendMap.put("AI", did);
			sendMap.put("AJ", ud.getGrade());
			sendMap.put("AK",ud.getPrice());
			sendMap.put("AL",ud.getDhp());
			sendMap.put("AM",ud.getDatk());
			sendMap.put("AN",ud.getDdef());
			sendMap.put("AO",ud.getDevicecode());
			sendMap.put("AP",-1);
			//logger.info("精炼信息装备名称："+ud.getDevicename()+"等级："+ud.getGrade());
			sendMap.put("AW", ud.getQuality());
		}
		this.send(sendMap);
	}
	//开始精炼装备
	public void UpgradeDevice(Map<String, Object> _umap){
		int did=Integer.parseInt(_umap.get("AA").toString());//要精炼装备id
		int fid=Integer.parseInt(_umap.get("BB").toString());//是否强化
		UserDevice ud=this.getUdevicelist().get(did);
		int flag=0;
		CardLogicProc clogic = new CardLogicProc();
		try {
			Map<String,Object> sendMap = new HashMap<String,Object>();
			sendMap.put("BA",ud.getDhp());
			sendMap.put("BB",ud.getDatk());
			sendMap.put("BC",ud.getDdef());
			if(ud.getGrade()+1>this.level*3||ud.getGrade()>=297){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1059"); //强化等级已到上限
				this.send(sendMap);
			}else{
				flag=clogic.UpgradeDevice(this,ud,fid);
				if(flag<0){//钱不够
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1027"); //没有足够的钱
					this.send(sendMap);
				}else{				
					String dty=ud.getDevicetype();
					String dqu=ud.getQuality();
					if(ud.getCardid()>0){
						UserCard uc=this.cardlist.get(ud.getCardid());
						sendMap.put("AS",uc.getHp()+uc.getSameaddhp());
						sendMap.put("AT", uc.getAtk()+uc.getSameaddatk());
						sendMap.put("AU", uc.getDef()+uc.getSameadddef());
					}
					if(flag>0){//精炼成功
						int curg=ud.getGrade();
						sendMap.put("AA",GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(curg+flag).getAddhp());
						sendMap.put("AB", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(curg+flag).getAddatk());
						sendMap.put("AC", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(curg+flag).getAdddef());
						sendMap.put("AD", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(curg+flag).getMoneyvalue());
						sendMap.put("AE", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(curg+flag).getUpgold());
						sendMap.put("AH", GameManager.getInstance().getDeviceaddval().get(dty).get(dqu).get(curg+flag).getAddprice());
					}
					//logger.info("结果："+flag);
					sendMap.put("AI", did);
					sendMap.put("AJ", ud.getGrade());
					sendMap.put("AK",ud.getPrice());
					sendMap.put("AL",ud.getDhp());
					sendMap.put("AM",ud.getDatk());
					sendMap.put("AN",ud.getDdef());
					sendMap.put("AO",ud.getDevicecode());
					sendMap.put("AP",flag);
					sendMap.put("AQ",this.money);
					sendMap.put("AR",this.getGold());
					sendMap.put("AW", ud.getQuality());
					sendMap.put("AZ", flag);//是否爆机
					/*logger.info("精炼完成装备名称装备id："+ud.getDevicename()+ud.getDeviceid()+"等级："+ud.getGrade());
					logger.info("卡牌名称:"+this.cardlist.get(ud.getCardid()).getDeviceMap().get(ud.getDeviceid()).getDevicename());
					logger.info("卡牌等级:"+this.cardlist.get(ud.getCardid()).getDeviceMap().get(ud.getDeviceid()).getGrade());*/
					sendMap.put("CMD", PacketCommandType.UPGRADESUC);
					this.send(sendMap);
				}
			}
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1011"); //精炼失败
			this.send(smap);
			e.printStackTrace();
		}
	}
	//培养信息
	public void liveupcard(Map<String, Object> _umap){
		int cid = Integer.parseInt(_umap.get("AA").toString());
		UserCard uc = this.getCardlist().get(cid);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD",PacketCommandType.RETURNLIVEUP);
		sendMap.put("AA",uc.getHp()-uc.getLivehp());
		sendMap.put("AB", uc.getLivehp());
		sendMap.put("AC", uc.getAtk()-uc.getLiveatk());
		sendMap.put("AD", uc.getLiveatk());
		sendMap.put("AE", uc.getDef()-uc.getLivedef());
		sendMap.put("AF",uc.getLivedef());
		sendMap.put("AI",uc.getCardlevel()*30);//培养上限def
		sendMap.put("AH", uc.getCardlevel()*30);//atk
		sendMap.put("AG", uc.getCardlevel()*30);//hp
		if(uc.getCardlevel()<=9) sendMap.put("AJ", 1000);
		else if(uc.getCardlevel()<=19) sendMap.put("AJ", 2000);
		else if(uc.getCardlevel()<=29) sendMap.put("AJ", 3000);
		else if(uc.getCardlevel()<=39) sendMap.put("AJ", 4000);
		else if(uc.getCardlevel()<=49) sendMap.put("AJ", 5000);
		else if(uc.getCardlevel()<=59) sendMap.put("AJ", 6000);
		else if(uc.getCardlevel()<=69) sendMap.put("AJ", 7000);
		else if(uc.getCardlevel()<=79) sendMap.put("AJ", 8000);
		else if(uc.getCardlevel()<=89) sendMap.put("AJ", 9000);
		else if(uc.getCardlevel()<=99) sendMap.put("AJ", 10000);
		sendMap.put("AK", 10);
		sendMap.put("AL", 50);
		LiveUpDictCla luc = GameManager.getInstance().getLiveupdict().get("L003");
		sendMap.put("AM",luc.getVipgrade());
		sendMap.put("AN",uc.getCardid());
		sendMap.put("AO",uc.getTlivehp());
		sendMap.put("AP",uc.getTliveatk());
		sendMap.put("AQ",uc.getTlivedef());
		this.send(sendMap);
	}
	//培养
	public void actliveupcard(Map<String, Object> _umap){
		int cid = Integer.parseInt(_umap.get("AA").toString());
		String ltype =_umap.get("BB").toString();
		UserCard uc = this.getCardlist().get(cid);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try {
			int maxhp=0,maxdef=0,maxatk=0,curcost=0;
			maxdef=uc.getCardlevel()*30;//培养上限
			maxatk=uc.getCardlevel()*30;
			maxhp=uc.getCardlevel()*30;
			if(uc.getCardlevel()<=9) curcost=1000;
			else if(uc.getCardlevel()<=19) curcost=2000;
			else if(uc.getCardlevel()<=29) curcost=3000;
			else if(uc.getCardlevel()<=39) curcost=4000;
			else if(uc.getCardlevel()<=49) curcost=5000;
			else if(uc.getCardlevel()<=59) curcost=6000;
			else if(uc.getCardlevel()<=69) curcost=7000;
			else if(uc.getCardlevel()<=79) curcost=8000;
			else if(uc.getCardlevel()<=89) curcost=9000;
			else if(uc.getCardlevel()<=99) curcost=10000;
			else curcost=10000;
			if(uc.getLivehp()==maxhp&&uc.getLiveatk()==maxatk&&uc.getLivedef()==maxdef){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1062"); //已达到培养上限		
			}else{
				int flag=dao.actliveupcard(this, uc, ltype,sendMap,maxhp,maxatk,maxdef,curcost);
				if(flag>0){
					sendMap.put("CMD",PacketCommandType.ACTLIVEUPSUC);
					sendMap.put("AA",uc.getCardid());
					sendMap.put("AH", this.money);
					sendMap.put("AI", this.getGold());
				}else{
					if(flag<0){
						sendMap.put("CMD", PacketCommandType.GETERROR);
						sendMap.put("EC", "1041"); //VIP等级不够				
					}else{
						sendMap.put("CMD", PacketCommandType.GETERROR);
						sendMap.put("EC", "1027"); //钱不够培养失败
					}
					
				}				
			}			
			this.send(sendMap);
		}catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1012"); //培养失败
			this.send(smap);
			e.printStackTrace();
		}		
	}
	//放弃培养
	public void abortliveup(Map<String, Object> _umap){
		int cid = Integer.parseInt(_umap.get("AA").toString());
		UserCard uc = this.getCardlist().get(cid);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try {
			uc.setTlivehp(0);
			uc.setTliveatk(0);
			uc.setTlivedef(0);
			dao.abortliveup(uc.getCardid(),this);
			sendMap.put("CMD",PacketCommandType.ABORTLIVEUP);
			sendMap.put("AA",uc.getCardid());
			this.send(sendMap);
		}catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1012"); //放弃培养数据失败
			this.send(smap);
			e.printStackTrace();
		}
		
	}
	//保存培养值
	public void saveliveup(Map<String, Object> _umap){
		int cid = Integer.parseInt(_umap.get("AA").toString());
		UserCard uc = this.getCardlist().get(cid);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try {
			dao.saveactliveup(this, uc);
			sendMap.put("CMD",PacketCommandType.SAVELIVEUPSUC);
			sendMap.put("AA",uc.getCardid());
			sendMap.put("AB",0);
			sendMap.put("AC",0);
			sendMap.put("AD",0);
			sendMap.put("AE",uc.getLivehp());
			sendMap.put("AF",uc.getLiveatk());
			sendMap.put("AG",uc.getLivedef());
			sendMap.put("AH", this.money);
			sendMap.put("AI", this.getGold());
			this.send(sendMap);
		}catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1013"); //保存失败
			this.send(smap);
			e.printStackTrace();
		}
	}
	//转生请求
	public void cardlifeup(Map<String, Object> _umap){
		int cid = Integer.parseInt(_umap.get("AA").toString());
		String flag=_umap.get("BB").toString();
		Map<String,Object> sendMap = new HashMap<String,Object>();
		UserCard uc = this.getCardlist().get(cid);
		if(uc.getStarlevel()==7){
			sendMap.put("CMD",PacketCommandType.GETERROR);
			sendMap.put("EC","1067");//已到转生最高星级
		}else{
			CLifeUpDict lfud =GameManager.getInstance().getLifeupdict().get(uc.getStarlevel());
			sendMap.put("CMD",PacketCommandType.RETURNLIFEUP);
			sendMap.put("AA",uc.getCardid());//卡牌ID
			UserCard copyuc=uc.clone();
			sendMap.put("BE",copyuc.getOrighp());
			sendMap.put("BF",copyuc.getOrigatk());
			sendMap.put("BG",copyuc.getOrigdef());
			CardLogicProc clp= new CardLogicProc();
			clp.preactlifeup(copyuc, flag);
			sendMap.put("BB",copyuc.getOrighp());
			sendMap.put("BC",copyuc.getOrigatk());
			sendMap.put("BD",copyuc.getOrigdef());
			sendMap.put("AB",lfud.getMoney());//普通money
			sendMap.put("AC",lfud.getGold());//普通金条
			sendMap.put("AF",lfud.getBall());//普通转生丹
			sendMap.put("AG",lfud.getExtexp());//继承经验百分比
			sendMap.put("AH",lfud.getExtval());//继承魅力百分比
			sendMap.put("AI",lfud.getVipgrade());//完美转生vip等级
			sendMap.put("AJ",lfud.getPermoney());//完美money
			sendMap.put("AK",lfud.getPergold());//完美金条
			sendMap.put("AN",lfud.getPerball());//完美转生丹
			sendMap.put("AO",lfud.getPextexp());//完美继承经验百分比
			sendMap.put("AP",lfud.getPextval());//完美继承魅力百分比
			sendMap.put("AQ",lfud.getGrade());//转生等级要求
			sendMap.put("AR",lfud.getCardval());//转生魅力要求
		}
		
		this.send(sendMap);
	}
	//开始转生
	public void actlifeup(Map<String, Object> _umap) throws SQLException{
		int cid = Integer.parseInt(_umap.get("AA").toString());
		String flag = _umap.get("BB").toString();
		UserCard uc = this.getCardlist().get(cid);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		CardLogicProc clogic = new CardLogicProc();
		try {
			int chkflag=clogic.actlifeup(this, uc,flag);
			if(chkflag>0){
				sendMap.put("CMD",PacketCommandType.ACTLIFEUPSUC);
				sendMap.put("AA",uc.getCardid());
				sendMap.put("BB",uc.getHp()+uc.getSameaddhp());
				sendMap.put("BC",uc.getAtk()+uc.getSameaddatk());
				sendMap.put("BD",uc.getDef()+uc.getSameadddef());
				sendMap.put("BE",uc.getStarlevel());
				sendMap.put("BF",uc.getCurexp());
				sendMap.put("BG",uc.getCurvalue());
				sendMap.put("BH", this.money);
				sendMap.put("BI", this.getGold());
				sendMap.put("BJ", this.ball);
				/*//下一次转生
				CLifeUpDict lfud =GameManager.getInstance().getLifeupdict().get(uc.getStarlevel());
				sendMap.put("BB",lfud.getMoney());//普通money
				sendMap.put("BC",lfud.getGold());//普通金条
				sendMap.put("BD",lfud.getInnercoin());//普通斗魂
				sendMap.put("BE",lfud.getGod());//普通女神泪
				sendMap.put("BF",lfud.getBall());//普通转生丹
				sendMap.put("BG",lfud.getExtexp());//继承经验百分比
				sendMap.put("BH",lfud.getExtval());//继承魅力百分比
				sendMap.put("BI",lfud.getVipgrade());//完美转生vip等级
				sendMap.put("BJ",lfud.getPermoney());//完美money
				sendMap.put("BK",lfud.getPergold());//完美金条
				sendMap.put("BL",lfud.getPerinnercoin());//完美斗魂
				sendMap.put("BM",lfud.getPergod());//完美女神泪
				sendMap.put("BN",lfud.getPerball());//完美转生丹
				sendMap.put("BO",lfud.getPextexp());//完美继承经验百分比
				sendMap.put("BP",lfud.getPextval());//完美继承魅力百分比
				sendMap.put("BQ",lfud.getGrade());//转生等级要求
				sendMap.put("BR",lfud.getCardval());//转生魅力要求
*/			}
			if(chkflag==-1) {
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1027"); //钱不够
			}
			if(chkflag==-2) {
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1031"); //转生丹不够
			}
			if(chkflag==-3) {
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1032"); //等级不够
			}
			if(chkflag==-4) {
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1033"); //魅力不足
			}
			if(chkflag==-5) {
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1067"); //转生最高星级已到
			}
			this.send(sendMap);
		}catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1034"); //转生数据异常
			this.send(smap);
			e.printStackTrace();
		}
	}
	//融魂请求 
	public void getmergeinfo(Map<String, Object> _umap){
		int cid = Integer.parseInt(_umap.get("AA").toString());//融魂卡
		int tid = Integer.parseInt(_umap.get("BB").toString());//散魂卡
		UserCard uc = this.getCardlist().get(cid).clone();
		UserCard tuc = this.getCardlist().get(tid);
		int exp=GameManager.getInstance().getUseruplevel().get(tuc.getCardlevel()).getUpcount()+tuc.getCurexp(); //散魂卡牌总经验
		int val=tuc.getCurvalue(); //魅力
		int transval=0;//魅力点数
		int upflag=0; //升级数
		int extexp=0,extval=0;
		MergeCardDict mc=GameManager.getInstance().getMergecarddict().get(tuc.getStarlevel());//融魂字典
		//VIP 1-3继承60%  4-5 80% 6-7 90% 8+ 100%
		switch(this.viplevel){
		case 0://不是vip
			extexp=exp*mc.getExtexp()/100; //继承经验
			transval=mc.getBreakcharm()*(1+val)+tuc.getCurpointval();			
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		case 1://Vip1
			extexp=exp*60/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip1charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		case 2://Vip2
			extexp=exp*60/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip2charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		case 3://Vip3
			extexp=exp*60/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip3charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		case 4://Vip4
			extexp=exp*80/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip4charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		case 5://Vip5
			extexp=exp*80/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip5charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		case 6://Vip6
			extexp=exp*90/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip6charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		case 7://Vip7
			extexp=exp*90/100; //继承经验
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip7charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		case 8://Vip8
		case 9://Vip9
		case 10://Vip10
		case 11://Vip11
		case 12://Vip12
			extexp=exp; //继承经验100%
			transval=(int)(mc.getBreakcharm()*(1+val)*mc.getVip8charm()/100)+tuc.getCurpointval();
			extval=uc.CheckCardVal(transval); //预先计算获得魅力点数
			break;
		}		
		CardLogicProc clogic = new CardLogicProc();
		upflag=clogic.CalcExp(uc, extexp);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD",PacketCommandType.RETURNMERGEINFO);
		sendMap.put("AA",uc.getCardid());//卡牌ID
		sendMap.put("AB",tuc.getCardid());//散魂卡牌id
		sendMap.put("AC",extexp);//散魂提供经验
		sendMap.put("AD",extval);//散魂提供魅力
		sendMap.put("AE",upflag);//融魂卡升级数
		if(uc.getCurvalue()+extval>99) extval=99-uc.getCurvalue();
		sendMap.put("AF",extval);//融魂卡获得魅力
		sendMap.put("CE",uc.getCurpointval());//融魂后剩余魅力点数
		sendMap.put("CF",tuc.getCurpointval());//散魂卡当前魅力点数
		this.send(sendMap);
	}
	//融魂
	public void mergecard(Map<String, Object> _umap){
		int cid = Integer.parseInt(_umap.get("AA").toString());//融魂卡
		int tid = Integer.parseInt(_umap.get("BB").toString());//散魂卡
		UserCard uc = this.getCardlist().get(cid);
		UserCard tuc = this.getCardlist().get(tid);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		CardLogicProc clogic = new CardLogicProc();
		try {
			if(tuc.getIfuse()>0&&tuc.getLocate()>=0) {
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1035"); //融魂失败
			}else{
				clogic.mergecard(this, uc,tuc);
				sendMap.put("CMD",PacketCommandType.CARDMERGESUC);
				sendMap.put("AA",uc.getCardid());//卡牌ID
				sendMap.put("AB",tid);//散魂id
				sendMap.put("AC",uc.getCardlevel());//融魂后等级
				sendMap.put("AD",uc.getCurvalue());//融魂后魅力值
				sendMap.put("AE",uc.getCurstyle());//融魂后形态
				sendMap.put("CE",uc.getCurpointval());//融魂后剩余魅力点数
				sendMap.put("CF",0);//散魂卡当前魅力点数
			}			
			this.send(sendMap);
		}catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1014"); //融魂失败
			this.send(smap);
			e.printStackTrace();
		}
		
	}
	//技能修炼
	public void produceskill(Map<String, Object> _umap){
		String skillcode=_umap.get("AA").toString();
		//logger.info("sp:"+skillcode);
		SkillPages sp=this.skillpagelist.get(skillcode);
		CardLogicProc clogic = new CardLogicProc();
		try {
			int flag=0;
			switch(sp.getPagenum()){
			case 3:
				if(sp.getPage1()<1||sp.getPage2()<1||sp.getPage3()<1){
					flag=1;
				}
				break;
			case 4:
				if(sp.getPage1()<1||sp.getPage2()<1||sp.getPage3()<1||sp.getPage4()<1){
					flag=1;
				}
				break;
			case 5:
				if(sp.getPage1()<1||sp.getPage2()<1||sp.getPage3()<1||sp.getPage4()<1||sp.getPage5()<1){
					flag=1;
				}
				break;
			}
			Map<String,Object> sendMap = new HashMap<String,Object>();
			if(flag>0){
				sendMap.put("EC", "1037");
				sendMap.put("CMD", PacketCommandType.GETERROR);
			}else{
				clogic.produceskill(this,sp);
				sendMap.put("AA", sp.getSkillcode());
				sendMap.put("CMD",PacketCommandType.PROSKILLSUC);
			}			
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1015"); //技能修炼失败
			this.send(smap);
			e.printStackTrace();
		}		
	}
	//技能合成
	public void unionskill(Map<String, Object> _umap){
		int skid=Integer.parseInt(_umap.get("AA").toString());
		int tskid=Integer.parseInt(_umap.get("BB").toString());
		UserSkill sk=this.uskilllist.get(skid);
		UserSkill tsk=this.uskilllist.get(tskid);
		CardLogicProc clogic = new CardLogicProc();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			if(sk.getSkillcode().equals(tsk.getSkillcode())){
				if(sk.getSkillgrade()==50){
					map.put("CMD", PacketCommandType.GETERROR);
					map.put("EC", "1065");//技能已到最高级
				}else{
					if(tsk.getCardid()>0){
						map.put("CMD", PacketCommandType.GETERROR);
						map.put("EC", "1045");//已装备技能不能合成
					}else{
						DecimalFormat df = new DecimalFormat("0.00");
						clogic.unionskill(this,sk,tsk);
						map.put("AA", sk.getSkillgrade());
						map.put("CMD", PacketCommandType.UNIONSKILLSUC);
						int perhurt=0,hurtadd=0;
						perhurt=(int)(sk.getHurtpert()+sk.getUphurtper()*sk.getSkillgrade())-1;
						//（伤害百分比+Lv*百分比增幅）+$（附加值+Lv*附加值增幅）
						hurtadd=(int)(sk.getHurt()+sk.getUphurt()*sk.getSkillgrade());
						if(sk.getSkilltype().equals("被动")) {
							if(sk.getShp()>0) map.put("EM", "命+"+df.format((sk.getShp()+sk.getUphurtper()*sk.getSkillgrade())*100)+"%");
							if(sk.getSatk()>0) map.put("EM", "攻+"+df.format((sk.getSatk()+sk.getUphurtper()*sk.getSkillgrade())*100)+"%");
							if(sk.getSdef()>0) map.put("EM", "防+"+df.format((sk.getSdef()+sk.getUphurtper()*sk.getSkillgrade())*100)+"%");
						}
						else {
							String sendstr="";
							if(perhurt>0) {
								sendstr=perhurt+"%";
								if(hurtadd>0) sendstr=sendstr+"+"+hurtadd;
							}else{
								if(hurtadd>0) sendstr=Integer.toString(hurtadd);
							}
							map.put("EM", sendstr);
						}
					}		
				}					
			}else{
				map.put("CMD", PacketCommandType.GETERROR);
				map.put("EC", "1046");//不同技能不能合成
			}
			this.send(map);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1016"); //技能合成失败
			this.send(smap);
			e.printStackTrace();
		}		
	}
	//进入任务
	public void actmission(Map<String, Object> _umap){
		String sc=_umap.get("AA").toString();
		int sceneno=0;
		if(sc.equals("NO")){
			sceneno=GameManager.getInstance().getMissionscene().get(this.curmission);
		}else
			sceneno=Integer.parseInt(sc); //用户选择场景
		MissionLogic ml = new MissionLogic();
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(ml.actmission(this,sendMap,sceneno)>0) sendMap.put("CMD", PacketCommandType.RETURNMISSION);
		else{
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1017"); //场景未开启
		}
		this.send(sendMap);
		//logger.info("返回任务包");
		
	}
	//开始战斗
	public void runmission(Map<String, Object> _umap){
		int missid=Integer.parseInt(_umap.get("AA").toString());
		UserMission um=this.getUmisson().get(missid);//用户关卡
		MissionLogic ml = new MissionLogic();
		//logger.info("当前任务副本状态："+um.getIfcopy());
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			int usemp=0;
			if(um.getIfcopy()>0)
				usemp=um.getCopymp();
			else
				usemp=um.getMissionmp();
			if(um.getIfcanuse()<=0){//关卡未开启
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1022");
			}else
			if(um.getCurnum()<1){//任务次数已用完
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1021");
			}else	
			if(this.power<usemp){//体力值不够
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1023");
			}else{
				ml.RunMission(this,um,sendMap);//战斗
				sendMap.put("CMD", PacketCommandType.RETURNRESULT);
			}			
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1018"); //战斗异常
			this.send(smap);
			e.printStackTrace();
		}
		
	}
	
	//连续战斗
	public void runmissions(Map<String, Object> _umap){
		int missid=Integer.parseInt(_umap.get("AA").toString());
		int times = 0;
		UserMission um=this.getUmisson().get(missid);//用户关卡
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(this.level < 20){//20级以上可以
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1086");//20级才可连闯
		}else if(!um.getAssess().equals("3")){//此关卡非3星不能连续战斗
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1077");//不足3星
		}else if(um.getCurnum() > 10){
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1078");//战斗次数必须小于10次
		}else if(this.power < 1){
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1023");//体力不足
		}else{
			 times = this.power <= um.getCurnum() ? this.power : um.getCurnum();
		}	
		try{ 
			if(times > 0){
				MissionLogic ml = new MissionLogic();
				ArrayList<HashMap<String,Object>> fightResults = new ArrayList<HashMap<String,Object>>();
				ArrayList<UserCard> ul=new ArrayList<UserCard>();
				for(int i=0;i<6;i++){ //战斗阵容
					if(this.getCardlocid()[i]<=0) break;
					UserCard uu=this.getCardlist().get(this.getCardlocid()[i]);
					uu.setBcardlevel(uu.getCardlevel());
					ul.add(uu);
				}
				int sumtempexp=0,summoney=0;
				for(int i = 0;i<times;i++){
					HashMap<String,Object> temp = new HashMap<String,Object>();
					int oldlevel=this.level;
					ml.FinishBattle(1, this, um, temp, ul,0);//0连闯 
					if(this.level>oldlevel){//升级
						sendMap.put("EA",temp.get("EA").toString());
						sendMap.put("EB",temp.get("EB").toString());
						sendMap.put("EC",temp.get("EC").toString());
						sendMap.put("ED",temp.get("ED").toString());
						sendMap.put("EE",temp.get("EE").toString());
						sendMap.put("EF",temp.get("EF").toString());
						sendMap.put("LR",temp.get("LR").toString());
					}
					fightResults.add(temp);
					//logger.info("连续战斗数据掉落数据："+fightResults.toString());
					int tempexp=um.getGiveexp();
					sumtempexp=sumtempexp+tempexp;
					summoney=summoney+um.getGivemoeny();					
				}
				sendMap.put("AD", sumtempexp);
				sendMap.put("AR", summoney);
				ArrayList<HashMap<String, Object>> fightLists = new ArrayList<HashMap<String, Object>>();
				for(int i=0;i<6;i++){ //战斗阵容
					if(this.getCardlocid()[i]<=0) break;
					UserCard uu=this.getCardlist().get(this.getCardlocid()[i]);
					HashMap<String,Object> after = new HashMap<String,Object>();
					after.put("DA", uu.getCardid());
					after.put("DB", uu.getLocate());
					after.put("DC", uu.getBcardlevel());
					after.put("DD", uu.getCardlevel());
					fightLists.add(after);
					//logger.info("连续战斗数据阵容数据："+fightLists.toString());
				}
				sendMap.put("AL", fightLists.toArray());//战斗阵容前后变化
				sendMap.put("BA", fightResults.toArray());//战斗掉落物品
				sendMap.put("CA", String.valueOf(missid));//关卡id
				sendMap.put("CB", um.getCurnum());//当前关卡剩余次数***
				sendMap.put("CC", this.power);//当前剩余体力
				sendMap.put("AS", this.level);//当前等级
				sendMap.put("AV", this.getExp());//用户当前经验
				sendMap.put("AU", this.getLevelup());//用户升级经验
				sendMap.put("AW", this.members);//当前阵容
				sendMap.put("CF", this.exp);//当前经验
				sendMap.put("CG", this.money);//当前钞票
				sendMap.put("CH", this.getGold());//当前金条	
				sendMap.put("AJ", this.ball);//当前转生丹
				this.setTaskcdval((int)(System.currentTimeMillis()/1000));
				GameServerDao dao = new GameServerDao();
				dao.UpdateTaskCD(this);
				sendMap.put("CI", this.getTaskcdval()+Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AK").toString()));//冷却时间
				sendMap.put("CMD",PacketCommandType.RETURNRUNMISSIONS);
				//logger.info("连续战斗数据总数据："+sendMap.toString());
			}
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1018"); //连续战斗异常
			this.send(smap);
			e.printStackTrace();
		}
	}
	
	//获取夺技用户列表
	public void getSkillList(Map<String, Object> _umap){
		String skcode=_umap.get("AA").toString();
		int pageno=Integer.parseInt(_umap.get("BB").toString());
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			GameServerDao dao=new GameServerDao();
			dao.getSkillList(this,skcode,pageno,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNGETSKILL);
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1019"); //获取夺技玩家列表异常
			this.send(smap);
			e.printStackTrace();
		}		
	}
	//夺技战斗
	public void actGetskill(Map<String, Object> _umap){
		String skcode=_umap.get("AA").toString();
		int pageno=Integer.parseInt(_umap.get("BB").toString());
		String rname=_umap.get("CC").toString();
		int enlevel=Integer.parseInt(_umap.get("DD").toString());
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			if(this.getbook<=0){//夺技次数用完
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1024");
			}else if(GameManager.getInstance().getLockgetbook().containsKey(rname)||GameManager.getInstance().getLockrank().containsKey(this.urole)){//已有人锁定
				sendMap.put("CMD",PacketCommandType.GETERROR);
				sendMap.put("EC", "1020"); //夺计冲突	
			}else{
				GameManager.getInstance().getLockgetbook().put(this.urole, 1);
				GameManager.getInstance().getLockgetbook().put(rname, 1);
				//判断是否还有技能书页
				GameServerDao dao = new GameServerDao();
				if(dao.checkpgnum(this.svrcode,rname,skcode,pageno)<=0){
					sendMap.put("CMD",PacketCommandType.GETERROR);
					sendMap.put("EC", "1072"); //对方书页已被其他玩家夺走
				}else{
					UPkLogic pklogic=new UPkLogic();
					pklogic.actpk(this,rname,skcode,pageno,enlevel,sendMap);
					sendMap.put("CMD", PacketCommandType.RETURNPKSKILL);
				}				
			}			
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //夺技异常
			this.send(smap);
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//获取决斗用户列表
	public void getUserFightList(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			GameServerDao dao=new GameServerDao();
			dao.getUserFightList(this,sendMap);
			sendMap.put("CMD", PacketCommandType.FIGHTULIST);
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //数据库异常
			this.send(smap);
			e.printStackTrace();
		}		
	}
	//获取反击用户列表
	public void getUserFightBackList(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			GameServerDao dao=new GameServerDao();
			dao.getUserFightBackList(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNFIGHTBACK);
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //数据库异常
			this.send(smap);
			e.printStackTrace();
		}		
	}
	
	//决斗
	public void startfight(Map<String, Object> _umap){
		String rname=_umap.get("AA").toString();
		int crank=Integer.parseInt(_umap.get("BB").toString());
		int ctype=Integer.parseInt(_umap.get("CC").toString());
		int otherlevel=Integer.parseInt(_umap.get("DD").toString());
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			if(this.pknum<=0){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1025");//决斗次数不足
			}else{
				if(!rname.equals(this.urole)){
					if(GameManager.getInstance().getLockrank().containsKey(rname)||GameManager.getInstance().getLockrank().containsKey(this.urole)){//已有人锁定
						sendMap.put("CMD",PacketCommandType.GETERROR);
						sendMap.put("EC", "1026"); //决斗冲突
					}else{
						GameManager.getInstance().getLockrank().put(rname, 1);
						GameManager.getInstance().getLockrank().put(this.urole,1);
						//检查此决斗对手现排名及等级 排名低于自己不打
						GameServerDao dao=new GameServerDao();
						//刷新自己最新排名
						dao.getUserCurrank(this);
						if(dao.CheckFightUser(this, rname, crank)>0){
							CUserFight cuf= new CUserFight();
							cuf.ActFight(this, rname, ctype, crank, sendMap,otherlevel);
							sendMap.put("CMD", PacketCommandType.FINISHFIGHT);
						}else{
							GameManager.getInstance().getLockrank().remove(rname);
							GameManager.getInstance().getLockrank().remove(this.urole);
							sendMap.put("CMD",PacketCommandType.GETERROR);
							sendMap.put("EC", "1074"); //排名发生变化
						}						
					}
				}else{	
					sendMap.put("CMD",PacketCommandType.GETERROR);
					sendMap.put("EC", "1036"); //不可和自己PK
				}
			}
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //数据库异常
			this.send(smap);
			e.printStackTrace();
		}
		catch(Exception e){
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD",PacketCommandType.GETERROR);
			smap.put("EC", "1026"); //决斗冲突
			this.send(smap);
			e.printStackTrace();
		}
	}
	//好友切磋
	public void friendmatch(Map<String, Object> _umap){
		String rname=_umap.get("AA").toString();
		int enlevel=Integer.parseInt(_umap.get("DD").toString());
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			if(this.friendpknum>=this.friendsum){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1088");//好友pk次数已到上限
			}else{
				UPkLogic upk= new UPkLogic();
				upk.friendmatch(this, rname,enlevel,sendMap);
				sendMap.put("CMD", PacketCommandType.FINISHMATCH);
			}			
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //数据库异常
			this.send(smap);
			e.printStackTrace();
		}	
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//获取蛋列表
	public void getEgg(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			GameServerDao dao = new GameServerDao();
			dao.getEgg(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNEGG);
			this.send(sendMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//开蛋
	public void openEgg(Map<String, Object> _umap){
		String ecode=_umap.get("AA").toString();
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			int flag=0;
			int k=(int)(System.currentTimeMillis()/1000);
			GameServerDao dao = new GameServerDao();
			UserEgg egg = this.useregg.get(ecode);
			if(egg.getStarttime()>0){//计时中
				if((k-egg.getStarttime())>=egg.getStepsec()){//时间到可以开
					egg.setIfopen(1);
					egg.setRemaintime(0);
				}else{
					egg.setIfopen(0);
					egg.setRemaintime(egg.getStepsec()+egg.getStarttime()-k);
				}
			}else{
				if(egg.getRemainnum()>0)	egg.setIfopen(1); 
				else egg.setIfopen(0);				
			}						
			if(egg.getIfopen()==0){
				flag=1;//金条开蛋
			}
			if(this.getGold()<egg.getCost()&&flag>0){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1028");
			}else{
				int starno=this.openEggDown(egg);
				dao.openEgg(this,starno,egg,sendMap,flag);
				sendMap.put("CMD", PacketCommandType.OPENRESULT);	
			}
			this.send(sendMap);
		} catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");
			this.send(sendMap);
		}
	}
	public int openEggDown(UserEgg egg){
		int starno=0;
		if(egg.getIfopen()>0){//可以开蛋
			egg.setIfopen(0);
			if(egg.getDaynum()>0) {//标示此蛋每日开蛋有次数限制,用完不计时
				if(egg.getRemainnum()>0){
					egg.setRemainnum(egg.getRemainnum()-1);
					if(egg.getRemainnum()==0) egg.setStarttime(0);
					else egg.setStarttime((int)(System.currentTimeMillis()/1000));
				}else{
					egg.setStarttime(0);
					egg.setRemainnum(0);
				}
			}else{//此类蛋，无次数限制
				if(egg.getRemainnum()==1&&egg.getEggcode().equals("e3")) {//金蛋第一次免费开蛋掉4星卡
					egg.setStarttime((int)(System.currentTimeMillis()/1000));
					egg.setRemainnum(0);
					egg.setRemaintime(egg.getStepsec());
					//logger.info("首次开蛋掉："+4);
					return 4;
				}else{
					egg.setStarttime((int)(System.currentTimeMillis()/1000));
					egg.setRemainnum(0);
				}
				
			}
			if(egg.getStarttime()>0){//计时中
				int k=(int)(System.currentTimeMillis()/1000);
				egg.setRemaintime(egg.getStepsec()+egg.getStarttime()-k);
			}
		}else{//直接金条开蛋
			egg.setOpennum(egg.getOpennum()+1);
			if(egg.getFirstdown()>0){
				//logger.info("金条首开："+firstdown);
				if(egg.getOpennum()==1){
					return egg.getFirstdown();
				}
			}
		}
		int[] a={egg.getStar1(),egg.getStar2(),egg.getStar3(),egg.getStar4(),egg.getStar5(),egg.getStar6()};
		/*int[] loc={0,1,2,3,4,5};
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
		}*/
		int maxval=0;
	    int minval=0;
	    
		Random rd = new Random();
		int k=rd.nextInt(1000);
		for(int i=0;i<a.length;i++){
			minval=maxval;
			if(a[i]==0) continue;
			maxval=maxval+a[i];
			if(k>=minval && k<maxval){
				//starno=loc[i];
				starno=i;
				break;
			}
		}
		return starno+1;
	}
	//出售装备
	public void devsale(Map<String,Object> _umap){
		int did=Integer.parseInt(_umap.get("AA").toString());
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(this.udevicelist.get(did).getCardid()>0){//装备中的道具不能出售
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1035"); //不可出售
			
		}else{
			try{
				GameServerDao dao = new GameServerDao();
				dao.devsale(this,did);
				this.udevicelist.remove(did);
				sendMap.put("CMD", PacketCommandType.DEVSALED);
				sendMap.put("AA", did); //卖出
				sendMap.put("AB", this.money);
			}catch (SQLException e) {
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1099"); //数据异常
				logger.error("deviceid="+did+e.getMessage());
			}
		}
		this.send(sendMap);
	}
	//获取好友列表
	public void getFriends(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			dao.getFriends(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNFRIEND);
			this.send(sendMap);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	//搜索
	public void FindPlayer(String rn){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			dao.FindPlayer(this.svrcode,rn,sendMap,this);
			sendMap.put("CMD", PacketCommandType.RETROLELIST);
			this.send(sendMap);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	//申请好友
	public void ApplyPlayer(String prole){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			if(dao.ApplyPlayer(this,prole,sendMap)<=0){
				sendMap.put("CMD", PacketCommandType.GETERROR);
			}else sendMap.put("CMD", PacketCommandType.REQSUCC);
			this.send(sendMap);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//获取待审列表
	public void GetApplyList(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			dao.GetApplyList(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RTNAPPLYLIST);
			this.send(sendMap);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//审核好友
	public void verifyFriend(String frole,int verify){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			int flag=dao.verifyFriend(this,frole,verify);
			if(flag==0){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1043"); //好友数已满
			}else if(flag==-1){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1071"); //对方好友数已满
			}else sendMap.put("CMD", PacketCommandType.ADDSUCC);
			this.send(sendMap);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据异常
			this.send(sendMap);
			e.printStackTrace();
		}
	}
	//删除好友
	public void DelFriend(String prole){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			dao.DelFriend(this,prole,sendMap);
			sendMap.put("CMD", PacketCommandType.DELSUCC);
			this.send(sendMap);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//数据库异常
			this.send(sendMap);
			e.printStackTrace();
		}
	}
	public void CheckShop(Map<String,Object> sendMap){
		int flag;
		ArrayList<Map<String,Object>> al=new ArrayList<Map<String,Object>>();
		for(int i=0;i<this.usershop.size();i++){
			flag=0;
			int nowtime=(int)(System.currentTimeMillis()/1000);
			ShopClass sc = this.usershop.get(i);
			switch (sc.getBuycond()){
			case 1://普通限时购买
				if(nowtime<sc.getBuystart()||nowtime>=sc.getBuyend())	flag=1;
				if(nowtime>=sc.getBuystart()&&nowtime<sc.getBuyend()){
					sc.setBuystart(sc.getBuyend()-nowtime);//记录距离下架时间
				}
				break;
			case 2://普通限次购买
				if(nowtime<sc.getBuystart()||nowtime>=sc.getBuyend()||sc.getBuysum()<=0)	flag=1;		
				if(nowtime>=sc.getBuystart()&&nowtime<sc.getBuyend()){
					sc.setBuystart(sc.getBuyend()-nowtime);//记录距离下架时间
				}
				break;
			case 4://冷却限次
				if(sc.getBuysum()<=0){
					if(nowtime-sc.getBuyend()-sc.getBuystart()>=0){//冷却结束可以购买
						sc.setBuystart(0);
						sc.setBuysum(1);
					}else{
						sc.setBuystart(sc.getBuystart()-nowtime+sc.getBuyend());//剩余冷却时间
						sc.setBuysum(0);
					}
				}
				break;
			}
			if(flag==1) continue;
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("SS", sc.getOrderinx());
			map.put("AB", sc.getGoodscode());
			map.put("AC", sc.getGoodsname());
			map.put("AD", sc.getGoodsdesc());
			map.put("AE", sc.getGoodstype());
			map.put("AP", sc.getDispic());
			if(sc.getGoodstype()==3) map.put("AR", GameManager.getInstance().getDevsetdict().get(sc.getGoodscode()).getDevcolor());
			else if(sc.getGoodstype()==5) map.put("AR", GameManager.getInstance().getSkilldict().get(sc.getGoodscode()).getSkillcolor());
			else map.put("AR", 0);
			if(this.userprop.containsKey(sc.getGoodscode())){//背包里有此道具,计算拥有的数量
				map.put("AQ", this.userprop.get(sc.getGoodscode()).getPropsnum());
			}else  map.put("AQ", 0);
			if(sc.getGoodstype()==6) map.put("AQ", this.ball);//转生丹
			ArrayList<Map<String,Object>> pl= new ArrayList<Map<String,Object>>();
			if(sc.getGoodstype()==1){//包
				for(int j=0;j<sc.getPackgoods().size();j++){
					Map<String,Object> pmap =new HashMap<String,Object>();
					packageGoods pkg=sc.getPackgoods().get(j);
					pmap.put("BA", pkg.getGoodscode());
					pmap.put("AB", pkg.getGoodsname());
					pmap.put("AC", pkg.getGoodsnum());	
					pmap.put("AP", pkg.getGoodspic());
					if(pkg.getInnertype()==10){//卡牌
						switch(GameManager.getInstance().getCarddict().get(pkg.getGoodscode()).getStarlevel()){
						case 1:
						case 2:
							pmap.put("AR", 1);//白
							break;
						case 3:
						case 4:
							pmap.put("AR", 2);//绿
							break;
						case 5:
							pmap.put("AR", 3);//蓝
							break;
						case 6:
							pmap.put("AR", 4);//紫
							break;
						case 7:
							pmap.put("AR", 5);//橙
							break;							
						}
					}else	if(pkg.getInnertype()==11) pmap.put("AR", GameManager.getInstance().getDevsetdict().get(pkg.getGoodscode()).getDevcolor());
					else if(pkg.getInnertype()==12) pmap.put("AR", GameManager.getInstance().getSkilldict().get(pkg.getGoodscode()).getSkillcolor());
					else pmap.put("AR", 0);
					pl.add(pmap);
				}
			}
			map.put("AM", pl.toArray());
			map.put("AF", sc.getGoodstyle());
			map.put("AG", sc.getGoldp());
			map.put("AH", sc.getBuycond()); //购买限制类
			if(sc.getBuycond()==6) 
				if(this.viplevel>=sc.getBuysum()&&this.viplevel>0) 	map.put("AI",1); //可购买次数
				else map.put("AI", 0);
			else 	map.put("AI", sc.getBuysum()); //可购买次数
			
			if(sc.getDiscount()==100){
				map.put("AJ", 0);
			}else{//折扣价
				if(sc.getBuycond()==5){//限时打折
					if(nowtime>=sc.getBuystart()&&nowtime<sc.getBuyend())
						map.put("AJ", sc.getDiscount()*sc.getGoldp()/100);
					else {
						map.put("AJ", 0);
						map.put("AF", 0);
					}
				}else{
					map.put("AJ", sc.getDiscount()*sc.getGoldp()/100);
				}
				
			}			 
			map.put("AK", sc.getBuystart()); //冷却剩余时间 或时间范围开始
			map.put("AL", sc.getBuyend());  //上次冷却开始时间  或时间范围结束
			al.add(map);
		}	
		Collections.sort(al,new Comparator<Map<String,Object>>(){
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
				return Integer.parseInt(arg0.get("SS").toString())-Integer.parseInt(arg1.get("SS").toString()); }  

		});
		sendMap.put("AA", al.toArray());
	}
	public void getShop() throws ClassNotFoundException, IOException, SQLException{
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			if(this.usershop.size()<=0){
				GameServerDao dao = new GameServerDao();
				dao.getShop(this);
			}
			CheckShop(sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNSHOP);
			this.send(sendMap);
		}catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		
	}
	public void buyGoods(Map<String, Object> _umap) throws ClassNotFoundException, IOException{
		String goodscode=_umap.get("AA").toString();
		int goodsnum=Integer.parseInt(_umap.get("BB").toString());
		Map<String,Object> sendMap = new HashMap<String,Object>();
		int flag=0;
		if(goodsnum<=0){
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据异常
		}else
		{
			for(int i=0;i<this.usershop.size();i++){
				int nowtime=(int)(System.currentTimeMillis()/1000);
				ShopClass sc = this.usershop.get(i).copy();
				if(sc.getGoodscode().equals(goodscode)){
					switch (sc.getBuycond()){
					case 0:
						flag=1;
						break;
					case 1:
						if(nowtime<sc.getBuystart()||nowtime>=sc.getBuyend()){
							sendMap.put("CMD", PacketCommandType.GETERROR);
							sendMap.put("EC", "1038"); //限购时间已过
							this.usershop.remove(i);//移除商品
							i--;
						}else flag=1;
						break;
					case 2:
						if(sc.getBuysum()<=0){
							sendMap.put("CMD", PacketCommandType.GETERROR);
							sendMap.put("EC", "1039"); //限购次数已到
							this.usershop.remove(i);//移除商品
							i--;
						}else{
							flag=1;
							if(sc.getBuysum()-1==0)	{
								this.usershop.remove(i);
								i--;
							}
						}
						break;
					case 3:
						flag=1;
						break;
					case 4:
						if(sc.getBuysum()<=0){
							if(nowtime-sc.getBuyend()-sc.getBuystart()>=0){//冷却结束可以购买
								sc.setBuystart(0);
								sc.setBuysum(0);//本次购买后从新设置冷却时间
								sc.setBuyend(nowtime);
								sc.setBuystart(GameManager.getInstance().getShopdict().get(goodscode).getBuystart());
								flag=1;
							}else{
								sc.setBuystart(sc.getBuystart()-nowtime+sc.getBuyend());//剩余冷却时间
								sc.setBuysum(0);
								sendMap.put("CMD", PacketCommandType.GETERROR);
								sendMap.put("EC", "1040"); //冷却中
							}
						}
						break;
					case 5:
						flag=1;
						break;
					case 6:
						if(this.viplevel>=sc.getBuysum()&&this.viplevel>0){
							sc.setBuysum(0);
							flag=1;//可购买
							this.usershop.remove(i);//移除商品
							i--;
						}else{
							sendMap.put("CMD", PacketCommandType.GETERROR);
							sendMap.put("EC", "1041"); //VIP等级不够
						}
						break;
					}
					int price=0;
					if(sc.getBuycond()==5){//限时折扣
						if(nowtime>=sc.getBuystart()&&nowtime<sc.getBuyend()){
							price=sc.getGoldp()*sc.getDiscount()/100*goodsnum;
						}else price=sc.getGoldp()*goodsnum;
					}else{
						price=sc.getGoldp()*sc.getDiscount()/100*goodsnum;
					}					
					if(this.getGold()<price) {
						flag=0;
						sendMap.put("CMD", PacketCommandType.GETERROR);
						sendMap.put("EC", "1028"); //金条不足
						break;
					}
					if(flag>0){//可以购买
						try{
							GameServerDao dao = new GameServerDao();
							dao.BuyGoods(this,price,goodsnum,sc);
							CheckShop(sendMap);
							sendMap.put("CA", this.getGold());
							sendMap.put("CB", this.ball);
							sendMap.put("CMD", PacketCommandType.BUYSUCC);
						}catch (SQLException e) {
							sendMap.put("CMD", PacketCommandType.GETERROR);
							sendMap.put("EC", "1099"); //数据库异常
							e.printStackTrace();
						}
					}
					break;
				}
			}
		}		
		this.send(sendMap);
	}
	public void drinkapp(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			dao.getCurrDrink(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNDRINK);
			this.send(sendMap);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//畅饮
	public void startdrink(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			if(this.drinknum>0){
				GameServerDao dao = new GameServerDao();
				if(dao.startdrink(this)>0){
					sendMap.put("AA", this.poweradd);
					sendMap.put("AC", this.drinknum);
					sendMap.put("AD", this.power);
					sendMap.put("CMD", PacketCommandType.FINISHDRINK);
				}else{
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1047");//时间未到
				}
			}else{//畅饮次数不足
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1048");//已畅饮过
			}			
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			e.printStackTrace();
		}
		this.send(sendMap);
	}
	//获取关公状态
	public void getguangong(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			dao.getguangong(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNGG);
			this.send(sendMap);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//参拜关公
	public void visitgg(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			if(dao.visitgg(this,sendMap)>0){
				sendMap.put("AE", this.getGold());
				//logger.info(this.gold);
				sendMap.put("AF", this.ball);
				sendMap.put("CMD", PacketCommandType.FINISHVISIT);
			}else{//今日参拜过
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1047");//时间未到
			}			
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
		}
		this.send(sendMap);
	}
	//获取街霸列表
	public void getstreet(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			if(this.ustreet.size()<=0){
				GameServerDao dao = new GameServerDao();
				dao.getstreet(this,sendMap);
			}
			UserStreet ust=this.ustreet.get(this.curstreetid);
			sendMap.put("BA", ust.getStreetid());
			sendMap.put("BB", ust.getCurtime());//挑战时间
			sendMap.put("BC", ust.getWinlose());//胜负
			sendMap.put("BD", ust.getIfcanuse());//是否可用
			sendMap.put("BE", ust.getIfgold());//是否金条重置
			sendMap.put("BF", ust.getGold());//金条重置价
			sendMap.put("BW", ust.getGiveexp());
			sendMap.put("BX", ust.getGivemoeny());
			sendMap.put("BY", ust.getIflock());
			sendMap.put("BJ", ust.getStreetdesc());
			int limitnum=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AC").toString());
			sendMap.put("BG",limitnum);
			sendMap.put("BH",this.resetstreet);
			int temp=0;
			ArrayList<String> tl=new ArrayList<String>(); 
			ArrayList<Integer> colors=new ArrayList<Integer>(); 
			for(int i=0;i<ust.getSnpclist().size();i++){
				CMissionNpcDict mnpc =ust.getSnpclist().get(i);
				temp=temp+1;
				tl.add(mnpc.getNpccode());
				if(mnpc.getNpctype().equals("杂碎")) colors.add(1);
				if(mnpc.getNpctype().equals("精英")) colors.add(GameManager.getInstance().getCarddict().get(mnpc.getNpccode()).getStarlevel());
				if(mnpc.getNpctype().equals("BOSS")) colors.add(GameManager.getInstance().getCarddict().get(mnpc.getNpccode()).getStarlevel());
				if(temp>3) break;
			}
			sendMap.put("BZ", tl.toArray());
			sendMap.put("BR", colors.toArray());
			sendMap.put("CMD", PacketCommandType.RETURNSTREET);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			e.printStackTrace();
		}
		this.send(sendMap);
	}
	//挑战街霸
	public void fightstreet(int fid){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		UserStreet us=this.ustreet.get(fid);//用户街霸
		try {
			MissionLogic ml = new MissionLogic();
			if(us.getIfcanuse()<=0){//关卡未开启
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1022");
			}if(us.getIflock()>0){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1056");//关卡已锁重置前不可再次挑战
			}else{
				ml.RunSteetMission(this,us,sendMap);//街霸挑战
				//logger.info("当前关卡："+this.curstreetid);
				UserStreet ust=this.ustreet.get(this.curstreetid);
				sendMap.put("BA", ust.getStreetid());
				sendMap.put("BB", ust.getCurtime());//挑战时间
				sendMap.put("BC", ust.getWinlose());//胜负
				sendMap.put("BD", ust.getIfcanuse());//是否可用
				sendMap.put("BE", ust.getIfgold());//是否金条重置
				sendMap.put("BF", ust.getGold());//金条重置价
				sendMap.put("BW", ust.getGiveexp());
				sendMap.put("BX", ust.getGivemoeny());
				sendMap.put("BY", ust.getIflock());
				sendMap.put("BJ", ust.getStreetdesc());
				int temp=0;
				ArrayList<String> tl=new ArrayList<String>(); 
				ArrayList<Integer> colors=new ArrayList<Integer>(); 
				for(int i=0;i<ust.getSnpclist().size();i++){
					CMissionNpcDict mnpc =ust.getSnpclist().get(i);
					temp=temp+1;
					tl.add( mnpc.getNpccode());
					if(mnpc.getNpctype().equals("杂碎")) colors.add(1);
					if(mnpc.getNpctype().equals("精英")) colors.add(2);
					if(mnpc.getNpctype().equals("BOSS")) colors.add(3);
					if(temp>3) break;
				}
				sendMap.put("BR", colors.toArray());
				sendMap.put("BZ", tl.toArray());
				sendMap.put("CMD", PacketCommandType.STREETSUCC);
			}			
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //数据异常
			this.send(smap);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void goldResetstreet(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try {
			int cost=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.resetstreet).get("AF").toString());
			int limitnum=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AC").toString());
			if(this.resetstreet>=limitnum){
				sendMap.put("CMD", PacketCommandType.GETERROR);//
				sendMap.put("EC", "1057"); //街霸重置次数已用完
			}else if(this.getGold()<cost){
				sendMap.put("CMD", PacketCommandType.GETERROR);//
				sendMap.put("EC", "1028"); //金币不够
			}else{
				GameServerDao dao = new GameServerDao();
				dao.goldResetstreet(this,cost);
				UserStreet ust=this.ustreet.get(this.curstreetid);
				sendMap.put("BA", ust.getStreetid());
				sendMap.put("BB", ust.getCurtime());//挑战时间
				sendMap.put("BC", ust.getWinlose());//胜负
				sendMap.put("BD", ust.getIfcanuse());//是否可用
				sendMap.put("BE", ust.getIfgold());//是否金条重置
				cost=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.resetstreet).get("AF").toString());
				sendMap.put("BF", cost);//金条重置价
				sendMap.put("BW", ust.getGiveexp());
				sendMap.put("BX", ust.getGivemoeny());
				sendMap.put("BY", ust.getIflock());
				sendMap.put("BG",limitnum);
				sendMap.put("BI",this.getGold());
				sendMap.put("BH",this.resetstreet);
				sendMap.put("BJ", ust.getStreetdesc());
				int temp=0;
				ArrayList<String> tl=new ArrayList<String>(); 
				ArrayList<Integer> colors=new ArrayList<Integer>(); 
				for(int i=0;i<ust.getSnpclist().size();i++){
					CMissionNpcDict mnpc =ust.getSnpclist().get(i);
					temp=temp+1;
					tl.add( mnpc.getNpccode());
					if(mnpc.getNpctype().equals("杂碎")) colors.add(1);
					if(mnpc.getNpctype().equals("精英")) colors.add(2);
					if(mnpc.getNpctype().equals("BOSS")) colors.add(3);
					if(temp>3) break;
				}
				sendMap.put("BZ", tl.toArray());
				sendMap.put("BR", colors.toArray());
				sendMap.put("CMD", PacketCommandType.RESETSUCC);
			}
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //数据异常
			this.send(smap);
			e.printStackTrace();
		}
	}
	public void goldResetmissions(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		int gold = 20;//假设20
		try {
			if(this.taskcdval == 0){
				sendMap.put("CMD", PacketCommandType.GETERROR);//
				sendMap.put("EC", "1079"); //不需要清CD
			}else if(this.getGold()<gold){
				sendMap.put("CMD", PacketCommandType.GETERROR);//
				sendMap.put("EC", "1028"); //金币不够
			}else{
				GameServerDao dao = new GameServerDao();
				dao.goldResetmissions(this,gold);
				sendMap.put("CMD", PacketCommandType.RESETMSTACKCDSUCC);//
				sendMap.put("AA", this.getTaskcdval());//
				sendMap.put("AB", this.getGold());//
			}
			this.send(sendMap);
		} catch (SQLException e) {
			Map<String,Object> smap= new HashMap<String,Object>();
			smap.put("CMD", PacketCommandType.GETERROR);
			smap.put("EC", "1099"); //数据异常
			this.send(smap);
			e.printStackTrace();
		}
	}
	//金条补充
	public void buychance(int buytype){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		int buyflag=0,goldnum=0;
		try{
			switch(buytype){
			case 0://体力
				if(this.goldrenew<Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AG").toString())){
					if(this.goldrenew<3) goldnum=20;
					if(this.goldrenew>=3&&this.goldrenew<6) goldnum=40;
					if(this.goldrenew>=6&&this.goldrenew<9) goldnum=80;
					if(this.goldrenew>=9) goldnum=200;
					if(goldnum>this.getGold()) buyflag=1;//金币不够
					else buyflag=2;
				}			
				break;
			case 1://夺计 修正bug
				if(this.goldaddnum<Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AH").toString())){
					if(this.goldaddnum<3) goldnum=20;
					if(this.goldaddnum>=3&&this.goldaddnum<6) goldnum=40;
					if(this.goldaddnum>=6&&this.goldaddnum<9) goldnum=80;
					if(this.goldaddnum>=9) goldnum=200;
					if(goldnum>this.getGold()) buyflag=1;//金币不够
					else buyflag=2;
				}
				break;
			case 2://争斗
				if(this.buyfight<Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AE").toString())){
					if(this.buyfight<3) goldnum=20;
					if(this.buyfight>=3&&this.buyfight<6) goldnum=40;
					if(this.buyfight>=6&&this.buyfight<9) goldnum=80;
					if(this.buyfight>=9) goldnum=200;
					if(goldnum>this.getGold()) buyflag=1;//金币不够
					else buyflag=2;
				}
				break;
			}
			switch(buyflag){
			case 0:
				sendMap.put("CMD", PacketCommandType.GETERROR);//
				sendMap.put("EC", "1049"); //购买次数上限已到
				break;
			case 1:
				sendMap.put("CMD", PacketCommandType.GETERROR);//
				sendMap.put("EC", "1028"); //金币不够
				break;
			case 2://可以买
				GameServerDao dao = new GameServerDao();
				dao.buychance(this,sendMap,buytype,goldnum);
				sendMap.put("CMD", PacketCommandType.SUCCBUY);
				break;			
			}
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			e.printStackTrace();
		}
		this.send(sendMap);
	}
	public void getpropslist(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		Iterator<UserProps> it = this.userprop.values().iterator();
		ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
		while(it.hasNext()){
			Map<String,Object> map =new HashMap<String,Object>();
			UserProps ups=it.next();
			map.put("EB", ups.getPropscode());
			map.put("EC", ups.getPropsname());
			map.put("EE", ups.getPropsdesc());
			//临时修改，待客户端修改
			if(ups.getPropstype()==4) map.put("EF", 2);
			else map.put("EF", ups.getPropstype());
			map.put("EH", ups.getPropsnum());
			map.put("PP", ups.getPropspic());
			ArrayList<Map<String,Object>> packal = new ArrayList<Map<String,Object>>();
			for(int i=0;i<ups.getPackgoods().size();i++){
				Map<String,Object> packmap = new HashMap<String,Object>();
				packageGoods pg=ups.getPackgoods().get(i);
				packmap.put("AA", pg.getGoodscode());
				packmap.put("AB", pg.getGoodsname());
				packmap.put("AC", pg.getGoodsnum());
				packmap.put("AD", pg.getInnertype());
				if(pg.getInnertype()==10){//卡牌
					switch(GameManager.getInstance().getCarddict().get(pg.getGoodscode()).getStarlevel()){
					case 1:
					case 2:
						 packmap.put("AR", 1);//白
						break;
					case 3:
					case 4:
						 packmap.put("AR", 2);//绿
						break;
					case 5:
						 packmap.put("AR", 3);//蓝
						break;
					case 6:
						 packmap.put("AR", 4);//紫
						break;
					case 7:
						 packmap.put("AR", 5);//橙
						break;							
					}
				}else if(pg.getInnertype()==15){	//碎片
					ArrayList<ChipDict> tal=GameManager.getInstance().getChipdictlist();
					for(int j=0;j<al.size();j++){
						if(tal.get(i).getChipcode().equals(pg.getGoodscode())){
							ChipDict cd= tal.get(j);
							map.put("AR", cd.getChipcolor());//颜色
							break;
						}
					}
				}else if(pg.getInnertype()==11) packmap.put("AR", GameManager.getInstance().getDevsetdict().get(pg.getGoodscode()).getDevcolor());
						else if(pg.getInnertype()==12) packmap.put("AR", GameManager.getInstance().getSkilldict().get(pg.getGoodscode()).getSkillcolor());
							else packmap.put("AR", 0);
				packmap.put("AP", pg.getGoodspic());
				packal.add(packmap);
			}
			map.put("EG",packal.toArray());
			map.put("EI", ups.getUsecond());
			al.add(map);
		}
		sendMap.put("EA", al.toArray());
		sendMap.put("CMD", PacketCommandType.RETURNPROPS);
		this.send(sendMap);
	}
	public void useprops(String propcode) {
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		UserProps up= this.userprop.get(propcode);
		try{
			int flag=1;
			if(up.getPropsnum()==0){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1050"); //此道具数量为0
			}else{
				if(up.getPropstype()==2){
					if(this.userprop.containsKey(up.getUsecond())){
						flag=0;
					}else{
						sendMap.put("CMD", PacketCommandType.GETERROR);
						sendMap.put("EC", "1051"); //开宝箱没有钥匙
						/*sendMap.put("EB", up.getUsecond());//返回钥匙code	
						this.getShop();
						sendMap.put("EF", -1);//需要钥匙
						for(int i=0;i<this.usershop.size();i++){
							ShopClass sc = this.usershop.get(i);
							if(sc.getGoodscode().equals(up.getUsecond())){
								sendMap.put("PP", sc.getGoodsname());//钥匙名称
								sendMap.put("KP", sc.getGoldp());//钥匙单价
								break;
							}
						}
						sendMap.put("CMD", PacketCommandType.USEDPROPS);*/
					}
				}else{
					flag=0;
				}
				if(flag<1){
					dao.useprops(this,up,sendMap);
					sendMap.put("EB", propcode);
					sendMap.put("PP", up.getPropspic());
					sendMap.put("CMD", PacketCommandType.USEDPROPS);
				}				
			}			
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			logger.error("propcode="+propcode+"---"+e.getMessage());
		} /*catch (ClassNotFoundException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			logger.error(e.getMessage());
		} catch (IOException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			logger.error(e.getMessage());
		}*/
		this.send(sendMap);
	}
	//兑换
	public void exchstart(String exchcode){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try{
			String batnostr=exchcode.substring(0, 4);
			int batno=Integer.parseInt(batnostr);
			if(batno==0) {
				if(exchcode.substring(4,exchcode.length()).equals(this.uname)){
					dao.excholduser(this,exchcode,sendMap);
				}else{
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1066"); //兑换码不合法
				}
				
			}else 
				dao.exchstart(this,exchcode,sendMap);
			
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			e.printStackTrace();
		}catch (Exception e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1066"); //
			//e.printStackTrace();
		}
		this.send(sendMap);
	}
	//获取日常任务列表
	public void getdaytask(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			if(this.userdaytasklist.size()==0){
				GameServerDao dao = new GameServerDao();
				dao.getdaytask(this);
			}
			ArrayList<Map<String,Object>> al= new ArrayList<Map<String,Object>>();
			for(int i=0;i<this.userdaytasklist.size();i++){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("AA", this.userdaytasklist.get(i).getTaskid());
				map.put("AB", this.userdaytasklist.get(i).getTaskdesc());
				map.put("AC", this.userdaytasklist.get(i).getTaskcond());
				map.put("AD", this.userdaytasklist.get(i).getTasktype());
				map.put("AE", this.userdaytasklist.get(i).getCurcond());
				map.put("AF", this.userdaytasklist.get(i).getIfsuccess());
				map.put("AG", this.userdaytasklist.get(i).getMoney());
				map.put("AH", this.userdaytasklist.get(i).getBall());
				map.put("AI", this.userdaytasklist.get(i).getGold());
				if(this.userdaytasklist.get(i).getCurcond()-this.userdaytasklist.get(i).getTaskcond()<0){
					map.put("BA",  this.userdaytasklist.get(i).getTaskid());
				}else{
					map.put("BA", -1);
				}
				
				al.add(map);
			}
			Collections.sort(al,new Comparator<Map<String,Object>>(){
				public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
					return Integer.parseInt(arg0.get("BA").toString())-Integer.parseInt(arg1.get("BA").toString()); }  

			});
			sendMap.put("TT", al.toArray());
			sendMap.put("CMD", PacketCommandType.RETURNDAYTASK);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			e.printStackTrace();
		}
		this.send(sendMap);
		
	}
	// 执行日常任务 任务id
	public void rundaytask(int tid){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		for(int i=0;i<this.userdaytasklist.size();i++){
			UserDayTask udt=this.userdaytasklist.get(i);
			if(udt.getTaskid()==tid){	
				if(udt.getCurcond()<udt.getTaskcond()){
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1055"); //条件不足
				}else if(udt.getIfsuccess()>0){
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1063"); //已领过奖励
				}else{
					GameServerDao dao = new GameServerDao();
					try{
						dao.rundaytask(this,udt);
						udt.setIfsuccess(1);
						sendMap.put("AA", udt.getTaskid());
						sendMap.put("AF", udt.getIfsuccess());
						sendMap.put("AJ",this.money);
						sendMap.put("AK",this.ball);
						sendMap.put("AL",this.undotask);
						sendMap.put("AI",this.getGold());
						sendMap.put("CMD", PacketCommandType.FINDAYTASK);
					}catch (SQLException e) {
						sendMap.put("CMD", PacketCommandType.GETERROR);
						sendMap.put("EC", "1099"); //数据库异常
						e.printStackTrace();
					}					
				}				
				break;
			}
		}		
		this.send(sendMap);
		
	}
	//获取图鉴列表
	public void getpiclist(int pgnum){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try{
			dao.getpiclist(this,sendMap,pgnum);
			sendMap.put("CMD", PacketCommandType.RETURNPICS);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			e.printStackTrace();
		}					
		this.send(sendMap);
	}
	//摇钱树
	public void shaketree(){
		int flagcost=0;//摇钱消耗
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(this.goldtree>=this.maxgoldtree){//摇钱次数限制到
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1058"); //每日摇钱次数限额
		}else if(this.getGold()>=this.treegold){//钱够
				flagcost=this.treegold;
				this.setGold(this.getGold()-flagcost);//消耗金币
				this.goldtree=this.goldtree+1;//记录摇钱次数
				this.treegold=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.goldtree+1).get("AA").toString());//摇钱费用
				this.maxgoldtree=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AB").toString());//最大摇钱次数
				// 120000 * this.level /(this.level+100)*(1 + 0.02 * this.goldtree)
				int downmoney=(int)(120000 * this.level/(this.level+100)*(1+0.02 * this.goldtree));
				//（25 *（等级/2 + 5）^2 + 等级 * 600 – 800） * (1 + 0.02 * 次数)
				this.money=this.money+downmoney;
				//logger.info("摇钱次数："+this.goldtree+"----掉落钱："+downmoney);
				GameServerDao dao = new GameServerDao();
				try{
					dao.shaketree(this,flagcost);
					sendMap.put("AA", this.goldtree);
					sendMap.put("AB", this.maxgoldtree);
					sendMap.put("AC", this.treegold);
					sendMap.put("AD", this.getGold());
					sendMap.put("AE", this.money);
					sendMap.put("CMD", PacketCommandType.SHAKETREEFIN);
				}catch (SQLException e) {
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1099"); //数据库异常
					logger.info(e.getMessage());
				}		
			}else{
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1028"); //金条不足
			}			
		
		this.send(sendMap);
	}
	public void getpointshoplist(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		Iterator<Cpointdict> it = GameManager.getInstance().getCpdict().values().iterator();
		ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>(); 
		while(it.hasNext()){
			Cpointdict cpd=it.next();
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("BA",cpd.getExchid());
			map.put("BB",cpd.getExchcode());
			map.put("BC",cpd.getExchdesc());
			if(cpd.getGoodstype()>0){//兑换物品
				if(this.curpoints>=cpd.getTakecond()) map.put("BD", 2);//显示兑换按钮
				else map.put("BD", 0);
			}
			if(cpd.getGoodstype()<=0){//领取奖励类型，判断是否可领取
				try{
					if(this.rankseq<=cpd.getTakecond()){
						GameServerDao dao = new GameServerDao();
						map.put("BD", dao.checkpointaward(this,cpd.getExchid()));
					}else map.put("BD", 0);
					
				}catch (SQLException e) {
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1099"); //数据库异常
					e.printStackTrace();
				}
			}
			map.put("BE",cpd.getTakecond());
			map.put("BF",cpd.getCostpoint());
			al.add(map);
		}
		Collections.sort(al,new Comparator<Map<String,Object>>(){
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
				return Integer.parseInt(arg1.get("BA").toString())-Integer.parseInt(arg0.get("BA").toString()); }  

		});
		sendMap.put("AA", al.toArray());
		sendMap.put("AB", this.curpoints);
		sendMap.put("CMD",PacketCommandType.POINTLIST);
		this.send(sendMap);
	}
	public void exchangepoint(Map<String,Object> _umap){
		int exchid=Integer.parseInt(_umap.get("AA").toString());
		int extype=Integer.parseInt(_umap.get("AB").toString());
		Cpointdict cpd=GameManager.getInstance().getCpdict().get(exchid);
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			GameServerDao dao = new GameServerDao();
			if(extype<=0){//领取排名奖励
				dao.getpointaward(this,cpd,sendMap);
			}else{//积分兑换
				dao.exchangepoint(this,cpd,sendMap);
			}
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099"); //数据库异常
			e.printStackTrace();
		}		
		this.send(sendMap);
	}
	public void getrebacktime(){
		long cursecond=System.currentTimeMillis()/1000;
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("CMD",PacketCommandType.UPDATEMESS);
		sendMap.put("AA", this.power);
		int k=0;
		if(this.powerstart>0) k=  this.perpoint-(int)cursecond+this.powerstart;
		else k=0;
		sendMap.put("AB",k);
		sendMap.put("AC", this.getbook);
		if(this.getskstart>0) k=this.everyskp-(int)cursecond+this.getskstart;
		else k=0;
		sendMap.put("AD",k );
		this.send(sendMap);
	}
	public void getmailbox(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try {
			dao.getmailbox(this);
			Iterator<UserMailBox> it = this.usermailbox.values().iterator();
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>(); 
			while(it.hasNext()){
				UserMailBox umb = it.next();
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("AB",umb.getMailid());
				map.put("AC",umb.getMesscont());
				map.put("AE", umb.getMailtop());
				if(umb.getIfuse()==1) map.put("AD", 0); //需主动删除的
				else if((umb.getGoodscode().equals("@")&&umb.getGivegold()==0&&umb.getGivemoney()==0)){//文本信件
					map.put("AD", 0);//需主动删除的
				}else map.put("AD",1);
				al.add(map);
			}
			sendMap.put("AA", al.toArray());
			sendMap.put("CMD", PacketCommandType.RETURNMAILBOX);
		} catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");			
		}
		this.send(sendMap);
	}
	public void openmail(int mailid){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try {
			if(this.usermailbox.containsKey(mailid)){
				dao.openmail(this,mailid);
				sendMap.put("AA", this.getGold());
				sendMap.put("AB",this.getMoney());
				sendMap.put("AC", this.ball);
				sendMap.put("CMD", PacketCommandType.DRAWSUCCESS);
			}
		} catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");			
		}
		this.send(sendMap);
	}
	public void reqcharge(int goldnum){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try	{
			String ordid=dao.reqcharge(this,goldnum);
			//logger.info("订单id："+ordid);
			if(!ordid.equals("")&&!ordid.equals(null)){
				sendMap.put("AA", goldnum);
				sendMap.put("AB",ordid);
				sendMap.put("CMD", PacketCommandType.RETURNCHARGE);	
			}else{
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1068");//订单创建失败
			}
			
		} catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1068");//订单创建失败
		}
		this.send(sendMap);
	}
	public void chargeend(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try	{
			dao.chargeend(this);
			sendMap.put("AA", this.getGold());
			sendMap.put("AB",this.viplevel);
			sendMap.put("AC",Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AG").toString()));//购买体力上限
			sendMap.put("AD",Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AE").toString()));//争斗
			sendMap.put("AE",Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AH").toString()));//夺计
			sendMap.put("AF",this.maxgoldtree);//最大摇钱次数
			sendMap.put("AG",this.friendsum);//最大好友数
			sendMap.put("AH",this.chargenum);//首冲标志
			int vpgold=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AI").toString())-this.chargenum;
			sendMap.put("VP", vpgold);
			sendMap.put("CMD", PacketCommandType.FINISHCHARGE);	
			
		} catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1069");//发货失败
		}
		this.send(sendMap);
	}
	//appstore 发货
	public void AppchargeSucc(Map<String,Object> _cmap){
		String oid=_cmap.get("AB").toString();
		String apporderid=_cmap.get("AC").toString();
		Map<String,Object> sendMap = new HashMap<String,Object>();
		GameServerDao dao = new GameServerDao();
		try	{
			dao.chargesucc(this,oid,apporderid);
			sendMap.put("AA", this.getGold());
			sendMap.put("AB",this.viplevel);
			sendMap.put("AC",Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AG").toString()));//购买体力上限
			sendMap.put("AD",Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AE").toString()));//争斗
			sendMap.put("AE",Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AH").toString()));//夺计
			sendMap.put("AF",this.maxgoldtree);//最大摇钱次数
			sendMap.put("AG",this.friendsum);//最大好友数
			sendMap.put("AH",this.chargenum);//首冲标志
			int vpgold=Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AI").toString())-this.chargenum;
			sendMap.put("VP", vpgold);
			sendMap.put("CMD", PacketCommandType.FINISHCHARGE);	
			
		} catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1069");//发货失败
		}
		this.send(sendMap);
	}
	public void loginaward(int contlog){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try	{
			String temp="";
			switch(contlog){
			case 1:
				temp=String.valueOf(this.innercoin).substring(0, 1);
				break;
			case 2:
				temp=String.valueOf(this.innercoin).substring(1, 2);
				break;
			case 3:
				temp=String.valueOf(this.innercoin).substring(2, 3);
				break;
			case 4:
				temp=String.valueOf(this.innercoin).substring(3, 4);
				break;
			case 5:
				temp=String.valueOf(this.innercoin).substring(4, 5);
				break;
			}
			if(temp.equals("1")){
				sendMap.put("CMD", PacketCommandType.GETERROR);	
				sendMap.put("EC","1055");//条件不足
			}else if(temp.equals("3")){
				sendMap.put("CMD", PacketCommandType.GETERROR);	
				sendMap.put("EC","1070");//已领过相同奖励
			}else{
				/*if(this.continuelog==5){
					sendMap.put("CMD", PacketCommandType.GETERROR);	
					sendMap.put("EC","1075");//活动已停止
				}else{*/
					GameServerDao dao = new GameServerDao();
					dao.loginaward(this,contlog);
					//logger.info("连续登陆天数："+this.continuelog);
					sendMap.put("CMD", PacketCommandType.LOGAWARDFIN);
					sendMap.put("AA", this.getGold());
					sendMap.put("AB", this.money);
					sendMap.put("AC", this.innercoin);
					sendMap.put("AD", this.continuelog);
				}							
			//}
			
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
		}
		this.send(sendMap);
		
	}
	public void getcardlotluck(String cardcode,int cardid){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		UserCard uc=this.getCardlist().get(cardid);
		ArrayList<Map<String,Object>> luckal=new ArrayList<Map<String,Object>>();
		//判断缘分卡牌
				if(GameManager.getInstance().getCardluckdict().containsKey(cardcode)){
					Iterator<lotluck> cardit = GameManager.getInstance().getCardluckdict().get(cardcode).values().iterator();
					while(cardit.hasNext()){
						lotluck ll = cardit.next();
						String[] luckstr=ll.getSomename().split(",");
						Map<String,Object> luckmap = new HashMap<String,Object>();
						luckmap.put("LB", ll.getLuckname());
						luckmap.put("LC", ll.getLuckdesc());
						luckmap.put("LD", 0);//1获得 0未获得
						if(uc.getIfuse()>0&&uc.getLocate()>=0){//计算是否已获得缘buff
							String ccode="";
							int flag=0;
							for(int i=0;i<luckstr.length;i++){
								ccode=luckstr[i];
								for(int j=0;j<this.cardlocid.length;j++){	
									if(this.cardlocid[j]>0){
										if(ccode.equals(this.cardlist.get(this.cardlocid[j]).getCardcode())){
											flag=flag+1;
											break;
										}
									}							
								}
							}
							if(flag==luckstr.length){
								luckmap.put("LD", 1);//1获得 0未获得
							}
						}				
						luckal.add(luckmap);
					}				
				}
		//判断缘分装备
		if(GameManager.getInstance().getCarddevicedict().containsKey(cardcode)){
			Iterator<lotluck> luckit = GameManager.getInstance().getCarddevicedict().get(cardcode).values().iterator();
			while(luckit.hasNext()){
				lotluck ll=luckit.next();
				Map<String,Object> luckmap = new HashMap<String,Object>();
				luckmap.put("LB", ll.getLuckname());
				luckmap.put("LC", ll.getLuckdesc());
				luckmap.put("LD", 0);//1获得 0未获得
				if(uc.getIfuse()>0&&uc.getLocate()>=0){//计算是否已获得缘buff
					Iterator<UserDevice> devit = uc.getDeviceMap().values().iterator();
					while(devit.hasNext()){
						UserDevice ud = devit.next();
						if(ud.getDevicecode().equals(ll.getSomename())){
							luckmap.put("LD", 1);//1获得 0未获得
							break;
						}
					}					
				}				
				luckal.add(luckmap);
			}				
		}
		//判断缘分技能
		if(GameManager.getInstance().getCardskilldict().containsKey(cardcode)){
			Iterator<lotluck> luckit = GameManager.getInstance().getCardskilldict().get(cardcode).values().iterator();
			while(luckit.hasNext()){
				lotluck lt=luckit.next();
				Map<String,Object> luckmap = new HashMap<String,Object>();
				luckmap.put("LB", lt.getLuckname());
				luckmap.put("LC", lt.getLuckdesc());
				luckmap.put("LD", 0);//1获得 0未获得
				if(uc.getIfuse()>0&&uc.getLocate()>=0){//计算是否已获得缘buff
					Iterator<UserSkill> skit = uc.getSkillMap().values().iterator();
					while(skit.hasNext()){
						UserSkill usk = skit.next();
						if(usk.getSkillcode().equals(lt.getSomename())){
							luckmap.put("LD", 1);//1获得 0未获得
							break;
						}
					}					
				}				
				luckal.add(luckmap);
			}				
		}
		
		sendMap.put("LA", luckal.toArray());
		sendMap.put("CMD", PacketCommandType.RETURNLOTLUCK);
		this.send(sendMap);
	}
	public void getpassward(int sno){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try	{
			GameServerDao dao = new GameServerDao();
			if(dao.getpassward(this,sno,sendMap)>0){
				sendMap.put("CMD", PacketCommandType.PASSAWARDSUCC);
			}else{
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1073");//通关奖励已领取
			}
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
		}
		this.send(sendMap);
	}
	//购买任务次数
	public void buymissionnum(int missid){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		UserMission um=this.umisson.get(missid);
		try	{
			int goldnum=(um.getMissnum()+1)*10;
			if(goldnum>200) goldnum=200;
			GameServerDao dao = new GameServerDao();
			if(this.viplevel>=1){
				if(goldnum<=this.getGold()){
					dao.buymissionnum(this,um,sendMap,goldnum);
					sendMap.put("CMD", PacketCommandType.BUYMISSFINISH);
				}else{
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1028");//金条不足
				}
			}else{
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1041");//VIP等级不够
			}
			
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
		}
		this.send(sendMap);
	}
	public void applyluckaward(){
		this.awardgold=200;//初始设置摇奖价格
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try	{
			GameServerDao dao = new GameServerDao();
			int cursecond=(int)(System.currentTimeMillis()/1000);
			if(GameManager.getInstance().getSvractdict().containsKey(2)){
				if(cursecond>=GameManager.getInstance().getSvractdict().get(2).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(2).getEtime()){
					if(dao.CheckCondValue(this,GameManager.getInstance().getSvractdict().get(2).getBtime(),GameManager.getInstance().getSvractdict().get(2).getEtime())>0){
						this.awardgold=(int)(this.awardgold*GameManager.getInstance().getSvractdict().get(2).getActvalue());
					}
				}
			}	
			if(this.userluckaward.size()<=0){				
				dao.applyluckaward(this);
			}
			ArrayList<Map<String,Object>> al=new ArrayList<Map<String,Object>>();
			for(int i=1;i<=10;i++){
				UserLuckAward ula=this.userluckaward.get(i);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("AB", ula.getAwardcode());
				map.put("AC", ula.getAwardname());
				if(this.awardnum==4) {
					ula.setCurrand(ula.getDownrand());
					ula.setIfchoose(0);
					map.put("AD", 0);				
				}
				else map.put("AD", ula.getIfchoose());
				map.put("AE", ula.getAwardid());
				map.put("AF", ula.getPiccode());
				al.add(map);
			}
			if(this.awardnum==4) {
				this.awardnum=0;
			}
			sendMap.put("AA", al.toArray());
			sendMap.put("AG", this.awardnum);
			sendMap.put("AH", this.awardgold);
			sendMap.put("CMD", PacketCommandType.RETURNLUCKAWARD);
		}catch(SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
		}
		this.send(sendMap);
	}
	public void actluckaward(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(this.getGold()<this.awardgold){
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1028");//金条不足
		}else{
			try	{
				GameServerDao dao = new GameServerDao();
				dao.actluckaward(this,this.awardgold,sendMap);
				//logger.info("aa:"+sendMap.get("AE").toString());
				sendMap.put("AG", this.getGold());
				sendMap.put("AM", this.money);
				sendMap.put("AN", this.ball);
				sendMap.put("AH", this.awardgold);
				sendMap.put("CMD", PacketCommandType.LUCKAWARDFINISH);
			}catch(SQLException e) {
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1099");//
			}			
		}
		this.send(sendMap);			
		
	}
	public void returntalklist(int missionid){//返回关卡对话
		Map<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("EC", missionid);
		int sceneid=GameManager.getInstance().getMissionscene().get(missionid);
		sendMap.put("ED",GameManager.getInstance().getScenedict().get(sceneid).getScecode());
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		if(GameManager.getInstance().getTalkdict().containsKey(missionid)){
			Map<Integer, ArrayList<Map<String, Object>>> map =GameManager.getInstance().getTalkdict().get(missionid);
			if(map.containsKey(0)){//战前
				sendMap.put("EA", map.get(0).toArray());
			}else sendMap.put("EA",al.toArray());
			if(map.containsKey(1)){
				sendMap.put("EB", map.get(1).toArray());
			}else sendMap.put("EB",al.toArray());
		}else{
			sendMap.put("EA",al.toArray());
			sendMap.put("EB",al.toArray());
		}
		sendMap.put("CMD", PacketCommandType.RETURNMISSIONTALK);	
		this.send(sendMap);
	}
	public void GetChipBag(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		
		for(int i=0;i<this.userchips.size();i++){
			Map<String,Object> map = new HashMap<String,Object>();
			CUserChip cc=this.userchips.get(i);
			map.put("BA", cc.getChipcode());
			map.put("BB", cc.getChipname());
			map.put("BC", cc.getCurnum());
			map.put("BD", cc.getNeedsum());//合成数量
			map.put("BE", cc.getChipcolor());//颜色
			map.put("BF", cc.getChiptype());//碎片类型 0卡牌1装备
			if(cc.getChiptype()==1){
				String dtype="",dqu="";
				int dhval=0,daval=0,ddval=0;
				dtype=GameManager.getInstance().getDevsetdict().get(cc.getChipcode()).getDevicetype();
				dqu=GameManager.getInstance().getDevsetdict().get(cc.getChipcode()).getQuality();
				dhval=GameManager.getInstance().getDevsetdict().get(cc.getChipcode()).getDhp();
				daval=GameManager.getInstance().getDevsetdict().get(cc.getChipcode()).getDatk();
				ddval=GameManager.getInstance().getDevsetdict().get(cc.getChipcode()).getDdef();
				map.put("BH", dqu);
				if(dtype.equals("攻")){
					map.put("BG", "攻+"+daval);
					map.put("BI", "武器");
				}else if(dtype.equals("防")){
					map.put("BG", "防+"+ddval);
					map.put("BI", "防具");
				}else{
					map.put("BG", "血+"+dhval);
					map.put("BI", "饰品");
				}
				
			}else {
				map.put("BG", "@");
				map.put("BH", "@");
				map.put("BI", "@");
			}
			al.add(map);
		}
		sendMap.put("AA",al.toArray());
		sendMap.put("CMD", PacketCommandType.RETEURNCHIP);	
		this.send(sendMap);
	}
	//祈愿
	public void BlessMeDown(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try	{
			GameServerDao dao = new GameServerDao();
			if(this.blessnum<this.blessall){//可以免费祈愿
				dao.actbless(this,0,sendMap);
				sendMap.put("CMD", PacketCommandType.RETURNBLESS);
			}else{
				if(this.blessnum<this.blessall*2){//可金条消费
					if(this.getGold()>=10){//
						dao.actbless(this,10,sendMap);
						sendMap.put("CMD", PacketCommandType.RETURNBLESS);
					}else{//钱不够
						sendMap.put("CMD", PacketCommandType.GETERROR);
						sendMap.put("EC", "1028");//
					}
				}else{//已到上限
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1080");//
				}
			}
			this.send(sendMap);
		}catch(SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
		}			
		
	}
	//碎片合成
	public void UnionChip(String chipcode){
		int keyflag=-1;
		Map<String,Object> sendMap = new HashMap<String,Object>();
		for(int i=0;i<this.userchips.size();i++){
			if(this.userchips.get(i).getChipcode().equals(chipcode)){//寻找在用户背包里的位置
				keyflag=i;
				break;
			}
		}
		if(keyflag>=0){
			CUserChip cuc=this.userchips.get(keyflag);
			if(cuc.getNeedsum()>cuc.getCurnum()){//碎片不够
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1081");//碎片不够
			}else{//合成
				try	{
					GameServerDao dao = new GameServerDao();
					dao.actUnionChip(this,keyflag,sendMap);
					sendMap.put("CMD", PacketCommandType.SUCCHIPUNION);
					
				}catch(SQLException e) {
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1099");//
				}			
			}
		}else{
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1082");//没有对应碎片
		}
		this.send(sendMap);
		
	}
	
	//想挑战世界boss
	public void WantWorldBoss(){
		if(this.level < 15) return;//15级以上可以挑战世界boss
		Map<String,Object> sendMap = new HashMap<String,Object>();
		int currtime = (int)(System.currentTimeMillis()/1000);
		if(currtime < CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getBhour(),GameManager.getInstance().getWorldBoss().getBminute())){//12:30
			sendMap.put("CMD", PacketCommandType.RETURNWORLDBOSS);
			sendMap.put("AA", 2);//现在不能挑战世界boss			
			sendMap.put("AB", CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getBhour(),GameManager.getInstance().getWorldBoss().getBminute())-currtime);	
		}else if(currtime > CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getEhour(),GameManager.getInstance().getWorldBoss().getEminute())){//13:00
			sendMap.put("CMD", PacketCommandType.RETURNWORLDBOSS);
			sendMap.put("AA", 3);//现在不能挑战世界boss
			sendMap.put("AB", CommTools.GetTimeInMillis(1,GameManager.getInstance().getWorldBoss().getBhour(),GameManager.getInstance().getWorldBoss().getBminute()) - currtime);//明天的12点半
		}else if(!GameManager.getInstance().getWorldBoss().getCurtime().equals(CommTools.TimestampToStr(System.currentTimeMillis()))){//
			sendMap.put("CMD", PacketCommandType.RETURNWORLDBOSS);
			sendMap.put("AA", 1);//可以挑战世界boss
			sendMap.put("AB", "0");
		}/*else if(!GameManager.getInstance().getWorldBoss().isIsnew()){
			sendMap.put("CMD", PacketCommandType.RETURNWORLDBOSS);
			sendMap.put("AA", 1);//可以挑战世界boss
			sendMap.put("AB", "0");
		}*/
		else{
			sendMap.put("CMD", PacketCommandType.RETURNWORLDBOSS);
			sendMap.put("AA", 3);//现在不能挑战世界boss
			sendMap.put("AB", CommTools.GetTimeInMillis(1,GameManager.getInstance().getWorldBoss().getBhour(),GameManager.getInstance().getWorldBoss().getBminute()) - currtime);//明天的12点半
		}
		
		//FIX YU
		//sendMap.put("CMD", PacketCommandType.RETURNWORLDBOSS);
		//sendMap.put("AA", 1);//可以挑战世界boss
		//sendMap.put("AB", "0");
		//END
		
		sendMap.put("AC", GameManager.getInstance().getWorldBoss().getBossID());
		sendMap.put("AF", GameManager.getInstance().getWorldBoss().getBossname());
		sendMap.put("AD", GameManager.getInstance().getWorldBoss().getLastrank().toArray());
		sendMap.put("AE", GameManager.getInstance().getWorldBoss().getKiller().toArray());
		this.send(sendMap);
	}
	
	//设定自动挑战boss
	public void SetAutoFightBoss(){
		GameServerDao dao = new GameServerDao();
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try{
			if(this.viplevel<3){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1041");//
			}else{
				int curtime = (int)(System.currentTimeMillis()/1000);
				if(GameManager.getInstance().getWorldBoss().getCurtime().equals(CommTools.TimestampToStr(System.currentTimeMillis()))||curtime<CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getBhour(),GameManager.getInstance().getWorldBoss().getBminute()) || curtime>=CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getEhour(),GameManager.getInstance().getWorldBoss().getEminute())){
					dao.SetAutoFightBoss(this);
					sendMap.put("CMD", PacketCommandType.RETURNAUTOWORLDBOSS);
					sendMap.put("AA", this.getAutoboss());
					sendMap.put("AB", this.getGold());
				}else{//战斗开始了不能设置挑战BOSS状态
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1085");//
				}
			}
		}catch(SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
		}	
		this.send(sendMap);
	}
	
	//干boss
	public void PKWorldBoss(int type){
		
		int hurt = 0;
		int ret = 0;
		int curtime = (int)(System.currentTimeMillis()/1000);
		if(GameManager.worldbosschannel.find(this.channel.getId())==null){
			GameManager.worldbosschannel.add(this.getChannel());
		}	
		Map<String,Object> sendMap = new HashMap<String,Object>();
		//type=0 准备挑战  type=1普通挑战 type=2立即挑战(免CD) type=3极限挑战(免CD)
		if(type ==1 && this.bosscdval != 0 && curtime < (this.bosscdval + Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AJ").toString()))){
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1083");//
			this.send(sendMap);
			return;
		}
		for(int i=0;i<6;i++){ //战斗阵容
			if(this.getCardlocid()[i]<=0) break;
			UserCard uu = this.getCardlist().get(this.getCardlocid()[i]);
			hurt+=uu.getAtk();
		}	
		if(type == 0) hurt = 0;
		else if(type == 3) //极限重生  
			hurt = hurt + hurt *20/100;
		WorldBossDict boss = GameManager.getInstance().getWorldBoss();
		if(type!=0)		ret = boss.BossHurt(hurt); //计算预计伤害
		if(curtime < CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getBhour(),GameManager.getInstance().getWorldBoss().getBminute())){//12:30
			ret=0;
		}else if(curtime > CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getEhour(),GameManager.getInstance().getWorldBoss().getEminute())){//13:00
			ret=0;
		}else if(GameManager.getInstance().getWorldBoss().getCurtime().equals(CommTools.TimestampToStr(System.currentTimeMillis()))){//
			ret=0;
		}
		if(ret==0&&type!=0){//boss已死或结束或未开始
			WantWorldBoss();
		}else{
			GameServerDao dao = new GameServerDao();
			//ret 0boss已经死了,准备打boss, 1打了boss 2打死了boss
			try{
				if(ret==0) sendMap.put("BB", 0);
				else if(ret == 1) sendMap.put("BB", 1);
				else sendMap.put("BB", 2);
				sendMap.put("AJ", 0);
				sendMap.put("BD",0);
				sendMap.put("BE", 0);
				sendMap.put("BF",0);
				dao.HurtBossTop5(this,boss,type,curtime,ret,hurt,sendMap);
				sendMap.put("CMD", PacketCommandType.RETURNPKWORLDBOSS);
				sendMap.put("BA", boss.getBossID());
				sendMap.put("BC", boss.getCurhp());
				sendMap.put("AO", boss.getBossname());
				if(this.bosscdval == 0) 
					sendMap.put("BG",0);
				else 
					sendMap.put("BG", this.getBosscdval()+Integer.parseInt(GameManager.getInstance().getShakedict().get(this.viplevel).get("AJ").toString()));
				sendMap.put("AE", this.gold);
				sendMap.put("AF", this.ball);
				sendMap.put("AK", this.money);
			}catch(SQLException e){
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1099");//
				logger.error("PKboss异常："+e.getMessage());
			}
			this.send(sendMap);
		}		
	}
	//获取其他玩家打boss列表
	/*public void ReqOtherUserBeat(){
		HashMap<String,Object> sendMap = new HashMap<String,Object>();
		sendMap.put("BA", GameManager.getInstance().getUseractbosslist().toArray());
		ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
		for(int i=0;i<GameManager.getInstance().getBossTop5().size();i++){
			if(i==5) break;
			al.add(GameManager.getInstance().getBossTop5().get(i));
		}
		sendMap.put("BH", al.toArray());
		sendMap.put("CMD", PacketCommandType.RETURNOTHERUSER);
		this.send(sendMap);		
	}*/
	//返回合成符文列表
	public void getmarkproclist(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		markproclist(sendMap);
		sendMap.put("CMD", PacketCommandType.RETURNPROCMARKS);
		this.send(sendMap);
	}
	
	public void markproclist(Map<String,Object> sendMap){
		ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
		Iterator<String> it = GameManager.getInstance().getMergemarks().keySet().iterator();
		while(it.hasNext()){
			String newcode = it.next();
			Map<String,Object> mmap = new HashMap<String,Object>();
			mmap.put("FA", newcode);
			mmap.put("FB", GameManager.getInstance().getMarkdict().get(newcode).getMarkname());
			mmap.put("FC", GameManager.getInstance().getMarkdict().get(newcode).getMarkdesc());
			mmap.put("FD", GameManager.getInstance().getMarkdict().get(newcode).getMarktype());
			ArrayList<Map<String,Object>> marklist = new ArrayList<Map<String,Object>>();
			ArrayList<Map<String,Object>> desclist = new ArrayList<Map<String,Object>>();
			Iterator<String> olds = GameManager.getInstance().getMergemarks().get(newcode).keySet().iterator();//符文Merge字典key
			while(olds.hasNext()){//CC BB
				Map<String,Object> mark = new HashMap<String,Object>();
				Map<String,Object> desc = new HashMap<String,Object>();
				String oldcode = olds.next();
				int num = GameManager.getInstance().getMergemarks().get(newcode).get(oldcode);
				
				desc.put("FE", GameManager.getInstance().getMarkdict().get(oldcode).getMarkname());//符文名字
				desc.put("FF", num);//需要多少
				desc.put("FG", this.umarksnumlist.get(oldcode));//当前有多少
				desclist.add(desc);
				
				mark.put("FE", oldcode);//符文名字
				mark.put("FF", 1);//符文名字
				marklist.add(mark);
				
				while(num > 1){
					Map<String,Object> tempmark = new HashMap<String,Object>();
					tempmark.put("FE", oldcode);//符文名字
					tempmark.put("FF", 1);//符文名字
					marklist.add(tempmark);
					num--;
				}
			}
			mmap.put("BB",marklist.toArray());
			mmap.put("CC",desclist.toArray());
			al.add(mmap);
		}
		Collections.sort(al,new Comparator<Map<String,Object>>(){
			public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
				return Integer.parseInt(arg0.get("FA").toString().substring(1))-Integer.parseInt(arg1.get("FA").toString().substring(1)); }  

		});
		sendMap.put("AA", al.toArray());
	}
	
	public void procmark(String newcode){
		boolean flag = true;
		Map<String,Object> sendMap = new HashMap<String,Object>();
		ArrayList<Integer> marklist = new ArrayList<Integer>();
		Map<String,Integer> needlist = new HashMap<String, Integer>(); 
		needlist = GameManager.getInstance().getMergemarks().get(newcode);
		Iterator<String> needit = needlist.keySet().iterator();
		while(needit.hasNext()){
			String needmarkcode = needit.next();
			int neednum = needlist.get(needmarkcode);
			if(this.umarksnumlist.get(needmarkcode) < neednum){//背包可用<需要的
				flag = false;
				break;
			}			
			//获取可用需要的符文id
			while(neednum >= 1){
				Iterator<UserMark> currit = this.getUmarklist().values().iterator();
				while(currit.hasNext()){
					UserMark currmark = currit.next();
					if(currmark.getMarkcode().equals(needmarkcode)&&currmark.getDeviceid()<1 && !marklist.contains(currmark.getMarkid())){
						marklist.add(currmark.getMarkid());
						break;
					}
				}
				neednum--;
			}
		}
		if(flag && marklist.size() == 3){
			GameServerDao dao = new GameServerDao();
			try{
				dao.sureprocmark(this,newcode,needlist,marklist);
				markproclist(sendMap);
				sendMap.put("CMD", PacketCommandType.SUCCMARK);
			}catch(SQLException e){//数据库操作异常
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1099");//
				logger.error("procmark异常："+e.getMessage());
			}
		}else{//数量不足
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1084");//
			logger.error("procmark符文数量不足");
		}
		this.send(sendMap);
	}
	
	/*public void apploginaward(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
		for(int j=1;j<=7;j++){
			Map<String,Object> map = new HashMap<String,Object>();
			LoginDict ld = GameManager.getInstance().getLogdict().get(j);
			map.put("EB", ld.getDowncode());
			PropsDict pd= GameManager.getInstance().getPropsdict().get(ld.getDowncode());
			map.put("EC", pd.getPropsname());
			map.put("EE", pd.getPropsdesc());
			map.put("EF", pd.getPropstype());
			map.put("EH", ld.getDownnum());
			ArrayList<Map<String,Object>> packal = new ArrayList<Map<String,Object>>();
			for(int i=0;i<pd.getPackgoods().size();i++){
				Map<String,Object> packmap = new HashMap<String,Object>();
				packageGoods pg=pd.getPackgoods().get(i);
				packmap.put("AA", pg.getGoodscode());
				packmap.put("AB", pg.getGoodsname());
				packmap.put("AC", pg.getGoodsnum());
				packmap.put("AD", pg.getInnertype());
				packmap.put("AP", pg.getGoodspic());
				packal.add(packmap);
			}
			map.put("EG",packal.toArray());
			map.put("EI", pd.getUsecond());
			al.add(map);
		}
		sendMap.put("EA", al.toArray());
		sendMap.put("CMD", PacketCommandType.BACKLOGINAWARD);
		this.send(sendMap);
	}*/
	//获取等级排名
	public void reqlevelrank(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try	{
			GameServerDao dao = new GameServerDao();
			dao.reqlevelrank(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNLEVELRANK);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
			logger.info(e.getMessage());
		}
		this.send(sendMap);
	}
	//获取消费排名
	public void reqgoldrank(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try	{
			GameServerDao dao = new GameServerDao();
			dao.reqgoldrank(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNGOLDRANK);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
			logger.info(e.getMessage());
		}
		this.send(sendMap);
	}
	//获取宝箱排名
	public void reqboxrank(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		try	{
			GameServerDao dao = new GameServerDao();
			dao.reqboxrank(this,sendMap);
			sendMap.put("CMD", PacketCommandType.RETURNBOXRANK);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
			logger.info(e.getMessage());
		}
		this.send(sendMap);
	}
	//领奖
	public void reqrankaward(String awardtype){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(awardtype.equals("0")){
			if(this.ifgetgold>0||this.ifgetgold<0){//领过或无奖
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1055");//条件不足
				this.send(sendMap);
				return;
			}
		}
		if(awardtype.equals("1")){
			if(this.ifgetbox>0||this.ifgetbox<0){//领过或无奖
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1055");//
				this.send(sendMap);
				return;
			}
		}
		try	{
			GameServerDao dao = new GameServerDao();
			dao.reqrankaward(this,awardtype,sendMap);
			sendMap.put("AA", this.gold);
			sendMap.put("CMD", PacketCommandType.GIVERANKAWARD);
		}catch (SQLException e) {
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("EC", "1099");//
			logger.info(e.getMessage());
		}
		this.send(sendMap);
	}
	//返回vip信息
	public void reqvipdesc(){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
		for(int i=0;i<GameManager.getInstance().getVipdescdict().size();i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("BA", i+1);
			map.put("BB", GameManager.getInstance().getVipdescdict().get(i).toArray());
			al.add(map);
		}
		sendMap.put("AA", al.toArray());
		sendMap.put("CMD", PacketCommandType.RETURNVIPDESC);
		this.send(sendMap);
	}
	
	public void readayps(){
		if(this.level < 20) return;//20级以上可以看到
		Map<String,Object> sendMap = new HashMap<String,Object>();
		switch(5-this.advps){
		case 0:
			sendMap.put("AD", "魅点+10~20");
			sendMap.put("AF", "20");
			break;
		case 1:
			sendMap.put("AD", "魅点+12~24");
			sendMap.put("AF", "40");
			break;
		case 2:
			sendMap.put("AD", "魅点+14~28");
			sendMap.put("AF", "60");
			break;	
		case 3:
			sendMap.put("AD", "魅点+16~32");
			sendMap.put("AF", "80");
			break;	
		case 4:
			sendMap.put("AD", "魅点+18~36");
			sendMap.put("AF", "100");
			break;
		default:
			sendMap.put("AD", "魅点+10~20");
			sendMap.put("AF", "20");
			break;
		}
		
		sendMap.put("CMD", PacketCommandType.RETURNPS);
		sendMap.put("AA", this.gold);
		sendMap.put("AB", this.money);
		sendMap.put("AC", "魅点+5~15");
		sendMap.put("AE", 1000);
		sendMap.put("AN", this.stdps);
		sendMap.put("AO", this.advps);
		//logger.info("readayps="+sendMap.toString());
		this.send(sendMap);
	}
	
	public void actpscard(int cardid,int pstype){
		Map<String,Object> sendMap = new HashMap<String,Object>();
		if(pstype == 0){//标准美容
			if(this.stdps > 0){//可以美容
				if(this.money >= 1000){//钱够
					int bao = 30;
					CardLogicProc clp = new CardLogicProc();
					sendMap = clp.pscard(this, cardid, pstype, bao, 0, 1000,sendMap);
					sendMap.put("AN", this.stdps);
					sendMap.put("AO", this.advps);
					if(this.advps==0) {
						sendMap.put("AF",20);
						sendMap.put("AK", "魅点+10~20");
					}
					else {
						sendMap.put("AF", (6-this.advps)*20);
						sendMap.put("AK", "魅点+"+((5-this.advps)*2+10)+"~"+((5-this.advps)*4+20));
					}
					sendMap.put("AE", 1000);					
					sendMap.put("AJ", "魅点+5~15");
					sendMap.put("AI", this.gold);
					sendMap.put("AH", this.money);
				}else{//钱不够
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1027");//
				}
			}else{
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1087");//
			}
		}else if(pstype == 1){//高级美容
			if(this.advps > 0){//可以美容
				int i = 5 - this.advps;
				int needgold = (i+1) * 20;
				if(this.gold >= needgold){
					int bao = 30;
					if(this.viplevel >= 5) bao = 70;
					else if(this.viplevel >= 3 && this.viplevel < 5) bao = 50;
					else bao = 30;
					CardLogicProc clp = new CardLogicProc();
					sendMap = clp.pscard(this, cardid, pstype, bao, i, needgold,sendMap);
					sendMap.put("AN", this.stdps);
					sendMap.put("AO", this.advps);
					if(this.advps==0) {
						sendMap.put("AF",20);
						sendMap.put("AK", "魅点+10~20");
					}
					else {
						sendMap.put("AF", (6-this.advps)*20);
						sendMap.put("AK", "魅点+"+((5-this.advps)*2+10)+"~"+((5-this.advps)*4+20));
					}
					sendMap.put("AE", 1000);
					sendMap.put("AJ", "魅点+5~15");
					sendMap.put("AI", this.gold);
					sendMap.put("AH", this.money);
				}else{
					sendMap.put("CMD", PacketCommandType.GETERROR);
					sendMap.put("EC", "1028");//
				}
			}else{
				sendMap.put("CMD", PacketCommandType.GETERROR);
				sendMap.put("EC", "1087");//
			}
		}
		//logger.info("actpscard="+sendMap.toString());
		this.send(sendMap);
	}
	
	public void updatetrade(int val){
		try	{
			GameServerDao dao = new GameServerDao();
			dao.updatetrade(this,val);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void send(Map<String, Object> sendMap){
		try{
			//logger.info("跟踪--------------------------->"+sendMap.get("CMD"));
			if(this.channel!=null && this.channel.isConnected()){
				this.channel.write(sendMap);
			}
		}
		catch(Exception e){
			logger.info("-------------->"+sendMap.get("CMD")+":::"+e.getMessage());
		}
	}
	
	public Date getHeartTime() {
		return heartTime;
	}

	public void setHeartTime(Date heartTime) {
		this.heartTime = heartTime;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
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
	public String getCurrlogin() {
		return Currlogin;
	}
	public void setCurrlogin(String currlogin) {
		Currlogin = currlogin;
	}
	public Channel getChannel() {
		return channel;
	}
	public String getSvrcode() {
		return svrcode;
	}
	public void setSvrcode(String svrcode) {
		this.svrcode = svrcode;
	}
	public String getFirstlogin() {
		return firstlogin;
	}
	public void setFirstlogin(String firstlogin) {
		this.firstlogin = firstlogin;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getMaxpower() {
		return maxpower;
	}
	public void setMaxpower(int maxpower) {
		this.maxpower = maxpower;
	}
	public int getMembers() {
		return members;
	}
	public void setMembers(int members) {
		this.members = members;
	}
	public int getPowerstart() {
		return powerstart;
	}
	public void setPowerstart(int powerstart) {
		this.powerstart = powerstart;
	}
	public int getPerpoint() {
		return perpoint;
	}
	public void setPerpoint(int perpoint) {
		this.perpoint = perpoint;
	}
	public int getLevelup() {
		return levelup;
	}
	public void setLevelup(int levelup) {
		this.levelup = levelup;
	}
	public int getViplevel() {
		return viplevel;
	}
	public void setViplevel(int viplevel) {
		this.viplevel = viplevel;
	}
	public int getMoney() {
		return money;
	}
	public  void setMoney(int money) {
		this.money = money;
	}
	public  int getGold() {
		return gold;
	}
	public  void setGold(int gold) {
		this.gold = gold;
	}
	public int getInnercoin() {
		return innercoin;
	}
	public void setInnercoin(int innercoin) {
		this.innercoin = innercoin;
	}
	public int getGod() {
		return god;
	}
	public void setGod(int god) {
		this.god = god;
	}
	public int getBall() {
		return ball;
	}
	public void setBall(int ball) {
		this.ball = ball;
	}
	public int getGoldrenew() {
		return goldrenew;
	}
	public void setGoldrenew(int goldrenew) {
		this.goldrenew = goldrenew;
	}
	public int getGetbook() {
		return getbook;
	}
	public void setGetbook(int getbook) {
		this.getbook = getbook;
	}
	public int getGoldaddnum() {
		return goldaddnum;
	}
	public void setGoldaddnum(int goldaddnum) {
		this.goldaddnum = goldaddnum;
	}
	public int getTrade() {
		return trade;
	}
	public void setTrade(int trade) {
		this.trade = trade;
	}
	public int getContinuelog() {
		return continuelog;
	}
	public void setContinuelog(int continuelog) {
		this.continuelog = continuelog;
	}
	public Map<Integer, UserCard> getCardlist() {
		return cardlist;
	}
	public void setCardlist(Map<Integer, UserCard> cardlist) {
		this.cardlist = cardlist;
	}
	public int getUhp() {
		return uhp;
	}
	public void setUhp(int uhp) {
		this.uhp = uhp;
	}
	public int getUatk() {
		return uatk;
	}
	public void setUatk(int uatk) {
		this.uatk = uatk;
	}
	public int getUdef() {
		return udef;
	}
	public void setUdef(int udef) {
		this.udef = udef;
	}
	public Map<String, SkillPages> getSkillpagelist() {
		return skillpagelist;
	}
	public void setSkillpagelist(Map<String, SkillPages> skillpagelist) {
		this.skillpagelist = skillpagelist;
	}
	public int getAddvalue() {
		return addvalue;
	}
	public void setAddvalue(int addvalue) {
		this.addvalue = addvalue;
	}
	public int[] getCardlocid() {
		return cardlocid;
	}
	public void setCardlocid(int[] cardlocid) {
		this.cardlocid = cardlocid;
	}
	public int getUndotask() {
		return undotask;
	}
	public void setUndotask(int undotask) {
		this.undotask = undotask;
	}
	public Map<Integer, UserDevice> getUdevicelist() {
		return udevicelist;
	}
	public void setUdevicelist(Map<Integer, UserDevice> udevicelist) {
		this.udevicelist = udevicelist;
	}
	public Map<Integer, UserSkill> getUskilllist() {
		return uskilllist;
	}
	public void setUskilllist(Map<Integer, UserSkill> uskilllist) {
		this.uskilllist = uskilllist;
	}
	public Map<Integer, UserMark> getUmarklist() {
		return umarklist;
	}
	public void setUmarklist(Map<Integer, UserMark> umarklist) {
		this.umarklist = umarklist;
	}
	public Map<Integer, UserCard> getInusecards() {
		return inusecards;
	}
	public void setInusecards(Map<Integer, UserCard> inusecards) {
		this.inusecards = inusecards;
	}
	public Map<Integer, UserMission> getUmisson() {
		return umisson;
	}
	public void setUmisson(Map<Integer, UserMission> umisson) {
		this.umisson = umisson;
	}
	public int getCurmission() {
		return curmission;
	}
	public void setCurmission(int curmission) {
		this.curmission = curmission;
	}
	public int getMissionsum() {
		return missionsum;
	}
	public void setMissionsum(int missionsum) {
		this.missionsum = missionsum;
	}
	public int getPknum() {
		return pknum;
	}
	public void setPknum(int pknum) {
		this.pknum = pknum;
	}
	public int getGetskstart() {
		return getskstart;
	}
	public void setGetskstart(int getskstart) {
		this.getskstart = getskstart;
	}
	public int getEveryskp() {
		return everyskp;
	}
	public void setEveryskp(int everyskp) {
		this.everyskp = everyskp;
	}
	public int getRankseq() {
		return rankseq;
	}
	public void setRankseq(int rankseq) {
		this.rankseq = rankseq;
	}
	public int getCurpoints() {
		return curpoints;
	}
	public void setCurpoints(int curpoints) {
		this.curpoints = curpoints;
	}
	public Map<String, UserEgg> getUseregg() {
		return useregg;
	}
	public void setUseregg(Map<String, UserEgg> useregg) {
		this.useregg = useregg;
	}
	public Map<Integer, ArrayList<UserMission>> getScemisson() {
		return scemisson;
	}
	public void setScemisson(Map<Integer, ArrayList<UserMission>> scemisson) {
		this.scemisson = scemisson;
	}
	public ArrayList<ShopClass> getUsershop() {
		return usershop;
	}
	public void setUsershop(ArrayList<ShopClass> usershop) {
		this.usershop = usershop;
	}
	public int getFriendsum() {
		return friendsum;
	}
	public void setFriendsum(int friendsum) {
		this.friendsum = friendsum;
	}
	public int getDrinknum() {
		return drinknum;
	}
	public void setDrinknum(int drinknum) {
		this.drinknum = drinknum;
	}
	public int getPoweradd() {
		return poweradd;
	}
	public void setPoweradd(int poweradd) {
		this.poweradd = poweradd;
	}
	public String getLastvisit() {
		return lastvisit;
	}
	public void setLastvisit(String lastvisit) {
		this.lastvisit = lastvisit;
	}
	public int getVisitnum() {
		return visitnum;
	}
	public void setVisitnum(int visitnum) {
		this.visitnum = visitnum;
	}
	public Map<Integer, UserStreet> getUstreet() {
		return ustreet;
	}
	public void setUstreet(Map<Integer, UserStreet> ustreet) {
		this.ustreet = ustreet;
	}
	public int getRenewgold() {
		return renewgold;
	}
	public void setRenewgold(int renewgold) {
		this.renewgold = renewgold;
	}
	public int getAddnumgold() {
		return addnumgold;
	}
	public void setAddnumgold(int addnumgold) {
		this.addnumgold = addnumgold;
	}
	public int getBuyfight() {
		return buyfight;
	}
	public void setBuyfight(int buyfight) {
		this.buyfight = buyfight;
	}
	public int getBuylimit() {
		return buylimit;
	}
	public void setBuylimit(int buylimit) {
		this.buylimit = buylimit;
	}
	public Map<String, UserProps> getUserprop() {
		return userprop;
	}
	public void setUserprop(Map<String, UserProps> userprop) {
		this.userprop = userprop;
	}
	public int getGoldtree() {
		return goldtree;
	}
	public void setGoldtree(int goldtree) {
		this.goldtree = goldtree;
	}
	public int getMaxgoldtree() {
		return maxgoldtree;
	}
	public void setMaxgoldtree(int maxgoldtree) {
		this.maxgoldtree = maxgoldtree;
	}
	public int getTreegold() {
		return treegold;
	}
	public void setTreegold(int treegold) {
		this.treegold = treegold;
	}
	public ArrayList<UserDayTask> getUserdaytasklist() {
		return userdaytasklist;
	}
	public void setUserdaytasklist(ArrayList<UserDayTask> userdaytasklist) {
		this.userdaytasklist = userdaytasklist;
	}
	public int getResetstreet() {
		return resetstreet;
	}
	public void setResetstreet(int resetstreet) {
		this.resetstreet = resetstreet;
	}
	public int getResetshop() {
		return resetshop;
	}
	public void setResetshop(int resetshop) {
		this.resetshop = resetshop;
	}
	public int getCurstreetid() {
		return curstreetid;
	}
	public void setCurstreetid(int curstreetid) {
		this.curstreetid = curstreetid;
	}
	public int getCurdesid() {
		return curdesid;
	}
	public void setCurdesid(int curdesid) {
		this.curdesid = curdesid;
	}
	public int getCurmessid() {
		return curmessid;
	}
	public void setCurmessid(int curmessid) {
		this.curmessid = curmessid;
	}
	public String getMarkcode() {
		return markcode;
	}
	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}
	public String getPlatcode() {
		return platcode;
	}
	public void setPlatcode(String platcode) {
		this.platcode = platcode;
	}
	public Map<Integer, UserMailBox> getUsermailbox() {
		return usermailbox;
	}
	public void setUsermailbox(Map<Integer, UserMailBox> usermailbox) {
		this.usermailbox = usermailbox;
	}
	public Integer getChargenum() {
		return chargenum;
	}
	public void setChargenum(Integer chargenum) {
		this.chargenum = chargenum;
	}
	public Map<Integer, UserLuckAward> getUserluckaward() {
		return userluckaward;
	}
	public void setUserluckaward(Map<Integer, UserLuckAward> userluckaward) {
		this.userluckaward = userluckaward;
	}
	public Map<Integer, UserLuckAward> getUserluckawardrand() {
		return userluckawardrand;
	}
	public void setUserluckawardrand(Map<Integer, UserLuckAward> userluckawardrand) {
		this.userluckawardrand = userluckawardrand;
	}
	public int getAwardnum() {
		return awardnum;
	}
	public void setAwardnum(int awardnum) {
		this.awardnum = awardnum;
	}
	public int getVisitlevle() {
		return visitlevle;
	}
	public void setVisitlevle(int visitlevle) {
		this.visitlevle = visitlevle;
	}
	public Map<String, Integer> getUsergetallmap() {
		return usergetallmap;
	}
	public void setUsergetallmap(Map<String, Integer> usergetallmap) {
		this.usergetallmap = usergetallmap;
	}
	public int getAwardgold() {
		return awardgold;
	}
	public void setAwardgold(int awardgold) {
		this.awardgold = awardgold;
	}
	public int getBosscdval() {
		return bosscdval;
	}
	public void setBosscdval(int bosscdval) {
		this.bosscdval = bosscdval;
	}
	public int getTaskcdval() {
		return taskcdval;
	}
	public void setTaskcdval(int taskcdval) {
		this.taskcdval = taskcdval;
	}
	public int getBlessnum() {
		return blessnum;
	}
	public void setBlessnum(int blessnum) {
		this.blessnum = blessnum;
	}
	public int getBlessall() {
		return blessall;
	}
	public void setBlessall(int blessall) {
		this.blessall = blessall;
	}
	public ArrayList<CUserChip> getUserchips() {
		return userchips;
	}
	public void setUserchips(ArrayList<CUserChip> userchips) {
		this.userchips = userchips;
	}
	public int getAutoboss() {
		return autoboss;
	}
	public void setAutoboss(int autoboss) {
		this.autoboss = autoboss;
	}
	public Map<String, Integer> getUmarksnumlist() {
		return umarksnumlist;
	}
	public void setUmarksnumlist(Map<String, Integer> umarksnumlist) {
		this.umarksnumlist = umarksnumlist;
	}
	public int getIfgetgold() {
		return ifgetgold;
	}
	public void setIfgetgold(int ifgetgold) {
		this.ifgetgold = ifgetgold;
	}
	public int getIfgetbox() {
		return ifgetbox;
	}
	public void setIfgetbox(int ifgetbox) {
		this.ifgetbox = ifgetbox;
	}
	public int[] getRankaward() {
		return rankaward;
	}
	public void setRankaward(int[] rankaward) {
		this.rankaward = rankaward;
	}
	public int getAdvps() {
		return advps;
	}
	public void setAdvps(int advps) {
		this.advps = advps;
	}
	public int getStdps() {
		return stdps;
	}
	public void setStdps(int stdps) {
		this.stdps = stdps;
	}
	public int getFriendpknum() {
		return friendpknum;
	}
	public void setFriendpknum(int friendpknum) {
		this.friendpknum = friendpknum;
	}
	
//	public int getBeatboss() {
//		return beatboss;
//	}
//	public void setBeatboss(int beatboss) {
//		this.beatboss = beatboss;
//	}
	
	/*public  Integer getReceivePoint() {
		return ReceivePoint;
	}

	public synchronized  void setReceivePoint(Integer receivePoint) {//同步控制，防止冲突
		ReceivePoint = receivePoint;
	}*/

/*	public HashMap<Integer, Map<String, Object>> getSendlist() {
		return sendlist;
	}

	public  void setSendlist(HashMap<Integer, Map<String, Object>> sendlist) {
		this.sendlist = sendlist;
	}
*/
}
