package io.leopard.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import io.leopard.jdbc.builder.InsertBuilder;
import io.leopard.jdbc.builder.ReplaceBuilder;
import io.leopard.jdbc.builder.SqlBuilder;
import io.leopard.lang.Paging;

public class JdbcWrapper implements Jdbc {

	private Jdbc jdbc;

	public Jdbc getJdbc() {
		return jdbc;
	}

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType) {
		return this.getJdbc().queryForList(sql, elementType);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, Object... params) {
		return this.getJdbc().queryForList(sql, elementType, params);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param) {
		return this.getJdbc().queryForList(sql, elementType, param);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param, int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> queryForLongs(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> queryForLongs(String sql, StatementParameter param, int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> queryForInts(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> queryForInts(String sql, StatementParameter param, int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> queryForStrings(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> queryForStrings(String sql, int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> queryForStrings(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> queryForStrings(String sql, StatementParameter param, int start, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long queryForLong(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long queryForLong(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long queryForLong(String sql, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer queryForInt(String sql) {
		// TODO Auto-generated method stub
		return null;
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
		return this.getJdbc().insertForBoolean(builder);
	}

	@Override
	public boolean insertForBoolean(ReplaceBuilder builder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateForBoolean(String sql, Object... params) {
		return this.getJdbc().updateForBoolean(sql, params);
	}

	@Override
	public boolean updateForBoolean(String sql, StatementParameter param) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateForBoolean(SqlBuilder builder) {
		return this.getJdbc().updateForBoolean(builder);
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
	public boolean insert(String tableName, Object bean) {
		// TODO Auto-generated method stub
		return false;
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
		return this.getJdbc().queryForPaging(sql, elementType);
	}

	@Override
	public <T> Paging<T> queryForPaging(String sql, Class<T> elementType, Object... params) {
		return this.getJdbc().queryForPaging(sql, elementType, params);
	}

	@Override
	public <T> Paging<T> queryForPaging(String sql, Class<T> elementType, StatementParameter param) {
		return this.getJdbc().queryForPaging(sql, elementType, param);
	}

	@Override
	public <T> Paging<T> queryForPaging(String sql, Class<T> elementType, StatementParameter param, int start, int size) {
		return this.getJdbc().queryForPaging(sql, elementType, param, start, size);
	}

}
