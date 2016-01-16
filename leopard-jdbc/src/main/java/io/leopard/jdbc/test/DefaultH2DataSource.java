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
		super.dataSource = H2Util.createDataSource("mock", this.name, true);
	}

	public void destroy() {

	}

}
