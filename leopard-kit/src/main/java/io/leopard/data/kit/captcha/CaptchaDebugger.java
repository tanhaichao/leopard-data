package io.leopard.data.kit.captcha;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CaptchaDebugger {
	private static ThreadLocal<String> CAPTCHA = new ThreadLocal<String>();

	public static String getCaptcha() {
		return CAPTCHA.get();
	}

	public static void debug(String captcha, String content) {
		CAPTCHA.set(captcha);
		httpDebug(content);
	}

	public static void httpDebug(String content) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attr == null) {
			return;
		}
		HttpServletRequest request = attr.getRequest();
		request.setAttribute("debug", content);
	}

}
