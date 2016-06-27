package io.leopard.data.kit.password;

import java.util.ArrayList;
import java.util.List;

public class PasswordVerifierImpl implements PasswordVerifier {

	private PasswordVerifier[] passwordVerifiers;

	public PasswordVerifierImpl() {
		List<PasswordVerifier> list = new ArrayList<PasswordVerifier>();
		list.add(new Sha1PasswordVerifier());
		list.add(new Md5PasswordVerifier());

		passwordVerifiers = new PasswordVerifier[list.size()];
		list.toArray(passwordVerifiers);
	}

	@Override
	public boolean verify(String username, String password, String salt, String dbPassword) {
		for (PasswordVerifier verifier : passwordVerifiers) {
			if (verifier.verify(username, password, salt, dbPassword)) {
				return true;
			}
		}
		return false;
	}

}
