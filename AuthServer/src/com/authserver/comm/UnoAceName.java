/**
 * UNO每张牌所对应的中文名
 */
package com.authserver.comm;

import java.io.Serializable;

/**
 * @author hejl
 * 
 * @create 2011-4-19
 */
public class UnoAceName implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1234584373960887947L;

	// 手牌原始数组
	public static final String[] aceArray = { "1111", "1112", "1113", "1114",
			"1115", "1116", "1117", "1118", "1119", "1110", "1121", "1122",
			"1123", "1124", "1125", "1126", "1127", "1128", "1129", "1211",
			"1212", "1213", "1214", "1215", "1216", "1217", "1218", "1219",
			"1210", "1221", "1222", "1223", "1224", "1225", "1226", "1227",
			"1228", "1229", "1311", "1312", "1313", "1314", "1315", "1316",
			"1317", "1318", "1319", "1310", "1321", "1322", "1323", "1324",
			"1325", "1326", "1327", "1328", "1329", "1411", "1412", "1413",
			"1414", "1415", "1416", "1417", "1418", "1419", "1410", "1421",
			"1422", "1423", "1424", "1425", "1426", "1427", "1428", "1429",
			"2111", "2112", "2121", "2122", "2131", "2132", "2211", "2212",
			"2221", "2222", "2231", "2232", "2311", "2312", "2321", "2322",
			"2331", "2332", "2411", "2412", "2421", "2422", "2431", "2432",
			"3011", "3012", "3013", "3014", "3021", "3022", "3023", "3024" };

	// 手牌说明数组
	public static final String[] aceNoticeArray = { "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?", "�?", "�?", "跳过(�?", "跳过(�?", "翻转(�?",
			"翻转(�?", "+2(�?", "+2(�?", "跳过(�?", "跳过(�?", "翻转(�?", "翻转(�?",
			"+2(�?", "+2(�?", "跳过(�?", "跳过(�?", "翻转(�?", "翻转(�?", "+2(�?",
			"+2(�?", "跳过(�?", "跳过(�?", "翻转(�?", "翻转(�?", "+2(�?", "+2(�?",
			"变色(万能)", "变色(万能)", "变色(万能)", "变色(万能)", "+4(万能)", "+4(万能)",
			"+4(万能)", "+4(万能)" };

	// 手牌说明数组
	/*
	 * public static final String[] aceNoticeArray={"0","1","2","3","4","5","6"
	 * ,"7","8","9","0","1","2","3","4","5","6","7","8","9"
	 * ,"0","1","2","3","4","5","6","7","8","9"
	 * ,"0","1","2","3","4","5","6","7","8","9"
	 * ,"0","1","2","3","4","5","6","7","8","9"
	 * ,"0","1","2","3","4","5","6","7","8","9"
	 * ,"0","1","2","3","4","5","6","7","8","9"
	 * ,"0","1","2","3","4","5","6","7","8","9"
	 * ,"0","1","2","3","4","5","6","7","8","9"
	 * ,LanguagePack.getInstance().getMessage
	 * ("ace.1"),LanguagePack.getInstance()
	 * .getMessage("ace.1"),LanguagePack.getInstance
	 * ().getMessage("ace.1"),LanguagePack
	 * .getInstance().getMessage("ace.1"),LanguagePack
	 * .getInstance().getMessage("ace.1"
	 * ),LanguagePack.getInstance().getMessage("ace.1")
	 * ,"+1","+1","+1","+1","+1","+1" ,"+2","+2","+2","+2","+2","+2"
	 * ,LanguagePack
	 * .getInstance().getMessage("ace.2"),LanguagePack.getInstance()
	 * .getMessage("ace.2"
	 * ),LanguagePack.getInstance().getMessage("ace.2"),LanguagePack
	 * .getInstance(
	 * ).getMessage("ace.2"),LanguagePack.getInstance().getMessage("ace.2"
	 * ),LanguagePack.getInstance().getMessage("ace.2")
	 * ,LanguagePack.getInstance
	 * ().getMessage("ace.3"),LanguagePack.getInstance()
	 * .getMessage("ace.3"),LanguagePack
	 * .getInstance().getMessage("ace.3"),LanguagePack
	 * .getInstance().getMessage("ace.3")
	 * ,LanguagePack.getInstance().getMessage(
	 * "ace.4"),LanguagePack.getInstance()
	 * .getMessage("ace.4"),LanguagePack.getInstance
	 * ().getMessage("ace.4"),LanguagePack.getInstance().getMessage("ace.4")
	 * ,LanguagePack
	 * .getInstance().getMessage("ace.5"),LanguagePack.getInstance()
	 * .getMessage("ace.5")};
	 */
}
