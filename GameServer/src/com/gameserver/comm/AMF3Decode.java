package com.gameserver.comm;

import java.io.ByteArrayInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;


//public class AMF3Decode extends FrameDecoder{
public class AMF3Decode extends LengthFieldBasedFrameDecoder  {

	private boolean isReadHead = false;
	private int dataLength = 0;
	private static int maxFrameLength=32768;
	private static int lengthFieldOffset=0;   
	private static int lengthFieldLength=4;
	private static int lengthAdjustment=0;
	private static int initialBytesToStrip=4;
	
	public AMF3Decode(){
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength,lengthAdjustment,initialBytesToStrip); 
	}
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,ChannelBuffer buffer) throws Exception {
		ChannelBuffer frame = (ChannelBuffer) super.decode(ctx, channel, buffer);   
		if (frame == null) {
			
			 return null;   
		}
		// 读AMF3字节流的内容
		Object message = null;
		byte[] content = new byte[frame.readableBytes()];
		frame.readBytes(content);
		SerializationContext serializationContext = new SerializationContext();
		Amf3Input amf3Input = new Amf3Input(serializationContext);
		amf3Input.setInputStream(new ByteArrayInputStream(content));
		message = amf3Input.readObject();
		return message;
	}
		 
	/*protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		
	if (buffer == null){
	       return null;
	}
	if(buffer.readableBytes() >= 8 && !isReadHead){
		dataLength = buffer.readInt();
		this.isReadHead = true;		
	}
	Object message = null;
	if(isReadHead && buffer.readableBytes() >=  dataLength){
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
	}*/
	
		
		
	}
