package cn.orderMeal.common.config;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
import cn.orderMeal.common.config.wx.miniapp.WxMaConfiguration;
import cn.orderMeal.common.config.wx.miniapp.WxMaProperties;
import cn.orderMeal.common.config.wx.miniapp.WxMaProperties.Config;
import cn.orderMeal.common.config.wx.miniapp.WxPayConfiguration;
import cn.orderMeal.common.config.wx.miniapp.WxPayProperties;
import cn.orderMeal.common.controller.AppPayController;
import cn.orderMeal.common.controller.DishController;
import cn.orderMeal.common.controller.DishTypeController;
import cn.orderMeal.common.controller.GuestController;
import cn.orderMeal.common.controller.OrderController;
import cn.orderMeal.common.controller.OrderItemController;
import cn.orderMeal.common.controller.QrcodeController;
import cn.orderMeal.common.kit.session.CorsHandler;
import cn.orderMeal.common.model._MappingKit;
import cn.orderMeal.common.task.OrderStatusTask;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jfinal 主配置类
 * @author xubinbin
 */
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
		me.add("/guest", GuestController.class);
		me.add("/dishType", DishTypeController.class);
		me.add("/dish", DishController.class);
		me.add("/order", OrderController.class);
		me.add("/orderItem", OrderItemController.class);
		me.add("/appPay", AppPayController.class);
		me.add("/qrcode", QrcodeController.class);
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
		arp.getEngine().setSourceFactory(new ClassPathSourceFactory());
		arp.addSqlTemplate("/sql/all.sql");
		arp.setShowSql(PropKit.getBoolean("devMode"));
		arp.setDialect(new MysqlDialect());
		dbPlugin.setDriverClass("com.mysql.jdbc.Driver");
		/********在此添加数据库 表-Model 映射*********/
		//如果使用了JFinal Model 生成器 生成了BaseModel 把下面注释解开即可
		_MappingKit.mapping(arp);
		
		//添加到插件列表中
		me.add(dbPlugin);
		me.add(arp);
		RedisPlugin redisPlugin = new RedisPlugin("main", PropKit.get("redis.host"), PropKit.getInt("redis.port"));
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(100);
		jedisPoolConfig.setMaxWaitMillis(10000);
		me.add(redisPlugin);
		
		
		Cron4jPlugin cp = new Cron4jPlugin();
		cp.addTask("* * * * *", new OrderStatusTask());
		me.add(cp);
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
		me.add(new CorsHandler());
	}
	
	/**
	 * JFinal启动后调用
	 */
	@Override
	public void afterJFinalStart() {
      WxaConfig wxaConfig = new WxaConfig();
      wxaConfig.setAppId(PropKit.get("appId"));
      wxaConfig.setAppSecret(PropKit.get("appSecret"));
      wxaConfig.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
      wxaConfig.setMessageEncrypt(PropKit.getBoolean("encryptMessage", false));
      wxaConfig.setToken(PropKit.get("token"));
      WxaConfigKit.setDevMode(PropKit.getBoolean("devMode"));
      WxaConfigKit.setWxaConfig(wxaConfig);
      
      configWxMiniApp();
      configWxPay();
	}
	
	private void configWxMiniApp() {
		Config config = new Config();
		config.setAppid(PropKit.get("appId"));
		config.setSecret(PropKit.get("appSecret"));
		config.setAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		config.setMsgDataFormat("JSON");
		config.setToken(PropKit.get("token"));
		
		WxMaProperties wxMaProperties = new WxMaProperties();
		List<Config> configs = new ArrayList<>();
		configs.add(config);
		wxMaProperties.setConfigs(configs);
		
		WxMaConfiguration wxMaConfiguration = new WxMaConfiguration(wxMaProperties);
		wxMaConfiguration.services();
	}
	
	private void configWxPay() {
		WxPayProperties wxPayProperties = new WxPayProperties();
		wxPayProperties.setAppId(PropKit.get("appId"));
		wxPayProperties.setMchId(PropKit.get("mch_id"));
		wxPayProperties.setMchKey(PropKit.get("signKey"));
		
		WxPayConfiguration wxPayConfiguration = new WxPayConfiguration(wxPayProperties);
		wxPayConfiguration.wxService();
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
		JFinal.start("src/main/webapp", 8082, "/");
	}
	

}