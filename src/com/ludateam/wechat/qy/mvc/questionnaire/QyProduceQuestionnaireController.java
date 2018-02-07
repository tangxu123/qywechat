package com.ludateam.wechat.qy.mvc.questionnaire;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HandlerKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.spring.Inject;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;
import com.ludateam.wechat.api.CallService;
import com.ludateam.wechat.qy.entity.TaxEnterprise;
import com.ludateam.wechat.qy.vo.BindingRep;
import com.ludateam.wechat.qy.vo.VipSqidRep;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

@Controller("/wechat/questionnaire")
public class QyProduceQuestionnaireController extends BaseController {

	private static final Log log = Log
			.getLog(QyProduceQuestionnaireController.class);

	@Inject.BY_NAME
	private CallService callService;

	public void index() {
		try {
			String redirect_uri = URLEncoder.encode(PropKit.get("domain")
					+ "/wechat/questionnaire/main", "utf-8");
			String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123", true);
			System.out.println("codeUrl>>>" + codeUrl);
			HandlerKit.redirect301(codeUrl, getRequest(), getResponse(),
					new boolean[] { true });
			// redirect(codeUrl);
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
			log.info("code:" + code);
			if (!isParaBlank("state")) {
				state = getPara("state");
				log.info(" state:" + state);
			}
			ApiResult userInfoApiResult = OAuthApi.getUserInfoByCode(code);
			log.info("-------------json--------------------:"
					+ userInfoApiResult.getJson());
			log.info("-------------errocode--------------------:"
					+ userInfoApiResult.getErrorCode());
			log.info("-------------errmsg--------------------:"
					+ userInfoApiResult.getErrorMsg());
			log.info("-------------isSucceed--------------------:"
					+ userInfoApiResult.isSucceed());
			if (userInfoApiResult.isSucceed()) {
				String userInfoJson = userInfoApiResult.getJson();
				JSONObject object = JSON.parseObject(userInfoJson);
				try {
					userId = object.getString("UserId");
					log.info("-------------userId--------------------:"
							+ userId);
					log.info("-------------userId--------------------:"
							+ userId);
					log.info("-------------userId--------------------:"
							+ userId);
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
						log.info("-------------openid--------------------:"
								+ openid);
						log.info("-------------openid--------------------:"
								+ openid);
						log.info("-------------openid--------------------:"
								+ openid);
						String json = "{\"openid\":\"" + openid + "\"}";
						// 如果未关�? openid无法转化为userid
						ApiResult toUserIdApiResult = OAuthApi.ToUserId(json);
						log.info("-------------openid to userid--------------------:"
								+ toUserIdApiResult.getJson());
						log.info("-------------openid to userid--------------------:"
								+ toUserIdApiResult.getJson());
						log.info("-------------openid to userid--------------------:"
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
			log.info("-------------userId--------------------:" + userId);
			log.info("-------------userId--------------------:" + userId);
			try {
				String nsrmc = "";
				String sqid = "";
				String djxh = "";
				String jsonString = callService.getBindingList(userId);
				BindingRep bindingRep = FastJson.getJson().parse(jsonString,
						BindingRep.class);
				if ("0".equals(bindingRep.getErrcode())) {
					List<TaxEnterprise> bindingList = bindingRep
							.getBindingList();
					for (TaxEnterprise taxEnterprise : bindingList) {
						if ("Y".equals(taxEnterprise.getIsUse())) {
							nsrmc = taxEnterprise.getNsrmc();
							djxh = taxEnterprise.getDjxh();
							break;
						}
					}

					String vipString = callService.getVipSqid(userId);
					VipSqidRep vipRep = FastJson.getJson().parse(vipString,
							VipSqidRep.class);
					if ("0".equals(vipRep.getErrcode())) {
						sqid = vipRep.getSqid();
					}
				}

				setAttr("sqid", sqid);
				setAttr("nsrmc", nsrmc);
				setAttr("userId", userId);
				setAttr("djxh", djxh);
			} catch (Exception e) {
				e.printStackTrace();
			}
			render("/questionnaire/index.html");
		}
	}

	public void show() {
		String nsrmc = "";
		String sqid = "";
		String userid = getRequest().getParameter("userid");
		String jsonString = callService.getBindingList(userid);
		BindingRep bindingRep = FastJson.getJson().parse(jsonString,
				BindingRep.class);
		if ("0".equals(bindingRep.getErrcode())) {
			List<TaxEnterprise> bindingList = bindingRep.getBindingList();
			for (TaxEnterprise taxEnterprise : bindingList) {
				if ("Y".equals(taxEnterprise.getIsUse())) {
					nsrmc = taxEnterprise.getNsrmc();
					break;
				}
			}

			String vipString = callService.getVipSqid(userid);
			VipSqidRep vipRep = FastJson.getJson().parse(vipString,
					VipSqidRep.class);
			if ("0".equals(vipRep.getErrcode())) {
				sqid = vipRep.getSqid();
			}
		}

		setAttr("sqid", sqid);
		setAttr("nsrmc", nsrmc);
		setAttr("userId", userid);
		render("/questionnaire/index.html");
	}
}
