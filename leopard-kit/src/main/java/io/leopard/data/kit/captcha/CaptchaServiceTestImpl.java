package io.leopard.data.kit.captcha;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import io.leopard.core.exception.forbidden.CaptchaWrongException;

@Primary
@Service
public class CaptchaServiceTestImpl extends CaptchaServiceImpl {

	@Override
	public Captcha check(String mobile, String type, String captcha) throws CaptchaWrongException {
		if ("1234".equals(captcha)) {
			// TODO ahai 上线去掉
			Captcha bean = new Captcha();
			bean.setCaptchaId("null");
			return bean;
		}
		return super.check(mobile, type, captcha);
	}
}
