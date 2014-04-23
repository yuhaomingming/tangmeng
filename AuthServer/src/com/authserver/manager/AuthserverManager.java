package com.authserver.manager;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import com.authserver.comm.PacketCommandType;
import com.authserver.dao.AuthServerDao;
import com.authserver.model.GameServer;
import com.authserver.model.SceneDict;
import com.authserver.model.User;

public class AuthserverManager implements Serializable {

	private static final long serialVersionUID = -2271779088777378674L;
	private static final Logger logger = Logger.getLogger(AuthserverManager.class.getName());
	private static AuthserverManager instance;
	private Map<Integer, User> userMap;// 玩家对象集合
	private Map<String, Channel> userInfoMap;// 键值为用户名的玩家集合
	private Map<Integer, GameServer> servermap; // 服务器channeid，服务器对象
	private Map<String, GameServer> serverlistmap; // 服务器code，服务器对象
	private String cardlist;//出生可选卡牌
	private String cardcode; //卡牌编码
	private static Properties props;
	private String carddesc;//卡牌描述
	private ArrayList<SceneDict> scenedict ;//<场景id，场景>


	public AuthserverManager() throws SQLException {
		this.servermap = new HashMap<Integer, GameServer>();
		this.userMap = new ConcurrentHashMap<Integer, User>();
		this.userInfoMap = new ConcurrentHashMap<String, Channel>();
		this.cardlist = props.getProperty("CardList");
		this.cardcode = props.getProperty("CardCode");
		this.carddesc=props.getProperty("CardDesc");
		this.serverlistmap = new HashMap<String, GameServer>();
		this.scenedict=new ArrayList<SceneDict>();
	
	}

	public static AuthserverManager getInstance() throws SQLException {
		if (instance == null) {
			instance = new AuthserverManager();
		}
		return instance;
	}
	public void init() throws SQLException{
		AuthServerDao dao = new AuthServerDao();
		dao.GetServList(this.serverlistmap);
	}

	public void addUser(User _user) {
		synchronized (this) {
			this.userMap.put(_user.getChannel().getId(), _user);
			this.userInfoMap.put(_user.getUname(), _user.getChannel());
		}
	}

	public void addGameMap(Integer chid, String svrcode) {
		logger.info("服务器列表长度"+this.serverlistmap.size());
		this.serverlistmap.get(svrcode).setIfrun("1");
		logger.info("设置服务器为可用："+this.serverlistmap.get(svrcode).getCode()+"++++:"+this.serverlistmap.get(svrcode).getIfrun());
		this.servermap.put(chid, this.serverlistmap.get(svrcode));
		
		
	}

	public void removeGameServer(Integer chid) {
		String svrcode = "";
		svrcode = this.servermap.get(chid).getCode();
		this.serverlistmap.get(svrcode).setIfrun("0");
		this.servermap.remove(chid);
	}

	public void returnSvrRoleList(User player) {
		Map<String, Object> sendMap = new HashMap<String, Object>();
		ArrayList<Map<String, String>> serverlist = new ArrayList<Map<String, String>>();// 游戏服列表 服务器编码，服务器对象
		sendMap.put("CMD", PacketCommandType.RETURNSERVERLIST);
		String mdisp="@",tdisp="@",bdisp="@";
		int stime=0,etime=0;
		int curtime = (int)(System.currentTimeMillis()/1000);
		Iterator<GameServer> it = this.serverlistmap.values().iterator();
		while(it.hasNext()){
			GameServer ss = it.next();
			Map<String, String> umap = new HashMap<String, String>();
			umap.put("SN", ss.getName());
			umap.put("SC", ss.getCode());
			umap.put("IP", ss.getIpadd());
			umap.put("PO", ss.getPort());
			umap.put("SS", ss.getIfrun());
			umap.put("NE", ss.getIfnew());
			umap.put("DE", ss.getIfdef());
			umap.put("VE", ss.getGamever());
			umap.put("RV", ss.getResVersion());
			umap.put("LV", ss.getLastVersion());
			mdisp=ss.getMaindisp();
			tdisp=ss.getTopdisp();
			bdisp=ss.getBottdisp();
			stime=ss.getStarttime();
			etime=ss.getEndtime();
			serverlist.add(umap);
		}
		Collections.sort(serverlist,new Comparator<Map<String,String>>(){
			public int compare(Map<String,String> arg0, Map<String,String> arg1) {  
				return Integer.parseInt(arg1.get("SC").toString())-Integer.parseInt(arg0.get("SC").toString()); }  

		});
		Map<String, String> testmap=new HashMap<String, String>();
		for(int i=0;i<serverlist.size();i++){
			if(serverlist.get(i).get("DE").equals("1")){
				testmap.put("SN", serverlist.get(i).get("SN"));
				testmap.put("SC", serverlist.get(i).get("SC"));
				testmap.put("IP", serverlist.get(i).get("IP"));
				testmap.put("PO", serverlist.get(i).get("PO"));
				testmap.put("SS", serverlist.get(i).get("SS"));
				testmap.put("NE", serverlist.get(i).get("NE"));
				testmap.put("DE", serverlist.get(i).get("DE"));
				testmap.put("VE", serverlist.get(i).get("VE"));
				testmap.put("RV", serverlist.get(i).get("RV"));
				testmap.put("LV", serverlist.get(i).get("LV"));
				serverlist.remove(i);
				serverlist.add(testmap);
				break;
			}
		}	
		ArrayList<Map<String, Object>> slist=new ArrayList<Map<String, Object>>();
		for(int i=0;i<this.scenedict.size();i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("AA", this.scenedict.get(i).getSceneno());
			map.put("AB", this.scenedict.get(i).getScecode());
			slist.add(map);
		}
		sendMap.put("AC", slist.toArray()); //场景列表
		sendMap.put("SL", serverlist.toArray()); // 服务器列表
		sendMap.put("RL", player.getRolelist().toArray());// 用户服务器角色对照
		sendMap.put("CU", player.getCursvrcode());// 最近游戏服务器
		sendMap.put("CL", this.cardlist);
		sendMap.put("CC", this.cardcode);
		sendMap.put("CD", this.carddesc);
		sendMap.put("UI", player.getUname());
		if(curtime>=stime&&curtime<etime){
			sendMap.put("MP", mdisp);
			sendMap.put("TP", tdisp);
			sendMap.put("BP", bdisp);
		}else{
			sendMap.put("MP", "Bg_main.swf");
			sendMap.put("TP", "Bg_allTop.swf");
			sendMap.put("BP", "Bg_allbottom.swf");
		}
		player.send(sendMap);
		//slogger.info("已返回服务器列表可选卡牌："+this.cardlist);
	}

	/**
	 * 玩家断开连接
	 */
	public void userExit(int channelId) {
		User user = this.getUserMap().get(channelId);
		if (user != null) {
			user.getChannel().disconnect();
		}
	}

	public void removeUser(int channelId ) {
		synchronized (this) {
			if(this.userMap.containsKey(channelId)){
				String uname=this.userMap.get(channelId).getUname();
				this.userMap.remove(channelId);
				this.userInfoMap.remove(uname);
			}			
		}
	}

	public Map<Integer, User> getUserMap() {
		return userMap;
	}

	public static Properties getProps() {
		return props;
	}

	public static void setProps(Properties _props) {
		props = _props;
	}

	public ArrayList<SceneDict> getScenedict() {
		return scenedict;
	}

	public void setScenedict(ArrayList<SceneDict> scenedict) {
		this.scenedict = scenedict;
	}

	
}
