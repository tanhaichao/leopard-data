package io.leopard.jdbc.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import io.leopard.autounit.FileUtils;
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
		DataSource dataSource = H2Util.createDataSource("jdbc");
		jdbc.setDataSource(ConnectionContext.register(dataSource));

		super.setFieldValue(bean, field, jdbc);

		this.populate(jdbc.getDataSource());

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

	/**
	 * 导入表结构.
	 * 
	 * @param dataSource
	 */
	protected String populate(DataSource dataSource) {
		String sql;
		try {
			sql = FileUtils.toString(new ClassPathResource("/init.sql").getInputStream());
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		String dir = H2Util.getDir("jdbc");
		File file = new File(dir, "jdbc.hash");

		int hashCode = sql.hashCode();
		{// 验证表结构是否有变化

			if (file.exists()) {
				String content = FileUtils.readToString(file);
				System.out.println("content:" + content + " " + hashCode);
				if (Integer.parseInt(content) == hashCode) {
					// 表结构没有改动
					System.err.println("表结构没有改动");
					// new Exception().printStackTrace();
					return sql;
				}
			}
		}

		sql = H2SqlUtil.filter(sql);

		System.err.println("开始导入表结构");
		// System.err.println(sql);

		Resource scripts = new ByteArrayResource(sql.getBytes());
		DatabasePopulator populator = new ResourceDatabasePopulator(scripts);
		DatabasePopulatorUtils.execute(populator, dataSource);

		FileUtils.write(file, Integer.toString(hashCode));
		return sql;
	}

}
