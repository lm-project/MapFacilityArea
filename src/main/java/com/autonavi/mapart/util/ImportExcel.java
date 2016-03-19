package com.autonavi.mapart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.entity.Taskitem;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * desc: 任务清单数据表导入
 * <p>
 * Copyright: Copyright(c)AutoNavi 2013
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2013-11-20 11:00
 * 
 */

public class ImportExcel {
	
	private static Logger log = Logger.getLogger(ImportExcel.class);
	private static List<Task> tasklist = null;								//任务包集合
	private static List<Taskitem> taskitemlist = null;						//任务项集合
	private static String username = null;								    //作业人，分割任务包的关键
	private static OPCPackage opcPackage = null;							//2007以上版本打开excel对象
	
	/**
	 * 对外提供导入excel 的方法
	 * 
	 * @param path
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<Task> readTaskExcel(String path) throws IOException  {
		String extension = path.lastIndexOf(".") == -1 ? "" : 
								path.substring(path.lastIndexOf(".") + 1);
		File file = new File(path);
		log.info(file.getAbsolutePath()+"-------->"+ file.exists());
		if (file.exists()) {
			if ("xls".equals(extension)) {
				return read2003TaskExcel(file);
			} else if ("xlsx".equals(extension)) {
				return read2007TaskExcel(file);
			}
		}
		return null;
		
	}

	public static String getHSSFCellValue(HSSFCell cell) {
		if (null == cell || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			return "";
		}
		return cell.toString();

	}

	public static String getXSSFCellValue(XSSFCell cell) {
		if (null == cell || cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			return "";
		}
		return cell.toString();
	}

	
	private static void setTaskItem2003(HSSFRow row, String taskname, String oppeple) throws IOException {
		Taskitem taskitem = new Taskitem();
		taskitem.setTaskname( taskname +"——"+ oppeple);						//任务包名称
		taskitem.setName( getHSSFCellValue(row.getCell(1)));				// 名称
		taskitem.setX_coord( getHSSFCellValue(row.getCell(2)));				// 变形前经度
		taskitem.setY_coord( getHSSFCellValue(row.getCell(3)));				// 变形前维度
		taskitem.setCity( getHSSFCellValue(row.getCell(4)));				// 城市
		taskitem.setProcesstype( getHSSFCellValue( row.getCell(5)));		// 处理类型
		taskitemlist.add(taskitem);
	}
	
	
	/**
	 * 读取 office 2003 excel
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<Task> read2003TaskExcel(File file) throws IOException {
		tasklist = new ArrayList<Task>();
		Task task = null;										//任务包
		FileInputStream out = null;
		HSSFWorkbook hwb = null;
		try {	
			out = new FileInputStream(file);
			hwb = new HSSFWorkbook(out);
			HSSFSheet sheet = hwb.getSheetAt(0);
			int i = 1;
			while (true) {
				HSSFRow row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				String taskname = getHSSFCellValue(row.getCell(0));// 项目名称
				if("".equals(taskname)) {
					break;
				}
				String oppeple = getHSSFCellValue(row.getCell(6)); // 作业人员
				if (username == null) {
					task = setTask(taskname,oppeple);
				}
				if( username != null && !oppeple.equals(username) ) {
						task.setList(taskitemlist);
						tasklist.add(task);
						task = setTask(taskname,oppeple);
				}
				setTaskItem2003(row,taskname,oppeple);
				i++;
			}
			task.setList(taskitemlist);
			tasklist.add(task);
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			username = null;
			out.close();
		}
		return tasklist;
	}

	/**
	 * 读取Office 2007 excel
	 * 
	 * @param filePath
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private static List<Task> read2007TaskExcel( File file ) throws IOException {
		log.debug("---------"+username);
		Task task = null;										//任务包
		tasklist = new ArrayList<Task>();
		try {
			XSSFRow row = null;
			opcPackage = OPCPackage.open(file);
			XSSFWorkbook xwb = new XSSFWorkbook(opcPackage);
			XSSFSheet sheet = xwb.getSheetAt(0);// 读取第一个工作簿
			int i = 1;
			while (true) {
				row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				String taskname = getXSSFCellValue(row.getCell(0));			// 项目名称
				if("".equals(taskname)) {
					break;
				}
				String oppeple = getXSSFCellValue(row.getCell(6)).toString();// 作业人员
				if (username == null) {
					task = setTask(taskname,oppeple);
				}
				if( username != null && !oppeple.equals(username) ) {
					log.debug("-----------task:"+task);
						task.setList(taskitemlist);                 //每个任务包包含的任务项集合
						tasklist.add(task);							//任务包集合
						task = setTask(taskname,oppeple);			//按领取人分配任务包
				}
				taskitemlist.add( setTaskItem(taskname,row,oppeple));
				i++;
			}
			task.setList(taskitemlist);
			tasklist.add(task);
		} catch (Exception e) {
			log.debug("解析上传Excel异常"+e.getMessage());
			e.printStackTrace();
		} finally {
			coloseReadExcelObj(opcPackage);
		}

		return tasklist;
	}
    private static Task setTask(String taskname, String oppeple){
    	Task task = new Task();
		task.setName(taskname+ "——" +oppeple);
		task.setProjectname(taskname);
		task.setTasktype(0);
		taskitemlist = new ArrayList<Taskitem>();	//清空任务项list
		username = oppeple;							//记录作业人员
		return task;
    }
    private static Taskitem setTaskItem(String taskname, XSSFRow row, String oppeple){
    	Taskitem taskitem = new Taskitem();
		taskitem.setTaskname(taskname + "——" + oppeple);				// 任务包名称
		taskitem.setName(getXSSFCellValue(row.getCell(1)));				// 名称
		taskitem.setX_coord(getXSSFCellValue(row.getCell(2)));			// 变形前经度
		taskitem.setY_coord(getXSSFCellValue(row.getCell(3)));			// 变形前纬度
		taskitem.setCity(getXSSFCellValue(row.getCell(4)));				// 城市
		taskitem.setProcesstype(getXSSFCellValue(row.getCell(5)));		// 处理类型
		return taskitem;
    }
    
    
    private static void coloseReadExcelObj(OPCPackage opcPackage){
    	try {
    		username = null;
			if(opcPackage != null){
				opcPackage.flush();
				opcPackage.close();
				opcPackage = null;
			}
		} catch (IOException e) {
			log.debug("关闭打开excel 对象异常："+e.getMessage());
			e.printStackTrace();
		}
    }
    
	public static void main(String[] args) {
		String path = "C:/Users/i-caiqiang/Desktop/设施区域在线制作平台/taskdemo.xls";
		File file = new File(path);
		try {
			if (file.exists()) {
				System.out.println(file.getAbsolutePath());
			}
			List<Task> list = readTaskExcel(file.getAbsolutePath());
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getProjectname());
				List<Taskitem> taskitemslist = list.get(i).getList();
				for (int j = 0; j < taskitemslist.size(); j++) {
					System.out.println(i +"__"+  j +"__"+ taskitemslist.get(j).getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}