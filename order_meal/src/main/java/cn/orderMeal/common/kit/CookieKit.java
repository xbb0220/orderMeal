package cn.orderMeal.common.kit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CookieKit {
	
	public static String getCookie(HttpServletRequest request, String name, String defaultValue) {
		Cookie cookie = getCookieObject(request, name);
		return cookie != null ? cookie.getValue() : defaultValue;
	}
	
	private static Cookie getCookieObject(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(name))
					return cookie;
		return null;
	}

	/**
	 * Set Cookie.
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds -1: clear cookie when close browser. 0: clear cookie immediately.  n>0 : max age in n seconds.
	 * @param isHttpOnly true if this cookie is to be marked as HttpOnly, false otherwise
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, boolean isHttpOnly) {
		 doSetCookie(response, name, value, maxAgeInSeconds, null, null, isHttpOnly);
	}
	
	/**
	 * Set Cookie.
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds -1: clear cookie when close browser. 0: clear cookie immediately.  n>0 : max age in n seconds.
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		doSetCookie(response, name, value, maxAgeInSeconds, null, null, null);
	}
	
	public static void setCookie(HttpServletResponse response, Cookie cookie) {
		response.addCookie(cookie);
	}
	
	/**
	 * Set Cookie to response.
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds -1: clear cookie when close browser. 0: clear cookie immediately.  n>0 : max age in n seconds.
	 * @param path see Cookie.setPath(String)
	 * @param isHttpOnly true if this cookie is to be marked as HttpOnly, false otherwise
	 */
	public void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path, boolean isHttpOnly) {
		doSetCookie(response, name, value, maxAgeInSeconds, path, null, isHttpOnly);
	}
	
	/**
	 * Set Cookie to response.
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds -1: clear cookie when close browser. 0: clear cookie immediately.  n>0 : max age in n seconds.
	 * @param path see Cookie.setPath(String)
	 */
	public void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path) {
		doSetCookie(response, name, value, maxAgeInSeconds, path, null, null);
	}
	
	/**
	 * Set Cookie to response.
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds -1: clear cookie when close browser. 0: clear cookie immediately.  n>0 : max age in n seconds.
	 * @param path see Cookie.setPath(String)
	 * @param domain the domain name within which this cookie is visible; form is according to RFC 2109
	 * @param isHttpOnly true if this cookie is to be marked as HttpOnly, false otherwise
	 */
	public void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path, String domain, boolean isHttpOnly) {
		doSetCookie(response, name, value, maxAgeInSeconds, path, domain, isHttpOnly);
	}
	
	/**
	 * Remove Cookie.
	 */
	public void removeCookie(HttpServletResponse response, String name) {
		doSetCookie(response, name, null, 0, null, null, null);
	}
	
	/**
	 * Remove Cookie.
	 */
	public void removeCookie(HttpServletResponse response, String name, String path) {
		doSetCookie(response, name, null, 0, path, null, null);
	}
	
	/**
	 * Remove Cookie.
	 */
	public void removeCookie(HttpServletResponse response, String name, String path, String domain) {
		 doSetCookie(response, name, null, 0, path, domain, null);
	}
	
	private static void doSetCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path, String domain, Boolean isHttpOnly) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAgeInSeconds);
		// set the default path value to "/"
		if (path == null) {
			path = "/";
		}
		cookie.setPath(path);
		
		if (domain != null) {
			cookie.setDomain(domain);
		}
		if (isHttpOnly != null) {
			cookie.setHttpOnly(isHttpOnly);
		}
		response.addCookie(cookie);
	}

	
}
