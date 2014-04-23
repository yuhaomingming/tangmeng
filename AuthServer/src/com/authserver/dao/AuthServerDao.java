package com.authserver.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.authserver.comm.C3P0DBConnectionPool;
import com.authserver.manager.AuthserverManager;
import com.authserver.model.GameServer;
import com.authserver.model.SceneDict;

public class AuthServerDao implements Serializable {

	private static final long serialVersionUID = -687715959653048921L;
	/** 
	 */
	private static C3P0DBConnectionPool connPool = null;

	public int AuthUser(String username, String userpass) throws SQLException {
		int returnflag = 0;
		if (connPool == null) {
			connPool = C3P0DBConnectionPool.getInstance(); 
		}
		Connection conn = connPool.getConnection();
		String sql = "select username from usertable where username=? and userpass=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		if (conn != null) {
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setString(1, userpass);
				rs = ps.executeQuery();
				if (rs.next()) {
					returnflag = 1;
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		return returnflag;
	}
	public void GetServList(Map<String, GameServer> gsm) throws SQLException{
		if (connPool == null) {
			connPool = C3P0DBConnectionPool.getInstance();
		}
		Connection conn = connPool.getConnection();
		String sql = "select * from svrlist ORDER BY serverno";
		PreparedStatement ps = null;
		ResultSet rs = null;
		GameServer gs;
		if (conn != null) {
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					gs= new GameServer();
					gs.setCode(rs.getString("serverno")); 
					gs.setName(rs.getString("servername"));
					gs.setIpadd(rs.getString("ipadd"));
					gs.setPort(rs.getString("svrport"));
					gs.setIfrun(rs.getString("ifrun"));
					gs.setIfnew(rs.getString("ifnew"));
					gs.setIfdef(rs.getString("ifdefault"));
					gs.setGamever(rs.getString("startdate"));
					gs.setResVersion(rs.getString("resversion"));
					gs.setLastVersion(rs.getString("lastversion"));
					gs.setMaindisp(rs.getString("maindisp"));
					gs.setTopdisp(rs.getString("topdisp"));
					gs.setBottdisp(rs.getString("bottdisp"));
					gs.setStarttime(rs.getInt("starttime"));
					gs.setEndtime(rs.getInt("endtime"));
					gsm.put(gs.getCode(), gs);
				}
				rs.close();
				ps.close();
				sql="SELECT * FROM scenedict ORDER BY sceneno";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while(rs.next()){
					SceneDict sd = new SceneDict();
					sd.setSceneno(rs.getInt("sceneno"));
					sd.setScecode(rs.getString("scecode"));
					sd.setScenename(rs.getString("scenename"));
					sd.setMissions(rs.getInt("missions"));
					AuthserverManager.getInstance().getScenedict().add(sd);
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
	}

	public String GetUserRole(ArrayList<Map<String, String>> rolelist,String username) throws SQLException {
		if (connPool == null) {
			connPool = C3P0DBConnectionPool.getInstance();
		}
		int k = 0;
		String cursvrcode = "0";// 最近游戏
		Connection conn = connPool.getConnection();
		String sql = "select rolename,serverno from userroletbl where username=? ORDER BY currlogin DESC";
		PreparedStatement ps = null;
		ResultSet rs = null;
		if (conn != null) {
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, username);
				rs = ps.executeQuery();
				while (rs.next()) {
					Map<String, String> rmap = new HashMap<String, String>();
					if (k == 0)
						cursvrcode = rs.getString("serverno");
					k = k + 1;
					rmap.put("SC", rs.getString("serverno"));
					rmap.put("RN", rs.getString("rolename"));
					rolelist.add(rmap);
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
		}
		return cursvrcode;
	}
	
}
