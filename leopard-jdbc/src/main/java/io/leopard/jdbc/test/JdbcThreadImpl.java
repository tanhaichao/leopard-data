package io.leopard.jdbc.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import io.leopard.autounit.unitdb.ConnectionContext;
import io.leopard.autounit.unitdb.H2Util;
import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.StatementParameter;
import io.leopard.jdbc.builder.InsertBuilder;
import io.leopard.jdbc.builder.ReplaceBuilder;
import io.leopard.jdbc.builder.SqlBuilder;
import io.leopard.lang.Paging;

public class JdbcThreadImpl implements Jdbc {

	private Jdbc original;

	private JdbcH2Impl jdbcH2Impl;

	public JdbcThreadImpl(Jdbc original) {
		this.original = original;

	}

	protected Jdbc getJdbcH2Impl() {
		if (jdbcH2Impl != null) {
			return this.jdbcH2Impl;
		}
		jdbcH2Impl = new JdbcH2Impl();
		DataSource dataSource = H2Util.createDataSource("jdbc-web");
		DataSource dataSource2 = ConnectionContext.register(dataSource);
		jdbcH2Impl.setDataSource(dataSource2);

		List<String> tableList = this.showTables(this.original);
		for (String tableName : tableList) {
			String sql = this.showCreateTable(original, tableName);

			jdbcH2Impl.update("drop table if exists " + tableName);
			this.populate(dataSource2, tableName, sql);
		}
		return jdbcH2Impl;
	}

	private void populate(DataSource dataSource, String tableName, String sql) {

		sql = H2SqlUtil.filter(sql);
		sql = sql.replaceAll("\n", "");
		sql = sql.replaceAll("\r", "");

		// System.out.println(sql);
		// System.err.println("开始导入表结构:" + tableName);
		Resource scripts = new ByteArrayResource(sql.getBytes());
		DatabasePopulator populator = new ResourceDatabasePopulator(scripts);
		DatabasePopulatorUtils.execute(populator, dataSource);
	}

	private String showCreateTable(Jdbc jdbc, String tableName) {
		List<Map<String, Object>> list = jdbc.queryForMaps("show create table " + tableName);
		for (Map<String, Object> map : list) {
			String sql = (String) map.get("Create Table");
			return sql;
		}
		return null;
	}

	private List<String> showTables(Jdbc jdbc) {
		List<Map<String, Object>> list = jdbc.queryForMaps("SHOW TABLES;");
		List<String> tableList = new ArrayList<String>();
		for (Map<String, Object> map : list) {
			String tableName = (String) map.values().iterator().next();
			tableList.add(tableName);
		}
		return tableList;
	}

	private Jdbc getJdbc() {
		// new Exception("getJdbc").printStackTrace();
		if (true) {
			return this.getJdbcH2Impl();
		}
		return this.original;
	}

	@Override
	public DataSource getDataSource() {
		return getJdbc().getDataSource();
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return getJdbc().getJdbcTemplate();
	}

	@Override
	public String printSQL(Log logger, String sql, StatementParameter param) {
		return getJdbc().printSQL(logger, sql, param);
	}

	@Override
	public String getSQL(String sql, StatementParameter param) {
		return getJdbc().getSQL(sql, param);
	}

	@Override
	public boolean exist(String sql) {
		return getJdbc().exist(sql);
	}

	@Override
	public boolean exist(String sql, StatementParameter param) {
		return getJdbc().exist(sql, param);
	}

	@Override
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter setter) {
		return getJdbc().batchUpdate(sql, setter);
	}

	@Override
	public <T> T query(String sql, Class<T> elementType) {
		return getJdbc().query(sql, elementType);
	}

	@Override
	public <T> T query(String sql, Class<T> elementType, StatementParameter param) {
		return getJdbc().query(sql, elementType, param);
	}

	@Override
	public <T> T query(String sql, Class<T> elementType, Object... params) {
		return getJdbc().query(sql, elementType, params);
	}

	@Override
	public List<Map<String, Object>> queryForMaps(String sql) {
		return getJdbc().queryForMaps(sql);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType) {
		return getJdbc().queryForList(sql, elementType);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, Object... params) {
		return getJdbc().queryForList(sql, elementType, params);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param) {
		return getJdbc().queryForList(sql, elementType, param);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param, int start, int size) {
		return getJdbc().queryForList(sql, elementType, param, start, size);
	}

	@Override
	public List<Long> queryForLongs(String sql, StatementParameter param) {
		return getJdbc().queryForLongs(sql, param);
	}

	@Override
	public List<Long> queryForLongs(String sql, StatementParameter param, int start, int size) {
		return getJdbc().queryForLongs(sql, param, start, size);
	}

	@Override
	public List<Integer> queryForInts(String sql, StatementParameter param) {
		return getJdbc().queryForInts(sql, param);
	}

	@Override
	public List<Integer> queryForInts(String sql, StatementParameter param, int start, int size) {
		return getJdbc().queryForInts(sql, param, start, size);
	}

	@Override
	public List<String> queryForStrings(String sql) {
		return getJdbc().queryForStrings(sql);
	}

	@Override
	public List<String> queryForStrings(String sql, int start, int size) {
		return getJdbc().queryForStrings(sql, start, size);
	}

	@Override
	public List<String> queryForStrings(String sql, StatementParameter param) {
		return getJdbc().queryForStrings(sql, param);
	}

	@Override
	public List<String> queryForStrings(String sql, StatementParameter param, int start, int size) {
		return getJdbc().queryForStrings(sql, param, start, size);
	}

	@Override
	public Long queryForLong(String sql) {
		return getJdbc().queryForLong(sql);
	}

	@Override
	public Long queryForLong(String sql, StatementParameter param) {
		return getJdbc().queryForLong(sql, param);
	}

	@Override
	public Long queryForLong(String sql, Object... params) {
		return getJdbc().queryForLong(sql, params);
	}

	@Override
	public Integer queryForInt(String sql) {
		return getJdbc().queryForInt(sql);
	}

	@Override
	public Integer queryForInt(String sql, StatementParameter param) {
		return getJdbc().queryForInt(sql, param);
	}

	@Override
	public Integer queryForInt(String sql, Object... params) {
		return getJdbc().queryForInt(sql, params);
	}

	@Override
	public Date queryForDate(String sql) {
		return getJdbc().queryForDate(sql);
	}

	@Override
	public Date queryForDate(String sql, StatementParameter param) {
		return getJdbc().queryForDate(sql, param);
	}

	@Override
	public String queryForString(String sql) {
		return getJdbc().queryForString(sql);
	}

	@Override
	public String queryForString(String sql, StatementParameter param) {
		return getJdbc().queryForString(sql, param);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean insertIgnoreForBoolean(InsertBuilder builder) {
		return getJdbc().insertIgnoreForBoolean(builder);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean insertIgnoreForBoolean(ReplaceBuilder builder) {
		return getJdbc().insertIgnoreForBoolean(builder);
	}

	@Override
	public boolean insertIgnoreForBoolean(String sql, StatementParameter param) {
		return getJdbc().insertForBoolean(sql, param);
	}

	@Override
	public boolean insertForBoolean(String sql, StatementParameter param) {
		return getJdbc().insertForBoolean(sql, param);
	}

	@Override
	public boolean insertForBoolean(String sql, Object... params) {
		return getJdbc().insertForBoolean(sql, params);
	}

	@Override
	public Long incr(String sql, StatementParameter param) {
		return getJdbc().incr(sql, param);
	}

	@Override
	public boolean insertForBoolean(InsertBuilder builder) {
		return getJdbc().insertForBoolean(builder);
	}

	@Override
	public boolean insertForBoolean(ReplaceBuilder builder) {
		return getJdbc().insertForBoolean(builder);
	}

	@Override
	public boolean updateForBoolean(String sql, Object... params) {
		return getJdbc().updateForBoolean(sql, params);
	}

	@Override
	public boolean updateForBoolean(String sql, StatementParameter param) {
		return getJdbc().updateForBoolean(sql, param);
	}

	@Override
	public boolean updateForBoolean(SqlBuilder builder) {
		return getJdbc().updateForBoolean(builder);
	}

	@Override
	public int update(SqlBuilder builder) {
		return getJdbc().update(builder);
	}

	@Override
	public int update(String sql, StatementParameter param) {
		return getJdbc().update(sql, param);
	}

	@Override
	public int update(String sql) {
		return getJdbc().update(sql);
	}

	@Override
	public long insertForLastId(String sql, StatementParameter param) {
		return getJdbc().insertForLastId(sql, param);
	}

	@Override
	public int[] batchUpdate(String[] sqls) {
		return getJdbc().batchUpdate(sqls);
	}

	@Override
	public boolean insertByBean(String sql, Object bean) {
		return getJdbc().insertByBean(sql, bean);
	}

	@Override
	public boolean updateByBean(String sql, Object bean) {
		return getJdbc().updateByBean(sql, bean);
	}

	@Override
	public <T> Paging<T> queryForPaging(String sql, Class<T> elementType) {
		return getJdbc().queryForPaging(sql, elementType);
	}

	@Override
	public <T> Paging<T> queryForPaging(String sql, Class<T> elementType, Object... params) {
		return getJdbc().queryForPaging(sql, elementType, params);
	}

	@Override
	public <T> Paging<T> queryForPaging(String sql, Class<T> elementType, StatementParameter param) {
		return getJdbc().queryForPaging(sql, elementType, param);
	}

	@Override
	public <T> Paging<T> queryForPaging(String sql, Class<T> elementType, StatementParameter param, int start, int size) {
		return getJdbc().queryForPaging(sql, elementType, param, start, size);
	}

	@Override
	public boolean insert(String tableName, Object bean) {
		return getJdbc().insert(tableName, bean);
	}

}
