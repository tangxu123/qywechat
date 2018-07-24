package com.ludateam.wechat.qy.mvc.znzx;
/*
 * Copyright 2017 Luda Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created by Him on 2017/7/18.
 */


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HandlerKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;
import com.ludateam.wechat.qy.mvc.scheduler.QySchedulerController;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;
import java.net.URLEncoder;

import java.io.UnsupportedEncodingException;


@Controller("/wechat/znzx")
public class QyZnzxController extends BaseController {
    private static final Log log = Log.getLog(QySchedulerController.class);

    public void index() {
//        try {
//
//            String redirect_uri = URLEncoder.encode(PropKit.get("domain") + "/wechat/znzx/main", "utf-8");
//            String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123", "11", true);
//            System.out.println("codeUrl>>>" + codeUrl);
//            HandlerKit.redirect301(codeUrl, getRequest(), getResponse(),
//                    new boolean[] { true });
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }



        setAttr("userId", "13101040424");
        setAttr("openid", "");
        render("/znzx/index.html");

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
            render("/znzx/index.html");
        }
    }


}
