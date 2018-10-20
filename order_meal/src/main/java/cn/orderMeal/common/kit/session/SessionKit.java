package cn.orderMeal.common.kit.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public class SessionKit {
	public static final int EXPIRE_SECONDS = 10 * 60;
	public static final String SESSION_KEY_PREFIX="session";
	public static Cache cache = Redis.use("main");
	public static ThreadLocal<String> tokenLocal = new ThreadLocal<>();
	
	public static void setAttr(String key, Object value){
		String token = StrKit.isBlank(tokenLocal.get()) ? UUID.randomUUID().toString() : tokenLocal.get();
		Map<String, Object> map = cache.get(SESSION_KEY_PREFIX + token);
		if (null != map){
			map.put(key, value);
		}
		else{
			map = new ConcurrentHashMap<>();
			map.put(key, value);
		}
		cache.setex(SESSION_KEY_PREFIX + token, EXPIRE_SECONDS, map);
		tokenLocal.set(token);
	}
	 
	@SuppressWarnings("unchecked")
	public static <T>T getAttr(String key){
		String token = StrKit.isBlank(tokenLocal.get()) ? UUID.randomUUID().toString() : tokenLocal.get();
		Map<String, Object> map = cache.get(SESSION_KEY_PREFIX + token);
		if (null == map){
			return null;
		}
		else{
			cache.setex(SESSION_KEY_PREFIX + token, EXPIRE_SECONDS, map);
			return (T) map.get(key);
		}
	}
	
	public static void removeAttr(String key){
		String token = StrKit.isBlank(tokenLocal.get()) ? UUID.randomUUID().toString() : tokenLocal.get();
		Map<String, Object> map = cache.get(SESSION_KEY_PREFIX + token);
		if (null != map){
			map.remove(key);
			cache.setex(SESSION_KEY_PREFIX + token, EXPIRE_SECONDS, map);
		}
	}
	
}
