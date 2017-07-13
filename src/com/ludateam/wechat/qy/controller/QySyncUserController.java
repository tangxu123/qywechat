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
 * Created by Him on 2017/7/12.
 */

import com.alibaba.fastjson.JSON;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConBatchApi;
import com.jfinal.qyweixin.sdk.api.ConDepartmentApi;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.api.media.MediaApi;
import com.jfinal.qyweixin.sdk.api.media.MediaFile;
import com.jfinal.qyweixin.sdk.msg.send.QiYeFileMsg;
import com.jfinal.qyweixin.sdk.msg.send.SendFile;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

import java.io.File;

@Controller("/wechat/syncuser")
public class QySyncUserController extends BaseController {

    private static final Log log = Log.getLog(QySyncUserController.class);

    public void index() {

        ApiResult apiResult = MediaApi.uploadMedia(MediaApi.MediaType.FILE, new File("D://batch_user_sample.csv"));
        String json = apiResult.getJson();
        String mediaId = JSON.parseObject(json).getString("media_id");
        System.out.println(mediaId);

        QiYeFileMsg file=new QiYeFileMsg();
        file.setAgentid("4");

        SendFile tempSendFile = new SendFile();
        tempSendFile.setMedia_id(mediaId);
        file.setFile(tempSendFile);

        file.setSafe("1");
        file.setTouser("Him");

        //SendMessageApi.sendFileMsg(file);

        MediaFile media =  MediaApi.getMedia(mediaId);

        apiResult = ConBatchApi.updateSyncUser("{\"media_id\":\""+mediaId+"\"" +"}");

        System.out.println(apiResult.getJson());
    }
}
