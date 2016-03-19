package com.autonavi.mapart.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.autonavi.mapart.util.GetGeometry;
import com.autonavi.mapart.util.Utils;

import net.sf.json.JSONObject;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * desc: 设施区域实体类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-25 9:28
 * 
 */
public class Facilityachieve {

	private static final String POLYGON = "POLYGON";
	private int gid;			// 表id
	private long fa_id;			// 用户编号
	private String name_chn;	// 设施区域中文名称
	private String mesh;		// 设施区域所在图幅号
	private long poi_id;		// 设施区域主POIid
	private int fa_type;		// 设施区域类型
	private int disp_class;		// 显示等级
	private int fa_flag;		// 设施区域制作方式
	private int area_flag;		// 区域归属
	private String geom;		// 面类型
	private int taskitem_id;	// 任务项id，关联task_item表id
	private String reverse_geom;// 坐标反变形后设施区域
	private String poi_guid;    // 
	private String precision;   // 精度
	private int sources;        // 数据来源
	private String updatetime;  // 更新时间
	private String proname;     // 来源项目名称
	private int fatype_id;      // 设施区域类型关系id
	private String data_version;// 数据版本
	private String lng_lat;     //变形后的经纬度坐标
	private int release;        //0：为入cms库，1：已入cms库
	private int x_id;           //x_id
	private int condition;      //相对于cms库的状态，0：新增，1：修改， 2：删除
	private String center;      //设施区域中心点坐标
	private double area;        //设施区域面积
	public Facilityachieve() {
		
	}
	
	public Facilityachieve(int gid,String mesh,long poi_id) {
		this.mesh = mesh;
		this.poi_id = poi_id;
		this.gid = gid;
	}
	
	public static Facilityachieve createFromJson(JSONObject json) {
		Facilityachieve fa = new Facilityachieve();
		fa.setTaskitem_id(json.getInt("taskitem_id"));
		fa.setPoi_id(json.getLong("poi_id"));
		fa.setFa_flag(json.getInt("fa_flag"));
		fa.setFa_type(json.getInt("fa_type"));
		fa.setDisp_class(json.getInt("disp_class"));
		fa.setArea_flag(json.getInt("area_flag"));
		fa.setSources(json.getInt("sources"));
		fa.setFatype_id(json.getInt("fatype_id"));
		fa.setName_chn(json.getString("name_chn"));
		fa.setMesh(json.getString("mesh"));
		fa.setPrecision(json.getString("precision"));
		String taskname = json.getString("proname");
		fa.setProname(taskname.indexOf("—") > -1 ? taskname.substring(0, taskname.indexOf("—")): taskname);
		fa.setUpdatetime(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		String polygon = json.getString("polygon");
		String[] appendFirstPoint = polygon.split(";");
		if(!(appendFirstPoint[0]).equals(appendFirstPoint[appendFirstPoint.length-1])){
			polygon+=";"+appendFirstPoint[0];
		}
		fa.setLng_lat(polygon);
		final boolean isReverse = true;
		final boolean isSecond = false;
		fa.setGeom(GetGeometry.getGeometry(polygon, ! isReverse, POLYGON, isSecond));		//变形后秒坐标
		fa.setCenter(Utils.caculateCenter(fa.getGeom()));									//设置中心点坐标
		double area = json.getDouble("polygonArea");
		fa.setArea(area==0 ? Utils.caculateArea(fa.getGeom()): area);						//设置设施区域面积
		fa.setReverse_geom(GetGeometry.getGeometry(polygon, isReverse, POLYGON, isSecond));	//变形前秒坐标 
		return fa;
	}


	/**
	 * @param fa 索引编号
	 * @param fa_id 用户编号
	 * @param name_chn 设施区域中文名称
	 * @param mesh 设施区域所在图幅号
	 * @param poi_id 设施区域主POIid
	 * @param fa_type 设施区域制作方式
	 * @param disp_class 显示等级
	 * @param fa_flag
	 * @param area_flag
	 * @param geom
	 * @param poi_guid
	 * @param precision
	 * @param sources
	 * @param updatetime
	 * @param proname
	 * @param data_version
	 * @param taskitem_id
	 */
	public Facilityachieve(long fa_id, String name_chn,
			String mesh, long poi_id, int fa_type, int disp_class, int fa_flag,
			int area_flag, String geom, String poi_guid, String precision, int sources,
			String updatetime, String proname, String data_version, int taskitem_id) {
		super();
		this.fa_id = fa_id;
		this.name_chn = name_chn;
		this.mesh = mesh;
		this.poi_id = poi_id;
		this.fa_type = fa_type;
		this.disp_class = disp_class;
		this.fa_flag = fa_flag;
		this.area_flag = area_flag;
		this.geom = geom;
		this.poi_guid = poi_guid;
		this.precision = precision;
		this.sources = sources;
		this.updatetime = updatetime;
		this.proname = proname;
		this.data_version = data_version;
		this.taskitem_id = taskitem_id;
	}
	
	public Facilityachieve(String name_chn,String mesh, long poi_id,
			int fa_type, int disp_class, int fa_flag,
			int area_flag, String geom, String precision, int sources,
			String updatetime, String proname) {
		super();
		this.name_chn = name_chn;
		this.mesh = mesh;
		this.poi_id = poi_id;
		this.fa_type = fa_type;
		this.disp_class = disp_class;
		this.fa_flag = fa_flag;
		this.area_flag = area_flag;
		this.geom = geom;
		this.precision = precision;
		this.sources = sources;
		this.updatetime = updatetime;
		this.proname = proname;
	}

	public int getRelease() {
		return release;
	}

	public void setRelease(int release) {
		this.release = release;
	}

	public int getX_id() {
		return x_id;
	}

	public void setX_id(int x_id) {
		this.x_id = x_id;
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getLng_lat() {
		return lng_lat;
	}

	public void setLng_lat(String lng_lat) {
		this.lng_lat = lng_lat;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}


	public long getFa_id() {
		return fa_id;
	}

	public void setFa_id(long fa_id) {
		this.fa_id = fa_id;
	}

	public String getName_chn() {
		return name_chn;
	}

	public void setName_chn(String name_chn) {
		this.name_chn = name_chn;
	}

	public String getMesh() {
		return mesh;
	}

	public void setMesh(String mesh) {
		this.mesh = mesh;
	}

	public long getPoi_id() {
		return poi_id;
	}

	public void setPoi_id(long poi_id) {
		this.poi_id = poi_id;
	}

	public int getFa_type() {
		return fa_type;
	}

	public void setFa_type(int fa_type) {
		this.fa_type = fa_type;
	}

	public int getDisp_class() {
		return disp_class;
	}

	public void setDisp_class(int disp_class) {
		this.disp_class = disp_class;
	}

	public int getFa_flag() {
		return fa_flag;
	}

	public void setFa_flag(int fa_flag) {
		this.fa_flag = fa_flag;
	}

	public int getArea_flag() {
		return area_flag;
	}

	public void setArea_flag(int area_flag) {
		this.area_flag = area_flag;
	}

	public String getGeom() {
		return geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}

	public int getTaskitem_id() {
		return taskitem_id;
	}

	public void setTaskitem_id(int taskitem_id) {
		this.taskitem_id = taskitem_id;
	}

	public String getReverse_geom() {
		return reverse_geom;
	}

	public void setReverse_geom(String reverse_geom) {
		this.reverse_geom = reverse_geom;
	}

	public String getPoi_guid() {
		return poi_guid;
	}

	public void setPoi_guid(String poi_guid) {
		this.poi_guid = poi_guid;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public int getSources() {
		return sources;
	}

	public void setSources(int sources) {
		this.sources = sources;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public int getFatype_id() {
		return fatype_id;
	}

	public void setFatype_id(int fatype_id) {
		this.fatype_id = fatype_id;
	}

	public String getData_version() {
		return data_version;
	}

	public void setData_version(String data_version) {
		this.data_version = data_version;
	}
	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Facilityachieve [gid=" + gid + ", fa_id="
				+ fa_id + ", name_chn=" + name_chn + ", mesh=" + mesh
				+ ", poi_id=" + poi_id + ", fa_type=" + fa_type
				+ ", disp_class=" + disp_class + ", fa_flag=" + fa_flag
				+ ", area_flag=" + area_flag + ", geom=" + geom
				+ ", taskitem_id=" + taskitem_id + ", reverse_geom=" + reverse_geom 
				+ ", poi_guid=" + poi_guid + ", precision=" + precision 
				+ ", sources=" + sources + ", updatetime=" + updatetime + ", proname=" + proname + ", fatype_id=" + fatype_id +"]";
	}
	
	@Override
	public int hashCode() { //哈希码的作用是确定该对象在哈希表中的索引位置。
		if(mesh==null){
			return -1;
		}
        return (int) poi_id +  mesh.hashCode();  
    }
	@Override
	public boolean equals(Object obj) { 
		Facilityachieve facility = (Facilityachieve)obj;
		if(facility == null){
			return false;              
		}  
        return poi_id == facility.poi_id && facility.mesh.equals(mesh);  
    } 
	
}
