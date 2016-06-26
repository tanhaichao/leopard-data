package io.leopard.data.kit.captcha;

import org.springframework.stereotype.Repository;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.builder.InsertBuilder;

@Repository
public class CaptchaDaoMysqlImpl implements CaptchaDao {

	private Jdbc jdbc;

	public Jdbc getJdbc() {
		return jdbc;
	}

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public boolean add(Captcha captcha) {
		InsertBuilder builder = new InsertBuilder("captcha");

		builder.setString("captchaId", captcha.getCaptchaId());
		builder.setString("category", captcha.getCategory());
		builder.setString("type", captcha.getType());
		builder.setString("target", captcha.getTarget());
		builder.setString("account", captcha.getAccount());
		builder.setString("captcha", captcha.getCaptcha());
		builder.setBool("used", captcha.isUsed());
		builder.setDate("posttime", captcha.getPosttime());

		return jdbc.insertForBoolean(builder);
	}

	@Override
	public Captcha last(String account, String category, String type, String target) {
		String sql = "select * from captcha where account=? and category=? and type=? and target=? and used=0 order by posttime desc limit 1";
		return this.jdbc.query(sql, Captcha.class, account, category, type, target);
	}

	@Override
	public boolean updateUsed(String captchaId, boolean used) {
		String sql = "update captcha set used=? where captchaId=?";
		return this.jdbc.updateForBoolean(sql, used, captchaId);
	}

}
