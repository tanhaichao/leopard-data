package io.leopard.rpc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.leopard.json.Json;

public class RpcBuilder {

	private String domain;

	private String uri;
	private String url;

	private Map<String, Object> params = new LinkedHashMap<String, Object>();

	public RpcBuilder(String domain, String uri) {
		this.domain = domain;
		this.uri = uri;
		this.url = domain + uri;
	}

	public RpcBuilder setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	public RpcBuilder setUri(String uri) {
		this.uri = uri;
		return this;
	}

	public void setString(String name, String value) {
		this.params.put(name, value);
	}

	public void setLong(String name, long value) {
		this.params.put(name, value);
	}

	public void setInt(String name, int value) {
		this.params.put(name, value);
	}

	public Boolean getForBoolean() {
		return (Boolean) RpcClient.doPost(url, params, 10000L);
	}

	public Long getForLong() {
		return (Long) RpcClient.doPost(url, params, 10000L);
	}

	public <T> T doPost(Class<T> clazz) {
		String data = RpcClient.doPostForData(url, params, 10000L);
		return Json.toObject(data, clazz);
	}

	public <T> List<T> doPostForList(Class<T> clazz) {
		return (List<T>) RpcClient.doPostForList(url, params, clazz, 10000L);
	}
}
