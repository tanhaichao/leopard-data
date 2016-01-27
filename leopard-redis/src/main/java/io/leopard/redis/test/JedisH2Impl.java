package io.leopard.redis.test;

import io.leopard.autounit.unitdb.DatabaseScriptImpl;
import io.leopard.redis.util.RedisUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.util.Assert;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

public class JedisH2Impl extends Jedis {

	public JedisH2Impl() {
		super("127.0.0.1", 16379);
	}

	
	private JedisDb jedisDb;

	public JedisDb getJedisDb() {
		return jedisDb;
	}

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void init() {
		// DataSource dataSource = H2Util.createDataSource("redis");
		DatabaseScriptImpl.populate(dataSource, RedisEntity.class, JedisDb.TABLE);// 导入表结构
		
		this.jedisDb = new JedisDb();
		jedisDb.setDataSource(dataSource);
	}

	@Override
	public String set(String key, String value) {
		int seconds = 60 * 60 * 24 * 30;
		return this.setex(key, seconds, value);
	}

	@Override
	public String get(String key) {
		return jedisDb.get(key);
	}

	@Override
	public Boolean exists(String key) {
		return jedisDb.exist(key);
	}

	@Override
	public String type(String key) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public Long expire(String key, int seconds) {
		long millis = System.currentTimeMillis() + (seconds * 1000L);
		Date expire = new Date(millis);
		String sql = "update " + JedisDb.TABLE + " set expire=? where `key`=?;";
		jedisDb.update(sql, expire, key);
		return 1L;
	}

	@Override
	public Long expireAt(String key, long unixTime) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long ttl(String key) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public Boolean setbit(String key, long offset, boolean value) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public Boolean getbit(String key, long offset) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public Long setrange(String key, long offset, String value) {
		// System.out.println("setrange key:" + key + " offset:" + offset +
		// " value:" + value);
		String oldValue = this.get(key);
		oldValue = oldValue == null ? "" : oldValue;
		StringBuilder sb = new StringBuilder(oldValue);

		if (true) {
			int diff = (int) (offset - sb.length());
			for (int i = 0; i < diff; i++) {
				sb.append((char) 2);
			}
		}
		if (true) {
			int start = (int) offset;
			int end = (int) (offset + value.length());
			sb.replace(start, end, value);
		}

		this.set(key, sb.toString());
		return (long) sb.length();
	}

	@Override
	public String getrange(String key, long startOffset, long endOffset) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public String getSet(String key, String value) {
		String oldValue = this.get(key);
		this.set(key, value);
		return oldValue;
	}

	@Override
	public Long setnx(String key, String value) {
		if (!jedisDb.exist(key)) {
			jedisDb.insert(key, value, JedisDb.SECONDS);
			return 1L;
		}
		return 0L;
	}

	@Override
	public synchronized String setex(String key, int seconds, String value) {
		// if (!this.exist(key)) {
		jedisDb.delete(key);
		jedisDb.insert(key, value, seconds);
		// }
		return value;
	}

	@Override
	public Long decrBy(String key, long integer) {
		String value = this.get(key);
		long num = value == null ? 0 : Long.parseLong(value);
		num = num - integer;
		jedisDb.delete(key);
		jedisDb.insert(key, Long.toString(num));
		return num;
	}

	@Override
	public Long decr(String key) {
		return this.decrBy(key, 1);
	}

	@Override
	public Long incrBy(String key, long integer) {
		String value = this.get(key);
		long num = value == null ? 0 : Long.parseLong(value);
		num = num + integer;
		jedisDb.delete(key);
		jedisDb.insert(key, Long.toString(num));
		return num;
	}

	@Override
	public Long incr(String key) {
		return this.incrBy(key, 1);
	}

	@Override
	public Long append(String key, String value) {
		String str = this.get(key);
		str = str == null ? "" : str;
		str += value;
		this.set(key, str);
		return (long) value.length();// 字符
	}

	@Override
	public String substr(String key, int start, int end) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public synchronized Long hset(String key, String field, String value) {
		boolean exist = jedisDb.existByField(key, field);
		if (exist) {
			jedisDb.deleteByField(key, field);
		}
		jedisDb.insert(key, field, value);
		System.err.println("exist:" + exist);
		return jedisDb.boolToLong(!exist);
	}

	@Override
	public String hget(String key, String field) {
		// System.out.println("hget key:" + key + " field:" + field);
		String value = jedisDb.getByField(key, field);
		return value;
	}

	@Override
	public Long hsetnx(String key, String field, String value) {
		if (jedisDb.existByField(key, field)) {
			return 0L;
		}
		jedisDb.insert(key, field, value);
		return 1L;
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		Iterator<Entry<String, String>> iterator = hash.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String field = entry.getKey();
			String value = entry.getValue();
			this.hset(key, field, value);
		}
		return "OK";
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		List<String> list = new ArrayList<String>();
		for (String field : fields) {
			String value = this.hget(key, field);
			list.add(value);
		}
		return list;
	}

	@Override
	public Long hincrBy(String key, String field, long value) {
		String oldValue = this.hget(key, field);
		// long num = NumberUtils.toLong(oldValue);
		long num = oldValue == null ? 0 : Long.parseLong(oldValue);
		num = num + value;
		this.hset(key, field, Long.toString(num));
		// System.out.println(" oldValue:" + oldValue + " num:" + num);
		return num;
	}

	@Override
	public Boolean hexists(String key, String field) {
		return jedisDb.existByField(key, field);
	}

	@Override
	public Long hdel(String key, String... fields) {
		boolean success = jedisDb.deleteByFields(key, fields);
		return jedisDb.boolToLong(success);
	}

	@Override
	public Long hlen(String key) {
		Set<String> set = this.hkeys(key);
		return set == null ? 0L : set.size();
	}

	@Override
	public Set<String> hkeys(String key) {
		String sql = "select field from " + JedisDb.TABLE + " where `key`=?";
		List<String> list = jedisDb.queryForStrings(sql, key);
		return jedisDb.toSet(list);
	}

	@Override
	public List<String> hvals(String key) {
		String sql = "select value from " + JedisDb.TABLE + " where `key`=?";
		List<String> list = jedisDb.queryForStrings(sql, key);
		return list;
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		String sql = "select score, value from " + JedisDb.TABLE + " where `key`=?";
		List<RedisEntity> list = jedisDb.queryForList(sql, RedisEntity.class, key);
		if (list == null) {
			return null;
		}
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (RedisEntity bean : list) {
			String field = bean.getField();
			String value = bean.getValue();
			result.put(field, value);
		}
		return result;
	}

	@Override
	public Long rpush(String key, String... strings) {
		long score = (long) jedisDb.getScore(key, false);
		if (score <= 0) {
			score = 100000 / 2L;
		}
		score++;
		jedisDb.inserts(key, score, strings);
		return (long) jedisDb.count(key);
	}

	@Override
	public Long lpush(String key, String... strings) {
		long score = (long) jedisDb.getScore(key, true);
		if (score <= 0) {
			score = 100000 / 2L;
		}
		score--;
		jedisDb.inserts(key, score, strings);
		return (long) jedisDb.count(key);
	}

	@Override
	public Long llen(String key) {
		int count = jedisDb.count(key);
		return (long) count;
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		if (end == -1) {
			end = JedisDb.LONG_MAX_VALUE;
		}
		long size = end - start + 1;

		if (size <= 0) {
			throw new IllegalArgumentException("size不能小于0[" + size + "." + start + "." + end + "].");
		}
		String sql = "select value from " + JedisDb.TABLE + " where `key`=? order by " + jedisDb.getScoreString() + " asc limit ?,?;";
		List<String> list = jedisDb.queryForStrings(sql, key, start, size);
		return list;
	}

	@Override
	public String ltrim(String key, long start, long end) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public String lindex(String key, long index) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public String lset(String key, long index, String value) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public Long lrem(String key, long count, String value) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public String lpop(String key) {
		Set<Tuple> set = this.zrangeWithScores(key, 0, 0);
		if (set == null || set.isEmpty()) {
			return null;
		}
		Tuple tuple = set.iterator().next();
		String field = Double.toString(tuple.getScore());
		jedisDb.deleteByField(key, field);

		String element = tuple.getElement();
		return element;
	}

	@Override
	public String rpop(String key) {
		Set<Tuple> set = this.zrevrangeWithScores(key, 0, 0);
		if (set == null || set.isEmpty()) {
			return null;
		}
		Tuple tuple = set.iterator().next();
		String field = Double.toString(tuple.getScore());
		jedisDb.deleteByField(key, field);

		String element = tuple.getElement();
		return element;
	}

	@Override
	public Long sadd(String key, String... members) {
		double score = jedisDb.getScore(key, false);// 随便用的
		// System.out.println("key:" + key + " score:" + score);
		if (score < 0) {
			score = 0;
		}
		score++;
		long count = 0;
		for (String member : members) {
			long num = this.zadd(key, score, member);
			count += num;
		}
		return count;
	}

	@Override
	public Set<String> smembers(String key) {
		return this.zrevrange(key, 0, Integer.MAX_VALUE);
	}

	@Override
	public Long srem(String key, String... members) {
		return this.zrem(key, members);
	}

	@Override
	public String spop(String key) {
		Set<Tuple> set = this.zrevrangeWithScores(key, 0, -1);
		if (set == null || set.isEmpty()) {
			return null;
		}
		// System.out.println("set:" + set);
		Tuple tuple = set.iterator().next();
		String field = Double.toString(tuple.getScore());
		jedisDb.deleteByField(key, field);

		String element = tuple.getElement();
		return element;
	}

	@Override
	public Long scard(String key) {
		return this.zcard(key);
	}

	@Override
	public Boolean sismember(String key, String member) {
		RedisEntity bean = jedisDb.getBean(key, member);
		return (bean != null);
	}

	@Override
	public String srandmember(String key) {
		Assert.hasLength(key, "参数key不能为空.");
		String sql = "select * from " + JedisDb.TABLE + " where `key`=? order by rand() limit 1;";
		RedisEntity bean = jedisDb.query(sql, RedisEntity.class, key);
		if (bean == null) {
			return null;
		}
		return bean.getValue();
	}

	// @Override
	public List<String> srandmember(String key, int count) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public synchronized Long zadd(String key, double score, String member) {
		// System.err.println("zadd " + key + " " + score + " " + member);
		jedisDb.delete(key, member);
		jedisDb.insert(key, score, member);
		return 1L;
	}

	@Override
	public Set<String> zrange(String key, long start, long end) {
		Set<Tuple> set = this.zrangeWithScores(key, start, end);
		return jedisDb.tupleToString(set);

		// if (end == -1) {
		// end = Integer.MAX_VALUE;
		// }
		// int size = end - start;
		//
		// AssertUtil.assertTrue(size > 0, "size不能小于0[" + size + "." + start +
		// "." + end + "].");
		// String sql = "select value from " + TABLE +
		// " where `key`=? order by score+0 asc limit ?,?;";
		// StatementParameter param = new StatementParameter();
		// param.setString(key);
		// param.setInt(start);
		// param.setInt(size);
		//
		// super.printSQL(sql, param);
		// List<String> list = super.queryForStrings(sql, param);
		// return this.toSet(list);
	}

	@Override
	public Long zrem(String key, String... members) {
		boolean success = jedisDb.deletes(key, members);
		// System.out.println("success:" + success);
		return jedisDb.boolToLong(success);
	}

	@Override
	public synchronized Double zincrby(String key, double score, String member) {
		RedisEntity bean = jedisDb.getBean(key, member);
		double totalScore = score;
		if (bean != null) {
			totalScore += bean.getScore();
		}
		this.zadd(key, totalScore, member);
		return totalScore;
	}

	@Override
	public Long zrank(String key, String member) {
		Set<String> set = this.zrange(key, 0, JedisDb.LONG_MAX_VALUE);
		return jedisDb.index(set, member);
	}

	@Override
	public Long zrevrank(String key, String member) {
		Set<String> set = this.zrevrange(key, 0, JedisDb.LONG_MAX_VALUE);
		return jedisDb.index(set, member);
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		Set<Tuple> set = this.zrevrangeWithScores(key, start, end);
		return jedisDb.tupleToString(set);

		// int size = end - start;
		// String sql = "select value from " + TABLE +
		// " where `key`=? order by score+0 desc limit ?,?;";
		// StatementParameter param = new StatementParameter();
		// param.setString(key);
		// param.setInt(start);
		// param.setInt(size);
		// List<String> list = super.queryForStrings(sql, param);
		// return this.toSet(list);
	}

	@Override
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		Assert.hasLength(key, "参数key不能为空.");
		if (end == -1) {
			end = JedisDb.LONG_MAX_VALUE;
		}
		long size = end - start;
		if (size < Long.MAX_VALUE) {
			size++;
		}

		if (size <= 0) {
			throw new IllegalArgumentException("size不能小于0[" + size + "." + start + "." + end + "].");
		}
		String sql = "select * from " + JedisDb.TABLE + " where `key`=? order by " + jedisDb.getScoreString() + " asc limit ?,?;";
		List<RedisEntity> list = jedisDb.queryForList(sql, RedisEntity.class, key, start, size);
		return jedisDb.toTupleSet(list);
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		if (end == -1 || end > JedisDb.LONG_MAX_VALUE) {
			end = JedisDb.LONG_MAX_VALUE;
		}
		long size = end - start;
		if (size < Long.MAX_VALUE) {
			size++;
		}
		String sql = "select * from " + JedisDb.TABLE + " where `key`=? order by " + jedisDb.getScoreString() + " desc limit ?,?;";
		List<RedisEntity> list = jedisDb.queryForList(sql, RedisEntity.class, key, start, size);
		return jedisDb.toTupleSet(list);
	}

	@Override
	public Long zcard(String key) {
		Set<String> set = this.zrange(key, 0, JedisDb.LONG_MAX_VALUE);
		return (long) set.size();
	}

	@Override
	public Double zscore(String key, String member) {
		RedisEntity bean = jedisDb.getBean(key, member);
		if (bean == null) {
			return null;
		}
		else {
			return bean.getScore();
		}
	}

	@Override
	public List<String> sort(String key) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public List<String> sort(String key, SortingParams sortingParameters) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public Long zcount(String key, double min, double max) {
		Set<String> set = this.zrangeByScore(key, min, max);
		return (long) set.size();
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		return this.zrangeByScore(key, min, max, 0, Integer.MAX_VALUE);

	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		return this.zrevrangeByScore(key, max, min, 0, Integer.MAX_VALUE);
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<Tuple> set = this.zrangeByScoreWithScores(key, min, max, offset, count);
		return jedisDb.tupleToString(set);
		// if (max == -1) {
		// max = Double.MAX_VALUE;
		// }
		// String sql = "select value from " + TABLE +
		// " where `key`=? and score+0>=? and score+0<=? order by score+0 asc limit ?,?";
		//
		// StatementParameter param = new StatementParameter();
		// param.setString(key);
		// param.setDouble(min);
		// param.setDouble(max);
		// param.setInt(offset);
		// param.setInt(count);
		// super.printSQL(sql, param);
		// List<String> list = super.queryForStrings(sql, param);
		// super.printSQL(sql, param);
		// return this.toSet(list);

	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		String sql = "select value from " + JedisDb.TABLE + " where `key`=? and " + jedisDb.getScoreString() + ">=? and " + jedisDb.getScoreString() + "<=? order by " + jedisDb.getScoreString()
				+ " desc limit ?,?";
		List<String> list = jedisDb.queryForStrings(sql, key, min, max, offset, count);
		return jedisDb.toSet(list);
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		int offset = 0;
		int count = Integer.MAX_VALUE;
		return this.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		if (max == -1) {
			max = Double.MAX_VALUE;
		}
		String sql = "select * from " + JedisDb.TABLE + " where `key`=? and " + jedisDb.getScoreString() + ">=? and " + jedisDb.getScoreString() + "<=? order by " + jedisDb.getScoreString()
				+ " asc limit ?,?";
		List<RedisEntity> list = jedisDb.queryForList(sql, RedisEntity.class, key, min, max, offset, count);
		return jedisDb.toTupleSet(list);
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long zremrangeByRank(String key, long start, long end) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		Set<String> set = this.zrangeByScore(key, start, end);
		// System.out.println("set:" + set + " start:" + start + " end:" + end);
		if (set == null || set.isEmpty()) {
			return new Long(0);
		}
		for (String member : set) {
			jedisDb.delete(key, member);
		}
		return new Long(set.size());
	}

	@Override
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String rename(String oldkey, String newkey) {
		this.del(newkey);
		String sql = "update " + JedisDb.TABLE + " set key=? where `key`=?;";
		int count = jedisDb.update(sql, newkey, oldkey);
		return count > 0 ? "OK" : "";
	}

	@Override
	public Transaction multi() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String flushDB() {
		String sql = "delete from " + JedisDb.TABLE;
		int count = this.jedisDb.update(sql);
		return count > 0 ? "OK" : "";
		// throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String info() {
		throw new UnsupportedOperationException("Not Implemented");

	}

	// @Override
	// public String rename(String oldkey, String newkey, int seconds) {
	// throw new UnsupportedOperationException("Not Implemented");
	//
	// }

	// @Override
	// public long getUsedMemory() {
	// throw new UnsupportedOperationException("Not Implemented");
	//
	// }

	@Override
	public Long dbSize() {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public String flushAll() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long del(String key) {
		boolean success = jedisDb.delete(key);
		if (success) {
			return 1L;
		}
		return 0L;
	}

	@Override
	public Long del(String... keys) {
		long count = 0;
		for (String key : keys) {
			boolean success = jedisDb.delete(key);
			if (success) {
				count++;
			}
		}
		return count;
	}

	@Override
	public List<String> mget(String... keys) {
		List<String> list = new ArrayList<String>();
		for (String key : keys) {
			String value = this.get(key);
			list.add(value);
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long zinterstore(String dstkey, String... sets) {
		int[] weights = RedisUtil.getDefaultWeights(sets);
		ZParams params = new ZParams().aggregate(ZParams.Aggregate.SUM);
		params.weights(weights);
		return this.zinterstore(dstkey, params, sets);
		// throw new UnsupportedOperationException("Not Implemented");
	}

	public static String getUnionSql(List<Double> weights, String... sets) {
		if (weights == null || weights.isEmpty()) {
			throw new IllegalArgumentException("参数weights不能为空.");
		}

		String prefixSql;
		{
			StringBuilder sb = new StringBuilder();
			sb.append("select t0.value");
			for (int i = 0; i < sets.length; i++) {
				sb.append(", t" + i + ".score score" + i);
			}
			// sb.deleteCharAt(sb.length() - 1);
			sb.append(" from");
			prefixSql = sb.toString();
		}
		String postfixSql;
		{
			StringBuilder sb = new StringBuilder();
			int index = 0;
			for (String key : sets) {
				double weight = weights.get(index);
				String sql = "select value, score*" + weight + " score from memcache where `key`='" + key + "'";
				// String sql = getWeightSql(key, weight);
				sb.append(" (" + sql + ") t" + index + ",");
				index++;
			}
			sb.deleteCharAt(sb.length() - 1);
			postfixSql = sb.toString();
		}
		return prefixSql + " " + postfixSql;
	}

	@Override
	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		jedisDb.delete(dstkey);
		// ZParams zParams = new ZParams().aggregate(ZParams.Aggregate.SUM);
		List<Double> weights = RedisUtil.getWeights(params);
		for (int i = 0; i < sets.length; i++) {
			weights.add(1D);
		}

		ZParams.Aggregate aggregate = RedisUtil.getAggregate(params);
		StringBuilder where = new StringBuilder("where (");
		// where (t0.value=t1.value);
		for (int i = 0; i < sets.length; i++) {
			where.append("t" + i + ".value=");
		}
		where.deleteCharAt(where.length() - 1);
		where.append(')');

		String unionSql = getUnionSql(weights, sets);
		String sql = unionSql + " " + where.toString();

		List<Map<String, Object>> list = jedisDb.queryForMaps(sql);
		if (list == null || list.isEmpty()) {
			return 0L;
		}

		for (Map<String, Object> map : list) {

			String member = (String) map.get("value");
			Map<String, Object> scoreMap = toScoreMap(map, sets.length);
			double score = aggregateScore(aggregate, scoreMap);
			this.zadd(dstkey, score, member);
		}
		return (long) list.size();
	}

	/**
	 * score聚合计算
	 * 
	 * @return
	 */
	protected static double aggregateScore(ZParams.Aggregate aggregate, Map<String, Object> map) {
		if (aggregate == ZParams.Aggregate.SUM) {
			double totalScore = 0;
			// int size = map.size() - 1;
			for (int i = 0; i < map.size(); i++) {
				double score = Double.parseDouble(map.get("score" + i).toString());
				totalScore += score;
			}
			return totalScore;
		}
		if (aggregate == ZParams.Aggregate.MIN) {
			double minScore = Double.MAX_VALUE;
			for (int i = 0; i < map.size(); i++) {
				double score = Double.parseDouble(map.get("score" + i).toString());
				if (score < minScore) {
					minScore = score;
				}
			}
			return minScore;
		}
		if (aggregate == ZParams.Aggregate.MAX) {
			double maxScore = Double.MIN_VALUE;
			for (int i = 0; i < map.size(); i++) {
				double score = Double.parseDouble(map.get("score" + i).toString());
				if (score > maxScore) {
					maxScore = score;
				}
			}
			return maxScore;
		}
		throw new RuntimeException("未知聚合类型.");
	}

	protected Map<String, Object> toScoreMap(Map<String, Object> map, int count) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < count; i++) {
			String key = "score" + i;
			if (!map.containsKey(key)) {
				continue;
			}
			Object score = map.get(key);
			result.put(key, score);
		}
		return result;
	}

	@Override
	public Set<String> keys(String pattern) {
		throw new UnsupportedOperationException("Not Implemented");

	}

	@Override
	public Long zunionstore(String dstkey, String... sets) {
		// TODO ahai 未实现
		return 1L;
	}

	// @Override
	// public Long hdel(String key, int field) {
	// return this.hdel(key, Integer.toString(field));
	// }

	@Override
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long zcount(String key, String min, String max) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long zremrangeByScore(String key, String start, String end) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	// @Override
	// public Long zadd2(String key, Map<String, Double> scoreMembers) {
	// Iterator<Entry<String, Double>> iterator =
	// scoreMembers.entrySet().iterator();
	// long count = 0;
	// while (iterator.hasNext()) {
	// Entry<String, Double> entry = iterator.next();
	// double score = entry.getValue();
	// String member = entry.getKey();
	// Long result = this.zadd(key, score, member);
	// count += result;
	// }
	// return count;
	// }

	@Override
	public Object eval(String arg0) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Object eval(String arg0, int arg1, String... arg2) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Object evalsha(String arg0) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Object evalsha(String arg0, List<String> arg1, List<String> arg2) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Object evalsha(String arg0, int arg1, String... arg2) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	// @Override
	// public Ludis getLudis() {
	// throw new UnsupportedOperationException("Not Implemented");
	// }

	@Override
	public String bgrewriteaof() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String bgsave() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String save() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long publish(String channel, String message) {
		JedisPubSub jedisPubSub = this.channelMap.get(channel);
		if (jedisPubSub != null) {
			jedisPubSub.onMessage(channel, message);
		}
		return 1L;
	}

	private Map<String, JedisPubSub> channelMap = new ConcurrentHashMap<String, JedisPubSub>();

	@Override
	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		for (String channel : channels) {
			channelMap.put(channel, jedisPubSub);
		}
	}

	@Override
	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Set<String> sdiff(String... keys) {
		Set<String> diffSet = this.smembers(keys[0]);
		System.out.println("diffSet:" + diffSet);
		for (int i = 1; i < keys.length; i++) {
			Set<String> set = this.smembers(keys[i]);
			if (set == null || set.isEmpty()) {
				continue;
			}
			diffSet.removeAll(set);
		}
		if (diffSet.isEmpty()) {
			return null;
		}
		return diffSet;
	}

	@Override
	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		// FIXME ahai 未实现
		return 1L;
	}

	@Override
	public String randomKey() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long persist(String key) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Boolean setbit(String key, long offset, String value) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long strlen(String key) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long lpushx(String key, String... string) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long rpushx(String key, String... string) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public List<String> blpop(String arg) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public List<String> brpop(String arg) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String echo(String string) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long move(String key, int dbIndex) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long bitcount(String key) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long bitcount(String key, long start, long end) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public ScanResult<String> sscan(String key, int cursor) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public ScanResult<Tuple> zscan(String key, int cursor) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public ScanResult<String> sscan(String key, String cursor) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public ScanResult<Tuple> zscan(String key, String cursor) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public Long pfadd(String key, String... elements) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public long pfcount(String key) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public String set(String key, String arg1, String arg2, String arg3, long arg4) {
		throw new UnsupportedOperationException("Not Implemented");
	}

}
