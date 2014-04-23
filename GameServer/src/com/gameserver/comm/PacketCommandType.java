package com.gameserver.comm;

public class PacketCommandType {
	public static final int LOGIN=101;//登录请求
	//public static final int LOGOUT=102;//登出请求
	public static final int LOGINSUCCESS=104;//登录成功
	public static final int CREATEROLE=106;//创建角色
	public static final int GETERROR=107;  //发生异常"/>
	public static final int CHANGECARD=108; //卡牌换位
	public static final int GETCARDINFO=109;  //"卡牌信息"/>
	public static final int RETURNCARDINFO=110;  //返回卡牌信息"/>
	public static final int GETCARDLIST=111; //请求背包卡牌列表
	public static final int RETURNCARDLIST=112; //返回背包卡牌列表
	public static final int GETDEVICELIST=113; //请求背包装备列表
	public static final int RETURNDEVICELIST=114; //返回背包装备列表
	public static final int GETUSKILLLIST=115; //请求背包技能列表
	public static final int RETURNUSKILLLIST=116; //返回背包技能列表
	public static final int GETUMARKLIST=117; //请求背包符文列表
	public static final int RETURNUMARKLIST=118; //返回背包符文列表
	public static final int GETUMPLIST=119; //请求技能书页列表
	public static final int RETURNUMPLIST=120; //返回技能书页列表
	public static final int ALTERCARD=121;      //更换卡牌
	public static final int RETURNALTER=122; //卡牌更换完成
	public static final int LOADDEVICE=125; //穿上装备"/>
	public static final int USEDDEVICE=126; //穿上装备成功"/>
	public static final int LOADMARK=127;  //符文镶嵌"/>
	public static final int STAMPMARK=128; //符文镶嵌成功"/>
	public static final int GETMARKSETS=129;//获取符文组"/>
	public static final int RETURNMARKSETS=130;//返回符文组"/>
	public static final int LOADSKILL=131;//技能安装
	public static final int SKILLACT=132;//技能安完
	public static final int UPGRADEDEVICE=135;//精练装备
	public static final int RETURNDEVGRADE=136;//返回精练数值
	public static final int STARTUPGRADE=137; //开始精炼
	public static final int UPGRADESUC=138; //精炼完成
	public static final int LIVEUPCARD=139;//申请培养
	public static final int RETURNLIVEUP=140;//返回培养信息
	public static final int ACTLIVEUP=141; //开始培养
	public static final int ACTLIVEUPSUC=142;//培养完成
	public static final int SAVELIVEUP=143; //培养保存
	public static final int SAVELIVEUPSUC=144; //保存成功LIFEUP
	public static final int LIFEUP=145; //请求转生
	public static final int RETURNLIFEUP=146; //返回转生信息
	public static final int ACTLIFEUP=147; //开始转生
	public static final int ACTLIFEUPSUC=148; //转生成功
	public static final int CARDMERGE=149; //融魂
	public static final int RETURNMERGEINFO=124; //融魂信息
	public static final int ACTCARDMERGE=133;//开始融魂
	public static final int CARDMERGESUC=150; //融魂完成
	public static final int CHANGECARDSUC=151; //卡牌换位完成
	public static final int ABORTLIVEUP=123;//放弃培养
	public static final int PRODUCESKILL=152;//技能修炼"/>
	public static final int PROSKILLSUC=153;//技能修炼结果"/>
	public static final int UNIONSKILL=154;//技能合成"/>
	public static final int UNIONSKILLSUC=155;//合成结果"/>

	//##
	public static final int ACTMISSION=160; //进入任务
	public static final int RETURNMISSION=161; //返回任务
	public static final int RUNMISSION=162; //战斗
	public static final int RETURNRESULT=163; //战斗结果
	
	//##
	public static final int  GETSKILL=164; //夺技"/>
	public static final int RETURNGETSKILL=165;//返回可夺技玩家列表"/>
	public static final int  ACTPK=166; //玩家对战"/>
	public static final int RETURNPKSKILL=167;//返回夺技结果"/>
	
	//##
	public static final int PLAYERFIGHT=168;//准备争斗"/>
	public static final int FIGHTULIST=169;//返回争斗列表"
	public static final int STARTFIGHT=170;//"开始决斗"/>
	public static final int FINISHFIGHT=171;//返回决斗结果"/>
	
	public static final int GETEGGLIST=172;//请求扭蛋列表"/>
	public static final int RETURNEGG=173;//返回扭蛋列表"/>
	public static final int OPENEGG=174;///开蛋"/>
	public static final int OPENRESULT=175;//蛋掉落"/>
	public static final int EXCHSTART=176;//活动兑换"/>
	public static final int RETURNEXCH=177;//兑换完成"/>
	public static final int GETSHOPLIST=178;//请求商城列表"/>
	public static final int RETURNSHOP=179;//返回商城列表"/>
	public static final int DEVSALE=180;//卖出装备"/>
	public static final int DEVSALED=181;//卖出成功"/>
	public static final int GETFRIENDS=182;//获取好友列表
	public static final int RETURNFRIEND=183;//返回好友列表
	public static final int GETAPPLYLIST=193;//获取待审核列表"/>
	public static final int RTNAPPLYLIST=194;//返回待审核列表"/>
	public static final int GETROLELIST=184;//搜索角色"/>
	public static final int RETROLELIST=185;//角色列表"/>
	public static final int REQFRIEND=186;//申请好友"/>
	public static final int REQSUCC=187;//申请完成"/>
	public static final int ADDFRIEND=188;//审核好友"/>
	public static final int ADDSUCC=189;//审核完成"/>
	public static final int BUYSOME=191;// 购买商品"/>
	public static final int BUYSUCC=192;//购买商品成功"/>
	
	//##
	public static final int FRIENDMATCH=195;//好友切磋"/>
	public static final int FINISHMATCH=196;//返回切磋结果"/>
	
	public static final int DELFRIEND=197;//删除好友"/>
	public static final int DELSUCC=198;//删除好友完成"/>
	public static final int APPDRINK=199;//准备畅饮
	public static final int RETURNDRINK=200;//返回畅饮状态"/>
	public static final int STARTDRINK=201;//开喝"/>
	public static final int FINISHDRINK=202;//畅饮完成
	public static final int GETGUAN=203;//获取关公状态"/>
	public static final int RETURNGG=204;//返回关公状态"/>
	public static final int VISITGG=205;//拜关公"/>
	public static final int FINISHVISIT=206;//参拜完成"/>
	
	//##
	public static final int GETSTEET=207;//获取街霸列表"/>
	public static final int RETURNSTREET=208;//返回街霸列表"/>
	public static final int FIGHTSTREET=209;//挑战街霸"/>
	public static final int STREETSUCC=210;//挑战结果"/>
	
	
	public static final int RESETSTREET=211;//重置街霸"/>
	public static final int RESETSUCC=212;//重置完成"/>
	public static final int BUYCHANCE=213;//金条补充体力夺计争斗"/>
	public static final int SUCCBUY=214;//补充成功"/>
	public static final int GETPROPS=215;//获取道具背包"/>
	public static final int RETURNPROPS=216;//返回道具背包"/>
	public static final int USEPROPS=217;//使用道具"/>
	public static final int USEDPROPS=218;//道具使用完成"/>
	public static final int SHAKETREE=219;//摇钱"/>
	public static final int SHAKETREEFIN=220;//摇完成"/>
	public static final int GETDAYTASK=221;//获取日常任务列表"/>
	public static final int RUNDAYTASK=222;//完成日常任务"/>
	public static final int RETURNDAYTASK=223;//返回日常任务列表"/>
	public static final int FINDAYTASK=224;//完成日常任务"/>
	public static final int GETPICLIST=225;//获取图鉴列表
	public static final int RETURNPICS=226;//返回图鉴列表
	public static final int GETPOINTLIST=227;//请求积分兑换列表"/>
	public static final int POINTLIST=228;//返回积分兑换列表"/>
	public static final int POINTEXCH=229;//积分兑换"/>
	public static final int FINISHEXCH=230;//兑换完成"/>
	public static final int REQMESSLIST=231;//获取消息列表"/>
	public static final int RETURNMESS=232;//返回消息列表"/>
	public static final int PUBLICMESS=233;//滚动消息列表"/>
	public static final int UPDATEMESS=234;//体力夺计更新消息
	public static final int GETTIMEMESS=235;//获取体力夺计时间
	public static final int GETMAILBOX=236;//获取邮箱内容"/>
	public static final int RETURNMAILBOX=237;//返回邮箱列表"/>
	public static final int DRAWGOODS=238;//领取邮箱物品"/>
	public static final int DRAWSUCCESS=239;//领取邮箱完成"/>
	public static final int REQCHARGE=240;//请求充值"/>
	public static final int RETURNCHARGE=241;//返回订单"/>
	public static final int CHARGEEND=242;//充值确认"/>
	public static final int FINISHCHARGE=243;//充值完成"/>
	public static final int LOGINAWARD=244;//领取连续登陆奖励"/>
	public static final int LOGAWARDFIN=245;//领取完成"/>
	public static final int TRADEFLAG=246;//新手步骤号更新
	public static final int GETLOTLUCK=247;//获取卡牌原信息"/>
	public static final int RETURNLOTLUCK=248;//返回卡牌缘"/>
	public static final int GETFIGHTBACK=249;//获取反击列表"/>
	public static final int RETURNFIGHTBACK=250;//返回反击列表"/>
	public static final int GETPASSAWARD=251;//领取通关奖励"/>
	public static final int PASSAWARDSUCC=252;//通关奖励完成"/>
	public static final int BUYMISSNUM=253;//购买任务次数"/>
	public static final int BUYMISSFINISH=254;//购买任务次数完成"/>
	public static final int APPLYLUCKAWARD=255;//申请幸运抽奖"/>
	public static final int RETURNLUCKAWARD=256;//返回抽奖"/>
	public static final int ACTLUCKAWARD=257;//开始抽奖"/>
	public static final int LUCKAWARDFINISH=258;//幸运抽奖结果"/>
	public static final int CHARGESUCC=259;//APPStroe充值成功"/>
	public static final int REQMISSIONTALK=260;//获取关卡对话"/>
	public static final int RETURNMISSIONTALK=261;//返回关卡对话"/>
	
	public static final int RUNMISSIONS=262; //连续战斗
	public static final int RETURNRUNMISSIONS=263; //连续战斗结果
	public static final int RESETMSTASKCD=264; //重置连续战斗CD
	public static final int RESETMSTACKCDSUCC=265;//重置连续战斗CD成功
	public static final int REQCHIPBAG=266;//获取碎片背包"/>
	public static final int RETEURNCHIP=267;//返回碎片背包"/>
	public static final int BLESSME=268;//祈愿碎片"/>
	public static final int RETURNBLESS=269;//返回祈愿结果"/>
	public static final int CHIPUNION=270;//合成碎片"/>
	public static final int SUCCHIPUNION=271;//合成碎片完成"/>
	public static final int WANTWORLDBOSS=272;//想挑战世界BOSS"/>
	public static final int RETURNWORLDBOSS=273;//返回挑战世界BOSS数据"/>
	public static final int AUTOWORLDBOSS=274;//设置自动挑战世界BOSS"/>
	public static final int RETURNAUTOWORLDBOSS=275;//返回设置自动挑战世界BOSS结果"/>
	public static final int PKWORLDBOSS=276;//挑战世界BOSS"/>
	public static final int RETURNPKWORLDBOSS=277;//返回挑战世界BOSS战斗结果"/>
	public static final int REQOTHERUSER=278;//请求其他用户打boss列表"/>
	public static final int RETURNOTHERUSER=279;//返回其他玩家打boss列表"/>
	public static final int READYPROCMARK=280;//准备生成符文"/>
	public static final int RETURNPROCMARKS=281;//返回生成符文列表"/>
	public static final int PROCMARK=282;//生成符文"/>
	public static final int SUCCMARK=283;//生成符文完成"/>
	public static final int REQLEVELRANK=284;//请求等级排名"/>
	public static final int RETURNLEVELRANK=285;//返回等级排名"/>
	public static final int REQGOLDRANK=286;//请求消费排名"/>
	public static final int RETURNGOLDRANK=287;//返回消费排名"/>
	public static final int REQBOXRANK=288;//请求宝箱排名"/>
	public static final int RETURNBOXRANK=289;//返回宝箱排名"/>
	public static final int REQRANKAWARD=290;//领排名奖"/>
	public static final int GIVERANKAWARD=291;//发排名奖"/>
	public static final int REDAYPS=292;//准备美容"/>
	public static final int RETURNPS=293;//返回美容信息"/>

	public static final int ACTPSCARD=296;//选择美容卡牌美容"/>
	public static final int RETURNACTPS=297;//返回美容结果"/>
	public static final int REQVIPDESC=298;//请求vip描述"/>
	public static final int RETURNVIPDESC=299;//返回vip描述"/>
	
	
	public static final int HERATBEAT=333;//心跳包"/>
	public static final int REDAYSHUT=444;
	
	
	
}
