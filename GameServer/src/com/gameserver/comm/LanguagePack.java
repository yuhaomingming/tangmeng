package com.gameserver.comm;
import java.io.FileInputStream;
import java.util.Properties;


public class LanguagePack {
	private static LanguagePack instance;
	private static Properties props;
	
	public LanguagePack(){
		try{
		FileInputStream is = new FileInputStream(props.getProperty("LanguagePath"));
		props = new Properties();
		props.load(is);
		is.close();
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}
	public static LanguagePack getInstance(){
		if(instance==null){
			instance=new LanguagePack();
		}
		return instance;
	}
	public String getMessage(String key){
		return props.getProperty(key);
	}
	public static Properties getProps() {
		return props;
	}
	public static void setProps(Properties _props) {
		props = _props;
	}
}
