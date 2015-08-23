package io.leopard.jdbc;

import java.sql.Connection;

//TODO ahai 此类未测试
public class ProxyConnection extends ConnectionWrapper {

	public ProxyConnection(Connection connection) {
		super(connection);
	}

}
