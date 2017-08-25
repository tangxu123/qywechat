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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.spring.Inject;
import com.jfinal.qyweixin.sdk.api.ApiConfig;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.jfinal.ApiController;
import com.jfinal.qyweixin.sdk.msg.send.*;

import com.ludateam.wechat.api.UserService;
import com.ludateam.wechat.entity.User;
import com.platform.annotation.Controller;


import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        QiYeNewsMsg qiYeNewsMsg = new QiYeNewsMsg();
        qiYeNewsMsg.setAgentid("4");
        qiYeNewsMsg.setArticleCount(1);
        qiYeNewsMsg.setSafe("0");
        qiYeNewsMsg.setTouser("Him");

        News news = new News();
        List<Article> articles = new ArrayList<Article>();
        Article article = new Article();
        article.setTitle("【图解税收】一图掌握 7月1日起增值税税率调整为11%的商品");
        article.setDescription("一图掌握 7月1日起增值税税率调整为11%的商品");
        article.setPicurl("https://mmbiz.qlogo.cn/mmbiz/ibHRiaZ9MRcUpjHhhNQzCl9zGicPBWibh1ndW6Mj27ibCREGGVa9mag0iatwDJ1fSPhsib2LiaBVVenAU8ibqW1kGeka9HQ/0?wx_fmt=png");
        article.setUrl("https://mp.weixin.qq.com/s/0romzmtiWqzH44jLUxMnug");
        articles.add(article);
        news.setArticles(articles);
        qiYeNewsMsg.setNews(news);

        ApiResult sendTextMsg = SendMessageApi.sendNewsMsg(qiYeNewsMsg);
        renderText(sendTextMsg.getJson());
    }
}
