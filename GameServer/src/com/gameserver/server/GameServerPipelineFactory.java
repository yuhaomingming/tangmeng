/**
 * 
 */
package com.gameserver.server;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import com.gameserver.comm.AMF3Decode;
import com.gameserver.comm.AMF3Encoder;
import com.gameserver.handler.GameServerHandler;

public class GameServerPipelineFactory implements ChannelPipelineFactory{
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		pipeline.addLast("decode", new AMF3Decode());
		pipeline.addLast("encoder", new AMF3Encoder());
		pipeline.addLast("handler", new GameServerHandler());
		//pipeline.addLast("business", new LoginBusinessHandler());
		return pipeline;
	}

}
