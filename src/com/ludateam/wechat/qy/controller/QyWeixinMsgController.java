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


import com.jfinal.kit.PropKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConBatchApi;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.qyweixin.sdk.msg.in.*;
import com.jfinal.qyweixin.sdk.msg.in.event.*;
import com.jfinal.qyweixin.sdk.msg.out.OutImageMsg;
import com.jfinal.qyweixin.sdk.msg.out.OutTextMsg;
import com.jfinal.qyweixin.sdk.msg.out.OutVoiceMsg;
import com.jfinal.qyweixin.sdk.msg.send.QiYeTextMsg;
import com.jfinal.qyweixin.sdk.msg.send.Text;
import com.platform.annotation.Controller;
import com.platform.constant.ConstantInit;
import com.platform.mvc.base.BaseController;

import com.jfinal.log.Log;

import java.util.HashMap;

@Controller("/wechat/qymsg")
public class QyWeixinMsgController extends MsgControllerAdapter {
    private static final Log log = Log.getLog(QyWeixinMsgController.class);

    @Override
    protected void processInTextMsg(InTextMsg inTextMsg) {
        String msgContent = inTextMsg.getContent().trim();
        System.out.println("收到的信息：" + msgContent);
        if ("OAuth".equalsIgnoreCase(msgContent)) {
            String url = PropKit.get("domain") + "/qyoauth2";
            String urlStr = "<a href=\"" + url + "\">点击我授权</a>";

            OutTextMsg outMsg = new OutTextMsg(inTextMsg);
            outMsg.setContent(urlStr);
            render(outMsg);
        } else if ("jssdk".equalsIgnoreCase(msgContent)) {
            String url = PropKit.get("domain") + "/qyjssdk";
            String urlStr = "<a href=\"" + url + "\">JSSDK</a>";
            renderOutTextMsg("授权地址" + urlStr);
        }
        // 其它文本消息直接返回原值 + 帮助提示
        else {
            renderOutTextMsg("\t文本消息已成功接收，内容为： " + inTextMsg.getContent() + "\n\n");
        }
    }

    /**
     * 实现父类抽方法，处理图片消息
     */
    @Override
    protected void processInImageMsg(InImageMsg inImageMsg) {
        OutImageMsg outMsg = new OutImageMsg(inImageMsg);
        // 将刚发过来的图片再发回去
        outMsg.setMediaId(inImageMsg.getMediaId());
        render(outMsg);
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
            OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
            outMsg.setContent("感谢关注徐汇专管员企业号");
            render(outMsg);
        }// 如果为取消关注事件，将无法接收到传回的信息
        if (InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent.getEvent())) {
            log.debug("取消关注：" + inFollowEvent.getFromUserName());
        }
    }

    @Override
    protected void processInMenuEvent(InMenuEvent inMenuEvent) {
        OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
        outMsg.setContent("菜单事件内容是：" + inMenuEvent.getEventKey());
        render(outMsg);
        if (InMenuEvent.EVENT_INMENU_CLICK.equals(inMenuEvent.getEvent())) {

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
