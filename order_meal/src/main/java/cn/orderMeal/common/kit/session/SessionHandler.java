package cn.orderMeal.common.kit.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public class SessionHandler extends Handler {
	
	public Cache cache = Redis.use("main");

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		try {
			String token = request.getParameter("token");
			SessionKit.tokenLocal.set(token);
			next.handle(target, request, response, isHandled);
		} finally {
			if (!StrKit.isBlank(SessionKit.tokenLocal.get())) {
				cache.expire(SessionKit.SESSION_KEY_PREFIX + SessionKit.tokenLocal.get(), SessionKit.EXPIRE_SECONDS);
			}
			SessionKit.tokenLocal.remove();
		}
	}

}
