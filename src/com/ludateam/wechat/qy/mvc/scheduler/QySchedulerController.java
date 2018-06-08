package com.ludateam.wechat.qy.mvc.scheduler;
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
 * Created by Him on 2017/7/18.
 */


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jfinal.kit.HandlerKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;
import com.jfinal.qyweixin.sdk.utils.HttpUtils;
import com.ludateam.wechat.qy.entity.*;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;


@Controller("/wechat/myscheduler")
public class QySchedulerController extends BaseController {
    private static final Log log = Log.getLog(QySchedulerController.class);

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//    private final static String url = "http://172.16.2.50:9009";
    private final static String url = "http://127.0.0.1:9009/service";



    public void index() {
//        try {
////            String redirect_uri = URLEncoder.encode(PropKit.get("domain") + "/wechat/myscheduler/main", "utf-8");
////            String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123", true);
////            System.out.println("codeUrl>>>" + codeUrl);
////            redirect(codeUrl);
//
//            String redirect_uri = URLEncoder.encode(PropKit.get("domain") + "/wechat/myscheduler/main", "utf-8");
//            String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123", "13", true);
//            System.out.println("codeUrl>>>" + codeUrl);
//            HandlerKit.redirect301(codeUrl, getRequest(), getResponse(),
//                    new boolean[] { true });
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


        setAttr("userId", "13101040424");
        setAttr("openid", "");
        render("/scheduler/index.html");

    }

    public void main() {
        String userId = null;
        String deviceId = null;
        String openid = null;
        String state = null;
        if (!isParaBlank("code")) {
            String code = getPara("code");
            System.out.println("code>>>" + code);
            log.info("code:" + code);
            if (!isParaBlank("state")) {
                state = getPara("state");
                log.info(" state:" + state);
                System.out.println(" state:" + state);
            }
            ApiResult userInfoApiResult = OAuthApi.getUserInfoByCode(code);
            if (userInfoApiResult.isSucceed()) {
                String userInfoJson = userInfoApiResult.getJson();
                JSONObject object = JSON.parseObject(userInfoJson);
                deviceId = object.getString("DeviceId");
                try {
                    userId = object.getString("UserId");
                    System.out.println("userId:" + userId);
                    //如果获取userId为空 说明没有关注
                    if (userId != null && !userId.equals("")) {
                        ApiResult toOpenIdApiResult = OAuthApi.ToOpenId("{\"userid\":\"" + userId + "\",\"agentid\":" + ApiConfigKit.getApiConfig().getAgentId() + "}");
                        System.out.println("toOpenIdApiResult:" + toOpenIdApiResult.getJson());
                        if (toOpenIdApiResult.isSucceed()) {
                            openid = JSON.parseObject(toOpenIdApiResult.getJson()).getString("openid");
                        }
                    } else {
                        openid = object.getString("OpenId");
                        String json = "{\"openid\":\"" + openid + "\"}";
                        System.out.println("json..." + json);
                        //如果未关注 openid无法转化为userid
                        ApiResult toUserIdApiResult = OAuthApi.ToUserId(json);
                        System.out.println("toUserIdApiResult:" + toUserIdApiResult.getJson());
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
            if (state.equals("123")) {
                redirect("/");
            }
            //renderText(userInfoApiResult.getJson()+">>>userId:"+userId+" deviceId:"+deviceId+" openid:"+openid);
            setAttr("userId", userId);
            setAttr("openid", openid);
            render("/scheduler/index.html");
        }
    }

    public void add() {
        render("/scheduler/add.html");
    }

    public void getTpl() {
        render("/scheduler/tpl_data.html");
    }

    public void getSwryList() {
        render("/scheduler/swry_data.html");
    }

    public void getMeetingDesc() {
        render("/scheduler/meetingdesc.html");
    }
    /**
     * 获取所在周日期
     *
     * @param dataStr
     * @return
     */
    public List<String> getFirstAndLastOfWeek(String dataStr) throws ParseException {
        List<String> returnList = new ArrayList();
        Calendar temp;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dataStr));
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周开始日期
        String data1 = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        returnList.add(data1);

        for (int i = 1; i < 7; i++) {
            temp = (Calendar) cal.clone();
            temp.add(Calendar.DAY_OF_WEEK, i);
            returnList.add(new SimpleDateFormat("yyyy-MM-dd").format(temp.getTime()));

        }
        return returnList;
    }

    public List<String> getFirstAndLastOfMonth(String dataStr) throws ParseException {
        List<String> returnList = new ArrayList();
        Calendar temp;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dataStr));
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        String first = format.format(c.getTime());

        for (int i = 1; i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            temp = (Calendar) c.clone();
            temp.set(Calendar.DATE, i);
            returnList.add(format.format(temp.getTime()));
        }
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());

        return returnList;
    }

    /**
     * 模拟数据
     *
     * @return
     */
    private List<Meeting> getMeetingData(String swrydm, String rysx) {
//        String s = HttpUtils.get(url+"/calendar/meeting/meetings?userid="+swrydm);
        String s = HttpUtils.get(url+"/meeting/meetings?userid="+swrydm+"&rysx="+rysx);

        System.out.println("sssssssssssssssssssssssssssssssss="+s);

        if("".equals(s)){
            return null;
        }

        ArrayList<Meeting> meetingList = JSON.parseObject(s, new TypeReference<ArrayList<Meeting>>() {});
        System.out.println(meetingList);
        return meetingList;
    }

    public void getRysx(){
        //登入人员代码
        String drrydm = getPara("drrydm");
        String ss = HttpUtils.get(url+"/meeting/rysx?userid="+drrydm);
//        System.out.println(ss);
        if("".equals(ss)){
            return;
        }

        renderJson(ss);

    }
    public void getSchedules() throws ParseException {
        //年
        Integer year = Integer.parseInt(getPara("year"));
        //月
        Integer month = Integer.parseInt(getPara("month"));
        //日
        Integer day = Integer.parseInt(getPara("day"));
        //登入人员代码
        String drrydm = getPara("drrydm");
        //人员属性
        String rysx = getPara("rysx");

        Calendar now = Calendar.getInstance();
        now.set(year, month - 1, day);

        List<String> weekDays = getFirstAndLastOfWeek(format.format(now.getTime()));
        List<String> monthDays = getFirstAndLastOfMonth(format.format(now.getTime()));
        //模拟数据
        List<Meeting> MOCK_DATA = getMeetingData(drrydm,rysx);
        if(null == MOCK_DATA){
            return;
        }

        List<Scheduler> returnlist = new ArrayList<>();
        MOCK_DATA.stream().collect(Collectors.groupingBy(p -> {
            return p.getKsrq();
        }, Collectors.counting())).forEach((k, v) -> {
            Scheduler scheduler = new Scheduler();
            scheduler.setD(k);
            scheduler.setCount(v);
            returnlist.add(scheduler);
        });


        List<MeetingData> meetingDataList = new ArrayList<>();
        //日历下方会议列表只显示本周
        List<Meeting> thisweekmeetinglist = MOCK_DATA.stream().filter(s -> {
            return weekDays.contains(s.getKsrq());
        }).collect(Collectors.toList());

        thisweekmeetinglist.stream().collect(Collectors.groupingBy(p->{ return p.getKsrq();})).forEach((k, v) -> {
            MeetingData meetingData = new MeetingData();
            meetingData.setTitle(k);
            try {
                meetingData.setTitledate(new SimpleDateFormat("yyyy-M-d").parse(k));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            meetingData.setMeetingList(v);
            meetingDataList.add(meetingData);
        });

        List<MeetingData> sortedMeetingDataList = meetingDataList.stream().sorted(comparing(MeetingData::getTitledate)).collect(Collectors.toList());


        SchedulerData schedulerData = new SchedulerData();
        schedulerData.setData(returnlist);
        schedulerData.setMeetings(sortedMeetingDataList);

        renderJson(schedulerData);
    }

    public void getSwrys(){
        String drrydm = String.valueOf(getPara("drrydm"));
        String hybh = String.valueOf(getPara("hybh"));



        List<MeetingSwry> returnSwryList = new ArrayList<MeetingSwry>();
//        String s = HttpUtils.get(url+"/calendar/meeting/designatedPersons?userid="+drrydm+"&meetingNumber="+hybh);
        String s = HttpUtils.get(url+"/meeting/designatedPersons?userid="+drrydm+"&meetingNumber="+hybh);

        if("".equals(s)){
            return;
        }
        returnSwryList = JSON.parseArray(s,MeetingSwry.class);
        Map m = new HashMap();
        m.put("swryList",returnSwryList);
        List<Meeting> meetList = new ArrayList();
//        String ss = HttpUtils.get(url+"/calendar/meeting/desc?meetingNumber="+hybh);
        String ss = HttpUtils.get(url+"/meeting/desc?meetingNumber="+hybh);

        if("".equals(ss)){
            return;
        }

        meetList = JSON.parseArray(ss,Meeting.class);
        m.put("meetingList",meetList);
//        System.out.println(returnSwryList);

        renderJson(m);

    }

    public String bcJoinMeetingSwrys(){
        String s = "F";
        try {
        String swryStr = String.valueOf(getPara("swryStr"));
        System.out.println(swryStr);
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encode(swryStr.getBytes("UTF-8"));
//        String base64 =   Base64.getEncoder().encodeToString(swryStr.getBytes("UTF-8"));
        if(null != swryStr && !"".equals(swryStr)) {

//            String s = HttpUtils.post(url+"/calendar/meeting/persons/"+swryStr,"");

            base64 = URLEncoder.encode(base64, "UTF-8");
            System.out.println(base64);
//            String req = url + "/calendar/meeting/persons?persons=" + base64;
            String req = url + "/meeting/persons?persons=" + base64;

            System.out.println(req);

            s = HttpUtils.get(req);
        }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return s;
        }


    public static void main(String[] args) throws Exception {
        Meeting m = new Meeting();
        m.setHybh("1");
        m.setHydd("XUHUI1");
        m.setHynr("就开始看看是打开");
        m.setKsrq("2018-05-29");
        m.setHymc("撒低级的卡");
        m.setKssj("12:00");

        Meeting m2 = new Meeting();
        m2.setHybh("1");
        m2.setHydd("XUHUI1");
        m2.setHynr("就开始看看是打开");
        m2.setKsrq("2018-05-29");
        m2.setHymc("撒低级的卡");
        m2.setKssj("12:00");

        List l = new ArrayList();
        l.add(m);
        l.add(m2);
try {
//    String s= JSON.toJSONString(l);
//    System.out.println(s);
//    List<Meeting> meetingList = new ArrayList<>();
//    meetingList = (List)JSON.parseObject(s,Meeting.class);
//    System.out.println(meetingList);

//
    String s = "庞金顺,13101040520,13101040100,1#陈军,13101040552,13101040100,1";
//    s = URLDecoder.decode(s,"UTF-8");
//    s = Base64.getEncoder().encodeToString(s.getBytes("UTF-8"));
//    System.out.println(s);
//    String a = new String(Base64.getDecoder().decode(s),"UTF-8");
//    System.out.println(a);
//    System.out.println(Base64.getDecoder().decode(s).toString());

    BASE64Encoder encoder = new BASE64Encoder();
    String base64 = encoder.encode(s.getBytes("UTF-8"));
    System.out.println(base64);
    BASE64Decoder decoder = new BASE64Decoder();
    String a = new String(decoder.decodeBuffer(base64),"UTF-8");
    System.out.println(a);

    base64 = URLEncoder.encode(base64, "UTF-8");
    System.out.println(base64);
}catch(Exception e){
    e.printStackTrace();
}
//        QySchedulerController controller = new QySchedulerController();s
//        System.out.println(controller.getFirstAndLastOfMonth("2018-02-22"));

    }
}
