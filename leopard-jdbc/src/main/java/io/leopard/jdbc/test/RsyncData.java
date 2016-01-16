package io.leopard.jdbc.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import io.leopard.jdbc.JdbcMysqlImpl;
import io.leopard.jdbc.JdbcUrlInfo;
import io.leopard.jdbc.ProxyDataSource;

public class RsyncData {

	// private DataSource mysqlDataSource;
	// private DataSource h2DataSource;

	private static H2DataSource h2DataSource;

	public static void registerH2DataSource(H2DataSource h2DataSource) {
		RsyncData.h2DataSource = h2DataSource;
	}

	public static void rsyncServerDataToH2() {
		JdbcUrlInfo jdbcUrlInfo = parseUrl(h2DataSource.getUrl());
		String host = jdbcUrlInfo.getHost();
		int port = jdbcUrlInfo.getPort();
		String database = jdbcUrlInfo.getDatabase();
		String driverClass = h2DataSource.getDriverClass();
		String user = h2DataSource.getUser();
		String password = h2DataSource.getPassword();
		int maxPoolSize = h2DataSource.getMaxPoolSize();

		String jdbcUrl = ProxyDataSource.getJdbcUrl(host, port, database);
		DataSource mysqlDataSource = ProxyDataSource.createDataSource(driverClass, jdbcUrl, user, password, maxPoolSize);
		JdbcMysqlImpl jdbc = new JdbcMysqlImpl();
		jdbc.setDataSource(mysqlDataSource);

		RsyncData rsyncData = new RsyncData();
		rsyncData.setMysqlDataSource(mysqlDataSource);
		rsyncData.setH2DataSource(h2DataSource);
		rsyncData.start();
	}

	protected static JdbcUrlInfo parseUrl(String url) {
		String regex = "jdbc:mysql://(.*?):([0-9]+)/([a-z0-9A-Z_]+)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		if (!m.find()) {
			throw new IllegalArgumentException("JdbcUrl[" + url + "]不符合规则.");
		}
		String host = m.group(1);
		int port = Integer.parseInt(m.group(2));
		String database = m.group(3);

		JdbcUrlInfo jdbcUrlInfo = new JdbcUrlInfo();
		jdbcUrlInfo.setDatabase(database);
		jdbcUrlInfo.setPort(port);
		jdbcUrlInfo.setHost(host);
		return jdbcUrlInfo;
	}

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
		System.out.println("rsyncTable:" + tableName);
		this.jdbcH2Impl.update("delete from " + tableName);
		List<Map<String, Object>> list = jdbcMysqlImpl.queryForMaps("select * from " + tableName);
		if (list == null || list.isEmpty()) {
			return;
		}

		StringBuilder names = new StringBuilder();
		StringBuilder values = new StringBuilder();
		for (Entry<String, Object> entry : list.get(0).entrySet()) {
			if (values.length() > 0) {
				values.append(",");
				names.append(",");
			}
			names.append(entry.getKey());
			values.append("?");
		}
		String sql = "insert into " + tableName + "(" + names.toString() + ")" + " values(" + values.toString() + ");";
		// System.out.println("sql:" + sql);

		List<Object[]> argsList = new ArrayList<Object[]>();

		for (Map<String, Object> row : list) {
			Object[] args = this.toArgs(row);
			argsList.add(args);
		}
		jdbcH2Impl.getJdbcTemplate().batchUpdate(sql, argsList);
	}

	protected Object[] toArgs(Map<String, Object> row) {
		Collection<Object> values = row.values();
		Object[] args = new Object[values.size()];
		values.toArray(args);
		return args;
	}

	// protected void insert(String tableName, Map<String, Object> row) {
	// List<Object> valueList = new ArrayList<Object>();
	//
	// StringBuilder names = new StringBuilder();
	// StringBuilder values = new StringBuilder();
	// for (Entry<String, Object> entry : row.entrySet()) {
	// if (values.length() > 0) {
	// values.append(",");
	// names.append(",");
	// }
	// names.append(entry.getKey());
	// values.append("?");
	// valueList.add(entry.getValue());
	// }
	// String sql = "insert into " + tableName + "(" + names.toString() + ")" + " values(" + values.toString() + ");";
	// // System.out.println("sql:" + sql);
	// Object[] args = new Object[valueList.size()];
	// valueList.toArray(args);
	//
	//
	//
	// jdbcH2Impl.getJdbcTemplate().update(sql, args);
	// }

	protected List<String> listAllTables() {
		List<Map<String, Object>> list = jdbcMysqlImpl.queryForMaps("show tables");
		System.out.println("listAllTables:" + list);
		// Json.printList(list, "tables");
		List<String> tableNameList = new ArrayList<String>();
		for (Map<String, Object> map : list) {
			String table = (String) map.values().iterator().next();
			tableNameList.add(table);
		}
		return tableNameList;
	}
}
