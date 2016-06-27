package io.leopard.data.kit.password;

import io.leopard.util.EncryptUtil;

public class Md5PasswordVerify implements PasswordVerify {

	@Override
	public boolean verify(String username, String password, String salt, String dbPassword) {
		String encryptedPassword;
		if (password.length() == 32) {
			encryptedPassword = password;
		}
		else {
			String str = password + "@" + salt;
			encryptedPassword = EncryptUtil.md5(str).toLowerCase();
		}
		return encryptedPassword.equalsIgnoreCase(dbPassword);
	}

}
