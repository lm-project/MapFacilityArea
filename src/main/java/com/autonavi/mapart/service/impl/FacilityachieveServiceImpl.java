package com.autonavi.mapart.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.orm.impl.FacilityHistoryDao;
import com.autonavi.mapart.service.FacilityachieveService;
import com.autonavi.mapart.util.TarUtil;
import com.autonavi.mapart.util.ZipUtil;

@Service
public class FacilityachieveServiceImpl implements FacilityachieveService {
	
	private static final String TABLE_NAME = "#tableName#";

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

	private static final String JDBC_PROPERTIES = "/jdbc.properties";

	private static final String PORT = "#port#";

	private static final String EXP_PATH = "#exp_path#";

	private static final String SID = "#sid#";

	private static final String HOST = "#host#";
	
	private static final String SHAPE_PATH = "#imp_path#";
	
	private static final String SQL_PATH = "#sqlPath#";
	

	@Autowired
	private FacilityareaDao facilityareaDao;
	@Autowired
	private FacilityHistoryDao facilityHistoryDao;

	private String exportCmd;
	private String importCmd;
	private String shp2psqlCmd;
	private String psqlCmd;
	

	public void setExportCmd(String exportCmd) {
		this.exportCmd = exportCmd;
	}
	
	public void setImportCmd(String importCmd) {
		this.importCmd = importCmd;
	}

	public void setShp2psqlCmd(String shp2psqlCmd) {
		this.shp2psqlCmd = shp2psqlCmd;
	}

	public void setPsqlCmd(String psqlCmd) {
		this.psqlCmd = psqlCmd;
	}

	private Log log = LogFactory.getLog(getClass());
	
	@Override
	public String pgsql2shp(String param, String type) throws Exception {
		try {
			exportShape(param, type);// 生成到webapps/download/
			return getRelativeUrl(getShapeOutputPath());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	//TODO 事务
	private void exportShape(String param, String type) throws Exception {
		String[] hostSid = getHostSid();
		// create tmp tablename
		String tmpTableName = getTmpTableName(param);
		try {
			facilityareaDao.creatTmpTable(tmpTableName, param, type);
			if(facilityareaDao.getDataCount(tmpTableName) == 0){
				throw new Exception("此任务没有提交的数据");
			}
			String shapeOutputPath = getShapeOutputPath();
			log.debug("导出的路径为："+shapeOutputPath);
			if( ! new File(shapeOutputPath).exists() ) {
				new File(shapeOutputPath).mkdirs();
			}
			log.info("____________________________________________________________________");
			//replace tablename 
			String newCmd = exportCmd.replaceAll(HOST, hostSid[0]).replaceAll(SID, hostSid[2]).replaceAll(PORT, hostSid[1])
					.replaceAll(EXP_PATH, shapeOutputPath).replaceAll(TABLE_NAME, tmpTableName);
			
			log.info("export shap command :" + newCmd);
			new DefaultExecutor().execute(CommandLine.parse(newCmd));
			tarFile(getFileDir(shapeOutputPath), getFileName(shapeOutputPath));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw e;
		} finally {
			facilityareaDao.deleteTmpTable(tmpTableName);
		}
	}
	

	private String getTmpTableName(String param) {
		return "facilityarea_" + new Date().getTime();
	}
	
	private String[] getHostSid() throws IOException, FileNotFoundException {
		Properties p = getJdbcProperties();

		String[] hostSid = getHostSid(p.getProperty("jdbc.url"));
		if (hostSid.length != 3) {
			throw new IOException("解析jdbc.properties出错" + p.getProperty("jdbc.url"));
		}
		return hostSid;
	}
	
	protected Properties getJdbcProperties() throws IOException, FileNotFoundException {
		final Properties p = new Properties();
		p.load(FacilityachieveServiceImpl.class.getResourceAsStream(JDBC_PROPERTIES));
		return p;
	}
	
	protected String[] getHostSid(String jdbcUrl) {
		Pattern pattern = Pattern.compile("jdbc:postgresql://([\\w\\.]+):([0-9]+)/(\\w+)");
		Matcher matcher = pattern.matcher(jdbcUrl);
		while (matcher.find()) {
			return new String[] { matcher.group(1), matcher.group(2), matcher.group(3) };
		}
		return new String[] {};
	}
	
	protected String getShapeOutputPath() {
		return new File(FacilityachieveServiceImpl.class.getResource(JDBC_PROPERTIES).getFile()).getParentFile().getParentFile()
				.getParentFile().getParentFile().getPath().replace("\\","/")
				+ "/download/" + SIMPLE_DATE_FORMAT.format(new Date());
	}
	
	private String getFileName(String shapeOutputPath) {
		String str = shapeOutputPath.substring(shapeOutputPath.lastIndexOf("/") + 1, shapeOutputPath.length());
		log.debug("导出文件的名称："+str);
		return str;
	}

	private String getFileDir(String shapeOutputPath) {
		String str = shapeOutputPath.substring(0, shapeOutputPath.lastIndexOf("/"));
		log.debug("导出的路径为："+str);
		return str;
	}
	
	private void tarFile(String dir, String fileName) throws ExecuteException, IOException {
		TarUtil.pack(new File[] { new File(dir + "/" + fileName + ".shp"), new File(dir + "/" + fileName + ".shx"),
				new File(dir + "/" + fileName + ".dbf") }, new File(dir + "/" + fileName + ".tar"));
	}
	
	protected String getRelativeUrl(String shapeOutputPath) {
		final String web = "webapps";
		log.debug("导出的路径为："+shapeOutputPath);
		log.debug("下载的路径为："+shapeOutputPath.substring(shapeOutputPath.indexOf(web) + web.length(), shapeOutputPath.length()));
		return shapeOutputPath.substring(shapeOutputPath.indexOf(web) + web.length(), shapeOutputPath.length());
	}

	@Override
	public boolean shp2pgsql(String dir) throws Exception {
		@SuppressWarnings("unused")
		File fDir = new File(dir);
		@SuppressWarnings("unused")
		String sqlDir = String.valueOf( new Date().getTime() )+"/";
//		createSqlFiles(fDir, implTablName, sqlDir);
//		facilityHistoryDao.insertHistory( buildSqls(sqlDir) );
		return false;
	}

	@Override
	public DaoResult importShp(String filepath, String dataVersion) throws Exception {
		
		String fileDir = getFileDir(filepath)+"/"; // 文件所在目录
		//文件解压
		String shapeFileDir = unzipFile(filepath, fileDir);
		
		// 查找shape
		Collection<File> anShpFiles = new ArrayList<File>();
		findShpFiles(shapeFileDir, anShpFiles);
		
		int importCount = 0;
		String sqlDir = fileDir + "tmp.sql";
		for (File file : anShpFiles) {
			String tableName = "facilityarea_" + dataVersion;
			try {
				createSqlFiles(file.getAbsolutePath().replace("\\", "/"), tableName, sqlDir);
				System.out.println("end");
//				buildPsql(sqlDir);
				importCount = facilityareaDao.getDataCount(tableName);
			} catch (Exception e) {
				log.error(e.getMessage());
				return new DaoResult(200, e.getMessage());
			} finally {
//				UploadUtil.delFile(sqlDir);
//				UploadUtil.delFile(shapeFileDir);
			}
		}

		return new DaoResult(null, importCount, 100, "上传文件成功!");
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath*:service-context.xml"});
		FacilityachieveServiceImpl bean = ctx.getBean(FacilityachieveServiceImpl.class);
		String filePath = "D:/cq/fileio/upload/20141015155633_ConverN.zip";
		String dataVersion = "14q3";
		try {
			bean.importShp(filePath, dataVersion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param filepath
	 * @param file
	 * @param fileDir
	 * @return 
	 * @throws IOException
	 */
	protected String unzipFile(String filepath, String fileDir)
			throws IOException {
		// TODO unzip file
		File file = new File(filepath);
		String fileName = getFileName(filepath);
		if (file.exists()){
			ZipUtil.unZipFiles(file, fileDir);
			return fileDir + fileName.substring(fileName.indexOf("_")+1, fileName.lastIndexOf("."));
		}
		return null;
	}

	/**
	 * 根据sql文件导入数据
	 * command：psql -U postgres -h localhost -d <database> -p 5432 -f <sqlDir>
	 * @param sqlDir
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	protected void buildPsql(String sqlDir) throws FileNotFoundException, IOException {
		String[] hostSid = getHostSid();
		String cmd = psqlCmd.replaceAll(HOST, hostSid[0]).replaceAll(SID, hostSid[2]).replaceAll(PORT, hostSid[1]).replaceAll(SQL_PATH, sqlDir);
		log.info("____________________________________________________________________");
		log.info("import sql command :" + cmd);
		try {
			new DefaultExecutor().execute(CommandLine.parse(cmd));
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * 根据shape文件创建sql文件
	 * command：shp2pgsql -W GBK <shapefile> <tablename> > <sqlDir>
	 * @param filepath shape文件路径
	 * @param tableName 表名
	 * @param sqlDir sql文件存放路径
	 * @throws Exception 
	 */
	protected void createSqlFiles(String filepath, String tableName, String sqlDir) throws Exception {
		//replace tablename 
//		String cmd = shp2psqlCmd.replaceAll(SHAPE_PATH, filepath).replaceAll(TABLE_NAME, tableName).replaceAll(SQL_PATH, sqlDir);
		String[] hostSid = getHostSid();
		String cmd = importCmd.replaceAll(SHAPE_PATH, filepath).replaceAll(TABLE_NAME, tableName).replaceAll(HOST, hostSid[0])
				.replaceAll(SID, hostSid[2]).replaceAll(PORT, hostSid[1]);
		log.info("____________________________________________________________________");
		log.info("import shape command :" + cmd);
		try {
			new DefaultExecutor().execute(CommandLine.parse(cmd));
//			File file = new File(sqlDir);
//			if (file.exists()) {
//				log.info("sql文件创建成功");
//			}else {
//				log.info("sql文件创建失败");
//			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw e;
		}
		
	}
	
	protected Collection<File> findShpFiles(String dir, Collection<File> shpFiles) {
		File[] files = new File(dir).listFiles();
		if (files == null) {
			return shpFiles;
		}

		for (File file : files) {
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

	@Override
	public List<Facilityachieve> getFacilityareaList(String id) throws DataAccessException {
		return facilityareaDao.getFacilityareaList(id);
	}

	@Override
	public void updateFacilityachieve(Facilityachieve fa) {
		facilityareaDao.updateByTaskitemId(fa);
	}
}
