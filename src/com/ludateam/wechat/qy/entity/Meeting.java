package com.ludateam.wechat.qy.entity;
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
 * Created by Him on 2018/4/17.
 */

import java.util.Date;

public class Meeting {
    /**
     * 会议地点
     */
    private String hydd;
    /**
     * 会议名称
     */
    private String hymc;
    /**
     * 会议内容
     */
    private String hynr;

    private String  show_kssj;

    /**
     * 开始时间
     */
    private Date  kssj;
    /**
     * 结束时间
     */
    private Date  jssj;
    /**
     * 参与人
     */
    private String  cyr;

    public String getHydd() {
        return hydd;
    }

    public void setHydd(String hydd) {
        this.hydd = hydd;
    }

    public String getHymc() {
        return hymc;
    }

    public void setHymc(String hymc) {
        this.hymc = hymc;
    }

    public String getHynr() {
        return hynr;
    }

    public void setHynr(String hynr) {
        this.hynr = hynr;
    }

    public Date getKssj() {
        return kssj;
    }

    public void setKssj(Date kssj) {
        this.kssj = kssj;
    }

    public Date getJssj() {
        return jssj;
    }

    public void setJssj(Date jssj) {
        this.jssj = jssj;
    }

    public String getCyr() {
        return cyr;
    }

    public void setCyr(String cyr) {
        this.cyr = cyr;
    }

    public String getShow_kssj() {
        return show_kssj;
    }

    public void setShow_kssj(String show_kssj) {
        this.show_kssj = show_kssj;
    }
}
