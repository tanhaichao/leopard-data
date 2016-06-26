package io.leopard.data.kit.captcha;

import io.leopard.core.exception.forbidden.CaptchaWrongException;

public interface CaptchaService {

	String add(String email, String type, String captcha);

	Captcha last(String email, String type);

	boolean updateUsed(String captchaId, boolean used);

	Captcha check(String email, String type, String captcha) throws CaptchaWrongException;

}
