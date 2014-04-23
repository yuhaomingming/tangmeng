/**
 * 
 */
package com.gameserver.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import com.gameserver.comm.PacketCommandType;
import com.gameserver.manager.GameManager;


public class BeatBossTimeTask extends TimerTask implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 843632282013939582L;
	private static final Logger logger = Logger.getLogger(BeatBossTimeTask.class.getName());
	private int beatinx=0;
	public BeatBossTimeTask(){
		logger.info("进程启动");
		beatinx=0;
	}
	@Override
	public void run(){
		try{
			ArrayList<Map<String,Object>> ul=GameManager.getInstance().getUseractbosslist();
			if(ul.size()>=(beatinx+1)*5){
				Map<String,Object> broadMap = new HashMap<String,Object>();
				ArrayList<Map<String,Object>> al= new ArrayList<Map<String,Object>>();
				for(int k=beatinx*5;k<(beatinx+1)*5;k++){
					al.add(ul.get(k));					
				}	
				beatinx++;
				broadMap.put("BA", al.toArray());
				ArrayList<Map<String,Object>> toplist = new ArrayList<Map<String,Object>>();
				for(int i=0;i<GameManager.getInstance().getBossTop5().size();i++){
					if(i==5) break;
					toplist.add(GameManager.getInstance().getBossTop5().get(i));
				}				
				broadMap.put("BH", toplist.toArray());
				broadMap.put("CMD", PacketCommandType.RETURNOTHERUSER);	
				GameManager.worldbosschannel.write(broadMap);
			}			
			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

	
}