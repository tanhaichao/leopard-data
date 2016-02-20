package io.leopard.data.kit.rank;

import org.junit.Assert;
import org.junit.Test;

import io.leopard.redis.Redis;
import io.leopard.redis.RedisMemoryImpl;

public class CountRankImplTest {

	private CountRankImpl rank = new CountRankImpl();
	Redis redis;

	public CountRankImplTest() {
		redis = new RedisMemoryImpl();
		rank.setKey("key");
		rank.setRedis(redis);
	}

	@Test
	public void incr() {
		Assert.assertEquals(1, rank.incr("member1", 1));
		Assert.assertEquals(2, rank.incr("member1", 1));
		Assert.assertEquals(1, rank.incr("member2", 1));
		Assert.assertEquals(1, rank.getScore("member2").longValue());

		System.out.println(rank.listMembers(0, 10));
		Assert.assertEquals("[member1, member2]", rank.listMembers(0, 10).toString());

	}

	@Test
	public void getScore() {
		// redis.zadd("key", 1, "member1");
		redis.zincrby("key", 1, "member1");
		Assert.assertEquals(1D, rank.getScore("member1"), 0);

	}

	@Test
	public void delete() {
		Assert.assertEquals(1, rank.incr("member1", 1));
		Assert.assertTrue(rank.delete("member1"));
		Assert.assertFalse(rank.delete("member1"));
		Assert.assertNull(rank.getScore("member1"));
	}

}