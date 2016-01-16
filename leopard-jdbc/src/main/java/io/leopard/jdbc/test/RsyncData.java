package io.leopard.jdbc.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.JdbcMysqlImpl;

public class RsyncData {

	// private DataSource mysqlDataSource;
	// private DataSource h2DataSource;

	private JdbcMysqlImpl jdbcMysqlImpl;
	private JdbcH2Impl jdbcH2Impl;

	public void setMysqlDataSource(DataSource mysqlDataSource) {
		// this.mysqlDataSource = mysqlDataSource;
		jdbcMysqlImpl = new JdbcMysqlImpl();
		jdbcMysqlImpl.setDataSource(mysqlDataSource);
	}

	public void setH2DataSource(DataSource h2DataSource) {
		// this.h2DataSource = h2DataSource;
		jdbcH2Impl = new JdbcH2Impl();
		jdbcH2Impl.setDataSource(h2DataSource);
	}

	public void start() {
		List<String> tableNameList = this.listAllTables();
		System.out.println("tableNameList:" + tableNameList);
		for (String tableName : tableNameList) {
			this.rsyncTable(tableName);
		}
	}

	protected void rsyncTable(String tableName) {
		String sql = "select * from " + tableName;
		List<Map<String, Object>> list = jdbcMysqlImpl.queryForMaps(sql);
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
	}

	protected List<String> listAllTables() {
		List<Map<String, Object>> list = jdbcMysqlImpl.queryForMaps("show tables");
		// Json.printList(list, "tables");
		List<String> tableNameList = new ArrayList<String>();
		for (Map<String, Object> map : list) {
			String table = (String) map.get("Tables_in_example");
			tableNameList.add(table);
		}
		return tableNameList;
	}
}
