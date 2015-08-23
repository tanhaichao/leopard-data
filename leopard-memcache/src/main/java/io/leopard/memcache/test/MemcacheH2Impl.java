package io.leopard.memcache.test;

import io.leopard.autounit.unitdb.DatabaseScriptImpl;
import io.leopard.autounit.unitdb.Unitdb;
import io.leopard.autounit.unitdb.UnitdbH2Impl;
import io.leopard.memcache.Memcache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;

public class MemcacheH2Impl implements Memcache {

	private Unitdb unitdb;

	public MemcacheH2Impl() {

	}

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void init() {
		// DataSource dataSource = H2Util.createDataSource("autounit");
		DatabaseScriptImpl.populate(dataSource, MemcacheEntity.class, "memcache");// 导入表结构
		UnitdbH2Impl unitdb = new UnitdbH2Impl();
		unitdb.setDataSource(dataSource);
		this.unitdb = unitdb;
	}

	@Override
	public boolean remove(String key) {
		String sql = "delete from memcache where `key`=?";
		int count = unitdb.update(sql, key);
		return count > 0;
	}

	@Override
	public boolean put(String key, String value) {
		return this.put(key, value, Integer.MAX_VALUE);
	}

	@Override
	public boolean put(String key, String value, int seconds) {
		Date posttime = new Date();
		Date expire = new Date(posttime.getTime() + seconds * 1000L);// 过期时间
		try {
			String sql = "insert into memcache(`key`, `value`,`expire`,`posttime`) values(?,?,?,?)";
			this.unitdb.update(sql, key, value, expire, posttime);
		}
		catch (DuplicateKeyException e) {
			String sql = "update memcache set value=?,expire=? where `key`=?";
			this.unitdb.update(sql, value, expire, key);
		}
		return true;
	}

	@Override
	public boolean put(String key, int value) {
		return this.put(key, Integer.toString(value));
	}

	@Override
	public boolean put(String key, int value, int seconds) {
		return this.put(key, Integer.toString(value), seconds);
	}

	@Override
	public String get(String key) {
		String sql = "select value from memcache where `key`=?;";
		return this.unitdb.queryForString(sql, key);
	}

	@Override
	public List<String> mget(String[] keys) {
		List<String> list = new ArrayList<String>();
		for (String key : keys) {
			String value = this.get(key);
			list.add(value);
		}
		return list;
	}

	@Override
	public boolean flushAll() {
		String sql = "delete from memcache";
		this.unitdb.update(sql);
		return true;
	}

	@Override
	public long incr(String key) {
		return this.addOrIncr(key, 1);
	}

	@Override
	public long addOrIncr(String key, long num) {
		String value = this.get(key);
		if (value == null) {
			this.put(key, Long.toString(num));
			return num;
		}

		long tmpNum = Integer.parseInt(value) + num;
		String sql = "update memcache set value=? where `key`=?;";
		this.unitdb.update(sql, tmpNum, key);
		return tmpNum;
	}

	@Override
	public int getInt(String key) {
		String value = this.get(key);
		if (value == null) {
			return 0;
		}
		return Integer.parseInt(value);
	}

	@Override
	public boolean add(String key, String value) {
		return this.put(key, value);
	}

	@Override
	public boolean add(String key, String value, int seconds) {
		return this.put(key, value, seconds);
	}

}
