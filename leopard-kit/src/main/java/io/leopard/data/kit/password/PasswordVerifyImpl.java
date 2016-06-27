package io.leopard.data.kit.password;

import java.util.ArrayList;
import java.util.List;

public class PasswordVerifyImpl implements PasswordVerify {

	private PasswordVerify[] passwordVerifys;

	public PasswordVerifyImpl() {
		List<PasswordVerify> list = new ArrayList<PasswordVerify>();
		list.add(new Sha1PasswordVerify());
		list.add(new Md5PasswordVerify());

		passwordVerifys = new PasswordVerify[list.size()];
		list.toArray(passwordVerifys);
	}

	@Override
	public boolean verify(String username, String password, String salt, String dbPassword) {
		for (PasswordVerify verify : passwordVerifys) {
			if (verify.verify(username, password, salt, dbPassword)) {
				return true;
			}
		}
		return false;
	}

}
