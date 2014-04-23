package com.authserver.comm;

import java.io.ByteArrayInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;

public class AMF3Decode extends FrameDecoder {

	private boolean isReadHead = false;
	private int dataLength = 0;
 
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {

		if (buffer == null) { 
			return null;
		}
		if (buffer.readableBytes() >= 8 && !isReadHead) {
			dataLength = buffer.readInt();
			this.isReadHead = true;
		}
		Object message = null;
		if (isReadHead && buffer.readableBytes() >= dataLength) {
			// 读AMF3字节流的内容
			byte[] content = new byte[buffer.readableBytes()];
			buffer.readBytes(content);
			SerializationContext serializationContext = new SerializationContext();
			Amf3Input amf3Input = new Amf3Input(serializationContext);
			amf3Input.setInputStream(new ByteArrayInputStream(content));
			message = amf3Input.readObject();
			this.isReadHead = false;
			this.dataLength = 0;

		}
		return message;
	}

}
