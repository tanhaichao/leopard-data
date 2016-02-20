package io.leopard.data.kit.rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.leopard.redis.Redis;
import redis.clients.jedis.Tuple;

/**
 * 数量排名实现.
 * 
 * @author 阿海
 *
 */
public class CountRankImpl implements CountRank {

	private Redis redis;

	private String key;

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Redis getRedis() {
		return redis;
	}

	@Override
	public boolean clean() {
		Long num = redis.del(getKey());
		return num != null && num > 0;
	}

	@Override
	public boolean delete(String member) {
		Long num = redis.zrem(getKey(), member);
		return num != null && num > 0;
	}

	@Override
	public Double getScore(String member) {
		return redis.zscore(getKey(), member);
	}

	@Override
	public List<Tuple> list(int start, int size) {
		long end = start + size;
		Set<Tuple> set = redis.zrevrangeWithScores(getKey(), start, end);
		if (set == null || set.isEmpty()) {
			return null;
		}
		List<Tuple> list = new ArrayList<Tuple>();
		for (Tuple tuple : set) {
			list.add(tuple);
		}
		return list;
	}

	@Override
	public List<String> listMembers(int start, int size) {
		long end = start + size;
		Set<String> set = redis.zrevrange(getKey(), start, end);
		if (set == null || set.isEmpty()) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (String member : set) {
			list.add(member);
		}
		return list;
	}

	@Override
	public long incr(String member, long count) {
		Double totalCount = redis.zincrby(getKey(), count, member);
		return totalCount.longValue();
	}

}
