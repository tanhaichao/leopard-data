package io.leopard.data.kit.password;

public interface PasswordVerify {

	boolean verify(String username, String password, String salt, String dbPassword);
}
