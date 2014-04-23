/**
 * 数据库连接池管理�?
 */
package com.gameserver.comm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DBConnectionPool{

	private static final Logger logger = Logger.getLogger(C3P0DBConnectionPool.class.getName());
	private static ComboPooledDataSource cpds = null;
	private static C3P0DBConnectionPool instance = null;
	private static Properties props;

	//private static ComboPooledDataSource cpds1 = null;
    /**
     * 私有的构造方法，禁止外部创建本类的对象，要想获得本类的对象，通过getIstance方法�?
     * 使用了设计模式中的单子模式�?
     */
    private C3P0DBConnectionPool() {
        init();
    }

    /**
     * 连接池初始化方法，读取属性文件的内容 建立连接池中的初始连�?
     */
    private void init() {    	
    	cpds = new ComboPooledDataSource();
    	logger.info("0");
    	readConfig(cpds, 0);//读取配置文件
    }
    
    
 

    /**
     * 返回当前连接池的�?��对象
     */
    public static C3P0DBConnectionPool getInstance() {
        if (instance == null) {
            instance = new C3P0DBConnectionPool();
        }
        return instance;
    }

    /**
     * 返回连接池中的一个数据库连接
     */
    public Connection getConnection() { 
    	Connection conn = null;
        if(cpds == null){
        	init();
        }
       try {
    	   conn = cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
    }

   
    
 
    /**
     * 读取设置连接池的属�?文件
     */
    private void readConfig(ComboPooledDataSource cpds, int flag) {
        try {
            cpds.setDriverClass(props.getProperty("driverClassName"));
            if(flag == 1){
            	cpds.setJdbcUrl(props.getProperty("url1"));//jdbcURL
            }else{
            	cpds.setJdbcUrl(props.getProperty("url"));//jdbcURL
            }
            String k=props.getProperty("password");
            //k=Sha1Utils.MakeMD5(k+WeiyouxiClientConfig.secret).toUpperCase();
            cpds.setUser(props.getProperty("user"));//用户�?
            cpds.setPassword(k);//密码
            cpds.setMaxPoolSize(Integer.parseInt(props.getProperty("maxPoolSize")));//连接池中保留�?��连接�?
            cpds.setMinPoolSize(Integer.parseInt(props.getProperty("minPoolSize")));//连接池中保留�?��连接�?
            cpds.setMaxStatements(Integer.parseInt(props.getProperty("maxStatements")));//�?��statements�?
            cpds.setInitialPoolSize(Integer.parseInt(props.getProperty("initialPoolSize")));//初始化连接数
            cpds.setMaxIdleTime(Integer.parseInt(props.getProperty("maxIdleTime")));//�?��空闲时间，多少时间内未使用则丢弃�?为永不丢�?
            cpds.setAcquireIncrement(Integer.parseInt(props.getProperty("acquireIncrement")));//连接池中的连接�?尽时，一次创建多少个连接
            cpds.setUnreturnedConnectionTimeout(Integer.parseInt(props.getProperty("unreturnedConnectionTimeout")));
            cpds.setCheckoutTimeout(Integer.parseInt(props.getProperty("checkoutTimeout")));
            cpds.setAcquireRetryAttempts(Integer.parseInt(props.getProperty("acquireRetryAttempts")));//从数据库获取新连接失败后重复尝试的次�?
            cpds.setIdleConnectionTestPeriod(Integer.parseInt(props.getProperty("idleConnectionTestPeriod")));//�?���?��连接池中的空闲连�?
            cpds.setNumHelperThreads(10);
            cpds.setAutoCommitOnClose(true);
            cpds.setTestConnectionOnCheckout(false);
            cpds.setBreakAfterAcquireFailure(false);
            logger.info("DB Config Successed...");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("DB Config Error...");        
        }
    }

	public static Properties getProps() {
		return props;
	}

	public static void setProps(Properties _props) {
		props = _props;
	}
}
