package io.leopard.data.kit.captcha;

import io.leopard.core.exception.forbidden.CaptchaWrongException;

public interface CaptchaService {

	String add(String account, String category, String type, String target, String captcha);

	Captcha last(String account, String category, String type, String target);

	boolean updateUsed(String captchaId, boolean used);

	Captcha check(String account, String category, String type, String target, String captcha) throws CaptchaWrongException;

}
