package com.gameserver.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.gameserver.manager.GameManager;
import com.gameserver.model.User;

public class MessageClientHandler extends SimpleChannelUpstreamHandler{
	private static final Logger logger = Logger.getLogger(MessageClientHandler.class.getName());
	private Integer ClientMessageCommand=0;
	
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		/*try{
			User user;
			Map<String, Object> clientMap = (HashMap<String, Object>) e.getMessage();
			ClientMessageCommand=Integer.parseInt(clientMap.get("CMD").toString());//解析客户端命令类型
			switch(ClientMessageCommand){
			case 2://
				logger.info("收到消息："+clientMap.get("UC"));
				user= new User((Map<String, Object>)clientMap.get("MS"),Integer.parseInt(clientMap.get("UC").toString()));
				user.testsend();
				break;
			}
		}catch(Exception e1){
			
		}*/
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		GameManager.getInstance().setSendServerInfo(false);
		e.getChannel().close();
	}
}
