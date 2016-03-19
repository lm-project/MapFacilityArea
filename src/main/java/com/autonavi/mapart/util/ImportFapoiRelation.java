
package com.autonavi.mapart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.autonavi.mapart.entity.FapoiRelation;

/**
 * 设施区域与poi类型对应表导入
 * @author huandi.yang
 *
 */
public class ImportFapoiRelation {
	
	private static Log log = LogFactory.getLog(ImportFapoiRelation.class);
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static List<FapoiRelation> readRelationExcel(String path) throws IOException{
		File file = new File(path);
		String extension = path.lastIndexOf(".") == -1 ? "" : 
			path.substring(path.lastIndexOf(".") + 1);
		log.info(file.getAbsolutePath()+"-------->"+ file.exists());
		if (file.exists()) {
			@SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream(file);
			int sheetNum = 0;       //读取第二个sheet
			if ("xls".equals(extension)) {
				return getRelationList(new ReadExcel03Parse().readExcel(fis, sheetNum));
			} else if ("xlsx".equals(extension)) {
				return getRelationList(new ReadExcel07Parse().readExcel(fis, sheetNum));
			}
		}else {
			
		}
		return null;
	}
	
	private static List<FapoiRelation> getRelationList(
			List<List<String>> excelData) {
		List<FapoiRelation> relationlist = new ArrayList<FapoiRelation>();
		FapoiRelation relation = null;
		List<String> list  = null;
		if(excelData.size() == 0){
			return null;
		} else {
			for(int i = 1; i < excelData.size()-1; i++){
				relation = new FapoiRelation();
				list = excelData.get(i);
				relation.setFa_category(list.get(1));  //设施区域大类
				relation.setFa_type(list.get(2));      //设施区域中类
				relation.setPoi_typecode(list.get(3)); //poi类型代码
				relation.setPoi_type(list.get(4));     //poi类型
				
				relationlist.add(relation);
			}
			return relationlist;
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String path = "E:/test.xlsx";
		List<FapoiRelation> list = readRelationExcel(path);
		System.out.println(list.size());

	}

}