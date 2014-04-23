package com.gfan.tmwebserver;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DBConnectionPool{

	private static final Logger logger = Logger.getLogger(C3P0DBConnectionPool.class.getName());
	private static ComboPooledDataSource cpds = null;
	private static C3P0DBConnectionPool instance = null;
	private static Properties props;

	
    private C3P0DBConnectionPool() {
        init();
    }

   public DataSource getDataSource(){
	   return cpds;
   }
   public String getconfig(String key){
	   if(props!=null){
		   return props.getProperty(key);
	   }else{
		   return "";
	   }

   }
    private void init() {    	
        InputStream in =C3P0DBConnectionPool.class.getClassLoader().getResourceAsStream("ServerConfig.properties");
        props = new Properties();
    	try {
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("DB Config Read Error..."); 
		}
    	cpds = new ComboPooledDataSource();
    	readConfig(cpds, 0);
    }
    
    
 

  
    public static C3P0DBConnectionPool getInstance() {
        if (instance == null) {
            instance = new C3P0DBConnectionPool();
        }
        return instance;
    }

   
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
            cpds.setUser(props.getProperty("user"));
            cpds.setPassword(k);
            cpds.setMaxPoolSize(Integer.parseInt(props.getProperty("maxPoolSize")));
            cpds.setMinPoolSize(Integer.parseInt(props.getProperty("minPoolSize")));
            cpds.setMaxStatements(Integer.parseInt(props.getProperty("maxStatements")));
            cpds.setInitialPoolSize(Integer.parseInt(props.getProperty("initialPoolSize")));
            cpds.setMaxIdleTime(Integer.parseInt(props.getProperty("maxIdleTime")));
            cpds.setAcquireIncrement(Integer.parseInt(props.getProperty("acquireIncrement")));
            cpds.setUnreturnedConnectionTimeout(Integer.parseInt(props.getProperty("unreturnedConnectionTimeout")));
            cpds.setCheckoutTimeout(Integer.parseInt(props.getProperty("checkoutTimeout")));
            cpds.setAcquireRetryAttempts(Integer.parseInt(props.getProperty("acquireRetryAttempts")));
            cpds.setIdleConnectionTestPeriod(Integer.parseInt(props.getProperty("idleConnectionTestPeriod")));
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

	
}
