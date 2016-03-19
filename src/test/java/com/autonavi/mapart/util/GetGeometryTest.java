package com.autonavi.mapart.util;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.mapart.entity.ReferenceGeom;
import com.autonavi.mapart.orm.ReferenceGeomDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:service-context.xml")
public class GetGeometryTest {

	@Autowired
	private ReferenceGeomDao dao;
	@Test
	public void testGetGeometry() {
		String pointString = "116.48,39.95,116.49,39.95,116.49,39.96,116.48,39.95";
		boolean isSecond = false;
		boolean isReverse = isSecond;
		String geomType = "LINESTRING";
		String geometry = GetGeometry.getGeometry(pointString, isReverse, geomType, isSecond);
		System.out.println(geometry);
		assertEquals("LINESTRING(419328.0 143820.0,419364.0 143820.0,419364.0 143856.0,419328.0 143820.0)", geometry);
	}
	
	@Test
	public void convert() {
		Collection<ReferenceGeom> geoms = dao.findCachedGeoms("baidu");
		boolean isSecond = true;
		boolean isReverse = false;
		String geomType = GetGeometry.POLYGON;
		for( ReferenceGeom g: geoms) {
			try{
				if(StringUtils.isBlank(g.getContext())) {
					continue;
				}
				String geometry = GetGeometry.getGeometry(g.getContext(), isReverse, geomType, isSecond);
				g.setGeom(geometry);
				dao.insert(g);
			} catch(Exception e) {
				System.out.println( g.getKey() + "\t" + g.getId());
				
			}
		}
	}
	
	@Test
	public void testGetPointPairString() {
		String pointString = "116.48,39.95,116.49,39.95,116.49,39.96";
		boolean isSecond = false;
		boolean isReverse = isSecond;
		String pointPair = GetGeometry.getPointPairString(pointString, isReverse, "POLYGON", isSecond);
		System.out.println(pointPair);
		assertEquals("419328.0 143820.0,419364.0 143820.0,419364.0 143856.0,419328.0 143820.0", pointPair);	
	}

	@Test
	public void testGetSecondCoord() {
		String x_coord = "116.48";
		String y_coord = "39.95";
		boolean isSecond = false;
		boolean isReverse = isSecond;
		Double[] coord = GetGeometry.getSecondCoord(x_coord, y_coord, isReverse, isSecond);
		System.out.println(coord[0] +"," +coord[1]);
		assertEquals(Double.valueOf("419328.0"), coord[0]);
	}

	@Test
	public void testReverseCoord() {
		double x_coord = 116.488231;
		double y_coord = 39.964918;
		Double[] coord = GetGeometry.reverseCoord(x_coord, y_coord);
		System.out.println(coord[0] +"," +coord[1]);
		assertEquals(Double.valueOf("0.0"), coord[0]);
	}

}
