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
 * Created by Him on 2017/7/4.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.jfinal.ApiController;
import com.jfinal.qyweixin.sdk.msg.send.Article;
import com.jfinal.qyweixin.sdk.msg.send.News;
import com.jfinal.qyweixin.sdk.msg.send.QiYeNewsMsg;
import com.jfinal.qyweixin.sdk.msg.send.QiYeTextMsg;
import com.platform.annotation.Controller;

@Controller("/wechat/qyapi")
public class QyWeixinApiController extends ApiController {
    private static final Log log = Log.getLog(QyWeixinApiController.class);


    /*@Inject.BY_NAME
    private UserService userService;*/
    /**
     * 发送文本消息
     */
    public void sendTextMessage() throws IOException {

        String jsonStr = HttpKit.readData(getRequest());

        if (log.isDebugEnabled()) log.debug("发送文本消息");

        QiYeTextMsg text =  FastJson.getJson().parse(jsonStr,QiYeTextMsg.class);

        /*QiYeTextMsg text = new QiYeTextMsg();
        text.setAgentid("4");
        text.setText(new Text("测试消息"));
        text.setSafe("0");
        text.setTouser("Him");*/



        ApiResult sendTextMsg = SendMessageApi.sendTextMsg(text);
        renderText(sendTextMsg.getJson());
    }

    /**
     * 图文混排的消息
     */
    public void sendNewsMessage() {
        String jsonStr = HttpKit.readData(getRequest());
        QiYeNewsMsg news = FastJson.getJson().parse(jsonStr, QiYeNewsMsg.class);
        ApiResult sendTextMsg = SendMessageApi.sendNewsMsg(news);
        renderText(sendTextMsg.getJson());
    }
}
