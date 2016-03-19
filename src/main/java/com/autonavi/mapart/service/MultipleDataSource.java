package com.autonavi.mapart.service;

import java.sql.SQLFeatureNotSupportedException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultipleDataSource extends AbstractRoutingDataSource {
	
	private static Logger log = Logger.getLogger("MultipleDataSource");
	 private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

	    public static void setDataSourceKey(String dataSource) {
	    	log.debug("----连接的数据库为："+dataSource);
	        dataSourceKey.set(dataSource);
	    }

		@Override
		public java.util.logging.Logger getParentLogger()
				throws SQLFeatureNotSupportedException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object determineCurrentLookupKey() {
			 return dataSourceKey.get();
		}

		
}
