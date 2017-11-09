package com.ludateam.wechat.qy.controller;/*
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
 * Created by Him on 2017/11/8.
 */

import com.jfinal.json.FastJson;
import com.jfinal.json.Json;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.utils.HttpUtils;
import com.jfinal.template.ext.directive.Str;
import com.ludateam.wechat.qy.vo.SubmitRsp;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;
import com.platform.tools.security.md.ToolMD5;
import com.ludateam.wechat.qy.vo.SubmitReq;
import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

@Controller("/wechat/sms")
public class SendSMSController extends BaseController {


    private static final Log log = Log.getLog(SendSMSController.class);

    public void send() {

        String rwId = getPara("rwId");
        String sjhm = getPara("sjhm");
        String dxnr = getPara("dxnr");

        SubmitReq submitReq = new SubmitReq();
        submitReq.setApId("xhsw");
        submitReq.setEcName("上海市地方税务局徐汇区分局");
        submitReq.setSecretKey("!@#asd123");
        submitReq.setContent(dxnr);
        submitReq.setMobiles(sjhm);
        submitReq.setAddSerial("");
        submitReq.setSign("EM0TUCaMT");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(submitReq.getEcName());
        stringBuffer.append(submitReq.getApId());
        stringBuffer.append(submitReq.getSecretKey());
        stringBuffer.append(submitReq.getMobiles());
        stringBuffer.append(submitReq.getContent());
        stringBuffer.append(submitReq.getSign());
        stringBuffer.append(submitReq.getAddSerial());

        submitReq.setMac(ToolMD5.encodeMD5Hex(stringBuffer.toString()));
        String reqText = Json.getJson().toJson(submitReq);
        String encode = Base64.encodeBase64String(reqText.getBytes());

        String requrl = "http://112.35.1.155:1992/sms/norsubmit";
        String resText = HttpUtils.post(requrl, encode);
        SubmitRsp submitRsp = FastJson.getJson().parse(resText, SubmitRsp.class);
        submitRsp.setSjhm(sjhm);
        submitRsp.setRwId(rwId);


        renderText(Json.getJson().toJson(submitRsp));
    }

}
