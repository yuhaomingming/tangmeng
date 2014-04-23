package com.gfan.tmwebserver;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class MoneyDao {

	private static final Logger logger = Logger.getLogger(MoneyDao.class
			.getName());
	private static C3P0DBConnectionPool pool = C3P0DBConnectionPool
			.getInstance();

	public static String dateToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	// uid == username 但是不唯一
	public static String reqcharge(String orderid, String username,
			String rolename, String serverno, int exchgold) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = pool.getConnection();
		try {
			String sql = "SELECT gold FROM userroletbl WHERE username = ? AND rolename = ? AND serverno = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, rolename);
			ps.setString(3, serverno);
			rs = ps.executeQuery();
			int lastgold = 0;
			if (rs.next()) {
				lastgold = rs.getInt("gold");
			} else {
				throw new SQLException("have no such the user");
			}
			ps.close();
			rs.close();
			ps = null;
			rs = null;
			sql = "SELECT orderid FROM usercharge WHERE orderid = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, orderid);
			rs = ps.executeQuery();
			int order = 0;
			if (rs.next()) {
				order = 1;
			}
			ps.close();
			rs.close();
			ps = null;
			rs = null;
			if (order == 0) {
				sql = "INSERT INTO usercharge VALUES(?,?,?,?,?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, orderid);
				ps.setString(2, username);
				ps.setString(3, rolename);
				ps.setString(4, serverno);
				ps.setString(5, dateToString(new Date()));
				ps.setInt(6, 0);
				ps.setInt(7, exchgold);
				ps.setInt(8, -1);
				ps.setInt(9, lastgold);
				ps.setString(10, "");
				ps.setInt(11, 0);
				System.out.println("orderid:" + orderid + ",username:"
						+ username + ",rolename:" + rolename + ",serverno:"
						+ serverno + ",exchgold:" + exchgold + ",lastgold:"
						+ lastgold);
				ps.executeUpdate();
				ps.close();
				conn.close();
				ps = null;
				conn = null;
			}
		} catch (SQLException e) {
			logger.info(e.getMessage());
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		}
		return orderid;
	}

	public static int callchargesuccess(String orderid, String username,
			String rolename, String serverno, int gold) throws SQLException {
		int rseult = 0;
		Connection conn = pool.getConnection();
		CallableStatement cs = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		if (conn != null) {
			try {

				String sql = "SELECT orderid FROM usercharge WHERE orderid = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, orderid);
				rs = ps.executeQuery();
				if (rs.next()) {
					rseult = 1;
				}
				rs.close();
				rs = null;
				ps.close();
				ps = null;
				if (rseult == 0) {
					sql = "SELECT gold FROM userroletbl WHERE username = ? AND rolename = ? AND serverno = ?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, username);
					ps.setString(2, rolename);
					ps.setString(3, serverno);
					rs = ps.executeQuery();
					int ishad = 0;
					if (rs.next()) {
						ishad = 1;
					} else {
						throw new SQLException("have no such the user");
					}

					rs.close();
					rs = null;
					ps.close();
					ps = null;

					if (ishad == 1) {
						cs = conn.prepareCall("{call CHARGESUCC(?,?,?,?,?)}");
						cs.setString(1, orderid);
						cs.setInt(2, gold);
						cs.setString(3, username);
						cs.setString(4, serverno);
						cs.registerOutParameter(5, Types.INTEGER);
						cs.execute();
						rseult = cs.getInt(5);
						cs.close();
						cs = null;
						conn.close();
						conn = null;
					}

				}

			} catch (SQLException e) {
				logger.info(e.getMessage());
				throw e;
			} finally {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (cs != null) {
					cs.close();
					cs = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
		}
		return rseult;
	}
}
