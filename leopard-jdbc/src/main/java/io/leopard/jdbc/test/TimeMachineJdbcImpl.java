package io.leopard.jdbc.test;

import io.leopard.autounit.web.TimeMachine;
import io.leopard.jdbc.Jdbc;

public class TimeMachineJdbcImpl implements TimeMachine {

	@Override
	public Object mock(Object bean) {
		if (!(bean instanceof Jdbc)) {
			return null;
		}
		return new JdbcThreadImpl((Jdbc) bean);
	}

}
