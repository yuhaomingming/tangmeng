package com.gameserver.client;

import static org.jboss.netty.channel.Channels.pipeline;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.gameserver.comm.AMF3Decode;
import com.gameserver.comm.AMF3Encoder;


public class MessageClientPipelineFactory implements ChannelPipelineFactory{
	public ChannelPipeline getPipeline() throws Exception{
		ChannelPipeline pipline = pipeline();
		pipline.addLast("decoder", new StringDecoder());
		pipline.addLast("encoder", new StringEncoder());
		pipline.addLast("handler", new MessageClientHandler());
		return pipline;
	}
}
