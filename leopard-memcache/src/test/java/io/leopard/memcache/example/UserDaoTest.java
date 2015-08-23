package io.leopard.memcache.example;

import io.leopard.autounit.AutoUnit;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class UserDaoTest {

	UserDao userDao = AutoUnit.mock(UserDao.class);

	@Test
	public void get() {
		{
			User user = new User();
			user.setUid(1);
			user.setNickname("Leopard");
			user.setPosttime(new Date());
			this.userDao.add(user);
		}

		User user = userDao.get(1);
		// Json.print(user, "user");
		Assert.assertEquals(1, user.getUid());
	}

	@Test
	public void delete() {
		userDao.clean();
		Assert.assertFalse(this.userDao.delete(1));
		this.get();
		Assert.assertTrue(this.userDao.delete(1));
	}

}