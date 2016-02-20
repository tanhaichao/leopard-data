package io.leopard.data.kit.rank;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.leopard.redis.Redis;
import io.leopard.redis.RedisMemoryImpl;

public class CountRankTimeBucketImplTest {

	private CountRankTimeBucketImpl rank = new CountRankTimeBucketImpl();

	private Redis redis;

	public CountRankTimeBucketImplTest() {
		redis = new RedisMemoryImpl();
		rank.setKey("key");
		rank.setRedis(redis);
		rank.setTimeBucket(TimeBucket.DAY);
		rank.init();
	}

	@Test
	public void getTimeBucketKey() {
		Date date = new Date();
		rank.setTimeBucket(TimeBucket.MINUTE);
		Assert.assertEquals("201602202315", rank.getTimeBucketKey(date));
		rank.setTimeBucket(TimeBucket.HOUR);
		Assert.assertEquals("2016022023", rank.getTimeBucketKey(date));
		rank.setTimeBucket(TimeBucket.DAY);
		Assert.assertEquals("20160220", rank.getTimeBucketKey(date));
		rank.setTimeBucket(TimeBucket.MONTH);
		Assert.assertEquals("201602", rank.getTimeBucketKey(date));
		rank.setTimeBucket(TimeBucket.YEAR);
		Assert.assertEquals("2016", rank.getTimeBucketKey(date));
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
		rank.incr("member1", 1);
		Assert.assertEquals(1D, rank.getScore("member1"), 0);

	}

	@Test
	public void delete() {
		Assert.assertEquals(1, rank.incr("member1", 1));
		Assert.assertTrue(rank.delete("member1"));
		Assert.assertFalse(rank.delete("member1"));
		Assert.assertNull(rank.getScore("member1"));
	}

	@Test
	public void keysHour() {
		List<String> keys = new CountRankTimeBucketImpl.TimeBucketKeysHourImpl().keys(new Date());
		System.out.println("keys:" + keys);
	}

	@Test
	public void keysDay() {
		List<String> keys = new CountRankTimeBucketImpl.TimeBucketKeysDayImpl().keys(new Date());
		System.out.println("keys:" + keys);
	}

	@Test
	public void keys() {
		List<String> keys = rank.keys(new Date());
		System.out.println("keys:" + keys);
	}
}