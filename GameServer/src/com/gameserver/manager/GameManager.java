package com.gameserver.manager;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import com.gameserver.client.MessageClientPipelineFactory;
import com.gameserver.comm.CommTools;
import com.gameserver.dao.GameServerDao;
import com.gameserver.model.ActDict;
import com.gameserver.model.AllgetDict;
import com.gameserver.model.CLifeUpDict;
import com.gameserver.model.CMissionDict;
import com.gameserver.model.CMissionNpcDict;
import com.gameserver.model.CNpcDict;
import com.gameserver.model.CardDict;
import com.gameserver.model.ChipDict;
import com.gameserver.model.Cpointdict;
import com.gameserver.model.DeviceDict;
import com.gameserver.model.DeviceSetDict;
import com.gameserver.model.DeviceUpgrade;
import com.gameserver.model.LiveUpDictCla;
import com.gameserver.model.LoginDict;
import com.gameserver.model.MarkDict;
import com.gameserver.model.MarkStringCA;
import com.gameserver.model.MergeCardDict;
import com.gameserver.model.Pkexpmoney;
import com.gameserver.model.PropsDict;
import com.gameserver.model.SceneDict;
import com.gameserver.model.ShopClass;
import com.gameserver.model.SkillPages;
import com.gameserver.model.StreetDict;
import com.gameserver.model.UpLevelDict;
import com.gameserver.model.User;
import com.gameserver.model.UserUpgradeVal;
import com.gameserver.model.WorldBossDict;
import com.gameserver.model.downDict;
import com.gameserver.model.lotluck;
import com.gameserver.model.messageClass;
import com.gameserver.server.SysTimeTask;


public class GameManager implements Serializable{

	private static final long serialVersionUID = 5710576981158858146L;
	/**
	 * UNO游戏服务器管理类
	 */
	
	private static final Logger logger = Logger.getLogger(GameManager.class.getName());
	private boolean sendServerInfo=false; //连接认证服务器标志
	private static GameManager instance;
	private String Token;
	private ChannelFuture future;
	private ClientBootstrap bootstrap;
	private static Properties props;
	private String[] randboss;
	private int runnum;//计数器
	private ConcurrentHashMap<Integer, User> playerMap;//玩家对象集合
	private ConcurrentHashMap<String, User> onlineMap;//玩家对象集合2
	private ConcurrentHashMap<String, CardDict> carddict; //卡牌字典
	private ConcurrentHashMap<String, DeviceDict> devsetdict; //装备字典<装备code，装备字典类>
	private ConcurrentHashMap<String, MarkDict> markdict; //符文字典
	private ConcurrentHashMap<Integer, Double> valuedict; //魅力值字典
	private ConcurrentHashMap<Integer, UserUpgradeVal> useruplevel; //账号升级经验字典
	private ConcurrentHashMap<String, MarkStringCA> marksetdict; //符文串字典
	private ConcurrentHashMap<String, DeviceSetDict> devicesetdict; //套装字典
	private ConcurrentHashMap<String, LiveUpDictCla> liveupdict; //培养字典表 
	private ConcurrentHashMap<Integer,CLifeUpDict> lifeupdict; //转生字典
	private ConcurrentHashMap<Integer, LoginDict> logdict;//连续登录奖励字典
	private ConcurrentHashMap<String, SkillPages> skilldict;//技能字典
	private ConcurrentHashMap<Integer,MergeCardDict> mergecarddict; //<散魂星级，融魂字典>
	private ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<Integer,DeviceUpgrade>>> deviceaddval; //精炼字典 <装备类型攻防血<装备质量<装备等级,精炼字典>>>
	private ConcurrentHashMap<Integer,CNpcDict> cnpcdict = new ConcurrentHashMap<Integer,CNpcDict>();//<npcid,npc>
	private ConcurrentHashMap<Integer,ArrayList<CMissionNpcDict>> cmnpccdict;//<关卡id，关卡npc组>
	private ConcurrentHashMap<Integer,CMissionDict> cmissiondict ;//<关卡id，关卡>
	private ConcurrentHashMap<Integer,Integer> missionscene ; //关卡id 场景id
	private ConcurrentHashMap<Integer,SceneDict> scenedict ;//<场景id，场景>
	private ConcurrentHashMap<String,ShopClass> shopdict ;//<物品代码，商城类>
	private ConcurrentHashMap<Integer,StreetDict> streetdict ;//<关卡，街霸>
	private ConcurrentHashMap<Integer,ArrayList<CMissionNpcDict>> streetnpcdict;//<关卡，npc>
	private ConcurrentHashMap<Integer,ArrayList<downDict>> downdictl;//<掉落id，掉落>
	private ConcurrentHashMap<String,PropsDict> propsdict;//<道具代码，道具类>
	private ConcurrentHashMap<Integer,Map<String,Object>> shakedict;//<次数及vip等级，Map值>
	private ConcurrentHashMap<Integer,Pkexpmoney> pkexpmon;//<等级，钱经验>
	private ConcurrentHashMap<Integer,Cpointdict> cpdict;//<id，积分兑换奖励>
	private ConcurrentHashMap<Integer,UpLevelDict> uplvldict;//<id，升级奖励>
	private ConcurrentHashMap<Integer,messageClass> logmessdict;//<id，登录消息>
	private ConcurrentHashMap<String,Integer> lockrank;//<排名角色，锁定>
	private ConcurrentHashMap<String,Integer> lockgetbook;//<角色名，锁定>
	private ConcurrentHashMap<Integer,String> usersendmess;//用户发送消息<消息id，内容>
	private ConcurrentHashMap<String,Map<String,lotluck>> carddevicedict;//卡牌装备缘分<卡牌code,<装备code,buff>>
	private ConcurrentHashMap<String,Map<String,lotluck>> cardskilldict;//卡牌技能缘分<卡牌code,<技能code,buff>>
	private ConcurrentHashMap<String,Map<String,lotluck>> cardluckdict;//卡牌装备缘分<卡牌code,<有缘卡牌code,buff>>
	private ConcurrentHashMap<String,AllgetDict> cardgetall;//<卡系列，集齐buff>
	private ConcurrentHashMap<Integer,Map<Integer, ArrayList<Map<String, Object>>>> talkdict;//<关卡id，<战前后,对话顺序列表<对话内容>>>>
	private ConcurrentHashMap<Integer,ActDict> svractdict;
	private ArrayList<ArrayList<String>> vipdescdict; //vip
	private ArrayList<ChipDict> chipdictlist;//<祈愿碎片列表> 
	private ArrayList<Map<String,Object>> useractbosslist;//<攻打过程Map>
	private ArrayList<Map<String,Object>> bossTop5;//<攻打过程Map>
	private ConcurrentHashMap<String,Map<String,Integer>> mergemarks;//<产生符文code，符文字典> 
	private WorldBossDict WorldBoss;
	private int messcount;//消息计数器
	private Timer systimer;//系统时间任务
	private int readyshut=0;
	private String bossdate="1999-01-01";
	private String rankdate="1999-01-01";
	private int gamelevel=0;//冲级活动等级
	public static ChannelGroup worldbosschannel = new DefaultChannelGroup();
	private Timer beattimer;//打boss时间任务	
	private String goldname;
	private String mname;
	private String ballname;
	public GameManager(){
		try {	
			this.goldname="金条";
			this.mname="钞票";
			this.ballname="转生丹";
			this.playerMap = new ConcurrentHashMap<Integer, User>();
			this.onlineMap = new  ConcurrentHashMap<String, User>();
			this.carddict= new ConcurrentHashMap<String, CardDict>();
			this.skilldict = new ConcurrentHashMap<String,SkillPages>();
			this.devsetdict= new ConcurrentHashMap<String,DeviceDict>();
			this.useruplevel=new ConcurrentHashMap<Integer,UserUpgradeVal>();
			this.markdict = new ConcurrentHashMap<String, MarkDict>();
			this.valuedict = new ConcurrentHashMap<Integer, Double>();
			this.marksetdict = new ConcurrentHashMap<String, MarkStringCA>(); //符文串字典
			this.devicesetdict=new ConcurrentHashMap<String, DeviceSetDict>(); //套装字典
			this.liveupdict = new  ConcurrentHashMap<String, LiveUpDictCla>();
			this.lifeupdict = new ConcurrentHashMap<Integer, CLifeUpDict>();
			this.logdict = new ConcurrentHashMap<Integer, LoginDict>() ;//<连续登录天数，字典>
			this.mergecarddict = new  ConcurrentHashMap<Integer,MergeCardDict>();
			this.deviceaddval = new ConcurrentHashMap<String,ConcurrentHashMap<String,ConcurrentHashMap<Integer,DeviceUpgrade>>>();
			this.missionscene = new ConcurrentHashMap<Integer,Integer>();
			this.scenedict = new ConcurrentHashMap<Integer,SceneDict>();
			this.cnpcdict = new ConcurrentHashMap<Integer,CNpcDict>();
			this.cmnpccdict = new  ConcurrentHashMap<Integer,ArrayList<CMissionNpcDict>>();
			this.cmissiondict=new ConcurrentHashMap<Integer,CMissionDict>();
			this.shopdict = new ConcurrentHashMap<String,ShopClass>();
			this.streetdict = new ConcurrentHashMap<Integer,StreetDict>();
			this.streetnpcdict=new ConcurrentHashMap<Integer,ArrayList<CMissionNpcDict>>();
			this.propsdict=new  ConcurrentHashMap<String,PropsDict>();
			this.shakedict=new ConcurrentHashMap<Integer,Map<String,Object>>();
			this.downdictl=new ConcurrentHashMap<Integer,ArrayList<downDict>>();//<掉落id，掉落>
			this.pkexpmon=new ConcurrentHashMap<Integer,Pkexpmoney>();
			this.cpdict =new  ConcurrentHashMap<Integer,Cpointdict>();
			this.uplvldict = new ConcurrentHashMap<Integer,UpLevelDict>();
			this.logmessdict = new ConcurrentHashMap<Integer,messageClass>();
			this.lockrank=new ConcurrentHashMap<String,Integer>();
			this.lockgetbook =new ConcurrentHashMap<String,Integer>();
			this.usersendmess = new ConcurrentHashMap<Integer,String>();
			this.carddevicedict=new ConcurrentHashMap<String,Map<String,lotluck>>();
			this.cardluckdict=new ConcurrentHashMap<String,Map<String,lotluck>>();
			this.cardskilldict= new ConcurrentHashMap<String,Map<String,lotluck>>();
			this.cardgetall=new ConcurrentHashMap<String,AllgetDict>();
			this.talkdict = new ConcurrentHashMap<Integer,Map<Integer, ArrayList<Map<String, Object>>>>();
			this.svractdict = new ConcurrentHashMap<Integer,ActDict>();
			this.chipdictlist = new ArrayList<ChipDict>();
			this.bossTop5=new ArrayList<Map<String,Object>>();
			this.useractbosslist=new ArrayList<Map<String,Object>>();
			this.vipdescdict= new ArrayList<ArrayList<String>>();
			this.WorldBoss = new WorldBossDict(props.getProperty("DeamonExp").toString());
			this.mergemarks =new ConcurrentHashMap<String,Map<String,Integer>>();
			this.messcount=0;
			this.runnum=0;
			this.readyshut=0;
			this.gamelevel=Integer.parseInt(props.getProperty("ServerTimes").toString());//冲级活动值
			this.randboss=props.getProperty("DeamonList").toString().split(",");
			bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
			bootstrap.setPipelineFactory(new MessageClientPipelineFactory());
			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("keepAlive", true);
			this.beattimer=null;
			this.systimer= new Timer();
			this.systimer.schedule(new SysTimeTask(), 300000, 300000);//启动系统任务
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}		
	}
	public void init(){ //获得各字典数据
		GameServerDao dao = new GameServerDao();
		try {
			dao.getBaseDict(props.getProperty("ServerCode").toString());
		} catch (SQLException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
	}
	public static GameManager getInstance(){
		if(instance == null){
			instance = new GameManager();	
		}
		return instance;
	}
	
	public void connectAuthServer(){
		if(!sendServerInfo){
			future = bootstrap.connect(new InetSocketAddress(props.getProperty("AuthServer_IP"),Integer.parseInt(props.getProperty("AuthServer_Port"))));
			future.awaitUninterruptibly();
			if(!future.isSuccess()){
				logger.info("Connect AuthServer 错误...");
				future.getChannel().getCloseFuture().awaitUninterruptibly();
				sendServerInfo=false;
			}
			else{
				if(!sendServerInfo){
					sendServerInfo=true;
					logger.info(props.getProperty("ServerName"));
					future.getChannel().write(props.getProperty("ServerCode")+","+props.getProperty("ServerName")+","
							+props.getProperty("Public_IP")+","	+props.getProperty("Public_Port"));
				}
			}
		}
	}
	public void RefreshMess(){
		 GameServerDao dao =new GameServerDao();
		 try {
			dao.getloginmess(props.getProperty("ServerCode").toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SendTo(Map<String, Object> uMap){ //发送消息至消息服务器
		if(!future.isSuccess()){
			logger.info("Connect AuthServer Error...");
			future.getChannel().getCloseFuture().awaitUninterruptibly();
		}
		else{
			future.getChannel().write(uMap);
		}
	}
	//用户对象集合
   public synchronized void addPlayers(User player){
	   this.playerMap.put(player.getChannel().getId(), player);
	   this.onlineMap.put(player.getUrole(), player);
   }

   public  void exitPlayer(int chid){
	   GameServerDao dao =new GameServerDao();
	   if(this.playerMap.containsKey(chid)){
		   User p=this.playerMap.get(chid);
		   try {
			   if(GameManager.worldbosschannel.find(chid)!=null) GameManager.worldbosschannel.remove(p.getChannel());//移除信道
			   if(p.getTimer()!=null){
				   //logger.info("停止心跳计时");
				   p.getTimer().cancel();
				   p.setTimer(null);
			   }
			   if(p.getTrade()>0) dao.newuserreback(p);
			   else   dao.PlayerExitUpdate(p);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		   synchronized(this) {
			   this.onlineMap.remove(p.getUrole());
			   this.playerMap.remove(chid);		   
		   }	   
	   }
	   
   }
   public void checkSysTask(){
	   GameServerDao dao = new GameServerDao();
	   try {
		dao.updateOnline(this.playerMap.size(),props.getProperty("ServerCode").toString());
		dao.CheckDictAlter(props.getProperty("ServerCode").toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}			
   }
   public void ReadyShutSvr(){
	   this.readyshut=1;
	   String svrcode=props.getProperty("ServerCode").toString();
	   ArrayList<User> al = new ArrayList<User>();
	   Iterator<User> usit = this.playerMap.values().iterator();
		while(usit.hasNext()){
			User us=usit.next();
			al.add(us);
		}
		for(int i=0;i<al.size();i++){
			al.get(i).getChannel().disconnect();			
		}
		GameServerDao dao = new GameServerDao();
		try{
			dao.ReadyShutSvr(svrcode);
		}catch (SQLException e) {
			e.printStackTrace();
		}
   }
   public void queryplayer(String stime,String etime,Map<String,Object> smap){
	   String svrcode=props.getProperty("ServerCode").toString();
	   GameServerDao dao = new GameServerDao();
		try{
			dao.queryplayer(svrcode,stime,etime,smap);
		}catch (SQLException e) {
			e.printStackTrace();
		}
   }
   public void querymoney(String stime,String etime,Map<String,Object> smap){
	   String svrcode=props.getProperty("ServerCode").toString();
	   GameServerDao dao = new GameServerDao();
		try{
			dao.querymoney(svrcode,stime,etime,smap);
		}catch (SQLException e) {
			e.printStackTrace();
		}
   }
   
	public void ResetWorldBoss(){ //重置世界boss数据
		GameServerDao dao = new GameServerDao();
		try {
			if(this.beattimer!=null){
				this.beattimer.cancel();
				this.beattimer=null;
			}
			worldbosschannel.clear();
			this.useractbosslist.clear();
			dao.ResetBossWorld();
			this.bossTop5.clear();
		} catch (SQLException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
	}
	//选择自动打世界boss
	public void AutoCalcBoss(){
		if(!this.bossdate.equals(CommTools.TimestampToStr(System.currentTimeMillis()))&&!this.WorldBoss.getCurtime().equals(CommTools.TimestampToStr(System.currentTimeMillis()))){
			GameServerDao dao = new GameServerDao();
			try {
				dao.AutoCalcBoss(props.getProperty("ServerCode").toString());
				this.bossdate=CommTools.TimestampToStr(System.currentTimeMillis());
			} catch (SQLException e) {
				logger.info(e.toString());
				e.printStackTrace();
			}
		}
		
	}
	//打倒排名结算
	public void CalcBossRank(){
		if(this.WorldBoss.getDeadTime()>0&&!this.rankdate.equals(CommTools.TimestampToStr(System.currentTimeMillis()))&&
				!this.WorldBoss.getCurtime().equals(CommTools.TimestampToStr(System.currentTimeMillis()))){
			GameServerDao dao = new GameServerDao();
			try {
				dao.CalcBossRank(props.getProperty("ServerCode").toString());
				this.rankdate=CommTools.TimestampToStr(System.currentTimeMillis());
			} catch (SQLException e) {
				logger.info(e.toString());
				e.printStackTrace();
			}
		}
		
	}
   
	public static Properties getProps() {
		return props;
	}


	public static void setProps(Properties props) {
		GameManager.props = props;
	}

	public boolean getSendServerInfo() {
		return sendServerInfo;
	}

	public void setSendServerInfo(boolean sendServerInfo) {
		this.sendServerInfo = sendServerInfo;
	}

	public ConcurrentHashMap<Integer, User> getPlayerMap() {
		return playerMap;
	}

	public void setPlayerMap(ConcurrentHashMap<Integer, User> playerMap) {
		this.playerMap = playerMap;
	}

	public ConcurrentHashMap<String, CardDict> getCarddict() {
		return carddict;
	}

	public void setCarddict(ConcurrentHashMap<String, CardDict> carddict) {
		this.carddict = carddict;
	}

	public ConcurrentHashMap<String, DeviceDict> getDevsetdict() {
		return devsetdict;
	}

	public void setDevsetdict(ConcurrentHashMap<String, DeviceDict> devsetdict) {
		this.devsetdict = devsetdict;
	}

	public ConcurrentHashMap<String, MarkDict> getMarkdict() {
		return markdict;
	}

	public void setMarkdict(ConcurrentHashMap<String, MarkDict> markdict) {
		this.markdict = markdict;
	}

	public ConcurrentHashMap<Integer, Double> getValuedict() {
		return valuedict;
	}

	public void setValuedict(ConcurrentHashMap<Integer, Double> valuedict) {
		this.valuedict = valuedict;
	}

	public ConcurrentHashMap<String, MarkStringCA> getMarksetdict() {
		return marksetdict;
	}

	public void setMarksetdict(ConcurrentHashMap<String, MarkStringCA> marksetdict) {
		this.marksetdict = marksetdict;
	}

	public ConcurrentHashMap<String, DeviceSetDict> getDevicesetdict() {
		return devicesetdict;
	}

	public void setDevicesetdict(ConcurrentHashMap<String, DeviceSetDict> devicesetdict) {
		this.devicesetdict = devicesetdict;
	}
	public ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<Integer, DeviceUpgrade>>> getDeviceaddval() {
		return deviceaddval;
	}
	public void setDeviceaddval(
			ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<Integer, DeviceUpgrade>>> deviceaddval) {
		this.deviceaddval = deviceaddval;
	}
	public ConcurrentHashMap<String, LiveUpDictCla> getLiveupdict() {
		return liveupdict;
	}
	public void setLiveupdict(ConcurrentHashMap<String, LiveUpDictCla> liveupdict) {
		this.liveupdict = liveupdict;
	}
	public ConcurrentHashMap<Integer, CLifeUpDict> getLifeupdict() {
		return lifeupdict;
	}
	public void setLifeupdict(ConcurrentHashMap<Integer, CLifeUpDict> lifeupdict) {
		this.lifeupdict = lifeupdict;
	}
	public ConcurrentHashMap<Integer, MergeCardDict> getMergecarddict() {
		return mergecarddict;
	}
	public void setMergecarddict(
			ConcurrentHashMap<Integer, MergeCardDict> mergecarddict) {
		this.mergecarddict = mergecarddict;
	}
	public ConcurrentHashMap<Integer, LoginDict> getLogdict() {
		return logdict;
	}
	public void setLogdict(ConcurrentHashMap<Integer, LoginDict> logdict) {
		this.logdict = logdict;
	}
	public ConcurrentHashMap<Integer,ArrayList<CMissionNpcDict>> getCmnpccdict() {
		return cmnpccdict;
	}
	public void setCmnpccdict(ConcurrentHashMap<Integer,ArrayList<CMissionNpcDict>> cmnpccdict) {
		this.cmnpccdict = cmnpccdict;
	}
	public ConcurrentHashMap<Integer, CMissionDict> getCmissiondict() {
		return cmissiondict;
	}
	public void setCmissiondict(
			ConcurrentHashMap<Integer, CMissionDict> cmissiondict) {
		this.cmissiondict = cmissiondict;
	}
	public ConcurrentHashMap<Integer, Integer> getMissionscene() {
		return missionscene;
	}
	public void setMissionscene(ConcurrentHashMap<Integer, Integer> missionscene) {
		this.missionscene = missionscene;
	}
	public ConcurrentHashMap<String, SkillPages> getSkilldict() {
		return skilldict;
	}
	public void setSkilldict(ConcurrentHashMap<String, SkillPages> skilldict) {
		this.skilldict = skilldict;
	}
	public ConcurrentHashMap<Integer, SceneDict> getScenedict() {
		return scenedict;
	}
	public void setScenedict(ConcurrentHashMap<Integer, SceneDict> scenedict) {
		this.scenedict = scenedict;
	}
	public ConcurrentHashMap<Integer, UserUpgradeVal> getUseruplevel() {
		return useruplevel;
	}
	public void setUseruplevel(ConcurrentHashMap<Integer, UserUpgradeVal> useruplevel) {
		this.useruplevel = useruplevel;
	}
	public ConcurrentHashMap<Integer, CNpcDict> getCnpcdict() {
		return cnpcdict;
	}
	public void setCnpcdict(ConcurrentHashMap<Integer, CNpcDict> cnpcdict) {
		this.cnpcdict = cnpcdict;
	}
	public ConcurrentHashMap<String, ShopClass> getShopdict() {
		return shopdict;
	}
	public void setShopdict(ConcurrentHashMap<String, ShopClass> shopdict) {
		this.shopdict = shopdict;
	}
	public ConcurrentHashMap<Integer, StreetDict> getStreetdict() {
		return streetdict;
	}
	public void setStreetdict(ConcurrentHashMap<Integer, StreetDict> streetdict) {
		this.streetdict = streetdict;
	}
	public ConcurrentHashMap<Integer, ArrayList<downDict>> getDowndictl() {
		return downdictl;
	}
	public void setDowndictl(
			ConcurrentHashMap<Integer, ArrayList<downDict>> downdictl) {
		this.downdictl = downdictl;
	}
	public ConcurrentHashMap<Integer,ArrayList<CMissionNpcDict>> getStreetnpcdict() {
		return streetnpcdict;
	}
	public void setStreetnpcdict(
			ConcurrentHashMap<Integer,ArrayList<CMissionNpcDict>> streetnpcdict) {
		this.streetnpcdict = streetnpcdict;
	}
	public ConcurrentHashMap<String, PropsDict> getPropsdict() {
		return propsdict;
	}
	public void setPropsdict(ConcurrentHashMap<String, PropsDict> propsdict) {
		this.propsdict = propsdict;
	}
	public ConcurrentHashMap<Integer, Map<String, Object>> getShakedict() {
		return shakedict;
	}
	public void setShakedict(
			ConcurrentHashMap<Integer, Map<String, Object>> shakedict) {
		this.shakedict = shakedict;
	}
	public ConcurrentHashMap<Integer, Pkexpmoney> getPkexpmon() {
		return pkexpmon;
	}
	public void setPkexpmon(ConcurrentHashMap<Integer, Pkexpmoney> pkexpmon) {
		this.pkexpmon = pkexpmon;
	}
	public ConcurrentHashMap<Integer, Cpointdict> getCpdict() {
		return cpdict;
	}
	public void setCpdict(ConcurrentHashMap<Integer, Cpointdict> cpdict) {
		this.cpdict = cpdict;
	}
	public Timer getSystimer() {
		return systimer;
	}
	public void setSystimer(Timer systimer) {
		this.systimer = systimer;
	}
	public ConcurrentHashMap<Integer, UpLevelDict> getUplvldict() {
		return uplvldict;
	}
	public void setUplvldict(ConcurrentHashMap<Integer, UpLevelDict> uplvldict) {
		this.uplvldict = uplvldict;
	}
	public ConcurrentHashMap<Integer, messageClass> getLogmessdict() {
		return logmessdict;
	}
	public void setLogmessdict(ConcurrentHashMap<Integer, messageClass> logmessdict) {
		this.logmessdict = logmessdict;
	}
	public int getRunnum() {
		return runnum;
	}
	public void setRunnum(int runnum) {
		this.runnum = runnum;
	}
	public ConcurrentHashMap<String, User> getOnlineMap() {
		return onlineMap;
	}
	public void setOnlineMap(ConcurrentHashMap<String, User> onlineMap) {
		this.onlineMap = onlineMap;
	}
	public ConcurrentHashMap<String, Integer> getLockrank() {
		return lockrank;
	}
	public void setLockrank(ConcurrentHashMap<String, Integer> lockrank) {
		this.lockrank = lockrank;
	}
	public ConcurrentHashMap<String, Integer> getLockgetbook() {
		return lockgetbook;
	}
	public void setLockgetbook(ConcurrentHashMap<String, Integer> lockgetbook) {
		this.lockgetbook = lockgetbook;
	}
	public ConcurrentHashMap<Integer, String> getUsersendmess() {
		return usersendmess;
	}
	public void setUsersendmess(ConcurrentHashMap<Integer, String> usersendmess) {
		this.usersendmess = usersendmess;
	}
	public int getMesscount() {
		return messcount;
	}
	public void setMesscount(int messcount) {
		this.messcount = messcount;
	}
	public ConcurrentHashMap<String, Map<String, lotluck>> getCarddevicedict() {
		return carddevicedict;
	}
	public void setCarddevicedict(
			ConcurrentHashMap<String, Map<String, lotluck>> carddevicedict) {
		this.carddevicedict = carddevicedict;
	}
	public ConcurrentHashMap<String, Map<String, lotluck>> getCardluckdict() {
		return cardluckdict;
	}
	public void setCardluckdict(
			ConcurrentHashMap<String, Map<String, lotluck>> cardluckdict) {
		this.cardluckdict = cardluckdict;
	}
	public ConcurrentHashMap<String, Map<String, lotluck>> getCardskilldict() {
		return cardskilldict;
	}
	public void setCardskilldict(
			ConcurrentHashMap<String, Map<String, lotluck>> cardskilldict) {
		this.cardskilldict = cardskilldict;
	}
	public int getReadyshut() {
		return readyshut;
	}
	public void setReadyshut(int readyshut) {
		this.readyshut = readyshut;
	}

	public ConcurrentHashMap<String, AllgetDict> getCardgetall() {
		return cardgetall;
	}
	public void setCardgetall(ConcurrentHashMap<String, AllgetDict> cardgetall) {
		this.cardgetall = cardgetall;
	}
	public int getGamelevel() {
		return gamelevel;
	}
	public void setGamelevel(int gamelevel) {
		this.gamelevel = gamelevel;
	}
	public ConcurrentHashMap<Integer, Map<Integer, ArrayList<Map<String, Object>>>> getTalkdict() {
		return talkdict;
	}
	public void setTalkdict(ConcurrentHashMap<Integer, Map<Integer, ArrayList<Map<String, Object>>>> talkdict) {
		this.talkdict = talkdict;
	}
	public ConcurrentHashMap<Integer, ActDict> getSvractdict() {
		return svractdict;
	}
	public void setSvractdict(ConcurrentHashMap<Integer, ActDict> svractdict) {
		this.svractdict = svractdict;
	}
	public ArrayList<ChipDict> getChipdictlist() {
		return chipdictlist;
	}
	public void setChipdictlist(ArrayList<ChipDict> chipdictlist) {
		this.chipdictlist = chipdictlist;
	}
	public WorldBossDict getWorldBoss() {
		return WorldBoss;
	}
	public void setWorldBoss(WorldBossDict worldBoss) {
		WorldBoss = worldBoss;
	}
	public ArrayList<Map<String, Object>> getUseractbosslist() {
		return useractbosslist;
	}
	public void setUseractbosslist(ArrayList<Map<String, Object>> useractbosslist) {
		this.useractbosslist = useractbosslist;
	}
	public ArrayList<Map<String, Object>> getBossTop5() {
		return bossTop5;
	}
	public void setBossTop5(ArrayList<Map<String, Object>> bossTop5) {
		this.bossTop5 = bossTop5;
	}
	
	public ConcurrentHashMap<String, Map<String, Integer>> getMergemarks() {
		return mergemarks;
	}
	public void setMergemarks(
			ConcurrentHashMap<String, Map<String, Integer>> mergemarks) {
		this.mergemarks = mergemarks;
	}
	public static ChannelGroup getWorldbosschannel() {
		return worldbosschannel;
	}
	public static void setWorldbosschannel(ChannelGroup worldbosschannel) {
		GameManager.worldbosschannel = worldbosschannel;
	}
	public Timer getBeattimer() {
		return beattimer;
	}
	public void setBeattimer(Timer beattimer) {
		this.beattimer = beattimer;
	}
	public String getRankdate() {
		return rankdate;
	}
	public void setRankdate(String rankdate) {
		this.rankdate = rankdate;
	}
	public String[] getRandboss() {
		return randboss;
	}
	public void setRandboss(String[] randboss) {
		this.randboss = randboss;
	}
	public ArrayList<ArrayList<String>> getVipdescdict() {
		return vipdescdict;
	}
	public void setVipdescdict(ArrayList<ArrayList<String>> vipdescdict) {
		this.vipdescdict = vipdescdict;
	}
	public String getGoldname() {
		return goldname;
	}
	public void setGoldname(String goldname) {
		this.goldname = goldname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getBallname() {
		return ballname;
	}
	public void setBallname(String ballname) {
		this.ballname = ballname;
	}


	
}
