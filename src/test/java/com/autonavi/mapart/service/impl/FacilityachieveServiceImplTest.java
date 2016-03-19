package com.autonavi.mapart.service.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.service.BaseTestCase;
import com.autonavi.mapart.util.GetGeometry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-context.xml")
public class FacilityachieveServiceImplTest {

	@Autowired
	private FacilityachieveServiceImpl facilityService;

	@Autowired
	private TaskServiceImpl taskServiceImpl;
	
	@Autowired
	private TaskitemServiceImpl taskitemServiceImpl;

	@Test
	public void testPgsql2shp() throws Exception {

		String type = "qaresult"; // 按项目导出
		String param = "台州市"; // 项目名称
		String outPath = "";
		try {
			outPath = facilityService.pgsql2shp(param, type);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.getStackTrace();
		}
		assertEquals("", outPath);
	}

	@Test
	public void testGetJdbcProperties() throws FileNotFoundException,
			IOException {
		Properties p = facilityService.getJdbcProperties();
		System.out.println(p.getProperty("jdbc.url"));
	}

	@Test
	public void testGetHostSid() throws FileNotFoundException, IOException {
		// String[] hostSid =
		// facilityService.getHostSid("jdbc:postgresql://localhost:5432/map_facility");
		Properties p = facilityService.getJdbcProperties();
		String[] hostSid = facilityService
				.getHostSid(p.getProperty("jdbc.url"));
		assertEquals(3, hostSid.length);
	}

	@Test
	public void testGetShapeOutputPath() {
		String path = facilityService.getShapeOutputPath();
		System.out.println(path);
	}

	@Test
	public void testGetRelativeUrl() {
		String path = facilityService.getShapeOutputPath();
		String url = facilityService.getRelativeUrl(path);
		System.out.println(url);
	}

	@Test
	public void testShp2pgsql() {
		fail("Not yet implemented");
	}

	@Test
	public void testImportShp() throws Exception {
		// String filePath = "D:/cq/fileio/upload/20141015155633_ConverN.zip";
		String filePath = "D:/AN.zip";

		String dataVersion = "14q3";
		DaoResult result = null;
		try {
			result = facilityService.importShp(filePath, dataVersion);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.getStackTrace();
		}
		assertEquals(100, result.getTotalCount());
	}

	@Test
	public void testgetFacilityareaList() {
		String projectname  = "火车站设施区域核实修改";
		StringBuffer task_ids_String = new StringBuffer("");
		List<Task> task_list = taskServiceImpl.getTasksByProjectName(projectname);
		for (int i = 0; i < task_list.size(); i++) {
			task_ids_String.append(task_list.get(i).getId()+",");
		}
		
		String task_ids = task_ids_String.substring(0, task_ids_String.length()-1);
//		String task_ids = "714,715,716,717,718,719,720,721,722,723";
		List<Taskitem> task_item_list = taskitemServiceImpl.getTaskitemByIds(task_ids);
		StringBuffer task_item_ids = new StringBuffer("");
		for (int i = 0; i < task_item_list.size(); i++) {
			task_item_ids.append(task_item_list.get(i).getId()+",");
		}
		String ss = task_item_ids.substring(0, task_item_ids.length()-1);
		List<Facilityachieve> list = facilityService.getFacilityareaList(ss);
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			Facilityachieve fa = list.get(i);
			fa.setReverse_geom(GetGeometry.getGeometry(fa.getGeom(), true, "POLYGON", true));
			System.out.println(fa.getGeom() +"--"+ fa.getReverse_geom());
			facilityService.updateFacilityachieve(fa);
		}
		
	}
	
	@Test
	public void cover() {
		//MULTIPOLYGON(((424402.5564 113815.1088,424410.3972 113813.5968,424409.238 113809.122,424407.4596 113809.4532,424401.2424 113810.7348,424401.318 113810.67,424402.5564 113815.1088)))
		String s = "MULTIPOLYGON(((424402.5564 113815.1088,424410.3972 113813.5968,424409.238 113809.122,424407.4596 113809.4532,424401.2424 113810.7348,424401.318 113810.67,424402.5564 113815.1088)))";
		System.out.println(GetGeometry.getGeometry(s, true, "POLYGON", true));
	}

	@Test
	public void testUnzipFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testBuildPsql() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateSqlFiles() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindShpFiles() {
		fail("Not yet implemented");
	}

}
