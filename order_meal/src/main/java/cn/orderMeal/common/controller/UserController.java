package cn.orderMeal.common.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.SnsApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import cn.orderMeal.common.cons.SessionConst;
import cn.orderMeal.common.kit.session.SessionKit;
import cn.orderMeal.common.vo.WeixinUserInfo;

public class UserController extends ApiController{

	
	/**
	 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9165032ccba66690&redirect_uri=http://172.20.10.14/user/login?appId=wx9165032ccba66690&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
	 */
	public void login(){
		String code = getPara("code");
		//String state = getPara("state");
		ApiConfig ac = ApiConfigKit.getApiConfig();
		SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(ac.getAppId(), ac.getAppSecret(),code);
		ApiResult apiResult = SnsApi.getUserInfo(snsAccessToken.getAccessToken(), snsAccessToken.getOpenid());
		if (apiResult.isSucceed()){
			WeixinUserInfo weixinUserInfo = JSON.parseObject(apiResult.getJson(), WeixinUserInfo.class);
			SessionKit.setAttr(SessionConst.WEIXIN_USERINFO, weixinUserInfo);
			renderText(SessionKit.tokenLocal.get());
			return;
		}
		renderText("微信获取用户信息失败");
	}
	
	public void getWeixinUserInfo(){
		renderJson(SessionKit.getAttr(SessionConst.WEIXIN_USERINFO));
	}
	
}
