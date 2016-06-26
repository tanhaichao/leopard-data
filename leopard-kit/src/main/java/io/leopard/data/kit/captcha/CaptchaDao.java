package io.leopard.data.kit.captcha;

public interface CaptchaDao {

	boolean add(Captcha captcha);

	Captcha last(String account, String category, String type, String target);

	boolean updateUsed(String captchaId, boolean used);
}
