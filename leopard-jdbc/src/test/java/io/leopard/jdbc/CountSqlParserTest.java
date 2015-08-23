package io.leopard.jdbc;

import io.leopard.jdbc.CountSqlParser;

import org.junit.Test;

public class CountSqlParserTest {

	@Test
	public void parseLimitParamCount() {
		CountSqlParser parser = new CountSqlParser("where uid=? and user=?", null);
		int count = parser.parseLimitParamCount("where uid=? and user=?");
		System.out.println("count:" + count);
	}

}