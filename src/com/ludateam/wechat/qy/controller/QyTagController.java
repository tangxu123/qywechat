package com.ludateam.wechat.qy.controller;

import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConTagApi;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

import java.util.HashMap;

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
 * Created by Him on 2017/9/11.
 */
@Controller("/wechat/tag")
public class QyTagController extends BaseController {
    private static final Log log = Log.getLog(QyTagController.class);

    public void create() {
        String jsonStr = HttpKit.readData(getRequest());
        ApiResult apiResult = ConTagApi.createTag(jsonStr);

        renderText(apiResult.getJson());
    }

    public void delete() {
        String jsonStr = HttpKit.readData(getRequest());
        HashMap<String, String> map = FastJson.getJson().parse(jsonStr, HashMap.class);

        String tagid = map.get("tagid");
        ApiResult apiResult = ConTagApi.deleteTag(tagid);
        renderText(apiResult.getJson());
    }

    public void addusers() {
        String jsonStr = HttpKit.readData(getRequest());

        ApiResult apiResult = ConTagApi.addTagUsers(jsonStr);
        renderText(apiResult.getJson());
    }

    public void getusers() {
        String jsonStr = HttpKit.readData(getRequest());
        HashMap<String, String> map = FastJson.getJson().parse(jsonStr, HashMap.class);

        String tagid = map.get("tagid");
        ApiResult apiResult = ConTagApi.getTagUser(tagid);
        renderText(apiResult.getJson());
    }

    public void deltagusers() {
        String jsonStr = HttpKit.readData(getRequest());

        ApiResult apiResult = ConTagApi.deleteTagUsers(jsonStr);
        renderText(apiResult.getJson());
    }
    public void list() {

        ApiResult apiResult = ConTagApi.getTagList();
        renderText(apiResult.getJson());
    }
}
