package io.leopard.jdbc.test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class H2SqlUtil {

	protected static String filter(String sql) {
		if (true) {
			// 临时过滤
			sql = sql.replace("DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", "DEFAULT NULL");
		}

		if (true) {
			sql = sql.replaceAll("/\\*.*?\\*/;", "");
			sql = sql.replaceAll("--[^\n]{0,}", "");
			sql = sql.replaceAll("  KEY [^\n]{0,}", "");
			sql = sql.replaceAll("  FULLTEXT KEY [^\n]{0,}", "");

			sql = sql.replace("UNIQUE KEY ", "UNIQUE INDEX ");
			sql = sql.replace("USING HASH", "");
			sql = sql.replace("double(11,0)", "double(11)");
			sql = sql.replaceAll("double\\([0-9]+,[0-9]+\\)", "double");

			sql = replaceUniqueKeyName(sql);

			sql = sql.replaceAll("COMMENT='.*?'", "");
			sql = sql.replaceAll("AUTO_INCREMENT=[0-9]+", "");
			sql = sql.replace("DEFAULT CHARSET=utf8mb4", "");
			sql = sql.replace("DEFAULT CHARSET=utf8", "");
			sql = sql.replace("ENGINE=InnoDB", "");
			sql = sql.replace("ENGINE=MyISAM", "");

			// ) ENGINE=MyISAM COMMENT='该表仅保存最后将公告同步到公告推送表';

			// create INDEX `idx_username_uu` on message (`game_id`,
			// `server_id`);
			// System.out.println(content);

		}
		return sql;
	}

	protected static String replaceUniqueKeyName(String content) {
		// UNIQUE KEY `un_groupId_activityId_username`
		String regex = "UNIQUE INDEX `(.*?)`";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String keyName = m.group(1);
			int rand = random.nextInt();
			String newKeyName = keyName + "_" + rand + "a'";
			String replacement = "UNIQUE INDEX `" + newKeyName + "`";
			m.appendReplacement(sb, replacement);
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
