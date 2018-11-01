package cn.orderMeal.common.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.SnsApi;
import com.jfinal.wxaapp.api.WxaUserApi;

import cn.orderMeal.common.cons.SessionConst;
import cn.orderMeal.common.kit.AjaxJson;
import cn.orderMeal.common.kit.session.SessionKit;
import cn.orderMeal.common.model.Guest;
import cn.orderMeal.common.service.GuestService;
import cn.orderMeal.common.service.impl.GuestServiceImpl;
import cn.orderMeal.common.vo.WeixinUserInfo;

public class GuestController extends BaseController{

	
	GuestService guestService = GuestServiceImpl.service;
	
	/**
	 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9165032ccba66690&redirect_uri=http://xbb.jscssui.cn/guest/login?appId=wx9165032ccba66690&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
	 */
	public void login(){
		String code = getPara("code");
		String state = getPara("state");
		ApiConfig ac = ApiConfigKit.getApiConfig();
		SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(ac.getAppId(), ac.getAppSecret(),code);
		ApiResult apiResult = SnsApi.getUserInfo(snsAccessToken.getAccessToken(), snsAccessToken.getOpenid());
		if (apiResult.isSucceed()){
			WeixinUserInfo weixinUserInfo = JSON.parseObject(apiResult.getJson(), WeixinUserInfo.class);
			SessionKit.setAttr(SessionConst.WEIXIN_USERINFO, weixinUserInfo);
			
			Guest guest = getGuestByOpenId(weixinUserInfo.getOpenid());
			if (null == guest){
				Guest saveGuest = new Guest().setOpenid(weixinUserInfo.getOpenid());
				guestService.save(saveGuest);
				SessionKit.setAttr(SessionConst.GUEST_INFO, saveGuest);
			} else{
				SessionKit.setAttr(SessionConst.GUEST_INFO, guest);
			}
			redirect(state + "?token=" + SessionKit.tokenLocal.get());
			return;
		}
		renderText("微信获取用户信息失败");
		return;
	}
	
	public void applogin() {
		ApiResult ar = new WxaUserApi().getSessionKey(getPara("code"));
		System.out.println(ar.getJson());
		String openid = "";
		String sessionKey = "";
		SessionKit.setAttr(SessionConst.OPENID, openid);
		SessionKit.setAttr(SessionConst.SESSION_KEY, sessionKey);
		Guest guest = getGuestByOpenId(openid);
		if (null == guest){
			Guest saveGuest = new Guest().setOpenid(openid);
			guestService.save(saveGuest);
			SessionKit.setAttr(SessionConst.GUEST_INFO, saveGuest);
		} else{
			SessionKit.setAttr(SessionConst.GUEST_INFO, guest);
		}
		renderJson(AjaxJson.success().setData(SessionKit.tokenLocal.get()));
	}
	
	/**
	 * 通过openid获取用户信息
	 * @param openid
	 * @return
	 */
	private Guest getGuestByOpenId(String openid){
		Guest guest = new Guest();
		guest.setOpenid(openid);
		guest = guestService.getByEqualAttr(false, guest, "openid");
		return guest;
	}
	
	
	public void currentUserInfo(){
		WeixinUserInfo weixinUserInfo = SessionKit.getAttr(SessionConst.WEIXIN_USERINFO);
		Guest guest = SessionKit.getAttr(SessionConst.GUEST_INFO);
		Record result = new Record();
		result.set("guestInfo", guest).set("weixinInfo", weixinUserInfo);
		setCookie("token", SessionKit.tokenLocal.get(), SessionKit.EXPIRE_SECONDS);
		renderJson(result);
	}
	
	public void updateGuestInfo(){
		Guest guest = simpleModel(Guest.class);
		Guest sessionGuest = SessionKit.getAttr(SessionConst.GUEST_INFO);
		guest.setId(sessionGuest.getId());
		guest.setOpenid(sessionGuest.getOpenid());
		if (guestService.update(guest)){
			SessionKit.setAttr(SessionConst.GUEST_INFO, guest);
			renderJson(AjaxJson.success());
		} else{
			renderJson(AjaxJson.failure());
		}
	}
	
}
