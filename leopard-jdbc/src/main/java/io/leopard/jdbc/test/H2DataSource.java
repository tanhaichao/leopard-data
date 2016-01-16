package io.leopard.jdbc.test;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.util.StringUtils;

import io.leopard.autounit.unitdb.H2Util;
import io.leopard.jdbc.JdbcDataSource;
import io.leopard.jdbc.JdbcMysqlImpl;
import io.leopard.jdbc.JdbcUrlInfo;
import io.leopard.jdbc.ProxyDataSource;
import io.leopard.json.Json;

public class H2DataSource extends JdbcDataSource {

	private String jdbcId;

	public String getJdbcId() {
		return jdbcId;
	}

	public void setJdbcId(String jdbcId) {
		this.jdbcId = jdbcId;
	}

	private String url;

	public void setUrl(String url) {
		// AssertUtil.assertNotEmpty(url, "参数url不能为空.");
		if (StringUtils.isEmpty(url)) {
			throw new IllegalArgumentException("参数url不能为空.");
		}
		logger.info("jdbcUrl:" + url);
		this.url = url;
	}

	public void init() {
		super.dataSource = H2Util.createDataSource("mock", this.jdbcId, true);
		H2SqlUtil.populate("mock", dataSource);
	}

	public void rsyncServerDataToLocal() {
		JdbcUrlInfo jdbcUrlInfo = this.parseUrl(url);
		this.setHost(jdbcUrlInfo.getHost());
		this.setPort(jdbcUrlInfo.getPort());
		this.setDatabase(jdbcUrlInfo.getDatabase());

		String jdbcUrl = ProxyDataSource.getJdbcUrl(host, port, database);
		DataSource mysqlDataSource = ProxyDataSource.createDataSource(driverClass, jdbcUrl, user, password, maxPoolSize);
		JdbcMysqlImpl jdbc = new JdbcMysqlImpl();
		jdbc.setDataSource(mysqlDataSource);

		List<Map<String, Object>> list = jdbc.queryForMaps("show tables");
		Json.printList(list, "tables");
	}

	protected JdbcUrlInfo parseUrl(String url) {
		String regex = "jdbc:mysql://(.*?):([0-9]+)/([a-z0-9A-Z_]+)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		if (!m.find()) {
			throw new IllegalArgumentException("JdbcUrl[" + url + "]不符合规则.");
		}
		String host = m.group(1);
		int port = Integer.parseInt(m.group(2));
		String database = m.group(3);

		JdbcUrlInfo jdbcUrlInfo = new JdbcUrlInfo();
		jdbcUrlInfo.setDatabase(database);
		jdbcUrlInfo.setPort(port);
		jdbcUrlInfo.setHost(host);
		return jdbcUrlInfo;
	}

	public void destroy() {

	}

}
