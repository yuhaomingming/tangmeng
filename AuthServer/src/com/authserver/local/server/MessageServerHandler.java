package com.authserver.local.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.authserver.manager.AuthserverManager;
import com.authserver.model.GameServer;

public class MessageServerHandler extends SimpleChannelHandler {

	private static final Logger logger = Logger
			.getLogger(MessageServerHandler.class.getName()); 

	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		super.handleUpstream(ctx, e);
	}
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		System.out.println("A New GameServer Connected...");
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		try {

			String loginfo[] = ((String) e.getMessage()).split(",");
			logger.info("get message:" + loginfo[1]);
			AuthserverManager.getInstance().addGameMap(e.getChannel().getId(),loginfo[0]);
		} catch (Exception e1) {
			logger.error(e.getMessage(), e1);
		}
	}
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,ChannelStateEvent e) {
		try {
			logger.info("A AuthServer 断开...");
			AuthserverManager.getInstance().removeGameServer(e.getChannel().getId());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	} 
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// logger.warn("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}

}
