package io.leopard.jdbc.test;

import java.util.Map;

import io.leopard.json.Json;

public class AgentnbTimeLogger {

	public void save(Map<String, Long> timeMap) {
		Json.printMap(timeMap, "AgentnbTimeLogger timeMap");
	}
}
