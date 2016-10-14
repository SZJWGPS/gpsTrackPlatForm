package com.hengdian.entity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GpsJson {
	// gpsJson根对象
	private JSONObject gpsJson = new JSONObject();
	// 散点图坐标json数组
	private JSONArray sanDianTu = new JSONArray();
	// 热力图json数组
	private JSONArray heatMap = new JSONArray();
	// 热力图最大值
	private int heatMapMax = 0;
	// 上下客点数组
	private JSONArray onOffValue = new JSONArray();

	/**********************************************/
	// 重点监控区域和热点区域对象
	private JSONObject point = new JSONObject();
	// 重点监控区域数据对象
	private JSONObject point_normal = new JSONObject();
	private JSONArray point_normal_name = new JSONArray();
	private JSONArray point_normal_value = new JSONArray();
	private JSONArray point_normal_spots = new JSONArray();

	// 热点区域数据对象
	private JSONObject point_hotpoint = new JSONObject();
	private JSONArray point_hotpoint_name = new JSONArray();
	private JSONArray point_hotpoint_value = new JSONArray();

	/**********************************************/
	// 走势图数据对象
	private JSONObject trendChart = new JSONObject();
	// 走势图按时段统计数据对象
	private JSONObject trendChart_byHourFrame = new JSONObject();
	// 走势图按时段统计数据对象纵轴数据
	private JSONArray trendChart_byHourFrame_data = new JSONArray();
	// 走势图按时段统计数据对象横轴坐标
	private JSONArray trendChart_byHourFrame_xAxi = new JSONArray();
	// ---------------------------------------------
	// 走势图按天统计数据对象
	private JSONObject trendChart_byday = new JSONObject();
	private JSONArray trendChart_byday_data = new JSONArray();
	private JSONArray trendChart_byday_xAxi = new JSONArray();

	// ---------------------------------------------
	// 走势图按星期统计数据对象
	private JSONObject trendChart_byweek = new JSONObject();
	private JSONArray trendChart_byweek_data = new JSONArray();
	private JSONArray trendChart_byweek_xAxi = new JSONArray();

	/**********************************************/

	/**
	 *
	 * 添加散点图的散点
	 * 
	 * @param jingdu_lo
	 *            散点的经度
	 * @param weidu_la
	 *            散点的维度
	 * 
	 */
	public void addSanDian(double jingdu_lo, double weidu_la) {
		JSONArray jingWeiArr = new JSONArray();
		jingWeiArr.add(0, jingdu_lo);
		jingWeiArr.add(1, weidu_la);

		JSONObject sanDian = new JSONObject();
		sanDian.put("lnglat", jingWeiArr);
		sanDianTu.add(sanDian);
	}

	/**
	 * 添加添加热度图的点和值
	 * 
	 * @param jingdu_lo
	 *            经度
	 * @param weidu_la
	 *            纬度
	 * @param value
	 *            热度值(权重)
	 */
	public void addHeatMapPoint(double jingdu_lo, double weidu_la, int value) {
		JSONObject HeatMapPointObj = new JSONObject();
		HeatMapPointObj.put("lng", jingdu_lo);
		HeatMapPointObj.put("lat", weidu_la);
		HeatMapPointObj.put("count", value);

		heatMap.add(HeatMapPointObj);
	}

	/**
	 * 设置热力图最大值
	 * 
	 * @param heatMapMaxValue
	 *            热力图最大值
	 */
	public void setHeatMapMax(int heatMapMaxValue) {
		heatMapMax = heatMapMaxValue;
	}

	/**
	 * 设置上下客统计值
	 * 
	 * @param on
	 *            上客总数
	 * @param off
	 *            下客总数
	 */
	public void setOnOffValue(long on, long off) {
		onOffValue.add(0, on);
		onOffValue.add(1, off);
	}

	/**
	 * 
	 * 添加重点监控区域名称,和对应上客次数
	 * 
	 * @param normalArea_name
	 *            重点监控区域名称
	 * @param jingdu_lo
	 *            区域中心经度
	 * @param weidu_la
	 *            区域中心纬度
	 * @param normalArea_value
	 *            重点监控区域上客次数
	 * 
	 */
	public void addPoint_normal(String normalArea_name, double jingdu_lo,
			double weidu_la, int normalArea_value) {
		point_normal_name.add(normalArea_name);
		point_normal_value.add(normalArea_value);

		JSONArray jingWeiArr = new JSONArray();
		jingWeiArr.add(0, jingdu_lo);
		jingWeiArr.add(1, weidu_la);

		JSONObject spot = new JSONObject();
		spot.put("lnglat", jingWeiArr);
		spot.put("name", normalArea_name);

		point_normal_spots.add(spot);
	}

	/**
	 * 添加热点区域名称,和对应上客次数
	 * 
	 * @param normalArea_name
	 *            热点区域名称
	 * @param hotpoint_value
	 *            热点区域上客次数
	 */
	public void addPoint_hotpoint(String hotpoint_name, int hotpoint_value) {
		point_hotpoint_name.add(hotpoint_name);
		point_hotpoint_value.add(hotpoint_value);
	}

	/**
	 * 添加走势图按时段统计纵轴数据和横轴名称
	 * 
	 * @param byHourFrame_data
	 *            上客次数
	 * @param xAxi_huor
	 *            对应时段
	 */
	public void addPoint_trendChart_byHourFrame(int byHourFrame_data,
			String xAxi_huor) {
		trendChart_byHourFrame_data.add(byHourFrame_data);
		trendChart_byHourFrame_xAxi.add(xAxi_huor);
	}

	/**
	 * 添加走势图按天统计纵轴数据和横坐标名称
	 * 
	 * @param byday_data
	 * @param xAxi_day
	 */
	public void addPoint_trendChart_byday(int byday_data, String xAxi_day) {
		trendChart_byday_data.add(byday_data);
		trendChart_byday_xAxi.add(xAxi_day);
	}

	/**
	 * 添加走势图按星期统计纵轴数据和横坐标名称
	 * 
	 * @param byweek_data
	 * @param xAxi_week
	 */
	public void addPoint_trendChart_byweek(int byweek_data, String xAxi_week) {
		trendChart_byweek_data.add(byweek_data);
		trendChart_byweek_xAxi.add(xAxi_week);
	}

	/**
	 * 获取GpsJson对象
	 * 
	 * @return 返回JSONObject类型的GpsJson对象
	 */
	public JSONObject getGpsJson() {
		gpsJson.put("sandiantu", sanDianTu);
		gpsJson.put("heatmap", heatMap);
		gpsJson.put("heatmapmax", heatMapMax);
		gpsJson.put("onoffValue", onOffValue);
		// 重点区域数据
		point_normal.put("name", point_normal_name);
		point_normal.put("value", point_normal_value);
		point_normal.put("spot", point_normal_spots);
		point.put("normal", point_normal);
		// 热点区域数据
		point_hotpoint.put("name", point_hotpoint_name);
		point_hotpoint.put("value", point_hotpoint_value);
		point.put("hotpoint", point_hotpoint);

		gpsJson.put("point", point);

		// 按时段统计走势图数据
		trendChart_byHourFrame.put("data", trendChart_byHourFrame_data);
		trendChart_byHourFrame.put("xAxi", trendChart_byHourFrame_xAxi);
		trendChart.put("byhourframe", trendChart_byHourFrame);
		// 按天统计走势图数据
		trendChart_byday.put("data", trendChart_byday_data);
		trendChart_byday.put("xAxi", trendChart_byday_xAxi);
		trendChart.put("byday", trendChart_byday);
		// 按星期统计走势图数据
		trendChart_byweek.put("data", trendChart_byweek_data);
		trendChart_byweek.put("xAxi", trendChart_byweek_xAxi);
		trendChart.put("byweek", trendChart_byweek);

		gpsJson.put("trendchart", trendChart);
		return gpsJson;
	}

	public static void main(String[] args) {

		GpsJson gpsJsonUtil = new GpsJson();
		double jingdu_lo = 122.123456;
		double weidu_la = 22.123456;
		double jingdu_lo1 = 222.123456;
		double weidu_la1 = 32.123456;
		gpsJsonUtil.addSanDian(jingdu_lo, weidu_la);
		gpsJsonUtil.addSanDian(jingdu_lo1, weidu_la1);
		gpsJsonUtil.addHeatMapPoint(jingdu_lo, weidu_la, 1);
		gpsJsonUtil.addHeatMapPoint(jingdu_lo1, weidu_la1, 2);

		gpsJsonUtil.setHeatMapMax(200);
		gpsJsonUtil.setOnOffValue(400, 300);

		gpsJsonUtil.addPoint_normal("深圳机场", 114.22, 22.35, 100);
		gpsJsonUtil.addPoint_normal("华强北", 123.223, 23.88, 200);

		gpsJsonUtil.addPoint_hotpoint("竹子林", 80);
		gpsJsonUtil.addPoint_hotpoint("罗湖站", 160);

		gpsJsonUtil.addPoint_trendChart_byHourFrame(160, "0点");
		gpsJsonUtil.addPoint_trendChart_byHourFrame(200, "2点");

		gpsJsonUtil.addPoint_trendChart_byday(500, "07-01");
		gpsJsonUtil.addPoint_trendChart_byday(200, "07-02");

		System.out.println("\n\n\n\n");
		System.out.println(gpsJsonUtil.getGpsJson());

	}

}
