package com.autonavi.mapart.entity;

public class Anf_hicaicaibian {
	private int  x_id;      //x-平台的xid(poiid)
	private String  mesh;   //图幅号
	private int  poi_id;
	private int gid;
	private int fa_id;
	
	public Anf_hicaicaibian(){
		
	}
	public Anf_hicaicaibian(int x_id,String mesh, int poi_id,int gid,int fa_id){
		this.gid=gid;
		this.x_id = x_id;
		this.fa_id = fa_id;
		this.mesh = mesh;
		this.poi_id = poi_id;
	}
	
	public int getFa_id() {
		return fa_id;
	}
	public void setFa_id(int fa_id) {
		this.fa_id = fa_id;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getX_id() {
		return x_id;
	}

	public void setX_id(int X_id) {
		x_id = X_id;
	}

	public String getMesh() {
		return mesh;
	}

	public void setMesh(String Mesh) {
		mesh = Mesh;
	}

	public int getPoi_id() {
		return poi_id;
	}

	public void setPoi_id(int Poi_id) {
		poi_id = Poi_id;
	}
}
