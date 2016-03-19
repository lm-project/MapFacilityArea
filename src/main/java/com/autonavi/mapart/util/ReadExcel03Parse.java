package com.autonavi.mapart.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import com.autonavi.mapart.entity.ExcelMergedRegionBean;

/**
 * 读取 office 2003 excel
 * @author huandi.yang
 *
 */
public class ReadExcel03Parse {

	public static List<List<String>> readExcel(InputStream is, int sheetNum)
			throws FileNotFoundException, IOException {

		// 构造 HSSFWorkbook 对象，传入参数为excel文件的io流

		HSSFWorkbook wb = new HSSFWorkbook(is);

		// 读取第一个sheet的内容

		HSSFSheet sheet = wb.getSheetAt(sheetNum);

		// 获取所有行的迭代对象

		Iterator<Row> rowIter = sheet.rowIterator();

		// 获取合并单元格对象的相关信息

		List<ExcelMergedRegionBean> mergedRegionMapList = new CopyOnWriteArrayList<ExcelMergedRegionBean>(

		getMergedRegionMapList(sheet));

		List<List<String>> contentList = new ArrayList<List<String>>();

		// 迭代所有行

		while (rowIter.hasNext()) {

			List<String> cellList = new ArrayList<String>();

			// 获取每一行

			Row row = rowIter.next();

			// 获取该行的列迭代对象

			Iterator<Cell> cellIter = row.cellIterator();

			// 迭代该行的每一列

			while (cellIter.hasNext()) {

				// 获取该行的每一列

				Cell cell = cellIter.next();

				// 获取该单元格的值

				String content = getActualCellValue(sheet, mergedRegionMapList,

				cell);

				cellList.add(content);

			}

			contentList.add(cellList);

		}

		return contentList;

	}

	public static String getActualCellValue(HSSFSheet sheet,

	List<ExcelMergedRegionBean> mergedRegionMapList, Cell myCell) {

		Cell actualCell = myCell;

		// 迭代合并单元格对象，判断myCell该对象是否属于合并单元格

		for (ExcelMergedRegionBean mb : mergedRegionMapList) {

			if (myCell.getRowIndex() > mb.getLastRow()) {

				mergedRegionMapList.remove(mb);

			}

			// 判断myCell该对象是否属于合并单元格，如果是的话，则直接退出循环

			if (myCell.getColumnIndex() <= mb.getLastCell()

			&& myCell.getColumnIndex() >= mb.getFirstCell()

			&& myCell.getRowIndex() <= mb.getLastRow()

			&& myCell.getRowIndex() >= mb.getFirstRow()) {

				Row row = sheet.getRow(mb.getFirstRow());

				Cell cell = row.getCell(mb.getFirstCell());

				actualCell = cell;

				break;

			}

		}

		// 返回该单元对应的真实值

		return getCellValue(actualCell);

	}

	public static List<ExcelMergedRegionBean> getMergedRegionMapList(
			HSSFSheet sheet) {

		List<ExcelMergedRegionBean> mergedRegionMapList = new ArrayList<ExcelMergedRegionBean>();

		// 获得一个 sheet 中合并单元格的数量

		int sheetmergerCount = sheet.getNumMergedRegions();

		// 便利合并单元格

		for (int i = 0; i < sheetmergerCount; i++) {

			// 获得合并单元格

			CellRangeAddress ca = sheet.getMergedRegion(i);

			// 获得合并单元格的起始行, 结束行, 起始列, 结束列

			int firstC = ca.getFirstColumn();

			int lastC = ca.getLastColumn();

			int firstR = ca.getFirstRow();

			int lastR = ca.getLastRow();

			ExcelMergedRegionBean mb = new ExcelMergedRegionBean();

			mb.setFirstRow(firstR);

			mb.setLastRow(lastR);

			mb.setFirstCell(firstC);

			mb.setLastCell(lastC);

			mergedRegionMapList.add(mb);

		}

		// 排序，便于后面循环删除

		Collections.sort(mergedRegionMapList);

		return mergedRegionMapList;

	}

	public static String getCellValue(Cell cell) {

		String cellValue = "";

		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {

			cellValue = rPadZeroUtil(String.valueOf(cell.getNumericCellValue()));

		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {

			cellValue = String.valueOf(cell.getBooleanCellValue());

		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {

			cellValue = cell.getStringCellValue();

		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {

			cellValue = String.valueOf(cell.getCellFormula());

		}

		return cellValue;

	}

	public static String rPadZeroUtil(String value) {

		if (value != null && !"".equals(value)) {

			if (value.endsWith(".0")) {

				return value.substring(0, value.indexOf(".0"));

			}

		}

		return value;

	}

}