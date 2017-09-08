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
 * Created by Him on 2017/9/8.
 */

import com.alibaba.fastjson.JSON;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConBatchApi;
import com.jfinal.qyweixin.sdk.api.media.MediaApi;
import com.jfinal.qyweixin.sdk.api.media.MediaFile;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;
import com.jfinal.log.Log;

import java.util.HashMap;

@Controller("/wechat/replaceuser")
public class QyReplaceUserController extends BaseController {
    private static final Log log = Log.getLog(QyReplaceUserController.class);

    public void index() {

        String jsonStr = HttpKit.readData(getRequest());

        HashMap<String, String> map = FastJson.getJson().parse(jsonStr, HashMap.class);

        ApiResult apiResult = MediaApi.uploadMedia(MediaApi.MediaType.FILE, map.get("content"));


        String json = apiResult.getJson();
        String mediaId = JSON.parseObject(json).getString("media_id");
        log.info(mediaId);
        MediaFile media = MediaApi.getMedia(mediaId);
        apiResult = ConBatchApi.batgReplaceUser("{\"media_id\":\"" + mediaId + "\"" + "}");

        renderText(apiResult.getJson());
    }
}
