package cn.orderMeal.common.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import com.alibaba.fastjson.JSON;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Record;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.orderMeal.common.config.wx.miniapp.WxMaConfiguration;
import cn.orderMeal.common.cons.SessionConst;
import cn.orderMeal.common.kit.AjaxJson;
import cn.orderMeal.common.kit.session.SessionKit;
import cn.orderMeal.common.model.Guest;
import cn.orderMeal.common.service.GuestService;
import cn.orderMeal.common.service.impl.GuestServiceImpl;
import cn.orderMeal.common.vo.WeixinUserInfo;
import me.chanjar.weixin.common.error.WxErrorException;

public class GuestController extends BaseController{

	
	GuestService guestService = GuestServiceImpl.service;
	
//	public void applogin() {
//		ApiResult ar = new WxaUserApi().getSessionKey(getPara("code"));
//		@SuppressWarnings("unchecked")
//		Map<String, String> map = JSON.parseObject(ar.getJson(), Map.class);
//		String openid = map.get("openid");
//		String sessionKey = map.get("session_key");
//		SessionKit.setAttr(SessionConst.OPENID, openid);
//		SessionKit.setAttr(SessionConst.SESSION_KEY, sessionKey);
//		Guest guest = getGuestByOpenId(openid);
//		if (null == guest){
//			Guest saveGuest = new Guest().setOpenid(openid);
//			guestService.save(saveGuest);
//			SessionKit.setAttr(SessionConst.GUEST_INFO, saveGuest);
//		} else{
//			SessionKit.setAttr(SessionConst.GUEST_INFO, guest);
//		}
//		renderJson(AjaxJson.success().setData(SessionKit.tokenLocal.get()));
//	}
	
	public void applogin() throws WxErrorException {
		WxMaService maService = WxMaConfiguration.getMaService(getPara("appid", PropKit.get("appId")));
		WxMaJscode2SessionResult sessionInfo = maService.getUserService().getSessionInfo(getPara("code"));
		SessionKit.setAttr(SessionConst.OPENID, sessionInfo.getOpenid());
		SessionKit.setAttr(SessionConst.SESSION_KEY, sessionInfo.getSessionKey());
		Guest guest = getGuestByOpenId(sessionInfo.getOpenid());
		if (null == guest){
			Guest saveGuest = new Guest().setOpenid(sessionInfo.getOpenid());
			guestService.save(saveGuest);
			SessionKit.setAttr(SessionConst.GUEST_INFO, saveGuest);
		} else{
			SessionKit.setAttr(SessionConst.GUEST_INFO, guest);
		}
		renderJson(AjaxJson.success().setData(SessionKit.tokenLocal.get()));
	}
	
	
	public static void main(String args[]) {
		
		System.out.println(JsonKit.toJson(AjaxJson.success().setData(UUID.randomUUID().toString())));
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
	
	public void updateGuestInfo() throws UnsupportedEncodingException, IOException{
		String jsonStr = readPostStr();
		Guest guest = JSON.parseObject(jsonStr, Guest.class);
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
