package com.autonavi.mapart.orm.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class FacilityHistoryDao extends BasicMyBatisDao {
	
	public void insertHistory(List<String> sqls) throws SQLException {
		SqlSession sqlSession = super.getSqlSession();
		Connection conn = sqlSession.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			for(String sql : sqls) {
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try{
				if( stmt != null) {
					stmt.close();
				}
			} catch(Exception e) {
				
			}
			try {
				if(conn != null) {
					conn.close();
				}
			}catch(Exception e) {
				
			}
		}
	}

}
