package io.leopard.memcache.test;

import io.leopard.memcache.test.MemcacheH2Impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MemcacheH2ImplTest {

	private MemcacheH2Impl memcache = new MemcacheH2Impl();

	@Before
	public void cleanData() {
		memcache.flushAll();
	}

	@Test
	public void put() {
		memcache.put("key", "value");
		Assert.assertEquals("value", memcache.get("key"));
		memcache.put("key", "value1");
		Assert.assertEquals("value1", memcache.get("key"));
		memcache.remove("key");
		Assert.assertNull(memcache.get("key"));
	}

	@Test
	public void incr() {
		for (int i = 1; i < 3; i++) {
			this.memcache.incr("key");
			Assert.assertEquals(i, memcache.getInt("key"));
		}

	}

}