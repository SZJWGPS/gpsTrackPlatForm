package com.hengdian.entity;
/**
 * 经纬度坐标实体
 * 
 * @author admin
 *
 */
public class LoLatitude {
	public double longitude; //经度
	public double latitude; //纬度
	public LoLatitude(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
}
