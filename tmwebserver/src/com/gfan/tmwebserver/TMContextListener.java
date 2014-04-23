package com.gfan.tmwebserver;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.DataSources;

public class TMContextListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(TMContextListener.class
			.getName());
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		//TODO 这里需要修改一下，得到dataSource
        DataSource dataSource = C3P0DBConnectionPool.getInstance().getDataSource();
        try
        {
            //调用c3p0的关闭数据库连接的方法
            DataSources.destroy(dataSource);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.info(e.getMessage());

        }
        //等待连接池关闭线程退出，避免Tomcat报线程未关闭导致memory leak的错误
        try {
            
        	Thread.sleep(3000);
            
        } catch (InterruptedException e) {
        	logger.info(e.getMessage());
            e.printStackTrace();

        }
        logger.info("##########HYContextListener##########contextDestroyed##########" );
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		C3P0DBConnectionPool.getInstance();
		logger.info("##########HYContextListener##########contextInitialized##########");
		
	}

}
