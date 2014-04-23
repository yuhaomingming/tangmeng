package com.gameserver.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;


public class MessageClient {
	public static MessageClient client = new MessageClient();
	
	private ClientBootstrap bootstrap;
	private ChannelFuture future;
	private MessageClient(){
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new MessageClientPipelineFactory());
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		future = bootstrap.connect(new InetSocketAddress("localhost", 12001));
		future.awaitUninterruptibly();
		if(!future.isSuccess()){
			future.getCause().printStackTrace();
			future.getChannel().getCloseFuture().awaitUninterruptibly();
			return ;
		}
	}
	
	public void messageSend(String msg){
		future.getChannel().write(msg);
	}
	
	public static MessageClient getInstance(){
		return client;
	}
	
	public static void main(String[] args) {
//		MessageClient.getInstance().messageSend("1,192.168.18.89,12001,\\u6E38\\u620F\\u5F00\\u59CB\\uFF0C\\u62BD\\u51FA\\u7684\\u5F15\\u5BFC\\u724C\\u662F,0");
	}
	
	public static String decodeUnicode(String theString) {       
		char aChar;     
		int len = theString.length();    
		StringBuffer outBuffer = new StringBuffer(len);    
		for (int x = 0; x < len;) {    
			aChar = theString.charAt(x++);     
			if (aChar == '\\') {    
				aChar = theString.charAt(x++);     
			if (aChar == 'u') {    
				int value = 0;     
				for (int i = 0; i < 4; i++) {     
					aChar = theString.charAt(x++);     
					switch (aChar) {     
						case '0':      
						case '1':     
						case '2':     
						case '3':     
						case '4':     
						case '5':     
						case '6':     
						case '7':     
						case '8':     
						case '9':     
							value = (value << 4) + aChar - '0';     
							break;     
						case 'a':     
						case 'b':     
						case 'c':     
						case 'd':     
						case 'e':     
						case 'f':     
							value = (value << 4) + 10 + aChar - 'a';     
							break;     
						case 'A':     
						case 'B':     
						case 'C':     
						case 'D':     
						case 'E':     
						case 'F':     
							value = (value << 4) + 10 + aChar - 'A';     
							break;     
							default:     
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");     
						}     
				}     
				outBuffer.append((char) value);     
        }else{     
        	if (aChar == 't')     
        		aChar = '\t';     
        	else if (aChar == 'r')     
        		aChar = '\r';     
        	else if (aChar == 'n')     
        		aChar = '\n';     
        	else if (aChar == 'f')     
        		aChar = '\f';     
        	outBuffer.append(aChar);     
       }     
	}else     
		outBuffer.append(aChar);     
	}     
		return outBuffer.toString();     
    }   
	
}
