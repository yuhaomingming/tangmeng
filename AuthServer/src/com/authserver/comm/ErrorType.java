package com.authserver.comm;

public class ErrorType {
	public static final int NET_UNUSUAL = 1000;// 网络异常
	public static final int USER_LOGINED = 1001;// 同名用户已登�?
	public static final int USER_NICKEXIST = 1002;// 存在相同昵称
	public static final int FREEGOLDLIMITED = 1003;// 免费金币超过领取次数
	public static final int TABLEFULL = 1004;// 桌上人数已满
	public static final int TOKENERROR = 1005;// 用户数据异常（Token认证失败�?
	public static final int GOLDNOTENOUGH = 1006;// 金币数量不足
	public static final int ERRORPWD = 1009;// 密码�?
	public static final int ERRORTABLE = 1010;// 桌子已移�?
	public static final int COINNOTENOUGH = 1020;// 光合币不�?
	public static final int DBERROR = 1021;// 数据库异�?
	public static final int USERLOST = 1024; // 用户数据异常被托�?
	public static final int NEEDCHARGE = 1025; // 非vip用户
}
