package com.gameserver.server;

import java.io.Serializable;
import java.util.TimerTask;

import com.gameserver.manager.GameManager;


public class ConnectAuthServerTask  extends TimerTask implements Serializable{

	/**
	 * UNO游戏服务器连接AuthServer任务
	 */
	private static final long serialVersionUID = -5755571130573370158L;

	@Override
	public void run() {
		GameManager.getInstance().connectAuthServer();
	}

}
