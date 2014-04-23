package com.gameserver.dao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.sql.CallableStatement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.gameserver.comm.C3P0DBConnectionPool;
import com.gameserver.comm.CommTools;
import com.gameserver.comm.PacketCommandType;
import com.gameserver.logic.CardLogicProc;
import com.gameserver.manager.GameManager;
import com.gameserver.model.ActDict;
import com.gameserver.model.AllgetDict;
import com.gameserver.model.CLifeUpDict;
import com.gameserver.model.CMissionDict;
import com.gameserver.model.CMissionNpcDict;
import com.gameserver.model.CNpcDict;
import com.gameserver.model.CUserChip;
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
import com.gameserver.model.UserCard;
import com.gameserver.model.UserDayTask;
import com.gameserver.model.UserDevice;
import com.gameserver.model.UserEgg;
import com.gameserver.model.UserLuckAward;
import com.gameserver.model.UserMailBox;
import com.gameserver.model.UserMark;
import com.gameserver.model.UserMission;
import com.gameserver.model.UserProps;
import com.gameserver.model.UserSkill;
import com.gameserver.model.UserStreet;
import com.gameserver.model.UserUpgradeVal;
import com.gameserver.model.WorldBossDict;
import com.gameserver.model.downDict;
import com.gameserver.model.lotluck;
import com.gameserver.model.messageClass;
import com.gameserver.model.packageGoods;


public class GameServerDao implements Serializable{
	/**
	 * UNO游戏服务器数据访问类
	 */
	private static final long serialVersionUID = 1443335955475553532L;
	private static C3P0DBConnectionPool pool = C3P0DBConnectionPool.getInstance();
	private static final Logger logger = Logger.getLogger(GameServerDao.class.getName());
	/**
	 * 获取玩家基本信息
	 * @throws Exception 
	 */
	public void getPlayerInfo(User player, String un,String ur,String scode) throws SQLException {
		
		String sql ="SELECT * FROM userroletbl WHERE username=? AND rolename=? AND serverno=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		String curlogin=dateToString(new Date());
		int flag=0;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, un);
			ps.setString(2, ur);
			ps.setString(3, scode);
			rs = ps.executeQuery();
			if(rs.next()){
				player.setFirstlogin(rs.getString("firstlogin"));
				player.setCurrlogin(curlogin);//最近登录
				player.setLevel(rs.getInt("level"));
				player.setExp(rs.getInt("exp")); //经验
				player.setPower(rs.getInt("power")); //当前体力值
				player.setMaxpower(rs.getInt("maxpower")); //体力值上限
				player.setMembers(rs.getInt("members")); //	队伍上限	
				player.setPowerstart(rs.getInt("powerstart"));//体力计时
				player.setPerpoint(rs.getInt("perpoint")); //每点恢复时间 秒级时间戳
				player.setLevelup(rs.getInt("levelup")); //升级经验值
				player.setViplevel(rs.getInt("viplevel")); //vip等级
				player.setMoney(rs.getInt("money")) ; //钞票
				player.setGold(rs.getInt("gold")); //金条
				player.setInnercoin(rs.getInt("innercoin")) ; //修改为是否领取连续登陆奖
				player.setGod(rs.getInt("god")) ; //女神泪
				if(player.getGod()>0) throw new SQLException("banduser");//封停用户
				player.setBall(rs.getInt("ball")) ; //转生丹
				player.setGoldrenew(rs.getInt("goldrenew")) ; //金条恢复次数
				player.setGetbook(rs.getInt("getbook")); //夺书次数
				player.setGoldaddnum(rs.getInt("goldaddnum")) ;//金条补充次数
				player.setTrade(rs.getInt("trade")) ; //新手引导标记
				player.setAddvalue(rs.getInt("addvalue"));//补正百分比
				player.setCurmission(rs.getInt("curmission")); //当前关卡id
				player.setMissionsum(rs.getInt("missionsum"));//任务关卡次数
				player.setPknum(rs.getInt("playerpknum"));//pk次数
				player.setGetskstart(rs.getInt("getskstart"));
				player.setEveryskp(rs.getInt("everyskp"));
				player.setRankseq(rs.getInt("currank"));
				player.setCurpoints(rs.getInt("curpoints"));
				player.setFriendsum(rs.getInt("friendsum"));
				player.setBuyfight(rs.getInt("buyfight"));//购买争斗次数
				player.setGoldtree(rs.getInt("goldtree"));
				player.setResetstreet(rs.getInt("resetstreet"));
				player.setResetshop(rs.getInt("resetshop"));
				player.setCurstreetid(rs.getInt("curstreetid"));
				player.setCurdesid(rs.getInt("curdesid"));
				player.setCurmessid(rs.getInt("curmessid"));//最新系统消息id
				player.setChargenum(rs.getInt("chargnum"));//已充值金额
				player.setContinuelog(rs.getInt("continuelog"));//连续登录天数
				player.setBosscdval(rs.getInt("bosscd"));//世界boss cd
				player.setTaskcdval(rs.getInt("taskcd"));//连闯任务 cd	
				player.setBlessnum(rs.getInt("blessnum"));//当日已祈愿次数
				player.setBlessall(Integer.parseInt(GameManager.getInstance().getShakedict().get(player.getViplevel()).get("AL").toString()));//免费次数
				player.setAutoboss(rs.getInt("autoboss"));
				player.setAdvps(rs.getInt("advps"));
				player.setStdps(rs.getInt("stdps"));
				player.setFriendpknum(rs.getInt("friendpknum"));
				//player.setBeatboss(rs.getInt("beatboss"));
				flag=checklog(rs.getString("currlogin"),player.getCurrlogin());
				//logger.info("登录日期"+rs.getString("currlogin")+" new:"+player.getCurrlogin());
				if(flag==1){//间隔1日 重置夺技次数、重置金条补充体力次数、金条补充次数
					if(player.getContinuelog()<5){//领取登录奖5天不再记录
						player.setContinuelog(rs.getInt("continuelog")+1);
					}				
					player.setGoldrenew(0);
					if(player.getGetbook()<10) 	player.setGetbook(10);
					player.setGetskstart(0);
					if(player.getPknum()<10)  player.setPknum(10);
					player.setGoldtree(0);
					player.setResetstreet(0);
					player.setBuyfight(0);
					player.setResetshop(0);
					player.setCurstreetid(1);
					player.setCurdesid(1);
					player.setGoldaddnum(0);
					player.setBlessnum(0);
					player.setBosscdval(0);
					player.setAdvps(5);
					player.setStdps(5);
					player.setFriendpknum(0);
					//自动打boss 隔天登录自动扣钱 存储过程也做同样判断扣钱
					if(player.getAutoboss()>0){
						if(player.getGold()<100) player.setAutoboss(0);
						else player.setGold(player.getGold()-100);
					}
					switch(player.getContinuelog()){
					case 1:
						if(String.valueOf(player.getInnercoin()).substring(0, 1).equals("1")) player.setInnercoin(player.getInnercoin()+10000);
						break;
					case 2:
						if(String.valueOf(player.getInnercoin()).substring(1, 2).equals("1")) player.setInnercoin(player.getInnercoin()+1000);
						break;
					case 3:
						if(String.valueOf(player.getInnercoin()).substring(2, 3).equals("1")) player.setInnercoin(player.getInnercoin()+100);
						break;
					case 4:
						if(String.valueOf(player.getInnercoin()).substring(3, 4).equals("1"))  player.setInnercoin(player.getInnercoin()+10);
						break;
					case 5:
						if(String.valueOf(player.getInnercoin()).substring(4, 5).equals("1")) player.setInnercoin(player.getInnercoin()+1);
						break;
					}
				}else if(flag>1){
					if(player.getContinuelog()<5){
						player.setContinuelog(1);
					}					
					player.setGoldrenew(0);
					if(player.getGetbook()<10) 	player.setGetbook(10);
					player.setGetskstart(0);
					if(player.getPknum()<10)  player.setPknum(10);
					player.setGoldtree(0);
					player.setResetstreet(0);
					player.setBuyfight(0);
					player.setResetshop(0);
					player.setCurstreetid(1);
					player.setCurdesid(1);
					player.setGoldaddnum(0);
					player.setBlessnum(0);
					player.setBosscdval(0);
					player.setAdvps(5);
					player.setStdps(5);
					player.setFriendpknum(0);
					//自动打boss 隔天登录自动扣钱 存储过程也做同样判断扣钱
					if(player.getAutoboss()>0){
						if(player.getGold()<100) player.setAutoboss(0);
						else player.setGold(player.getGold()-100);
					}
				}
				int pflag=0;
				if(player.getPowerstart()>0){
					pflag=CheckPower(player.getPowerstart(),player.getPower(),player.getPerpoint());
					if(pflag>0){//补充体力点数
						if(pflag+player.getPower()>=player.getMaxpower()){
							player.setPower(player.getMaxpower());
							player.setPowerstart(0);
						}else
						{
							player.setPower(pflag+player.getPower());
							player.setPowerstart((int)(System.currentTimeMillis()/1000));
						}
					}
				}	
				if(player.getGetskstart()>0){
					pflag=CheckPower(player.getGetskstart(),player.getGetbook(),player.getEveryskp());
					if(pflag>0){//恢复夺技次数
						if(pflag+player.getGetbook()>=10){
							player.setGetbook(10);
							player.setGetskstart(0);
						}else
						{
							player.setGetbook(pflag+player.getGetbook());
							player.setGetskstart((int)(System.currentTimeMillis()/1000));
						}
					}
				}
				int curtimestamp=(int)(System.currentTimeMillis()/1000);
				if(player.getBosscdval()>0){//世界boss cd
					int bcd=Integer.parseInt(GameManager.getInstance().getShakedict().get(player.getViplevel()).get("AJ").toString());//boss冷却时长
					if(curtimestamp-player.getBosscdval()>=bcd){//cd时间到
						player.setBosscdval(0);
					}
				}
				if(player.getTaskcdval()>0){//连闯任务 cd
					int bcd=Integer.parseInt(GameManager.getInstance().getShakedict().get(player.getViplevel()).get("AK").toString());//连闯冷却时长
					if(curtimestamp-player.getTaskcdval()>=bcd){//cd时间到
						player.setTaskcdval(0);
					}
				}	
			}
			rs.close();
			ps.close();
			//新登录设置日常任务完成情况
			// 登录后可能会变更的数据保存 最近登录时间、当前体力、体力恢复计时、连续登录天数 
			CallableStatement cs = null;
			cs = conn.prepareCall("{call UPDATEPLAYERINFO(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setString(1,un);
			cs.setString(2,ur);
			cs.setString(3,scode);
			cs.setString(4, player.getCurrlogin());
			cs.setInt(5, player.getPower());
			cs.setInt(6, player.getPowerstart());
			cs.setInt(7, player.getGetskstart());
			cs.setInt(8, player.getGetbook());
			cs.setInt(9,player.getContinuelog());
			cs.setInt(10,player.getPknum());
			cs.setInt(11,flag);
			cs.registerOutParameter(12, Types.INTEGER);
			cs.registerOutParameter(13, Types.INTEGER);
			cs.setInt(14,player.getInnercoin());
			cs.setInt(15,player.getGold());
			cs.setInt(16,player.getAutoboss());
			cs.execute();
			//logger.info("参数"+player.getPower()+","+player.getPowerstart()+","+player.getGoldrenew()+","+player.getGetbook()+","+player.getGoldaddnum()+","+flag);
			if(cs.getInt(12)==0){
				cs.close();
				throw new SQLException("role1");
			}	
			player.setUndotask(cs.getInt(13));
			cs.close();
			//获取玩家装备 技能，符文
			this.getPlayerMark(player, ur, scode);
			this.getPlayerSkill(player, ur, scode);
			this.getPlayerDevice(player, ur, scode);//
			this.getPlayerUseCard(player,ur,scode);//获取玩家卡牌
			//获取卡牌上装备，技能，符文？？？
			this.getPlayerSkillPage(player,ur,scode);//获取玩家技能书页
			this.getpropslist(player);
			this.CheckUserGetall(player);//获取玩家集齐map
			this.GetChipBag(player);//获取玩家碎片背包
			GetCurrMission(player);
		} 
		catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;			
		}
		finally{
			conn.close();
		}		
	}
	//获取玩家卡牌
	public void getPlayerUseCard(User player, String ur,String scode) throws SQLException {
		String sql ="SELECT * FROM usercard"+scode+" WHERE rolename=? AND SERVERNO=?"; //阵容中的卡牌
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, ur);
			ps.setString(2, scode);
			rs = ps.executeQuery();
			while(rs.next()){
				UserCard mycard = new UserCard();
				mycard.setCardid(rs.getInt("cardid"));//玩家卡牌id
				mycard.setCardcode(rs.getString("cardcode"));
				mycard.setCardname(rs.getString("cardname")); //卡牌名称
				mycard.setCarddesc(rs.getString("carddesc")); //描述
				mycard.setCardprop(rs.getString("cardprop")) ;  //属性邪、萌、冷、霸
				mycard.setStarlevel(rs.getInt("starlevel")) ;  //星级
				mycard.setCardtype(rs.getString("cardtype")) ; //系列 同系2个以上有增益
				mycard.setUpexp(rs.getInt("upexp")) ;     //升级经验
				mycard.setCurexp(rs.getInt("curexp")) ;   //当前经验
				mycard.setCardlevel(rs.getInt("cardlevel"));//卡牌等级
				mycard.setCurvalue(rs.getInt("curvalue")) ;  //魅力值
				mycard.setBasehp(rs.getDouble("basehp")) ; //基础血成长
				mycard.setBaseatk(rs.getDouble("baseatk")) ;   //基础攻成长
				mycard.setBasedef(rs.getDouble("basedef")) ;   //基础防成长
				mycard.setAlterhp(rs.getDouble("alterhp")) ;   //基础转生血成长
				mycard.setAlteratk(rs.getDouble("alteratk")) ;  //基础转生攻成长
				mycard.setAlterdef(rs.getDouble("alterdef")) ;  //基础转生防成长
				mycard.setHp(rs.getInt("hp"));  //血量
				mycard.setAtk(rs.getInt("atk")) ; //攻
				mycard.setDef(rs.getInt("def")) ;  //防
				mycard.setCurstyle(rs.getInt("curstyle")) ;  //当前形态
				mycard.setIfuse(rs.getInt("ifuse")) ;   //是否阵容中
				mycard.setLocate(rs.getInt("locate")) ;   //阵容中位置
				mycard.setSetid(rs.getString("setid")) ; //套装code串
				mycard.setOrighp(rs.getInt("orighp"));//裸身血
				mycard.setOrigatk(rs.getInt("origatk"));//裸身攻
				mycard.setOrigdef(rs.getInt("origdef"));//裸身防
				mycard.setHavedevice(rs.getInt("havedevice"));//是否有装备
				mycard.setHaveskill(rs.getInt("haveskill")); //是否有技能
				mycard.setLivehp(rs.getInt("livehp")); //养成值
				mycard.setLiveatk(rs.getInt("liveatk"));
				mycard.setLivedef(rs.getInt("livedef"));
				mycard.setTlivehp(rs.getInt("tlivehp")); //临时记录养成值
				mycard.setTliveatk(rs.getInt("tliveatk"));
				mycard.setTlivedef(rs.getInt("tlivedef"));
				mycard.setLifeskill(rs.getString("lifeskill"));
				mycard.setCurpointval(rs.getInt("curpointval"));//卡牌当前魅力点数
				player.getCardlist().put(mycard.getCardid(), mycard); //玩家全部卡牌
				if(mycard.getIfuse()==1&&mycard.getLocate()>=0){
					player.getInusecards().put(mycard.getCardid(), mycard); //记录阵容中卡牌
					player.getCardlocid()[mycard.getLocate()]=mycard.getCardid();//记录位置对应的卡牌id
				}
				this.getCardDevice(player, mycard, ur, scode);
				this.getCardSkill(player, mycard, ur, scode);
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	//获取用户的装备
	public void getPlayerDevice(User p,String ur,String scode) throws SQLException{
		String sql ="SELECT * FROM userdevice"+p.getSvrcode()+" WHERE rolename=? AND serverno=? "; //卡牌上的装备
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, ur);
			ps.setString(2, scode);
			rs = ps.executeQuery();
			while(rs.next()){
				UserDevice mydevice = new UserDevice();
				mydevice.setDeviceid(rs.getInt("deviceid")) ; //装备id
				mydevice.setDevicecode(rs.getString("devicecode"));
				mydevice.setDevicename(rs.getString("devicename")) ; //装备名称
				mydevice.setDevicedesc(rs.getString("devicedesc")) ; //描述
				mydevice.setDevicetype(rs.getString("devicetype")) ;  //类型 攻防饰
				mydevice.setQuality(rs.getString("quality")) ;  //质量
				mydevice.setGrade(rs.getInt("grade")) ;  //等级
				mydevice.setDhp(rs.getInt("dhp")) ;  //血增益
				mydevice.setDatk(rs.getInt("datk")) ;  //攻增益
				mydevice.setDdef(rs.getInt("ddef")) ;  //防增益
				mydevice.setBaseprice(rs.getInt("baseprice")) ;  //基础价
				mydevice.setPrice(rs.getInt("price")) ;  //实际价
				mydevice.setCardid(rs.getInt("cardid")) ;  //装备卡牌id
				mydevice.setMarkstring(rs.getString("markstring")) ; //符文组合串 空：无组合
				mydevice.setHavemark(rs.getInt("havemark")); //是否有符文
				mydevice.setDevcolor(rs.getInt("devcolor"));
				p.getUdevicelist().put(mydevice.getDeviceid(), mydevice); //记录用户所有装备
				this.getDeviceMark(p, mydevice, ur, scode);
								
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	//获取卡牌上安装的装备
	public void getCardDevice(User p,UserCard mycard, String ur,String scode) {
		Iterator<UserDevice> it = p.getUdevicelist().values().iterator();
		while(it.hasNext()){
			UserDevice mydevice=it.next();
			if(mydevice.getCardid()==mycard.getCardid()) 
				mycard.getDeviceMap().put(mydevice.getDeviceid(), mydevice); //记录此卡牌携带装备
			this.getDeviceMark(p, mydevice, ur, scode);
		}
	}
	//获取卡牌上的技能
	public void getCardSkill(User p,UserCard mycard, String ur,String scode){
		Iterator<UserSkill> it = p.getUskilllist().values().iterator();
		while(it.hasNext()){
			UserSkill cardskill=it.next();
			if(cardskill.getCardid()==mycard.getCardid())  //此技能已安装到卡牌上，记录卡牌技能列表
				mycard.getSkillMap().put(cardskill.getSkillid(), cardskill);
		}
	}
	//获取用户的技能
	public void getPlayerSkill(User p, String ur,String scode) throws SQLException{
		String sql ="SELECT * FROM userskill"+p.getSvrcode()+" WHERE rolename=? AND serverno=?"; //所有技能
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, ur);
			ps.setString(2, scode);
			rs = ps.executeQuery();
			while(rs.next()){
				UserSkill cardskill = new UserSkill();
				cardskill.setSkillid(rs.getInt("skillid")) ;  //技能id
				cardskill.setSkillcode(rs.getString("skillcode"));
				cardskill.setSkillname(rs.getString("skillname"))  ;   //技能名称
				cardskill.setSkilldesc(rs.getString("skilldesc")) ;   //技能描述
				cardskill.setSkilltype(rs.getString("skilltype")) ;   //技能类型 主动、被动
				cardskill.setSkillgrade(rs.getInt("skillgrade")) ;    // 技能等级
				cardskill.setSkillloc(rs.getInt("skillloc"));//技能位置 -1未安装
				cardskill.setIflife(rs.getInt("iflife"));
				SkillPages skp=GameManager.getInstance().getSkilldict().get(cardskill.getSkillcode());
				cardskill.setShp(skp.getHps()) ; //增血
				cardskill.setSatk(skp.getAtks()) ; //增攻
				cardskill.setSdef(skp.getDefs()) ; //增防
				cardskill.setCardid(rs.getInt("cardid")) ;  //装备卡id
				cardskill.setHurt(skp.getHurt());//装备上的主动伤害，伤害附加值
				cardskill.setSvs(skp.getSvs());
				cardskill.setSkillto(skp.getSkillto());
				cardskill.setSkillrand(skp.getSkillrand());
				cardskill.setHurtpert(skp.getHurtpert());//伤害百分比
				cardskill.setUphurt(skp.getUphurt());//升级伤害附加值增长百分比
				cardskill.setUphurtper(skp.getUphurtper());//升级伤害百分比增长百分比
				cardskill.setSkillcolor(skp.getSkillcolor());//技能颜色
				p.getUskilllist().put(cardskill.getSkillid(), cardskill); //记录用户技能列表				
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	//获取装备镶嵌的符文
	public void getDeviceMark(User p,UserDevice mydevice, String ur,String scode) {
		Iterator<UserMark> it = p.getUmarklist().values().iterator();
		while(it.hasNext()){
			UserMark mymark=it.next();
			if(mymark.getDeviceid()==mydevice.getDeviceid()&&mymark.getMarkloc()>=0)  //符文已插入，记录装备上插入的符文列表
				mydevice.getMarkMap().put(mymark.getMarkid(),mymark);
		}
	}
	
	//获取用户的符文
	public void getPlayerMark(User p,String ur,String scode) throws SQLException{
		String sql ="SELECT * FROM usermark"+p.getSvrcode()+" WHERE rolename=? AND serverno=?"; //装备镶嵌的符文
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, ur);
			ps.setString(2, scode);
			rs = ps.executeQuery();
			while(rs.next()){
				UserMark mymark = new UserMark();
				mymark.setMarkid(rs.getInt("markid")) ; //符文id
				mymark.setMarkcode(rs.getString("markcode"));
				mymark.setMarkname(rs.getString("markname")) ;  //符文名
				mymark.setMarkdesc(rs.getString("markdesc")) ;  //描述
				mymark.setMarktype(rs.getString("marktype")) ;  //符文类型 攻防血
				mymark.setAtk(rs.getDouble("atk")) ;  //增攻
				mymark.setHp(rs.getDouble("hp"));   //增血
				mymark.setDef(rs.getDouble("def")) ;  //增防
				mymark.setDeviceid(rs.getInt("deviceid")) ;  //插入装备id
				mymark.setMarkloc(rs.getInt("markloc")) ;   //符文位置
				p.getUmarklist().put(mymark.getMarkid(), mymark); //记录用户符文列表
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	//判断连续登录
	public int checklog(String oldlog,String newlog){
		/*SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date odate=dt.parse(oldlog);
		Date ndate = dt.parse(newlog);
		long between=(ndate.getTime()-odate.getTime())/1000;//除以1000是为了转换成秒
		long day1=between/(24*3600);
		long hour1=between%(24*3600)/3600;
		long minute1=between%3600/60;
		long second1=between%60/60;*/
		int flag=0;
		SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
		Date odate;
		try {
			
			odate = dt.parse(oldlog.substring(0,10));
			Date ndate = dt.parse(newlog.substring(0,10));
			long between=(ndate.getTime()-odate.getTime())/1000;//除以1000是为了转换成秒
			flag=(int)(between/(24*3600));
			return flag;
		} catch (ParseException e) {
			logger.info(e.getMessage());
			return flag;			
		}
			
	}
	//字符时间转时间戳 10位
	public int strtotimpstamp(String datestr){
		SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date odate;
		int flag=0;
		try {
			odate=dt.parse(datestr);
			flag= (int)(odate.getTime()/1000);
			return flag;
		} catch (ParseException e) {
			logger.info(e.getMessage());
			return flag;			
		}
	}
	
	
	//体力恢复计算,返回恢复的体力值
	public int CheckPower(int starttime,int pow,int perpow){
		long cursecond=0;
		int timevalue=0;
		if(starttime>0){//已开始计时
			cursecond=System.currentTimeMillis()/1000;
			timevalue=(int)Math.floor(((int)cursecond-starttime)/perpow);			
		}
		return timevalue;
	}
	/*//检查用户角色是否存在
	public int checkuserRole(User player,String un,String ur,String scode,String cname) throws SQLException{
		int flag=0;
		String sql ="SELECT rolename FROM userroletbl WHERE username=? AND rolename=? AND serverno=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, un);
			ps.setString(2, ur);
			ps.setString(3, scode);
			rs = ps.executeQuery();
			if(rs.next()){
				flag=1;				
			}else
			{//创建新角色
				CreateUser(player,un,ur,scode,cname);
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) {
			throw e;
		}
		finally{
			conn.close();
		}
		return flag;
	}*/
	//创建角色
	public void CreateUser(User player,String un,String ur,String scode,String ccode,String mcode,String pcode) throws SQLException {
		CallableStatement cs = null;
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				//pid varchar(32),wingold int,atype int,out currgold int
				cs = conn.prepareCall("{call CREATEUSER(?,?,?,?,?,?,?)}");
				cs.setString(1,un);
				cs.setString(2, ur);
				cs.setString(3,scode);
				cs.setString(4,ccode);
				cs.setString(6, mcode);
				cs.setString(7, pcode);
				cs.registerOutParameter(5, Types.INTEGER);
				cs.execute();
				int checkflag=cs.getInt(5);
				if(checkflag<=0){
					cs.close();
					throw new SQLException("role"+checkflag);
				}
				cs.close();
			}catch (SQLException e) {
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();				
			}
		}
	}
	
	//获取玩家拥有的技能书页
	public void getPlayerSkillPage(User player, String ur,String scode) throws SQLException {
		String sql ="SELECT * FROM userskillpage"+scode+" WHERE rolename=? AND SERVERNO=?"; //玩家拥有的技能书页
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, ur);
			ps.setString(2, scode);
			rs = ps.executeQuery();
			while(rs.next()){
				SkillPages skillp = new SkillPages();
				skillp.setSkillname(rs.getString("skillname")); //技能名称
				skillp.setSkillcode(rs.getString("skillcode"));
				SkillPages skp=GameManager.getInstance().getSkilldict().get(skillp.getSkillcode());
				skillp.setPagenum(skp.getPagenum()) ;      //书页数量
				skillp.setPage1(rs.getInt("page1")) ;        //书页1数量
				skillp.setPage2(rs.getInt("page2")) ;  //书页2数量
				skillp.setPage3(rs.getInt("page3")) ;	  //书页3数量
				skillp.setPage4(rs.getInt("page4")) ;      //书页4数量
				skillp.setPage5(rs.getInt("page5")) ;     //书页5数量  -1表示没有此书页
				skillp.setSkdesc(skp.getSkdesc()) ;  //技能描述
				skillp.setSktype(skp.getSktype()) ;  //技能类型 主动被动
				skillp.setSkgrade(rs.getInt("skillgrade")) ;    //技能等级
				skillp.setDiycustom(skp.getDiycustom()) ;  //修炼消耗
				skillp.setAtks(skp.getAtks()) ;    //攻击增量 小于1表示百分比
				skillp.setDefs(skp.getDefs()) ;
				skillp.setHps(skp.getHps()) ;
				skillp.setSvs(skp.getSvs()) ;       //增量系数
				skillp.setHurt(skp.getHurt());
				skillp.setSkillto(skp.getSkillto());
				skillp.setSkillrand(skp.getSkillrand());
				skillp.setUphurt(skp.getUphurt());
				skillp.setUphurtper(skp.getUphurtper());
				skillp.setHurtpert(skp.getHurtpert());
				skillp.setSkillcolor(skp.getSkillcolor());
				player.getSkillpagelist().put(skillp.getSkillcode(), skillp);		
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	/**
	 * 日期转字符串
	 * @return
	 */
	public String dateToString(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
	//卡牌换位
	public void cardChange(User p,String[] al) throws SQLException{
		Connection conn = pool.getConnection();
		String sql ="UPDATE usercard"+p.getSvrcode()+" SET locate=? WHERE rolename=? AND serverno=? AND cardid=?";
		PreparedStatement ps = null;
		if(conn != null){
			try {
				int ocardid;
				conn.setAutoCommit(false);
				for(int i=0;i<al.length;i++){
					ocardid=Integer.parseInt(al[i]);
					UserCard oricard=p.getInusecards().get(ocardid);
					oricard.setLocate(i);
					p.getCardlocid()[i]=ocardid;
					ps = conn.prepareStatement(sql);
					ps.setInt(1, oricard.getLocate());
					ps.setString(2, p.getUrole());
					ps.setString(3, p.getSvrcode());
					ps.setInt(4, ocardid);
					ps.executeUpdate();
					ps.close();
				}
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackCard(p);//回滚卡牌信息
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//卡牌更换
	public void AlterUserCard(int ocardid,int tcardid,User p,int lskillid) throws SQLException{
		Connection conn = pool.getConnection();
		UserCard ouc=null,tuc=null;
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				tuc=p.getCardlist().get(tcardid);//换上的卡牌
				Iterator<UserDevice> its = tuc.getDeviceMap().values().iterator();
				while(its.hasNext()){
					UserDevice ud=its.next();
					DeviceUpdate(p,ud, conn);
				}
				Iterator<UserSkill> it = tuc.getSkillMap().values().iterator();
				while(it.hasNext()){
					UserSkill us=it.next();
					SkillUpdate(p, tuc.getCardid(), us.getSkillid(), conn);
				}
				CardUpdate(p,tuc,conn);
				if(ocardid>0){ //从阵容中换下 原卡牌携带的装备技能转到目标卡牌上//换下的卡牌属性恢复为裸身值
					ouc=p.getCardlist().get(ocardid);
					CardUpdate(p,ouc,conn);
				}
				if(lskillid>0){//无法转到新卡上的主动技能
					SkillUpdate(p, 0, lskillid, conn);
				}
				//PlayerUpdate(p,conn); //更新用户补正信息
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackDevice(p);//回滚装备
				this.rollbackSkill(p);//回滚技能
				this.rollbackCard(p);//回滚卡牌信息
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//数值增益计算
	public int calvalue(int orival,double nowval){
		int val=0;
		if(nowval>0){ //判断是增值还是增百分比
			if(nowval<1){ //百分比
				val=(int)Math.floor(orival*(1+nowval/100));
			}else
				val=(int)Math.floor(nowval);
		}
		return val;
	}
	//装备更新数据库
	public void DeviceUpdate(User p,UserDevice ud,Connection conn) throws SQLException{
		String sql ="";
		PreparedStatement ps = null;
		if(conn != null){
			try{
				//更新数据库，卡牌血攻防，装备穿上
				sql="UPDATE userdevice"+p.getSvrcode()+" SET cardid=?,havemark=?,markstring=?,"+
				"grade=?,dhp=?,datk=?,ddef=?,price=? "+
				" WHERE deviceid=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,ud.getCardid());
				ps.setInt(2,ud.getHavemark());
				ps.setString(3,ud.getMarkstring());
				ps.setInt(4,ud.getGrade());
				ps.setInt(5,ud.getDhp());
				ps.setInt(6, ud.getDatk());
				ps.setInt(7, ud.getDdef());
				ps.setInt(8, ud.getPrice());
				ps.setInt(9,ud.getDeviceid());
				ps.executeUpdate();
				ps.close();		
			}catch(SQLException e){
				logger.info(e.getMessage());
				throw e;
			}					
		}
	}
	//卡牌更新数据库
	public void CardUpdate(User p,UserCard uc,Connection conn) throws SQLException{
		String sql ="";
		PreparedStatement ps = null;
		if(conn != null){
			try{
				//更新数据库，卡牌血攻防，装备穿上
				sql ="UPDATE usercard"+p.getSvrcode()+" SET starlevel=?,hp=?,atk=?,def=?,cardlevel=?,upexp=?,"
					+"curexp=?,curvalue=?,basehp=?,baseatk=?,basedef=?,orighp=?,origatk=?,origdef=?,"
					+"havedevice=?,setid=?,haveskill=?,locate=?,ifuse=?,livehp=?,liveatk=?,livedef=?,"
					+"tlivehp=?,tliveatk=?,tlivedef=?,curstyle=?,curpointval=? "+
						"  WHERE cardid=? ";
				//sql="UPDATE usercard SET starlevel=?,HP=?,ATK=?,DEF=?,havedevice=?,setid=?,haveskill=? WHERE rolename=? AND serverno=? AND cardid=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,uc.getStarlevel());
				ps.setInt(2,uc.getHp());
				ps.setInt(3,uc.getAtk());
				ps.setInt(4,uc.getDef());
				ps.setInt(5,uc.getCardlevel());
				ps.setInt(6,uc.getUpexp());
				ps.setInt(7,uc.getCurexp());
				ps.setInt(8,uc.getCurvalue());
				ps.setDouble(9,uc.getBasehp());
				ps.setDouble(10,uc.getBaseatk());
				ps.setDouble(11,uc.getBasedef());
				ps.setInt(12,uc.getOrighp());
				ps.setInt(13,uc.getOrigatk());
				ps.setInt(14,uc.getOrigdef());
				ps.setInt(15,uc.getHavedevice());
				ps.setString(16,uc.getSetid());
				ps.setInt(17,uc.getHaveskill());
				ps.setInt(18,uc.getLocate());
				ps.setInt(19,uc.getIfuse());
				ps.setInt(20,uc.getLivehp());
				ps.setInt(21,uc.getLiveatk());
				ps.setInt(22,uc.getLivedef());
				ps.setInt(23,uc.getTlivehp());
				ps.setInt(24,uc.getTliveatk());
				ps.setInt(25,uc.getTlivedef());
				ps.setInt(26, uc.getCurstyle());
				ps.setInt(27,uc.getCurpointval());
				ps.setInt(28,uc.getCardid());
				ps.executeUpdate();
				ps.close();
				if(uc.getAltercurstyle()>0){//形态发生改变
					sql="UPDATE userpicindex"+p.getSvrcode()+"  SET curstyle=? WHERE rolename=? AND serverno=? AND cardcode=?";
					ps=conn.prepareStatement(sql);
					ps.setInt(1, uc.getAltercurstyle());
					ps.setString(2, p.getUrole());
					ps.setString(3, p.getSvrcode());
					ps.setString(4, uc.getCardcode());
					ps.executeUpdate();
					ps.close();
					uc.setAltercurstyle(0);//形态变换状态重新记录
				}
			}catch(SQLException e){
				logger.info(e.getMessage());
				throw e;
			}
		}
	}
	//更新用户信息  gnum:变化金条数
	public void PlayerUpdate(User p,Connection conn,int gnum) throws SQLException{
		String sql ="";
		PreparedStatement ps = null;
		if(conn != null){
			try{
				sql="UPDATE userroletbl SET money=?,gold=gold+?,exp=?,god=?,ball=?,addvalue=?,maxpower=?,"+
				"level=?,members=?,perpoint=?,levelup=?,powerstart=?,power=?,getbook=?,missionsum=?,getskstart=?,playerpknum=?,"
				+"goldrenew=?,goldaddnum=?,buyfight=?,curstreetid=?,curdesid=?,curmission=?,curmessid=?,resetstreet=?,currank=?,friendpknum=?"
					+" WHERE username=? AND rolename=? AND serverno=? ";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,p.getMoney());
				ps.setInt(2, gnum);
				ps.setInt(3, p.getExp());
				ps.setInt(4, p.getGod());
				ps.setInt(5, p.getBall());
				ps.setInt(6,p.getAddvalue());
				ps.setInt(7, p.getMaxpower());
				ps.setInt(8, p.getLevel());
				ps.setInt(9, p.getMembers());
				ps.setInt(10,p.getPerpoint());
				ps.setInt(11,p.getLevelup());
				ps.setInt(12, p.getPowerstart());
				ps.setInt(13, p.getPower());
				ps.setInt(14, p.getGetbook());
				ps.setInt(15, p.getMissionsum());
				ps.setInt(16, p.getGetskstart());
				ps.setInt(17, p.getPknum());				
				ps.setInt(18, p.getGoldrenew());
				ps.setInt(19, p.getGoldaddnum());
				ps.setInt(20, p.getBuyfight());
				ps.setInt(21, p.getCurstreetid());
				ps.setInt(22, p.getCurdesid());
				ps.setInt(23, p.getCurmission());
				ps.setInt(24, p.getCurmessid());
				ps.setInt(25, p.getResetstreet());
				ps.setInt(26, p.getRankseq());
				ps.setInt(27, p.getFriendpknum());
				ps.setString(28,p.getUname());
				ps.setString(29, p.getUrole());
				ps.setString(30, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
			}catch(SQLException e){
				logger.info(e.getMessage());
				throw e;
			}			
		}		
	}
	//穿上装备
	public void LoadDevice(User p,UserCard uc,int did,UserCard otheruc,int odid) throws SQLException{
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				if(odid>0){
					DeviceUpdate(p,p.getUdevicelist().get(odid),conn);
				}
				//更新数据库，卡牌血攻防，装备穿上
				DeviceUpdate(p,p.getUdevicelist().get(did),conn);
				CardUpdate(p,uc,conn);
				if(otheruc!=null){
					CardUpdate(p,otheruc,conn);
				}				
				conn.commit();
				conn.setAutoCommit(true);
				
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackDevice(p);
				this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();					
			}
		}
	}

	//镶嵌符文
	public void LoadMark(User p,UserDevice ud,ArrayList<UserMark> loadlist,ArrayList<Integer> destl) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		String destorymarkstr,loadmarkstr;
		String sql="";
		//更新符文记录的装备id，更新对应装备是否有符文
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				destorymarkstr="";
				for(int i=0;i<destl.size();i++){
					//sql="DELETE FROM usermark  WHERE  markid=?";
					sql="UPDATE usermark"+p.getSvrcode()+" SET deviceid=-1,markloc=-1  WHERE  markid=?";
					ps = conn.prepareStatement(sql);
					ps.setInt(1,destl.get(i));
					ps.executeUpdate();
					ps.close();
					destorymarkstr=destorymarkstr+destl.get(i)+",";
					/*//消费
					sql="UPDATE userroletbl SET gold=gold-30 WHERE username=? AND rolename=? AND serverno=?";
					ps.setString(1, p.getUname());
					ps.setString(2, p.getUrole());
					ps.setString(4, p.getSvrcode());
					ps.executeUpdate();
					ps.close();	*/
				}
				loadmarkstr="";
				for(int i=0;i<loadlist.size();i++){
					sql="UPDATE usermark"+p.getSvrcode()+" SET deviceid=?,markloc=? WHERE markid=?";
					ps = conn.prepareStatement(sql);
					ps.setInt(1,ud.getDeviceid());
					ps.setInt(2,loadlist.get(i).getMarkloc());
					ps.setInt(3,loadlist.get(i).getMarkid());
					ps.executeUpdate();
					ps.close();	
					loadmarkstr=loadmarkstr+loadlist.get(i).getMarkid()+"+"+loadlist.get(i).getMarkloc()+",";
				}
				//如果此装备在卡牌上，计算增量
				if(ud.getCardid()>0){
					int cid=ud.getCardid();
					UserCard uc=p.getCardlist().get(cid);
					//logger.info("卡牌现血攻防"+uc.getHp()+","+uc.getAtk()+","+uc.getDef());
					CardUpdate(p,uc,conn);
				}
				DeviceUpdate(p,ud,conn);
				//记录用户活动
				sql="INSERT INTO useract"+p.getSvrcode()+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				//rolename,serverno,acttime,acttype,recordid,curexp,curlevel,curgold,curmoney,downgold,downmoney,downexp,downlevel,downprops,downball,	memo
				ps = conn.prepareStatement(sql);
				ps.setString(1,p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setInt(3, (int)(System.currentTimeMillis()/1000));
				ps.setInt(4, 25);//符文镶嵌
				ps.setString(5, ud.getDeviceid()+"");//镶嵌符文的装备id
				ps.setInt(6, p.getExp());
				ps.setInt(7, p.getLevel());
				ps.setInt(8, p.getGold());
				ps.setInt(9, p.getMoney());
				ps.setInt(10, 0);
				ps.setInt(11, 0);
				ps.setInt(12, 0);
				ps.setInt(13, 0);
				ps.setString(14, loadmarkstr);//镶嵌的符文id ，号分割
				ps.setInt(15, 0);
				ps.setString(16, destorymarkstr);//卸下的“符文id串+位置”，分割
				ps.executeUpdate();
				ps.close();	
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackMark(p);
				this.rollbackDevice(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();					
			}
		}
	}
	//更新技能数据库
	public void SkillUpdate(User p,int cardid,int sid,Connection conn) throws SQLException{
		String sql ="";
		PreparedStatement ps = null;
		if(conn != null){
			try{
				UserSkill us=p.getUskilllist().get(sid);
				//更新数据库，卡牌血攻防，装备穿上
				sql="UPDATE userskill"+p.getSvrcode()+" SET cardid=?,skillloc=? WHERE skillid=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,cardid);
				ps.setInt(2,us.getSkillloc());
				ps.setInt(3,sid);
				ps.executeUpdate();
				ps.close();		
			}catch(SQLException e){
				logger.info(e.getMessage());
				throw e;
			}						
		}
	}
	//安装技能
	public void LoadSkill(User p,UserCard uc,int sid,UserCard otheruc,int osid) throws SQLException{
		//重新计算卡牌的血攻防 
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				//更新数据库，卡牌血攻防，安装技能
				conn.setAutoCommit(false);
				//logger.info("卸下技能id"+osid);
				if(osid>0){
					SkillUpdate(p,0,osid,conn);
				}
				CardUpdate(p,uc,conn);
				SkillUpdate(p,uc.getCardid(),sid,conn);
				if(otheruc!=null){
					CardUpdate(p,otheruc,conn);
				}
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackSkill(p);
				this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();					
			}
		}
	}
	
	//获取字典信息
	public void getBaseDict(String svrcode) throws SQLException{
		String sql ="SELECT * FROM markstrdict"; //符文组合字典
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			//符文组合字典
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				MarkStringCA msa = new MarkStringCA();
				msa.setMkstring(rs.getString("markstring"));
				msa.setMksaddhp(rs.getDouble("HP"));
				msa.setMksaddatk(rs.getDouble("ATK"));
				msa.setMksadddef(rs.getDouble("DEF"));
				msa.setMkdesc(rs.getString("markdesc"));
				msa.setMktype(rs.getString("marktype"));
				msa.setMkflag(rs.getInt("markflag"));
				GameManager.getInstance().getMarksetdict().put(msa.getMkstring(), msa);
			}
			rs.close();
			ps.close();
			//货币字典
			sql="SELECT * FROM coindict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				GameManager.getInstance().setGoldname(rs.getString("goldname"));
				GameManager.getInstance().setMname(rs.getString("mname"));
				GameManager.getInstance().setBallname(rs.getString("ballname"));
			}
			rs.close();
			ps.close();
			//符文产生字典
			sql="SELECT * FROM markprocdict ORDER BY newmark";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			String markstr="";
			while(rs.next()){
				Map<String,Integer> tempmap = new HashMap<String,Integer>();
				if(!markstr.equals(rs.getString("newmark"))){					
					tempmap.put(rs.getString("oldmark"),rs.getInt("marknum"));	
					markstr=rs.getString("newmark");
					GameManager.getInstance().getMergemarks().put(markstr, tempmap);
				}else{
					GameManager.getInstance().getMergemarks().get(markstr).put(rs.getString("oldmark"),rs.getInt("marknum"));					
				}
			}
			rs.close();
			ps.close();
			//vip描述字典
			sql="SELECT * FROM vipdescdict ORDER BY viplevel,vipindex";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			int vipl=0;
			while(rs.next()){
				int vipval=rs.getInt("viplevel");
				if(vipl!=vipval){
					ArrayList<String> al = new ArrayList<String>();
					al.add(rs.getString("vipdesc"));
					GameManager.getInstance().getVipdescdict().add(al);
					vipl=vipval;
				}else{
					GameManager.getInstance().getVipdescdict().get(vipval-1).add(rs.getString("vipdesc"));
				}
			}
			rs.close();
			ps.close();
			//各服活动字典
			sql ="SELECT * FROM actdict WHERE serverno=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, svrcode);
			rs = ps.executeQuery();
			while(rs.next()){
				ActDict sad= new ActDict(); 
				sad.setActtype(rs.getInt("acttype"));
				sad.setActvalue(rs.getDouble("actvalue"));
				sad.setBtime(rs.getInt("btime"));
				sad.setEtime(rs.getInt("etime"));
				GameManager.getInstance().getSvractdict().put(sad.getActtype(), sad);
			}
			rs.close();
			ps.close();
			//碎片字典
			sql ="SELECT * FROM chipdict ORDER BY randval DESC";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				ChipDict cd= new ChipDict(); 
				cd.setChipcode(rs.getString("chipcode"));
				cd.setChipname(rs.getString("chipname"));
				cd.setChiptype(rs.getInt("chiptype"));
				cd.setRandval(rs.getInt("randval"));
				cd.setNeedsum(rs.getInt("needsum"));
				cd.setChipcolor(rs.getInt("chipcolor"));
				GameManager.getInstance().getChipdictlist().add(cd);
			}
			rs.close();
			ps.close();
			//获取登录公告信息
			this.getloginmess(svrcode);
			//升级奖励字典
			sql="SELECT * FROM upleveldict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				UpLevelDict uld = new UpLevelDict();
				uld.setCurlevle(rs.getInt("curlevle"));
				uld.setDownmoney(rs.getInt("downmoney"));
				uld.setDowngold(rs.getInt("downgold"));
				uld.setDowncode(rs.getString("downcode"));
				uld.setGoodstype(rs.getInt("goodstype"));
				uld.setDownnum(rs.getInt("downnum"));
				GameManager.getInstance().getUplvldict().put(uld.getCurlevle(), uld);
			}
			rs.close();
			ps.close();
			//卡牌收集字典
			sql="SELECT * FROM allgetdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				AllgetDict ad=new AllgetDict();
				ad.setServerno(rs.getString("serverno"));
				ad.setCardtype(rs.getString("cardtype"));
				ad.setAddhp(rs.getDouble("addhp"));
				ad.setAddatk(rs.getDouble("addatk"));
				ad.setAdddef(rs.getDouble("adddef"));
				ad.setAlldesc(rs.getString("alldesc"));
				ad.setTypedesc(rs.getString("typedesc"));
				GameManager.getInstance().getCardgetall().put(ad.getCardtype(), ad);
			}
			rs.close();
			ps.close();
			//卡牌装备缘分表
			sql="SELECT * FROM cardluckdev";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				lotluck ll = new lotluck();
				ll.setSomename(rs.getString("devicecode"));
				ll.setAddhp(rs.getDouble("addhp"));
				ll.setAddatk(rs.getDouble("addatk"));
				ll.setAdddef(rs.getDouble("adddef"));
				ll.setLuckname(rs.getString("luckname"));
				ll.setLuckdesc(rs.getString("luckdesc"));
				if(GameManager.getInstance().getCarddevicedict().containsKey(rs.getString("cardcode"))){
					GameManager.getInstance().getCarddevicedict().get(rs.getString("cardcode")).put(rs.getString("devicecode"),ll);
				}else{
					Map<String,lotluck> map = new HashMap<String,lotluck>();
					map.put(rs.getString("devicecode"),ll);
					GameManager.getInstance().getCarddevicedict().put(rs.getString("cardcode"),map);
				}
			}
			rs.close();
			ps.close();
			//卡牌技能缘分表
			sql="SELECT * FROM cardluckskill";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				lotluck ll = new lotluck();
				ll.setSomename(rs.getString("skillcode"));
				ll.setAddhp(rs.getDouble("addhp"));
				ll.setAddatk(rs.getDouble("addatk"));
				ll.setAdddef(rs.getDouble("adddef"));
				ll.setLuckname(rs.getString("luckname"));
				ll.setLuckdesc(rs.getString("luckdesc"));
				if(GameManager.getInstance().getCardskilldict().containsKey(rs.getString("cardcode"))){
					GameManager.getInstance().getCardskilldict().get(rs.getString("cardcode")).put(rs.getString("skillcode"),ll);
				}else{
					Map<String,lotluck> map = new HashMap<String,lotluck>();
					map.put(rs.getString("skillcode"),ll);
					GameManager.getInstance().getCardskilldict().put(rs.getString("cardcode"),map);
				}
			}
			rs.close();
			ps.close();
			//卡牌缘分表
			sql="SELECT * FROM cardluck";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				lotluck ll = new lotluck();
				ll.setSomename(rs.getString("cardcodestr"));
				ll.setAddhp(rs.getDouble("addhp"));
				ll.setAddatk(rs.getDouble("addatk"));
				ll.setAdddef(rs.getDouble("adddef"));
				ll.setLuckname(rs.getString("luckname"));
				ll.setLuckdesc(rs.getString("luckdesc"));
				if(GameManager.getInstance().getCardluckdict().containsKey(rs.getString("cardcode"))){
					GameManager.getInstance().getCardluckdict().get(rs.getString("cardcode")).put(rs.getString("cardcodestr"),ll);
				}else{
					Map<String,lotluck> map = new HashMap<String,lotluck>();
					map.put(rs.getString("cardcodestr"),ll);
					GameManager.getInstance().getCardluckdict().put(rs.getString("cardcode"),map);
				}
			}
			rs.close();
			ps.close();
			//积分兑换奖励字典
			sql="SELECT * FROM pointsdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				Cpointdict cpd = new Cpointdict();
				cpd.setExchid(rs.getInt("exchid"));
				cpd.setExchcode(rs.getString("exchcode"));
				cpd.setExchdesc(rs.getString("exchdesc"));
				cpd.setGoodstype(rs.getInt("goodstype"));
				cpd.setCostpoint(rs.getInt("costpoint"));
				cpd.setTakecond(rs.getInt("takecond"));
				GameManager.getInstance().getCpdict().put(cpd.getExchid(), cpd);
			}
			rs.close();
			ps.close();
			//pk经验字典
			sql="SELECT * FROM expdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				Pkexpmoney pkem = new Pkexpmoney();
				pkem.setUlevel(rs.getInt("ulevel"));
				pkem.setExp(rs.getInt("exp"));
				pkem.setMoney(rs.getInt("money"));
				GameManager.getInstance().getPkexpmon().put(pkem.getUlevel(), pkem);
			}
			rs.close();
			ps.close();
			//摇钱树字典含街霸 拆店重置vip次数
			sql="SELECT * FROM shakenumdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("AA", rs.getInt("shakegold"));//摇钱花费
				map.put("AB", rs.getInt("shakemax"));//摇钱最大次数
				map.put("AC", rs.getInt("resetstreenum"));//vip对应重置街霸次数
				map.put("AD", rs.getInt("friendnum"));//vip对应好友数
				map.put("AE", rs.getInt("buyfight"));//vip对应购买争斗次数上限
				map.put("AF", rs.getInt("streetcost"));//重置街霸费用对应重置次数
				map.put("AG", rs.getInt("buypower"));//购买体力上限
				map.put("AH", rs.getInt("buygetskill"));//购买夺计上限
				map.put("AI", rs.getInt("vipgold"));//vip条件
				map.put("AJ", rs.getInt("bosscd"));//vip bosscd时间
				map.put("AK", rs.getInt("taskcd"));//vip 连闯任务时间
				map.put("AL", rs.getInt("blessnum"));//VIP免费祈愿次数
				GameManager.getInstance().getShakedict().put(rs.getInt("shakenum"), map);
			}
			rs.close();
			ps.close();
			//道具字典
			sql="SELECT * FROM propsdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				PropsDict pd = new PropsDict();
				pd.setPropscode(rs.getString("propscode"));
				pd.setPropsname(rs.getString("propsname"));
				pd.setPropsdesc(rs.getString("propsdesc"));
				pd.setPropsgrade(rs.getInt("propsgrade"));
				pd.setPropstype(rs.getInt("propstype"));
				pd.setPropsuse(rs.getInt("propsuse"));
				pd.setPropsadd(rs.getInt("propsadd"));
				pd.setUsecond(rs.getString("usecond"));
				pd.setPropspic(rs.getString("propspic"));
				if(pd.getPropstype()==1){//包类型商品
					String psql="SELECT * FROM packagetbl WHERE goodscode=?";
					PreparedStatement pps = null;
					ResultSet prs = null;
					pps=conn.prepareStatement(psql);
					pps.setString(1, pd.getPropscode());
					prs=pps.executeQuery();
					while(prs.next()){
						packageGoods psc=new packageGoods();
						psc.setGoodscode(prs.getString("innercode"));
						psc.setGoodsname(prs.getString("goodsname"));
						psc.setGoodsnum(prs.getInt("goodsnum"));
						psc.setInnertype(prs.getInt("innertype"));
						psc.setGoodspic(prs.getString("goodspic"));
						pd.getPackgoods().add(psc);
					}
					pps.close();
					prs.close();
				}
				GameManager.getInstance().getPropsdict().put(pd.getPropscode(), pd);
			}
			rs.close();
			ps.close();
			//商城字典
			sql="SELECT * FROM shoptbl WHERE serverno="+svrcode;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				ShopClass sc = new ShopClass();
				sc.setGoodscode(rs.getString("goodscode"));
				sc.setGoodsname(rs.getString("goodsname"));
				sc.setGoodsdesc(rs.getString("goodsdesc"));
				sc.setGoodstype(rs.getInt("goodstype"));
				sc.setGoodstyle(rs.getInt("goodstyle"));
				sc.setMoneyp(rs.getInt("moneyp"));
				sc.setGoldp(rs.getInt("goldp"));
				sc.setBuycond(rs.getInt("buycond"));
				sc.setBuysum(rs.getInt("buysum"));
				sc.setDiscount(rs.getInt("discount"));
				sc.setBuystart(rs.getInt("buystart"));
				sc.setBuyend(rs.getInt("buyend"));
				sc.setDispic(rs.getString("dispic"));
				sc.setOrderinx(rs.getInt("orderinx"));
				if(sc.getGoodstype()==1){//包类型商品
					String psql="SELECT * FROM packagetbl WHERE goodscode=?";
					PreparedStatement pps = null;
					ResultSet prs = null;
					pps=conn.prepareStatement(psql);
					pps.setString(1, sc.getGoodscode());
					prs=pps.executeQuery();
					while(prs.next()){
						packageGoods psc=new packageGoods();
						psc.setGoodscode(prs.getString("innercode"));
						psc.setGoodsname(prs.getString("goodsname"));
						psc.setGoodsnum(prs.getInt("goodsnum"));
						psc.setInnertype(prs.getInt("innertype"));
						psc.setGoodspic(prs.getString("goodspic"));
						sc.getPackgoods().add(psc);
					}
					pps.close();
					prs.close();
				}
				GameManager.getInstance().getShopdict().put(sc.getGoodscode(),sc);
			}
			rs.close();
			ps.close();
			//符文字典
			sql="SELECT * FROM markdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				MarkDict md = new MarkDict();
				md.setMarkcode(rs.getString("markcode"));
				md.setMarkdesc(rs.getString("markdesc"));
				md.setMarkname(rs.getString("markname"));
				md.setMarktype(rs.getString("marktype"));
				md.setHp(rs.getDouble("hp"));
				md.setAtk(rs.getDouble("atk"));
				md.setDef(rs.getDouble("def"));
				md.setDownrand(rs.getInt("downrand"));
				GameManager.getInstance().getMarkdict().put(md.getMarkcode(), md);
			}
			rs.close();
			ps.close();
			//登录奖励字典
			sql="SELECT * FROM logindict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				LoginDict ld = new LoginDict();
				ld.setContinuelog(rs.getInt("continuelog"));
				ld.setMoney(rs.getInt("money"));
				ld.setGold(rs.getInt("gold"));
				ld.setInnercoin(rs.getInt("innercoin"));
				ld.setGod(rs.getInt("god"));
				ld.setBall(rs.getInt("ball"));
				ld.setDowncode(rs.getString("downcode"));
				ld.setDownnum(rs.getInt("downnum"));
				GameManager.getInstance().getLogdict().put(ld.getContinuelog(),ld);
				}
			rs.close();
			ps.close();
			//场景字典
			sql="SELECT * FROM scenedict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				SceneDict sd = new SceneDict();
				sd.setSceneno(rs.getInt("sceneno"));
				sd.setScecode(rs.getString("scecode"));
				sd.setScenename(rs.getString("scenename"));
				sd.setMissions(rs.getInt("missions"));
				sd.setAwardcode(rs.getString("awardcode"));
				sd.setAwardtype(rs.getInt("awardtype"));
				sd.setAwardnum(rs.getInt("awardnum"));
				sd.setAwardname(rs.getString("awardname"));
				GameManager.getInstance().getScenedict().put(sd.getSceneno(), sd);
				}
			rs.close();
			ps.close();
			//卡牌字典
			sql="SELECT * FROM carddict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				CardDict cd = new CardDict();
				cd.setCardcode(rs.getString("cardcode"));
				cd.setCardname(rs.getString("cardname"));
				cd.setCarddesc(rs.getString("carddesc"));
				cd.setCardprop(rs.getString("cardprop"));
				cd.setStarlevel(rs.getInt("starlevel"));
				cd.setCardtype(rs.getString("cardtype"));
				cd.setBasehp(rs.getDouble("basehp"));
				cd.setBaseatk(rs.getDouble("baseatk"));
				cd.setBasedef(rs.getDouble("basedef"));
				cd.setAlterhp(rs.getDouble("alterhp"));
				cd.setAlteratk(rs.getDouble("alteratk"));
				cd.setAlterdef(rs.getDouble("alterdef"));
				cd.setLifeskill(rs.getString("lifeskill"));
				GameManager.getInstance().getCarddict().put(cd.getCardcode(),cd);
				}
			rs.close();
			ps.close();
			//技能字典
			sql="SELECT * FROM skilldict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				SkillPages skillp = new SkillPages();
				skillp.setSkillname(rs.getString("skillname")); //技能名称
				skillp.setSkillcode(rs.getString("skillcode"));
				skillp.setPagenum(rs.getInt("pagenum")) ;      //书页数量
				skillp.setPage1(rs.getInt("page1")) ;        //书页1数量
				skillp.setPage2(rs.getInt("page2")) ;  //书页2数量
				skillp.setPage3(rs.getInt("page3")) ;	  //书页3数量
				skillp.setPage4(rs.getInt("page4")) ;      //书页4数量
				skillp.setPage5(rs.getInt("page5")) ;     //书页5数量  -1表示没有此书页
				skillp.setSkdesc(rs.getString("skilldesc")) ;  //技能描述
				skillp.setSktype(rs.getString("skilltype")) ;  //技能类型 主动被动
				skillp.setSkgrade(rs.getInt("skillgrade")) ;    //技能等级 1-3
				skillp.setDiycustom(rs.getInt("diycustom")) ;  //修炼消耗
				skillp.setAtks(rs.getDouble("atks")) ;    //攻击增量 小于1表示百分比
				skillp.setDefs(rs.getDouble("defs")) ;
				skillp.setHps(rs.getDouble("hps")) ;
				skillp.setSvs(rs.getInt("svs")) ;       //增量系数
				skillp.setHurt(rs.getDouble("hurt"));
				skillp.setSkillto(rs.getInt("skillto"));//0群攻 1单体
				skillp.setSkillrand(rs.getInt("skillrand"));//发动几率
				skillp.setHurtpert(rs.getDouble("hurtper"));//伤害百分比
				skillp.setUphurtper(rs.getDouble("uphurtper"));//升级伤害百分比
				skillp.setUphurt(rs.getDouble("uphurt"));//升级伤害附加百分比
				skillp.setSkillcolor(rs.getInt("skillcolor"));//技能颜色
				GameManager.getInstance().getSkilldict().put(skillp.getSkillcode(),skillp);
				}
			rs.close();
			ps.close();
			//装备字典
			sql="SELECT * FROM devicedict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				DeviceDict dd = new DeviceDict();
				dd.setDevicecode(rs.getString("devicecode"));
				dd.setDevicename(rs.getString("devicename"));
				dd.setDevicedesc(rs.getString("devicedesc"));
				dd.setDevicetype(rs.getString("devicetype"));
				dd.setQuality(rs.getString("quality"));
				dd.setBaseprice(rs.getInt("price"));
				dd.setDhp(rs.getInt("hpval"));
				dd.setDatk(rs.getInt("atkval"));
				dd.setDdef(rs.getInt("defval"));
				dd.setDevcolor(rs.getInt("devcolor"));
				GameManager.getInstance().getDevsetdict().put(dd.getDevicecode(),dd);
				}
			rs.close();
			ps.close();
			//转生字典
			sql="SELECT * FROM lifeupdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				CLifeUpDict lfud = new CLifeUpDict();
				lfud.setStar(rs.getInt("star"));
				lfud.setMoney(rs.getInt("money"));
				lfud.setGold(rs.getInt("gold"));
				lfud.setInnercoin(rs.getInt("innercoin"));
				lfud.setGod(rs.getInt("god"));
				lfud.setBall(rs.getInt("ball"));
				lfud.setExtexp(rs.getInt("extexp"));
				lfud.setExtval(rs.getInt("extval"));
				lfud.setGrade(rs.getInt("grade"));
				lfud.setCardval(rs.getInt("cardval"));
				lfud.setVipgrade(rs.getInt("vipgrade"));
				lfud.setPermoney(rs.getInt("permoney"));
				lfud.setPergold(rs.getInt("pergold"));
				lfud.setPerinnercoin(rs.getInt("perinnercoin"));
				lfud.setPergod(rs.getInt("pergod"));
				lfud.setPerball(rs.getInt("perball"));
				lfud.setPextexp(rs.getInt("pextexp"));
				lfud.setPextval(rs.getInt("pextval"));
				GameManager.getInstance().getLifeupdict().put(lfud.getStar(), lfud);
			}
			rs.close();
			ps.close();
			//培养字典
			sql="SELECT livecode,livetype,hpmin,hpmax,atkmin,atkmax,defmax,defmin,cust,gold,vipgrade FROM liveupdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				LiveUpDictCla lud = new LiveUpDictCla();
				lud.setLivecode(rs.getString("livecode"));
				lud.setLivetype(rs.getString("livetype"));
				lud.setHpmin(rs.getInt("hpmin"));
				lud.setHpmax(rs.getInt("hpmax"));
				lud.setAtkmax(rs.getInt("atkmax"));
				lud.setAtkmin(rs.getInt("atkmin"));
				lud.setDefmax(rs.getInt("defmax"));
				lud.setDefmin(rs.getInt("defmin"));
				lud.setCust(rs.getInt("cust"));
				lud.setGold(rs.getInt("gold"));
				lud.setVipgrade(rs.getInt("vipgrade"));
				GameManager.getInstance().getLiveupdict().put(lud.getLivecode(),lud);
			}
			rs.close();
			ps.close();
			//套装字典
			sql="SELECT * FROM devsetdict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				DeviceSetDict dsd = new DeviceSetDict();
				dsd.setDevicestr(rs.getString("setid"));
				dsd.setDdhp(rs.getDouble("HP"));
				dsd.setDdatk(rs.getDouble("ATK"));
				dsd.setDddef(rs.getDouble("DEF"));
				GameManager.getInstance().getDevicesetdict().put(dsd.getDevicestr(), dsd);
			}
			rs.close();
			ps.close();
			//魅力系数字典
			sql="SELECT * FROM valuedict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				GameManager.getInstance().getValuedict().put(rs.getInt("mvalue"), rs.getDouble("realvalue"));
			}
			rs.close();
			ps.close();
			//账号升级字典
			sql="SELECT * FROM upgradedict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				UserUpgradeVal uuv=new UserUpgradeVal();
				uuv.setGrade(rs.getInt("grade"));
				uuv.setUpsum(rs.getInt("upsum"));
				uuv.setMembers(rs.getInt("members"));
				uuv.setMaxpower(rs.getInt("maxpower"));
				uuv.setPerpoint(rs.getInt("perpoint"));	
				uuv.setCardpercent(rs.getInt("cardpercent"));
				uuv.setUpcount(rs.getInt("upcount"));
				GameManager.getInstance().getUseruplevel().put(rs.getInt("grade"), uuv);
			}
			rs.close();
			ps.close();
			//装备精炼字典
			String dtp[]={"攻","防","饰"};
			String dqu[]={"精良","稀有","传说"};
			for(int i=0;i<dtp.length;i++){
				ConcurrentHashMap<String,ConcurrentHashMap<Integer,DeviceUpgrade>> ttmap = new ConcurrentHashMap<String,ConcurrentHashMap<Integer,DeviceUpgrade>>();
				for(int j=0;j<dqu.length;j++){
					sql="SELECT * FROM deviceupdict WHERE devicetype=? AND devicequality=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1,dtp[i]);
					ps.setString(2,dqu[j]);
					rs = ps.executeQuery();
					ConcurrentHashMap<Integer,DeviceUpgrade> tmap=new ConcurrentHashMap<Integer,DeviceUpgrade>();
					while(rs.next()){
						//HashMap<String,Map<String,Map<Integer,DeviceUpgrade>>>
						DeviceUpgrade dup=new DeviceUpgrade();
						dup.setDevicetype(rs.getString("devicetype"));
						dup.setDevicelevel(rs.getInt("devicelevel"));
						dup.setDevicequality(rs.getString("devicequality"));
						dup.setAddhp(rs.getInt("addhp"));
						dup.setAddatk(rs.getInt("addatk"));
						dup.setAdddef(rs.getInt("adddef"));
						dup.setMoneyvalue(rs.getInt("moneyvalue"));
						dup.setSuccval(rs.getInt("succval"));
						dup.setUpgold(rs.getInt("upgold"));
						dup.setUpsucval(rs.getInt("upsucval"));
						dup.setAddprice(rs.getInt("addprice"));
						dup.setCoinval(rs.getInt("coinval"));
						dup.setTearval(rs.getInt("tearval"));
						tmap.put(dup.getDevicelevel(), dup);
					}
					ps.close();
					rs.close();
					ttmap.put(dqu[j], tmap);
				}
				GameManager.getInstance().getDeviceaddval().put(dtp[i], ttmap);
			}
			//融魂字典
			sql="SELECT * FROM mergedict";
			ps = conn.prepareStatement(sql);				
			rs = ps.executeQuery();
			ConcurrentHashMap<Integer,MergeCardDict> mcmap= new ConcurrentHashMap<Integer,MergeCardDict>();
			while(rs.next()){					
				MergeCardDict mcd = new MergeCardDict();
				mcd.setMergestar(rs.getInt("mergestar"));
				mcd.setBreakstar(rs.getInt("breakstar"));
				mcd.setBreakcharm(rs.getInt("breakcharm"));
				mcd.setExtexp(rs.getInt("extexp"));
				mcd.setExtval(rs.getInt("extval"));
				mcd.setVip1charm(rs.getInt("vip1charm"));
				mcd.setVip2charm(rs.getInt("vip2charm"));
				mcd.setVip3charm(rs.getInt("vip3charm"));
				mcd.setVip4charm(rs.getInt("vip4charm"));
				mcd.setVip5charm(rs.getInt("vip5charm"));
				mcd.setVip6charm(rs.getInt("vip6charm"));
				mcd.setVip7charm(rs.getInt("vip7charm"));
				mcd.setVip8charm(rs.getInt("vip8charm"));
				GameManager.getInstance().getMergecarddict().put(mcd.getBreakstar(),mcd);
			}
			rs.close();
			ps.close();
			//任务字典
			sql="SELECT * FROM missiondict";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){				
				CMissionDict cmnd = new CMissionDict();
				cmnd.setMissionid(rs.getInt("missionid"));
				cmnd.setSceneno(rs.getInt("sceneno"));
				SceneDict sd= GameManager.getInstance().getScenedict().get(cmnd.getSceneno());
				cmnd.setScenecode(sd.getScecode());
				cmnd.setMissionseq(rs.getInt("missionseq"));
				cmnd.setScenename(sd.getScenename());
				cmnd.setMissions(sd.getMissions());
				cmnd.setMissionmp(rs.getInt("missionmp"));
				cmnd.setCopymp(rs.getInt("copymp"));
				cmnd.setSkpage(rs.getInt("skpage"));
				cmnd.setDevone(rs.getInt("devone"));
				cmnd.setDevtwo(rs.getInt("devtwo"));
				cmnd.setDevthr(rs.getInt("devthr"));
				cmnd.setStar1(rs.getInt("star1"));
				cmnd.setStar2(rs.getInt("star2"));
				cmnd.setStar3(rs.getInt("star3"));
				cmnd.setStar4(rs.getInt("star4"));//
				cmnd.setStar5(rs.getInt("star5"));//
				cmnd.setMarks(rs.getInt("marks"));//
				cmnd.setGrade(rs.getInt("grade"));//
				cmnd.setMdesc(rs.getString("mdesc"));//
				cmnd.setGiveexp(rs.getInt("giveexp"));
				cmnd.setGivemoeny(rs.getInt("givemoney"));
				cmnd.setLosepercent(rs.getInt("losepercent"));
				cmnd.setCurnum(rs.getInt("curnum"));
				cmnd.setNextmission(rs.getInt("nextmission"));
				cmnd.setGoods1(rs.getInt("goods1"));
				cmnd.setGoods1code(rs.getString("goods1code"));
				cmnd.setGoods2(rs.getInt("goods2"));
				cmnd.setGoods2code(rs.getString("goods2code"));
				cmnd.setProp1(rs.getInt("prop1"));
				cmnd.setProp2(rs.getInt("prop2"));
				cmnd.setProp3(rs.getInt("prop3"));
				cmnd.setMissname(rs.getString("missname"));
				GameManager.getInstance().getCmissiondict().put(cmnd.getMissionid(),cmnd);
				GameManager.getInstance().getMissionscene().put(cmnd.getMissionid(),cmnd.getSceneno());
				//任务NPC字典
				String sql1="SELECT * FROM missionnpc t1,npcdict t2 where t1.npcid=t2.npcid and missionid=? ORDER BY t1.npcloc";
				PreparedStatement ps1 = null;
				ResultSet rs1 = null;
				ps1 = conn.prepareStatement(sql1);
				ps1.setInt(1, cmnd.getMissionid());
				rs1 = ps1.executeQuery();
				ArrayList<CMissionNpcDict> ac= new ArrayList<CMissionNpcDict>(); 
				while(rs1.next()){
					CMissionNpcDict cnd = new CMissionNpcDict();
					cnd.setMissionid(rs1.getInt("missionid"));
					cnd.setNpcid(rs1.getInt("npcid"));
					cnd.setNpcname(rs1.getString("npcname"));
					cnd.setNpcprop(rs1.getString("npcprop"));
					cnd.setNpcgrade(rs1.getInt("npcgrade"));
					cnd.setNpctype(rs1.getString("npctype"));
					cnd.setNpccode(rs1.getString("npccode"));
					cnd.setHP(rs1.getInt("hp"));
					cnd.setATK(rs1.getInt("atk"));
					cnd.setDEF(rs1.getInt("def"));
					cnd.setHpalter(rs1.getInt("hpalter"));
					cnd.setAtkalter(rs1.getInt("atkalter"));
					cnd.setDefalter(rs1.getInt("defalter"));
					cnd.setSkillcode(rs1.getString("skillcode"));
					cnd.setSkillgrade(rs1.getInt("skillgrade"));
					cnd.setNpcloc(rs1.getInt("npcloc"));//位置
					ac.add(cnd);
				}
				GameManager.getInstance().getCmnpccdict().put(cmnd.getMissionid(),ac);
				rs1.close();
				ps1.close();
			}
			rs.close();
			ps.close();
			//掉率字典downdict
			sql="SELECT * FROM downdict ORDER BY downid,downrand DESC";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList<downDict> dal;
			int did=-1;
			while(rs.next()){
				downDict dd = new downDict();
				dd.setDownid(rs.getInt("downid"));//0:关公 1宝箱
				dd.setDowncode(rs.getString("downcode"));//掉率物品码 @为不指定具体物品
				dd.setGoodstype(rs.getInt("goodstype"));//0装备1武器2防具3饰品4技能5技能书页6道具7符文8卡牌
				dd.setGoodslevel(rs.getInt("goodslevel"));//1精良2稀有3传说  或技能等级 道具等级
				dd.setDownrand(rs.getInt("downrand"));//掉落概率
				dd.setDownnum(rs.getInt("downnum"));
				if(did!=dd.getDownid()){
					dal=new ArrayList<downDict>();
					dal.add(dd);
					GameManager.getInstance().getDowndictl().put(dd.getDownid(), dal);
					did=dd.getDownid();
				}else{
					GameManager.getInstance().getDowndictl().get(did).add(dd);
				}
			}
			rs.close();
			ps.close();
			//获取关卡对话
			sql="SELECT * FROM storytalkdict ORDER BY missionid,talktype,talkseq";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				if(GameManager.getInstance().getTalkdict().containsKey(rs.getInt("missionid"))){//已有此关卡
					if(rs.getInt("talktype")==0){//战前
						if(GameManager.getInstance().getTalkdict().get(rs.getInt("missionid")).containsKey(0)){//已有战前对话
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("AA", rs.getInt("talkloc"));
							map.put("AB", rs.getInt("rolestyle"));
							map.put("AC", rs.getString("talkcont"));
							map.put("AD", "@");
							GameManager.getInstance().getTalkdict().get(rs.getInt("missionid")).get(0).add(map);
						}else{
							ArrayList<Map<String, Object>> tempal = new ArrayList<Map<String, Object>>();
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("AA", rs.getInt("talkloc"));
							map.put("AB", rs.getInt("rolestyle"));
							map.put("AC", rs.getString("talkcont"));
							map.put("AD", "@");
							tempal.add(map);
							GameManager.getInstance().getTalkdict().get(rs.getInt("missionid")).put(0,tempal);
						}						
					}else{//战后
						if(GameManager.getInstance().getTalkdict().get(rs.getInt("missionid")).containsKey(1)){//已有战后对话
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("AA", rs.getString("talkrole"));
							map.put("AB", rs.getInt("rolestyle"));
							map.put("AC", rs.getString("talkcont"));
							
							if(rs.getInt("rolestyle")==0){//电脑
								map.put("AD", GameManager.getInstance().getCarddict().get(rs.getString("talkrole")).getCardname());
							}else map.put("AD", "@");
							GameManager.getInstance().getTalkdict().get(rs.getInt("missionid")).get(1).add(map);
						}else{
							ArrayList<Map<String, Object>> tempal = new ArrayList<Map<String, Object>>();
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("AA", rs.getString("talkrole"));
							map.put("AB", rs.getInt("rolestyle"));
							map.put("AC", rs.getString("talkcont"));
							if(rs.getInt("rolestyle")==0){//电脑
								map.put("AD", GameManager.getInstance().getCarddict().get(rs.getString("talkrole")).getCardname());
							}else map.put("AD", "@");
							tempal.add(map);
							GameManager.getInstance().getTalkdict().get(rs.getInt("missionid")).put(1,tempal);
						}			
					}
					
				}else{//新关卡
					Map<Integer, ArrayList<Map<String, Object>>> talktyepmap = new HashMap<Integer, ArrayList<Map<String, Object>>>();
					ArrayList<Map<String, Object>> talkal = new ArrayList<Map<String, Object>>();
					if(rs.getInt("talktype")==0){//战前
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("AA", rs.getInt("talkloc"));
						map.put("AB", rs.getInt("rolestyle"));
						map.put("AC", rs.getString("talkcont"));
						map.put("AD", "@");
						talkal.add(map);
					}else{//战后
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("AA", rs.getString("talkrole"));
						map.put("AB", rs.getInt("rolestyle"));
						map.put("AC", rs.getString("talkcont"));
						if(rs.getInt("rolestyle")==0){//电脑
							map.put("AD", GameManager.getInstance().getCarddict().get(rs.getString("talkrole")).getCardname());
						}else map.put("AD", "@");
						talkal.add(map);
					}
					talktyepmap.put(rs.getInt("talktype"), talkal);
					GameManager.getInstance().getTalkdict().put(rs.getInt("missionid"), talktyepmap);
				}				
			}
			rs.close();
			ps.close();
			//街头霸王字典
			sql="SELECT * FROM streetleader";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				StreetDict sd = new StreetDict();
				sd.setStreetid(rs.getInt("streetid"));
				sd.setStreetname(rs.getString("streetname"));
				sd.setStreetdesc(rs.getString("streetdesc"));
				sd.setGiveexp(rs.getInt("giveexp"));
				sd.setGivemoeny(rs.getInt("givemoeny"));
				sd.setGold(rs.getInt("gold"));
				sd.setNextid(rs.getInt("nextid"));
				sd.setMarks(rs.getInt("marks"));
				sd.setGoods1(rs.getInt("goods1"));
				sd.setGoods1code(rs.getString("goods1code"));
				sd.setGoods2(rs.getInt("goods2"));
				sd.setGoods2code(rs.getString("goods2code"));
				GameManager.getInstance().getStreetdict().put(sd.getStreetid(), sd);
				//街霸NPC字典
				String sql1="SELECT * FROM streetnpc t1,npcdict t2 where t1.npcid=t2.npcid and streetid=? ORDER BY t1.npcloc";
				PreparedStatement ps1 = null;
				ResultSet rs1 = null;
				ps1 = conn.prepareStatement(sql1);
				ps1.setInt(1, sd.getStreetid());
				rs1 = ps1.executeQuery();
				ArrayList<CMissionNpcDict> ac= new ArrayList<CMissionNpcDict>(); 
				int i=0;
				while(rs1.next()){
					i=i+1;
					if(i>6) break;
					CMissionNpcDict cnd = new CMissionNpcDict();
					cnd.setMissionid(rs1.getInt("streetid"));
					cnd.setNpcid(rs1.getInt("npcid"));
					cnd.setNpcname(rs1.getString("npcname"));
					cnd.setNpcprop(rs1.getString("npcprop"));
					cnd.setNpcgrade(rs1.getInt("npcgrade"));
					cnd.setNpctype(rs1.getString("npctype"));
					cnd.setNpccode(rs1.getString("npccode"));
					cnd.setHP(rs1.getInt("hp"));
					cnd.setATK(rs1.getInt("atk"));
					cnd.setDEF(rs1.getInt("def"));
					cnd.setHpalter(rs1.getInt("hpalter"));
					cnd.setAtkalter(rs1.getInt("atkalter"));
					cnd.setDefalter(rs1.getInt("defalter"));
					cnd.setSkillcode(rs1.getString("skillcode"));
					cnd.setSkillgrade(rs1.getInt("skillgrade"));
					cnd.setNpcloc(rs1.getInt("npcloc"));//位置
					ac.add(cnd);
				}
				GameManager.getInstance().getStreetnpcdict().put(sd.getStreetid(),ac);
				rs1.close();
				ps1.close();
			}
			rs.close();
			ps.close();
			//NPC字典
			String sql1="SELECT * FROM npcdict";
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			ps1 = conn.prepareStatement(sql1);
			rs1 = ps1.executeQuery();
			while(rs1.next()){
				CNpcDict cnd = new CNpcDict();
				cnd.setNpcid(rs1.getInt("npcid"));
				cnd.setNpcprop(rs1.getString("npcprop"));
				cnd.setNpcgrade(rs1.getInt("npcgrade"));
				cnd.setNpctype(rs1.getString("npctype"));
				cnd.setNpccode(rs1.getString("npccode"));
				cnd.setHP(rs1.getInt("hp"));
				cnd.setATK(rs1.getInt("atk"));
				cnd.setDEF(rs1.getInt("def"));
				cnd.setHpalter(rs1.getInt("hpalter"));
				cnd.setAtkalter(rs1.getInt("atkalter"));
				cnd.setDefalter(rs1.getInt("defalter"));
				cnd.setSkillcode(rs1.getString("skillcode"));
				cnd.setSkillgrade(rs1.getInt("skillgrade"));
				GameManager.getInstance().getCnpcdict().put(cnd.getNpcid(), cnd);
			}			
			rs1.close();
			ps1.close();
			//
			//世界BOSS
			sql="SELECT * FROM bossactor WHERE serverno=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, GameManager.getProps().getProperty("ServerCode").toString());
			rs = ps.executeQuery();
			if(rs.next()){
				WorldBossDict boss = GameManager.getInstance().getWorldBoss();
				boss.setBossID(rs.getString("bosscode"));
				boss.setBossval(rs.getInt("bossval"));
				boss.setDeadTime(rs.getInt("lasttime"));
				boss.setSumhp(rs.getInt("bosshp"));
				boss.setSrvno(rs.getString("serverno"));
				boss.setBasehp(boss.getSumhp());
				boss.setCurhp(boss.getBasehp());
				boss.setCurtime(rs.getString("curtime"));
				boss.setIsnew(true);
			}
			rs.close();
			ps.close();	
			ResetBossWorld(conn);
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	
	//更新worldboss数据
	public void ResetBossWorld() throws SQLException{
		Connection conn = pool.getConnection();
		try{
			ResetBossWorld(conn);
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}finally{
			conn.close();
		}
		
	}
	public void ResetBossWorld(Connection conn) throws SQLException{
		//Connection conn = pool.getConnection();
		try{
			WorldBossDict boss = GameManager.getInstance().getWorldBoss();
			String serverno = GameManager.getProps().getProperty("ServerCode").toString();			
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			String bossid="";
			//String[] randboss={"A049","A051","A052","A064","A067","A077","A085","A086","A087","A088","A089","A096"};
			int addlevel = 0;
			Random rd = new Random();
			int k=rd.nextInt(GameManager.getInstance().getRandboss().length);
			bossid=GameManager.getInstance().getRandboss()[k];
			logger.info("今日boss ："+bossid);
			//更新掉落字典 每次攻击有概率掉落boss本体卡碎片9 
			ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(100);
			for(int i=0;i<al.size();i++){
				if(al.get(i).getGoodstype()==9){
					al.get(i).setDowncode(bossid);
					al.get(i).setGoodslevel(GameManager.getInstance().getCarddict().get(bossid).getStarlevel());
					break;
				}
			}				
			String sql ="UPDATE bossactor SET bosscode=?,curtime=?,lasttime=? WHERE serverno=?";
			if(!boss.isIsnew()){//被挑战过的boss
				ps1 = conn.prepareStatement(sql);
				ps1.setString(1, bossid);
				ps1.setString(2, CommTools.TimestampToStr(System.currentTimeMillis()));
				ps1.setInt(3,  boss.getDeadTime());
				ps1.setString(4, serverno);
				ps1.executeUpdate();
				ps1.close();
				boss.setCurtime(CommTools.TimestampToStr(System.currentTimeMillis()));
				ps1 = null;
			}
			//新boss
			if(boss.getDeadTime()>0&&boss.getDeadTime()<=300) addlevel = 3;
			boss.setSumhp(boss.getBasehp()*(boss.getBossval()+addlevel));
			boss.setCurhp(boss.getSumhp());
			boss.setDeadTime(0);
			boss.setIsnew(true);
			boss.setBossID(bossid);
			boss.setBossname(GameManager.getInstance().getCarddict().get(bossid).getCardname());
			//获取前三名
			sql = "SELECT t1.rolename,sum(t1.hurtvalue) sumval,t2.cardcode,t2.starlevel FROM useractboss t1,usercard"+serverno+" t2 WHERE t1.rolename=t2.rolename AND t1.serverno=t2.serverno AND t2.locate=0 AND t1.serverno=? AND t1.acttime>=? AND t1.acttime<? GROUP BY t1.rolename ORDER BY sumval DESC LIMIT 3";
			int temp = (int)(System.currentTimeMillis()/1000);
			int btime = 0;
			int etime = 0;
			if(temp >= CommTools.GetTimeInMillis(0, GameManager.getInstance().getWorldBoss().getBhour(), GameManager.getInstance().getWorldBoss().getBminute())){
				btime = CommTools.GetTimeInMillis(0, GameManager.getInstance().getWorldBoss().getBhour(), GameManager.getInstance().getWorldBoss().getBminute());
				etime = CommTools.GetTimeInMillis(0, GameManager.getInstance().getWorldBoss().getEhour(), GameManager.getInstance().getWorldBoss().getEminute());
			}else{
				btime = CommTools.GetTimeInMillis(-1, GameManager.getInstance().getWorldBoss().getBhour(), GameManager.getInstance().getWorldBoss().getBminute());
				etime = CommTools.GetTimeInMillis(-1, GameManager.getInstance().getWorldBoss().getEhour(), GameManager.getInstance().getWorldBoss().getEminute());
			}
			ps1 = conn.prepareStatement(sql);
			ps1.setString(1, serverno);
			ps1.setInt(2, btime);
			ps1.setInt(3, etime);
			rs1 = ps1.executeQuery();
			ArrayList<HashMap<String,String>> lastrank = new ArrayList<HashMap<String,String>>();
			while(rs1.next()){
				HashMap<String,String> u = new HashMap<String,String>();
				u.put("BA", rs1.getString("rolename"));
				u.put("BB", rs1.getString("cardcode"));
				u.put("BC", rs1.getString("starlevel"));
				lastrank.add(u);
			}
			GameManager.getInstance().getWorldBoss().setLastrank(lastrank);
			rs1.close();
			rs1 = null;
			ps1.close();
			ps1 = null;			
			ArrayList<HashMap<String,String>> killer = new ArrayList<HashMap<String,String>>();
			//获取补刀
			sql="SELECT t1.rolename,t2.cardcode,t2.starlevel FROM useractboss t1,usercard"+serverno+" t2 WHERE t1.rolename=t2.rolename AND t1.serverno=t2.serverno AND t1.serverno=? AND t1.acttime>? AND t1.acttime<? AND t1.ifdown=2 AND t2.locate=0";
			ps1 = conn.prepareStatement(sql);
			ps1.setString(1, serverno);
			ps1.setInt(2, btime);
			ps1.setInt(3, etime);
			rs1 = ps1.executeQuery();
			if(rs1.next()){
				HashMap<String,String> u = new HashMap<String,String>();
				u.put("BA", rs1.getString("rolename"));
				u.put("BB", rs1.getString("cardcode"));
				u.put("BC", rs1.getString("starlevel"));
				killer.add(u);
			}			
			ps1.close();
			rs1.close();
			GameManager.getInstance().getWorldBoss().setKiller(killer);
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}
	}
	
	//计算当天top5
	public void HurtBossTop5(User player,WorldBossDict boss,int type,int curtime,int ret,int hurt,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		String serverno = GameManager.getProps().getProperty("ServerCode").toString();
		int chipflag=0;
		try{
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			sendMap.put("AM", boss.getSumhp());
			sendMap.put("AL", 0);		
			conn.setAutoCommit(false);
			String sql ="INSERT INTO useractboss VALUES(?,?,?,?,?,?,?,?)";
			if(ret != 0){//boss已经死了或者准备战斗
				ps1 = conn.prepareStatement(sql);
				ps1.setString(1, boss.getBossID());
				ps1.setString(2, player.getSvrcode());
				ps1.setInt(3, curtime);
				ps1.setString(4, player.getUrole());
				ps1.setInt(5, hurt);
				ps1.setInt(6, ret);
				int award = hurt/5;
				int downflag= -1;
				downflag = CalcDown(100);
				downDict dd=null;
				if(downflag>=0){
					ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(100);		
					dd = al.get(downflag);
					if(dd.getGoodstype()==9)  chipflag=1;//记录是否掉落碎片
					ps1.setString(7, award+","+dd.getDowncode());
					sendMap.put("AD", 0);//掉落
				}else{//无掉落
					ps1.setString(7, award+"");		
					sendMap.put("AD", -1);										
				}					
				ps1.setInt(8, player.getLevel());
				ps1.executeUpdate();
				ps1.close();
				if(downflag>=0){
					procdowngoods(player,dd,sendMap,conn);	
				}
				ps1 = null;				
				/////////////////////////
				int gold = 0;
				sql ="UPDATE userroletbl SET money=money+?,bosscd=?,gold=gold+? WHERE username=? AND rolename=? AND serverno=?";
				ps1 = conn.prepareStatement(sql);
				ps1.setInt(1, award);
				ps1.setInt(2, curtime);
				if(type==2)//立即挑战
					gold =-2;
				else if(type==3)//极限挑战 
					gold =-10;
				ps1.setInt(3, gold);
				ps1.setString(4, player.getUname());
				ps1.setString(5, player.getUrole());
				ps1.setString(6, serverno);
				ps1.executeUpdate();
				ps1.close();
				ps1 = null;
				if(gold<0){//gold<0 有消费则记录消费
					sql="INSERT INTO  userconsum"+player.getSvrcode()+"  VALUES(?,?,?,1,?,'挑战BOSS',?)";
					ps1=conn.prepareStatement(sql);
					ps1.setString(1, player.getUrole());
					ps1.setString(2, player.getSvrcode());
					ps1.setString(3, Long.toString(System.currentTimeMillis()));
					ps1.setInt(4,gold*-1);
					ps1.setString(5,String.valueOf(player.getGold()+gold));//金条强化成功等级
					ps1.executeUpdate();
					ps1.close();
				}
				sendMap.put("AL", hurt);
				sendMap.put("AJ", hurt/5);
				if(ret==2){//boss被击倒 排名结算
					this.CalcBossRank(player.getSvrcode(),conn);	
					if(GameManager.getInstance().getBeattimer()!=null){
						GameManager.getInstance().getBeattimer().cancel();
						GameManager.getInstance().setBeattimer(null);
					}
					GameManager.worldbosschannel.clear();
					GameManager.getInstance().getUseractbosslist().clear();
					GameManager.getInstance().getBossTop5().clear();
					GameManager.getInstance().setRankdate(CommTools.TimestampToStr(System.currentTimeMillis()));
				}
				player.setMoney(award+player.getMoney());
				player.setBosscdval(curtime);
				player.setGold(player.getGold()+gold);
				ArrayList<Map<String,Object>> ul=GameManager.getInstance().getUseractbosslist();
				Map<String,Object> ulmap = new HashMap<String,Object>();
				ulmap.put("BB", player.getUrole());
				ulmap.put("BC",player.getCardlist().get(player.getCardlocid()[0]).getCardcode());
				ulmap.put("BD",String.valueOf(player.getCardlist().get(player.getCardlocid()[0]).getStarlevel()));
				ulmap.put("BE",String.valueOf(hurt));
				ulmap.put("BF",String.valueOf(boss.getCurhp()));
				ulmap.put("BG",String.valueOf(boss.getSumhp()));
				ul.add(ulmap);
			}
			conn.commit();
			conn.setAutoCommit(true);
			sql="(SELECT rolename,curlevel,sum(hurtvalue) sumval,count(1) countsum  FROM useractboss WHERE acttime>? AND acttime<? AND serverno=? GROUP BY rolename ORDER BY sumval DESC  LIMIT 5) UNION (SELECT rolename,curlevel,sum(hurtvalue) sumval,count(1) countsum FROM useractboss WHERE acttime>? AND acttime<? AND serverno=? AND rolename=?)";
			ArrayList<Map<String,Object>> TOP5 = new ArrayList<Map<String,Object>>();
			int btime = CommTools.GetTimeInMillis(0, 00, 00);
			int etime = CommTools.GetTimeInMillis(0, 23, 59);
			ps1 = conn.prepareStatement(sql);
			ps1.setInt(1, btime);
			ps1.setInt(2, etime);
			ps1.setString(3, serverno);
			ps1.setInt(4, btime);
			ps1.setInt(5, etime);
			ps1.setString(6, serverno);
			ps1.setString(7, player.getUrole());
			rs1 = ps1.executeQuery();
			DecimalFormat df1 = new DecimalFormat("0.00%");
			int flag = 0;
			while(rs1.next()){
				if(rs1.getInt("sumval")>0){
					if(player.getUrole().equals(rs1.getString("rolename"))){
						sendMap.put("BD", rs1.getInt("countsum"));
						sendMap.put("BE", rs1.getInt("sumval"));
						sendMap.put("BF", df1.format(rs1.getInt("sumval")*1.0/boss.getSumhp()));
						if(flag==0){
							Map<String,Object> u = new HashMap<String,Object>();
							u.put("CA", rs1.getString("rolename"));
							u.put("CB", rs1.getInt("curlevel"));
							u.put("CC", rs1.getInt("sumval"));
							u.put("CD", df1.format(rs1.getInt("sumval")*1.0/boss.getSumhp()*1.0));
							TOP5.add(u);
							flag++;
						}
					}else{
						Map<String,Object> u = new HashMap<String,Object>();
						u.put("CA", rs1.getString("rolename"));
						u.put("CB", rs1.getInt("curlevel"));
						u.put("CC", rs1.getInt("sumval"));
						u.put("CD", df1.format(rs1.getInt("sumval")*1.0/boss.getSumhp()*1.0));
						TOP5.add(u);
					}
				}				
			}
			GameManager.getInstance().setBossTop5(TOP5);
			sendMap.put("BH", TOP5.toArray());
			rs1.close();
			ps1.close();
			if(chipflag>0){
				String mess="恭喜！玩家--"+player.getUrole()+"--在攻击魔王时获得了魔王本体碎片";
				this.playerpubmess(player.getSvrcode(), mess);
			}			
		}catch(SQLException e){
			conn.rollback();
			conn.setAutoCommit(true);
			boss.setCurhp(boss.getCurhp()+hurt);
			e.printStackTrace();
			logger.info(e.getMessage());
			throw e;
		}finally{
			conn.close();
		}
	}
	
	//精炼装备
	public void UpgradeDevice(User p,UserDevice ud,int fid,int sflag,int goldnum,int moneynum) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				if(sflag>0){//精炼成
					DeviceUpdate(p,ud,conn);
					//如果此装备在卡牌上，更新卡牌数据
					if(ud.getCardid()>0){
						UserCard uc= p.getCardlist().get(ud.getCardid());
						CardUpdate(p,uc,conn);
					}
				}
				PlayerUpdate(p,conn,goldnum*-1);//消费
				if(fid>0){//强化
					String sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,'强化',?)";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, Long.toString(System.currentTimeMillis()));
					ps.setInt(4,goldnum);
					ps.setString(5,String.valueOf(sflag));//金条强化成功等级
					ps.executeUpdate();
					ps.close();
					
				}
				this.UpdateDayMission(p, "精炼", conn);
				conn.commit();
				conn.setAutoCommit(true);
				if(fid>0&&sflag>5){//连升5级记录消息
					String str=ud.getDevicename();
					String mess="人品爆发！玩家--"+p.getUrole()+"--在对装备*"+str+"*完美强化时触发了爆击，装备连升"+sflag+"级";
					this.playerpubmess(p.getSvrcode(), mess);
				}
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				if(fid>0) p.setGold(p.getGold()+goldnum);
				else p.setMoney(p.getMoney()+moneynum);
				this.rollbackDevice(p);
				this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//放弃培养
	public void abortliveup(int cid,User p) throws SQLException{
		Connection conn = pool.getConnection();
		String sql ="UPDATE usercard"+p.getSvrcode()+" SET tlivehp=0,tliveatk=0,tlivedef=0 WHERE cardid=?"; //放弃培养
		PreparedStatement ps = null;
		if(conn != null){
			try {
				ps=conn.prepareStatement(sql);
				ps.setInt(1,cid);
				ps.executeUpdate();
				ps.close();
			}catch (SQLException e) {
				this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//培养数值
	public int actliveupcard(User p,UserCard uc,String ltype,Map<String,Object> sendMap,int mhp,int matk,int mdef,int ccost) throws SQLException{
		//根据培养code获取可能的培养值
		int returnflag=1,gnum=0;
		LiveUpDictCla luc = GameManager.getInstance().getLiveupdict().get(ltype);
		int adhp=0,adatk=0,addef=0;
		if(ltype.equals("L001")) {
			if(p.getMoney()>=ccost)	p.setMoney(p.getMoney()-ccost);
			else return 0;
		}
		if(ltype.equals("L002")) {
			ccost=0;
			if(p.getGold()>=luc.getGold()){
				gnum=luc.getGold();
				p.setGold(p.getGold()-luc.getGold());
			}
			else return 0;
		}
		if(ltype.equals("L003")) {
			ccost=0;
			//if(p.getViplevel()<luc.getVipgrade()) return -1;
			if(p.getGold()>=luc.getGold()) {
				gnum=luc.getGold();
				p.setGold(p.getGold()-luc.getGold());
			}
			else return 0;
		}
		int kflag=0,inxflag=0,liveflag=0;//培养正值概率
		if(ltype.equals("L001")){
			liveflag=0;
			kflag =(int)(1000/(uc.getLivehp()-0.1*mhp+10))+20;
			inxflag=CommTools.randomval(0,100);
			if(kflag>=50){
				if(inxflag<=kflag){
					liveflag=1;
				}
			}else{
				if(inxflag>100-kflag){
					liveflag=1;
				}
			}
			if(uc.getLivehp()<mhp*0.1){
				adhp=CommTools.randomval(0, luc.getHpmax());
			}else {
				if(liveflag>0)	adhp=CommTools.randomval(0, luc.getHpmax());
				else adhp=CommTools.randomval(0,luc.getHpmin())*-1;
			}
			if(uc.getLiveatk()<matk*0.1){
				adatk=CommTools.randomval(0,luc.getAtkmax());
			}else{
				if(liveflag>0)	adhp=CommTools.randomval(0, luc.getAtkmax());
				else adatk=CommTools.randomval(0,luc.getAtkmin())*-1;
			}
			if(uc.getLivedef()<mdef*0.1){
				addef=CommTools.randomval(1,luc.getDefmax());
			}else{
				if(liveflag>0)	adhp=CommTools.randomval(0, luc.getDefmax());
				else addef=CommTools.randomval(0,luc.getDefmin())*-1;
			}			
		}else if(ltype.equals("L002")){
			adhp=CommTools.randomval(luc.getHpmin(), luc.getHpmax());		
			adatk=CommTools.randomval(luc.getAtkmin(),luc.getAtkmax());		
			addef=CommTools.randomval(luc.getDefmin(),luc.getDefmax());				
		}else if(ltype.equals("L003")){
			adhp=CommTools.randomval(luc.getHpmin(), luc.getHpmax())*5;		
			adatk=CommTools.randomval(luc.getAtkmin(),luc.getAtkmax())*5;		
			addef=CommTools.randomval(luc.getDefmin(),luc.getDefmax())*5;		
		}
		if(adhp+uc.getLivehp()>mhp){//大于培养上限
			adhp=mhp-uc.getLivehp();
		}
		if(adatk+uc.getLiveatk()>matk){
			adatk=matk-uc.getLiveatk();
		}
		if(addef+uc.getLivedef()>mdef){
			addef=mdef-uc.getLivedef();
		}
		//logger.info("11:"+adhp+" 22:"+adatk+" 33:"+addef);
		sendMap.put("AE",uc.getLivehp());
		sendMap.put("AF",uc.getLiveatk());
		sendMap.put("AG",uc.getLivedef());
		sendMap.put("AB", adhp);
		sendMap.put("AC", adatk);
		sendMap.put("AD", addef);
		uc.setTlivehp(adhp);
		uc.setTliveatk(adatk);
		uc.setTlivedef(addef);
		//更新消耗
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				String sql="UPDATE usercard"+p.getSvrcode()+" SET tlivehp=?,tliveatk=?,tlivedef=? WHERE cardid=?";
				PreparedStatement ps = null;
				ps=conn.prepareStatement(sql);
				ps.setInt(1, adhp);
				ps.setInt(2, adatk);
				ps.setInt(3, addef);
				ps.setInt(4, uc.getCardid());
				ps.executeUpdate();
				ps.close();
				PlayerUpdate(p,conn,gnum*-1); //消费
				if(!ltype.equals("L001")) {//金条培养
					sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,'培养',?)";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, Long.toString(System.currentTimeMillis()));
					ps.setInt(4, luc.getGold());
					ps.setString(5, String.valueOf(p.getGold()));//培养后剩余金条数
					ps.executeUpdate();
					ps.close();
				}
				this.UpdateDayMission(p, "培养", conn);
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				if(ltype.equals("L001")) p.setMoney(p.getMoney()+ccost);//非金条培养
				else p.setGold(p.getGold()+luc.getGold());
				this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{
				conn.close();	
			}
		}	
		return returnflag;
	}
	//保存培养值
	public void saveactliveup(User p,UserCard uc) throws SQLException{
		Connection conn = pool.getConnection();
		int ahp=uc.getTlivehp();
		int aatk=uc.getTliveatk();
		int adef=uc.getTlivedef();
		if(conn != null){
			try {
				int maxhp=0,maxdef=0,maxatk=0;
				maxdef=uc.getCardlevel()*30;//培养上限
				maxatk=uc.getCardlevel()*30;
				maxhp=uc.getCardlevel()*30;
				if(uc.getLivehp()+ahp>maxhp){
					ahp=maxhp-uc.getLivehp();
				}
				if(uc.getLiveatk()+aatk>maxatk){
					aatk=maxatk-uc.getLiveatk();
				}
				if(uc.getLivedef()+adef>maxdef){
					adef=maxdef-uc.getLivedef();
				}
				if(uc.getLivehp()+ahp<=0){
					ahp=-1*uc.getLivehp();
					uc.setLivehp(0);
				}else uc.setLivehp(uc.getLivehp()+ahp);
				if(uc.getLiveatk()+aatk<=0) {
					uc.setLiveatk(0);
					aatk=-1*uc.getLiveatk();
				}else uc.setLiveatk(uc.getLiveatk()+aatk);
				if(uc.getLivedef()+adef<=0) {
					uc.setLivedef(0);
					adef=-1*uc.getLivedef();
				}else uc.setLivedef(uc.getLivedef()+adef);
				//logger.info("血："+ahp+"攻："+aatk+"防："+adef);
				conn.setAutoCommit(false);
				/*if(uc.getIfuse()>0){ // 卡牌在阵容中修改用户总血攻防
					p.setUhp(p.getUhp()+ahp);
					p.setUatk(p.getUatk()+aatk);
					p.setUdef(p.getUdef()+adef);
				}				*/		
				uc.setHp(uc.getHp()+ahp);
				uc.setAtk(uc.getAtk()+aatk);
				uc.setDef(uc.getDef()+adef);
				uc.setTlivehp(0);
				uc.setTliveatk(0);
				uc.setTlivedef(0);
				CardUpdate(p,uc,conn);
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	
	//转生
	public void actlifeup(User p,UserCard uc,String flag,int money,int ball) throws SQLException{
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				CardUpdate(p,uc,conn);
				//转生消耗 
				PlayerUpdate(p,conn,0);
				if(uc.getStarlevel()>=5){//恭喜！玩家XXX成功将X星XX提升至X星，实力大增！
					String str="恭喜！玩家--"+p.getUrole()+"--成功将"+(uc.getStarlevel()-1)+"星"+uc.getCardname()+"提升至"+uc.getStarlevel()+"星，实力大增！";
					this.playerpubmess(p.getSvrcode(), str);
				}
				//记录转生丹消耗
				String sql="INSERT INTO  userpropuse"+p.getSvrcode()+"  VALUES(?,?,?,?,?,'BALL',?)";
				PreparedStatement ps = null;
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUname());
				ps.setString(2, p.getUrole());
				ps.setString(3, p.getSvrcode());
				ps.setString(4, Long.toString(System.currentTimeMillis()));
				ps.setInt(5,ball);//消耗转生丹
				ps.setString(6,uc.getCardcode());//当前转生卡牌
				ps.executeUpdate();
				ps.close();
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				p.setMoney(p.getMoney()+money);
				p.setBall(p.getBall()+ball);
				this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	/*public void userpropconsum(User p,int costnum,String memo){
		
	}*/
	//融魂 要返回转移经验 魅力值
	public void mergecard(User p,UserCard uc,int tcid) throws SQLException{
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				CardUpdate(p,uc,conn);
				PreparedStatement ps = null;
				String sql="DELETE FROM usercard"+p.getSvrcode()+" WHERE cardid=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, tcid);
				ps.executeUpdate();
				ps.close();
				//散魂牌删除
				conn.commit();
				conn.setAutoCommit(true);
				
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//构造用户任务表
	public void GetUserMission(User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM usermission"+p.getSvrcode()+" t1,missiondict t2 where t1.missionid=t2.missionid AND t1.rolename=? AND t1.serverno=?";
		if(conn != null){
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1,p.getUrole());
				ps.setString(2,p.getSvrcode());
				rs=ps.executeQuery();
				while(rs.next()){
					UserMission um= new UserMission();
					um.setRolename(rs.getString("rolename"));
					um.setServerno(rs.getString("serverno"));
					um.setCurtime(rs.getString("curtime"));
					um.setWinlose(rs.getInt("winlose"));
					um.setAssess(rs.getString("assess"));
					um.setIfcopy(rs.getInt("ifcopy"));
					um.setMissionid(rs.getInt("missionid"));
					um.setSceneno(rs.getInt("sceneno"));
					um.setMissionseq(rs.getInt("missionseq"));
					um.setScenename(rs.getString("scenename"));
					um.setMissions(rs.getInt("missions"));
					um.setMissionmp(rs.getInt("missionmp"));
					um.setCopymp(rs.getInt("copymp"));
					um.setSkpage(rs.getInt("skpage"));
					um.setDevone(rs.getInt("devone"));
					um.setDevtwo(rs.getInt("devtwo"));
					um.setDevthr(rs.getInt("devthr"));
					um.setStar1(rs.getInt("star1"));
					um.setStar2(rs.getInt("star2"));
					um.setStar3(rs.getInt("star3"));
					um.setStar4(rs.getInt("star4"));
					um.setStar5(rs.getInt("star5"));
					um.setMarks(rs.getInt("marks"));
					um.setGrade(rs.getInt("grade"));
					um.setMdesc(rs.getString("mdesc"));
					um.setIfcanuse(rs.getInt("ifcanuse"));
					p.getUmisson().put(um.getMissionid(), um);
				}
				rs.close();
				ps.close();
				
			}catch (SQLException e) {
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//技能修炼完成
	public int SkillSuccess(String skcode,User p) throws SQLException{
		int skillid=0;
		CallableStatement cs = null;
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				cs = conn.prepareCall("{call PRODUCESKILL(?,?,?,?,?,?)}");
				cs.setString(1,skcode);
				cs.setString(2, p.getUrole());
				cs.setString(3,p.getSvrcode());
				cs.setString(4,p.getUname());
				cs.registerOutParameter(5, Types.INTEGER);
				cs.registerOutParameter(6, Types.INTEGER);//技能id
				cs.execute();
				skillid=cs.getInt(6);
				if(cs.getInt(5)==0){
					cs.close();
					throw new SQLException("skill rollback");
				}					
				cs.close();
			}catch (SQLException e) {
				this.rollbackSkillP(p);
				this.rollbackSkill(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();				
			}
		}
		return skillid;
	}
	//获取技能
	public void GetUserSkill(User p,int skid) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM userskill"+p.getSvrcode()+" WHERE skillid=?";
		if(conn != null){
			try {
				ps = conn.prepareStatement(sql);
				ps.setInt(1,skid);//????
				rs=ps.executeQuery();
				if(rs.next()){
					UserSkill cardskill = new UserSkill();
					cardskill.setSkillid(rs.getInt("skillid")) ;  //技能id
					cardskill.setSkillcode(rs.getString("skillcode"));
					cardskill.setSkillname(rs.getString("skillname"))  ;   //技能名称
					cardskill.setSkilldesc(rs.getString("skilldesc")) ;   //技能描述
					cardskill.setSkilltype(rs.getString("skilltype")) ;   //技能类型 主动、被动
					cardskill.setSkillgrade(rs.getInt("skillgrade")) ;    // 技能等级
					cardskill.setIflife(rs.getInt("iflife"));
					SkillPages skp=GameManager.getInstance().getSkilldict().get(cardskill.getSkillcode());
					cardskill.setShp(skp.getHps()) ; //增血
					cardskill.setSatk(skp.getAtks()) ; //增攻
					cardskill.setSdef(skp.getDefs()) ; //增防
					cardskill.setCardid(rs.getInt("cardid")) ;  //装备卡id
					cardskill.setHurt(skp.getHurt());//装备上的主动伤害，伤害附加值
					cardskill.setSvs(skp.getSvs());
					cardskill.setSkillto(skp.getSkillto());
					cardskill.setSkillrand(skp.getSkillrand());
					cardskill.setHurtpert(skp.getHurtpert());//伤害百分比
					cardskill.setUphurt(skp.getUphurt());//升级伤害附加值增长百分比
					cardskill.setUphurtper(skp.getUphurtper());//升级伤害百分比增长百分比
					cardskill.setSkillcolor(skp.getSkillcolor());
					p.getUskilllist().put(cardskill.getSkillid(), cardskill);
				}
				rs.close();
				ps.close();
			}catch (SQLException e) {
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//合并技能
	public void unionskill(User p,UserSkill sk,int tsid) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		String sql ="";
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				sql="UPDATE userskill"+p.getSvrcode()+" SET skillgrade=? WHERE skillid=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,sk.getSkillgrade());
				ps.setInt(2,sk.getSkillid());
				ps.executeUpdate();
				ps.close();
				sql="DELETE FROM  userskill"+p.getSvrcode()+" WHERE skillid=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,tsid);
				ps.executeUpdate();
				ps.close();
				if(sk.getCardid()>0){
					UserCard uc=p.getCardlist().get(sk.getCardid());
					this.CardUpdate(p, uc, conn);
				}
				conn.commit();
				conn.setAutoCommit(true);				
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackSkill(p);
				if(sk.getCardid()>0) this.rollbackCard(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//构造用户当前任务表
	public void GetCurrMission(User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM usermission"+p.getSvrcode()+" t1,missiondict t2 where t1.missionid=t2.missionid AND t1.rolename=? AND t1.serverno=? ORDER BY t2.sceneno,t2.missionseq"; //AND t2.sceneno=?";
		if(conn != null){
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1,p.getUrole());
				ps.setString(2,p.getSvrcode());
				//ps.setInt(3,sceneno);
				rs=ps.executeQuery();
				int secno=0;				
				while(rs.next()){
					UserMission um= new UserMission();
					um.setRolename(rs.getString("rolename"));
					um.setServerno(rs.getString("serverno"));
					um.setCurtime(rs.getString("curtime"));
					um.setWinlose(rs.getInt("winlose"));
					um.setAssess(rs.getString("assess"));
					um.setIfcopy(rs.getInt("ifcopy"));
					um.setMissionid(rs.getInt("missionid"));
					um.setSceneno(rs.getInt("sceneno"));
					um.setMissionseq(rs.getInt("missionseq"));
					um.setMissions(rs.getInt("missions"));
					um.setMissionmp(rs.getInt("missionmp"));
					um.setCopymp(rs.getInt("copymp"));
					um.setSkpage(rs.getInt("skpage"));
					um.setDevone(rs.getInt("devone"));
					um.setDevtwo(rs.getInt("devtwo"));
					um.setDevthr(rs.getInt("devthr"));
					um.setStar1(rs.getInt("star1"));
					um.setStar2(rs.getInt("star2"));
					um.setStar3(rs.getInt("star3"));
					um.setStar4(rs.getInt("star4"));
					um.setStar5(rs.getInt("star5"));
					um.setMarks(rs.getInt("marks"));
					um.setGrade(rs.getInt("grade"));
					um.setMdesc(rs.getString("mdesc"));
					um.setIfcanuse(rs.getInt("ifcanuse"));
					um.setGiveexp(rs.getInt("giveexp"));
					um.setGivemoeny(rs.getInt("givemoney"));
					um.setLosepercent(rs.getInt("losepercent"));
					um.setCurnum(rs.getInt("curnum"));
					um.setNextmiss(rs.getInt("nextmission"));
					um.setMissnum(rs.getInt("misssum"));
					um.setGoods1(rs.getInt("goods1"));
					um.setGoods2(rs.getInt("goods2"));
					um.setGoods1code(rs.getString("goods1code"));
					um.setGoods2code(rs.getString("goods2code"));
					um.setProp1(rs.getInt("prop1"));
					um.setProp2(rs.getInt("prop2"));
					um.setProp3(rs.getInt("prop3"));
					um.setMissname(rs.getString("missname"));
					p.getUmisson().put(um.getMissionid(), um);
					if(secno!=(rs.getInt("sceneno"))){
						ArrayList<UserMission> sl=new ArrayList<UserMission>();
						sl.add(um);
						p.getScemisson().put(rs.getInt("sceneno"), sl);
					}else{
						p.getScemisson().get(rs.getInt("sceneno")).add(um);
					}
					secno=rs.getInt("sceneno");
				}
				ps.close();
				rs.close();
			}catch (SQLException e) {
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
	//任务完成结算
	public int FinishBattle(User p, UserMission um, int flag,int wlflag,Map<String,Object> semap,int mtype) throws SQLException{
		//获得掉落物品 code
		//更新用户信息 经验等级 队伍数。。。
		//更新阵容卡牌信息   经验升级或魅力改变重新运算卡牌
		//掉落物品进背包
		int skillid=0;
		int cid=0,pnum=0,ifupdate=0;
		String downcode="@",downname;
		CallableStatement cs = null;
		Connection conn = pool.getConnection();
		int exp=0;
		exp=um.getGiveexp();
		int mon=0;
		mon=um.getGivemoeny();
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				int cursecond=(int)(System.currentTimeMillis()/1000);
				if(wlflag>0){//胜且有掉落
					p.setCurmission(um.getNextmiss());//胜利进入下一关
					if(flag>0){
						//un ,rn ,sn,flag, downcode,OUT equalcardid,OUT pageno ,OUT oldnewflag ,OUT skcode 
						cs = conn.prepareCall("{call DOWNSOME(?,?,?,?,?,?,?,?,?)}"); //要加参数
						cs.setString(1, p.getUname());
						cs.setString(2, p.getUrole());
						cs.setString(3,p.getSvrcode());
						cs.setInt(4,flag);
						if(flag==10){//符文掉率单独计算
							int k=0;
							int[] a=new int[GameManager.getInstance().getMarkdict().size()];
							int[] loc=new int[GameManager.getInstance().getMarkdict().size()];
							String[] dcode=new String[GameManager.getInstance().getMarkdict().size()];
							Iterator<MarkDict> it = GameManager.getInstance().getMarkdict().values().iterator();
							while(it.hasNext()){
								MarkDict md=it.next();
								a[k]=md.getDownrand();
								loc[k]=k;
								dcode[k]=md.getMarkcode();
								k=k+1;
							}
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
							int r=rd.nextInt(10000);
							for(int i=0;i<a.length;i++){
								minval=maxval;
								if(a[i]==0) break;
								maxval=maxval+a[i];
								if(r>=minval && r<maxval){
									downcode=dcode[loc[i]];
									break;
								}
							}
						}
						if(flag==10) cs.setString(5, downcode); else
						if(flag==11) cs.setString(5, um.getGoods1code()); else
						if(flag==12) cs.setString(5, um.getGoods2code()); else 	cs.setString(5, "@"); //掉落code
						cs.registerOutParameter(5, Types.VARCHAR);//掉落code
						cs.registerOutParameter(6, Types.INTEGER);//卡牌id，>0表明已有相同卡牌 魅力变化
						cs.registerOutParameter(7, Types.INTEGER);//>0 技能页号
						cs.registerOutParameter(8, Types.INTEGER);//技能书页是否新
						cs.registerOutParameter(9, Types.VARCHAR);//掉落物品名称
						cs.execute();
						downcode=cs.getString(5);
						cid=cs.getInt(6);
						pnum=cs.getInt(7);
						ifupdate=cs.getInt(8);
						downname=cs.getString(9);						
						cs.close();
						//logger.info("掉落:"+flag+"--物品名称："+downname);
						semap.put("DV", 0);
						switch(flag){
						case 1://技能书页
							DownSkillPages(p,downcode,ifupdate,pnum,conn);
							semap.put("AF", downcode);
							semap.put("AE", pnum);
							semap.put("AG", downname);
							semap.put("DV", GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor());
							break;
						case 2://装备
						case 3:
						case 4:
						case 21:
						case 22:
							DownDevice(p,downcode,conn);
							semap.put("AF", downcode);
							semap.put("AE", 0);
							semap.put("AG", downname);
							semap.put("DV", GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor());
							break;
						case 5://卡牌					
						case 6:
						case 7:
						case 8:
						case 9:
							DownCard(p, downcode, cid, conn);
							semap.put("AF", downcode);
							semap.put("AE", 0);
							semap.put("AG", downname);
							semap.put("DV", GameManager.getInstance().getCarddict().get(downcode).getStarlevel());
							break;
						case 10://符文
						case 101:
							downname=GameManager.getInstance().getMarkdict().get(downcode).getMarkname();
							DownMark(p, downcode, conn);
							semap.put("AF", downcode);
							semap.put("AE", 0);
							semap.put("AG", downname);
							break;
						case 11://掉落指定装备1
						case 12://掉落指定装备2
							DownGoods(p, downcode,cid, conn);
							semap.put("AF", downcode);
							semap.put("AE", 0);
							semap.put("AG", downname);
							if(downcode.substring(0,1).equals("A")){
								semap.put("DV", GameManager.getInstance().getCarddict().get(downcode).getStarlevel());
							}
							if(downcode.substring(0,1).equals("B")){
								semap.put("DV", GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor());
							}
							if(downcode.substring(0,1).equals("M")){
								semap.put("DV", 0);
							}
							if(downcode.substring(0,1).equals("C")){
								semap.put("DV", GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor());
							}
							
							break;
						case 13://掉落道具1	
						case 14://掉落道具2
						case 15://掉落道具3
							DownProps(p, downcode, conn,1);
							semap.put("AF", downcode);
							semap.put("AE", 0);
							semap.put("AG", downname);
							break;
						}
					}	
					//额外掉落
					ArrayList<Map<String,Object>> mapal =new ArrayList<Map<String,Object>>();
					if(GameManager.getInstance().getSvractdict().containsKey(5)&&um.getMissionid()>=5&&mtype>0){
						if(cursecond>=GameManager.getInstance().getSvractdict().get(5).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(5).getEtime()){
							int downflag=CalcDown(201);
							if(downflag>=0){
								ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(201);
								downDict dd = al.get(downflag);
								Map<String,Object> map = new HashMap<String,Object>();
								procdowngoods(p,dd,map,conn);//处理掉落
								mapal.add(map);
							}
						}
					}
					semap.put("EX", mapal.toArray());
					//日常任务完成情况
					UpdateDayMission(p,"任务",conn);
					um.setCurnum(um.getCurnum()-1);//胜利算挑战一次
					
				}else{
					p.setCurmission(um.getMissionid());//败了还在此关卡
					exp=(int)(exp*um.getLosepercent()/100);
					mon=(int)(mon*um.getLosepercent()/100);
				}
				//重新计算是否经验加成
				
				if(GameManager.getInstance().getSvractdict().containsKey(1)&&um.getMissionid()>=9){
					if(cursecond>=GameManager.getInstance().getSvractdict().get(1).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(1).getEtime()){
						exp=(int)(exp*GameManager.getInstance().getSvractdict().get(1).getActvalue());
					}
				}				
				int curmp;
				if(um.getIfcopy()>0) curmp=um.getCopymp();
				else curmp=um.getMissionmp();
				p.setPower(p.getPower()-curmp);
				um.setWinlose(wlflag);				
				CalcCardFin(p, exp, conn);
				CalcUserFin(p, um,exp, mon, conn,semap);
				UpdateMission(p, um, wlflag,semap, conn);
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackPlay(p);//全部回滚
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();				
			}
		}		
		return skillid;
	}
	//日常任务类型，
	public void UpdateDayMission(User p,String mtype,Connection conn) throws SQLException{
		String sql ="";
		PreparedStatement ps = null;
		if(conn != null){
			try{
				sql="UPDATE userdaytask"+p.getSvrcode()+" SET curcond=curcond+1 WHERE rolename=? AND serverno=? AND tasktype=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3, mtype);
				ps.executeUpdate();
				ps.close();
				if(p.getUserdaytasklist().size()>0){
					for(int i=0;i<p.getUserdaytasklist().size();i++){
						if(p.getUserdaytasklist().get(i).getTasktype().equals(mtype)){
							p.getUserdaytasklist().get(i).setCurcond(p.getUserdaytasklist().get(i).getCurcond()+1);
						}
					}
				}
			}catch(SQLException e){
				logger.info(e.getMessage());
				throw e;
			}			
		}
	}
	//夺计结果记录
	public void FinGetSkill(User p,int flag,String skcode,int pageno,String rname,int elevel,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		int exp=0,mon=0;
		String sql="";
		PreparedStatement ps = null;
		int cursecond=(int)(System.currentTimeMillis()/1000);
		int batnum=1;
		try{
			conn.setAutoCommit(false);
			Pkexpmoney pkem=GameManager.getInstance().getPkexpmon().get(p.getLevel());
			if(GameManager.getInstance().getSvractdict().containsKey(3)){//针对夺计活动 经验翻倍掉率增加
				if(cursecond>=GameManager.getInstance().getSvractdict().get(3).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(3).getEtime()){
					batnum=2;
				}
			}
			switch(flag){
			/*case 0://失败扣钱
				exp=0;
				mon=-1*(int)((elevel*100-200)/1.5);
				break;*/
			case 1://胜利无掉落 获得经验钱
				smap.put("AE", -1);
				exp=pkem.getExp()*batnum;
				mon=pkem.getMoney();
				smap.put("DV", 0);
				break;
			case 2://技能书页
				exp=pkem.getExp()*batnum;
				mon=pkem.getMoney();
				DownSkillPages(p,skcode,1,pageno,conn);
				smap.put("AF", skcode);
				smap.put("AE", pageno);
				smap.put("AG", GameManager.getInstance().getSkilldict().get(skcode).getSkillname());
				smap.put("DV", GameManager.getInstance().getSkilldict().get(skcode).getSkillcolor());
				//更新对方书页数
				switch(pageno){
				case 1:
					sql="UPDATE userskillpage"+p.getSvrcode()+" SET page1=page1-1 ";
					break;
				case 2:
					sql="UPDATE userskillpage"+p.getSvrcode()+" SET page2=page2-1 ";
					break;
				case 3:
					sql="UPDATE userskillpage"+p.getSvrcode()+" SET page3=page3-1 ";
					break;
				case 4:
					sql="UPDATE userskillpage"+p.getSvrcode()+" SET page4=page4-1 ";
					break;
				case 5:
					sql="UPDATE userskillpage"+p.getSvrcode()+" SET page5=page5-1 ";
					break;
				}
				sql=sql+"WHERE rolename=? AND serverno=? AND skillcode=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, rname);
				ps.setString(2, p.getSvrcode());
				ps.setString(3, skcode);
				ps.executeUpdate();
				ps.close();
				//夺技能成功发邮件通知被夺玩家
				sql="INSERT INTO usermailbox VALUES(NULL,?,?,'技能碎片被夺',?,'@',0,'@',0,0,0,0,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1,rname);
				ps.setString(2, p.getSvrcode());
				ps.setString(3, "玩家--"+p.getUrole()+"--抢夺了你的"+smap.get("AG").toString()+"技能碎片-->"+pageno);
				ps.setInt(4, (int)(System.currentTimeMillis()/1000));
				ps.executeUpdate();
				ps.close();
				break;
			}
			this.UpdateDayMission(p, "夺技", conn);
			this.CalcCardFin(p, exp, conn);
			this.CalcUserFin(p,exp, mon, conn,smap);
			this.UpdateUserAct(p, 1, "夺技", 1,conn);			
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();
			GameManager.getInstance().getLockgetbook().remove(p.getUrole());
			GameManager.getInstance().getLockgetbook().remove(rname);
		}
		
	}
	//记录用户的夺技、决斗、扭蛋活动，保留5天
	public void UpdateUserAct(User p,int acttype,String memo,int anum,Connection conn)throws SQLException{
		PreparedStatement ps = null;
		long cursecond=System.currentTimeMillis()/1000;
		String sql="INSERT INTO useract VALUES(?,?,?,?,?,?)";
		try{			
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, (int)cursecond);
			ps.setInt(4,acttype);
			ps.setInt(5, anum);
			ps.setString(6, memo);
			ps.executeUpdate();
			ps.close();
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}		
	}
	//处理掉落符文
	public void DownMark(User p,String getcode,Connection conn) throws SQLException{
		PreparedStatement ps = null;
		MarkDict md= GameManager.getInstance().getMarkdict().get(getcode);
		String sql="INSERT INTO usermark"+p.getSvrcode()+" VALUES(?,?,NULL,?,?,?,?,?,?,?,0,-1)";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, md.getMarkcode());
			ps.setString(4, md.getMarkname());
			ps.setString(5, md.getMarkdesc());
			ps.setString(6, md.getMarktype());
			ps.setDouble(7, md.getHp());
			ps.setDouble(8, md.getAtk());
			ps.setDouble(9, md.getDef());
			ps.executeUpdate();
			ps.close();
			RefreshMark(p,conn,getcode);
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
				
	}
	//处理掉指定物品
	public void DownGoods(User p,String getcode,int cid,Connection conn) throws SQLException{
		try{
			//A卡  B装备  C 技能  M 符文
			if(getcode.substring(0,1).equals("A")){
				DownCard(p,getcode,cid,conn);
			}
			if(getcode.substring(0,1).equals("B")){
				DownDevice(p,getcode,conn);
			}
			if(getcode.substring(0,1).equals("M")){
				DownMark(p, getcode, conn);
			}
			if(getcode.substring(0,1).equals("C")){
				this.DownSkill(p, getcode, conn);
			}
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		
	}
	//处理掉落道具
	public void DownProps(User p,String getcode,Connection conn,int downnum) throws SQLException{
		PreparedStatement ps = null;
		String sql="";
		PropsDict pd= GameManager.getInstance().getPropsdict().get(getcode);
		try{
			if(p.getUserprop().containsKey(getcode)){//已有此道具
				//logger.info("更新道具："+getcode);
				UserProps up=p.getUserprop().get(getcode);
				up.setPropsnum(up.getPropsnum()+downnum);		
				sql="UPDATE userprops"+p.getSvrcode()+" SET propsnum=propsnum+? WHERE rolename=? AND serverno=? AND propscode=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, downnum);
				ps.setString(2, p.getUrole());
				ps.setString(3, p.getSvrcode());
				ps.setString(4, getcode);
				ps.executeUpdate();
				ps.close();
			}else{	
				//logger.info("新道具code："+getcode);
				sql="INSERT INTO userprops"+p.getSvrcode()+" VALUES(?,?,?,?)";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3, getcode);
				ps.setInt(4, downnum);
				ps.executeUpdate();
				ps.close();
				UserProps ups= new UserProps();
				ups.setPropscode(getcode);
				ups.setPropsnum(downnum);
				pd=GameManager.getInstance().getPropsdict().get(ups.getPropscode());
				ups.setPropsname(pd.getPropsname());
				ups.setPropsdesc(pd.getPropsdesc());
				ups.setPropsgrade(pd.getPropsgrade());
				ups.setPropstype(pd.getPropstype());
				ups.setPropsuse(pd.getPropsuse());
				ups.setPropsadd(pd.getPropsadd());
				ups.setPropspic(pd.getPropspic());
				if(pd.getPackgoods().size()>0){
					for(int i=0;i<pd.getPackgoods().size();i++){
						packageGoods pg=pd.getPackgoods().get(i).clone();
						ups.getPackgoods().add(pg);
					}
				}
				ups.setUsecond(pd.getUsecond());	
				p.getUserprop().put(getcode, ups);
			}
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}				
	}
	//掉落技能
	public void DownSkill(User p,String skcode,Connection conn) throws SQLException{
		PreparedStatement ps = null;
		SkillPages sk= GameManager.getInstance().getSkilldict().get(skcode);
		String sql="INSERT INTO userskill"+p.getSvrcode()+" VALUES(?,?,NULL,?,?,?,?,1,0,-1,0)";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, skcode);
			ps.setString(4, sk.getSkillname());
			ps.setString(5, sk.getSkdesc());
			ps.setString(6, sk.getSktype());
			ps.executeUpdate();
			ps.close();
			RefreshSkill(p,conn,skcode);
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}			
	}
	//掉落出生技能
	public void DownLifeSkill(User p,String skcode,String ccode,Connection conn) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		SkillPages sk= GameManager.getInstance().getSkilldict().get(skcode);
		String sql="";
		try{
			sql="SELECT cardid FROM usercard"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND cardcode=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, ccode);
			rs=ps.executeQuery();
			int cid=0;
			if(rs.next()) cid=rs.getInt("cardid");
			rs.close();
			ps.close();
			sql="INSERT INTO userskill"+p.getSvrcode()+" VALUES(?,?,NULL,?,?,?,?,1,?,0,1)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, skcode);
			ps.setString(4, sk.getSkillname());
			ps.setString(5, sk.getSkdesc());
			ps.setString(6, sk.getSktype());
			ps.setInt(7, cid);
			ps.executeUpdate();
			ps.close();
			RefreshSkill(p,conn,skcode);
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}			
	}
	//技能发生变化刷新内存
	public void RefreshSkill(User p,Connection conn,String skcode) throws SQLException{
		String sql ="SELECT * FROM userskill"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND skillcode=?"; //用户技能
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3,skcode);
			rs = ps.executeQuery();
			while(rs.next()){
				UserSkill cardskill = new UserSkill();
				cardskill.setSkillid(rs.getInt("skillid")) ;  //技能id
				cardskill.setSkillcode(rs.getString("skillcode"));
				cardskill.setSkillname(rs.getString("skillname"))  ;   //技能名称
				cardskill.setSkilldesc(rs.getString("skilldesc")) ;   //技能描述
				cardskill.setSkilltype(rs.getString("skilltype")) ;   //技能类型 主动、被动
				cardskill.setSkillgrade(rs.getInt("skillgrade")) ;    // 技能等级
				cardskill.setSkillloc(rs.getInt("skillloc"));
				cardskill.setIflife(rs.getInt("iflife"));
				SkillPages skp=GameManager.getInstance().getSkilldict().get(cardskill.getSkillcode());
				cardskill.setShp(skp.getHps()) ; //增血
				cardskill.setSatk(skp.getAtks()) ; //增攻
				cardskill.setSdef(skp.getDefs()) ; //增防
				cardskill.setCardid(rs.getInt("cardid")) ;  //装备卡id
				cardskill.setHurt(skp.getHurt());//装备上的主动伤害，伤害附加值
				cardskill.setSvs(skp.getSvs());
				cardskill.setSkillto(skp.getSkillto());
				cardskill.setSkillrand(skp.getSkillrand());
				cardskill.setHurtpert(skp.getHurtpert());//伤害百分比
				cardskill.setUphurt(skp.getUphurt());//升级伤害附加值增长百分比
				cardskill.setUphurtper(skp.getUphurtper());//升级伤害百分比增长百分比
				cardskill.setSkillcolor(skp.getSkillcolor());
				p.getUskilllist().put(cardskill.getSkillid(), cardskill); //记录用户技能列表		
			}
			rs.close();
			ps.close();
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}			
	}
	//刷新符文内存
	public void RefreshMark(User p,Connection conn,String mcode) throws SQLException{
		String sql ="SELECT * FROM usermark"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND markcode=?"; //装备镶嵌的符文
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, mcode);
			rs = ps.executeQuery();
			while(rs.next()){
				UserMark mymark = new UserMark();
				mymark.setMarkid(rs.getInt("markid")) ; //符文id
				mymark.setMarkcode(rs.getString("markcode"));
				mymark.setMarkname(rs.getString("markname")) ;  //符文名
				mymark.setMarkdesc(rs.getString("markdesc")) ;  //描述
				mymark.setMarktype(rs.getString("marktype")) ;  //符文类型 攻防血
				mymark.setAtk(rs.getInt("atk")) ;  //增攻
				mymark.setHp(rs.getInt("hp"));   //增血
				mymark.setDef(rs.getInt("def")) ;  //增防
				mymark.setDeviceid(rs.getInt("deviceid")) ;  //插入装备id
				mymark.setMarkloc(rs.getInt("markloc")) ;   //符文位置
				p.getUmarklist().put(mymark.getMarkid(), mymark); //记录用户符文列表		
			}
			rs.close();
			ps.close();
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}	
	}
	//处理掉落卡牌
		PreparedStatement ps = null;
		public void DownCard(User p,String getcode,int cid,Connection conn) throws SQLException{
		CardDict cd= GameManager.getInstance().getCarddict().get(getcode);
		CardLogicProc cplogic=new CardLogicProc();
		try{
			if(cid>0){//已有此卡牌 魅力增加
				UserCard uc=p.getCardlist().get(cid);
				uc.setCurvalue(uc.getCurvalue()+1);
				if(uc.getCurvalue()>99) uc.setCurvalue(99);
				//魅力变化判断是否开启新形态
				if(uc.getCurvalue()>=30&&uc.getCurvalue()<60)	{
					uc.setCurstyle(2);//开启二形态
					uc.setAltercurstyle(2);
				}
				if(uc.getCurvalue()>=60)  {
					uc.setCurstyle(3);//开启三形态
					uc.setAltercurstyle(3);
				}
				cplogic.CalcCardOrign(uc);
				if(uc.getIfuse()>0&&uc.getLocate()>=0){//在阵容中
					//重新穿装备及技能
					Iterator<UserDevice> its = uc.getDeviceMap().values().iterator();
					while(its.hasNext()){
						UserDevice udtemp=its.next();
						cplogic.LoadDev(p,uc,udtemp.getDeviceid());
					}
					Iterator<UserSkill> it = uc.getSkillMap().values().iterator();
					while(it.hasNext()){
						UserSkill uktemp=it.next();
						cplogic.InstSkill(p,uc,uktemp.getSkillid(),uktemp.getSkillloc());
					}
					cplogic.checktype(p);
				}
				this.CardUpdate(p, uc, conn);
			}else{//新卡牌
				int ohp,oatk,odef;
				//double cval=GameManager.getInstance().getValuedict().get(1);
				ohp=cd.getStarlevel()*25+(int)(cd.getBasehp()*10)+(int)(cd.getStarlevel()*6+cd.getBasehp()+8);
				oatk=cd.getStarlevel()*15+(int)(cd.getBaseatk()*4)+(int)(cd.getStarlevel()*2+cd.getBaseatk());
				odef=(int)(cd.getStarlevel()*6.5)+(int)(cd.getBasedef()*2)+(int)(cd.getStarlevel()*0.5+cd.getBasedef());
				//logger.info("新卡牌掉落");
				String sql="INSERT INTO usercard"+p.getSvrcode()+"(rolename,serverno,cardcode,cardname,carddesc,cardprop,starlevel,cardtype,"+
				"upexp,	basehp,baseatk,basedef,	alterhp,alteratk,alterdef,hp,atk,def,orighp,origatk,origdef,lifeskill,haveskill) VALUES("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3, cd.getCardcode());
				ps.setString(4, cd.getCardname());
				ps.setString(5, cd.getCarddesc());
				ps.setString(6, cd.getCardprop());
				ps.setInt(7, cd.getStarlevel());
				ps.setString(8, cd.getCardtype());
				ps.setInt(9,20);
				ps.setDouble(10,cd.getBasehp());
				ps.setDouble(11,cd.getBasedef());
				ps.setDouble(12,cd.getBasedef());
				ps.setDouble(13,cd.getAlterhp());
				ps.setDouble(14,cd.getAlteratk());
				ps.setDouble(15,cd.getAlterdef());
				ps.setInt(16, ohp);
				ps.setInt(17, oatk);
				ps.setInt(18, odef);
				ps.setInt(19, ohp);
				ps.setInt(20, oatk);
				ps.setInt(21, odef);
				ps.setString(22, cd.getLifeskill());
				if(!cd.getLifeskill().equals("@")) {//带天赋技能的卡
					ps.setInt(23, 1);//是否有技能
				}else ps.setInt(23, 0);
				ps.executeUpdate();
				ps.close();
				if(cd.getStarlevel()>5){
					if(!cd.getLifeskill().equals("@")) {//带天赋技能的卡
						this.DownLifeSkill(p, cd.getLifeskill(), cd.getCardcode(),conn);			
					}
				}
				sql="UPDATE userpicindex"+p.getSvrcode()+"  SET ifuse=1 WHERE rolename=? AND serverno=? AND cardcode=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3, getcode);
				ps.executeUpdate();
				ps.close();
				int upflag=0;
				if(p.getUsergetallmap().get(cd.getCardtype())==0){//未集齐
					ResultSet rs=null;
					sql="SELECT COUNT(1) CNUM FROM userpicindex"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND cardtype=? AND ifuse=0";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, cd.getCardtype());
					rs=ps.executeQuery();
					if(rs.next()){
						if(rs.getInt("CNUM")<1){
							p.getUsergetallmap().put(cd.getCardtype(), 1); 
							upflag=1;
						}
					}
					rs.close();
					ps.close();
				}		
				if(upflag>0){
					sql="UPDATE usergetall"+p.getSvrcode()+" SET ifgetall=1 WHERE rolename=? AND serverno=? AND cardtype=?";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, cd.getCardtype());
					ps.executeUpdate();
					ps.close();
					cplogic.checktype(p);//新集齐计算buff
				}
				RefreshCard(p,getcode,conn);
			}
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}				
	}
	//刷新卡牌信息
	public void RefreshCard(User p,String ccode,Connection conn) throws SQLException{
		String sql ="SELECT * FROM usercard"+p.getSvrcode()+" WHERE rolename=? AND SERVERNO=? AND cardcode=?"; //卡牌
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, ccode);
			rs = ps.executeQuery();
			while(rs.next()){
				UserCard mycard = new UserCard();
				mycard.setCardid(rs.getInt("cardid"));//玩家卡牌id
				mycard.setCardcode(rs.getString("cardcode"));
				mycard.setCardname(rs.getString("cardname")); //卡牌名称
				mycard.setCarddesc(rs.getString("carddesc")); //描述
				mycard.setCardprop(rs.getString("cardprop")) ;  //属性邪、萌、冷、霸
				mycard.setStarlevel(rs.getInt("starlevel")) ;  //星级
				mycard.setCardtype(rs.getString("cardtype")) ; //系列 同系2个以上有增益
				mycard.setUpexp(rs.getInt("upexp")) ;     //升级经验
				mycard.setCurexp(rs.getInt("curexp")) ;   //当前经验
				mycard.setCardlevel(rs.getInt("cardlevel"));//卡牌等级
				mycard.setCurvalue(rs.getInt("curvalue")) ;  //魅力值
				mycard.setBasehp(rs.getDouble("basehp")) ; //基础血成长
				mycard.setBaseatk(rs.getDouble("baseatk")) ;   //基础攻成长
				mycard.setBasedef(rs.getDouble("basedef")) ;   //基础防成长
				mycard.setAlterhp(rs.getDouble("alterhp")) ;   //基础转生血成长
				mycard.setAlteratk(rs.getDouble("alteratk")) ;  //基础转生攻成长
				mycard.setAlterdef(rs.getDouble("alterdef")) ;  //基础转生防成长
				mycard.setHp(rs.getInt("hp"));  //血量
				mycard.setAtk(rs.getInt("atk")) ; //攻
				mycard.setDef(rs.getInt("def")) ;  //防
				mycard.setCurstyle(rs.getInt("curstyle")) ;  //当前形态
				mycard.setIfuse(rs.getInt("ifuse")) ;   //是否阵容中
				mycard.setLocate(rs.getInt("locate")) ;   //阵容中位置
				mycard.setSetid(rs.getString("setid")) ; //套装code串
				mycard.setOrighp(rs.getInt("orighp"));//裸身血
				mycard.setOrigatk(rs.getInt("origatk"));//裸身攻
				mycard.setOrigdef(rs.getInt("origdef"));//裸身防
				mycard.setHavedevice(rs.getInt("havedevice"));//是否有装备
				mycard.setHaveskill(rs.getInt("haveskill")); //是否有技能
				mycard.setLivehp(rs.getInt("livehp")); //养成值
				mycard.setLiveatk(rs.getInt("liveatk"));
				mycard.setLivedef(rs.getInt("livedef"));
				mycard.setLifeskill(rs.getString("lifeskill"));
				mycard.setCurpointval(rs.getInt("curpointval"));//卡牌当前魅力点数
				if(!rs.getString("lifeskill").equals("@")) this.getCardSkill(p, mycard, "", "");
				p.getCardlist().put(mycard.getCardid(), mycard); //玩家全部卡牌			
			}
			rs.close();
			ps.close();
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}		
	}
	//处理掉落装备
	public void DownDevice(User p,String getcode,Connection conn) throws SQLException{
		PreparedStatement ps = null;
		//logger.info("掉落："+getcode);
		DeviceDict dd=GameManager.getInstance().getDevsetdict().get(getcode);
		String sql="INSERT INTO userdevice"+p.getSvrcode()+" VALUES(?,?,NULL,?,?,?,?,?,?,?,?,?,?,?,0,'@',0,?)";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, dd.getDevicecode());
			ps.setString(4,	dd.getDevicename());
			ps.setString(5,	dd.getDevicedesc());
			ps.setString(6, dd.getDevicetype());
			ps.setString(7, dd.getQuality());
			ps.setInt(8, 1);
			ps.setInt(9, dd.getDhp());
			ps.setInt(10, dd.getDatk());
			ps.setInt(11, dd.getDdef());
			ps.setInt(12,dd.getBaseprice());
			ps.setInt(13, dd.getBaseprice());
			ps.setInt(14, dd.getDevcolor());
			ps.executeUpdate();
			ps.close();
			RefreshDevice(p, getcode, conn);//刷新装备列表
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}		
	}
	//刷新装备
	public void RefreshDevice(User p,String dcode,Connection conn) throws SQLException{
		String sql ="SELECT * FROM userdevice"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND devicecode=?"; //卡牌上的装备
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3,dcode);
			rs = ps.executeQuery();
			while(rs.next()){
				UserDevice mydevice = new UserDevice();
				mydevice.setDeviceid(rs.getInt("deviceid")) ; //装备id
				mydevice.setDevicecode(rs.getString("devicecode"));
				mydevice.setDevicename(rs.getString("devicename")) ; //装备名称
				mydevice.setDevicedesc(rs.getString("devicedesc")) ; //描述
				mydevice.setDevicetype(rs.getString("devicetype")) ;  //类型 攻防饰
				mydevice.setQuality(rs.getString("quality")) ;  //质量quality
				mydevice.setGrade(rs.getInt("grade")) ;  //等级
				mydevice.setDhp(rs.getInt("dhp")) ;  //血增益
				mydevice.setDatk(rs.getInt("datk")) ;  //攻增益
				mydevice.setDdef(rs.getInt("ddef")) ;  //防增益
				mydevice.setBaseprice(rs.getInt("baseprice")) ;  //基础价
				mydevice.setPrice(rs.getInt("price")) ;  //实际价
				mydevice.setCardid(rs.getInt("cardid")) ;  //装备卡牌id
				mydevice.setMarkstring(rs.getString("markstring")) ; //符文组合串 空：无组合
				mydevice.setHavemark(rs.getInt("havemark")); //是否有符文
				mydevice.setDevcolor(rs.getInt("devcolor"));
				p.getUdevicelist().put(mydevice.getDeviceid(), mydevice); //记录用户所有装备		
			}
			rs.close();
			ps.close();
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}		
	}
	//处理掉落技能书页
	public void DownSkillPages(User p,String getcode,int ifupdate,int pageno,Connection conn) throws SQLException{
		SkillPages skp;
		String sql="";
		PreparedStatement ps = null;
		try{
			if(ifupdate>0){//更新
				skp=p.getSkillpagelist().get(getcode);
				switch(pageno){
				case 1:
					if(skp.getPage1()<0) skp.setPage1(0);
					skp.setPage1(skp.getPage1()+1);
					break;
				case 2:
					if(skp.getPage2()<0) skp.setPage2(0);
					skp.setPage2(skp.getPage2()+1);
					break;
				case 3:
					if(skp.getPage3()<0) skp.setPage3(0);
					skp.setPage3(skp.getPage3()+1);
					break;
				case 4:
					if(skp.getPage4()<0) skp.setPage4(0);
					skp.setPage4(skp.getPage4()+1);
					break;
				case 5:
					if(skp.getPage5()<0) skp.setPage5(0);
					skp.setPage5(skp.getPage5()+1);
					break;
				}
				sql="UPDATE userskillpage"+p.getSvrcode()+" SET page1=?,page2=?,page3=?,page4=?,page5=? WHERE rolename=? AND serverno=? AND skillcode=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, skp.getPage1());
				ps.setInt(2, skp.getPage2());
				ps.setInt(3, skp.getPage3());
				ps.setInt(4, skp.getPage4());
				ps.setInt(5, skp.getPage5());
				ps.setString(6,p.getUrole());
				ps.setString(7, p.getSvrcode());
				ps.setString(8, getcode);
				ps.executeUpdate();
				ps.close();
			}else{//新掉技能书页
				int page1,page2,page3,page4,page5;
				skp=GameManager.getInstance().getSkilldict().get(getcode);
				page1=skp.getPage1();
				if(page1>0) page1=0;
				page2=skp.getPage2();
				if(page2>0) page2=0;
				page3=skp.getPage3();
				if(page3>0) page3=0;
				page4=skp.getPage4();
				if(page4>0) page4=0;
				page5=skp.getPage5();
				if(page5>0) page5=0;
				switch(pageno){
				case 1:
					page1=1;
					break;
				case 2:
					page2=1;
					break;
				case 3:
					page3=1;
					break;
				case 4:
					page4=1;
					break;
				case 5:
					page5=1;
					break;
				}
				skp = GameManager.getInstance().getSkilldict().get(getcode);
				sql="INSERT INTO userskillpage"+p.getSvrcode()+" VALUES(?,?,?,?,?,?,?,?,?,?,?,1)";
				ps = conn.prepareStatement(sql);
				ps.setString(1,p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3, getcode);
				ps.setString(4, skp.getSkillname());
				ps.setString(5, skp.getSkdesc());
				ps.setString(6, skp.getSktype());
				ps.setInt(7, page1);
				ps.setInt(8, page2);
				ps.setInt(9, page3);
				ps.setInt(10, page4);
				ps.setInt(11, page5);
				ps.executeUpdate();
				ps.close();
				SkillPages sp=new SkillPages();
				sp.setSkillname(skp.getSkillname()); //技能名称
				sp.setSkillcode(skp.getSkillcode());
				sp.setPagenum(skp.getPagenum()) ;      //书页数量
				sp.setPage1(page1) ;        //书页1数量
				sp.setPage2(page2) ;  //书页2数量
				sp.setPage3(page3) ;	  //书页3数量
				sp.setPage4(page4) ;      //书页4数量
				sp.setPage5(page5) ;     //书页5数量  -1表示没有此书页
				sp.setSkdesc(skp.getSkdesc()) ;  //技能描述
				sp.setSktype(skp.getSktype()) ;  //技能类型 主动被动
				sp.setSkgrade(1) ;    //技能等级
				sp.setDiycustom(skp.getDiycustom()) ;  //修炼消耗
				sp.setAtks(skp.getAtks()) ;    //攻击增量 小于1表示百分比
				sp.setDefs(skp.getDefs()) ;
				sp.setHps(skp.getHps()) ;
				sp.setSvs(skp.getSvs()) ;       //增量系数
				sp.setHurt(skp.getHurt());
				sp.setSkillto(skp.getSkillto());
				sp.setSkillrand(skp.getSkillrand());
				sp.setUphurt(skp.getUphurt());
				sp.setUphurtper(skp.getUphurtper());
				sp.setHurtpert(skp.getHurtpert());
				sp.setSkillcolor(skp.getSkillcolor());
				p.getSkillpagelist().put(sp.getSkillcode(), sp);
			}
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}		
	}
		
	
	//计算卡牌获得经验
	public void CalcCardFin(User p,int expsum,Connection conn) throws SQLException{
		int upflag=0;
		Iterator<UserCard> it = p.getInusecards().values().iterator();
		CardLogicProc cplogic=new CardLogicProc();
		try{
			while(it.hasNext()){
				UserCard uc=it.next();			
				upflag=cplogic.CalcExp(uc, expsum);
				//logger.info("卡牌是否升级:"+upflag);
				if(upflag>0){
					cplogic.CalcCardOrign(uc);
					//重新穿装备及技能
					Iterator<UserDevice> its = uc.getDeviceMap().values().iterator();
					while(its.hasNext()){
						UserDevice udtemp=its.next();
						cplogic.LoadDev(p,uc,udtemp.getDeviceid());
					}
					Iterator<UserSkill> itk = uc.getSkillMap().values().iterator();
					while(itk.hasNext()){
						UserSkill uktemp=itk.next();
						cplogic.InstSkill(p,uc,uktemp.getSkillid(),uktemp.getSkillloc());
					}
					cplogic.checktype(p);//卡牌升级重新运算补正值
				}			
				CardUpdate(p, uc, conn);
			}
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}		
		
	}
	//计算关卡用户收益
	public void CalcUserFin(User p,UserMission um,int expsum,int monsum,Connection conn,Map<String,Object> smap) throws SQLException{
		int upg=p.getLevelup();
		int curexp=p.getExp();
		int curmp=0;
		int gnum=0,mnum=0;
		gnum=p.getGold();
		mnum=p.getMoney();
		if(expsum+curexp>=upg&&p.getLevel()<99){
			int newupexp=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getUpsum();
			int newmem=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMembers();
			int newpow=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMaxpower();
			int newper=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getPerpoint();
			p.setLevel(p.getLevel()+1);
			p.setExp(expsum+curexp-upg);
			p.setLevelup(newupexp);
			p.setMaxpower(newpow);
			p.setPerpoint(newper);
			p.setMembers(newmem);
			this.UserUpLevDown(p, conn, smap);
		}else{
			p.setExp(expsum+curexp);
		}
		p.setMissionsum(p.getMissionsum()+1);
		p.setMoney(p.getMoney()+monsum);
		if(um.getIfcopy()>0) curmp=um.getCopymp();
		else curmp=um.getMissionmp();
		if(p.getPower()<p.getMaxpower()){
			long cursecond=System.currentTimeMillis()/1000;
			if(p.getPowerstart()<=0)	p.setPowerstart((int)cursecond);
		}
		try{
			PlayerUpdate(p, conn,p.getGold()-gnum);
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}
		
	}
	//计算夺技用户收益
	public void CalcUserFin(User p,int expsum,int monsum,Connection conn,Map<String,Object> smap) throws SQLException{
		int upg=p.getLevelup();
		int curexp=p.getExp();
		int gnum=0,mnum=0;
		gnum=p.getGold();
		mnum=p.getMoney();
		if(expsum+curexp>=upg&&p.getLevel()<99){
			int newupexp=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getUpsum();
			int newmem=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMembers();
			int newpow=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMaxpower();
			int newper=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getPerpoint();
			p.setLevel(p.getLevel()+1);
			p.setExp(expsum+curexp-upg);
			p.setLevelup(newupexp);
			p.setMaxpower(newpow);
			p.setPerpoint(newper);
			p.setMembers(newmem);
			this.UserUpLevDown(p, conn, smap);
		}else{
			p.setExp(expsum+curexp);
		}
		p.setGetbook(p.getGetbook()-1);
		if(p.getGetbook()<10){
			long cursecond=System.currentTimeMillis()/1000;
			if(p.getGetskstart()<=0) p.setGetskstart((int)cursecond);
		}		
		p.setMoney(p.getMoney()+monsum);
		try{
			PlayerUpdate(p, conn,p.getGold()-gnum);
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}
	}
	//计算决斗用户收益
	public void CalcUserFightFin(User p,int expsum,Connection conn,Map<String,Object> smap) throws SQLException{
		int upg=p.getLevelup();
		int curexp=p.getExp();
		int gnum=0,mnum=0;
		gnum=p.getGold();
		mnum=p.getMoney();
		if(expsum+curexp>=upg&&p.getLevel()<99){
			int newupexp=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getUpsum();
			int newmem=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMembers();
			int newpow=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMaxpower();
			int newper=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getPerpoint();
			p.setLevel(p.getLevel()+1);
			p.setExp(expsum+curexp-upg);
			p.setLevelup(newupexp);
			p.setMaxpower(newpow);
			p.setPerpoint(newper);
			p.setMembers(newmem);
			this.UserUpLevDown(p, conn, smap);
		}else{
			p.setExp(expsum+curexp);
		}
		try{
			PlayerUpdate(p, conn,p.getGold()-gnum);
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}
		
	}
	//计算街霸用户收益
		public void CalcUserStreetFin(User p,int expsum,Connection conn,Map<String,Object> smap) throws SQLException{
			int upg=p.getLevelup();
			int curexp=p.getExp();
			int gnum=0,mnum=0;
			gnum=p.getGold();
			mnum=p.getMoney();
			if(expsum+curexp>=upg&&p.getLevel()<99){
				int newupexp=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getUpsum();
				int newmem=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMembers();
				int newpow=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMaxpower();
				int newper=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getPerpoint();
				p.setLevel(p.getLevel()+1);
				p.setExp(expsum+curexp-upg);
				p.setLevelup(newupexp);
				p.setMaxpower(newpow);
				p.setPerpoint(newper);
				p.setMembers(newmem);
				this.UserUpLevDown(p, conn, smap);
			}else{
				p.setExp(expsum+curexp);
			}
			try{
				PlayerUpdate(p, conn,p.getGold()-gnum);
			}catch(SQLException e){
				logger.info(e.getMessage());
				throw e;
			}
		}
	//计算好友PK收益
		public void CalcUserFriendFin(User p,int expsum,int monsum,Connection conn,Map<String,Object> smap) throws SQLException{
			int upg=p.getLevelup();
			int curexp=p.getExp();
			int gnum=0,mnum=0;
			gnum=p.getGold();
			mnum=p.getMoney();
			if(expsum+curexp>=upg&&p.getLevel()<99){
				int newupexp=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getUpsum();
				int newmem=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMembers();
				int newpow=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getMaxpower();
				int newper=GameManager.getInstance().getUseruplevel().get(p.getLevel()+1).getPerpoint();
				p.setLevel(p.getLevel()+1);
				p.setExp(expsum+curexp-upg);
				p.setLevelup(newupexp);
				p.setMaxpower(newpow);
				p.setPerpoint(newper);
				p.setMembers(newmem);
				this.UserUpLevDown(p, conn, smap);
			}else{
				p.setExp(expsum+curexp);
			}
			p.setMoney(p.getMoney()+monsum);
			try{				
				PlayerUpdate(p, conn,p.getGold()-gnum);
			}catch(SQLException e){
				logger.info(e.getMessage());
				throw e;
			}
		}
	//记录用户升级获取奖励
	public void UserUpLevDown(User p,Connection conn,Map<String,Object> smap)  throws SQLException{
		UpLevelDict uld = GameManager.getInstance().getUplvldict().get(p.getLevel());
		smap.put("EA", uld.getDownmoney());
		smap.put("EB", uld.getDowngold());
		p.setMoney(p.getMoney()+uld.getDownmoney());
		p.setGold(p.getGold()+uld.getDowngold());
		String downname="";
		String downcode=uld.getDowncode();
		try{
			if(GameManager.getInstance().getGamelevel()>0&&p.getLevel()==GameManager.getInstance().getGamelevel()){//达到要求的等级记录到useract
				this.UpdateUserAct(p, 4, "冲级", p.getLevel(),conn);
			}
			smap.put("LR", 0);
		switch(uld.getGoodstype()){
		case 0://0无
			smap.put("EC", "@");
			smap.put("ED", "@");
			smap.put("EE", -1);
			smap.put("EF", 1);
			break;
		case 1://1装备
			if(!downcode.equals("@")){
				downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				DownDevice(p,downcode,conn);
				smap.put("EC", downcode);
				smap.put("ED", downname);
				smap.put("EE", 0);
				smap.put("EF", 1);
				smap.put("LR",GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor());
			}			
			break;
		case 2://2技能书页
			if(!downcode.equals("@")){
				downname=GameManager.getInstance().getSkilldict().get(downcode).getSkillname();
				int pagenum=GameManager.getInstance().getSkilldict().get(downcode).getPagenum();
				int pageno=CommTools.randomval(1, pagenum);
				int ifupdate=0;
				if(p.getSkillpagelist().containsKey(downcode)) ifupdate=1; 
				this.DownSkillPages(p, downcode, ifupdate, pageno, conn);
				smap.put("EC", downcode);
				smap.put("ED", downname);
				smap.put("EE", pageno);
				smap.put("EF", 1);
				smap.put("LR",GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor());
			}			
			break;
		case 3://3道具
			if(!downcode.equals("@")){
				downname=GameManager.getInstance().getPropsdict().get(downcode).getPropsname();
				this.DownProps(p, downcode, conn, uld.getDownnum());
				smap.put("EC", downcode);
				smap.put("ED", downname);
				smap.put("EE", 0);
				smap.put("EF", uld.getDownnum());
			}			
			break;
		case 4://4符文
			if(!downcode.equals("@")){
				downname=GameManager.getInstance().getMarkdict().get(downcode).getMarkname();
				this.DownMark(p, downcode, conn);
				smap.put("EC", downcode);
				smap.put("ED", downname);
				smap.put("EE", 0);
				smap.put("EF", 1);
			}			
			break;
		case 5://5技能
			if(!downcode.equals("@")){
				downname=GameManager.getInstance().getSkilldict().get(downcode).getSkillname();
				this.DownSkill(p, downcode, conn);
				smap.put("EC", downcode);
				smap.put("ED", downname);
				smap.put("EE", 0);
				smap.put("EF", 1);
				smap.put("LR",GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor());
			}			
			break;
		case 6://转生丹
			if(!downcode.equals("@")){
				downname=GameManager.getInstance().getBallname();
				downcode="P902";
				p.setBall(p.getBall()+uld.getDownnum());
				this.UpdateUserAct(p, 3, "升级获得"+GameManager.getInstance().getBallname(), uld.getDownnum(), conn);
				smap.put("EC", downcode);
				smap.put("ED", downname);
				smap.put("EE", 0);
				smap.put("EF", uld.getDownnum());
			}			
			break;
		}
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}		
	}
	//新手引导未完成处理
	public void newuserreback(User p) throws SQLException{
		Connection conn = pool.getConnection();
		CallableStatement cs = null;
		try{
			cs = conn.prepareCall("{call NEWUSERBACK(?,?,?,?)}");
			cs.setString(1,p.getUname());
			cs.setString(2,p.getUrole());
			cs.setString(3,p.getSvrcode());
			cs.registerOutParameter(4, Types.INTEGER);
			cs.execute();
			if(cs.getInt(4)==0){
				cs.close();
				throw new SQLException("newuserback error");
			}	
			cs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
		
	}
	//玩家退出处理
	public void PlayerExitUpdate(User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		//logger.info("用户退出更新数据");
		try{
			conn.setAutoCommit(false);
			/*String sql="UPDATE useronline SET offtime=? WHERE rolename=? AND serverno=? AND currlogin=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, (int)(System.currentTimeMillis()/1000));
			ps.setString(2, p.getUrole());
			ps.setString(3, p.getSvrcode());
			ps.setString(4, p.getCurrlogin());
			ps.executeUpdate();
			ps.close();*/
			String sql="UPDATE userroletbl SET power=?,powerstart=?,getbook=?,getskstart=? WHERE username=? AND rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, p.getPower());
			ps.setInt(2, p.getPowerstart());
			ps.setInt(3, p.getGetbook());
			ps.setInt(4, p.getGetskstart());
			ps.setString(5, p.getUname());
			ps.setString(6, p.getUrole());
			ps.setString(7, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
		
	}
	//获取技能列表
	public void getSkillList(User p,String skcode,int pageno,Map<String,Object> smap) throws SQLException{
		String addsql="";
		switch(pageno){
		case 1:
			addsql=" AND t1.page1>0 ORDER BY RAND() LIMIT 7";
			break;
		case 2:
			addsql=" AND t1.page2>0 ORDER BY RAND() LIMIT 7";
			break;
		case 3:
			addsql=" AND t1.page3>0 ORDER BY RAND() LIMIT 7";
			break;
		case 4:
			addsql=" AND t1.page4>0 ORDER BY RAND() LIMIT 7";
			break;
		case 5:
			addsql=" AND t1.page5>0 ORDER BY RAND() LIMIT 7";
			break;
		}
		Connection conn = pool.getConnection();
		String sql ="SELECT t1.rolename,t2.level FROM userskillpage"+p.getSvrcode()+" t1,userroletbl t2 "+
		"WHERE t1.rolename=t2.rolename AND t1.serverno=t2.serverno "+
		" AND t1.rolename<>? AND t1.serverno=? AND t1.skillcode=? AND t2.level>=? AND t2.level <=? "; //-5 2级的玩家
		sql=sql+addsql;
		String sql1="";
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		int minp=p.getLevel()-5;
		int maxp=p.getLevel()+2;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, skcode);
			ps.setInt(4, minp);
			ps.setInt(5, maxp);
			rs = ps.executeQuery();
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>(); 
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("AB", rs.getString("rolename"));
				map.put("AC", rs.getInt("level"));
				sql1="SELECT cardcode,starlevel FROM usercard"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND ifuse=1 ORDER BY starlevel";
				ps1 = conn.prepareStatement(sql1);
				ps1.setString(1,rs.getString("rolename"));
				ps1.setString(2, p.getSvrcode());
				rs1=ps1.executeQuery();
				int temp=0;
				ArrayList<Map<String,Object>> tl=new ArrayList<Map<String,Object>>(); 
				while(rs1.next()){
					Map<String,Object> tempmap=new HashMap<String,Object>();
					temp=temp+1;
					tempmap.put("BB", rs1.getString("cardcode"));
					switch(rs1.getInt("starlevel")){
					case 1:
					case 2:
						tempmap.put("BR", 1);
						break;
					case 3:
					case 4:
						tempmap.put("BR", 2);
						break;
					case 5:
						tempmap.put("BR", 3);
						break;
					case 6:
						tempmap.put("BR", 4);
						break;
					case 7:
						tempmap.put("BR", 5);
						break;
					}
					tl.add(tempmap);
					if(temp>3) break;
				}
				rs1.close();
				ps1.close();
				map.put("AD", tl.toArray());
				al.add(map);
			}
			Collections.sort(al,new Comparator<Map<String,Object>>(){
				public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {  
					return Integer.parseInt(arg1.get("AC").toString())-Integer.parseInt(arg0.get("AC").toString()); }  

			});
			smap.put("AA", al.toArray());
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	//获取用户阵容卡牌
	public void GetUserCard(String rolename,String svrcode,ArrayList<UserCard> enl) throws SQLException{
		Connection conn = pool.getConnection();
		String sql ="SELECT * FROM usercard"+svrcode+" WHERE rolename=? AND serverno=? AND ifuse=1 AND locate>=0 ORDER BY locate"; //对战玩家阵容卡牌
		PreparedStatement ps = null;
		ResultSet rs = null;
		int i=0;
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, rolename);
			ps.setString(2,svrcode);
			rs = ps.executeQuery(); 
			while(rs.next()){
				i=i+1;
				if(i>6) break;
				UserCard mycard=new UserCard();
				mycard.setCardid(rs.getInt("cardid"));//玩家卡牌id
				mycard.setCardcode(rs.getString("cardcode"));
				mycard.setCardname(rs.getString("cardname")); //卡牌名称
				mycard.setCarddesc(rs.getString("carddesc")); //描述
				mycard.setCardprop(rs.getString("cardprop")) ;  //属性邪、萌、冷、霸
				mycard.setStarlevel(rs.getInt("starlevel")) ;  //星级
				mycard.setCardtype(rs.getString("cardtype")) ; //系列 同系2个以上有增益
				mycard.setUpexp(rs.getInt("upexp")) ;     //升级经验
				mycard.setCurexp(rs.getInt("curexp")) ;   //当前经验
				mycard.setCardlevel(rs.getInt("cardlevel"));//卡牌等级
				mycard.setCurvalue(rs.getInt("curvalue")) ;  //魅力值
				mycard.setBasehp(rs.getDouble("basehp")) ; //基础血成长
				mycard.setBaseatk(rs.getDouble("baseatk")) ;   //基础攻成长
				mycard.setBasedef(rs.getDouble("basedef")) ;   //基础防成长
				mycard.setAlterhp(rs.getDouble("alterhp")) ;   //基础转生血成长
				mycard.setAlteratk(rs.getDouble("alteratk")) ;  //基础转生攻成长
				mycard.setAlterdef(rs.getDouble("alterdef")) ;  //基础转生防成长
				mycard.setHp(rs.getInt("hp"));  //血量
				mycard.setAtk(rs.getInt("atk")) ; //攻
				mycard.setDef(rs.getInt("def")) ;  //防
				mycard.setCurstyle(rs.getInt("curstyle")) ;  //当前形态
				mycard.setIfuse(rs.getInt("ifuse")) ;   //是否阵容中
				mycard.setLocate(rs.getInt("locate")) ;   //阵容中位置
				mycard.setSetid(rs.getString("setid")) ; //套装code串
				mycard.setOrighp(rs.getInt("orighp"));//裸身血
				mycard.setOrigatk(rs.getInt("origatk"));//裸身攻
				mycard.setOrigdef(rs.getInt("origdef"));//裸身防
				mycard.setHavedevice(rs.getInt("havedevice"));//是否有装备
				mycard.setHaveskill(rs.getInt("haveskill")); //是否有技能
				mycard.setLivehp(rs.getInt("livehp")); //养成值
				mycard.setLiveatk(rs.getInt("liveatk"));
				mycard.setLivedef(rs.getInt("livedef"));
				mycard.setCurhp(rs.getInt("hp"));  //战斗血量
				mycard.setCuratk(rs.getInt("atk")) ; //战斗攻
				mycard.setCurdef(rs.getInt("def")) ;  //战斗防
				getEnCardSkill(rolename,svrcode,mycard);//获取技能
				enl.add(mycard);
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	//获取对方卡牌技能列表
	public void getEnCardSkill(String rolename,String svrcode,UserCard uc) throws SQLException{
		String sql ="SELECT * FROM userskill"+svrcode+" WHERE rolename=? AND serverno=? AND cardid=?"; //卡牌上技能
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, rolename);
			ps.setString(2, svrcode);
			ps.setInt(3, uc.getCardid());
			rs = ps.executeQuery();
			while(rs.next()){
				UserSkill cardskill = new UserSkill();
				cardskill.setSkillid(rs.getInt("skillid")) ;  //技能id
				cardskill.setSkillcode(rs.getString("skillcode"));
				cardskill.setSkillname(rs.getString("skillname"))  ;   //技能名称
				cardskill.setSkilldesc(rs.getString("skilldesc")) ;   //技能描述
				cardskill.setSkilltype(rs.getString("skilltype")) ;   //技能类型 主动、被动
				cardskill.setSkillgrade(rs.getInt("skillgrade")) ;    // 技能等级
				cardskill.setIflife(rs.getInt("iflife"));
				SkillPages skp=GameManager.getInstance().getSkilldict().get(cardskill.getSkillcode());
				cardskill.setShp(skp.getHps()) ; //增血
				cardskill.setSatk(skp.getAtks()) ; //增攻
				cardskill.setSdef(skp.getDefs()) ; //增防
				cardskill.setCardid(rs.getInt("cardid")) ;  //装备卡id
				cardskill.setHurt(skp.getHurt());//装备上的主动伤害，伤害附加值
				cardskill.setSvs(skp.getSvs());
				cardskill.setSkillto(skp.getSkillto());
				cardskill.setSkillrand(skp.getSkillrand());
				cardskill.setHurtpert(skp.getHurtpert());//伤害百分比
				cardskill.setUphurt(skp.getUphurt());//升级伤害附加值增长百分比
				cardskill.setUphurtper(skp.getUphurtper());//升级伤害百分比增长百分比
				uc.getSkillMap().put(cardskill.getSkillid(), cardskill); //记录卡牌技能列表
				
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	//任务完成更新任务 用户类，用户关卡任务类，输赢标志，回包map，链接
	public void UpdateMission(User p, UserMission um,int wlflag,Map<String,Object> semap,Connection conn) throws SQLException{
		String sql="";
		PreparedStatement ps = null;
		try{
			if(wlflag>0){//胜利可以进入向下一关卡或场景
				um.setIfcopy(1);	
				if(um.getMissionseq()<um.getMissions()){//本场景未打完，更新下一关卡为可用
					semap.put("AO", um.getNextmiss());
					semap.put("AP", 0);
				}else{//本场景打完更新为副本关，开启新场景，记录是否3星通关
					semap.put("AO", um.getNextmiss());
					int newseon=GameManager.getInstance().getMissionscene().get(um.getNextmiss());
					semap.put("AP", newseon);
				}
				if(p.getUmisson().get(um.getNextmiss()).getIfcanuse()<=0){//下一关未打过
					p.getUmisson().get(um.getNextmiss()).setIfcanuse(1);
					sql="UPDATE usermission"+p.getSvrcode()+" SET ifcanuse=1,misssum=? WHERE rolename=? AND serverno=? AND missionid=?";
					ps=conn.prepareStatement(sql);
					ps.setInt(1, 0);
					ps.setString(2, p.getUrole());
					ps.setString(3, p.getSvrcode());
					ps.setInt(4, um.getNextmiss());
					ps.executeUpdate();
					ps.close();
				}			
			}else{//输了还在同一场景同一任务关卡
				semap.put("AO",0);
				semap.put("AP",0);	
			}
			semap.put("AQ", um.getCurnum());
			semap.put("AY", um.getIfcopy());
			sql ="UPDATE usermission"+p.getSvrcode()+" SET curtime=?,winlose=?,assess=?,curnum=?,ifcopy=? WHERE rolename=? AND serverno=? AND missionid=?"; //更新任务
			ps=conn.prepareStatement(sql);
			ps.setString(1, dateToString(new Date()));
			ps.setInt(2, wlflag);
			ps.setString(3, um.getAssess());
			ps.setInt(4, um.getCurnum());
			ps.setInt(5, um.getIfcopy());
			ps.setString(6, p.getUrole());
			ps.setString(7, p.getSvrcode());
			ps.setInt(8, um.getMissionid());
			ps.executeUpdate();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
	}
	//检查是否三星通关及获得奖励  用户 场景号，当前任务
	public int CheckMissPass(User p,UserMission um) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int flag=0;
		String sql="SELECT * FROM userpass"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND sceneno=? AND ifpass=1";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, um.getSceneno());
			rs=ps.executeQuery();
			if(rs.next()) flag=1;//已通关 
			rs.close();
			ps.close();
			if(flag<1){
				int sumflag=0;
				sql="SELECT count(1) counsum FROM usermission"+p.getSvrcode()+" t1,missiondict t2 where t1.missionid=t2.missionid AND t1.assess=3 AND t2.sceneno=?  AND t1.rolename=? AND t1.serverno=?"; 
				ps=conn.prepareStatement(sql);
				ps.setInt(1, um.getSceneno());
				ps.setString(2, p.getUrole());
				ps.setString(3, p.getSvrcode());
				rs=ps.executeQuery();
				if(rs.next()) sumflag=rs.getInt("counsum");
				rs.close();
				ps.close();
				if(sumflag==8){//通关
					flag=1;
					sql="UPDATE userpass"+p.getSvrcode()+" SET ifpass=1 WHERE rolename=? AND serverno=? AND sceneno=?";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setInt(3, um.getSceneno());
					ps.executeUpdate();
					ps.close();
				}
			}
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}finally{
			conn.close();
		}
		return flag;	
	}
	//决斗列表
	public void getUserFightList(User p,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		int currank=0,calflag=0;
		String sql1="";
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
		try{
			sql1="SELECT rankloc FROM userpkrank"+p.getSvrcode()+" WHERE rolename=? AND serverno=?";
			ps1=conn.prepareStatement(sql1);
			ps1.setString(1, p.getUrole());
			ps1.setString(2, p.getSvrcode());
			rs1=ps1.executeQuery();
			if(rs1.next()){
				p.setRankseq(rs1.getInt("rankloc"));
			}else p.setRankseq(0);
			rs1.close();
			ps1.close();
			conn.setAutoCommit(false);
			if(p.getRankseq()<=0){
				//sql1="INSERT INTO userpkrank VALUES(NULL,?,?,1,?)";
				sql1="INSERT INTO userpkrank"+p.getSvrcode()+" SELECT max(rankloc)+1,?,?,1,? FROM userpkrank"+p.getSvrcode()+" WHERE serverno=?";
				ps1=conn.prepareStatement(sql1);
				ps1.setString(1, p.getSvrcode());
				ps1.setString(2,p.getUrole());
				ps1.setInt(3, p.getLevel());
				ps1.setString(4, p.getSvrcode());
				ps1.executeUpdate();
				ps1.close();
				sql1="SELECT rankloc FROM userpkrank"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND loctype=1";
				ps1=conn.prepareStatement(sql1);
				ps1.setString(1,p.getUrole());
				ps1.setString(2, p.getSvrcode());
				rs1=ps1.executeQuery();
				if(rs1.next()){
					currank=rs1.getInt("rankloc");
				}
				ps1.close();
				rs1.close();
				sql1="UPDATE userroletbl SET currank=? WHERE username=? AND rolename=? AND serverno=?";
				ps1=conn.prepareStatement(sql1);
				ps1.setInt(1,currank);
				ps1.setString(2,p.getUname());
				ps1.setString(3,p.getUrole());
				ps1.setString(4, p.getSvrcode());
				ps1.executeUpdate();
				ps1.close();
				p.setRankseq(currank);
			}
			currank=p.getRankseq();
			PreparedStatement ps = null;
			String sql ="";
			ResultSet rs = null;
			if(currank<=10)	calflag=0;
			else if(currank<=100) calflag=1;
			else if(currank<500) calflag=3;
			else if(currank<2000) calflag=6;
			else calflag=30;
			if(calflag>0) {
				//可决斗列表
				sql="SELECT * FROM userpkrank"+p.getSvrcode()+" WHERE serverno=? AND rankloc IN (1,2,3,?,?,?,?,?,?,?) ORDER BY rankloc";
				ps=conn.prepareStatement(sql);
				for(int i=0;i<7;i++){
					ps.setInt(i+2,currank-calflag*i);
				}
			}else{//前十名
				sql="SELECT * FROM userpkrank"+p.getSvrcode()+" WHERE serverno=? AND rankloc<=? ORDER BY rankloc";
				ps=conn.prepareStatement(sql);
				ps.setInt(2, currank);
			}
			ps.setString(1, p.getSvrcode());
			rs=ps.executeQuery();
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				String rolename=rs.getString("rolename");
				int utype=rs.getInt("loctype");
				map.put("AB", rolename);
				map.put("AC",rs.getInt("rankloc"));
				map.put("AE", utype);
				map.put("AH", rs.getInt("curgrade"));
				if(utype==1){//玩家
					/*sql1="SELECT level FROM userroletbl WHERE rolename=? AND serverno=?";
					ps1 = conn.prepareStatement(sql1);
					ps1.setString(1,rolename);
					ps1.setString(2, p.getSvrcode());
					rs1=ps1.executeQuery();
					if(rs1.next()){
						map.put("AH", rs1.getInt("level"));
					}
					ps1.close();
					rs1.close();*/
					sql1="SELECT cardcode,starlevel FROM usercard"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND ifuse=1 ORDER BY starlevel";
					ps1 = conn.prepareStatement(sql1);
					ps1.setString(1,rolename);
					ps1.setString(2, p.getSvrcode());
					rs1=ps1.executeQuery();
					int temp=0;
					ArrayList<Map<String,Object>> tl=new ArrayList<Map<String,Object>>(); 
					while(rs1.next()){
						Map<String,Object> tempmap=new HashMap<String,Object>();
						temp=temp+1;
						tempmap.put("BB", rs1.getString("cardcode"));
						switch(rs1.getInt("starlevel")){
						case 1:
						case 2:
							tempmap.put("BR", 1);
							break;
						case 3:
						case 4:
							tempmap.put("BR", 2);
							break;
						case 5:
							tempmap.put("BR", 3);
							break;
						case 6:
							tempmap.put("BR", 4);
							break;
						case 7:
							tempmap.put("BR", 5);
							break;
						}
						tl.add(tempmap);
						if(temp>=3) break;
					}
					rs1.close();
					ps1.close();
					map.put("AD", tl.toArray());
					al.add(map);
				}else{//npc占坑
					sql1="SELECT npcid FROM pknpc WHERE rolename=? ORDER BY npcloc";
					ps1 = conn.prepareStatement(sql1);
					ps1.setString(1,rolename);
					rs1=ps1.executeQuery();
					int temp=0;
					ArrayList<Map<String,Object>> tl=new ArrayList<Map<String,Object>>(); 
					while(rs1.next()){
						Map<String,Object> tempmap=new HashMap<String,Object>();
						temp=temp+1;
						String npccode=GameManager.getInstance().getCnpcdict().get(rs1.getInt("npcid")).getNpccode();
						tempmap.put("BB", npccode);
						if(!npccode.substring(0,1).equals("L")){
							switch(GameManager.getInstance().getCarddict().get(npccode).getStarlevel()){
							case 1:
							case 2:
								tempmap.put("BR", 1);
								break;
							case 3:
							case 4:
								tempmap.put("BR", 2);
								break;
							case 5:
								tempmap.put("BR", 3);
								break;
							case 6:
								tempmap.put("BR", 4);
								break;
							case 7:
								tempmap.put("BR", 5);
								break;
							}
						}
						else tempmap.put("BR", 1);
						tl.add(tempmap);
						if(temp>3) break;
					}
					rs1.close();
					ps1.close();
					map.put("AD", tl.toArray());
					al.add(map);
				}
			}
			
			smap.put("AA", al.toArray());
			smap.put("AF", p.getPknum());
			smap.put("AG", p.getCurpoints());
			smap.put("AH", p.getLevel());
			smap.put("AK", p.getRankseq());
			rs.close();
			ps.close();
			sql="SELECT curpoints FROM userroletbl WHERE username=? AND rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUname());
			ps.setString(2, p.getUrole());
			ps.setString(3, p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				p.setCurpoints(rs.getInt("curpoints"));
			}
			ps.close();
			rs.close();
			conn.commit();
			conn.setAutoCommit(true);
			smap.put("AG", p.getCurpoints());
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	//反击列表
	public void getUserFightBackList(User p,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		int currank=0,calflag=0;
		String sql1="";
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
		try{
			String sql ="";
			PreparedStatement ps = null;
			ResultSet rs = null;
			sql="SELECT rankloc FROM userpkrank"+p.getSvrcode()+" WHERE rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()) {
				currank=rs.getInt("rankloc");
				p.setRankseq(currank);
			}
			ps.close();
			rs.close();
			//获取曾击败我的
			sql="SELECT t1.rolename,t1.rankloc,t2.curgrade FROM userpkrank"+p.getSvrcode()+" t1,userfight"+p.getSvrcode()+" t2 WHERE t1.rolename=t2.winrole AND t1.serverno=t2.serverno AND t1.loctype=1 "+
			"AND t1.rankloc<? AND t2.rolename=? AND t2.serverno=? ORDER BY t1.rankloc,RAND() LIMIT 5";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, currank);
			ps.setString(2, p.getUrole());
			ps.setString(3, p.getSvrcode());
			rs=ps.executeQuery();
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("AB", rs.getString("rolename"));//曾击败我
				map.put("AC",rs.getInt("rankloc"));
				map.put("AE", 1);
				map.put("AH", rs.getInt("curgrade"));
				sql1="SELECT cardcode,starlevel FROM usercard"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND ifuse=1";
				ps1 = conn.prepareStatement(sql1);
				ps1.setString(1,rs.getString("rolename"));
				ps1.setString(2, p.getSvrcode());
				rs1=ps1.executeQuery();
				int temp=0;
				ArrayList<Map<String,Object>> tl=new ArrayList<Map<String,Object>>(); 
				while(rs1.next()){
					Map<String,Object> tempmap=new HashMap<String,Object>();
					temp=temp+1;
					tempmap.put("BB", rs1.getString("cardcode"));
					switch(rs1.getInt("starlevel")){
					case 1:
					case 2:
						tempmap.put("BR", 1);
						break;
					case 3:
					case 4:
						tempmap.put("BR", 2);
						break;
					case 5:
						tempmap.put("BR", 3);
						break;
					case 6:
						tempmap.put("BR", 4);
						break;
					case 7:
						tempmap.put("BR", 5);
						break;
					}
					tl.add(tempmap);
					if(temp>=3) break;
				}
				rs1.close();
				ps1.close();
				map.put("AD", tl.toArray());
				al.add(map);
			}
			rs.close();
			ps.close();
			smap.put("AA", al.toArray());
			smap.put("AF", p.getPknum());
			smap.put("AG", p.getCurpoints());
			smap.put("AH", p.getLevel());
			smap.put("AK", p.getRankseq());
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}	
	public void getNpcList(String rolen,ArrayList<CMissionNpcDict> al) throws SQLException{
		//占位NPC字典
		Connection conn = pool.getConnection();
		String sql1="SELECT * FROM pknpc t1,npcdict t2 where t1.npcid=t2.npcid and rolename=? ORDER BY t1.npcloc";
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try {
			int i=0;
			ps1 = conn.prepareStatement(sql1);
			ps1.setString(1,rolen );
			rs1 = ps1.executeQuery();
			while(rs1.next()){
				i=i+1;
				if(i>6) break;
				CMissionNpcDict cnd = new CMissionNpcDict();
				cnd.setMissionid(-1);
				cnd.setNpcid(rs1.getInt("npcid"));
				cnd.setNpcprop(rs1.getString("npcprop"));
				cnd.setNpcgrade(rs1.getInt("npcgrade"));
				cnd.setNpctype(rs1.getString("npctype"));
				cnd.setNpccode(rs1.getString("npccode"));
				cnd.setHP(rs1.getInt("hp"));
				cnd.setATK(rs1.getInt("atk"));
				cnd.setDEF(rs1.getInt("def"));
				cnd.setHpalter(rs1.getInt("hpalter"));
				cnd.setAtkalter(rs1.getInt("atkalter"));
				cnd.setDefalter(rs1.getInt("defalter"));
				cnd.setSkillcode(rs1.getString("skillcode"));
				cnd.setSkillgrade(rs1.getInt("skillgrade"));
				cnd.setNpcloc(rs1.getInt("npcloc"));//位置
				al.add(cnd);
			}		
			rs1.close();
			ps1.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	public void getUserCurrank(User p) throws SQLException {
		Connection conn = pool.getConnection();
		String sql="";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			sql="SELECT rankloc FROM userpkrank"+p.getSvrcode()+" WHERE rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				p.setRankseq(rs.getInt("rankloc"));
			}
			rs.close();
			ps.close();
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	//获取选中决斗的角色当前排名
	public int CheckFightUser(User p,String fightrole,int rankloc) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		int flag=0;
		if(p.getRankseq()<=rankloc) return 0;//当前排名高于对手，不打
		String sql="SELECT rolename FROM userpkrank"+p.getSvrcode()+" WHERE rankloc=? AND serverno=?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setInt(1, rankloc);
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				if(rs.getString("rolename").equals(fightrole)) flag=1;
			}
			ps.close();
			rs.close();
			return flag;
		}catch (SQLException e){
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	//决斗结束，胜负标志；用户；对方排名；对方角色名，对方类型,经验，钱，包，对方等级
	public void FinishFight(int wlflag,User p,int rseq,String rolename,int ftype,int exp,int mon,Map<String,Object> smap,int olevel) throws SQLException{
		Connection conn = pool.getConnection();
		ResultSet rs=null;
		p.setPknum(p.getPknum()-1);
		String sql="";
		int myrank=p.getRankseq();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			if(wlflag>0){//胜
				sql="UPDATE userpkrank"+p.getSvrcode()+" SET rolename=?,loctype=1,curgrade=? WHERE rankloc=? AND serverno=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setInt(2, p.getLevel());
				ps.setInt(3, rseq);				
				ps.setString(4, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
				if(ftype>0) sql="UPDATE userpkrank"+p.getSvrcode()+" SET rolename=?,curgrade=? WHERE rankloc=? AND serverno=?"; //对方玩家  
				else sql="UPDATE userpkrank"+p.getSvrcode()+" SET rolename=?,loctype=0,curgrade=? WHERE rankloc=? AND serverno=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, rolename);
				ps.setInt(2, olevel);
				ps.setInt(3, myrank);
				ps.setString(4, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
				if(ftype>0) {
					sql="UPDATE userroletbl SET currank=? WHERE rolename=? AND serverno=? ";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, myrank);
					ps.setString(2, rolename);
					ps.setString(3, p.getSvrcode());
					ps.executeUpdate();
					ps.close();
					sql="DELETE FROM userfight"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND winrole=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, rolename);
					ps.executeUpdate();
					ps.close();
					int winsum=0;
					sql="SELECT COUNT(1) fnum FROM userfight"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND winrole=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, rolename);
					ps.setString(2, p.getSvrcode());
					ps.setString(3, p.getUrole());
					rs=ps.executeQuery();
					if(rs.next()) winsum=rs.getInt("fnum");
					rs.close();
					ps.close();
					if(winsum==0){
						sql="INSERT INTO userfight"+p.getSvrcode()+" VALUES(?,?,?,?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1,rolename);
						ps.setString(2, p.getSvrcode());
						ps.setString(3, p.getUrole());
						ps.setInt(4, p.getLevel());
						ps.executeUpdate();
						ps.close();
						//取消发送邮件
						/*sql="INSERT INTO usermailbox VALUES(NULL,?,?,'决斗失败',?,'@',0,'@',0,0,0,0,?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1,rolename);
						ps.setString(2, p.getSvrcode());
						ps.setString(3, "玩家--"+p.getUrole()+"--在决斗中把你打得满地找牙，赶紧重整旗鼓打回去，失去的面子一定要夺回来！");
						ps.setInt(4, (int)(System.currentTimeMillis()/1000));
						ps.executeUpdate();
						ps.close();*/
					}
				}				
				p.setRankseq(rseq);
				p.setMoney(p.getMoney()+mon);
				CalcCardFin(p, exp, conn);
				CalcUserFightFin(p, exp, conn,smap);
			}else{
				sql="UPDATE userroletbl SET playerpknum=? WHERE username=? AND rolename=? AND serverno=? ";
				ps = conn.prepareStatement(sql);
				ps.setInt(1,p.getPknum());
				ps.setString(2, p.getUname());
				ps.setString(3,p.getUrole());
				ps.setString(4, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
			}
			UpdateDayMission(p,"决斗",conn);
			this.UpdateUserAct(p, 0, "决斗",1, conn);
			conn.commit();
			conn.setAutoCommit(true);
			if(wlflag>0&&rseq<10){
				String mess="漂亮！，玩家--"+p.getUrole()+"--在挑战中战胜了玩家--"+rolename+"，排名提升至第"+rseq+"位，不服来战！";
				this.playerpubmess(p.getSvrcode(), mess);
			}
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
			if(GameManager.getInstance().getLockrank().containsKey(rolename)){
				GameManager.getInstance().getLockrank().remove(rolename);
			}
			if(GameManager.getInstance().getLockrank().containsKey(p.getUrole())){
				GameManager.getInstance().getLockrank().remove(p.getUrole());
			}
		}				
	}
	public void getEgg(User p,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		String sql="SELECT * FROM useropentbl t1,eggdict t2 WHERE t1.eggcode=t2.eggcode AND t1.serverno=t2.serverno AND t1.rolename=? AND t1.serverno=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,p.getUrole() );
			ps.setString(2,p.getSvrcode() );
			rs = ps.executeQuery();
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>(); 
				UserEgg ug = new UserEgg();
				ug.setRolename(rs.getString("rolename"));
				ug.setServerno(rs.getString("serverno"));
				ug.setEggcode(rs.getString("eggcode"));
				ug.setEggname(rs.getString("eggname"));
				ug.setStarttime(rs.getInt("starttime"));
				ug.setOpennum(rs.getInt("opennum"));//金币开蛋次数
				ug.setRemainnum(rs.getInt("remainnum"));
				int k=(int)(System.currentTimeMillis()/1000);
				ug.setDaynum(rs.getInt("daynum"));
				ug.setStepsec(rs.getInt("stepsec"));
				ug.setFirstdown(rs.getInt("firstdown"));
				ug.setDiscount(rs.getInt("discount"));
				ug.setUprand(rs.getInt("uprand"));
				ug.setStime(rs.getInt("stime"));
				ug.setEndtime(rs.getInt("endtime"));
				ug.setCost(rs.getInt("cost"));	
				if(k>=ug.getStime()&&k<ug.getEndtime()&&ug.getStime()>0){
					ug.setCost((int)((ug.getCost()*ug.getDiscount())/100));	
					ug.setStar1(rs.getInt("star1"));
					if(ug.getEggcode().equals("e1")){
						ug.setStar2(rs.getInt("star2")-ug.getUprand());
						ug.setStar4(rs.getInt("star4")+ug.getUprand());
						ug.setStar3(rs.getInt("star3"));
						ug.setStar5(rs.getInt("star5"));
						ug.setStar6(rs.getInt("star6"));
					}
					if(ug.getEggcode().equals("e2")){
						ug.setStar2(rs.getInt("star2"));
						ug.setStar3(rs.getInt("star3")-ug.getUprand());
						ug.setStar5(rs.getInt("star5")+ug.getUprand());
						ug.setStar4(rs.getInt("star4"));
						ug.setStar6(rs.getInt("star6"));
					}
					if(ug.getEggcode().equals("e3")){
						ug.setStar4(rs.getInt("star4")-ug.getUprand());
						ug.setStar6(rs.getInt("star6")+ug.getUprand());
						ug.setStar3(rs.getInt("star3"));
						ug.setStar2(rs.getInt("star2"));
						ug.setStar5(rs.getInt("star5"));
					}
				}else {
					ug.setStar1(rs.getInt("star1"));
					ug.setStar2(rs.getInt("star2"));
					ug.setStar3(rs.getInt("star3"));
					ug.setStar4(rs.getInt("star4"));
					ug.setStar5(rs.getInt("star5"));
					ug.setStar6(rs.getInt("star6"));
				}
				if(ug.getStarttime()>0){//计时中
					if((k-ug.getStarttime())>=ug.getStepsec()){//时间到可以开
						ug.setIfopen(1);
						ug.setRemaintime(0);
						map.put("AH", 1);
						map.put("AD", 0);
					}else{
						ug.setIfopen(0);
						ug.setRemaintime(ug.getStepsec()+ug.getStarttime()-k);
						map.put("AH", 0);
						map.put("AD", ug.getStepsec()+ug.getStarttime()-k);//剩余时间
					}
				}else{
					if(ug.getRemainnum()>0)	ug.setIfopen(1); 
					else ug.setIfopen(0);
					map.put("AH", ug.getIfopen());
					map.put("AD", 0);
				}						
				p.getUseregg().put(ug.getEggcode(), ug);
				map.put("AB",ug.getEggcode());
				map.put("AC", ug.getEggname());
				map.put("AE", ug.getRemainnum());
				map.put("AF", ug.getDaynum());
				map.put("AG", ug.getCost());
				//logger.info("最新价格："+ug.getCost());
				map.put("AI", ug.getOpennum());
				al.add(map);
			}		
			smap.put("AA", al.toArray());
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	//开蛋 flag=1 金条开蛋 0普通		
	public void openEgg(User p,int starno,UserEgg ug,Map<String,Object> map,int flag) throws SQLException{
		Connection conn = pool.getConnection();
		String downcode,downname;
		int cid=0;
		String sql="";
		PreparedStatement ps = null;
		try{
			conn.setAutoCommit(false);
			Map<String,Object> refValue = new HashMap<String,Object>();
			int downflag=starno+4;
			if(starno==6) downflag=20;//掉落6星卡
			ProcDown(p, conn, downflag, refValue);
			cid=Integer.parseInt(refValue.get("AB").toString());
			downcode=refValue.get("AA").toString();
			downname=refValue.get("AE").toString();
			map.put("AB", ug.getEggcode());
			map.put("AC", ug.getEggname());
			map.put("AD", ug.getRemaintime());
			map.put("AE", ug.getRemainnum());
			map.put("AF", ug.getDaynum());
			map.put("AG", ug.getCost());
			map.put("AH", ug.getIfopen());
			map.put("AI", ug.getOpennum());
			map.put("AJ", downcode);
			CardDict cd = GameManager.getInstance().getCarddict().get(downcode);
			map.put("AL", cd.getCardname());
			map.put("AM", cd.getCardprop());
			map.put("AN", cd.getStarlevel());
			map.put("AR", cd.getLifeskill());
			int ohp,oatk,odef;		
			ohp=cd.getStarlevel()*25+(int)(cd.getBasehp()*10)+(int)(cd.getStarlevel()*6+cd.getBasehp()+8);
			oatk=cd.getStarlevel()*15+(int)(cd.getBaseatk()*4)+(int)(cd.getStarlevel()*2+cd.getBaseatk());
			odef=(int)(cd.getStarlevel()*6.5)+(int)(cd.getBasedef()*2)+(int)(cd.getStarlevel()*0.5+cd.getBasedef());
			
			/*HP：星级*25+人物基础成长*10+等级*(星级*6+人物基础成长+8)*魅力成长系数
			ATK:星级*15+人物基础成长*4+等级*(星级*2+人物基础成长)*魅力成长系数
			DEF:星级*6.5+人物基础成长*2+等级*(星级*0.5+人物基础成长)*魅力成长系数*/
			map.put("AO", ohp);
			map.put("AP", oatk);
			map.put("AQ", odef);
			DownCard(p, downcode, cid, conn);
			if(flag>0){//金条开蛋
				sql="UPDATE useropentbl SET opennum=? WHERE rolename=? AND serverno=? AND eggcode=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, ug.getOpennum());
				ps.setString(2, p.getUrole());
				ps.setString(3, p.getSvrcode());
				ps.setString(4, ug.getEggcode());
				ps.executeUpdate();
				ps.close();
				sql="UPDATE userroletbl SET gold=gold-? WHERE username=? AND rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1,ug.getCost());
				ps.setString(2,p.getUname());
				ps.setString(3, p.getUrole());
				ps.setString(4, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
				p.setGold(p.getGold()-ug.getCost());
				sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,?,?)";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3, Long.toString(System.currentTimeMillis()));
				ps.setInt(4,ug.getCost());
				ps.setString(5,ug.getEggname());
				ps.setString(6,cd.getStarlevel()+"星级卡");//抽中卡牌星级
				ps.executeUpdate();
				ps.close();
			}else{
				sql="UPDATE useropentbl SET starttime=?,remainnum=? WHERE rolename=? AND serverno=? AND eggcode=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, ug.getStarttime());
				ps.setInt(2, ug.getRemainnum());
				ps.setString(3, p.getUrole());
				ps.setString(4, p.getSvrcode());
				ps.setString(5, ug.getEggcode());
				ps.executeUpdate();
				ps.close();
			}
			//开蛋额外掉落
			int cursecond=(int)(System.currentTimeMillis()/1000);
			ArrayList<Map<String,Object>> mapal =new ArrayList<Map<String,Object>>();
			if(ug.getEggcode().equals("e3")){
				if(GameManager.getInstance().getSvractdict().containsKey(6)&&p.getLevel()>=2){
					if(cursecond>=GameManager.getInstance().getSvractdict().get(6).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(6).getEtime()){
						int tdownflag=CalcDown(202);
						if(tdownflag>=0){
							ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(202);
							downDict dd = al.get(tdownflag);
							Map<String,Object> map1 = new HashMap<String,Object>();
							procdowngoods(p,dd,map1,conn);//处理掉落
							mapal.add(map1);
						}
					}
				}
			}
			if(ug.getEggcode().equals("e2")){
				if(GameManager.getInstance().getSvractdict().containsKey(9)&&p.getLevel()>=2){
					if(cursecond>=GameManager.getInstance().getSvractdict().get(9).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(9).getEtime()){
						int tdownflag=CalcDown(205);
						if(tdownflag>=0){
							ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(205);
							downDict dd = al.get(tdownflag);
							Map<String,Object> map1 = new HashMap<String,Object>();
							procdowngoods(p,dd,map1,conn);//处理掉落
							mapal.add(map1);
						}
					}
				}
			}
			map.put("EX", mapal.toArray());
			map.put("AS", p.getGold());
			this.UpdateUserAct(p, 2, ug.getEggcode()+cd.getStarlevel(),1, conn);
			conn.commit();
			conn.setAutoCommit(true);
			String str="";
			if(cd.getStarlevel()>4){
				str="超神了！玩家--"+p.getUrole()+"--在开启"+ug.getEggname()+"扭蛋后获得了一个"+cd.getStarlevel()+"星的"+cd.getCardname()+"！";
				this.playerpubmess(p.getSvrcode(), str);
			}
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			if(flag>0) p.setGold(p.getGold()+ug.getCost());//金条开蛋
			this.rollbackCard(p);
			this.rollbackegg(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void devsale(User p ,int devid) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		try{
			p.setMoney(p.getMoney()+p.getUdevicelist().get(devid).getPrice());
			conn.setAutoCommit(false);
			String delsql="DELETE FROM userdevice"+p.getSvrcode()+" WHERE deviceid=?";
			ps=conn.prepareStatement(delsql);
			ps.setInt(1, devid);
			ps.executeUpdate();
			ps.close();
			//装备上的符文被卸下
			Iterator<UserMark> it = p.getUmarklist().values().iterator();
			ArrayList<Integer> markal=new ArrayList<Integer>();
			while(it.hasNext()){
				UserMark um=it.next();
				if(um.getDeviceid()==devid){
					markal.add(um.getMarkid());
				}
			}
			for(int i=0;i<markal.size();i++){
				delsql="UPDATE usermark"+p.getSvrcode()+" SET deviceid=-1,markloc=-1 WHERE markid=?";
				ps=conn.prepareStatement(delsql);
				ps.setInt(1, markal.get(i));
				ps.executeUpdate();
				ps.close();
				p.getUmarklist().get(markal.get(i)).setDeviceid(-1);
				p.getUmarklist().get(markal.get(i)).setMarkloc(-1);
			}
			delsql="UPDATE userroletbl SET money=? WHERE username=? AND rolename=? AND serverno=? ";
			ps=conn.prepareStatement(delsql);
			ps.setInt(1, p.getMoney());
			ps.setString(2, p.getUname());
			ps.setString(3, p.getUrole());
			ps.setString(4, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			p.setMoney(p.getMoney()-p.getUdevicelist().get(devid).getPrice());
			this.rollbackMark(p);
			this.rollbackDevice(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	//获取好友列表
	public void getFriends(User p,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM userfriend"+p.getSvrcode()+" t1,userroletbl t2 WHERE t1.friendrole=t2.rolename AND t1.serverno=t2.serverno AND t1.rolename=? AND t1.serverno=? AND friendask=1";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			ArrayList<Map<String,Object>> fal = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> newmap=new HashMap<String,Object>();
				newmap.put("AB", rs.getString("friendrole"));
				newmap.put("AC", rs.getInt("level"));
				newmap.put("AE", rs.getInt("limitfight"));
				fal.add(newmap);
			}
			sendMap.put("AA", fal.toArray());
			ps.close();
			rs.close();
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	public void FindPlayer(String scode,String rn,Map<String,Object> sendMap,User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="";
		try{
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
			if(rn.equals("@")){
				sql="SELECT * FROM userroletbl  WHERE  serverno=? AND rolename<>? and level=?  ORDER BY RAND() LIMIT 10";
				ps=conn.prepareStatement(sql);
				ps.setString(1,scode);
				ps.setString(2,p.getUrole());
				ps.setInt(3, p.getLevel());
			}
			else{
				sql="SELECT * FROM userroletbl  WHERE rolename=? AND serverno=? AND rolename<>?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, rn);
				ps.setString(2,scode);
				ps.setString(3,p.getUrole());
			}
			rs=ps.executeQuery();
			while(rs.next()){
				Map<String,Object> smap=new HashMap<String,Object>();
				smap.put("AA",rs.getString("rolename"));
				smap.put("AB", rs.getInt("level"));
				al.add(smap);
			}
			ps.close();
			rs.close();
			sendMap.put("FF", al.toArray());
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	//申请好友
	public int ApplyPlayer(User p,String rn,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="";
		sql="SELECT COUNT(1) asum FROM userfriend"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND friendask=1";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				if(rs.getInt("asum")==p.getFriendsum()){//已满
					sendMap.put("EC", "1043");
					ps.close();
					rs.close();
					return 0;
				}
			}
			ps.close();
			rs.close();
			sql="SELECT * FROM userfriend"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND friendrole=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			ps.setString(3,rn);
			rs=ps.executeQuery();
			if(rs.next()){
				ps.close();
				rs.close();
				sendMap.put("EC", "1044");
				return 0;
			}
			ps.close();
			rs.close();
			sql="INSERT INTO userfriend"+p.getSvrcode()+" VALUES(?,?,?,0,'',1)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			ps.setString(3,rn);
			ps.executeUpdate();
			ps.close();
			return 1;
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	//获取待审列表
	public void GetApplyList(User p,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM userfriend"+p.getSvrcode()+" t1,userroletbl t2  WHERE t1.rolename=t2.rolename AND t1.serverno=t2.serverno AND t1.serverno=? AND t1.friendrole=? AND t1.friendask=0";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getSvrcode());
			ps.setString(2,p.getUrole());
			rs=ps.executeQuery();
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>(); 
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("AB",rs.getString("rolename"));
				map.put("AC", rs.getInt("level"));
				al.add(map);				
			}
			sendMap.put("AA", al.toArray());
			ps.close();
			rs.close();
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	//审核好友
	public int verifyFriend(User p,String frole,int verify) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="";
		try{
			conn.setAutoCommit(false);
			if(verify==0){//取消审核
				sql="DELETE FROM userfriend"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND friendrole=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1,frole);
				ps.setString(2,p.getSvrcode());
				ps.setString(3,p.getUrole());
				ps.executeUpdate();
				ps.close();
			}else{//同意
				sql="SELECT COUNT(1) asum FROM userfriend"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND friendask=1";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2,p.getSvrcode());
				rs=ps.executeQuery();
				if(rs.next()){
					if(rs.getInt("asum")==p.getFriendsum()){//已满
						ps.close();
						rs.close();
						return 0;
					}
				}
				ps.close();
				rs.close();
				int fnum=0;
				sql="SELECT friendsum  FROM userroletbl WHERE rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, frole);
				ps.setString(2,p.getSvrcode());
				rs=ps.executeQuery();
				if(rs.next()){
					fnum=rs.getInt("friendsum");
				}
				ps.close();
				rs.close();
				sql="SELECT  COUNT(1) asum FROM userfriend"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND friendask=1";
				ps=conn.prepareStatement(sql);
				ps.setString(1, frole);
				ps.setString(2,p.getSvrcode());
				rs=ps.executeQuery();
				if(rs.next()){
					if(rs.getInt("asum")>=fnum){//对方好友已满
						ps.close();
						rs.close();
						return -1;
					}
				}
				ps.close();
				rs.close();
				int ifhave=0;
				sql="SELECT * FROM userfriend"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND friendrole=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2,p.getSvrcode());
				ps.setString(3,frole);
				rs=ps.executeQuery();
				if(rs.next()){
					ps.close();
					rs.close();
					ifhave=1;
				}
				ps.close();
				rs.close();
				if(ifhave>0){
					sql="UPDATE userfriend"+p.getSvrcode()+" SET friendask=1 WHERE  rolename=? AND serverno=? AND friendrole=?";
					ps=conn.prepareStatement(sql);
					ps.setString(1,p.getUrole());
					ps.setString(2,p.getSvrcode());
					ps.setString(3, frole);
					ps.executeUpdate();
					ps.close();
				}else{
					sql="INSERT INTO userfriend"+p.getSvrcode()+" VALUES(?,?,?,1,'',1)";//通过
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2,p.getSvrcode());
					ps.setString(3, frole);
					ps.executeUpdate();
					ps.close();
				}				
				sql="UPDATE userfriend"+p.getSvrcode()+" SET friendask=1 WHERE  rolename=? AND serverno=? AND friendrole=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1,frole);
				ps.setString(2,p.getSvrcode());
				ps.setString(3,  p.getUrole());
				ps.executeUpdate();
				ps.close();						
			}	
			conn.commit();
			conn.setAutoCommit(true);		
			return 1;
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	//删除好友
	public void DelFriend(User p,String rn,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		String sql="DELETE FROM userfriend"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND friendrole=?";
		try{
			conn.setAutoCommit(false);
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			ps.setString(3,rn);
			ps.executeUpdate();
			ps.close();
			sql="DELETE FROM userfriend"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND friendrole=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, rn);
			ps.setString(2,p.getSvrcode());
			ps.setString(3,p.getUrole());
			ps.executeUpdate();
			ps.close();		
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	//获取商城列表
	public void getShop(User p) throws SQLException, ClassNotFoundException, IOException {
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="";
		/*1 普通限时购买
		2 普通限次购买
		3 普通打折
		4 冷却限次
		5 限时打折
		6 VIP等级限制*/
		try{
			p.getUsershop().clear();
			Iterator<ShopClass> it = GameManager.getInstance().getShopdict().values().iterator();
			while(it.hasNext()){
				int k=0;
				int nowtime=(int)(System.currentTimeMillis()/1000);
				ShopClass sc = it.next();
				ShopClass usershop=null;
				switch (sc.getBuycond()){
				case 0://无限制
					usershop=sc.copy();
					break;
				case 1://普通限时购买
					usershop=sc.copy();
					break;
				case 2://普通限次购买
					sql="SELECT COUNT(1) COUSUM FROM userbuy"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND goodscode=? ";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, sc.getGoodscode());
					rs=ps.executeQuery();
					if(rs.next()) k=rs.getInt("COUSUM");
					if(k<sc.getBuysum()) {
						usershop=sc.copy();
						usershop.setBuysum(sc.getBuysum()-k);
					}
					ps.close();
					rs.close();
					break;
				case 3://普通打折
					usershop=sc.copy();
					break;
				case 4://冷却限次
					sql="SELECT COUNT(1) COUSUM,MAX(buytime) lasttime  FROM userbuy"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND goodscode=? ";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, sc.getGoodscode());
					rs=ps.executeQuery();
					k=0;
					if(rs.next()) k=rs.getInt("COUSUM");
					 usershop=sc.copy();
					if(k>=sc.getBuysum()){
						if(nowtime-rs.getInt("lasttime")-sc.getBuystart()>=0){//冷却结束可以购买
							usershop.setBuystart(0);
							usershop.setBuysum(1);
						}else{
							usershop.setBuystart(sc.getBuystart()-nowtime+rs.getInt("lasttime"));//剩余冷却时间
							usershop.setBuyend(rs.getInt("lasttime"));
							usershop.setBuysum(0);
						}
					}else
						usershop.setBuysum(sc.getBuysum()-k);//剩余购买次数
					rs.close();
					ps.close();
					break;
				case 5://限时打折
					usershop=sc.copy();
					break;
				case 6://VIP等级限制
					if(p.getViplevel()>=sc.getBuysum()&&p.getViplevel()>0){
						sql="SELECT 1 FROM userbuy"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND goodscode=? ";
						ps=conn.prepareStatement(sql);
						ps.setString(1, p.getUrole());
						ps.setString(2, p.getSvrcode());
						ps.setString(3, sc.getGoodscode());
						rs=ps.executeQuery();
						if(!rs.next()) usershop=sc.copy();
						ps.close();
						rs.close();
					}else{
						usershop=sc.copy();
					}
					break;
				}
				if(usershop!=null)	p.getUsershop().add(usershop);
			}
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;			
		}catch (ClassNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void BuyGoods(User p,int price,int buysum,ShopClass sc) throws SQLException, ClassNotFoundException, IOException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="";		
		try{
			conn.setAutoCommit(false);
			sql="INSERT INTO userbuy"+p.getSvrcode()+" VALUES(?,?,?,?,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, Long.toString(System.currentTimeMillis()));
			ps.setString(4,sc.getGoodscode());
			ps.setInt(5, sc.getGoodstype());
			ps.setInt(6,buysum);
			ps.setInt(7,price);
			ps.executeUpdate();
			ps.close();
			sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,-1,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, Long.toString(System.currentTimeMillis()));
			ps.setInt(4,price);
			ps.setString(5,sc.getGoodscode());
			ps.setString(6, String.valueOf(p.getGold()));//购买后剩余金条数
			ps.executeUpdate();
			ps.close();
			p.setGold(p.getGold()-price);
			sql="";
			//根据物品type，更新相应背包      0：道具 1为包 2 卡，3装备，4符文，5 技能 6转生丹
			switch(sc.getGoodstype()){
			case 0://非包道具
			case 1://包
				//logger.info("购买道具"+sc.getGoodscode()+"数量："+buysum);
				this.DownProps(p, sc.getGoodscode(), conn, buysum);
				break;
			case 2://卡
				Iterator<UserCard> it = p.getCardlist().values().iterator();
				int cid=0;
				while(it.hasNext()){
					UserCard uc=it.next();
					if(uc.getCardcode().equals(sc.getGoodscode())){
						cid=uc.getCardid();
						break;
					}
				}
				for(int i=0;i<buysum;i++){
					this.DownCard(p, sc.getGoodscode(), cid, conn);
				}
				break;
			case 3://装备
				for(int i=0;i<buysum;i++){
					this.DownDevice(p, sc.getGoodscode(), conn);
				}
				break;
			case 4://符文
				for(int i=0;i<buysum;i++){
					this.DownMark(p, sc.getGoodscode(), conn);
				}
				break;
			case 5://技能
				for(int i=0;i<buysum;i++){
					this.DownSkill(p, sc.getGoodscode(), conn);
				}
				break;
			case 6://转生丹
				p.setBall(p.getBall()+buysum);
				this.UpdateUserAct(p, 3, "购买"+GameManager.getInstance().getBallname(), buysum, conn);
				break;
			}
			this.PlayerUpdate(p, conn,price*-1);
			conn.commit();
			conn.setAutoCommit(true);
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			this.getShop(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
	}
	public void FinFriendMatch(User p,int flag,String rname,int elevel,Map<String,Object> smap,int exp,int mon) throws SQLException{
		Connection conn = pool.getConnection();
		int sucflag=0;
		try{
			conn.setAutoCommit(false);
			if(flag>0){//胜利
				int downflag=CalcDown(9);
				if(downflag>=0){
					Map<String,Object> tempmap =new HashMap<String,Object>();
					ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(9);		
					downDict dd = al.get(downflag);
					procdowngoods(p,dd,tempmap,conn);
					smap.put("AF", tempmap.get("AP").toString());
					smap.put("AG", tempmap.get("AB").toString());
					smap.put("AE", tempmap.get("XP").toString());
					smap.put("DV", tempmap.get("AR").toString());
				}
			}
			this.UpdateDayMission(p, "切磋", conn);
			PreparedStatement ps = null;
			String sql="UPDATE userfriend"+p.getSvrcode()+" SET limitfight=limitfight-1 WHERE rolename=? AND serverno=? AND friendrole=? ";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, rname);
			ps.executeUpdate();
			ps.close();
			p.setFriendpknum(p.getFriendpknum()+1);
			this.CalcCardFin(p, exp, conn);
			this.CalcUserFriendFin(p,exp, mon, conn,smap);
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
	}
	public void getCurrDrink(User p,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM userdrink WHERE rolename=? AND serverno=?";
		int nowtime=(int)(System.currentTimeMillis()/1000); 
		String startdate1=dateToString(new Date()).substring(0,10)+" 11:00:00";
		String enddate1=dateToString(new Date()).substring(0,10)+" 13:00:00";
		String startdate2=dateToString(new Date()).substring(0,10)+" 17:00:00";
		String enddate2=dateToString(new Date()).substring(0,10)+" 19:00:00";
		try{
			int drinknum=0,flag=0,drinkadd=0;
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				flag=checklog(rs.getString("drinkdate"),dateToString(new Date()));
				drinknum=rs.getInt("drinknum");
				drinkadd=rs.getInt("drinkadd");
				p.setDrinknum(drinknum);//记录用户当前可畅饮次数
				if(flag>=1){//跨天可以畅饮
					//更新数据库，重置畅饮次数
					if(nowtime>=strtotimpstamp(enddate1)) drinknum=1;
					else drinknum=2;
					String sql1="UPDATE userdrink SET drinkdate=?,drinknum=? WHERE rolename=? AND serverno=?";
					PreparedStatement ps1 = null;
					ps1=conn.prepareStatement(sql1);
					ps1.setString(1,dateToString(new Date()).substring(0,10));
					ps1.setInt(2, 2);
					ps1.setString(3, p.getUrole());
					ps1.setString(4, p.getSvrcode());
					ps1.executeUpdate();
					ps1.close();
					p.setDrinknum(2);
				}
				sendMap.put("AC", drinknum);
				sendMap.put("AD", drinkadd);
				p.setPoweradd(drinkadd);//记录补充体力值
				if(drinknum>0){
					if(nowtime>=strtotimpstamp(enddate2)){//超过结束时间
						sendMap.put("AA", 0);//畅饮不开放
						sendMap.put("AB", 0);//计时
					}else if(nowtime>=strtotimpstamp(startdate2)&& nowtime<strtotimpstamp(enddate2)){//第二次畅饮时间段
						sendMap.put("AA", 1);//畅饮开放
					}else if(nowtime>=strtotimpstamp(enddate1)&&nowtime<strtotimpstamp(startdate2)){//第一次结束至第二次开始之间
						sendMap.put("AA", 0);//畅饮不开放
						sendMap.put("AB",strtotimpstamp(startdate2)-nowtime);
					}else if(nowtime>=strtotimpstamp(startdate1)&& nowtime<strtotimpstamp(enddate1)){//第一次畅饮时间段
						if(drinknum>1) sendMap.put("AA", 1);//畅饮开放
						else {
							sendMap.put("AA", 0);//畅饮不开放
							sendMap.put("AB", strtotimpstamp(startdate2)-nowtime);//计时
						}
					}else if(nowtime<strtotimpstamp(startdate1)){
						sendMap.put("AA", 0);//畅饮不开放
						sendMap.put("AB",strtotimpstamp(startdate1)-nowtime);
					}
				}else{
					sendMap.put("AA", 0);//畅饮不开放
					sendMap.put("AB", 0);//计时
				}
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	public int startdrink(User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		int nowtime=(int)(System.currentTimeMillis()/1000)+120; //误差2分钟
		String startdate1=dateToString(new Date()).substring(0,10)+" 11:00:00";
		String enddate1=dateToString(new Date()).substring(0,10)+" 13:00:00";
		String startdate2=dateToString(new Date()).substring(0,10)+" 17:00:00";
		String enddate2=dateToString(new Date()).substring(0,10)+" 19:00:00";
		if(nowtime<strtotimpstamp(startdate1)||(nowtime>=strtotimpstamp(enddate1)&&nowtime<strtotimpstamp(startdate2))||nowtime>=strtotimpstamp(enddate2))
			{
			return 0;
			}
		p.setDrinknum(p.getDrinknum()-1);
		p.setPower(p.getPower()+p.getPoweradd());
		if(p.getPower()>=p.getMaxpower()){
			p.setPowerstart(0);
		}
		try{
			if(nowtime>=strtotimpstamp(startdate2)) {
				p.setDrinknum(0);
			}
			//logger.info("畅饮次数："+p.getDrinknum());
			String sql="UPDATE userdrink SET drinknum=? WHERE rolename=? AND serverno=?";
			conn.setAutoCommit(false);
			ps=conn.prepareStatement(sql);
			ps.setInt(1,p.getDrinknum());
			ps.setString(2, p.getUrole());
			ps.setString(3,p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			this.UpdateDayMission(p, "畅饮", conn);//记入日常畅饮
			this.PlayerUpdate(p, conn,0);
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			p.setPower(p.getPower()-p.getPoweradd());
			p.setDrinknum(p.getDrinknum()+1);		
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
		return 1;
	}
	public void getguangong(User p,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM uservisitgg WHERE rolename=? AND serverno=?";
		try{
			int flag=0;
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			rs=ps.executeQuery();
			sendMap.put("AA",-1);
			if(rs.next()){
				p.setLastvisit(rs.getString("visitdate"));
				p.setVisitnum(rs.getInt("visitnum"));
				p.setVisitlevle(rs.getInt("visitlevel"));
				int visitdays=0;
				switch(p.getVisitlevle()){
				case 1:
					visitdays=3;
					break;
				case 2:
					visitdays=6;
					break;
				case 3:
					visitdays=6;
					break;
				case 4:
					visitdays=5;
					break;
				}
				if(checklog(p.getLastvisit(),dateToString(new Date()))>1) p.setVisitnum(0);
				if(!rs.getString("visitdate").equals(dateToString(new Date()).substring(0,10))){
					flag=1;//今日还没参拜过
					sendMap.put("AA", 1);
				}else{
					flag=0;
					sendMap.put("AA", 0);
				}
				sendMap.put("AG", p.getVisitnum());
				sendMap.put("AC", visitdays);
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	public int visitgg(User p,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		int visitdays=0,vflag=0,candown=0,visitnum=0;
		if(dateToString(new Date()).substring(0,10).equals(p.getLastvisit())){//已参拜过
			return 0;
		}else{			
			switch(p.getVisitlevle()){
			case 1:
				visitdays=3;
				vflag=1;
				break;
			case 2:
				visitdays=6;
				vflag=2;
				break;
			case 3:
				visitdays=6;
				vflag=3;
				break;
			case 4:
				vflag=0;
				visitdays=5;
				break;
			}
			String sql="UPDATE uservisitgg SET visitdate=?,visitnum=?,visitlevel=? WHERE rolename=? AND serverno=?";
			int flag=checklog(p.getLastvisit(),dateToString(new Date()));
			if(flag==1){//间隔一天连续登录
				 if(p.getVisitnum()+1==visitdays) {
					 visitnum=p.getVisitnum()+1;
					 p.setVisitnum(0);
					 candown=1;//可以领取奖励
					 if(p.getVisitlevle()+1>=4) p.setVisitlevle(4);
					 else p.setVisitlevle(p.getVisitlevle()+1);
				 }
				 else  {
					 p.setVisitnum(p.getVisitnum()+1);
					 visitnum=p.getVisitnum();
				 }
			}
			if(flag>1){
				p.setVisitnum(1);
				visitnum=1;
			}
			p.setLastvisit(p.getCurrlogin().substring(0,10));
			sendMap.put("AG", p.getVisitnum());
			switch(p.getVisitlevle()){
			case 1:
				visitdays=3;
				break;
			case 2:
				visitdays=6;
				break;
			case 3:
				visitdays=6;
				break;
			case 4:
				visitdays=5;
				break;
			}
			sendMap.put("AD", visitdays);
			try{
				conn.setAutoCommit(false);
				ps=conn.prepareStatement(sql);
				ps.setString(1,p.getLastvisit());
				ps.setInt(2, p.getVisitnum());
				ps.setInt(3, p.getVisitlevle());
				ps.setString(4, p.getUrole());
				ps.setString(5,p.getSvrcode());				
				ps.executeUpdate();
				ps.close();
				this.UpdateDayMission(p, "关公", conn);//记入日常参拜
				sendMap.put("AA", "@");
				if(candown>0){//根据连续参拜次数 奖励
					int downflag=CalcDown(vflag);
					//logger.info("掉落"+downflag);
					if(downflag>=0){
						ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(vflag);		
						downDict dd = al.get(downflag);
						procdowngoods(p,dd,sendMap,conn);					
					}
					//关公额外掉落
					int cursecond=(int)(System.currentTimeMillis()/1000);
					ArrayList<Map<String,Object>> mapal =new ArrayList<Map<String,Object>>();
					if(GameManager.getInstance().getSvractdict().containsKey(8)){
						if(cursecond>=GameManager.getInstance().getSvractdict().get(8).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(8).getEtime()){
							downflag=CalcDown(204);
							if(downflag>=0){
								ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(204);
								downDict dd = al.get(downflag);
								Map<String,Object> map = new HashMap<String,Object>();
								procdowngoods(p,dd,map,conn);//处理掉落
								mapal.add(map);
							}
						}
					}
					sendMap.put("EX", mapal.toArray());
				}
				conn.commit();
				conn.setAutoCommit(true);
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackPlay(p);
				this.rollguangong(p);
				logger.info(e.getMessage());
				throw e;
			}
			finally{
				conn.close();
			}	
			return 1;
		}		
	}
	//计算掉落物品  did掉落id
	public int CalcDown(int did){
		int flag=-1;
		ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(did);
		int sumval=0;
		for(int i=0;i<al.size();i++){
			sumval=sumval+al.get(i).getDownrand();
		}
	    int maxval=100000-sumval;
	    int minval=0;
		Random rd = new Random();
		int r=rd.nextInt(100000);
		for(int i=0;i<al.size();i++){
			minval=maxval;
			if(al.get(i).getDownrand()==0) continue;
			maxval=maxval+al.get(i).getDownrand();
			if(r>=minval && r<maxval){
				flag=i;
				break;
			}
		}
		return flag;
	}
	public void procdowngoods(User p,downDict dd,Map<String,Object> sendMap,Connection conn) throws SQLException{
		String downcode="@";
		String downname="@";
		int downcolor=0;
		String mess="";
		int pageno=0,ifupdate=0,cid=0;
		//logger.info("类型："+dd.getGoodstype());
		try{
			switch(dd.getGoodstype()){
			case 0://装备
				//logger.info("sadfasdf"+dd.getDowncode());
				if(!dd.getDowncode().equals("@")){//直接掉落装备
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 2, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 3, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 4, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownDevice(p, downcode, conn);
				}
				sendMap.put("AP", downcode);
				break;
			case 1://武器
				if(!dd.getDowncode().equals("@")){//直接掉落装备
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 21, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 31, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 41, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownDevice(p, downcode, conn);
				}
				sendMap.put("AP", downcode);
				break;
			case 2://防具
				if(!dd.getDowncode().equals("@")){//直接掉落装备
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 22, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 32, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 42, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownDevice(p, downcode, conn);
				}
				sendMap.put("AP", downcode);
				break;
			case 3://饰品
				if(!dd.getDowncode().equals("@")){//直接掉落装备
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 23, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 33, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 43, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();			
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownDevice(p, downcode, conn);
				}
				sendMap.put("AP", downcode);
				break;
			case 4://技能
				if(!dd.getDowncode().equals("@")){//直接掉落技能
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getSkilldict().get(downcode).getSkillname();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://等级1技能
						this.ProcDown(p, conn, 46, refValue);
						break;
					case 2://2
						this.ProcDown(p, conn, 47, refValue);
						break;
					case 3://3
						this.ProcDown(p, conn, 48, refValue);
						break;
					case 4://4
						this.ProcDown(p, conn, 45, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();			
				}
				downcolor=GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownSkill(p, downcode, conn);
				}
				sendMap.put("AP", downcode);
				break;
			case 5://技能书页
				if(!dd.getDowncode().equals("@")){//掉落技能书页
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getSkilldict().get(downcode).getSkillname();
					pageno=CommTools.randomval(1,GameManager.getInstance().getSkilldict().get(downcode).getPagenum());
					if(p.getSkillpagelist().containsKey(downcode)) ifupdate=1;
					else ifupdate=0;
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://等级1技能
						this.ProcDown(p, conn, 36, refValue);
						break;
					case 2://2
						this.ProcDown(p, conn, 37, refValue);
						break;
					case 3://3
						this.ProcDown(p, conn, 38, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
					pageno=Integer.parseInt(refValue.get("AC").toString());
					ifupdate=Integer.parseInt(refValue.get("AD").toString());			
				}
				downcolor=GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownSkillPages(p, downcode, ifupdate, pageno, conn);
				}
				sendMap.put("AP", downcode);
				break;
			case 6://道具
				if(!dd.getDowncode().equals("@")){//直接掉落道具
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getPropsdict().get(downcode).getPropsname();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://等级1道具
						this.ProcDown(p, conn, 13, refValue);
						break;
					case 2://2
						this.ProcDown(p, conn, 14, refValue);
						break;
					case 3://3
						this.ProcDown(p, conn, 15, refValue);
						break;
					}
					//this.ProcDown(p, conn, 13, refValue);//掉落不同等级道具
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();				
				}
				sendMap.put("AP", GameManager.getInstance().getPropsdict().get(downcode).getPropspic());
				this.DownProps(p, downcode, conn, dd.getDownnum());
				break;
			case 7://符文
				//logger.info("符文开启");
				if(!dd.getDowncode().equals("@")){//直接掉落符文
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getMarkdict().get(downcode).getMarkname();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					this.ProcDown(p, conn, 10, refValue);
					downcode=refValue.get("AA").toString();
					downname=GameManager.getInstance().getMarkdict().get(downcode).getMarkname();
				}
				for(int i=0;i<dd.getDownnum();i++){
					this.DownMark(p, downcode, conn);
				}
				sendMap.put("AP", downcode);
				break;
			case 8://卡牌
				if(!dd.getDowncode().equals("@")){//直接掉落卡牌
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getCarddict().get(downcode).getCardname();
					Iterator<UserCard> it = p.getCardlist().values().iterator();
					while(it.hasNext()){
						UserCard uc=it.next();
						if(uc.getCardcode().equals(downcode)){
							cid=uc.getCardid();
							break;
						}
					}
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					int starlevel=dd.getGoodslevel()+4;
					if(starlevel==10) starlevel=20;
					if(starlevel==11) starlevel=30;
					this.ProcDown(p, conn, starlevel, refValue);
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
					cid=Integer.parseInt(refValue.get("AB").toString());			
				}
				switch(GameManager.getInstance().getCarddict().get(downcode).getStarlevel()){
				case 1:
				case 2:
					downcolor=1;//白
					break;
				case 3:
				case 4:
					downcolor=2;//绿
					break;
				case 5:
					downcolor=3;//蓝
					break;
				case 6:
					downcolor=4;//紫
					break;
				case 7:
					downcolor=5;//橙
					break;							
				}
				for(int i=0;i<dd.getDownnum();i++){
					this.DownCard(p, downcode, cid, conn);
				}
				sendMap.put("AP", downcode);
				break;
			case 10://money
				downcode="P900";
				//downname="钞票";
				downname=GameManager.getInstance().getMname();
				p.setMoney(p.getMoney()+dd.getDownnum());
				sendMap.put("AP", downcode);
				break;
			case 11://Gold
				downcode="P901";
				//downname="金条";
				downname=GameManager.getInstance().getGoldname();
				p.setGold(p.getGold()+dd.getDownnum());
				sendMap.put("AP", downcode);
				break;
			case 12://money
				downcode="P902";
				downname=GameManager.getInstance().getBallname();
				p.setBall(p.getBall()+dd.getDownnum());
				this.UpdateUserAct(p, 3, "掉落"+downname, dd.getDownnum(), conn);
				sendMap.put("AP", downcode);
				break;
			case 9://卡牌碎片
				if(!dd.getDowncode().equals("@")){//直接掉落卡牌碎片
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getCarddict().get(downcode).getCardname()+"碎片";
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					int starlevel=dd.getGoodslevel()+4;
					if(starlevel==10) starlevel=20;
					if(starlevel==11) starlevel=30;
					this.ProcDown(p, conn, starlevel, refValue);
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString()+"碎片";		
				}
				switch(GameManager.getInstance().getCarddict().get(downcode).getStarlevel()){
				case 1:
				case 2:
					downcolor=1;//白
					break;
				case 3:
				case 4:
					downcolor=2;//绿
					break;
				case 5:
					downcolor=3;//蓝
					break;
				case 6:
					downcolor=4;//紫
					break;
				case 7:
					downcolor=5;//橙
					break;							
				}
				this.Downchip(p, downcode, dd.getDownnum(), conn);
				sendMap.put("AP", downcode);
				break;
			case 13://装备碎片
				//logger.info("sadfasdf"+dd.getDowncode());
				if(!dd.getDowncode().equals("@")){//直接掉落装备碎片
					downcode=dd.getDowncode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename()+"碎片";
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 2, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 3, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 4, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString()+"碎片";
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				this.Downchip(p, downcode, dd.getDownnum(), conn);
				sendMap.put("AP", downcode);
				break;
			}
		//logger.info("宝箱掉落："+downcode);
		sendMap.put("AA", downcode);
		sendMap.put("AB", downname);
		sendMap.put("XP", pageno);//技能页号
		sendMap.put("AC", dd.getDownnum());
		sendMap.put("AR", downcolor);
		sendMap.put("LR", downcolor);
		}catch(SQLException e){
			logger.info(e.getMessage());
			throw e;
		}			
	}
	public void getstreet(User p,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM userstreet"+p.getSvrcode()+" WHERE rolename=? AND serverno=? ORDER BY streetid ";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			rs=ps.executeQuery();
			while(rs.next()){
				UserStreet ust= new UserStreet();
				ust.setRolename(rs.getString("rolename"));
				ust.setServerno(rs.getString("serverno"));
				ust.setStreetid(rs.getInt("streetid"));
				ust.setCurtime(rs.getString("curtime"));
				ust.setWinlose(rs.getInt("winlose"));
				ust.setIfcanuse(rs.getInt("ifcanuse"));
				ust.setIflock(rs.getInt("iflock"));
				ust.setIfgold(rs.getInt("ifgold"));
				StreetDict sd = GameManager.getInstance().getStreetdict().get(ust.getStreetid());
				ust.setStreetdesc(sd.getStreetdesc());
				ust.setGiveexp(sd.getGiveexp());
				ust.setGivemoeny(sd.getGivemoeny());
				ust.setNextid(sd.getNextid());
				ust.setGold(sd.getGold());
				ust.setMarks(sd.getMarks());
				ust.setGoods1(sd.getGoods1());
				ust.setGoods1code(sd.getGoods1code());
				ust.setGoods2(sd.getGoods2());
				ust.setGoods2code(sd.getGoods2code());
				//取关卡阵容
				ArrayList<CMissionNpcDict> sl= GameManager.getInstance().getStreetnpcdict().get(ust.getStreetid());
				ust.getSnpclist().clear(); 
				for(int i=0;i<sl.size();i++){
					CMissionNpcDict mnpc =sl.get(i).clone();
					ust.getSnpclist().add(mnpc);
				}
				p.getUstreet().put(ust.getStreetid(), ust);
			}			
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void ProcDown(User p,Connection conn,int flag,Map<String,Object> refValue) throws SQLException{
		CallableStatement cs = null;
		String downcode="@";
		try{
			cs = conn.prepareCall("{call DOWNSOME(?,?,?,?,?,?,?,?,?)}"); //要加参数
			cs.setString(1, p.getUname());
			cs.setString(2, p.getUrole());
			cs.setString(3,p.getSvrcode());
			cs.setInt(4,flag);
			if(flag==10){//符文掉率单独计算
				int k=0;
				int[] a=new int[GameManager.getInstance().getMarkdict().size()];
				int[] loc=new int[GameManager.getInstance().getMarkdict().size()];
				String[] dcode=new String[GameManager.getInstance().getMarkdict().size()];
				Iterator<MarkDict> it = GameManager.getInstance().getMarkdict().values().iterator();
				while(it.hasNext()){
					MarkDict md=it.next();
					a[k]=md.getDownrand();
					loc[k]=k;
					dcode[k]=md.getMarkcode();
					k=k+1;
				}
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
				int r=rd.nextInt(10000);
				for(int i=0;i<a.length;i++){
					minval=maxval;
					if(a[i]==0) break;
					maxval=maxval+a[i];
					if(r>=minval && r<maxval){
						downcode=dcode[loc[i]];
						break;
					}
				}
			}
			cs.setString(5, downcode); //掉落code
			cs.registerOutParameter(5, Types.VARCHAR);//掉落code
			cs.registerOutParameter(6, Types.INTEGER);//卡牌id，>0表明已有相同卡牌 魅力变化
			cs.registerOutParameter(7, Types.INTEGER);//>0 技能页号
			cs.registerOutParameter(8, Types.INTEGER);//技能书页是否新
			cs.registerOutParameter(9, Types.VARCHAR);//掉落物品名称
			cs.execute();
			downcode=cs.getString(5);
			int cid=cs.getInt(6);
			int pnum=cs.getInt(7);
			int ifupdate=cs.getInt(8);
			String downname=cs.getString(9);	
			refValue.put("AA", downcode);
			refValue.put("AB", cid);
			refValue.put("AC", pnum);
			refValue.put("AD", ifupdate);
			refValue.put("AE", downname);
			cs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		//logger.info("掉落:"+flag+"--物品代码:"+downcode+"物品名称："+downname);
	}
	public void buychance(User p ,Map<String,Object> sendMap,int btype,int gnum) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		int nextgold=0;
		String miao="";
		try{
			p.setGold(p.getGold()-gnum);
			switch(btype){
			case 0:
				nextgold=0;
				p.setGoldrenew(p.getGoldrenew()+1);
				p.setPower(p.getMaxpower());
				p.setPowerstart(0);
				sendMap.put("BB",p.getPower());
				sendMap.put("DD",p.getGoldrenew());
				if(p.getGoldrenew()+1<3) nextgold=20;
				if(p.getGoldrenew()+1>=3&&p.getGoldrenew()+1<6) nextgold=40;
				if(p.getGoldrenew()+1>=6&&p.getGoldrenew()+1<9) nextgold=80;
				if(p.getGoldrenew()+1>=9) nextgold=200;
				sendMap.put("EE",nextgold);
				miao="体力";
				this.UpdateDayMission(p, "体力", conn);
				break;
			case 1:
				nextgold=0;
				p.setGoldaddnum(p.getGoldaddnum()+1);
				p.setGetbook(10);
				p.setGetskstart(0);
				sendMap.put("BB", p.getGetbook());
				sendMap.put("DD",p.getGoldaddnum());
				if(p.getGoldaddnum()+1<3) nextgold=20;
				if(p.getGoldaddnum()+1>=3&&p.getGoldaddnum()+1<6) nextgold=40;
				if(p.getGoldaddnum()+1>=6&&p.getGoldaddnum()+1<9) nextgold=80;
				if(p.getGoldaddnum()+1>=9) nextgold=200;
				sendMap.put("EE",nextgold);
				miao="夺计";
				break;
			case 2:
				nextgold=0;
				p.setBuyfight(p.getBuyfight()+1);
				p.setPknum(10);
				sendMap.put("BB", p.getPknum());
				sendMap.put("DD",p.getBuyfight());
				if(p.getBuyfight()+1<3) nextgold=20;
				if(p.getBuyfight()+1>=3&&p.getBuyfight()+1<6) nextgold=40;
				if(p.getBuyfight()+1>=6&&p.getBuyfight()+1<9) nextgold=80;
				if(p.getBuyfight()+1>=9) nextgold=200;
				sendMap.put("EE",nextgold);
				miao="争斗";
				break;
			}
			
			conn.setAutoCommit(false);
			this.PlayerUpdate(p, conn,gnum*-1);
			String sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, Long.toString(System.currentTimeMillis()));
			ps.setInt(4,gnum);
			ps.setString(5,miao);
			ps.setString(6, String.valueOf(p.getGold()));//培养后剩余金条数
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
			sendMap.put("AA",btype);
			sendMap.put("CC", p.getGold());		
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollPlayerInfo(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void getpropslist(User p)throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql="SELECT * FROM userprops"+p.getSvrcode()+" WHERE rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			while(rs.next()){
				UserProps ups= new UserProps();
				ups.setPropscode(rs.getString("propscode"));
				ups.setPropsnum(rs.getInt("propsnum"));
				PropsDict pd=GameManager.getInstance().getPropsdict().get(ups.getPropscode());
				ups.setPropsname(pd.getPropsname());
				ups.setPropsdesc(pd.getPropsdesc());
				ups.setPropsgrade(pd.getPropsgrade());
				ups.setPropstype(pd.getPropstype());
				ups.setPropsuse(pd.getPropsuse());
				ups.setPropsadd(pd.getPropsadd());
				ups.setPropspic(pd.getPropspic());
				if(pd.getPackgoods().size()>0){
					for(int i=0;i<pd.getPackgoods().size();i++){
						packageGoods pg=pd.getPackgoods().get(i).clone();
						ups.getPackgoods().add(pg);
					}
				}
				ups.setUsecond(pd.getUsecond());
				p.getUserprop().put(ups.getPropscode(), ups);
			}
			ps.close();
			rs.close();
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void useprops(User p,UserProps up,Map<String,Object> sendMap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		downDict dd=null;
		int gnum=0,mnum=0;
		gnum=p.getGold();
		mnum=p.getMoney();
		try{
			ArrayList<Map<String,Object>> mapal=new ArrayList<Map<String,Object>>();
			conn.setAutoCommit(false);
			switch(up.getPropstype()){
			case 0://0可用
				switch(up.getPropsuse()){
				case 1://增体力
					p.setPower(p.getPower()+up.getPropsadd());
					if(p.getPower()>=p.getMaxpower()) p.setPowerstart(0);
					break;
				case 2://增夺计
					p.setGetbook(p.getGetbook()+up.getPropsadd());
					if(p.getGetbook()>=10) p.setGetskstart(0);
					break;
				case 3://增争斗
					p.setPknum(p.getPknum()+up.getPropsadd());
					break;
				}
				up.setPropsnum(up.getPropsnum()-1);//记录用后数量
				break;
			case 1://1礼包
				for(int i=0;i<up.getPackgoods().size();i++){
					Map<String,Object> map = new HashMap<String,Object>();
					packageGoods pkg=up.getPackgoods().get(i);
					switch(pkg.getInnertype()){
					case 0://钞票 P900
						p.setMoney(p.getMoney()+pkg.getGoodsnum());
						break;
					case 1://金条 P901
						p.setGold(p.getGold()+pkg.getGoodsnum());
						break;
					case 2://转生丹 P902
						p.setBall(p.getBall()+pkg.getGoodsnum());
						this.UpdateUserAct(p, 3, "礼包内"+GameManager.getInstance().getBallname(), pkg.getGoodsnum(), conn);
						break;
					case 10://10 卡牌 
						Iterator<UserCard> it = p.getCardlist().values().iterator();
						int cid=0;
						while(it.hasNext()){
							UserCard uc=it.next();
							if(uc.getCardcode().equals(pkg.getGoodscode())){
								cid=uc.getCardid();
								break;
							}
						}
						for(int j=0;j<pkg.getGoodsnum();j++){
							this.DownCard(p, pkg.getGoodscode(), cid, conn);
						}						
						break;
					case 11://11 装备 
						for(int j=0;j<pkg.getGoodsnum();j++){
							this.DownDevice(p, pkg.getGoodscode(), conn);
						}						
						break;
					case 12://12 技能
						for(int j=0;j<pkg.getGoodsnum();j++){
							this.DownSkill(p, pkg.getGoodscode(), conn);
						}						
						break;
					case 13:// 13 符文 
						for(int j=0;j<pkg.getGoodsnum();j++){
							this.DownMark(p, pkg.getGoodscode(), conn);
						}						
						break;
					case 14://道具
						this.DownProps(p, pkg.getGoodscode(), conn, pkg.getGoodsnum());
						break;
					case 15://碎片
						this.Downchip(p, pkg.getGoodscode(), pkg.getGoodsnum(), conn);
						break;
					}
					map.put("AA", pkg.getGoodscode());
					map.put("AB", pkg.getGoodsname());
					map.put("XP", 0);
					map.put("AC", pkg.getGoodsnum());
					map.put("AP", pkg.getGoodspic());
					if(pkg.getInnertype()==10){//卡牌
						switch(GameManager.getInstance().getCarddict().get(pkg.getGoodscode()).getStarlevel()){
						case 1:
						case 2:
							map.put("AR", 1);//白
							break;
						case 3:
						case 4:
							map.put("AR", 2);//绿
							break;
						case 5:
							map.put("AR", 3);//蓝
							break;
						case 6:
							map.put("AR", 4);//紫
							break;
						case 7:
							map.put("AR", 5);//橙
							break;							
						}
					}else if(pkg.getInnertype()==15){	
						ArrayList<ChipDict> al=GameManager.getInstance().getChipdictlist();
						for(int j=0;j<al.size();j++){
							if(al.get(i).getChipcode().equals(pkg.getGoodscode())){
								ChipDict cd= al.get(j);
								map.put("AR", cd.getChipcolor());//颜色
								break;
							}
						}
					}else if(pkg.getInnertype()==11) map.put("AR", GameManager.getInstance().getDevsetdict().get(pkg.getGoodscode()).getDevcolor());
								else if(pkg.getInnertype()==12) map.put("AR", GameManager.getInstance().getSkilldict().get(pkg.getGoodscode()).getSkillcolor());
									else map.put("AR", 0);
					mapal.add(map);
				}
				up.setPropsnum(up.getPropsnum()-1);//记录用后数量
				break;
			case 2: //2宝箱 3钥匙
				up.setPropsnum(up.getPropsnum()-1);//记录用后数量
				String keycode=up.getUsecond();
				p.getUserprop().get(keycode).setPropsnum(p.getUserprop().get(keycode).getPropsnum()-1);//记录对应钥匙减少
				int downflag=CalcDown(9+up.getPropsadd());
				//logger.info("掉率标志："+downflag);
				if(downflag>=0){
					ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(up.getPropsadd()+9);
					dd = al.get(downflag);
					Map<String,Object> map = new HashMap<String,Object>();
					procdowngoods(p,dd,map,conn);//处理掉落
					mapal.add(map);
				}
				break;
			case 4://随机掉落物品的礼包 downdict 20 新增功能2013-09-16 不用钥匙类似宝箱功能 冯
				up.setPropsnum(up.getPropsnum()-1);//记录用后数量
				int dflag=CalcDown(19+up.getPropsadd());
				//logger.info("掉率标志："+downflag);
				if(dflag>=0){
					ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(19+up.getPropsadd());
					dd = al.get(dflag);
					Map<String,Object> map = new HashMap<String,Object>();
					procdowngoods(p,dd,map,conn);//处理掉落
					mapal.add(map);
					//logger.info("开启节日礼包："+map.get("AP"));
				}				
				break;
			}
			sendMap.put("AA", p.getPower());
			sendMap.put("AB", p.getGetbook());
			sendMap.put("AC", p.getPknum());
			sendMap.put("AD", p.getGold());
			sendMap.put("AE", p.getMoney());
			sendMap.put("AH", p.getBall());
			sendMap.put("AF", up.getPropsnum());
			sendMap.put("AG", mapal.toArray());
			//临时增加功能 
			if(up.getPropstype()==4) sendMap.put("EF",2);
			else sendMap.put("EF", up.getPropstype());
			this.PlayerUpdate(p, conn,p.getGold()-gnum);
			String sql;
			if(up.getPropstype()==2){//开宝箱同时消耗钥匙
				if(p.getUserprop().get(up.getUsecond()).getPropsnum()<=0){
					sql="DELETE FROM userprops"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND propscode=? ";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, up.getUsecond());
					ps.executeUpdate();
					ps.close();
					p.getUserprop().remove(up.getUsecond());
				}else{
					sql="UPDATE userprops"+p.getSvrcode()+" SET propsnum=? WHERE rolename=? AND serverno=? AND propscode=?";
					ps=conn.prepareStatement(sql);
					ps.setInt(1, p.getUserprop().get(up.getUsecond()).getPropsnum());
					ps.setString(2, p.getUrole());
					ps.setString(3, p.getSvrcode());
					ps.setString(4, up.getUsecond());
					ps.executeUpdate();
					ps.close();
				}
			}
			if(up.getPropsnum()==0){
				sql="DELETE FROM userprops"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND propscode=? ";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3, up.getPropscode());
				ps.executeUpdate();
				ps.close();
				p.getUserprop().remove(up.getPropscode());
			}else{
				sql="UPDATE userprops"+p.getSvrcode()+" SET propsnum=? WHERE rolename=? AND serverno=? AND propscode=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, up.getPropsnum());
				ps.setString(2, p.getUrole());
				ps.setString(3, p.getSvrcode());
				ps.setString(4, up.getPropscode());
				ps.executeUpdate();
				ps.close();
			}		
			if(up.getPropstype()==2){
				String str="",mess="",ccode="";
				switch(up.getPropsadd()){
				case 1:
					str="木宝箱";
					break;
				case 2:
					str="铜宝箱";
					break;
				case 3:
					str="银宝箱";
					break;
				case 4:
					str="金宝箱";
					break;
				case 5:
					str="白金宝箱";
					break;
				}
				mess="人品爆发！玩家--"+p.getUrole()+"--在开启"+str+"后获得了";
				if(mapal.size()>0){
					switch(dd.getGoodstype()){
					case 0:
					case 1:
					case 2:
					case 3:
						if(dd.getGoodslevel()>=3){
							mess=mess+mapal.get(0).get("AB").toString()+"！";
							this.playerpubmess(p.getSvrcode(), mess);
						}						
						break;
					case 8:
						ccode=mapal.get(0).get("AA").toString();
						if(GameManager.getInstance().getCarddict().get(ccode).getStarlevel()>4){
							 mess=mess+GameManager.getInstance().getCarddict().get(ccode).getCardname()+"！";
							 this.playerpubmess(p.getSvrcode(), mess);
						}						
						break;
					}					
				}
			}
			//记录道具消耗
			sql="INSERT INTO  userpropuse"+p.getSvrcode()+"  VALUES(?,?,?,?,1,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUname());
			ps.setString(2, p.getUrole());
			ps.setString(3, p.getSvrcode());
			ps.setString(4, Long.toString(System.currentTimeMillis()));
			ps.setString(5, up.getPropscode());
			if(mapal.size()>0){
				String tempstr="";
				for(int i=0;i<mapal.size();i++){
					tempstr=tempstr+mapal.get(i).get("AB").toString()+"--"+mapal.get(i).get("AC").toString()+"|";
				}
				ps.setString(6,tempstr);//使用道具掉落内容
			}else	ps.setString(6,up.getPropsname());//道具名称
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void exchstart(User p,String ecode,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String batnostr=ecode.substring(0, 4);
			int batno=Integer.parseInt(batnostr);
			String sql="";
			conn.setAutoCommit(false);
			if(batno>=7000){//全服通用兑换码
				sql="SELECT 1 FROM exchtbl WHERE batno=? AND rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, batno);
				ps.setString(2, p.getUrole());
				ps.setString(3, p.getSvrcode());
				rs=ps.executeQuery();
				if(rs.next()){
					smap.put("CMD", PacketCommandType.GETERROR);
					smap.put("EC", "1064");//已兑换过
					rs.close();
					ps.close();
				}else{
					rs.close();
					ps.close();
					sql="SELECT * FROM exchtbl WHERE exchcode=? AND serverno='999'";
					ps=conn.prepareStatement(sql);
					ps.setString(1, ecode);
					rs=ps.executeQuery();
					if(rs.next()){
						int exptime=rs.getInt("expdate");
						int ifuse=rs.getInt("ifuse");
						String goodscode=rs.getString("goodscode");
						if(ifuse>0){//
							smap.put("CMD", PacketCommandType.GETERROR);
							smap.put("EC", "1054");//已使用
						}else{					
							int curtime=(int)(System.currentTimeMillis()/1000);
							if(curtime>exptime){//过期
								smap.put("CMD", PacketCommandType.GETERROR);
								smap.put("EC", "1053");//过期
							}else{//可用
								this.DownProps(p, goodscode, conn, 1);
								//logger.info("掉落code"+goodscode);
								PreparedStatement ps1 = null;
								String sql1="UPDATE exchtbl SET ifuse=1,rolename=?,serverno=? WHERE exchcode=? AND serverno='999'";
								ps1=conn.prepareStatement(sql1);
								ps1.setString(1, p.getUrole());
								ps1.setString(2, p.getSvrcode());
								ps1.setString(3, ecode);								
								ps1.executeUpdate();
								ps1.close();
								smap.put("CMD", PacketCommandType.RETURNEXCH);
								smap.put("EB", goodscode);
								smap.put("EF", GameManager.getInstance().getPropsdict().get(goodscode).getPropstype());
								smap.put("EG", GameManager.getInstance().getPropsdict().get(goodscode).getPropsname());
								smap.put("PP", GameManager.getInstance().getPropsdict().get(goodscode).getPropspic());
								smap.put("AA", p.getGold());
								smap.put("AB", p.getMoney());							
							}
						}
					}else{
						smap.put("CMD", PacketCommandType.GETERROR);
						smap.put("EC", "1052");//没有此兑换码
					}
					rs.close();
					ps.close();
				}
			}else{//分服兑换码
					sql="SELECT 1 FROM exchtbl WHERE rolename=? AND serverno=? AND batno=?";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setInt(3, batno);
					rs=ps.executeQuery();
					if(rs.next()){
						smap.put("CMD", PacketCommandType.GETERROR);
						smap.put("EC", "1064");//同批次已兑换过
						ps.close();
						rs.close();
					}else{
						rs.close();
						ps.close();
						sql="SELECT * FROM exchtbl WHERE exchcode=? AND serverno=?";
						ps=conn.prepareStatement(sql);
						ps.setString(1, ecode);
						ps.setString(2, p.getSvrcode());
						rs=ps.executeQuery();
						if(rs.next()){
							int exptime=rs.getInt("expdate");
							int ifuse=rs.getInt("ifuse");
							String goodscode=rs.getString("goodscode");
							if(ifuse>0){//
								smap.put("CMD", PacketCommandType.GETERROR);
								smap.put("EC", "1054");//已使用
							}else{					
								int curtime=(int)(System.currentTimeMillis()/1000);
								if(curtime>exptime){//过期
									smap.put("CMD", PacketCommandType.GETERROR);
									smap.put("EC", "1053");//过期
								}else{//可用
									
									this.DownProps(p, goodscode, conn, 1);
									//logger.info("掉落code"+goodscode);
									PreparedStatement ps1 = null;
									String sql1="UPDATE exchtbl SET ifuse=1,rolename=? WHERE exchcode=? AND serverno=?";
									ps1=conn.prepareStatement(sql1);
									ps1.setString(1, p.getUrole());
									ps1.setString(2, ecode);
									ps1.setString(3, p.getSvrcode());
									ps1.executeUpdate();
									ps1.close();
									smap.put("CMD", PacketCommandType.RETURNEXCH);
									smap.put("EB", goodscode);
									smap.put("EF", GameManager.getInstance().getPropsdict().get(goodscode).getPropstype());
									smap.put("EG", GameManager.getInstance().getPropsdict().get(goodscode).getPropsname());
									smap.put("PP", GameManager.getInstance().getPropsdict().get(goodscode).getPropspic());
									smap.put("AA", p.getGold());
									smap.put("AB", p.getMoney());							
								}
							}						
						}else{
							smap.put("CMD", PacketCommandType.GETERROR);
							smap.put("EC", "1052");//没有此兑换码
						}
						ps.close();
						rs.close();
					}
			}
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void excholduser(User p,String ecode,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		String sql1="";
		try{
			String batnostr=ecode.substring(0, 4);
			int batno=Integer.parseInt(batnostr);
			String sql="SELECT * FROM exchtbl WHERE exchcode=? AND serverno=? AND rolename=? AND batno=0";
			ps=conn.prepareStatement(sql);
			ps.setString(1,ecode);
			ps.setString(2, p.getSvrcode());
			ps.setString(3, p.getUname());
			rs=ps.executeQuery();
			if(rs.next()){
				int exptime=rs.getInt("expdate");
				int ifuse=rs.getInt("ifuse");
				String goodscode=rs.getString("goodscode");
				int addgold=rs.getInt("addgold");
				int addmoney=rs.getInt("addmoney");
				if(ifuse>0){//
					smap.put("CMD", PacketCommandType.GETERROR);
					smap.put("EC", "1054");//已使用
				}else{					
					int curtime=(int)(System.currentTimeMillis()/1000);
					if(curtime>exptime){//过期
						smap.put("CMD", PacketCommandType.GETERROR);
						smap.put("EC", "1053");//过期
					}else{//可用
						conn.setAutoCommit(false);
						this.DownProps(p, goodscode, conn, 1);
						if(addgold>0||addmoney>0){
							sql1="UPDATE userroletbl SET gold=gold+?,money=money+? WHERE username=? AND rolename=? AND serverno=?";
							ps1=conn.prepareStatement(sql1);
							ps1.setInt(1, addgold);
							ps1.setInt(2, addmoney);
							ps1.setString(3, p.getUname());
							ps1.setString(4, p.getUrole());
							ps1.setString(5, p.getSvrcode());
							ps1.executeUpdate();
							ps1.close();
							p.setGold(p.getGold()+addgold);
							p.setMoney(p.getMoney()+addmoney);
						}						
						sql1="UPDATE exchtbl SET ifuse=1 WHERE exchcode=? AND serverno=? AND rolename=? AND batno=0";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1,ecode);
						ps1.setString(2, p.getSvrcode());
						ps1.setString(3, p.getUname());
						ps1.executeUpdate();
						ps1.close();
						conn.commit();
						conn.setAutoCommit(true);
						smap.put("CMD", PacketCommandType.RETURNEXCH);
						smap.put("EB", goodscode);
						smap.put("EF", GameManager.getInstance().getPropsdict().get(goodscode).getPropstype());
						smap.put("EG", GameManager.getInstance().getPropsdict().get(goodscode).getPropsname());
						smap.put("PP", GameManager.getInstance().getPropsdict().get(goodscode).getPropspic()); 
						smap.put("AA", p.getGold());
						smap.put("AB", p.getMoney());						
					}
				}
				
			}else{
				smap.put("CMD", PacketCommandType.GETERROR);
				smap.put("EC", "1052");//没有此兑换码
			}			
			ps.close();
			rs.close();
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void getdaytask(User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			String sql="SELECT * FROM userdaytask"+p.getSvrcode()+" WHERE rolename=? AND serverno=? ORDER BY taskid";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			while(rs.next()){
				UserDayTask udt= new UserDayTask();
				udt.setTaskid(rs.getInt("taskid"));
				udt.setTaskdesc(rs.getString("taskdesc"));
				udt.setTaskcond(rs.getInt("taskcond"));
				udt.setTasktype(rs.getString("tasktype"));
				udt.setCurcond(rs.getInt("curcond"));
				udt.setIfsuccess(rs.getInt("ifsuccess"));
				udt.setMoney(rs.getInt("money"));
				udt.setBall(rs.getInt("ball"));
				udt.setGold(rs.getInt("gold"));
				p.getUserdaytasklist().add(udt);
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void rundaytask(User p,UserDayTask udaytask) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try{
			String sql="SELECT COUNT(1) countnum FROM userdaytask"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND ifsuccess=0";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());	
			rs=ps.executeQuery();
			if(rs.next()){
				p.setUndotask(rs.getInt("countnum")-1);
			}else p.setUndotask(0);
			conn.setAutoCommit(false);
			sql="UPDATE userdaytask"+p.getSvrcode()+" SET ifsuccess=1 WHERE rolename=? AND serverno=? AND taskid=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, udaytask.getTaskid());		
			ps.executeUpdate();
			ps.close();
			sql="UPDATE userroletbl SET money=money+?,gold=gold+?,ball=ball+? WHERE username=? AND rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1,udaytask.getMoney());
			ps.setInt(2, udaytask.getGold());
			ps.setInt(3, udaytask.getBall());
			ps.setString(4, p.getUname());
			ps.setString(5, p.getUrole());
			ps.setString(6, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			if(udaytask.getBall()>0) this.UpdateUserAct(p, 3, "日常任务"+GameManager.getInstance().getBallname(), udaytask.getBall(), conn);
			conn.commit();
			conn.setAutoCommit(true);
			p.setBall(p.getBall()+udaytask.getBall());
			p.setMoney(p.getMoney()+udaytask.getMoney());
			p.setGold(p.getGold()+udaytask.getGold());
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			p.setUndotask(p.getUndotask()+1);
			this.rollbackdaytask(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	//街霸完成结算
	public int FinishSBattle(User p, UserStreet us, int flag,int wlflag,Map<String,Object> semap,int dflag) throws SQLException{
		//获得掉落物品 code
		//更新用户信息 经验等级 队伍数。。。
		//更新阵容卡牌信息   经验升级或魅力改变重新运算卡牌
		//掉落物品进背包
		int skillid=0;
		int exp=0,mon=0;
		String downcode="@",downname;
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				conn.setAutoCommit(false);
				if(wlflag>0){//胜且有掉落
					if(flag>0){
						switch(flag){
						case 1://掉符文
							int k=0;
							int[] a=new int[GameManager.getInstance().getMarkdict().size()];
							int[] loc=new int[GameManager.getInstance().getMarkdict().size()];
							String[] dcode=new String[GameManager.getInstance().getMarkdict().size()];
							Iterator<MarkDict> it = GameManager.getInstance().getMarkdict().values().iterator();
							while(it.hasNext()){
								MarkDict md=it.next();
								a[k]=md.getDownrand();
								loc[k]=k;
								dcode[k]=md.getMarkcode();
								k=k+1;
							}
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
							int r=rd.nextInt(10000);
							for(int i=0;i<a.length;i++){
								minval=maxval;
								if(a[i]==0) break;
								maxval=maxval+a[i];
								if(r>=minval && r<maxval){
									downcode=dcode[loc[i]];
									break;
								}
							}
							downname=GameManager.getInstance().getMarkdict().get(downcode).getMarkname();
							DownMark(p, downcode, conn);
							semap.put("AF", downcode);
							semap.put("AE", 0);
							semap.put("AG", downname);
							break;
						case 2:
							downcode=us.getGoods1code();
							downname=GameManager.getInstance().getPropsdict().get(downcode).getPropsname();
							DownProps(p, downcode, conn,1);
							semap.put("AF", downcode);
							semap.put("AE", 0);
							semap.put("AG", downname);
							break;
						case 3:
							downcode=us.getGoods2code();
							downname=GameManager.getInstance().getPropsdict().get(downcode).getPropsname();
							DownProps(p, downcode, conn,1);
							semap.put("AF", downcode);
							semap.put("AE", 0);
							semap.put("AG", downname);
							break;
						}
					}
					//额外掉落
					int cursecond=(int)(System.currentTimeMillis()/1000);
					ArrayList<Map<String,Object>> mapal =new ArrayList<Map<String,Object>>();
					if(GameManager.getInstance().getSvractdict().containsKey(7)){
						if(cursecond>=GameManager.getInstance().getSvractdict().get(7).getBtime()&&cursecond<GameManager.getInstance().getSvractdict().get(7).getEtime()){
							int downflag=CalcDown(203);
							if(downflag>=0){
								ArrayList<downDict> al=	GameManager.getInstance().getDowndictl().get(203);
								downDict dd = al.get(downflag);
								Map<String,Object> map = new HashMap<String,Object>();
								procdowngoods(p,dd,map,conn);//处理掉落
								mapal.add(map);
							}
						}
					}
					semap.put("EX", mapal.toArray());
				}
				if(wlflag>0){
					exp=us.getGiveexp()*dflag;
					mon=us.getGivemoeny()*dflag;
					//logger.info("下一关卡id："+us.getNextid());
					p.setCurstreetid(us.getNextid());//胜利开启下一关
				}
				//日常任务完成情况
				UpdateDayMission(p,"霸王",conn);
				us.setWinlose(wlflag);				
				CalcCardFin(p, exp, conn);
				p.setMoney(p.getMoney()+mon);
				CalcUserStreetFin(p, exp, conn,semap);
				UpdateSteetMission(p, us, wlflag,semap, conn);
				conn.commit();
				conn.setAutoCommit(true);
				if(wlflag>0&&us.getStreetid()%5==0&&us.getStreetid()/20>=1){
					String mess="厉害！玩家--"+p.getUrole()+"--成功突破试炼第"+us.getStreetid()+"关，名利双收！";
					this.playerpubmess(p.getSvrcode(), mess);
				}
			}catch (SQLException e) {
				conn.rollback();
				conn.setAutoCommit(true);
				this.rollbackPlay(p);
				this.rollstreet(p);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();				
			}
		}
		
		return skillid;
	}
	//任务完成更新任务 用户类，用户关卡任务类，输赢标志，回包map，链接
	public void UpdateSteetMission(User p, UserStreet us,int wlflag,Map<String,Object> semap,Connection conn) throws SQLException{
		String sql="";
		PreparedStatement ps = null;
		try{
			if(wlflag>0){//胜利可以进入向下一关卡
				us.setIflock(1);//当前关卡锁死
				p.getUstreet().get(us.getNextid()).setIfcanuse(1);//下一关卡开放
				sql="UPDATE userstreet"+p.getSvrcode()+" SET ifcanuse=1 WHERE rolename=? AND serverno=? AND streetid=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setInt(3, us.getNextid());
				ps.executeUpdate();
				ps.close();	
				semap.put("AO",us.getNextid());
			}else{//输了还在同一场景同一任务关卡
				semap.put("AO",0);
			}
			sql ="UPDATE userstreet"+p.getSvrcode()+" SET curtime=?,winlose=?,iflock=? WHERE rolename=? AND serverno=? AND streetid=?"; //更新任务
			ps=conn.prepareStatement(sql);
			ps.setString(1, dateToString(new Date()));
			ps.setInt(2, wlflag);
			ps.setInt(3, us.getIflock());
			ps.setString(4, p.getUrole());
			ps.setString(5, p.getSvrcode());
			ps.setInt(6, us.getStreetid());
			ps.executeUpdate();
			ps.close();
		}catch(SQLException e){
			throw e;
		}
	}
	public void goldResetstreet(User p,int cost) throws SQLException{
		String sql="";
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		int lastcurstreetid=p.getCurstreetid();
		p.setCurstreetid(1);
		p.setResetstreet(p.getResetstreet()+1);
		p.setGold(p.getGold()-cost);
		try{
			conn.setAutoCommit(false);
			Iterator<UserStreet> it = p.getUstreet().values().iterator();
			while(it.hasNext()){
				UserStreet us=it.next();
				if (us.getStreetid()==1) us.setIfcanuse(1);//第一关设为开放
				else us.setIfcanuse(0);
				us.setIflock(0);//锁定状态清空
				us.setIfgold(1);//设置金条重置
			}
			PlayerUpdate(p, conn,cost*-1);
			sql="UPDATE userstreet"+p.getSvrcode()+" SET ifcanuse=IF(streetid=1,1,0),ifgold=1,iflock=0 WHERE rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,'街霸',?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, Long.toString(System.currentTimeMillis()));
			ps.setInt(4, cost);
			ps.setString(5, String.valueOf(p.getGold()));//培养后剩余金条数
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			p.setCurstreetid(lastcurstreetid);
			p.setResetstreet(p.getResetstreet()-1);
			p.setGold(p.getGold()+cost);
			this.rollstreet(p);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
	}
	public void goldResetmissions(User p,int cost) throws SQLException{
		String sql="";
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		try{
			conn.setAutoCommit(false);
			sql="UPDATE userroletbl SET taskcd=0,gold=gold-? WHERE username=? AND rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, cost);
			ps.setString(2, p.getUname());
			ps.setString(3, p.getUrole());
			ps.setString(4, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,'战斗CD重置',?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, Long.toString(System.currentTimeMillis()));
			ps.setInt(4, cost);
			ps.setString(5, String.valueOf(p.getGold()));//培养后剩余金条数
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
			p.setTaskcdval(0);
			p.setGold(p.getGold()-cost); 
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
	}
	public void getpiclist(User p,Map<String,Object> smap,int pgnum) throws SQLException{
		String sql="";
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		if(pgnum>GameManager.getInstance().getCardgetall().size()){
			pgnum=1;
		}
		try{
			sql="SELECT * FROM userpicindex"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND pageinx=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, pgnum);
			rs=ps.executeQuery();
			ArrayList<Map<String,Object>> al=new ArrayList<Map<String,Object>>();
			String ctype="";
			while(rs.next()){
				String cardcode=rs.getString("cardcode");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("AB", cardcode);
				map.put("AC", rs.getInt("curstyle"));
				map.put("AI", rs.getInt("ifuse"));				
				CardDict uc=GameManager.getInstance().getCarddict().get(cardcode);
				map.put("AD", uc.getCardname());
				map.put("AE", uc.getCardprop());
				ctype=uc.getCardtype();
				map.put("AF", uc.getCardtype());
				map.put("AG", uc.getStarlevel());
				int ohp=0,oatk=0,odef=0;
				ohp=uc.getStarlevel()*25+(int)(uc.getBasehp()*10)+(int)(uc.getStarlevel()*6+uc.getBasehp()+8);
				oatk=uc.getStarlevel()*15+(int)(uc.getBaseatk()*4)+(int)(uc.getStarlevel()*2+uc.getBaseatk());
				odef=(int)(uc.getStarlevel()*6.5)+(int)(uc.getBasedef()*2)+(int)(uc.getStarlevel()*0.5+uc.getBasedef());
				map.put("AJ", ohp);
				map.put("AK", oatk);
				map.put("AL", odef);
				map.put("AM", uc.getCarddesc());
				ArrayList<Map<String,Object>> luckal=new ArrayList<Map<String,Object>>();
				//判断缘分装备
				if(GameManager.getInstance().getCarddevicedict().containsKey(cardcode)){
					Iterator<lotluck> luckit = GameManager.getInstance().getCarddevicedict().get(cardcode).values().iterator();
					while(luckit.hasNext()){
						lotluck ll=luckit.next();
						Map<String,Object> luckmap = new HashMap<String,Object>();
						luckmap.put("LB", ll.getLuckname());
						luckmap.put("LC", ll.getLuckdesc());
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
						luckal.add(luckmap);
					}				
				}
				//判断缘分卡牌
				if(GameManager.getInstance().getCardluckdict().containsKey(cardcode)){
					Iterator<lotluck> cardit = GameManager.getInstance().getCardluckdict().get(cardcode).values().iterator();
					while(cardit.hasNext()){
						lotluck ll = cardit.next();
						Map<String,Object> luckmap = new HashMap<String,Object>();
						luckmap.put("LB", ll.getLuckname());
						luckmap.put("LC", ll.getLuckdesc());
						luckal.add(luckmap);
					}				
				}
				map.put("LA", luckal.toArray());
				al.add(map);				
			}
			smap.put("AA", al.toArray());
			smap.put("AH", ctype);
			smap.put("AN", GameManager.getInstance().getCardgetall().get(ctype).getAlldesc());
			smap.put("AO", GameManager.getInstance().getCardgetall().size());
			smap.put("AP", GameManager.getInstance().getCardgetall().get(ctype).getTypedesc());			
			ps.close();
			rs.close();			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
	}
	public void shaketree(User p,int shakecost) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		String sql="UPDATE userroletbl SET money=?,gold=gold-?,goldtree=? WHERE username=? AND rolename=? AND serverno=?";
		try{
			conn.setAutoCommit(false);
			ps=conn.prepareStatement(sql);
			ps.setInt(1, p.getMoney());
			ps.setInt(2, shakecost);
			ps.setInt(3, p.getGoldtree());
			ps.setString(4, p.getUname());
			ps.setString(5, p.getUrole());
			ps.setString(6, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,'摇钱',?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, Long.toString(System.currentTimeMillis()));
			ps.setInt(4, shakecost);
			ps.setString(5, String.valueOf(p.getGold()));//培养后剩余金条数
			ps.executeUpdate();
			ps.close();			
			this.UpdateDayMission(p, "摇钱", conn);
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollPlayerInfo(p);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
	}
	public void getpointaward(User p,Cpointdict cpd,Map<String,Object> map) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		p.setMoney(p.getMoney()+cpd.getCostpoint());
		String sql="SELECT 1 FROM useraward WHERE rolename=? AND serverno=? AND exchid=?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, cpd.getExchid());
			rs=ps.executeQuery();
			if(rs.next()){//已领取
				map.put("CMD", PacketCommandType.GETERROR);
				map.put("EC", "1060");//奖励已领取
				rs.close();
				ps.close();
			}else{
				rs.close();
				ps.close();
				conn.setAutoCommit(false);
				sql="INSERT INTO useraward VALUES(?,?,?,?)";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setInt(3,cpd.getExchid());
				ps.setString(4, dateToString(new Date()));
				ps.executeUpdate();
				ps.close();
				sql="UPDATE userroletbl SET money=? WHERE username=? AND rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, p.getMoney());
				ps.setString(2, p.getUname());
				ps.setString(3, p.getUrole());
				ps.setString(4, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
				conn.commit();
				conn.setAutoCommit(true);
				map.put("AA", p.getMoney());
				map.put("AB", p.getBall());
				map.put("AC", p.getCurpoints());
				map.put("AE", "@");
				map.put("AF", "@");
				map.put("CMD", PacketCommandType.FINISHEXCH);
			}
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			p.setMoney(p.getMoney()-cpd.getCostpoint());
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
	}
	public void exchangepoint(User p,Cpointdict cpd,Map<String,Object> semap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		CallableStatement cs = null;
		int cid=0,pnum=0,ifupdate=0;
		String downcode="@",downname="";
		try{
			if(p.getCurpoints()<cpd.getCostpoint()){
				semap.put("CMD", PacketCommandType.GETERROR);
				semap.put("EC", "1061");//积分不够
			}else{
				conn.setAutoCommit(false);
				p.setCurpoints(p.getCurpoints()-cpd.getCostpoint());
				if(cpd.getGoodstype()>1){//41传说武器42传说防具43传说饰品	31稀有武器32稀有防具33稀有饰品10符文49技能  
					cs = conn.prepareCall("{call DOWNSOME(?,?,?,?,?,?,?,?,?)}"); //要加参数
					cs.setString(1, p.getUname());
					cs.setString(2, p.getUrole());
					cs.setString(3,p.getSvrcode());
					cs.setInt(4,cpd.getGoodstype());
					if(cpd.getGoodstype()==10){//符文掉率单独计算
						int k=0;
						int[] a=new int[GameManager.getInstance().getMarkdict().size()];
						int[] loc=new int[GameManager.getInstance().getMarkdict().size()];
						String[] dcode=new String[GameManager.getInstance().getMarkdict().size()];
						Iterator<MarkDict> it = GameManager.getInstance().getMarkdict().values().iterator();
						while(it.hasNext()){
							MarkDict md=it.next();
							a[k]=md.getDownrand();
							loc[k]=k;
							dcode[k]=md.getMarkcode();
							k=k+1;
						}
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
						int r=rd.nextInt(10000);
						for(int i=0;i<a.length;i++){
							minval=maxval;
							if(a[i]==0) continue;
							maxval=maxval+a[i];
							if(r>=minval && r<maxval){
								downcode=dcode[loc[i]];
								break;
							}
						}
					}
					if(cpd.getGoodstype()==10) cs.setString(5, downcode); else	cs.setString(5, "@"); //掉落code
					cs.registerOutParameter(5, Types.VARCHAR);//掉落code
					cs.registerOutParameter(6, Types.INTEGER);//卡牌id，>0表明已有相同卡牌 魅力变化
					cs.registerOutParameter(7, Types.INTEGER);//>0 技能页号
					cs.registerOutParameter(8, Types.INTEGER);//技能书页是否新
					cs.registerOutParameter(9, Types.VARCHAR);//掉落物品名称
					cs.execute();
					downcode=cs.getString(5);
					cid=cs.getInt(6);
					pnum=cs.getInt(7);
					ifupdate=cs.getInt(8);	
					downname=cs.getString(9);
					cs.close();
					//logger.info("掉落code:"+downcode+"名称："+downname);
				}
				semap.put("AR", 0);
				switch(cpd.getGoodstype()){
				case 1://0排名领取 1转生丹	41传说武器42传说防具43传说饰品	31稀有武器32稀有防具33稀有饰品10符文49技能  
					p.setBall(p.getBall()+1);
					this.UpdateUserAct(p, 3, "积分兑换"+GameManager.getInstance().getBallname(), 1, conn);
					semap.put("AE", GameManager.getInstance().getBallname());
					semap.put("AF", "P902");
					break;
				case 31://装备
				case 32:
				case 33:
				case 41:
				case 42:
				case 43:
					DownDevice(p,downcode,conn);
					semap.put("AE", downname);
					semap.put("AF", downcode);
					semap.put("AR", GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor());
					break;
				case 5://卡牌					
				case 6:
				case 7:
				case 8:
				case 9:
					DownCard(p, downcode, cid, conn);
					semap.put("AE", downname);
					semap.put("AF", downcode);
					semap.put("AR",GameManager.getInstance().getCarddict().get(downcode).getStarlevel());
					break;
				case 10://符文
					downname=GameManager.getInstance().getMarkdict().get(downcode).getMarkname();
					DownMark(p, downcode, conn);
					semap.put("AE", downname);
					semap.put("AF", downcode);
					//logger.info("掉落code:"+downcode+"名称："+downname);
					break;
				case 49://技能
					DownSkill(p, downcode, conn);
					semap.put("AE", downname);
					semap.put("AF", downcode);
					semap.put("AR", GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor());
					break;
				}
				String sql="UPDATE userroletbl SET ball=?,curpoints=? WHERE username=? AND rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, p.getBall());
				ps.setInt(2, p.getCurpoints());
				ps.setString(3, p.getUname());
				ps.setString(4, p.getUrole());
				ps.setString(5, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
				semap.put("AA", p.getMoney());
				semap.put("AB", p.getBall());
				semap.put("AC", p.getCurpoints());
				semap.put("CMD", PacketCommandType.FINISHEXCH);
				conn.commit();
				conn.setAutoCommit(true);
			}
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}		
	}
	public int checkpointaward(User p,int exchid) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		int flag=0;
		String sql="SELECT 1 FROM useraward WHERE rolename=? AND serverno=? AND exchid=?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, exchid);
			rs=ps.executeQuery();
			if(rs.next()){//已领取
				flag= -1;
			}else{
				flag= 1;
			}
			rs.close();
			ps.close();
			sql="SELECT curpoints FROM userroletbl WHERE username=? AND rolename=? AND serverno=? ";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUname());
			ps.setString(2, p.getUrole());
			ps.setString(3, p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				p.setCurpoints(rs.getInt("curpoints"));
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
		//logger.info("是否可领取"+flag+"奖励id："+exchid);
		return flag;
	}
	public void updateOnline(int onlinenum,String svrcode) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		long cursecond=System.currentTimeMillis()/1000;
		try{
			String sql="INSERT INTO onlinelog VALUES(?,?,?)";
			ps=conn.prepareStatement(sql);
			ps.setInt(1,(int)cursecond);
			ps.setInt(2, onlinenum);
			ps.setString(3, svrcode);
			ps.executeUpdate();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
		
	}
	public int getsysmessage(User p,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		int flag=0,bandflag=0;
		long cursecond=System.currentTimeMillis()/1000;
		try{
			ArrayList<Map<String,Object>> al =new ArrayList<Map<String,Object>>();
			String sql="SELECT * FROM pubmess WHERE serverno=? AND messtype<=1 AND messid>? AND exptime>? AND (usemess='@' OR usemess=?) ORDER BY messid";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getSvrcode());
			ps.setInt(2,p.getCurmessid());
			ps.setInt(3, (int)cursecond);			
			ps.setString(4, p.getUrole());
			rs=ps.executeQuery();
			while(rs.next()){
				flag=rs.getInt("messid");//记录最新消息id
				Map<String,Object> map = new HashMap<String,Object>();
				if(rs.getInt("messtype")==1){
					bandflag=1;
					p.setGod(1);//封号
					break;
				}else{
					map.put("AB", rs.getString("messcont"));
					map.put("AC", rs.getInt("pubnum"));
					al.add(map);
				}
			}
			smap.put("AA", al.toArray());
			ps.close();
			rs.close();
			if(flag>0){
				if(bandflag==0) bandflag=2;
				sql="UPDATE userroletbl SET curmessid=?,god=? WHERE username=? AND rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, flag);
				ps.setInt(2, p.getGod());
				ps.setString(3, p.getUname());
				ps.setString(4, p.getUrole());
				ps.setString(5, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
				p.setCurmessid(flag);
			}			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();				
		}
		return bandflag;
	}
	//回滚用户信息
	public void rollbackPlay(User p) throws SQLException{
		try{
			p.getUmarklist().clear();
			p.getUskilllist().clear();
			p.getUdevicelist().clear();
			p.getInusecards().clear();
			for(int i=0;i<p.getCardlocid().length;i++){
				p.getCardlocid()[i]=0;
			}
			p.getCardlist().clear();
			p.getSkillpagelist().clear();
			p.getUserprop().clear();
			p.getUmisson().clear();
			p.getScemisson().clear();
			getPlayerInfo(p, p.getUname(), p.getUrole(),p.getSvrcode());
			/*private Map<Integer,UserStreet> ustreet;//用户街霸挑战列表<街霸id，街霸>
			*/
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}		
	}
	//回滚卡牌信息
	public void rollbackCard(User player) throws SQLException{
		player.getInusecards().clear();
		for(int i=0;i<player.getCardlocid().length;i++){
			player.getCardlocid()[i]=0;
		}
		player.getCardlist().clear();
		try{
			this.getPlayerUseCard(player,player.getUrole(),player.getSvrcode());
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}		
	}
	//回滚装备信息
	public void rollbackDevice(User p) throws SQLException{
		p.getUdevicelist().clear();
		try{
			this.getPlayerDevice(p, p.getUrole(), p.getSvrcode());
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}			
	}
	//回滚技能书页信息
	public void rollbackSkillP(User p) throws SQLException{
		p.getSkillpagelist().clear();
		try{
			this.getPlayerSkillPage(p, p.getUrole(), p.getSvrcode());
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}	
	}
	//回滚技能信息
	public void rollbackSkill(User p) throws SQLException{
		p.getUskilllist().clear();		
		try{
			this.getPlayerSkill(p, p.getUrole(), p.getSvrcode());	
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}	
	}
	//回滚符文信息
	public void rollbackMark(User p) throws SQLException{
		p.getUmarklist().clear();
		try{
			this.getPlayerMark(p, p.getUrole(), p.getSvrcode());		
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}	
	}
	//回滚日常任务
	public void rollbackdaytask(User p) throws SQLException{
		p.getUserdaytasklist().clear();
		try{
			this.getdaytask(p);		
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}	
	}
	//回滚扭蛋
	public void rollbackegg(User p)throws SQLException{
		p.getUseregg().clear();
		try{
			this.getEgglist(p);
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}	
	}
	//回滚商城
	public void rollbackshop(User p)throws SQLException{
		p.getUseregg().clear();
		try{
			this.getEgglist(p);
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}	
	}
	//回滚道具
	public void rollbackprop(User p)throws SQLException{
		p.getUserprop().clear();
		try{
			this.getpropslist(p);
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}	
	}	
	public void getEgglist(User p) throws SQLException{
		Connection conn = pool.getConnection();
		String sql="SELECT * FROM useropentbl t1,eggdict t2 WHERE t1.eggcode=t2.eggcode AND t1.serverno=t2.serverno AND t1.rolename=? AND t1.serverno=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,p.getUrole() );
			ps.setString(2,p.getSvrcode() );
			rs = ps.executeQuery();
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>(); 
				UserEgg ug = new UserEgg();
				ug.setRolename(rs.getString("rolename"));
				ug.setServerno(rs.getString("serverno"));
				ug.setEggcode(rs.getString("eggcode"));
				ug.setEggname(rs.getString("eggname"));
				ug.setStarttime(rs.getInt("starttime"));
				ug.setOpennum(rs.getInt("opennum"));//金币开蛋次数
				ug.setRemainnum(rs.getInt("remainnum"));
				ug.setStar1(rs.getInt("star1"));
				ug.setStar2(rs.getInt("star2"));
				ug.setStar3(rs.getInt("star3"));
				ug.setStar4(rs.getInt("star4"));
				ug.setStar5(rs.getInt("star5"));
				ug.setStar6(rs.getInt("star6"));
				ug.setDaynum(rs.getInt("daynum"));
				ug.setStepsec(rs.getInt("stepsec"));
				ug.setFirstdown(rs.getInt("firstdown"));
				ug.setCost(rs.getInt("cost"));		
				if(ug.getStarttime()>0){//计时中
					int k=(int)(System.currentTimeMillis()/1000);
					if((k-ug.getStarttime())>=ug.getStepsec()){//时间到可以开
						ug.setIfopen(1);
						ug.setRemaintime(0);
					}else{
						ug.setIfopen(0);
						ug.setRemaintime(ug.getStepsec()+ug.getStarttime()-k);
					}
				}else{
					if(ug.getRemainnum()>0)	ug.setIfopen(1); 
					else ug.setIfopen(0);
				}						
				p.getUseregg().put(ug.getEggcode(), ug);
			}		
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void rollguangong(User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM uservisitgg WHERE rolename=? AND serverno=?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				p.setLastvisit(rs.getString("visitdate"));
				p.setVisitnum(rs.getInt("visitnum"));
				if(checklog(p.getLastvisit(),dateToString(new Date()))>1) p.setVisitnum(0);
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	//回滚街霸列表
	public void rollstreet(User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="SELECT * FROM userstreet"+p.getSvrcode()+" WHERE rolename=? AND serverno=? ORDER BY streetid";
		try{
			p.getUstreet().clear();
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2,p.getSvrcode());
			rs=ps.executeQuery();
			while(rs.next()){
				UserStreet ust= new UserStreet();
				ust.setRolename(rs.getString("rolename"));
				ust.setServerno(rs.getString("serverno"));
				ust.setStreetid(rs.getInt("streetid"));
				ust.setCurtime(rs.getString("curtime"));
				ust.setWinlose(rs.getInt("winlose"));
				ust.setIfcanuse(rs.getInt("ifcanuse"));
				ust.setIflock(rs.getInt("iflock"));
				ust.setIfgold(rs.getInt("ifgold"));
				StreetDict sd = GameManager.getInstance().getStreetdict().get(ust.getStreetid());
				ust.setStreetdesc(sd.getStreetdesc());
				ust.setGiveexp(sd.getGiveexp());
				ust.setGivemoeny(sd.getGivemoeny());
				ust.setNextid(sd.getNextid());
				ust.setGold(sd.getGold());
				ust.setMarks(sd.getMarks());
				ust.setGoods1(sd.getGoods1());
				ust.setGoods1code(sd.getGoods1code());
				ust.setGoods2(sd.getGoods2());
				ust.setGoods2code(sd.getGoods2code());
				//取关卡阵容
				ArrayList<CMissionNpcDict> sl= GameManager.getInstance().getStreetnpcdict().get(ust.getStreetid());
				ust.getSnpclist().clear(); 
				for(int i=0;i<sl.size();i++){
					CMissionNpcDict mnpc =sl.get(i).clone();
					ust.getSnpclist().add(mnpc);
				}
				p.getUstreet().put(ust.getStreetid(), ust);
			}			
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void rollPlayerInfo(User player) throws SQLException {
			String sql ="SELECT * FROM userroletbl WHERE USERNAME=? AND rolename=? AND SERVERNO=?";
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = pool.getConnection();
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, player.getUname());
				ps.setString(2, player.getUrole());
				ps.setString(3, player.getSvrcode());
				rs = ps.executeQuery();
				if(rs.next()){
					player.setFirstlogin(rs.getString("firstlogin"));
					player.setCurrlogin(rs.getString("currlogin"));//最近登录
					player.setLevel(rs.getInt("level"));
					player.setExp(rs.getInt("exp")); //经验
					player.setPower(rs.getInt("power")); //当前体力值
					player.setMaxpower(rs.getInt("maxpower")); //体力值上限
					player.setMembers(rs.getInt("members")); //	队伍上限	
					player.setPowerstart(rs.getInt("powerstart"));//体力计时
					player.setPerpoint(rs.getInt("perpoint")); //每点恢复时间 秒级时间戳
					player.setLevelup(rs.getInt("levelup")); //升级经验值
					player.setViplevel(rs.getInt("viplevel")); //vip等级
					player.setMoney(rs.getInt("money")) ; //钞票
					player.setGold(rs.getInt("gold")); //金条
					player.setInnercoin(rs.getInt("innercoin")) ; //修改为是否领取连续登陆奖
					player.setGod(rs.getInt("god")) ; //女神泪
					if(player.getGod()>0) throw new SQLException("banduser");//封停用户
					player.setBall(rs.getInt("ball")) ; //转生丹
					player.setGoldrenew(rs.getInt("goldrenew")) ; //金条恢复次数
					player.setGetbook(rs.getInt("getbook")); //夺书次数
					player.setGoldaddnum(rs.getInt("goldaddnum")) ;//金条补充次数
					player.setTrade(rs.getInt("trade")) ; //新手引导标记
					player.setAddvalue(rs.getInt("addvalue"));//补正百分比
					player.setCurmission(rs.getInt("curmission")); //当前关卡id
					player.setMissionsum(rs.getInt("missionsum"));//任务关卡次数
					player.setPknum(rs.getInt("playerpknum"));//pk次数
					player.setGetskstart(rs.getInt("getskstart"));
					player.setEveryskp(rs.getInt("everyskp"));
					player.setRankseq(rs.getInt("currank"));
					player.setCurpoints(rs.getInt("curpoints"));
					player.setFriendsum(rs.getInt("friendsum"));
					player.setBuyfight(rs.getInt("buyfight"));//购买争斗次数
					player.setGoldtree(rs.getInt("goldtree"));
					player.setResetstreet(rs.getInt("resetstreet"));
					player.setResetshop(rs.getInt("resetshop"));
					player.setCurstreetid(rs.getInt("curstreetid"));
					player.setCurdesid(rs.getInt("curdesid"));
					player.setCurmessid(rs.getInt("curmessid"));//最新系统消息id
					player.setChargenum(rs.getInt("chargnum"));
					player.setContinuelog(rs.getInt("continuelog"));//连续登录天数
					player.setBosscdval(rs.getInt("bosscd"));//世界boss cd
					player.setTaskcdval(rs.getInt("taskcd"));//连闯任务 cd	
					player.setBlessnum(rs.getInt("blessnum"));//当日已祈愿次数
					player.setBlessall(Integer.parseInt(GameManager.getInstance().getShakedict().get(player.getViplevel()).get("AL").toString()));//免费次数
					player.setAutoboss(rs.getInt("autoboss"));
					player.setAdvps(rs.getInt("advps"));
					player.setStdps(rs.getInt("stdps"));
					player.setFriendpknum(rs.getInt("friendpknum"));
				}
			} 
			catch (SQLException e) {
				logger.info(e.getMessage());
				throw e;
			}
			finally{
				conn.close();
			}		
		}
	public void getloginmess(String svrcode) throws SQLException{
		String sql ="SELECT * FROM pubmess WHERE SERVERNO=? AND messtype>=2 AND pubnum>0 ORDER BY starttime DESC ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			int nowstamp=(int)(System.currentTimeMillis()/1000);
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.setString(1, svrcode);
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getInt("messtype")==3){//1小时后解封账号
					String urole=rs.getString("usemess");
					String sql1="UPDATE userroletbl SET god=0 WHERE rolename=? AND serverno=?";
					PreparedStatement ps1 = null;
					ps1=conn.prepareStatement(sql1);
					ps1.setString(1, urole);
					ps1.setString(2, svrcode);
					ps1.executeUpdate();
					ps1.close();
					sql1="UPDATE pubmess SET pubnum=0 WHERE serverno=? AND messtype=3 AND usemess=?";
					ps1=conn.prepareStatement(sql1);
					ps1.setString(1, svrcode);
					ps1.setString(2, urole);
					ps1.executeUpdate();
					ps1.close();
				}else{
					
					if(GameManager.getInstance().getLogmessdict().containsKey(rs.getInt("messid"))){
						messageClass mclass = GameManager.getInstance().getLogmessdict().get(rs.getInt("messid"));
						if(rs.getInt("starttime")<=nowstamp&&rs.getInt("exptime")>nowstamp){//生效
							mclass.setMesscont(rs.getString("messcont"));
							mclass.setMesstitle(rs.getString("messtitle"));
							mclass.setExptime(rs.getInt("exptime"));
						}else{
							GameManager.getInstance().getLogmessdict().remove(rs.getInt("messid"));
						}
					}else{
						if(rs.getInt("starttime")<=nowstamp&&rs.getInt("exptime")>nowstamp){//生效
							messageClass mclass =new messageClass();
							mclass.setMessid(rs.getInt("messid"));
							mclass.setMesstitle(rs.getString("messtitle"));
							mclass.setMesstype(rs.getInt("messtype"));
							mclass.setMesscont(rs.getString("messcont"));
							mclass.setServerno(rs.getString("serverno"));
							mclass.setExptime(rs.getInt("exptime"));
							GameManager.getInstance().getLogmessdict().put(mclass.getMessid(), mclass);
						}						
					}
					
				}				
			}
			rs.close();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
		} 
		catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
	}
	public void getmailbox(User p) throws SQLException{
		String sql ="SELECT * FROM usermailbox WHERE rolename=? AND serverno=? AND ifuse<2";
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs = ps.executeQuery();
			p.getUsermailbox().clear();
			while(rs.next()){
				UserMailBox umb=new UserMailBox();
				umb.setMailid(rs.getInt("mailid"));
				umb.setMesscont(rs.getString("messcont"));
				umb.setGoodscode(rs.getString("goodscode"));
				umb.setPiccode(rs.getString("piccode"));
				umb.setGivenum(rs.getInt("givenum"));
				umb.setGoodstype(rs.getInt("goodstype"));
				umb.setGivegold(rs.getInt("givegold"));
				umb.setGivemoney(rs.getInt("givemoney"));
				umb.setMailtop(rs.getString("mailtop"));
				umb.setIfuse(rs.getInt("ifuse"));
				p.getUsermailbox().put(umb.getMailid(), umb);
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void openmail(User p,int mid) throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		int flag=0;
		UserMailBox umb=p.getUsermailbox().get(mid);
		if(umb.getIfuse()<1){
			p.setGold(p.getGold()+umb.getGivegold());
			p.setMoney(p.getMoney()+umb.getGivemoney());
		}		
		try {
			conn.setAutoCommit(false);	
			if(umb.getIfuse()<1){
				if(!umb.getGoodscode().equals("@")){//有物品发放
					switch(umb.getGoodstype()){
					case 0://道具
						this.DownProps(p, umb.getGoodscode(), conn, umb.getGivenum());
						break;
					case 1://技能
						this.DownSkill(p, umb.getGoodscode(), conn);
						break;
					case 2://装备
						this.DownDevice(p, umb.getGoodscode(), conn);
						break;
					case 3://符文
						this.DownMark(p, umb.getGoodscode(), conn);
						break;
					case 4://卡牌
						int cid=0;
						Iterator<UserCard> it =p.getCardlist().values().iterator();
						while(it.hasNext()){
							UserCard tempuc=it.next();
							if(tempuc.getCardcode().equals(umb.getGoodscode())){
								cid=tempuc.getCardid();
								break;
							}
						}
						this.DownCard(p, umb.getGoodscode(), cid, conn);
						break;
					case 5://转生丹
						p.setBall(p.getBall()+umb.getGivenum());
						this.UpdateUserAct(p, 3, "邮件附赠"+GameManager.getInstance().getBallname(), umb.getGivenum(), conn);
						break;
					case 6://碎片
						this.Downchip(p, umb.getGoodscode(), umb.getGivenum(), conn);
						break;
					}					
				}
				if(umb.getGivegold()>0||umb.getGivemoney()>0||umb.getGoodstype()==5)
					this.PlayerUpdate(p, conn,umb.getGivegold());
				if(umb.getGoodscode().equals("@")&&umb.getGivegold()==0&&umb.getGivemoney()==0) flag=2;
				else flag=1;
			}else flag=2;	
			//logger.info("领取标志："+flag+"消息id"+mid);
			String sql ="UPDATE usermailbox SET ifuse=? WHERE mailid=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1,flag);
			ps.setInt(2,mid);
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
			if(flag==1) umb.setIfuse(1);
			if(flag==2)	p.getUsermailbox().remove(mid);
		} 
		catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			if(umb.getIfuse()<1){
				if(!umb.getGoodscode().equals("@")) this.rollbackprop(p);
				if(umb.getGivegold()>0) p.setGold(p.getGold()-umb.getGivegold());
				if(umb.getGivemoney()>0) p.setMoney(p.getMoney()-umb.getGivemoney());
			}			
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
	}
	public void playerpubmess(String svrcode,String mess){
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		int exptime=(int)(System.currentTimeMillis()/1000)+24*60*60;
		String sql="INSERT INTO pubmess VALUES(NULL,?,'@',0,?,?,'@',0,1)";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, svrcode);
			ps.setString(2, mess);
			ps.setInt(3,exptime);
			ps.executeUpdate();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
		}
		finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String reqcharge(User p ,int goldnum) throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		String orderid=p.getSvrcode()+p.getUname()+Long.toString(System.currentTimeMillis());
		String sql="INSERT INTO usercharge VALUES(?,?,?,?,?,?,?,-1,?,'')";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, orderid);
			ps.setString(2, p.getUname());
			ps.setString(3, p.getUrole());
			ps.setString(4, p.getSvrcode());
			ps.setString(5, dateToString(new Date()));
			ps.setInt(6, 0);
			ps.setInt(7, goldnum);
			ps.setInt(8, p.getGold());
			ps.executeUpdate();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
		return orderid;
	}
	public void chargeend(User p)  throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();		
		ResultSet rs = null;
		String sql="SELECT gold,viplevel,friendsum,chargnum FROM userroletbl WHERE username=? AND rolename=? AND serverno=?";
		try{
			if(p.getChargenum()==0){//首冲，刷新道具
				this.rollbackprop(p);
			}
			int oldvip=p.getViplevel();
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUname());
			ps.setString(2, p.getUrole());
			ps.setString(3, p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				p.setGold(rs.getInt("gold"));
				p.setBuylimit(Integer.parseInt(GameManager.getInstance().getShakedict().get(rs.getInt("viplevel")).get("AG").toString()));
				p.setMaxgoldtree(Integer.parseInt(GameManager.getInstance().getShakedict().get(rs.getInt("viplevel")).get("AB").toString()));
				p.setFriendsum(rs.getInt("friendsum"));
				p.setViplevel(rs.getInt("viplevel"));
				p.setChargenum(rs.getInt("chargnum"));
			}
			rs.close();
			ps.close();
			if(p.getViplevel()>oldvip&&p.getViplevel()>=4){//vip等级发生变化
				String mess="恭喜！玩家--"+p.getUrole()+"--达到VIP"+p.getViplevel()+"，特权满载，傲视江湖！";
				this.playerpubmess(p.getSvrcode(), mess);
			}
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	//appstore 充值发货
	public void chargesucc(User p,String oid,String appid)  throws SQLException{
		Connection conn = pool.getConnection();		
		CallableStatement cs = null;
		try{
			cs = conn.prepareCall("{call APPCHARGESUCC(?,?,?)}");
			cs.setString(1,oid);
			cs.setString(2, appid);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.execute();
			//logger.info("参数"+player.getPower()+","+player.getPowerstart()+","+player.getGoldrenew()+","+player.getGetbook()+","+player.getGoldaddnum()+","+flag);
			//logger.info("返回值："+cs.getInt(3));
			if(cs.getInt(3)==0){
				cs.close();
				throw new SQLException("charge error");
			}
			cs.close();
			chargeend(p);
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void loginaward(User p,int contlog) throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		try{
			conn.setAutoCommit(false);
			LoginDict ld=GameManager.getInstance().getLogdict().get(contlog);
			this.DownProps(p, ld.getDowncode(), conn, ld.getDownnum());
			String sql="UPDATE userroletbl SET innercoin=? WHERE username=? AND rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			switch(contlog){
			case 1:
				p.setInnercoin(p.getInnercoin()+10000);
				break;
			case 2:
				p.setInnercoin(p.getInnercoin()+1000);
				break;
			case 3:
				p.setInnercoin(p.getInnercoin()+100);
				break;
			case 4:
				p.setInnercoin(p.getInnercoin()+10);
				break;
			case 5:
				p.setInnercoin(p.getInnercoin()+1);
				break;
			}
			ps.setInt(1, p.getInnercoin());
			ps.setString(2, p.getUname());
			ps.setString(3, p.getUrole());
			ps.setString(4, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackprop(p);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public void updatetrade(User p,int val) throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		String sql="UPDATE userroletbl SET trade=? WHERE username=? AND rolename=? AND serverno=?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setInt(1, val);
			ps.setString(2, p.getUname());
			ps.setString(3, p.getUrole());
			ps.setString(4, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			p.setTrade(val);
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}		
	}
	public int checkpgnum(String svrcode,String rolename,String skcode,int pno) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs=null;
		int flag=0;
		Connection conn = pool.getConnection();
		String sql="SELECT page? pagenum FROM userskillpage"+svrcode+" WHERE rolename=? AND serverno=? AND skillcode=?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setInt(1, pno);
			ps.setString(2, rolename);
			ps.setString(3, svrcode);
			ps.setString(4, skcode);
			rs=ps.executeQuery();
			if(rs.next()){
				flag=rs.getInt("pagenum"); 
			}
			ps.close();
			rs.close();
			return flag;
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
			
		}		
	}
	public int checkgetaward(User p,int sno) throws SQLException{//>0已领 <=0没领或没通关
		PreparedStatement ps = null;
		ResultSet rs=null;
		int flag=0;
		Connection conn = pool.getConnection();
		String sql="SELECT * FROM userpass"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND sceneno=? AND ifuse=1 AND ifpass=1";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, sno);
			rs=ps.executeQuery();
			if(rs.next())	flag=1;
			ps.close();
			rs.close();
			return flag;
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public int getpassward(User p,int sno,Map<String,Object> smap) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs=null;
		int flag=0;
		Connection conn = pool.getConnection();
		String sql="SELECT * FROM userpass"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND sceneno=? AND ifuse=0 AND ifpass=1";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, sno);
			rs=ps.executeQuery();
			if(rs.next())	flag=1;
			ps.close();
			rs.close();
			if(flag>0){//领取奖励
				conn.setAutoCommit(false);
				SceneDict sd=GameManager.getInstance().getScenedict().get(sno);
				String acode=sd.getAwardcode();
				int awtype=sd.getAwardtype();
				int awnum=sd.getAwardnum();
				switch(awtype){
				case 0://0装备1技能2道具3符文
					for(int i=0;i<awnum;i++){
						this.DownDevice(p, acode, conn);
					}					
					break;
				case 1:
					for(int i=0;i<awnum;i++){
						this.DownSkill(p, acode, conn);
					}					
					break;
				case 2:
					this.DownProps(p, acode, conn, awnum);
					break;
				case 3:
					for(int i=0;i<awnum;i++){
						this.DownMark(p, acode, conn);
					}					
					break;				
				}
				sql="UPDATE userpass"+p.getSvrcode()+" SET ifuse=1  WHERE rolename=? AND serverno=? AND sceneno=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setInt(3, sno);
				ps.executeUpdate();
				ps.close();
				conn.commit();
				conn.setAutoCommit(true);
			}
			return flag;
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackPlay(p);//全部回滚
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void CheckDictAlter(String svrcode) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs=null;
		int flag=0;
		Connection conn = pool.getConnection();
		ArrayList<Integer> al=new ArrayList<Integer>();//记录主键
		String sql1="";
		PreparedStatement ps1 = null;
		ResultSet rs1=null;
		conn.setAutoCommit(false);
		String sql="SELECT * FROM userdictalter WHERE serverno=? AND ifuse=0";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, svrcode);
			rs=ps.executeQuery();
			while(rs.next()){
				al.add(rs.getInt("id"));
				String keystr=rs.getString("keystr");
				switch(rs.getInt("tableflag")){
				case 0://道具字典					
					sql1="SELECT * FROM propsdict WHERE propscode=?";
					ps1 = conn.prepareStatement(sql1);
					ps1.setString(1, keystr);
					rs1 = ps1.executeQuery();
					if(rs1.next()){
						PropsDict pd = new PropsDict();
						pd.setPropscode(rs1.getString("propscode"));
						pd.setPropsname(rs1.getString("propsname"));
						pd.setPropsdesc(rs1.getString("propsdesc"));
						pd.setPropsgrade(rs1.getInt("propsgrade"));
						pd.setPropstype(rs1.getInt("propstype"));
						pd.setPropsuse(rs1.getInt("propsuse"));
						pd.setPropsadd(rs1.getInt("propsadd"));
						pd.setUsecond(rs1.getString("usecond"));
						pd.setPropspic(rs1.getString("propspic"));
						if(pd.getPropstype()==1){//包类型商品
							String psql="SELECT * FROM packagetbl WHERE goodscode=?";
							PreparedStatement pps = null;
							ResultSet prs = null;
							pps=conn.prepareStatement(psql);
							pps.setString(1, pd.getPropscode());
							prs=pps.executeQuery();
							while(prs.next()){
								packageGoods psc=new packageGoods();
								psc.setGoodscode(prs.getString("innercode"));
								psc.setGoodsname(prs.getString("goodsname"));
								psc.setGoodsnum(prs.getInt("goodsnum"));
								psc.setInnertype(prs.getInt("innertype"));
								psc.setGoodspic(prs.getString("goodspic"));
								pd.getPackgoods().add(psc);
							}
							pps.close();
							prs.close();
						}
						GameManager.getInstance().getPropsdict().put(pd.getPropscode(), pd);
					}
					rs1.close();
					ps1.close();
					break;
				case 1://商城字典
					sql1="SELECT * FROM shoptbl WHERE serverno=? AND goodscode=?";
					ps1 = conn.prepareStatement(sql1);
					ps1.setString(1, svrcode);
					ps1.setString(2, keystr);
					rs1 = ps1.executeQuery();					
					if(rs1.next()){
						ShopClass sc = new ShopClass();
						sc.setGoodscode(rs1.getString("goodscode"));
						sc.setGoodsname(rs1.getString("goodsname"));
						sc.setGoodsdesc(rs1.getString("goodsdesc"));
						sc.setGoodstype(rs1.getInt("goodstype"));
						sc.setGoodstyle(rs1.getInt("goodstyle"));
						sc.setMoneyp(rs1.getInt("moneyp"));
						sc.setGoldp(rs1.getInt("goldp"));
						sc.setBuycond(rs1.getInt("buycond"));
						sc.setBuysum(rs1.getInt("buysum"));
						sc.setDiscount(rs1.getInt("discount"));
						sc.setBuystart(rs1.getInt("buystart"));
						sc.setBuyend(rs1.getInt("buyend"));
						sc.setDispic(rs1.getString("dispic"));
						sc.setOrderinx(rs1.getInt("orderinx"));
						if(sc.getGoodstype()==1){//包类型商品
							String psql="SELECT * FROM packagetbl WHERE goodscode=?";
							PreparedStatement pps = null;
							ResultSet prs = null;
							pps=conn.prepareStatement(psql);
							pps.setString(1, sc.getGoodscode());
							prs=pps.executeQuery();
							while(prs.next()){
								packageGoods psc=new packageGoods();
								psc.setGoodscode(prs.getString("innercode"));
								psc.setGoodsname(prs.getString("goodsname"));
								psc.setGoodsnum(prs.getInt("goodsnum"));
								psc.setInnertype(prs.getInt("innertype"));
								psc.setGoodspic(prs.getString("goodspic"));
								sc.getPackgoods().add(psc);
							}
							pps.close();
							prs.close();
						}
						GameManager.getInstance().getShopdict().put(sc.getGoodscode(),sc);
					}
					rs1.close();
					ps1.close();
					break;
				case 2://活动字典
					sql1 ="SELECT * FROM actdict WHERE acttype=? AND serverno=?";
					ps1 = conn.prepareStatement(sql1);
					ps1.setInt(1, Integer.parseInt(keystr));
					ps1.setString(2, svrcode);
					rs1 = ps1.executeQuery();
					while(rs1.next()){
						ActDict sad= new ActDict(); 
						sad.setActtype(rs1.getInt("acttype"));
						sad.setActvalue(rs1.getDouble("actvalue"));
						sad.setBtime(rs1.getInt("btime"));
						sad.setEtime(rs1.getInt("etime"));
						GameManager.getInstance().getSvractdict().put(sad.getActtype(), sad);
					}
					rs1.close();
					ps1.close();
					break;
				/*case 3://活动字典
					sql1 ="SELECT * FROM pubmess WHERE messid=? AND serverno=? AND messtype=2";
					ps1 = conn.prepareStatement(sql1);
					ps1.setInt(1, Integer.parseInt(keystr));
					ps1.setString(2, svrcode);
					rs1 = ps1.executeQuery();
					while(rs1.next()){
						ActDict sad= new ActDict(); 
						sad.setActtype(rs1.getInt("acttype"));
						sad.setActvalue(rs1.getDouble("actvalue"));
						sad.setBtime(rs1.getInt("btime"));
						sad.setEtime(rs1.getInt("etime"));
						GameManager.getInstance().getSvractdict().put(sad.getActtype(), sad);
					}
					rs1.close();
					ps1.close();
					break;*/
				}
			}
			rs.close();
			ps.close();
			for(int i=0;i<al.size();i++){
				sql="UPDATE userdictalter SET ifuse=1,usetime=? WHERE id=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, (int)(System.currentTimeMillis()/1000));
				ps.setInt(2, al.get(i));
				ps.executeUpdate();
				ps.close();
			}
			conn.commit();
			conn.setAutoCommit(true);
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void buymissionnum(User p,UserMission um,Map<String,Object> smap,int goldnum) throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		int cnum=GameManager.getInstance().getCmissiondict().get(um.getMissionid()).getCurnum();
		String sql="UPDATE usermission"+p.getSvrcode()+" SET misssum=misssum+1,curnum=? WHERE rolename=? AND serverno=? AND missionid=?";
		try{
			conn.setAutoCommit(false);
			ps=conn.prepareStatement(sql);
			ps.setInt(1, cnum);
			ps.setString(2, p.getUrole());
			ps.setString(3, p.getSvrcode());
			ps.setInt(4, um.getMissionid());
			ps.executeUpdate();
			ps.close();
			sql="UPDATE userroletbl SET gold=gold-? WHERE username=? AND rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, goldnum);
			ps.setString(2, p.getUname());
			ps.setString(3, p.getUrole());
			ps.setString(4, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
			p.setGold(p.getGold()-goldnum);
			um.setCurnum(cnum);
			um.setMissnum(um.getMissnum()+1);
			smap.put("AA",um.getMissionid());
			smap.put("AB",um.getCurnum());
			smap.put("AC", p.getGold());
			goldnum=(um.getMissnum()+1)*10;
			if(goldnum>200) goldnum=200;
			smap.put("AD", goldnum);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void applyluckaward(User p) throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		ResultSet rs = null;
		String sql="SELECT * FROM luckaward  ORDER BY downrand DESC";
		try{
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			int inxflag=0;
			while(rs.next()){
				inxflag=inxflag+1;
				UserLuckAward ula = new UserLuckAward();
				ula.setAwardid(rs.getInt("awardid"));
				ula.setServerno(rs.getString("serverno"));
				ula.setAwardcode(rs.getString("awardcode"));
				ula.setGoodstype(rs.getInt("goodstype"));
				ula.setGoodslevel(rs.getInt("goodslevel"));
				ula.setDownrand(rs.getInt("downrand"));
				ula.setDownnum(rs.getInt("downnum"));
				ula.setAwardname(rs.getString("awardname"));
				ula.setPiccode(rs.getString("piccode"));
				ula.setCurrand(ula.getDownrand());
				p.getUserluckaward().put(ula.getAwardid(), ula);
				p.getUserluckawardrand().put(inxflag, ula);
			}
			ps.close();
			rs.close();
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void actluckaward(User p,int goldnum,Map<String,Object> smap) throws SQLException{
		int maxval=0,minval=0,flag=0;
		int startgold=p.getGold();
		Random rd = new Random();
		int realgold=goldnum;
		int k=rd.nextInt(10000);		
		for(int i=1;i<=10;i++){
			UserLuckAward ula=p.getUserluckawardrand().get(i);
			if(ula.getCurrand()==0) continue;
			minval=maxval;
			maxval=maxval+ula.getCurrand();
			//logger.info("概率值："+k+"起："+minval+"终"+maxval);
			if(k>=minval && k<maxval){
				flag=i;
				break;
			}			
		}
		UserLuckAward dd=p.getUserluckawardrand().get(flag);
		String downcode="@";
		String downname="@";
		int pageno=0,ifupdate=0,cid=0;
		int downcolor=0;
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		String sql="";
		try{
			conn.setAutoCommit(false);
			switch(dd.getGoodstype()){
			case 0://装备
				if(!dd.getAwardcode().equals("@")){//直接掉落装备
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 2, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 3, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 4, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownDevice(p, downcode, conn);
				}
				smap.put("AP", downcode);
				break;
			case 1://武器
				if(!dd.getAwardcode().equals("@")){//直接掉落装备
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 21, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 31, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 41, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownDevice(p, downcode, conn);
				}
				smap.put("AP", downcode);
				break;
			case 2://防具
				if(!dd.getAwardcode().equals("@")){//直接掉落装备
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 22, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 32, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 42, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownDevice(p, downcode, conn);
				}
				smap.put("AP", downcode);
				break;
			case 3://饰品
				if(!dd.getAwardcode().equals("@")){//直接掉落装备
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getDevsetdict().get(downcode).getDevicename();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://精良
						this.ProcDown(p, conn, 23, refValue);
						break;
					case 2://稀有
						this.ProcDown(p, conn, 33, refValue);
						break;
					case 3://传说
						this.ProcDown(p, conn, 43, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();			
				}
				downcolor=GameManager.getInstance().getDevsetdict().get(downcode).getDevcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownDevice(p, downcode, conn);
				}
				smap.put("AP", downcode);
				break;
			case 4://技能
				if(!dd.getAwardcode().equals("@")){//直接掉落技能
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getSkilldict().get(downcode).getSkillname();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://等级1技能
						this.ProcDown(p, conn, 46, refValue);
						break;
					case 2://2
						this.ProcDown(p, conn, 47, refValue);
						break;
					case 3://3
						this.ProcDown(p, conn, 48, refValue);
						break;
					case 4://4
						this.ProcDown(p, conn, 45, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();			
				}
				downcolor=GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownSkill(p, downcode, conn);
				}
				smap.put("AP", downcode);
				break;
			case 5://技能书页
				if(!dd.getAwardcode().equals("@")){//掉落技能书页
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getSkilldict().get(downcode).getSkillname();
					pageno=CommTools.randomval(1,GameManager.getInstance().getSkilldict().get(downcode).getPagenum());
					if(p.getSkillpagelist().containsKey(downcode)) ifupdate=1;
					else ifupdate=0;
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://等级1技能
						this.ProcDown(p, conn, 36, refValue);
						break;
					case 2://2
						this.ProcDown(p, conn, 37, refValue);
						break;
					case 3://3
						this.ProcDown(p, conn, 38, refValue);
						break;
					}
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
					pageno=Integer.parseInt(refValue.get("AC").toString());
					ifupdate=Integer.parseInt(refValue.get("AD").toString());			
				}
				downcolor=GameManager.getInstance().getSkilldict().get(downcode).getSkillcolor();
				for(int i=0;i<dd.getDownnum();i++){
					this.DownSkillPages(p, downcode, ifupdate, pageno, conn);
				}
				smap.put("AP", downcode);
				break;
			case 6://道具
				if(!dd.getAwardcode().equals("@")){//直接掉落道具
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getPropsdict().get(downcode).getPropsname();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					switch(dd.getGoodslevel()){
					case 1://等级1道具
						this.ProcDown(p, conn, 13, refValue);
						break;
					case 2://2
						this.ProcDown(p, conn, 14, refValue);
						break;
					case 3://3
						this.ProcDown(p, conn, 15, refValue);
						break;
					}
					//this.ProcDown(p, conn, 13, refValue);//掉落不同等级道具
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();				
				}
				smap.put("AP", GameManager.getInstance().getPropsdict().get(downcode).getPropspic());
				this.DownProps(p, downcode, conn, dd.getDownnum());
				break;
			case 7://符文
				//logger.info("符文开启");
				if(!dd.getAwardcode().equals("@")){//直接掉落符文
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getMarkdict().get(downcode).getMarkname();
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					this.ProcDown(p, conn, 10, refValue);
					downcode=refValue.get("AA").toString();
					downname=GameManager.getInstance().getMarkdict().get(downcode).getMarkname();
				}
				for(int i=0;i<dd.getDownnum();i++){
					this.DownMark(p, downcode, conn);
				}
				smap.put("AP", downcode);
				break;
			case 8://卡牌
				if(!dd.getAwardcode().equals("@")){//直接掉落卡牌
					downcode=dd.getAwardcode();
					downname=GameManager.getInstance().getCarddict().get(downcode).getCardname();
					Iterator<UserCard> it = p.getCardlist().values().iterator();
					while(it.hasNext()){
						UserCard uc=it.next();
						if(uc.getCardcode().equals(downcode)){
							cid=uc.getCardid();
							break;
						}
					}
				}else{
					Map<String,Object> refValue =new HashMap<String,Object>();
					int starlevel=dd.getGoodslevel()+4;
					if(starlevel==10) starlevel=20;
					if(starlevel==11) starlevel=30;
					this.ProcDown(p, conn, starlevel, refValue);
					downcode=refValue.get("AA").toString();
					downname=refValue.get("AE").toString();
					cid=Integer.parseInt(refValue.get("AB").toString());			
				}
				for(int i=0;i<dd.getDownnum();i++){
					this.DownCard(p, downcode, cid, conn);
				}
				smap.put("AP", downcode);
				downcolor=GameManager.getInstance().getCarddict().get(downcode).getStarlevel();
				break;
			case 10://money
				downcode="P900";
				//downname="钞票";
				downname=GameManager.getInstance().getMname();
				p.setMoney(p.getMoney()+dd.getDownnum());
				smap.put("AP", downcode);
				break;
			case 11://Gold
				downcode="P901";
				downname=GameManager.getInstance().getGoldname();
				p.setGold(p.getGold()+dd.getDownnum());
				realgold=goldnum-dd.getDownnum();
				smap.put("AP", downcode);
				break;
			case 12://money
				downcode="P902";
				downname=GameManager.getInstance().getBallname();
				p.setBall(p.getBall()+dd.getDownnum());
				this.UpdateUserAct(p, 3, "分赃"+downname, dd.getDownnum(), conn);
				smap.put("AP", downcode);
				break;
			}
			smap.put("AA", downcode);
			smap.put("AB", downname);
			smap.put("XP", pageno);//技能页号
			smap.put("AC", dd.getDownnum());
			smap.put("LR", downcolor);
			sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,'摇奖',?)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, Long.toString(System.currentTimeMillis()));
			ps.setInt(4,goldnum);
			ps.setString(5, String.valueOf(p.getGold()-goldnum));//培养后剩余金条数
			ps.executeUpdate();
			ps.close();
			sql="UPDATE userroletbl SET gold=gold-?,money=?,ball=? WHERE username=? AND rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1,realgold);
			ps.setInt(2,p.getMoney());
			ps.setInt(3,p.getBall());
			ps.setString(4, p.getUname());
			ps.setString(5, p.getUrole());
			ps.setString(6, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			p.setGold(p.getGold()-goldnum);
			conn.commit();
			conn.setAutoCommit(true);
			p.setAwardnum(p.getAwardnum()+1);
			dd.setIfchoose(1);
			int oldrand=dd.getCurrand();
			dd.setCurrand(0);
			CheckRandNow(p,flag,oldrand);
			smap.put("AD", p.getAwardnum());
			smap.put("AE", dd.getAwardid());
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			p.setGold(startgold);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void CheckRandNow(User p,int flag,int oldrand){
		UserLuckAward ula1,ula2;
		int myflag=flag;
		int breakflag=0;
		for(int i=1;i<=10;i++){
			if(breakflag==0) myflag=myflag-1;
			else myflag=myflag+1;
			if(myflag==0) {//第一位
				breakflag=1;
				myflag=flag+1;
			}
			ula1=p.getUserluckawardrand().get(myflag);
			if(ula1.getCurrand()==0) continue;
			else {
				ula1.setCurrand(ula1.getCurrand()+oldrand);
				break;
			}
		}
		/*for(int i=1;i<=10;i++){
			for(int j=i+1;j<=10;j++){
				ula1=p.getUserluckawardrand().get(i);
				ula2=p.getUserluckawardrand().get(j);
				if(ula2.getCurrand()>ula1.getCurrand()){
					p.getUserluckawardrand().put(i, ula2);
					p.getUserluckawardrand().put(j,ula1);
				}
			}
		}*/
		/*for(int i=1;i<=10;i++){
			UserLuckAward ula=p.getUserluckawardrand().get(i);
			logger.info("位置："+ula.getAwardid()+"概率："+ula.getCurrand());
		}*/
	}
	public void ReadyShutSvr(String svrcode) throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		String sql="UPDATE svrlist SET ifrun=0 WHERE serverno=?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1,svrcode);
			ps.executeUpdate();
			ps.close();			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public void CheckUserGetall(User p) throws SQLException{
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		ResultSet rs=null;
		String sql="SELECT * FROM usergetall"+p.getSvrcode()+" WHERE rolename=? AND serverno=?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1,p.getUrole());
			ps.setString(2,p.getSvrcode());
			rs=ps.executeQuery();
			while(rs.next()){
				p.getUsergetallmap().put(rs.getString("cardtype"), rs.getInt("ifgetall"));
			}
			ps.close();			
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
	}
	public int CheckCondValue(User p,int btime,int etime) throws SQLException{
		int returnflag=0;
		PreparedStatement ps = null;
		Connection conn = pool.getConnection();
		ResultSet rs=null;
		String sql="SELECT count(1) countsum FROM userconsum"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND usetime/1000>=? AND usetime/1000<?";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1,p.getUrole());
			ps.setString(2,p.getSvrcode());
			ps.setInt(3, btime);
			ps.setInt(4, etime);
			rs=ps.executeQuery();
			if(rs.next()){
				returnflag=rs.getInt("countsum");
			}
			ps.close();			
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}
		return returnflag;
	}
	
	//玩家退出处理
		public void UpdateTaskCD(User p) throws SQLException{
			Connection conn = pool.getConnection();
			PreparedStatement ps = null;
			try{
				String sql="UPDATE userroletbl SET taskcd=? WHERE username=? AND rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, p.getTaskcdval());
				ps.setString(2, p.getUname());
				ps.setString(3, p.getUrole());
				ps.setString(4, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
			}catch (SQLException e) {
				logger.info(e.getMessage());
				throw e;
			}
			finally{
				conn.close();
			}	
		}
	//获取用户碎片背包
	public void GetChipBag(User p) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		p.getUserchips().clear();
		try{
			String sql="SELECT * FROM chipdict T1,userchip"+p.getSvrcode()+" T2 WHERE T1.chipcode=T2.chipcode AND T2.rolename=? AND T2.serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			while(rs.next()){
				CUserChip cuc =new CUserChip();
				cuc.setChipcode(rs.getString("chipcode"));
				cuc.setChipname(rs.getString("chipname"));
				cuc.setChiptype(rs.getInt("chiptype"));
				cuc.setRandval(rs.getInt("randval"));
				cuc.setNeedsum(rs.getInt("needsum"));
				cuc.setChipcolor(rs.getInt("chipcolor"));
				cuc.setCurnum(rs.getInt("curnum"));
				cuc.setRolename(p.getUrole());
				cuc.setServerno(p.getSvrcode());
				p.getUserchips().add(cuc);
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
	}
	//祈愿掉落
	public void actbless(User p,int goldnum,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		try{
			ArrayList<ChipDict> al=GameManager.getInstance().getChipdictlist();
			Random rd = new Random();
			int val=rd.nextInt(10000);
			int maxval=0;
		    int minval=0,flag=-1;
			for(int i=0;i<al.size();i++){
				minval=maxval;
				ChipDict cd=al.get(i);
				if(cd.getRandval()==0) continue;
				maxval=maxval+cd.getRandval();
				if(val>=minval && val<maxval){
					flag=i;
					break;
				}
			}
			int givemoney=0;
			conn.setAutoCommit(false);
			String sql="";
			if(flag<0){// 掉钞票
				smap.put("BC", 0);
				givemoney=1000;
				smap.put("BA","@");
			}else {//掉落
				int inupflag=-1;
				ChipDict cd=al.get(flag);
				for(int k=0;k<p.getUserchips().size();k++){
					if(p.getUserchips().get(k).getChipcode().equals(cd.getChipcode())){
						inupflag=k;
						break;
					}
				}				
				if(inupflag>=0){//更新数据
					sql="UPDATE userchip"+p.getSvrcode()+" SET curnum=curnum+1 WHERE  rolename=? AND serverno=? AND chipcode=?";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3,cd.getChipcode());
					ps.executeUpdate();
					ps.close();
					p.getUserchips().get(inupflag).setCurnum(p.getUserchips().get(inupflag).getCurnum()+1);
				}
				else{//插入新碎片数据
					sql="INSERT INTO userchip"+p.getSvrcode()+" VALUES(?,?,?,1)";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3,cd.getChipcode());
					ps.executeUpdate();
					ps.close();
					CUserChip cuc =new CUserChip();
					cuc.setChipcode(cd.getChipcode());
					cuc.setChipname(cd.getChipname());
					cuc.setChiptype(cd.getChiptype());
					cuc.setRandval(cd.getRandval());
					cuc.setNeedsum(cd.getNeedsum());
					cuc.setChipcolor(cd.getChipcolor());
					cuc.setCurnum(1);
					cuc.setRolename(p.getUrole());
					cuc.setServerno(p.getSvrcode());
					p.getUserchips().add(cuc);					
				}	
				smap.put("BA", cd.getChipcode());
				smap.put("BB", cd.getChipname());
				smap.put("BC", 1);
				smap.put("BE", cd.getChipcolor());
				smap.put("BF", cd.getChiptype());
				if(goldnum>0){
					sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,'祈愿',?)";
					ps=conn.prepareStatement(sql);
					ps.setString(1, p.getUrole());
					ps.setString(2, p.getSvrcode());
					ps.setString(3, Long.toString(System.currentTimeMillis()));
					ps.setInt(4,goldnum);
					ps.setString(5, String.valueOf(p.getGold()-goldnum));//培养后剩余金条数
					ps.executeUpdate();
					ps.close();
				}
			}
			sql="UPDATE userroletbl SET gold=gold-?,money=money+?,blessnum=blessnum+1 WHERE  username=? AND rolename=? AND serverno=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1,goldnum);
			ps.setInt(2,givemoney);
			ps.setString(3, p.getUname());
			ps.setString(4, p.getUrole());
			ps.setString(5, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
			p.setGold(p.getGold()-goldnum);
			p.setBlessnum(p.getBlessnum()+1);
			p.setMoney(p.getMoney()+givemoney);
			smap.put("BG", p.getGold());
			smap.put("BH",p.getBlessnum());
			smap.put("BI", p.getMoney());
			smap.put("BJ",givemoney);
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
	}
	//掉落碎片
	public void Downchip(User p,String downcode,int downsum,Connection conn) throws SQLException{
		PreparedStatement ps = null;
		int inupflag=-1;
		for(int k=0;k<p.getUserchips().size();k++){
			if(p.getUserchips().get(k).getChipcode().equals(downcode)){
				inupflag=k;
				break;
			}
		}
		String sql="";
		try{
			if(inupflag>=0){//更新数据
				sql="UPDATE userchip"+p.getSvrcode()+" SET curnum=curnum+? WHERE  rolename=? AND serverno=? AND chipcode=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, downsum);
				ps.setString(2, p.getUrole());
				ps.setString(3, p.getSvrcode());
				ps.setString(4,downcode);
				ps.executeUpdate();
				ps.close();
				p.getUserchips().get(inupflag).setCurnum(p.getUserchips().get(inupflag).getCurnum()+downsum);
			}
			else{//插入新碎片数据
				sql="INSERT INTO userchip"+p.getSvrcode()+" VALUES(?,?,?,?)";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3,downcode);
				ps.setInt(4, downsum);
				ps.executeUpdate();
				ps.close();
				ArrayList<ChipDict> al=GameManager.getInstance().getChipdictlist();
				for(int i=0;i<al.size();i++){
					if(al.get(i).getChipcode().equals(downcode)){
						ChipDict cd= al.get(i);
						CUserChip cuc =new CUserChip();
						cuc.setChipcode(cd.getChipcode());
						cuc.setChipname(cd.getChipname());
						cuc.setChiptype(cd.getChiptype());
						cuc.setRandval(cd.getRandval());
						cuc.setNeedsum(cd.getNeedsum());
						cuc.setChipcolor(cd.getChipcolor());
						cuc.setCurnum(1);
						cuc.setRolename(p.getUrole());
						cuc.setServerno(p.getSvrcode());
						p.getUserchips().add(cuc);		
						break;
					}
				}							
			}	
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		
	}
	//合成碎片，keyflag 用户碎片背包位置
	public void actUnionChip(User p,int keyflag,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		try{
			conn.setAutoCommit(false);
			String sql="";
			String unionname="";
			CUserChip cuc=p.getUserchips().get(keyflag);
			switch(cuc.getChiptype()){
			case 0://卡牌
				int cid=0;
				Iterator<UserCard> it = p.getCardlist().values().iterator();
				while(it.hasNext()){
					UserCard uc=it.next();
					if(uc.getCardcode().equals(cuc.getChipcode())){
						cid=uc.getCardid();
						break;
					}
				}
				this.DownCard(p, cuc.getChipcode(), cid, conn);
				unionname=GameManager.getInstance().getCarddict().get(cuc.getChipcode()).getCardname();
				break;
			case 1://装备
				this.DownDevice(p, cuc.getChipcode(), conn);
				unionname=GameManager.getInstance().getDevsetdict().get(cuc.getChipcode()).getDevicename();
				break;
			}
			sql="UPDATE userchip"+p.getSvrcode()+" SET curnum=curnum-? WHERE rolename=? AND serverno=? AND chipcode=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, cuc.getNeedsum());
			ps.setString(2, p.getUrole());
			ps.setString(3, p.getSvrcode());
			ps.setString(4,cuc.getChipcode());
			ps.executeUpdate();
			ps.close();
			
			conn.commit();
			conn.setAutoCommit(true);
			cuc.setCurnum(cuc.getCurnum()-cuc.getNeedsum());
			smap.put("BA", cuc.getChipcode());
			smap.put("BB", cuc.getChipname());
			smap.put("BC", cuc.getCurnum());
			smap.put("BE", cuc.getChipcolor());
			smap.put("BF", cuc.getChiptype());
			String mess="真牛！玩家--"+p.getUrole()+"--通过收集碎片合成了"+unionname;
			this.playerpubmess(p.getSvrcode(), mess);
			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
	}
	//结算boss排名奖励
	public void CalcBossRank(String svrcode) throws SQLException{
		Connection conn = pool.getConnection();
		conn.setAutoCommit(true);
		try{
			CalcBossRank(svrcode,conn);
			conn.commit();
			conn.setAutoCommit(true);
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}	
		
	}
	public void CalcBossRank(String svrcode,Connection conn) throws SQLException{
		if(GameManager.getInstance().getWorldBoss().getDeadTime()>0){//打到
			//Connection conn = pool.getConnection();
			PreparedStatement ps = null;
			ResultSet rs=null;
			PreparedStatement ps1 = null;
			ResultSet rs1=null;
			int bosslevel=GameManager.getInstance().getWorldBoss().getBossval();
			String play1="@",play2=" ",play3=" ",play4=" ";
			String rn="";
			int awardmoney=0;
			try {
				String sql="SELECT rolename,sum(hurtvalue) sumval FROM useractboss WHERE acttime>? AND acttime<? AND serverno=?  GROUP BY rolename ORDER BY sumval DESC LIMIT 3";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, CommTools.GetTimeInMillis(0, 1, 0));
				ps.setInt(2, CommTools.GetTimeInMillis(0, 20, 0));
				ps.setString(3, svrcode);
				rs=ps.executeQuery();
				String sql1="";
				int rankloc=0;
				while(rs.next()){
					String downcode="";
					rankloc=rankloc+1;
					try{
						//conn.setAutoCommit(false);
						rn=rs.getString("rolename");
						switch(rankloc){
						case 1:
							awardmoney=bosslevel*100000;
							play1=rn;
							downcode="P036";
							break;
						case 2:
							downcode="P037";
							play2=rn;
							awardmoney=bosslevel*50000;
							break;
						case 3:
							play3=rn;
							downcode="P038";
							awardmoney=bosslevel*30000;
							break;						
						}			
						sql1="INSERT INTO usermailbox VALUES(NULL,?,?,?,?,?,0,?,0,1,0,?,?)";
						//sql1="INSERT INTO usermailbox VALUES(NULL,?,?,'击杀魔王排名奖励','您在今日的群殴魔王活动中，排名第',?,'位共获得金币',?,'，排名礼包*1',?,0,?,0,1,0,?,?)";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1, rn);
						ps1.setString(2, svrcode);
						String str="击杀魔王排名奖励";
						ps1.setString(3, str);
						str="您在今日的群殴魔王活动中，排名第"+String.valueOf(rankloc)+"位共获得"+GameManager.getInstance().getMname()+String.valueOf(awardmoney)+"，排名礼包*1";
						ps1.setString(4, str);
						ps1.setString(5, downcode);
						ps1.setString(6, downcode);
						ps1.setInt(7, awardmoney);
						ps1.setInt(8,(int)(System.currentTimeMillis()/1000));
						ps1.executeUpdate();
						ps1.close();
						sql1="INSERT INTO useract VALUES(?,?,?,?,?,?)";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1, rn);
						ps1.setString(2,svrcode);
						ps1.setInt(3, (int)(System.currentTimeMillis()/1000));
						ps1.setInt(4,6);
						ps1.setInt(5,1);
						ps1.setString(6, String.valueOf(awardmoney)+"--"+downcode+"*1");
						ps1.executeUpdate();
						ps1.close();
						/*conn.commit();
						conn.setAutoCommit(true);*/
					}catch (SQLException e) {
						logger.info(e.getMessage());
						throw e;
						/*conn.rollback();
						conn.setAutoCommit(true);			*/			
					}	
				}
				rs.close();
				ps.close();
				sql="SELECT rolename FROM useractboss WHERE serverno=? AND acttime>? AND acttime<? AND ifdown=2";
				ps=conn.prepareStatement(sql);
				ps.setString(1, svrcode);
				ps.setInt(2, CommTools.GetTimeInMillis(0, 1, 0));
				ps.setInt(3, CommTools.GetTimeInMillis(0, 20, 0));
				rs=ps.executeQuery();
				if(rs.next()){
					rn=rs.getString("rolename");
					play4=rn;
					awardmoney=bosslevel*200000;
					//conn.setAutoCommit(false);
					try{
						sql1="INSERT INTO usermailbox VALUES(NULL,?,?,?,?,'@',0,'@',0,0,0,?,?)";
						//sql1="INSERT INTO usermailbox VALUES(NULL,?,?,'补刀奖励','您在今日的群殴魔王活动中给予魔王最后一击，获得钞票',?,'@',0,'@',0,0,0,?,?)";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1, rn);
						ps1.setString(2, svrcode);
						String str="补刀奖励";
						ps1.setString(3, str);
						str="您在今日的群殴魔王活动中给予魔王最后一击，获得"+GameManager.getInstance().getMname()+String.valueOf(awardmoney);
						ps1.setString(4, str);
						ps1.setInt(5, awardmoney);
						ps1.setInt(6, (int)(System.currentTimeMillis()/1000));
						ps1.executeUpdate();
						ps1.close();
						sql1="INSERT INTO useract VALUES(?,?,?,?,?,?)";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1, rn);
						ps1.setString(2,svrcode);
						ps1.setInt(3, (int)(System.currentTimeMillis()/1000));
						ps1.setInt(4,7);
						ps1.setInt(5,1);
						ps1.setString(6, String.valueOf(awardmoney));
						ps1.executeUpdate();
						ps1.close();
						/*conn.commit();
						conn.setAutoCommit(true);*/
					}catch (SQLException e) {
						logger.info(e.getMessage());
						throw e;
						/*conn.rollback();
						conn.setAutoCommit(true);*/
					}						
				}
				rs.close();
				ps.close();	
				//重置世界boss
				ResetBossWorld(conn);
				String mess="魔王"+GameManager.getInstance().getWorldBoss().getBossname()+"被击退,伤害前三名分别为:"
						+play1+","+play2+","+play3+"各自获得英勇奖一份,"+play4+"给予boss最后一击获得补刀奖一份";
				this.playerpubmess(svrcode, mess);
			}catch (SQLException e) {
				logger.info(e.getMessage());
				throw e;
			}	
		}
		
	}
	//设置自动打世界boss的结算
	public void AutoCalcBoss(String svrcode) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs=null;
		ResultSet rs1=null;
		try {
			String rn="";
			String sql1="";
			int sumatk=0;
			String sql=" SELECT username,rolename,gold,currlogin from userroletbl WHERE serverno=? AND autoboss=1";
			ps=conn.prepareStatement(sql);
			ps.setString(1, svrcode);
			rs=ps.executeQuery();
			String curlogin=dateToString(new Date());
			while(rs.next()){
				String oldlog=rs.getString("currlogin");
				try{
					conn.setAutoCommit(false);
					rn=rs.getString("rolename");
					if(this.checklog(oldlog,curlogin)>0&&rs.getInt("gold")<100){//隔天未登录自动扣金条且金条不够用不处理
						sql1="UPDATE userroletbl SET autoboss=0 WHERE rolename=? AND serverno=?";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1, rn);
						ps1.setString(2,svrcode);
						ps1.executeUpdate();
						ps1.close();	
						conn.commit();
						conn.setAutoCommit(true);	
					}else{
						if(this.checklog(oldlog,curlogin)>0){
							sql1="UPDATE userroletbl SET gold=gold-100 WHERE rolename=? AND serverno=?";
							ps1=conn.prepareStatement(sql1);
							ps1.setString(1, rn);
							ps1.setString(2,svrcode);
							ps1.executeUpdate();
							ps1.close();
							sql1="INSERT INTO  userconsum"+svrcode+"  VALUES(?,?,?,1,?,'自动boss',?)";
							ps1=conn.prepareStatement(sql1);
							ps1.setString(1, rn);
							ps1.setString(2, svrcode);
							ps1.setString(3, Long.toString(System.currentTimeMillis()));
							ps1.setInt(4,100);
							ps1.setString(5,String.valueOf(rs.getInt("gold")-100));//自动扣费后剩余
							ps1.executeUpdate();
							ps1.close();
						}
						sql1="SELECT sum(atk) atkall FROM usercard"+svrcode+" WHERE rolename=? AND serverno=? AND ifuse=1";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1, rn);
						ps1.setString(2, svrcode);
						rs1=ps1.executeQuery();
						if(rs1.next()) sumatk=rs1.getInt("atkall");
						rs1.close();
						ps1.close();
						int downmoney=sumatk*30/2;
						sql1="INSERT INTO usermailbox VALUES(NULL,?,?,'自动攻击魔王奖励',?,'P035',0,'P035',0,1,0,?,?)";
						String str="您在今日的群殴魔王活动中，对魔王造成了"+String.valueOf(sumatk*30)+"点伤害，共获得"+GameManager.getInstance().getMname()+String.valueOf(downmoney)+"，自动攻击奖励1份";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1, rn);
						ps1.setString(2, svrcode);
						ps1.setString(3, str);
						ps1.setInt(4, downmoney);
						ps1.setInt(5, (int)(System.currentTimeMillis()/1000));
						ps1.executeUpdate();
						ps1.close();
						sql1="INSERT INTO useract VALUES(?,?,?,?,?,?)";
						ps1=conn.prepareStatement(sql1);
						ps1.setString(1, rn);
						ps1.setString(2,svrcode);
						ps1.setInt(3, (int)(System.currentTimeMillis()/1000));
						ps1.setInt(4,5);
						ps1.setInt(5, 1);
						ps1.setString(6, String.valueOf(sumatk*30)+"--P035*1");
						ps1.executeUpdate();
						ps1.close();
						conn.commit();
						conn.setAutoCommit(true);		
					}
				}					
				catch (SQLException e) {					
					conn.rollback();
					conn.setAutoCommit(true);
					logger.info(e.getMessage());
				}				
			}
			ps.close();
			rs.close();
			
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}
		finally{
			conn.close();
		}			
	}
	
	//设定自动挑战boss
	public void SetAutoFightBoss(User p) throws SQLException{
		Connection conn = pool.getConnection();
		if(conn != null){
			try {
				PreparedStatement ps = null;
				//conn.setAutoCommit(true);
				int flag = 0;
				int tempAuto = 0;
				if(p.getAutoboss() == 1){//当前自动改为取消
					flag = -1;
				}else{
					flag = 1;
					tempAuto = 1;
				}	
				String sql="UPDATE userroletbl SET autoboss=?,gold=gold-? WHERE username=? AND rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, tempAuto);
				ps.setInt(2, flag*100);
				ps.setString(3, p.getUname());
				ps.setString(4, p.getUrole());
				ps.setString(5, p.getSvrcode());
				ps.executeUpdate();
				ps.close();				
//				if(flag == -1){
//					sql="INSERT INTO  userconsum  VALUES(?,?,?,?,1,?,'自动挑战BOSS',?)";
//					ps=conn.prepareStatement(sql);
//					ps.setString(1, p.getUname());
//					ps.setString(2, p.getUrole());
//					ps.setString(3, p.getSvrcode());
//					ps.setString(4, Long.toString(System.currentTimeMillis()));
//					ps.setInt(5,100);
//					ps.setString(6,"");//金条强化成功等级
//					ps.executeUpdate();
//					ps.close();
//				}
				/*conn.commit();
				conn.setAutoCommit(true);*/
				p.setGold(p.getGold()-flag*100);
				p.setAutoboss(tempAuto);
			}catch (SQLException e) {
				//conn.rollback();
				//conn.setAutoCommit(true);
				logger.info(e.getMessage());
				throw e;
			}finally{				
				conn.close();	
			}
		}
	}
//	//返回合成符文列表
//	public void procmark(User p) throws SQLException{
//		Connection conn = pool.getConnection();
//		String sql="SELECT t2.*,IFNULL(t1.curnum,0) curnum FROM markprocdict t2 LEFT JOIN (SELECT count(1) curnum,markcode FROM usermark WHERE deviceid<=0 and rolename=? AND serverno=? GROUP BY markcode) t1 ON t1.markcode=t2.oldmark";
//		PreparedStatement ps = null;
//		ResultSet rs=null;
//		try {
//			ps=conn.prepareStatement(sql);
//			ps.setString(1, p.getUrole());
//			ps.setString(2, p.getSvrcode());
//			rs=ps.executeQuery();
//			while(rs.next()){
//				MergeMarkDict mmd =new MergeMarkDict();
//				mmd.setNewmark(rs.getString("newmark"));
//				mmd.setOldmark(rs.getString("oldmark"));
//				mmd.setMarknum(rs.getInt("marknum"));
//				mmd.setCurnum(rs.getInt("curnum"));
//				p.getMymarkmergemap().put(mmd.getNewmark(), mmd);
//			}
//			ps.close();		
//			rs.close();
//			
//		}catch (SQLException e) {
//			logger.info(e.getMessage());
//			throw e;
//		}finally{				
//			conn.close();	
//		}
//	}
	
	//合成符文
	public void sureprocmark(User p,String newcode,Map<String,Integer> needlist,ArrayList<Integer> marklist) throws SQLException{
		Connection conn = pool.getConnection();
		String sql="DELETE FROM usermark"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND markid in (?,?,?)";
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, marklist.get(0));
			ps.setInt(4, marklist.get(1));
			ps.setInt(5, marklist.get(2));
			ps.executeUpdate();
			ps.close();		
			MarkDict md= GameManager.getInstance().getMarkdict().get(newcode);
			sql="INSERT INTO usermark"+p.getSvrcode()+" VALUES(?,?,NULL,?,?,?,?,?,?,?,0,-1)";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setString(3, md.getMarkcode());
			ps.setString(4, md.getMarkname());
			ps.setString(5, md.getMarkdesc());
			ps.setString(6, md.getMarktype());
			ps.setDouble(7, md.getHp());
			ps.setDouble(8, md.getAtk());
			ps.setDouble(9, md.getDef());
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
			//刷新新老符文数量
			this.rollbackMark(p);
			Iterator<String> needit = needlist.keySet().iterator();
			while(needit.hasNext()){
				String needcode = needit.next();
				int currnum = p.getUmarksnumlist().get(needcode);
				p.getUmarksnumlist().put(needcode, currnum - needlist.get(needcode));
			}
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();	
		}
	}
	//返回等级排名 map
	public void reqlevelrank(User p,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		int enterflag=0;
		String sql="SELECT t1.*,t2.cardcode,t2.starlevel,t2.curstyle FROM levelrank"+p.getSvrcode()+" t1,usercard"+p.getSvrcode()+" t2 "+
		"WHERE t1.rolename=t2.rolename AND t1.serverno=t2.serverno AND t1.ranktype=0  AND t1.serverno=? AND t2.ifuse=1 AND t2.locate=0 AND (t1.rankloc<=20 OR t1.rolename=?) ORDER BY t1.rankloc";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getSvrcode());
			ps.setString(2, p.getUrole());
			rs=ps.executeQuery();
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				
				Map<String,Object> mymap=new HashMap<String,Object>();
				mymap.put("BA", rs.getInt("rankloc"));
				mymap.put("BB", rs.getString("rolename"));
				mymap.put("BC", rs.getString("cardcode"));
				mymap.put("BD", rs.getInt("level"));
				switch(rs.getInt("starlevel")){
				case 1:
				case 2:
					mymap.put("BE", 1);
					break;
				case 3:
				case 4:
					mymap.put("BE", 2);
					break;
				case 5:
					mymap.put("BE", 3);
					break;
				case 6:
					mymap.put("BE", 4);
					break;
				case 7:
					mymap.put("BE", 5);
					break;
				}
				mymap.put("BF", rs.getInt("curstyle"));
				if(p.getUrole().equals(rs.getString("rolename"))) {
					enterflag=1;
					smap.put("CA",mymap.get("BA"));
					smap.put("CB",mymap.get("BB"));
					smap.put("CC",mymap.get("BC"));
					smap.put("CD",mymap.get("BD"));
					smap.put("CE",mymap.get("BE"));
					smap.put("CF",mymap.get("BF"));
				}
				al.add(mymap);
			}
			smap.put("AA", al.toArray());
			smap.put("AD", enterflag);
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();	
		}
	}
	//返回消费排名及上周奖励
	public void reqgoldrank(User p,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		int enterflag=0;
		String sql="SELECT t1.*,t2.cardcode,t2.starlevel,t2.curstyle FROM levelrank"+p.getSvrcode()+" t1,usercard"+p.getSvrcode()+" t2 "+
		"WHERE t1.rolename=t2.rolename AND t1.serverno=t2.serverno AND t1.ranktype=1 AND t1.serverno=? AND t2.ifuse=1 AND t2.locate=0 AND (t1.rankloc<=20 OR t1.rolename=?) ORDER BY t1.rankloc";
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getSvrcode());
			ps.setString(2, p.getUrole());
			rs=ps.executeQuery();
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> mymap=new HashMap<String,Object>();
				mymap.put("BA", rs.getInt("rankloc"));
				mymap.put("BB", rs.getString("rolename"));
				mymap.put("BC", rs.getString("cardcode"));
				mymap.put("BD", rs.getInt("level"));
				switch(rs.getInt("starlevel")){
				case 1:
				case 2:
					mymap.put("BE", 1);
					break;
				case 3:
				case 4:
					mymap.put("BE", 2);
					break;
				case 5:
					mymap.put("BE", 3);
					break;
				case 6:
					mymap.put("BE", 4);
					break;
				case 7:
					mymap.put("BE", 5);
					break;
				}
				mymap.put("BF", rs.getInt("curstyle"));
				if(p.getUrole().equals(rs.getString("rolename"))) {
					enterflag=1;
					smap.put("CA",mymap.get("BA"));
					smap.put("CB",mymap.get("BB"));
					smap.put("CC",mymap.get("BC"));
					smap.put("CD",mymap.get("BD"));
					smap.put("CE",mymap.get("BE"));
					smap.put("CF",mymap.get("BF"));
				}
				al.add(mymap);
			}
			smap.put("AA", al.toArray());
			smap.put("AD", enterflag);
			rs.close();
			ps.close();
			sql="SELECT * FROM userrankaward"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND awardtype=0 AND ifget=0";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				smap.put("AB", rs.getInt("givesum"));
				smap.put("AC", rs.getInt("ifget"));
				p.setIfgetgold( rs.getInt("ifget"));
				p.getRankaward()[0]=rs.getInt("givesum");
			}
			else{
				smap.put("AB", 0);
				smap.put("AC", -1);//无奖励
				p.getRankaward()[0]=0;
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();	
		}
	}
	//获取宝箱排名及上周奖励
	public void reqboxrank(User p,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		int enterflag=0;
		String sql="SELECT t1.*,t2.cardcode,t2.starlevel,t2.curstyle FROM levelrank"+p.getSvrcode()+" t1,usercard"+p.getSvrcode()+" t2 "+
		"WHERE t1.rolename=t2.rolename AND t1.serverno=t2.serverno AND t1.ranktype=2 AND t1.serverno=? AND t2.ifuse=1 AND t2.locate=0 AND (t1.rankloc<=20 OR t1.rolename=?) ORDER BY t1.rankloc";;
		try{
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getSvrcode());
			ps.setString(2, p.getUrole());
			rs=ps.executeQuery();
			ArrayList<Map<String,Object>> al = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> mymap=new HashMap<String,Object>();
				mymap.put("BA", rs.getInt("rankloc"));
				mymap.put("BB", rs.getString("rolename"));
				mymap.put("BC", rs.getString("cardcode"));
				mymap.put("BD", rs.getInt("level"));
				switch(rs.getInt("starlevel")){
				case 1:
				case 2:
					mymap.put("BE", 1);
					break;
				case 3:
				case 4:
					mymap.put("BE", 2);
					break;
				case 5:
					mymap.put("BE", 3);
					break;
				case 6:
					mymap.put("BE", 4);
					break;
				case 7:
					mymap.put("BE", 5);
					break;
				}
				mymap.put("BF", rs.getInt("curstyle"));
				if(p.getUrole().equals(rs.getString("rolename"))) {
					enterflag=1;
					smap.put("CA",mymap.get("BA"));
					smap.put("CB",mymap.get("BB"));
					smap.put("CC",mymap.get("BC"));
					smap.put("CD",mymap.get("BD"));
					smap.put("CE",mymap.get("BE"));
					smap.put("CF",mymap.get("BF"));
				}
				al.add(mymap);
			}
			smap.put("AA", al.toArray());
			smap.put("AD", enterflag);
			rs.close();
			ps.close();
			sql="SELECT * FROM userrankaward"+p.getSvrcode()+" WHERE rolename=? AND serverno=? AND awardtype=1 AND ifget=0";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			rs=ps.executeQuery();
			if(rs.next()){
				smap.put("AB", rs.getInt("givesum"));
				smap.put("AC", rs.getInt("ifget"));
				p.setIfgetbox(rs.getInt("ifget"));//0未领 1已领
				p.getRankaward()[1]=rs.getInt("givesum");
			}else{
				smap.put("AB", 0);
				smap.put("AC", -1);//无奖励
				p.getRankaward()[1]=0;
			}
			rs.close();
			ps.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();	
		}
	}
	//
	public void reqrankaward(User p,String awardtype,Map<String,Object> smap) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		int atype=Integer.parseInt(awardtype);
		String sql="";
		try{
			conn.setAutoCommit(false);
			switch(atype){
			case 0:
				sql="UPDATE userroletbl SET gold=gold+? WHERE username=? AND rolename=? AND serverno=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, p.getRankaward()[0]);
				ps.setString(2, p.getUname());
				ps.setString(3, p.getUrole());
				ps.setString(4, p.getSvrcode());
				ps.executeUpdate();
				ps.close();
				break;
			case 1:
				this.DownProps(p, "P008", conn, p.getRankaward()[1]);
				break;
			}
			sql="UPDATE userrankaward"+p.getSvrcode()+" SET ifget=1 WHERE rolename=? AND serverno=? AND awardtype=?";
			ps=conn.prepareStatement(sql);
			ps.setString(1, p.getUrole());
			ps.setString(2, p.getSvrcode());
			ps.setInt(3, atype);
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.setAutoCommit(true);
			switch(atype){
			case 0:
				p.setIfgetgold(1);//设为已领奖
				p.setGold(p.getGold()+p.getRankaward()[0]);
				smap.put("AB", p.getRankaward()[0]);
				smap.put("AC", 0);
				p.getRankaward()[0]=0;
				break;
			case 1:
				p.setIfgetbox(1);
				smap.put("AB", p.getRankaward()[1]);
				smap.put("AC", 1);
				p.getRankaward()[1]=0;
				break;
			}			
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();	
		}
	}
	
	public void actpscard(User p,int cardid,int pstype,int need,UserCard uc,int expaward) throws SQLException{
		Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		String sql ="";
		try{
			conn.setAutoCommit(false);
			CardUpdate(p, uc, conn);
			
			////////////////////////更新钻石 次数等/////////////////////////////////
			if(pstype == 1){
				sql ="UPDATE userroletbl SET gold=gold-?,advps=advps-? WHERE username=? AND rolename=? AND serverno=?";
			}else{
				sql ="UPDATE userroletbl SET money=money-?,stdps=stdps-? WHERE username=? AND rolename=? AND serverno=?";
			}
			ps=conn.prepareStatement(sql);
			ps.setInt(1, need);
			ps.setInt(2, expaward);
			ps.setString(3, p.getUname());
			ps.setString(4, p.getUrole());
			ps.setString(5, p.getSvrcode());
			ps.executeUpdate();
			ps.close();
			if(pstype == 1){
				sql="INSERT INTO  userconsum"+p.getSvrcode()+"  VALUES(?,?,?,1,?,'美容',?)";
				ps=conn.prepareStatement(sql);
				ps.setString(1, p.getUrole());
				ps.setString(2, p.getSvrcode());
				ps.setString(3, Long.toString(System.currentTimeMillis()));
				ps.setInt(4,need);
				ps.setString(5,String.valueOf(uc.getCurvalue()));//
				ps.executeUpdate();
				ps.close();
			}
			conn.commit();
			conn.setAutoCommit(true);
			if(pstype == 1){
				p.setGold(p.getGold() - need);
				p.setAdvps(p.getAdvps() - expaward);
			}else{
				p.setMoney(p.getMoney() - need);
				p.setStdps(p.getStdps() - expaward);
			}
		}catch (SQLException e) {
			conn.rollback();
			conn.setAutoCommit(true);
			this.rollbackCard(p);
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();
		}
	}
	public void queryplayer(String svrcode,String stime,String etime,Map<String,Object> smap) throws SQLException{
		 Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		String sql ="";
		try{
			sql="SELECT COUNT(1) sump FROM userroletbl WHERE serverno=? AND firstlogin>=? AND firstlogin<=? ";
			ps=conn.prepareStatement(sql);
			ps.setString(1, svrcode);
			ps.setString(2, stime);
			ps.setString(3, etime);
			rs=ps.executeQuery();
			if(rs.next()){
				smap.put("AA", rs.getInt("sump"));
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();
		}
	}
	public void querymoney(String svrcode,String stime,String etime,Map<String,Object> smap) throws SQLException{
		 Connection conn = pool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs=null;
		String sql ="";
		try{
			sql="SELECT sum(exchgold) sumgold FROM usercharge WHERE serverno=? AND chargetime>=? AND chargetime<=? AND status=1";
			ps=conn.prepareStatement(sql);
			ps.setString(1, svrcode);
			ps.setString(2, stime);
			ps.setString(3, etime);
			rs=ps.executeQuery();
			if(rs.next()){
				smap.put("AA", rs.getInt("sumgold"));
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		}finally{				
			conn.close();
		}
	}
}

