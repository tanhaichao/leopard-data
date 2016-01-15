package io.leopard.jdbc.test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import io.leopard.autounit.inject.AbstractInject;
import io.leopard.autounit.inject.Inject;
import io.leopard.autounit.unitdb.ConnectionContext;
import io.leopard.autounit.unitdb.H2Util;
import io.leopard.jdbc.Jdbc;

public class AutoUnitInjectJdbcImpl extends AbstractInject {

	private JdbcH2Impl jdbc;

	@Override
	public Inject inject(Object bean, Field field) {
		if (!this.isNeedSetValue(bean, field, Jdbc.class)) {
			return null;
		}

		jdbc = new JdbcH2Impl();
		DataSource dataSource = H2Util.createDataSource("autounit");
		jdbc.setDataSource(ConnectionContext.register(dataSource));

		super.setFieldValue(bean, field, jdbc);

		H2SqlUtil.populate("autounit", jdbc.getDataSource());

		return this;
	}

	@Override
	public boolean clean() {
		List<Map<String, Object>> list = this.jdbc.queryForMaps("SHOW TABLES;");
		for (Map<String, Object> map : list) {
			String tableName = (String) map.get("TABLE_NAME");

			String sql = "delete from " + tableName;
			System.err.println("clean sql:" + sql);
			this.jdbc.update(sql);
		}
		return true;
	}

}
