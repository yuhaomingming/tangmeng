package com.authserver.local.server;

import static org.jboss.netty.channel.Channels.pipeline;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class MessageServerPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipline = pipeline();
		pipline.addLast("decoder", new StringDecoder());
		pipline.addLast("encoder", new StringEncoder());
		pipline.addLast("handler", new MessageServerHandler());
		return pipline;
	}
}
