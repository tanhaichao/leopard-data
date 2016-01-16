package io.leopard.redis.test;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import io.leopard.redis.RedisImpl;
import io.leopard.redis.util.IJedisPool;
import redis.clients.jedis.Jedis;

public class RedisH2Impl extends RedisImpl {

	// private JedisDb jedisDb;
	private Jedis jedis;

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void init() {
		// DataSource dataSource = H2Util.createDataSource("redis");
		// DatabaseScriptImpl.populate(dataSource, RedisEntity.class, JedisDb.TABLE);// 导入表结构
		// this.jedisDb = new JedisDb();
		// jedisDb.setDataSource(dataSource);

		JedisH2Impl jedisH2Impl = new JedisH2Impl();
		jedisH2Impl.setDataSource(dataSource);
		jedisH2Impl.init();
		this.jedis = jedisH2Impl;
	}

	@Override
	public Jedis getResource() {
		return jedis;
	}

	@Override
	public Double zscore(final String key, final long member) {
		return this.zscore(key, Long.toString(member));
	}

	@Override
	public String set(String key, String value, int seconds) {
		this.set(key, value);
		this.expire(key, seconds);
		return value;
	}

	@Override
	public boolean set(List<String> keyList, List<String> valueList) {
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			String value = valueList.get(i);
			this.set(key, value);
		}
		return true;
	}

	@Override
	public boolean append(List<String> keyList, List<String> valueList, int seconds) {
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			String value = valueList.get(i);
			// this.set(key, value, seconds);
			this.append(key, value, seconds);
		}
		return true;
	}

	@Override
	public String getServerInfo() {
		return "redis.transaction.impl";
	}

	// @Override
	// public String hget(String key, int field) {
	// return this.hget(key, Integer.toString(field));
	// }

	@Override
	public String hget(String key, long field) {
		return this.hget(key, Long.toString(field));
	}

	@Override
	public Long hset(String key, long field, String value) {
		Long result = this.hset(key, Long.toString(field), value);
		return result;
	}

	@Override
	public Long hdel(String key, long field) {
		return this.hdel(key, Long.toString(field));
	}

	@Override
	public Long zadd(String key, double score, long member) {
		return this.zadd(key, score, Long.toString(member));
	}

	@Override
	public Long zrem(String key, long member) {
		return this.zrem(key, Long.toString(member));
	}

	@Override
	public Set<String> zunionStoreInJava(String... sets) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<String> zunionStoreByScoreInJava(double min, double max, String... sets) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long lpushx(String key, String string) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long rpushx(String key, String string) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long setrange(String key, int offset, String value) {

		return this.setrange(key, (long) offset, value);
	}

	@Override
	public Object evalAssertSha(String arg0, String arg1) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String evalReturnSha(String arg0) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean append(String key, String value, int seconds) {
		String str = this.get(key);
		str = str == null ? "" : str;
		str += value;
		this.set(key, str, seconds);
		return true;
	}

	@Override
	public IJedisPool getJedisPool() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	// @Override
	// public Long hset(String key, int field, String value) {
	// Long result = this.hset(key, Integer.toString(field), value);
	// return result;
	// }
	@Override
	public void returnResource(Jedis jedis) {

	}

	@Override
	public void destroy() {

	}

}
