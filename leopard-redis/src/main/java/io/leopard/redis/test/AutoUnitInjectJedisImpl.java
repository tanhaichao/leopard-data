package io.leopard.redis.test;

import io.leopard.autounit.inject.AbstractInject;
import io.leopard.autounit.inject.Inject;
import io.leopard.autounit.unitdb.ConnectionContext;
import io.leopard.autounit.unitdb.H2Util;

import java.lang.reflect.Field;

import javax.sql.DataSource;

import redis.clients.jedis.Jedis;

public class AutoUnitInjectJedisImpl extends AbstractInject {

	@Override
	public Inject inject(Object bean, Field field) {
		if (!this.isNeedSetValue(bean, field, Jedis.class)) {
			return null;
		}

		JedisH2Impl jedis = new JedisH2Impl();
		DataSource dataSource = H2Util.createDataSource("autounit", "jedis");
		jedis.setDataSource(ConnectionContext.register(dataSource));
		jedis.init();

		super.setFieldValue(bean, field, jedis);
		return this;
	}

	@Override
	public boolean clean() {
		return false;
	}

}
