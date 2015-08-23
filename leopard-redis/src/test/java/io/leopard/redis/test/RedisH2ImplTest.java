package io.leopard.redis.test;

import org.junit.Assert;
import org.junit.Test;

public class RedisH2ImplTest {

	private RedisH2Impl redis = new RedisH2Impl();

	@Test
	public void hset() {
		redis.flushDB();
		Assert.assertEquals(1, (long) redis.hset("key", 1, "value"));
		Assert.assertEquals("value", redis.hget("key", 1));
	}

}