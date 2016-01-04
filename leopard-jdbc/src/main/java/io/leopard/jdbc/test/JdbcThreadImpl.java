package io.leopard.jdbc.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.StatementParameter;
import io.leopard.jdbc.builder.InsertBuilder;
import io.leopard.jdbc.builder.ReplaceBuilder;
import io.leopard.jdbc.builder.SqlBuilder;
import io.leopard.lang.Paging;

public class JdbcThreadImpl implements Jdbc {

	private Jdbc original;

	private Jdbc jdbcH2Impl;

	public JdbcThreadImpl(Jdbc original) {
		this.original = original;
	}

	private Jdbc getJdbc() {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer queryForInt(String sql, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date queryForDate(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date queryForDate(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryForString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryForString(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertIgnoreForBoolean(InsertBuilder builder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertIgnoreForBoolean(ReplaceBuilder builder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertIgnoreForBoolean(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertForBoolean(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertForBoolean(String sql, Object... params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long incr(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertForBoolean(InsertBuilder builder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertForBoolean(ReplaceBuilder builder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateForBoolean(String sql, Object... params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateForBoolean(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateForBoolean(SqlBuilder builder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int update(SqlBuilder builder) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String sql) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long insertForLastId(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] batchUpdate(String[] sqls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertByBean(String sql, Object bean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateByBean(String sql, Object bean) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Paging<T> queryForPaging(String sql, Class<T> elementType, StatementParameter param, int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

}
