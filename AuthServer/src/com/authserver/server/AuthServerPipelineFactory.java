package com.authserver.server;

import static org.jboss.netty.channel.Channels.pipeline;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import com.authserver.comm.AMF3Decode;
import com.authserver.comm.AMF3Encoder;
import com.authserver.handler.AuthServerHandler;

public class AuthServerPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipline = pipeline();
		pipline.addLast("decode", new AMF3Decode());
		pipline.addLast("encoder", new AMF3Encoder());
		pipline.addLast("handler", new AuthServerHandler());
		return pipline;
	}
}
