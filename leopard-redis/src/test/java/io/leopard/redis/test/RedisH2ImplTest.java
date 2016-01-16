package io.leopard.redis.test;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import io.leopard.autounit.unitdb.H2Util;

public class RedisH2ImplTest {

	private RedisH2Impl redis = new RedisH2Impl();

	public RedisH2ImplTest() {
		DataSource dataSource = H2Util.createDataSource("mock", "redis", true);
		redis.setDataSource(dataSource);
		redis.init();
	}

	@Test
	public void hset() {
		redis.flushDB();
		Assert.assertEquals(1, (long) redis.hset("key", 1, "value"));
		Assert.assertEquals("value", redis.hget("key", 1));
	}

	@Test
	public void set() {
		redis.set("key", "value1");
		Assert.assertEquals("value1", redis.get("key"));
	}

}