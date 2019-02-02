package cn.orderMeal.common.kit.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import com.jfinal.handler.Handler;

public class CorsHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		enalbeCros(request, response);
		next.handle(target, request, response, isHandled);
	}

	private void enalbeCros(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers",
				"Content-Type, x-requested-with, X-Custom-Header, HaiYi-Access-Token");
		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpStatus.SC_NO_CONTENT);
		}
	}

}
