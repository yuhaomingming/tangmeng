package com.authserver.comm;

public class PacketCommandType {
	
	public static final int LOGIN = 101;// 登录请求
	public static final int GETSERVERLIST = 102;//请求服务器列表
	public static final int RETURNSERVERLIST = 105;//返回服务器列表
	public static final int GETERROR=107;//发生异常
}
