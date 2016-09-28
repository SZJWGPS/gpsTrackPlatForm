package com.hengdian.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.hengdian.entity.LoLatitude;

public class BorderUtil {
	public static void main(String[] args) {
		
		getDistrictsBorder("南山区");
		
		// String str = getDistrictsBorder2javaArrStr("深圳");
		// System.out.println(str);
	}

	/**
	 * 从高德地图获取行政区域边界经纬坐标点集合
	 * 
	 * @param districtsName
	 *            行政区域名称
	 * @return
	 */
	public static List<LoLatitude> getDistrictsBorderList(String districtsName) {

		String polyline = getDistrictsBorder(districtsName);
		//System.out.println(polyline);
		ArrayList<LoLatitude> list = new ArrayList<LoLatitude>();
		// 坐标字符串拆分
		String[] polylineSpotStr = polyline.split(";");
		String[] spotStr;
		for (int i = 0; i < polylineSpotStr.length; i++) {
			double lo;
			double la;
			// 经纬字符串拆分
			if (polylineSpotStr[i].indexOf("|") >= 0) {				
				spotStr = polylineSpotStr[i].split("|");
				lo = Double.parseDouble(spotStr[1]); // 经度
				la = Double.parseDouble(spotStr[0]); // 纬度
			} else {
				spotStr = polylineSpotStr[i].split(",");
				lo = Double.parseDouble(spotStr[0]);
				la = Double.parseDouble(spotStr[1]);
			}
			list.add(new LoLatitude(la, lo));
		}
		return list;
	}

	/**
	 * java数组格式边界
	 * 
	 * @param districtsName
	 * @return
	 */
	public static String getDistrictsBorderJavaArrStr(String districtsName) {

		String polyline = getDistrictsBorder(districtsName);
		
		StringBuffer sbf = new StringBuffer();
		String[] polylineSpotStr = polyline.split(";");
		String[] spotStr;
		for (int i = 0; i < polylineSpotStr.length; i++) {
			double lo;
			double la;
			if (polylineSpotStr[i].indexOf("|") >= 0) {
				spotStr = polylineSpotStr[i].split("|");
				lo = Double.parseDouble(spotStr[1]);
				la = Double.parseDouble(spotStr[0]);
			} else {
				spotStr = polylineSpotStr[i].split(",");
				lo = Double.parseDouble(spotStr[0]);
				la = Double.parseDouble(spotStr[1]);
			}
			sbf.append("{" + lo + "," + la + "},");
		}
		int trimIndex = sbf.lastIndexOf("},");
		if (trimIndex >= 0) {
			sbf.deleteCharAt(trimIndex + 1);
		}
		return "{" + sbf.toString() + "}";
	}

	/**
	 * js数组格式边界
	 * 
	 * @param districtsName
	 * @return
	 */
	public static String getDistrictsBorderJsArrStr(String districtsName) {

		String polyline = getDistrictsBorder(districtsName);
		
		StringBuffer sbf = new StringBuffer();
		String[] polylineSpotStr = polyline.split(";");
		String[] spotStr;
		for (int i = 0; i < polylineSpotStr.length; i++) {
			double lo;
			double la;
			if (polylineSpotStr[i].indexOf("|") >= 0) {
				spotStr = polylineSpotStr[i].split("|");
				lo = Double.parseDouble(spotStr[1]);
				la = Double.parseDouble(spotStr[0]);
			} else {
				spotStr = polylineSpotStr[i].split(",");
				lo = Double.parseDouble(spotStr[0]);
				la = Double.parseDouble(spotStr[1]);
			}
			sbf.append("[" + lo + "," + la + "],");
		}
		int trimIndex = sbf.lastIndexOf("],");
		if (trimIndex >= 0) {
			sbf.deleteCharAt(trimIndex + 1);
		}
		return "[" + sbf.toString() + "]";
	}

	private static String getDistrictsBorder(String districtsName) {
		// 发送 GET 请求行政区域边界数据
		String s = HttpTookit.sendGet(
				"http://restapi.amap.com/v3/config/district",
				"key=1a870acc1d7db122ae4ab484acac1847&keywords="
						+ districtsName
						+ "&level=city&subdistrict=0&extensions=all");
		System.out.println(s);
		// 将获取的Json字符串转换为Json		
		JSONObject jsonObject = new JSONObject(s);
		String polyline = jsonObject.getJSONArray("districts").getJSONObject(0)
				.getString("polyline");
		System.out.println(polyline);
		return polyline;
	}
}
