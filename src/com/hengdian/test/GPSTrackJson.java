package com.hengdian.test;

import net.sf.json.util.JSONStringer;

public class GPSTrackJson {
	
        private String sanDianTu ="";
        private String heatMap ="";
        private String onOff ="";
        private String point_normal_Name ="";
        private String point_normal_Value ="";
        private String point_hotpoint_Name ="";
        private String point_hotpoint_Value ="";
        private String trendchart_time_Data ="";
        private String trendchart_time_xAxi ="";
        private String trendchart_day_Data ="";
        private String trendchart_day_xAxi ="";
		
		/**
		 * 获取GPS前台数据Json字符串
		 * @return 返回GPS前台数据Json字符串
		 */
		public String getGPSTrackJson() {
			JSONStringer stringer = new JSONStringer();				
	        stringer.object()
				.key("sandiantu").array().value(sanDianTu).endArray()
				.key("heatmap").array().value(heatMap).endArray()
				.key("onoff").array().value(onOff).endArray()
				.key("point").object()
					.key("normal").object()
							.key("name").array().value(point_normal_Name).endArray()
							.key("value").array().value(point_normal_Value).endArray().endObject()
					.key("hotpoint").object()
							.key("name").array().value(point_hotpoint_Name).endArray()
							.key("value").array().value(point_hotpoint_Value ).endArray().endObject()
					.endObject()
				.key("trendchart").object()
					.key("time").object()
						.key("data").array().value(trendchart_time_Data ).endArray()
						.key("xAxi").array().value(trendchart_time_xAxi).endArray()
						.endObject()
					.key("date").object()
						.key("day").array().value(trendchart_day_Data).endArray()
						.key("xAxi").array().value(trendchart_day_xAxi ).endArray()
						.endObject()
					.endObject()
				.endObject();
			return stringer.toString();
		}

		public String getSanDianTu() {
			return sanDianTu;
		}

		public void setSanDianTu(String sanDianTu) {
			this.sanDianTu = sanDianTu;
		}

		public String getHeatMap() {
			return heatMap;
		}

		public void setHeatMap(String heatMap) {
			this.heatMap = heatMap;
		}

		public String getOnOff() {
			return onOff;
		}

		public void setOnOff(String onOff) {
			this.onOff = onOff;
		}

		public String getPoint_normal_Name() {
			return point_normal_Name;
		}

		public void setPoint_normal_Name(String point_normal_Name) {
			this.point_normal_Name = point_normal_Name;
		}

		public String getPoint_normal_Value() {
			return point_normal_Value;
		}

		public void setPoint_normal_Value(String point_normal_Value) {
			this.point_normal_Value = point_normal_Value;
		}

		public String getPoint_hotpoint_Name() {
			return point_hotpoint_Name;
		}

		public void setPoint_hotpoint_Name(String point_hotpoint_Name) {
			this.point_hotpoint_Name = point_hotpoint_Name;
		}

		public String getPoint_hotpoint_Value() {
			return point_hotpoint_Value;
		}

		public void setPoint_hotpoint_Value(String point_hotpoint_Value) {
			this.point_hotpoint_Value = point_hotpoint_Value;
		}

		public String getTrendchart_time_Data() {
			return trendchart_time_Data;
		}

		public void setTrendchart_time_Data(String trendchart_time_Data) {
			this.trendchart_time_Data = trendchart_time_Data;
		}

		public String getTrendchart_time_xAxi() {
			return trendchart_time_xAxi;
		}

		public void setTrendchart_time_xAxi(String trendchart_time_xAxi) {
			this.trendchart_time_xAxi = trendchart_time_xAxi;
		}

		public String getTrendchart_day_Data() {
			return trendchart_day_Data;
		}

		public void setTrendchart_day_Data(String trendchart_day_Data) {
			this.trendchart_day_Data = trendchart_day_Data;
		}

		public String getTrendchart_day_xAxi() {
			return trendchart_day_xAxi;
		}

		public void setTrendchart_day_xAxi(String trendchart_day_xAxi) {
			this.trendchart_day_xAxi = trendchart_day_xAxi;
		}	
}
