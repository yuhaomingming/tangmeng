package com.authserver.comm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class AMF3Encoder extends OneToOneEncoder {
	private static Deflater deflater = new Deflater();
	private static int cacheSize = 1024;

	@Override 
	protected Object encode(ChannelHandlerContext cxt, Channel channel,
			Object msg) throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		SerializationContext serializationContext = new SerializationContext();
		Amf3Output amf3Output = new Amf3Output(serializationContext);
		amf3Output.setOutputStream(stream);
		amf3Output.writeObject(msg);
		byte[] objSe = stream.toByteArray();
		objSe = compress(objSe);// 压缩数据
		if (objSe != null && objSe.length > 0) {
			ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();// (2)
			// ChannelBuffer buffer = ChannelBuffers.buffer(objSe.length + 4);
			buffer.writeInt(objSe.length);
			buffer.writeBytes(objSe);
			return buffer;
		}
		return null;
	} 

	/**
	 * 压缩数据
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] input) throws IOException {
		deflater.reset();
		deflater.setInput(input);
		deflater.finish();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				input.length);
		byte[] buf = new byte[cacheSize];
		int len;
		while (!deflater.finished()) {
			len = deflater.deflate(buf);
			outputStream.write(buf, 0, len);
		}
		byte[] tmpByte = outputStream.toByteArray();
		outputStream.close();
		return tmpByte;
	}

}
