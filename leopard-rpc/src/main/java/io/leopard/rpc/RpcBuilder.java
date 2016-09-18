package io.leopard.rpc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.leopard.json.Json;

public class RpcBuilder {

	private String domain;
	private String uri;
	private String url;

	private long timeout;

	private Map<String, Object> params = new LinkedHashMap<String, Object>();

	public RpcBuilder(String domain, String uri) {
		this(domain, uri, 10000);
	}

	public RpcBuilder(String domain, String uri, long timeout) {
		this.domain = domain;
		this.uri = uri;
		this.url = domain + uri;
	}

	public RpcBuilder setDomain(String domain) {
		this.domain = domain;
		this.url = domain + uri;
		return this;
	}

	public RpcBuilder setUri(String uri) {
		this.uri = uri;
		this.url = domain + uri;
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
		return (Boolean) RpcClient.doPostForObject(url, params, timeout);
	}

	public Long getForLong() {
		return (Long) RpcClient.doPostForObject(url, params, timeout);
	}

	public Double getForDouble() {
		return (Double) RpcClient.doPostForObject(url, params, timeout);
	}

	public <T> T doPost(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) RpcClient.doPostForData(url, params, timeout);
		String json = Json.toJson(map);
		return Json.toObject(json, clazz);
	}

	public <T> List<T> doPostForList(Class<T> clazz) {
		return (List<T>) RpcClient.doPostForList(url, params, clazz, timeout);
	}
}
