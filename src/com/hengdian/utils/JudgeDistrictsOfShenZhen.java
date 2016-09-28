package com.hengdian.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.hengdian.entity.LoLatitude;

public class JudgeDistrictsOfShenZhen {
	//static List<LoLatitude> shenzhenshi = getDistrictsBorderList("shenzhenshi.txt");
	static List<LoLatitude> luohuqu = getDistrictsBorderList("luohuqu.txt");
	static List<LoLatitude> futianqu = getDistrictsBorderList("futianqu.txt");
	static List<LoLatitude> nanshanqu = getDistrictsBorderList("nanshanqu.txt");
	static List<LoLatitude> longgangqu = getDistrictsBorderList("longgangqu.txt");
	static List<LoLatitude> baoanqu = getDistrictsBorderList("baoanqu.txt");
	static List<LoLatitude> yantianqu = getDistrictsBorderList("yantianqu.txt");

	public static int isInPolygon(LoLatitude loLatitude) {

		int districtCode = 0; // 0深圳市外，1罗湖，2福田，3南山，4龙岗，5宝安，6盐田

		if (JudgePointInPolygon.isInPolygon(loLatitude, luohuqu)) {
			districtCode = 1;
		} else if (JudgePointInPolygon.isInPolygon(loLatitude, futianqu)) {
			districtCode = 2;
		} else if (JudgePointInPolygon.isInPolygon(loLatitude, nanshanqu)) {
			districtCode = 3;
		} else if (JudgePointInPolygon.isInPolygon(loLatitude, yantianqu)) {
			districtCode = 6;
		} else if (JudgePointInPolygon.isInPolygon(loLatitude, baoanqu)) {
			districtCode = 5;
		} else if (JudgePointInPolygon.isInPolygon(loLatitude, longgangqu)) {
			districtCode = 4;
		}
		return districtCode;
	}

	/**
	 * 从src目录下获取行政区域边界经纬坐标点集合
	 * 
	 * @param districtsName
	 *            行政区域名称(拼音)
	 * @return
	 */
	public static List<LoLatitude> getDistrictsBorderList(String districtsName) {

		String polyline = getSrcFilleString(districtsName);
		ArrayList<LoLatitude> list = new ArrayList<LoLatitude>();
		// 坐标字符串拆分
		String[] polylineSpotStr = polyline.split(";");
		String[] spotStr;
		for (int i = 0; i < polylineSpotStr.length; i++) {
			double lo;
			double la;
			// 经纬字符串拆分
			if (polylineSpotStr[i].indexOf("|") >= 0) {
				String[] spotArr = polylineSpotStr[i].split("\\|");
				for (int q = 0; q < spotArr.length; q++) {
					spotStr = spotArr[q].split(",");
					lo = Double.parseDouble(spotStr[0]);
					la = Double.parseDouble(spotStr[1]);
					list.add(new LoLatitude(la, lo));
				}
			} else {
				spotStr = polylineSpotStr[i].split(",");
				lo = Double.parseDouble(spotStr[0]);
				la = Double.parseDouble(spotStr[1]);
				list.add(new LoLatitude(la, lo));
			}
		}
		return list;
	}

	/**
	 * 获取src目录下的文本内容
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getSrcFilleString(String fileName) {
		InputStream is = JudgeDistrictsOfShenZhen.class.getClassLoader().getResourceAsStream(fileName);
		StringBuffer sbf = new StringBuffer();
		try {
			InputStreamReader isReader = new InputStreamReader(is, "utf-8");
			char[] temp = new char[2048];
			int tmpLength = 0;
			while ((tmpLength = isReader.read(temp)) != -1) {
				for (int i = 0; i < tmpLength; i++) {
					sbf.append(temp[i]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sbf.toString();
	}
}