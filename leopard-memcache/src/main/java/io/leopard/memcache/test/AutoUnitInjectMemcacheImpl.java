package io.leopard.memcache.test;

import io.leopard.autounit.inject.AbstractInject;
import io.leopard.autounit.inject.Inject;
import io.leopard.autounit.unitdb.ConnectionContext;
import io.leopard.autounit.unitdb.H2Util;
import io.leopard.memcache.Memcache;

import java.lang.reflect.Field;

import javax.sql.DataSource;

public class AutoUnitInjectMemcacheImpl extends AbstractInject {

	@Override
	public Inject inject(Object bean, Field field) {
		if (!this.isNeedSetValue(bean, field, Memcache.class)) {
			return null;
		}

		MemcacheH2Impl memcache = new MemcacheH2Impl();
		DataSource dataSource = H2Util.createDataSource("memcache");
		memcache.setDataSource(ConnectionContext.register(dataSource));
		memcache.init();

		super.setFieldValue(bean, field, memcache);

		return this;
	}

	@Override
	public boolean clean() {
		// TODO Auto-generated method stub
		return false;
	}

}
