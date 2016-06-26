package io.leopard.data.kit.captcha;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.builder.InsertBuilder;

@Repository
public class CaptchaDaoMysqlImpl implements CaptchaDao {

	@Resource
	private Jdbc jdbc;

	@Override
	public boolean add(Captcha captcha) {
		InsertBuilder builder = new InsertBuilder("captcha");

		builder.setString("captchaId", captcha.getCaptchaId());
		builder.setString("type", captcha.getType());
		builder.setString("mobile", captcha.getMobile());
		builder.setString("captcha", captcha.getCaptcha());
		builder.setBool("used", captcha.isUsed());
		builder.setDate("posttime", captcha.getPosttime());

		return jdbc.insertForBoolean(builder);
	}

	@Override
	public Captcha last(String mobile, String type) {
		String sql = "select * from captcha where mobile=? and type=? and used=0 order by posttime desc limit 1";
		return this.jdbc.query(sql, Captcha.class, mobile, type);
	}

	@Override
	public boolean updateUsed(String captchaId, boolean used) {
		String sql = "update captcha set used=? where captchaId=?";
		return this.jdbc.updateForBoolean(sql, used, captchaId);
	}

}
