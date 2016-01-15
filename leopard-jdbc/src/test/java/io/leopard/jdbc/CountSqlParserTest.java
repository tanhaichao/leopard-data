package io.leopard.jdbc;

import org.junit.Test;

public class CountSqlParserTest {

	@Test
	public void parseLimitParamCount() {
		CountSqlParser parser = new CountSqlParser("where uid=? and user=?", null);
		// int count = parser.parseLimitParamCount("where uid=? and user=?");
		// System.out.println("count:" + count);
	}

	@Test
	public void getCountSql() {
		CountSqlParser parser = new CountSqlParser("select * from user where uid=? and user=? order by postime desc limit ?,?", null);

		System.out.println("sql:" + parser.getCountSql());
	}

}