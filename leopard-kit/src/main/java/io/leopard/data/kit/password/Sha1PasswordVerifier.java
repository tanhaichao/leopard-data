package io.leopard.data.kit.password;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Sha1PasswordVerifier implements PasswordVerifier {
	protected Log logger = LogFactory.getLog(this.getClass());

	@Override
	public boolean verify(String username, String password, String salt, String dbPassword) {
		String encryptedPassword;
		if (password.length() == 40) {
			encryptedPassword = password;
		}
		else {
			encryptedPassword = PasswordUtil.encryptPassword(password, salt);
		}
		boolean correctPassword = encryptedPassword.equalsIgnoreCase(dbPassword);
		if (!correctPassword) {
			logger.info("username:" + username + " password:" + password + " salt:" + salt + " encryptedPassword:" + encryptedPassword + " dbPassword:" + dbPassword);
		}
		return correctPassword;
	}

}
