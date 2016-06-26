package io.leopard.data.kit.captcha;

import io.leopard.core.exception.forbidden.CaptchaWrongException;

public interface CaptchaService {

	String add(String account, String category, String type, String target, String captcha);

	Captcha last(String account, String category, String type, String target);

	boolean updateUsed(String captchaId, boolean used);

	Captcha check(String account, String category, String type, String target, String captcha) throws CaptchaWrongException;

	/**
	 * 发送验证码
	 * 
	 * @param account 账号，如手机号码、邮箱地址
	 * @param category 类别:captcha:图片验证码 seccode:文本验证码
	 * @param type 类型:mobile:手机，email:邮件
	 * @param target 目标，即在哪里使用
	 * @param content 消息模板
	 * @return
	 */
	String send(String account, String category, String type, String target, String content);

}
