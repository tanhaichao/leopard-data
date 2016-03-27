package io.leopard.jdbc.test;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.LeopardBeanFactoryAware;
import io.leopard.jdbc.builder.InsertBuilder;
import io.leopard.json.Json;

public class AgentnbTimeLogger {

	public void save(Map<String, Long> timeMap) {
		// Json.printMap(timeMap, "AgentnbTimeLogger timeMap");
		Jdbc jdbc = LeopardBeanFactoryAware.getBeanFactory().getBean(Jdbc.class);
		InsertBuilder builder = new InsertBuilder("agentnb_startup");
		builder.setDate("posttime", new Date());
		for (Entry<String, Long> entry : timeMap.entrySet()) {
			String fieldName = entry.getKey();
			Long time = entry.getValue();
			builder.setLong(fieldName, time);
		}
		jdbc.insertForBoolean(builder);
	}
}
