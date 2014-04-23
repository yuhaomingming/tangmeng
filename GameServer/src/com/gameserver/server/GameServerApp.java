/**
 * 
 */
package com.gameserver.server;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import com.gameserver.comm.C3P0DBConnectionPool;
import com.gameserver.comm.LanguagePack;
import com.gameserver.manager.GameManager;



public class GameServerApp {

	private static final Logger logger = Logger.getLogger(GameServerApp.class.getName());
	private static Properties props;
	
	public static void main(String[] args) {
		readArgs(args);
		init();
		setUpSocketServer();
	}
	
	private static void readArgs(String[] args){
		try {
			FileInputStream is = new FileInputStream(args[0]);
			props = new Properties();
			props.load(is);
			logger.info("ServerConfig Finished...");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("ServerConfig Error...");
		}
	}
	

	private static void init() {
		try {
			C3P0DBConnectionPool.setProps(props);
			C3P0DBConnectionPool.getInstance();// 初始化数据库连接池
			GameManager.setProps(props);
			GameManager.getInstance();
			GameManager.getInstance().init();
			GameManager.getInstance().connectAuthServer();
			LanguagePack.setProps(props);
			new Timer().schedule(new ConnectAuthServerTask(), 5000, 5000);
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
	private static void setUpSocketServer() {		
		// 启动外网服务监听
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new GameServerPipelineFactory());
		bootstrap.bind(new InetSocketAddress(props.getProperty("Public_IP"),
				Integer.parseInt(props.getProperty("Public_Port"))));

	}


}
