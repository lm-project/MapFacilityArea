
package com.autonavi.mapart.orm.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.service.BaseTestCase;
import com.autonavi.mapart.util.ExecuteMultiPolygon;


public class FacilityareaDaoImplTest extends BaseTestCase {

	@Autowired
	private FacilityareaDao dao;
	@Test
	public void testGetFacility() {
		Facilityachieve facility = dao.getFacility(15104);
		assertNotNull(facility);
		
		String geom = facility.getGeom();
		assertEquals(ExecuteMultiPolygon.getSecondGeometry(geom,false),"");
	}
	
	@Test
	public void testCreatTmpTable() throws SQLException {
		String param = "小区设施区域编辑";
		String tmpTableName = "facilityarea_" + new Date().getTime();
		//建表
		dao.creatTmpTable(tmpTableName, param, "project");
		ResultSet rs = super.getResultSet("select count(*) from pg_class where relname = '" + tmpTableName +"'" );
		while(rs.next()){
			assertEquals(1, rs.getInt(1));
		}
		
		//查询数据
		int count = dao.getDataCount(tmpTableName);
		assertEquals(count, 0);
		
		//删除表
		dao.deleteTmpTable(tmpTableName);
		rs = super.getResultSet("select count(*) from pg_class where relname = '" + tmpTableName +"'" );
		while(rs.next()){
			assertEquals(0, rs.getInt(1));
		}
		
		
	}
	
	/*@Test
	public void testDeleteTmpTable() {
		String tmpTableName = "facilityarea_1407401971516";
		dao.deleteTmpTable(tmpTableName);
	}
	
	@Test
	public void testGetDataCount() {
		String tmpTableName = "facilityarea_1407401971516";
		int count = dao.getDataCount(tmpTableName);
		assertEquals(count, 0);
	}*/
	
	@Test
	public void saveCheck() {
		Qaresult[] rs = Qaresult.createFromJson(JSONObject.fromObject("{\"remark\":\"\t\t\t\t\t\t\tb\t\t\",\"task_item_id\":\"14294\",\"path\":"
				+ "[\"116.463575,39.929517,116.454735,39.927181,116.455679,39.929254\""
				+ ",\"116.463575,39.929517,116.454735,39.927181,116.455679,39.929254\"]"
				+ "}"));
//		int count = super.getCount("select count(1) from qa_result");
//		assertEquals(0, count);
		dao.insertQaresult(rs);
		int count = super.getCount("select count(1) from qa_result");
		assertEquals(11, count);
	}
	
	@Test
	public void saeCheckNoPath() {
		Qaresult[] rs = Qaresult.createFromJson(JSONObject.fromObject("{\"remark\":\"\t\t\t\t\t\t\tdddb\t\t\",\"task_item_id\":\"14268\",\"path\":"
				+ "[]"
				+ "}"));
		dao.insertQaresult(rs);
	}
	@Test
	public void getQa() {
		List<Qaresult> qaresult = dao.getQaresult(14385);
		assertNotNull(qaresult);
	}
	@Test
	public void getPolygon() {
		
		List<Facilityachieve> rs = dao.getPolygonByBound("116.463554,39.92454;116.471987,39.929073");
		assertEquals(0, rs.size());
//		Map m = rs.get(0);
//		assertEquals("", m.get(Integer.valueOf(747)));
	}
	
	@Test
	public void testGetAreaFlag() {
//		String path = "116.48,39.95,116.49,39.95,116.49,39.96,116.48,39.95";
		String path = "113.93775,22.316334,113.941183,22.307044,113.945475,22.311411,113.9429,22.317287";
		int area_flag = dao.getAreaFlag(path);
		assertEquals(0, area_flag);
	}

}
