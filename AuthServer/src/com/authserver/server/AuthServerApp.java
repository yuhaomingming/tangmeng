package com.authserver.server;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.authserver.comm.C3P0DBConnectionPool;
import com.authserver.comm.LanguagePack;
import com.authserver.local.server.MessageServerPipelineFactory;
import com.authserver.manager.AuthserverManager;

public class AuthServerApp {
	private static final Logger logger = Logger.getLogger(AuthServerApp.class
			.getName());
	private static Properties props;
	

	public static void main(String[] args) {
		readArgs(args);
		init();
		setUpSocketServer();
		logger.info("AuthServer Ready...");
	}

	private static void readArgs(String[] args) {
		try {
			FileInputStream is = new FileInputStream(args[0]);
			props = new Properties();
			props.load(is);
			logger.info("ServerConfig Finished...");
			// System.out.println("token server is already started! update time "
			// + Integer.parseInt(props.getProperty("Token_Update")));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ServerConfig Error...");
			logger.info("ServerConfig Error...");
		}
	}

	private static void init() {
		try {
			C3P0DBConnectionPool.setProps(props);
			C3P0DBConnectionPool.getInstance();// 初始化数据库连接池
			AuthserverManager.setProps(props);
			AuthserverManager.getInstance();
			AuthserverManager.getInstance().init();
			LanguagePack.setProps(props);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setUpSocketServer() {
		// 启动外网服务监听
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new AuthServerPipelineFactory());
		bootstrap.bind(new InetSocketAddress(props.getProperty("Public_IP"),
				Integer.parseInt(props.getProperty("Public_Port"))));
		// 启动内网服务监听
		ServerBootstrap bootstraplocal = new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors
						.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstraplocal.setPipelineFactory(new MessageServerPipelineFactory());
		bootstraplocal.bind(new InetSocketAddress(props
				.getProperty("Private_IP"), Integer.parseInt(props
				.getProperty("Private_Port"))));
	}
}
