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

	public void setList(String name, List<?> list) {
		this.params.put(name, Json.toJson(list));
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
		return Json.toObject(json, clazz, true);
	}

	public <T> List<T> doPostForList(Class<T> clazz) {
		return (List<T>) RpcClient.doPostForList(url, params, clazz, timeout);
	}

	public String queryForString() {
		return (String) RpcClient.doPostForData(url, params, timeout);
	}

	public Long queryForLong() {
		String str = this.queryForString();
		if (str == null) {
			return null;
		}
		return Long.parseLong(str);
	}

	public Integer queryForInteger() {
		String str = this.queryForString();
		if (str == null) {
			return null;
		}
		return Integer.parseInt(str);
	}

	public List<Long> queryForLongs() {
		@SuppressWarnings("unchecked")
		List<Long> list = (List<Long>) RpcClient.doPostForData(url, params, timeout);
		// System.out.println("list:" + list);
		// Json.toListObject(json, clazz)
		// String json = Json.toJson(map);
		// return Json.toObject(json, clazz, true);
		return list;
	}
}
