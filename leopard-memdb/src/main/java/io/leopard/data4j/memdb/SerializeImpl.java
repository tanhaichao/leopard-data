package io.leopard.data4j.memdb;

import io.leopard.burrow.lang.Json;

public class SerializeImpl implements Serialize {
	private static Serialize instance = new SerializeImpl();

	public static Serialize getInstance() {
		return instance;
	}

	@Override
	public String serialize(Object obj) {
		return Json.toJson(obj);
	}

	@Override
	public <T> T toObject(String json, Class<T> clazz) {
		return Json.toObject(json, clazz);
	}

}
