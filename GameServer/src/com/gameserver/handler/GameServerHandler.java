package com.gameserver.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.gameserver.comm.PacketCommandType;
import com.gameserver.manager.GameManager;
import com.gameserver.model.User;



public class GameServerHandler extends SimpleChannelHandler{	
	
	private static final Logger logger = Logger.getLogger(GameServerHandler.class.getName());
	private Integer ClientMessageCommand=0;
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)throws Exception {
		if (e instanceof ChannelStateEvent) {
			//logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e){
		//System.out.println("连接成功");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		
		if(GameManager.getInstance().getReadyshut()==0){
			try {
				Map<String, Object> clientMap = (HashMap<String, Object>) e.getMessage();
				ClientMessageCommand=Integer.parseInt(clientMap.get("CMD").toString());//解析客户端命令类型
				//logger.info("接收到客户端数据包，包头为:"+ClientMessageCommand.toString());
				User player;
				switch(ClientMessageCommand){
				case PacketCommandType.LOGIN:
					if(GameManager.getInstance().getOnlineMap().containsKey(clientMap.get("RN").toString())){
						//logger.info("接收到客户端数据包，角色名为:"+clientMap.get("RN").toString());
						/*Map<String,Object> map = new HashMap<String,Object>();
						map.put("CMD", PacketCommandType.GETERROR);
						map.put("EC", "1001");
						e.getChannel().write(map);*/
						User oldp=GameManager.getInstance().getOnlineMap().get(clientMap.get("RN").toString());
						oldp.getChannel().disconnect();
					}
					player = new User(clientMap, e.getChannel(),1);
					if(player.getGod()>0){//账号封停
						e.getChannel().close();
					}else{
						GameManager.getInstance().addPlayers(player);//添加玩家至当前游戏应用
						//登录成功之后用户信息
						player.ReturnLogin();
					}					
					break;
				case PacketCommandType.CREATEROLE:
					//判断角色是否重名
					//logger.info("newuser"+clientMap.get("RN").toString());
					player = new User(clientMap, e.getChannel(),0);
					String newrolecode=clientMap.get("CN").toString();
					String markcode=clientMap.get("AN").toString();
					String platcode=clientMap.get("BN").toString();
					//logger.info("接收到客户端数据包，角色名为:"+newrolecode);
					//A017,A023,A029,A030
					if(newrolecode.equals("A003")||newrolecode.equals("A005")||newrolecode.equals("A041")||newrolecode.equals("A044")){
					//if(newrolecode.equals("A017")||newrolecode.equals("A023")||newrolecode.equals("A029")||newrolecode.equals("A030")){
						player.CheckRole(newrolecode,markcode,platcode);
						GameManager.getInstance().addPlayers(player);
					}else e.getChannel().disconnect();
					break;
				case PacketCommandType.CHANGECARD: //阵容内卡牌换位
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					String chcardlist[] = clientMap.get("AA").toString().split(",");
					player.changeCard(chcardlist);
					break;
				case PacketCommandType.GETCARDINFO:  //卡牌明细 获取装备 技能  
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					int cid=Integer.parseInt(clientMap.get("AA").toString()); //卡牌id
					player.ReturnCardInfo(cid);
					break;
				case PacketCommandType.GETCARDLIST://请求背包卡牌列表  返回背包中卡牌列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.GetUserCardList();
					break;
				case PacketCommandType.GETDEVICELIST://请求背包装备列表 
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.GetUserDevList();
					break;
				case PacketCommandType.GETUSKILLLIST://请求背包技能列表 
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.GetUserSkList();
					break;
				case PacketCommandType.GETUMARKLIST://请求背包符文列表 
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.GetUMarkList();
					break;
				case PacketCommandType.GETUMPLIST: //请求技能书页列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.GetUMarkPgList();
					break;
				case PacketCommandType.ALTERCARD: //换牌
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.AlterCard(clientMap);
					break;
				case PacketCommandType.LOADDEVICE: //穿上装备
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.LoadDevice(clientMap);
					break;
				case PacketCommandType.LOADMARK: //符文镶嵌
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.LoadMark(clientMap);
					break;				
				case PacketCommandType.GETMARKSETS: //获取符文组合
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getMarksets();
					break;
				case PacketCommandType.LOADSKILL: //安装技能
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.LoadSkill(clientMap);
					break;
				case PacketCommandType.UPGRADEDEVICE: //精炼申请
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.ReturnUpgradeInfo(clientMap);
					break;
				case PacketCommandType.STARTUPGRADE: //开始精炼
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.UpgradeDevice(clientMap);
					break;
				case PacketCommandType.LIVEUPCARD: //培养
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.liveupcard(clientMap);
					break;
				case PacketCommandType.ACTLIVEUP: //培养信息
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.actliveupcard(clientMap);
					break;
				case PacketCommandType.ABORTLIVEUP: //放弃培养
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.abortliveup(clientMap);
					break;
				case PacketCommandType.SAVELIVEUP: //保存培养值
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.saveliveup(clientMap);
					break;
				case PacketCommandType.LIFEUP: //转生
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.cardlifeup(clientMap);
					break;
				case PacketCommandType.ACTLIFEUP: //开始转生
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.actlifeup(clientMap);
					break;
				case PacketCommandType.CARDMERGE: //融魂 ，融魂完成后散魂卡牌消失
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getmergeinfo(clientMap);
					break;
				case PacketCommandType.ACTCARDMERGE: //融魂 操作
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.mergecard(clientMap);
					break;
				case PacketCommandType.PRODUCESKILL: //合成
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.produceskill(clientMap);
					break;
				case PacketCommandType.UNIONSKILL: //合并技能
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.unionskill(clientMap);
					break;
				case PacketCommandType.HERATBEAT: //心跳包
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.setHeartTime(new Date());
					//logger.info("HeartBeat :"+player.getUrole()+"心跳时间："+player.getHeartTime());//心跳检测玩家掉线
					break;
				case PacketCommandType.ACTMISSION: //进入任务
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					//logger.info("收到任务获取包"+player.getUname());
					player.actmission(clientMap);
					break;
				case PacketCommandType.RUNMISSION: //战斗
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.runmission(clientMap);
					break;
				case PacketCommandType.RUNMISSIONS: //连续战斗
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.runmissions(clientMap);
					break;	
				case PacketCommandType.GETPASSAWARD://获取通关奖励
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getpassward(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.GETSKILL: //夺技 低5级高5级玩家且拥有要夺的技能页
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getSkillList(clientMap);
					break;
				case PacketCommandType.ACTPK: //夺技 低5级高10级玩家且拥有要夺的技能页
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.actGetskill(clientMap);
					break;
				case PacketCommandType.PLAYERFIGHT: //准备决斗
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getUserFightList();
					break;
				case PacketCommandType.GETFIGHTBACK: //获取反击列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getUserFightBackList();
					break;
				case PacketCommandType.STARTFIGHT: //开始决斗
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.startfight(clientMap);
					break;
				case PacketCommandType.GETEGGLIST: //请求扭蛋
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getEgg();
					break;
				case PacketCommandType.OPENEGG: //开蛋
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.openEgg(clientMap);
					break;
				case PacketCommandType.DEVSALE: //装备卖出
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.devsale(clientMap);
					break;
				case PacketCommandType.GETFRIENDS://获取好友列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getFriends();
					break;
				case PacketCommandType.GETROLELIST://搜索REQFRIEND
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					String rname=clientMap.get("AA").toString();
					player.FindPlayer(rname);
					break;
				case PacketCommandType.REQFRIEND://申请好友
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					String applyrole=clientMap.get("AA").toString();
					player.ApplyPlayer(applyrole);
					break;
				case PacketCommandType.GETAPPLYLIST://获取待审列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.GetApplyList();
					break;
				case PacketCommandType.ADDFRIEND://审核好友
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					String arole=clientMap.get("BB").toString();
					int flag=Integer.parseInt(clientMap.get("AA").toString());
					player.verifyFriend(arole,flag);
					break;
				case PacketCommandType.DELFRIEND://删除好友
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					String delrole=clientMap.get("AA").toString();
					player.DelFriend(delrole);
					break;
				case PacketCommandType.FRIENDMATCH: //好友切磋
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.friendmatch(clientMap);
					break;
				case PacketCommandType.GETSHOPLIST://获取商城
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getShop();
					break;
				case PacketCommandType.BUYSOME://购买商城物品
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.buyGoods(clientMap);
					break;
				case PacketCommandType.APPDRINK://准备畅饮
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.drinkapp();
					break;
				case PacketCommandType.STARTDRINK://畅饮
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.startdrink();
					break;
				case PacketCommandType.GETGUAN://准备拜关公
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getguangong();
					break;
				case PacketCommandType.VISITGG://拜关公
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.visitgg();
					break;
				case PacketCommandType.GETSTEET://获取街霸列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getstreet();
					break;
				case PacketCommandType.FIGHTSTREET://挑战街霸
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.fightstreet(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.RESETSTREET://重置街霸
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.goldResetstreet();
					break;
				case PacketCommandType.RESETMSTASKCD://重置连续战斗CD
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.goldResetmissions();
					break;
				case PacketCommandType.BUYCHANCE://金条补充体力、夺计争斗次数
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.buychance(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.GETPROPS://道具背包
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getpropslist();
					break;
				case PacketCommandType.USEPROPS://使用道具
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.useprops(clientMap.get("AA").toString());
					break;
				case PacketCommandType.EXCHSTART://兑换码
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.exchstart(clientMap.get("AA").toString());
					break;
				case PacketCommandType.GETDAYTASK://获取日常任务列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getdaytask();
					break;
				case PacketCommandType.RUNDAYTASK://执行日常任务
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.rundaytask(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.GETPICLIST://获取图鉴
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getpiclist(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.SHAKETREE://摇钱
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.shaketree();
					break;
				case PacketCommandType.GETPOINTLIST://获取兑换商城列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getpointshoplist();
					break;
				case PacketCommandType.POINTEXCH://兑换
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.exchangepoint(clientMap);
					break;
				case PacketCommandType.REQMESSLIST://获取消息列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getusermess();
					break;
				case PacketCommandType.GETTIMEMESS://获取体力夺计恢复时间
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getrebacktime();
					break;
				case PacketCommandType.GETMAILBOX://打开邮箱
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getmailbox();
					break;
				case PacketCommandType.DRAWGOODS://打开邮件
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.openmail(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.REQCHARGE://请求充值
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.reqcharge(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.CHARGEEND://请求发货
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.chargeend();
					break;
				case PacketCommandType.CHARGESUCC://App发货
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.AppchargeSucc(clientMap);
					break;
				case PacketCommandType.LOGINAWARD://请求领取连续登陆奖励
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.loginaward(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.TRADEFLAG://完成新手
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.updatetrade(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.GETLOTLUCK://获取卡牌缘信息
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getcardlotluck(clientMap.get("AA").toString(),Integer.parseInt(clientMap.get("AB").toString()));
					break;
				case PacketCommandType.BUYMISSNUM://购买任务次数
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.buymissionnum(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.APPLYLUCKAWARD://获取抽奖列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.applyluckaward();
					break;
				case PacketCommandType.ACTLUCKAWARD://开始抽奖
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.actluckaward();
					break;
				case PacketCommandType.REQMISSIONTALK://获取对话
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.returntalklist(Integer.parseInt(clientMap.get("AA").toString()));
					break;
				case PacketCommandType.REQCHIPBAG://获取碎片背包
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.GetChipBag();
					break;
				case PacketCommandType.BLESSME://祈愿
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.BlessMeDown();
					break;
				case PacketCommandType.CHIPUNION://合成碎片
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.UnionChip(clientMap.get("AA").toString());
					break;
				case PacketCommandType.WANTWORLDBOSS://想要挑战世界BOSS
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.WantWorldBoss();
					break;
				case PacketCommandType.AUTOWORLDBOSS://设定自动挑战世界BOSS
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.SetAutoFightBoss();
					break;						
				case PacketCommandType.PKWORLDBOSS://挑战世界BOSS
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.PKWorldBoss(Integer.parseInt(clientMap.get("AA").toString()));
					break;	
				case PacketCommandType.REQOTHERUSER://获取其他玩家打boss列表
					/*player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.ReqOtherUserBeat();*/
					break;
				case PacketCommandType.READYPROCMARK://获取生成符文列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.getmarkproclist();
					break;
				case PacketCommandType.PROCMARK://生成符文
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.procmark(clientMap.get("AA").toString());
					break;					
				/*case PacketCommandType.APPLOGINAWARD://获取登录奖列表
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.apploginaward();
					break;*/
				case PacketCommandType.REQLEVELRANK://请求等级排名
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.reqlevelrank();
					break;
				case PacketCommandType.REQGOLDRANK://请求消费排名
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.reqgoldrank();
					break;
				case PacketCommandType.REQBOXRANK://请求宝箱排名
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.reqboxrank();
					break;
				case PacketCommandType.REQRANKAWARD://请求领奖
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.reqrankaward(clientMap.get("AA").toString());
					break;
				case PacketCommandType.REQVIPDESC://请求vip描述
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.reqvipdesc();
					break;
				case PacketCommandType.REDAYPS://准备美容
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.readayps();
					break;
				case PacketCommandType.ACTPSCARD://美容
					player=GameManager.getInstance().getPlayerMap().get(e.getChannel().getId());
					player.actpscard(Integer.parseInt(clientMap.get("AA").toString()),Integer.parseInt(clientMap.get("AB").toString()));
					break;
				case PacketCommandType.REDAYSHUT://准备停止服务器
					if(clientMap.get("AA").toString().equals("%5HHnz)ZZ@")){
						GameManager.getInstance().ReadyShutSvr();
					}					
					break;
				case 445:
					if(clientMap.get("AA").toString().equals("%5HHnz)ZZ@")){
						Map<String,Object> smap= new HashMap<String,Object>();
						smap.put("AA", 0);
						GameManager.getInstance().queryplayer(clientMap.get("BA").toString(),clientMap.get("BB").toString(),smap);
						smap.put("CMD", 1);
						e.getChannel().write(smap);
					}					
					break;
				case 446:
					if(clientMap.get("AA").toString().equals("%5HHnz)ZZ@")){
						Map<String,Object> smap= new HashMap<String,Object>();
						smap.put("AA", 0);
						GameManager.getInstance().querymoney(clientMap.get("BA").toString(),clientMap.get("BB").toString(),smap);
						smap.put("CMD", 2);
						e.getChannel().write(smap);
					}					
					break;
				}

			} catch (Exception e2) {
				//异常通知客户端？？？
				logger.error(e2.getMessage(), e2);
			}
		}else{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("CMD", PacketCommandType.GETERROR);
			map.put("EC", 1097);
			e.getChannel().write(map);
		}
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		try {
			//logger.info("用户断线");
			GameManager.getInstance().exitPlayer(e.getChannel().getId());
		} catch (Exception e1) 
		{
			//logger.info("User Offline Error"+e1.getMessage());
		}
		
	}
	
	   @Override
       public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
               throws Exception {
          // logger.info("信道关闭");
       }


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		//logger.warn("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
	public void closeRequested(ChannelHandlerContext ctx, ChannelStateEvent e) {  
	    ctx.sendUpstream(e);  
	}  
	
}
