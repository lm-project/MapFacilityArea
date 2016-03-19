package com.autonavi.mapart.entity;

import net.sf.json.JSONObject;

/**
 * <p>
 * desc: 设施区域与poi对应关系实体类
 * <p>
 * @author huandi.yang
 *
 */
public class FapoiRelation {
	
	private int id;                //序号
	private String fa_category;    //设施区域大类	
	private String fa_type;        //设施区域中类
	private String poi_typecode;   //poi类型代码
	private String poi_type;       //poi类型
	
	public static FapoiRelation createFromJson(JSONObject json) {
		FapoiRelation relation = new FapoiRelation();
		relation.setId(json.getInt("id"));
		relation.setFa_category(json.getString("fa_category"));
		relation.setFa_type(json.getString("fa_type"));
		relation.setPoi_type(json.getString("poi_type"));
		relation.setPoi_typecode(json.getString("poi_typecode"));
		return relation;
	}
	
	public FapoiRelation(){
		
	}
	
	public FapoiRelation(String fa_category,String fa_type,String poi_typecode,String poi_type){
		this.fa_category = fa_category;
		this.fa_type = fa_type;
		this.poi_type = poi_type;
		this.poi_typecode = poi_typecode;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFa_category() {
		return fa_category;
	}
	public void setFa_category(String fa_category) {
		this.fa_category = fa_category;
	}
	public String getFa_type() {
		return fa_type;
	}
	public void setFa_type(String fa_type) {
		this.fa_type = fa_type;
	}
	public String getPoi_typecode() {
		return poi_typecode;
	}
	public void setPoi_typecode(String poi_typecode) {
		this.poi_typecode = poi_typecode;
	}
	public String getPoi_type() {
		return poi_type;
	}
	public void setPoi_type(String poi_type) {
		this.poi_type = poi_type;
	}

}
