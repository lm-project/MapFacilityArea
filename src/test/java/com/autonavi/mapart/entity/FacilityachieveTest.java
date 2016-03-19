package com.autonavi.mapart.entity;

import static org.junit.Assert.*;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:service-context.xml")
public class FacilityachieveTest {

	@Test
	public void testCreateFromJson() {
		JSONObject jsonObj = new JSONObject();   
		@SuppressWarnings("static-access")
		JSONObject json = jsonObj.fromObject("{\"gid\":\"\",\"fa\":\"\",\"fa_id\":\"\",\"name_chn\":\"朝阳区酒仙桥十街坊三号院(北门)\","
				+ "\"mesh\":\"J50F001020\",\"poi_id\":\"292233\",\"disp_class\":\"0\",\"fa_flag\":\"0\",\"area_flag\":\"0\","
				+ "\"polygon\":\"116.488231,39.964918,116.490739,39.963223,116.489391,39.962,116.488234,39.960935,116.487931,39.961478,"
				+ "116.488687,39.96215,116.488612,39.962201,116.487277,39.963057,116.487105,39.962923,116.486554,39.963282,116.486677,39.963465,"
				+ "116.488231,39.964918\",\"taskitem_id\":15105,\"fa_type\":3110,\"precision\":\"A\",\"sources\":0,\"proname\":\"小区类型设施区域互查——王香香\"}");
	
		Facilityachieve fa = Facilityachieve.createFromJson(json);
		System.out.println(fa.getGeom());
		System.out.println(fa.getProname());
		assertEquals("朝阳区酒仙桥十街坊三号院(北门)", fa.getName_chn());
	}

}
