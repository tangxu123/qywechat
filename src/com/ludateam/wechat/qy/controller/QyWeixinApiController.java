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
import java.io.InputStream;

import com.alibaba.fastjson.JSON;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.api.media.MediaApi;
import com.jfinal.qyweixin.sdk.api.media.MediaHelper;
import com.jfinal.qyweixin.sdk.api.media.MediaMpNews;
import com.jfinal.qyweixin.sdk.api.media.XHMediaMpNews;
import com.jfinal.qyweixin.sdk.jfinal.ApiController;
import com.jfinal.qyweixin.sdk.msg.send.*;

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

        QiYeTextMsg text = FastJson.getJson().parse(jsonStr, QiYeTextMsg.class);

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

    public void sendImageMessage() throws Exception {
        String jsonStr = HttpKit.readData(getRequest());
        QiYeImageMsg qiYeImageMsg = new QiYeImageMsg();
        QiYeFileMsg qiYeFileMsg = FastJson.getJson().parse(jsonStr, QiYeFileMsg.class);
        MediaHelper mediaHelper = new MediaHelper();

        ApiResult result = mediaHelper.send("image", qiYeFileMsg.getPath());
        String json = result.getJson();

        String media_Id = JSON.parseObject(json).getString("media_id");
        if (media_Id == null) {
            renderText(json);
        } else {
            log.info(media_Id);
            QiYeImage image = new QiYeImage();
            image.setMedia_id(media_Id);
            qiYeImageMsg.setTouser(qiYeFileMsg.getTouser());
            qiYeImageMsg.setAgentid(qiYeFileMsg.getAgentid());
            qiYeImageMsg.setImage(image);

            ApiResult sendResult = SendMessageApi.sendImageMsg(qiYeImageMsg);
            renderText(sendResult.getJson());
        }
    }

    public void sendFileMessage() throws Exception {
        String jsonStr = HttpKit.readData(getRequest());

        QiYeFileMsg qiYeFileMsg = FastJson.getJson().parse(jsonStr, QiYeFileMsg.class);
        MediaHelper mediaHelper = new MediaHelper();

        ApiResult result = mediaHelper.send("file", qiYeFileMsg.getPath());
        String json = result.getJson();

        String media_Id = JSON.parseObject(json).getString("media_id");
        if (media_Id == null) {
            renderText(json);
        } else {
            log.info(media_Id);
            SendFile sendFile = new SendFile();
            sendFile.setMedia_id(media_Id);
            qiYeFileMsg.setFile(sendFile);
            ApiResult sendResult = SendMessageApi.sendFileMsg(qiYeFileMsg);
            renderText(sendResult.getJson());
        }
        /*
        ApiResult result1 = MediaApi.getMaterialCount("15");
        result1 = MediaApi.batchGetMaterial(MediaApi.MediaType.MPNEWS, 0, 10, 15);

        String mediaId = "2GjYs1LaIDqpoWSwsd2j65IdeRJxTJnoXyiIoRtQs5OyaWjVwemnuN__cYNf4ykmT";


        XHMediaMpNews xhMediaMpNews = new XHMediaMpNews();
        xhMediaMpNews.setAgentid("15");
        xhMediaMpNews.setTouser("18616864830");
        MediaMpNews mediaMpNews = new MediaMpNews();
        mediaMpNews.setMedia_id(mediaId);
        xhMediaMpNews.setMpnews(mediaMpNews);
        xhMediaMpNews.setMsgtype("mpnews");

        //result1 = SendMessageApi.sendMpNewsMsg(xhMediaMpNews);


        String remotepath = getPara("path");

        String jsonStr = HttpKit.readData(getRequest());
        QiYeFileMsg qiYeFileMsg = FastJson.getJson().parse(jsonStr, QiYeFileMsg.class);

        MediaHelper mediaHelper = new MediaHelper();
        try {

            String path = SFtpUtil.downloadSftpFile("127.0.0.1", "tester", "password", 22, "/", "E:\\XUHUI\\qywechat\\WebContent\\temp", "74a5b4bagw1f6vhcmjzfbj218a1qh42p.jpg");

            ApiResult result = mediaHelper.send("image", path);
            String json = result.getJson();
            String media_Id = JSON.parseObject(json).getString("media_id");
            log.info(media_Id);


            SendFile sendFile = new SendFile();
            sendFile.setMedia_id(media_Id);
            qiYeFileMsg.setFile(sendFile);

            //ApiResult sendResult = SendMessageApi.sendFileMsg(qiYeFileMsg);

            QiYeImageMsg qiYeImageMsg = new QiYeImageMsg();



            QiYeImage image = new QiYeImage();
            image.setMedia_id(media_Id);


            qiYeImageMsg.setImage(image);

            qiYeImageMsg.setAgentid("15");
            qiYeImageMsg.setTouser("18616864830");

            ApiResult sendResult =  SendMessageApi.sendImageMsg(qiYeImageMsg);


            renderText(sendResult.getJson());

        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }


    /**
     * 文本卡片消息
     */
    public void sendTextcardMessage() {
        String jsonStr = HttpKit.readData(getRequest());
        QiYeTextCardMsg textcard = FastJson.getJson().parse(jsonStr, QiYeTextCardMsg.class);
        ApiResult sendTextMsg = SendMessageApi.sendTextCardMsg(textcard);
        renderText(sendTextMsg.getJson());
    }

}
