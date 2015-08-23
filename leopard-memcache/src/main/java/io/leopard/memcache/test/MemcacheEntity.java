package io.leopard.memcache.test;

import java.util.Date;

import javax.xml.bind.annotation.XmlID;

/**
 * memcache表结构.
 * 
 * @author 阿海
 *
 */

public class MemcacheEntity {
	

	// 为了不引入其他jar包，暂时使用这个注解替代
	@XmlID
	private String key;

	private String value;
	private Date expire;
	private Date posttime;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public Date getPosttime() {
		return posttime;
	}

	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}

}
