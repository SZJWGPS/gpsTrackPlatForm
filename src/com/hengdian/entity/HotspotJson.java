package com.hengdian.entity;

import net.sf.json.JSONObject;

public class HotspotJson {

	// HotsoptJson根对象
	private JSONObject hotspotJson = new JSONObject();

	/**********************************************/

	/**
	 * 获取HotspotJson对象
	 * 
	 * @param tableTrStr	table行字符串
	 * @param daoCode		数据库操作结果代码
	 * @param msg
	 * @return 返回JSONObject类型的HotspotJson对象
	 */
	public JSONObject makeHotspotJson(String tableTrStr, int daoCode, String msg) {
		hotspotJson.put("table_row", tableTrStr);
		hotspotJson.put("dao_code", daoCode);
		hotspotJson.put("msg", msg);

		return hotspotJson;
	}
}
