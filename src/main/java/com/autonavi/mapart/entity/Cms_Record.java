package com.autonavi.mapart.entity;

public class Cms_Record {
	private int start ;
	private int num ;
	private int last_fa_id;
	
	public int getLast_fa_id() {
		return last_fa_id;
	}

	public void setLast_fa_id(int last_fa_id) {
		this.last_fa_id = last_fa_id;
	}

	public Cms_Record(int start,int num,int last_fa_id){
		this.start = start;
		this.num = num;
		this.last_fa_id = last_fa_id;
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
