package io.leopard.data.kit.password;

/**
 * 密码检验器.
 * 
 * @author 谭海潮
 *
 */
public interface PasswordVerifier {

	boolean verify(String username, String password, String salt, String dbPassword);
}
