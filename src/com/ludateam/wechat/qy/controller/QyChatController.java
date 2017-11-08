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
 * Created by Him on 2017/9/25.
 */

import com.alibaba.fastjson.JSON;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ChatApi;
import com.jfinal.qyweixin.sdk.api.ConBatchApi;
import com.jfinal.qyweixin.sdk.api.media.MediaApi;
import com.jfinal.qyweixin.sdk.api.media.MediaFile;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

import java.util.HashMap;

@Controller("/wechat/chat")
public class QyChatController extends BaseController {
    private static final Log log = Log.getLog(QyChatController.class);

    public void create() {
        String data = "{\n" +
                "   \"chatid\": \"1\",\n" +
                "   \"name\": \"测试会话\",\n" +
                "   \"owner\": \"Him\",\n" +
                "   \"userlist\": [\"Jetzzzzz\"]\n" +
                "}";

        ApiResult result = ChatApi.Chat(ChatApi.ChatUrl.createUrl, data);

        renderText(result.getJson());
    }
}
