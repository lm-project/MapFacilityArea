package com.autonavi.mapart.entity;

public class CmsData {
	private int  x_id;      //x-平台的xid(poiid)
	private String lng_lat;
	private String center;
	private int condition;
	private int fa_id;
	public CmsData(){}
	public CmsData(int x_id,String lng_lat,String center,int condition,int fa_id){
		this.x_id = x_id;
		this.lng_lat = lng_lat;
		this.center = center;
		this.condition = condition;
		this.fa_id = fa_id;
	}
	public int getX_ID() {
		return x_id;
	}
	public void setX_ID(int x_ID) {
		x_id = x_ID;
	}
	public String getLng_lat() {
		return lng_lat;
	}
	public void setLng_lat(String lng_lat) {
		this.lng_lat = lng_lat;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	public int getFa_id() {
		return fa_id;
	}
	public void setFa_id(int fa_id) {
		this.fa_id = fa_id;
	}
	
}
