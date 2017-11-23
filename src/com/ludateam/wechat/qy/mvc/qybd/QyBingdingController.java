package com.ludateam.wechat.qy.mvc.qybd;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.spring.Inject;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;
import com.ludateam.wechat.api.CallService;
import com.ludateam.wechat.qy.entity.TaxEnterprise;
import com.ludateam.wechat.qy.vo.BindingRep;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

@Controller("/wechat/qybd")
public class QyBingdingController extends BaseController {

	private static final Log log = Log.getLog(QyBingdingController.class);

	@Inject.BY_NAME
	private CallService callService;

	public void index() {
		try {
			String redirect_uri = URLEncoder.encode(PropKit.get("domain")
					+ "/wechat/qybd/main", "utf-8");
			String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123", true);
			System.out.println("codeUrl>>>" + codeUrl);
			redirect(codeUrl);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void main() {
		String userId = null;
		String deviceId = null;
		String openid = null;
		String state = null;
		if (!isParaBlank("code")) {
			String code = getPara("code");
			log.info("code:" + code);
			if (!isParaBlank("state")) {
				state = getPara("state");
				log.info(" state:" + state);
			}
			ApiResult userInfoApiResult = OAuthApi.getUserInfoByCode(code);
			if (userInfoApiResult.isSucceed()) {
				String userInfoJson = userInfoApiResult.getJson();
				JSONObject object = JSON.parseObject(userInfoJson);
				deviceId = object.getString("DeviceId");
				try {
					userId = object.getString("UserId");
					System.out.println("userId:" + userId);
					// 如果获取userId为空 说明没有关注
					if (userId != null && !userId.equals("")) {
						ApiResult toOpenIdApiResult = OAuthApi
								.ToOpenId("{\"userid\":\""
										+ userId
										+ "\",\"agentid\":"
										+ ApiConfigKit.getApiConfig()
												.getAgentId() + "}");
						System.out.println("toOpenIdApiResult:"
								+ toOpenIdApiResult.getJson());
						if (toOpenIdApiResult.isSucceed()) {
							openid = JSON.parseObject(
									toOpenIdApiResult.getJson()).getString(
									"openid");
						}
					} else {
						openid = object.getString("OpenId");
						String json = "{\"openid\":\"" + openid + "\"}";
						System.out.println("json..." + json);
						// 如果未关注 openid无法转化为userid
						ApiResult toUserIdApiResult = OAuthApi.ToUserId(json);
						System.out.println("toUserIdApiResult:"
								+ toUserIdApiResult.getJson());
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

			try {
				String jsonString = callService.getBindingList(userId);
				BindingRep bindingRep = FastJson.getJson().parse(jsonString,
						BindingRep.class);
				if ("0".equals(bindingRep.getErrcode())) {
					setAttr("bindingList", bindingRep.getBindingList());
				} else {
					setAttr("bindingList", new ArrayList<TaxEnterprise>());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			setAttr("userId", userId);
			setAttr("openid", openid);
			render("/qybd/index.html");
		}
	}

	public void list() {

		String userid = getRequest().getParameter("userid");
		String jsonString = callService.getBindingList(userid);
		BindingRep bindingRep = FastJson.getJson().parse(jsonString,
				BindingRep.class);

		if ("0".equals(bindingRep.getErrcode())) {
			setAttr("bindingList", bindingRep.getBindingList());
		} else {
			setAttr("bindingList", new ArrayList<TaxEnterprise>());
		}

		setAttr("userId", userid);
		render("/qybd/index.html");
	}

	public void update() {
		try {
			String userid = getRequest().getParameter("userid");
			String djxh = getRequest().getParameter("djxh");
			if (userid == null || "".equals(userid)) {
				renderJson("{\"errmsg\":\"用户id不能为空\"}");
			}
			if (djxh == null || "".equals(djxh)) {
				renderJson("{\"errmsg\":\"请选择一家企业\"}");
			}
			String jsonString = callService.setDefaultCompany(userid, djxh);
			renderJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
