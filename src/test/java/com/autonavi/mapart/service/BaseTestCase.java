package com.autonavi.mapart.service;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DBTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-context-test.xml")
public class BaseTestCase extends DBTestCase  {

	public Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IDatabaseTester databaseTester;
	Connection conn;
	private IDatabaseConnection dsConn;
	Statement stmt = null;
	
	@Before
	public void setup() {
		try {
			dsConn = databaseTester.getConnection();
			conn = dsConn.getConnection();
//			DatabaseOperation.CLEAN_INSERT.execute(dsConn, getDataSet());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		logger.debug("close db connection.");
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (dsConn != null) {
				dsConn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if (conn != null) {
				conn.close();
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet getResultSet(String sql) {
		
		try {
			stmt = conn.createStatement();
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			
		}
	}
	
	public int getCount(String sql) {
		ResultSet rs = getResultSet(sql);
		try {
			if( rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	protected  IDataSet getDataSet() throws Exception {
		IDataSet flatXmlDataSet = new XmlDataSet(new FileInputStream(
				"src/test/resources/data/dataset.xml"));
		return flatXmlDataSet;
	}

}
