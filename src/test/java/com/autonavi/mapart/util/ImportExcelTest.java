package com.autonavi.mapart.util;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.entity.Taskitem;

public class ImportExcelTest {
	
	/**
	 * 测试数据模板错误时，数据库是否回滚
	 */
	@Test
	public void testReadTaskExcel() {
		
		String path = "src/test/resources/data/taskdemo.xlsx";
		File file = new File(path);
		try {
			if (file.exists()) {
				System.out.println(file.getAbsolutePath());
			}
			List<Task> list = ImportExcel.readTaskExcel(file.getAbsolutePath());
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getProjectname() + "________________");
				
				List<Taskitem> taskitemslist = list.get(i).getList();
//				System.out.println(list.get(i).getList().size());
				
				for (int j = 0; j < taskitemslist.size(); j++) {
					System.out.println(i +"__"+  j +"__"+ taskitemslist.get(j).getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
