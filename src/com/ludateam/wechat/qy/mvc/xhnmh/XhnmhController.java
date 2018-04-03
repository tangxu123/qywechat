package com.ludateam.wechat.qy.mvc.xhnmh;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
 
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

@Controller("/wechat/xhnmh")
public class XhnmhController extends BaseController {
	   private static final Log log = Log.getLog(XhnmhController.class);
	   public void index() {
	        try {
	            String redirect_uri = URLEncoder.encode(PropKit.get("domain") + "/wechat/xhnmh/main", "utf-8");
	            String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123","13" ,true);
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
	            System.out.println("code>>>" + code);
	            log.info("code:" + code);
	            if (!isParaBlank("state")) {
	                state = getPara("state");
	                log.info(" state:" + state);
	                System.out.println(" state:" + state);
	            }
	            ApiResult userInfoApiResult = OAuthApi.getUserInfoByCode(code);
	            if (userInfoApiResult.isSucceed()) {
	                String userInfoJson = userInfoApiResult.getJson();
	                JSONObject object = JSON.parseObject(userInfoJson);
	                deviceId = object.getString("DeviceId");
	                try {
	                    userId = object.getString("UserId");
	                    System.out.println("userId:" + userId);
	                    //如果获取userId为空 说明没有关注
	                    if (userId != null && !userId.equals("")) {
	                        ApiResult toOpenIdApiResult = OAuthApi.ToOpenId("{\"userid\":\"" + userId + "\",\"agentid\":" + ApiConfigKit.getApiConfig().getAgentId() + "}");
	                        System.out.println("toOpenIdApiResult:" + toOpenIdApiResult.getJson());
	                        if (toOpenIdApiResult.isSucceed()) {
	                            openid = JSON.parseObject(toOpenIdApiResult.getJson()).getString("openid");
	                        }
	                    } else {
	                        openid = object.getString("OpenId");
	                        String json = "{\"openid\":\"" + openid + "\"}";
	                        System.out.println("json..." + json);
	                        //如果未关注 openid无法转化为userid
	                        ApiResult toUserIdApiResult = OAuthApi.ToUserId(json);
	                        System.out.println("toUserIdApiResult:" + toUserIdApiResult.getJson());
	                        if (toUserIdApiResult.isSucceed()) {
	                            userId = JSON.parseObject(toUserIdApiResult.getJson()).getString("userid");
	                        }
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	            setSessionAttr("userId", userId);
	            setSessionAttr("openId", openid);
	            if (state.equals("123")) {
	                redirect("/");
	            }
	            //renderText(userInfoApiResult.getJson()+">>>userId:"+userId+" deviceId:"+deviceId+" openid:"+openid);
	            setAttr("userId", userId);
	            setAttr("openid", openid);
	            render("/xhnmh/index.html");
	        }
	    }
}
