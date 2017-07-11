package com.ludateam.wechat.qy.utils;

import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.msg.send.*;
import com.jfinal.render.Render;
import com.jfinal.render.RenderManager;

/**
 * Created by Administrator on 2017/7/11.
 */
public class QyWeixinAPI {
    private static final Log log = Log.getLog(QyWeixinAPI.class);
    /**
     * Hold Render object when invoke renderXxx(...)
     */
    private static Render render;
    private static final RenderManager renderManager = RenderManager.me();
    public static void renderText(String text) {
        render = renderManager.getRenderFactory().getTextRender(text);
    }
    /**
     * 发送文本消息
     */
    public static void sendTextMessage(QiYeTextMsg textMsg) {
        if (log.isDebugEnabled()) log.debug("发送文本消息");
        ApiResult sendTextMsg = SendMessageApi.sendTextMsg(textMsg);
        renderText(sendTextMsg.getJson());
    }

    /**
     * 图文混排的消息
     */
    public static void sendNewsMessage(QiYeNewsMsg newsMsg) {
        ApiResult sendTextMsg = SendMessageApi.sendNewsMsg(newsMsg);
        renderText(sendTextMsg.getJson());
    }
}
