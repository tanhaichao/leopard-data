package io.leopard.jdbc.test;

import io.leopard.autounit.unitdb.H2Util;
import io.leopard.jdbc.JdbcDataSource;

public class H2DataSource extends JdbcDataSource {

	private String jdbcId;

	public String getJdbcId() {
		return jdbcId;
	}

	public void setJdbcId(String jdbcId) {
		this.jdbcId = jdbcId;
	}

	public void init() {
		super.dataSource = H2Util.createDataSource("mock", this.jdbcId, true);
		H2SqlUtil.populate("mock", dataSource);
	}

	public void destroy() {

	}

}
