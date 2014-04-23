package com.authserver.comm;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

public class CommTools implements Serializable {

	/**
	 * UNO服务器公共应用类
	 */ 
	private static final long serialVersionUID = 2084629867887147146L;
	public static final int CHATMESSAGE_NORMAL = 0;// 普�?聊天
	public static final int CHATMESSAGE_GLOBAL = 1;// 世界喊话
	public static final int CHATMESSAGE_SECRET = 2;// 密语聊天
	public static final int CHATMESSAGE_FACE = 3;// 动�?表情
	public static final int CHATMESSAGE_SYSTEM = 4;// 系统公告
	public static final int SERVER_GATE = 1;// 入口服务�?
	public static final int SERVER_AUTH = 2;// 认证服务�?
	public static final int SERVER_LOBBY = 3;// 游戏服务�?
	public static final int SERVER_INFO = 4;// 信息服务�?
	public static final int SERVER_SHOPPING = 5;// 商城服务�? 
	public static final int QUITAIUSER = 6; // 保持断线托管用户
	public static final int DELQUITAIUSER = 7; // 清除断线托管用户
	private static String oldToken = "UNOAuthToken";
	private static String newToken = "UNOAuthToken";

	/**
	 * 返回与参数site相同长度的字符串，不足的填�?0”占�?
	 * 
	 * @param source
	 * @param site
	 * @return
	 */
	public static String returnStrByInt(String source, int site) {
		while (source.length() < site) {
			source = "0" + source;
		}
		return source;
	}

	public static boolean checkUserAuthToken(String _authToken) {
		/*
		 * if(!_authToken.equals(newToken)&&!_authToken.equals(oldToken)){
		 * return false; }else{ return true; }
		 */
		return true;
	}

	public static void setToken(String _newToken) {
		oldToken = newToken;
		newToken = _newToken;
	}

	public static String getToken() {
		return newToken;
	}
	/*
	 * public static String toDropsJSONStrings(List<AbstractDrop> drops){
	 * JSONArray result = new JSONArray(); for(int i = 0; i < drops.size();
	 * i++){ JSONObject object = drops.get(i).getJsonObject();
	 * result.put(object); } return result.toString(); } public JSONObject
	 * getJsonObject(){ JSONObject object = new JSONObject();
	 * object.put(getItemType(), getItemIdOrCount()); return object; }
	 */

}
