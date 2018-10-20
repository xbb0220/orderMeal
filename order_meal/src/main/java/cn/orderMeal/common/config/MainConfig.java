package cn.orderMeal.common.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

import cn.orderMeal.common.controller.DepartmentController;
import cn.orderMeal.common.controller.UserController;
import cn.orderMeal.common.kit.session.SessionHandler;
import cn.orderMeal.common.model._MappingKit;
import redis.clients.jedis.JedisPoolConfig;

import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.render.ViewType;

public class MainConfig extends JFinalConfig {
	/**
	 * 配置JFinal常量
	 */
	@Override
	public void configConstant(Constants me) {
		//读取数据库配置文件
		PropKit.use("config.properties");
		//设置当前是否为开发模式
		me.setDevMode(PropKit.getBoolean("devMode"));
		//设置默认上传文件保存路径 getFile等使用
		me.setBaseUploadPath("upload/temp/");
		//设置上传最大限制尺寸
		//me.setMaxPostSize(1024*1024*10);
		//设置默认下载文件路径 renderFile使用
		me.setBaseDownloadPath("download");
		//设置默认视图类型
		me.setViewType(ViewType.JFINAL_TEMPLATE);
		//设置404渲染视图
		//me.setError404View("404.html");
	}
	/**
	 * 配置JFinal路由映射
	 */
	@Override
	public void configRoute(Routes me) {
		me.add("department", DepartmentController.class);
		me.add("/user", UserController.class);
	}
	/**
	 * 配置JFinal插件
	 * 数据库连接池
	 * ORM
	 * 缓存等插件
	 * 自定义插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		//配置数据库连接池插件
		DruidPlugin dbPlugin=new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		//orm映射 配置ActiveRecord插件
		ActiveRecordPlugin arp=new ActiveRecordPlugin(dbPlugin);
		arp.setShowSql(PropKit.getBoolean("devMode"));
		arp.setDialect(new MysqlDialect());
		dbPlugin.setDriverClass("com.mysql.jdbc.Driver");
		/********在此添加数据库 表-Model 映射*********/
		//如果使用了JFinal Model 生成器 生成了BaseModel 把下面注释解开即可
		_MappingKit.mapping(arp);
		
		//添加到插件列表中
		me.add(dbPlugin);
		me.add(arp);
		
		RedisPlugin redisPlugin = new RedisPlugin("main", "localhost");
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(100);
		jedisPoolConfig.setMaxWaitMillis(10000);
		me.add(redisPlugin);
	}
	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		me.addGlobalActionInterceptor(new SessionInViewInterceptor());
	}
	
	/**
	 * 配置全局处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new SessionHandler());
	}
	
	/**
	 * JFinal启动后调用
	 */
	@Override
	public void afterJFinalStart() {
        // 1.5 之后支持redis存储access_token、js_ticket，需要先启动RedisPlugin
//      ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache());
      // 1.6新增的2种初始化
//      ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache(Redis.use("weixin")));
//      ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache("weixin"));

      ApiConfig ac = new ApiConfig();
      // 配置微信 API 相关参数
      ac.setToken(PropKit.get("token"));
      ac.setAppId(PropKit.get("appId"));
      ac.setAppSecret(PropKit.get("appSecret"));
      
      /**
       *  是否对消息进行加密，对应于微信平台的消息加解密方式：
       *  1：true进行加密且必须配置 encodingAesKey
       *  2：false采用明文模式，同时也支持混合模式
       */
      ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
      ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));

      /**
       * 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认。
       */
      ApiConfigKit.putApiConfig(ac);
	}
	/**
	 * JFinal Stop之前调用 
	 */
	@Override
	public void beforeJFinalStop() {
		
	}
	
	/**
	 * 配置模板引擎 
	 */
	@Override
	public void configEngine(Engine me) {
		//这里只有选择JFinal TPL的时候才用
		//配置共享函数模板
		//me.addSharedFunction("/view/common/layout.html")
	}
	
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
	}
	

}