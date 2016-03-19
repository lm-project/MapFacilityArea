package com.autonavi.mapart.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:service-context.xml")
public class ReadExcelTest {
	
	
	@Test
	public void testReadExcel() throws Exception {
//		String path = "E:/test.xls";
		String path = "E:/test.xlsx";
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			int sheetNum = 1;
//			List<List<String>> list = ReadExcel03Parse.readExcel(fis, sheetNum);
			List<List<String>> list = ReadExcel07Parse.readExcel(fis, sheetNum);
			assertEquals(5,list.get(0).size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
