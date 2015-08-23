package io.leopard.memcache.example;

import io.leopard.memcache.Memcache;

import javax.annotation.Resource;

public class UserDao {

	@Resource
	private Memcache memcache;

	protected String getKey(long uid) {
		return "user:" + uid;
	}

	/**
	 * 添加用户.
	 * 
	 * @param user
	 * @return 添加成功返回true，出错抛异常
	 */
	public boolean add(User user) {
		String key = this.getKey(user.getUid());
		String json = null;// Json.toJson(user);
		return this.memcache.put(key, json);
	}

	/**
	 * 根据uid获取用户信息.
	 * 
	 * @param uid
	 * @return 用户存在则返回用户对象，不存在则返回null.
	 */
	public User get(long uid) {
		String key = this.getKey(uid);
		String json = null;// this.memcache.get(key);
		// return Json.toObject(json, User.class);
		return null;
	}

	/**
	 * 删除用户
	 * 
	 * @param uid
	 * @return 成功删除记录就返回true，记录不存在则返回false，出错则抛异常.
	 */
	public boolean delete(long uid) {
		String key = this.getKey(uid);
		return this.memcache.remove(key);
	}

	public boolean clean() {
		return this.memcache.flushAll();
	}
}
