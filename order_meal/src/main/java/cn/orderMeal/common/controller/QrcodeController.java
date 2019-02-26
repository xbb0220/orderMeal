package cn.orderMeal.common.controller;

import java.io.File;

import com.jfinal.kit.PropKit;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.orderMeal.common.config.wx.miniapp.WxMaConfiguration;
import me.chanjar.weixin.common.error.WxErrorException;

public class QrcodeController extends BaseController{

	public void index() throws WxErrorException {
		String path = getPara("path");
		Integer width = getParaToInt("width", 200);
		WxMaService maService = WxMaConfiguration.getMaService(getPara("appid", PropKit.get("appId")));
		WxMaQrcodeService qrcodeService = maService.getQrcodeService();
		File createQrcode = qrcodeService.createQrcode(path, width);
//		FileUtils.copyFile(srcFile, destFile);
		renderFile(createQrcode);
	}
	
	public void unlimit() throws WxErrorException {
		String scene = getPara("scene"), page = getPara("page");
		WxMaService maService = WxMaConfiguration.getMaService(getPara("appid", PropKit.get("appId")));
		WxMaQrcodeService qrcodeService = maService.getQrcodeService();
		File createQrcode = qrcodeService.createWxaCodeUnlimit(scene, page);
		renderFile(createQrcode);
	}
	
}
