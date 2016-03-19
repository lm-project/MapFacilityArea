package com.autonavi.mapart.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.mapart.entity.DaoResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-context.xml")
public class AnShpServiceImplTest {
	
	@Autowired
	private AnShpServiceImpl anShpService;

	@Test
	public void testImportShp() {
		String filePath = "D:/20141017_183616.zip";
		String dataVersion = "14q3";
		try{
			DaoResult result = anShpService.importShp(filePath, dataVersion);
			assertEquals(100, result.getTotalCount());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.getStackTrace();
		}
	}

	@Test
	public void testUnzipFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testImpShps() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindShpFiles() {
		fail("Not yet implemented");
	}

	@Test
	public void testExitsTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testDrop() {
		fail("Not yet implemented");
	}

}
