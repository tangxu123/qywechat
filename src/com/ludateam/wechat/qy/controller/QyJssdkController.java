package com.ludateam.wechat.qy.controller;
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
 * Created by Him on 2017/7/13.
 */

import com.jfinal.kit.HashKit;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.JsTicket;
import com.jfinal.qyweixin.sdk.api.JsTicketApi;
import com.jfinal.qyweixin.sdk.jfinal.ApiController;
import com.platform.annotation.Controller;

import java.util.UUID;

@Controller("/wechat/jssdk")
public class QyJssdkController extends ApiController {
    public void index() {
        JsTicket jsApiTicket = JsTicketApi.getTicket(JsTicketApi.JsApiType.jsapi);
        String jsapi_ticket = jsApiTicket.getTicket();
        String nonce_str = create_nonce_str();
        // 注意 URL 一定要动态获取，不能 hardcode.
        String url = "http://" + getRequest().getServerName() // 服务器地址
                // + ":"
                // + getRequest().getServerPort() //端口号
                + getRequest().getContextPath() // 项目名称
                + getRequest().getServletPath();// 请求页面或其他地址
        String qs = getRequest().getQueryString(); // 参数
        if (qs != null) {
            url = url + "?" + (getRequest().getQueryString());
        }
        // url="http://javen.tunnel.mobi/my_weixin/_front/share.jsp";
        System.out.println("url>>>>" + url);
        String timestamp = create_timestamp();
        // 这里参数的顺序要按照 key 值 ASCII 码升序排序
        //注意这里参数名必须全部小写，且必须有序
        String str = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;

        String signature = HashKit.sha1(str);

        System.out.println("corpId " + ApiConfigKit.getApiConfig().getCorpId()
                + "  nonceStr " + nonce_str + " timestamp " + timestamp);
        System.out.println("url " + url + " signature " + signature);
        System.out.println("nonceStr " + nonce_str + " timestamp " + timestamp);
        System.out.println(" jsapi_ticket " + jsapi_ticket);
        System.out.println("nonce_str  " + nonce_str);
        setAttr("appId", ApiConfigKit.getApiConfig().getCorpId());
        setAttr("nonceStr", nonce_str);
        setAttr("timestamp", timestamp);
        setAttr("url", url);
        setAttr("signature", signature);
        setAttr("jsapi_ticket", jsapi_ticket);

        render("share.jsp");

    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
}
