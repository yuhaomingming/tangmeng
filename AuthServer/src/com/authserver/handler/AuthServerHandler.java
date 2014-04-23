package com.authserver.handler;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.authserver.comm.PacketCommandType;
import com.authserver.manager.AuthserverManager;
import com.authserver.model.User;

public class AuthServerHandler extends SimpleChannelHandler {

	private static final Logger logger = Logger.getLogger(AuthServerHandler.class.getName());
	private Integer ClientMessageCommand = 0;

	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		// System.out.println("Client Connected...");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		try {
			User user;
			Map<String, Object> clientMap = (HashMap<String, Object>) e.getMessage();
			Map<String, Object> sendMap = new HashMap<String, Object>();
			ClientMessageCommand = Integer.parseInt(clientMap.get("CMD").toString());// 解析客户端命令类型
			switch (ClientMessageCommand) {
			case PacketCommandType.GETSERVERLIST:// 获取服务器列表
				user = new User(clientMap, e.getChannel());
				AuthserverManager.getInstance().addUser(user);
				//logger.info("收到请求服务器包：" + clientMap.get("UN").toString());
				AuthserverManager.getInstance().returnSvrRoleList(user);
				break;

			}
		} catch (Exception e1) {
			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("CMD", PacketCommandType.GETERROR);
			sendMap.put("ER","1000");
			e.getChannel().write(sendMap);
			e1.printStackTrace();
			logger.info(e1.getMessage()); 
		}
	}

	public void sendMessage(Map<String, Object> map, Channel channel) {
		if (channel != null) {			
			channel.write(map);
		}
	}
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		try {
			AuthserverManager.getInstance().removeUser(e.getChannel().getId());
			// System.out.println("Client Disconnected...");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// logger.warn("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}
