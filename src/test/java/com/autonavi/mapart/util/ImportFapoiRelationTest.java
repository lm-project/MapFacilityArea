package com.autonavi.mapart.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.autonavi.mapart.entity.FapoiRelation;

public class ImportFapoiRelationTest {

	@Test
	public void testReadRelationExcel() {
		String path = "E:/test.xlsx";
		File file = new File(path);
		try {
			if (file.exists()) {
				System.out.println(file.getAbsolutePath());
			}
			List<FapoiRelation> list = ImportFapoiRelation.readRelationExcel(path);
			assertEquals(97, list.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
