package io.leopard.data.kit.captcha;

public interface CaptchaDao {

	boolean add(Captcha captcha);

	Captcha last(String mobile, String type);

	boolean updateUsed(String captchaId, boolean used);
}
