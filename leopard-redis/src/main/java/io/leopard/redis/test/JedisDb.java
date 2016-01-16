package io.leopard.redis.test;

import io.leopard.autounit.unitdb.UnitdbH2Impl;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

import redis.clients.jedis.Tuple;

public class JedisDb extends UnitdbH2Impl {

	protected static final String TABLE = "redis";
	protected static final int SECONDS = 60 * 60 * 24 * 30;
	protected static final long LONG_MAX_VALUE = Integer.MAX_VALUE - 10L;

	// protected String toNum(String fieldName) {
	//
	// return "cast(" + fieldName + " as double)";
	// }

	protected String get(String key) {
		String sql = "select value from " + TABLE + " where `key`=?;";

		String value = super.queryForString(sql, key);
		// System.out.println("get sql:" + sql + " key:" + key + " value:" + value);
		return value;
	}

	protected int count(String key) {
		String sql = "select count(*) from " + TABLE + " where `key`=?;";
		return super.queryForInt(sql, key);
	}

	protected String getScoreString() {
		return "CAST(score as Double)";
	}

	protected double getScore(String key, boolean asc) {
		String order = asc ? "asc" : "desc";
		String sql = "select " + getScoreString() + " from " + TABLE + " where `key`=? order by " + this.getScoreString() + " " + order + " limit 1;";
		Integer count = super.queryForInt(sql, key);
		// super.printSQL(sql, param);
		if (count == null) {
			return 0;
		}
		return count;
	}

	protected String get(String key, String member) {
		RedisEntity bean = this.getBean(key, member);
		if (bean == null) {
			return null;
		}
		return bean.getValue();
	}

	public RedisEntity getBean(String key, String member) {
		Assert.hasLength(key, "参数key不能为空.");
		Assert.hasLength(member, "参数member不能为空.");
		String sql = "select * from " + TABLE + " where `key`=? and value=?;";
		RedisEntity bean = super.query(sql, RedisEntity.class, key, member);
		return bean;
	}

	protected String getByField(String key, String field) {
		String sql = "select value from " + TABLE + " where `key`=? and field=?;";
		String value = super.queryForString(sql, key, field);
		// unitdb.printSQL(sql, param);
		return value;
	}

	public boolean exist(String key) {
		String value = this.get(key);
		return (value != null);
	}

	protected boolean exist(String key, String member) {
		String value = this.get(key, member);
		return (value != null);
	}

	protected synchronized boolean existByField(String key, String field) {
		String value = this.getByField(key, field);
		return (value != null);
	}

	protected synchronized boolean delete(String key) {
		String sql = "delete from " + TABLE + " where `key`=?;";
		int count = super.update(sql, key);
		return count > 0;
	}

	protected synchronized boolean deletes(String key, String... members) {
		boolean success = true;
		for (String member : members) {
			success = success & this.delete(key, member);
		}
		return success;
	}

	protected synchronized boolean delete(String key, String member) {
		Assert.hasLength(key, "参数key不能为空.");
		Assert.hasLength(member, "参数member不能为空.");
		String sql = "delete from " + TABLE + " where `key`=? and value=?;";
		int count = super.update(sql, key, member);
		return count > 0;
	}

	protected synchronized boolean deleteByFields(String key, String... fields) {
		if (fields.length == 0) {
			return false;
		}
		boolean success = true;
		for (String field : fields) {
			success = success & this.deleteByField(key, field);
		}
		return success;
	}

	protected synchronized boolean deleteByField(String key, String field) {
		String sql = "delete from " + TABLE + " where `key`=? and field=?;";
		// System.out.println("deleteByField sql:" + sql + " key:" + key + " field:" + field);
		int count = super.update(sql, key, field);
		return count > 0;
	}

	protected synchronized boolean insert(String key, String value) {
		return this.insert(key, value, SECONDS);
	}

	protected synchronized boolean insert(String key, String value, int seconds) {
		long millis = System.currentTimeMillis() + (seconds * 1000L);
		Date expiry = new Date(millis);
		String field = "";
		return this.insert(key, field, value, expiry);
	}

	protected synchronized boolean inserts(String key, double score, String... values) {
		boolean success = false;
		for (String value : values) {
			success = success & this.insert(key, score, value);
		}
		return success;
	}

	protected synchronized boolean insert(String key, double score, String value) {
		int seconds = 60 * 60 * 24 * 30;
		long millis = System.currentTimeMillis() + (seconds * 1000L);
		Date expiry = new Date(millis);
		String field = Double.toString(score);
		return this.insert(key, field, value, expiry);
	}

	protected synchronized boolean insert(String key, String field, String value) {
		int seconds = 60 * 60 * 24 * 30;
		long millis = System.currentTimeMillis() + (seconds * 1000L);
		Date expiry = new Date(millis);
		return this.insert(key, field, value, expiry);
	}

	protected synchronized boolean insert(String key, String field, String value, Date expire) {
		Assert.hasLength(key, "参数key不能为空.");
		Assert.notNull(value, "参数value不能为null.");
		Assert.notNull(expire, "参数expire不能为null.");
		String sql = "insert into " + TABLE + "(`key`, field, value, expire) values(?,?,?,?);";

		// System.out.println("insert sql:" + sql + " key:" + key + " field:" + field + " value:" + value);
		int count = super.update(sql, key, field, value, expire);
		return count > 0;
	}

	protected Long boolToLong(boolean bool) {
		if (bool) {
			return 1L;
		}
		else {
			return 0L;
		}
	}

	protected Long index(Set<String> set, String member) {
		if (set == null) {
			return null;
		}
		long index = 0;
		for (String str : set) {
			if (str.equals(member)) {
				return index;
			}
			index++;
		}
		return null;
	}

	public Set<String> tupleToString(Set<Tuple> set) {
		Set<String> result = new LinkedHashSet<String>();
		for (Tuple tuple : set) {
			String element = tuple.getElement();
			result.add(element);
		}
		return result;
	}

	public Set<Double> tupleToScores(Set<Tuple> set) {
		Set<Double> result = new LinkedHashSet<Double>();
		for (Tuple tuple : set) {
			Double score = tuple.getScore();
			result.add(score);
		}
		return result;
	}

	protected Set<String> toSet(List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		Set<String> set = new LinkedHashSet<String>();
		for (String element : list) {
			set.add(element);
		}
		if (set.size() != list.size()) {
			throw new RuntimeException("list转换成set之后，size不一样了[" + set.size() + "." + list.size() + "].");
		}
		return set;
	}

	protected Set<Tuple> toTupleSet(List<RedisEntity> list) {
		if (list == null) {
			return null;
		}
		Set<Tuple> set = new LinkedHashSet<Tuple>();
		for (RedisEntity bean : list) {
			String element = bean.getValue();
			Double score = bean.getScore();
			Tuple tuple = new Tuple(element, score);
			set.add(tuple);
		}
		return set;
	}

}
