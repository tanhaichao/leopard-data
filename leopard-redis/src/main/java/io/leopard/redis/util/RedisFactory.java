package io.leopard.redis.util;

import io.leopard.redis.RedisImpl;

public class RedisFactory {

	public static RedisImpl create(String server, String password) {
		RedisImpl redis = new RedisImpl(server, 10, 0, false, 3000, password);
		redis.init();
		return redis;
	}
}
