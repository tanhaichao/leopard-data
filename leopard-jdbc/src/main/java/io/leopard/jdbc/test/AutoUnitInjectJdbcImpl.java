package io.leopard.jdbc.test;

import io.leopard.autounit.FileUtils;
import io.leopard.autounit.inject.AbstractInject;
import io.leopard.autounit.inject.Inject;
import io.leopard.autounit.unitdb.ConnectionContext;
import io.leopard.autounit.unitdb.H2Util;
import io.leopard.jdbc.Jdbc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

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
					return sql;
				}
			}
		}

		sql = this.filter(sql);

		System.err.println("开始导入表结构");
		// System.err.println(sql);

		Resource scripts = new ByteArrayResource(sql.getBytes());
		DatabasePopulator populator = new ResourceDatabasePopulator(scripts);
		DatabasePopulatorUtils.execute(populator, dataSource);

		FileUtils.write(file, Integer.toString(hashCode));
		return sql;
	}

	protected String filter(String sql) {
		if (true) {
			// TODO 临时过滤
			sql = sql.replace("DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", "DEFAULT NULL");
		}
		if (true) {
			sql = sql.replaceAll("/\\*.*?\\*/;", "");
			sql = sql.replaceAll("--[^\n]{0,}", "");
			sql = sql.replaceAll("  KEY [^\n]{0,}", "");
			sql = sql.replaceAll("  FULLTEXT KEY [^\n]{0,}", "");

			sql = sql.replace("UNIQUE KEY ", "UNIQUE INDEX ");
			sql = sql.replace("double(11,0)", "double(11)");
			sql = sql.replaceAll("double\\([0-9]+,[0-9]+\\)", "double");

			sql = this.replaceUniqueKeyName(sql);

			sql = sql.replaceAll("COMMENT='.*?'", "");
			sql = sql.replaceAll("AUTO_INCREMENT=[0-9]+", "");
			sql = sql.replace("DEFAULT CHARSET=utf8", "");
			sql = sql.replace("ENGINE=InnoDB", "");
			sql = sql.replace("ENGINE=MyISAM", "");

			// ) ENGINE=MyISAM COMMENT='该表仅保存最后将公告同步到公告推送表';

			// create INDEX `idx_username_uu` on message (`game_id`,
			// `server_id`);
			// System.out.println(content);

		}
		return sql;
	}

	protected String replaceUniqueKeyName(String content) {
		// UNIQUE KEY `un_groupId_activityId_username`
		String regex = "UNIQUE INDEX `(.*?)`";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String keyName = m.group(1);
			int rand = random.nextInt();
			String newKeyName = keyName + "_" + rand + "a'";
			String replacement = "UNIQUE INDEX `" + newKeyName + "`";
			m.appendReplacement(sb, replacement);
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
