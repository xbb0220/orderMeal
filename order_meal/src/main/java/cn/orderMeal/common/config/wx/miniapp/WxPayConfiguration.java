package cn.orderMeal.common.config.wx.miniapp;

import org.apache.commons.lang3.StringUtils;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

/**
 * @author Binary Wang
 */
public class WxPayConfiguration {

  public static WxPayService wxPayService;
  
  private WxPayProperties properties;

  public WxPayConfiguration(WxPayProperties properties) {
    this.properties = properties;
  }

  public WxPayService wxService() {
    WxPayConfig payConfig = new WxPayConfig();
    payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppId()));
    payConfig.setMchId(StringUtils.trimToNull(this.properties.getMchId()));
    payConfig.setMchKey(StringUtils.trimToNull(this.properties.getMchKey()));
    payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
    payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
    payConfig.setKeyPath(StringUtils.trimToNull(this.properties.getKeyPath()));

    // 可以指定是否使用沙箱环境
    payConfig.setUseSandboxEnv(false);

    WxPayService wxPayService = new WxPayServiceImpl();
    wxPayService.setConfig(payConfig);
    WxPayConfiguration.wxPayService = wxPayService;
    return wxPayService;
  }

}
