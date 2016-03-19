package com.autonavi.mapart.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.h2gis.drivers.shp.SHPRead;
import org.h2gis.drivers.shp.SHPWrite;
import org.h2gis.h2spatialext.CreateSpatialExtension;

/**
 * 
 * h2gis 内存空间数据库
 */
public class H2gisServer {
	private static final String ENCODING = "cp936";

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(getClass());

	private Connection connection;
	private Statement st;

	private static H2gisServer server = new H2gisServer();

	private H2gisServer() {
	}

	public static H2gisServer getInstance() {
		return server;
	}

	public void execute(String sql) {
		try {
			st.execute(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 获取h2gis连接
	public void startup() throws ClassNotFoundException, SQLException {
		// Open memory H2 table
		Class.forName("org.h2.Driver");
		connection = DriverManager.getConnection("jdbc:h2:mem:syntax", "sa", "sa");
		st = connection.createStatement();
		// Import spatial functions, domains and drivers
		// If you are using a file database, you have to do only that once.
		CreateSpatialExtension.initSpatialExtension(connection);
	}

	/**
	 * 导入shap数据:</br> CALL SHPRead(VARCHAR path, VARCHAR tableName, VARCHAR
	 * fileEncoding);</br> 创建空间索引(Spatial indices):</br> CREATE SPATIAL INDEX
	 * [index_name] ON table_name(geometry_column);</br>
	 * 
	 * @param filePath
	 *            文件路径
	 * @param tableName
	 *            创建数据表名
	 * @throws SQLException
	 */
	public void importData(String filePath, String tableName) throws SQLException {
		try {
			SHPRead.readShape(connection, filePath, tableName, ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		st.execute("CREATE SPATIAL INDEX IDX_" + tableName + " ON " + tableName + "(THE_GEOM);");
	}

	/**
	 * 导出shap数据:</br> CALL SHPWrite(VARCHAR path, VARCHAR tableName, VARCHAR
	 * fileEncoding);</br> 创建空间索引(Spatial indices):</br> CREATE SPATIAL INDEX
	 * [index_name] ON table_name(geometry_column);</br>
	 * 
	 * @param filePath
	 *            文件路径
	 * @param tableName
	 *            创建数据表名
	 * @throws SQLException
	 */
	public void outputShape(String outputFile, String tableName) {
		if (!new File(outputFile).getParentFile().exists()) {
			new File(outputFile).getParentFile().mkdirs();
		}

		try {
			SHPWrite.exportTable(connection, outputFile, tableName, "cp936");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ResultSet executeQuery(String sql) {
		try {
			return st.executeQuery(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int queryForInt(String sql) {
		ResultSet rrs = H2gisServer.getInstance().executeQuery(sql);
		try {
			if (rrs.next()) {
				return rrs.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				rrs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}
