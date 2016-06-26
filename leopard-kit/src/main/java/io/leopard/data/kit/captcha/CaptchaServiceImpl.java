package io.leopard.data.kit.captcha;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import io.leopard.core.exception.forbidden.CaptchaWrongException;

//@Service
public class CaptchaServiceImpl implements CaptchaService {

	@Resource
	private CaptchaDao captchaDao;

	@Override
	public Captcha check(String mobile, String type, String captcha) throws CaptchaWrongException {
		// String securityCode2 = lastSecurityCode(mobile, type);
		Captcha bean = this.last(mobile, type);
		if (bean == null) {
			// System.err.println("class:" + this);
			throw new CaptchaWrongException(captcha);
		}
		if (!bean.getCaptcha().equals(captcha)) {
			throw new CaptchaWrongException(captcha);
		}
		return bean;
	}

	@Override
	public String add(String mobile, String type, String captcha) {
		String captchaId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

		Captcha bean = new Captcha();
		bean.setCaptchaId(captchaId);
		bean.setMobile(mobile);
		bean.setPosttime(new Date());
		bean.setCaptcha(captcha);
		bean.setType(type);
		bean.setUsed(false);
		captchaDao.add(bean);

		return captchaId;
	}

	protected String lastSecurityCode(String mobile, String type) {
		Captcha captcha = this.last(mobile, type);
		if (captcha == null) {
			return null;
		}
		return captcha.getCaptcha();
	}

	@Override
	public Captcha last(String mobile, String type) {
		// CheckUtil.isValidPassport(mobile);
		return this.captchaDao.last(mobile, type);
	}

	@Override
	public boolean updateUsed(String captchaId, boolean used) {
		return captchaDao.updateUsed(captchaId, used);
	}

}
