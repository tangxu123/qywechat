package com.ludateam.wechat.qy.mvc.qybd;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.kit.HandlerKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.spring.Inject;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.msg.send.QiYeTextMsg;
import com.jfinal.qyweixin.sdk.msg.send.Text;
import com.ludateam.wechat.api.CallService;
import com.ludateam.wechat.qy.entity.TaxEnterprise;
import com.ludateam.wechat.qy.vo.BindingRep;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

@Controller("/wechat/qybd")
public class QyBingdingController extends BaseController {

    private static final Log log = Log.getLog(QyBingdingController.class);

    @Inject.BY_NAME
    private CallService callService;

    public void index() {
        try {
            String redirect_uri = URLEncoder.encode(PropKit.get("domain") + "/wechat/qybd/main", "utf-8");
            String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123", true);
            System.out.println("codeUrl>>>" + codeUrl);
            HandlerKit.redirect301(codeUrl, getRequest(), getResponse(), new boolean[]{true});
            //redirect(codeUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void main() {
        String userId = null;
        String deviceId = null;
        String openid = null;
        String state = null;
        if (!isParaBlank("code")) {
            String code = getPara("code");
            log.info("code:" + code);
            if (!isParaBlank("state")) {
                state = getPara("state");
                log.info(" state:" + state);
            }
            ApiResult userInfoApiResult = OAuthApi.getUserInfoByCode(code);
            log.info("-------------json--------------------:" + userInfoApiResult.getJson());
            log.info("-------------errocode--------------------:" + userInfoApiResult.getErrorCode());
            log.info("-------------errmsg--------------------:" + userInfoApiResult.getErrorMsg());
            log.info("-------------isSucceed--------------------:" + userInfoApiResult.isSucceed());
            if (userInfoApiResult.isSucceed()) {
                String userInfoJson = userInfoApiResult.getJson();
                JSONObject object = JSON.parseObject(userInfoJson);
                deviceId = object.getString("DeviceId");
                try {
                    userId = object.getString("UserId");
                    log.info("-------------userId--------------------:" + userId);
                    log.info("-------------userId--------------------:" + userId);
                    log.info("-------------userId--------------------:" + userId);
                    // 如果获取userId为空 说明没有关注
                    if (userId != null && !userId.equals("")) {
                        ApiResult toOpenIdApiResult = OAuthApi
                                .ToOpenId("{\"userid\":\""
                                        + userId
                                        + "\",\"agentid\":"
                                        + ApiConfigKit.getApiConfig()
                                        .getAgentId() + "}");
                        System.out.println("toOpenIdApiResult:"
                                + toOpenIdApiResult.getJson());
                        if (toOpenIdApiResult.isSucceed()) {
                            openid = JSON.parseObject(
                                    toOpenIdApiResult.getJson()).getString(
                                    "openid");
                        }
                    } else {
                        openid = object.getString("OpenId");
                        log.info("-------------openid--------------------:" + openid);
                        log.info("-------------openid--------------------:" + openid);
                        log.info("-------------openid--------------------:" + openid);
                        String json = "{\"openid\":\"" + openid + "\"}";
                        // 如果未关注 openid无法转化为userid
                        ApiResult toUserIdApiResult = OAuthApi.ToUserId(json);
                        log.info("-------------openid to userid--------------------:" + toUserIdApiResult.getJson());
                        log.info("-------------openid to userid--------------------:" + toUserIdApiResult.getJson());
                        log.info("-------------openid to userid--------------------:" + toUserIdApiResult.getJson());
                        if (toUserIdApiResult.isSucceed()) {
                            userId = JSON.parseObject(toUserIdApiResult.getJson()).getString("userid");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setSessionAttr("userId", userId);
            setSessionAttr("openId", openid);
            log.info("-------------userId--------------------:" + userId);
            log.info("-------------userId--------------------:" + userId);
            log.info("-------------userId--------------------:" + userId);
            try {
                String jsonString = callService.getBindingList(userId);
                log.info("-------------jsonString--------------------");
                log.info("-------------jsonString--------------------:" + jsonString);
                log.info("-------------jsonString--------------------");
                BindingRep bindingRep = FastJson.getJson().parse(jsonString,
                        BindingRep.class);
                if ("0".equals(bindingRep.getErrcode())) {
                    setAttr("bindingList", bindingRep.getBindingList());
                } else {
                    setAttr("bindingList", new ArrayList<TaxEnterprise>());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setAttr("userId", userId);
            setAttr("openid", openid);
            render("/qybd/index.html");
        }
    }

    public void list() {

        String userid = getRequest().getParameter("userid");
        String jsonString = callService.getBindingList(userid);
        BindingRep bindingRep = FastJson.getJson().parse(jsonString,
                BindingRep.class);

        if ("0".equals(bindingRep.getErrcode())) {
            setAttr("bindingList", bindingRep.getBindingList());
        } else {
            setAttr("bindingList", new ArrayList<TaxEnterprise>());
        }

        setAttr("userId", userid);
        render("/qybd/index.html");
    }

    public void change() {
        try {
            String userid = getRequest().getParameter("userid");
            String djxh = getRequest().getParameter("djxh");
            String nsrmc = getRequest().getParameter("nsrmc");
            if (userid == null || "".equals(userid)) {
                renderJson("{\"errmsg\":\"用户id不能为空\"}");
            }
            if (djxh == null || "".equals(djxh)) {
                renderJson("{\"errmsg\":\"请选择一家企业\"}");
            }
            String jsonString = callService.setDefaultCompany(userid, djxh);

            //发送消息
            QiYeTextMsg text = new QiYeTextMsg();
            text.setAgentid("9");
            text.setText(new Text("您当前绑定的身份为:" + nsrmc));
            text.setSafe("0");
            text.setTouser(userid);


            ApiResult sendTextMsg = SendMessageApi.sendTextMsg(text);

            renderJson(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
