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
 * Created by Him on 2017/7/3.
 */


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConBatchApi;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.qyweixin.sdk.msg.in.InImageMsg;
import com.jfinal.qyweixin.sdk.msg.in.InLocationMsg;
import com.jfinal.qyweixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.qyweixin.sdk.msg.in.InTextMsg;
import com.jfinal.qyweixin.sdk.msg.in.InVideoMsg;
import com.jfinal.qyweixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.qyweixin.sdk.msg.in.event.BatchJob;
import com.jfinal.qyweixin.sdk.msg.in.event.InEnterAgentEvent;
import com.jfinal.qyweixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.qyweixin.sdk.msg.in.event.InJobEvent;
import com.jfinal.qyweixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.qyweixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.qyweixin.sdk.msg.in.event.ScanCodeInfo;
import com.jfinal.qyweixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.qyweixin.sdk.msg.out.OutTextMsg;
import com.jfinal.qyweixin.sdk.msg.out.OutVoiceMsg;
import com.jfinal.qyweixin.sdk.msg.send.Article;
import com.jfinal.qyweixin.sdk.msg.send.News;
import com.jfinal.qyweixin.sdk.msg.send.QiYeNewsMsg;
import com.jfinal.qyweixin.sdk.msg.send.QiYeTextMsg;
import com.jfinal.qyweixin.sdk.msg.send.Text;
import com.ludateam.wechat.api.MessageService;
import com.ludateam.wechat.qy.utils.QyWeixinAPI;
import com.platform.annotation.Controller;

@Controller("/wechat/qymsg")
public class QyWeixinMsgController extends MsgControllerAdapter {
    private static final Log log = Log.getLog(QyWeixinMsgController.class);

   /* @Inject.BY_NAME
    private MessageService messageService;*/


    @Override
    protected void processInTextMsg(InTextMsg inTextMsg,MessageService messageService) {

    	log.info("start processInTextMsg");
    	log.info("messageService init "+messageService);
        String msgContent = inTextMsg.getContent().trim();
		Map msgMap = (Map) JSON.toJSON(inTextMsg);
		log.info("inTextMsg content" + inTextMsg);
		long createTimeLong = Long.parseLong(String.valueOf(inTextMsg.getCreateTime()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		msgMap.put("createTime", format.format(new Date(createTimeLong * 1000)));
		log.info("msgjson" + msgMap);
		messageService.receiveMessage(JSON.toJSONString(msgMap));

        System.out.println("收到的信息：" + msgContent);
        if ("OAuth".equalsIgnoreCase(msgContent)) {
            String url = PropKit.get("domain") + "/wechat/qyoauth";
            String urlStr = "<a href=\"" + url + "\">点击我授权</a>";

            OutTextMsg outMsg = new OutTextMsg(inTextMsg);
            outMsg.setContent(urlStr);
            render(outMsg);
        } else if ("jssdk".equalsIgnoreCase(msgContent)) {
            String url = PropKit.get("domain") + "/wechat/jssdk";
            String urlStr = "<a href=\"" + url + "\">JSSDK</a>";
            renderOutTextMsg("授权地址" + urlStr);
        } else {
        	// 其它文本消息直接返回原值 + 帮助提示
            //renderOutTextMsg("\t文本消息已成功接收，内容为： " + inTextMsg.getContent() + "\n\n");
        }
    }

    /**
     * 实现父类抽方法，处理图片消息
     */
    @Override
    protected void processInImageMsg(InImageMsg inImageMsg,MessageService messageService) {
		Map msgMap = (Map) JSON.toJSON(inImageMsg);
		long createTimeLong = Long.parseLong(String.valueOf(inImageMsg.getCreateTime()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		msgMap.put("createTime", format.format(new Date(createTimeLong * 1000)));
		messageService.receiveMessage(JSON.toJSONString(msgMap));

        //OutImageMsg outMsg = new OutImageMsg(inImageMsg);
        // 将刚发过来的图片再发回去
        //outMsg.setMediaId(inImageMsg.getMediaId());
        //render(outMsg);
    }

    /**
     * 实现父类抽方法，处理语音消息
     */
    protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
        OutVoiceMsg outMsg = new OutVoiceMsg(inVoiceMsg);
        // 将刚发过来的语音再发回去
        outMsg.setMediaId(inVoiceMsg.getMediaId());
        render(outMsg);
    }

    /**
     * 实现父类抽方法，处理视频消息
     */
    protected void processInVideoMsg(InVideoMsg inVideoMsg) {
        /* 腾讯 api 有 bug，无法回复视频消息，暂时回复文本消息代码测试
        OutVideoMsg outMsg = new OutVideoMsg(inVideoMsg);
		outMsg.setTitle("OutVideoMsg 发送");
		outMsg.setDescription("刚刚发来的视频再发回去");
		// 将刚发过来的视频再发回去，经测试证明是腾讯官方的 api 有 bug，待 api bug 却除后再试
		outMsg.setMediaId(inVideoMsg.getMediaId());
		render(outMsg);
		*/
        OutTextMsg outMsg = new OutTextMsg(inVideoMsg);
        outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " + inVideoMsg.getMediaId());
        render(outMsg);
    }

    @Override
    protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
        OutTextMsg outMsg = new OutTextMsg(inShortVideoMsg);
        outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " + inShortVideoMsg.getMediaId());
        render(outMsg);
    }

    /**
     * 实现父类抽方法，处理地址位置消息
     */
    protected void processInLocationMsg(InLocationMsg inLocationMsg) {
        OutTextMsg outMsg = new OutTextMsg(inLocationMsg);
        outMsg.setContent("已收到地理位置消息:" +
                "\nlocation_X = " + inLocationMsg.getLocation_X() +
                "\nlocation_Y = " + inLocationMsg.getLocation_Y() +
                "\nscale = " + inLocationMsg.getScale() +
                "\nlabel = " + inLocationMsg.getLabel());
        render(outMsg);
    }

    /**
     * 实现父类抽方法，处理关注/取消关注消息
     */
    @Override
    protected void processInFollowEvent(InFollowEvent inFollowEvent) {
        if (InFollowEvent.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEvent.getEvent())) {
			if ("19".equals(inFollowEvent.getAgentId())) {
	            List<com.jfinal.qyweixin.sdk.msg.out.News> articles = new ArrayList<com.jfinal.qyweixin.sdk.msg.out.News>();
	            com.jfinal.qyweixin.sdk.msg.out.News news = new com.jfinal.qyweixin.sdk.msg.out.News();
	            news.setTitle("大调研");
	            news.setDescription("徐汇区大调研");
	            news.setPicUrl("http://sw.xh.sh.cn:8080/qywx/static/img/xhqddy.jpg");
	            news.setUrl("http://sw.xh.sh.cn:8080/qywx/wechat/questionnaire");
	            articles.add(news);
	            OutNewsMsg outMsg = new OutNewsMsg(inFollowEvent);
	            outMsg.setArticles(articles);
	            render(outMsg);
			}
			if ("10".equals(inFollowEvent.getAgentId())) {
	            OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
	            outMsg.setContent("尊敬的纳税人：\n       为深化税务系统\"放管服\"改革，进一步优化营商环境，我局推出\"上海徐汇税务号\"微信服务，期待能带给您优质的纳税服务和全新的办税体验！\n                    徐汇区税务局");
				render(outMsg);
			}
        }// 如果为取消关注事件，将无法接收到传回的信息
        if (InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent.getEvent())) {
            log.debug("取消关注：" + inFollowEvent.getFromUserName());
        }
    }

    @Override
    protected void processInMenuEvent(InMenuEvent inMenuEvent) {
        OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
        outMsg.setContent("菜单事件内容是：" + inMenuEvent.getEventKey());
        //render(outMsg);
        if (InMenuEvent.EVENT_INMENU_CLICK.equals(inMenuEvent.getEvent())) {
            String key = inMenuEvent.getEventKey();
            if("ZQRL".equals(key)){
                //菜单：征期日历
                QiYeNewsMsg qiYeNewsMsg = new QiYeNewsMsg();
                qiYeNewsMsg.setAgentid("4");
                qiYeNewsMsg.setArticleCount(1);
                qiYeNewsMsg.setSafe("0");
                String toUserName = inMenuEvent.getFromUserName();
                qiYeNewsMsg.setTouser(toUserName);

                News news = new News();
                List<Article> articles = new ArrayList<Article>();
                Article article = new Article();
                article.setTitle("征期日历");
                article.setDescription("征期日历");
                article.setPicurl("http://img01.taopic.com/161009/201959-16100Z3223922.jpg");
                article.setUrl("http://mp.weixin.qq.com/s/jDDvVGbZqnk9kV8_AJtIiQ");
                articles.add(article);
                news.setArticles(articles);
                qiYeNewsMsg.setNews(news);
                QyWeixinAPI.sendNewsMessage(qiYeNewsMsg);
            }else if("RDSM".equals(key)){
                //热点扫描
                // TODO: 2017/7/11 热点扫描返回的文章内容是否需要设置为动态
                render(outMsg);
            }else if("BSDT".equals(key)){
                //办税地图
                QiYeNewsMsg qiYeNewsMsg = new QiYeNewsMsg();
                qiYeNewsMsg.setAgentid("4");
                qiYeNewsMsg.setArticleCount(2);
                qiYeNewsMsg.setSafe("0");
                String toUserName = inMenuEvent.getFromUserName();
                qiYeNewsMsg.setTouser(toUserName);

                News news = new News();
                List<Article> articles = new ArrayList<Article>();
                Article article = new Article();
                article.setTitle("徐汇区办税服务大厅地图");
                article.setDescription("徐汇区办税服务大厅地图");
                article.setPicurl("http://www.hb-l-tax.gov.cn/xts/dsdt/xwbd/200911/W020091102584357769195.jpg");
                article.setUrl("http://mp.weixin.qq.com/s/krzLpUm-DfSXFurQNl-bvA");
                Article article2 = new Article();
                article2.setTitle("徐汇区私房出租代征点一览表");
                article2.setDescription("徐汇区私房出租代征点一览表");
                article2.setPicurl("http://hot.online.sh.cn/images/attachement/jpeg/site1/20160829/IMGf48e389446714224375673.jpeg");
                article2.setUrl("http://mp.weixin.qq.com/s/c8GLbAeF_3YPoY8wH2kgmw");

                articles.add(article);
                articles.add(article2);
                news.setArticles(articles);

                qiYeNewsMsg.setNews(news);
                QyWeixinAPI.sendNewsMessage(qiYeNewsMsg);
            }
        }
    }

    /**
     * 实现父类抽方法，处理扫码推事件
     */
    @Override
    protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
        ScanCodeInfo scanCodeInfo = inQrCodeEvent.getScanCodeInfo();
        String scanResult = scanCodeInfo.getScanResult();
        String scantype = scanCodeInfo.getScanType();
        OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
        outMsg.setContent("处理扫码推事件\n 扫描结果:" + scanResult + "扫描类型:" + scantype);
        render(outMsg);

    }

    /**
     * 实现父类抽方法，处理成员进入应用的事件
     */
    @Override
    protected void processInEnterAgentEvent(InEnterAgentEvent inAgentEvent) {
        OutTextMsg outMsg = new OutTextMsg(inAgentEvent);
        outMsg.setContent("欢迎:" + inAgentEvent.getFromUserName() + "再次进入");
        render(outMsg);
    }

    /**
     * 实现父类抽方法，处理异步任务完成事件
     */
    @Override
    protected void processInJobEvent(InJobEvent inJobEvent) {
        BatchJob batchJob = inJobEvent.getBatchJob();

        ApiResult batchGetResult = ConBatchApi.batchGetResult(batchJob.getJobId());

        QiYeTextMsg text = new QiYeTextMsg();
        text.setAgentid("16");
        text.setSafe("0");
        text.setTouser("Javen");
        text.setText(new Text("异步任务完成:\n JobType:" + batchJob.getJobType()
                + "\n JobId:" + batchJob.getJobId()
                + "\n 执行结果：" + batchGetResult.getJson()));
        SendMessageApi.sendTextMsg(text);
        renderNull();
    }
}
