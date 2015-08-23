package io.leopard.jdbc.test;

import io.leopard.jdbc.test.JdbcH2Impl;

import org.junit.Test;

public class JdbcH2ImplTest {

	@Test
	public void JdbcH2Impl() {
		JdbcH2Impl jdbc = new JdbcH2Impl();

		String user = jdbc.queryForString("select user()");
		System.out.println("user:" + user);
	}

}