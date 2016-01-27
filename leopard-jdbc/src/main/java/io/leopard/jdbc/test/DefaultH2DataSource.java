package io.leopard.jdbc.test;

import io.leopard.autounit.unitdb.H2Util;
import io.leopard.jdbc.JdbcDataSource;

public class DefaultH2DataSource extends JdbcDataSource {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void init() {
		super.dataSource = H2Util.createDataSource(getCategory(), this.name, isAutoCommit());
	}

	public void destroy() {

	}

	public static String getCategory() {
		String category = System.getProperty("h2Category");
		if (category == null || category.length() == 0) {
			throw new IllegalArgumentException("h2Category属性不能为空.");
		}
		return category;
	}

	public static void setUseH2(boolean useH2, String category, boolean autoCommit) {
		System.setProperty("h2Category", category);
		System.setProperty("useH2", Boolean.toString(useH2));
		System.setProperty("h2AutoCommit", Boolean.toString(autoCommit));
	}

	public static boolean isAutoCommit() {
		return "true".equals(System.getProperty("h2AutoCommit"));
	}
}
