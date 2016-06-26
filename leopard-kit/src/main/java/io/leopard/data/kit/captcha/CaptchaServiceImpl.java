package io.leopard.data.kit.captcha;

import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.leopard.core.exception.forbidden.CaptchaWrongException;
import io.leopard.jdbc.Jdbc;

//@Service
public class CaptchaServiceImpl implements CaptchaService {

	/**
	 * 类别，如：captcha:图片验证码,seccode:短信验证码
	 * 
	 */

	private Jdbc jdbc;

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	private CaptchaDao captchaDao;

	@PostConstruct
	public void init() {
		CaptchaDaoMysqlImpl captchaDaoMysqlImpl = new CaptchaDaoMysqlImpl();
		captchaDaoMysqlImpl.setJdbc(jdbc);
		this.captchaDao = captchaDaoMysqlImpl;
	}

	@Override
	public Captcha checkCaptcha(String account, String target, String captcha) throws CaptchaWrongException {
		return this.check(account, CaptchaCategory.CAPTCHA, target, captcha);
	}

	@Override
	public Captcha checkSeccode(String account, String target, String captcha) throws CaptchaWrongException {
		return this.check(account, CaptchaCategory.SECCODE, target, captcha);
	}

	@Override
	public Captcha check(String account, String category, String target, String captcha) throws CaptchaWrongException {
		Assert.hasText(account, "参数account不能为空");
		Assert.hasText(category, "参数category不能为空");
		Assert.hasText(target, "参数target不能为空");
		Assert.hasText(captcha, "参数captcha不能为空");

		// String securityCode2 = lastSecurityCode(mobile, type);
		Captcha bean = this.last(account, category, target);
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
	public String add(String account, String category, String type, String target, String captcha) {
		Assert.hasText(account, "参数account不能为空");
		Assert.hasText(category, "参数category不能为空");
		Assert.hasText(type, "参数type不能为空");
		Assert.hasText(target, "参数target不能为空");
		Assert.hasText(captcha, "参数captcha不能为空");

		String captchaId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

		Captcha bean = new Captcha();
		bean.setCaptchaId(captchaId);
		bean.setAccount(account);
		bean.setCategory(category);
		bean.setType(type);
		bean.setTarget(target);
		bean.setPosttime(new Date());
		bean.setCaptcha(captcha);
		bean.setUsed(false);
		captchaDao.add(bean);

		return captchaId;
	}

	protected String lastCaptcha(String account, String category, String target) {
		Captcha captcha = this.last(account, category, target);
		if (captcha == null) {
			return null;
		}
		return captcha.getCaptcha();
	}

	@Override
	public Captcha last(String account, String category, String target) {
		Assert.hasText(account, "参数account不能为空");
		Assert.hasText(category, "参数category不能为空");
		Assert.hasText(target, "参数target不能为空");
		return this.captchaDao.last(account, category, target);
	}

	@Override
	public boolean updateUsed(String captchaId, boolean used) {
		Assert.hasText(captchaId, "参数captchaId不能为空");
		return captchaDao.updateUsed(captchaId, used);
	}

	@Override
	public String send(String account, String category, String type, String target, String content) {
		String captcha = lastCaptcha(account, category, target);
		if (captcha == null) {
			String str = Long.toString(System.nanoTime());
			captcha = str.substring(str.length() - 4);
			this.add(account, category, type, target, captcha);
		}
		content = content.replaceFirst("{captcha}", captcha);
		// DebugUtil.setDebug(content);
		this.debug(content);
		return captcha;
	}

	private void debug(String content) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attr == null) {
			return;
		}
		HttpServletRequest request = attr.getRequest();
		request.setAttribute("debug", content);
	}

	@Override
	public String sendCaptcha(String account, String type, String target) {
		return this.send(account, CaptchaCategory.CAPTCHA, type, target, "您的验证码:{captcha}");
	}

	@Override
	public String sendSeccode(String account, String type, String target) {
		return this.send(account, CaptchaCategory.SECCODE, type, target, "您的验证码:{captcha}");
	}

	@Override
	public String sendCaptcha(String account, String type, String target, String content) {
		return this.send(account, CaptchaCategory.CAPTCHA, type, target, content);
	}

	@Override
	public String sendSeccode(String account, String type, String target, String content) {
		return this.send(account, CaptchaCategory.SECCODE, type, target, content);
	}

}
