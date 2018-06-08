package com.ludateam.wechat.qy.entity;/*
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
 * Created by Him on 2018/4/17.
 */

import java.util.List;
import java.util.Map;

public class SchedulerData {
    private List<Scheduler> data;
    private List<MeetingData> meetings;
    private List<Map<String,String>> swrys;

    public List<Map<String, String>> getSwrys() {
        return swrys;
    }

    public void setSwrys(List<Map<String, String>> swrys) {
        this.swrys = swrys;
    }

    public List<Scheduler> getData() {
        return data;
    }

    public void setData(List<Scheduler> data) {
        this.data = data;
    }

    public List<MeetingData> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<MeetingData> meetings) {
        this.meetings = meetings;
    }
}
