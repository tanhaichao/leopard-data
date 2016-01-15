package io.leopard.jdbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class CountSqlParser {

	private final String sql;
	private final StatementParameter param;

	private String countSql;

	public CountSqlParser(String sql, StatementParameter param) {
		this.sql = sql;
		this.param = param;
		this.parse();
	}

	private static final Pattern LIMIT_PATTERN = Pattern.compile(" limit .*$", Pattern.CASE_INSENSITIVE);
	private static final Pattern ORDERBY_PATTERN = Pattern.compile(" order by .*$", Pattern.CASE_INSENSITIVE);

	protected void parse() {
		String sql = this.sql;
		sql = sql.replaceAll("select .*? from", "select count(*) from");
		sql = sql.replaceAll("SELECT .*? FROM", "SELECT count(*) FROM");
		{
			Matcher m = ORDERBY_PATTERN.matcher(sql);
			if (m.find()) {
				sql = sql.substring(0, m.start());
			}
		}
		{
			Matcher m = LIMIT_PATTERN.matcher(sql);
			if (m.find()) {
				sql = sql.substring(0, m.start());
			}
		}

		this.countSql = sql;
	}

	public String getCountSql() {
		return this.countSql;
	}

	public StatementParameter getCountParam() {
		int max = StringUtils.countOccurrencesOf(this.countSql, "?");
		Object[] values = this.param.getArgs();
		StatementParameter param = new StatementParameter();
		for (int i = 0; i < max; i++) {
			Class<?> type = this.param.getType(i);
			param.setObject(type, values[i]);
		}
		return param;
	}
}
