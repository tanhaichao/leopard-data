package io.leopard.data.kit.captcha;

import org.springframework.stereotype.Repository;

import io.leopard.jdbc.Jdbc;
import io.leopard.jdbc.builder.InsertBuilder;

@Repository
public class CaptchaDaoMysqlImpl implements CaptchaDao {

	private Jdbc jdbc;

	private String category;

	public Jdbc getJdbc() {
		return jdbc;
	}

	public void setJdbc(Jdbc jdbc) {
		this.jdbc = jdbc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean add(Captcha captcha) {
		InsertBuilder builder = new InsertBuilder(category);

		builder.setString("captchaId", captcha.getCaptchaId());
		builder.setString("type", captcha.getType());
		builder.setString("account", captcha.getAccount());
		builder.setString("captcha", captcha.getCaptcha());
		builder.setBool("used", captcha.isUsed());
		builder.setDate("posttime", captcha.getPosttime());

		return jdbc.insertForBoolean(builder);
	}

	@Override
	public Captcha last(String account, String type) {
		String sql = "select * from " + category + " where account=? and type=? and used=0 order by posttime desc limit 1";
		return this.jdbc.query(sql, Captcha.class, account, type);
	}

	@Override
	public boolean updateUsed(String captchaId, boolean used) {
		String sql = "update " + category + " set used=? where captchaId=?";
		return this.jdbc.updateForBoolean(sql, used, captchaId);
	}

}
