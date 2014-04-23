/**
 * 
 */
package com.gameserver.server;

import java.io.Serializable;
import java.util.TimerTask;
import org.apache.log4j.Logger;

import com.gameserver.model.User;


public class HeartBeatTask extends TimerTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6364207841571763263L;

	private User player = null;
	
	private static final Logger logger = Logger.getLogger(HeartBeatTask.class.getName());
	
	public HeartBeatTask(User player){
		 
		this.player = player;
	}
	
	@Override
	public void run(){
		try{
			this.player.checkPlayerDiconnStatus();//检查玩家是否掉线
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

	
}