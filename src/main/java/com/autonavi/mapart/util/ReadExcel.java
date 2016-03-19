package com.autonavi.mapart.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.service.PolygonCacheService;

/**
 * @author zhentao.liu
 * 
 */
@Service("ReadExcel")
public class ReadExcel {
	private static Log log = LogFactory.getLog(ReadExcel.class);

	public static List<FapoiRelation> readXlsx(String path) {
		
		File inputFile = new File(path);
		if(!inputFile.exists()){
			return null;
		}
		Collection<String> allNames = new ArrayList<String>();
		List<FapoiRelation> list = new ArrayList<FapoiRelation>();
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(inputFile));
			XSSFSheet sheet = wb.getSheetAt(0);
			Row row;
			int i = 0;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				
				row = rowIterator.next();
				String PoiName = StringUtils.trim(row.getCell(0).toString());
				String dd = StringUtils.trim(row.getCell(1).toString());
				String tt = StringUtils.trim(row.getCell(2).toString().replace(".0", ""));
				String xx = StringUtils.trim(row.getCell(3).toString());
				list.add(new FapoiRelation(PoiName,dd,tt,xx));
//				if (!PoiName.equals("")) {
//					allNames.add(PoiName);
//				}
				log.info(++i + ":" + PoiName+","+dd+","+tt+","+xx);
			}
			return list;
		} catch (Exception e) {
			log.debug("read_excel_Exception :" + e.getMessage());
			return list;
		}
	}


	public static void main(String[] args) throws Exception {
		  try { 
			  File inputFile = new File("D:/设施区域类型编码.xlsx");
		  
//			  BeanFactory factory = new ClassPathXmlApplicationContext("service-context.xml");
//			  PolygonCacheService polygonCache = (PolygonCacheService)factory.getBean("polygonCacheService");
		  
//			  Collection<String> allPoiNames = 
			  readXlsx("F:\\设施区域类型编码.xlsx");
		  
//			  polygonCache.updateBdAll(allPoiNames);
			
			 } catch (Exception e) {
				 e.printStackTrace(); 
			}

	
		
	}
	
	
}
