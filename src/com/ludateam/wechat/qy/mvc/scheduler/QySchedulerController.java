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
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.OAuthApi;
import com.ludateam.wechat.qy.entity.Meeting;
import com.ludateam.wechat.qy.entity.MeetingData;
import com.ludateam.wechat.qy.entity.Scheduler;
import com.ludateam.wechat.qy.entity.SchedulerData;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;
import com.platform.mvc.base.BaseModel;
import org.apache.xmlbeans.impl.jam.internal.elements.SourcePositionImpl;

import java.io.UnsupportedEncodingException;
import java.net.SocketPermission;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Controller("/wechat/myscheduler")
public class QySchedulerController extends BaseController {
    private static final Log log = Log.getLog(QySchedulerController.class);

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public void index() {
       /* try {
            String redirect_uri = URLEncoder.encode(PropKit.get("domain") + "/wechat/myscheduler/main", "utf-8");
            String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123", true);
            System.out.println("codeUrl>>>" + codeUrl);
            redirect(codeUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


        setAttr("userId", "");
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
    private List<Meeting> getMockData(List<String> weekDays, List<String> monthDays) {
        List<Meeting> returnMeetingList = new ArrayList<>();
        System.out.println(weekDays);
        System.out.println(monthDays);

        for (int i = 0; i < 100; i++) {
            Meeting meeting = new Meeting();
            meeting.setHydd("测试会议地点-" + i);
            meeting.setHymc("测试会议名称-" + i);
            meeting.setHynr("测试会议内容-" + i);
            Calendar cal = Calendar.getInstance();

            int randomIndex = new Random().nextInt(monthDays.size());

            try {
                cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(monthDays.get(randomIndex)));
                cal.add(Calendar.HOUR, new Random().nextInt(13));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            meeting.setKssj(cal.getTime());
            meeting.setJssj(cal.getTime());
            meeting.setShow_kssj(new SimpleDateFormat("HH:mm").format(cal.getTime()));
            meeting.setCyr("参与人 - " + i);

            returnMeetingList.add(meeting);
        }


        return returnMeetingList;
    }

    public void getSchedules() throws ParseException {
        //年
        Integer year = Integer.parseInt(getPara("year"));
        //月
        Integer month = Integer.parseInt(getPara("month"));
        //日
        Integer day = Integer.parseInt(getPara("day"));

        Calendar now = Calendar.getInstance();
        now.set(year, month - 1, day);

        List<String> weekDays = getFirstAndLastOfWeek(format.format(now.getTime()));
        List<String> monthDays = getFirstAndLastOfMonth(format.format(now.getTime()));
        //模拟数据
        List<Meeting> MOCK_DATA = getMockData(weekDays, monthDays);

        List<Scheduler> returnlist = new ArrayList<>();
        MOCK_DATA.stream().collect(Collectors.groupingBy(p -> {
            return format.format(p.getKssj());
        }, Collectors.counting())).forEach((k, v) -> {
            Scheduler scheduler = new Scheduler();
            scheduler.setD(k);
            scheduler.setCount(v);
            returnlist.add(scheduler);
        });


        List<MeetingData> meetingDataList = new ArrayList<>();
        //日历下方会议列表只显示本周
        List<Meeting> thisweekmeetinglist = MOCK_DATA.stream().filter(s -> {
            return weekDays.contains(new SimpleDateFormat("yyyy-MM-dd").format(s.getKssj()));
        }).collect(Collectors.toList());

        thisweekmeetinglist.stream().collect(Collectors.groupingBy(p->{ return new SimpleDateFormat("yyyy-M-d").format(p.getKssj());})).forEach((k, v) -> {
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

    public static void main(String[] args) throws ParseException {
        QySchedulerController controller = new QySchedulerController();
        System.out.println(controller.getFirstAndLastOfMonth("2018-02-22"));
    }
}
