package cn.orderMeal.common.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import com.alibaba.fastjson.JSON;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.orderMeal.common.config.wx.miniapp.WxMaConfiguration;
import cn.orderMeal.common.kit.CompressedFileKit;
import cn.orderMeal.common.kit.FileKit;
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
		try {
			String scene = getPara("scene"), page = getPara("page");
			WxMaService maService = WxMaConfiguration.getMaService(getPara("appid", PropKit.get("appId")));
			WxMaQrcodeService qrcodeService = maService.getQrcodeService();
			File createQrcode = qrcodeService.createWxaCodeUnlimit(scene, page);
			renderFile(createQrcode);
		}
		catch (Exception e) {
			renderText(e.toString());
		}
	}
	
	
	public void generateTablesQrCode() throws Exception {
		String page = PropKit.get("page.index");
		String scene = PropKit.get("page.table.para");
		
		String readPostStr = readPostStr();
		List<String> tables = JSON.parseArray(readPostStr, String.class);
		String zipFileName = UUID.randomUUID().toString();
		String dir = PathKit.getWebRootPath() + "/" + zipFileName;
		FileKit.createDir(dir);
		for (String table : tables) {
			WxMaService maService = WxMaConfiguration.getMaService(getPara("appid", PropKit.get("appId")));
			WxMaQrcodeService qrcodeService = maService.getQrcodeService();
			File createQrcode = qrcodeService.createWxaCodeUnlimit(scene+table, page);
			FileUtils.copyFile(createQrcode, new File(dir + "/" + table + ".jpg"));
		}
		new CompressedFileKit().compressedFile(dir, dir);
		renderFile(new File(dir + "/" + zipFileName + ".zip"));
	}
	
	
	public static void main(String[] args) {
		List<String> tables = new ArrayList<>();
		tables.add("1");
		tables.add("2");
		tables.add("3");
		tables.add("4");
		System.out.println(JSON.toJSONString(tables));
	}
}
