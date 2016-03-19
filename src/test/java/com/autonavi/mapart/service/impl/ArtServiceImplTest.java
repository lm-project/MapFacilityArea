package com.autonavi.mapart.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.service.ArtService;
import com.autonavi.mapart.service.BaseTestCase;
import com.autonavi.mapart.service.PolygonCacheService;

public class ArtServiceImplTest extends BaseTestCase {

	@Autowired
	private  PolygonCacheService polygonCacheService;
	@Autowired
	private ArtService artService;

//	@Test
//	public void testGetBaiduMapresult() {
//		String key = "北京大学";
//		ResponseStatus status = polygonCacheService.readBd(key);
//		assertEquals(100, status.getCode());
//	}
//
//	@Test
//	public void testGetGetQQMapresult() {
//		String key = "北京大学";
//		ResponseStatus status = polygonCacheService.readQQ(key);
//		assertEquals(100, status.getCode());
//	}
//
//	@Test
//	public void testGetAmapresult() {
//		String key = "北京大学";
//		ResponseStatus status = polygonCacheService.readAutonavi(key);
//		assertEquals(100, status.getCode());
//	}

	@Test
	public void testGetPolygonByBound() {
		List<Facilityachieve> list = artService.getPolygonByBound("116.447182,39.95074;116.536102,39.992246");
		assertEquals(1, list.size());
	}

	@Test
	public void testInsertFacilityarea() {
		JSONObject jsonObj = new JSONObject();
		Object obj = "{\"gid\":\"\",\"fa\":\"\",\"fa_id\":\"\",\"name_chn\":\"朝阳区酒仙桥十街坊三号院(北门)\",\"mesh\":\"J50F001020\","
				+ "\"poi_id\":\"292233\",\"disp_class\":\"0\",\"fa_flag\":\"0\",\"area_flag\":\"0\","
				+ "\"polygon\":\"116.488231,39.964918,116.490739,39.963223,116.489391,39.962,116.488234,39.960935,116.487931,39.961478,"
				+ "116.488687,39.96215,116.488612,39.962201,116.487277,39.963057,116.487105,39.962923,116.486554,39.963282,116.486677,39.963465\","
				+ "\"taskitem_id\":\"15104\",\"fa_type\":3110,\"precision\":\"A\",\"sources\":0,\"proname\":\"小区类型设施区域互查——王香香\"}";
//		Object obj = "{\"gid\":\"\",\"fa\":\"\",\"fa_id\":\"\",\"name_chn\":\"寰球之家\",\"mesh\":\"J50F001020\","
//				+ "\"poi_id\":\"219702\",\"disp_class\":\"0\",\"fa_flag\":\"0\",\"area_flag\":\"0\","
//				+ "\"polygon\":\"116.415832,39.975607,116.415843,39.97504,116.417366,39.975028,116.417388,39.975389,116.41735,39.975611\","
//				+ "\"taskitem_id\":\"9660\",\"fa_type\":\"3110\"}";
		@SuppressWarnings("static-access")
		JSONObject json = jsonObj
				.fromObject(obj);

		Facilityachieve fa = Facilityachieve.createFromJson(json);
		artService.insertFacilityarea(fa);
	}

	@Test
	public void testInsertQaresult() {
		JSONObject jsonObj = new JSONObject();
		@SuppressWarnings("static-access")
		JSONObject json = jsonObj
				.fromObject("{\"id\":\"\",\"task_item_id\":\"14294\",\"qamark\":\"116.488231,39.964918,116.490739,39.963223,116.489391,39.962\",\"remark\":\"eeeeee\",\"path\":"
				+ "[\"116.463575,39.929517,116.454735,39.927181,116.455679,39.929254\""
				+ ",\"116.463575,39.929517,116.454735,39.927181,116.455679,39.929254\"]"
				+ "}");
		Qaresult[] qa = Qaresult.createFromJson(json);
		artService.insertQaresult(qa);
	}

	/**
	 * savedPolygon.js (第 44 行) GET
	 * http://localhost:8080/MapFacilityArea/getPolylines
	 * /116.469927,39.913489&116.503658,39.934552
	 * 
	 * 200 OK 62ms jquery-1.9.1.js (第 5 行) 头信息响应JSONCookies
	 */
	@Test
	public void testGetPolylineByBound() {
		List<Qaresult> list = artService.getPolylineByBound("116.469927,39.913489;116.503658,39.934552");
		// System.out.println(list.get(0).getQamark());
		assertEquals(0, list.size());
	}

	@Test
	public void testGetQaresult() {
		List<Qaresult> qa = artService.getQaresult(14294);
//		System.out.println(qa.get(0).getQamark());
	}

}
