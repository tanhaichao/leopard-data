package io.leopard.data.kit.captcha;

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;

import io.leopard.core.exception.forbidden.CaptchaWrongException;
import io.leopard.jdbc.Jdbc;

//@Service
public class CaptchaServiceImpl implements CaptchaService {

	/**
	 * 类别，如：captcha:图片验证码,seccode:短信验证码
	 * 
	 */

	private Jdbc jdbc;

	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	private CaptchaDao captchaDao;

	@PostConstruct
	public void init() {
		CaptchaDaoMysqlImpl captchaDaoMysqlImpl = new CaptchaDaoMysqlImpl();
		captchaDaoMysqlImpl.setJdbc(jdbc);
		captchaDaoMysqlImpl.setCategory(category);
		this.captchaDao = captchaDaoMysqlImpl;
	}

	@Override
	public Captcha check(String account, String type, String captcha) throws CaptchaWrongException {
		// String securityCode2 = lastSecurityCode(mobile, type);
		Captcha bean = this.last(account, type);
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
	public String add(String account, String type, String captcha) {
		String captchaId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

		Captcha bean = new Captcha();
		bean.setCaptchaId(captchaId);
		bean.setAccount(account);
		bean.setPosttime(new Date());
		bean.setCaptcha(captcha);
		bean.setType(type);
		bean.setUsed(false);
		captchaDao.add(bean);

		return captchaId;
	}

	protected String lastSecurityCode(String account, String type) {
		Captcha captcha = this.last(account, type);
		if (captcha == null) {
			return null;
		}
		return captcha.getCaptcha();
	}

	@Override
	public Captcha last(String account, String type) {
		return this.captchaDao.last(account, type);
	}

	@Override
	public boolean updateUsed(String captchaId, boolean used) {
		return captchaDao.updateUsed(captchaId, used);
	}

}
