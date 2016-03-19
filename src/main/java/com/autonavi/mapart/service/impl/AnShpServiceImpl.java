package com.autonavi.mapart.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.orm.AnShapeDao;
import com.autonavi.mapart.service.AnShpService;
import com.autonavi.mapart.service.H2gisServer;
import com.autonavi.mapart.util.GetGeometry;
import com.autonavi.mapart.util.ZipUtil;

@Service
public class AnShpServiceImpl implements AnShpService{
	
	private Log log = LogFactory.getLog(getClass());
	@Autowired
	private AnShapeDao anShapeDao;
	@Override
	public DaoResult importShp(String filepath, String dataVersion)throws Exception {
		final String mainTable = "TMP_TABLE";
		ArrayList<Facilityachieve> insertList =  new ArrayList<Facilityachieve>();
		ArrayList<Facilityachieve> updateList = new  ArrayList<Facilityachieve>();
		H2gisServer h2gisServer = H2gisServer.getInstance();
		ResultSet rs = null;
		try {
			String shapeFileDir = unzipFile(filepath); 			//解压导入文件
			List<Facilityachieve> list = anShapeDao.getDbData();//查询所有本地数据库的数据做差分
			log.debug("db query ok ....."+list.size());
			h2gisServer.startup();								//启动h2数据库
			impShps(shapeFileDir, mainTable);					//导入shape文件到内存数据库临时表
			rs = h2gisServer.executeQuery(createSqlStr(mainTable)); //读取临时表数据
			int i =0;
			while (rs.next()) {
				if(i==5){break;}
				Facilityachieve fa = new Facilityachieve(rs.getString(1), rs.getString(2), rs.getLong(3),
									rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), 
									GetGeometry.getGeometry(rs.getString(8), true , "POLYGON", true), 
									rs.getString(9), rs.getInt(10),rs.getString(11), rs.getString(12));	
				i++;
				setImportData(rs,list,insertList,updateList,fa);
			}
			log.debug("导入数据读取完毕------------------------------------------");
			dealImportData(insertList,updateList);
			return new DaoResult(null, insertList.size(), 100, "");
		} catch(Exception e) {
			e.printStackTrace();
			log.error(" import File error:"+e.getMessage());
			return new DaoResult(200, e.getMessage());
		} finally {
			log.debug("finally……………………………………………………………………………………………………");
			shutdowH2(rs,h2gisServer,mainTable);
		}
	}
	/**
	 *去重
	 * @param insertList 
	 * @param list2 
	 * @param rs 
	 * @param fa 
	 * @param lists 
	 * @throws SQLException 
	 */
	private void setImportData(ResultSet rs, List<Facilityachieve> list, ArrayList<Facilityachieve> insertList,
			ArrayList<Facilityachieve> updateList, Facilityachieve fa) throws SQLException{
		if(list.contains(fa)){
			fa.setGid(list.get(list.lastIndexOf(fa)).getGid());
			updateList.add(fa);
			return;
		}
		insertList.add(fa);
	}
	
	private void dealImportData(ArrayList<Facilityachieve> insertList, ArrayList<Facilityachieve> updateList){
		try {
			int insertListLength = insertList.size();
			int updateListLength = updateList.size();
			log.debug("导入数据的长度为："+insertListLength);
			log.debug("更新的数据长度为："+updateListLength);
			if(insertListLength>0){
				for(int i=0;i<Math.ceil(insertListLength/1000.0);i++){
					anShapeDao.importAnData(insertList.subList(i*1000,
							(i+1)*1000+1 > insertListLength?insertListLength-1:(i+1)*1000));
				}
			}
			if(updateListLength>0){
				for(int j=0;j<updateListLength;j++){
					anShapeDao.updateDataByImport(updateList.get(j));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("插入导入数据报错："+e.getMessage());
		}finally{
			insertList = null;
			updateList = null;
		}
	}
	private String createSqlStr(String mainTable){
		String querySql = "SELECT NAME_CHN, MESH, POI_ID, FA_TYPE, DISP_CLASS,FA_FLAG, "
				+ "AREA_FLAG, THE_GEOM, PRECISION, SOURCES, UPDATETIME, PRONAME from "+ mainTable;
		return querySql;
	}
	
	private void shutdowH2(ResultSet rs, H2gisServer h2gisServer, String mainTable){
		drop(mainTable);
		try {
			if(rs !=null){
				rs.close();
				rs = null;
			}
			if(h2gisServer!=null){
				h2gisServer.shutdown();
				h2gisServer = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * @param filepath .zip文件路径 /home/MapFacilityArea/importFile/20150115162516_14Q4鐗堝彉褰㈠墠姣嶅簱璁炬柦鍖哄煙.zip
	 * @param fileDir 文件解压路径 /home/MapFacilityArea/importFile/
	 * @return 解压后文件夹路径
	 * @throws IOException
	 */
	protected String unzipFile(String filepath) throws IOException {
		// TODO unzip file
		File file = new File(filepath);
		if (file.exists()){
			String fileStr = file.getAbsolutePath().substring(0,
					file.getAbsolutePath().lastIndexOf(".zip")).replaceAll("\\\\", "/")+"/";
			ZipUtil.unZipFiles(file, fileStr);
			return fileStr;
		}
		return "";
	}
	
	/**
	 * 导入shape文件到内存数据库
	 * @param shpDir
	 * @param mainTable
	 * @throws SQLException
	 */
	protected void impShps(String shpDir, String mainTable) throws SQLException {
		log.debug("解压导入的文件后的 路劲"+shpDir);
		ArrayList<File> shpFiles = new ArrayList<File>();
		findShpFiles(shpDir, shpFiles);
		log.debug("shp文件的数量为："+shpFiles.size());
		for (File f : shpFiles) {
			log.debug("shp文件的路径为："+f.getAbsolutePath());
			appendDataIntoTable(f.getAbsolutePath(), mainTable);
		}
	}
	
	/**
	 * 查找目录下shape文件
	 * @param dir
	 * @param shpFiles
	 * @return
	 */
	protected Collection<File> findShpFiles(String dir, Collection<File> shpFiles) {
		log.debug("文件存在吗？"+new File(dir).exists());
		File[] files = new File(dir).listFiles();
		if(files==null || files.length<=0){
			return null;
		}
		for (File file : files) {
			log.debug("每个文件路径为："+file.getAbsolutePath());
			if (file.isDirectory()) {
				findShpFiles(file.getAbsolutePath(), shpFiles);
			} else {
				if (file.getName().endsWith("shp")) {
					shpFiles.add(file);
				}
			}
		}
		return shpFiles;
	}
	
	
	private void appendDataIntoTable(String shp, String mainTable) throws SQLException {
		String tmpTname = "TMP_MERGE";
		H2gisServer.getInstance().importData(shp, tmpTname);
		if (!exitsTable(mainTable)) {
			String sql = "create table " + mainTable + " as select * from " + tmpTname + " where 1=2";
			log.debug("sql===="+sql);
			H2gisServer.getInstance().execute(sql);
		}
		H2gisServer.getInstance().execute(getInsertSql(tmpTname, mainTable));
		drop(tmpTname);
	}

	protected boolean exitsTable(String mainTable) {
		ResultSet rs = H2gisServer.getInstance().executeQuery(
				"select count(1) from information_schema.tables where table_schema ='PUBLIC' "
						+ "and table_type = 'TABLE' and lower(table_name) = '" + mainTable.toLowerCase() + "'");

		try {
			return rs.next() ? rs.getInt(1) > 0 : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getInsertSql(String tmpTname, String mainTable) {
		return " insert into "
				+ mainTable + " (THE_GEOM, NAME_CHN, MESH, POI_ID, FA_TYPE, AREA_FLAG, FA, FA_ID, DISP_CLASS,"
					+ " FA_FLAG, POI_GUID, PRECISION, SOURCES, UPDATETIME, PRONAME) "
				+ " select THE_GEOM, NAME_CHN, MESH, POI_ID, FA_TYPE, AREA_FLAG, FA, FA_ID, DISP_CLASS,"
					+ " FA_FLAG, POI_GUID, PRECISION, SOURCES, UPDATETIME, PRONAME from " + tmpTname;
	}

	protected void drop(String... tnames) {
		for(String tname: tnames) {
			if(exitsTable( tname )) {
				H2gisServer.getInstance().execute("drop table " + tname);
			}
		}
	}

	@Override
	public Facilityachieve getAnShapeByName(String name) {
		return anShapeDao.getShapeByName(name);
	}
}
