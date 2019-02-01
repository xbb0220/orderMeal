package cn.orderMeal.common.kit.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import cn.orderMeal.common.kit.CookieKit;



public class SessionHandler extends Handler {
	
	public Cache cache = Redis.use("main");

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		enalbeCros(request, response);
		String token = CookieKit.getCookie(request, "token", null);
		if (StrKit.isBlank(token)){
			token = request.getParameter("token");
		}
		if (StrKit.isBlank(token)) {
			token = request.getHeader("token");
		}
		try {
			SessionKit.tokenLocal.set(token);
			SessionKit.requestLocal.set(request);
			SessionKit.responseLocal.set(response);
			next.handle(target, request, response, isHandled);
		} finally {
			if (!StrKit.isBlank(SessionKit.tokenLocal.get())) {
				cache.expire(SessionKit.SESSION_KEY_PREFIX + SessionKit.tokenLocal.get(), SessionKit.EXPIRE_SECONDS);
				CookieKit.setCookie(response, "token", token, SessionKit.EXPIRE_SECONDS);
			}
			SessionKit.tokenLocal.remove();
			SessionKit.requestLocal.remove();
			SessionKit.responseLocal.remove();
		}
	}
	
	private void enalbeCros(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, HaiYi-Access-Token");
		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpStatus.SC_NO_CONTENT);
		}
	}
	
}
