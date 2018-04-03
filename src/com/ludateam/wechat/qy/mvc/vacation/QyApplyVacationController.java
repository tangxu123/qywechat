package com.ludateam.wechat.qy.mvc.vacation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HandlerKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;
import com.ludateam.wechat.qy.utils.MD5Utils;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

@Controller("/wechat/vacation")
public class QyApplyVacationController extends BaseController {

	private static final Log log = Log.getLog(QyApplyVacationController.class);

	public void index() {
		try {
			String redirect_uri = URLEncoder.encode(PropKit.get("domain") + "/wechat/vacation/main", "utf-8");
			String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123", "19", true);
			System.out.println("codeUrl>>>" + codeUrl);
			HandlerKit.redirect301(codeUrl, getRequest(), getResponse(),
					new boolean[] { true });
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void main() {
		String userId = null;
		String openid = null;
		String state = null;
		if (!isParaBlank("code")) {
			String code = getPara("code");
			if (!isParaBlank("state")) {
				state = getPara("state");
			}
			ApiResult userInfoApiResult = OAuthApi.getUserInfoByCode(code);
			if (userInfoApiResult.isSucceed()) {
				String userInfoJson = userInfoApiResult.getJson();
				JSONObject object = JSON.parseObject(userInfoJson);
				try {
					userId = object.getString("UserId");
					// 如果获取userId为空 说明没有关注
					if (userId != null && !userId.equals("")) {
						ApiResult toOpenIdApiResult = OAuthApi
								.ToOpenId("{\"userid\":\""
										+ userId
										+ "\",\"agentid\":"
										+ ApiConfigKit.getApiConfig()
												.getAgentId() + "}");
						if (toOpenIdApiResult.isSucceed()) {
							openid = JSON.parseObject(
									toOpenIdApiResult.getJson()).getString(
									"openid");
						}
					} else {
						openid = object.getString("OpenId");
						String json = "{\"openid\":\"" + openid + "\"}";
						// 如果未关�? openid无法转化为userid
						ApiResult toUserIdApiResult = OAuthApi.ToUserId(json);
						if (toUserIdApiResult.isSucceed()) {
							userId = JSON.parseObject(
									toUserIdApiResult.getJson()).getString(
									"userid");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			setSessionAttr("userId", userId);
			setSessionAttr("openId", openid);
			String md5UserId = MD5Utils.encodeString(userId);
			setAttr("userId", userId);
			setAttr("openId", openid);
			setAttr("wxzhid", md5UserId);
			
			log.info("-------------userId--------------------:" + userId);
			log.info("-------------userId--------------------:" + userId);
			log.info("-------------userId--------------------:" + userId);
			log.info("-------------userId--------------------:" + userId);
			log.info("-------------md5UserId--------------------:" + md5UserId);
			log.info("-------------md5UserId--------------------:" + md5UserId);
			log.info("-------------md5UserId--------------------:" + md5UserId);
			log.info("-------------md5UserId--------------------:" + md5UserId);

			render("/vacation/index.html");
		}
	}

}
