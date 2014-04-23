/**
 * 
 */
package com.gameserver.server;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;

import com.gameserver.comm.CommTools;
import com.gameserver.manager.GameManager;


public class SysTimeTask extends TimerTask implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192248248648430096L;
	private static final Logger logger = Logger.getLogger(SysTimeTask.class.getName());
	private int rnum=0;
	public SysTimeTask(){
		rnum=0;
	}
	@Override
	public void run(){
		try{
			GameManager.getInstance().checkSysTask();//检查系统任务 检查字典更新			
			rnum=GameManager.getInstance().getRunnum()+1;
			GameManager.getInstance().setRunnum(rnum);
			if(rnum>=2) {
				rnum=0;
				GameManager.getInstance().setRunnum(0);
				//更新系统活动公告
				GameManager.getInstance().RefreshMess();
			}	
			//每天boss启动前10分钟开启计时器
			if(System.currentTimeMillis()/1000 > CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getBhour(),GameManager.getInstance().getWorldBoss().getBminute()-10)&&
					System.currentTimeMillis()/1000 <= CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getEhour(),GameManager.getInstance().getWorldBoss().getEminute()+5)){
				if(GameManager.getInstance().getBeattimer()==null){
					logger.info("开启计时器");
					GameManager.getInstance().setBeattimer(new Timer());
					GameManager.getInstance().getBeattimer().schedule(new BeatBossTimeTask(), 5000, 5000);
				}
			}
			//每天13:00点以后 并且不是新的boss则重置boss,
			if(System.currentTimeMillis()/1000 > CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getEhour(),GameManager.getInstance().getWorldBoss().getEminute()) && !GameManager.getInstance().getWorldBoss().isIsnew()){//过了13点
				GameManager.getInstance().ResetWorldBoss();
			}
			//12:31-12:40开始自动结算设置自动boss的用户 
			if(System.currentTimeMillis()/1000>CommTools.GetTimeInMillis(0, GameManager.getInstance().getWorldBoss().getBhour(), GameManager.getInstance().getWorldBoss().getBminute())&&
					System.currentTimeMillis()/1000<=CommTools.GetTimeInMillis(0,GameManager.getInstance().getWorldBoss().getEhour(),GameManager.getInstance().getWorldBoss().getEminute())){
				GameManager.getInstance().AutoCalcBoss();
				GameManager.getInstance().CalcBossRank();
			}
			
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

	
}