package io.leopard.rpc;

public class RpcBuilder {

	private String domain;

	private String uri;

	public RpcBuilder(String domain, String uri) {
		this.domain = domain;
		this.uri = uri;
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

	}

	public void setLong(String name, long value) {

	}

	public void setInt(String name, int value) {

	}

	public boolean getForBoolean() {
		return true;
	}

}
