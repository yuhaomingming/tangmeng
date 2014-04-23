package com.authserver.model;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import com.authserver.dao.AuthServerDao;


public class User implements Serializable {

	private static final long serialVersionUID = -3322965795421874433L;
	private static final Logger logger = Logger.getLogger(User.class.getName());
	private String uname;// 玩家昵称
	private ArrayList<Map<String, String>> rolelist;// 角色服务器列表 服务器编码，角色名
	private String cursvrcode;// 最近登录的服务器
	private final Channel channel;
	
	public User(Map<String, Object> _cmap, Channel channel) throws Exception {
		this.channel = channel;
		this.uname = _cmap.get("UN").toString();
		//logger.info("返回token="+this.uname);
		if(!_cmap.get("VE").toString().equals("Debug")){
			//传递渠道号
			this.rolelist = new ArrayList<Map<String, String>>();
			// 获取用户角色及服务器组
			this.cursvrcode = GetUserRole(this.rolelist, this.uname);
			
		}else{

			//传递渠道号  qiduname
			this.rolelist = new ArrayList<Map<String, String>>();
			// 获取用户角色及服务器组
			this.cursvrcode = GetUserRole(this.rolelist, this.uname);
		}
		logger.info("this.uname####################" + this.uname);
	}

	public String GetUserRole(ArrayList<Map<String, String>> rolelist,String username) throws SQLException {
		AuthServerDao dao = new AuthServerDao();
		return dao.GetUserRole(rolelist, username);

	}
	
	public void send(Map<String, Object> sendMap) {
		try {
			if (this.channel != null && this.channel.isConnected()) {
				this.channel.write(sendMap);
			}
		} catch (Exception e) {
			logger.info("-------------->" + sendMap.get("CMD") + ":::"
					+ e.getMessage());
		}
	}

	public Channel getChannel() {
		return this.channel;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public ArrayList<Map<String, String>> getRolelist() {
		return rolelist;
	}

	public void setRolelist(ArrayList<Map<String, String>> rolelist) {
		this.rolelist = rolelist;
	}

	public String getCursvrcode() {
		return cursvrcode;
	}

	public void setCursvrcode(String cursvrcode) {
		this.cursvrcode = cursvrcode;
	}
	
}
